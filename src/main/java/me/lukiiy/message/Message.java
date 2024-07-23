package me.lukiiy.message;

import me.lukiiy.message.cmds.Msg;
import me.lukiiy.message.cmds.Reply;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Message extends JavaPlugin {
    public static Message inst;
    public static FileConfiguration config;
    public static HashMap<UUID, UUID> replyData = new HashMap<>();
    public static BukkitAudiences bukkitAudience;
    public static MiniMessage miniMessage = MiniMessage.miniMessage();
    // static.

    @Override
    public void onEnable() {
        inst = this;
        bukkitAudience = BukkitAudiences.create(this);

        getCommand("msg").setExecutor(new Msg());
        getCommand("r").setExecutor(new Reply());

        saveDefaultConfig();
        load();
    }

    @Override
    public void onDisable() {
        bukkitAudience.close();
    }

    // Config – I don't want to deal with this
    public void load() {
        getConfig().options().copyDefaults(true);
        config = getConfig();
        saveConfig();
    }
    public static String get(String p) {return config.getString("msgs." + p, "<red><i>Message not set. :(").replace("§", "");}
    public static boolean getBool(String path) {return config.getBoolean(path);}

    // Extra stuff ;)
    public static void sendRich(Audience aud, String txt) {aud.sendMessage(miniMessage.deserialize(txt));}

    public static void message(CommandSender sender, Player receiver, String content) {
        Audience s = Message.bukkitAudience.sender(sender);

        if (sender instanceof Player p) {
            if (!p.canSee(receiver) && Message.getBool("visibilityCheck")) {
                sendRich(s, Message.get("notfound"));
                return;
            }
            if (p.equals(receiver) && !Message.getBool("selfMsg")) {
                sendRich(s, Message.get("self"));
                return;
            }
            Message.replyData.put(p.getUniqueId(), receiver.getUniqueId());
            Message.replyData.put(receiver.getUniqueId(), p.getUniqueId());
        }

        String toMsg = Message.get("to").replace("%p", receiver.getName());
        String fromMsg = Message.get("from").replace("%p", sender.getName());
        Component formatted = Message.getBool("formatting") ? LegacyComponentSerializer.legacyAmpersand().deserialize(content) : Component.text(content);

        s.sendMessage(Message.miniMessage.deserialize(toMsg).append(formatted));
        Message.bukkitAudience.player(receiver).sendMessage(Message.miniMessage.deserialize(fromMsg).append(formatted));
        receiver.playSound(receiver, Sound.ENTITY_CHICKEN_EGG, SoundCategory.PLAYERS, 1, 1);
    }
}
