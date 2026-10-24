package dev.vyro.client.module.modules;

import dev.vyro.client.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

import java.util.HashSet;
import java.util.Set;

/** Scannt geladene Chunks und meldet moegliche Basen mit Koordinaten im Chat. */
public class BaseFinder extends Module {
    // Einstellungen (hier im Code aenderbar)
    private static final int MIN_STORAGE = 5;       // Lager-Bloecke pro Chunk
    private static final int MIN_SUS_SECTIONS = 2;  // Abschnitte mit unnatuerlichen Bloecken
    private static final int SCAN_INTERVAL = 40;    // Ticks (40 = 2 Sekunden)

    private static final Set<Block> SUS = Set.of(
        Blocks.BEACON, Blocks.CONDUIT, Blocks.ENCHANTING_TABLE, Blocks.JUKEBOX,
        Blocks.NOTE_BLOCK, Blocks.OBSERVER, Blocks.PISTON, Blocks.STICKY_PISTON,
        Blocks.REPEATER, Blocks.COMPARATOR, Blocks.REDSTONE_LAMP, Blocks.DAYLIGHT_DETECTOR,
        Blocks.GLASS, Blocks.WHITE_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.BLACK_CONCRETE,
        Blocks.LIGHT_GRAY_CONCRETE
    );

    private final Set<Long> reported = new HashSet<>();
    private ClientWorld lastWorld;
    private int timer;

    public BaseFinder() { super("BaseFinder", "Meldet moegliche Basen im Chat."); }

    @Override
    public void onEnable() {
        reported.clear();
        timer = SCAN_INTERVAL;
        say("BaseFinder aktiv. Bewege dich, um neue Chunks zu laden.");
    }

    @Override
    public void onTick() {
        if (mc.world == null || mc.player == null) return;
        if (mc.world != lastWorld) {
            lastWorld = mc.world;
            reported.clear();
        }
        if (++timer < SCAN_INTERVAL) return;
        timer = 0;

        int radius = mc.options.getViewDistance().getValue();
        int pcx = mc.player.getBlockX() >> 4;
        int pcz = mc.player.getBlockZ() >> 4;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int cx = pcx + dx;
                int cz = pcz + dz;
                long key = ((long) cx << 32) ^ (cz & 0xFFFFFFFFL);
                if (reported.contains(key)) continue;

                WorldChunk chunk = mc.world.getChunkManager().getWorldChunk(cx, cz);
                if (chunk == null) continue;

                int storage = 0;
                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    if (isStorage(be)) storage++;
                }

                int susSections = 0;
                for (ChunkSection section : chunk.getSectionArray()) {
                    if (section != null && !section.isEmpty()
                        && section.hasAny(state -> SUS.contains(state.getBlock()))) {
                        susSections++;
                    }
                }

                if (storage >= MIN_STORAGE || susSections >= MIN_SUS_SECTIONS) {
                    reported.add(key);
                    say(String.format("Moegliche Base bei X %d, Z %d (%d Lager-Bloecke, %d verdaechtige Abschnitte)",
                        cx * 16 + 8, cz * 16 + 8, storage, susSections));
                }
            }
        }
    }

    private static boolean isStorage(BlockEntity be) {
        return be instanceof ChestBlockEntity || be instanceof BarrelBlockEntity
            || be instanceof ShulkerBoxBlockEntity || be instanceof EnderChestBlockEntity
            || be instanceof HopperBlockEntity || be instanceof AbstractFurnaceBlockEntity
            || be instanceof DispenserBlockEntity;
    }

    private void say(String text) {
        if (mc.player != null) mc.player.sendMessage(Text.literal("[Vyro] " + text), false);
    }
}
