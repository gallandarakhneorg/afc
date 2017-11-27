package fr.utbm.set.jasim.environment.model.influencereaction;

import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AbstractEnvironmentalActionTest extends AbstractTestCase {

	private AbstractEnvironmentalAction tested;
	private Clock clock;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.clock = new ClockStub(1., 1.);
		this.tested = new AbstractEnvironmentalActionStub(this, this.clock);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.clock = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 */
	public void testGetClock() {
		assertSame(this.clock, this.tested.getClock());
	}

	/**
	 */
	public void testGetEnvironmentalObject() {
		assertSame(this, this.tested.getEnvironmentalObject(Object.class));
	}

}
