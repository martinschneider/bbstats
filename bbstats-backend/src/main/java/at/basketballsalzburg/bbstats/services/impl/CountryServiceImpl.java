package at.basketballsalzburg.bbstats.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import at.basketballsalzburg.bbstats.dto.CountryDTO;
import at.basketballsalzburg.bbstats.services.CountryService;
import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author Martin Schneider
 */
public class CountryServiceImpl implements CountryService
{
    @Override
    public List<CountryDTO> findAll(Locale locale)
    {
        List<CountryDTO> countries = new ArrayList<CountryDTO>();
        for (String countryCode : Locale.getISOCountries())
        {
            countries.add(new CountryDTO(countryCode.toLowerCase(), new Locale("", countryCode)
                .getDisplayCountry(locale)));
        }
        Collections.sort(countries);
        return countries;
    }

    @Override
    public String findCountryNameForIsoCode(String isoCode, Locale locale)
    {
        return new Locale("", isoCode).getDisplayCountry(locale);
    }
}
