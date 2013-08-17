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
class AwtImage implements Image, NativeWrapper {

	private final java.awt.Image image;
	
	/**
	 * @param image
	 */
	public AwtImage(java.awt.Image image) {
		this.image = image;
	}

	@Override
	public int getWidth(ImageObserver observer) {
		return this.image.getWidth(observer==null ? null : new AwtImageObserver(observer));
	}

	@Override
	public int getHeight(ImageObserver observer) {
		return this.image.getHeight(observer==null ? null : new AwtImageObserver(observer));
	}
	
	/** Replies the AWT image.
	 * 
	 * @return the AWT image.
	 */
	public java.awt.Image getImage() {
		return this.image;
	}

	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this.image);
	}

	@Override
	public int getNumBands() {
		if (this.image instanceof BufferedImage) {
			return ((BufferedImage)this.image).getSampleModel().getNumBands();
		}
		return 4;
	}

	@Override
	public int getRGB(int x, int y) {
		if (this.image instanceof BufferedImage) {
			return ((BufferedImage)this.image).getRGB(x, y);
		}
		return 0x0;
	}

	@Override
	public VectorGraphics2D getVectorGraphics() {
		Graphics2D g2d = (Graphics2D)this.image.getGraphics();
		LODGraphics2D lg = new DefaultLODGraphics2D(g2d, null, Graphics2DLOD.HIGH_LEVEL_OF_DETAIL);
		return new DelegatedVectorGraphics2D<LODGraphics2D>(lg);
	}

	@Override
	public Raster getData(Rectangle2f area) {
		if (this.image instanceof BufferedImage) {
			BufferedImage bimg = (BufferedImage)this.image;
			java.awt.image.Raster awtRaster = bimg.getData(
					new Rectangle(
							(int)area.getMinX(),
							(int)area.getMinY(),
							(int)area.getWidth(),
							(int)area.getHeight()));
			return new AwtRaster(awtRaster);
		}
		throw new UnsupportedOperationException();
	}

}