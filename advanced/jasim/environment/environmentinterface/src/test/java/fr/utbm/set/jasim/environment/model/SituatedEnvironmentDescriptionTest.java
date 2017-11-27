/**
 * 
 */
package fr.utbm.set.jasim.environment.model;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import fr.utbm.set.unittest.AbstractTestCase;

import org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription;
import org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription;
import org.arakhne.afc.jasim.environment.model.place.PlaceDescription;
import org.arakhne.afc.jasim.environment.model.place.PortalDescription;
import org.arakhne.afc.jasim.environment.model.world.MobileEntity;
import org.arakhne.afc.jasim.environment.model.world.WorldEntity;
import org.arakhne.afc.jasim.environment.time.ConstantTimeManager;
import org.arakhne.afc.jasim.environment.time.TimeManager;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SituatedEnvironmentDescriptionTest extends AbstractTestCase {

	private UUID id;
	private TimeManager timeManager;
	private SituatedEnvironmentDescription<WorldEntity<?>,MobileEntity<?>> tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.id = UUID.randomUUID();
		this.timeManager = new ConstantTimeManager();
		this.tested = new SituatedEnvironmentDescription<WorldEntity<?>,MobileEntity<?>>(
				this.id, this.timeManager);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.id = null;
		this.timeManager = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#getIdentifier()}.
	 */
	public void testGetIdentifier() {
		assertEquals(this.id, this.tested.getIdentifier());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#getName()}.
	 */
	public void testGetName() {
		assertNull(this.tested.getName());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#setName(java.lang.String)}.
	 */
	public void testSetName() {
		assertNull(this.tested.getName());
		String n = randomString();
		this.tested.setName(n);
		assertSame(n, this.tested.getName());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#getDate()}.
	 */
	public void testGetDate() {
		assertNull(this.tested.getDate());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#setDate(java.util.Date)}.
	 */
	public void testSetDate() {
		assertNull(this.tested.getDate());
		Date dt = new Date(this.RANDOM.nextLong());
		this.tested.setDate(dt);
		assertSame(dt, this.tested.getDate());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#getAuthors()}.
	 */
	public void testGetAuthors() {
		assertNull(this.tested.getAuthors());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#setAuthors(java.lang.String)}.
	 */
	public void testSetAuthors() {
		assertNull(this.tested.getAuthors());
		String auths = randomString();
		this.tested.setAuthors(auths);
		assertSame(auths, this.tested.getAuthors());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#getVersion()}.
	 */
	public void testGetVersion() {
		assertNull(this.tested.getVersion());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#setVersion(java.lang.String)}.
	 */
	public void testSetVersion() {
		assertNull(this.tested.getVersion());
		String v = randomString();
		this.tested.setVersion(v);
		assertSame(v, this.tested.getVersion());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#getDescription()}.
	 */
	public void testGetDescription() {
		assertNull(this.tested.getDescription());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#setDescription(java.lang.String)}.
	 */
	public void testSetDescription() {
		assertNull(this.tested.getDescription());
		String d = randomString();
		this.tested.setDescription(d);
		assertSame(d, this.tested.getDescription());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#getTimeManager()}.
	 */
	public void testGetTimeManager() {
		assertSame(this.timeManager, this.tested.getTimeManager());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#addPlace(org.arakhne.afc.jasim.environment.model.place.PlaceDescription)}.
	 */
	public void testAddPlace() {
		assertEquals(0, this.tested.getPlaceCount());
		UUID i = UUID.randomUUID();
		PlaceDescription<WorldEntity<?>, MobileEntity<?>> desc = new DefaultPlaceDescription<WorldEntity<?>, MobileEntity<?>>(i);
		this.tested.addPlace(desc);
		assertEquals(1, this.tested.getPlaceCount());
		i = UUID.randomUUID();
		PlaceDescription<WorldEntity<?>, MobileEntity<?>> desc2 = new DefaultPlaceDescription<WorldEntity<?>, MobileEntity<?>>(i);
		this.tested.addPlace(desc2);
		assertEquals(2, this.tested.getPlaceCount());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#removePlace(org.arakhne.afc.jasim.environment.model.place.PlaceDescription)}.
	 */
	public void testRemovePlace() {
		UUID i = UUID.randomUUID();
		PlaceDescription<WorldEntity<?>, MobileEntity<?>> desc = new DefaultPlaceDescription<WorldEntity<?>, MobileEntity<?>>(i);
		this.tested.addPlace(desc);
		i = UUID.randomUUID();
		PlaceDescription<WorldEntity<?>, MobileEntity<?>> desc2 = new DefaultPlaceDescription<WorldEntity<?>, MobileEntity<?>>(i);
		this.tested.addPlace(desc2);
		i = UUID.randomUUID();
		PlaceDescription<WorldEntity<?>, MobileEntity<?>> desc3 = new DefaultPlaceDescription<WorldEntity<?>, MobileEntity<?>>(i);
		
		assertEquals(2, this.tested.getPlaceCount());
		this.tested.removePlace(desc);
		assertEquals(1, this.tested.getPlaceCount());
		this.tested.removePlace(desc3);
		assertEquals(1, this.tested.getPlaceCount());
		this.tested.removePlace(desc);
		assertEquals(1, this.tested.getPlaceCount());
		this.tested.removePlace(desc2);
		assertEquals(0, this.tested.getPlaceCount());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#getPlaces()}.
	 */
	public void testGetPlaces() {
		UUID i = UUID.randomUUID();
		PlaceDescription<WorldEntity<?>, MobileEntity<?>> desc = new DefaultPlaceDescription<WorldEntity<?>, MobileEntity<?>>(i);
		this.tested.addPlace(desc);
		i = UUID.randomUUID();
		PlaceDescription<WorldEntity<?>, MobileEntity<?>> desc2 = new DefaultPlaceDescription<WorldEntity<?>, MobileEntity<?>>(i);
		this.tested.addPlace(desc2);
		
		Iterable<PlaceDescription<WorldEntity<?>,MobileEntity<?>>> iter = this.tested.getPlaces();
		Iterator<PlaceDescription<WorldEntity<?>,MobileEntity<?>>> iterator = iter.iterator();
		assertTrue(iterator.hasNext());
		assertSame(desc, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(desc2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#getPlaceCount()}.
	 */
	public void testGetPlaceCount() {
		assertEquals(0, this.tested.getPlaceCount());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#addPortal(org.arakhne.afc.jasim.environment.model.place.PortalDescription)}.
	 */
	public void testAddPortal() {
		assertEquals(0, this.tested.getPlaceCount());
		UUID i1 = UUID.randomUUID();
		UUID i2 = UUID.randomUUID();
		PortalDescription desc = new PortalDescription(i1,i2);
		this.tested.addPortal(desc);
		assertEquals(1, this.tested.getPortalCount());
		i1 = UUID.randomUUID();
		i2 = UUID.randomUUID();
		PortalDescription desc2 = new PortalDescription(i1,i2);
		this.tested.addPortal(desc2);
		assertEquals(2, this.tested.getPortalCount());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#removePortal(org.arakhne.afc.jasim.environment.model.place.PortalDescription)}.
	 */
	public void testRemovePortal() {
		UUID i1 = UUID.randomUUID();
		UUID i2 = UUID.randomUUID();
		PortalDescription desc = new PortalDescription(i1,i2);
		this.tested.addPortal(desc);
		i1 = UUID.randomUUID();
		i2 = UUID.randomUUID();
		PortalDescription desc2 = new PortalDescription(i1,i2);
		this.tested.addPortal(desc2);
		i1 = UUID.randomUUID();
		i2 = UUID.randomUUID();
		PortalDescription desc3 = new PortalDescription(i1,i2);

		assertEquals(2, this.tested.getPortalCount());
		this.tested.removePortal(desc);
		assertEquals(1, this.tested.getPortalCount());
		this.tested.removePortal(desc3);
		assertEquals(1, this.tested.getPortalCount());
		this.tested.removePortal(desc);
		assertEquals(1, this.tested.getPortalCount());
		this.tested.removePortal(desc2);
		assertEquals(0, this.tested.getPortalCount());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#getPortals()}.
	 */
	public void testGetPortals() {
		UUID i1 = UUID.randomUUID();
		UUID i2 = UUID.randomUUID();
		PortalDescription desc = new PortalDescription(i1,i2);
		this.tested.addPortal(desc);
		i1 = UUID.randomUUID();
		i2 = UUID.randomUUID();
		PortalDescription desc2 = new PortalDescription(i1,i2);
		this.tested.addPortal(desc2);

		Iterable<PortalDescription> iter = this.tested.getPortals();
		Iterator<PortalDescription> iterator = iter.iterator();
		assertTrue(iterator.hasNext());
		assertSame(desc, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(desc2, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.SituatedEnvironmentDescription#getPortalCount()}.
	 */
	public void testGetPortalCount() {
		assertEquals(0, this.tested.getPortalCount());
	}

}
