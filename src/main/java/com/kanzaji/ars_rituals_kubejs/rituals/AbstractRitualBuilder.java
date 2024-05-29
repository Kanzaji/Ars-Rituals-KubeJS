package com.kanzaji.ars_rituals_kubejs.rituals;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.setup.registry.APIRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AbstractRitualBuilder {
    //TODO: Methods to add
    // - canConsumeItem + Some ready-to-go impl (Server Only? I think)
    // Stuff to do:
    // - Generate localization files automatically.
    // - Force this to be only avaiable in the Startup scripts.
    // - Create your own event
    // - Create few ready-to-go impl of few things
    // > setConsumedItems.
    private final ResourceLocation id;
    private boolean canBeTraded = true;
    private int sourceCost = 0;
    private int particleIntensity = 10;
    private ParticleColor outerColor = ParticleColor.WHITE;
    private ParticleColor innerColor = ParticleColor.WHITE;
    private Predicate<ItemStack> canConsume = (i) -> false;
    private Predicate<AbstractRitual> canStartServer = (r) -> true;
    private Predicate<AbstractRitual> canStartClient = (r) -> true;
    private Consumer<AbstractRitual> onServerStart = (r) -> {};
    private Consumer<AbstractRitual> onClientStart = (r) -> {};
    private Consumer<AbstractRitual> serverTick = (r) -> {};
    private Consumer<AbstractRitual> clientTick = (r) -> {};
    private Consumer<AbstractRitual> onServerEnd = (r) -> {};
    private Consumer<AbstractRitual> onClientEnd = (r) -> {};

    private AbstractRitualBuilder(ResourceLocation id) {
        this.id = id;
    }

    @Contract("_ -> new")
    public static @NotNull AbstractRitualBuilder create(@NotNull String id) {
        try {
            return new AbstractRitualBuilder(new ResourceLocation(Objects.requireNonNull(id, "ID Can't be null!")));
        } catch (Exception e) {
            throw new IllegalArgumentException("Incorrect ID provided! " + e.getMessage());
        }
    }

    public @NotNull AbstractRitualBuilder setCost(int sourceCost) {
        this.sourceCost = sourceCost;
        return this;
    }

    public @NotNull AbstractRitualBuilder canBeTraded(boolean value) {
        this.canBeTraded = value;
        return this;
    }

    public @NotNull AbstractRitualBuilder setParticleIntensity(int intensity) {
        this.particleIntensity = intensity;
        return this;
    }

    public @NotNull AbstractRitualBuilder setInnerColor(@NotNull ParticleColor color) {
        this.innerColor = Objects.requireNonNull(color);
        return this;
    }

    public @NotNull AbstractRitualBuilder setInnerColor(int r, int g, int b) {
        if (r > 255 || g > 255 || b > 255) throw new IllegalArgumentException("Illegal value(s) passed as the argument. RGB Value can't be above 255! Ritual ID: " + id);
        this.innerColor = new ParticleColor(r, g, b);
        return this;
    }

    public @NotNull AbstractRitualBuilder setOuterColor(@NotNull ParticleColor color) {
        this.outerColor = Objects.requireNonNull(color);
        return this;
    }

    public @NotNull AbstractRitualBuilder setOuterColor(int r, int g, int b) {
        if (r > 255 || g > 255 || b > 255) throw new IllegalArgumentException("Illegal value(s) passed as the argument. RGB Value can't be above 255! Ritual ID: " + id);
        this.innerColor = new ParticleColor(r, g, b);
        return this;
    }

    public @NotNull AbstractRitualBuilder setConsumePredicate(@NotNull Predicate<ItemStack> predicate) {
        this.canConsume = Objects.requireNonNull(predicate);
        return this;
    }

    /**
     * Creates itemConsumptionPredicate that checks if item to be consumed is one of the provided items.
     * @param items List of String IDs
     * @return AbstractRitualBuilder
     * @implNote NOT_IMPLEMENTED
     */
    public @NotNull AbstractRitualBuilder setConsumedItems(@NotNull List<String> items) {
        return this;
    }

    @Experimental
    public @NotNull AbstractRitualBuilder setStartPredicate(@NotNull Predicate<AbstractRitual> function) {
        this.canStartServer = Objects.requireNonNull(function);
        return this;
    }

    @Experimental
    public @NotNull AbstractRitualBuilder setClientStartPredicate(@NotNull Predicate<AbstractRitual> function) {
        this.canStartClient = Objects.requireNonNull(function);
        return this;
    }

    public @NotNull AbstractRitualBuilder onRitualStart(@NotNull Consumer<AbstractRitual> action) {
        this.onServerStart = Objects.requireNonNull(action);
        return this;
    }

    public @NotNull AbstractRitualBuilder onClientRitualStart(@NotNull Consumer<AbstractRitual> action) {
        this.onClientStart = Objects.requireNonNull(action);
        return this;
    }

    public @NotNull AbstractRitualBuilder setTick(@NotNull Consumer<AbstractRitual> action) {
        this.serverTick = Objects.requireNonNull(action);
        return this;
    }

    public @NotNull AbstractRitualBuilder setClientTick(@NotNull Consumer<AbstractRitual> action) {
        this.clientTick = Objects.requireNonNull(action);
        return this;
    }

    public @NotNull AbstractRitualBuilder onRitualEnd(@NotNull Consumer<AbstractRitual> action) {
        this.onServerEnd = Objects.requireNonNull(action);
        return this;
    }

    public @NotNull AbstractRitualBuilder onClientRitualEnd(@NotNull Consumer<AbstractRitual> action) {
        this.onClientEnd = Objects.requireNonNull(action);
        return this;
    }

    /**
     * Build method that is used to register the new Ritual to the Ars Nouveau registry.
     * @apiNote If used with already existing ID, it will override it.
     */
    public void build() {
        build(id);
    }

    /**
     * Build method that allows to create a ritual based on this builder, but with different ID.
     * @param id ResourceLocation under which Ritual will be registered.
     * @apiNote If used with already existing ID, it will override it.
     */
    public void build(@NotNull ResourceLocation id) {
        APIRegistry.registerRitual(new AbstractRitualWrapper(
                Objects.requireNonNull(id),
                canBeTraded,
                sourceCost,
                particleIntensity,
                outerColor,
                innerColor,
                canConsume,
                canStartServer,
                canStartClient,
                onServerStart,
                onClientStart,
                serverTick,
                clientTick,
                onServerEnd,
                onClientEnd
        ));
    }

    /**
     * Build method that allows to create a ritual based on this builder, but with different ID.
     * @param id String representation of ResourceLocation under which Ritual will be registered.
     * @apiNote If used with already existing ID, it will override it.
     */
    public void build (@NotNull String id) {
        try {
            this.build(new ResourceLocation(Objects.requireNonNull(id, "ID Can't be null!")));
        } catch (Exception e) {
            throw new IllegalArgumentException("Incorrect ID provided! " + e.getMessage());
        }
    }
}
