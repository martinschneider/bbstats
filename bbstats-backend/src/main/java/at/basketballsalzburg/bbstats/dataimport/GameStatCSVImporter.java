package at.basketballsalzburg.bbstats.dataimport;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.dto.GameStatDTO;
import at.basketballsalzburg.bbstats.dto.PlayerDTO;
import at.basketballsalzburg.bbstats.services.GameService;
import at.basketballsalzburg.bbstats.services.PlayerService;

public class GameStatCSVImporter {

	private PlayerService playerService;

	private GameService gameService;

	public GameService getGameService() {
		return gameService;
	}

	@Autowired
	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}

	public PlayerService getPlayerService() {
		return playerService;
	}

	@Autowired
	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	public String importCSV(String input) throws ImportException {
		StringBuffer retString = new StringBuffer();
		
		String[] data = input.split("\n");
		for (String rowData : data) {
			String[] row = rowData.split(";");
			if (row.length != 4) {
				throw new ImportException("Invalid input data");
			}
			GameStatDTO gameStat = new GameStatDTO();
			GameDTO game = gameService.findById(Long.parseLong(row[0].trim()));
			if (game == null) {
				throw new ImportException("Game not found: " + row[0].trim());
			}
			String names[] = row[1].split(" ");
			if (names.length != 2) {
				throw new ImportException("Invalid input data");
			}
			PlayerDTO player = playerService.findByName(names[1], names[0]);
			if (player == null) {
				throw new ImportException("Player not found: " + row[1]);
			}
			int zeros = StringUtils.countMatches(row[2], "0");
			int ones = StringUtils.countMatches(row[2], "1");
			int twos = StringUtils.countMatches(row[2], "2");
			int threes = StringUtils.countMatches(row[2], "3");
			gameStat.setFtm(ones);
			gameStat.setFta(zeros + ones);
			gameStat.setPoints(ones + 2 * twos + 3 * threes);
			gameStat.setThrees(threes);

			gameStat.setFouls(Integer.parseInt(row[3].trim()));
			gameStat.setPlayer(player);
			gameStat.setGame(game);

			game.addStat(gameStat);
			gameService.save(game);
			retString.append("\nOK: ");
			retString.append(rowData);
		}
		return retString.toString();
	}
}
