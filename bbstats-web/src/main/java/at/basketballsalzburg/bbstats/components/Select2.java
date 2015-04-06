package at.basketballsalzburg.bbstats.components;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.TextStreamResponse;
import org.json.JSONException;
import org.json.JSONWriter;

import at.basketballsalzburg.bbstats.select2.AjaxSettings;
import at.basketballsalzburg.bbstats.select2.ChoiceProvider;
import at.basketballsalzburg.bbstats.select2.Response;
import at.basketballsalzburg.bbstats.select2.Settings;
import at.basketballsalzburg.bbstats.select2.json.JsonBuilder;

/**
 * @author Victor Kanopelko
 */
@SupportsInformalParameters
public class Select2 extends AbstractField {

	public static final String COMMA_ESCAPING_CHAR = "&comma;";

	@Parameter
	@Property
	private boolean verbose = false;

	@Parameter(required = true, allowNull = false)
	@Property
	private Settings settings;

	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String type;

	@Parameter(required = true)
	@Property
	private Object singleValue;

	@Parameter(required = true, allowNull = false)
	private ChoiceProvider provider;

	@Parameter(defaultPrefix = BindingConstants.VALIDATE)
	private FieldValidator<Object> validate;

	@Property
	private String value;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@Environmental
	private ValidationDecorator decorator;

	@InjectComponent
	private TapSelect2 select2InputField;

	@InjectComponent
	private Zone ajaxZone;

	@Inject
	private Messages messages;

	@Inject
	private Request request;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private FieldValidationSupport fieldValidationSupport;

	@Inject
	private ComponentDefaultProvider defaultProvider;

	@Environmental
	private ValidationTracker tracker;

	Object onSelect2Changed() {
		value = request.getParameter("param");
		select2InputField.setSelectedValue(value);
		return request.isXHR() ? (verbose ? ajaxZone.getBody() : null) : null;
	}

	StreamResponse onResourceRequested() throws IOException {
		// retrieve choices matching the search term

		String term = request.getParameter("term");

		int page = Integer.valueOf(StringUtils.defaultIfBlank(
				request.getParameter("page"), "1"));
		// select2 uses 1-based paging, but in java world we are used to 0-based
		page -= 1;

		Response response = new Response();
		provider.query(term, page, response);

		// jsonize and write out the choices to the response

		StringWriter out = new StringWriter();
		JSONWriter json = new JSONWriter(out);

		try {
			json.object();
			json.key("results").array();
			for (Object item : response) {
				json.object();
				provider.toJson(item, json);
				json.endObject();
			}
			json.endArray();
			json.key("more").value(response.getHasMore()).endObject();
		} catch (JSONException e) {
			throw new RuntimeException("Could not write Json response", e);
		}

		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(
					"Could not write Json to servlet response", e);
		}
		return new TextStreamResponse("application/json", out.toString());
	}

	public String getSelectedValue() {
		return select2InputField.getSelectedValue();
	}

	// The first state to render a component: perform initialization here
	@SetupRender
	boolean setupRender() {

		decorator.insideField(this);

		String trackedValue = tracker.getInput(this);

		if (trackedValue != null) {
			// If this is a response to a form submission, and the user provided
			// a value.
			// then send that exact value back at them.

			value = trackedValue;
		} else {
			// Otherwise, get the value from the parameter ...
			value = StringUtils
					.join(provider
							.toIds(Collections.singletonList(singleValue)),
							',');
		}

		select2InputField.setSelectedValue(value);

		settings.setMultiple(false);
		settings.setMinimumInputLength(1);

		AjaxSettings ajax = settings.getAjax(true);

		ajax.setData(String
				.format("function(term, page) { return { term: term, page: page, ajax: true }; }"));

		ajax.setResults("function(data, page) { return data; }");

		// configure the localized strings/renderers
		settings.setFormatNoMatches("function() { return '"
				+ getEscapedJsString("noMatches") + "';}");
		settings.setFormatInputTooShort("function(input, min) { return min - input.length == 1 ? '"
				+ getEscapedJsString("inputTooShortSingular")
				+ "' : '"
				+ getEscapedJsString("inputTooShortPlural")
				+ "'.replace('{number}', min - input.length); }");
		settings.setFormatSelectionTooBig("function(limit) { return limit == 1 ? '"
				+ getEscapedJsString("selectionTooBigSingular")
				+ "' : '"
				+ getEscapedJsString("selectionTooBigPlural")
				+ "'.replace('{limit}', limit); }");
		settings.setFormatLoadMore("function() { return '"
				+ getEscapedJsString("loadMore") + "';}");
		settings.setFormatSearching("function() { return '"
				+ getEscapedJsString("searching") + "';}");

		String listenerURI = componentResources.createEventLink(
				"resourceRequested").toAbsoluteURI();
		settings.getAjax().setUrl(listenerURI);

		return true;
	}

	private String getString(String key) {
		return messages.get(key);
	}

	/**
	 * Escapes single quotes in localized strings to be used as JavaScript
	 * strings enclosed in single quotes
	 *
	 * @param key
	 *            resource key for localized message
	 * @return localized string with escaped single quotes
	 */
	protected String getEscapedJsString(String key) {
		String value = getString(key);

		return StringUtils.replace(value, "'", "\\'");
	}

	public Object getConvertedInput(String value) {
		final Collection choices;
		if (StringUtils.isEmpty(value)) {
			choices = new ArrayList();
		} else {
			choices = provider.toChoices(Arrays.asList(value.replaceAll(
					COMMA_ESCAPING_CHAR, ",").split(",")));
		}
		if (choices.iterator().hasNext()) {
			return choices.iterator().next();
		}
		return null;
	}

	@Cached
	public String getAjaxZoneId() {
		return javaScriptSupport.allocateClientId("ajaxZone");
	}

	@AfterRender
	void afterRender() {

		if (provider == null)
			return;

		Object choice = getConvertedInput(value);

		if (null != choice) {

			JsonBuilder selection = new JsonBuilder();

			try {
				selection.object();
				provider.toJson(choice, selection);
				selection.endObject();
			} catch (JSONException e) {
				throw new RuntimeException(
						"Error converting model object to Json", e);
			}
			javaScriptSupport.addScript("jQuery('#%s').select2('data', %s);",
					getClientId(), selection.toJson());
		}
	}

	final Binding defaultValidate() {
		return defaultProvider.defaultValidatorBinding("value",
				componentResources);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void processSubmission(String controlName) {
		String rawValue = request.getParameter(controlName);

		tracker.recordInput(this, rawValue);

		try {
			Object translated = getConvertedInput(rawValue);

			putPropertyNameIntoBeanValidationContext("value");

			fieldValidationSupport.validate(translated, componentResources,
					validate);

			value = rawValue;
			singleValue = translated;
		} catch (ValidationException ex) {
			tracker.recordError(this, ex.getMessage());
		}

		removePropertyNameFromBeanValidationContext();
	}
}