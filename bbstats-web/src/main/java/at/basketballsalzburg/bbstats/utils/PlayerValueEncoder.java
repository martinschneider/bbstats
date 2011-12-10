package at.basketballsalzburg.bbstats.utils;

import org.apache.tapestry5.ValueEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.services.PlayerService;

public class PlayerValueEncoder implements ValueEncoder<PlayerDTO> {

	private PlayerService playerService;

	@Autowired
	public PlayerValueEncoder(final PlayerService playerService) {
		this.playerService = playerService;
	}

	public String toClient(PlayerDTO value) {
		if (value.getId() == null) {
			return null;
		}
		return value.getId().toString();
	}

	public PlayerDTO toValue(String id) {
		if (id == null || id.isEmpty()) {
			return null;
		}
		return playerService.findById(Long.parseLong(id));
	}

}
