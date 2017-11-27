/**
 * 
 */
package fr.utbm.set.jasim.environment.interfaces.body;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.model.world.Mesh3D;
import fr.utbm.set.jasim.environment.semantics.ObjectType;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class InfluencerMobileEntity3DStub extends InfluencerMobileEntity3D<Influence,AlignedBoundingBox> {

	private static final long serialVersionUID = 5230268214569082414L;

	private static AlignedBoundingBox createBox(Mesh3D mesh) {
		assert(mesh.isGlobalMesh());
		return mesh.toBounds(AlignedBoundingBox.class);
	}
	
	/**
	 * @param mesh
	 */
	public InfluencerMobileEntity3DStub(Mesh3D mesh) {
		super(createBox(mesh),
				ObjectType.OBJECTTYPE_SINGLETON,
				true,
				mesh);
	}
	
}
