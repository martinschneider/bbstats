package at.basketballsalzburg.bbstats.export;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import at.basketballsalzburg.bbstats.dto.GameDTO;

/**
 * @author Martin Schneider
 */
public interface GameExporter {
	public InputStream getFile(List<GameDTO> games) throws IOException;
}
