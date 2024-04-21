package com.kanzaji.ars_rituals_kubejs.rituals;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.setup.registry.APIRegistry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class AbstractRitualBuilder {
    private final ResourceLocation id;
    private Consumer<AbstractRitual> tick = (r) -> {};

    private AbstractRitualBuilder(ResourceLocation id) {
        this.id = id;
    }

    @Contract("_ -> new")
    public static @NotNull AbstractRitualBuilder create(String id) {
        try {
            return new AbstractRitualBuilder(new ResourceLocation(id));
        } catch (Exception e) {
            throw new IllegalArgumentException("Incorrect ID provided! " + e.getMessage());
        }
    }

    public @NotNull AbstractRitualBuilder setTick(@NotNull Consumer<AbstractRitual> action) {
        this.tick = Objects.requireNonNull(action);
        return this;
    }

    public void build() {
        APIRegistry.registerRitual(new AbstractRitualWrapper(id, tick));
    }
}
