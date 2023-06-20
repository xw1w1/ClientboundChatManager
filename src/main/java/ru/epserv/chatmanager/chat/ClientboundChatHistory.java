package ru.epserv.chatmanager.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import ru.epserv.chatmanager.chat.object.ChatMessage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class ClientboundChatHistory {
    public static final int HISTORY_SIZE = 128;
    public static int pointer = 0;
    private final @NotNull ReentrantLock sendLock = new ReentrantLock();
    private final LinkedList<ChatMessage> history = new LinkedList<>();

    public void putMessage(ChatMessage chatMessage) {
        LinkedList<ChatMessage> history = this.history;
        synchronized (history) {
            if (chatMessage != null) history.add(chatMessage);
        }
    }

    public void removeMessage(int id) {
        ChatMessage message = this.history.get(id);
        if (message != null && message.isDeletable() && !message.isDeleted()) {
            message.setDeleted(true);
            this.resend();
            this.ensureSize();
        }
    }

    public void restoreMessage(int id) {
        ChatMessage message = this.history.get(id);
        if (message != null && message.isDeletable() && message.isDeleted()) {
            message.setDeleted(false);
            this.resend();
            this.ensureSize();
        }
    }

    public ChatMessage getMessage(int id) {
        return this.history.get(id);
    }

    private void ensureSize() {
        LinkedList<ChatMessage> history = this.history;
        synchronized (history) {
            while (history.size() > HISTORY_SIZE) {
                history.removeFirst();
            }
        }
    }

    public synchronized void resend() {
        LinkedList<ChatMessage> history = this.history;
        synchronized (history) {
            try {
                this.sendLock.lock();

                Component[] lines = new Component[100];
                Arrays.fill(lines, Component.newline());
                MinecraftClient.getInstance().player.sendMessage(Component.join(JoinConfiguration.noSeparators(), lines));

                history.forEach(text -> text.send(MinecraftClient.getInstance().player));
            } finally {
                this.sendLock.unlock();
            }
        }
    }

    public LinkedList<ChatMessage> get() {
        return this.history;
    }

    public boolean isLocked() {
        return this.sendLock.isLocked();
    }
}
