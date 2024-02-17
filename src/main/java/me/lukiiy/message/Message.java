package me.lukiiy.message;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Message extends JavaPlugin {

    private static JavaPlugin plugin;

    public static TextColor mainColor = TextColor.color(0xB49BC7);


    @Override
    public void onEnable() {
        plugin = this;
        getCommand("msg").setExecutor(new MessageCmd());
        getCommand("r").setExecutor(new ReplyCmd());
        load();
    }

    @Override
    public void onDisable() {}


    public static JavaPlugin getInstance() {return plugin;}

    // the config.java file
    // TODO remake this
    
    private static YamlConfiguration config;

    public void load() {
        File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) plugin.saveResource("config.yml", false);
        config = new YamlConfiguration();
        config.options().parseComments(false);

        try {config.load(file);}
        catch (Exception e) {e.printStackTrace();}
    }

    public static String get(String path) {return config.getString(path);}
}
