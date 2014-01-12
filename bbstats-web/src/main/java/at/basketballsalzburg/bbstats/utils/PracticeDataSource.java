package at.basketballsalzburg.bbstats.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.dto.PracticeDTO;
import at.basketballsalzburg.bbstats.services.PracticeService;

/**
 * {@link GridDataSource} for practices
 * 
 * @author Martin Schneider
 */
public class PracticeDataSource implements GridDataSource {

	private int startIndex;
	private int size = 20;
	private PracticeMode mode = PracticeMode.ALL;
	private Long playerId;
	private Long coachId;
	private PracticeService practiceService;
	private List<PracticeDTO> preparedResults;

	public PracticeDataSource(final PracticeService practiceService) {
		this.practiceService = practiceService;
	}
	
	public PracticeDataSource(final PracticeService practiceService, final PracticeMode mode, final Long id) {
		this.practiceService = practiceService;
		this.mode = mode;
		if (mode.equals(PracticeMode.COACH))
		{
			coachId = id;
		}
		else if (mode.equals(PracticeMode.PLAYER))
		{
			playerId = id;
		}
	}

	@Override
	public int getAvailableRows() {
		if (mode.equals(PracticeMode.COACH))
		{
			return practiceService.countByCoach(coachId);
		}
		else if (mode.equals(PracticeMode.PLAYER))
		{
			return practiceService.countByPlayer(playerId);
		}
		return (int) practiceService.count();
	}

	@Override
	public void prepare(int startIndex, int endIndex,
			List<SortConstraint> sortConstraints) {

		List<Order> orders = new ArrayList<Order>();

		for (SortConstraint sortConstraint : sortConstraints) {
			String propertyName = sortConstraint.getPropertyModel()
					.getPropertyName();
			if (propertyName.equals("gym")) {
				propertyName = propertyName + ".name";
			}
			Direction sortDirection = null;

			switch (sortConstraint.getColumnSort()) {
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

		if (mode.equals(PracticeMode.COACH))
		{
			preparedResults = practiceService.findPracticesForCoach(coachId, startIndex / size, size,
					new Sort(orders));
		}
		else if (mode.equals(PracticeMode.PLAYER))
		{
			preparedResults = practiceService.findPracticesForPlayer(playerId, startIndex / size, size,
					new Sort(orders));
		}
		else
		{
			preparedResults = practiceService.findPractices(startIndex / size, size,
				new Sort(orders));
		}
		this.startIndex = startIndex;
	}

	@Override
	public Object getRowValue(int index) {
		return preparedResults.get(index - startIndex);
	}

	@Override
	public Class<GameDTO> getRowType() {
		return GameDTO.class;
	}

}
