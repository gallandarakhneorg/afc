/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, 2012, Multiagent Team - Systems and Transportation Laboratory (SeT)
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

import java.util.UUID;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.math.AngularUnit;
import fr.utbm.set.math.MeasureUnitUtil;
import fr.utbm.set.math.SpeedUnit;

/** This class representes a mobile object in a 1D, 2D or 3D space.
 *
 * @param <B> is the type of the bounding box associated to this entity.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractMobileEntity<B extends Bounds<?,?,?>>
extends AbstractWorldEntity<B> implements MobileEntity<B> {

	private static final long serialVersionUID = -4175298268563665897L;

	/**
	 * @param id is the identifier of this entity.
	 */
	public AbstractMobileEntity(UUID id) {
		super(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final double getLinearSpeed(SpeedUnit unit) {
		return MeasureUnitUtil.convert(getLinearSpeed(),
				SpeedUnit.METERS_PER_SECOND, unit);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final double getAngularSpeed(AngularUnit unit) {
		return MeasureUnitUtil.convert(getAngularSpeed(),
				AngularUnit.RADIANS_PER_SECOND, unit);
	}

}