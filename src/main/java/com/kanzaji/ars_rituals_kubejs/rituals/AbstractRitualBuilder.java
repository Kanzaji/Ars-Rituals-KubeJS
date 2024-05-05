package com.kanzaji.ars_rituals_kubejs.rituals;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.setup.registry.APIRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class AbstractRitualBuilder {
    private final ResourceLocation id;
    private int cost = 0;
    private Consumer<AbstractRitual> serverTick = (r) -> {};
    private Consumer<AbstractRitual> clientTick = (r) -> {};
    private Consumer<AbstractRitual> onServerStart = (r) -> {};
    private Consumer<AbstractRitual> onClientStart = (r) -> {};
    private Function<AbstractRitual, Boolean> canStart = (r) -> true;

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
        this.serverTick = Objects.requireNonNull(action);
        return this;
    }

    public @NotNull AbstractRitualBuilder setClientTick(@NotNull Consumer<AbstractRitual> action) {
        this.clientTick = Objects.requireNonNull(action);
        return this;
    }

    public @NotNull AbstractRitualBuilder onStart(@NotNull Consumer<AbstractRitual> action) {
        this.onServerStart = Objects.requireNonNull(action);
        return this;
    }

    public @NotNull AbstractRitualBuilder onClientStart(@NotNull Consumer<AbstractRitual> action) {
        this.onClientStart = Objects.requireNonNull(action);
        return this;
    }

    public @NotNull AbstractRitualBuilder startCondition(@NotNull Function<AbstractRitual, Boolean> function) {
        this.canStart = Objects.requireNonNull(function);
        return this;
    }

    public @NotNull AbstractRitualBuilder setItemsToConsume(List<ItemStack> items) {
        return this;
    }

    public @NotNull AbstractRitualBuilder setCost(int sourceCost) {
        this.cost = sourceCost;
        return this;
    }

    /**
     * Build method that is used to register the new Ritual to the Ars Nouveau registry.
     * If used with already existing ID, it will override it.
     */
    public void build() {
        APIRegistry.registerRitual(new AbstractRitualWrapper(id, cost, serverTick, clientTick, onServerStart, onClientStart));
    }

    /**
     * Build method that allows to create a ritual based on this builder, but with different ID.
     * @param id ResourceLocation under which Ritual will be registered.
     */
    public void build(@NotNull ResourceLocation id) {
        APIRegistry.registerRitual(new AbstractRitualWrapper(Objects.requireNonNull(id), cost, serverTick, clientTick, onServerStart, onClientStart));
    }

    public void build (@NotNull String id) {
        try {
            this.build(new ResourceLocation(id));
        } catch (Exception e) {
            throw new IllegalArgumentException("Incorrect ID provided! " + e.getMessage());
        }
    }
}
