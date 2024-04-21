package com.kanzaji.ars_rituals_kubejs.rituals;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class AbstractRitualWrapper extends AbstractRitual {
    private final ResourceLocation id;
    private final Consumer<AbstractRitual> tick;
    public AbstractRitualWrapper(ResourceLocation id, Consumer<AbstractRitual> tick) {
        this.id = id;
        this.tick = tick;
    }

    public AbstractRitualWrapper copy() {
        return new AbstractRitualWrapper(this.id, this.tick);
    }

    @Override
    protected void tick() {
        this.tick.accept(this);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return id;
    }
}
