package me.lukiiy.message.cmds;

import me.lukiiy.message.Message;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Msg implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Component defUsage = Message.msg("usage").append(Component.text("/msg <player> <msg>"));
        Audience senderAudience = Message.bukkitAudience.sender(commandSender);

        if (strings.length == 0) {
            senderAudience.sendMessage(defUsage);
            return true;
        }

        if (strings[0].equals("-reload")) {
            if (!commandSender.hasPermission("message.reload")) {
                senderAudience.sendMessage(defUsage);
                return true;
            }

            Message.getInstance().reloadConfig();
            senderAudience.sendMessage(Message.msg("reload"));
            return true;
        }

        Player to = Bukkit.getPlayer(strings[0]);
        if (to == null) {
            senderAudience.sendMessage(Message.msg("notfound"));
            return true;
        }

        Message.message(commandSender, to, String.join(" ", strings).substring(strings[0].length() + 1));
        return true;
    }
}
