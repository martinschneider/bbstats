package at.basketballsalzburg.bbstats.export;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import at.basketballsalzburg.bbstats.dto.AgeGroupDTO;
import at.basketballsalzburg.bbstats.dto.GameDTO;
import at.basketballsalzburg.bbstats.dto.GymDTO;
import at.basketballsalzburg.bbstats.dto.TeamDTO;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

public class ICalGameExporterTest
{

    private static final String EXPECTED_RESULT = "BEGIN:VCALENDAR"
        + "\r\nCALSCALE:GREGORIAN"
        + "\r\nBEGIN:VEVENT"
        + "\r\nDTSTART:20130101T200000"
        + "\r\nDTEND:20130101T220000"
        + "\r\nSUMMARY:[WU17\\,WU15] BSC Salzburg - BBU Salzburg"
        + "\r\nTZID:Europe/Vienna" + "\r\nLOCATION:Salzburg\\, Riedenburghalle"
        + "\r\nEND:VEVENT" + "\r\nBEGIN:VEVENT"
        + "\r\nDTSTART:20130101T180000" + "\r\nDTEND:20130101T200000"
        + "\r\nSUMMARY:[WU13] BBU Salzburg - BSC Salzburg"
        + "\r\nTZID:Europe/Vienna" + "\r\nLOCATION:Salzburg\\, Riedenburghalle"
        + "\r\nEND:VEVENT" + "\r\nEND:VCALENDAR\r\n";

    //@Test
    public void testGameExport() throws IOException
    {
        ICalGameExporter target = new ICalGameExporter();
        List<GameDTO> games = new ArrayList<GameDTO>();
        GameDTO game1 = new GameDTO();
        GameDTO game2 = new GameDTO();
        AgeGroupDTO ageGroup1 = new AgeGroupDTO();
        ageGroup1.setName("WU17");
        AgeGroupDTO ageGroup2 = new AgeGroupDTO();
        ageGroup2.setName("WU15");
        AgeGroupDTO ageGroup3 = new AgeGroupDTO();
        ageGroup3.setName("WU13");
        TeamDTO teamA = new TeamDTO();
        teamA.setName("BSC Salzburg");
        TeamDTO teamB = new TeamDTO();
        teamB.setName("BBU Salzburg");
        GymDTO gym = new GymDTO();
        gym.setName("Riedenburghalle");
        gym.setCity("Salzburg");
        game1.setDateTime(new DateTime(2013, 1, 1, 19, 0, 0, DateTimeZone.UTC).toDate());
        game1.setTeamA(teamA);
        game1.setTeamB(teamB);
        game1.setAgeGroups(Arrays.asList(ageGroup1, ageGroup2));
        game1.setGym(gym);

        game2.setDateTime(new DateTime(2013, 1, 1, 17, 0, 0, DateTimeZone.UTC).toDate());
        game2.setTeamA(teamB);
        game2.setTeamB(teamA);
        game2.setAgeGroups(Arrays.asList(ageGroup3));
        game2.setGym(gym);

        games.add(game1);
        games.add(game2);

        assertEquals(EXPECTED_RESULT,
            CharStreams.toString(new InputStreamReader(
                target.getFile(games), Charsets.UTF_8)).replaceAll("DTSTAMP:.*\\s+", ""));
    }
}
