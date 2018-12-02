package com.thuisapp.model;

import com.thuisapp.adapter.WebhookEventAdapter;
import lombok.ToString;

import javax.json.bind.annotation.JsonbTypeAdapter;
import java.util.Arrays;
import java.util.Optional;

@ToString
public class Webhook {

	@JsonbTypeAdapter(WebhookEventAdapter.class)
	public Event event;
	public boolean user;
	public boolean owner;
	public Account account;
	public Server server;
	public Player player;
	public Metadata metadata;

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
