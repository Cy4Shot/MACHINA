#version 330 core

layout(location = 0) in vec2 Position;

out vec2 texCoord;

void main() {
    gl_Position = vec4(Position * 2.0 - 1.0, 0.0, 1.0);
    texCoord = Position;
    texCoord.y = 1.0 - texCoord.y;
}