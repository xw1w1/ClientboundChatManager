package ru.epserv.chatmanager.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import ru.epserv.chatmanager.ChatManager;
import ru.epserv.chatmanager.chat.object.ChatMessage;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;

public class DeleteMessage {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("delete-message")
                .then(CommandManager.argument("id", integer()).executes(DeleteMessage::handle)));
    }

    private static int handle(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        int id = ctx.getArgument("id", Integer.class);
        ChatMessage message = ChatManager.getChatHistory().getMessage(id);
        if (message != null && !message.isDeleted() && message.getID() == id) {
            ChatManager.getChatHistory().removeMessage(id);
            return 1;
        } else {
            return -1;
        }
    }

}
