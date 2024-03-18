package me.lukiiy.message;

import me.lukiiy.message.commands.Msg;
import me.lukiiy.message.commands.Reply;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Message extends JavaPlugin {

    public static Message INSTANCE;
    public static TextColor mainColor = TextColor.color(0xB49BC7);


    @Override
    public void onEnable() {
        INSTANCE = this;
        getCommand("msg").setExecutor(new Msg());
        getCommand("r").setExecutor(new Reply());
        load();
    }

    @Override
    public void onDisable() {}


    // Config
    private static FileConfiguration config;

    public static String get(String path) {return config.getString(path);}

    public void load() {
        config = getConfig();
        config.options()
                .parseComments(false)
                .copyDefaults(true);
        saveDefaultConfig();
    }
}
