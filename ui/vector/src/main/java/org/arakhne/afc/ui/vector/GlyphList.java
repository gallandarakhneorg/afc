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




/** Interface that is representing a list of glyphs
 * associated to a font. 
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see Font
 */
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
	
}
