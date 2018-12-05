package com.thuisapp.model;

import lombok.Data;

import javax.json.bind.annotation.JsonbProperty;

@Data
public class Notification {

	@JsonbProperty("NotificationContainer")
	private NotificationContainer notificationContainer;

}
