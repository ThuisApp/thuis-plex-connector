package com.thuisapp.connector.plex.event;

import com.thuisapp.connector.plex.model.PlaySessionStateNotification;

import javax.enterprise.util.AnnotationLiteral;

public class PlaySessionEventLiteral extends AnnotationLiteral<PlaySessionEvent> implements PlaySessionEvent {

	private PlaySessionStateNotification.State value;

	public PlaySessionEventLiteral(PlaySessionStateNotification.State value) {
		this.value = value;
	}

	public PlaySessionStateNotification.State value() {
		return value;
	}
}
