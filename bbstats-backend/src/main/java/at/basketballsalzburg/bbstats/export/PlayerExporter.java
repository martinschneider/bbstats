package at.basketballsalzburg.bbstats.export;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import at.basketballsalzburg.bbstats.dto.PlayerDTO;

/**
 * @author Martin Schneider
 */
public interface PlayerExporter {

	public InputStream getFile(List<PlayerDTO> players) throws IOException;
}