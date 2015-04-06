package at.basketballsalzburg.bbstats.select2.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;

import at.basketballsalzburg.bbstats.dto.LeagueDTO;
import at.basketballsalzburg.bbstats.select2.Response;
import at.basketballsalzburg.bbstats.select2.TextChoiceProvider;
import at.basketballsalzburg.bbstats.services.LeagueService;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;

public class LeagueProvider extends TextChoiceProvider<LeagueDTO> {

	private LeagueService leagueService;

	@Autowired
	public LeagueProvider(LeagueService leagueService) {
		this.leagueService = leagueService;
	}

	@Override
	protected String getDisplayText(LeagueDTO choice) {
		return choice.getDisplayName();
	}

	@Override
	protected Object getId(LeagueDTO choice) {
		return choice.getId();
	}

	@Override
	public void query(String term, int page, Response<LeagueDTO> response) {
		response.addAll(queryMatches(term, page, 10));
		response.setHasMore(response.size() == 10);

	}

	@Override
	public Collection<LeagueDTO> toChoices(Collection<String> ids) {
		return leagueService.findByIds(Lists.newArrayList(Lists.transform(
				new ArrayList<String>(ids), new Function<String, Long>() {
					public Long apply(final String in) {
						return in == null ? null : Longs.tryParse(in);
					}
				})));
	}

	@Override
	public Collection<String> toIds(Collection<LeagueDTO> choices) {
		return CollectionUtils.collect(choices, new Transformer() {
			@Override
			public String transform(Object input) {
				if (input == null) {
					return null;
				}
				return ((LeagueDTO) input).getId().toString();
			}
		});
	}

	private List<LeagueDTO> queryMatches(String term, int page, int pageSize) {
		if (term.equals(SearchConstants.WILDCARD)) {
			return leagueService.findAll();
		}
		return leagueService.findByQuery(term.toLowerCase());
	}
}
