/**
 * 
 */
package fr.utbm.set.jasim.environment.interfaces.body;

import java.util.UUID;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception3D;
import fr.utbm.set.jasim.environment.model.world.Mesh3D;
import fr.utbm.set.math.AngularUnit;
import fr.utbm.set.math.MeasureUnitUtil;
import fr.utbm.set.math.SpeedUnit;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SteeringAgentBody3DTest extends AbstractTestCase {

	private double forwardSpeed, angularSpeed;
	private double linearAcceleration, linearDeceleration;
	private double angularAcceleration, angularDeceleration;
	private Mesh3D mesh;
	private SteeringAgentBody3D<Influence,Perception3D,AlignedBoundingBox> tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.forwardSpeed = 100.;
		this.angularSpeed = 25.;
		this.linearAcceleration = 100.;
		this.linearDeceleration = 80.;
		this.angularAcceleration = 10.;
		this.angularDeceleration = 8.;
		this.mesh = new Mesh3D(false, randomPoints3D());
		this.tested = new SteeringAgentBody3D<Influence,Perception3D,AlignedBoundingBox>(
				UUID.randomUUID(),
				Perception3D.class,
				this.mesh.toBounds(AlignedBoundingBox.class),
				this.mesh,
				this.forwardSpeed, this.angularSpeed,
				this.linearAcceleration, this.linearDeceleration,
				this.angularAcceleration, this.angularDeceleration);
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
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody3D#getMaxAngularAcceleration()}.
	 */
	public void testGetMaxAngularAcceleration() {
		assertEpsilonEquals(this.angularAcceleration, this.tested.getMaxAngularAcceleration());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody3D#getMaxAngularAcceleration(AngularUnit)}.
	 */
	public void testGetMaxAngularAccelerationAngularUnit() {
		double expected;
		for(AngularUnit unit : AngularUnit.values()) {
			expected = MeasureUnitUtil.convert(this.angularAcceleration, AngularUnit.RADIANS_PER_SECOND, unit);
			assertEpsilonEquals(expected, this.tested.getMaxAngularAcceleration(unit));
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody3D#getMaxAngularDeceleration()}.
	 */
	public void testGetMaxAngularDeceleration() {
		assertEpsilonEquals(this.angularDeceleration, this.tested.getMaxAngularDeceleration());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody3D#getMaxAngularDeceleration(AngularUnit)}.
	 */
	public void testGetMaxAngularDecelerationAngularUnit() {
		double expected;
		for(AngularUnit unit : AngularUnit.values()) {
			expected = MeasureUnitUtil.convert(this.angularDeceleration, AngularUnit.RADIANS_PER_SECOND, unit);
			assertEpsilonEquals(expected, this.tested.getMaxAngularDeceleration(unit));
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody3D#getMaxAngularSpeed()}.
	 */
	public void testGetMaxAngularSpeed() {
		assertEpsilonEquals(this.angularSpeed, this.tested.getMaxAngularSpeed());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody3D#getMaxAngularSpeed(AngularUnit)}.
	 */
	public void testGetMaxAngularSpeedAngularUnit() {
		double expected;
		for(AngularUnit unit : AngularUnit.values()) {
			expected = MeasureUnitUtil.convert(this.angularSpeed, AngularUnit.RADIANS_PER_SECOND, unit);
			assertEpsilonEquals(expected, this.tested.getMaxAngularSpeed(unit));
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody3D#getMaxLinearAcceleration()}.
	 */
	public void testGetMaxLinearAcceleration() {
		assertEpsilonEquals(this.linearAcceleration, this.tested.getMaxLinearAcceleration());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody3D#getMaxLinearAcceleration(SpeedUnit)}.
	 */
	public void testGetMaxLinearAccelerationSpeedUnit() {
		double expected;
		for(SpeedUnit unit : SpeedUnit.values()) {
			expected = MeasureUnitUtil.convert(this.linearAcceleration, SpeedUnit.METERS_PER_SECOND, unit);
			assertEpsilonEquals(expected, this.tested.getMaxLinearAcceleration(unit));
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody3D#getMaxLinearDeceleration()}.
	 */
	public void testGetMaxLinearDeceleration() {
		assertEpsilonEquals(this.linearDeceleration, this.tested.getMaxLinearDeceleration());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody3D#getMaxLinearDeceleration(SpeedUnit)}.
	 */
	public void testGetMaxLinearDecelerationSpeedUnit() {
		double expected;
		for(SpeedUnit unit : SpeedUnit.values()) {
			expected = MeasureUnitUtil.convert(this.linearDeceleration, SpeedUnit.METERS_PER_SECOND, unit);
			assertEpsilonEquals(expected, this.tested.getMaxLinearDeceleration(unit));
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody3D#getMaxLinearSpeed()}.
	 */
	public void testGetMaxLinearSpeed() {
		assertEpsilonEquals(this.forwardSpeed, this.tested.getMaxLinearSpeed());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody3D#getMaxLinearSpeed(SpeedUnit)}.
	 */
	public void testGetMaxLinearSpeedSpeedUnit() {
		double expected;
		for(SpeedUnit unit : SpeedUnit.values()) {
			expected = MeasureUnitUtil.convert(this.forwardSpeed, SpeedUnit.METERS_PER_SECOND, unit);
			assertEpsilonEquals(expected, this.tested.getMaxLinearSpeed(unit));
		}
	}

}
