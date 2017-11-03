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

import org.junit.Test;

/**
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AlignedBox3dTest extends AbstractBoxedShape3fTestCase<AlignedBox3d>{

	@Override
	protected AlignedBox3d createShape() {
		return new AlignedBox3d(0, 0, 0, 1, 1, 1);
	}

	@Override
	public void toBoundingBox() {
		assertTrue(new AlignedBox3d(0,0,0,1,1,1).equals(this.r.toBoundingBox()));
	}

	@Override
	public void toBoundingBoxAlignedBox3x() {
		AlignedBox3d A = new AlignedBox3d();
		this.r.toBoundingBox(A);

		assertTrue(this.r.toBoundingBox().equals(A));
	}

	@Override
	public void clear() {
		this.r.clear();

		assertTrue(this.r.equals(new AlignedBox3d(0,0,0,0,0,0)));
	}

	@Override
	public void setDoubleDoubleDoubleDoubleDoubleDouble() {
		this.r.set(0, 0, 0, 2, 2, 2);
		assertTrue(this.r.equals(new AlignedBox3d(0,0,0,2,2,2)));

		this.r.set(-1, -1, -1, 2, 2, 2);
		assertTrue(this.r.equals(new AlignedBox3d(-1,-1,-1,2,2,2)));

		this.r.set(1, 1, 1, 2, 2, 2);
		assertTrue(this.r.equals(new AlignedBox3d(1,1,1,2,2,2)));
	}

	@Override
	public void setPoint3xPoint3x() {
		this.r.set(new Point3d(0,0,0), new Point3d(2,2,2));
		assertTrue(this.r.equals(new AlignedBox3d(0,0,0,2,2,2)));

		this.r.set(new Point3d(-1,-1,-1), new Point3d(1,1,1));
		assertTrue(this.r.equals(new AlignedBox3d(-1,-1,-1,2,2,2)));

		this.r.set(new Point3d(1,1,1), new Point3d(3,3,3));
		assertTrue(this.r.equals(new AlignedBox3d(1,1,1,2,2,2)));
	}

	@Override
	public void setSizeXDouble() {
		this.r.setSizeX(3);

		assertEpsilonEquals(0, this.r.getMinX());
		assertEpsilonEquals(3, this.r.getMaxX());

		this.r.setFromCorners(1, 1, 1, 2, 2, 2);
		this.r.setSizeX(9);

		assertEpsilonEquals(1, this.r.getMinX());
		assertEpsilonEquals(10, this.r.getMaxX());
	}

	@Override
	public void setSizeYDouble() {
		this.r.setSizeY(3);

		assertEpsilonEquals(0, this.r.getMinY());
		assertEpsilonEquals(3, this.r.getMaxY());

		this.r.setFromCorners(1, 1, 1, 2, 2, 2);
		this.r.setSizeY(9);

		assertEpsilonEquals(1, this.r.getMinY());
		assertEpsilonEquals(10, this.r.getMaxY());
	}

	@Override
	public void setSizeZDouble() {
		this.r.setSizeZ(3);

		assertEpsilonEquals(0, this.r.getMinZ());
		assertEpsilonEquals(3, this.r.getMaxZ());

		this.r.setFromCorners(1, 1, 1, 2, 2, 2);
		this.r.setSizeZ(9);

		assertEpsilonEquals(1, this.r.getMinZ());
		assertEpsilonEquals(10, this.r.getMaxZ());
	}

	@Override
	public void setFromCornersPoint3DPoint3D() {
		this.r.setFromCorners(new Point3d(0,0,0), new Point3d(2,2,2));
		assertTrue(this.r.equals(new AlignedBox3d(0,0,0,2,2,2)));

		this.r.setFromCorners(new Point3d(-1,-1,-1), new Point3d(1,1,1));
		assertTrue(this.r.equals(new AlignedBox3d(-1,-1,-1,2,2,2)));

		this.r.setFromCorners(new Point3d(1,1,1), new Point3d(3,3,3));
		assertTrue(this.r.equals(new AlignedBox3d(1,1,1,2,2,2)));
	}

	@Override
	public void setFromCornersDoubleDoubleDoubleDoubleDoubleDouble() {
		this.r.setFromCorners(0,0,0,2,2,2);
		assertTrue(this.r.equals(new AlignedBox3d(0,0,0,2,2,2)));

		this.r.setFromCorners(-1,-1,-1,1,1,1);
		assertTrue(this.r.equals(new AlignedBox3d(-1,-1,-1,2,2,2)));

		this.r.setFromCorners(1,1,1,3,3,3);
		assertTrue(this.r.equals(new AlignedBox3d(1,1,1,2,2,2)));
	}

	@Override
	public void setFromCenterDoubleDoubleDoubleDoubleDoubleDouble() {
		this.r.setFromCenter(1, 1, 1, 2, 2, 2);
		assertTrue(this.r.equals(new AlignedBox3d(0,0,0,2,2,2)));

		this.r.setFromCenter(0, 0, 0, -1, -1, -1);
		assertTrue(this.r.equals(new AlignedBox3d(-1,-1,-1,2,2,2)));

		this.r.setFromCenter(2, 2, 2, 3, 3, 3);
		assertTrue(this.r.equals(new AlignedBox3d(1,1,1,2,2,2)));
	}

	@Override
	public void getMinX() {
		assertEpsilonEquals(0, this.r.getMinX());

		this.r.setFromCorners(-1,-1,-1,1,1,1);
		assertEpsilonEquals(-1, this.r.getMinX());

		this.r.setFromCorners(-2,-1,-1,1,1,1);
		assertEpsilonEquals(-2, this.r.getMinX());

		this.r.set(new Point3d(0,0,0), new Point3d(2,2,2));
		assertEpsilonEquals(0, this.r.getMinX());
	}

	@Override
	public void setMinXDouble() {
		this.r.setMinX(2.3f);
		assertEpsilonEquals(1f, this.r.getMinX());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		this.r.setMinX(-3.4f);
		assertEpsilonEquals(-3.4f, this.r.getMinX());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
	}

	@Override
	public void getMin() {
		assertEpsilonEquals(new Point3d(0,0,0),this.r.getMin());
		this.r.setMinX(2.3f);
		this.r.setMinY(3f);
		this.r.setMaxZ(2.3f);
		assertEpsilonEquals(new Point3d(1,1,0),this.r.getMin());
	}

	@Override
	public void getMax() {
		assertEpsilonEquals(new Point3d(1,1,1),this.r.getMax());
		this.r.setMinX(2.3f);
		this.r.setMinY(3f);
		this.r.setMaxZ(2.3f);
		assertEpsilonEquals(new Point3d(2.3,3,2.3),this.r.getMax());
	}

	@Override
	public void getCenter() {
		assertEpsilonEquals(new Point3d(0.5,0.5,0.5),this.r.getCenter());
		this.r.setMinX(2.3f);
		this.r.setMinY(3f);
		this.r.setMaxZ(2.3f);
		assertEpsilonEquals(new Point3d((2.3+1)/2.,2,2.3/2.),this.r.getCenter());
	}

	@Override
	public void getCenterX() {
		assertEpsilonEquals(0.5,this.r.getCenterX());
		this.r.setMinX(2.3f);
		assertEpsilonEquals((2.3+1)/2f,this.r.getCenterX());
	}

	@Override
	public void getMaxX() {
		assertEpsilonEquals(1, this.r.getMaxX());

		this.r.setFromCorners(-1,-1,-1,1,1,1);
		assertEpsilonEquals(1, this.r.getMaxX());

		this.r.setFromCorners(-1,-1,-1,2,1,1);
		assertEpsilonEquals(2, this.r.getMaxX());

		this.r.set(new Point3d(0,0,0), new Point3d(2,2,2));
		assertEpsilonEquals(2, this.r.getMaxX());
	}

	@Override
	public void setMaxXDouble() {
		this.r.setMaxX(2.3f);
		assertEpsilonEquals(0f, this.r.getMinX());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		this.r.setMaxX(-3.4f);
		assertEpsilonEquals(-3.4f, this.r.getMinX());
		assertEpsilonEquals(0f, this.r.getMaxX());
	}

	@Override
	public void getMinY() {
		assertEpsilonEquals(0, this.r.getMinY());

		this.r.setFromCorners(-1,-1,-1,1,1,1);
		assertEpsilonEquals(-1, this.r.getMinY());

		this.r.setFromCorners(-1,-2,-1,1,1,1);
		assertEpsilonEquals(-2, this.r.getMinY());

		this.r.set(new Point3d(0,0,0), new Point3d(2,2,2));
		assertEpsilonEquals(0, this.r.getMinY());
	}

	@Override
	public void setMinYDouble() {
		this.r.setMinY(2.3f);
		assertEpsilonEquals(1f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxY());
		this.r.setMinY(-3.4f);
		assertEpsilonEquals(-3.4f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxY());
	}

	@Override
	public void getCenterY() {
		assertEpsilonEquals(0.5,this.r.getCenterY());
		this.r.setMinY(2.3f);
		assertEpsilonEquals((2.3+1)/2f,this.r.getCenterY());
	}

	@Override
	public void getMaxY() {
		assertEpsilonEquals(1, this.r.getMaxY());

		this.r.setFromCorners(-1,-1,-1,1,1,1);
		assertEpsilonEquals(1, this.r.getMaxY());

		this.r.setFromCorners(-1,-1,-1,1,2,1);
		assertEpsilonEquals(2, this.r.getMaxY());

		this.r.set(new Point3d(0,0,0), new Point3d(2,2,2));
		assertEpsilonEquals(2, this.r.getMaxY());
	}

	@Override
	public void setMaxYDouble() {
		this.r.setMaxY(2.3f);
		assertEpsilonEquals(0f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxY());
		this.r.setMaxY(-3.4f);
		assertEpsilonEquals(-3.4f, this.r.getMinY());
		assertEpsilonEquals(0f, this.r.getMaxY());
	}

	@Override
	public void getMinZ() {
		assertEpsilonEquals(0, this.r.getMinZ());

		this.r.setFromCorners(-1,-1,-1,1,1,1);
		assertEpsilonEquals(-1, this.r.getMinZ());

		this.r.setFromCorners(-1,-1,-2,1,1,1);
		assertEpsilonEquals(-2, this.r.getMinZ());

		this.r.set(new Point3d(0,0,0), new Point3d(2,2,2));
		assertEpsilonEquals(0, this.r.getMinZ());
	}

	@Override
	public void setMinZDouble() {
		this.r.setMinZ(2.3f);
		assertEpsilonEquals(1f, this.r.getMinZ());
		assertEpsilonEquals(2.3f, this.r.getMaxZ());
		this.r.setMinZ(-3.4f);
		assertEpsilonEquals(-3.4f, this.r.getMinZ());
		assertEpsilonEquals(2.3f, this.r.getMaxZ());
	}

	@Override
	public void getCenterZ() {
		assertEpsilonEquals(0.5,this.r.getCenterZ());
		this.r.setMinZ(2.3f);
		assertEpsilonEquals((2.3+1)/2f,this.r.getCenterZ());
	}

	@Override
	public void getMaxZ() {
		assertEpsilonEquals(1, this.r.getMaxZ());

		this.r.setFromCorners(-1,-1,-1,1,1,9);
		assertEpsilonEquals(9, this.r.getMaxZ());

		this.r.setFromCorners(-1,-1,-1,1,1,3);
		assertEpsilonEquals(3, this.r.getMaxZ());

		this.r.set(new Point3d(0,0,0), new Point3d(2,2,2));
		assertEpsilonEquals(2, this.r.getMaxZ());
	}

	@Override
	public void setMaxZDouble() {
		this.r.setMaxZ(2.3f);
		assertEpsilonEquals(0f, this.r.getMinZ());
		assertEpsilonEquals(2.3f, this.r.getMaxZ());
		this.r.setMaxZ(-3.4f);
		assertEpsilonEquals(-3.4f, this.r.getMinZ());
		assertEpsilonEquals(0f, this.r.getMaxZ());
	}

	@Override
	public void getSizeX() {
		assertEpsilonEquals(1, this.r.getSizeX());

		this.r.setFromCorners(-1,-1,-1,1,1,9);
		assertEpsilonEquals(2, this.r.getSizeX());

		this.r.setFromCorners(-1,-1,-1,1,1,3);
		assertEpsilonEquals(2, this.r.getSizeX());

		this.r.set(new Point3d(0,0,0), new Point3d(2,2,2));
		assertEpsilonEquals(2, this.r.getSizeX());
	}

	@Override
	public void getSizeY() {
		assertEpsilonEquals(1, this.r.getSizeY());

		this.r.setFromCorners(-1,-1,-1,1,1,9);
		assertEpsilonEquals(2, this.r.getSizeY());

		this.r.setFromCorners(-1,-1,-1,1,2,3);
		assertEpsilonEquals(3, this.r.getSizeY());

		this.r.set(new Point3d(0,0,0), new Point3d(2,2,2));
		assertEpsilonEquals(2, this.r.getSizeY());
	}

	@Override
	public void getSizeZ() {
		assertEpsilonEquals(1, this.r.getSizeZ());

		this.r.setFromCorners(-1,-1,-1,1,1,9);
		assertEpsilonEquals(10, this.r.getSizeZ());

		this.r.setFromCorners(-1,-1,-1,1,1,3);
		assertEpsilonEquals(4, this.r.getSizeZ());

		this.r.set(new Point3d(0,0,0), new Point3d(2,2,2));
		assertEpsilonEquals(2, this.r.getSizeZ());
	}

	@Override
	public void translateDoubleDoubleDouble() {
		Vector3d translation = new Vector3d(this.random.nextDouble()*20,this.random.nextDouble()*20,this.random.nextDouble()*20);
		this.r.translate(translation.getX(),translation.getY(),translation.getZ());
		assertTrue(this.r.equals(new AlignedBox3d(0+translation.getX(),0+translation.getY(),0+translation.getZ(),this.r.getSizeX(),this.r.getSizeY(),this.r.getSizeZ())));
	}

	@Override
	public void isEmpty() {
		assertFalse(this.r.isEmpty());
		this.r.clear();
		assertTrue(this.r.isEmpty());
		this.r.setFromCorners(0, 0, 0, 2, 2, 2);
		assertFalse(this.r.isEmpty());
	}

	@Override
	public void inflateDoubleDoubleDoubleDoubleDoubleDouble() {
		double minX = 1;///this.random.nextDouble()*20;
		double minY = 2;//this.random.nextDouble()*20;
		double minZ = 3;//this.random.nextDouble()*20;
		double maxX = 4;//this.random.nextDouble()*20;
		double maxY = 5;//this.random.nextDouble()*20;
		double maxZ = 6;//this.random.nextDouble()*20;

		AlignedBox3d A = new AlignedBox3d(0,0,0,1,1,1);
		A.setFromCorners(
				minX + A.getMinX() ,minY + A.getMinY() ,minZ + A.getMinZ() ,
				maxX + A.getMaxX() ,maxY + A.getMaxY() ,maxZ + A.getMaxZ() );

		this.r.inflate(minX ,minY ,minZ ,maxX ,maxY ,maxZ);

		assertTrue(this.r.equals(A));
	}

	@Override
	public void translateVector3D() {
		Vector3d translation = new Vector3d(this.random.nextDouble()*20,this.random.nextDouble()*20,this.random.nextDouble()*20);
		this.r.translate(translation);
		assertTrue(this.r.equals(new AlignedBox3d(0+translation.getX(),0+translation.getY(),0+translation.getZ(),this.r.getSizeX(),this.r.getSizeY(),this.r.getSizeZ())));
	}

	@Override
	public void setXDoubleDouble() {
		assertEpsilonEquals(0, this.r.getMinX());
		assertEpsilonEquals(1, this.r.getMaxX());

		this.r.setX(2, 12);

		assertEpsilonEquals(2, this.r.getMinX());
		assertEpsilonEquals(12, this.r.getMaxX());

		this.r.setX(12, -120);

		assertEpsilonEquals(-120, this.r.getMinX());
		assertEpsilonEquals(12, this.r.getMaxX());

		this.r.setX(1, 0);

		assertEpsilonEquals(0, this.r.getMinX());
		assertEpsilonEquals(1, this.r.getMaxX());
	}

	@Override
	public void setYDoubleDouble() {
		assertEpsilonEquals(0, this.r.getMinY());
		assertEpsilonEquals(1, this.r.getMaxY());

		this.r.setY(2, 12);

		assertEpsilonEquals(2, this.r.getMinY());
		assertEpsilonEquals(12, this.r.getMaxY());

		this.r.setY(12, -120);

		assertEpsilonEquals(-120, this.r.getMinY());
		assertEpsilonEquals(12, this.r.getMaxY());

		this.r.setY(1, 0);

		assertEpsilonEquals(0, this.r.getMinY());
		assertEpsilonEquals(1, this.r.getMaxY());
	}

	@Override
	public void setZDoubleDouble() {
		assertEpsilonEquals(0, this.r.getMinZ());
		assertEpsilonEquals(1, this.r.getMaxZ());

		this.r.setZ(2, 12);

		assertEpsilonEquals(2, this.r.getMinZ());
		assertEpsilonEquals(12, this.r.getMaxZ());

		this.r.setZ(12, -120);

		assertEpsilonEquals(-120, this.r.getMinZ());
		assertEpsilonEquals(12, this.r.getMaxZ());

		this.r.setZ(1, 0);

		assertEpsilonEquals(0, this.r.getMinZ());
		assertEpsilonEquals(1, this.r.getMaxZ());
	}

	@Override
	public void testClone() {
		AlignedBox3d b = this.r.clone();

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
		this.r.setFromCorners(0, 0, 0, 2, 2, 2);

		assertEpsilonEquals(0,this.r.distance(new Point3d(0,0,0)));
		assertEpsilonEquals(0,this.r.distance(new Point3d(1,1,1)));
		assertEpsilonEquals(0,this.r.distance(new Point3d(2,0,2)));
		assertEpsilonEquals(0,this.r.distance(new Point3d(0.5,0,2)));

		assertEpsilonEquals(new Point3d(2,0,0).getDistance(new Point3d(3,0,0)),this.r.distance(new Point3d(3,0,0)));

		assertEpsilonEquals(new Point3d(1,2,1).getDistance(new Point3d(1,7,1)),this.r.distance(new Point3d(1,7,1)));

		assertEpsilonEquals(new Point3d(2,2,2).getDistance(new Point3d(3,7,3)),this.r.distance(new Point3d(3,7,3)));

		assertEpsilonEquals(new Point3d(0,2,2).getDistance(new Point3d(-1,7,3)),this.r.distance(new Point3d(-1,7,3)));
	}

	@Override
	public void containsPoint3D() {
		this.r.setFromCorners(0, 0, 0, 2, 2, 2);

		assertTrue(this.r.contains(new Point3f(1, 1, 1)));
		assertTrue(this.r.contains(new Point3d(0, 0, 0)));
		assertTrue(this.r.contains(new Point3f(0, 0, 2)));
		assertTrue(this.r.contains(new Point3d(2, 0, 2)));
		assertTrue(this.r.contains(new Point3f(0.5, 1, 0.5)));
		assertTrue(this.r.contains(new Point3d(1.5, 2, 1.5)));

		assertFalse(this.r.contains(new Point3f(10.5, 2, 10.5)));
		assertFalse(this.r.contains(new Point3d(105, 200, 105)));
		assertFalse(this.r.contains(new Point3f(2, 2, 3)));
		assertFalse(this.r.contains(new Point3f(0, 0, -1)));
		assertFalse(this.r.contains(new Point3d(-2, 2, 0)));
		assertFalse(this.r.contains(new Point3d(10.5, 0, 10.5)));
	}

	@Override
	public void testEquals() {
		AlignedBox3d b = this.r.clone();
		AlignedBox3d c = new AlignedBox3d(this.r);

		assertTrue(b.equals(this.r));
		assertTrue(this.r.equals(b));
		assertTrue(this.r.equals(this.r));
		assertTrue(b.equals(c));
		assertTrue(this.r.equals(c));
	}

	@Override
	public void testHashCode() {
		long bits = 1L;
		bits = 31L * bits + Double.doubleToLongBits(this.r.getMinX());
		bits = 31L * bits + Double.doubleToLongBits(this.r.getMinY());
		bits = 31L * bits + Double.doubleToLongBits(this.r.getMinZ());
		bits = 31L * bits + Double.doubleToLongBits(this.r.getMaxX());
		bits = 31L * bits + Double.doubleToLongBits(this.r.getMaxY());
		bits = 31L * bits + Double.doubleToLongBits(this.r.getMaxZ());
		int hashcode=  (int) (bits ^ (bits >> 32));

		assertEpsilonEquals(hashcode,this.r.hashCode());
	}

	@Override
	public void distanceSquaredPoint3D() {
		this.r.setFromCorners(0, 0, 0, 2, 2, 2);

		assertEpsilonEquals(0,this.r.distanceSquared(new Point3d(0,0,0)));
		assertEpsilonEquals(0,this.r.distanceSquared(new Point3d(1,1,1)));
		assertEpsilonEquals(0,this.r.distanceSquared(new Point3d(2,0,2)));
		assertEpsilonEquals(0,this.r.distanceSquared(new Point3d(0.5,0,2)));

		assertEpsilonEquals(new Point3d(2,0,0).getDistanceSquared(new Point3d(3,0,0)),this.r.distanceSquared(new Point3d(3,0,0)));

		assertEpsilonEquals(new Point3d(1,2,1).getDistanceSquared(new Point3d(1,7,1)),this.r.distanceSquared(new Point3d(1,7,1)));

		assertEpsilonEquals(new Point3d(2,2,2).getDistanceSquared(new Point3d(3,7,3)),this.r.distanceSquared(new Point3d(3,7,3)));

		assertEpsilonEquals(new Point3d(0,2,2).getDistanceSquared(new Point3d(-1,7,3)),this.r.distanceSquared(new Point3d(-1,7,3)));
	}

	@Override
	public void distanceL1Point3D() {
		this.r.setFromCorners(0, 0, 0, 2, 2, 2);

		assertEpsilonEquals(0,this.r.distanceL1(new Point3d(0,0,0)));
		assertEpsilonEquals(0,this.r.distanceL1(new Point3d(1,1,1)));
		assertEpsilonEquals(0,this.r.distanceL1(new Point3d(2,0,2)));
		assertEpsilonEquals(0,this.r.distanceL1(new Point3d(0.5,0,2)));

		assertEpsilonEquals(new Point3d(2,0,0).getDistanceL1(new Point3d(3,0,0)),this.r.distanceL1(new Point3d(3,0,0)));

		assertEpsilonEquals(new Point3d(1,2,1).getDistanceL1(new Point3d(1,7,1)),this.r.distanceL1(new Point3d(1,7,1)));

		assertEpsilonEquals(new Point3d(2,2,2).getDistanceL1(new Point3d(3,7,3)),this.r.distanceL1(new Point3d(3,7,3)));

		assertEpsilonEquals(new Point3d(0,2,2).getDistanceL1(new Point3d(-1,7,3)),this.r.distanceL1(new Point3d(-1,7,3)));
	}

	@Override
	public void distanceLinfPoint3D() {
		this.r.setFromCorners(0, 0, 0, 2, 2, 2);

		assertEpsilonEquals(0,this.r.distanceLinf(new Point3d(0,0,0)));
		assertEpsilonEquals(0,this.r.distanceLinf(new Point3d(1,1,1)));
		assertEpsilonEquals(0,this.r.distanceLinf(new Point3d(2,0,2)));
		assertEpsilonEquals(0,this.r.distanceLinf(new Point3d(0.5,0,2)));

		assertEpsilonEquals(new Point3d(2,0,0).getDistanceLinf(new Point3d(3,0,0)),this.r.distanceLinf(new Point3d(3,0,0)));

		assertEpsilonEquals(new Point3d(1,2,1).getDistanceLinf(new Point3d(1,7,1)),this.r.distanceLinf(new Point3d(1,7,1)));

		assertEpsilonEquals(new Point3d(2,2,2).getDistanceLinf(new Point3d(3,7,3)),this.r.distanceLinf(new Point3d(3,7,3)));

		assertEpsilonEquals(new Point3d(0,2,2).getDistanceLinf(new Point3d(-1,7,3)),this.r.distanceLinf(new Point3d(-1,7,3)));
	}

	@Override
	public void containsDoubleDoubleDouble() {
		this.r.setFromCorners(0, 0, 0, 2, 2, 2);

		assertTrue(this.r.contains(1, 1, 1));
		assertTrue(this.r.contains(0, 0, 0));
		assertTrue(this.r.contains(0, 0, 2));
		assertTrue(this.r.contains(2, 0, 2));
		assertTrue(this.r.contains(0.5, 1, 0.5));
		assertTrue(this.r.contains(1.5, 2, 1.5));

		assertFalse(this.r.contains(10.5, 2, 10.5));
		assertFalse(this.r.contains(105, 200, 105));
		assertFalse(this.r.contains(2, 2, 3));
		assertFalse(this.r.contains(0, 0, -1));
		assertFalse(this.r.contains(-2, 2, 0));
		assertFalse(this.r.contains(10.5, 0, 10.5));
	}

	@Override
	public void intersectsAlignedBox3x() {
		this.r.setFromCorners(0, 0, 0, 2, 2, 2);
		
		AlignedBox3d alignedBox = new AlignedBox3d();
		
		alignedBox.setFromCorners(0, 0, 0, 2, 2, 2);
		assertTrue(this.r.intersects(alignedBox));
		
		alignedBox.setFromCorners(-2, 0, 0, 0, 2, 2);
		assertFalse(this.r.intersects(alignedBox));
		
		alignedBox.setFromCorners(-1.9, 0, 0, 0.1, 2, 2);
		assertTrue(this.r.intersects(alignedBox));
		
		alignedBox.setFromCorners(-1, 0, 0, 1, 2, 2);
		assertTrue(this.r.intersects(alignedBox));
		
		alignedBox.setFromCorners(5, 5, 5, 10, 10, 10);
		assertFalse(this.r.intersects(alignedBox));
		
		alignedBox.setFromCorners(1, 1, 1, 3, 3, 3);
		assertTrue(this.r.intersects(alignedBox));
	}

	@Override
	public void intersectsSphere3x() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void intersectsSegment3x() {
		this.r.setFromCorners(0, 0, 0, 2, 2, 2);

		assertTrue(this.r.intersects(new Segment3d(0,0,0,2,2,2)));
		assertTrue(this.r.intersects(new Segment3d(-1,-1,-1,3,3,3)));
		assertTrue(this.r.intersects(new Segment3d(0.5,0.5,0.5,1,1,1)));
		assertTrue(this.r.intersects(new Segment3d(2,0,0,1,1,2)));
		assertTrue(this.r.intersects(new Segment3d(0,0,0,-1,-1,-1)));

		assertFalse(this.r.intersects(new Segment3d(-1,-1,-1,-3,-3,-3)));
		assertFalse(this.r.intersects(new Segment3d(-0.0001,-0.0001,-0.0001,-0.00001,-0.00001,-0.00001)));
	}

	@Override
	public void intersectsTriangle3x() {
		this.r.setFromCorners(0, 0, 0, 2, 2, 2);

		assertTrue(this.r.intersects(new Triangle3d(0,0,0,2,2,2,1,3,1)));
		assertTrue(this.r.intersects(new Triangle3d(-1,-1,-1,3,3,3,0,10,10)));
		assertTrue(this.r.intersects(new Triangle3d(0.5,0.5,0.5,1.5,1,1,1,2,2)));
		assertTrue(this.r.intersects(new Triangle3d(3,0,2,7,1,1,-0.5,-0.5,-2)));
		assertTrue(this.r.intersects(new Triangle3d(0,0,0,-1,-1,-1,-1,1,1)));

		assertFalse(this.r.intersects(new Triangle3d(-1,-1,-1,-3,0,-3,-5,-5,0)));
		assertFalse(this.r.intersects(new Triangle3d(-0.0001,-0.0001,-0.0001,-0.00001,-0.00001,-0.00001,-1,-1,-1)));
	}

	@Override
	public void intersectsCapsule3x() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void intersectsOrientedBox3x() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void intersectsAbstractPlane3D() {
		this.r.setFromCorners(0, 0, 0, 2, 2, 2);
		
		assertTrue(this.r.intersects(new Plane4d(new Vector3d(0,0,1),new Point3d(1,1,1))));
		assertTrue(this.r.intersects(new Plane4d(new Vector3d(0,1,0),new Point3d(1,1,1))));
		assertTrue(this.r.intersects(new Plane4d(new Vector3d(1,0,0),new Point3d(1,1,1))));
		
		assertFalse(this.r.intersects(new Plane4d(new Vector3d(0,1,0),new Point3d(1,3,1))));
		
		assertTrue(this.r.intersects(new Plane4d(new Vector3d(0,1,0),new Point3d(0,2,2))));
		assertTrue(this.r.intersects(new Plane4d(new Vector3d(1,0,0),new Point3d(0,2,2))));
		
		assertFalse(this.r.intersects(new Plane4d(new Vector3d(1,0,1),new Point3d(4,0,4))));
	}
	
	/**
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testModifyProperties() {
		Point3d min = new Point3d(0,0,0);
		Point3d max = new Point3d(1,1,1);
		
		AlignedBox3d test = new AlignedBox3d(min,max);
		
		assertTrue(test.equals(new AlignedBox3f(0,0,0,1,1,1)));
		
		min.set(2, 2, 2);
		max.set(4, 4, 4);
		
		assertTrue(test.equals(new AlignedBox3f(2,2,2,2,2,2)));
		
		test.setFromCorners(0,0,0,10,5,10);
		
		assertTrue(min.equals(new Point3f(0,0,0)));
		assertTrue(max.equals(new Point3f(10,5,10)));
		
		AlignedBox3d box = new AlignedBox3d(-1,-1,-1,2,2,2);
		test = new AlignedBox3d(box);
		
		assertTrue(test.equals(new AlignedBox3d(-1,-1,-1,2,2,2)));
		
		box.setFromCorners(-10, 10, 1, 10, -10, 0);
		
		assertTrue(test.equals(new AlignedBox3d(-10,-10,0,20,20,1)));
		
		box.setFromCornersProperties(min, max);	
		min.set(2, 2, 2);
		max.set(4, 4, 4);
		
		assertTrue(test.equals(new AlignedBox3d(-10,-10,0,20,20,1)));
		assertTrue(box.equals(new AlignedBox3f(2,2,2,2,2,2)));
		
		test.setFromCornersProperties(min, max);	
		test.setFromCorners(0, 0, 0, 1, 1,1);
		
		assertTrue(box.equals(new AlignedBox3f(0,0,0,1,1,1)));
		
		assertTrue(min.equals(new Point3f(0,0,0)));
		assertTrue(max.equals(new Point3f(1,1,1)));
		
	}
}
