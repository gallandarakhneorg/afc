/**
 * 
 */
package fr.utbm.set.jasim.environment.interfaces.body;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.influences.InfluenceApplicationStatus;
import fr.utbm.set.jasim.environment.model.world.Mesh3D;
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
public class InfluencerMobileEntity3DTest extends AbstractTestCase {

	private Mesh3D mesh;
	private InfluencerMobileEntity3D<Influence,AlignedBoundingBox> tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.mesh = new Mesh3D(false, randomPoints3D());
		this.tested = new InfluencerMobileEntity3DStub(this.mesh);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.mesh = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.InfluencerMobileEntity3D#getEnvironment()}.
	 */
	public void testGetEnvironment() {
		assertNull(this.tested.getEnvironment());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.InfluencerMobileEntity3D#setEnvironment(fr.utbm.set.jasim.environment.interfaces.body.BodyContainerEnvironment)}.
	 */
	public void testSetEnvironment() {
		assertNull(this.tested.getEnvironment());

		BodyContainerEnvironment env = new BodyContainerEnvironmentStub();
		this.tested.setEnvironment(env);
		assertSame(env, this.tested.getEnvironment());

		this.tested.setEnvironment(null);
		assertNull(this.tested.getEnvironment());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.InfluencerMobileEntity3D#getSimulationClock()}.
	 */
	public void testGetSimulationClock() {
		Clock clock = new ClockStub(this.RANDOM.nextDouble(), this.RANDOM.nextDouble());
		BodyContainerEnvironment env = new BodyContainerEnvironmentStub(clock);
		this.tested.setEnvironment(env);
		assertSame(clock, this.tested.getSimulationClock());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.InfluencerMobileEntity3D#getLastInfluenceStatus()}.
	 */
	public void testGetLastInfluenceStatus() {
		assertEquals(InfluenceApplicationStatus.SUCCESS, this.tested.getLastInfluenceStatus());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.InfluencerMobileEntity3D#setLastInfluenceStatus(fr.utbm.set.jasim.environment.interfaces.body.influences.InfluenceApplicationStatus)}.
	 */
	public void testSetLastInfluenceStatus() {
		assertEquals(InfluenceApplicationStatus.SUCCESS, this.tested.getLastInfluenceStatus());

		for(InfluenceApplicationStatus status : InfluenceApplicationStatus.values()) {
			this.tested.setLastInfluenceStatus(status);
			assertEquals(status, this.tested.getLastInfluenceStatus());
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.InfluencerMobileEntity3D#influence(Influence[])}.
	 */
	public void testInfluence() {
		BodyContainerEnvironmentStub env = new BodyContainerEnvironmentStub();
		this.tested.setEnvironment(env);

		Collection<Influence> reference = new ArrayList<Influence>();
		int count = this.RANDOM.nextInt(500)+20;
		for(int i=0; i<count; ++i) {
			reference.add(new InfluenceStub());
		}
		
		for(Influence influence : reference) {
			this.tested.influence(influence);
		}
		
		assertEquals(count, env.influences.size());
		
		for(Influence influence : reference) {
			assertTrue(env.influences.remove(influence));
		}
		
		assertTrue(env.influences.isEmpty());
	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
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

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Date getOperatingSystemStartUpTime() {
			return null;
		}
		
	}

}
