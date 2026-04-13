#version 150

in vec3 worldDir;
out vec4 fragColor;

uniform float GameTime;
uniform float AuroraStrength;
uniform vec2 CameraXZ;
uniform sampler2D Sampler0;

const int AURORA_SAMPLES = 36;
const float INV_AURORA_SAMPLES = 1.0 / float(AURORA_SAMPLES);
const float INV_SAMPLE_DENOM = 1.0 / (float(AURORA_SAMPLES) + 6.0);
const float INV_NOISE_SIZE = 1.0 / 256.0;
const float COARSE_BLEND = 0.35;
const float ALPHA_DENSITY_SCALE = 0.9;
const float SAMPLE_REFERENCE = 100.0;
const float LOW_SAMPLE = clamp((SAMPLE_REFERENCE - float(AURORA_SAMPLES)) / SAMPLE_REFERENCE, 0.0, 1.0);

float hash12(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

vec3 noiseRGB(vec2 p) {
    vec2 uv = fract((p + 0.5) * INV_NOISE_SIZE);
    return texture(Sampler0, uv).rgb;
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
    vec2 animOffset = vec2(anim, -anim * 0.72);
    vec2 detailAnim = vec2(anim * 0.5);
    float streakAnim = anim * 3.0;
    vec2 driftAnimOffset = worldDrift + animOffset;

    float fiBase = (dither + 4.0) * INV_SAMPLE_DENOM;
    float fi = fiBase;

    vec3 acc = vec3(0.0);
    float alphaAcc = 0.0;
    vec3 prevTint = vec3(0.0);
    float prevDensity = 0.0;

    for (int i = 0; i < AURORA_SAMPLES; i++) {
        float depth = fi * fi;

        vec2 plane = uv * (3.0 + 15.0 * depth);
        plane += driftAnimOffset;

        vec3 planeNoise = noiseRGB(plane * 0.2);
        float warp = planeNoise.r;
        vec2 flow = plane + (warp - 0.5) * 3.0;

        vec3 flowNoise = noiseRGB(flow * 0.6 + detailAnim);
        float coarse = mix(planeNoise.g, flowNoise.g, COARSE_BLEND);
        float detail = flowNoise.b;
        float ridgeValue = ridge(detail);
        float ridge2 = ridgeValue * ridgeValue;
        float filament6 = ridge2 * ridge2 * ridge2;
        float filament4 = ridge2 * ridge2;
        float filaments = mix(filament6, filament4, LOW_SAMPLE * 0.8);

        float soften = 0.10 * LOW_SAMPLE;
        float curtain = smoothstep(0.42 - soften, 0.78 + soften, coarse) * filaments;
        float streaks = 0.7 + 0.3 * sin(flow.x + streakAnim);
        curtain *= streaks;

        float density = curtain * (1.0 - depth);

        float colorT = clamp(0.4 * coarse + 0.75 * filaments + 0.25 * depth, 0.0, 1.0);
        vec3 tint = auroraPalette(colorT);
        tint *= mix(0.6, 1.35, filaments);

        float densityAccum = mix(density, 0.5 * (density + prevDensity), LOW_SAMPLE);
        vec3 tintAccum = mix(tint, 0.5 * (tint + prevTint), LOW_SAMPLE);

        acc += tintAccum * densityAccum;
        alphaAcc += densityAccum * ALPHA_DENSITY_SCALE;

        prevTint = tint;
        prevDensity = density;
        fi += INV_SAMPLE_DENOM;
    }

    acc *= INV_AURORA_SAMPLES;
    alphaAcc *= INV_AURORA_SAMPLES;

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

    float temporalPhase = GameTime * 0.75487766 * LOW_SAMPLE;
    float dither = fract(hash12(gl_FragCoord.xy) + temporalPhase);

    float visibility = max(AuroraStrength, 0.0);

    if (visibility <= 0.001) {
        discard;
    }

    vec4 aurora = renderAurora(dir, GameTime, dither);
    aurora.rgb *= visibility;
    aurora.a = clamp(aurora.a * visibility, 0.0, 1.0);

    fragColor = vec4(aurora.rgb, aurora.a);
}
