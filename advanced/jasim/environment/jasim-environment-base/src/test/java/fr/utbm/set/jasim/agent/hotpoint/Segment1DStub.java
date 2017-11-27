package fr.utbm.set.jasim.agent.hotpoint;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.object.Segment1D;
import fr.utbm.set.geom.system.CoordinateSystem2D;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Segment1DStub implements Segment1D {

	/**
	 * First point.
	 */
	public final Point2d p1;

	/**
	 * Second point.
	 */
	public final Point2d p2;
	
	/**
	 * @param p1
	 * @param p2
	 */
	public Segment1DStub(Point2d p1, Point2d p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	@Override
	public Point2d getFirstPoint() {
		return this.p1;
	}

	@Override
	public Point2d getLastPoint() {
		return this.p2;
	}

	@Override
	public double getLength() {
		return this.p1.distance(this.p2);
	}

	@Override
	public boolean isFirstPointConnectedTo(Segment1D otherSegment) {
		return GeometryUtil.epsilonEquals(this.p1, otherSegment.getFirstPoint())
				||
				GeometryUtil.epsilonEquals(this.p1, otherSegment.getLastPoint());
	}

	@Override
	public boolean isLastPointConnectedTo(Segment1D otherSegment) {
		return GeometryUtil.epsilonEquals(this.p2, otherSegment.getFirstPoint())
				||
				GeometryUtil.epsilonEquals(this.p2, otherSegment.getLastPoint());
	}

	@Override
	public Vector2d getTangentAt(double positionOnSegment) {
		Vector2d v = new Vector2d();
		v.sub(this.p2,this.p1);
		v.normalize();
		return v;
	}

	@Override
	public void projectsOnPlane(double positionOnSegment, Point2d position,
			Vector2d tangent, CoordinateSystem2D system) {
		Vector2d v = new Vector2d();
		v.sub(this.p2,this.p1);
		v.normalize();
		
		if (position!=null) {
			position.set(this.p1);
			v.scale(positionOnSegment);
			position.add(v);
			v.normalize();
		}
		
		if (tangent!=null) {
			tangent.set(v);
		}
	}

	@Override
	public void projectsOnPlane(double positionOnSegment, double shiftDistance,
			Point2d position, Vector2d tangent, CoordinateSystem2D system) {
		Vector2d v = new Vector2d();
		v.sub(this.p2,this.p1);
		v.normalize();
		
		if (position!=null) {
			position.set(this.p1);
			v.scale(positionOnSegment);
			position.add(v);
			v.normalize();
			
			Vector2d perp = GeometryUtil.perpendicularVector(v, system);
			perp.scale(shiftDistance);
			position.add(perp);
		}
		
		v.normalize();
		if (tangent!=null) {
			tangent.set(v);
		}
	}

}
