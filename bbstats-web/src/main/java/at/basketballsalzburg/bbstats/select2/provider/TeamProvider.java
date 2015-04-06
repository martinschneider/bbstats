package at.basketballsalzburg.bbstats.select2.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;

import at.basketballsalzburg.bbstats.dto.TeamDTO;
import at.basketballsalzburg.bbstats.select2.Response;
import at.basketballsalzburg.bbstats.select2.TextChoiceProvider;
import at.basketballsalzburg.bbstats.services.TeamService;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;

public class TeamProvider extends TextChoiceProvider<TeamDTO> {

	private TeamService teamService;

	@Autowired
	public TeamProvider(TeamService teamService) {
		this.teamService = teamService;
	}

	@Override
	protected String getDisplayText(TeamDTO choice) {
		return choice.getName();
	}

	@Override
	protected Object getId(TeamDTO choice) {
		return choice.getId();
	}

	@Override
	public void query(String term, int page, Response<TeamDTO> response) {
		response.addAll(queryMatches(term, page, 10));
		response.setHasMore(response.size() == 10);

	}

	@Override
	public Collection<TeamDTO> toChoices(Collection<String> ids) {
		return teamService.findByIds(Lists.newArrayList(Lists.transform(
				new ArrayList<String>(ids), new Function<String, Long>() {
					public Long apply(final String in) {
						return in == null ? null : Longs.tryParse(in);
					}
				})));
	}

	@Override
	public Collection<String> toIds(Collection<TeamDTO> choices) {
		return CollectionUtils.collect(choices, new Transformer() {
			@Override
			public String transform(Object input) {
				if (input == null) {
					return null;
				}
				return ((TeamDTO) input).getId().toString();
			}
		});
	}

	private List<TeamDTO> queryMatches(String term, int page, int pageSize) {
		if (term.equals(SearchConstants.WILDCARD)) {
			return teamService.findAll();
		}
		return teamService.findByQuery(term.toLowerCase());
	}
}
