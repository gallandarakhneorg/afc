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

import java.awt.AlphaComposite;

import org.arakhne.afc.ui.vector.Composite;

/** AWT implementation of the generic alpha composite.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
class AwtComposite implements Composite, NativeWrapper {

	private final java.awt.Composite composite;
	
	/**
	 * @param type
	 * @param alpha
	 */
	public AwtComposite(int type, float alpha) {
		this(AlphaComposite.getInstance(type, alpha));
	}
	
	/**
	 * @param composite
	 */
	public AwtComposite(java.awt.Composite composite) {
		this.composite = composite;
	}

	@Override
	public String toString() {
		return this.composite.toString();
	}

	/**
	 * Replies the composite.
	 * 
	 * @return the composite.
	 */
	public java.awt.Composite getComposite() {
		return this.composite;
	}

	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this.composite);
	}

	@Override
	public float getAlpha() {
		if (this.composite instanceof AlphaComposite) {
			return ((AlphaComposite)this.composite).getAlpha();
		}
		return 1f;
	}

}