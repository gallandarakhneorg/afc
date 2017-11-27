/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, 2012 Multiagent Team - Systems and Transportation Laboratory (SeT)
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
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.vecmath.Point3d;

import org.arakhne.afc.vmutil.locale.Locale;

import fr.utbm.set.collection.CollectionUtil;
import fr.utbm.set.geom.bounds.bounds1d.Bounds1D;
import fr.utbm.set.geom.bounds.bounds1d5.Bounds1D5;
import fr.utbm.set.geom.bounds.bounds2d.Bounds2D;
import fr.utbm.set.geom.bounds.bounds3d.Bounds3D;
import fr.utbm.set.geom.object.EuclidianPoint2D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencable;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.KinematicPerceivable;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.SteeringPerceivable;
import fr.utbm.set.jasim.environment.semantics.ObjectType;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.environment.semantics.SemanticComparator;

/** This interface representes an object in a 3D space.
 *
 * @param <B> is the type of the bounding box associated to this entity.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Entity3D<B extends Bounds3D> extends AbstractWorldEntity<B> {

	private static final long serialVersionUID = -8625139513787357112L;

	private Set<Semantic> semantics = null;

	/** Bounds of this entity.
	 */
	protected final B bounds;
	
	/** Bufferized position.
	 */
	private EuclidianPoint3D positionBuffer;

	/** Indicates if this entity is supported to be on a ground.
	 * This attribute influence z parameter replied by
	 * the getPosition() function. 
	 */
	protected final boolean onGround;
	
	/** Type of this entity.
	 */
	private final Semantic type;
	
	/**
	 * @param bounds is the bounds of this entity.
	 * @param type is the type of this entity.
	 * @param onGround is <code>true</code> if this entity is located on a ground,
	 * otherwhise <code>false</code>
	 */ 
	public Entity3D(B bounds, Semantic type, boolean onGround) {
		super(UUID.randomUUID());
		this.type = type==null ? ObjectType.OBJECTTYPE_SINGLETON : type;
		this.onGround = onGround;
		this.bounds = bounds;
	}
	
	/**
	 * @param identifier is the identifier of this object. This value should be unique.
	 * @param type is the type of this entity.
	 * @param bounds is the bounds of this entity.
	 * @param onGround is <code>true</code> if this entity is located on a ground,
	 * otherwhise <code>false</code>
	 */ 
	public Entity3D(UUID identifier, B bounds, Semantic type, boolean onGround) {
		super(identifier);
		this.type = type==null ? ObjectType.OBJECTTYPE_SINGLETON : type;
		this.onGround = onGround;
		this.bounds = bounds;
	}
	
	/** Invoked to clear the buffers.
	 */
	protected void clearBuffers() {
		this.positionBuffer = null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public B getBounds() {
		return this.bounds;
		
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Semantic getType() {
		return this.type;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Collection<? extends Semantic> getAllSemantics() {
		if (this.semantics==null) {
			if (this.type==null)
				return Collections.emptySet();
			return Collections.singleton(this.type);
		}
		return CollectionUtil.mergeCollections(
				Collections.singleton(this.type),
				this.semantics);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isA(Semantic entityType) {
		if (this.type!=null && this.type.isA(entityType))
			return true;
		if (this.semantics!=null) {
			for(Semantic s : this.semantics) {
				if (s!=null && s.isA(entityType))
					return true;
			}
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void addSemantics(Collection<? extends Semantic> semanticalTag) {
		if (this.semantics==null) 
			this.semantics = new TreeSet<Semantic>(new SemanticComparator());
		this.semantics.addAll(semanticalTag);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void removeSemantic(Semantic semanticalTag) {
		if (this.semantics==null) return;
		this.semantics.remove(semanticalTag);
		if (this.semantics.isEmpty()) this.semantics = null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isInfluencable() {
		return (this instanceof Influencable);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Influencable toInfluencable() {
		if (this instanceof Influencable) return (Influencable)this;
		throw new UnsupportedOperationException("entity is not influencable"); //$NON-NLS-1$
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isKinematic() {
		return (this instanceof KinematicPerceivable);
	}

	/** {@inheritDoc}
	 */
	@Override
	public KinematicPerceivable toKinematic() {
		if (this instanceof KinematicPerceivable) return (KinematicPerceivable)this;
		throw new UnsupportedOperationException("entity is not kinematic"); //$NON-NLS-1$
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isSteering() {
		return (this instanceof SteeringPerceivable);
	}

	@Override
	public SteeringPerceivable toSteering() {
		if (this instanceof SteeringPerceivable) return (SteeringPerceivable)this;
		throw new UnsupportedOperationException("entity is not steering"); //$NON-NLS-1$
	}

	/** Invoked to compute the position of this entity.
	 * <p>
	 * By default this function uses the center of the bounding box.
	 * 
	 * @return the position of the entity
	 */
	protected EuclidianPoint3D calcPosition() {
		B lbounds = getBounds();
		assert(lbounds!=null);
		EuclidianPoint3D p = lbounds.getCenter();
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		double z = cs.height(this.onGround ? lbounds.getLower() : p);
		p = new EuclidianPoint3D(p);
		cs.setHeight(p, z);
		return p;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getPosition3D() {
		if (this.positionBuffer==null) {
			this.positionBuffer = calcPosition();
		}
		assert(this.positionBuffer!=null);
		return this.positionBuffer;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getPosition2D5() {
		return getPosition3D();
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getPosition2D() {
		if (this.positionBuffer==null) {
			this.positionBuffer = calcPosition();
		}
		assert(this.positionBuffer!=null);
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		return cs.toCoordinateSystem2D(this.positionBuffer);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point1D5 getPosition1D5() {
		throw new UnsupportedOperationException("1.5D is not supported from 3D"); //$NON-NLS-1$
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point1D getPosition1D() {
		throw new UnsupportedOperationException("1D is not supported from 3D"); //$NON-NLS-1$
	}

	/** Replies the position of this object.
	 * 
	 * @return the position of this object
	 */
    public final Point3d getTranslation() {
		return getPosition3D();
    }

    /** Replies if this entity is assumed to be located on the ground (keep on floor).
	 * 
	 * @return <code>true</code> if the entity uses keep on floor, otherwise <code>false</code>
	 */
	public final boolean isOnGround() {
		return this.onGround;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds1D<?> getBounds1D() {
		throw new UnsupportedOperationException("1D not supported from 3D"); //$NON-NLS-1$
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds1D5<?> getBounds1D5() {
		throw new UnsupportedOperationException("1.5D not supported from 3D"); //$NON-NLS-1$
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds2D getBounds2D() {
		return this.bounds.toBounds2D(CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds3D getBounds2D5() {
		return this.bounds;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds3D getBounds3D() {
		return this.bounds;
	}

	@Override
	public String toString() {
		B b = getBounds();
		return Locale.getString("DESCRIPTION", b==null ? null : b.toString()); //$NON-NLS-1$
	}
	
	
	
}