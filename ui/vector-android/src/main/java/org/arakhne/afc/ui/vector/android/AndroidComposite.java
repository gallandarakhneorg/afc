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

import org.arakhne.afc.ui.vector.Composite;

import android.graphics.Paint;
import android.graphics.Paint.Style;

/** Android implementation of the generic composite.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
class AndroidComposite implements Composite, NativeWrapper, Cloneable {

	private Paint original;

	/**
	 * @param paint
	 */
	public AndroidComposite(Paint paint) {
		assert(paint!=null);
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
	public AndroidComposite clone() {
		try {
			AndroidComposite c = (AndroidComposite)super.clone();
			c.original = new Paint(this.original);
			return c;
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
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