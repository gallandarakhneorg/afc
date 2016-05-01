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
package org.arakhne.afc.ui.vector.awt;

import java.awt.Insets;

import org.arakhne.afc.ui.vector.Margins;

/** AWT implementation of the generic dimension.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
class AwtMargins implements Margins, NativeWrapper {

	private float top;
	private float left;
	private float right;
	private float bottom;
	
	/**
	 * @param t
	 * @param l
	 * @param r
	 * @param b
	 */
	public AwtMargins(float t, float l, float r, float b) {
		this.top = t;
		this.left = l;
		this.right = r;
		this.bottom = b;
	}

	@Override
	public float top() {
		return this.top;
	}

	@Override
	public float left() {
		return this.left;
	}
		
	@Override
	public float right() {
		return this.right;
	}

	@Override
	public float bottom() {
		return this.bottom;
	}

	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(new Insets(
				(int)this.top, (int)this.left, (int)this.bottom, (int)this.right));
	}

	@Override
	public void set(float top, float left, float right, float bottom) {
		this.top = top;
		this.left = left;
		this.right = right;
		this.bottom = bottom;
	}

}