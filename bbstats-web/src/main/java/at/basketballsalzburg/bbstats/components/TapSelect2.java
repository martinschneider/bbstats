package at.basketballsalzburg.bbstats.components;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import at.basketballsalzburg.bbstats.select2.Settings;

/**
 * @author Victor Kanopelko
 */
@Import(library = {"js/select2.js"},
        stylesheet = {"css/select2.css","css/select2-bootstrap.css"})
public class TapSelect2 extends TextField {

	@Parameter(required = true)
	private Settings settings;

	@Parameter(allowNull = false, value = "text", defaultPrefix = BindingConstants.LITERAL)
	private String owntype;

	@Parameter(required = true, value = "text", defaultPrefix = BindingConstants.LITERAL)
	private String name;

	@Persist("flash")
	@Property
	private String value;

	@Inject
	private ComponentDefaultProvider defaultProvider;

	@Inject
	private ComponentResources resources;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	public String getSelectedValue() {
		return value;
	}

	public void setSelectedValue(String value) {
		this.value = value;
	}

	Binding defaultValidate() {
		return defaultProvider.defaultValidatorBinding("value", resources);
	}

	@InjectContainer
	private ClientElement container;

	@BeginRender
	void begin(final MarkupWriter writer) {
		resources.renderInformalParameters(writer);
	}

	@AfterRender
	void afterRender() {
		javaScriptSupport.addScript("jQuery('#%s').select2(%s);", container.getClientId(), settings.toJson());
	}

	@Override
	protected void writeFieldTag(MarkupWriter writer, String value) {
		writer.element("input",

				"type", owntype,

				"name", container.getClientId(),

				"id", container.getClientId(),

				"value", value,

				"size", getWidth());
	}
}
