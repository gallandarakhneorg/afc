/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.frustum;

import java.util.UUID;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.unittest.AbstractExtendedTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RectangularFrustum3DTest extends AbstractExtendedTestCase {

	private UUID id;
	private Point3d position;
	private RectangularFrustum3D tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.id = UUID.randomUUID();
		this.position = new Point3d(100, 100, 100);
		this.tested = new RectangularFrustum3D(
				this.id, this.position, 
				new Direction3D(0., 1., 0, 0.),
				100., 50., 100.);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.id = null;
		this.position = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 */
	public void testGetNearDistance() {
		assertEpsilonEquals(0., this.tested.getNearDistance());
	}

	/**
	 */
	public void testGetLeftRightDistance() {
		assertEpsilonEquals(100., this.tested.getLeftRightDistance());
	}

	/**
	 */
	public void testGetBottomUpDistance() {
		assertEpsilonEquals(50., this.tested.getBottomUpDistance());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.RectangularFrustum3D#getFarDistance()}.
	 */
	public void testGetFarDistance() {
		assertEpsilonEquals(100., this.tested.getFarDistance());
	}

	/**
	 */
	public void testGetEye() {
		assertEpsilonEquals(this.position, this.tested.getEye());
	}
	
	/**
	 */
	public void testGetIdentifier() {
		assertEquals(this.id, this.tested.getIdentifier());
	}

	/**
	 */
	public void testGetFrontVector() {
		assertEquals(new Vector3d(0., 1., 0.), this.tested.getFrontVector());
	}

	/**
	 */
	public void testGetSideVector() {
		assertEquals(new Vector3d(-1., 0., 0.), this.tested.getSideVector());
	}

	/**
	 */
	public void testGetUpVector() {
		assertEquals(new Vector3d(0., 0., 1.), this.tested.getUpVector());
	}

	/**
	 */
	public void testRotate() {
		Vector3d expected = new Vector3d(-1,1,0);
		expected.normalize();
		
		AxisAngle4d aa = new AxisAngle4d(0., 0., 1., Math.PI/4.);
		Quat4d q = new Quat4d();
		q.set(aa);
		this.tested.rotate(q);
		
		assertEpsilonEquals(this.position, this.tested.getEye());
		assertEpsilonEquals(expected, this.tested.getFrontVector());
	}

	/**
	 */
	public void testSetRotation() {
		Vector3d expected = new Vector3d(1,1,0);
		expected.normalize();
		
		AxisAngle4d aa = new AxisAngle4d(0., 0., 1., Math.PI/4.);
		Quat4d q = new Quat4d();
		q.set(aa);
		this.tested.setRotation(q);
		
		assertEpsilonEquals(this.position, this.tested.getEye());
		assertEpsilonEquals(expected, this.tested.getFrontVector());
	}

	/**
	 */
	public void testTransform() {
		Transform3D mat = new Transform3D();
		Vector3d newPosition = randomVector3D();
		AxisAngle4d aa = new AxisAngle4d(0., 0., 1., Math.PI/4.);
		Quat4d q = new Quat4d();
		q.set(aa);
		mat.setRotation(q);
		mat.setTranslation(newPosition);
		
		this.tested.transform(mat);

		Point3d expectedPosition = new Point3d();
		expectedPosition.add(this.position, newPosition);
		assertEpsilonEquals(expectedPosition, this.tested.getEye());
		Vector3d expectedDirection = new Vector3d(-1,1,0);
		expectedDirection.normalize();
		assertEpsilonEquals(expectedDirection, this.tested.getFrontVector());
	}

	/**
	 */
	public void testTranslate() {
		Vector3d tr = randomVector3D();
		
		this.tested.translate(tr);
		
		Point3d expected = new Point3d();
		expected.add(this.position, tr);
		
		assertEpsilonEquals(expected, this.tested.getEye());
	}

}
