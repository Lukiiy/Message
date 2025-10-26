package me.lukiiy.message.cmds;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import me.lukiiy.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Reply {
    public LiteralArgumentBuilder<CommandSourceStack> main = Commands.literal("reply").requires(source -> source.getSender() instanceof Player)
            .then(Commands.argument("message", StringArgumentType.greedyString())
                    .executes(ctx -> {
                        var sender = (Player) ctx.getSource().getSender();

                        UUID replyUUID = Message.getInstance().getLastReply(sender);
                        if (replyUUID == null || Bukkit.getPlayer(replyUUID) == null) throw new SimpleCommandExceptionType(MessageComponentSerializer.message().serialize(Message.getInstance().formattedConfigMessage("notfound"))).create();
                        Player target = Bukkit.getPlayer(replyUUID);

                        Message.getInstance().message(sender, target, StringArgumentType.getString(ctx, "message"));
                        return Command.SINGLE_SUCCESS;
                    })
            );

    public LiteralCommandNode<CommandSourceStack> register() {
        return main.build();
    }
}
