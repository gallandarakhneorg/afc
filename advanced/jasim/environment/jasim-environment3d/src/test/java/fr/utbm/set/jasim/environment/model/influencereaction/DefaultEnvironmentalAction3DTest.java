/**
 * 
 */
package fr.utbm.set.jasim.environment.model.influencereaction;

import java.util.concurrent.TimeUnit;

import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.jasim.environment.time.AbstractClock;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.math.MeasureUnitUtil;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultEnvironmentalAction3DTest extends AbstractTestCase {

	private Clock clock;
	private Transform3D transformation;
	private DefaultEnvironmentalAction3D tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.clock = new ClockStub(1., .5);
		this.transformation = new Transform3D();
		this.tested = new DefaultEnvironmentalAction3D(this, this.clock, this.transformation);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.clock = null;
		this.transformation = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.influencereaction.DefaultEnvironmentalAction3D#getTransformation()}.
	 */
	public void testGetTransformation() {
		assertSame(this.transformation, this.tested.getTransformation());
	}

	/**
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid fr.utbm.set.sfc.jasim
	 * @mavenartifactid jasim-environment-base
	 */
	private final class ClockStub extends AbstractClock {

		/**
		 */
		double currentTime;
		/**
		 */
		double duration;
		
		/**
		 * @param time
		 * @param duration
		 */
		public ClockStub(double time, double duration) {
			super(TimeUnit.SECONDS);
			this.currentTime = time;
			this.duration = duration;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public double getSimulationStepDuration(TimeUnit desired_unit) {
			return MeasureUnitUtil.convert(this.duration, TimeUnit.SECONDS, desired_unit);
		}

		/** {@inheritDoc}
		 */
		@Override
		public double getSimulationTime(TimeUnit desired_unit) {
			return MeasureUnitUtil.convert(this.currentTime, TimeUnit.SECONDS, desired_unit);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long getSimulationStepCount() {
			return 0;
		}
		
	}
	 
}
