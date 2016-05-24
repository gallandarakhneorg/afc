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