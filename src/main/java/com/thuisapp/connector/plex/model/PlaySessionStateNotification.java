package com.thuisapp.connector.plex.model;

import com.thuisapp.connector.plex.adapter.PlaySessionStateNotificationStateAdapter;
import lombok.Data;

import javax.json.bind.annotation.JsonbTypeAdapter;
import java.net.URI;
import java.util.Arrays;

@Data
public class PlaySessionStateNotification {

	private String guid;
	private URI key;
	private String ratingKey;
	private String sessionKey;
	@JsonbTypeAdapter(PlaySessionStateNotificationStateAdapter.class)
	private State state;
	private String transcodeSession;
	private String url;
	private Long viewOffset;
	private Long playQueueItemID;

	public enum State {
		BUFFERING("buffering"),
		PLAYING("playing"),
		PAUSED("paused"),
		STOPPED("stopped");

		private String value;

		State(String value) {
			this.value = value;
		}

		public static State of(String string) {
			return Arrays.stream(State.values())
					.filter(state -> state.value.equals(string))
					.findFirst()
					.orElse(null);
		}

		public String getValue() {
			return value;
		}
	}

}
