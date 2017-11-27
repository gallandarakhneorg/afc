/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.frustum;

import java.util.UUID;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AbstractBoxedFrustum3DTest extends AbstractTestCase {

	private UUID id;
	private AbstractBoxedFrustum3DStub tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.id = UUID.randomUUID();
		this.tested = new AbstractBoxedFrustum3DStub(this.id);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.id = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#getIdentifier()}.
	 */
	public void testGetIdentifier() {
		assertEquals(this.id, this.tested.getIdentifier());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#clone()}.
	 */
	public void testClone() {
		AbstractBoxedFrustum3D clone = this.tested.clone();
		assertNotSame(this.tested, clone);
		assertEquals(this.id, clone.getIdentifier());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#getEye()}.
	 */
	public void testGetEye() {
		this.tested.getPosition();
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#getNearDistance()}.
	 */
	public void testGetNearDistance() {
		assertEpsilonEquals(0., this.tested.getNearDistance());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#getPoints()}.
	 */
	public void testGetPoints() {
		Point3d[] points = new Point3d[] {
				new Point3d(1., -100., 0.),
				new Point3d(1., -100., 10.),
				new Point3d(1., 100., 0.),
				new Point3d(1., 100., 10.),
				new Point3d(100., -100., 0.),
				new Point3d(100., -100., 10.),
				new Point3d(100., 100., 0.),
				new Point3d(100., 100., 10.),
		};
		assertEpsilonEquals(points, this.tested.getPoints());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#getSize()}.
	 */
	public void testGetSize() {
		assertEpsilonEquals(
				new Vector3d(99., 200., 10.),
				this.tested.getSize());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#getSizeX()}.
	 */
	public void testGetSizeX() {
		assertEpsilonEquals(99., this.tested.getSizeX());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#getSizeY()}.
	 */
	public void testGetSizeY() {
		assertEpsilonEquals(200., this.tested.getSizeY());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#getSizeZ()}.
	 */
	public void testGetSizeZ() {
		assertEpsilonEquals(10., this.tested.getSizeZ());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#isEmpty()}.
	 */
	public void testIsEmpty() {
		assertFalse(this.tested.isEmpty());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#getCenter()}.
	 */
	public void testGetCenter() {
		assertEpsilonEquals(
				new Point3d(50.5, 0., 5.),
				this.tested.getCenter());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#getUpper()}.
	 */
	public void testGetUpper() {
		assertEpsilonEquals(
				new Point3d(100., 100., 10.),
				this.tested.getUpper());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#getLower()}.
	 */
	public void testGetLower() {
		assertEpsilonEquals(
				new Point3d(1., -100., 0.),
				this.tested.getLower());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#toBoundingBox()}.
	 */
	public void testToBoundingBox() {
		AlignedBoundingBox expected = new AlignedBoundingBox(
				1., -100., 0., 100., 100., 10.);
		AlignedBoundingBox actual = this.tested.toBoundingBox();
		assertEpsilonEquals(expected.getLower(), actual.getLower());
		assertEpsilonEquals(expected.getUpper(), actual.getUpper());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#toBounds2D()}.
	 */
	public void testToBounds2D() {
		try {
			this.tested.toBounds2D();
			fail("the stub is supported to not create a 2D equivalent box"); //$NON-NLS-1$
		}
		catch(UnsupportedOperationException _) {
			// expected case
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#toBounds2D()}.
	 */
	public void testToBounds2DCoordinateSystem3D() {
		try {
			this.tested.toBounds2D(CoordinateSystem3D.getDefaultCoordinateSystem());
			fail("the stub is supported to not create a 2D equivalent box"); //$NON-NLS-1$
		}
		catch(UnsupportedOperationException _) {
			// expected case
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.AbstractBoxedFrustum3D#getViewDirection()}.
	 */
	public void testGetViewDirection() {
		try {
			this.tested.getViewDirection();
			fail("The planes are supposed to not converge. Intersection computation could not produce eye location."); //$NON-NLS-1$
		}
		catch(Throwable _) {
			// planes do not converge
		}
	}

}
