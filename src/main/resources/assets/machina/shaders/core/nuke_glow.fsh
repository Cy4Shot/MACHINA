#version 330 core

in vec3 fragWorldPos;
in vec4 clipSpacePos;

uniform sampler3D NoiseTex;
uniform vec3 CameraPos;
uniform mat4 InvViewProj;
uniform vec3 ExplosionCenter;
uniform float Time;
uniform float Progress;
uniform float Power;
uniform mat4 ProjMat;
uniform mat4 ModelViewMat;

out vec4 fragColor;

// Shader inspired by https://www.shadertoy.com/user/TekF

// Sample 3D noise texture directly - 32x32x32 volume with trilinear filtering
float grayNoise3D(vec3 x) {
    // Normalize to 0-1 range for texture sampling (tiles every 32 units)
    vec3 uvw = fract(x / 32.0);
    return texture(NoiseTex, uvw).r;
}

vec3 g_cloudCentre;
float g_blastTime;
float g_blastTimeEased;  // Ease-out version for animation
float g_scale;

// Ease-out functions
float easeOutQuad(float t) {
    return 1.0 - (1.0 - t) * (1.0 - t);
}

float easeOutCubic(float t) {
    return 1.0 - pow(1.0 - t, 3.0);
}

float easeOutQuint(float t) {
    return 1.0 - pow(1.0 - t, 5.0);
}

// Cloud shape parameters that evolve over time
float g_cloudWidth;   // Horizontal spread
float g_cloudFlatten; // Vertical compression
float g_wispiness;    // How wispy/noisy

void initBlastParams() {
    g_blastTime = clamp(Progress, 0.001, 0.999);
    g_scale = Power * 10.0;

    // Rise phase - only rises during first 7.5% of animation (doubled duration, same real time)
    float risePhase = smoothstep(0.0, 0.075, g_blastTime);
    float riseHeight = risePhase * 2.3 * g_scale;
    g_cloudCentre = ExplosionCenter + vec3(0.0, riseHeight, 0.0);

    // Spread phase - halved timings for doubled duration
    float spreadPhase = smoothstep(0.05, 0.4, g_blastTime);

    // Cloud gets much wider after it stops rising
    g_cloudWidth = 1.0 + spreadPhase * 4.0;

    // Flatten dramatically
    g_cloudFlatten = 1.0 - spreadPhase * 0.7;

    // Gets wispier over time
    g_wispiness = 0.5 + g_blastTime * 1.5;

    // For other calculations that need eased time
    g_blastTimeEased = easeOutQuint(g_blastTime);
}

vec3 flow(vec3 pos) {
    vec3 p = pos - g_cloudCentre;
    vec3 v;
    float pxzLen = length(p.xz);
    if (pxzLen > 0.001) {
        v.xz = -normalize(p.xz) * p.y;
    } else {
        v.xz = vec2(0.0);
    }
    v.y = pxzLen - 0.8 * g_scale;
    v *= 0.1;

    float g = length(vec2(p.y, pxzLen - 0.8 * g_scale)) - 1.0 * g_scale;
    v *= exp2(-pow(g * 3.0 / g_scale, 2.0));

    return v;
}

// Track if current sample is debris (for coloring)
float g_debrisAmount = 0.0;

