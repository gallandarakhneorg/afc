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

import org.arakhne.afc.math.discrete.object2d.Rectangle2i;
import org.arakhne.afc.ui.vector.Raster;

import android.graphics.Bitmap;

/** Android implementation of the generic raster.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
class AndroidRaster implements Raster, Cloneable {

	private Bitmap image;
	private final int x;
	private final int y;
	private final int width;
	private final int height;

	/**
	 * @param img
	 * @param area
	 */
	public AndroidRaster(Bitmap img, Rectangle2i area) {
		assert(img!=null);
		this.image = img;
		this.x = area.getMinX();
		this.y = area.getMinY();
		this.width = area.getWidth();
		this.height = area.getHeight();
	}

	@Override
	public AndroidRaster clone() {
		try {
			AndroidRaster r = (AndroidRaster)super.clone();
			r.image = Bitmap.createBitmap(this.image);
			return r;
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	public int getNumBands() {
		return this.image.getByteCount();
	}

	@Override
	public int[] getPixel(int x, int y, int[] samples) {
		int[] tab = samples;
		int size = this.width * this.height;
		if (tab==null || tab.length<size) {
			tab = new int[size];
		}
		this.image.getPixels(
				tab, 0, 0,
				this.x, this.y, this.width, this.height);
		return tab;
	}

	@Override
	public String toString() {
		return this.image.toString();
	}

}