/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.attrs.attr;

import java.io.Serializable;

import org.eclipse.xtext.xbase.lib.Pure;


/**
 * Class that is representing a <code>null</code> value
 * for the embedded type of attribute.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class NullAttribute implements Serializable, Cloneable {

	private static final long serialVersionUID = 259205366482306499L;

	private final AttributeType type;

	/** Construct.
	 *
	 * @param type the type.
	 */
	public NullAttribute(AttributeType type) {
		this.type = type;
	}

	/** Replies the type.
	 *
	 * @return the type.
	 */
	@Pure
	public AttributeType getType() {
		return this.type;
	}

	@Pure
	@Override
	public NullAttribute clone() {
		try {
			return (NullAttribute) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof NullAttribute) {
			return this.type == ((NullAttribute) obj).type;
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		return this.type.hashCode();
	}

	@Pure
	@Override
	public String toString() {
		return "null"; //$NON-NLS-1$
	}

}
