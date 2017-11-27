/**
 * 
 */
package fr.utbm.set.jasim.environment.model.world;

import java.util.UUID;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.unittest.AbstractRepeatedTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AbstractMobileEntityTest extends AbstractRepeatedTestCase {

	private UUID id;
	private AbstractMobileEntity<Bounds<?,?,?>> tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.id = UUID.randomUUID();
		this.tested = new AbstractMobileEntityStub(this.id);
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
	 */
	public void testGetIdentifier() {
		assertEquals(this.id, this.tested.getIdentifier());
	}

}
