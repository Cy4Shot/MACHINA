/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.api.starchart;

import java.util.Collection;
import java.util.Optional;
import java.util.function.UnaryOperator;

import com.machina.api.planet.attribute.PlanetAttribute;
import com.machina.api.planet.attribute.PlanetAttributeType;
import com.matyrobbrt.lib.nbt.NBTList;

import net.minecraft.nbt.CompoundNBT;

public class PlanetAttributeList extends NBTList<PlanetAttribute<?>, CompoundNBT> {

    private static final long serialVersionUID = 357647471350483603L;

    public PlanetAttributeList() {
        super(PlanetAttribute::fromNBT);
    }

    @Override
    public boolean add(PlanetAttribute<?> e) {
        if (getAttributeForType(e.getAttributeType()).isPresent()) {
            return false;
        }
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends PlanetAttribute<?>> c) {
        int oldSize = size();
        c.forEach(this::add);
        return oldSize != size();
    }

    @SuppressWarnings("unchecked")
    public <Z> Optional<PlanetAttribute<Z>> getAttributeForType(PlanetAttributeType<Z> type) {
        // TODO a better filter
        for (int i = 0; i < size(); i++) {
            if (get(i).getAttributeType() == type) {
                return Optional.ofNullable((PlanetAttribute<Z>) get(i));
            }
        }
        return Optional.empty();
    }
    
    public <Z> Z getValue(PlanetAttributeType<Z> type) {
        return getAttributeForType(type).get().getValue();
    }

    /**
     * Sets the value of the attribute, creating it if it does not exist
     *
     * @param <Z>
     * @param type
     * @param value a function accepting the old value and returning the new one.
     *              The input value will be null if the attribute doesn't exist so
     *              keep that in mind
     */
    public <Z> void setValue(PlanetAttributeType<Z> type, UnaryOperator<Z> value) {
        getAttributeForType(type).ifPresent(attr -> attr.setValue(value.apply(attr.getValue())));
        if (!getAttributeForType(type).isPresent()) {
            add(new PlanetAttribute<>(type, value.apply(null)));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void set(PlanetAttributeType<?> type, Object value) {
        getAttributeForType(type).ifPresent(attr -> attr.set(value));
        if (!getAttributeForType(type).isPresent()) {
            add(new PlanetAttribute(type, value));
        }
    }

    public static UnaryOperator<Integer> intOperator(Operation operation, Integer value) {
        switch (operation) {
            case ADDITION:
                return old -> value + old;
            case SUBTRACTION:
                return old -> old - value;
            case MULTIPLICATION:
                return old -> value * old;
            case DIVISION:
                return old -> old / value;
        }
        return old -> value;
    }

    public enum Operation {
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION;
    }

}
