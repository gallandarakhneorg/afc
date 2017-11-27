/**
 * 
 */
package fr.utbm.set.jasim.environment.model.place;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import fr.utbm.set.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultPortalTest extends AbstractTestCase {
	
	private DefaultPortal<EnvironmentalAction, WorldEntity<?>, MobileEntity<?>> tested;
	private PortalPosition position1, position2;
	private Place<EnvironmentalAction, WorldEntity<?>, MobileEntity<?>> place1;
	private Place<EnvironmentalAction, WorldEntity<?>, MobileEntity<?>> place2;
	private Place<EnvironmentalAction, WorldEntity<?>, MobileEntity<?>> place3;

	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.tested = new DefaultPortal<EnvironmentalAction, WorldEntity<?>, MobileEntity<?>>();
		Point2d p1 = randomPoint2D();
		Point2d p2 = randomPoint2D();
		Point2d p3 = randomPoint2D();
		Vector2d v = randomVector2D();
		this.position1 = new SegmentPortalPosition(
				p1.x, p1.y, p2.x, p2.y, this.RANDOM.nextBoolean(),
				p3.x, p3.y, v.x, v.y);
		p1 = randomPoint2D();
		p2 = randomPoint2D();
		p3 = randomPoint2D();
		v = randomVector2D();
		this.position2 = new SegmentPortalPosition(
				p1.x, p1.y, p2.x, p2.y, this.RANDOM.nextBoolean(),
				p3.x, p3.y, v.x, v.y);
		this.place1 = new PlaceStub();
		this.place2 = new PlaceStub();
		this.place3 = new PlaceStub();
		this.tested.attachPlace(this.place1, this.position1);
		this.tested.attachPlace(this.place2, this.position2);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.tested = null;
		this.place1 = this.place2 = this.place3 = null;
		this.position1 = this.position2 = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.place.DefaultPortal#getFirstPlace()}.
	 */
	public void testGetFirstPlace() {
		assertSame(this.place1, this.tested.getFirstPlace());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.place.DefaultPortal#getSecondPlace()}.
	 */
	public void testGetSecondPlace() {
		assertSame(this.place2, this.tested.getSecondPlace());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.place.DefaultPortal#isPlace(fr.utbm.set.jasim.environment.model.place.Place)}.
	 */
	public void testIsPlace() {
		assertTrue(this.tested.isPlace(this.place1));
		assertTrue(this.tested.isPlace(this.place2));
		assertFalse(this.tested.isPlace(this.place3));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.place.DefaultPortal#getPosition(fr.utbm.set.jasim.environment.model.place.Place)}.
	 */
	public void testGetPosition() {
		assertSame(this.position1, this.tested.getPosition(this.place1));
		assertSame(this.position2, this.tested.getPosition(this.place2));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.place.DefaultPortal#isTraversableFrom(fr.utbm.set.jasim.environment.model.place.Place)}.
	 */
	public void testIsTraversableFrom() {
		assertTrue(this.tested.isTraversableFrom(this.place1));
		assertTrue(this.tested.isTraversableFrom(this.place2));
		try {
			this.tested.isTraversableFrom(this.place3);
			fail("An IllegalArgumentException is expected"); //$NON-NLS-1$
		}
		catch(IllegalArgumentException _) {
			// Expected exception
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.place.DefaultPortal#getOtherSide(fr.utbm.set.jasim.environment.model.place.Place)}.
	 */
	public void testGetOtherSide() {
		assertSame(this.place2, this.tested.getOtherSide(this.place1));
		assertSame(this.place1, this.tested.getOtherSide(this.place2));
	}

}
