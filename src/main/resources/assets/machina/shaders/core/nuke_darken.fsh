#version 330 core

uniform float Darkness;

out vec4 fragColor;

void main() {
    fragColor = vec4(0.0, 0.0, 0.0, Darkness);
}
