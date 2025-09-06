package com.machina.api.client.shader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.machina.api.util.MachinaRL;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;

public class ShaderHandler {

    public static final List<MachinaShader> SHADERS = new ArrayList<>();

    //@formatter:off
	public static final MachinaShader ROCKET_PART_BENCH = create("rocket_part_bench");
	//@formatter:on

    private static MachinaShader create(String name) {
        MachinaShader shader = new MachinaShader(name);
        SHADERS.add(shader);
        return shader;
    }

    public static void register(BiConsumer<ShaderInstance, Consumer<ShaderInstance>> cons, ResourceProvider man) {
        SHADERS.forEach(shader -> {
            try {
                cons.accept(new ShaderInstance(man, new MachinaRL(shader.name), DefaultVertexFormat.NEW_ENTITY),
                        shader::setShaderInstance);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
