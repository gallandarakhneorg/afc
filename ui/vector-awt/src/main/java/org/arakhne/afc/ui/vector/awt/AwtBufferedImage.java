/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
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