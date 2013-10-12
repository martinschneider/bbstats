package at.basketballsalzburg.bbstats.utils;

import org.apache.tapestry5.ValueEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.services.CoachService;

public class CoachValueEncoder implements ValueEncoder<CoachDTO> {

	private CoachService coachService;

	@Autowired
	public CoachValueEncoder(final CoachService coachService) {
		this.coachService = coachService;
	}

	public String toClient(CoachDTO value) {
		if (value.getId() == null) {
			return null;
		}
		return value.getId().toString();
	}

	public CoachDTO toValue(String id) {
		if (id == null || id.isEmpty()) {
			return null;
		}
		return coachService.findById(Long.parseLong(id));
	}

}
