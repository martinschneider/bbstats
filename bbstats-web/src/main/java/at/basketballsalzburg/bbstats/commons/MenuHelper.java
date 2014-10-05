package at.basketballsalzburg.bbstats.commons;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * Helper class to populate the menu according to the user's permissions
 * 
 * @author Martin Schneider
 */
public class MenuHelper
{

    /**
     * @param parent parent menu item
     * @param key menu item key
     * @param page menu item page
     * @return parent with added child item, if the user has an appropriate permission
     */
    public static MenuItem addItem(MenuItem parent, String key,
        Class<?> page)
    {
        RequiresPermissions annotation = ((RequiresPermissions) page
            .getAnnotation(RequiresPermissions.class));
        if (annotation == null)
        {
            parent.addItem(new MenuItem(key, page.getSimpleName(), false));
        }
        else
        {
            for (String permission : annotation.value())
            {
                if (SecurityUtils.getSubject().isPermitted(permission))
                {
                    parent.addItem(new MenuItem(key, page.getSimpleName(), false));
                }
            }
        }
        return parent;
    }

    /**
     * @param parent list of menu items
     * @param key menu item key
     * @param page menu item page
     * @return parent with added menu item, if the user has an appropriate permission
     */
    public static List<MenuItem> addItem(List<MenuItem> parent, String key,
        Class<?> page)
    {
        RequiresPermissions annotation = ((RequiresPermissions) page
            .getAnnotation(RequiresPermissions.class));
        if (annotation == null)
        {
            parent.add(new MenuItem(key, page.getSimpleName(), false));
        }
        else
        {
            for (String permission : annotation.value())
            {
                if (SecurityUtils.getSubject().isPermitted(permission))
                {
                    parent.add(new MenuItem(key, page.getSimpleName(), false));
                }
            }
        }
        return parent;
    }
}
