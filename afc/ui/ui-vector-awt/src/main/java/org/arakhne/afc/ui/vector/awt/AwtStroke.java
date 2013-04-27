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

import java.awt.BasicStroke;

import org.arakhne.afc.ui.vector.Stroke;

/** AWT implementation of the generic Stroke.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AwtStroke implements Stroke, NativeWrapper {

	private static int toAwtLineJoin(LineJoin join) {
		switch(join) {
		case BEVEL:
			return BasicStroke.JOIN_BEVEL;
		case MITER:
			return BasicStroke.JOIN_MITER;
		case ROUND:
			return BasicStroke.JOIN_ROUND;
		default:
		}
		throw new RuntimeException("unsupported line join: "+join); //$NON-NLS-1$
	}
	
	private static int toAwtEndCap(EndCap endCap) {
		switch(endCap) {
		case BUTT:
			return BasicStroke.CAP_BUTT;
		case ROUND:
			return BasicStroke.CAP_ROUND;
		case SQUARE:
			return BasicStroke.CAP_SQUARE;
		default:
		}
		throw new RuntimeException("unsupported end cap: "+endCap); //$NON-NLS-1$
	}
	
	private static LineJoin fromAwtLineJoin(int join) {
		if (join==BasicStroke.JOIN_MITER) return LineJoin.MITER;
		if (join==BasicStroke.JOIN_BEVEL) return LineJoin.BEVEL;
		if (join==BasicStroke.JOIN_ROUND) return LineJoin.ROUND;
		throw new RuntimeException("unsupported line join: "+join); //$NON-NLS-1$
	}
	
	private static EndCap fromAwtEndCap(int endCap) {
		if (endCap==BasicStroke.CAP_SQUARE) return EndCap.SQUARE;
		if (endCap==BasicStroke.CAP_BUTT) return EndCap.BUTT;
		if (endCap==BasicStroke.CAP_ROUND) return EndCap.ROUND;
		throw new RuntimeException("unsupported end cap: "+endCap); //$NON-NLS-1$
	}

	private final BasicStroke stroke;

	/**
	 * @param width
	 * @param lineJoin
	 * @param endCap
	 * @param miterLimit
	 * @param dashes
	 * @param dashPhase
	 */
	public AwtStroke(float width, LineJoin lineJoin, EndCap endCap, float miterLimit, float[] dashes, float dashPhase) {
		this.stroke = new BasicStroke(width, toAwtLineJoin(lineJoin), toAwtEndCap(endCap), miterLimit, dashes, dashPhase);
	}
	
	/**
	 * @param stroke
	 */
	public AwtStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}

	@Override
	public String toString() {
		return this.stroke.toString();
	}
	
	@Override
	public float[] getDashArray() {
		return this.stroke.getDashArray();
	}

	@Override
	public float getDashPhase() {
		return this.stroke.getDashPhase();
	}

	@Override
	public float getLineWidth() {
		return this.stroke.getLineWidth();
	}

	@Override
	public LineJoin getLineJoin() {
		return fromAwtLineJoin(this.stroke.getLineJoin());
	}

	@Override
	public EndCap getEndCap() {
		return fromAwtEndCap(this.stroke.getEndCap());
	}

	@Override
	public float getMiterLimit() {
		return this.stroke.getMiterLimit();
	}
	
	/** Replies the AWT stroke.
	 * 
	 * @return the AWT stroke.
	 */
	public java.awt.Stroke getStroke() {
		return this.stroke;
	}

	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this.stroke);
	}

}