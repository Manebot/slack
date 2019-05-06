package io.manebot.plugin.slack.platform.user;

import com.ullink.slack.simpleslackapi.SlackUser;
import io.manebot.chat.Chat;
import io.manebot.platform.Platform;
import io.manebot.platform.PlatformUser;
import io.manebot.plugin.slack.platform.SlackPlatformConnection;

import java.util.Collection;

public class SlackPlatformUser implements PlatformUser {
    private final SlackPlatformConnection connection;
    private final SlackUser slackUser;

    public SlackPlatformUser(SlackPlatformConnection connection, SlackUser slackUser) {
        this.connection = connection;
        this.slackUser = slackUser;
    }

    public SlackUser getSlackUser() {
        return slackUser;
    }

    @Override
    public Platform getPlatform() {
        return connection.getPlatform();
    }

    @Override
    public String getNickname() {
        return slackUser.getUserName();
    }

    @Override
    public String getId() {
        return slackUser.getId();
    }

    @Override
    public boolean isSelf() {
        return slackUser.isBot();
    }

    @Override
    public Collection<Chat> getChats() {
        return connection.getChatsByUser(this);
    }

    //TODO: Timezone
}
