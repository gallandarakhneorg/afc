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