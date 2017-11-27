/**
 * 
 */
package fr.utbm.set.jasim.environment.model.world;

import java.util.Collection;
import java.util.UUID;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.bounds.bounds1d.Bounds1D;
import fr.utbm.set.geom.bounds.bounds1d5.Bounds1D5;
import fr.utbm.set.geom.bounds.bounds2d.Bounds2D;
import fr.utbm.set.geom.bounds.bounds3d.Bounds3D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencable;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.KinematicPerceivable;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.SteeringPerceivable;
import fr.utbm.set.jasim.environment.model.place.Place;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.math.AngularUnit;
import fr.utbm.set.math.SpeedUnit;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AbstractMobileEntityStub extends AbstractMobileEntity<Bounds<?,?,?>> {

	/**
	 */
	private static final long serialVersionUID = 8826801785436989666L;

	/**
	 * @param id
	 */
	public AbstractMobileEntityStub(UUID id) {
		super(id);
	}
	
	@Override
	public void addSemantics(Collection<? extends Semantic> semanticalTag) {
		//
	}

	@Override
	public Bounds<?, ?, ?> getBounds() {
		return null;
	}

	@Override
	public void removeSemantic(Semantic semanticalTag) {
		//
	}

	@Override
	public Collection<? extends Semantic> getAllSemantics() {
		return null;
	}

	@Override
	public Semantic getType() {
		return null;
	}

	@Override
	public Bounds1D<?> getBounds1D() {
		return null;
	}

	@Override
	public Bounds1D5<?> getBounds1D5() {
		return null;
	}

	@Override
	public Bounds2D getBounds2D() {
		return null;
	}

	@Override
	public Bounds3D getBounds2D5() {
		return null;
	}

	@Override
	public Bounds3D getBounds3D() {
		return null;
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
	public Point2d getPosition2D() {
		return null;
	}

	@Override
	public Point3d getPosition2D5() {
		return null;
	}

	@Override
	public Point3d getPosition3D() {
		return null;
	}

	@Override
	public boolean isA(Semantic type) {
		return false;
	}

	@Override
	public boolean isInfluencable() {
		return false;
	}

	@Override
	public Influencable toInfluencable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isKinematic() {
		return false;
	}

	@Override
	public boolean isSteering() {
		return false;
	}

	@Override
	public KinematicPerceivable toKinematic() {
		throw new UnsupportedOperationException();
	}

	@Override
	public SteeringPerceivable toSteering() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getAngularAcceleration() {
		return 0;
	}

	@Override
	public double getAngularAcceleration(AngularUnit unit) {
		return 0;
	}

	@Override
	public double getAngularSpeed() {
		return 0;
	}

	@Override
	public double getLinearAcceleration() {
		return 0;
	}

	@Override
	public double getLinearSpeed() {
		return 0;
	}

	@Override
	public Place<?, ?, ? extends MobileEntity<Bounds<?, ?, ?>>> getPlace() {
		return null;
	}

	@Override
	public void setPlace(Place<?, ?, ? extends MobileEntity<Bounds<?, ?, ?>>> place) {
		//
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
	public Vector3d getLinearVelocity3D() {
		return new Vector3d();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2d getLinearVelocity2D() {
		return new Vector2d();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2d getLinearVelocity1D5() {
		return new Vector2d();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getLinearVelocity1D() {
		return 0;
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

}
