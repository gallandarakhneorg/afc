/**
 * 
 */
package fr.utbm.set.jasim.environment.model.place;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment3D;
import fr.utbm.set.jasim.environment.model.perception.algorithm.SequencialTopDownPerceptionAlgorithm;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.BoundCenterPartitionPolicy;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree.BspTreeManipulator3D;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree.DynamicBspTree3D;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree.StaticBspTree3D;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree.StaticBspTreeBuilder3D;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.model.world.WorldModelManager3D;
import fr.utbm.set.jasim.environment.semantics.ObjectType;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Place3DTest extends AbstractTestCase {

	private UUID placeId;
	private SituatedEnvironment3D<AlignedBoundingBox,AlignedBoundingBox> environment;
	private Place3D<AlignedBoundingBox,AlignedBoundingBox> tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.placeId = UUID.randomUUID();
		this.environment = new SituatedEnvironment3D<AlignedBoundingBox, AlignedBoundingBox>(AlignedBoundingBox.class);
		this.tested = new Place3D<AlignedBoundingBox,AlignedBoundingBox>(
				this.environment, this.placeId, AlignedBoundingBox.class,
				new SequencialTopDownPerceptionAlgorithm<AlignedBoundingBox,AlignedBoundingBox>());
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.environment = null;
		this.placeId = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.place.Place3D#init(fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree, fr.utbm.set.jasim.environment.model.perception.tree.StaticPerceptionTree)}.
	 */
	public void testInit_1() {
		BspTreeManipulator3D<AlignedBoundingBox> manipulator = new BspTreeManipulator3D<AlignedBoundingBox>(
				5, BoundCenterPartitionPolicy.SINGLETON, null);
		DynamicBspTree3D<AlignedBoundingBox> dTree = new DynamicBspTree3D<AlignedBoundingBox>(manipulator);
		StaticBspTree3D<AlignedBoundingBox> sTree = new StaticBspTree3D<AlignedBoundingBox>();
		
		this.tested.init(dTree, sTree);


		WorldModelManager3D<AlignedBoundingBox,AlignedBoundingBox> manager = this.tested.getWorldModelManager3D();
		assertNotNull(manager);
		assertSame(dTree, manager.getInnerWorldModel(DynamicBspTree3D.class));
		assertNull(manager.getInnerWorldModel(StaticBspTree3D.class));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.place.Place3D#init(fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree, fr.utbm.set.jasim.environment.model.perception.tree.StaticPerceptionTree)}
	 * @throws Exception
	 */
	public void testInit_2() throws Exception {
		BspTreeManipulator3D<AlignedBoundingBox> manipulator = new BspTreeManipulator3D<AlignedBoundingBox>(
				5, BoundCenterPartitionPolicy.SINGLETON, null);
		DynamicBspTree3D<AlignedBoundingBox> dTree = new DynamicBspTree3D<AlignedBoundingBox>(manipulator);

		Entity3D<AlignedBoundingBox> entity = new Entity3D<AlignedBoundingBox>(new AlignedBoundingBox(-1, -1, -1, 1, 1, 1),
				ObjectType.OBJECTTYPE_SINGLETON,
				true);
		List<Entity3D<AlignedBoundingBox>> collection = 
				new ArrayList<Entity3D<AlignedBoundingBox>>();
		collection.add(entity);
		
		StaticBspTreeBuilder3D<AlignedBoundingBox> sBuilder = new StaticBspTreeBuilder3D<AlignedBoundingBox>(5, new BoundCenterPartitionPolicy());
		StaticBspTree3D<AlignedBoundingBox> sTree = sBuilder.buildTree(
				collection);
		
		this.tested.init(dTree, sTree);

		WorldModelManager3D<AlignedBoundingBox,AlignedBoundingBox> manager = this.tested.getWorldModelManager3D();
		assertNotNull(manager);
		assertSame(dTree, manager.getInnerWorldModel(DynamicBspTree3D.class));
		assertSame(sTree, manager.getInnerWorldModel(StaticBspTree3D.class));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.place.Place3D#destroy()}.
	 */
	public void testDestroy() {
		BspTreeManipulator3D<AlignedBoundingBox> manipulator = new BspTreeManipulator3D<AlignedBoundingBox>(
				5, BoundCenterPartitionPolicy.SINGLETON, null);
		DynamicBspTree3D<AlignedBoundingBox> dTree = new DynamicBspTree3D<AlignedBoundingBox>(manipulator);
		StaticBspTree3D<AlignedBoundingBox> sTree = new StaticBspTree3D<AlignedBoundingBox>();
		
		this.tested.init(dTree, sTree);
		
		this.tested.destroy();

		WorldModelManager3D<AlignedBoundingBox,AlignedBoundingBox> manager = this.tested.getWorldModelManager3D();
		assertNotNull(manager);
		assertNull(manager.getInnerWorldModel(DynamicBspTree3D.class));
		assertNull(manager.getInnerWorldModel(StaticBspTree3D.class));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.place.Place3D#getWorldModel()}.
	 */
	public void testGetWorldModel() {
		BspTreeManipulator3D<AlignedBoundingBox> manipulator = new BspTreeManipulator3D<AlignedBoundingBox>(
				5, BoundCenterPartitionPolicy.SINGLETON, null);
		DynamicBspTree3D<AlignedBoundingBox> dTree = new DynamicBspTree3D<AlignedBoundingBox>(manipulator);
		StaticBspTree3D<AlignedBoundingBox> sTree = new StaticBspTree3D<AlignedBoundingBox>();
		
		this.tested.init(dTree, sTree);

		WorldModelManager3D<AlignedBoundingBox,AlignedBoundingBox> manager = this.tested.getWorldModelManager3D();
		assertNotNull(manager);

		assertSame(manager, this.tested.getWorldModel());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.place.Place3D#getWorldModelUpdater()}.
	 */
	public void testGetWorldModelUpdater() {
		BspTreeManipulator3D<AlignedBoundingBox> manipulator = new BspTreeManipulator3D<AlignedBoundingBox>(
				5, BoundCenterPartitionPolicy.SINGLETON, null);
		DynamicBspTree3D<AlignedBoundingBox> dTree = new DynamicBspTree3D<AlignedBoundingBox>(manipulator);
		StaticBspTree3D<AlignedBoundingBox> sTree = new StaticBspTree3D<AlignedBoundingBox>();
		
		this.tested.init(dTree, sTree);

		WorldModelManager3D<AlignedBoundingBox,AlignedBoundingBox> manager = this.tested.getWorldModelManager3D();
		assertNotNull(manager);

		assertSame(manager, this.tested.getWorldModelUpdater());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.place.Place3D#getWorldModelManager3D()}.
	 */
	public void testGetWorldModelManager3D_1() {
		BspTreeManipulator3D<AlignedBoundingBox> manipulator = new BspTreeManipulator3D<AlignedBoundingBox>(
				5, BoundCenterPartitionPolicy.SINGLETON, null);
		DynamicBspTree3D<AlignedBoundingBox> dTree = new DynamicBspTree3D<AlignedBoundingBox>(manipulator);
		StaticBspTree3D<AlignedBoundingBox> sTree = new StaticBspTree3D<AlignedBoundingBox>();
		
		this.tested.init(dTree, sTree);

		WorldModelManager3D<AlignedBoundingBox,AlignedBoundingBox> manager = this.tested.getWorldModelManager3D();
		assertNotNull(manager);
		assertSame(dTree, manager.getInnerWorldModel(DynamicBspTree3D.class));
		assertNull(manager.getInnerWorldModel(StaticBspTree3D.class));
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.place.Place3D#getWorldModelManager3D()}.
	 * @throws Exception
	 */
	public void testGetWorldModelManager3D_2() throws Exception {
		BspTreeManipulator3D<AlignedBoundingBox> manipulator = new BspTreeManipulator3D<AlignedBoundingBox>(
				5, BoundCenterPartitionPolicy.SINGLETON, null);
		DynamicBspTree3D<AlignedBoundingBox> dTree = new DynamicBspTree3D<AlignedBoundingBox>(manipulator);

		Entity3D<AlignedBoundingBox> entity = new Entity3D<AlignedBoundingBox>(
				new AlignedBoundingBox(-1, -1, -1, 1, 1, 1),
				ObjectType.OBJECTTYPE_SINGLETON,
				true);
		List<Entity3D<AlignedBoundingBox>> collection = new ArrayList<Entity3D<AlignedBoundingBox>>();
		collection.add(entity);
		
		StaticBspTreeBuilder3D<AlignedBoundingBox> sBuilder = new StaticBspTreeBuilder3D<AlignedBoundingBox>(
				5, new BoundCenterPartitionPolicy());
		StaticBspTree3D<AlignedBoundingBox> sTree =
				sBuilder.buildTree(collection);
				
		
		this.tested.init(dTree, sTree);

		WorldModelManager3D<AlignedBoundingBox,AlignedBoundingBox> manager = this.tested.getWorldModelManager3D();
		assertNotNull(manager);
		assertSame(dTree, manager.getInnerWorldModel(DynamicBspTree3D.class));
		assertSame(sTree, manager.getInnerWorldModel(StaticBspTree3D.class));
	}

}
