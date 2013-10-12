package at.basketballsalzburg.bbstats.utils;

import java.lang.annotation.Annotation;

import org.apache.tapestry5.PropertyConduit;

import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.dto.TeamDTO;

public class TeamPropertyConduit implements PropertyConduit {

	private int team;

	public TeamPropertyConduit(int team) {
		this.team = team;
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return null;
	}

	/**
	 * @see org.apache.tapestry5.PropertyConduit#get(java.lang.Object)
	 */
	public Object get(Object instance) {
		GameDTO game = (GameDTO) instance;
		if (team == 1)
			return game.getTeamA();
		else
			return game.getTeamB();
	}

	public Class getPropertyType() {
		return String.class;
	}

	public void set(Object instance, Object value) {
		GameDTO game = (GameDTO) instance;
		if (team == 1)
			game.setTeamA((TeamDTO) value);
		else
			game.setTeamB((TeamDTO) value);
	}
}
