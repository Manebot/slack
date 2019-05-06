package io.manebot.plugin.slack;

import io.manebot.plugin.Plugin;
import io.manebot.plugin.PluginException;
import io.manebot.plugin.PluginType;
import io.manebot.plugin.java.PluginEntry;
import io.manebot.plugin.slack.platform.SlackPlatformConnection;

public class Entry implements PluginEntry {
    @Override
    public void instantiate(Plugin.Builder builder) throws PluginException {
        builder.setType(PluginType.FEATURE);

        builder.addPlatform(platformBuilder -> {
            platformBuilder.setId("slack").setName("Slack");

            final SlackPlatformConnection platformConnection = new SlackPlatformConnection(
                    platformBuilder.getPlatform(),
                    platformBuilder.getPlugin()
            );

            platformBuilder.setConnection(platformConnection);
        });
    }
}
