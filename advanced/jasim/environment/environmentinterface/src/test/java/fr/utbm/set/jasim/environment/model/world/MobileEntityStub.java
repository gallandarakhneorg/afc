/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.environment.model.world;

import java.util.Collection;
import java.util.Map;
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
import fr.utbm.set.math.AngularUnit;
import fr.utbm.set.math.SpeedUnit;

import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influencable;
import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.KinematicPerceivable;
import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.SteeringPerceivable;
import org.arakhne.afc.jasim.environment.model.place.Place;
import org.arakhne.afc.jasim.environment.model.world.MobileEntity;
import org.arakhne.afc.jasim.environment.model.world.WorldEntity;
import org.arakhne.afc.jasim.environment.semantics.Semantic;

/** Stub for MobileEntity
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MobileEntityStub implements MobileEntity<Bounds<?,?,?>> {

	private static final long serialVersionUID = 5443794373666088497L;

	@Override
	public Place<?, ?, ? extends MobileEntity<Bounds<?, ?, ?>>> getPlace() {
		return null;
	}

	@Override
	public void setPlace(
			Place<?, ?, ? extends MobileEntity<Bounds<?, ?, ?>>> place) {
		//
	}

	@Override
	public void addSemantic(Semantic... semanticalTag) {
		//
	}

	@Override
	@Deprecated
	public void addSemantic(Collection<? extends Semantic> semanticalTag) {
		//
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
	public UUID getIdentifier() {
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
	public int compareTo(WorldEntity<? extends Bounds<?, ?, ?>> o) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserData(String name, Object value) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserData(Map<String,Object> userData) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getUserData(String name) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getUserData(String name, Class<T> type) {
		return null;
	}
	
}