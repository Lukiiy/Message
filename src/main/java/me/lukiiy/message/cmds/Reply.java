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

import java.util.UUID;

public class Reply implements CommandExecutor {
    private final Message instance;

    public Reply(Message instance) {
        this.instance = instance;
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Audience senderAudience = instance.getAudience().sender(commandSender);

        if (strings.length == 0) {
            senderAudience.sendMessage(instance.formattedConfigMessage("usage").append(Component.text("/r <msg>")));
            return true;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        UUID replyUUID = instance.getLastReply((Player) commandSender);
        if (replyUUID == null || Bukkit.getPlayer(replyUUID) == null) {
            senderAudience.sendMessage(instance.formattedConfigMessage("notfound"));
            return true;
        }
        Player to = Bukkit.getPlayer(replyUUID);

        instance.message(commandSender, to, String.join(" ", strings));
        return true;
    }
}
