#version 120

varying vec2 texCoord;

void main() {
    gl_Position = ftransform();
    texCoord = gl_Position.xy * 0.5 + 0.5;
    texCoord.y = 1.0 - texCoord.y;
}