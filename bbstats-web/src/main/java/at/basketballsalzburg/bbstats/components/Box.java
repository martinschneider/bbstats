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
@Import(stylesheet = { "Box.css" })
public class Box {
	/** default css class used to render the box */
	private static final String CSS_BASE_CLASS = "box";

	/*******************************************************************************************************************
	 * Parameter
	 ******************************************************************************************************************/

	/** additional css class(es) passed in as parameter */
	@Parameter(name = "class", defaultPrefix = BindingConstants.LITERAL)
	private String cssClass;

	/**
	 * type (use predefined cssClass(es) (defined in SimpleBox.css), ex. type =
	 * inner, type = important, ..."
	 */
	@Parameter(name = "type", defaultPrefix = BindingConstants.LITERAL)
	private String type;

	/** caption */
	@Property(write = false)
	@Parameter(required = false, name = "title", defaultPrefix = BindingConstants.LITERAL)
	private String boxTitle;

	/** small icon displayed in front of the caption */
	@Property(write = false)
	@Parameter(required = false, name = "icon", defaultPrefix = BindingConstants.LITERAL)
	private String boxIcon;

	/** alt text, title text for the box-icon */
	@Parameter(required = false, name = "alt", defaultPrefix = BindingConstants.LITERAL)
	private String boxIconAltText;

	/** header block, used to pass for ex. edit links to the box-header */
	@Parameter(allowNull = true, required = false, name = "header")
	@Property
	private Block headerBlock;

	/*******************************************************************************************************************
	 * common
	 ******************************************************************************************************************/

	@Inject
	private ComponentResources componentResources;

	@Environmental
	private JavaScriptSupport javascriptSupport;

	@Property
	private String clientId;

	/*******************************************************************************************************************
	 * methods
	 ******************************************************************************************************************/

	/**
	 * render informal parameters, clientId, etc.
	 * 
	 * @param writer
	 */
	void beginRender(final MarkupWriter writer) {
		clientId = javascriptSupport.allocateClientId(componentResources);
		writer.element("div", "class", getCssClass(), "id", clientId);
		componentResources.renderInformalParameters(writer);
	}

	/**
	 * @param writer
	 */
	void afterRender(final MarkupWriter writer) {
		writer.end();
	}

	/*******************************************************************************************************************
	 * getter & setter
	 ******************************************************************************************************************/
	/**
	 * @return CSS base class + optional additional class(es).
	 */
	public String getCssClass() {
		if (cssClass != null) {
			return CSS_BASE_CLASS + " " + cssClass;
		} else {
			if (!getType().isEmpty()) // type defined
			{
				return getType() + " " + CSS_BASE_CLASS;
			}
			return CSS_BASE_CLASS; // no type, no additional class defined
		}
	}

	/**
	 * if the optional boxIconAltText is not set, write empty alt="" title=""
	 * attributes to markup, to keep html valid.
	 * 
	 * @return alt-text for box icon
	 */
	public String getBoxIconAltText() {
		return (boxIconAltText == null) ? "" : boxIconAltText;
	}

	/**
	 * @return type
	 */
	public String getType() {
		if (type == null) {
			return "";
		}
		return type + " ";
	}

}
