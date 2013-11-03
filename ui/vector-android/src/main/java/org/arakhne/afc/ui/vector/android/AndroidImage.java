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

import java.io.InputStream;

import org.arakhne.afc.math.discrete.object2d.Rectangle2i;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.ImageObserver;
import org.arakhne.afc.ui.vector.Raster;
import org.arakhne.afc.ui.vector.VectorGraphics2D;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;

/** Android implementation of the generic image.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AndroidImage implements Image, NativeWrapper, Cloneable {

	private Bitmap image;

	/**
	 * @param img
	 */
	public AndroidImage(Bitmap img) {
		assert(img!=null);
		this.image = img;
	}

	/**
	 * @param stream
	 */
	public AndroidImage(InputStream stream) {
		this.image = BitmapFactory.decodeStream(stream);
	}
	
	/**
	 * @param width
	 * @param height
	 * @param isAlpha
	 */
	public AndroidImage(int width, int height, boolean isAlpha) {
		this.image = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		if (isAlpha) {
			this.image.eraseColor(Color.TRANSPARENT);
		}
		else {
			this.image.eraseColor(Color.WHITE);
		}
	}
	
	@Override
	public AndroidImage clone() {
		try {
			AndroidImage i = (AndroidImage)super.clone();
			i.image = Bitmap.createBitmap(this.image);
			return i;
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	/** Replies the droid bitmap.
	 * 
	 * @return the droid bitmap.
	 */
	public Bitmap getBitmap() {
		return this.image;
	}

	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this.image);
	}

	@Override
	public int getWidth(ImageObserver observer) {
		return this.image.getWidth();
	}

	@Override
	public int getHeight(ImageObserver observer) {
		return this.image.getHeight();
	}

	@Override
	public int getNumBands() {
		return this.image.getByteCount();
	}

	@Override
	public int getRGB(int x, int y) {
		return this.image.getPixel(x, y);
	}

	@Override
	public VectorGraphics2D getVectorGraphics() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Raster getData(Rectangle2i area) {
		return new AndroidRaster(this.image, area);
	}

	@Override
	public String toString() {
		return this.image.toString();
	}

}