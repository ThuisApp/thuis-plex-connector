package com.thuisapp.connector.plex;

import com.thuisapp.connector.plex.event.WebhookEventLiteral;
import com.thuisapp.connector.plex.model.Webhook;
import com.thuisapp.connector.plex.model.WebhookForm;
import lombok.extern.java.Log;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;

@Log
@Path("webhook")
@RequestScoped
public class WebhookEndpoint {

	@Inject
	Event<Webhook> webhookEvent;

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void handleMultipartWebhook(@MultipartForm WebhookForm form) {
		Webhook webhook = form.getWebhook();
		if (webhook != null) {
			log.log(Level.INFO, "Received webhook: " + webhook);

			Event<Webhook> event = webhookEvent.select(new WebhookEventLiteral(webhook.getEvent()));
			event.fireAsync(webhook);
		}
	}

}
