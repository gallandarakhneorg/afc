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
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
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