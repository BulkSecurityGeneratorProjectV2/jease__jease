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
package jease.cms.web.role;

import jease.cms.domain.Role;
import jease.cms.service.Contents;
import jease.cms.web.i18n.Strings;
import jfix.db4o.Database;
import jfix.util.Arrays;
import jfix.zk.Checkbox;
import jfix.zk.Checklist;
import jfix.zk.ItemRenderer;
import jfix.zk.ObjectEditor;
import jfix.zk.Scrollbox;
import jfix.zk.Textfield;

public class Editor extends ObjectEditor<Role> {

	Textfield name = new Textfield();
	Checkbox administrator = new Checkbox();
	Checklist types = new Checklist();

	public Editor() {
		types.setItemRenderer(new ItemRenderer() {
			public String render(Object value) {
				return ((String) value).substring(((String) value)
						.lastIndexOf(".") + 1);
			}
		});
	}

	public void init() {
		add(Strings.Name, name);
		add(Strings.Type, new Scrollbox(types));
		add(Strings.Administrator, administrator);
	}

	public void load() {
		name.setText(getObject().getName());
		administrator.setChecked(getObject().isAdministrator());
		types.setSelection(Contents.getClassNamesForAvailableTypes(),
				getObject().getTypes());
	}

	public void save() {
		getObject().setName(name.getText());
		getObject().setAdministrator(administrator.isChecked());
		getObject().setTypes(Arrays.cast(types.getSelected(), String.class));
		Database.save(getObject());
	}

	public void delete() {
		Database.delete(getObject());
	}

	public void validate() {
		validate(name.isEmpty(), Strings.Name_is_required);
	}

}
