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
package fr.utbm.set.jasim.environment.interfaces.probes;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import fr.utbm.set.jasim.environment.interfaces.body.AgentBody;
import fr.utbm.set.jasim.environment.interfaces.body.frustums.Frustum;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment;
import fr.utbm.set.jasim.environment.model.place.Place;
import fr.utbm.set.jasim.environment.model.place.PlaceContainer;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;

/** This class is probing the frustums of the simulated entities.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class FrustumProbe extends AbstractEnvironmentalProbe {
	
	private final Map<String,Object> collectedValues = new TreeMap<String, Object>();
	private final WeakReference<Place<?,?,?>> place;

	/**
	 * @param name is the name of the probe.
	 * @param placeName is the name of the place to probe.
	 * @param environment is the environment to probe
	 */
	public FrustumProbe(String name, UUID placeName, SituatedEnvironment<?,?,?,?> environment) {
		super(name, null);
		Place<?,?,?> p = null;
		if (environment instanceof PlaceContainer<?,?>) {
			PlaceContainer<?,?> container = (PlaceContainer<?,?>)environment;
			p = container.getPlaceObject(placeName);
		}
		
		this.place = new WeakReference<Place<?,?,?>>(p);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public UUID getPlace() {
		Place<?,?,?> p = (this.place!=null) ? this.place.get() : null;
		if (p!=null) return p.getIdentifier();
		return null;
	}

	/** Collects the values.
	 */
	@Override
	protected void collect() {
		this.collectedValues.clear();
		Place<?,?,?> thePlace = (this.place==null) ? null : this.place.get();
		if (thePlace!=null) {
			Iterator<? extends WorldEntity<?>> staticEntities = thePlace.getWorldModel().getStaticEntities();
			if (staticEntities!=null) {
				WorldEntity<?> sentity;
				while (staticEntities.hasNext()) {
					sentity = staticEntities.next();
					if (sentity instanceof AgentBody<?,?>) {
						Collection<? extends Frustum<?,?,?>> frustums = ((AgentBody<?,?>)sentity).getFrustums();
						Collection<Object> col = new ArrayList<Object>(frustums.size());
						for(Frustum<?,?,?> frustum : frustums) {
							col.add(frustum.clone());
						}
						this.collectedValues.put(sentity.getIdentifier().toString()+"#ProbedFrustums",col); //$NON-NLS-1$
					}
				}
			}

			Iterator<? extends MobileEntity<?>> mobileEntities = thePlace.getWorldModel().getMobileEntities();
			if (mobileEntities!=null) {
				MobileEntity<?> mentity;
				while (mobileEntities.hasNext()) {
					mentity = mobileEntities.next();
					if (mentity instanceof AgentBody<?,?>) {
						Collection<? extends Frustum<?,?,?>> frustums = ((AgentBody<?,?>)mentity).getFrustums();
						Collection<Object> col = new ArrayList<Object>(frustums.size());
						for(Frustum<?,?,?> frustum : frustums) {
							col.add(frustum.clone());
						}
						this.collectedValues.put(mentity.getIdentifier().toString(),col);
					}
				}
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public Map<String,Object> getCollectedValues() {
		return new TreeMap<String, Object>(this.collectedValues);
	}
		
}