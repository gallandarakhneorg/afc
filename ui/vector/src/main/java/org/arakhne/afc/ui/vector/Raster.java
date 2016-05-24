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


/** Interface that is representing an image raster. 
 * See {@link VectorToolkit} to create an instance.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
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
