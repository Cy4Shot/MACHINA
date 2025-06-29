#version 150

#moj_import <minecraft:fog.glsl>

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float GameTime;
uniform float RevealAmount;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

float valueNoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);

    // Four corners
    float a = fract(sin(dot(i, vec2(127.1, 311.7))) * 43758.5453);
    float b = fract(sin(dot(i + vec2(1.0, 0.0), vec2(127.1, 311.7))) * 43758.5453);
    float c = fract(sin(dot(i + vec2(0.0, 1.0), vec2(127.1, 311.7))) * 43758.5453);
    float d = fract(sin(dot(i + vec2(1.0, 1.0), vec2(127.1, 311.7))) * 43758.5453);

    // Interpolation
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

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
    float speed = 20.0;  // Scanline movement speed
    float frequency = 800.0;  // Line spacing
    float scanline = sin((texCoord0.y + GameTime * speed) * frequency) * 0.05;
    color.rgb -= scanline;
    
    // === Remap black to transparency ===
	float brightness = (color.r + color.g + color.b) / 3.0;
	color.a *= sqrt(brightness);
	
	// Generate a pseudo-random value based on screen-space UVs
	float noiseScale = 60.0; // Adjust for size of burn patches
	float burnNoise = valueNoise(texCoord0 * noiseScale);
	
	// Gradually reveal based on RevealAmount
	if (burnNoise > RevealAmount) {
	    discard;
	}
	
	float glow = smoothstep(RevealAmount - 0.05, RevealAmount, burnNoise); // edge thickness
	vec3 burnGlowColor = vec3(0.0, 0.9, 1.0); // orange glow
	color.rgb += burnGlowColor * glow * 0.5;

    // Final fog calculation
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
