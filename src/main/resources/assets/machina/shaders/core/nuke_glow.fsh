#version 330 core

in vec3 fragWorldPos;
in vec4 clipSpacePos;

uniform sampler3D NoiseTex;
uniform vec3 CameraPos;
uniform vec3 ExplosionCenter;
uniform float Time;
uniform float Progress;
uniform float Power;
uniform mat4 ProjMat;
uniform mat4 ModelViewMat;

out vec4 fragColor;

// Sample 3D noise texture directly - 32x32x32 volume with trilinear filtering
float grayNoise3D(vec3 x) {
    // Normalize to 0-1 range for texture sampling (tiles every 32 units)
    vec3 uvw = fract(x / 32.0);
    return texture(NoiseTex, uvw).r;
}

vec3 g_cloudCentre;
float g_blastTime;
float g_scale;

void initBlastParams() {
    g_blastTime = clamp(Progress, 0.001, 0.999);
    g_scale = Power * 10.0;
    g_cloudCentre = ExplosionCenter + vec3(0.0, g_blastTime * 8.0 * g_scale, 0.0);
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
    ff *= 0.5 * g_scale;

    vec3 p = pos - g_cloudCentre;
    float bulge = 1.0 - exp2(-20.0 * g_blastTime);
    float g = length(vec2(p.y, length(p.xz) - 1.0 * g_scale * bulge)) - 1.0 * g_scale;
    ff *= bulge;

    // Stem
    float h = length(pos.xz - ExplosionCenter.xz) - 0.7 * g_scale + 0.15 * (g_cloudCentre.y - pos.y - 1.5 * g_scale);
    h = max(h, pos.y - g_cloudCentre.y);
    h = max(h, (ExplosionCenter.y - 2.0 * g_scale - pos.y) * 0.3);

    g = min(g, h);
    ff += g * 0.6;

    return ff;
}

void main() {
    initBlastParams();

    vec3 rayOrigin = CameraPos;
    vec3 rayDir = normalize(fragWorldPos - CameraPos);

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

            light0 += contribution * smoothstep(-0.5, 1.0, (sdf(pos + sunDir * softness) - h) / softness);

            vec3 lightDelta = g_cloudCentre - pos;
            float lightDist2 = dot(lightDelta, lightDelta) / (g_scale * g_scale);
            light1 += contribution * pow(smoothstep(-1.0, 1.0, (sdf(pos + normalize(lightDelta) * softness) - h) / softness), 2.0)
                      / (lightDist2 + 1.0);

            visibility = newvis;
        }

        if (visibility <= 0.01) break;
        if (totalDist > distToCenter + g_scale * 6.0) break;

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
