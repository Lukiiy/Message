package me.lukiiy.message.cmds;

import me.lukiiy.message.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Msg implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            commandSender.sendRichMessage(Message.get("usage") + "/msg <player> <msg>");
            return true;
        }

        if (strings[0].equalsIgnoreCase("-reload")) {
            if (!commandSender.hasPermission("message.reload")) {
                commandSender.sendMessage(Bukkit.permissionMessage());
                return true;
            }
            Message.inst.reloadConfig();
            Message.inst.load();
            commandSender.sendRichMessage(Message.get("reload"));
            return true;
        }

        Player to = Bukkit.getPlayer(strings[0]);
        if (to == null) {
            commandSender.sendRichMessage(Message.get("notfound"));
            return true;
        }

        message(commandSender, to, String.join(" ", strings).substring(strings[0].length() + 1));
        return true;
    }

    public static void message(CommandSender sender, Player receiver, String content) {
        if (sender instanceof Player p) {
            if (!p.canSee(receiver) && Message.getBool("visibilityCheck")) {
                sender.sendRichMessage(Message.get("notfound"));
                return;
            }
            if (p.equals(receiver) && !Message.getBool("selfMsg")) {
                sender.sendRichMessage(Message.get("self"));
                return;
            }
            Message.replyData.put(p.getUniqueId(), receiver.getUniqueId());
            Message.replyData.put(receiver.getUniqueId(), p.getUniqueId());
        }

        String toMsg = Message.get("to").replace("%p", receiver.getName());
        String fromMsg = Message.get("from").replace("%p", sender.getName());
        Component formatted = Message.getBool("formatting") ? LegacyComponentSerializer.legacyAmpersand().deserialize(content) : Component.text(content);

        sender.sendMessage(MiniMessage.miniMessage().deserialize(toMsg).append(formatted));
        receiver.sendMessage(MiniMessage.miniMessage().deserialize(fromMsg).append(formatted));
        receiver.playSound(receiver, Sound.ENTITY_CHICKEN_EGG, SoundCategory.PLAYERS, 1, 1);
    }
}
