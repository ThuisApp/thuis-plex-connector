package com.thuisapp.model;

import lombok.ToString;

import java.util.List;

@ToString
public class NotificationContainer {

	public String type;
	public Long size;
	public List<PlaySessionStateNotification> playSessionStateNotification;

}
