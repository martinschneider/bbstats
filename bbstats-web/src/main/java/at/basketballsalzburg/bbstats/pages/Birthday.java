package at.basketballsalzburg.bbstats.pages;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.TextStreamResponse;

import at.basketballsalzburg.bbstats.services.PlayerService;

public class Birthday
{
    @Inject
    private PlayerService playerService;

    @Inject
    private Messages messages;

    public String getPlayers()
    {
        List<String> birthdayPlayers = playerService.getBirthdays();
        if (birthdayPlayers.isEmpty())
        {
            return messages.get("noBirthdaysToday").concat("\n");
        }
        else if (birthdayPlayers.size() == 1)
        {
            return messages.format("birthdayToday", birthdayPlayers.get(0)).concat("\n");
        }
        else
        {
            String players = StringUtils.join(birthdayPlayers, ", ");
            StringBuilder builder = new StringBuilder(players);
            builder.replace(players.lastIndexOf(","), players.lastIndexOf(",") + 1, " ".concat(messages.get("and")));
            return messages.format("birthdaysToday", builder.toString()).concat("\n");
        }
    }

    Object onActivate()
    {
        return new TextStreamResponse("text/plain", getPlayers());
    }
}
