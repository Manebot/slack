package io.manebot.plugin.slack.platform.chat;

import io.manebot.chat.DefaultChatSender;
import io.manebot.plugin.slack.platform.user.SlackPlatformUser;

public class SlackChatSender extends DefaultChatSender {
    private final SlackPlatformUser user;
    private final SlackChat chat;

    public SlackChatSender(SlackPlatformUser user, SlackChat chat) {
	super(user, chat);

	this.user = user;
	this.chat = chat;
    }

    @Override
    public SlackPlatformUser getPlatformUser() {
	return user;
    }

    @Override
    public SlackChat getChat() {
	return chat;
    }
}