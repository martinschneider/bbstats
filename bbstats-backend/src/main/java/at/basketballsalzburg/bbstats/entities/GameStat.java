package at.basketballsalzburg.bbstats.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Martin Schneider
 */
@Entity
@Table(name = "bbstats_stat")
public class GameStat
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "playerid")
    private Player player;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "gameid")
    private Game game;

    private Integer points;
    private Integer threes;
    private Integer fta;
    private Integer ftm;
    private Integer fouls;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public Game getGame()
    {
        return game;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public Integer getPoints()
    {
        return points;
    }

    public void setPoints(Integer points)
    {
        this.points = points;
    }

    public Integer getThrees()
    {
        return threes;
    }

    public void setThrees(Integer threes)
    {
        this.threes = threes;
    }

    public Integer getFta()
    {
        return fta;
    }

    public void setFta(Integer fta)
    {
        this.fta = fta;
    }

    public Integer getFtm()
    {
        return ftm;
    }

    public void setFtm(Integer ftm)
    {
        this.ftm = ftm;
    }

    public Integer getFouls()
    {
        return fouls;
    }

    public void setFouls(Integer fouls)
    {
        this.fouls = fouls;
    }
}
