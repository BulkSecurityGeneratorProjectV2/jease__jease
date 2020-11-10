/*
    Copyright (C) 2009 maik.jablonski@gmail.com

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
package jease.cms.web.content.editor;

import jease.cms.domain.News;
import jease.cms.web.i18n.Strings;
import jfix.zk.*;

import org.zkoss.zk.ui.event.Event;

public class NewsEditor extends ContentEditor<News> {

	Textarea teaser = new Textarea();
	RichTextarea story = new RichTextarea();
	Checkbox emptyTeaser = new Checkbox(Strings.Empty_Teaser);
	Datefield date = new Datefield();
	
	public NewsEditor() {
		emptyTeaser.addCheckListener(new ActionListener() {
			public void actionPerformed(Event event) {
				emptyTeaserChecked(emptyTeaser.isChecked());
			}
		});
	}

	public void init() {
		add(Strings.Teaser, new Column(teaser, emptyTeaser));
		add(Strings.Story, story);
		add(Strings.Date, date);
	}

	public void load() {
		teaser.setText(getNode().getTeaser());
		story.setText(getNode().getStory());
		date.setValue(getNode().getDate());
		emptyTeaserChecked(teaser.isEmpty());
	}

	public void save() {
		getNode().setTeaser(teaser.getText());
		getNode().setStory(story.getText());
		getNode().setDate(date.getDate());
	}

	public void validate() {
		validate(story.isEmpty(), Strings.Story_is_required);
	}

	private void emptyTeaserChecked(boolean empty) {
		if (empty) {
			teaser.setText("");
		}
		teaser.setVisible(!empty);
		emptyTeaser.setChecked(empty);
	}
}
