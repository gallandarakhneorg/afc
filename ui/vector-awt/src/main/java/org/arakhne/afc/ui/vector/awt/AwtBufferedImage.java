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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.arakhne.afc.math.discrete.object2d.Rectangle2i;
import org.arakhne.afc.ui.Graphics2DLOD;
import org.arakhne.afc.ui.StringAnchor;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.ImageObserver;
import org.arakhne.afc.ui.vector.Raster;
import org.arakhne.afc.ui.vector.VectorGraphics2D;

/** AWT implementation of the generic Image.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
class AwtBufferedImage extends BufferedImage implements Image, NativeWrapper {

	/**
	 * @param width is the width of the image.
	 * @param height is the height of the image.
	 * @param isAlpha indicates if the image supports the alpha colors.
	 */
	public AwtBufferedImage(int width, int height, boolean isAlpha) {
		super(width, height, isAlpha ? BufferedImage.TYPE_INT_ARGB
				: BufferedImage.TYPE_INT_RGB);
	}

	@Override
	public int getWidth(ImageObserver observer) {
		return getWidth(observer==null ? null : new AwtImageObserver(observer));
	}

	@Override
	public int getHeight(ImageObserver observer) {
		return getHeight(observer==null ? null : new AwtImageObserver(observer));
	}

	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this);
	}

	@Override
	public int getNumBands() {
		return getSampleModel().getNumBands();
	}

	@Override
	public VectorGraphics2D getVectorGraphics() {
		Graphics2D g2d = (Graphics2D)getGraphics();
		return new AwtVectorGraphics2D<>(
				g2d, Graphics2DLOD.NORMAL_LEVEL_OF_DETAIL,
				StringAnchor.LEFT_BASELINE);
	}

	@Override
	public Raster getData(Rectangle2i area) {
		java.awt.image.Raster awtRaster = getData(
				new Rectangle(
						area.getMinX(),
						area.getMinY(),
						area.getWidth(),
						area.getHeight()));
		return new AwtRaster(awtRaster);
	}

}