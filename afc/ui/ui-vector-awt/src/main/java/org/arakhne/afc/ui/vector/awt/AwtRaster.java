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

import org.arakhne.afc.ui.vector.Raster;

/** AWT implementation of the generic raster.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AwtRaster implements Raster, NativeWrapper {

	private final java.awt.image.Raster raster;
	
	/**
	 * @param raster
	 */
	public AwtRaster(java.awt.image.Raster raster) {
		this.raster = raster;
	}
	
	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this.raster);
	}

	@Override
	public int getNumBands() {
		return this.raster.getNumBands();
	}

	@Override
	public int[] getPixel(int x, int y, int[] samples) {
		return this.raster.getPixel(x, y, samples);
	}

}