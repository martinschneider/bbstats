package at.basketballsalzburg.bbstats.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.util.AbstractSelectModel;

public class GenericSelectModel<T> extends AbstractSelectModel
{
    private PropertyAdapter labelFieldAdapter;
    private Collection<T> list;

    /**
     * constructor
     * 
     * @param list list of entries
     * @param clazz collection type class
     * @param labelField name of property for the label
     * @param propertyAccess {@link PropertyAccess}
     */
    public GenericSelectModel(final Collection<T> list, final Class<T> clazz,
        final String labelField, final PropertyAccess propertyAccess)
    {
        this.list = list;
        if (!labelField.isEmpty())
        {
            this.labelFieldAdapter = propertyAccess.getAdapter(clazz)
                .getPropertyAdapter(labelField);
        }
    }

    public List<OptionGroupModel> getOptionGroups()
    {
        return null;
    }

    public List<OptionModel> getOptions()
    {
        List<OptionModel> optionModelList = new ArrayList<OptionModel>();
        if (labelFieldAdapter == null)
        {
            for (T obj : list)
            {
                optionModelList.add(new OptionModelImpl(nvl(obj)));
            }
        }
        else
        {
            for (T obj : list)
            {
                optionModelList.add(new OptionModelImpl(nvl(labelFieldAdapter
                    .get(obj)), obj));
            }
        }
        return optionModelList;

    }

    private String nvl(final Object o)
    {
        if (o == null)
        {
            return "";
        }
        else
        {
            return o.toString();
        }
    }
}