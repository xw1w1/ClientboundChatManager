package ru.epserv.chatmanager.listeners;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import ru.epserv.chatmanager.ChatManager;
import ru.epserv.chatmanager.chat.ClientboundChatHistory;
import ru.epserv.chatmanager.chat.object.ChatMessage;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

public class ChatListener implements ClientReceiveMessageEvents.Chat, ClientReceiveMessageEvents.Game{
    @Override
    public void onReceiveChatMessage(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) {
        ChatMessage newMessage = null;
        if (MinecraftClient.getInstance().player != null) {
            newMessage = new ChatMessage(
                    ClientboundChatHistory.pointer,
                    message.asComponent(), true, false
            );
        }
        if (message.getString().contains("[")) {
            ChatManager.getChatHistory().resend();
        }
        handle(newMessage);
    }

    @Override
    public void onReceiveGameMessage(Text message, boolean overlay) {
        ChatMessage newMessage = null;
        if (MinecraftClient.getInstance().player != null) {
            newMessage = new ChatMessage(
                    ClientboundChatHistory.pointer,
                    message.asComponent(), true, false
            );
        }
        if (message.getString().contains("[")) {
            ChatManager.getChatHistory().resend();
        }
        handle(newMessage);
    }

    public void handle(ChatMessage message) {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        ChatManager.getChatHistory().putMessage(message);
                        ClientboundChatHistory.pointer = ClientboundChatHistory.pointer + 1;
                        ChatManager.getChatHistory().resend();
                    }
                },
                1
        );
    }
}
