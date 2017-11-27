/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.percepts;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import fr.utbm.set.jasim.environment.model.ground.PerceivableGround;
import fr.utbm.set.jasim.environment.semantics.NotTraversableGroundType;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultGroundPerceptionTest extends AbstractTestCase {

	private Point2d position;
	private DefaultGroundPerception tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.position = randomPoint2D();
		PerceivableGround ground = new PerceivableGroundStub();
		this.tested = new DefaultGroundPerception(ground, this.position.x, this.position.y);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.position = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.percepts.DefaultGroundPerception#getAttraction()}.
	 */
	public void testGetAttraction() {
		assertEquals(new Vector2d(1.,0.), this.tested.getAttraction());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.percepts.DefaultGroundPerception#getRepulsion()}.
	 */
	public void testGetRepulsion() {
		assertEquals(new Vector2d(0.,1.), this.tested.getRepulsion());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.percepts.DefaultGroundPerception#getSemantic()}.
	 */
	public void testGetSemantic() {
		assertSame(NotTraversableGroundType.NOTTRAVERSABLEGROUNDTYPE_SINGLETON, this.tested.getSemantic());
	}

}
