package com.jsblock.client;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jsblock.Joban;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * Class responsible for reading/saving the config value.<br>
 * For config screen, see {@link com.jsblock.screen.ConfigScreen} and {@link com.jsblock.screen.ConfigScreenBase}
 * @author LX86
 * @since 1.0.6
 */
public class ClientConfig {
    private static final String CONFIG_PATH = System.getProperty("user.dir") + "/config/" + "jsclient.json";
    private static boolean renderDisabled = false;
    private static boolean bypassServerVersionCheck = false;
    private static boolean debugMode = false;

    /**
     * This loads the config file and sets the variable internally
     */
    public static void loadConfig() {
        if (!Files.exists(Paths.get(CONFIG_PATH))) {
            Joban.LOGGER.warn("[Joban Client] Config file not found, generating one...");
            try {
                writeConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        Joban.LOGGER.info("[Joban Client] Reading Config...");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(Paths.get(CONFIG_PATH)))).getAsJsonObject();

            if (jsonConfig.has("renderDisabled")) {
                renderDisabled = jsonConfig.get("renderDisabled").getAsBoolean();
            }

            if(jsonConfig.has("bypassVersionCheck")) {
                bypassServerVersionCheck = jsonConfig.get("bypassVersionCheck").getAsBoolean();
            }

            if(jsonConfig.has("debugMode")) {
                debugMode = jsonConfig.get("debugMode").getAsBoolean();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                writeConfig();
            } catch (Exception er) {
                er.printStackTrace();
            }
        }
    }

    /**
     * This writes the current config
     */
    public static void writeConfig() {
        Joban.LOGGER.info("[Joban Client] Writing Config...");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty("renderDisabled", renderDisabled);
        jsonConfig.addProperty("bypassVersionCheck", bypassServerVersionCheck);
        jsonConfig.addProperty("debugMode", debugMode);

        try {
            Files.write(Paths.get(CONFIG_PATH), Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getRenderDisabled() {
        return renderDisabled;
    }

    public static boolean getVersionCheckDisabled() {
        return bypassServerVersionCheck;
    }

    public static boolean getDebugModeEnabled() {
        return debugMode;
    }

    public static boolean setRenderDisabled(boolean disabled) {
        renderDisabled = disabled;
        return renderDisabled;
    }

    public static boolean setDebugMode(boolean enabled) {
        debugMode = enabled;
        return debugMode;
    }

    public static boolean setVersionCheckDisabled(boolean disabled) {
        bypassServerVersionCheck = disabled;
        return bypassServerVersionCheck;
    }
}