// Ground debris ring SDF - expanding ring of dust
float debrisSdf(vec3 pos) {
    vec3 p = pos - ExplosionCenter;

    // Ease-out - expands during first 20% (halved for doubled duration)
    float expandTime = min(g_blastTime / 0.2, 1.0);
    float debrisTime = 1.0 - pow(1.0 - expandTime, 3.0);

    // Ring expands but slows down
    float minRadius = 1.5 * g_scale;
    float maxRadius = 6.0 * g_scale;
    float ringRadius = minRadius + debrisTime * (maxRadius - minRadius);

    // Ring gets wider as it expands (thicker band)
    float ringThickness = (1.0 + debrisTime * 2.0) * g_scale;  // 1x to 3x thickness

    // Density decreases linearly as ring expands
    float densityFalloff = minRadius / ringRadius;

    // Distance from ring center (horizontal)
    float distFromCenter = length(p.xz);

    // How far from the ring centerline (0 = on ring, 1 = at edge)
    float ringPos = clamp(abs(distFromCenter - ringRadius) / ringThickness, 0.0, 1.0);

    // Height tapers - doubled height
    float maxHeight = 0.3 * g_scale;
    float taperHeight = maxHeight * (1.0 - ringPos * ringPos);
    taperHeight = max(taperHeight, 0.04 * g_scale);

    // Horizontal ring distance
    float ringDist = abs(distFromCenter - ringRadius) - ringThickness;

    // Vertical constraint - hugs the ground
    float vertDist = abs(p.y - 0.1 * g_scale) - taperHeight;

    float d = max(ringDist, vertDist);

    // Animated noise - slows down over time
    float noiseSpeed = 0.3 * (1.0 - g_blastTime * 0.8);  // Slows to 20% speed
    vec3 noisePos = pos / (2.0 * g_scale);
    vec3 noiseOffset = vec3(Time * noiseSpeed, 0.0, Time * noiseSpeed);
    float noise = grayNoise3D((noisePos + noiseOffset) * 6.0) * 0.4;
    noise += grayNoise3D((noisePos + noiseOffset * 0.6) * 12.0) * 0.2;

    d += (noise - 0.25) * g_scale * 0.3;

    // Fade out debris - starts after expansion, uses full extended duration
    float fadeOut = smoothstep(0.2, 0.9, g_blastTime);
    d += fadeOut * g_scale * 10.0;

    return d;
}

float sdf(vec3 pos) {
    const float period = 1.6;
    float tt = fract(Time / period);
    float t0 = tt * period;
    float t1 = (tt - 1.0) * period;

    // Position relative to cloud, normalized
    vec3 uvw = (pos - g_cloudCentre) / (3.0 * g_scale);

    float f0 = 0.0;
    float f1 = 0.0;

    vec3 offset0 = flow(pos) * t0 / g_scale;
    vec3 u0 = uvw + offset0 * 0.2;
    offset0 *= 0.2;

    vec3 offset1 = flow(pos) * t1 / g_scale;
    vec3 u1 = uvw + offset1 * 0.2;
    offset1 *= 0.2;

    // FBM with 3D texture - multiply coords to get different scales
    f0 += grayNoise3D((offset0 + u0) * 4.0) / 2.0;
    f0 += grayNoise3D((offset0 + u0) * 8.0) / 4.0;
    f0 += grayNoise3D((offset0 + u0) * 16.0) / 8.0;
    f0 += grayNoise3D((offset0 + u0) * 32.0) / 16.0;
    f0 += grayNoise3D((offset0 + u0) * 64.0) / 32.0;

    f1 += grayNoise3D((offset1 + u1) * 4.0) / 2.0;
    f1 += grayNoise3D((offset1 + u1) * 8.0) / 4.0;
    f1 += grayNoise3D((offset1 + u1) * 16.0) / 8.0;
    f1 += grayNoise3D((offset1 + u1) * 32.0) / 16.0;
    f1 += grayNoise3D((offset1 + u1) * 64.0) / 32.0;

    float ff = mix(f0, f1, tt);
    // Noise influence increases with wispiness
    ff *= 0.5 * g_scale * g_wispiness;

    vec3 p = pos - g_cloudCentre;

    float bulge = 1.0 - exp2(-20.0 * g_blastTimeEased);

    // Cloud shape evolves: disc -> torus over time
    // Starts as filled disc, hole appears gradually (halved timings)
    float holeFormation = smoothstep(0.1, 0.3, g_blastTime);

    float outerRadius = 1.0 * g_scale * bulge * g_cloudWidth;  // Outer edge grows
    float innerRadius = outerRadius * 0.4 * holeFormation;     // Hole appears gradually
    float thickness = 1.0 * g_scale * g_cloudFlatten;          // Vertical thickness

    // Distance calculation - blend between disc and torus
    float pxzLen = length(p.xz);

    // Disc SDF (no hole)
    float discDist = length(vec2(p.y, max(0.0, pxzLen - outerRadius))) - thickness;

    // Torus SDF (with hole)
    float torusDist = length(vec2(p.y, pxzLen - (outerRadius + innerRadius) * 0.5)) - (outerRadius - innerRadius) * 0.5 - thickness * 0.5;

    // Blend from disc to torus
    float g = mix(discDist, torusDist, holeFormation);

    ff *= bulge;

    // Stem - also gets wider over time
    float stemWidth = 0.7 * g_scale * (1.0 + g_blastTime * 0.3);
    float h = length(pos.xz - ExplosionCenter.xz) - stemWidth + 0.15 * (g_cloudCentre.y - pos.y - 1.5 * g_scale);
    h = max(h, pos.y - g_cloudCentre.y);
    h = max(h, (ExplosionCenter.y - 2.0 * g_scale - pos.y) * 0.3);

    g = min(g, h);
    float mainCloud = ff + g * 0.6;

    // Fade out main cloud - starts later, uses full extended duration
    float cloudFadeOut = smoothstep(0.4, 1.0, g_blastTime);
    mainCloud += cloudFadeOut * g_scale * 10.0;

    // Combine with debris ring
    float debris = debrisSdf(pos);

    // Track how much we're in debris vs main cloud (for coloring)
    g_debrisAmount = debris < mainCloud ? 1.0 : 0.0;

    return min(mainCloud, debris);
}

