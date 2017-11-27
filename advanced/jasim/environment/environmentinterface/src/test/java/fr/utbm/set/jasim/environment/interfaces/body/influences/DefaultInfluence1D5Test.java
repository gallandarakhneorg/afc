/**
 * 
 */
package fr.utbm.set.jasim.environment.interfaces.body.influences;

import fr.utbm.set.geom.object.Segment1D;
import fr.utbm.set.geom.transform.Transform1D5;
import fr.utbm.set.jasim.environment.time.ClockStub;
import fr.utbm.set.unittest.AbstractExtendedTestCase;

import org.arakhne.afc.jasim.environment.interfaces.body.influences.DefaultInfluence1D5;
import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influencable;
import org.arakhne.afc.jasim.environment.time.Clock;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultInfluence1D5Test extends AbstractExtendedTestCase {

	private Influencable influencable;
	private Transform1D5<Segment1D> transform;
	private DefaultInfluence1D5<Segment1D> tested;

	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.influencable = new InfluencableStub();
		this.transform = new Transform1D5<Segment1D>();
		this.transform.setIdentity();
		this.transform.setTranslation(10.,10.);
		this.tested = new DefaultInfluence1D5<Segment1D>(this.influencable, this.transform);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.influencable = null;
		this.transform = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.interfaces.body.influences.DefaultInfluence1D5#getTransformation()}.
	 */
	public void testGetTransformation() {
		assertSame(this.transform, this.tested.getTransformation());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.interfaces.body.influences.DefaultInfluence1D5#getTransformation(org.arakhne.afc.jasim.environment.time.Clock)}.
	 */
	public void testGetTransformationClock() {
		Clock clock = new ClockStub(1., 1.);
		assertEpsilonEquals(this.transform, this.tested.getTransformation(clock));

		clock = new ClockStub(1., .5);
		Transform1D5<Segment1D> tr = new Transform1D5<Segment1D>();
		tr.setIdentity();
		tr.setTranslation(5.,5.);
		assertEpsilonEquals(tr, this.tested.getTransformation(clock));
	}

}
