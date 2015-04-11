package at.basketballsalzburg.bbstats.dto;

/**
 * @author Martin Schneider
 */
public class TeamDTO implements Comparable<TeamDTO>
{

    private Long id;

    private String name;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int compareTo(TeamDTO rhs)
    {
        return name.compareTo(rhs.getName());
    }

    public String toString()
    {
        return name;
    }
}
