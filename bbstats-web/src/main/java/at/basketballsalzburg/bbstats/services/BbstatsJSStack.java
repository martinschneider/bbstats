package at.basketballsalzburg.bbstats.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

public class BbstatsJSStack implements JavaScriptStack
{

    private final AssetSource assetSource;

    public BbstatsJSStack(final AssetSource assetSource)
    {
        this.assetSource = assetSource;
    }

    public String getInitialization()
    {
        return null;
    }

    public List<Asset> getJavaScriptLibraries()
    {
        List<Asset> assets = new ArrayList<Asset>();
        assets.add(assetSource.getClasspathAsset(
            "META-INF/assets/js/jquery.vegas.js", null));
        assets.add(assetSource.getClasspathAsset(
            "META-INF/assets/js/bootstrap.min.js", null));
        assets.add(assetSource.getClasspathAsset(
                "META-INF/assets/js/chosen.jquery.min.js", null));
        return assets;
    }

    public List<StylesheetLink> getStylesheets()
    {

        return Collections.emptyList();
    }

    public List<String> getStacks()
    {
        return Collections.emptyList();
    }

    @Override
    public List<String> getModules()
    {
        return Collections.emptyList();
    }

}
