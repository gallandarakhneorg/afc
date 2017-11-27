/**
 * 
 */
package fr.utbm.set.jasim.environment.model.place;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import fr.utbm.set.jasim.environment.model.ground.GroundStub;
import fr.utbm.set.jasim.environment.model.world.MobileEntityStub;
import fr.utbm.set.unittest.AbstractTestCase;

import org.arakhne.afc.jasim.environment.model.actions.EnvironmentalActionCollector;
import org.arakhne.afc.jasim.environment.model.dynamics.DynamicsEngine;
import org.arakhne.afc.jasim.environment.model.influences.InfluenceCollector;
import org.arakhne.afc.jasim.environment.model.influences.InfluenceSolver;
import org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription;
import org.arakhne.afc.jasim.environment.model.world.MobileEntity;
import org.arakhne.afc.jasim.environment.model.world.WorldEntity;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultPlaceDescriptionTest extends AbstractTestCase {

	private UUID id;
	private DefaultPlaceDescription<WorldEntity<?>, MobileEntity<?>> tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.id = UUID.randomUUID();
		this.tested = new DefaultPlaceDescription<WorldEntity<?>, MobileEntity<?>>(this.id);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.id = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#getIdentifier()}.
	 */
	public void testGetIdentifier() {
		assertEquals(this.id, this.tested.getIdentifier());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#getGround()}.
	 */
	public void testGetGround() {
		assertNull(this.tested.getGround());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#setGround(org.arakhne.afc.jasim.environment.model.ground.Ground)}.
	 */
	public void testSetGround() {
		assertNull(this.tested.getGround());
		GroundStub stub = new GroundStub();
		this.tested.setGround(stub);
		assertNotNull(this.tested.getGround());
		assertSame(stub,this.tested.getGround());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#getMobileEntities()}.
	 */
	public void testGetMobileEntities() {
		Iterator<?> iterator = this.tested.getMobileEntities();
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#setMobileEntities(java.util.Collection)}.
	 */
	public void testSetMobileEntities() {
		Iterator<?> iterator = this.tested.getMobileEntities();
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());
		
		Collection<MobileEntity<?>> col = null;
		this.tested.setMobileEntities(col);
		iterator = this.tested.getMobileEntities();
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		col = new ArrayList<MobileEntity<?>>();
		this.tested.setMobileEntities(col);
		iterator = this.tested.getMobileEntities();
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		col = new ArrayList<MobileEntity<?>>();
		col.add(new MobileEntityStub());
		this.tested.setMobileEntities(col);
		iterator = this.tested.getMobileEntities();
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertNotNull(iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#getStaticEntities()}.
	 */
	public void testGetStaticEntities() {
		Iterator<?> iterator = this.tested.getStaticEntities();
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#setStaticEntities(java.util.Collection)}.
	 */
	public void testSetStaticEntities() {
		Iterator<?> iterator = this.tested.getStaticEntities();
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());
		
		Collection<MobileEntity<?>> col = null;
		this.tested.setStaticEntities(col);
		iterator = this.tested.getStaticEntities();
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		col = new ArrayList<MobileEntity<?>>();
		this.tested.setStaticEntities(col);
		iterator = this.tested.getStaticEntities();
		assertNotNull(iterator);
		assertFalse(iterator.hasNext());

		col = new ArrayList<MobileEntity<?>>();
		col.add(new MobileEntityStub());
		this.tested.setStaticEntities(col);
		iterator = this.tested.getStaticEntities();
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertNotNull(iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#getWorldModel(java.lang.Class)}.
	 */
	public void testGetWorldModel() {
		assertNull(this.tested.getWorldModel(Object.class));
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#setWorldModel(Class, Object)}.
	 */
	public void testSetWorldModel() {
		assertNull(this.tested.getWorldModel(Object.class));
		Object obj = new Object();
		this.tested.setWorldModel(Object.class, obj);
		assertNotNull(this.tested.getWorldModel(Object.class));
		assertSame(obj, this.tested.getWorldModel(Object.class));
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#getInfluenceSolverType()}.
	 */
	public void testGetInfluenceSolverType() {
		assertNull(this.tested.getInfluenceSolverType());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#setInfluenceSolverType(java.lang.Class)}.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testSetInfluenceSolverType() {
		assertNull(this.tested.getInfluenceSolverType());
		
		Class type = InfluenceSolver.class;
		this.tested.setInfluenceSolverType(type);
		assertSame(type, this.tested.getInfluenceSolverType());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#getDynamicsEngineType()}.
	 */
	public void testGetDynamicsEngineType() {
		assertNull(this.tested.getDynamicsEngineType());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#setDynamicsEngineType(java.lang.Class)}.
	 */
	public void testSetDynamicsEngineType() {
		assertNull(this.tested.getDynamicsEngineType());
		
		Class<? extends DynamicsEngine> type = DynamicsEngine.class;
		this.tested.setDynamicsEngineType(type);
		assertSame(type, this.tested.getDynamicsEngineType());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#getInfluenceCollectorType()}.
	 */
	public void testGetInfluenceCollectorType() {
		assertNull(this.tested.getInfluenceCollectorType());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#setInfluenceCollectorType(java.lang.Class)}.
	 */
	public void testSetInfluenceCollectorType() {
		assertNull(this.tested.getInfluenceCollectorType());
		
		Class<? extends InfluenceCollector> type = InfluenceCollector.class;
		this.tested.setInfluenceCollectorType(type);
		assertSame(type, this.tested.getInfluenceCollectorType());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#getEnvironmentalActionCollectorType()}.
	 */
	public void testGetEnvironmentalActionCollectorType() {
		assertNull(this.tested.getEnvironmentalActionCollectorType());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.model.place.DefaultPlaceDescription#setEnvironmentalActionCollectorType(java.lang.Class)}.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testSetEnvironmentalActionCollectorType() {
		assertNull(this.tested.getEnvironmentalActionCollectorType());
		
		Class type = EnvironmentalActionCollector.class;
		this.tested.setEnvironmentalActionCollectorType(type);
		assertSame(type, this.tested.getEnvironmentalActionCollectorType());
	}

}
