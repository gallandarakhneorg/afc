/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
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
package org.arakhne.afc.ui.vector.android;

import org.arakhne.afc.ui.vector.Composite;

import android.graphics.Paint;
import android.graphics.Paint.Style;

/** Android implementation of the generic composite.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AndroidComposite implements Composite, NativeWrapper {

	private final Paint original;

	/**
	 * @param paint
	 */
	public AndroidComposite(Paint paint) {
		this.original = paint;
	}

	/**
	 * @param alpha
	 */
	public AndroidComposite(float alpha) {
		this.original = new Paint();
		this.original.setStyle(Style.FILL_AND_STROKE);
		this.original.setAlpha((int)(alpha * 255));
	}

	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this.original);
	}

	@Override
	public float getAlpha() {
		return this.original.getAlpha() / 255;
	}

	@Override
	public String toString() {
		return this.original.toString();
	}

}