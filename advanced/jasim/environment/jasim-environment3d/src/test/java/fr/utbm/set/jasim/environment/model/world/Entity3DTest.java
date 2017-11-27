/**
 * 
 */
package fr.utbm.set.jasim.environment.model.world;

import java.util.Arrays;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

import fr.utbm.set.geom.bounds.bounds2d.Bounds2D;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.Bounds3D;
import fr.utbm.set.jasim.environment.semantics.BodyType;
import fr.utbm.set.jasim.environment.semantics.MobileObjectType;
import fr.utbm.set.jasim.environment.semantics.ObjectType;
import fr.utbm.set.jasim.environment.semantics.PedestrianType;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Entity3DTest extends AbstractTestCase {

	private AlignedBoundingBox box;
	private Entity3D<AlignedBoundingBox> tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.box = new AlignedBoundingBox();
		this.box.combine(randomPoints3D());
		this.tested = new Entity3D<AlignedBoundingBox>(
				this.box, ObjectType.OBJECTTYPE_SINGLETON, true);
		this.tested.addSemantic(PedestrianType.PEDESTRIANTYPE_SINGLETON);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.box = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getBounds()}.
	 */
	public void testGetBounds() {
		assertSame(this.box, this.tested.getBounds());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getType()}.
	 */
	public void testGetType() {
		assertSame(ObjectType.OBJECTTYPE_SINGLETON, this.tested.getType());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getAllSemantics()}.
	 */
	public void testGetAllSemantics() {
		assertEpsilonEquals(
				Arrays.asList(
						ObjectType.OBJECTTYPE_SINGLETON,
						PedestrianType.PEDESTRIANTYPE_SINGLETON),
				this.tested.getAllSemantics());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#isA(fr.utbm.set.jasim.environment.semantics.Semantic)}.
	 */
	public void testIsA() {
		assertTrue(this.tested.isA(ObjectType.OBJECTTYPE_SINGLETON));
		assertTrue(this.tested.isA(BodyType.BODYTYPE_SINGLETON));
		assertTrue(this.tested.isA(PedestrianType.PEDESTRIANTYPE_SINGLETON));
		assertFalse(this.tested.isA(MobileObjectType.MOBILEOBJECTTYPE_SINGLETON));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#addSemantic(fr.utbm.set.jasim.environment.semantics.Semantic...)}.
	 */
	public void testAddSemantic() {
		this.tested.addSemantic(MobileObjectType.MOBILEOBJECTTYPE_SINGLETON);
		assertTrue(this.tested.isA(MobileObjectType.MOBILEOBJECTTYPE_SINGLETON));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#removeSemantic(fr.utbm.set.jasim.environment.semantics.Semantic)}.
	 */
	public void testRemoveSemantic() {
		assertTrue(this.tested.isA(PedestrianType.PEDESTRIANTYPE_SINGLETON));
		this.tested.removeSemantic(PedestrianType.PEDESTRIANTYPE_SINGLETON);
		assertFalse(this.tested.isA(PedestrianType.PEDESTRIANTYPE_SINGLETON));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#isInfluencable()}.
	 */
	public void testIsInfluencable() {
		assertFalse(this.tested.isInfluencable());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#toInfluencable()}.
	 */
	public void testToInfluencable() {
		try {
			this.tested.toInfluencable();
			fail("exception expected: UnsupportedOperationException"); //$NON-NLS-1$
		}
		catch(UnsupportedOperationException _) {
			// expected case
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getPosition3D()}.
	 */
	public void testGetPosition3D() {
		Point3d p = this.box.getPosition();
		p.z = this.box.getMinZ();
		assertEpsilonEquals(p, this.tested.getPosition3D());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getPosition2D5()}.
	 */
	public void testGetPosition2D5() {
		Point3d p = this.box.getPosition();
		p.z = this.box.getMinZ();
		assertEpsilonEquals(p, this.tested.getPosition2D5());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getPosition2D()}.
	 */
	public void testGetPosition2D() {
		Point3d p = this.box.getPosition();
		assertEpsilonEquals(new Point2d(p.x,p.y), this.tested.getPosition2D());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getPosition1D5()}.
	 */
	public void testGetPosition1D5() {
		try {
			this.tested.getPosition1D5();
			fail("expecting UnsupportedOperationException exception"); //$NON-NLS-1$
		}
		catch(Throwable _) {
			// Excepted case
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getPosition1D()}.
	 */
	public void testGetPosition1D() {
		try {
			this.tested.getPosition1D();
			fail("expecting UnsupportedOperationException exception"); //$NON-NLS-1$
		}
		catch(Throwable _) {
			// Excepted case
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getTranslation()}.
	 */
	public void testGetTranslation() {
		Point3d p = this.box.getPosition();
		p.z = this.box.getMinZ();
		assertEpsilonEquals(p, this.tested.getTranslation());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#isOnGround()}.
	 */
	public void testIsOnGround() {
		assertTrue(this.tested.isOnGround());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getBounds1D()}.
	 */
	public void testGetBounds1D() {
		try {
			this.tested.getBounds1D();
			fail("expecting UnsupportedOperationException exception"); //$NON-NLS-1$
		}
		catch(Throwable _) {
			// Excepted case
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getBounds1D5()}.
	 */
	public void testGetBounds1D5() {
		try {
			this.tested.getBounds1D5();
			fail("expecting UnsupportedOperationException exception"); //$NON-NLS-1$
		}
		catch(Throwable _) {
			// Excepted case
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getBounds2D()}.
	 */
	public void testGetBounds2D() {
		Bounds2D expected = this.box.toBounds2D();
		Bounds2D actual = this.tested.getBounds2D();
		assertEpsilonEquals(expected.getLower(), actual.getLower());
		assertEpsilonEquals(expected.getUpper(), actual.getUpper());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getBounds2D5()}.
	 */
	public void testGetBounds2D5() {
		Bounds3D actual = this.tested.getBounds2D5();
		assertEpsilonEquals(this.box.getLower(), actual.getLower());
		assertEpsilonEquals(this.box.getUpper(), actual.getUpper());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.Entity3D#getBounds3D()}.
	 */
	public void testGetBounds3D() {
		Bounds3D actual = this.tested.getBounds3D();
		assertEpsilonEquals(this.box.getLower(), actual.getLower());
		assertEpsilonEquals(this.box.getUpper(), actual.getUpper());
	}

}
