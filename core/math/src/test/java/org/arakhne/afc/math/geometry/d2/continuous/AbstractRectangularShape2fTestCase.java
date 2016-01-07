/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
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
package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.d2.continuous.AbstractRectangularShape2F;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.junit.Test;



/**
 * @param <T> is the type of the shape to test.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractRectangularShape2fTestCase<T extends AbstractRectangularShape2F<?>> extends AbstractShape2fTestCase<T> {
	
	/**
	 */
	@Test
	public void setFloatFloatFloatFloat() {
		this.r.set(2.3f, 3.4f, 4.5f, 5.6f);
		assertEpsilonEquals(2.3f, this.r.getMinX());
		assertEpsilonEquals(3.4f, this.r.getMinY());
		assertEpsilonEquals(6.8f, this.r.getMaxX()); 
		assertEpsilonEquals(9f, this.r.getMaxY());
		assertEpsilonEquals(4.5f, this.r.getWidth());
		assertEpsilonEquals(5.6f, this.r.getHeight());
	}
	
	@Test
	public void setInitiallyFloatFloatFloatFloat() {
		this.r.setInitiallyFromCorners(2.3f, 3.4f, 4.5f, 5.6f);
		assertEpsilonEquals(2.3f, this.r.getMinX());
		assertEpsilonEquals(3.4f, this.r.getMinY());
		assertEpsilonEquals(4.5f-2.3f, this.r.getWidth()); 
		assertEpsilonEquals(5.6f-3.4f, this.r.getHeight());
	}
	
	/**
	 */
	@Test
	public void setPoint2DPoint2D() {
		this.r.set(new Point2f(2.3f, 3.4f), new Point2f(4.5f, 5.6f));
		assertEpsilonEquals(2.3f, this.r.getMinX());
		assertEpsilonEquals(3.4f, this.r.getMinY());
		assertEpsilonEquals(4.5f, this.r.getMaxX());
		assertEpsilonEquals(5.6f, this.r.getMaxY());
	}
	
	/**
	 */
	@Test
	public void setWidth() {
		this.r.setWidth(2.3f);
		assertEpsilonEquals(0f, this.r.getMinX());
		assertEpsilonEquals(0f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		assertEpsilonEquals(1f, this.r.getMaxY());
		assertEpsilonEquals(2.3f, this.r.getWidth());
	}

	/**
	 */
	@Test
	public void setHeight() {
		this.r.setHeight(2.3f);
		assertEpsilonEquals(0f, this.r.getMinX());
		assertEpsilonEquals(0f, this.r.getMinY());
		assertEpsilonEquals(1f, this.r.getMaxX());
		assertEpsilonEquals(2.3f, this.r.getMaxY());
		assertEpsilonEquals(2.3f, this.r.getHeight());
	}

	/**
	 */
	@Test
	public void setFromCornersPoint2DPoint2D() {
		this.r.setFromCorners(new Point2f(-2.3f, 3.4f), new Point2f(0.5f, 5.6f));
		assertEpsilonEquals(-2.3f, this.r.getMinX());
		assertEpsilonEquals(0f, this.r.getMinY());
		assertEpsilonEquals(1f, this.r.getMaxX());
		assertEpsilonEquals(5.6f, this.r.getMaxY());
	}
	
	/**
	 */
	@Test
	public void setInitiallyFromCornersPoint2DPoint2D() {
		this.r.setInitiallyFromCorners(new Point2f(-2.3f, 3.4f), new Point2f(0.5f, 5.6f));
		assertEpsilonEquals(-2.3f, this.r.getMinX());
		assertEpsilonEquals(3.4f, this.r.getMinY());
		assertEpsilonEquals(0.5f, this.r.getMaxX());
		assertEpsilonEquals(5.6f, this.r.getMaxY());
	}

	/**
	 */
	@Test
	public void setFromCornersFloatFloatFloatFloat() {
		this.r.setFromCorners(2.3f, 3.4f, 4.5f, 5.6f);
		assertEpsilonEquals(0f, this.r.getMinX());
		assertEpsilonEquals(0f, this.r.getMinY());
		assertEpsilonEquals(4.5f, this.r.getMaxX());
		assertEpsilonEquals(5.6f, this.r.getMaxY());
	}
	
	/**
	 */
	@Test
	public void setFromCenterFloatFloatFloatFloat() {
		this.r.setFromCenter(2.3f, 3.4f, 4.5f, 5.6f);
		//w = 4.5-2.3 = 2.2
		//h = 5.6-3.4 = 2.2
		//assertEpsilonEquals(2.3f-2.2f, this.r.getMinX());
		assertEpsilonEquals(3.4f-2.2f, this.r.getMinY());
		assertEpsilonEquals(2.3f+2.2f, this.r.getMaxX());
		assertEpsilonEquals(3.4f+2.2f, this.r.getMaxY());
	}
	
	/**
	 */
	@Test
	public void setMinX() {
		this.r.setMinX(2.3f);
		assertEpsilonEquals(0f, this.r.getMinX());
		assertEpsilonEquals(1f, this.r.getMaxX());
		this.r.setMinX(-3.4f);
		assertEpsilonEquals(-3.4f, this.r.getMinX());
		assertEpsilonEquals(1f, this.r.getMaxX());
	}

	/**
	 */
	@Test
	public void setMaxX() {
		this.r.setMaxX(2.3f);
		assertEpsilonEquals(0f, this.r.getMinX());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		this.r.setMaxX(-3.4f);
		assertEpsilonEquals(0f, this.r.getMinX());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
	}

	/**
	 */
	@Test
	public void setMinY() {
		this.r.setMinY(2.3f);
		assertEpsilonEquals(0f, this.r.getMinY());
		assertEpsilonEquals(1f, this.r.getMaxY());
		this.r.setMinY(-3.4f);
		assertEpsilonEquals(-3.4f, this.r.getMinY());
		assertEpsilonEquals(1f, this.r.getMaxY());
	}

	/**
	 */
	@Test
	public void setMaxY() {
		this.r.setMaxY(2.3f);
		assertEpsilonEquals(0f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxY());
		this.r.setMaxY(-3.4f);
		assertEpsilonEquals(0f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxY());
	}
	
	/**
	 */
	@Test
	@Override
	public void translateFloatFloat() {
		this.r.translate(2.3f, 3.4f);
		assertEpsilonEquals(2.3f, this.r.getMinX());
		assertEpsilonEquals(3.4f, this.r.getMinY());
		assertEpsilonEquals(3.3f, this.r.getMaxX());
		assertEpsilonEquals(4.4f, this.r.getMaxY());
	}
	
}