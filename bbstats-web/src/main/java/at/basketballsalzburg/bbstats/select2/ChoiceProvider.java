package at.basketballsalzburg.bbstats.select2;

import java.util.Collection;

import org.json.JSONException;
import org.json.JSONWriter;

/**
 * <p>
 * Acts as a bridge between Select2 components and the application's domain model.
 * </p>
 * <p>
 * The two important services provided by implementations are:
 * <ul>
 * <li>Feeding choices, represented by the application's domain objects, to Select2 components</li>
 * <li>Transcoding said choices to and from Json so they can consumed by Select2</li>
 * </li>
 * </ul>
 * </p>
 * <p>
 * For the most common usecase where each choice is rendered as a text string see {@link TextChoiceProvider}.
 * </p>
 *
 * @author igor
 *
 * @param <T>
 *            type of choice object
 */
public abstract class ChoiceProvider<T> {
    /**
     * Queries application for choices that match the search {@code term} and adds them to the {@code response}
     *
     * @param term
     *            search term
     * @param page
     *            requested search term results page
     * @param response
     *            aggregate for matching choices as well as other response options
     */
    public abstract void query(String term, int page, Response<T> response);

    /**
     * Converts the specified choice to Json.
     *
     * <p>
     * At the very least each choice should contain an {@code id} attribute. If no custom rendering function is
     * specified, the {@code text} attribute should also be provided
     * </p>
     *
     * <p>
     * Example: If mapping a User {Long id, String name} using default rendering the code should look like this:
     *
     * <pre>
     * toJson(User choice, JSONWriter writer) {
     *     writer.key(&quot;id&quot;).value(choice.getId()).key(&quot;text&quot;).value(choice.getName());
     * }
     * </pre>
     *
     * </p>
     *
     * @param choice
     *            choice to convert
     * @param writer
     *            Json writer that should be used to convert the choice
     * @throws JSONException
     */
    public abstract void toJson(T choice, JSONWriter writer) throws JSONException;

    /**
     * Converts a list of choice ids back into application's choice objects. When the choice provider is attached to a
     * single-select component the {@code ids} collection will contain exactly one id, and a collection containing
     * exactly one choice should be returned.
     *
     * @param ids
     *            collection containing choice ids
     * @return collection containing application choice objects
     */
    public abstract Collection<T> toChoices(Collection<String> ids);

	/**
	 * Converts a list of choice objects back into String representation.
	 *
	 * @param choices
	 *            collection containing choices
	 * @return collection containing application ids of given objects
	 */
	public abstract Collection<String> toIds(Collection<T> choices);
}