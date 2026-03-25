#version 330 core

in vec2 texCoord;

uniform vec2 lightPos;   // 0..1 screen-space position of the light
uniform float intensity;  // overall flare brightness
uniform float aspect;     // screen width/height ratio

out vec4 fragColor;

float hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

vec3 cc(vec3 color, float factor, float factor2) {
    float w = color.x + color.y + color.z;
    return mix(color, vec3(w) * factor, w * factor2);
}

// https://www.shadertoy.com/view/4sX3Rs
vec3 lensflare(vec2 uv, vec2 pos) {
    vec2 main = uv - pos;
    vec2 uvd = uv * (length(uv));
    
    float ang = atan(main.x, main.y);
    float dist = length(main); 
    dist = pow(dist, 0.1);
    
    float n = hash(vec2(ang * 16.0, dist * 32.0));
    
    float f0 = 1.0 / (length(uv - pos) * 16.0 + 1.0);
    f0 *= (dist * 1.3 + 0.9);
    
    float f1 = max(0.01 - pow(length(uv + 1.2 * pos), 1.9), 0.0) * 7.0;
    
    float f2 = max(1.0 / (1.0 + 32.0 * pow(length(uvd + 0.8 * pos), 2.0)), 0.0) * 0.25;
    float f22 = max(1.0 / (1.0 + 32.0 * pow(length(uvd + 0.85 * pos), 2.0)), 0.0) * 0.23;
    float f23 = max(1.0 / (1.0 + 32.0 * pow(length(uvd + 0.9 * pos), 2.0)), 0.0) * 0.21;
    
    vec2 uvx = mix(uv, uvd, -0.5);
    
    float f4 = max(0.01 - pow(length(uvx + 0.4 * pos), 2.4), 0.0) * 6.0;
    float f42 = max(0.01 - pow(length(uvx + 0.45 * pos), 2.4), 0.0) * 5.0;
    float f43 = max(0.01 - pow(length(uvx + 0.5 * pos), 2.4), 0.0) * 3.0;
    
    uvx = mix(uv, uvd, -0.4);
    
    float f5 = max(0.01 - pow(length(uvx + 0.2 * pos), 5.5), 0.0) * 2.0;
    float f52 = max(0.01 - pow(length(uvx + 0.4 * pos), 5.5), 0.0) * 2.0;
    float f53 = max(0.01 - pow(length(uvx + 0.6 * pos), 5.5), 0.0) * 2.0;
    
    uvx = mix(uv, uvd, -0.5);
    
    float f6 = max(0.01 - pow(length(uvx - 0.3 * pos), 1.6), 0.0) * 6.0;
    float f62 = max(0.01 - pow(length(uvx - 0.325 * pos), 1.6), 0.0) * 3.0;
    float f63 = max(0.01 - pow(length(uvx - 0.35 * pos), 1.6), 0.0) * 5.0;
    
    vec3 c = vec3(0.0);
    
    c.r += f2 + f4 + f5 + f6; 
    c.g += f22 + f42 + f52 + f62;
    c.b += f23 + f43 + f53 + f63;
    c = c * 1.3 - vec3(length(uvd) * 0.05);
    c += vec3(f0);
    
    return c * intensity;
}

void main() {
    vec2 uv = texCoord * 2.0 - 1.0; // Convert from [0,1] to [-1,1]
    uv.x *= aspect; // Correct aspect ratio
    
    vec2 lightPosAdjusted = lightPos * 2.0 - 1.0;
    lightPosAdjusted.x *= aspect;
    
    vec3 color = lensflare(uv, lightPosAdjusted);
    color = cc(color, 0.5, 0.1);
    
    fragColor = vec4(color, 1.0);
}
