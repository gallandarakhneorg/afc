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

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class Plane4fTest extends AbstractPlane3DTestCase<AbstractPlane4F> {

	@Override
	protected AbstractPlane4F createPlane() {
		Plane4f p = new Plane4f(Math.random()*20,Math.random()*20,Math.random()*20,Math.random()*20);
		p.normalize();
		return p;
	}

	@Test
	@Override
	public void testClone() {
		Plane4f plane2 = (Plane4f) this.r.clone();
		
		assertTrue(plane2.equals(this.r));
	}

	@Test
	@Override
	public void distanceToPoint3D() {
		Point3D a = new Point3f(Math.random()*20,Math.random()*20,Math.random()*20);
		
		double distance = Math.abs(r.getEquationComponentA()*a.getX()+r.getEquationComponentB()*a.getY() +r.getEquationComponentC()*a.getZ() +r.getEquationComponentD())
				/Math.sqrt(r.getEquationComponentA()*r.getEquationComponentA()+r.getEquationComponentB()*r.getEquationComponentB()+r.getEquationComponentC()*r.getEquationComponentC());
	
		assertEpsilonEquals(Math.abs(this.r.distanceTo(a)),distance);
	}

	@Test
	public void distanceToDoubleDoubleDouble() {
		Point3D a = new Point3f(Math.random()*20,Math.random()*20,Math.random()*20);
		
		double distance = Math.abs(r.getEquationComponentA()*a.getX()+r.getEquationComponentB()*a.getY() +r.getEquationComponentC()*a.getZ() +r.getEquationComponentD())
				/Math.sqrt(r.getEquationComponentA()*r.getEquationComponentA()+r.getEquationComponentB()*r.getEquationComponentB()+r.getEquationComponentC()*r.getEquationComponentC());
	
		assertEpsilonEquals(Math.abs(this.r.distanceTo(a.getX(), a.getY(), a.getZ())),distance);
	}

	@Test
	@Override
	public void distanceToPlane3D() {
		Plane4f plane1 = new Plane4f(1,1,1,5);
		Plane4f plane2 = new Plane4f(1,1,1,10);
		
			
		double distance1 = Math.abs(plane1.distanceTo(new Point3f(1,-8,-3)));
		double distance2 = Math.abs(plane2.distanceTo(new Point3f(1,-4,-2)));
		
		assertEpsilonEquals(Math.abs(plane2.distanceTo(plane1)),Math.abs(plane1.distanceTo(plane2)));
		//assertEpsilonEquals(Math.abs(plane2.distanceTo(plane1)),distance1);
		assertEpsilonEquals(Math.abs(plane2.distanceTo(plane1)),distance2);
	}

	@Test
	@Override
	public void getIntersectionPlane3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void getIntersectionSegment3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void getProjectionPoint3D() {
		Point3f point = new Point3f(this.random.nextInt()%20,this.random.nextInt()%20,this.random.nextInt()%20);
		
		double k = (r.getEquationComponentA()*point.x+r.getEquationComponentB()*point.y+r.getEquationComponentC()*point.z+r.getEquationComponentD());
	
		double xProj = point.x -k*r.getEquationComponentA();
		double yProj = point.y -k*r.getEquationComponentB();
		double zProj = point.z -k*r.getEquationComponentC();
		
		Point3f projection = new Point3f(xProj,yProj,zProj);
		
		Point3f expected = this.r.getProjection(point);
		
		assertEpsilonEquals(projection.x,(expected.x));
		assertEpsilonEquals(projection.y,(expected.y));
		assertEpsilonEquals(projection.z,(expected.z));	
	}

	@Test
	public void getProjectionDoubleDoubleDouble() {
Point3f point = new Point3f(this.random.nextInt()%20,this.random.nextInt()%20,this.random.nextInt()%20);
		
		double k = (r.getEquationComponentA()*point.x+r.getEquationComponentB()*point.y+r.getEquationComponentC()*point.z+r.getEquationComponentD());
	
		double xProj = point.x -k*r.getEquationComponentA();
		double yProj = point.y -k*r.getEquationComponentB();
		double zProj = point.z -k*r.getEquationComponentC();
		
		Point3f projection = new Point3f(xProj,yProj,zProj);
		
		Point3f expected = (Point3f) this.r.getProjection(point.x,point.y,point.z);
		
		assertEpsilonEquals(projection.x,(expected.x));
		assertEpsilonEquals(projection.y,(expected.y));
		assertEpsilonEquals(projection.z,(expected.z));	
	}

	@Test
	@Override
	public void isValid() {
		//Function isValid() of AbstractPlane4F not implemented
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void classifiesPlane3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void classifiesPoint3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void classifiesSphere3f() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void classifiesAlignedBox3f() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsPlane3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsPoint3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsSphere3f() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsAlignedBox3f() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void classifiesDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
    public void setPivotPoint3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
    public void setPivotDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
    public void getPivot() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void computePointProjectionDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}
			
	@Test
	public void transformTransform3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void transformTransform3DPoint3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void translateVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void translateDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void rotateQuaternion() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void rotateQuaternionPoint3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void rotateVector3DDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void rotateVector3DDoublePoint3D() {
		throw new UnsupportedOperationException();
	}

}