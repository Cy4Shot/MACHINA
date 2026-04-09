#version 150

in vec3 Position;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec3 worldDir;

void main() {
    vec4 pos = ModelViewMat * vec4(Position, 1.0);
    worldDir = normalize(Position);

    gl_Position = ProjMat * pos;
}
