/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.percepts;

import java.util.UUID;

import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.jasim.environment.model.world.Entity3D;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AbstractPerception3DStub extends AbstractPerception3D {

	/**
	 * @param frustum
	 * @param type
	 * @param perceivedObject
	 */
	public AbstractPerception3DStub(UUID frustum, IntersectionType type, Entity3D<?> perceivedObject) {
		super(frustum, type, perceivedObject);
	}

}
