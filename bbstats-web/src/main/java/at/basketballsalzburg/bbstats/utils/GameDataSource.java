package at.basketballsalzburg.bbstats.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.services.GameService;

/**
 * {@link GridDataSource} for games
 * 
 * @author Martin Schneider
 */
public class GameDataSource implements GridDataSource
{

    private int startIndex;
    private int size = 20;
    private Long playerId;
    private Long coachId;
    private GameService gameService;
    private GameMode mode;
    private List<GameDTO> preparedResults;

    public GameDataSource(final GameService gameService, final GameMode mode)
    {
        this.gameService = gameService;
        this.mode = mode;
    }

    public GameDataSource(final GameService gameService, final GameMode mode,
        Long id)
    {
        this.gameService = gameService;
        this.mode = mode;
        if (mode.equals(GameMode.COACH))
        {
            coachId = id;
        }
        else if (mode.equals(GameMode.PLAYER))
        {
            playerId = id;
        }
    }

    @Override
    public int getAvailableRows()
    {
        if (mode.equals(GameMode.RESULTS))
        {
            return gameService.countResults();
        }
        else if (mode.equals(GameMode.SCHEDULE))
        {
            return gameService.countSchedule();
        }
        else if (mode.equals(GameMode.PLAYER))
        {
            return gameService.countByPlayer(playerId);
        }
        else if (mode.equals(GameMode.COACH))
        {
            return gameService.countByCoach(coachId);
        }
        return 0;
    }

    @Override
    public void prepare(int startIndex, int endIndex,
        List<SortConstraint> sortConstraints)
    {

        List<Order> orders = new ArrayList<Order>();

        for (SortConstraint sortConstraint : sortConstraints)
        {
            String propertyName = sortConstraint.getPropertyModel()
                .getPropertyName();
            if (propertyName.equals("teamA") || propertyName.equals("teamB")
                || propertyName.equals("league"))
            {
                propertyName = propertyName + ".name";
            }
            Direction sortDirection = null;

            switch (sortConstraint.getColumnSort())
            {
                case ASCENDING:
                    sortDirection = Direction.ASC;
                    break;
                case DESCENDING:
                    sortDirection = Direction.DESC;
                    break;
                default:
            }
            orders.add(new Order(sortDirection, propertyName));
        }

        if (mode.equals(GameMode.RESULTS))
        {
            preparedResults = gameService.findResults(startIndex / size, size,
                new Sort(orders));
        }
        else if (mode.equals(GameMode.SCHEDULE))
        {
            preparedResults = gameService.findSchedule(startIndex / size, size,
                new Sort(orders));
        }
        else if (mode.equals(GameMode.PLAYER))
        {
            preparedResults = gameService.findGamesForPlayer(playerId,
                startIndex / size, size, new Sort(orders));
        }
        else if (mode.equals(GameMode.COACH))
        {
            preparedResults = gameService.findGamesForCoach(coachId, startIndex
                / size, size, new Sort(orders));
        }
        this.startIndex = startIndex;
    }

    @Override
    public Object getRowValue(int index)
    {
        return preparedResults.get(index - startIndex);
    }

    @Override
    public Class<GameDTO> getRowType()
    {
        return GameDTO.class;
    }

}
