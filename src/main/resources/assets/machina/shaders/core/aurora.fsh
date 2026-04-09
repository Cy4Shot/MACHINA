#version 150

in vec3 worldDir;
out vec4 fragColor;

uniform float GameTime;
uniform float AuroraStrength;
uniform vec2 CameraXZ;

const int AURORA_SAMPLES = 120;
const float PI = 3.14159265;

float hash12(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

float valueNoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    vec2 u = f * f * (3.0 - 2.0 * f);

    float a = hash12(i + vec2(0.0, 0.0));
    float b = hash12(i + vec2(1.0, 0.0));
    float c = hash12(i + vec2(0.0, 1.0));
    float d = hash12(i + vec2(1.0, 1.0));

    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

float fbm(vec2 p) {
    float v = valueNoise(p);
    p *= 2.0;
    v += valueNoise(p) * 0.5;
    return v * 0.66;
}

float ridge(float x) {
    return 1.0 - abs(2.0 * x - 1.0);
}

vec3 auroraPalette(float x) {
    vec3 c0 = vec3(0.06, 0.92, 0.50);
    vec3 c1 = vec3(0.15, 0.48, 1.00);
    vec3 c2 = vec3(0.72, 0.22, 1.00);

    vec3 col = mix(c0, c1, smoothstep(0.2, 0.8, x));
    col = mix(col, c2, smoothstep(0.65, 1.0, x));
    return col;
}

float pow6(float x) {
    float x2 = x * x;
    return x2 * x2 * x2;
}

vec4 renderAurora(vec3 dir, float t, float dither) {
    vec3 sky = dir / max(dir.y, 0.03);
    vec2 uv = sky.xz;

    vec2 worldDrift = CameraXZ * 0.0022;
    float anim = t * 100.0;

    vec3 acc = vec3(0.0);
    float alphaAcc = 0.0;

    for (int i = 0; i < AURORA_SAMPLES; i++) {
        float fi = (float(i) + dither + 4.0) / (float(AURORA_SAMPLES) + 6.0);
        float depth = fi * fi;

        vec2 plane = uv * mix(3.0, 18.0, depth);
        plane += worldDrift + vec2(anim, -anim * 0.72);

        float warp = fbm(plane * 0.2);
        vec2 flow = plane + (warp - 0.5) * 3.0;

        float coarse = fbm(flow * 0.22);
        float detail = fbm(flow * 0.6 + anim * 0.5);
        float filaments = ridge(detail);
        filaments = pow6(max(filaments, 0.0));

        float curtain = smoothstep(0.42, 0.78, coarse) * filaments;
        float streaks = 0.7 + 0.3 * sin(flow.x + anim * 3.0);
        curtain *= streaks;

        float weight = (1.0 - depth);
        float density = curtain * weight;

        float colorT = clamp(0.4 * coarse + 0.75 * filaments + 0.25 * depth, 0.0, 1.0);
        vec3 tint = auroraPalette(colorT);
        tint *= mix(0.6, 1.35, filaments);

        acc += tint * density;
        alphaAcc += density * 0.9;
    }

    acc /= float(AURORA_SAMPLES);
    alphaAcc /= float(AURORA_SAMPLES);

    float horizonFade = smoothstep(0.0, 0.12, dir.y);
    acc *= horizonFade;
    alphaAcc *= horizonFade;

    return vec4(acc, alphaAcc);
}

void main() {
    vec3 dir = normalize(worldDir);
    if (dir.y <= 0.0) {
        discard;
    }

    float dither = hash12(gl_FragCoord.xy);

    float visibility = max(AuroraStrength, 0.0);

    if (visibility <= 0.001) {
        discard;
    }

    vec4 aurora = renderAurora(dir, GameTime, dither);
    aurora.rgb *= visibility;
    aurora.a = clamp(aurora.a * visibility, 0.0, 1.0);

    fragColor = vec4(aurora.rgb, aurora.a);
}