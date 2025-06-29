#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float Time;  // Add this uniform to pass in elapsed time

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    if (color.a < 0.1) {
        discard;
    }

    // Apply base color modifications
    color *= vertexColor * ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;

    // === Moving Scanline Effect ===
    float speed = 2.0;  // Scanline movement speed
    float frequency = 800.0;  // Line spacing
    float scanline = sin((texCoord0.y + Time * speed) * frequency) * 0.05;
    color.rgb -= scanline;

    // Final fog calculation
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
