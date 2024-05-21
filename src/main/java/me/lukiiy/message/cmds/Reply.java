package me.lukiiy.message.cmds;

import me.lukiiy.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Reply implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            commandSender.sendRichMessage(Message.get("usage") + "/r <msg>");
            return true;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendRichMessage("§cOnly players can use this command.");
            return true;
        }
        Player player = (Player) commandSender;

        Player to = Bukkit.getPlayer(Message.replyData.get(player.getUniqueId()));
        if (to == null) {
            commandSender.sendRichMessage(Message.get("notfound"));
            return true;
        }

        Msg.message(commandSender, to, String.join(" ", strings));
        return true;
    }
}
