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

import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.continous.object2d.PathElement2f;
import org.arakhne.afc.math.continous.object2d.PathIterator2f;
import org.arakhne.afc.math.generic.PathWindingRule;

/** AWT implementation of the generic path iterator.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
class AwtPathIterator implements PathIterator, NativeWrapper {

	private final PathIterator2f pathIterator;
	
	private PathElement2f element = null;
	
	/**
	 * @param pathIterator
	 */
	public AwtPathIterator(PathIterator2f pathIterator) {
		this.pathIterator = pathIterator;
		next();
	}

	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this.pathIterator);
	}

	@Override
	public int getWindingRule() {
		PathWindingRule rule = this.pathIterator.getWindingRule();
		switch(rule) {
		case NON_ZERO:
			return PathIterator.WIND_NON_ZERO;
		case EVEN_ODD:
			return PathIterator.WIND_EVEN_ODD;
		default:
		}
		throw new IllegalArgumentException();
	}

	@Override
	public boolean isDone() {
		return this.element==null;
	}

	@Override
	public void next() {
		this.element = null;
		if (this.pathIterator.hasNext())
			this.element = this.pathIterator.next();
	}

	@Override
	public int currentSegment(float[] coords) {
		if (this.element==null) throw new NoSuchElementException();
		switch(this.element.type) {
		case MOVE_TO:
			coords[0] = this.element.toX;
			coords[1] = this.element.toY;
			return PathIterator.SEG_MOVETO;
		case LINE_TO:
			coords[0] = this.element.toX;
			coords[1] = this.element.toY;
			return PathIterator.SEG_LINETO;
		case QUAD_TO:
			coords[0] = this.element.ctrlX1;
			coords[1] = this.element.ctrlY1;
			coords[2] = this.element.toX;
			coords[3] = this.element.toY;
			return PathIterator.SEG_QUADTO;
		case CURVE_TO:
			coords[0] = this.element.ctrlX1;
			coords[1] = this.element.ctrlY1;
			coords[2] = this.element.ctrlX2;
			coords[3] = this.element.ctrlY2;
			coords[4] = this.element.toX;
			coords[5] = this.element.toY;
			return PathIterator.SEG_CUBICTO;
		case CLOSE:
			return PathIterator.SEG_CLOSE;
		default:
		}
		throw new IllegalStateException();
	}

	@Override
	public int currentSegment(double[] coords) {
		if (this.element==null) throw new NoSuchElementException();
		switch(this.element.type) {
		case MOVE_TO:
			coords[0] = this.element.toX;
			coords[1] = this.element.toY;
			return PathIterator.SEG_MOVETO;
		case LINE_TO:
			coords[0] = this.element.toX;
			coords[1] = this.element.toY;
			return PathIterator.SEG_LINETO;
		case QUAD_TO:
			coords[0] = this.element.ctrlX1;
			coords[1] = this.element.ctrlY1;
			coords[2] = this.element.toX;
			coords[3] = this.element.toY;
			return PathIterator.SEG_QUADTO;
		case CURVE_TO:
			coords[0] = this.element.ctrlX1;
			coords[1] = this.element.ctrlY1;
			coords[2] = this.element.ctrlX2;
			coords[3] = this.element.ctrlY2;
			coords[4] = this.element.toX;
			coords[5] = this.element.toY;
			return PathIterator.SEG_CUBICTO;
		case CLOSE:
			return PathIterator.SEG_CLOSE;
		default:
		}
		throw new IllegalStateException();
	}

}