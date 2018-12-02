package com.thuisapp.adapter;

import com.thuisapp.model.Webhook;

import javax.json.bind.adapter.JsonbAdapter;

public class WebhookEventAdapter implements JsonbAdapter<Webhook.Event, String> {
	@Override
	public String adaptToJson(Webhook.Event event) {
		return event.getValue();
	}

	@Override
	public Webhook.Event adaptFromJson(String string) {
		return Webhook.Event.of(string);
	}
}
