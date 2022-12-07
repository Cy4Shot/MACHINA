#version 120

// This code is extremely cool. :D

uniform mat4 invViewMat;
uniform mat4 invProjMat;
uniform vec3 pos;
uniform vec2 screen;
uniform float time;
uniform sampler2D depthTex;
uniform vec4 col;

varying vec2 texCoord;
varying vec2 oneTexel;

#define PI 3.1415936535
#define NUMCONTROLS 27
#define THRESH 0.5
#define SNAPRANGE 100.0

const float radius = 15.;
const float opacity = .04;
const int num_ripples = 2;

vec3 worldpos(float depth) {
    float z = depth * 2.0 - 1.0;
    vec4 clipSpacePosition = vec4(texCoord * 2.0 - 1.0, z, 1.0);
    vec4 viewSpacePosition = invProjMat * clipSpacePosition;
    viewSpacePosition /= viewSpacePosition.w;
    vec4 worldSpacePosition = invViewMat * viewSpacePosition;

    return pos + worldSpacePosition.xyz;
}

float rand(vec2 co) {
    return fract(sin(mod(dot(co.xy, vec2(12.9898, 78.233)), 3.14)) * 43758.5453);
}

float Square(float x) {
	return x * x;
}

int inControl(vec2 screenCoord, float screenWidth) {
    if (screenCoord.y < 1.0) {
        float index = floor(screenWidth / 2.0) + THRESH / 2.0;
        index = (screenCoord.x - index) / 2.0;
        if (fract(index) < THRESH && index < NUMCONTROLS && index >= 0) {
            return int(index);
        }
    }
    return -1;
}

vec4 getNotControl(sampler2D inSampler, vec2 coords, bool inctrl) {
    if (inctrl) {
        return (texture2D(inSampler, coords - vec2(oneTexel.x, 0.0)) + texture2D(inSampler, coords + vec2(oneTexel.x, 0.0)) + texture2D(inSampler, coords + vec2(0.0, oneTexel.y))) / 3.0;
    } else {
        return texture2D(inSampler, coords);
    }
}

vec4 backProject(vec4 vec) {
    vec4 tmp = invProjMat * vec;
    return tmp / tmp.w;
}

// Get the normal vector. This should not be this complex, mojank.
vec3 getNormal() {
		bool inctrl = inControl(texCoord * screen, screen.x) > -1;
		vec2 normCoord = texCoord;
        float depth = getNotControl(depthTex, normCoord, inctrl).r;
        float depth2 = getNotControl(depthTex, normCoord + vec2(0.0, oneTexel.y), inControl((normCoord + vec2(0.0, oneTexel.y)) * screen, screen.x) > -1).r;
        float depth3 = getNotControl(depthTex, normCoord + vec2(oneTexel.x, 0.0), inControl((normCoord + vec2(oneTexel.x, 0.0)) * screen, screen.x) > -1).r;
        float depth4 = getNotControl(depthTex, normCoord - vec2(0.0, oneTexel.y), inControl((normCoord - vec2(0.0, oneTexel.y)) * screen, screen.x) > -1).r;
        float depth5 = getNotControl(depthTex, normCoord - vec2(oneTexel.x, 0.0), inControl((normCoord - vec2(oneTexel.x, 0.0)) * screen, screen.x) > -1).r;
        vec2 scaledCoord = 2.0 * (normCoord - vec2(0.5));
        vec3 fragpos = backProject(vec4(scaledCoord, depth, 1.0)).xyz;
        vec3 p2 = backProject(vec4(scaledCoord + 2.0 * vec2(0.0, oneTexel.y), depth2, 1.0)).xyz;
        p2 = p2 - fragpos;
        vec3 p3 = backProject(vec4(scaledCoord + 2.0 * vec2(oneTexel.x, 0.0), depth3, 1.0)).xyz;
        p3 = p3 - fragpos;
        vec3 p4 = backProject(vec4(scaledCoord - 2.0 * vec2(0.0, oneTexel.y), depth4, 1.0)).xyz;
        p4 = p4 - fragpos;
        vec3 p5 = backProject(vec4(scaledCoord - 2.0 * vec2(oneTexel.x, 0.0), depth5, 1.0)).xyz;
        p5 = p5 - fragpos;
        vec3 normal = normalize(cross(p2, p3)) + normalize(cross(-p4, p3)) + normalize(cross(p2, -p5)) + normalize(cross(-p4, -p5));
        normal = normal == vec3(0.0) ? vec3(0.0, 1.0, 0.0) : normalize(-normal);
        normal = normal.x >  (1.0 - 0.05 * clamp(length(fragpos) / SNAPRANGE, 0.0, 1.0)) ? vec3(1.0, 0.0, 0.0) : normal;
        normal = normal.x < -(1.0 - 0.05 * clamp(length(fragpos) / SNAPRANGE, 0.0, 1.0)) ? vec3(-1.0, 0.0, 0.0) : normal;
        normal = normal.y >  (1.0 - 0.05 * clamp(length(fragpos) / SNAPRANGE, 0.0, 1.0)) ? vec3(0.0, 1.0, 0.0) : normal;
        normal = normal.y < -(1.0 - 0.05 * clamp(length(fragpos) / SNAPRANGE, 0.0, 1.0)) ? vec3(0.0, -1.0, 0.0) : normal;
        normal = normal.z >  (1.0 - 0.05 * clamp(length(fragpos) / SNAPRANGE, 0.0, 1.0)) ? vec3(0.0, 0.0, 1.0) : normal;
        normal = normal.z < -(1.0 - 0.05 * clamp(length(fragpos) / SNAPRANGE, 0.0, 1.0)) ? vec3(0.0, 0.0, -1.0) : normal;
        return normal;
}

