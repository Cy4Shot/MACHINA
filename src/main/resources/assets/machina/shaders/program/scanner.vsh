#version 120

varying vec2 texCoord;

// From Scannable mod.
void main() {
    texCoord = gl_MultiTexCoord0.st;
    gl_Position = ftransform();
}