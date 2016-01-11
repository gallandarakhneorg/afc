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

import org.arakhne.afc.math.geometry.d2.continuous.Rectangle2f;

/**
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AlignedBox3fTest extends AbstractBoxedShape3fTestCase<AlignedBox3f>{

	@Override
	protected AlignedBox3f createShape() {
		return new AlignedBox3f(0, 0, 0, 1, 1, 1);
	}

	@Override
	public void toBoundingBox() {
		assertTrue(new AlignedBox3f(0,0,0,1,1,1).equals(this.r.toBoundingBox()));
	}

	@Override
	public void toBoundingBoxAlignedBox3f() {
		AlignedBox3f A = new AlignedBox3f();
		this.r.toBoundingBox(A);

		assertTrue(this.r.toBoundingBox().equals(A));
	}

	@Override
	public void clear() {
		this.r.clear();

		assertTrue(this.r.equals(new AlignedBox3f(0,0,0,0,0,0)));
	}

	@Override
	public void setDoubleDoubleDoubleDoubleDoubleDouble() {
		this.r.set(0, 0, 0, 2, 2, 2);
		assertTrue(this.r.equals(new AlignedBox3f(0,0,0,2,2,2)));

		this.r.set(-1, -1, -1, 2, 2, 2);
		assertTrue(this.r.equals(new AlignedBox3f(-1,-1,-1,2,2,2)));

		this.r.set(1, 1, 1, 2, 2, 2);
		assertTrue(this.r.equals(new AlignedBox3f(1,1,1,2,2,2)));
	}

	@Override
	public void setPoint3fPoint3f() {
		this.r.set(new Point3f(0,0,0), new Point3f(2,2,2));
		assertTrue(this.r.equals(new AlignedBox3f(0,0,0,2,2,2)));

		this.r.set(new Point3f(-1,-1,-1), new Point3f(1,1,1));
		assertTrue(this.r.equals(new AlignedBox3f(-1,-1,-1,2,2,2)));

		this.r.set(new Point3f(1,1,1), new Point3f(3,3,3));
		assertTrue(this.r.equals(new AlignedBox3f(1,1,1,2,2,2)));
	}

	@Override
	public void setSizeXDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSizeYDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSizeZDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFromCornersPoint3DPoint3D() {
		this.r.setFromCorners(new Point3f(0,0,0), new Point3f(2,2,2));
		assertTrue(this.r.equals(new AlignedBox3f(0,0,0,2,2,2)));

		this.r.setFromCorners(new Point3f(-1,-1,-1), new Point3f(1,1,1));
		assertTrue(this.r.equals(new AlignedBox3f(-1,-1,-1,2,2,2)));

		this.r.setFromCorners(new Point3f(1,1,1), new Point3f(3,3,3));
		assertTrue(this.r.equals(new AlignedBox3f(1,1,1,2,2,2)));
	}

	@Override
	public void setFromCornersDoubleDoubleDoubleDoubleDoubleDouble() {
		this.r.setFromCorners(0,0,0,2,2,2);
		assertTrue(this.r.equals(new AlignedBox3f(0,0,0,2,2,2)));

		this.r.setFromCorners(-1,-1,-1,1,1,1);
		assertTrue(this.r.equals(new AlignedBox3f(-1,-1,-1,2,2,2)));

		this.r.setFromCorners(1,1,1,3,3,3);
		assertTrue(this.r.equals(new AlignedBox3f(1,1,1,2,2,2)));
	}

	@Override
	public void setFromCenterDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getMinX() {
		assertEpsilonEquals(0, this.r.getMinX());

		this.r.setFromCorners(-1,-1,-1,1,1,1);
		assertEpsilonEquals(-1, this.r.getMinX());

		this.r.setFromCorners(-2,-1,-1,1,1,1);
		assertEpsilonEquals(-2, this.r.getMinX());

		this.r.set(new Point3f(0,0,0), new Point3f(2,2,2));
		assertEpsilonEquals(0, this.r.getMinX());
	}

	@Override
	public void setMinXDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getMin() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getMax() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getCenter() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getCenterX() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getMaxX() {
		assertEpsilonEquals(1, this.r.getMaxX());

		this.r.setFromCorners(-1,-1,-1,1,1,1);
		assertEpsilonEquals(1, this.r.getMaxX());

		this.r.setFromCorners(-1,-1,-1,2,1,1);
		assertEpsilonEquals(2, this.r.getMaxX());

		this.r.set(new Point3f(0,0,0), new Point3f(2,2,2));
		assertEpsilonEquals(2, this.r.getMaxX());
	}

	@Override
	public void setMaxXDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getMinY() {
		assertEpsilonEquals(0, this.r.getMinY());

		this.r.setFromCorners(-1,-1,-1,1,1,1);
		assertEpsilonEquals(-1, this.r.getMinY());

		this.r.setFromCorners(-1,-2,-1,1,1,1);
		assertEpsilonEquals(-2, this.r.getMinY());

		this.r.set(new Point3f(0,0,0), new Point3f(2,2,2));
		assertEpsilonEquals(0, this.r.getMinY());
	}

	@Override
	public void setMinYDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getCenterY() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getMaxY() {
		assertEpsilonEquals(1, this.r.getMaxY());

		this.r.setFromCorners(-1,-1,-1,1,1,1);
		assertEpsilonEquals(1, this.r.getMaxY());

		this.r.setFromCorners(-1,-1,-1,1,2,1);
		assertEpsilonEquals(2, this.r.getMaxY());

		this.r.set(new Point3f(0,0,0), new Point3f(2,2,2));
		assertEpsilonEquals(2, this.r.getMaxY());
	}

	@Override
	public void setMaxYDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getMinZ() {
		assertEpsilonEquals(0, this.r.getMinZ());

		this.r.setFromCorners(-1,-1,-1,1,1,1);
		assertEpsilonEquals(-1, this.r.getMinZ());

		this.r.setFromCorners(-1,-1,-2,1,1,1);
		assertEpsilonEquals(-2, this.r.getMinZ());

		this.r.set(new Point3f(0,0,0), new Point3f(2,2,2));
		assertEpsilonEquals(0, this.r.getMinZ());
	}

	@Override
	public void setMinZDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getCenterZ() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getMaxZ() {
		assertEpsilonEquals(1, this.r.getMaxZ());

		this.r.setFromCorners(-1,-1,-1,1,1,9);
		assertEpsilonEquals(9, this.r.getMaxZ());

		this.r.setFromCorners(-1,-1,-1,1,1,3);
		assertEpsilonEquals(3, this.r.getMaxZ());

		this.r.set(new Point3f(0,0,0), new Point3f(2,2,2));
		assertEpsilonEquals(2, this.r.getMaxZ());
	}

	@Override
	public void setMaxZDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getSizeX() {
		assertEpsilonEquals(1, this.r.getSizeX());

		this.r.setFromCorners(-1,-1,-1,1,1,9);
		assertEpsilonEquals(2, this.r.getSizeX());

		this.r.setFromCorners(-1,-1,-1,1,1,3);
		assertEpsilonEquals(2, this.r.getSizeX());

		this.r.set(new Point3f(0,0,0), new Point3f(2,2,2));
		assertEpsilonEquals(2, this.r.getSizeX());
	}

	@Override
	public void getSizeY() {
		assertEpsilonEquals(1, this.r.getSizeY());

		this.r.setFromCorners(-1,-1,-1,1,1,9);
		assertEpsilonEquals(2, this.r.getSizeY());

		this.r.setFromCorners(-1,-1,-1,1,2,3);
		assertEpsilonEquals(3, this.r.getSizeY());

		this.r.set(new Point3f(0,0,0), new Point3f(2,2,2));
		assertEpsilonEquals(2, this.r.getSizeY());
	}

	@Override
	public void getSizeZ() {
		assertEpsilonEquals(1, this.r.getSizeZ());

		this.r.setFromCorners(-1,-1,-1,1,1,9);
		assertEpsilonEquals(10, this.r.getSizeZ());

		this.r.setFromCorners(-1,-1,-1,1,1,3);
		assertEpsilonEquals(4, this.r.getSizeZ());

		this.r.set(new Point3f(0,0,0), new Point3f(2,2,2));
		assertEpsilonEquals(2, this.r.getSizeZ());
	}

	@Override
	public void translateDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void inflateDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void translateVector3D() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setXDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setYDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setZDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void testClone() {
		AlignedBox3f b = this.r.clone();

		assertNotSame(b, this.r);
		assertEpsilonEquals(this.r.getMinX(), b.getMinX());
		assertEpsilonEquals(this.r.getMinY(), b.getMinY());
		assertEpsilonEquals(this.r.getMinZ(), b.getMinZ());
		assertEpsilonEquals(this.r.getMaxX(), b.getMaxX());
		assertEpsilonEquals(this.r.getMaxY(), b.getMaxY());
		assertEpsilonEquals(this.r.getMaxZ(), b.getMaxZ());
		
		b.set(this.r.getMinX()+1f, this.r.getMinY()+1f, this.r.getMinZ()+1f,
				this.r.getSizeX()+1f, this.r.getSizeY()+1f, this.r.getSizeZ()+1f);

		assertNotEpsilonEquals(this.r.getMinX(), b.getMinX());
		assertNotEpsilonEquals(this.r.getMinY(), b.getMinY());
		assertNotEpsilonEquals(this.r.getMinZ(), b.getMinZ());
		assertNotEpsilonEquals(this.r.getMaxX(), b.getMaxX());
		assertNotEpsilonEquals(this.r.getMaxY(), b.getMaxY());
		assertNotEpsilonEquals(this.r.getMaxZ(), b.getMaxZ());
	}

	@Override
	public void distancePoint3D() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void containsPoint3D() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void testEquals() {
		AlignedBox3f b = this.r.clone();
		AlignedBox3f c = new AlignedBox3f(this.r);
		
		assertTrue(b.equals(this.r));
		assertTrue(this.r.equals(b));
		assertTrue(this.r.equals(this.r));
		assertTrue(b.equals(c));
		assertTrue(this.r.equals(c));
	}

	@Override
	public void testHashCode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void distanceSquaredPoint3D() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void distanceL1Point3D() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void distanceLinfPoint3D() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void containsDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void intersectsAlignedBox3f() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void intersectsSphere3f() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void intersectsSegment3f() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void intersectsTriangle3f() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void intersectsCapsule3f() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void intersectsOrientedBox3f() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void intersectsAbstractPlane3D() {
		throw new UnsupportedOperationException();
	}


}
