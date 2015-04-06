package at.basketballsalzburg.bbstats.select2;

import org.json.JSONException;
import org.json.JSONWriter;

/**
 * Takes care of Json serialization for the most common usecase where each choice is rendered as a text string.
 *
 * @author igor
 *
 * @param <T> type of choice object
 */
public abstract class TextChoiceProvider<T> extends ChoiceProvider<T> {

    protected abstract String getDisplayText(T choice);

    protected abstract Object getId(T choice);

    @Override
    public final void toJson(T choice, JSONWriter writer) throws JSONException {
        writer.key("id").value(getId(choice)).key("text").value(getDisplayText(choice));
    }

}