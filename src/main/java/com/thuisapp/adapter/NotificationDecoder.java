package com.thuisapp.adapter;

import com.thuisapp.model.Notification;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationDecoder implements Decoder.Text<Notification> {
	private static final Logger logger = Logger.getLogger(NotificationDecoder.class.getName());

	@Override
	public Notification decode(String string) {
		try {
			return JsonbBuilder.create().fromJson(string, Notification.class);
		} catch (JsonbException e) {
			logger.log(Level.WARNING, "Could not decode Plex WebSocket Notification", e);
			return null;
		}
	}

	@Override
	public boolean willDecode(String s) {
		return (s != null);
	}

	@Override
	public void init(EndpointConfig config) {
		// do nothing
	}

	@Override
	public void destroy() {
		// do nothing
	}
}
