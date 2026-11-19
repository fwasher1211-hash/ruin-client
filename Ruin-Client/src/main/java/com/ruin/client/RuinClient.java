package com.ruin.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class RuinClient implements ClientModInitializer {
    public static final String NAME = "RUIN CLIENT";
    public static final Gson JSON = new GsonBuilder().setPrettyPrinting().create();
    public static final List<Module> MODULES = new ArrayList<>();
    public static final String[] THEMES = {"Ruin", "Crimson", "Frost", "Emerald", "Void", "Solar", "Ocean", "Abyss"};
    public static int theme = 0;
    private static KeyBinding openGui;
    private static final Path CONFIG = Path.of("config", "ruin-client.json");

    @Override public void onInitializeClient() {
        registerModules();
        load();
        openGui = KeyBindingHelper.registerKeyBinding(new KeyBinding("Open Ruin GUI", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "Ruin Client"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGui.wasPressed()) client.setScreen(new RuinScreen());
        });
    }
    public static void registerModules() {
        add("Interface", "ClickGUI", "HUD Editor", "ArrayList", "TargetHUD", "Keybind Manager", "Config Manager", "Theme Manager", "Notification Manager", "Module Search", "Custom Fonts", "Custom Crosshair", "Keystrokes", "CPS Counter", "FPS Counter", "Ping HUD", "Armor HUD", "Potion HUD", "Coordinates HUD", "Session Info", "Custom Animations");
        add("Crystal PvP", "Crystal Aura", "Auto Crystal", "Crystal Macro", "Anchor Aura", "Anchor Macro", "Safe Anchor", "Auto Totem", "Inventory Totem", "Auto Armor", "Auto Obsidian", "Surround", "Auto Trap", "Self Trap", "Hole Fill", "Hole ESP", "Hole Snap", "Burrow", "Auto City", "Packet Mine", "Fast Mine", "Auto XP", "Auto Refill", "Auto Gapple", "Auto Pearl", "Anti Crystal", "Anti Anchor", "Auto Disconnect", "Kill Aura", "Criticals", "Velocity");
        add("UHC", "Kill Aura", "Aim Assist", "Trigger Bot", "Auto Clicker", "Reach", "Hitboxes", "Velocity", "Criticals", "W-Tap", "S-Tap", "D-Tap", "Auto Sprint", "No Slow", "Auto Gapple", "Auto Heal", "Auto Pot", "Auto Soup", "Auto Armor", "Auto Weapon", "Auto Rod", "Bow Aim", "Projectile Prediction", "Shield Breaker", "Auto Shield", "Backtrack", "Fake Lag", "Target Strafe", "Auto Dodge", "Anti Bot", "Teams");
        add("Mace PvP", "Mace Aura", "Mace Swap", "Auto Mace", "Auto Breach Swap", "Auto Density Swap", "Wind Charge Macro", "Auto Wind Charge", "Wind Charge Aim", "Mace Aim Assist", "Mace Criticals", "Mace Prediction", "Auto Jump", "High Jump", "Auto Pearl", "Auto Totem", "Inventory Totem", "Auto Gapple", "Velocity", "No Fall", "Kill Aura", "Reach", "Hitboxes", "Target ESP", "Auto Weapon", "Auto Armor");
        add("SMP", "Kill Aura", "Aim Assist", "Trigger Bot", "Auto Clicker", "Reach", "Hitboxes", "Criticals", "Velocity", "Backtrack", "Fake Lag", "Auto Totem", "Inventory Totem", "Auto Armor", "Auto Gapple", "Auto Pearl", "Auto Pot", "Auto Shield", "Shield Breaker", "Auto Weapon", "W-Tap", "S-Tap", "D-Tap", "Auto Sprint", "No Slow", "Speed", "Fly", "Elytra Fly", "Elytra Boost", "No Fall", "Scaffold", "Safe Walk", "Step", "Jesus", "Spider", "Phase", "Freecam", "Player ESP", "Storage ESP", "Chest ESP", "Tracers", "XRay", "Fullbright", "Nametags", "Auto Mine", "Auto Farm", "Auto Fish", "Auto Tool", "Fast Place", "Fast Break", "Chest Stealer", "Inventory Manager", "Auto Refill", "Auto Eat", "Auto Reconnect", "Anti AFK", "Baritone", "Auto Path", "Auto Build", "Auto Tunnel", "Packet Mine");
        add("BedWars", "Bridge Practice", "Speed Bridge Assist", "Breezily Practice", "Godbridge Practice", "Safe Walk", "Block Counter", "Bridge HUD", "Placement Timing", "Scaffold");
    }
    private static void add(String category, String... names) { for (String name : names) MODULES.add(new Module(category, name)); }
    public static int accent() { return switch (theme) {
        case 1 -> 0xFFED405A; case 2 -> 0xFF43C9FF; case 3 -> 0xFF3AE68D; case 4 -> 0xFFD8D8E4;
        case 5 -> 0xFFFFB642; case 6 -> 0xFF378CFF; case 7 -> 0xFFFF55C8; default -> 0xFFB65BFF;
    }; }
    public static void save() {
        try {
            Files.createDirectories(CONFIG.getParent());
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("theme", theme);
            Map<String, Boolean> toggles = new LinkedHashMap<>();
            for (Module m : MODULES) toggles.put(m.category + "/" + m.name, m.enabled);
            data.put("modules", toggles);
            Files.writeString(CONFIG, JSON.toJson(data));
        } catch (Exception e) { System.err.println("Ruin config save failed: " + e.getMessage()); }
    }
    public static void load() {
        if (!Files.exists(CONFIG)) return;
        try {
            var data = com.google.gson.JsonParser.parseString(Files.readString(CONFIG)).getAsJsonObject();
            if (data.has("theme")) theme = Math.floorMod(data.get("theme").getAsInt(), THEMES.length);
            if (data.has("modules")) {
                var modules = data.getAsJsonObject("modules");
                for (Module m : MODULES) {
                    String key = m.category + "/" + m.name;
                    if (modules.has(key)) m.enabled = modules.get(key).getAsBoolean();
                }
            }
        } catch (Exception e) { System.err.println("Ruin config load failed: " + e.getMessage()); }
    }
    public static final class Module {
        public final String category, name;
        public boolean enabled;
        public Module(String category, String name) { this.category = category; this.name = name; }
    }
}
