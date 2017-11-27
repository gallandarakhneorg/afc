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
package fr.utbm.set.jasim.environment.model.place;

import java.lang.ref.WeakReference;

import fr.utbm.set.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import fr.utbm.set.jasim.environment.model.place.Place;
import fr.utbm.set.jasim.environment.model.place.Portal;
import fr.utbm.set.jasim.environment.model.place.PortalPosition;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;

/** This class representes a portal between to {@link Place places}.
 * A portal is bi-directional.
 *
 * @param <EA> is the type of the environmental actions supported by this place.
 * @param <SE> is the type of the immobile entities supported by this environment.
 * @param <ME> is the type of the mobile entities supported by this environment.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultPortal<EA extends EnvironmentalAction, SE extends WorldEntity<?>, ME extends MobileEntity<?>> implements Portal<EA,SE,ME> {

	private WeakReference<Place<EA,SE,ME>> place1;
	private WeakReference<Place<EA,SE,ME>> place2;
	private PortalPosition position1;
	private PortalPosition position2;
	
	/**
	 * 
	 */
	public DefaultPortal() {
		this.place1 = null;
		this.place2 = null;
		this.position1 = null;
		this.position2 = null;
	}
	
	/** Attach a place to this portal.
	 * <p>
	 * A place is not attached when the portal is already attached to two places.
	 * 
	 * @param place is the place to attach
	 * @param position is the portal's position in the given place.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	boolean attachPlace(Place<EA,SE,ME> place, PortalPosition position) {
		Place<EA,SE,ME> p = getFirstPlace();
		if (p==null) {
			this.place1 = new WeakReference<Place<EA,SE,ME>>(place);
			this.position1 = position;
			return true;
		}
		p = getSecondPlace();
		if (p==null) {
			this.place2 = new WeakReference<Place<EA,SE,ME>>(place);
			this.position2 = position;
			return true;
		}
		return false;
	}
	
	/** Detach a place to this portal.
	 * 
	 * @param place is the place to attach
	 */
	void detachPlace(Place<EA,SE,ME> place) {
		if (getFirstPlace()==place) {
			this.place1 = null;
			this.position1 = null;
		}
		if (getSecondPlace()==place) {
			this.place2 = null;
			this.position2 = null;
		}
	}

	/** Replies the first place.
	 * 
	 * @return the first place or <code>null</code>.
	 */
	public Place<EA,SE,ME> getFirstPlace() {
		return this.place1==null ? null : this.place1.get();
	}
	
	/** Replies the second place.
	 * 
	 * @return the second place or <code>null</code>.
	 */
	public Place<EA,SE,ME> getSecondPlace() {
		return this.place2==null ? null : this.place2.get();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isPlace(Place<EA,SE,ME> place) {
		return getFirstPlace()==place || getSecondPlace()==place;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public PortalPosition getPosition(Place<EA,SE,ME> place) {
		if (place==getFirstPlace()) {
			return this.position1;
		}
		if (place==getSecondPlace()) {
			return this.position2;
		}
		throw new IllegalArgumentException("place is not attached to the portal"); //$NON-NLS-1$
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isTraversableFrom(Place<EA,SE,ME> place) {
		Place<EA,SE,ME> p1 = getFirstPlace();
		Place<EA,SE,ME> p2 = getSecondPlace();
		if (place==p1) return this.position1!=null;
		if (place==p2) return this.position2!=null;
		throw new IllegalArgumentException("place is not attached to the portal"); //$NON-NLS-1$
	}

	/** {@inheritDoc}
	 */
	@Override
	public Place<EA,SE,ME> getOtherSide(Place<EA,SE,ME> place) {
		Place<EA,SE,ME> p1 = getFirstPlace();
		Place<EA,SE,ME> p2 = getSecondPlace();
		if (place==p1) return p2;
		if (place==p2) return p1;
		throw new IllegalArgumentException("place is not attached to the portal"); //$NON-NLS-1$
	}

}