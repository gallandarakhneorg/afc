/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2016 Stephane GALLAND.
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.continuous.AbstractRectangle2F;
import org.arakhne.afc.math.geometry.d2.continuous.Circle2f;
import org.arakhne.afc.math.geometry.d2.continuous.Ellipse2f;
import org.arakhne.afc.math.geometry.d2.continuous.Path2f;
import org.arakhne.afc.math.geometry.d2.continuous.PathIterator2f;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.arakhne.afc.math.geometry.d3.continuous.Rectangle3f;
import org.arakhne.afc.math.geometry.d2.continuous.Segment2f;
import org.arakhne.afc.math.geometry.d2.continuous.Transform2D;
import org.junit.Test;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AlignedBox3fTest extends AbstractBoxedShape3fTestCase<AlignedBox3f>{

	@Override
	public void toBoundingBox() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toBoundingBoxAlignedBox3f() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDoubleDoubleDoubleDoubleDoubleDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPoint3fPoint3f() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSizeXDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSizeYDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSizeZDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFromCornersPoint3DPoint3D() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFromCornersDoubleDoubleDoubleDoubleDoubleDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFromCenterDoubleDoubleDoubleDoubleDoubleDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMinX() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMinXDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMax() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCenter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCenterX() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMaxX() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxXDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMinY() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMinYDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCenterY() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMaxY() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxYDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMinz() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMinZDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCenterZ() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMaxZ() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxZDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSizeX() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSizeY() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSizeZ() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void translateDoubleDoubleDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void isEmpty() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inflateDoubleDoubleDoubleDoubleDoubleDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void translateVector3D() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setXDoubleDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setYDoubleDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setZDoubleDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected AlignedBox3f createShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void testClone() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void distancePoint3D() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void containsPoint3D() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testEquals() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testHashCode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void distanceSquaredPoint3D() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void distanceL1Point3D() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void distanceLinfPoint3D() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void containsDoubleDoubleDouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void intersectsAlignedBox3f() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void intersectsSphere3f() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void intersectsSegment3f() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void intersectsTriangle3f() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void intersectsCapsule3f() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void intersectsOrientedBox3f() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void intersectsAbstractPlane3D() {
		// TODO Auto-generated method stub
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
	@Test
	public void  () {
		
	}
	
}
