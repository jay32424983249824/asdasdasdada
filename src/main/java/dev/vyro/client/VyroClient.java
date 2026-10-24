package dev.vyro.client;

import dev.vyro.client.gui.ClickGui;
import dev.vyro.client.hud.Hud;
import dev.vyro.client.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class VyroClient implements ClientModInitializer {
    /** Taste zum Oeffnen des Menues: Rechte Shift-Taste. */
    private static final int MENU_KEY = GLFW.GLFW_KEY_RIGHT_SHIFT;
    private boolean menuKeyWasDown = false;

    @Override
    public void onInitializeClient() {
        ModuleManager.init();
        Hud.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ModuleManager.tick();
            handleMenuKey(client);
        });
    }

    private void handleMenuKey(MinecraftClient client) {
        boolean down = GLFW.glfwGetKey(client.getWindow().getHandle(), MENU_KEY) == GLFW.GLFW_PRESS;
        if (down && !menuKeyWasDown && client.currentScreen == null && client.player != null) {
            client.setScreen(new ClickGui());
        }
        menuKeyWasDown = down;
    }
}
