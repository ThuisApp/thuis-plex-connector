package com.thuisapp.connector.plex;

import com.thuisapp.connector.plex.adapter.NotificationDecoder;
import com.thuisapp.connector.plex.event.PlaySessionEventLiteral;
import com.thuisapp.connector.plex.event.WebhookEvent;
import com.thuisapp.connector.plex.model.Notification;
import com.thuisapp.connector.plex.model.PlaySessionStateNotification;
import com.thuisapp.connector.plex.model.Webhook;
import com.thuisapp.connector.plex.util.PlexUtil;
import lombok.extern.java.Log;

import javax.annotation.PreDestroy;
import javax.enterprise.event.Event;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import java.io.IOException;
import java.util.logging.Level;

@Log
@Singleton
@ClientEndpoint(decoders = {NotificationDecoder.class})
public class WebsocketEndpoint {

	@Inject
	PlexUtil plexUtil;

	@Inject
	Event<PlaySessionStateNotification> playSessionEvent;

	private Session session;

	public void onPlay(@ObservesAsync @WebhookEvent(value = Webhook.Event.PLAY) Webhook webhook) {
		if (session == null) {
			try {
				session = ContainerProvider.getWebSocketContainer()
					.connectToServer(WebsocketEndpoint.class, plexUtil.buildWebSocketUri());
			} catch (IOException | DeploymentException e) {
				log.log(Level.WARNING, "Exception while connecting with Plex WebSocket", e);
			}
		}
	}

	@OnMessage
	public void onMessage(Notification notification) {
		if (notification != null && notification.getNotificationContainer().getPlaySessionStateNotification() != null) {
			notification.getNotificationContainer().getPlaySessionStateNotification().forEach(playSessionStateNotification -> {
				Event<PlaySessionStateNotification> event = playSessionEvent.select(new PlaySessionEventLiteral(playSessionStateNotification.getState()));
				event.fireAsync(playSessionStateNotification);
			});
		}
	}

	@OnError
	public void onError(Throwable throwable) {
		log.log(Level.WARNING, "Error in Plex WebSocket", throwable);
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
				log.log(Level.WARNING, "Exception while closing Plex WebSocket", e);
			}
		}
	}

}
