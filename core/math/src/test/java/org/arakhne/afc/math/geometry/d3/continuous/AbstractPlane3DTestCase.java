/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.d3.continuous;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @param <T> is the type of the plane.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public abstract class AbstractPlane3DTestCase<T extends Plane3D<? super T>> {

	/** Is the rectangular shape to test.
	 */
	protected T r;
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.r = createPlane();
	}
	
	/** Create the shape to test.
	 * 
	 * @return the shape to test.
	 */
	protected abstract T createPlane();
	
	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.r = null;
	}

	@Test
	public abstract void testClone();

	@Test
	public abstract void distanceToPoint3D();

	@Test
	public abstract void distanceToPlane3D();

	@Test
	public abstract void getIntersectionPlane3D();

	@Test
	public abstract void getIntersectionSegment3D();

	@Test
	public abstract void getProjectionPoint3D(); 
	
	@Test
	public abstract void isValid();

	@Test
    public abstract void classifiesPlane3D();

	@Test
    public abstract void classifiesPoint3D();

	@Test
    public abstract void classifiesSphere3f();

	@Test
    public abstract void classifiesAlignedBox3f();

	@Test
    public abstract void intersectsPlane3D();

	@Test
    public abstract void intersectsPoint3D();
    
	@Test
    public abstract void intersectsSphere3f();

	@Test
    public abstract void intersectsAlignedBox3f();

	@Test
	public abstract void getProjectionDoubleDoubleDouble();

	@Test
	public abstract void distanceToDoubleDoubleDouble();

	@Test
    public abstract void classifiesDoubleDoubleDouble();

	@Test
    public abstract void intersectsDoubleDoubleDouble();

	@Test
    public abstract void setPivotPoint3D();

	@Test
    public abstract void setPivotDoubleDoubleDouble();

	@Test
    public abstract void getPivot();

}