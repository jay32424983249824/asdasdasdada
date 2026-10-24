package dev.vyro.client.module.modules;

import dev.vyro.client.module.Module;

public class Sprint extends Module {
    public Sprint() { super("Sprint", "Sprintet automatisch in alle Richtungen."); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (isMoving() && !mc.player.isSneaking() && mc.player.getHungerManager().getFoodLevel() > 6) {
            mc.player.setSprinting(true);
        }
    }
}
