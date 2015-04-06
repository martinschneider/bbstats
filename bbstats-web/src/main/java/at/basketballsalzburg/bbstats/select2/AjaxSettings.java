package at.basketballsalzburg.bbstats.select2;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONWriter;

import at.basketballsalzburg.bbstats.select2.json.Json;

/**
 * Select2 Ajax settings. Refer to the Select2 documentation for what these options mean.
 *
 * @author igor
 */
public final class AjaxSettings implements Serializable {
    private CharSequence url;
    private String dataType = "json";
    private int quietMillis = 100;
    private String data;
    private String results;
    /**
     * whether or not to use traditional parameter encoding.
     */
    private Boolean traditional;

    void toJson(JSONWriter writer) throws JSONException {
        writer.object();
        Json.writeFunction(writer, "data", data);
        Json.writeObject(writer, "dataType", dataType);
        Json.writeObject(writer, "quietMillis", quietMillis);
        Json.writeFunction(writer, "results", results);
        Json.writeObject(writer, "url", url);
        Json.writeObject(writer, "traditional", traditional);
        writer.endObject();
    }

    public void setUrl(CharSequence url) {
        this.url = url;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setQuietMillis(int quietMillis) {
        this.quietMillis = quietMillis;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public CharSequence getUrl() {
        return url;
    }

    public String getDataType() {
        return dataType;
    }

    public int getQuietMillis() {
        return quietMillis;
    }

    public String getData() {
        return data;
    }

    public String getResults() {
        return results;
    }

    public boolean isTraditional() {
        return traditional;
    }

    public void setTraditional(boolean traditional) {
        this.traditional = traditional;
    }

}