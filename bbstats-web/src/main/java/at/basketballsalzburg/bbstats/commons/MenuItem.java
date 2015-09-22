package at.basketballsalzburg.bbstats.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Menu item
 * 
 * @author Martin Schneider
 */
public class MenuItem implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	String key;
    String page;
    boolean group;
    List<MenuItem> items;

    public MenuItem(String key, String page, boolean group)
    {
        super();
        this.key = key;
        this.page = page;
        this.group = group;
        if (group)
        {
            items = new ArrayList<MenuItem>();
        }
    }

    public boolean isGroup()
    {
        return group;
    }

    public void setGroup(boolean group)
    {
        this.group = group;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getPage()
    {
        return page;
    }

    public void setPage(String page)
    {
        this.page = page;
    }

    public List<MenuItem> getItems()
    {
        return items;
    }

    public void setItems(List<MenuItem> items)
    {
        this.items = items;
    }

    public void addItem(MenuItem item)
    {
        items.add(item);
    }
}
