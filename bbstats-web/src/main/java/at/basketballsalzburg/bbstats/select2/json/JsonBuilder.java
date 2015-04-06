package at.basketballsalzburg.bbstats.select2.json;

import org.json.JSONWriter;

/**
 * A JSONWriter that writes and allows access to the underlying {@link StringBuilder}. One of the advantages of this
 * class is that it can expose Json as a {@link CharSequence} instead of a {@link String} so no extra memory allocations
 * are necessary for code that can use a {@link CharSequence}.
 *
 * @author igor
 *
 */
public class JsonBuilder extends JSONWriter {

    /**
     * Constructs a builder with a new {@link StringBuilder}.
     */
    public JsonBuilder() {
        this(new StringBuilder());
    }

    /**
     * Constructs a builder with an existing {@link StringBuilder}.
     *
     * @param builder
     */
    public JsonBuilder(StringBuilder builder) {
        super(new StringBuilderWriter(builder));
    }

    /**
     * @return underlying {@link StringBuilder} as a {@link CharSequence}.
     */
    public CharSequence toJson() {
        return ((StringBuilderWriter) writer).getBuilder();
    }

}