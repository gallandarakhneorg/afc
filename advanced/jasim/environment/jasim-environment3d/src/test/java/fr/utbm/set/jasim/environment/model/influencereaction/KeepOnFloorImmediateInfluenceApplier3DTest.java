/**
 * 
 */
package fr.utbm.set.jasim.environment.model.influencereaction;

import java.util.UUID;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Direction2D;
import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.object.EuclidianPoint2D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.DefaultInfluence3D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencable;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence3D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.InfluenceApplicationStatus;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencer;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.SteeringEntity;
import fr.utbm.set.jasim.environment.model.ground.AlignedIndoorGround;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.jasim.environment.time.ConstantTimeManager;
import fr.utbm.set.math.AngularUnit;
import fr.utbm.set.math.SpeedUnit;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class KeepOnFloorImmediateInfluenceApplier3DTest extends AbstractTestCase {

	private ConstantTimeManager timeManager;
	private ActionCollectorStub actionCollector;
	private KeepOnFloorImmediateInfluenceApplier3D<MobileEntity3D<CombinableBounds3D>> tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.tested = new KeepOnFloorImmediateInfluenceApplier3D<MobileEntity3D<CombinableBounds3D>>();
		this.tested.setGround(new AlignedIndoorGround(UUID.randomUUID(), 
				0., 0., 1000., 1000., 5.));
		
		this.timeManager = new ConstantTimeManager(.5);
		this.timeManager.onEnvironmentBehaviourStarted();
		this.timeManager.onEnvironmentBehaviourFinished();
		this.timeManager.onEnvironmentBehaviourStarted();
		this.timeManager.onEnvironmentBehaviourFinished();
		this.tested.setTimeManager(this.timeManager);
		
		this.actionCollector = new ActionCollectorStub();
		this.tested.setActionCollector(this.actionCollector);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.actionCollector = null;
		this.timeManager = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 */
	public void testFilterStandardInfluenceInfluence_SteeringEntity() {
				
		assertTrue(this.actionCollector.collectedActions.isEmpty());

		Transform3D tr = new Transform3D();
		tr.setTranslation(10., 10., 500.);
		Influence3D influence = new DefaultInfluence3D(tr);
		SteeringInfluencableStub influencable = new SteeringInfluencableStub();
		influence.setInfluencedObject(influencable);
		InfluencerStub influencer = new InfluencerStub();
		influence.setInfluencer(influencer);
		
		EnvironmentalAction3D reaction = this.tested.filterStandardInfluence(
						influencer,
						influencable,
						influence);
		
		assertTrue(this.actionCollector.collectedActions.isEmpty());
		assertNotNull(reaction);
		//Transform3D tr2 = reaction.getTransformation();
		//assertEpsilonEquals(new Point3d(5.,5.,5.-influencable.pos3d.z), tr2.getTranslation());
	}

	private class InfluencerStub implements Influencer {
		
		public InfluencerStub() {
			//
		}

		@Override
		public void setLastInfluenceStatus(InfluenceApplicationStatus status) {
			//
		}

		@Override
		public void setLastInfluenceStatus(InfluenceApplicationStatus status,
				Throwable e) {
			//
		}
		
	}
	
	private class SteeringInfluencableStub implements Influencable, SteeringEntity {
		
		public final UUID id = UUID.randomUUID();
		public final EuclidianPoint3D pos3d = new EuclidianPoint3D(250., 240, 230.);
		
		public SteeringInfluencableStub() {
			//
		}

		@Override
		public UUID getIdentifier() {
			return this.id;
		}

		@Override
		public Point1D getPosition1D() {
			return null;
		}

		@Override
		public Point1D5 getPosition1D5() {
			return null;
		}

		@Override
		public EuclidianPoint2D getPosition2D() {
			return new EuclidianPoint2D(this.pos3d.x, this.pos3d.y);
		}

		@Override
		public EuclidianPoint3D getPosition2D5() {
			return this.pos3d;
		}

		@Override
		public EuclidianPoint3D getPosition3D() {
			return this.pos3d;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMaxAngularAcceleration() {
			return 1.;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMaxAngularAcceleration(AngularUnit unit) {
			return 1.;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMaxAngularDeceleration() {
			return 1.;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMaxAngularDeceleration(AngularUnit unit) {
			return 1.;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMaxLinearAcceleration() {
			return 1.;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMaxLinearAcceleration(SpeedUnit unit) {
			return 1.;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMaxLinearDeceleration() {
			return 1.;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMaxLinearDeceleration(SpeedUnit unit) {
			return 1.;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getAngularSpeed() {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getAngularSpeed(AngularUnit unit) {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getLinearSpeed() {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getLinearSpeed(SpeedUnit unit) {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMaxAngularSpeed() {
			return 1.;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMaxAngularSpeed(AngularUnit unit) {
			return 1.;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMaxLinearSpeed() {
			return 1.;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMaxLinearSpeed(SpeedUnit unit) {
			return 1.;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Direction1D getViewDirection1D() {
			return Direction1D.SEGMENT_DIRECTION;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Direction1D getViewDirection1D5() {
			return Direction1D.SEGMENT_DIRECTION;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Direction2D getViewDirection2D() {
			return new Direction2D(10,10);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Direction3D getViewDirection3D() {
			return new Direction3D(10,10,10,0.);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Vector3d getLinearUnitVelocity3D() {
			return getLinearVelocity3D();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Vector2d getLinearUnitVelocity2D() {
			return getLinearVelocity2D();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Vector2d getLinearUnitVelocity1D5() {
			return getLinearVelocity1D5();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getLinearUnitVelocity1D() {
			return getLinearVelocity1D();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Vector3d getLinearVelocity3D() {
			return new Vector3d(1,0,0);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Vector2d getLinearVelocity2D() {
			return new Vector2d(1,0);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Vector2d getLinearVelocity1D5() {
			return new Vector2d(1,0);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getLinearVelocity1D() {
			return 1;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public AxisAngle4d getAngularVelocity3D() {
			return new AxisAngle4d();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getAngularVelocity2D() {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getAngularVelocity1D5() {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getAngularVelocity1D() {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getLinearAcceleration() {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getLinearAcceleration(SpeedUnit unit) {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getAngularAcceleration() {
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getAngularAcceleration(AngularUnit unit) {
			return 0;
		}
		
	}
	
}
