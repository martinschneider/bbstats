package at.basketballsalzburg.bbstats.utils;

import java.lang.annotation.Annotation;

import org.apache.tapestry5.PropertyConduit;

import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.dto.LeagueDTO;

public class LeaguePropertyConduit implements PropertyConduit {

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return null;
	}

	/**
	 * @see org.apache.tapestry5.PropertyConduit#get(java.lang.Object)
	 */
	public Object get(Object instance) {
		GameDTO game = (GameDTO) instance;
		return game.getLeague().getName();
	}

	public Class getPropertyType() {
		return String.class;
	}

	public void set(Object instance, Object value) {
		GameDTO game = (GameDTO) instance;
		game.setLeague((LeagueDTO) value);
	}
}