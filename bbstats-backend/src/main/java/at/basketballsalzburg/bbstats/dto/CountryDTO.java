package at.basketballsalzburg.bbstats.dto;

/**
 * @author Martin Schneider
 */
public class CountryDTO implements Comparable<CountryDTO>
{
    public CountryDTO(String isoCode, String countryName)
    {
        this.isoCode = isoCode;
        this.countryName = countryName;
    }

    private String isoCode;

    private String countryName;

    public String getIsoCode()
    {
        return isoCode;
    }

    public void setIsoCode(String isoCode)
    {
        this.isoCode = isoCode;
    }

    public String getCountryName()
    {
        return countryName;
    }

    public void setCountryName(String countryName)
    {
        this.countryName = countryName;
    }

    @Override
    public int compareTo(CountryDTO country)
    {
        return countryName.compareTo(country.getCountryName());
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof CountryDTO))
        {
            return false;
        }
        CountryDTO country = (CountryDTO) o;
        return country.getIsoCode().equalsIgnoreCase(isoCode) && country.getCountryName().equals(countryName);
    }
}
