package com.thuisapp;

import com.thuisapp.event.WebhookEvent;
import com.thuisapp.model.PlaySessionStateNotification;
import com.thuisapp.model.Webhook;
import com.thuisapp.util.MqttUtil;
import com.thuisapp.util.PlexUtil;
import org.eclipse.microprofile.config.Config;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Optional.ofNullable;

@Singleton
public class MqttEndpoint implements MqttCallbackExtended {
	private static final Logger logger = Logger.getLogger(MqttEndpoint.class.getName());

	@Inject
	private MqttUtil mqttUtil;

	@Inject
	private PlexUtil plexUtil;

	@Inject
	private Config config;

	private IMqttAsyncClient client;
	private Map<String, String> sessions = new HashMap<>();

	public void onWebhook(@ObservesAsync Webhook webhook) {
		webhook.getEvent().getPlayState().ifPresent(playState -> publish(webhook.getPlayer().getTitle(), "/state", playState));
	}

	public void onPlay(@ObservesAsync @WebhookEvent(value = Webhook.Event.PLAY) Webhook webhook) {
		// Not ideal way of linking webhooks to websocket
		sessions.put(webhook.getMetadata().getRatingKey(), webhook.getPlayer().getTitle());

		publish(webhook.getPlayer().getTitle(), "/state/title", webhook.getMetadata().getTitle());
		publish(webhook.getPlayer().getTitle(), "/state/summary", webhook.getMetadata().getSummary());
		publish(webhook.getPlayer().getTitle(), "/state/art", plexUtil.buildUri(webhook.getMetadata().getArt()));
		publish(webhook.getPlayer().getTitle(), "/state/thumb", plexUtil.buildUri(webhook.getMetadata().getThumb()));
		publish(webhook.getPlayer().getTitle(), "/state/grandparentTitle", webhook.getMetadata().getGrandparentTitle());
		publish(webhook.getPlayer().getTitle(), "/state/grandparentThumb", plexUtil.buildUri(webhook.getMetadata().getGrandparentThumb()));
		// TODO: grab duration from the API
		// publish(webhook.player.title, "/state/duration", webhook.metadata.duration);
	}

	public void onNotification(@ObservesAsync PlaySessionStateNotification notification) {
		ofNullable(sessions.get(notification.getRatingKey()))
				.ifPresent(playerTitle -> publish(playerTitle, "/state/viewOffset", notification.getViewOffset() + ""));
	}


	public void onStop(@ObservesAsync @WebhookEvent(value = Webhook.Event.STOP) Webhook webhook) {
		sessions.remove(webhook.getMetadata().getRatingKey());

		publish(webhook.getPlayer().getTitle(), "/state/title", "");
		publish(webhook.getPlayer().getTitle(), "/state/summary", "");
		publish(webhook.getPlayer().getTitle(), "/state/art", "");
		publish(webhook.getPlayer().getTitle(), "/state/thumb", "");
		publish(webhook.getPlayer().getTitle(), "/state/grandparentTitle", "");
		publish(webhook.getPlayer().getTitle(), "/state/grandparentThumb", "");
		publish(webhook.getPlayer().getTitle(), "/state/duration", "");
		publish(webhook.getPlayer().getTitle(), "/state/viewOffset", "");
	}

	public void init(@Observes @Initialized(ApplicationScoped.class) Object event) {
		try {
			client = new MqttAsyncClient(
					mqttUtil.buildMqttUri(),
					config.getValue("mqtt.client_id", String.class),
					new MemoryPersistence()
			);
			MqttConnectOptions options = new MqttConnectOptions();
			config.getOptionalValue("mqtt.username", String.class).ifPresent(options::setUserName);
			config.getOptionalValue("mqtt.password", String.class).map(String::toCharArray).ifPresent(options::setPassword);
			options.setWill("connected", "0".getBytes(), 2, true);
			client.setCallback(this);
			client.connect(options).waitForCompletion();
		} catch (MqttException e) {
			logger.log(Level.WARNING, "Could not connect to MQTT broker", e);
		}
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		publish("", "connected", "1");
	}

	@Override
	public void connectionLost(Throwable cause) {
		publish("", "connected", "0");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// Do nothing
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// Do nothing
	}

	@PreDestroy
	public void destroy() {
		try {
			client.disconnect();
		} catch (MqttException e) {
			logger.log(Level.WARNING, "Could not disconnect MQTT broker", e);
		}
	}

	private void publish(String subject, String topic, String payload) {
		try {
			MqttMessage message = new MqttMessage(payload.getBytes());
			message.setRetained(true);
			client.publish(mqttUtil.buildTopic(subject, topic), message);
		} catch (MqttException e) {
			logger.log(Level.WARNING, "Could not send MQTT message", e);
		}
	}

}
