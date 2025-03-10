package me.lukiiy.message;

import me.lukiiy.message.cmds.Msg;
import me.lukiiy.message.cmds.Reply;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public final class Message extends JavaPlugin {
    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private final HashMap<UUID, UUID> replyData = new HashMap<>();
    public BukkitAudiences audience;

    @Override
    public void onEnable() {
        setupConfig();

        audience = BukkitAudiences.create(this);

        getCommand("msg").setExecutor(new Msg(this));
        getCommand("r").setExecutor(new Reply(this));
    }

    @Override
    public void onDisable() {
        audience.close();
    }

    public static Message getInstance() {
        return JavaPlugin.getPlugin(Message.class);
    }

    public BukkitAudiences getAudience() {
        return audience;
    }

    // Config
    public void setupConfig() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    // API
    public @NotNull Component formattedConfigMessage(String path) {
        return MINI_MESSAGE.deserialize(getConfig().getString("msgs." + path, "<red><i>Message not set.").replace("§", ""));
    }

    public void message(@NotNull CommandSender sender, @NotNull Player receiver, @NotNull String content) {
        Audience senderAudience = audience.sender(sender);
        Audience receiverAudience = audience.player(receiver);

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (getConfig().getBoolean("visibilityCheck") && !player.canSee(receiver)) {
                senderAudience.sendMessage(formattedConfigMessage("notfound"));
                return;
            }

            if (!getConfig().getBoolean("selfMsg") && player.equals(receiver)) {
                senderAudience.sendMessage(formattedConfigMessage("self"));
                return;
            }

            replyData.put(player.getUniqueId(), receiver.getUniqueId());
            replyData.put(receiver.getUniqueId(), player.getUniqueId());
        }

        Component toMsg = formattedConfigMessage("to").replaceText(TextReplacementConfig.builder().matchLiteral("%p").replacement(receiver.getName()).build());
        Component fromMsg = formattedConfigMessage("from").replaceText(TextReplacementConfig.builder().matchLiteral("%p").replacement(sender.getName()).build());

        Component formatted = getConfig().getBoolean("formatting") ? LegacyComponentSerializer.legacyAmpersand().deserialize(content) : Component.text(content);

        senderAudience.sendMessage(toMsg.append(formatted));
        receiverAudience.sendMessage(fromMsg.append(formatted));

        // Sound effect
        String sfxId = getConfig().getString("sfx.id", "");
        if (!sfxId.isEmpty()) {
            Sound sfx = Sound.sound(Key.key(sfxId), Sound.Source.PLAYER, (float) getConfig().getDouble("sfx.volume", 1), (float) getConfig().getDouble("sfx.pitch", 1));
            receiverAudience.playSound(sfx);
        }
    }

    public @Nullable UUID getLastReply(Player player) {
        return replyData.get(player.getUniqueId());
    }
}
