package me.lukiiy.message.cmds;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.lukiiy.message.Message;

public class Msg {
    public LiteralArgumentBuilder<CommandSourceStack> main = Commands.literal("message")
        .then(Commands.literal("-reload")
                .requires(source -> source.getSender().hasPermission("message.reload"))
                .executes(ctx -> {
                    Message.getInstance().reloadConfig();
                    ctx.getSource().getSender().sendMessage(Message.getInstance().formattedConfigMessage("reload"));
                    return Command.SINGLE_SUCCESS;
                })
        )

        .then(Commands.argument("player", ArgumentTypes.player()).then(Commands.argument("message", StringArgumentType.greedyString())
                    .executes(ctx -> {
                        Message.getInstance().message(ctx.getSource().getSender(), ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst(), StringArgumentType.getString(ctx, "message"));
                        return Command.SINGLE_SUCCESS;
                    })
            ));

    public LiteralCommandNode<CommandSourceStack> register() {
        return main.build();
    }
}
