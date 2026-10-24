package dev.vyro.client.module.modules;

import dev.vyro.client.module.Module;

/** Aktiviert Creative-Flug (Doppel-Leertaste). Auf vielen Servern verboten! */
public class Flight extends Module {
    public Flight() { super("Flight", "Creative-Flug (Doppel-Leertaste)."); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        mc.player.getAbilities().allowFlying = true;
    }

    @Override
    public void onDisable() {
        if (mc.player == null || mc.player.isCreative() || mc.player.isSpectator()) return;
        mc.player.getAbilities().allowFlying = false;
        mc.player.getAbilities().flying = false;
    }
}
