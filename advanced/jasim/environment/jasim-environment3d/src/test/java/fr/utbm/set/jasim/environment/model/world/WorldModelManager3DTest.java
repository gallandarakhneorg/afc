/**
 * 
 */
package fr.utbm.set.jasim.environment.model.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence3D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception3D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceptions;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment3D;
import fr.utbm.set.jasim.environment.model.perception.algorithm.PerceptionAlgorithm;
import fr.utbm.set.jasim.environment.model.perception.algorithm.SequencialTopDownPerceptionAlgorithm;
import fr.utbm.set.jasim.environment.model.perception.frustum.SphericalFrustum;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.BoundCenterPartitionPolicy;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree.BspTreeManipulator3D;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree.DynamicBspTree3D;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree.DynamicBspTreeNode3D;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree.StaticBspTree3D;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree.StaticBspTreeNode3D;
import fr.utbm.set.jasim.environment.model.place.Place3D;
import fr.utbm.set.jasim.environment.semantics.ObjectType;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class WorldModelManager3DTest extends AbstractTestCase {

	private WorldModelManager3D<AlignedBoundingBox,AlignedBoundingBox> tested;
	private UUID placeId;
	private SituatedEnvironment3D<AlignedBoundingBox,AlignedBoundingBox> environment;
	private Place3D<AlignedBoundingBox,AlignedBoundingBox> place;
	private DynamicBspTree3D<AlignedBoundingBox> dTree;
	private StaticBspTree3D<AlignedBoundingBox> sTree;
	private List<Entity3D<AlignedBoundingBox>> staticEntities;
	private List<MobileEntity3D<AlignedBoundingBox>> mobileEntities;
	private AgentBody3D<Influence3D,Perception3D,AlignedBoundingBox> agentBody;
	private SphericalFrustum frustum;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.placeId = UUID.randomUUID();
		this.environment = new SituatedEnvironment3D<AlignedBoundingBox, AlignedBoundingBox>(AlignedBoundingBox.class);
		PerceptionAlgorithm<AlignedBoundingBox,AlignedBoundingBox> perceptionAlgorithm = 
			new SequencialTopDownPerceptionAlgorithm<AlignedBoundingBox, AlignedBoundingBox>();
		this.place = new Place3D<AlignedBoundingBox,AlignedBoundingBox>(
				this.environment, this.placeId, AlignedBoundingBox.class,
				perceptionAlgorithm);		
		this.tested = new WorldModelManager3D<AlignedBoundingBox,AlignedBoundingBox>(perceptionAlgorithm);
		
		BspTreeManipulator3D<AlignedBoundingBox> manipulator = new BspTreeManipulator3D<AlignedBoundingBox>(
				5, BoundCenterPartitionPolicy.SINGLETON,
				new AlignedBoundingBox(-100,-100,-100,100,100,100));
		this.dTree = new DynamicBspTree3D<AlignedBoundingBox>(manipulator);
		this.dTree.setRoot(new DynamicBspTreeNode3D<AlignedBoundingBox>(this.dTree, 0., 0));
		this.sTree = new StaticBspTree3D<AlignedBoundingBox>();
		this.sTree.setRoot(new StaticBspTreeNode3D<AlignedBoundingBox>(this.sTree, 0., 0));
		
		this.staticEntities = new ArrayList<Entity3D<AlignedBoundingBox>>();
		int count = this.RANDOM.nextInt(20)+5;
		Entity3D<AlignedBoundingBox> se;
		for(int i=0; i<count; ++i) {
			AlignedBoundingBox box = new AlignedBoundingBox();
			se = new Entity3DStub(i,box);
			this.staticEntities.add(se);
		}
		this.sTree.getRoot().addUserData(this.staticEntities);

		this.mobileEntities = new ArrayList<MobileEntity3D<AlignedBoundingBox>>();
		count = this.RANDOM.nextInt(20)+5;
		MobileEntity3D<AlignedBoundingBox> me;
		for(int i=0; i<count; ++i) {
			Mesh3D mesh = new Mesh3D(false, randomPoints3D());
			AlignedBoundingBox box = new AlignedBoundingBox();
			box.set(mesh.getPoints());
			me = new MobileEntity3DStub(i, box, mesh);
			this.mobileEntities.add(me);
		}
		this.dTree.getRoot().addUserData(this.mobileEntities);
		
		Mesh3D mesh = new Mesh3D(false, randomPoints3D());
		AlignedBoundingBox box = new AlignedBoundingBox();
		box.set(mesh.getPoints());		
		this.agentBody = new AgentBody3DStub<Influence3D, Perception3D, AlignedBoundingBox>(
				UUID.randomUUID(), Perception3D.class, box, mesh);
		this.frustum = new SphericalFrustum(UUID.randomUUID(), this.agentBody.getPosition3D(), 1000.);
		this.agentBody.setFrustums(this.frustum);
		this.mobileEntities.add(this.agentBody);
		this.dTree.getRoot().addUserData(this.agentBody);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.sTree = null;
		this.dTree = null;
		this.place = null;
		this.placeId = null;
		this.environment = null;
		this.staticEntities = null;
		this.mobileEntities = null;
		this.agentBody = null;
		this.frustum = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.WorldModelManager3D#init(fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree, fr.utbm.set.jasim.environment.model.perception.tree.StaticPerceptionTree, fr.utbm.set.jasim.environment.model.place.Place3D)}.
	 */
	public void testInit() {
		this.tested.init(this.dTree, this.sTree, this.place);
		assertSame(this.dTree, this.tested.getInnerWorldModel(DynamicBspTree3D.class));
		assertSame(this.sTree, this.tested.getInnerWorldModel(StaticBspTree3D.class));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.WorldModelManager3D#destroy()}.
	 */
	public void testDestroy() {
		this.tested.init(this.dTree, this.sTree, this.place);
		this.tested.destroy();
		assertNull(this.tested.getInnerWorldModel(DynamicBspTree3D.class));
		assertNull(this.tested.getInnerWorldModel(StaticBspTree3D.class));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.WorldModelManager3D#getMobileEntities()}.
	 */
	public void testGetMobileEntities() {
		this.tested.init(this.dTree, this.sTree, this.place);
		Iterator<? extends MobileEntity3D<AlignedBoundingBox>> iterator = this.tested.getMobileEntities();
		while (iterator.hasNext()) {
			assertTrue(this.mobileEntities.remove(iterator.next()));
		}
		assertTrue(this.mobileEntities.isEmpty());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.WorldModelManager3D#getStaticEntities()}.
	 */
	public void testGetStaticEntities() {
		this.tested.init(this.dTree, this.sTree, this.place);
		Iterator<? extends Entity3D<AlignedBoundingBox>> iterator = this.tested.getStaticEntities();
		Entity3D<AlignedBoundingBox> entity;
		while (iterator.hasNext()) {
			entity = iterator.next();
			assertTrue(this.staticEntities.remove(entity));
		}
		assertTrue(this.staticEntities.isEmpty());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.WorldModelManager3D#registerMobileEntity(fr.utbm.set.jasim.environment.model.world.MobileEntity3D)}.
	 */
	public void testRegisterMobileEntity() {
		MobileEntity3D<AlignedBoundingBox> e;
		
		this.tested.init(this.dTree, this.sTree, this.place);

		Iterator<? extends MobileEntity3D<AlignedBoundingBox>> iterator = this.tested.getMobileEntities();
		int count = 0;
		while (iterator.hasNext()) {
			iterator.next();
			count ++;
		}
		assertEquals(this.mobileEntities.size(), count);

		Mesh3D mesh = new Mesh3D(false, randomPoints3D());
		AlignedBoundingBox box = new AlignedBoundingBox();
		box.set(mesh.getPoints());
		MobileEntity3D<AlignedBoundingBox> entity = new MobileEntity3D<AlignedBoundingBox>(
				box, ObjectType.OBJECTTYPE_SINGLETON, true, mesh);
		
		this.tested.registerMobileEntity(entity);
		this.tested.commit();
		
		iterator = this.tested.getMobileEntities();
		count = 0;
		boolean found = false;
		while (iterator.hasNext()) {
			e = iterator.next();
			if (e==entity) found = true;
			count ++;
		}
		assertTrue(found);
		assertEquals(this.mobileEntities.size()+1, count);
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.WorldModelManager3D#unregisterMobileEntity(fr.utbm.set.jasim.environment.model.world.MobileEntity3D)}.
	 */
	public void testUnregisterMobileEntity() {
		MobileEntity3D<AlignedBoundingBox> e, entity;
		
		this.tested.init(this.dTree, this.sTree, this.place);

		entity = this.mobileEntities.get(0);
		Iterator<? extends MobileEntity3D<AlignedBoundingBox>> iterator = this.tested.getMobileEntities();
		int count = 0;
		boolean found = false;
		while (iterator.hasNext()) {
			e = iterator.next();
			if (e==entity) found = true;
			count ++;
		}
		assertTrue(found);
		assertEquals(this.mobileEntities.size(), count);

		this.tested.unregisterMobileEntity(entity);
		this.tested.commit();
		
		iterator = this.tested.getMobileEntities();
		count = 0;
		found = false; 
		while (iterator.hasNext()) {
			e = iterator.next();
			if (e==entity) found = true;
			count ++;
		}
		assertFalse(found);
		assertEquals(this.mobileEntities.size()-1, count);
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.world.WorldModelManager3D#getPerceptions(fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceiver, java.lang.Class)}.
	 */
	public void testGetPerceptions() {
		this.tested.init(this.dTree, this.sTree, this.place);
		
		this.tested.computeAgentPerceptions();
		
		Perceptions<Perception3D> percepts = this.tested.getPerceptions(this.agentBody, Perception3D.class);
		
		assertNotNull(percepts);
		
		int intersecting = 0;
		
		Map<IntersectionType, Collection<Entity3D<AlignedBoundingBox>>> expected = new TreeMap<IntersectionType, Collection<Entity3D<AlignedBoundingBox>>>();
		for(Entity3D<AlignedBoundingBox> entity : this.staticEntities) {
			IntersectionType type = this.frustum.classifies(entity.getBounds3D());
			Collection<Entity3D<AlignedBoundingBox>> list = expected.get(type);
			if (list==null) {
				list = new ArrayList<Entity3D<AlignedBoundingBox>>();
				expected.put(type, list);
			}
			list.add(entity);
			if (type!=IntersectionType.OUTSIDE) {
				intersecting ++;
			}
		}
		
		for(MobileEntity3D<AlignedBoundingBox> entity : this.mobileEntities) {
			if (entity!=this.agentBody) {
				IntersectionType type = this.frustum.classifies(entity.getBounds3D());
				Collection<Entity3D<AlignedBoundingBox>> list = expected.get(type);
				if (list==null) {
					list = new ArrayList<Entity3D<AlignedBoundingBox>>();
					expected.put(type, list);
				}
				list.add(entity);
				if (type!=IntersectionType.OUTSIDE) {
					intersecting ++;
				}
			}
		}

		assertEquals(intersecting, percepts.getObjectPerceptCount());
		
		Iterator<Perception3D> iterator = percepts.iterator();
		while (iterator.hasNext()) {
			Perception3D percept = iterator.next();
			IntersectionType type = percept.getClassification();
			Collection<Entity3D<AlignedBoundingBox>> list = expected.get(type);
			assertNotNull(list);
			Iterator<Entity3D<AlignedBoundingBox>> iter = list.iterator();
			boolean deleted = false;
			while (iter.hasNext()) {
				Entity3D<AlignedBoundingBox> e = iter.next();
				if (percept.getPerceivedObject()==e) {
					iter.remove();
					deleted = true;
					break;
				}
			}
			assertTrue(deleted);
			if (list.isEmpty()) expected.remove(type);
		}
	}

	/**
	 * @author sgalland
	 */
	private class Entity3DStub extends Entity3D<AlignedBoundingBox> {
		
		private static final long serialVersionUID = 4902306500978826920L;
		
		private final int idx;
		
		public Entity3DStub(int idx, AlignedBoundingBox box) {
			super(box, ObjectType.OBJECTTYPE_SINGLETON, true);
			this.idx = idx;
		}
		
		@Override
		public String toString() {
			return "Static#"+this.idx; //$NON-NLS-1$
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof Entity3DStub) {
				return this.idx==((Entity3DStub)o).idx;
			}
			return false;
		}
		
	}

	/**
	 * @author sgalland
	 */
	private class MobileEntity3DStub extends MobileEntity3D<AlignedBoundingBox> {
		
		private static final long serialVersionUID = 4902306500978826920L;
		
		private final int idx;
		
		public MobileEntity3DStub(int idx, AlignedBoundingBox box, Mesh3D mesh) {
			super(box, ObjectType.OBJECTTYPE_SINGLETON, true, mesh);
			this.idx = idx;
		}
		
		@Override
		public String toString() {
			return "Mobile#"+this.idx; //$NON-NLS-1$
		}
		
	}

}
