package at.basketballsalzburg.bbstats.export;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import at.basketballsalzburg.bbstats.dto.PlayerDTO;

/**
 * @author Martin Schneider
 */
public interface PlayerExporter {

	public InputStream getFile(Set<PlayerDTO> players) throws IOException;
}