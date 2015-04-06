package at.basketballsalzburg.bbstats.select2.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;

import at.basketballsalzburg.bbstats.dto.GymDTO;
import at.basketballsalzburg.bbstats.select2.Response;
import at.basketballsalzburg.bbstats.select2.TextChoiceProvider;
import at.basketballsalzburg.bbstats.services.GymService;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;

public class GymProvider extends TextChoiceProvider<GymDTO> {
	private GymService gymService;

	@Autowired
	public GymProvider(GymService gymService) {
		this.gymService = gymService;
	}

	@Override
	protected String getDisplayText(GymDTO choice) {
		return choice.getDisplayName();
	}

	@Override
	protected Object getId(GymDTO choice) {
		return choice.getId();
	}

	@Override
	public void query(String term, int page, Response<GymDTO> response) {
		response.addAll(queryMatches(term, page, 10));
		response.setHasMore(response.size() == 10);

	}

	@Override
	public Collection<GymDTO> toChoices(Collection<String> ids) {
		return gymService.findByIds(Lists.newArrayList(Lists.transform(
				new ArrayList<String>(ids), new Function<String, Long>() {
					public Long apply(final String in) {
						return in == null ? null : Longs.tryParse(in);
					}
				})));
	}

	@Override
	public Collection<String> toIds(Collection<GymDTO> choices) {
		return CollectionUtils.collect(choices, new Transformer() {
			@Override
			public String transform(Object input) {
				if (input == null) {
					return null;
				}
				return ((GymDTO) input).getId().toString();
			}
		});
	}

	private List<GymDTO> queryMatches(String term, int page, int pageSize) {
		if (term.equals(SearchConstants.WILDCARD)) {
			return gymService.findAll();
		}
		return gymService.findByQuery(term.toLowerCase());
	}
}