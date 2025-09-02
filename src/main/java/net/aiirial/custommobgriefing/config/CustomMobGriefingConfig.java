package net.aiirial.custommobgriefing.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CustomMobGriefingConfig {

    private static final File CONFIG_FILE = new File("config/custom_mob_griefing.json");
    private static Map<String, Boolean> mobGriefing = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static {
        // Defaultwerte
        mobGriefing.put("creeper", true);
        mobGriefing.put("wither", true);
        mobGriefing.put("ender_dragon", true);
        mobGriefing.put("ghast", true);
        mobGriefing.put("enderman", true);
        mobGriefing.put("ravager", true);
        mobGriefing.put("silverfish", true);
        mobGriefing.put("zombie", true);

        loadConfig();
    }

    public static boolean isGriefingAllowed(String mob) {
        return mobGriefing.getOrDefault(mob, true);
    }

    public static void setGriefingAllowed(String mob, boolean allowed) {
        mobGriefing.put(mob.toLowerCase(), allowed);
        saveConfig();
    }

    public static void loadConfig() {
        if (!CONFIG_FILE.exists()) {
            saveConfig();
            return;
        }
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            Type type = new TypeToken<Map<String, Boolean>>() {}.getType();
            Map<String, Boolean> loaded = GSON.fromJson(reader, type);
            if (loaded != null) mobGriefing.putAll(loaded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            File parent = CONFIG_FILE.getParentFile();
            if (!parent.exists()) parent.mkdirs();

            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(mobGriefing, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Boolean> getAllSettings() {
        return new HashMap<>(mobGriefing);
    }
}
