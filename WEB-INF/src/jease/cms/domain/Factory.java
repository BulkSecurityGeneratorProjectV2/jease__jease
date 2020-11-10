/*
    Copyright (C) 2011 maik.jablonski@gmail.com

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
package jease.cms.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jease.cmf.domain.Node;
import jease.cmf.domain.NodeException;
import jease.cms.domain.property.Property;

/**
 * A Factory allows to define prototype content-objects which are used to
 * augment existing contents with additional properties.
 */
public class Factory extends Content {

	public Factory() {
	}

	public boolean isContainer() {
		return true;
	}

	public boolean isPage() {
		return false;
	}

	public boolean isPrivileged() {
		return true;
	}

	/**
	 * Returns an array of properties for given content synchronized with
	 * properties from appropriate prototype defined as child within factory.
	 */
	public Property[] getProperties(Content content) {
		for (Content prototype : getChildren(Content.class)) {
			if (prototype.getClass().equals(content.getClass())) {
				return sync(prototype.getProperties(), content.getProperties());
			}
		}
		return content.getProperties();
	}

	private Property[] sync(Property[] prototypes, Property[] clones) {
		Map<Integer, Property> clonesBySerial = new HashMap();
		List<Property> clonesWithoutSerial = new ArrayList();
		if (clones != null) {
			for (Property clone : clones) {
				if (clone.getSerial() != 0) {
					clonesBySerial.put(clone.getSerial(), clone);
				} else {
					clonesWithoutSerial.add(clone);
				}
			}
		}
		List<Property> newClones = new ArrayList();
		for (Property prototype : prototypes) {
			Property clone = clonesBySerial.get(prototype.getSerial());
			if (clone != null) {
				prototype.cloneTo(clone);
				newClones.add(clone);
			} else {
				newClones.add(prototype.copy());
			}
		}
		newClones.addAll(clonesWithoutSerial);
		return newClones.toArray(new Property[] {});
	}

	public Factory copy() {
		return (Factory) super.copy();
	}

	protected void validateDuplicate(Node potentialChild,
			String potentialChildId) throws NodeException {
		super.validateDuplicate(potentialChild, potentialChildId);
		for (Node child : getChildren()) {
			if (child != potentialChild
					&& child.getClass().equals(potentialChild.getClass())) {
				throw new NodeException.IllegalDuplicate();
			}
		}
	}
}