package me.lukiiy.message;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ReplyCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length < 1) {
            commandSender.sendMessage(Component.text(Message.get("msgs.usage") + "/r <msg>"));
            return true;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cOnly players can use this command.");
            return true;
        }
        Player player = (Player) commandSender;

        List<MetadataValue> data = player.getMetadata("reply");
        if (data.isEmpty()) {
            commandSender.sendMessage(Message.get("msgs.notfound"));
            return true;
        }
        Player to = Bukkit.getPlayer((UUID) Objects.requireNonNull(data.get(0).value()));

        if (to == null) {
            commandSender.sendMessage(Component.text(Message.get("msgs.notfound")));
            return true;
        }

        Bukkit.dispatchCommand(commandSender, "msg " + to.getName() + " " + String.join(" ", strings));
        return true;
    }
}
