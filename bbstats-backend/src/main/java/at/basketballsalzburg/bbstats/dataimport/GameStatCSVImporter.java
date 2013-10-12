package at.basketballsalzburg.bbstats.dataimport;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import at.basketballsalzburg.bbstats.dto.GameStatDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.services.PlayerService;

/**
 * @author Martin Schneider
 */
public class GameStatCSVImporter {

	private PlayerService playerService;

	public PlayerService getPlayerService() {
		return playerService;
	}

	@Autowired
	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	// TODO: error handling
	public List<GameStatDTO> importCSV(String input) {
		StringBuffer errString = new StringBuffer();
		List<GameStatDTO> gameStats = new ArrayList<GameStatDTO>();

		String[] data = input.split("\n");
		for (String rowData : data) {
			String[] row = rowData.split(";");
			if (row.length != 3) {
				errString.append("ERROR: ");
				errString.append(rowData);
				errString.append(" - invalid number of fields");
				continue;
			}
			GameStatDTO gameStat = new GameStatDTO();
			String names[] = row[0].split(" ");
			if (names.length != 2) {
				errString.append("ERROR: ");
				errString.append(rowData);
				errString.append(" - invalid input data for name");
				continue;
			}
			PlayerDTO player = playerService.findByName(names[1], names[0]);
			if (player == null) {
				errString.append("ERROR: ");
				errString.append(rowData);
				errString.append(" - player not found: ");
				errString.append(row[0].trim());
				continue;
			}
			int zeros = StringUtils.countMatches(row[1], "0");
			int ones = StringUtils.countMatches(row[1], "1");
			int twos = StringUtils.countMatches(row[1], "2");
			int threes = StringUtils.countMatches(row[1], "3");
			gameStat.setFtm(ones);
			gameStat.setFta(zeros + ones);
			gameStat.setPoints(ones + 2 * twos + 3 * threes);
			gameStat.setThrees(threes);

			try {
				gameStat.setFouls(Integer.parseInt(row[2].trim()));
			} catch (NumberFormatException e) {
				errString.append("ERROR: ");
				errString.append(rowData);
				errString.append(" - not a number: ");
				errString.append(row[2].trim());
				continue;
			}
			gameStat.setPlayer(player);

			gameStats.add(gameStat);
			errString.append("\nOK: ");
			errString.append(rowData);
		}
		return gameStats;
	}
}