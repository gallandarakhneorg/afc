/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.percepts;

import junit.framework.TestCase;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception1D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception1D5;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception2D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception3D;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PerceptionList3D1DTest extends TestCase {

	private PerceptionList3D1D<AlignedBoundingBox,AlignedBoundingBox> tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.tested = new PerceptionList3D1D<AlignedBoundingBox,AlignedBoundingBox>();
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D3D#isSupportedPerceptionType(java.lang.Class)}.
	 */
	public void testIsSupportedPerceptionType() {
		assertTrue(this.tested.isSupportedPerceptionType(Perception.class));
		
		assertTrue(this.tested.isSupportedPerceptionType(Perception1D.class));
		assertFalse(this.tested.isSupportedPerceptionType(Perception1D5.class));
		assertFalse(this.tested.isSupportedPerceptionType(Perception2D.class));
		assertFalse(this.tested.isSupportedPerceptionType(Perception3D.class));
		
		assertFalse(this.tested.isSupportedPerceptionType(Perception3D2D.class));
		assertFalse(this.tested.isSupportedPerceptionType(Perception3D3D.class));
	}

}
