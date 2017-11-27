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
package fr.utbm.set.jasim.environment.model.ground;

import java.util.UUID;

import org.arakhne.afc.jasim.environment.model.ground.Ground;
import org.arakhne.afc.jasim.environment.semantics.GroundType;

/** This interface representes the ground in a environment.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GroundStub implements Ground {

	private final UUID id = UUID.randomUUID();
	
	/** {@inheritDoc}
	 */
	@Override
	public UUID getIdentifier() {
		return this.id;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getMinX() {
		return 0.;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getMaxX() {
		return 0.;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getMinY() {
		return 0.;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getMaxY() {
		return 0.;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getMinZ() {
		return 0.;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getMaxZ() {
		return 0.;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSizeX() {
		return 0.;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double getSizeY() {
		return 0.;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double getSizeZ() {
		return 0.;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getHeightAt(double x, double y) {
		return 0.;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isTraversable(double x, double y) {
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public GroundType getGroundType(double x, double y) {
		return null;
	}

}