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

/** Interface that is representing a generic margins. 
 * See {@link VectorToolkit} to create an instance.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Margins {

    /**
     * Returns the size of the top margin.
     * 
     * @return the size.
     */
    public float top();

    /**
     * Returns the size of the left margin.
     * 
     * @return the size.
     */
    public float left();

    /**
     * Returns the size of the bottom margin.
     * 
     * @return the size.
     */
    public float bottom();

    /**
     * Returns the size of the right margin.
     * 
     * @return the size.
     */
    public float right();

    /**
     * Set the margins.
     * 
     * @param top
     * @param left
     * @param right
     * @param bottom
     */
    public void set(float top, float left, float right, float bottom);

}
