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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.ui.Graphics2DLOD;
import org.arakhne.afc.ui.awt.DefaultLODGraphics2D;
import org.arakhne.afc.ui.awt.LODGraphics2D;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.ImageObserver;
import org.arakhne.afc.ui.vector.Raster;
import org.arakhne.afc.ui.vector.VectorGraphics2D;

/** AWT implementation of the generic Image.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
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
		LODGraphics2D lg = new DefaultLODGraphics2D(g2d, null, Graphics2DLOD.HIGH_LEVEL_OF_DETAIL);
		return new DelegatedVectorGraphics2D<LODGraphics2D>(lg);
	}

	@Override
	public Raster getData(Rectangle2f area) {
		java.awt.image.Raster awtRaster = getData(
				new Rectangle(
						(int)area.getMinX(),
						(int)area.getMinY(),
						(int)area.getWidth(),
						(int)area.getHeight()));
		return new AwtRaster(awtRaster);
	}

}