void main() {
    initBlastParams();
    
    vec2 ndc = clipSpacePos.xy / clipSpacePos.w;
    vec4 nearP = InvViewProj * vec4(ndc, -1.0, 1.0);
	vec4 farP  = InvViewProj * vec4(ndc,  1.0, 1.0);
	nearP /= nearP.w;
	farP  /= farP.w;

    vec3 rayOrigin = nearP.xyz + CameraPos;
	vec3 rayDir    = normalize(farP.xyz - nearP.xyz);

    float softness = (0.1 + pow(g_blastTime, 2.0) * 0.5) * g_scale;
    float density = 1.2 / softness;

    float epsilon = 0.001 * g_scale;
    float visibility = 1.0;
    float light0 = 0.0;
    float light1 = 0.0;
    vec3 sunDir = normalize(vec3(1.0, 1.0, 1.0));

    vec3 pos = rayOrigin;
    float totalDist = 0.0;
    float firstHitDist = -1.0;

    float distToCenter = length(ExplosionCenter - rayOrigin);
    float startDist = max(0.0, distToCenter - g_scale * 5.0);
    pos += rayDir * startDist;
    totalDist = startDist;

    float debrisAccum = 0.0;  // Track debris contribution

    for (int i = 0; i < 24; i++) {
        float h = sdf(pos);
        float vis = smoothstep(epsilon, softness, h);

        if (pos.y < ExplosionCenter.y - 0.5 * g_scale) vis = 1.0;

        h = max(h, epsilon);

        if (vis < 1.0) {
            if (firstHitDist < 0.0) {
                firstHitDist = totalDist;
            }

            float newvis = visibility * pow(vis, h * density);
            float contribution = visibility - newvis;

            // Accumulate debris contribution for graying
            debrisAccum += contribution * g_debrisAmount;

            light0 += contribution * smoothstep(-0.5, 1.0, (sdf(pos + sunDir * softness) - h) / softness);

            vec3 lightDelta = g_cloudCentre - pos;
            float lightDist2 = dot(lightDelta, lightDelta) / (g_scale * g_scale);
            // Reduce fire glow for debris (more gray)
            float fireScale = 1.0 - g_debrisAmount * 0.8;
            light1 += contribution * fireScale * pow(smoothstep(-1.0, 1.0, (sdf(pos + normalize(lightDelta) * softness) - h) / softness), 2.0)
                      / (lightDist2 + 1.0);

            visibility = newvis;
        }

        if (visibility <= 0.01) break;
        if (totalDist > distToCenter + g_scale * 12.0) break;

        pos += h * rayDir;
        totalDist += h;
    }

    if (visibility > 0.99) discard;

    // Depth for first hit point
    if (firstHitDist > 0.0) {
        vec3 hitPos = rayOrigin + rayDir * firstHitDist;
        vec3 viewPos = hitPos - CameraPos;
        vec4 clip = ProjMat * ModelViewMat * vec4(viewPos, 1.0);
        gl_FragDepth = (clip.z / clip.w + 1.0) * 0.5;
    } else {
        gl_FragDepth = gl_FragCoord.z;
    }

    // Colors
    vec4 col = vec4(0.1, 0.2, 0.3, 1.0);
    col += light0 * vec4(0.9, 0.8, 0.7, 0.0);
    col *= pow(g_blastTime, 0.5) * 0.5;
    col += light1 * vec4(8.0, 2.0, 0.25, 0.0) / (25.0 * pow(g_blastTime, 2.0));

    col = mix(vec4(0.0), col, 1.0 - visibility);
    col = pow(col, vec4(1.0 / 2.2));

    fragColor = vec4(col.rgb, 1.0 - visibility);
}
