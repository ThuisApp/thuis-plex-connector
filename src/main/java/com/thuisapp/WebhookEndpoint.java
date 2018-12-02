package com.thuisapp;

import com.thuisapp.event.WebhookEventLiteral;
import com.thuisapp.model.Webhook;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("webhook")
@RequestScoped
public class WebhookEndpoint {
	private static final Logger logger = Logger.getLogger(WebhookEndpoint.class.getName());

	@Inject
	private Event<Webhook> webhookEvent;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void handleMultipartWebhook(@FormDataParam("payload") Webhook webhook, @FormDataParam("thumb") File thumb) {
		if (webhook != null) {
			logger.log(Level.FINE, "Received webhook: " + webhook);

			Event<Webhook> event = webhookEvent.select(new WebhookEventLiteral(webhook.event));
			event.fireAsync(webhook);
		}
	}

}
