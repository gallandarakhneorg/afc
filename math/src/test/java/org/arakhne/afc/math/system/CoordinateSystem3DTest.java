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
package org.arakhne.afc.math.system;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Quaternion;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.continous.object4d.AxisAngle4f;
import org.arakhne.afc.math.euclide.EuclidianPoint2D;
import org.arakhne.afc.math.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.matrix.Matrix4f;
import org.arakhne.afc.math.object.Direction2D;
import org.arakhne.afc.math.object.Direction3D;
import org.arakhne.util.ref.AbstractTestCase;


/**
 * Unit test for {@link CoordinateSystem3D}.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CoordinateSystem3DTest extends AbstractTestCase {

	/**
	 */
	public void testGetDefaultSimulationCoordinateSystem() {
		CoordinateSystem3D cs = CoordinateSystemConstants.SIMULATION_3D;
		assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND,cs);
	}

	/**
	 */
	public void testGetDefaultJava3DCoordinateSystem() {
		CoordinateSystem3D cs = CoordinateSystemConstants.JAVA3D_3D;
		assertSame(CoordinateSystem3D.XZY_RIGHT_HAND,cs);
	}
	
	private Point3f rotate(Point3f p, AxisAngle4f aa) {
		Matrix4f m = new Matrix4f();
		m.set(aa);
		Point3f pp = new Point3f(p);
		m.transform(pp);
		return pp;
	}

	private Point3f rotate(Point3f p, Quaternion q) {
		Matrix4f m = new Matrix4f();
		m.set(q);
		Point3f pp = new Point3f(p);
		m.transform(pp);
		return pp;
	}

	@SuppressWarnings("boxing")
	private void runTestFromVectors(Class<?>[] types) {
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0.f, 0.f, 0.f, 0.f );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 0, 0, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 0, 0, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 0, 1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 0, -1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 0, 1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 0, 1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 0, -1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 0, -1, -1 );
		
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 1, 0, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, -1, 0, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 1, 0, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 1, 0, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, -1, 0, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, -1, 0, -1 );
		assertSame(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.fromVectors(
				0, 1, 1, 0 ));
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 1, -1, 0 );
		assertSame(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.fromVectors(
				0, -1, 1, 0 ));
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, -1, -1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 1, 1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 1, 1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 1, -1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, -1, 1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, 1, -1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, -1, 1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, -1, -1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				0, -1, -1, -1 );
		
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 0, 0, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 0, 0, 0 );
		assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.fromVectors(
				1, 0, 0, 1 ));
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 0, 0, -1 );
		assertSame(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.fromVectors(
				-1, 0, 0, 1 ));
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 0, 0, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 0, 1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 0, -1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 0, 1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 0, -1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 0, 1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 0, 1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 0, -1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 0, 1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 0, -1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 0, 1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 0, -1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 0, -1, -1 );
		
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 1, 0, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, -1, 0, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 1, 0, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, -1, 0, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 1, 0, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 1, 0, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, -1, 0, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 1, 0, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, -1, 0, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 1, 0, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, -1, 0, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, -1, 0, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 1, 1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 1, -1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, -1, 1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 1, 1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, -1, -1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 1, -1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, -1, 1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, -1, -1, 0 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 1, 1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 1, 1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 1, -1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, -1, 1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 1, 1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, 1, -1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, -1, 1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 1, 1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, -1, -1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 1, -1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, -1, 1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				1, -1, -1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, 1, -1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, -1, 1, -1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, -1, -1, 1 );
		assertException(CoordinateSystemNotFoundException.class, CoordinateSystem3D.class, "fromVectors", types, //$NON-NLS-1$
				-1, -1, -1, -1 );
	}
	
	/**
	 */
	public void testFromVectorsFloatFloatFloatFloat() {
		runTestFromVectors(new Class<?>[] {float.class, float.class, float.class, float.class});
	}

	/**
	 */
	public void testFromVectorsIntIntIntIntInt() {
		runTestFromVectors(new Class<?>[] {int.class, int.class, int.class, int.class});
	}

	/**
	 */
	public void testToDefaultPoint3d() {
		Point3f pt, pt2, pt3;
		
		pt = randomPoint3D();
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toDefault(pt2);
		assertEpsilonEquals(pt, pt2);
		
		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		CoordinateSystem3D.XYZ_LEFT_HAND.toDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), -pt.getZ(), pt.getY());
		CoordinateSystem3D.XZY_RIGHT_HAND.toDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		CoordinateSystem3D.XZY_LEFT_HAND.toDefault(pt2);
		assertEpsilonEquals(pt3, pt2);
	}

	/**
	 */
	public void testToDefaultPoint3f() {
		Point3f pt, pt2, pt3;
		
		pt = randomPoint3F();
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toDefault(pt2);
		assertEpsilonEquals(pt, pt2);
		
		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		CoordinateSystem3D.XYZ_LEFT_HAND.toDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), -pt.getZ(), pt.getY());
		CoordinateSystem3D.XZY_RIGHT_HAND.toDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		CoordinateSystem3D.XZY_LEFT_HAND.toDefault(pt2);
		assertEpsilonEquals(pt3, pt2);
	}

	/**
	 */
	public void testFromDefaultPoint3d() {
		Point3f pt, pt2, pt3;
		
		pt = randomPoint3D();
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt, pt2);
		
		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), -pt.getY());
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);
	}
	
	/**
	 */
	public void testFromDefaultPoint3f() {
		Point3f pt, pt2, pt3;
		
		pt = randomPoint3F();
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt, pt2);
		
		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), -pt.getY());
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);
	}

	/**
	 */
	public void testToDefaultVector3d() {
		Vector3f vt, vt2, vt3;
		
		vt = randomVector3D();
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toDefault(vt2);
		assertEpsilonEquals(vt, vt2);
		
		vt2 = new Vector3f(vt);
		vt3 = new Vector3f(vt.getX(), -vt.getY(), vt.getZ());
		CoordinateSystem3D.XYZ_LEFT_HAND.toDefault(vt2);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		vt3 = new Vector3f(vt.getX(), -vt.getZ(), vt.getY());
		CoordinateSystem3D.XZY_RIGHT_HAND.toDefault(vt2);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		CoordinateSystem3D.XZY_LEFT_HAND.toDefault(vt2);
		assertEpsilonEquals(vt3, vt2);
	}

	/**
	 */
	public void testToDefaultVector3f() {
		Vector3f vt, vt2, vt3;
		
		vt = randomVector3F();
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toDefault(vt2);
		assertEpsilonEquals(vt, vt2);
		
		vt2 = new Vector3f(vt);
		vt3 = new Vector3f(vt.getX(), -vt.getY(), vt.getZ());
		CoordinateSystem3D.XYZ_LEFT_HAND.toDefault(vt2);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		vt3 = new Vector3f(vt.getX(), -vt.getZ(), vt.getY());
		CoordinateSystem3D.XZY_RIGHT_HAND.toDefault(vt2);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		CoordinateSystem3D.XZY_LEFT_HAND.toDefault(vt2);
		assertEpsilonEquals(vt3, vt2);
	}

	/**
	 */
	public void testFromDefaultVector3d() {
		Point3f pt, pt2, pt3;
		
		pt = randomPoint3D();
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt, pt2);
		
		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), -pt.getY());
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);
	}

	/**
	 */
	public void testFromDefaultVector3f() {
		Point3f pt, pt2, pt3;
		
		pt = randomPoint3F();
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt, pt2);
		
		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), -pt.getY());
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(pt2);
		assertEpsilonEquals(pt3, pt2);
	}

	/**
	 */
	public void testToDefaultAxisAngle4d() {
		AxisAngle4f aa, aa2, aa3;
		
		aa = randomAxisAngle4f();
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toDefault(aa2);
		assertEpsilonEquals(aa, aa2);
		
		aa2 = new AxisAngle4f(aa);
		aa3 = new AxisAngle4f(aa.x, -aa.y, aa.z, -aa.angle);
		CoordinateSystem3D.XYZ_LEFT_HAND.toDefault(aa2);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		aa3 = new AxisAngle4f(aa.x, -aa.z, aa.y, aa.angle);
		CoordinateSystem3D.XZY_RIGHT_HAND.toDefault(aa2);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		aa3 = new AxisAngle4f(aa.x, aa.z, aa.y, -aa.angle);
		CoordinateSystem3D.XZY_LEFT_HAND.toDefault(aa2);
		assertEpsilonEquals(aa3, aa2);
	}
	
	/**
	 */
	public void testToDefaultAxisAngle4f() {
		AxisAngle4f aa, aa2, aa3;
		
		aa = randomAxisAngle4f();
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toDefault(aa2);
		assertEpsilonEquals(aa, aa2);
		
		aa2 = new AxisAngle4f(aa);
		aa3 = new AxisAngle4f(aa.x, -aa.y, aa.z, -aa.angle);
		CoordinateSystem3D.XYZ_LEFT_HAND.toDefault(aa2);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		aa3 = new AxisAngle4f(aa.x, -aa.z, aa.y, aa.angle);
		CoordinateSystem3D.XZY_RIGHT_HAND.toDefault(aa2);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		aa3 = new AxisAngle4f(aa.x, aa.z, aa.y, -aa.angle);
		CoordinateSystem3D.XZY_LEFT_HAND.toDefault(aa2);
		assertEpsilonEquals(aa3, aa2);
	}

	/**
	 */
	public void testFromDefaultAxisAngle4d() {
		AxisAngle4f aa, aa2, aa3;
		
		aa = randomAxisAngle4f();
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(aa2);
		assertEpsilonEquals(aa, aa2);
		
		aa2 = new AxisAngle4f(aa);
		aa3 = new AxisAngle4f(aa.x, -aa.y, aa.z, -aa.angle);
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(aa2);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		aa3 = new AxisAngle4f(aa.x, aa.z, -aa.y, aa.angle);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(aa2);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		aa3 = new AxisAngle4f(aa.x, aa.z, aa.y, -aa.angle);
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(aa2);
		assertEpsilonEquals(aa3, aa2);
	}
	
	/**
	 */
	public void testFromDefaultAxisAngle4f() {
		AxisAngle4f aa, aa2, aa3;
		
		aa = randomAxisAngle4f();
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(aa2);
		assertEpsilonEquals(aa, aa2);
		
		aa2 = new AxisAngle4f(aa);
		aa3 = new AxisAngle4f(aa.x, -aa.y, aa.z, -aa.angle);
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(aa2);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		aa3 = new AxisAngle4f(aa.x, aa.z, -aa.y, aa.angle);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(aa2);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		aa3 = new AxisAngle4f(aa.x, aa.z, aa.y, -aa.angle);
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(aa2);
		assertEpsilonEquals(aa3, aa2);
	}

	private void quaternionToDefault(CoordinateSystem3D system, Quaternion q) {
		AxisAngle4f aa = new AxisAngle4f();
		aa.set(q);
		system.toDefault(aa);
		Quaternion expected = new Quaternion();
		expected.set(aa);
		
		Quaternion q1 = new Quaternion();
		q1.set(q);
		system.toDefault(q1);
		
		assertEpsilonEquals(expected, q1);
	}

	/**
	 */
	public void testToDefaultQuat4d() {
		Quaternion expected;
		
		expected = randomQuaternion();
		
		quaternionToDefault(CoordinateSystem3D.XYZ_RIGHT_HAND, expected);
		quaternionToDefault(CoordinateSystem3D.XZY_RIGHT_HAND, expected);
		quaternionToDefault(CoordinateSystem3D.XYZ_LEFT_HAND, expected);
		quaternionToDefault(CoordinateSystem3D.XZY_LEFT_HAND, expected);
	}

	/**
	 */
	public void testToDefaultQuat4f() {
		Quaternion expected;
		
		expected = randomQuaternion();
		
		quaternionToDefault(CoordinateSystem3D.XYZ_RIGHT_HAND, expected);
		quaternionToDefault(CoordinateSystem3D.XZY_RIGHT_HAND, expected);
		quaternionToDefault(CoordinateSystem3D.XYZ_LEFT_HAND, expected);
		quaternionToDefault(CoordinateSystem3D.XZY_LEFT_HAND, expected);
	}

	private void matrixToDefault(CoordinateSystem3D system, Matrix4f m) {
		Quaternion expectedRotation = new Quaternion();
		Vector3f expectedTranslation = new Vector3f();
		Quaternion rotation = new Quaternion();
		Vector3f translation = new Vector3f();
		
		m.get(expectedRotation);
		m.get(expectedTranslation);
		system.toDefault(expectedRotation);
		system.toDefault(expectedTranslation);
		
		Matrix4f mm = m.clone();
		system.toDefault(mm);
		mm.get(rotation);
		mm.get(translation);
		
		assertEpsilonEquals(expectedRotation, rotation);
		assertEpsilonEquals(expectedTranslation, translation);
	}

	/**
	 */
	public void testToDefaultMatrix4d() {
		Matrix4f matrix = new Matrix4f();
		Quaternion q = randomQuaternion();
		Vector3f t = randomVector3D();
		matrix.setRotation(q);
		matrix.setTranslation(t);
		
		matrixToDefault(CoordinateSystem3D.XYZ_RIGHT_HAND, matrix);
		matrixToDefault(CoordinateSystem3D.XZY_RIGHT_HAND, matrix);
		matrixToDefault(CoordinateSystem3D.XYZ_LEFT_HAND, matrix);
		matrixToDefault(CoordinateSystem3D.XZY_LEFT_HAND, matrix);
	}

	/**
	 */
	public void testToDefaultMatrix4f() {
		Matrix4f matrix = new Matrix4f();
		Quaternion q = randomQuaternion();
		Vector3f t = randomVector3F();
		matrix.setRotation(q);
		matrix.setTranslation(t);
		
		matrixToDefault(CoordinateSystem3D.XYZ_RIGHT_HAND, matrix);
		matrixToDefault(CoordinateSystem3D.XZY_RIGHT_HAND, matrix);
		matrixToDefault(CoordinateSystem3D.XYZ_LEFT_HAND, matrix);
		matrixToDefault(CoordinateSystem3D.XZY_LEFT_HAND, matrix);
	}

	private void quaternionFromDefault(CoordinateSystem3D system, Quaternion q) {
		AxisAngle4f aa = new AxisAngle4f();
		aa.set(q);
		system.fromDefault(aa);
		Quaternion expected = new Quaternion();
		expected.set(aa);
		
		Quaternion q1 = new Quaternion();
		q1.set(q);
		system.fromDefault(q1);
		
		assertEpsilonEquals(expected, q1);
	}

	/**
	 */
	public void testFromDefaultQuat4d() {
		Quaternion expected;
		
		expected = randomQuaternion();
		
		quaternionFromDefault(CoordinateSystem3D.XYZ_RIGHT_HAND, expected);
		quaternionFromDefault(CoordinateSystem3D.XZY_RIGHT_HAND, expected);
		quaternionFromDefault(CoordinateSystem3D.XYZ_LEFT_HAND, expected);
		quaternionFromDefault(CoordinateSystem3D.XZY_LEFT_HAND, expected);
	}

	/**
	 */
	public void testFromDefaultQuat4f() {
		Quaternion expected;
		
		expected = randomQuaternion();
		
		quaternionFromDefault(CoordinateSystem3D.XYZ_RIGHT_HAND, expected);
		quaternionFromDefault(CoordinateSystem3D.XZY_RIGHT_HAND, expected);
		quaternionFromDefault(CoordinateSystem3D.XYZ_LEFT_HAND, expected);
		quaternionFromDefault(CoordinateSystem3D.XZY_LEFT_HAND, expected);
	}

	private void matrixFromDefault(CoordinateSystem3D system, Matrix4f m) {
		Quaternion expectedRotation = new Quaternion();
		Vector3f expectedTranslation = new Vector3f();
		Quaternion rotation = new Quaternion();
		Vector3f translation = new Vector3f();
		
		m.get(expectedRotation);
		m.get(expectedTranslation);
		system.fromDefault(expectedRotation);
		system.fromDefault(expectedTranslation);
		
		Matrix4f mm = m.clone();
		system.fromDefault(mm);
		mm.get(rotation);
		mm.get(translation);
		
		assertEpsilonEquals(expectedRotation, rotation);
		assertEpsilonEquals(expectedTranslation, translation);
	}

	/**
	 */
	public void testFromDefaultMatrix4d() {
		Matrix4f matrix = new Matrix4f();
		Quaternion q = randomQuaternion();
		Vector3f t = randomVector3D();
		matrix.setRotation(q);
		matrix.setTranslation(t);
		
		matrixFromDefault(CoordinateSystem3D.XYZ_RIGHT_HAND, matrix);
		matrixFromDefault(CoordinateSystem3D.XZY_RIGHT_HAND, matrix);
		matrixFromDefault(CoordinateSystem3D.XYZ_LEFT_HAND, matrix);
		matrixFromDefault(CoordinateSystem3D.XZY_LEFT_HAND, matrix);
	}

	/**
	 */
	public void testFromDefaultMatrix4f() {
		Matrix4f matrix = new Matrix4f();
		Quaternion q = randomQuaternion();
		Vector3f t = randomVector3F();
		matrix.setRotation(q);
		matrix.setTranslation(t);
		
		matrixFromDefault(CoordinateSystem3D.XYZ_RIGHT_HAND, matrix);
		matrixFromDefault(CoordinateSystem3D.XZY_RIGHT_HAND, matrix);
		matrixFromDefault(CoordinateSystem3D.XYZ_LEFT_HAND, matrix);
		matrixFromDefault(CoordinateSystem3D.XZY_LEFT_HAND, matrix);
	}

	/**
	 */
	public void testToSystemPoint3dCoordinateSystem3D() {
		Point3f pt, pt2, pt3;
		
		pt = randomPoint3D();
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), -pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		//---
		
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), -pt.getY());
		assertEpsilonEquals(pt3, pt2);

		//---
		
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), -pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getY(), -pt.getZ());
		assertEpsilonEquals(pt3, pt2);

		//---
		
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), -pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getY(), -pt.getZ());
		assertEpsilonEquals(pt3, pt2);
	}

	/**
	 */
	public void testToSystemPoint3fCoordinateSystem3D() {
		Point3f pt, pt2, pt3;
		
		pt = randomPoint3F();
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), -pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		//---
		
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), -pt.getY(), pt.getZ());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), -pt.getY());
		assertEpsilonEquals(pt3, pt2);

		//---
		
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), -pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getY(), -pt.getZ());
		assertEpsilonEquals(pt3, pt2);

		//---
		
		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		pt3 = new Point3f(pt.getX(), -pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new Point3f(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		pt3 = new Point3f(pt.getX(), pt.getY(), -pt.getZ());
		assertEpsilonEquals(pt3, pt2);
	}

	/**
	 */
	public void testToSystemEuclidianPoint3dCoordinateSystem3D() {
		EuclidianPoint3D pt, pt2, pt3;
		
		pt = new EuclidianPoint3D(randomPoint3D());
		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		pt3 = new EuclidianPoint3D(pt.getX(), -pt.getY(), pt.getZ());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		pt3 = new EuclidianPoint3D(pt.getX(), pt.getZ(), -pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		pt3 = new EuclidianPoint3D(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		//---
		
		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		pt3 = new EuclidianPoint3D(pt.getX(), -pt.getY(), pt.getZ());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		pt3 = new EuclidianPoint3D(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		pt3 = new EuclidianPoint3D(pt.getX(), pt.getZ(), -pt.getY());
		assertEpsilonEquals(pt3, pt2);

		//---
		
		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		pt3 = new EuclidianPoint3D(pt.getX(), -pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		pt3 = new EuclidianPoint3D(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		pt3 = new EuclidianPoint3D(pt.getX(), pt.getY(), -pt.getZ());
		assertEpsilonEquals(pt3, pt2);

		//---
		
		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(pt, pt2);

		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		pt3 = new EuclidianPoint3D(pt.getX(), pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		pt3 = new EuclidianPoint3D(pt.getX(), -pt.getZ(), pt.getY());
		assertEpsilonEquals(pt3, pt2);

		pt2 = new EuclidianPoint3D(pt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(pt2, CoordinateSystem3D.XZY_LEFT_HAND);
		pt3 = new EuclidianPoint3D(pt.getX(), pt.getY(), -pt.getZ());
		assertEpsilonEquals(pt3, pt2);
	}

	/**
	 */
	public void testToSystemVector3dCoordinateSystem3D() {
		Vector3f vt, vt2, vt3;
		
		vt = randomVector3D();
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), -vt.getY(), vt.getZ());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), -vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		//---
		
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), -vt.getY(), vt.getZ());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), -vt.getY());
		assertEpsilonEquals(vt3, vt2);

		//---
		
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), -vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getY(), -vt.getZ());
		assertEpsilonEquals(vt3, vt2);

		//---
		
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), -vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getY(), -vt.getZ());
		assertEpsilonEquals(vt3, vt2);
	}

	/**
	 */
	public void testToSystemVector3fCoordinateSystem3D() {
		Vector3f vt, vt2, vt3;
		
		vt = randomVector3F();
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), -vt.getY(), vt.getZ());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), -vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		//---
		
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), -vt.getY(), vt.getZ());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), -vt.getY());
		assertEpsilonEquals(vt3, vt2);

		//---
		
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), -vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getY(), -vt.getZ());
		assertEpsilonEquals(vt3, vt2);

		//---
		
		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		vt3 = new Vector3f(vt.getX(), -vt.getZ(), vt.getY());
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Vector3f(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		vt3 = new Vector3f(vt.getX(), vt.getY(), -vt.getZ());
		assertEpsilonEquals(vt3, vt2);
	}

	/**
	 */
	public void testToSystemDirection3DCoordinateSystem3D() {
		Direction3D vt, vt2, vt3;
		
		vt = new Direction3D(randomVector3D(), 0.f);
		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		vt3 = new Direction3D(vt.getX(), -vt.getY(), vt.getZ(), 0.f);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		vt3 = new Direction3D(vt.getX(), vt.getZ(), -vt.getY(), 0.f);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		vt3 = new Direction3D(vt.getX(), vt.getZ(), vt.getY(), 0.f);
		assertEpsilonEquals(vt3, vt2);

		//---
		
		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		vt3 = new Direction3D(vt.getX(), -vt.getY(), vt.getZ(), 0.f);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		vt3 = new Direction3D(vt.getX(), vt.getZ(), vt.getY(), 0.f);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		vt3 = new Direction3D(vt.getX(), vt.getZ(), -vt.getY(), 0.f);
		assertEpsilonEquals(vt3, vt2);

		//---
		
		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		vt3 = new Direction3D(vt.getX(), -vt.getZ(), vt.getY(), 0.f);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		vt3 = new Direction3D(vt.getX(), vt.getZ(), vt.getY(), 0.f);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		vt3 = new Direction3D(vt.getX(), vt.getY(), -vt.getZ(), 0.f);
		assertEpsilonEquals(vt3, vt2);

		//---
		
		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(vt, vt2);

		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_LEFT_HAND);
		vt3 = new Direction3D(vt.getX(), vt.getZ(), vt.getY(), 0.f);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		vt3 = new Direction3D(vt.getX(), -vt.getZ(), vt.getY(), 0.f);
		assertEpsilonEquals(vt3, vt2);

		vt2 = new Direction3D(vt);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(vt2, CoordinateSystem3D.XZY_LEFT_HAND);
		vt3 = new Direction3D(vt.getX(), vt.getY(), -vt.getZ(), 0.f);
		assertEpsilonEquals(vt3, vt2);
	}

	/**
	 */
	public void testToSystemAxisAngle4dCoordinateSystem3D() {
		AxisAngle4f aa, aa2, aa3;
		
		aa = randomAxisAngle4f();
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(aa, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), -aa.getY(), aa.getZ(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_LEFT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getZ(), -aa.getY(), aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_RIGHT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getZ(), aa.getY(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		//---
		
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(aa, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_LEFT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), -aa.getY(), aa.getZ(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_LEFT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getZ(), aa.getY(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_RIGHT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getZ(), -aa.getY(), aa.angle);
		assertEpsilonEquals(aa3, aa2);

		//---
		
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(aa, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_LEFT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), -aa.getZ(), aa.getY(), aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getZ(), aa.getY(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_RIGHT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getY(), -aa.getZ(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		//---
		
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(aa, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_LEFT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getZ(), aa.getY(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), -aa.getZ(), aa.getY(), aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_LEFT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getY(), -aa.getZ(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);
	}

	/**
	 */
	public void testToSystemAxisAngle4fCoordinateSystem3D() {
		AxisAngle4f aa, aa2, aa3;
		
		aa = randomAxisAngle4f();
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(aa, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), -aa.getY(), aa.getZ(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_LEFT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getZ(), -aa.getY(), aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_RIGHT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getZ(), aa.getY(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		//---
		
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(aa, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_LEFT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), -aa.getY(), aa.getZ(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_LEFT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getZ(), aa.getY(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_RIGHT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getZ(), -aa.getY(), aa.angle);
		assertEpsilonEquals(aa3, aa2);

		//---
		
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(aa, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_LEFT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), -aa.getZ(), aa.getY(), aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getZ(), aa.getY(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_RIGHT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getY(), -aa.getZ(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		//---
		
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(aa, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_LEFT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getZ(), aa.getY(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XYZ_RIGHT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), -aa.getZ(), aa.getY(), aa.angle);
		assertEpsilonEquals(aa3, aa2);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(aa2, CoordinateSystem3D.XZY_LEFT_HAND);
		aa3 = new AxisAngle4f(aa.getX(), aa.getY(), -aa.getZ(), -aa.angle);
		assertEpsilonEquals(aa3, aa2);
	}

	private void quaternionToSystem(CoordinateSystem3D source, CoordinateSystem3D target, Quaternion q) {
		AxisAngle4f aa = new AxisAngle4f();
		aa.set(q);
		source.toSystem(aa, target);
		Quaternion expected = new Quaternion();
		expected.set(aa);
		
		Quaternion q1 = new Quaternion();
		q1.set(q);
		source.toSystem(q1, target);
		
		assertEpsilonEquals(expected, q1);
	}

	/**
	 */
	public void testToSystemQuat4dCoordinateSystem3D() {
		Quaternion expected = randomQuaternion();
		
		quaternionToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND, expected);

		quaternionToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND, expected);

		quaternionToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND, expected);

		quaternionToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND, expected);
	}
	
	/**
	 */
	public void testToSystemQuat4fCoordinateSystem3D() {
		Quaternion expected = randomQuaternion();
		
		quaternionToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND, expected);

		quaternionToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND, expected);

		quaternionToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND, expected);

		quaternionToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND, expected);
		quaternionToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND, expected);
	}

	/**
	 */
	public void testHeightTuple3f() {
		Point3f tuple = randomPoint3F();
		
		assertEquals(tuple.getZ(),CoordinateSystem3D.XYZ_LEFT_HAND.height(tuple));
		assertEquals(tuple.getZ(),CoordinateSystem3D.XYZ_RIGHT_HAND.height(tuple));
		assertEquals(tuple.getY(),CoordinateSystem3D.XZY_LEFT_HAND.height(tuple));
		assertEquals(tuple.getY(),CoordinateSystem3D.XZY_RIGHT_HAND.height(tuple));
	}

	/**
	 */
	public void testSetSideTuple3dFloat() {
		Point3f o = randomPoint3D();
		float side = o.getX()+o.getY()+o.getZ();
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.setSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(side, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.setSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(side, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(side, tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(side, tuple.getZ());
	}

	/**
	 */
	public void testSetSideTuple3fFloat() {
		Point3f o = randomPoint3F();
		float side = o.getX()+o.getY()+o.getZ();
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.setSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(side, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.setSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(side, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(side, tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(side, tuple.getZ());
	}
	
	/**
	 */
	public void testSetViewTuple3dFloat() {
		Point3f o = randomPoint3D();
		float view = o.getX()+o.getY()+o.getZ();
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.setView(tuple, view);
		assertEpsilonEquals(view, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.setView(tuple, view);
		assertEpsilonEquals(view, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setView(tuple, view);
		assertEpsilonEquals(view, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setView(tuple, view);
		assertEpsilonEquals(view, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
	}

	/**
	 */
	public void testSetViewTuple3fFloat() {
		Point3f o = randomPoint3F();
		float view = o.getX()+o.getY()+o.getZ();
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.setView(tuple, view);
		assertEpsilonEquals(view, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.setView(tuple, view);
		assertEpsilonEquals(view, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setView(tuple, view);
		assertEpsilonEquals(view, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setView(tuple, view);
		assertEpsilonEquals(view, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
	}

	/**
	 */
	public void testSetHeightTuple3dFloat() {
		Point3f o = randomPoint3D();
		float height = o.getX()+o.getY()+o.getZ();
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.setHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(height, tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.setHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(height, tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(height, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(height, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
	}

	/**
	 */
	public void testAddSideTuple3dFloat() {
		Point3f o = randomPoint3D();
		float side = o.getX()+o.getY()+o.getZ();
		float expectedY = o.getY() + side; 
		float expectedZ = o.getZ() + side; 
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.addSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(expectedY, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.addSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(expectedY, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(expectedZ, tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(expectedZ, tuple.getZ());
	}

	/**
	 */
	public void testAddSideTuple3fFloat() {
		Point3f o = randomPoint3F();
		float side = o.getX()+o.getY()+o.getZ();
		float expectedY = o.getY() + side; 
		float expectedZ = o.getZ() + side; 
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.addSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(expectedY, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.addSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(expectedY, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(expectedZ, tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addSide(tuple, side);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(expectedZ, tuple.getZ());
	}

	/**
	 */
	public void testAddViewTuple3dFloat() {
		Point3f o = randomPoint3D();
		float view = o.getX()+o.getY()+o.getZ();
		float expected = o.getX() + view; 
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.addView(tuple, view);
		assertEpsilonEquals(expected, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.addView(tuple, view);
		assertEpsilonEquals(expected, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addView(tuple, view);
		assertEpsilonEquals(expected, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addView(tuple, view);
		assertEpsilonEquals(expected, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
	}

	/**
	 */
	public void testAddViewTuple3fFloat() {
		Point3f o = randomPoint3F();
		float view = o.getX()+o.getY()+o.getZ();
		float expected = o.getX() + view; 
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.addView(tuple, view);
		assertEpsilonEquals(expected, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.addView(tuple, view);
		assertEpsilonEquals(expected, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addView(tuple, view);
		assertEpsilonEquals(expected, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addView(tuple, view);
		assertEpsilonEquals(expected, tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
	}

	/**
	 */
	public void testAddHeightTuple3dFloat() {
		Point3f o = randomPoint3D();
		float height = o.getX()+o.getY()+o.getZ();
		float expectedY = o.getY() + height; 
		float expectedZ = o.getZ() + height; 
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.addHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(expectedZ, tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.addHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(expectedZ, tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(expectedY, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(expectedY, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
	}

	/**
	 */
	public void testSetHeightTuple3fFloat() {
		Point3f o = randomPoint3F();
		float height = o.getX()+o.getY()+o.getZ();
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.setHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(height, tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.setHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(height, tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(height, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.setHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(height, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
	}

	/**
	 */
	public void testAddHeightTuple3fFloat() {
		Point3f o = randomPoint3F();
		float height = o.getX()+o.getY()+o.getZ();
		float expectedY = o.getY() + height; 
		float expectedZ = o.getZ() + height; 
		Point3f tuple;
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_LEFT_HAND.addHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(expectedZ, tuple.getZ());
		
		tuple = new Point3f(o);
		CoordinateSystem3D.XYZ_RIGHT_HAND.addHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(o.getY(), tuple.getY());
		assertEpsilonEquals(expectedZ, tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(expectedY, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());

		tuple = new Point3f(o);
		CoordinateSystem3D.XZY_LEFT_HAND.addHeight(tuple, height);
		assertEpsilonEquals(o.getX(), tuple.getX());
		assertEpsilonEquals(expectedY, tuple.getY());
		assertEpsilonEquals(o.getZ(), tuple.getZ());
	}

	/**
	 */
	public void testGetHeightCoordinateSystem() {
		assertEquals(2, CoordinateSystem3D.XYZ_LEFT_HAND.getHeightCoordinateIndex());
		assertEquals(2, CoordinateSystem3D.XYZ_RIGHT_HAND.getHeightCoordinateIndex());
		assertEquals(1, CoordinateSystem3D.XZY_LEFT_HAND.getHeightCoordinateIndex());
		assertEquals(1, CoordinateSystem3D.XZY_RIGHT_HAND.getHeightCoordinateIndex());
	}
	
	/**
	 */
	public void testGetSideCoordinateSystem() {
		assertEquals(1, CoordinateSystem3D.XYZ_LEFT_HAND.getSideCoordinateIndex());
		assertEquals(1, CoordinateSystem3D.XYZ_RIGHT_HAND.getSideCoordinateIndex());
		assertEquals(2, CoordinateSystem3D.XZY_LEFT_HAND.getSideCoordinateIndex());
		assertEquals(2, CoordinateSystem3D.XZY_RIGHT_HAND.getSideCoordinateIndex());
	}

	/**
	 */
	public void testGetViewCoordinateSystem() {
		assertEquals(0, CoordinateSystem3D.XYZ_LEFT_HAND.getViewCoordinateIndex());
		assertEquals(0, CoordinateSystem3D.XYZ_RIGHT_HAND.getViewCoordinateIndex());
		assertEquals(0, CoordinateSystem3D.XZY_LEFT_HAND.getViewCoordinateIndex());
		assertEquals(0, CoordinateSystem3D.XZY_RIGHT_HAND.getViewCoordinateIndex());
	}

	/**
	 */
	public void testHeightFloatFloatFloat() {
		Point3f tuple = randomPoint3D();
		
		assertEquals(tuple.getZ(),CoordinateSystem3D.XYZ_LEFT_HAND.height(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEquals(tuple.getZ(),CoordinateSystem3D.XYZ_RIGHT_HAND.height(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEquals(tuple.getY(),CoordinateSystem3D.XZY_LEFT_HAND.height(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEquals(tuple.getY(),CoordinateSystem3D.XZY_RIGHT_HAND.height(tuple.getX(),tuple.getY(),tuple.getZ()));
	}
	
	/**
	 */
	public void testViewFloatFloatFloat() {
		Point3f tuple = randomPoint3D();
		
		assertEquals(tuple.getX(),CoordinateSystem3D.XYZ_LEFT_HAND.view(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEquals(tuple.getX(),CoordinateSystem3D.XYZ_RIGHT_HAND.view(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEquals(tuple.getX(),CoordinateSystem3D.XZY_LEFT_HAND.view(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEquals(tuple.getX(),CoordinateSystem3D.XZY_RIGHT_HAND.view(tuple.getX(),tuple.getY(),tuple.getZ()));
	}
	
	/**
	 */
	public void testSideFloatFloatFloat() {
		Point3f tuple = randomPoint3D();
		
		assertEquals(tuple.getY(),CoordinateSystem3D.XYZ_LEFT_HAND.side(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEquals(tuple.getY(),CoordinateSystem3D.XYZ_RIGHT_HAND.side(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEquals(tuple.getZ(),CoordinateSystem3D.XZY_LEFT_HAND.side(tuple.getX(),tuple.getY(),tuple.getZ()));
		assertEquals(tuple.getZ(),CoordinateSystem3D.XZY_RIGHT_HAND.side(tuple.getX(),tuple.getY(),tuple.getZ()));
	}
	
	/**
	 */
	public void testToCoordinateSystem2DVoid() {
		assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D());
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D());
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D());
		assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D());
	}

	/**
	 */
	public void testToCoordinateSystem2DPoint2d() {
		Point3f pt;
		Point2f pt2;
		
		pt = randomPoint3D();

		pt2 = new Point2f(pt.getX(),pt.getY());
		assertEquals(pt2, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(pt));

		pt2 = new Point2f(pt.getX(),pt.getY());
		assertEquals(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(pt));

		pt2 = new Point2f(pt.getX(),pt.getZ());
		assertEquals(pt2, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(pt));

		pt2 = new Point2f(pt.getX(),pt.getZ());
		assertEquals(pt2, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(pt));
	}

	/**
	 */
	public void testToCoordinateSystem2DPoint2f() {
		Point3f pt;
		Point2f pt2;
		
		pt = randomPoint3F();

		pt2 = new Point2f(pt.getX(),pt.getY());
		assertEquals(pt2, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(pt));

		pt2 = new Point2f(pt.getX(),pt.getY());
		assertEquals(pt2, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(pt));

		pt2 = new Point2f(pt.getX(),pt.getZ());
		assertEquals(pt2, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(pt));

		pt2 = new Point2f(pt.getX(),pt.getZ());
		assertEquals(pt2, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(pt));
	}

	/**
	 */
	public void testToCoordinateSystem2DVector3d() {
		Vector3f vt;
		Vector2f vt2;
		
		vt = randomVector3D();

		vt2 = new Vector2f(vt.getX(),vt.getY());
		assertEquals(vt2, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(vt));

		vt2 = new Vector2f(vt.getX(),vt.getY());
		assertEquals(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(vt));

		vt2 = new Vector2f(vt.getX(),vt.getZ());
		assertEquals(vt2, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(vt));

		vt2 = new Vector2f(vt.getX(),vt.getZ());
		assertEquals(vt2, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(vt));
	}
	
	/**
	 */
	public void testToCoordinateSystem2DVector3f() {
		Vector3f vt;
		Vector2f vt2;
		
		vt = randomVector3F();

		vt2 = new Vector2f(vt.getX(),vt.getY());
		assertEquals(vt2, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(vt));

		vt2 = new Vector2f(vt.getX(),vt.getY());
		assertEquals(vt2, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(vt));

		vt2 = new Vector2f(vt.getX(),vt.getZ());
		assertEquals(vt2, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(vt));

		vt2 = new Vector2f(vt.getX(),vt.getZ());
		assertEquals(vt2, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(vt));
	}

	private float clamp(float angle) {
		float a = angle;
		while (a<=-MathConstants.PI) a += MathConstants.TWO_PI;
		while (a>MathConstants.PI) a -= MathConstants.TWO_PI;
		return a;
	}

	/**
	 */
	public void testToCoordinateSystem2DAxisAngle4d() {
		AxisAngle4f aa;
		float a;
		
		for(a=-MathConstants.PI; a<=MathConstants.PI; a+=MathConstants.PI/500.f) {
			aa = new AxisAngle4f(1,0,0,a);
			assertEpsilonEquals(0, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(aa)));
			assertEpsilonEquals(0, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(aa)));
			assertEpsilonEquals(0, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(aa)));
			assertEpsilonEquals(0, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(aa)));
		}

		aa = new AxisAngle4f(0,1,0,-MathConstants.PI);
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(aa)));
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(aa)));
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(aa)));
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(aa)));

		for(a=-MathConstants.PI+MathConstants.PI/500.f; a<=MathConstants.PI; a+=MathConstants.PI/500.f) {
			aa = new AxisAngle4f(0,1,0,a);
			assertEpsilonEquals(a, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(aa)));
			assertEpsilonEquals((a>=-MathConstants.PI/2.)&&(a<=MathConstants.PI/2.) ? 0 : MathConstants.PI, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(aa)));
			assertEpsilonEquals((a>=-MathConstants.PI/2.)&&(a<=MathConstants.PI/2.) ? 0 : MathConstants.PI, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(aa)));
			assertEpsilonEquals(a, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(aa)));
		}

		aa = new AxisAngle4f(0,0,1,-MathConstants.PI);
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(aa)));
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(aa)));
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(aa)));
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(aa)));

		for(a=-MathConstants.PI+MathConstants.PI/500.f; a<=MathConstants.PI; a+=MathConstants.PI/500.f) {
			aa = new AxisAngle4f(0,0,1,a);
			assertEpsilonEquals((a>=-MathConstants.PI/2.)&&(a<=MathConstants.PI/2.) ? 0 : MathConstants.PI, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(aa)));
			assertEpsilonEquals(a, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(aa)));
			assertEpsilonEquals(a, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(aa)));
			assertEpsilonEquals((a>=-MathConstants.PI/2.)&&(a<=MathConstants.PI/2.) ? 0 : MathConstants.PI, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(aa)));
		}
	}
	
	private Quaternion newQuat(float x, float y, float z, float angle) {
		AxisAngle4f aa = new AxisAngle4f(x,y,z,angle);
		Quaternion quat = new Quaternion();
		quat.set(aa);
		return quat;
	}
	
	/**
	 */
	public void testToCoordinateSystem2DQuat4d() {
		Quaternion quat;
		float a;
		
		for(a=-MathConstants.PI; a<=MathConstants.PI; a+=MathConstants.PI/500.f) {
			quat = newQuat(1,0,0,a);
			assertEpsilonEquals(0, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals(0, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals(0, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals(0, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(quat)));
		}

		quat = newQuat(0,1,0,-MathConstants.PI);
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(quat)));
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(quat)));
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(quat)));
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(quat)));

		for(a=-MathConstants.PI+MathConstants.PI/500.f; a<=MathConstants.PI; a+=MathConstants.PI/500.f) {
			quat = newQuat(0,1,0,a);
			assertEpsilonEquals(a, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals((a>=-MathConstants.PI/2.)&&(a<=MathConstants.PI/2.) ? 0 : MathConstants.PI, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals((a>=-MathConstants.PI/2.)&&(a<=MathConstants.PI/2.) ? 0 : MathConstants.PI, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals(a, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(quat)));
		}

		quat = newQuat(0,0,1,-MathConstants.PI);
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(quat)));
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(quat)));
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(quat)));
		assertEpsilonEquals(MathConstants.PI, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(quat)));

		for(a=-MathConstants.PI+MathConstants.PI/500.f; a<=MathConstants.PI; a+=MathConstants.PI/500.f) {
			quat = newQuat(0,0,1,a);
			assertEpsilonEquals((a>=-MathConstants.PI/2.)&&(a<=MathConstants.PI/2.) ? 0 : MathConstants.PI, clamp(CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals(a, clamp(CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals(a, clamp(CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(quat)));
			assertEpsilonEquals((a>=-MathConstants.PI/2.)&&(a<=MathConstants.PI/2.) ? 0 : MathConstants.PI, clamp(CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(quat)));
		}
	}

	/**
	 */
	public void testFromCoordinateSystem2DPoint2dFloat() {
		Point3f pt3;
		Point2f pt2;
		float tc;
		
		tc = this.RANDOM.nextFloat();
		pt2 = randomPoint2D();

		pt3 = new Point3f(pt2.getX(),pt2.getY(),tc);
		assertEquals(pt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(pt2, tc));

		pt3 = new Point3f(pt2.getX(),pt2.getY(),tc);
		assertEquals(pt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(pt2, tc));

		pt3 = new Point3f(pt2.getX(),tc,pt2.getY());
		assertEquals(pt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(pt2, tc));

		pt3 = new Point3f(pt2.getX(),tc,pt2.getY());
		assertEquals(pt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(pt2, tc));
	}

	/**
	 */
	public void testFromCoordinateSystem2DPoint2fFloat() {
		Point3f pt3;
		Point2f pt2;
		float tc;
		
		tc = this.RANDOM.nextFloat();
		pt2 = randomPoint2F();

		pt3 = new Point3f(pt2.getX(),pt2.getY(),tc);
		assertEquals(pt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(pt2, tc));

		pt3 = new Point3f(pt2.getX(),pt2.getY(),tc);
		assertEquals(pt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(pt2, tc));

		pt3 = new Point3f(pt2.getX(),tc,pt2.getY());
		assertEquals(pt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(pt2, tc));

		pt3 = new Point3f(pt2.getX(),tc,pt2.getY());
		assertEquals(pt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(pt2, tc));
	}

	/**
	 */
	public void testFromCoordinateSystem2DEuclidianPoint2DFloat() {
		EuclidianPoint3D pt3;
		EuclidianPoint2D pt2;
		float tc;
		
		tc = this.RANDOM.nextFloat();
		pt2 = new EuclidianPoint2D(randomPoint2D());

		pt3 = new EuclidianPoint3D(pt2.getX(),pt2.getY(),tc);
		assertEquals(pt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(pt2, tc));

		pt3 = new EuclidianPoint3D(pt2.getX(),pt2.getY(),tc);
		assertEquals(pt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(pt2, tc));

		pt3 = new EuclidianPoint3D(pt2.getX(),tc,pt2.getY());
		assertEquals(pt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(pt2, tc));

		pt3 = new EuclidianPoint3D(pt2.getX(),tc,pt2.getY());
		assertEquals(pt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(pt2, tc));
	}

	/**
	 */
	public void testFromCoordinateSystem2DPoint2d() {
		Point3f pt3;
		Point2f pt2;
		
		pt2 = randomPoint2D();

		pt3 = new Point3f(pt2.getX(),pt2.getY(),0);
		assertEquals(pt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(pt2));

		pt3 = new Point3f(pt2.getX(),pt2.getY(),0);
		assertEquals(pt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(pt2));

		pt3 = new Point3f(pt2.getX(),0,pt2.getY());
		assertEquals(pt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(pt2));

		pt3 = new Point3f(pt2.getX(),0,pt2.getY());
		assertEquals(pt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(pt2));
	}

	/**
	 */
	public void testFromCoordinateSystem2DPoint2f() {
		Point3f pt3;
		Point2f pt2;
		
		pt2 = randomPoint2F();

		pt3 = new Point3f(pt2.getX(),pt2.getY(),0);
		assertEquals(pt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(pt2));

		pt3 = new Point3f(pt2.getX(),pt2.getY(),0);
		assertEquals(pt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(pt2));

		pt3 = new Point3f(pt2.getX(),0,pt2.getY());
		assertEquals(pt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(pt2));

		pt3 = new Point3f(pt2.getX(),0,pt2.getY());
		assertEquals(pt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(pt2));
	}

	/**
	 */
	public void testFromCoordinateSystem2DEuclidianPoint2D() {
		EuclidianPoint3D pt3;
		EuclidianPoint2D pt2;
		
		pt2 = new EuclidianPoint2D(randomPoint2D());

		pt3 = new EuclidianPoint3D(pt2.getX(),pt2.getY(),0);
		assertEquals(pt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(pt2));

		pt3 = new EuclidianPoint3D(pt2.getX(),pt2.getY(),0);
		assertEquals(pt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(pt2));

		pt3 = new EuclidianPoint3D(pt2.getX(),0,pt2.getY());
		assertEquals(pt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(pt2));

		pt3 = new EuclidianPoint3D(pt2.getX(),0,pt2.getY());
		assertEquals(pt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(pt2));
	}

	/**
	 */
	public void testFromCoordinateSystem2DVector2dFloat() {
		Vector3f vt3;
		Vector2f vt2;
		float tc;
		
		tc = this.RANDOM.nextFloat();
		vt2 = randomVector2D();

		vt3 = new Vector3f(vt2.getX(),vt2.getY(),tc);
		assertEquals(vt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(vt2, tc));

		vt3 = new Vector3f(vt2.getX(),vt2.getY(),tc);
		assertEquals(vt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(vt2, tc));

		vt3 = new Vector3f(vt2.getX(),tc,vt2.getY());
		assertEquals(vt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(vt2, tc));

		vt3 = new Vector3f(vt2.getX(),tc,vt2.getY());
		assertEquals(vt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(vt2, tc));
	}

	/**
	 */
	public void testFromCoordinateSystem2DVector2fFloat() {
		Vector3f vt3;
		Vector2f vt2;
		float tc;
		
		tc = this.RANDOM.nextFloat();
		vt2 = randomVector2F();

		vt3 = new Vector3f(vt2.getX(),vt2.getY(),tc);
		assertEquals(vt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(vt2, tc));

		vt3 = new Vector3f(vt2.getX(),vt2.getY(),tc);
		assertEquals(vt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(vt2, tc));

		vt3 = new Vector3f(vt2.getX(),tc,vt2.getY());
		assertEquals(vt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(vt2, tc));

		vt3 = new Vector3f(vt2.getX(),tc,vt2.getY());
		assertEquals(vt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(vt2, tc));
	}

	/**
	 */
	public void testFromCoordinateSystem2DDirection2DFloat() {
		Direction3D vt3;
		Direction2D vt2;
		float tc;
		
		tc = this.RANDOM.nextFloat();
		vt2 = new Direction2D(randomVector2D());

		vt3 = new Direction3D(vt2.getX(),vt2.getY(),tc, 0.f);
		assertEquals(vt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(vt2, tc));

		vt3 = new Direction3D(vt2.getX(),vt2.getY(),tc, 0.f);
		assertEquals(vt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(vt2, tc));

		vt3 = new Direction3D(vt2.getX(),tc,vt2.getY(), 0.f);
		assertEquals(vt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(vt2, tc));

		vt3 = new Direction3D(vt2.getX(),tc,vt2.getY(), 0.f);
		assertEquals(vt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(vt2, tc));
	}

	/**
	 */
	public void testFromCoordinateSystem2DVector2d() {
		Vector3f vt3;
		Vector2f vt2;

		vt2 = randomVector2D();

		vt3 = new Vector3f(vt2.getX(),vt2.getY(),0);
		assertEquals(vt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(vt2));

		vt3 = new Vector3f(vt2.getX(),vt2.getY(),0);
		assertEquals(vt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(vt2));

		vt3 = new Vector3f(vt2.getX(),0,vt2.getY());
		assertEquals(vt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(vt2));

		vt3 = new Vector3f(vt2.getX(),0,vt2.getY());
		assertEquals(vt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(vt2));
	}

	/**
	 */
	public void testFromCoordinateSystem2DVector2f() {
		Vector3f vt3;
		Vector2f vt2;

		vt2 = randomVector2F();

		vt3 = new Vector3f(vt2.getX(),vt2.getY(),0);
		assertEquals(vt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(vt2));

		vt3 = new Vector3f(vt2.getX(),vt2.getY(),0);
		assertEquals(vt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(vt2));

		vt3 = new Vector3f(vt2.getX(),0,vt2.getY());
		assertEquals(vt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(vt2));

		vt3 = new Vector3f(vt2.getX(),0,vt2.getY());
		assertEquals(vt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(vt2));
	}

	/**
	 */
	public void testFromCoordinateSystem2DDirection2D() {
		Direction3D vt3;
		Direction2D vt2;

		vt2 = new Direction2D(randomVector2D());

		vt3 = new Direction3D(vt2.getX(),vt2.getY(),0, 0.f);
		assertEquals(vt3, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(vt2));

		vt3 = new Direction3D(vt2.getX(),vt2.getY(),0, 0.f);
		assertEquals(vt3, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(vt2));

		vt3 = new Direction3D(vt2.getX(),0,vt2.getY(), 0.f);
		assertEquals(vt3, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(vt2));

		vt3 = new Direction3D(vt2.getX(),0,vt2.getY(), 0.f);
		assertEquals(vt3, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(vt2));
	}

	/**
	 */
	public void testFromCoordinateSystem2DFloat() {
		AxisAngle4f aa;
		float rotation2d;

		rotation2d = (this.RANDOM.nextFloat() * MathConstants.PI * 2.f);

		aa = new AxisAngle4f(0f,0f,1f,rotation2d);
		assertEquals(aa, CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(rotation2d));

		aa = new AxisAngle4f(0f,0f,1f,rotation2d);
		assertEquals(aa, CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(rotation2d));

		aa = new AxisAngle4f(0f,1f,0f,rotation2d);
		assertEquals(aa, CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(rotation2d));

		aa = new AxisAngle4f(0f,1f,0f,rotation2d);
		assertEquals(aa, CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(rotation2d));
	}

	/**
	 */
	public void testPointRotation_AxisAngle4d_AroundZ() {
		// test if points are correctly rotated when the rotation is
		// expressed with an axis-angle in the different coordinate systems
		// See the description of CoordinateSystem3D for theoritical details.
		Point3f pOrig, pExpected;
		AxisAngle4f aa, aa2;

		pOrig = new Point3f(1,0,0);

		// Rotation around vectical vector about PI/2 radian
		aa = new AxisAngle4f(0,0,1,MathConstants.DEMI_PI);
		
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,-1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,-1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		// Rotation around vectical vector about -PI/2 radian
		aa = new AxisAngle4f(0,0,1,-MathConstants.DEMI_PI);

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,-1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,-1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));
	}

	/**
	 */
	public void testPointRotation_Quat4d_AroundZ() {
		// test if points are correctly rotated when the rotation is
		// expressed with an axis-angle in the different coordinate systems
		// See the description of CoordinateSystem3D for theoritical details.
		Point3f pOrig, pExpected;
		AxisAngle4f aa;
		Quaternion q1, q2;

		pOrig = new Point3f(1,0,0);

		// Rotation around vectical vector about PI/2 radian
		aa = new AxisAngle4f(0,0,1,MathConstants.DEMI_PI);
		q1 = new Quaternion();
		q1.set(aa);
		
		q2 = new Quaternion(q1);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,0,-1);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,-1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,0,1);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		// Rotation around vectical vector about -PI/2 radian
		aa = new AxisAngle4f(0,0,1,-MathConstants.DEMI_PI);
		q1.set(aa);

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,-1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,0,1);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));

		q2 = new Quaternion(q1);
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(q2);
		pExpected = new Point3f(0,0,-1);
		assertEpsilonEquals(pExpected, rotate(pOrig, q2));
	}

	/**
	 */
	public void testPointRotation_AxisAngle4d_AroundY() {
		// test if points are correctly rotated when the rotation is
		// expressed with an axis-angle in the different coordinate systems
		// See the description of CoordinateSystem3D for theoritical details.
		Point3f pOrig, pExpected;
		AxisAngle4f aa, aa2;

		pOrig = new Point3f(1,0,0);

		// Rotation around left vector about PI/2 radian
		aa = new AxisAngle4f(0,1,0,MathConstants.DEMI_PI);
		
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,-1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,-1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,-1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,-1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		// Rotation around left vector about -PI/2 radian
		aa = new AxisAngle4f(0,1,0,-MathConstants.DEMI_PI);
		
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,1,0);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));
	}

	/**
	 */
	public void testPointRotation_AxisAngle4d_AroundX() {
		// test if points are correctly rotated when the rotation is
		// expressed with an axis-angle in the different coordinate systems.
		// See the description of CoordinateSystem3D for theoritical details.
		Point3f pOrig, pExpected;
		AxisAngle4f aa, aa2;

		// Rotation around left vector about PI/2 radian
		pOrig = new Point3f(0,1,0);
		aa = new AxisAngle4f(1,0,0,MathConstants.DEMI_PI);
		
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,-1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,-1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		// Rotation around left vector about -PI/2 radian
		aa = new AxisAngle4f(1,0,0,-MathConstants.DEMI_PI);
		
		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,-1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,-1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XYZ_LEFT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));

		aa2 = new AxisAngle4f(aa);
		CoordinateSystem3D.XZY_LEFT_HAND.fromDefault(aa2);
		pExpected = new Point3f(0,0,1);
		assertEpsilonEquals(pExpected, rotate(pOrig, aa2));
	}
	
	/**
	 */
	public void testGetBackVectorVector3d() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getBackVector(v);
		assertEpsilonEquals(new Vector3f(-1,0,0), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getBackVector(v);
		assertEpsilonEquals(new Vector3f(-1,0,0), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getBackVector(v);
		assertEpsilonEquals(new Vector3f(-1,0,0), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getBackVector(v);
		assertEpsilonEquals(new Vector3f(-1,0,0), v);
	}

	/**
	 */
	public void testGetBackVectorVector3f() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getBackVector(v);
		assertEpsilonEquals(new Vector3f(-1,0,0), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getBackVector(v);
		assertEpsilonEquals(new Vector3f(-1,0,0), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getBackVector(v);
		assertEpsilonEquals(new Vector3f(-1,0,0), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getBackVector(v);
		assertEpsilonEquals(new Vector3f(-1,0,0), v);
	}

	/**
	 */
	public void testGetDownVectorVector3d() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getDownVector(v);
		assertEpsilonEquals(new Vector3f(0,0,-1), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getDownVector(v);
		assertEpsilonEquals(new Vector3f(0,0,-1), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getDownVector(v);
		assertEpsilonEquals(new Vector3f(0,-1,0), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getDownVector(v);
		assertEpsilonEquals(new Vector3f(0,-1,0), v);
	}

	/**
	 */
	public void testGetDownVectorVector3f() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getDownVector(v);
		assertEpsilonEquals(new Vector3f(0,0,-1), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getDownVector(v);
		assertEpsilonEquals(new Vector3f(0,0,-1), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getDownVector(v);
		assertEpsilonEquals(new Vector3f(0,-1,0), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getDownVector(v);
		assertEpsilonEquals(new Vector3f(0,-1,0), v);
	}

	/**
	 */
	public void testGetLeftVectorVector3d() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getLeftVector(v);
		assertEpsilonEquals(new Vector3f(0,-1,0), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getLeftVector(v);
		assertEpsilonEquals(new Vector3f(0,1,0), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getLeftVector(v);
		assertEpsilonEquals(new Vector3f(0,0,1), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getLeftVector(v);
		assertEpsilonEquals(new Vector3f(0,0,-1), v);
	}

	/**
	 */
	public void testGetLeftVectorVector3f() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getLeftVector(v);
		assertEpsilonEquals(new Vector3f(0,-1,0), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getLeftVector(v);
		assertEpsilonEquals(new Vector3f(0,1,0), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getLeftVector(v);
		assertEpsilonEquals(new Vector3f(0,0,1), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getLeftVector(v);
		assertEpsilonEquals(new Vector3f(0,0,-1), v);
	}

	/**
	 */
	public void testGetRightVectorVector3d() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getRightVector(v);
		assertEpsilonEquals(new Vector3f(0,1,0), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getRightVector(v);
		assertEpsilonEquals(new Vector3f(0,-1,0), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getRightVector(v);
		assertEpsilonEquals(new Vector3f(0,0,-1), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getRightVector(v);
		assertEpsilonEquals(new Vector3f(0,0,1), v);
	}

	/**
	 */
	public void testGetRightVectorVector3f() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getRightVector(v);
		assertEpsilonEquals(new Vector3f(0,1,0), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getRightVector(v);
		assertEpsilonEquals(new Vector3f(0,-1,0), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getRightVector(v);
		assertEpsilonEquals(new Vector3f(0,0,-1), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getRightVector(v);
		assertEpsilonEquals(new Vector3f(0,0,1), v);
	}

	/**
	 */
	public void testGetUpVectorVector3d() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getUpVector(v);
		assertEpsilonEquals(new Vector3f(0,0,1), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getUpVector(v);
		assertEpsilonEquals(new Vector3f(0,0,1), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getUpVector(v);
		assertEpsilonEquals(new Vector3f(0,1,0), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getUpVector(v);
		assertEpsilonEquals(new Vector3f(0,1,0), v);
	}

	/**
	 */
	public void testGetUpVectorVector3f() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getUpVector(v);
		assertEpsilonEquals(new Vector3f(0,0,1), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getUpVector(v);
		assertEpsilonEquals(new Vector3f(0,0,1), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getUpVector(v);
		assertEpsilonEquals(new Vector3f(0,1,0), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getUpVector(v);
		assertEpsilonEquals(new Vector3f(0,1,0), v);
	}

	/**
	 */
	public void testGetViewVectorVector3d() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getViewVector(v);
		assertEpsilonEquals(new Vector3f(1,0,0), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getViewVector(v);
		assertEpsilonEquals(new Vector3f(1,0,0), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getViewVector(v);
		assertEpsilonEquals(new Vector3f(1,0,0), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getViewVector(v);
		assertEpsilonEquals(new Vector3f(1,0,0), v);
	}

	/**
	 */
	public void testGetViewVectorVector3f() {
		Vector3f v = new Vector3f();
		
		CoordinateSystem3D.XYZ_LEFT_HAND.getViewVector(v);
		assertEpsilonEquals(new Vector3f(1,0,0), v);

		CoordinateSystem3D.XYZ_RIGHT_HAND.getViewVector(v);
		assertEpsilonEquals(new Vector3f(1,0,0), v);

		CoordinateSystem3D.XZY_LEFT_HAND.getViewVector(v);
		assertEpsilonEquals(new Vector3f(1,0,0), v);

		CoordinateSystem3D.XZY_RIGHT_HAND.getViewVector(v);
		assertEpsilonEquals(new Vector3f(1,0,0), v);
	}

}
