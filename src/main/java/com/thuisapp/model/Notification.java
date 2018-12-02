package com.thuisapp.model;

import lombok.ToString;

import javax.json.bind.annotation.JsonbProperty;

@ToString
public class Notification {

	@JsonbProperty("NotificationContainer")
	public NotificationContainer notificationContainer;

}
