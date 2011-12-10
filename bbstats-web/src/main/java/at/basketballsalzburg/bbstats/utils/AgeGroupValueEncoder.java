package at.basketballsalzburg.bbstats.utils;

import org.apache.tapestry5.ValueEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.services.AgeGroupService;

public class AgeGroupValueEncoder implements ValueEncoder<AgeGroupDTO> {

	private AgeGroupService ageGroupService;

	@Autowired
	public AgeGroupValueEncoder(final AgeGroupService ageGroupService) {
		this.ageGroupService = ageGroupService;
	}

	public String toClient(AgeGroupDTO value) {
		if (value.getId() == null) {
			return null;
		}
		return value.getId().toString();
	}

	public AgeGroupDTO toValue(String id) {
		if (id == null || id.isEmpty()) {
			return null;
		}
		return ageGroupService.findById(Long.parseLong(id));
	}

}
