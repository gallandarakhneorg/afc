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

package org.arakhne.afc.ui.vector.android;

import org.arakhne.afc.ui.vector.Dimension;
import org.arakhne.afc.util.HashCodeUtil;

/** Android implementation of the generic dimension.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
class AndroidDimension implements Dimension, Cloneable {

	private final float w;
	private final float h;
	
	/**
	 * @param w
	 * @param h
	 */
	public AndroidDimension(float w, float h) {
		this.w = w;
		this.h = h;
	}

	@Override
	public AndroidDimension clone() {
		try {
			return (AndroidDimension)super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	public float width() {
		return this.w;
	}

	@Override
	public float height() {
		return this.h;
	}
		
	@Override
	public String toString() {
		return this.h+";"+this.w; //$NON-NLS-1$
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj==this) return true;
		if (obj instanceof Dimension) {
			Dimension d = (Dimension)obj;
			return d.width()==width() && d.height()==height();
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtil.hash(this.w);
		h = HashCodeUtil.hash(h, this.h);
		return h;
	}

}