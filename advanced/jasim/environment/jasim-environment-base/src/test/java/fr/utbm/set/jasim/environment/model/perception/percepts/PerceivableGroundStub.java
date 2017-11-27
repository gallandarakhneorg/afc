/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.percepts;

import java.util.UUID;

import javax.vecmath.Vector2d;

import fr.utbm.set.jasim.environment.model.ground.PerceivableGround;
import fr.utbm.set.jasim.environment.semantics.GroundType;
import fr.utbm.set.jasim.environment.semantics.NotTraversableGroundType;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PerceivableGroundStub implements PerceivableGround {

	/**
	 */
	public PerceivableGroundStub() {
		//
	}
	
	@Override
	public Vector2d getAttraction(double x, double y) {
		return new Vector2d(1.,0.);
	}
	
	@Override
	public double getMaxForceWeight() {
		return 0;
	}
	
	@Override
	public Vector2d getRepulsion(double x, double y) {
		return new Vector2d(0.,1.);
	}
	
	@Override
	public GroundType getGroundType(double x, double y) {
		return NotTraversableGroundType.NOTTRAVERSABLEGROUNDTYPE_SINGLETON;
	}

	@Override
	public double getHeightAt(double x, double y) {
		return 0;
	}
	@Override
	public UUID getIdentifier() {
		return UUID.randomUUID();
	}
	@Override
	public double getMaxX() {
		return 0;
	}
	@Override
	public double getMaxY() {
		return 0;
	}
	@Override
	public double getMaxZ() {
		return 0;
	}
	@Override
	public double getMinX() {
		return 0;
	}
	@Override
	public double getMinY() {
		return 0;
	}
	@Override
	public double getMinZ() {
		return 0;
	}
	@Override
	public double getSizeX() {
		return 0;
	}
	@Override
	public double getSizeY() {
		return 0;
	}
	@Override
	public double getSizeZ() {
		return 0;
	}
	@Override
	public boolean isTraversable(double x, double y) {
		return false;
	}
	
}
