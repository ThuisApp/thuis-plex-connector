package com.thuisapp.connector.plex.model;

import lombok.Data;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.FormParam;
import java.io.File;

@Data
public class WebhookForm {

	@FormParam("payload")
	String payload;

	@FormParam("thumb")
	File file;

	public Webhook getWebhook() {
		try (Jsonb jsonb = JsonbBuilder.create()) {
			return jsonb.fromJson(payload, Webhook.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
