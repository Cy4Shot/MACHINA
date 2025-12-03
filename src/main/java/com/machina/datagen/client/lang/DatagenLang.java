package com.machina.datagen.client.lang;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.text.JTextComponent.KeyBinding;

import org.jetbrains.annotations.NotNull;

import com.machina.api.cap.sided.ConnectionSide;
import com.machina.api.item.ConnectorFilterItem.Mode;
import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.FruitInit.Fruit;
import com.machina.registration.init.RecipeInit.RecipeRegistryObject;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;

public abstract class DatagenLang extends LanguageProvider {

    protected final Map<String, String> data = new TreeMap<>();

    final String modid;
    String music_disc, bucket;

    public DatagenLang(PackOutput gen, String locale, String modid) {
        super(gen, modid, locale);
        this.modid = modid;
    }

    public <T> void add(RegistryObject<T> key, String name) {
        T item = key.get();
        if (item instanceof Block) {
            add(((Block) item).getDescriptionId(), name);
        } else if (item instanceof Item) {
            add(((Item) item).getDescriptionId(), name);
        } else if (item instanceof CreativeModeTab) {
            add(modid + ".creativemodetab." + key.getId().getPath(), name);
        } else if (item instanceof RocketPart<?> part) {
            add("rocket_part." + modid + "." + key.getId().getPath(), name);
            add(part.getItem().getDescriptionId(), name);
        }
    }

    protected void add(Fruit fruit, String name) {
        add(fruit.block(), name);
    }

    protected void add(Fluid fluid, String name) {
        add(new FluidStack(fluid, 2).getTranslationKey(), name);
    }

    protected void add(Mode mode, String name) {
        add(modid + ".filter." + mode.name().toLowerCase(), name);
    }

    protected void add(ConnectionSide mode, String name) {
        add(modid + ".connection_side." + mode.name().toLowerCase(), name);
    }

    protected void add(RocketPartType mode, String name) {
        add(modid + ".rocket_part_type." + mode.name().toLowerCase(), name);
    }

    protected void add(FluidObject obj, String name) {
        add(obj.fluid(), name);
        add(obj.bucket(), name + " " + this.bucket);
    }

    protected void add(RecipeRegistryObject<?> obj, String name) {
        add(modid + ".recipe." + obj.id().getPath(), name);
    }

    protected void addTooltip(String item, String name) {
        add(modid + ".tooltip." + item, name);
    }

    protected void add(KeyBinding key, String name) {
        add(key.actionName, name);
    }

    protected void addUI(String key, String name) {
        add("gui." + modid + "." + key, name);
    }

    protected void addKeyCategory(String category, String name) {
        add("key.category." + category, name);
    }

    public void addMusicDisc(Item key, String desc) {
        add(key.getDescriptionId(), this.music_disc);
        add(key.getDescriptionId() + ".desc", desc);
    }
    
    protected void addMisc(String thing, String name) {
        add(modid + "." + thing, name);
    }

    public void add(@NotNull String key, @NotNull String value) {
        if (data.put(key, value) != null)
            throw new IllegalStateException("Duplicate translation key " + key);
        super.add(key, value);
    }
}
