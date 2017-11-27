/**
 * 
 */
package fr.utbm.set.jasim.environment.model;

import java.util.Collections;
import java.util.UUID;

import fr.utbm.set.geom.PseudoHamelDimension;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.jasim.environment.model.ground.AlignedIndoorGround;
import fr.utbm.set.jasim.environment.model.place.DefaultPlaceDescription;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.jasim.environment.semantics.ObjectType;
import fr.utbm.set.jasim.environment.time.ConstantTimeManager;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SituatedEnvironment3DTest extends AbstractTestCase {

	private UUID place1, place2;
	private SituatedEnvironment3D<AlignedBoundingBox,AlignedBoundingBox> tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.tested = new SituatedEnvironment3D<AlignedBoundingBox,AlignedBoundingBox>(AlignedBoundingBox.class);

		SituatedEnvironmentDescription<Entity3D<AlignedBoundingBox>, MobileEntity3D<AlignedBoundingBox>> description =
			new SituatedEnvironmentDescription<Entity3D<AlignedBoundingBox>, MobileEntity3D<AlignedBoundingBox>>(
					UUID.randomUUID(),
					new ConstantTimeManager());
		
		// First place
		this.place1 = UUID.randomUUID();
		DefaultPlaceDescription<Entity3D<AlignedBoundingBox>, MobileEntity3D<AlignedBoundingBox>> pDescription =
			new DefaultPlaceDescription<Entity3D<AlignedBoundingBox>, MobileEntity3D<AlignedBoundingBox>>(
					this.place1);
		Entity3D<AlignedBoundingBox> entity = new Entity3D<AlignedBoundingBox>(
				new AlignedBoundingBox(), ObjectType.OBJECTTYPE_SINGLETON, true);
		pDescription.setStaticEntities(Collections.singleton(entity));
		pDescription.setGround(new AlignedIndoorGround(UUID.randomUUID(), 0., 0., 1000., 1000., 5.));
		description.addPlace(pDescription);
		
		// Second place
		this.place2 = UUID.randomUUID();
		entity = new Entity3D<AlignedBoundingBox>(
				new AlignedBoundingBox(), ObjectType.OBJECTTYPE_SINGLETON, true);
		pDescription = new DefaultPlaceDescription<Entity3D<AlignedBoundingBox>, MobileEntity3D<AlignedBoundingBox>>(
				this.place2);
		pDescription.setStaticEntities(Collections.singleton(entity));
		pDescription.setGround(new AlignedIndoorGround(UUID.randomUUID(), 600., 600., 1400., 1000., 50.));
		description.addPlace(pDescription);
		
		// Activate the environment
		this.tested.activate(description, null);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.place1 = this.place2 = null;
		this.tested = null;
		super.tearDown();
	}
	
	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.SituatedEnvironment3D#getMathematicalDimension()}.
	 */
	public void testGetMathematicalDimension() {
		assertEquals(PseudoHamelDimension.DIMENSION_3D, this.tested.getMathematicalDimension());
	}

}
