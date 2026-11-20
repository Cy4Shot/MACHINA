#version 330 core

in vec3 Position;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 CameraPos;

out vec3 fragWorldPos;
out vec4 clipSpacePos;

void main() {
    clipSpacePos = ProjMat * ModelViewMat * vec4(Position, 1.0);
    gl_Position = clipSpacePos;
    fragWorldPos = Position + CameraPos;
}
