package at.basketballsalzburg.bbstats.dto.statistics;

import java.math.BigDecimal;

/**
 * @author Martin Schneider
 */
public class FreethrowStatisticDTO
{
    private String firstname;
    private String lastname;
    private int fta;
    private int ftm;
    private BigDecimal ftPercentage;
    private BigDecimal ftapg;
    private BigDecimal ftmpg;

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public int getFta()
    {
        return fta;
    }

    public void setFta(int fta)
    {
        this.fta = fta;
    }

    public int getFtm()
    {
        return ftm;
    }

    public void setFtm(int ftm)
    {
        this.ftm = ftm;
    }

    public BigDecimal getFtPercentage()
    {
        return ftPercentage;
    }

    public void setFtPercentage(BigDecimal ftPercentage)
    {
        this.ftPercentage = ftPercentage;
    }

    public BigDecimal getFtapg()
    {
        return ftapg;
    }

    public void setFtapg(BigDecimal ftapg)
    {
        this.ftapg = ftapg;
    }

    public BigDecimal getFtmpg()
    {
        return ftmpg;
    }

    public void setFtmpg(BigDecimal ftmpg)
    {
        this.ftmpg = ftmpg;
    }
}
