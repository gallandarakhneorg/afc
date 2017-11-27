/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.percepts;

import java.util.UUID;

import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AbstractPerception3DTest extends AbstractTestCase {

	private UUID frustum;
	private AbstractPerception3D tested;
	private IntersectionType type;
	private Entity3D<?> entity;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.frustum = UUID.randomUUID();
		this.type = IntersectionType.values()[this.RANDOM.nextInt(IntersectionType.values().length)];
		this.entity = new Entity3DStub();
		this.tested = new AbstractPerception3DStub(
				this.frustum,
				this.type,
				this.entity);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.entity = null;
		this.type = null;
		this.frustum = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 */
	public void testGetClassification() {
		assertEquals(this.type, this.tested.getClassification());
	}

	/**
	 */
	public void testGetFrustum() {
		assertEquals(this.frustum, this.tested.getFrustum());
	}

	/**
	 */
	public void testGetPerceivedObject() {
		assertSame(this.entity, this.tested.getPerceivedObject());
	}

}
