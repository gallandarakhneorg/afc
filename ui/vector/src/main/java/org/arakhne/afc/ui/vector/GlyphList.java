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
