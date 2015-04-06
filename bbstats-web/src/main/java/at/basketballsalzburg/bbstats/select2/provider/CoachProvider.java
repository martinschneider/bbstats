package at.basketballsalzburg.bbstats.select2.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;

import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.select2.Response;
import at.basketballsalzburg.bbstats.select2.TextChoiceProvider;
import at.basketballsalzburg.bbstats.services.CoachService;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;

public class CoachProvider extends TextChoiceProvider<CoachDTO> {

	private CoachService coachService;

	@Autowired
	public CoachProvider(CoachService coachService) {
		this.coachService = coachService;
	}

	@Override
	protected String getDisplayText(CoachDTO choice) {
		return choice.getDisplayName();
	}

	@Override
	protected Object getId(CoachDTO choice) {
		return choice.getId();
	}

	@Override
	public void query(String term, int page, Response<CoachDTO> response) {
		response.addAll(queryMatches(term, page, 10));
		response.setHasMore(response.size() == 10);

	}

	@Override
	public Collection<CoachDTO> toChoices(Collection<String> ids) {
		return coachService.findByIds(Lists.newArrayList(Lists.transform(
				new ArrayList<String>(ids), new Function<String, Long>() {
					public Long apply(final String in) {
						return in == null ? null : Longs.tryParse(in);
					}
				})));
	}

	@Override
	public Collection<String> toIds(Collection<CoachDTO> choices) {
		return CollectionUtils.collect(choices, new Transformer() {
			@Override
			public String transform(Object input) {
				if (input == null) {
					return null;
				}
				return ((CoachDTO) input).getId().toString();
			}
		});
	}

	private List<CoachDTO> queryMatches(String term, int page, int pageSize) {
		if (term.equals(SearchConstants.WILDCARD)) {
			return coachService.findAll();
		}
		return coachService.findByQuery(term.toLowerCase());
	}
}