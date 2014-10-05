package at.basketballsalzburg.bbstats.utils;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.util.AbstractSelectModel;

import at.basketballsalzburg.bbstats.dto.TeamDTO;

public class TeamSelectModel extends AbstractSelectModel
{
    private final List<OptionModel> options = CollectionFactory.newList();

    public TeamSelectModel(final List<TeamDTO> dtos)
    {
        Validate.notNull(dtos);
        for (TeamDTO dto : dtos)
        {
            options.add(new OptionModelImpl(dto.getName(), dto.getId()));
        }
    }

    public List<OptionGroupModel> getOptionGroups()
    {
        return null;
    }

    public List<OptionModel> getOptions()
    {
        return options;
    }
}
