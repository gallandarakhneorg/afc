/**
 * 
 */
package fr.utbm.set.jasim.environment.model.place;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.system.CoordinateSystem2D;
import fr.utbm.set.unittest.AbstractTestCase;

import org.arakhne.afc.jasim.environment.model.place.SegmentPortalPosition;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SegmentPortalPositionTest extends AbstractTestCase {

	private double sx1, sy1, sz1, sx2, sy2, sz2;
	private double tx1, ty1, tz1, tx2, ty2, tz2;
	private boolean onLeft;
	private SegmentPortalPosition tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.sx1 = this.RANDOM.nextDouble();
		this.sy1 = this.RANDOM.nextDouble();
		this.sz1 = this.RANDOM.nextDouble();
		this.sx2 = this.RANDOM.nextDouble();
		this.sy2 = this.RANDOM.nextDouble();
		this.sz2 = this.RANDOM.nextDouble();
		this.tx1 = this.RANDOM.nextDouble();
		this.ty1 = this.RANDOM.nextDouble();
		this.tz1 = this.RANDOM.nextDouble();
		this.tx2 = this.RANDOM.nextDouble();
		this.ty2 = this.RANDOM.nextDouble();
		this.tz2 = this.RANDOM.nextDouble();
		this.onLeft = this.RANDOM.nextBoolean();
		this.tested = new SegmentPortalPosition(
				this.sx1, this.sy1, this.sz1, this.sx2, this.sy2, this.sz2,
				this.onLeft,
				this.tx1, this.ty1, this.tz1, this.tx2, this.ty2, this.tz2);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.SegmentPortalPosition#getExitPoint2D()}.
	 */
	public void testGetExitPoint2D() {
		assertEpsilonEquals(
				new Point2d(this.tx1, this.ty1),
				this.tested.getExitPoint2D());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.SegmentPortalPosition#getExitPoint3D()}.
	 */
	public void testGetExitPoint3D() {
		assertEpsilonEquals(
				new Point3d(this.tx1, this.ty1, this.tz1),
				this.tested.getExitPoint3D());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.SegmentPortalPosition#getExitVector2D()}.
	 */
	public void testGetExitVector2D() {
		assertEpsilonEquals(
				new Vector2d(this.tx2, this.ty2),
				this.tested.getExitVector2D());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.SegmentPortalPosition#getExitVector3D()}.
	 */
	public void testGetExitVector3D() {
		assertEpsilonEquals(
				new Vector3d(this.tx2, this.ty2, this.tz2),
				this.tested.getExitVector3D());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.SegmentPortalPosition#getEntryPoint2D()}.
	 */
	public void testGetEntryPoint2D() {
		assertEpsilonEquals(
				new Point2d(
						(this.sx1+this.sx2)/2.,
						(this.sy1+this.sy2)/2.),
				this.tested.getEntryPoint2D());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.SegmentPortalPosition#getEntryPoint3D()}.
	 */
	public void testGetEntryPoint3D() {
		assertEpsilonEquals(
				new Point3d(
						(this.sx1+this.sx2)/2.,
						(this.sy1+this.sy2)/2.,
						(this.sz1+this.sz2)/2.),
				this.tested.getEntryPoint3D());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.SegmentPortalPosition#getEntryVector2D()}.
	 */
	public void testGetEntryVector2D() {
		Vector2d v = new Vector2d(this.sx2-this.sx1, this.sy2-this.sy1);
		CoordinateSystem2D cs = CoordinateSystem2D.getDefaultCoordinateSystem();
		v = GeometryUtil.perpendicularVector(v,cs);
		if (this.onLeft!=cs.isRightHanded()) v.negate(); 
		assertEpsilonEquals(
				v,
				this.tested.getEntryVector2D());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.SegmentPortalPosition#getEntryVector3D()}.
	 */
	public void testGetEntryVector3D() {
		Vector2d v = new Vector2d(this.sx2-this.sx1, this.sy2-this.sy1);
		CoordinateSystem2D cs = CoordinateSystem2D.getDefaultCoordinateSystem();
		v = GeometryUtil.perpendicularVector(v,cs);
		if (this.onLeft!=cs.isRightHanded()) v.negate(); 
		assertEpsilonEquals(
				new Vector3d(v.x,v.y,0.),
				this.tested.getEntryVector3D());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.SegmentPortalPosition#isTraversable(double, double, double, double)}.
	 */
	public void testIsTraversableDoubleDoubleDoubleDouble() {
		assertFalse(this.tested.isTraversable(this.sx1+1.,this.sy1+1., this.sx2+1., this.sy2+1.)); 

		Vector2d v = new Vector2d(this.sx2-this.sx1, this.sy2-this.sy1);
		CoordinateSystem2D cs = CoordinateSystem2D.getDefaultCoordinateSystem();
		v = GeometryUtil.perpendicularVector(v,cs);
		if (this.onLeft!=cs.isRightHanded()) v.negate(); 
		
		// v is the normal to the portal
		
		double cx = (this.sx1+this.sx2)/2.;
		double cy = (this.sy1+this.sy2)/2.;

		Point2d p = new Point2d(cx,cy); // p is the portal center
		p.scaleAdd(10., v, p);  // p is far on the front of the portal
		
		assertFalse(this.tested.isTraversable(p.x,p.y,p.x+v.x,p.y+v.y)); 
		assertFalse(this.tested.isTraversable(p.x+v.x,p.y+v.x,p.x,p.y)); 
	}
	
	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.SegmentPortalPosition#isTraversable(double, double, double, double, double, double)}.
	 */
	public void testIsTraversableDoubleDoubleDoubleDoubleDoubleDouble() {
		assertFalse(this.tested.isTraversable(this.sx1+1.,this.sy1+1., this.sz1, this.sx2+1., this.sy2+1., this.sz2)); 

		Vector2d v = new Vector2d(this.sx2-this.sx1, this.sy2-this.sy1);
		CoordinateSystem2D cs = CoordinateSystem2D.getDefaultCoordinateSystem();
		v = GeometryUtil.perpendicularVector(v,cs);
		if (this.onLeft!=cs.isRightHanded()) v.negate(); 
		
		double cx = (this.sx1+this.sx2)/2.;
		double cy = (this.sy1+this.sy2)/2.;

		Point2d p = new Point2d(cx,cy);
		p.scaleAdd(10., v, p);
		
		assertFalse(this.tested.isTraversable(p.x,p.y,this.sz1,p.x+v.x,p.y+v.y,this.sz2)); 
		assertFalse(this.tested.isTraversable(p.x+v.x,this.sz1,p.y+v.x,p.x,p.y,this.sz2)); 
	}

}
