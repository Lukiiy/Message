package me.lukiiy.message.cmds;

import me.lukiiy.message.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Message.Plugin.load();
        commandSender.sendMessage("§aMessage Reload complete.");
        return true;
    }
}
