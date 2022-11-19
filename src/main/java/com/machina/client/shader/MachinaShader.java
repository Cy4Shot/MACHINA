package com.machina.client.shader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.machina.Machina;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.shader.ShaderDefault;
import net.minecraft.client.shader.ShaderInstance;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;

public class MachinaShader {

	private static final long START_TIME = System.currentTimeMillis();

	protected ShaderInstance shaderInstance;
	protected ShaderDefault timeUniform;
	protected List<String> uniforms;
	protected Map<String, ShaderDefault> defaults;
	protected String name;
	protected VertexFormat format;

	public MachinaShader(String name, String... uniforms) {
		this.name = name;
		this.uniforms = Arrays.asList(uniforms);
		this.defaults = new HashMap<>();
	}

	public void setUniform(final String name, final Matrix4f value) {
		defaults.get(name).set(value);
	}

	public void setUniform(final String name, final Vector3d value) {
		defaults.get(name).set((float) value.x(), (float) value.y(), (float) value.z());
	}

	public void setUniform(final String name, final float value) {
		defaults.get(name).set(value);
	}

	public void setSampler(final String name, final int buffer) {
		shaderInstance.setSampler(name, () -> buffer);
	}

	public void bind() {
		if (shaderInstance != null) {
			timeUniform.set((System.currentTimeMillis() - START_TIME) / 1000.0f);
			shaderInstance.apply();
		}
	}

	public void unbind() {
		if (shaderInstance != null) {
			shaderInstance.clear();
		}
	}

	public void reloadShader(final IResourceManager manager) {
		if (shaderInstance != null) {
			shaderInstance.close();
			shaderInstance = null;
		}

		try {
			shaderInstance = new ShaderInstance(manager, name);
			handleShaderLoad();
		} catch (final IOException e) {
			Machina.LOGGER.error(e);
		}
	}

	protected void handleShaderLoad() {
		timeUniform = shaderInstance.safeGetUniform("time");
		defaults.clear();
		for (String uniform : uniforms) {
			defaults.put(uniform, shaderInstance.safeGetUniform(uniform));
		}
	}

	public ShaderInstance instance() {
		return shaderInstance;
	}

}
