package me.lukiiy.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class MessageCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length < 1) {
            commandSender.sendMessage(Component.text(Message.get("msgs.usage") + "/msg <player> <msg>"));
            return true;
        }

        Player to = Bukkit.getPlayer(strings[0]);
        if (to == null) {
            commandSender.sendMessage(Component.text(Message.get("msgs.notfound")));
            return true;
        }
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (Boolean.parseBoolean(Message.get("canSeeCheck")) && !p.canSee(to)) {
                commandSender.sendMessage(Component.text(Message.get("msgs.notfound")));
                return true;
            }
            setReplyData(p, to);
            setReplyData(to, p);
        }

        String toMsg = Message.get("msgs.to").replace("%p", strings[0]);
        String fromMsg = Message.get("msgs.from").replace("%p", commandSender.getName());
        Component parts = LegacyComponentSerializer.legacyAmpersand().deserialize(String.join(" ", strings).substring(strings[0].length() + 1));

        commandSender.sendMessage(Component.text(toMsg).color(Message.mainColor).append(parts));
        to.sendMessage(Component.text(fromMsg).color(Message.mainColor).append(parts));
        to.playSound(to, Sound.ENTITY_CHICKEN_EGG, SoundCategory.PLAYERS, 1, 1);
        return true;
    }

    private void setReplyData(Player p1, Player p2) {p1.setMetadata("reply", new FixedMetadataValue(Message.getInstance(), p2.getUniqueId().toString()));}
}
