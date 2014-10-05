package at.basketballsalzburg.bbstats.dto.statistics;

/**
 * @author Martin Schneider
 */
public class GymPracticeStatisticDTO
{
    private String name;
    private String city;
    private int count;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }
}
