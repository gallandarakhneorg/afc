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

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;


/** Interface that is representing a list of glyphs
 * associated to a font. 
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see Font
 * @deprecated see JavaFX API
 */
@Deprecated
public interface GlyphList {

	/** Replies the size of the list.
	 * 
	 * @return the number of glyphs in the list.
	 */
	public int size();
	
	/** Replies the font associated to this list of glyphs.
	 * 
	 * @return the font.
	 */
	public Font getFont();
	
	/** Replies the character associated to the glyph at the given index.
	 * 
	 * @param index
	 * @return the character
	 */
	public char getCharAt(int index);
	
	/** Replies the width of the glyph at the given index.
	 * 
	 * @param index
	 * @return the width
	 */
	public float getWidthAt(int index);
	
	/**
     * Returns a shape whose interior corresponds to the
     * visual representation of the specified glyph.
	 *
     * @param index is the index of the glyph.
     * @return a shape
     * @throws IndexOutOfBoundsException if <var>index</var> is
     * outside the bounds of the list of glyphes.
     * @see #getOutlineAt(int, float, float)
     * @see #getBoundsAt(int)
     */
    public Shape2f getOutlineAt(int index);

	/**
     * Returns a shape whose interior corresponds to the
     * visual representation of the specified glyph,
     * offset to x,&nbsp;y.
	 *
     * @param index is the index of the glyph.
     * @param x
     * @param y
     * @return a shape
     * @throws IndexOutOfBoundsException if <var>index</var> is
     * outside the bounds of the list of glyphes.
     * @see #getOutlineAt(int)
     * @see #getBoundsAt(int, float, float)
     */
    public Shape2f getOutlineAt(int index, float x, float y);

	/**
     * Returns a shape whose interior corresponds to the
     * visual representation of all of the glyphes.
	 *
     * @return a shape
     * @see #getBounds()
     * @see #getOutline(float, float)
     */
    public Shape2f getOutline();

    /**
     * Returns a shape whose interior corresponds to the
     * visual representation of all of the glyphes,
     * offset to x,&nbsp;y.
	 *
     * @param x
     * @param y
     * @return a shape
     * @see #getOutline()
     * @see #getBounds(float, float)
     */
    public Shape2f getOutline(float x, float y);

	/**
     * Returns the bounds of the specified glyph.
	 *
     * @param index is the index of the glyph.
     * @return a rectangle
     * @throws IndexOutOfBoundsException if <var>index</var> is
     * outside the bounds of the list of glyphes.
     * @see #getBoundsAt(int, float, float)
     * @see #getOutlineAt(int)
     * @see #getBounds()
     */
    public Rectangle2f getBoundsAt(int index);

	/**
     * Returns the bounds of the specified glyph,
     * offset to x,&nbsp;y.
	 *
     * @param index is the index of the glyph.
     * @param x
     * @param y
     * @return a rectangle
     * @throws IndexOutOfBoundsException if <var>index</var> is
     * outside the bounds of the list of glyphes.
     * @see #getBoundsAt(int)
     * @see #getOutlineAt(int, float, float)
     * @see #getBounds()
     */
    public Rectangle2f getBoundsAt(int index, float x, float y);

	/**
     * Returns the bounds which is enclosing all the glyphes.
	 *
     * @return a shape
     * @see #getBounds(float, float)
     * @see #getOutline()
     */
    public Rectangle2f getBounds();

    /**
     * Returns the bounds which is enclosing all the glyphes,
     * offset to x,&nbsp;y.
	 *
     * @param x
     * @param y
     * @return a rectangle
     * @see #getBounds()
     * @see #getOutline(float, float)
     */
    public Rectangle2f getBounds(float x, float y);

}
