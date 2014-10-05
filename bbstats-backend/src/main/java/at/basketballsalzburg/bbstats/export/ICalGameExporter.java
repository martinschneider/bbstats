package at.basketballsalzburg.bbstats.export;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.GregorianCalendar;
import java.util.List;

import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Location;

import org.springframework.beans.factory.annotation.Value;

import at.basketballsalzburg.bbstats.dto.GameDTO;

import com.google.common.base.Joiner;

/**
 * @author Martin Schneider
 */
public class ICalGameExporter implements GameExporter
{

    @Value("${ical.gameDurationHours}")
    private int gameDurationHours = 2;
    @Value("${ical.gameDurationMinutes}")
    private int gameDurationMinutes = 0;
    @Value("${ical.timeZone}")
    private String timeZone = "Europe/Vienna";
    @Value("${ical.encoding}")
    private String encoding = "UTF-8";

    public InputStream getFile(List<GameDTO> games)
    {
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance()
            .createRegistry();
        TimeZone timezone = registry.getTimeZone(timeZone);
        VTimeZone tz = timezone.getVTimeZone();
        net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
        calendar.getProperties().add(CalScale.GREGORIAN);

        for (GameDTO game : games)
        {
            java.util.Calendar startDate = new GregorianCalendar();
            startDate.setTimeZone(timezone);
            startDate.setTime(game.getDateTime());
            java.util.Calendar endDate = new GregorianCalendar();
            endDate.setTimeZone(timezone);
            endDate.setTime(game.getDateTime());
            endDate.add(java.util.Calendar.HOUR_OF_DAY, gameDurationHours);
            endDate.add(java.util.Calendar.MINUTE, gameDurationMinutes);
            StringBuilder eventName = new StringBuilder();
            if (!game.getAgeGroups().isEmpty())
            {
                eventName.append("[");
                eventName.append(Joiner.on(",").join(game.getAgeGroups()));
                eventName.append("] ");
            }
            eventName.append(game.getTeamA().getName());
            eventName.append(" - ");
            eventName.append(game.getTeamB().getName());
            DateTime start = new DateTime(startDate.getTime());
            DateTime end = new DateTime(endDate.getTime());
            VEvent event = new VEvent(start, end, eventName.toString());
            event.getProperties().add(tz.getTimeZoneId());
            event.getProperties().add(
                new Location(game.getGym().getDisplayName()));
            calendar.getComponents().add(event);
        }
        return new ByteArrayInputStream(calendar.toString().getBytes(
            Charset.forName(encoding)));
    }
}
