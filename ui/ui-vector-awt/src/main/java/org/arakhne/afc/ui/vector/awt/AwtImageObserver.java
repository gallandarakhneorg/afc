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

import java.awt.Image;

/** AWT implementation of the generic Image.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AwtImageObserver implements java.awt.image.ImageObserver, NativeWrapper {

	private final org.arakhne.afc.ui.vector.ImageObserver obs;
	
	/**
	 * @param obs
	 */
	public AwtImageObserver(org.arakhne.afc.ui.vector.ImageObserver obs) {
		this.obs = obs;
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		int expectedFlags = ALLBITS | WIDTH | HEIGHT;
		boolean loaded = (infoflags&expectedFlags) == expectedFlags;
		if (loaded) {
			if (img instanceof org.arakhne.afc.ui.vector.Image) {
				this.obs.imageUpdate((org.arakhne.afc.ui.vector.Image)img, x, y, width, height);
			}
			else {
				this.obs.imageUpdate(new AwtImage(img), x, y, width, height);
			}
		}
		return !loaded;
	}

	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this.obs);
	}
}