package com.kanzaji.ars_rituals_kubejs;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ArsRituals.MODID)
public class ArsRituals {
    public static final String MODID = "ars_rituals_kubejs";
    private static final Logger logger = LogUtils.getLogger();
    public ArsRituals() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        logger.info("Ars Rituals KubeJS Addon successfully registered.");
    }
}
