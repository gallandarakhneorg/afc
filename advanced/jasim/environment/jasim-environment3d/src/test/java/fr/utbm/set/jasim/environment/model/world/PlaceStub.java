/**
 * 
 */
package fr.utbm.set.jasim.environment.model.world;

import java.util.UUID;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.jasim.environment.model.actions.EnvironmentalActionCollector;
import fr.utbm.set.jasim.environment.model.dynamics.DynamicsEngine;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.influencereaction.EnvironmentalAction3D;
import fr.utbm.set.jasim.environment.model.influences.InfluenceCollector;
import fr.utbm.set.jasim.environment.model.influences.InfluenceSolver;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGenerator;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGeneratorType;
import fr.utbm.set.jasim.environment.model.place.Place;
import fr.utbm.set.jasim.environment.model.place.Portal;
import fr.utbm.set.jasim.environment.time.Clock;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class PlaceStub implements Place<EnvironmentalAction3D,Entity3D<AlignedBoundingBox>,MobileEntity3D<AlignedBoundingBox>> {

	private final UUID id = UUID.randomUUID();
	private final Clock clock;
	
	/**
	 * @param clock
	 */
	public PlaceStub(Clock clock) {
		this.clock = clock;
	}

	@Override
	public DynamicsEngine getDynamicsEngine() {
		return null;
	}

	@Override
	public EnvironmentalActionCollector<EnvironmentalAction3D> getEnvironmentalActionCollector() {
		return null;
	}

	@Override
	public Ground getGround() {
		return null;
	}

	@Override
	public UUID getIdentifier() {
		return this.id;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public InfluenceCollector getInfluenceCollector() {
		return null;
	}

	@Override
	public InfluenceSolver<EnvironmentalAction3D,MobileEntity3D<AlignedBoundingBox>> getInfluenceSolver() {
		return null;
	}

	@Override
	public PerceptionGenerator getPerceptionGenerator() {
		return null;
	}

	@Override
	public Portal<EnvironmentalAction3D, Entity3D<AlignedBoundingBox>, MobileEntity3D<AlignedBoundingBox>> getPortalAt(int index) {
		return null;
	}

	@Override
	public int getPortalCount() {
		return 0;
	}

	@Override
	public Clock getSimulationClock() {
		return this.clock;
	}

	@Override
	public WorldModelContainer<Entity3D<AlignedBoundingBox>, MobileEntity3D<AlignedBoundingBox>> getWorldModel() {
		return null;
	}

	@Override
	public WorldModelActuator<EnvironmentalAction3D, MobileEntity3D<AlignedBoundingBox>> getWorldModelUpdater() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PerceptionGeneratorType getPerceptionGeneratorType() {
		return PerceptionGeneratorType.LOCAL_SEQUENTIAL_TOPDOWN;
	}
	
}
