package dev.seano.fullbright;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

/**
 * Part of this class was taken from https://github.com/PabloPerezRodriguez/twitch-chat and is licensed by MIT
 */

public class ModConfig {

    private static ModConfig INSTANCE = null;
    private final File file;

    private double defaultGamma;

    public ModConfig() {
        this.file = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("fullbright.json").toFile();
        this.defaultGamma = 1.0D;
    }

    public static ModConfig getConfig() {
        if (INSTANCE == null) INSTANCE = new ModConfig();
        return INSTANCE;
    }

    public void load() {
        try {
            String json = new String(Files.readAllBytes(this.file.toPath()));
            if (!json.equals("")) {
                JsonObject jsonObject = (JsonObject) new JsonParser().parse(json);
                this.defaultGamma = jsonObject.getAsJsonPrimitive("defaultGamma").getAsDouble();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("defaultGamma", 1.0D);

        try (PrintWriter out = new PrintWriter(file)) {
            out.println(jsonObject.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public double getDefaultGamma() {
        return defaultGamma;
    }

    public void setDefaultGamma(double defaultGamma) {
        this.defaultGamma = defaultGamma;
    }
}
