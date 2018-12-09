package com.thuisapp.connector.plex.event;

import com.thuisapp.connector.plex.model.PlaySessionStateNotification;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PlaySessionEvent {

	PlaySessionStateNotification.State value();

}
