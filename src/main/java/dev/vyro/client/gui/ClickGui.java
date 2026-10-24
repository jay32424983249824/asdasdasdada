package dev.vyro.client.gui;

import dev.vyro.client.module.Module;
import dev.vyro.client.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ClickGui extends Screen {
    public ClickGui() {
        super(Text.literal("Vyro"));
    }

    private static Text label(Module m) {
        return Text.literal(m.name + ": " + (m.isEnabled() ? "AN" : "AUS"));
    }

    @Override
    protected void init() {
        int y = 40;
        for (Module m : ModuleManager.MODULES) {
            ButtonWidget button = ButtonWidget.builder(label(m), b -> {
                    m.toggle();
                    b.setMessage(label(m));
                })
                .dimensions(this.width / 2 - 75, y, 150, 20)
                .tooltip(Tooltip.of(Text.literal(m.description)))
                .build();
            this.addDrawableChild(button);
            y += 24;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, "Vyro Client", this.width / 2 - 30, 20, 0xFF55FFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
