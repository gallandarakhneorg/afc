/**
 * 
 */
package fr.utbm.set.jasim.environment.model.world;

import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Mesh3DTest extends AbstractTestCase {

	private Point3d[] points;
	private Point3d lower, upper;
	private Mesh3D tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.points = randomPoints3D();
		this.tested = new Mesh3D(false, this.points);
		
		double x1, y1, z1;
		double x2, y2, z2;
		x1 = y1 = z1 = Double.POSITIVE_INFINITY;
		x2 = y2 = z2 = Double.NEGATIVE_INFINITY;
		
		for(Point3d p : this.points) {
			if (p.x<x1) x1 = p.x;
			if (p.y<y1) y1 = p.y;
			if (p.z<z1) z1 = p.z;
			if (p.x>x2) x2 = p.x;
			if (p.y>y2) y2 = p.y;
			if (p.z>z2) z2 = p.z;
		}
		
		this.lower = new Point3d(x1,y1,z1);
		this.upper = new Point3d(x2,y2,z2);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.lower = null;
		this.upper = null;
		this.points = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 */
	public void testIsGlobalMesh() {
		assertTrue(this.tested.isGlobalMesh());
	}

	/**
	 */
	public void testIsLocalMesh() {
		assertFalse(this.tested.isLocalMesh());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Mesh3D#getPoints()}.
	 */
	public void testGetPoints() {
		assertEpsilonEquals(this.points, this.tested.getPoints());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Mesh3D#toBounds(java.lang.Class)}.
	 */
	public void testToBounds() {
		AlignedBoundingBox box = this.tested.toBounds(AlignedBoundingBox.class);
		assertNotNull(box);
		assertEpsilonEquals(this.lower, box.getLower());
		assertEpsilonEquals(this.upper, box.getUpper());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Mesh3D#getBounds(fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D, Quat4d, javax.vecmath.Tuple3d)}.
	 */
	public void testGetBoundsBoxQuat4dTuple3d() {
		AlignedBoundingBox box = new AlignedBoundingBox();
		
		this.tested.getBounds(box, new Quat4d(0.,0.,0.,1.), new Vector3d(0.,0.,0.));

		assertEpsilonEquals(this.lower, box.getLower());
		assertEpsilonEquals(this.upper, box.getUpper());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Mesh3D#getBounds(fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D, Quat4d)}.
	 */
	public void testGetBoundsBoxQuat4d() {
		AlignedBoundingBox box = new AlignedBoundingBox();
		
		this.tested.getBounds(box, new Quat4d(0.,0.,0.,1.));

		assertEpsilonEquals(this.lower, box.getLower());
		assertEpsilonEquals(this.upper, box.getUpper());
	}

	/**
	 */
	public void testMakeLocalPoint3d() {
		Point3d p = new Point3d(10.,10.,10.);
		
		this.tested.makeLocal(p);
		
		assertTrue(this.tested.isLocalMesh());
		assertFalse(this.tested.isGlobalMesh());

		Point3d[] pts = new Point3d[this.points.length];
		for(int i=0; i<this.points.length; ++i) {
			pts[i] = new Point3d(this.points[i]);
			pts[i].sub(p);
		}
		
		assertEpsilonEquals(pts, this.tested.getPoints());
	}

	/**
	 */
	public void testMakeGlobalPoint3d() {
		Point3d p = new Point3d(10.,10.,10.);
		this.tested.makeLocal(p);
		assertTrue(this.tested.isLocalMesh());
		assertFalse(this.tested.isGlobalMesh());

		this.tested.makeGlobal(p);
		assertFalse(this.tested.isLocalMesh());
		assertTrue(this.tested.isGlobalMesh());

		assertEpsilonEquals(this.points, this.tested.getPoints());
	}

}
