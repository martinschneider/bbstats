package at.basketballsalzburg.bbstats.select2.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;

import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.select2.Response;
import at.basketballsalzburg.bbstats.select2.TextChoiceProvider;
import at.basketballsalzburg.bbstats.services.PlayerService;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;

public class PlayerProvider extends TextChoiceProvider<PlayerDTO> {

	private PlayerService playerService;

	@Autowired
	public PlayerProvider(PlayerService playerService) {
		this.playerService = playerService;
	}

	@Override
	protected String getDisplayText(PlayerDTO choice) {
		return choice.getDisplayName();
	}

	@Override
	protected Object getId(PlayerDTO choice) {
		return choice.getId();
	}

	@Override
	public void query(String term, int page, Response<PlayerDTO> response) {
		response.addAll(queryMatches(term, page, 10));
		response.setHasMore(response.size() == 10);

	}

	@Override
	public Collection<PlayerDTO> toChoices(Collection<String> ids) {
		return playerService.findByIds(Lists.newArrayList(Lists.transform(
				new ArrayList<String>(ids), new Function<String, Long>() {
					public Long apply(final String in) {
						return in == null ? null : Longs.tryParse(in);
					}
				})));
	}

	@Override
	public Collection<String> toIds(Collection<PlayerDTO> choices) {
		return CollectionUtils.collect(choices, new Transformer() {
			@Override
			public String transform(Object input) {
				if (input == null) {
					return null;
				}
				return ((PlayerDTO) input).getId().toString();
			}
		});
	}

	private List<PlayerDTO> queryMatches(String term, int page, int pageSize) {
		if (term.equals(SearchConstants.WILDCARD)) {
			return new ArrayList<PlayerDTO>(playerService.findAll());
		}
		return new ArrayList<PlayerDTO>(playerService.findByQuery(term
				.toLowerCase()));
	}
}