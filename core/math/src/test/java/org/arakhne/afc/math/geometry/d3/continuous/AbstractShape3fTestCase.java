/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d3.continuous;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public abstract class AbstractShape3fTestCase<T extends Shape3f> {

	/** Is the rectangular shape to test.
	 */
	protected T r;
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.r = createShape();
	}
	
	/** Create the shape to test.
	 * 
	 * @return the shape to test.
	 */
	protected abstract T createShape();
	
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
	public abstract void distancePoint3D();

	@Test
	public abstract void containsPoint3D();

    @Test
    public abstract void testEquals();
    
    @Test
    public abstract void testHashCode();
   
    @Test
    public abstract void toBoundingBox();
	
    @Test
    public abstract void toBoundingBoxAlignedBox3f();

    @Test
    public abstract void distanceSquaredPoint3D();

    @Test
    public abstract void distanceL1Point3D();

    @Test
    public abstract void distanceLinfPoint3D();

    @Test
    public abstract void translateVector3D(); 

    @Test
    public abstract void translateDoubleDoubleDouble(); 
	
    @Test
    public abstract void containsDoubleDoubleDouble();
	
    @Test
    public abstract void intersectsAlignedBox3f();

    @Test
    public abstract void intersectsSphere3f();

    @Test
    public abstract void intersectsSegment3f();

    @Test
    public abstract void intersectsTriangle3f();

    @Test
    public abstract void intersectsCapsule3f();

    @Test
    public abstract void intersectsOrientedBox3f();

    @Test
	public abstract void intersectsAbstractPlane3D();

}