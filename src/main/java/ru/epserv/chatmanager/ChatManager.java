package ru.epserv.chatmanager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.report.log.ChatLogEntry;
import ru.epserv.chatmanager.chat.ClientboundChatHistory;
import ru.epserv.chatmanager.commands.DeleteMessage;
import ru.epserv.chatmanager.commands.RestoreMessage;
import ru.epserv.chatmanager.listeners.ChatListener;

public class ChatManager implements ModInitializer {
	private static ClientboundChatHistory history;

	@Override
	public void onInitialize() {
		ClientReceiveMessageEvents.CHAT.register(new ChatListener());
		history = new ClientboundChatHistory();
		CommandRegistrationCallback.EVENT.register(DeleteMessage::register);
		CommandRegistrationCallback.EVENT.register(RestoreMessage::register);
	}

	public static ClientboundChatHistory getChatHistory() {
		return history;
	}
}
