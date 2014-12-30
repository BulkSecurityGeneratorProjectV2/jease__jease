/*
    Copyright (C) 2015 maik.jablonski@jease.org

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
package jfix.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

public class ObjectCollector<E> extends Collector {

	private List<E> input;
	private List<E> output;
	
	public ObjectCollector(List<E> objects) {
		this.input = objects;
		this.output = new ArrayList<E>();
	}
	
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}

	public void collect(int index) throws IOException {
		output.add(input.get(index));
	}

	public void setNextReader(IndexReader indexReader, int base)
			throws IOException {
	}

	public void setScorer(Scorer scorer) throws IOException {
	}
	
	public List<E> getOutput() {
		return output;
	}
	
}
