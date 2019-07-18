package io.manebot.plugin.slack.platform.chat;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackPreparedMessage;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import io.manebot.chat.*;
import io.manebot.platform.PlatformUser;
import io.manebot.plugin.slack.platform.SlackPlatformConnection;
import io.manebot.plugin.slack.platform.user.SlackPlatformUser;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SlackReceivedChatMessage extends SlackChatMessage {
    private final SlackPlatformConnection connection;
    private final SlackMessagePosted message;
    private final SlackChatSender sender;

    public SlackReceivedChatMessage(SlackPlatformConnection connection,
                    SlackChatSender sender,
                    SlackMessagePosted message) {
        super(sender, new Date((long)Math.floor(Double.parseDouble(message.getTimestamp()) * 1000D)));

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
        return message.getMessageContent();
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