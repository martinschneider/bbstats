package at.basketballsalzburg.bbstats.select2.json;

import org.json.JSONException;
import org.json.JSONWriter;

/**
 * Json utilities
 */
public class Json {
    private Json() {
    };

    /**
     * Writes a key/value pair into the {@code writer} if the value is not {@code null}
     *
     * @param writer
     *            json writer
     * @param key
     *            key
     * @param value
     *            value
     * @throws JSONException
     */
    public static void writeObject(JSONWriter writer, String key, Object value) throws JSONException {
        if (value != null) {
            writer.key(key);
            writer.value(value);
        }
    }

    /**
     * Writes a key/value pair into the {@code writer} where {@code value} represents a javascript function and should
     * be written out unencoded if the value is not {@code null}
     *
     * @param writer
     *            json writer
     * @param key
     *            key
     * @param value
     *            value
     * @throws JSONException
     */
    public static void writeFunction(JSONWriter writer, String key, String value) throws JSONException {
        if (value != null) {
            writer.key(key).value(new JsonFunction(value));
        }
    }

}