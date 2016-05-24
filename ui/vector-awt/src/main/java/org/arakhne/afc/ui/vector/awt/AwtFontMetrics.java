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