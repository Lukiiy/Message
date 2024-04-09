package me.lukiiy.message;

import me.lukiiy.message.cmds.Msg;
import me.lukiiy.message.cmds.Reload;
import me.lukiiy.message.cmds.Reply;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Message extends JavaPlugin {
    public static Message Plugin;
    public static HashMap<UUID, UUID> replyData = new HashMap<>();
    public static final TextColor mainColor = TextColor.color(0xB49BC7);

    @Override
    public void onEnable() {
        Plugin = this;
        getCommand("msg").setExecutor(new Msg());
        getCommand("r").setExecutor(new Reply());
        getCommand("messagereload").setExecutor(new Reload());
        load();
    }

    @Override
    public void onDisable() {}

    // Config
    private static FileConfiguration config;
    public static String get(String path) {return config.getString(path);}
    public static boolean getBoolean(String path) {return config.getBoolean(path);}

    public void load() {
        config = getConfig();
        config.options()
                .parseComments(false)
                .copyDefaults(true);
        saveDefaultConfig();
    }
}
