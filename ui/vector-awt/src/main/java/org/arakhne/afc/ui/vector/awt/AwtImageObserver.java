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

import java.awt.Image;

/** AWT implementation of the generic Image.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
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