package at.basketballsalzburg.bbstats.select2.json;

import java.io.IOException;
import java.io.Writer;

/**
 * A {@link Writer} that writes into a {@link StringBuilder}.
 *
 * @author igor
 *
 */
public class StringBuilderWriter extends Writer {
    private final StringBuilder builder;

    public StringBuilderWriter() {
        this(new StringBuilder());
    }

    public StringBuilderWriter(StringBuilder builder) {
        this.builder = builder;
    }

    public StringBuilder getBuilder() {
        return builder;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        builder.append(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }

}