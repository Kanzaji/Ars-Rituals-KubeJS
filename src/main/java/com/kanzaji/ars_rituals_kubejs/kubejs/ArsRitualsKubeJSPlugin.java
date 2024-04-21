package com.kanzaji.ars_rituals_kubejs.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class ArsRitualsKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerBindings(BindingsEvent event) {
        super.registerBindings(event);
        event.add("ArsRituals", null);
    }
}
