package com.thuisapp.connector.plex.util;

import org.eclipse.microprofile.config.Config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@ApplicationScoped
public class PlexUtil {

	private static final String PLEX_TOKEN_KEY = "X-Plex-Token";
	private static final String WS_PATH = "/:/websockets/notifications";

	@Inject
	Config config;

	public String buildUri(String path) {
		return createBuilder(path).build().toString();
	}

	public URI buildWebSocketUri() {
		return createBuilder(WS_PATH)
				.scheme(config.getValue("plex.ws_scheme", String.class))
				.build();
	}

	private UriBuilder createBuilder(String path) {
		return UriBuilder.fromPath(path)
				.scheme(config.getValue("plex.scheme", String.class))
				.host(config.getValue("plex.host", String.class))
				.port(config.getValue("plex.port", Integer.class))
				.queryParam(PLEX_TOKEN_KEY, config.getValue("plex.token", String.class));
	}
}
