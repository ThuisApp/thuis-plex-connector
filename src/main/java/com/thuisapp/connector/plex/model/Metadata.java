package com.thuisapp.connector.plex.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Metadata {

	private String librarySectionType;
	private String ratingKey;
	private String key;
	private String parentRatingKey;
	private String grandparentRatingKey;
	private String guid;
	private String librarySectionTitle;
	private String librarySectionID;
	private String librarySectionKey;
	private String type;
	private String title;
	private String grandparentKey;
	private String parentKey;
	private String grandparentTitle;
	private String parentTitle;
	private String contentRating;
	private String summary;
	private Long index;
	private Long parentIndex;
	private Long viewCount;
	private Long lastViewedAt;
	private Long year;
	private String thumb;
	private String art;
	private String parentThumb;
	private String grandparentThumb;
	private String grandparentArt;
	private String grandparentTheme;
	private LocalDate originallyAvailableAt;
	private Long addedAt;
	private Long updatedAt;
	// TODO
	// @JsonbProperty("Director")
	// private Person director;
	// @JsonbProperty("Writer")
	// private Person writer;

}
