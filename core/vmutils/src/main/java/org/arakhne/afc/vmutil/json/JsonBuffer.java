/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.vmutil.json;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Basic Json buffer.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class JsonBuffer {

	private static final String INDENT_STRING = "\t"; //$NON-NLS-1$

	private final Map<String, Object> content = new TreeMap<>();

	/** Add the given value.
	 *
	 * @param name the name.
	 * @param value the value.
	 */
	public void add(String name, Object value) {
		this.content.put(name, value);
	}

	/** Add the given value.
	 *
	 * @param name the name.
	 * @param value the value.
	 */
	public void add(String name, Iterable<?> value) {
		this.content.put(name, value);
	}

	/** Add the given value.
	 *
	 * @param name the name.
	 * @param value the value.
	 */
	public void add(String name, Map<?, ?> value) {
		this.content.put(name, value);
	}

	/** Add the given value.
	 *
	 * @param name the name.
	 * @param value the value.
	 */
	public void add(String name, JsonBuffer value) {
		this.content.put(name, value);
	}

	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		toString(buffer, 0, this.content);
		return buffer.toString();
	}

	private static void toString(StringBuilder buffer, int indent, Map<?, ?> map) {
		buffer.append("{\n"); //$NON-NLS-1$
		boolean first = true;
		for (final Entry<?, ?> entry : map.entrySet()) {
			if (first) {
				first = false;
			} else {
				buffer.append(",\n"); //$NON-NLS-1$
			}
			doIndent(buffer, indent + 1);
			buffer.append("\""); //$NON-NLS-1$
			buffer.append(entry.getKey());
			buffer.append("\": "); //$NON-NLS-1$
			final Object value = entry.getValue();
			valueToString(buffer, indent, value);
		}
		buffer.append("\n"); //$NON-NLS-1$
		doIndent(buffer, indent);
		buffer.append("}"); //$NON-NLS-1$
	}

	private static void toString(StringBuilder buffer, int indent, Iterable<?> iterable) {
		buffer.append("[\n"); //$NON-NLS-1$
		boolean first = true;
		for (final Object value : iterable) {
			if (first) {
				first = false;
			} else {
				buffer.append(",\n"); //$NON-NLS-1$
			}
			valueToString(buffer, indent, value);
		}
		buffer.append("\n"); //$NON-NLS-1$
		doIndent(buffer, indent);
		buffer.append("]"); //$NON-NLS-1$
	}

	private static void valueToString(StringBuilder buffer, int indent, Object value) {
		doIndent(buffer, indent + 1);
		if (value instanceof JsonBuffer) {
			toString(buffer, indent + 1, ((JsonBuffer) value).content);
		} else if (value instanceof Map<?, ?>) {
			toString(buffer, indent + 1, (Map<?, ?>) value);
		} else if (value instanceof Iterable<?>) {
			toString(buffer, indent + 1, (Iterable<?>) value);
		} else {
			buffer.append(Objects.toString(value));
		}
	}

	private static void doIndent(StringBuilder buffer, int level) {
		for (int i = 0; i < level; ++i) {
			buffer.append(INDENT_STRING);
		}
	}
}
