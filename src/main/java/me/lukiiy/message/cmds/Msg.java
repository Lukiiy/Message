package me.lukiiy.message.cmds;

import me.lukiiy.message.Message;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Msg implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Audience sender = Message.bukkitAudience.sender(commandSender);
        if (strings.length == 0) {
            Message.sendRich(sender, Message.get("usage") + "/msg <player> <msg>");
            return true;
        }

        if (strings[0].equals("-reload")) {
            if (!commandSender.hasPermission("message.reload")) {
                Message.sendRich(sender, Message.get("usage") + "/msg <player> <msg>");
                return true;
            } // bruh
            Message.inst.reloadConfig();
            Message.inst.load();
            Message.sendRich(sender, Message.get("reload"));
            return true;
        }

        Player to = Bukkit.getPlayer(strings[0]);
        if (to == null) {
            Message.sendRich(sender, Message.get("notfound"));
            return true;
        }

        Message.message(commandSender, to, String.join(" ", strings).substring(strings[0].length() + 1));
        return true;
    }
}
