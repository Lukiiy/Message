package me.lukiiy.message;

import me.lukiiy.message.cmds.Msg;
import me.lukiiy.message.cmds.Reply;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Message extends JavaPlugin {
    public static Message inst;
    public static FileConfiguration config;
    public static HashMap<UUID, UUID> replyData = new HashMap<>();

    @Override
    public void onEnable() {
        inst = this;
        getCommand("msg").setExecutor(new Msg());
        getCommand("r").setExecutor(new Reply());

        saveDefaultConfig();
        load();
    }

    @Override
    public void onDisable() {
    }

    // Config
    public void load() {
        config = getConfig();
        saveConfig();
    }

    public static String get(String p) {
        return config.getString("msgs." + p, "<red><i>Message not set.");
    }

    public static boolean getBool(String path) {
        return config.getBoolean(path);
    }
}
