#version 120

// From Scannable mod

uniform mat4 invViewMat;
uniform mat4 invProjMat;
uniform vec3 pos;
uniform vec3 center;
uniform float radius;
uniform float target;
uniform sampler2D depthTex;

varying vec2 texCoord;

const float width = 10;
const float sharpness = 10;
const vec4 outerColor = vec4(0.8, 1.0, 0.9, 1.0);
const vec4 midColor = vec4(0.3, 0.63, 0.7, 1.0);
const vec4 innerColor = vec4(0.0, 1.0, 1.0, 1.0);
const vec4 scanlineColor = vec4(0.6, 1.0, 0.2, 1.0);

float scanlines() {
    return sin(gl_FragCoord.y) * 0.5 + 0.5;
}

vec3 worldpos(float depth) {
    float z = depth * 2.0 - 1.0;
    vec4 clipSpacePosition = vec4(texCoord * 2.0 - 1.0, z, 1.0);
    vec4 viewSpacePosition = invProjMat * clipSpacePosition;
    viewSpacePosition /= viewSpacePosition.w;
    vec4 worldSpacePosition = invViewMat * viewSpacePosition;

    return pos + worldSpacePosition.xyz;
}

void main() {
    vec4 color = vec4(0, 0, 0, 0);

    float depth = texture2D(depthTex, texCoord).r;
    vec3 pos = worldpos(depth);
    float dist = distance(pos, center);

    if (dist < radius && dist > radius - width && depth < 1) {
        float diff = 1.0 - (radius - dist) / width;
        vec4 edge = mix(midColor, outerColor, pow(diff, sharpness));
        color = mix(innerColor, edge, diff) + scanlines() * scanlineColor;
        color *= diff * (1.0 - (radius / target));
    }

    gl_FragColor = color;
}