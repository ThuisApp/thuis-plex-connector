package com.thuisapp.connector.plex.event;

import com.thuisapp.connector.plex.model.Webhook;

import javax.enterprise.util.AnnotationLiteral;

public class WebhookEventLiteral extends AnnotationLiteral<WebhookEvent> implements WebhookEvent {

	private Webhook.Event value;

	public WebhookEventLiteral(Webhook.Event value) {
		this.value = value;
	}

	@Override
	public Webhook.Event value() {
		return value;
	}
}
