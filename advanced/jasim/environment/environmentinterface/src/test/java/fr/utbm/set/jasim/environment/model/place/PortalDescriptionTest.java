/**
 * 
 */
package fr.utbm.set.jasim.environment.model.place;

import java.util.UUID;

import fr.utbm.set.unittest.AbstractTestCase;

import org.arakhne.afc.jasim.environment.model.place.PortalDescription;
import org.arakhne.afc.jasim.environment.model.place.SegmentPortalPosition;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PortalDescriptionTest extends AbstractTestCase {

	private UUID id1, id2;
	private PortalDescription tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.id1 = UUID.randomUUID();
		this.id2 = UUID.randomUUID();
		this.tested = new PortalDescription(this.id1, this.id2);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.id1 = this.id2 = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.PortalDescription#getFirstPlaceIdentifier()}.
	 */
	public void testGetFirstPlaceIdentifier() {
		assertEquals(this.id1, this.tested.getFirstPlaceIdentifier());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.PortalDescription#getSecondPlaceIdentifier()}.
	 */
	public void testGetSecondPlaceIdentifier() {
		assertEquals(this.id2, this.tested.getSecondPlaceIdentifier());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.PortalDescription#setPositionOnFirstPlace(org.arakhne.afc.jasim.environment.model.place.PortalPosition)}.
	 */
	public void testSetPositionOnFirstPlace() {
		assertNull(this.tested.getPositionOnFirstPlace());
		SegmentPortalPosition pos = new SegmentPortalPosition(0,0,0,0,0,0,false,0,0,0,0,0,0);
		this.tested.setPositionOnFirstPlace(pos);
		assertSame(pos, this.tested.getPositionOnFirstPlace());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.PortalDescription#setPositionOnSecondPlace(org.arakhne.afc.jasim.environment.model.place.PortalPosition)}.
	 */
	public void testSetPositionOnSecondPlace() {
		assertNull(this.tested.getPositionOnSecondPlace());
		SegmentPortalPosition pos = new SegmentPortalPosition(0,0,0,0,0,0,false,0,0,0,0,0,0);
		this.tested.setPositionOnSecondPlace(pos);
		assertSame(pos, this.tested.getPositionOnSecondPlace());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.PortalDescription#getPositionOnFirstPlace()}.
	 */
	public void testGetPositionOnFirstPlace() {
		assertNull(this.tested.getPositionOnFirstPlace());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.PortalDescription#getPositionOnSecondPlace()}.
	 */
	public void testGetPositionOnSecondPlace() {
		assertNull(this.tested.getPositionOnFirstPlace());
	}

}
