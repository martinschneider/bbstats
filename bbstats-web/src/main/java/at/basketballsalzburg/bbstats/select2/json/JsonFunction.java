package at.basketballsalzburg.bbstats.select2.json;

import org.json.JSONString;

/**
 * Represents a Json function. When written out these values are not escaped so its possible to write out raw
 * JavaScript.
 */
public class JsonFunction implements JSONString {
    private final String value;

    public JsonFunction(String value) {
        this.value = value;
    }

    @Override
    public String toJSONString() {
        return value;
    }

}