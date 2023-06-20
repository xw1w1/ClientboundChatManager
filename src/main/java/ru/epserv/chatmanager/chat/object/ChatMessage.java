package ru.epserv.chatmanager.chat.object;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ChatMessage {
    private int id;
    private @NotNull Component content;
    private final boolean deletable;
    private boolean restorable;
    private boolean deleted = false;

    public ChatMessage(int id, Component content, boolean deletable, boolean restorable) {
        this.id = id;
        this.content = content;

        this.deletable = deletable;
        this.restorable = restorable;
    }
    public void send(@NotNull ClientPlayerEntity player) {
        player.sendMessage(this.render());
    }
    public final int getID() {
        return this.id;
    }
    public final boolean isDeleted() {
        return this.deleted;
    }
    public final boolean isDeletable() {
        return this.deletable;
    }
    public final boolean isRestorable() {
        return this.restorable;
    }
    public final void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public @NotNull Component button() {
        Component delete = Component.text("-").color(NamedTextColor.RED)
                .clickEvent(ClickEvent.runCommand("/delete-message " + this.getID()));
        Component restore = Component.text("+").color(NamedTextColor.GREEN)
                .clickEvent(ClickEvent.runCommand("/restore-message " + this.getID()));
        return this.isDeleted() ? restore : delete;
    }
    public @NotNull Component render() {
        Component message = this.isDeleted() ?
                Component.join(JoinConfiguration.noSeparators(), new Component[]{button(), Component.space(), Component.text("<< message deleted >>").color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC)})
                : Component.join(JoinConfiguration.noSeparators(), new Component[]{button(), Component.space(), this.content});
        return message;
    }
}
