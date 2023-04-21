#version 120

uniform mat4 invViewMat;
uniform mat4 invProjMat;
uniform vec3 col;
uniform vec3 center;
uniform float density;
uniform float render;
uniform sampler2D depthTex;

varying vec2 texCoord;

vec3 worldpos(float depth) {
    float z = depth * 2.0 - 1.0;
    vec4 clipSpacePosition = vec4(texCoord * 2.0 - 1.0, z, 1.0);
    vec4 viewSpacePosition = invProjMat * clipSpacePosition;
    viewSpacePosition /= viewSpacePosition.w;
    vec4 worldSpacePosition = invViewMat * viewSpacePosition;

    return center + worldSpacePosition.xyz;
}

void main() {
    float depth = texture2D(depthTex, texCoord).r;
    vec3 pos = worldpos(depth);
    float dist = distance(pos, center);
    float r = render / 2;
    gl_FragColor = vec4(col * clamp((dist - r) / r, 0, 1), 0);
}