package at.basketballsalzburg.bbstats.services;

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
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;
import org.joda.time.DateMidnight;
import org.tynamo.security.SecuritySymbols;

import at.basketballsalzburg.bbstats.filters.VegasFilter;

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
		configuration.add(SecuritySymbols.LOGIN_URL, "/signin");
		configuration.add(SymbolConstants.HMAC_PASSPHRASE,
				"&Uq-\"(#rRM5qoTB'~VL");
	}

	@Contribute(RequestHandler.class)
	public static void fixRelativeRequests(
			OrderedConfiguration<RequestFilter> configuration) {
		configuration.addInstance("vegasfilter", VegasFilter.class);
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
