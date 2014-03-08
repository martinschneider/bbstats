package at.basketballsalzburg.bbstats.services;

import java.io.IOException;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;
import org.joda.time.DateMidnight;
import org.slf4j.Logger;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
public class AppModule {
	public static void bind(ServiceBinder binder) {
	}

	public static void contributeApplicationDefaults(
			MappedConfiguration<String, String> configuration) {
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "de");
		configuration.add(SymbolConstants.PRODUCTION_MODE, "true");
		configuration.add(SymbolConstants.APPLICATION_VERSION, "14.03.08");
	}

	/**
	 * This is a service definition, the service will be named "TimingFilter".
	 * The interface, RequestFilter, is used within the RequestHandler service
	 * pipeline, which is built from the RequestHandler service configuration.
	 * Tapestry IoC is responsible for passing in an appropriate Logger
	 * instance. Requests for static resources are handled at a higher level, so
	 * this filter will only be invoked for Tapestry related requests.
	 * 
	 * <p>
	 * Service builder methods are useful when the implementation is inline as
	 * an inner class (as here) or require some other kind of special
	 * initialization. In most cases, use the static bind() method instead.
	 * 
	 * <p>
	 * If this method was named "build", then the service id would be taken from
	 * the service interface and would be "RequestFilter". Since Tapestry
	 * already defines a service named "RequestFilter" we use an explicit
	 * service id that we can reference inside the contribution method.
	 */
	public RequestFilter buildTimingFilter(final Logger log) {
		return new RequestFilter() {
			public boolean service(Request request, Response response,
					RequestHandler handler) throws IOException {
				long startTime = System.currentTimeMillis();

				try {
					return handler.service(request, response);
				} finally {
					long elapsed = System.currentTimeMillis() - startTime;

					log.info(String.format("Request time: %d ms", elapsed));
				}
			}
		};
	}

	/**
	 * This is a contribution to the RequestHandler service configuration. This
	 * is how we extend Tapestry using the timing filter. A common use for this
	 * kind of filter is transaction management or security. The @Local
	 * annotation selects the desired service by type, but only from the same
	 * module. Without @Local, there would be an error due to the other
	 * service(s) that implement RequestFilter (defined in other modules).
	 */
	public void contributeRequestHandler(
			OrderedConfiguration<RequestFilter> configuration,
			@Local RequestFilter filter) {
		configuration.add("Timing", filter);
	}

	public static void contributeTypeCoercer(
			Configuration<CoercionTuple> configuration) {
		Coercion<String, String[]> coercion = new Coercion<String, String[]>() {
			public String[] coerce(String input) {
				return input.split(",");
			}
		};

		configuration.add(new CoercionTuple<String, String[]>(String.class,
				String[].class, coercion));

		Coercion<java.util.Date, DateMidnight> toDateMidnight = new Coercion<java.util.Date, DateMidnight>() {
			public DateMidnight coerce(java.util.Date input) {
				return new DateMidnight(input);
			}
		};

		configuration.add(new CoercionTuple<java.util.Date, DateMidnight>(
				java.util.Date.class, DateMidnight.class, toDateMidnight));

		Coercion<DateMidnight, java.util.Date> fromDateMidnight = new Coercion<DateMidnight, java.util.Date>() {
			public java.util.Date coerce(DateMidnight input) {
				return input.toDate();
			}
		};

		configuration.add(new CoercionTuple<DateMidnight, java.util.Date>(
				DateMidnight.class, java.util.Date.class, fromDateMidnight));
	}

	public void contributeFactoryDefaults(
			MappedConfiguration<String, String> configuration) {
		configuration.add("shiro.ini.url", "classpath:shiro.ini");
	}

	@Contribute(JavaScriptStackSource.class)
	public static void addJQueryStack(
			MappedConfiguration<String, JavaScriptStack> configuration) {
		configuration.addInstance("bbstatsStack", BbstatsJSStack.class);
	}

	@Contribute(WebSecurityManager.class)
	public static void addRealms(Configuration<Realm> configuration,
			@Value("${shiro.ini.url}") String shiroUrl) {
		IniRealm realm = new IniRealm(shiroUrl);
		HashedCredentialsMatcher hcm = new HashedCredentialsMatcher("SHA-256");
		realm.setCredentialsMatcher(hcm);
		configuration.add(realm);
	}
}
