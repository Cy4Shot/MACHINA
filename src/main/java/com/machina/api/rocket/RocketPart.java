package com.machina.api.rocket;

import org.joml.Vector3d;

public record RocketPart(RocketPartType type, Vector3d upAnchor, Vector3d downAnchor) {
}