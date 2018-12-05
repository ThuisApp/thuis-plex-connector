package com.thuisapp.adapter;

import com.thuisapp.model.Notification;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationDecoder implements Decoder.Text<Notification> {
	private static final Logger logger = Logger.getLogger(NotificationDecoder.class.getName());

	private Jsonb jsonb;

	@Override
	public Notification decode(String string) {
		try {
			return jsonb.fromJson(string, Notification.class);
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
		jsonb = JsonbBuilder.create();
	}

	@Override
	public void destroy() {
		try {
			jsonb.close();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Could not close Jsonb", e);
		}
	}
}
