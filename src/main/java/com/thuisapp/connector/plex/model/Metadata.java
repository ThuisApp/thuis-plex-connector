package com.thuisapp.connector.plex.model;

import lombok.Data;

@Data
public class Metadata {

	private String librarySectionType;
	private String ratingKey;
	private String key;
	private String parentRatingKey;
	private String grandparentRatingKey;
	private String guid;
	private String librarySectionID;
	private String type;
	private String title;
	private String grandparentKey;
	private String parentKey;
	private String grandparentTitle;
	private String parentTitle;
	private String summary;
	private Long index;
	private Long parentIndex;
	private Long ratingCount;
	private String thumb;
	private String art;
	private String parentThumb;
	private String grandparentThumb;
	private String grandparentArt;
	private Long addedAt;
	private Long updatedAt;

}
