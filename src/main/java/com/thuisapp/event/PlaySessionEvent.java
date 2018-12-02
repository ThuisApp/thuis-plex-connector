package com.thuisapp.event;

import com.thuisapp.model.PlaySessionStateNotification;

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
