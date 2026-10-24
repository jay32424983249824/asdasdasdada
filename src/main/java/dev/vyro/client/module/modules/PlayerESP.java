package dev.vyro.client.module.modules;

import dev.vyro.client.module.Module;

public class PlayerESP extends Module {
    public PlayerESP() { super("PlayerESP", "Spieler leuchten durch Waende."); }

    @Override
    public void onTick() {
        if (mc.world == null) return;
        for (var p : mc.world.getPlayers()) {
            if (p != mc.player) p.setGlowing(true);
        }
    }

    @Override
    public void onDisable() {
        if (mc.world == null) return;
        for (var p : mc.world.getPlayers()) p.setGlowing(false);
    }
}
