package me.lukiiy.message;

import lombok.Getter;
import me.lukiiy.message.cmds.Msg;
import me.lukiiy.message.cmds.Reply;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Message extends JavaPlugin {
    @Getter private static Message instance;

    private FileConfiguration config;

    public static HashMap<UUID, UUID> replyData = new HashMap<>();
    public static BukkitAudiences bukkitAudience;

    public static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        instance = this;
        setupConfig();

        bukkitAudience = BukkitAudiences.create(this);

        getCommand("msg").setExecutor(new Msg());
        getCommand("r").setExecutor(new Reply());
    }

    @Override
    public void onDisable() {
        bukkitAudience.close();
    }

    // Config
    public void setupConfig() {
        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
    }

    public static FileConfiguration configFile() {return instance.config;}

    public static Component msg(String path) {return miniMessage.deserialize(configFile().getString("msgs." + path, "<red><i>Message not set. :(").replace("§", ""));}
    public static boolean getBool(String path) {return configFile().getBoolean(path);}

    public static void message(CommandSender sender, Player receiver, String content) {
        Audience senderAudience = Message.bukkitAudience.sender(sender);

        if (sender instanceof Player p) {
            if (!p.canSee(receiver) && getBool("visibilityCheck")) {
                senderAudience.sendMessage(msg("notfound"));
                return;
            }
            if (p.equals(receiver) && !getBool("selfMsg")) {
                senderAudience.sendMessage(msg("self"));
                return;
            }
            Message.replyData.put(p.getUniqueId(), receiver.getUniqueId());
            Message.replyData.put(receiver.getUniqueId(), p.getUniqueId());
        }

        Component toMsg = msg("to").replaceText(TextReplacementConfig.builder().matchLiteral("%p").replacement(receiver.getName()).build());
        Component fromMsg = msg("from").replaceText(TextReplacementConfig.builder().matchLiteral("%p").replacement(sender.getName()).build());

        Component formatted = getBool("formatting") ? LegacyComponentSerializer.legacyAmpersand().deserialize(content) : Component.text(content);

        senderAudience.sendMessage(toMsg.append(formatted));
        Message.bukkitAudience.player(receiver).sendMessage(fromMsg.append(formatted));

        if (getBool("sfx")) receiver.playSound(receiver, Sound.ENTITY_CHICKEN_EGG, 1, 1);
    }
}
