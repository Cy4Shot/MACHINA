package com.machina.client.shader;

import com.machina.Machina;
import com.machina.util.reflection.ClassHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;

public class ShaderHandler {

	//@formatter:off
	public static final MachinaShader SCANNER = create("scanner", "invViewMat", "invProjMat", "pos", "center", "radius", "target");
	public static final MachinaShader RAINDROP = create("raindrop", "invViewMat", "invProjMat", "pos", "screen", "col");
	public static final MachinaShader FOG = create("fog", "invViewMat", "invProjMat", "col", "center", "density", "render");
	//@formatter:on

	public static void setup() {
		final IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
		Minecraft.getInstance().submitAsync(() -> reloadShaders(resourceManager));
		if (resourceManager instanceof IReloadableResourceManager) {
			((IReloadableResourceManager) resourceManager)
					.registerReloadListener((ISelectiveResourceReloadListener) (manager, predicate) -> {
						if (predicate.test(VanillaResourceType.SHADERS)) {
							reloadShaders(manager);
						}
					});
		}
		Machina.LOGGER.info("Shaders set up.");
	}

	private static void reloadShaders(IResourceManager manager) {
		ClassHelper.<MachinaShader>doWithStatics(ShaderHandler.class, (name, data) -> {
			data.reloadShader(manager);
		});
	}

	private static MachinaShader create(String name, String... uniforms) {
		return new MachinaShader(Machina.MOD_ID + ":" + name, uniforms);
	}
}
