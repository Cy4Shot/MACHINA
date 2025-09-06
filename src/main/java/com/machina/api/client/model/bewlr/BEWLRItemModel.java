package com.machina.api.client.model.bewlr;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BEWLRItemModel implements IDynamicBakedModel {

    private final boolean isAmbientOcclusion;
    private final boolean isGui3d;
    private final boolean useBlockLight;

    public BEWLRItemModel(boolean isAmbientOcclusion, boolean isGui3d, boolean useBlockLight) {
        this.isAmbientOcclusion = isAmbientOcclusion;
        this.isGui3d = isGui3d;
        this.useBlockLight = useBlockLight;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.isAmbientOcclusion;
    }

    @Override
    public boolean isGui3d() {
        return this.isGui3d;
    }

    @Override
    public boolean usesBlockLight() {
        return this.useBlockLight;
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return null;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                             @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        return List.of();
    }
}
