package dev.vyro.client.module;

import dev.vyro.client.module.modules.BunnyHop;
import dev.vyro.client.module.modules.Flight;
import dev.vyro.client.module.modules.Glow;
import dev.vyro.client.module.modules.Sprint;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final List<Module> MODULES = new ArrayList<>();

    public static void init() {
        MODULES.add(new Sprint());
        MODULES.add(new BunnyHop());
        MODULES.add(new Glow());
        MODULES.add(new Flight());
    }

    public static void tick() {
        for (Module m : MODULES) {
            if (m.isEnabled()) m.onTick();
        }
    }
}
