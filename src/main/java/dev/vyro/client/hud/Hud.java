package dev.vyro.client.hud;

import dev.vyro.client.module.Module;
import dev.vyro.client.module.ModuleManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.Comparator;
import java.util.List;

public class Hud {
    public static void init() {
        HudRenderCallback.EVENT.register(Hud::render);
    }

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options.hudHidden) return;

        context.drawTextWithShadow(mc.textRenderer, "Vyro", 4, 4, 0xFF55FFFF);

        List<Module> active = ModuleManager.MODULES.stream()
            .filter(Module::isEnabled)
            .sorted(Comparator.comparingInt((Module m) -> mc.textRenderer.getWidth(m.name)).reversed())
            .toList();

        int y = 4;
        int screenWidth = context.getScaledWindowWidth();
        for (Module m : active) {
            int x = screenWidth - mc.textRenderer.getWidth(m.name) - 4;
            context.drawTextWithShadow(mc.textRenderer, m.name, x, y, 0xFFFFFFFF);
            y += 10;
        }
    }
}
