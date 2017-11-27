/**
 * 
 */
package fr.utbm.set.jasim.environment.interfaces.body.influences;

import java.util.UUID;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;

import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influencable;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
final class InfluencableStub implements Influencable {
	/**
	 */
	public InfluencableStub() {
		//
	}
	@Override
	public UUID getIdentifier() {
		return null;
	}
	@Override
	public Point1D getPosition1D() {
		return null;
	}
	@Override
	public Point1D5 getPosition1D5() {
		return null;
	}
	@Override
	public Point2d getPosition2D() {
		return null;
	}
	@Override
	public Point3d getPosition2D5() {
		return null;
	}
	@Override
	public Point3d getPosition3D() {
		return null;
	}
}		
