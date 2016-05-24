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

package org.arakhne.afc.ui.vector;

import org.arakhne.afc.math.discrete.object2d.Rectangle2i;


/** Interface that is representing a color. 
 * See {@link VectorToolkit} to create an instance.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
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
