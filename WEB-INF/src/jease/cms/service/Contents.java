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
import java.util.List;

import jease.Registry;
import jease.cmf.service.Nodes;
import jease.cms.domain.Content;
import jease.cms.domain.Reference;
import jease.cms.domain.Trash;
import jease.cms.domain.User;
import jfix.db4o.Database;
import jfix.functor.Function;
import jfix.functor.Functors;

public class Contents {

	/**
	 * Return array of all available content types.
	 */
	public static Content[] getAvailableTypes() {
		return Registry.getContents();
	}

	public static String[] getClassNamesForAvailableTypes() {
		return Functors.transform(getAvailableTypes(),
				new Function<Content, String>() {
					public String evaluate(Content content) {
						return content.getClass().getName();
					}
				});
	}

	/**
	 * Checks if content is referenced by Users or References. If no references
	 * exist, content can be deleted without dangling references in database.
	 */
	public static boolean isDeletable(Content content) {
		if (isDeleteGuardedByTrash(content)) {
			return true;
		}
		for (User user : Database.query(User.class)) {
			for (Content folder : user.getRoots()) {
				if (folder.isDescendant(content)) {
					return false;
				}
			}
		}
		for (Reference reference : Database.query(Reference.class)) {
			if (reference.getContent() != null
					&& reference.getContent().isDescendant(content)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isDeleteGuardedByTrash(Content content) {
		return !(content instanceof Trash)
				&& content.getParents(Trash.class).length == 0
				&& ((Content) content.getParent()).getGuard(Trash.class) != null;
	}

	/**
	 * Deletes given content. If a Trash-Object is guarding the given content,
	 * the content will be moved to Trash, otherwise it will be deleted
	 * directly. If the given content is a Trash-Object, the Trash-Object is
	 * deleted only when it is empty, otherwise the Trash will be emptied.
	 */
	public static void delete(Content content) {
		if (content instanceof Trash) {
			Trash trash = (Trash) content;
			if (trash.isEmpty()) {
				Nodes.delete(trash);
			} else {
				trash.empty();
				Nodes.save(trash);
			}
		} else {
			Trash trash = ((Content) content.getParent()).getGuard(Trash.class);
			if (trash == null || content.isDescendant(trash)) {
				Nodes.delete(content);
			} else {
				trash.appendChild(content);
				Nodes.save(trash);
			}
		}
	}

	/**
	 * Returns all descendants for given nodes.
	 */
	public static Content[] getDescendants(Content[] nodes) {
		List<Content> contents = new ArrayList();
		for (Content node : nodes) {
			for (Content content : node.getDescendants(Content.class)) {
				contents.add(content);
			}
		}
		return contents.toArray(new Content[] {});
	}

}