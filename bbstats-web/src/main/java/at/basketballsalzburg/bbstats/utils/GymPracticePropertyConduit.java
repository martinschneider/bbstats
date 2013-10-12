package at.basketballsalzburg.bbstats.utils;

import java.lang.annotation.Annotation;

import org.apache.tapestry5.PropertyConduit;

import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.dto.GameStatDTO;
import at.basketballsalzburg.bbstats.dto.GymDTO;
import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.dto.TeamDTO;

public class GymPracticePropertyConduit implements PropertyConduit {

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return null;
	}

	/**
	 * @see org.apache.tapestry5.PropertyConduit#get(java.lang.Object)
	 */
	public Object get(Object instance) {
		PracticeDTO game = (PracticeDTO) instance;
		return game.getGym();
	}

	public Class getPropertyType() {
		return String.class;
	}

	public void set(Object instance, Object value) {
		PracticeDTO game = (PracticeDTO) instance;
		game.setGym((GymDTO) value);
	}
}