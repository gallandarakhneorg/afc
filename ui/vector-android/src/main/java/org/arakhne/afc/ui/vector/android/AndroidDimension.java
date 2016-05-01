/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
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