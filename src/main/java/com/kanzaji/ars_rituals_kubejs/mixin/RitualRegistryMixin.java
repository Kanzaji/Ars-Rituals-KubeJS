package com.kanzaji.ars_rituals_kubejs.mixin;

import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.kanzaji.ars_rituals_kubejs.rituals.AbstractRitualWrapper;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ConcurrentHashMap;

@Mixin(RitualRegistry.class)
public class RitualRegistryMixin {
    @Shadow private static ConcurrentHashMap<ResourceLocation, AbstractRitual> ritualMap;

    @Inject(method = "getRitual(Lnet/minecraft/resources/ResourceLocation;)Lcom/hollingsworth/arsnouveau/api/ritual/AbstractRitual;", at = @At("HEAD"), remap = false, cancellable = true)
    private static void getRitual(ResourceLocation id, CallbackInfoReturnable<AbstractRitual> cir) {
        if (ritualMap.get(id) instanceof AbstractRitualWrapper) cir.setReturnValue(((AbstractRitualWrapper) ritualMap.get(id)).copy());
    }
}
