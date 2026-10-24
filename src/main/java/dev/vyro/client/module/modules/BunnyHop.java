package dev.vyro.client.module.modules;

import dev.vyro.client.module.Module;
import net.minecraft.util.math.Vec3d;

public class BunnyHop extends Module {
    public BunnyHop() { super("BunnyHop", "Huepft automatisch beim Laufen."); }

    @Override
    public void onTick() {
        if (mc.player == null || !mc.player.isOnGround() || !isMoving() || mc.player.isSneaking()) return;
        double yaw = Math.toRadians(mc.player.getYaw());
        double boost = 0.05;
        Vec3d v = mc.player.getVelocity();
        mc.player.setVelocity(v.x - Math.sin(yaw) * boost, 0.42, v.z + Math.cos(yaw) * boost);
    }
}
