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

import java.awt.BasicStroke;

import org.arakhne.afc.ui.vector.Stroke;

/** AWT implementation of the generic Stroke.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
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
		throw new RuntimeException("unsupported line join: "+join);  //$NON-NLS-1$
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
		throw new RuntimeException("unsupported end cap: "+endCap);  //$NON-NLS-1$
	}
	
	private static LineJoin fromAwtLineJoin(int join) {
		if (join==BasicStroke.JOIN_MITER) return LineJoin.MITER;
		if (join==BasicStroke.JOIN_BEVEL) return LineJoin.BEVEL;
		if (join==BasicStroke.JOIN_ROUND) return LineJoin.ROUND;
		throw new RuntimeException("unsupported line join: "+join);  //$NON-NLS-1$
	}
	
	private static EndCap fromAwtEndCap(int endCap) {
		if (endCap==BasicStroke.CAP_SQUARE) return EndCap.SQUARE;
		if (endCap==BasicStroke.CAP_BUTT) return EndCap.BUTT;
		if (endCap==BasicStroke.CAP_ROUND) return EndCap.ROUND;
		throw new RuntimeException("unsupported end cap: "+endCap);  //$NON-NLS-1$
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