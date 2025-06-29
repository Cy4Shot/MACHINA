#version 120

uniform mat4 invViewMat;
uniform mat4 invProjMat;
uniform vec3 center;
uniform vec3 col;
uniform sampler2D depthTex;
uniform float render;
uniform float daytime;

varying vec2 texCoord;

const float R0 = 6360e3; // Surface radius
const float Ra = 6380e3; // Atmosphere top radius
const vec3 bR = vec3(58e-7, 135e-7, 331e-7); // Rayleigh scattering coefficient RANDOMISE THESE AT SOME POINT???
const vec3 bMs = vec3(2e-5); // Mie scattering coefficient
const vec3 bMe = bMs * 1.1;
const float I = 7.; // Sun intensity
const vec3 C = vec3(0., -R0, 0.); // Planet center point

vec2 densitiesRM(vec3 p) {
	float h = max(0., length(p - C) - R0);
	return vec2(exp(-h/8e3), exp(-h/12e2));
}

float escape(vec3 p, vec3 d, float R) {
	vec3 v = p - C;
	float b = dot(v, d);
	float det = b * b - dot(v, v) + R*R;
	if (det < 0.) return -1.;
	det = sqrt(det);
	float t1 = -b - det, t2 = -b + det;
	return (t1 >= 0.) ? t1 : t2;
}

vec2 scatterDepthInt(vec3 o, vec3 d, float L, float steps) {
	vec2 depthRMs = vec2(0.);
	L /= steps;
	d *= L;
	for (float i = 0.; i < steps; ++i)
		depthRMs += densitiesRM(o + d * i);
	return depthRMs * L;
}

vec2 totalDepthRM;
vec3 I_R, I_M;
vec3 sundir;

void scatterIn(vec3 o, vec3 d, float L, float steps) {
	L /= steps;
	d *= L;
	for (float i = 0.; i < steps; ++i) {
		vec3 p = o + d * i;
		vec2 dRM = densitiesRM(p) * L;
		totalDepthRM += dRM;
		vec2 depthRMsum = totalDepthRM + scatterDepthInt(p, sundir, escape(p, sundir, Ra), 4.);
		vec3 A = exp(-bR * depthRMsum.x - bMe * depthRMsum.y);
		I_R += A * dRM.x;
		I_M += A * dRM.y;
	}
}

vec3 scatter(vec3 o, vec3 d, float L, vec3 Lo) {
	totalDepthRM = vec2(0.);
	I_R = I_M = vec3(0.);
	scatterIn(o, d, L, 16.);
	float mu = dot(d, sundir);
	
	return Lo * exp(-bR * totalDepthRM.x - bMe * totalDepthRM.y) + I * (1. + mu * mu) * (I_R * bR * .0597 + I_M * bMs * .0196 / pow(1.58 - 1.52 * mu, 1.5));
}

vec3 worldpos(float depth) {
    float z = depth * 2.0 - 1.0;
    vec4 clipSpacePosition = vec4(texCoord * 2.0 - 1.0, z, 1.0);
    vec4 viewSpacePosition = invProjMat * clipSpacePosition;
    viewSpacePosition /= viewSpacePosition.w;
    vec4 worldSpacePosition = invViewMat * viewSpacePosition;
    return center + worldSpacePosition.xyz;
}

vec3 getPointOnSphere(vec3 center, float radius, float theta, float phi) {
  vec3 point;
  point.y = center.x + radius * sin(theta) * cos(phi);
  point.x = center.y + radius * sin(theta) * sin(phi);
  point.z = center.z + radius * cos(theta);
  return point;
}

void main() {
	float depth = texture2D(depthTex, texCoord).r;
    vec3 pos = worldpos(depth);
    float dist = distance(pos, center);
    vec3 c = vec3(0.);
 	if (dist > render * 2) {
        vec3 O = center;
	    vec3 D = normalize(pos - center);
		sundir = normalize(getPointOnSphere(vec3(0.), 1., daytime, 0.));
	    float L = escape(O, D, Ra);
	    c = scatter(O, D, L, col);
    }

    gl_FragColor = vec4(sqrt(c) * clamp((sundir.y + 0.3) * 5, 0, 1), 0);
}