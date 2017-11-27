/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.frustum;

import java.util.UUID;

import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;

import fr.utbm.set.geom.bounds.bounds3d.BoundingPrimitiveType3D;
import fr.utbm.set.geom.plane.Plane4d;
import fr.utbm.set.geom.plane.XYPlane;
import fr.utbm.set.geom.plane.XZPlane;
import fr.utbm.set.geom.plane.YZPlane;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AbstractBoxedFrustum3DStub extends AbstractBoxedFrustum3D {

	private static final long serialVersionUID = -3067454386622463670L;

	/**
	 * @param identifier
	 */
	public AbstractBoxedFrustum3DStub(UUID identifier) {
		super(identifier);
		
		Point3d near = new Point3d(1., -100, 0.);
		Point3d far = new Point3d(100., 100., 10.);
		this.planes[FRONT] = new Plane4d(new YZPlane(near));
		this.planes[BACK] = new Plane4d(new YZPlane(far));
		this.planes[BACK].negate();
		this.planes[LEFT] = new Plane4d(new XZPlane(far));
		this.planes[LEFT].negate();
		this.planes[RIGHT] = new Plane4d(new XZPlane(near));
		this.planes[TOP] = new Plane4d(new XYPlane(far));
		this.planes[TOP].negate();
		this.planes[BOTTOM] = new Plane4d(new XYPlane(near));
	}

	@Override
	public void setRotation(Quat4d q) {
		//
	}

	@Override
	public double getFarDistance() {
		return 0;
	}

	@Override
	public boolean isInit() {
		return true;
	}

	@Override
	public BoundingPrimitiveType3D getBoundType() {
		return null;
	}
	
}
