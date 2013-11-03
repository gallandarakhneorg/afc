/* 
 * $Id$
 * 
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
package org.arakhne.afc.ui.vector;

import org.arakhne.afc.math.discrete.object2d.Rectangle2i;


/** Interface that is representing a color. 
 * See {@link VectorToolkit} to create an instance.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Image {
	
	/**
     * Determines the width of the image. If the width is not yet known,
     * this method returns <code>-1</code> and the specified
     * <code>ImageObserver</code> object is notified later.
     * @param     observer   an object waiting for the image to be loaded.
     * @return    the width of this image, or <code>-1</code>
     *                   if the width is not yet known.
     */
    public int getWidth(ImageObserver observer);

    /**
     * Determines the height of the image. If the height is not yet known,
     * this method returns <code>-1</code> and the specified
     * <code>ImageObserver</code> object is notified later.
     * @param     observer   an object waiting for the image to be loaded.
     * @return    the height of this image, or <code>-1</code>
     *                   if the height is not yet known.
     */
    public int getHeight(ImageObserver observer);
    
    /** Returns the total number of bands of image data.
     *  @return the number of bands of image data.
     */
    public int getNumBands();
    
    /**
     * Returns an integer pixel in the default RGB color model
     * (TYPE_INT_ARGB) and default sRGB colorspace.
     *
     * @param x the X coordinate of the pixel from which to get
     *          the pixel in the default RGB color model and sRGB
     *          color space
     * @param y the Y coordinate of the pixel from which to get
     *          the pixel in the default RGB color model and sRGB
     *          color space
     * @return an integer pixel in the default RGB color model and
     *          default sRGB colorspace.
     */
    public int getRGB(int x, int y);
    
    /** Replies the graphics context of the image; or <code>null</code>
     * if there is no context available.
     * 
     * @return the graphics context.
     */
    public VectorGraphics2D getVectorGraphics();

    /** Replies the raster of the image for the given area.
     * 
     * @param area is the area for which a raster must be returned.
     * @return the raster.
     */
    public Raster getData(Rectangle2i area);
    
}
