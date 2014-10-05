package at.basketballsalzburg.bbstats.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@SupportsInformalParameters
@Import(stylesheet = {"css/Box.css"})
public class Box
{
    private static final String CSS_BASE_CLASS = "box";

    @Parameter(name = "class", defaultPrefix = BindingConstants.LITERAL)
    private String cssClass;

    @Parameter(name = "type", defaultPrefix = BindingConstants.LITERAL)
    private String type;

    @Property(write = false)
    @Parameter(required = false, name = "title", defaultPrefix = BindingConstants.LITERAL)
    private String boxTitle;

    @Property(write = false)
    @Parameter(required = false, name = "icon", defaultPrefix = BindingConstants.LITERAL)
    private String boxIcon;

    @Parameter(required = false, name = "alt", defaultPrefix = BindingConstants.LITERAL)
    private String boxIconAltText;

    @Parameter(allowNull = true, required = false, name = "header")
    @Property
    private Block headerBlock;

    @Inject
    private ComponentResources componentResources;

    @Environmental
    private JavaScriptSupport javascriptSupport;

    @Property
    private String clientId;

    void beginRender(final MarkupWriter writer)
    {
        clientId = javascriptSupport.allocateClientId(componentResources);
        writer.element("div", "class", getCssClass(), "id", clientId);
        componentResources.renderInformalParameters(writer);
    }

    void afterRender(final MarkupWriter writer)
    {
        writer.end();
    }

    /**
     * @return CSS base class + optional additional class(es).
     */
    public String getCssClass()
    {
        if (cssClass != null)
        {
            return CSS_BASE_CLASS + " " + cssClass;
        }
        else
        {
            if (!getType().isEmpty())
            {
                return getType() + " " + CSS_BASE_CLASS;
            }
            return CSS_BASE_CLASS;
        }
    }

    /**
     * if the optional boxIconAltText is not set, write empty alt="" title="" attributes to markup, to keep html valid.
     * 
     * @return alt-text for box icon
     */
    public String getBoxIconAltText()
    {
        return (boxIconAltText == null) ? "" : boxIconAltText;
    }

    /**
     * @return type
     */
    public String getType()
    {
        if (type == null)
        {
            return "";
        }
        return type + " ";
    }

}