void main() {
	float depth = texture2D(depthTex, texCoord).r;
    vec3 wpos = floor(worldpos(depth) * 16) / 16;
	vec2 displacement = vec2(0.0, 0.0);
	float loop_time = 1.6;
	float ripple_width_inverse = 20. + rand(vec2(texCoord) * time) * 20.;
	float limits = PI / ripple_width_inverse;
	float strength_control =(float(num_ripples) - 5.0);
    
	vec2 screen_window_ripple = (wpos.xz  * 2 - 1);
	
	for (float i = 0.0; i < float(num_ripples); ++i) {
		float intensity = 0.2 + 0.3 * rand(vec2(i, 7.714673));
		float scale = 0.5 + 0.3 * rand(vec2(i, 49.4949));
	    vec2 origin = vec2(rand(vec2(919.,154.)), rand(vec2(490.,690.))) * i;
	    float min_mirror_size = 2.0 * loop_time;
	    float mirror_size = (1.0 + rand(vec2(i * 0.2, i * 0.17))) * min_mirror_size;
	    float mirror_correction = mirror_size / 2.0;
	    vec2 corr = origin - mirror_correction;
	    vec2 mirrored_window = mod(screen_window_ripple / scale - corr, mirror_size) + corr;
	    vec2 cell_num = floor((screen_window_ripple / scale - corr) / mirror_size);
	    vec2 dist = mirrored_window - origin;
	    float time_offset = i * 0.12 + 23.0 * rand(cell_num);
	    float t = length(dist) - mod(time_offset + time, loop_time);
	    float t_one_period = clamp(t, -limits, limits);
	    vec2 amplitude = dist / (Square(dot(dist, dist)) + 1e-3);
	    amplitude *= max(0.0, min(0.2 * (strength_control * intensity - i + 6.0), 1.0));
	    displacement += amplitude * sin(ripple_width_inverse * t_one_period);
	}
	vec3 up = vec3(0., 1., 0.);
	vec3 normal = normalize((invViewMat * vec4(getNormal(), 0.0)).xyz);
	vec2 ripple = displacement * dot(vec3(displacement, 1.0), up);
	vec3 colgen = vec3(clamp(ripple.x, 0., 1.), clamp(ripple.y, 0., 1.), 1.0) * clamp(1 - up.y + normal.y, 0., 1.);
    float NdotL = max(dot(colgen, vec3(0, 1, 0)), 0.0);
    vec4 diffuse = vec4(0);
    if(NdotL > 0) {
        diffuse = clamp(NdotL * vec4(1), 0, 1);
    }
	
	gl_FragColor = diffuse * col * (radius - distance(wpos, pos)) * opacity;
}