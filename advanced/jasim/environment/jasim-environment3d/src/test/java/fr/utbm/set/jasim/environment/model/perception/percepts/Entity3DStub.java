/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.percepts;

import javax.vecmath.Point3d;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.semantics.ObjectType;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class Entity3DStub extends Entity3D<AlignedBoundingBox> {

	private static final long serialVersionUID = -6063321133663701045L;

	/**
	 * @param lower
	 * @param upper
	 */
	public Entity3DStub(Point3d lower, Point3d upper) {
		super(
				new AlignedBoundingBox(lower, upper),
				ObjectType.OBJECTTYPE_SINGLETON,
				true);
	}

	/**
	 */
	public Entity3DStub() {
		super(
				new AlignedBoundingBox(),
				ObjectType.OBJECTTYPE_SINGLETON,
				true);
	}

}
