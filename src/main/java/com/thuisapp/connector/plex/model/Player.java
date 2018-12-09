package com.thuisapp.connector.plex.model;

import lombok.Data;

@Data
public class Player {

	private boolean local;
	private String publicAddress;
	private String title;
	private String uuid;

}
