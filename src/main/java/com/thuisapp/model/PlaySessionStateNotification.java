package com.thuisapp.model;

import com.thuisapp.adapter.PlaySessionStateNotificationStateAdapter;
import lombok.ToString;

import javax.json.bind.annotation.JsonbTypeAdapter;
import java.net.URI;
import java.util.Arrays;

@ToString
public class PlaySessionStateNotification {

	public String guid;
	public URI key;
	public String ratingKey;
	public String sessionKey;
	@JsonbTypeAdapter(PlaySessionStateNotificationStateAdapter.class)
	public State state;
	public String transcodeSession;
	public String url;
	public Long viewOffset;
	public Long playQueueItemID;

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
