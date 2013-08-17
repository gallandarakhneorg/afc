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


/** Interface that is representing a font. 
 * See {@link VectorToolkit} to create an instance.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Font {
	
	/**
     * Returns the family name of this <code>Font</code>.
     *
     * <p>The family name of a font is font specific. Two fonts such as
     * Helvetica Italic and Helvetica Bold have the same family name,
     * <i>Helvetica</i>, whereas their font face names are
     * <i>Helvetica Bold</i> and <i>Helvetica Italic</i>.
     *
     * <p>Use <code>getName</code> to get the logical name of the font.
     * Use <code>getFontName</code> to get the font face name of the font.
     * 
     * @return a <code>String</code> that is the family name of this
     *          <code>Font</code>.
     */
    public String getFamily();
    
    /**
     * Returns the font face name of this <code>Font</code>.  For example,
     * Helvetica Bold could be returned as a font face name.
     * Use <code>getFamily</code> to get the family name of the font.
     * Use <code>getName</code> to get the logical name of the font.
     * 
     * <p>The family name of a font is font specific. Two fonts such as
     * Helvetica Italic and Helvetica Bold have the same family name,
     * <i>Helvetica</i>, whereas their font face names are
     * <i>Helvetica Bold</i> and <i>Helvetica Italic</i>.
     * 
     * @return a <code>String</code> representing the font face name of
     *          this <code>Font</code>.
     */
    public String getFontName();
    
    /**
     * Returns the logical name of this <code>Font</code>.
     * Use <code>getFamily</code> to get the family name of the font.
     * Use <code>getFontName</code> to get the font face name of the font.
     * 
     * @return a <code>String</code> representing the logical name of
     *          this <code>Font</code>.
     */
    public String getName();
    
    /**
     * Returns the postscript name of this <code>Font</code>.
     * Use <code>getFamily</code> to get the family name of the font.
     * Use <code>getFontName</code> to get the font face name of the font.
     * This function may reply one of the Java logical fonts:
     * {@code Dialog}, {@code DialogInput}, {@code Monospaced},
     * {@code Serif}, or {@code SansSerif}.
     * 
     * @return a <code>String</code> representing the postscript name of
     *          this <code>Font</code>.
     * @see #getPhysicalPSName()       
     */
    public String getPSName();
    
    /**
     * Returns the postscript name of the physical font behind
     * this <code>Font</code>.
     * This function never replies the names of the Java logical
     * fonts.
     * Use <code>getFamily</code> to get the family name of the font.
     * Use <code>getFontName</code> to get the font face name of the font.
     * @return a <code>String</code> representing the postscript name of
     *          this <code>Font</code>.
     * @see #getPSName()
     */
    public String getPhysicalPSName();

    /**
     * Returns the point size of this <code>Font</code> in
     * <code>float</code> value.
     * 
     * @return the point size of this <code>Font</code> as a
     * <code>float</code> value.
     */
    public float getSize();
    
    /**
     * Indicates whether or not this <code>Font</code> object's style is
     * PLAIN.
     * 
     * @return    <code>true</code> if this <code>Font</code> has a
     *            PLAIN sytle;
     *            <code>false</code> otherwise.
     */
    public boolean isPlain();

    /**
     * Indicates whether or not this <code>Font</code> object's style is
     * BOLD.
     * 
     * @return    <code>true</code> if this <code>Font</code> object's
     *            style is BOLD;
     *            <code>false</code> otherwise.
     */
    public boolean isBold();

    /**
     * Indicates whether or not this <code>Font</code> object's style is
     * ITALIC.
     * 
     * @return    <code>true</code> if this <code>Font</code> object's
     *            style is ITALIC;
     *            <code>false</code> otherwise.
     */
    public boolean isItalic();
    
    /**
     * Creates a new <code>Font</code> object by replicating the current
     * <code>Font</code> object and applying a new size to it.
     * 
     * @param size the size for the new <code>Font</code>.
     * @return a new <code>Font</code> object.
     */
    public Font deriveFont(float size);

    /**
     * Creates a new <code>Font</code> object by replicating this
     * <code>Font</code> object and applying a new style and size.
     * 
     * @param style the style for the new <code>Font</code>
     * @param size the size for the new <code>Font</code>
     * @return a new <code>Font</code> object.
     */
    public Font deriveFont(FontStyle style, float size);
    
    /**
     * Creates a new <code>Font</code> object by replicating this
     * <code>Font</code> object and applying a new style and size.
     * 
     * @param style the style for the new <code>Font</code>
     * @return a new <code>Font</code> object.
     */
    public Font deriveFont(FontStyle style);

    /** Replies the bounds of the given font when it is drawn with the given font.
     * 
     * @param str
     * @return the bounds
     */
    public Rectangle2f getStringBounds(String str);

    /**
     * Returns the italic angle of this <code>Font</code>.  The italic angle
     * is the inverse slope of the caret which best matches the posture of this
     * <code>Font</code>.
     * @return the angle of the ITALIC style of this <code>Font</code>.
     */
    public float getItalicAngle();
    
    /** Replies the glyphs for the given characters.
     * 
     * @param g is the graphical context.
     * @param characters
     * @return the list of glyphs.
     */
    public GlyphList createGlyphList(VectorGraphics2D g, char... characters);
   
}
