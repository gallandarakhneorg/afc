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


/** Interface that is representing a font. 
 * See {@link VectorToolkit} to create an instance.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
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
   
    /** Replies the glyphs for the given text.
     * 
     * @param g is the graphical context.
     * @param text
     * @return the list of glyphs.
     */
    public GlyphList createGlyphList(VectorGraphics2D g, String text);

}
