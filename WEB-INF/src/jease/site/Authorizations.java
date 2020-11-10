/*
    Copyright (C) 2010 maik.jablonski@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jease.site;

import java.util.HashMap;
import java.util.Map;

import jease.cms.domain.Access;
import jease.cms.domain.Content;
import jfix.db4o.Database;
import jfix.functor.Supplier;
import jfix.util.Crypts;
import jfix.util.Validations;

/**
 * Service for handling authorizations via Access-Objects.
 */
public class Authorizations {

	private static Supplier<Map<Content, Access>> accessByContent = new Supplier<Map<Content, Access>>() {
		public Map<Content, Access> get() {
			return new HashMap<Content, Access>();
		}
	};

	/**
	 * Checks if given content is guarded by Access-Object against
	 * HTTP-Authorization-Header. Returns null if no guard exists or
	 * authorization against guard is successful, otherwise the Access-Guard is
	 * returned, which should force an unauthorized-response.
	 */
	public static Access check(Content content, String authorizationHeader) {
		Access access = getGuard(content);
		if (access != null) {
			String userpass = Crypts
					.decodeBasicAuthorization(authorizationHeader);
			if (userpass == null) {
				return access;
			}
			int index = userpass.indexOf(":");
			String login = userpass.substring(0, index);
			String password = userpass.substring(index + 1);
			if (!(Validations.equals(access.getLogin(), login) && access
					.hasPassword(password))) {
				return access;
			}
		}
		return null;
	}

	/**
	 * Returns guarding Access object for given content or null, if content is
	 * not guarded.
	 */
	public static Access getGuard(Content content) {
		Map<Content, Access> cache = Database.query(accessByContent);
		if (!cache.containsKey(content)) {
			cache.put(content, content.getGuard(Access.class));
		}
		return (Access) cache.get(content);
	}

	/**
	 * Returns true if content is guarded by an Access object.
	 */
	public static boolean isGuarded(Content content) {
		return getGuard(content) != null;
	}

}
