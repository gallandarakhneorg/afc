/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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
package org.arakhne.afc.ui.vector.awt;

import org.arakhne.afc.ui.awt.FloatDimension;
import org.arakhne.afc.ui.vector.Dimension;

/** AWT implementation of the generic dimension.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AwtDimension extends FloatDimension implements Dimension, NativeWrapper {

	/**
	 * @param w
	 * @param h
	 */
	public AwtDimension(float w, float h) {
		super(w, h);
	}

	@Override
	public float width() {
		return (float)getWidth();
	}

	@Override
	public float height() {
		return (float)getHeight();
	}
		
	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj==this) return true;
		if (obj instanceof Dimension) {
			Dimension d = (Dimension)obj;
			return d.width()==getWidth() && d.height()==getHeight();
		}
		return super.equals(obj);
	}
	
}