/*
 * 
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
package org.arakhne.afc.jasim.environment.model.place;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.UUID;

import org.arakhne.afc.jasim.environment.model.actions.EnvironmentalActionCollector;
import org.arakhne.afc.jasim.environment.model.dynamics.DynamicsEngine;
import org.arakhne.afc.jasim.environment.model.ground.Ground;
import org.arakhne.afc.jasim.environment.model.influences.InfluenceCollector;
import org.arakhne.afc.jasim.environment.model.influences.InfluenceSolver;
import org.arakhne.afc.jasim.environment.model.perceptions.PerceptionGeneratorType;
import org.arakhne.afc.jasim.environment.model.world.MobileEntity;
import org.arakhne.afc.jasim.environment.model.world.WorldEntity;
import org.arakhne.afc.jasim.environment.model.world.WorldModelContainer;

/**
 * Describes a {@link Place place} in a situated environment during
 * its initialization stage.
 * 
 * @param <SE> is the type of static entity supported by this environment.
 * @param <ME> is the type of mobile entity supported by this environment.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class WrappedPlaceDescription<SE extends WorldEntity<?>, ME extends MobileEntity<?>>
implements PlaceDescription<SE,ME> {

	private final WeakReference<Place<?,SE,ME>> place;

	/**
	 * @param place is the place to wrap.
	 */
	public WrappedPlaceDescription(Place<?,SE,ME> place) {
		assert(place!=null);
		this.place = new WeakReference<Place<?,SE,ME>>(place);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		Place<?,SE,ME> p = this.place.get();
		if (p!=null) {
			return p.getIdentifier().toString();
		}
		return super.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ground getGround() {
		return this.place.get().getGround();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID getIdentifier() {
		return this.place.get().getIdentifier();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.place.get().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<? extends ME> getMobileEntities() {
		WorldModelContainer<SE,ME> model = this.place.get().getWorldModel();
		return model.getMobileEntities();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<? extends SE> getStaticEntities() {
		WorldModelContainer<SE,ME> model = this.place.get().getWorldModel();
		return model.getStaticEntities();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getWorldModel(Class<T> type) {
		WorldModelContainer<SE,ME> model = this.place.get().getWorldModel();
		if (type.isInstance(model)) return type.cast(model);
		return model.getInnerWorldModel(type);
	}

	/** {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends InfluenceSolver<?,ME>> getInfluenceSolverType() {
		return (Class<? extends InfluenceSolver<?,ME>>)this.place.get().getInfluenceSolver().getClass();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class<? extends DynamicsEngine> getDynamicsEngineType() {
		return this.place.get().getDynamicsEngine().getClass();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class<? extends InfluenceCollector> getInfluenceCollectorType() {
		return this.place.get().getInfluenceCollector().getClass();
	}

	/** {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends EnvironmentalActionCollector<?>> getEnvironmentalActionCollectorType() {
		return (Class<? extends EnvironmentalActionCollector<?>>)this.place.get().getEnvironmentalActionCollector().getClass();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PerceptionGeneratorType getPerceptionGeneratorType() {
		return this.place.get().getPerceptionGeneratorType();
	}

}
