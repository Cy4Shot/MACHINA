#version 150

in vec2 surfaceUV;

uniform float GameTime;
uniform vec3  LightDir;
uniform float NoiseSeed;
uniform int   PlanetType;
uniform vec4  ColorModulator;
uniform vec3  ViewDir;

out vec4 fragColor;

const float PI = 3.14159265;

// --- 3D noise ---
float hash3(vec3 p) {
    p = fract(p * vec3(127.1, 311.7, 74.7));
    p += dot(p, p.yzx + 19.19);
    return fract((p.x + p.y) * p.z);
}

float noise3(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    vec3 u = f * f * (3.0 - 2.0 * f);
    return mix(
        mix(mix(hash3(i),                    hash3(i+vec3(1,0,0)), u.x),
            mix(hash3(i+vec3(0,1,0)),         hash3(i+vec3(1,1,0)), u.x), u.y),
        mix(mix(hash3(i+vec3(0,0,1)),         hash3(i+vec3(1,0,1)), u.x),
            mix(hash3(i+vec3(0,1,1)),         hash3(i+vec3(1,1,1)), u.x), u.y),
        u.z);
}

float fbm6_3(vec3 p) {
    float v = 0.0, a = 0.5;
    for (int i = 0; i < 6; i++) { v += a * noise3(p); p *= 2.0; a *= 0.5; }
    return v;
}

float fbm4_3(vec3 p) {
    float v = 0.0, a = 0.5;
    for (int i = 0; i < 4; i++) { v += a * noise3(p); p *= 2.0; a *= 0.5; }
    return v;
}

// --- colour ramps (unchanged) ---
vec3 terranColor(float h) {
    vec3 c = vec3(0.05, 0.15, 0.40);
    c = mix(c, vec3(0.10, 0.35, 0.65), smoothstep(0.35, 0.42, h));
    c = mix(c, vec3(0.76, 0.70, 0.50), smoothstep(0.48, 0.52, h));
    c = mix(c, vec3(0.20, 0.55, 0.20), smoothstep(0.52, 0.56, h));
    c = mix(c, vec3(0.40, 0.35, 0.30), smoothstep(0.68, 0.74, h));
    c = mix(c, vec3(0.93, 0.95, 0.97), smoothstep(0.82, 0.88, h));
    return c;
}

vec3 gasColor(float h) {
    vec3 c = vec3(0.70, 0.45, 0.20);
    c = mix(c, vec3(0.85, 0.65, 0.40), smoothstep(0.3, 0.5, h));
    c = mix(c, vec3(0.55, 0.30, 0.15), smoothstep(0.6, 0.8, h));
    return c;
}

vec3 iceColor(float h) {
    vec3 c = vec3(0.55, 0.72, 0.90);
    c = mix(c, vec3(0.88, 0.92, 0.97), smoothstep(0.45, 0.55, h));
    c = mix(c, vec3(0.70, 0.80, 0.95), smoothstep(0.70, 0.80, h));
    return c;
}

vec3 lavaColor(float h) {
    vec3 c = vec3(0.08, 0.04, 0.04);
    c = mix(c, vec3(0.60, 0.15, 0.05), smoothstep(0.30, 0.45, h));
    c = mix(c, vec3(1.00, 0.55, 0.05), smoothstep(0.55, 0.65, h));
    c = mix(c, vec3(1.00, 0.90, 0.40), smoothstep(0.75, 0.85, h));
    return c;
}

void main() {
    float phi_n   = surfaceUV.x * 2.0 * PI;
    float theta_n = surfaceUV.y * PI;
    vec3 normal = normalize(vec3(
        sin(theta_n) * cos(phi_n),
        cos(theta_n),
        sin(theta_n) * sin(phi_n)
    ));

    vec3 L = normalize(LightDir);
    float diff = max(dot(normal, L), 0.0);
    float terminator = smoothstep(0.0, 0.3, diff);

    float spinSpeed = (PlanetType == 1) ? 0.008 : 0.003;
    vec3 seedOffset = vec3(NoiseSeed * 17.53, NoiseSeed * 31.41, NoiseSeed * 43.17);

    float phi       = surfaceUV.x * 2.0 * PI;
    float theta     = surfaceUV.y * PI;
    float spinAngle = phi + GameTime * spinSpeed;

    // True 3D sphere point — no symmetry possible
    vec3 samplePos = vec3(
        sin(theta) * cos(spinAngle),
        cos(theta),
        sin(theta) * sin(spinAngle)
    ) * 2.5 + seedOffset;

    vec3 cloudPos = vec3(
        sin(theta) * cos(phi + GameTime * spinSpeed * 1.3),
        cos(theta),
        sin(theta) * sin(phi + GameTime * spinSpeed * 1.3)
    ) * 3.0 + seedOffset + vec3(1.7, 9.2, 5.4);

    float terrain;
    if (PlanetType == 1) {
        // Gas giant: bands driven by latitude, noise breaks symmetry
        vec3 bandPos = vec3(
            sin(theta * 3.0) * cos(spinAngle),
            cos(theta * 3.0),
            sin(theta * 3.0) * sin(spinAngle)
        ) * 2.0 + seedOffset;
        terrain = fbm4_3(bandPos);
    } else {
        terrain = fbm6_3(samplePos);
    }
    terrain = floor(terrain * 8.0) / 8.0;

    vec3 baseColor;
    if      (PlanetType == 0) baseColor = terranColor(terrain);
    else if (PlanetType == 1) baseColor = gasColor(terrain);
    else if (PlanetType == 2) baseColor = iceColor(terrain);
    else                      baseColor = lavaColor(terrain);

    if (PlanetType == 0 || PlanetType == 2) {
        float cloud = fbm4_3(cloudPos);
        cloud = floor(smoothstep(0.4, 0.65, cloud) * 3.0) / 3.0;
        baseColor = mix(baseColor, vec3(0.90, 0.92, 0.95), cloud * terminator);
    }

    if (PlanetType == 3) {
        baseColor += vec3(0.8, 0.2, 0.0) * smoothstep(0.6, 1.0, 1.0 - diff) * 0.4;
    }

    vec3 lit = baseColor * (terminator * 0.85 + 0.15);
    vec3 V = normalize(-ViewDir);
    float rim = pow(1.0 - abs(dot(normal, V)), 3.5);
    lit += vec3(0.3, 0.5, 1.0) * rim * 0.5;

    fragColor = vec4(lit, 1.0) * ColorModulator;
}
