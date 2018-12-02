package com.thuisapp;

import com.thuisapp.adapter.NotificationDecoder;
import com.thuisapp.event.PlaySessionEventLiteral;
import com.thuisapp.event.WebhookEvent;
import com.thuisapp.model.Notification;
import com.thuisapp.model.PlaySessionStateNotification;
import com.thuisapp.model.Webhook;
import com.thuisapp.util.PlexUtil;
import org.glassfish.tyrus.ext.client.java8.SessionBuilder;

import javax.annotation.PreDestroy;
import javax.enterprise.event.Event;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.asList;

@Singleton
public class WebsocketEndpoint {
	private static final Logger logger = Logger.getLogger(WebsocketEndpoint.class.getName());

	@Inject
	private PlexUtil plexUtil;

	@Inject
	private Event<PlaySessionStateNotification> playSessionEvent;

	private Session session;

	public void onPlay(@ObservesAsync @WebhookEvent(value = Webhook.Event.PLAY) Webhook webhook) {
		if (session == null) {
			try {
				session = new SessionBuilder()
						.uri(plexUtil.buildWebSocketUri())
						.clientEndpointConfig(
								ClientEndpointConfig.Builder.create()
										.decoders(asList(NotificationDecoder.class))
										.build()
						)
						.messageHandler(Notification.class, this::onMessage)
						.onError((session1, throwable) -> logger.log(Level.WARNING, "Error in Plex WebSocket", throwable))
						.connect();
			} catch (IOException | DeploymentException e) {
				logger.log(Level.WARNING, "Exception while connecting with Plex WebSocket", e);
			}
		}
	}

	public void onMessage(Notification notification) {
		if (notification != null && notification.notificationContainer.playSessionStateNotification != null) {
			notification.notificationContainer.playSessionStateNotification.forEach(playSessionStateNotification -> {
				Event<PlaySessionStateNotification> event = playSessionEvent.select(new PlaySessionEventLiteral(playSessionStateNotification.state));
				event.fireAsync(playSessionStateNotification);
			});
		}
	}

	public void onStop(@ObservesAsync @WebhookEvent(value = Webhook.Event.STOP) Webhook webhook) {
		closeSession();
	}

	@PreDestroy
	public void destroy() {
		closeSession();
	}

	private void closeSession() {
		if (session != null && session.isOpen()) {
			try {
				session.close();
			} catch (IOException e) {
				logger.log(Level.WARNING, "Exception while closing Plex WebSocket", e);
			}
		}
	}

}
