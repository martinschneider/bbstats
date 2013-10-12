package at.basketballsalzburg.bbstats.mixins;

import org.apache.shiro.SecurityUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;

/**
 * Mixin to prevent the rendering of a component based on the current user's
 * roles.
 * 
 * @author Martin Schneider
 */
public class Permission {

	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String[] allowedPermissions;

	boolean setupRender(MarkupWriter writer) {
		for (String permissionName : allowedPermissions) {
			if (SecurityUtils.getSubject().isPermitted(permissionName)) {
				return true; // -> begin render
			}
		}
		return false; // -> cleanup render
	}
}
