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

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.ui.vector.Raster;

import android.graphics.Bitmap;

/** Android implementation of the generic raster.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AndroidRaster implements Raster {

	private final Bitmap image;
	private final int x;
	private final int y;
	private final int width;
	private final int height;

	/**
	 * @param img
	 * @param area
	 */
	public AndroidRaster(Bitmap img, Rectangle2f area) {
		this.image = img;
		this.x = (int)area.getMinX();
		this.y = (int)area.getMinY();
		this.width = (int)Math.ceil(area.getWidth());
		this.height = (int)Math.ceil(area.getHeight());
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