#version 120

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 screen;

void main() {
    gl_Position = ftransform();
    texCoord = gl_MultiTexCoord0.st;
	oneTexel = 1.0 / screen;
}