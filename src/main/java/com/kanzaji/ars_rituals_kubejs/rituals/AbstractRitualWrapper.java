package com.kanzaji.ars_rituals_kubejs.rituals;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class AbstractRitualWrapper extends AbstractRitual {
    private final ResourceLocation id;
    private final boolean canBeTraded;
    private final int sourceCost;
    private final int particleIntensity;
    private final ParticleColor outerColor;
    private final ParticleColor innerColor;
    private final Predicate<ItemStack> canConsume;
    private final Predicate<AbstractRitual> canStartServer;
    private final Predicate<AbstractRitual> canStartClient;
    private final Consumer<AbstractRitual> onServerStart;
    private final Consumer<AbstractRitual> onClientStart;
    private final Consumer<AbstractRitual> serverTick;
    private final Consumer<AbstractRitual> clientTick;
    private final Consumer<AbstractRitual> onServerEnd;
    private final Consumer<AbstractRitual> onClientEnd;
    public AbstractRitualWrapper(
            ResourceLocation id,
            boolean canBeTraded,
            int sourceCost,
            int particleIntensity,
            ParticleColor outerColor,
            ParticleColor innerColor,
            Predicate<ItemStack> canConsume,
            Predicate<AbstractRitual> canStartServer,
            Predicate<AbstractRitual> canStartClient,
            Consumer<AbstractRitual> onServerStart,
            Consumer<AbstractRitual> onClientStart,
            Consumer<AbstractRitual> serverTick,
            Consumer<AbstractRitual> clientTick,
            Consumer<AbstractRitual> onServerEnd,
            Consumer<AbstractRitual> onClientEnd
    ) {
        this.id                 = id;
        this.canBeTraded        = canBeTraded;
        this.sourceCost         = sourceCost;
        this.particleIntensity  = particleIntensity;
        this.outerColor         = outerColor;
        this.innerColor         = innerColor;
        this.canConsume         = canConsume;
        this.canStartServer     = canStartServer;
        this.canStartClient     = canStartClient;
        this.onServerStart      = onServerStart;
        this.onClientStart      = onClientStart;
        this.serverTick         = serverTick;
        this.clientTick         = clientTick;
        this.onServerEnd        = onServerEnd;
        this.onClientEnd        = onClientEnd;
    }

    private AbstractRitualWrapper(@NotNull AbstractRitualWrapper ARW) {
        this.id                 = ARW.id;
        this.canBeTraded        = ARW.canBeTraded;
        this.sourceCost         = ARW.sourceCost;
        this.particleIntensity  = ARW.particleIntensity;
        this.outerColor         = ARW.outerColor;
        this.innerColor         = ARW.innerColor;
        this.canConsume         = ARW.canConsume;
        this.canStartServer     = ARW.canStartServer;
        this.canStartClient     = ARW.canStartClient;
        this.onServerStart      = ARW.onServerStart;
        this.onClientStart      = ARW.onClientStart;
        this.serverTick         = ARW.serverTick;
        this.clientTick         = ARW.clientTick;
        this.onServerEnd        = ARW.onServerEnd;
        this.onClientEnd        = ARW.onClientEnd;
    }

    public AbstractRitualWrapper copy() {
        return new AbstractRitualWrapper(this);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return id;
    }

    @Override
    public int getSourceCost() {
        return this.sourceCost;
    }

    @Override
    public ParticleColor getCenterColor() {
        return this.innerColor;
    }

    @Override
    public ParticleColor getOuterColor() {
        return this.outerColor;
    }

    @Override
    public int getParticleIntensity() {
        return this.particleIntensity;
    }

    @Override
    public boolean canBeTraded() {
        return this.canBeTraded;
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        return this.canConsume.test(stack);
    }

    // Wrapped functions for easier access to the original ones.

    /**
     * Convenient wrapper, as Ars Nouveau decided to name this getWorld()
     * @return Level Object of this ritual.
     */
    public Level getLevel() {
        return this.getWorld();
    }

    public boolean isClient() {
        return this.getLevel().isClientSide;
    }

    public boolean isServer() {
        return !this.getLevel().isClientSide;
    }

    // Wrapped functions created by the user in KubeJS.

    @Override
    public boolean canStart() {
        if (isServer()) return this.canStartServer.test(this); else return this.canStartClient.test(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sourceCost > 0) this.setNeedsSource(true);
        if (isServer()) onServerStart.accept(this); else onClientStart.accept(this);
    }

    @Override
    protected void tick() {
        if (isServer()) serverTick.accept(this); else clientTick.accept(this);
    }

    @Override
    public void onEnd() {
        super.onEnd();
        if (isServer()) onServerEnd.accept(this); else onClientEnd.accept(this);
    }
}

//TODO: Stuff to negotiate with users:
// - Should all custom methods be wrapped in try-catch and reported in the logs instead of crashing the game?
// - If 1st true -> Should tablet be voided or spawned above the brazer tile?
// - If 1st true -> Should we mention it on the chat/to the player (somehow)?
// - Should I add a text in RitualTablet#appnendHoverText to tell people the tablet is custom?