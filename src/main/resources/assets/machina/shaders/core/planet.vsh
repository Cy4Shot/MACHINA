#version 330 core

layout(location = 0) in vec3 Position;
layout(location = 1) in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 surfaceUV;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    // Reconstruct spherical UVs from normalised position
    vec3 n = normalize(Position);
    surfaceUV = UV0;
}
