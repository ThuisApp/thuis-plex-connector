package com.thuisapp.model;

import lombok.Data;

import java.util.List;

@Data
public class NotificationContainer {

	private String type;
	private Long size;
	private List<PlaySessionStateNotification> playSessionStateNotification;

}
