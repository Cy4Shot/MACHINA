package com.machina.api.client.shader;

import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.ShaderInstance;

public class MachinaShader {

	protected ShaderInstance shaderInstance;
	protected String name;
	protected VertexFormat format;

	public MachinaShader(String name) {
		this.name = name;
	}

	public void bind() {
		if (shaderInstance != null) {
			shaderInstance.apply();
		}
	}

	public void unbind() {
		if (shaderInstance != null) {
			shaderInstance.clear();
		}
	}

	public ShaderInstance instance() {
		return shaderInstance;
	}
	
	public void setShaderInstance(ShaderInstance shaderInstance) {
		this.shaderInstance = shaderInstance;
	}

}