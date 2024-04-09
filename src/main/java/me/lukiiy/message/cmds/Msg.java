package me.lukiiy.message.cmds;

import me.lukiiy.message.Message;
import net.kyori.adventure.text.Component;
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
        if (strings.length < 1) {
            commandSender.sendMessage(Message.get("msgs.usage") + "/msg <player> <msg>");
            return true;
        }

        Player to = Bukkit.getPlayer(strings[0]);
        if (to == null) {
            commandSender.sendMessage(Message.get("msgs.notfound"));
            return true;
        }

        message(commandSender, to, String.join(" ", strings).substring(strings[0].length() + 1));
        return true;
    }

    public static void message(CommandSender sender, Player receiver, String content) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (Message.getBoolean("canSeeCheck") && !p.canSee(receiver)) {
                sender.sendMessage(Message.get("msgs.notfound"));
                return;
            }
            if (!Message.getBoolean("selfMsg") && p.equals(receiver)) {
                sender.sendMessage(Message.get("msgs.self"));
                return;
            }
            Message.replyData.put(p.getUniqueId(), receiver.getUniqueId());
            Message.replyData.put(receiver.getUniqueId(), p.getUniqueId());
        }

        String toMsg = Message.get("msgs.to").replace("%p", receiver.getName());
        String fromMsg = Message.get("msgs.from").replace("%p", sender.getName());

        Component formatted = Component.text(content);
        if (Message.getBoolean("allowFormatting")) formatted = LegacyComponentSerializer.legacyAmpersand().deserialize(content);

        sender.sendMessage(Component.text(toMsg).color(Message.mainColor).append(formatted));
        receiver.sendMessage(Component.text(fromMsg).color(Message.mainColor).append(formatted));
        receiver.playSound(receiver, Sound.ENTITY_CHICKEN_EGG, SoundCategory.PLAYERS, 1, 1);
    }
}
