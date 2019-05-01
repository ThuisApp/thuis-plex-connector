package com.thuisapp.connector.plex.model;

import com.thuisapp.connector.plex.adapter.WebhookEventAdapter;
import lombok.Data;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTypeAdapter;
import java.util.Arrays;
import java.util.Optional;

@Data
public class Webhook {

	@JsonbTypeAdapter(WebhookEventAdapter.class)
	private Event event;
	private boolean user;
	private boolean owner;
	@JsonbProperty("Account")
	private Account account;
	@JsonbProperty("Server")
	private Server server;
	@JsonbProperty("Player")
	private Player player;
	@JsonbProperty("Metadata")
	private Metadata metadata;

	public enum Event {
		PLAY("media.play", "playing"),
		PAUSE("media.pause", "paused"),
		RESUME("media.resume", "playing"),
		STOP("media.stop", "stopped"),
		SCROBBLE("media.scrobble", null),
		RATE("media.rate", null);

		private String value;
		private String playState;

		Event(String value, String playState) {
			this.value = value;
			this.playState = playState;
		}

		public static Event of(String string) {
			return Arrays.stream(Event.values())
					.filter(event -> event.value.equals(string))
					.findFirst()
					.orElse(null);
		}

		public String getValue() {
			return value;
		}

		public Optional<String> getPlayState() {
			return Optional.ofNullable(playState);
		}
	}

}
