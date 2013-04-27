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

import java.awt.AlphaComposite;

import org.arakhne.afc.ui.vector.Composite;

/** AWT implementation of the generic alpha composite.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
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