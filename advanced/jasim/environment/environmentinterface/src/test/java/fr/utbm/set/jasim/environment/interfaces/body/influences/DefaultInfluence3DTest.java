/**
 * 
 */
package fr.utbm.set.jasim.environment.interfaces.body.influences;

import javax.vecmath.AxisAngle4d;

import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.jasim.environment.time.ClockStub;
import fr.utbm.set.math.MathConstants;
import fr.utbm.set.unittest.AbstractExtendedTestCase;

import org.arakhne.afc.jasim.environment.interfaces.body.influences.DefaultInfluence3D;
import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influencable;
import org.arakhne.afc.jasim.environment.time.Clock;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultInfluence3DTest extends AbstractExtendedTestCase {

	private Influencable influencable;
	private Transform3D transform;
	private DefaultInfluence3D tested;

	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.influencable = new InfluencableStub();
		this.transform = new Transform3D();
		this.transform.setIdentity();
		this.transform.setTranslation(10.,10.,19.);
		this.transform.setRotation(new AxisAngle4d(0.,0.,1.,MathConstants.QUARTER_PI));
		this.tested = new DefaultInfluence3D(this.influencable, this.transform);
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
	 * Test method for {@link org.arakhne.afc.jasim.environment.interfaces.body.influences.DefaultInfluence3D#getTransformation()}.
	 */
	public void testGetTransformation() {
		assertSame(this.transform, this.tested.getTransformation());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.interfaces.body.influences.DefaultInfluence3D#getTransformation(org.arakhne.afc.jasim.environment.time.Clock)}.
	 */
	public void testGetTransformationClock() {
		Clock clock = new ClockStub(1., 1.);
		assertEpsilonEquals(this.transform, this.tested.getTransformation(clock));

		clock = new ClockStub(1., .5);
		Transform3D tr = new Transform3D();
		tr.setIdentity();
		tr.setTranslation(5.,5.,9.5);
		tr.setRotation(new AxisAngle4d(0.,0.,1.,MathConstants.QUARTER_PI/2.));
		assertEpsilonEquals(tr, this.tested.getTransformation(clock));
	}

}
