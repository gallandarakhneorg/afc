/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012-13 Stephane GALLAND.
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
package org.arakhne.afc.ui.vector.awt;

import java.awt.geom.Rectangle2D;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.ui.vector.Font;
import org.arakhne.afc.ui.vector.FontMetrics;

/** AWT implementation of the generic font metrics.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
class AwtFontMetrics implements FontMetrics, NativeWrapper {

	private final java.awt.FontMetrics fontMetrics;
	
	/**
	 * @param fontMetrics
	 */
	public AwtFontMetrics(java.awt.FontMetrics fontMetrics) {
		this.fontMetrics = fontMetrics;
	}

	@Override
	public Font getFont() {
		return new AwtFont(this.fontMetrics.getFont());
	}

	@Override
	public float getLeading() {
		return this.fontMetrics.getLeading();
	}

	@Override
	public float getAscent() {
		return this.fontMetrics.getAscent();
	}

	@Override
	public float getDescent() {
		return this.fontMetrics.getDescent();
	}

	@Override
	public float getHeight() {
		return this.fontMetrics.getHeight();
	}

	@Override
	public float getMaxAscent() {
		return this.fontMetrics.getMaxAscent();
	}

	@Override
	public float getMaxDescent() {
		return this.fontMetrics.getMaxDescent();
	}

	@Override
	public float getMaxAdvance() {
		return this.fontMetrics.getMaxAdvance();
	}

	@Override
	public float stringWidth(String str) {
		return this.fontMetrics.stringWidth(str);
	}
	
	/** Replies the font metrics.
	 * 
	 * @return the font metrics.
	 */
	public java.awt.FontMetrics getFontMetrics() {
		return this.fontMetrics;
	}

	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this.fontMetrics);
	}

	@Override
	public Rectangle2f getMaxCharBounds() {
		Rectangle2D r = this.fontMetrics.getMaxCharBounds(null);
		return new Rectangle2f(
				(float)r.getMinX(), (float)r.getMinY(),
				(float)r.getWidth(), (float)r.getHeight());
	}

}