package io.manebot.plugin.slack.platform.chat;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackPreparedMessage;
import io.manebot.chat.*;
import io.manebot.plugin.slack.platform.user.SlackPlatformUser;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class SlackChatMessage extends AbstractChatMessage {
    public SlackChatMessage(ChatSender sender, Date date) {
	super(sender, date);
    }

    private static final String escape(String text) {
	return SlackTextFormat.INSTANCE.escape(text);
    }

    private static class ChatEmbed implements io.manebot.chat.ChatEmbed {
	private final SlackAttachment embed;

	private ChatEmbed(SlackAttachment embed) {
	    this.embed = embed;
	}

	@Override
	public Color getColor() {
	    return Color.getColor(embed.getColor());
	}

	@Override
	public String getTitle() {
	    return embed.getTitle();
	}

	@Override
	public String getDescription() {
	    return embed.getText();
	}

	@Override
	public Collection<Field> getFields() {
	    return embed.getFields().stream()
			    .map(field -> new Field(field.getTitle(), field.getValue(), field.isShort()))
			    .collect(Collectors.toList());
	}

	@Override
	public String getFooter() {
	    return embed.getFooter();
	}

	@Override
	public ImageElement getThumbnail() {
	    try {
		return embed.getImageUrl() != null ?
				new RemoteImage(URI.create(embed.getImageUrl()).toURL()) :
				null;
	    } catch (MalformedURLException e) {
		throw new IllegalArgumentException(e);
	    }
	}
    }

    /**
     * Slack EmbedBuilder.  This translates well-formatted text chat intents to Slack intents, supporting
     * text styles (bold, italics) along the way.  I've already stubbed out certain fields (footer, content, etc) that
     * don't appear to support styles.  At the moment, that seems to be all fields in an embed.
     */
    public static class EmbedBuilder implements io.manebot.chat.ChatEmbed.Builder {
	private final Chat chat;
	private final SlackAttachment attachment;

	public EmbedBuilder(Chat chat, SlackAttachment attachment) {
	    this.chat = chat;
	    this.attachment = attachment;
	}

	@Override
	public Chat getChat() {
	    return chat;
	}

	@Override
	public io.manebot.chat.ChatEmbed.Builder thumbnail(io.manebot.chat.ChatEmbed.ImageElement imageElement) {
	    if (imageElement instanceof io.manebot.chat.ChatEmbed.RemoteImage)
		attachment.setImageUrl(((io.manebot.chat.ChatEmbed.RemoteImage) imageElement).getUrl().toExternalForm());
	    else
		throw new UnsupportedOperationException("image element is not remote (Discord only supports URLs)");

	    return this;
	}

	@Override
	public io.manebot.chat.ChatEmbed.Builder descriptionRaw(String message) {
	    attachment.setText(message);
	    return this;
	}

	@Override
	public io.manebot.chat.ChatEmbed.Builder title(Consumer<TextBuilder> value) {
	    TextBuilder builder = new DefaultTextBuilder(getChat(), SlackTextFormat.INSTANCE);
	    value.accept(builder);
	    return titleRaw(builder.build());
	}

	@Override
	public io.manebot.chat.ChatEmbed.Builder titleRaw(String title) {
	    attachment.setTitle(title);
	    return this;
	}

	@Override
	public io.manebot.chat.ChatEmbed.Builder footerRaw(String footer) {
	    attachment.setFooter(footer);
	    return this;
	}

	@Override
	public io.manebot.chat.ChatEmbed.Builder footer(Consumer<TextBuilder> textBuilder) {
	    TextBuilder builder = new DefaultTextBuilder(getChat(), TextFormat.BASIC);
	    textBuilder.accept(builder);
	    return footerRaw(builder.build());
	}

	@Override
	public io.manebot.chat.ChatEmbed.Builder timestamp(java.util.Date date) {
	    attachment.setTimestamp(date.getTime());
	    return this;
	}

	@Override
	public io.manebot.chat.ChatEmbed.Builder timestamp(Instant instant) {
	    attachment.setTimestamp(instant.getEpochSecond());
	    return this;
	}

	@Override
	public io.manebot.chat.ChatEmbed.Builder color(Color color) {
	    attachment.setColor(color.toString());
	    return this;
	}

	@Override
	public io.manebot.chat.ChatEmbed.Builder field(String name, Consumer<TextBuilder> value) {
	    TextBuilder builder = new DefaultTextBuilder(getChat(), TextFormat.BASIC);
	    value.accept(builder);
	    return fieldRaw(name, builder.build());
	}

	@Override
	public io.manebot.chat.ChatEmbed.Builder field(String name, Consumer<TextBuilder> value, boolean inline) {
	    TextBuilder builder = new DefaultTextBuilder(getChat(), TextFormat.BASIC);
	    value.accept(builder);
	    return fieldRaw(name, builder.build(), inline);
	}

	@Override
	public io.manebot.chat.ChatEmbed.Builder fieldRaw(String name, String value) {
	    return field(name, value, false);
	}

	@Override
	public io.manebot.chat.ChatEmbed.Builder fieldRaw(String name, String value, boolean inline) {
	    if (name == null && value == null)
		attachment.addField("", "", inline);
	    else
		attachment.addField(name, value, inline);

	    return this;
	}
    }

    static class MessageBuilder implements ChatMessage.Builder {
	private final SlackPlatformUser user;
	private final SlackChat channel;

	private final SlackPreparedMessage.Builder builder;

	public MessageBuilder(SlackPlatformUser user,
			SlackChat channel) {
	    this.user = user;
	    this.channel = channel;
	    this.builder = new SlackPreparedMessage.Builder();
	}

	@Override
	public SlackPlatformUser getUser() {
	    return user;
	}

	@Override
	public SlackChat getChat() {
	    return channel;
	}

	@Override
	public Builder rawMessage(String message) {
	    builder.withMessage(message);
	    return this;
	}

	@Override
	public ChatMessage.Builder embed(Consumer<io.manebot.chat.ChatEmbed.Builder> function)
			throws UnsupportedOperationException {
	    SlackAttachment attachment = new SlackAttachment();
	    function.accept(new EmbedBuilder(channel, attachment));
	    this.builder.addAttachment(attachment);
	    return this;
	}

	public SlackPreparedMessage build() {
	    return builder.build();
	}
    }
}