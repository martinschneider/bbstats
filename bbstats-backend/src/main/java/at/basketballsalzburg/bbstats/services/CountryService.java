package at.basketballsalzburg.bbstats.services;

import java.util.List;
import java.util.Locale;

import at.basketballsalzburg.bbstats.dto.CountryDTO;

/**
 * @author Martin Schneider
 */
public interface CountryService
{
    public List<CountryDTO> findAll(Locale locale);

    public String findCountryNameForIsoCode(String isoCode, Locale locale);
}
