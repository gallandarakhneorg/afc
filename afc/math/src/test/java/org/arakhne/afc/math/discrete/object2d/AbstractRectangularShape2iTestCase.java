/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
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
package org.arakhne.afc.math.discrete.object2d;

import org.arakhne.afc.math.continous.object2d.Point2f;


/**
 * @param <T> is the type of the shape to test.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractRectangularShape2iTestCase<T extends AbstractRectangularShape2i<?>> extends AbstractShape2iTestCase<T> {
	
	/**
	 */
	public void testSetIntIntIntInt() {
		this.r.set(2, 3, 4, 5);
		assertEpsilonEquals(2, this.r.getMinX());
		assertEpsilonEquals(3, this.r.getMinY());
		assertEpsilonEquals(4, this.r.getWidth());
		assertEpsilonEquals(5, this.r.getHeight());
	}
	
	/**
	 */
	public void testSetPoint2DPoint2D() {
		this.r.set(new Point2f(2.3f, 3.4f), new Point2f(4.5f, 5.6f));
		assertEpsilonEquals(2, this.r.getMinX());
		assertEpsilonEquals(3, this.r.getMinY());
		assertEpsilonEquals(4, this.r.getMaxX());
		assertEpsilonEquals(5, this.r.getMaxY());
	}
	
	/**
	 */
	public void testSetRectangularShape2i() {
		Rectangle2i rr = new Rectangle2i(2, 3, 4, 5);
		this.r.set(rr);
		assertEpsilonEquals(2, this.r.getMinX());
		assertEpsilonEquals(3, this.r.getMinY());
		assertEpsilonEquals(4, this.r.getWidth());
		assertEpsilonEquals(5, this.r.getHeight());
	}

	/**
	 */
	public void testSetWidth() {
		this.r.setWidth(2);
		assertEpsilonEquals(0, this.r.getMinX());
		assertEpsilonEquals(0, this.r.getMinY());
		assertEpsilonEquals(2, this.r.getMaxX());
		assertEpsilonEquals(1, this.r.getMaxY());
		assertEpsilonEquals(2, this.r.getWidth());
	}

	/**
	 */
	public void testSetHeight() {
		this.r.setHeight(2);
		assertEpsilonEquals(0, this.r.getMinX());
		assertEpsilonEquals(0, this.r.getMinY());
		assertEpsilonEquals(1, this.r.getMaxX());
		assertEpsilonEquals(2, this.r.getMaxY());
		assertEpsilonEquals(2, this.r.getHeight());
	}

	/**
	 */
	public void testSetFromCornersPoint2DPoint2D() {
		this.r.setFromCorners(new Point2f(2.3f, 3.4f), new Point2f(4.5f, 5.6f));
		assertEpsilonEquals(2, this.r.getMinX());
		assertEpsilonEquals(3, this.r.getMinY());
		assertEpsilonEquals(4, this.r.getMaxX());
		assertEpsilonEquals(5, this.r.getMaxY());
	}

	/**
	 */
	public void testSetFromCornersFloatFloatFloatFloat() {
		this.r.setFromCorners(2, 3, 4, 5);
		assertEpsilonEquals(2, this.r.getMinX());
		assertEpsilonEquals(3, this.r.getMinY());
		assertEpsilonEquals(4, this.r.getMaxX());
		assertEpsilonEquals(5, this.r.getMaxY());
	}
	
	/**
	 */
	public void testSetFromCenterFloatFloatFloatFloat() {
		this.r.setFromCenter(2, 3, 4, 5);
		//w = 4-2 = 2
		//h = 5-3 = 2
		assertEpsilonEquals(2-2, this.r.getMinX());
		assertEpsilonEquals(3-2, this.r.getMinY());
		assertEpsilonEquals(2+2, this.r.getMaxX());
		assertEpsilonEquals(3+2, this.r.getMaxY());
	}
	
	/**
	 */
	public void testSetMinX() {
		this.r.setMinX(2);
		assertEpsilonEquals(1, this.r.getMinX());
		assertEpsilonEquals(2, this.r.getMaxX());
		this.r.setMinX(-3);
		assertEpsilonEquals(-3, this.r.getMinX());
		assertEpsilonEquals(2, this.r.getMaxX());
	}

	/**
	 */
	public void testSetMaxX() {
		this.r.setMaxX(2);
		assertEpsilonEquals(0, this.r.getMinX());
		assertEpsilonEquals(2, this.r.getMaxX());
		this.r.setMaxX(-3);
		assertEpsilonEquals(-3, this.r.getMinX());
		assertEpsilonEquals(0, this.r.getMaxX());
	}

	/**
	 */
	public void testSetMinY() {
		this.r.setMinY(2);
		assertEpsilonEquals(1, this.r.getMinY());
		assertEpsilonEquals(2, this.r.getMaxY());
		this.r.setMinY(-3);
		assertEpsilonEquals(-3, this.r.getMinY());
		assertEpsilonEquals(2, this.r.getMaxY());
	}

	/**
	 */
	public void testSetMaxY() {
		this.r.setMaxY(2);
		assertEpsilonEquals(0, this.r.getMinY());
		assertEpsilonEquals(2, this.r.getMaxY());
		this.r.setMaxY(-3);
		assertEpsilonEquals(-3, this.r.getMinY());
		assertEpsilonEquals(0, this.r.getMaxY());
	}
	
	/**
	 */
	@Override
	public void testTranslateIntInt() {
		this.r.translate(2, 3);
		assertEpsilonEquals(2, this.r.getMinX());
		assertEpsilonEquals(3, this.r.getMinY());
		assertEpsilonEquals(3, this.r.getMaxX());
		assertEpsilonEquals(4, this.r.getMaxY());
	}
	
}