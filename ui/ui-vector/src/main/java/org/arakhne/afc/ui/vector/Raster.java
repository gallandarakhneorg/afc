/* 
 * $Id$
 * 
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
package org.arakhne.afc.ui.vector;


/** Interface that is representing an image raster. 
 * See {@link VectorToolkit} to create an instance.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Raster {

	/** Returns the total number of bands of image data.
     *  @return the number of bands of image data.
     */
    public int getNumBands();
    
    /**
     * Returns the samples in an array of int for the specified pixel.
     * An ArrayIndexOutOfBoundsException may be thrown
     * if the coordinates are not in bounds.  However, explicit bounds
     * checking is not guaranteed.
     * 
     * @param x The X coordinate of the pixel location
     * @param y The Y coordinate of the pixel location
     * @param samples An optionally preallocated int array
     * @return the samples for the specified pixel.
     * @throws ArrayIndexOutOfBoundsException if the coordinates are not
     * in bounds, or if iArray is too small to hold the output.
     */
    public int[] getPixel(int x, int y, int[] samples);
    
}
