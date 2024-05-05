package com.kanzaji.ars_rituals_kubejs.rituals;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class AbstractRitualWrapper extends AbstractRitual {
    private final ResourceLocation id;
    private final Consumer<AbstractRitual> onServerStart;
    private final Consumer<AbstractRitual> onClientStart;
    private final Consumer<AbstractRitual> serverTick;
    private final Consumer<AbstractRitual> clientTick;
    private final int sourceCost;
    public AbstractRitualWrapper(
            ResourceLocation id,
            int sourceCost,
            Consumer<AbstractRitual> sTick,
            Consumer<AbstractRitual> cTick,
            Consumer<AbstractRitual> onServerStart,
            Consumer<AbstractRitual> onClientStart
    ) {
        this.id = id;
        this.sourceCost = sourceCost;
        this.serverTick = sTick;
        this.clientTick = cTick;
        this.onServerStart = onServerStart;
        this.onClientStart = onClientStart;
    }

    public AbstractRitualWrapper copy() {
        return new AbstractRitualWrapper(this.id, this.sourceCost, this.serverTick, this.clientTick, this.onServerStart, this.onClientStart);
    }

    @Override
    protected void tick() {
        if (getLevel().isClientSide) serverTick.accept(this); else clientTick.accept(this);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return id;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sourceCost > 0) this.setNeedsSource(true);
        if (getLevel().isClientSide) onServerStart.accept(this); else onClientStart.accept(this);
    }

    @Override
    public boolean canStart() {
        return super.canStart();
    }

    @Override
    public int getSourceCost() {
        return this.sourceCost;
    }

    /**
     * Convenient wrapper, as Ars Nouveau decided to name this getWorld()
     * @return Level Object of this ritual.
     */
    public Level getLevel() {
        return this.getWorld();
    }
}
