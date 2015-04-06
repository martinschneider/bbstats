package at.basketballsalzburg.bbstats.select2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Response that will be sent to Select2 after it queries for matching choices.
 *
 * @author igor
 *
 * @param <T>
 *            type of choice object
 */
public class Response<T> implements Iterable<T> {
    private List<T> results = new ArrayList<T>();
    private Boolean hasMore;

    public Response() {

    }

    /**
     * @return modifiable results collection
     */
    public List<T> getResults() {
        return results;
    }

    /**
     * Sets the results collection. Collection can also be incrementally built using {@link #add(Object)} and
     * {@link #addAll(Collection)} methods.
     *
     * @param results
     * @return {@code this} for chaining
     */
    public Response<T> setResults(List<T> results) {
        this.results = results;
        return this;
    }

    /**
     * @return the {@code mode} flag used to tell Select2 whether or not there are more results available.
     */
    public Boolean getHasMore() {
        return hasMore;
    }

    /**
     * Sets the {@code more} flag used to tell Select2 whether or not there are more results available.
     *
     * @param more
     *            more flag
     * @return {@code this} for chaining
     */
    public Response<T> setHasMore(Boolean more) {
        this.hasMore = more;
        return this;
    }

    /**
     * Adds choices to the collection
     *
     * @param choice
     * @return {@code this} for chaining
     */
    public Response<T> addAll(Collection<? extends T> choice) {
        results.addAll(choice);
        return this;
    }

    /**
     * Adds a choice to the collection
     *
     * @param choice
     * @return {@code this} for chaining
     */
    public <Z extends T> Response<T> add(Z choice) {
        results.add(choice);
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        return results.iterator();
    }

    /**
     * @return number of choices added
     */
    public int size() {
        return results.size();
    }
}