package at.basketballsalzburg.bbstats.utils;

import java.util.Locale;

import org.apache.tapestry5.ValueEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import at.basketballsalzburg.bbstats.dto.CountryDTO;
import at.basketballsalzburg.bbstats.services.CountryService;

/**
 * @author Martin Schneider
 */
public class CountryValueEncoder implements ValueEncoder<CountryDTO>
{
    private CountryService countryService;
    
    private Locale locale;

    @Autowired
    public CountryValueEncoder(final CountryService countryService)
    {
        this.countryService = countryService;
    }

    @Override
    public String toClient(CountryDTO value)
    {
        return value.getIsoCode();
    }

    @Override
    public CountryDTO toValue(String clientValue)
    {
        return new CountryDTO(clientValue, countryService.findCountryNameForIsoCode(clientValue, locale));
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

}
