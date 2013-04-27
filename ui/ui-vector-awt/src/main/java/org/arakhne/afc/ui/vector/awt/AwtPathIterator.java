/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.continous.object2d.PathElement2f;
import org.arakhne.afc.math.continous.object2d.PathIterator2f;
import org.arakhne.afc.math.generic.PathWindingRule;

/** AWT implementation of the generic path iterator.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
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