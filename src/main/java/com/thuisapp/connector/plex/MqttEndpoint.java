package com.thuisapp.connector.plex;

import com.thuisapp.connector.plex.event.WebhookEvent;
import com.thuisapp.connector.plex.model.PlaySessionStateNotification;
import com.thuisapp.connector.plex.model.Webhook;
import com.thuisapp.mqtt.MqttClient;
import com.thuisapp.connector.plex.util.PlexUtil;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Singleton
public class MqttEndpoint {

	@Inject
	MqttClient client;

	@Inject
	PlexUtil plexUtil;

	private Map<String, String> sessions = new HashMap<>();

	private void onStartup(@Observes StartupEvent event) {
		// Force eager instantiation
	}

	public void onWebhook(@ObservesAsync Webhook webhook) {
		webhook.getEvent().getPlayState().ifPresent(playState -> client.publish(webhook.getPlayer().getTitle(), "/state", playState));
	}

	public void onPlay(@ObservesAsync @WebhookEvent(value = Webhook.Event.PLAY) Webhook webhook) {
		// Not ideal way of linking webhooks to websocket
		sessions.put(webhook.getMetadata().getRatingKey(), webhook.getPlayer().getTitle());

		client.publish(webhook.getPlayer().getTitle(), "/state/title", webhook.getMetadata().getTitle());
		client.publish(webhook.getPlayer().getTitle(), "/state/summary", webhook.getMetadata().getSummary());
		client.publish(webhook.getPlayer().getTitle(), "/state/art", plexUtil.buildUri(webhook.getMetadata().getArt()));
		client.publish(webhook.getPlayer().getTitle(), "/state/thumb", plexUtil.buildUri(webhook.getMetadata().getThumb()));
		client.publish(webhook.getPlayer().getTitle(), "/state/grandparentTitle", webhook.getMetadata().getGrandparentTitle());
		client.publish(webhook.getPlayer().getTitle(), "/state/grandparentThumb", plexUtil.buildUri(webhook.getMetadata().getGrandparentThumb()));
		// TODO: grab duration from the API
		// publish(webhook.player.title, "/state/duration", webhook.metadata.duration);
	}

	public void onNotification(@ObservesAsync PlaySessionStateNotification notification) {
		ofNullable(sessions.get(notification.getRatingKey()))
				.ifPresent(playerTitle -> client.publish(playerTitle, "/state/viewOffset", notification.getViewOffset() + ""));
	}


	public void onStop(@ObservesAsync @WebhookEvent(value = Webhook.Event.STOP) Webhook webhook) {
		sessions.remove(webhook.getMetadata().getRatingKey());

		client.publish(webhook.getPlayer().getTitle(), "/state/title", "");
		client.publish(webhook.getPlayer().getTitle(), "/state/summary", "");
		client.publish(webhook.getPlayer().getTitle(), "/state/art", "");
		client.publish(webhook.getPlayer().getTitle(), "/state/thumb", "");
		client.publish(webhook.getPlayer().getTitle(), "/state/grandparentTitle", "");
		client.publish(webhook.getPlayer().getTitle(), "/state/grandparentThumb", "");
		client.publish(webhook.getPlayer().getTitle(), "/state/duration", "");
		client.publish(webhook.getPlayer().getTitle(), "/state/viewOffset", "");
	}

}
