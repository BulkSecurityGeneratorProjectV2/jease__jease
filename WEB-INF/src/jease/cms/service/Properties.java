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
package jease.cms.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jease.cmf.domain.Node;
import jease.cmf.service.Nodes;
import jease.cms.domain.Content;
import jease.cms.domain.property.LinesProperty;
import jease.cms.domain.property.Property;
import jfix.db4o.Database;
import jfix.functor.Procedure;
import jfix.functor.Supplier;
import jfix.util.Natural;

/**
 * Service for handling properties.
 */
public class Properties {

	private static Supplier<String[]> propertyNames = new Supplier<String[]>() {
		public String[] get() {
			Set<String> result = new HashSet();
			for (Property property : Database.query(Property.class)) {
				result.add(property.getName());
			}
			return Natural.sort(result.toArray(new String[] {}));
		}
	};

	private static Supplier<String[]> providerPaths = new Supplier<String[]>() {
		public String[] get() {
			final List<String> result = new ArrayList();
			Nodes.getRoot().traverse(new Procedure<Node>() {
				public void execute(Node node) {
					Content content = (Content) node;
					if (content.getProperties() != null) {
						for (Property property : content.getProperties()) {
							if (property instanceof LinesProperty) {
								result.add(getPath(content, property));
							}
						}
					}
				}
			});
			return result.toArray(new String[] {});
		}
	};

	/**
	 * Returns all existing property names stored within database.
	 */
	public static String[] getPropertyNames() {
		return Database.query(propertyNames);
	}

	/**
	 * Returns all paths for properties which can act as item providers for
	 * selectable properties.
	 */
	public static String[] getProviderPaths() {
		return Database.query(providerPaths);
	}

	/**
	 * Returns path for given property contained in given content.
	 */
	public static String getPath(Content content, Property property) {
		String path = content.getPath();
		if (path.endsWith("/")) {
			return path + property.getName();
		} else {
			return content.getPath() + "/" + property.getName();
		}
	}

	/**
	 * Returns property for given path. If the path cannot be resolved, null is
	 * returned.
	 */
	public static Property getByPath(String path) {
		int idx = path.lastIndexOf("/");
		String contentPath = path.substring(0, idx);
		String propertyPath = path.substring(idx + 1);
		Content content = (Content) Nodes.getByPath(contentPath);
		if (content != null) {
			return content.getProperty(propertyPath);
		}
		return null;
	}
}
