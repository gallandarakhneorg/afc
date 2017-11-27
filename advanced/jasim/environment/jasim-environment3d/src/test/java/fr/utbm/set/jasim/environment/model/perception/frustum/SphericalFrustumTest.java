/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.frustum;

import java.util.UUID;

import javax.vecmath.Point3d;

import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SphericalFrustumTest extends AbstractTestCase {

	private UUID id;
	private Point3d position;
	private double radius;
	private SphericalFrustum tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.id = UUID.randomUUID();
		this.position = randomPoint3D();
		this.radius = this.RANDOM.nextDouble() * 99. + 1.;
		this.tested = new SphericalFrustum(
				this.id, this.position, this.radius);
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
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.SphericalFrustum#clone()}.
	 */
	public void testClone() {
		SphericalFrustum clone = this.tested.clone();
		assertNotSame(this.tested, clone);
		assertEpsilonEquals(this.position, clone.getCenter());
		assertEpsilonEquals(this.radius, clone.getRadius());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.SphericalFrustum#getFarDistance()}.
	 */
	public void testGetFarDistance() {
		assertEpsilonEquals(this.radius, this.tested.getFarDistance());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.SphericalFrustum#getNearDistance()}.
	 */
	public void testGetNearDistance() {
		assertEpsilonEquals(0., this.tested.getNearDistance());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.SphericalFrustum#getEye()}.
	 */
	public void testGetEye() {
		assertEpsilonEquals(this.position, this.tested.getEye());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.SphericalFrustum#getIdentifier()}.
	 */
	public void testGetIdentifier() {
		assertEquals(this.id, this.tested.getIdentifier());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.frustum.SphericalFrustum#transform(fr.utbm.set.geom.transform.Transform3D)}.
	 */
	public void testTransform() {
		Transform3D tr = new Transform3D();
		tr.setTranslation(randomVector3D());
		this.tested.transform(tr);
		
		Point3d expected = new Point3d(this.position);
		expected.add(tr.getTranslation());
		assertEpsilonEquals(expected, this.tested.getCenter());
		assertEpsilonEquals(this.radius, this.tested.getRadius());
	}
	
}
