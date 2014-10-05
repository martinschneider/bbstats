package at.basketballsalzburg.bbstats.components;

import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.tynamo.security.components.LoginLink;

import com.jcabi.manifests.Manifests;

import at.basketballsalzburg.bbstats.commons.MenuItem;

public class Menu
{
    @Persist
    private List<MenuItem> menuItems;

    @Parameter
    @Property
    private String homepageURL;

    @Parameter
    @Property
    private String homepageName;

    @Property
    private MenuItem item;

    @Property
    private MenuItem subItem;

    private boolean initialized;

    @Inject
    private Request request;
    
    @Component
    private LoginLink loginLink;

    public List<MenuItem> getMenuItems()
    {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems)
    {
        this.menuItems = menuItems;
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public void setInitialized(boolean initialized)
    {
        this.initialized = initialized;
    }

    public String getVersion()
    {
        return Manifests.read("Version");
    }
}
