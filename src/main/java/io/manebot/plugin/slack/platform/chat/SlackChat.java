package io.manebot.plugin.slack.platform.chat;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackPreparedMessage;
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply;
import io.manebot.chat.Chat;
import io.manebot.chat.ChatMessage;
import io.manebot.chat.Community;
import io.manebot.chat.TextFormat;
import io.manebot.platform.Platform;

import io.manebot.platform.PlatformUser;
import io.manebot.plugin.slack.platform.SlackPlatformConnection;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SlackChat implements Chat {
    private final SlackPlatformConnection connection;
    private final SlackChannel slackChannel;

    public SlackChat(SlackPlatformConnection connection, SlackChannel slackChannel) {
        this.connection = connection;
        this.slackChannel = slackChannel;
    }

    @Override
    public Platform getPlatform() {
        return connection.getPlatform();
    }

    @Override
    public String getId() {
        return slackChannel.getId();
    }

    @Override
    public void setName(String name) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isConnected() {
        return connection.getPlatform().isConnected();
    }

    @Override
    public void removeMember(String platformId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addMember(String platformId) {
        throw new UnsupportedOperationException();
    }

    @Override public Community getCommunity() {
        return null;
    }

    @Override
    public Collection<ChatMessage> getLastMessages(int max) {
        return null;
    }

    @Override
    public Collection<PlatformUser> getPlatformUsers() {
        return slackChannel.getMembers().stream().map(connection::getPlatformUser).collect(Collectors.toList());
    }

    @Override
    public boolean isPrivate() {
        return slackChannel.isDirect();
    }

    @Override
    public boolean canChangeTypingStatus() {
        return false;
    }

    @Override
    public void setTyping(boolean typing) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isTyping() {
        return false;
    }

    public void setTopic(String topic) {
        throw new UnsupportedOperationException();
    }

    public String getTopic() {
        return slackChannel.getTopic();
    }

    @Override
    public TextFormat getFormat() {
        return SlackTextFormat.INSTANCE;
    }

    @Override
    public Collection<ChatMessage> sendMessage(Consumer<ChatMessage.Builder> function) {
        SlackChatMessage.MessageBuilder builder = new SlackChatMessage.MessageBuilder(connection.getSelf(), this);
        function.accept(builder);

        SlackPreparedMessage preparedMessage = builder.build();

        connection.getSession().sendMessage(slackChannel, preparedMessage);

        SlackSentChatMessage sentChatMessage = new SlackSentChatMessage(
                        connection,
                        new SlackChatSender(connection.getSelf(), this),
                        preparedMessage
        );

        return Collections.singletonList(sentChatMessage);
    }

    @Override
    public boolean canSendEmbeds() {
        return true;
    }

    @Override
    public boolean canSendEmoji() {
        return true;
    }

    @Override
    public int getDefaultPageSize() {
        return 5;
    }
}
