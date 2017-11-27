/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.frustum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.vecmath.Point3d;

import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.plane.Plane4d;
import fr.utbm.set.unittest.AbstractExtendedTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PyramidalFrustumTest extends AbstractExtendedTestCase {

	private UUID id;
	private Point3d position;
	//private AlignedBoundingBox reference;
	private PyramidalFrustum tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.id = UUID.randomUUID();
		this.position = randomPoint3D();
		this.tested = new PyramidalFrustum(
				this.id, this.position, 
				new Direction3D(1., 0., 0, 0.),
				5., 100., Math.PI/4., Math.PI/4.);
		//this.reference = this.tested.toBoundingBox();
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.id = null;
		this.position = null;
		//this.reference = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.PyramidalFrustum#clone()}.
	 */
	public void testClone() {
		PyramidalFrustum clone = this.tested.clone();
		List<Plane4d> planes = new ArrayList<Plane4d>(Arrays.asList(clone.getPlanes()));
		for(Plane4d p : this.tested.getPlanes()) {
			Iterator<Plane4d> iterator = planes.iterator();
			while (iterator.hasNext()) {
				Plane4d pl = iterator.next();
				if ((isEpsilonEquals(p.getEquationComponentA(), pl.getEquationComponentA()))
					&&
					(isEpsilonEquals(p.getEquationComponentB(), pl.getEquationComponentB()))
					&&
					(isEpsilonEquals(p.getEquationComponentC(), pl.getEquationComponentC()))
					&&
					(isEpsilonEquals(p.getEquationComponentD(), pl.getEquationComponentD()))) {
					iterator.remove();
					break;
				}
			}
		}
		assertTrue(planes.isEmpty());
	}


	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.PyramidalFrustum#getNearDistance()}.
	 */
	public void testGetNearDistance() {
		assertEpsilonEquals(5., this.tested.getNearDistance());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.PyramidalFrustum#getFarDistance()}.
	 */
	public void testGetFarDistance() {
		assertEpsilonEquals(100., this.tested.getFarDistance());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#distance(javax.vecmath.Point3d)}.
	 */
	public void testDistance() {
		Point3d p = new Point3d();
		
		p = new Point3d(this.position);
		p.x += 500.;
		assertEpsilonEquals(400., this.tested.distance(p));

		p = new Point3d(this.position);
		assertEpsilonEquals(5., this.tested.distance(p));

		p = new Point3d(this.position);
		p.y += 80.;
		double sideDist = Math.abs(this.tested.getPlane(AbstractBoxedFrustum3D.LEFT).distanceTo(p));
		assertEpsilonEquals(sideDist, this.tested.distance(p));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#classifies(javax.vecmath.Point3d)}.
	 */
	public void testClassifiesPoint3d() {
		Point3d p = new Point3d();
		
		p.set(this.position);
		p.x += 50.;
		p.y += 0.;
		p.z += 5.;
		assertEquals(IntersectionType.INSIDE, this.tested.classifies(p));

		p.set(this.position);
		p.x += 1.;
		p.y += -50.;
		p.z += 0.;
		assertEquals(IntersectionType.OUTSIDE, this.tested.classifies(p));
		
		p.set(this.position);
		p.x += 0.;
		p.y += -100.;
		p.z += 0.;
		assertEquals(IntersectionType.OUTSIDE, this.tested.classifies(p));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#intersects(javax.vecmath.Point3d)}.
	 */
	public void testIntersectsPoint3d() {
		Point3d p = new Point3d();
		
		p.set(this.position);
		p.x += 50.;
		p.y += 0.;
		p.z += 5.;
		assertTrue(this.tested.intersects(p));

		p.set(this.position);
		p.x += 1.;
		p.y += -50.;
		p.z += 0.;
		assertFalse(this.tested.intersects(p));
		
		p.set(this.position);
		p.x += 0.;
		p.y += -100.;
		p.z += 0.;
		assertFalse(this.tested.intersects(p));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#classifies(javax.vecmath.Point3d, javax.vecmath.Point3d)}.
	 */
	public void testClassifiesPoint3dPoint3d() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#intersects(javax.vecmath.Point3d, javax.vecmath.Point3d)}.
	 */
	public void testIntersectsPoint3dPoint3d() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#classifies(javax.vecmath.Point3d, double)}.
	 */
	public void testClassifiesPoint3dDouble() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#intersects(javax.vecmath.Point3d, double)}.
	 */
	public void testIntersectsPoint3dDouble() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#classifies(javax.vecmath.Point3d, javax.vecmath.Vector3d[], double[])}.
	 */
	public void testClassifiesPoint3dVector3dArrayDoubleArray() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#intersects(javax.vecmath.Point3d, javax.vecmath.Vector3d[], double[])}.
	 */
	public void testIntersectsPoint3dVector3dArrayDoubleArray() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#classifies(fr.utbm.set.geom.plane.Plane)}.
	 */
	public void testClassifiesPlane() {
		try {
			this.tested.classifies(new Plane4d());
			fail("expecting UnsupportedOperationException exception"); //$NON-NLS-1$
		}
		catch(Throwable _) {
			// expected case
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#intersects(fr.utbm.set.geom.plane.Plane)}.
	 */
	public void testIntersectsPlane() {
		try {
			this.tested.classifies(new Plane4d());
			fail("expecting UnsupportedOperationException exception"); //$NON-NLS-1$
		}
		catch(Throwable _) {
			// expected case
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#classifiesAgainst(fr.utbm.set.geom.plane.Plane)}.
	 */
	public void testClassifiesAgainst() {
		try {
			this.tested.classifiesAgainst(new Plane4d());
			fail("expecting UnsupportedOperationException exception"); //$NON-NLS-1$
		}
		catch(Throwable _) {
			// expected case
		}
	}

}
