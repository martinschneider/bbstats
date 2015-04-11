package at.basketballsalzburg.bbstats.select2.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import at.basketballsalzburg.bbstats.select2.Response;
import at.basketballsalzburg.bbstats.select2.TextChoiceProvider;

import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import edu.emory.mathcs.backport.java.util.Collections;

public class CountryProvider extends TextChoiceProvider<String> {

	private BiMap<String, String> countries = HashBiMap.create();

	public CountryProvider(Locale locale) {
		for (String iso : Locale.getISOCountries()) {
			Locale countryLocale = new Locale("", iso);
			countries.put(iso.toLowerCase(), countryLocale.getDisplayCountry(locale));
		}
	}

	@Override
	protected String getDisplayText(String choice) {
		return choice;
	}

	@Override
	protected Object getId(String choice) {
		return countries.inverse().get(choice);
	}

	@Override
	public void query(final String term, int page, Response<String> response) {
		if (term.equals(SearchConstants.WILDCARD)) {
			List<String> results = new ArrayList<String>(countries.values());
			Collections.sort(results);
			response.addAll(results);
		} else {
			Predicate<String> countryFilter = new Predicate<String>() {
				public boolean apply(String countryName) {
					return countryName.toLowerCase().startsWith(
							term.toLowerCase());
				}
			};
			response.addAll(Maps.filterValues(countries, countryFilter)
					.values());
		}
	}

	@Override
	public Collection<String> toChoices(Collection<String> ids) {
		return ids;
	}

	@Override
	public Collection<String> toIds(Collection<String> choices) {
		return CollectionUtils.collect(choices, new Transformer() {
			@Override
			public String transform(Object input) {
				if (input == null) {
					return null;
				}
				return countries.get((String) input);
			}
		});
	}
}