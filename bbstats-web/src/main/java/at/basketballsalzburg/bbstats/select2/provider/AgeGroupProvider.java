package at.basketballsalzburg.bbstats.select2.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;

import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.select2.Response;
import at.basketballsalzburg.bbstats.select2.TextChoiceProvider;
import at.basketballsalzburg.bbstats.services.AgeGroupService;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;

public class AgeGroupProvider extends TextChoiceProvider<AgeGroupDTO> {

	private AgeGroupService ageGroupService;

	@Autowired
	public AgeGroupProvider(AgeGroupService ageGroupService) {
		this.ageGroupService = ageGroupService;
	}

	@Override
	protected String getDisplayText(AgeGroupDTO choice) {
		return choice.getName();
	}

	@Override
	protected Object getId(AgeGroupDTO choice) {
		return choice.getId();
	}

	@Override
	public void query(String term, int page, Response<AgeGroupDTO> response) {
		response.addAll(queryMatches(term, page, 10));
		response.setHasMore(response.size() == 10);

	}

	@Override
	public Collection<AgeGroupDTO> toChoices(Collection<String> ids) {
		return ageGroupService.findByIds(Lists.newArrayList(Lists.transform(
				new ArrayList<String>(ids), new Function<String, Long>() {
					public Long apply(final String in) {
						return in == null ? null : Longs.tryParse(in);
					}
				})));
	}

	@Override
	public Collection<String> toIds(Collection<AgeGroupDTO> choices) {
		return CollectionUtils.collect(choices, new Transformer() {
			@Override
			public String transform(Object input) {
				if (input == null) {
					return null;
				}
				return ((AgeGroupDTO) input).getId().toString();
			}
		});
	}

	private List<AgeGroupDTO> queryMatches(String term, int page, int pageSize) {
		if (term.equals(SearchConstants.WILDCARD)) {
			return ageGroupService.findAll();
		}
		return ageGroupService.findByQuery(term.toLowerCase());
	}
}