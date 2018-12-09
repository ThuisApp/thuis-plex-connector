package com.thuisapp.connector.plex.adapter;

import com.thuisapp.connector.plex.model.PlaySessionStateNotification;

import javax.json.bind.adapter.JsonbAdapter;

public class PlaySessionStateNotificationStateAdapter implements JsonbAdapter<PlaySessionStateNotification.State, String> {
	@Override
	public String adaptToJson(PlaySessionStateNotification.State state) {
		return state.getValue();
	}

	@Override
	public PlaySessionStateNotification.State adaptFromJson(String string) {
		return PlaySessionStateNotification.State.of(string);
	}
}
