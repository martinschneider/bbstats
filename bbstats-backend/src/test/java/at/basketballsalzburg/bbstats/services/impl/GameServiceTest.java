package at.basketballsalzburg.bbstats.services.impl;

import static org.testng.Assert.assertEquals;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import at.basketballsalzburg.bbstats.dto.CoachDTO;
import at.basketballsalzburg.bbstats.dto.CountryDTO;
import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.dto.GameStatDTO;
import at.basketballsalzburg.bbstats.dto.GymDTO;
import at.basketballsalzburg.bbstats.dto.LeagueDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.dto.TeamDTO;
import at.basketballsalzburg.bbstats.services.GameService;

@ContextConfiguration("classpath:META-INF/db-test.xml")
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class GameServiceTest extends
    AbstractTransactionalTestNGSpringContextTests
{

    @Autowired
    private GameService gameService;

    @Test
    public void addGame()
    {
        int size = gameService.findAll().size();
        GameDTO game = new GameDTO();
        game.setDateTime(new Date());

        GymDTO gym = new GymDTO();
        gym.setName("my private gym");
        gym.setShortName("MPG");
        gym.setCity("los angeles");
        game.setGym(gym);

        CoachDTO coach = new CoachDTO();
        coach.setFirstName("tom");
        coach.setLastName("jones");
        game.addCoach(coach);

        game.setLeague(new LeagueDTO());
        game.setPeriods(4);
        game.setTeamA(new TeamDTO());
        game.setTeamB(new TeamDTO());
        game.setScoreA1(100);
        game.setScoreB1(99);

        PlayerDTO player1 = new PlayerDTO();
        player1.setFirstName("tom");
        player1.setLastName("jones");
        player1.setNationality("us");

        PlayerDTO player2 = new PlayerDTO();
        player2.setFirstName("elton");
        player2.setLastName("john");
        player1.setNationality("us");

        GameStatDTO stat1 = new GameStatDTO();
        stat1.setFouls(5);
        stat1.setFta(10);
        stat1.setFtm(9);
        stat1.setPoints(30);
        stat1.setThrees(3);
        stat1.setPlayer(player1);
        stat1.setGame(game);

        GameStatDTO stat2 = new GameStatDTO();
        stat2.setFouls(5);
        stat2.setFta(10);
        stat2.setFtm(9);
        stat2.setPoints(30);
        stat2.setThrees(3);
        stat2.setPlayer(player2);
        stat2.setGame(game);

        game.addStat(stat1);
        game.addStat(stat2);

        gameService.save(game);

        assertEquals(size + 1, gameService.findAll().size());
    }
}
