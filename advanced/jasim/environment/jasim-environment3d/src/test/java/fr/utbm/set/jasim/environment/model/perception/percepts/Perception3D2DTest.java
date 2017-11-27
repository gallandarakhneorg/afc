/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.percepts;

import java.util.UUID;

import javax.vecmath.Point3d;

import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Perception3D2DTest extends AbstractTestCase {

	private UUID frustum;
	private Perception3D2D tested;
	private IntersectionType type;
	private Entity3D<?> entity;
	private Point3d lower, upper;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.frustum = UUID.randomUUID();
		this.type = IntersectionType.values()[this.RANDOM.nextInt(IntersectionType.values().length)];
		this.lower = randomPoint3D();
		this.upper = new Point3d(this.lower);
		this.upper.add(randomVector3D());
		this.entity = new Entity3DStub(this.lower, this.upper);
		this.tested = new Perception3D2D(
				this.frustum,
				this.type,
				this.entity);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.lower = this.upper = null;
		this.entity = null;
		this.type = null;
		this.frustum = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 */
	public void testGetPerceivedObjectPosition() {
		assertEpsilonEquals(this.entity.getPosition2D(), this.tested.getPerceivedObjectPosition());
	}

}
