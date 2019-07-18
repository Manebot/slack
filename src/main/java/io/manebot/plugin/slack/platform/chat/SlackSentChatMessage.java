package io.manebot.plugin.slack.platform.chat;

import com.ullink.slack.simpleslackapi.SlackPreparedMessage;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import io.manebot.chat.AbstractChatMessage;
import io.manebot.chat.ChatEmbed;
import io.manebot.chat.ChatMessage;
import io.manebot.platform.PlatformUser;
import io.manebot.plugin.slack.platform.SlackPlatformConnection;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.function.Consumer;

public class SlackSentChatMessage extends SlackChatMessage {
    private final SlackPlatformConnection connection;
    private final SlackPreparedMessage message;
    private final SlackChatSender sender;

    public SlackSentChatMessage(SlackPlatformConnection connection,
		    SlackChatSender sender,
		    SlackPreparedMessage message) {
	super(sender, new Date(System.currentTimeMillis()));

	this.sender = sender;
	this.connection = connection;
	this.message = message;
    }

    @Override
    public SlackChatSender getSender() {
	return sender;
    }

    @Override
    public Collection<PlatformUser> getMentions() {
	return Collections.emptyList();
    }

    @Override public String getMessage() {
	return getRawMessage();
    }

    @Override
    public String getRawMessage() {
	return message.getMessage();
    }

    @Override
    public Collection<io.manebot.chat.ChatEmbed> getEmbeds() {
	return Collections.emptyList();
    }

    @Override
    public void delete() throws UnsupportedOperationException {
	throw new UnsupportedOperationException();
    }

    @Override
    public ChatMessage edit(Consumer<Builder> function) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean wasEdited() {
	return false;
    }

    @Override
    public java.util.Date getEditedDate() {
	return null;
    }
}