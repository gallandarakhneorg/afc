/**
 * 
 */
package fr.utbm.set.jasim.environment.interfaces.body;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector2d;

import fr.utbm.set.geom.PseudoHamelDimension;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.JasimConstants;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.influences.KillInfluence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.EverythingInterestFilter;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.IdentityPhysicalPerceptionAlterator;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.InterestFilter;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception3D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceptions;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.PhysicalPerceptionAlterator;
import fr.utbm.set.jasim.environment.model.perception.frustum.Frustum3D;
import fr.utbm.set.jasim.environment.model.perception.frustum.SphericalFrustum;
import fr.utbm.set.jasim.environment.model.world.Mesh3D;
import fr.utbm.set.jasim.environment.semantics.BodyType;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AgentBody3DTest extends AbstractTestCase {

	private UUID ownerId;
	private Mesh3D mesh;
	private AgentBody3D<Influence,Perception3D,CombinableBounds3D> tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.ownerId = UUID.randomUUID();
		this.mesh = new Mesh3D(false, randomPoints3D());
		this.tested = new AgentBody3DStub<Influence,Perception3D,CombinableBounds3D>(
				this.ownerId, Perception3D.class,
				this.mesh.toBounds(AlignedBoundingBox.class),
				this.mesh);
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.ownerId = null;
		this.mesh = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#getAllSemantics()}.
	 */
	public void testGetAllSemantics() {
		assertEpsilonEquals(
				Arrays.asList(BodyType.BODYTYPE_SINGLETON),
				this.tested.getAllSemantics());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#getPreferredMathematicalDimension()}.
	 */
	public void testGetPreferredMathematicalDimension() {
		assertEquals(PseudoHamelDimension.DIMENSION_3D, this.tested.getPreferredMathematicalDimension());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#getPerceptionFilter()}.
	 */
	public void testGetPerceptionFilter() {
		assertNull(this.tested.getPerceptionFilter());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#setPerceptionFilter(fr.utbm.set.jasim.environment.interfaces.body.perceptions.PhysicalPerceptionAlterator)}.
	 */
	public void testSetPerceptionFilter() {
		assertNull(this.tested.getPerceptionFilter());
		
		PhysicalPerceptionAlterator alterator = new IdentityPhysicalPerceptionAlterator();
		this.tested.setPerceptionFilter(alterator);
		
		assertSame(alterator, this.tested.getPerceptionFilter());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#getInterestFilter()}.
	 */
	public void testGetInterestFilter() {
		assertNull(this.tested.getInterestFilter());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#setInterestFilter(fr.utbm.set.jasim.environment.interfaces.body.perceptions.InterestFilter)}.
	 */
	public void testSetInterestFilter() {
		assertNull(this.tested.getInterestFilter());
		
		InterestFilter filter = new EverythingInterestFilter();
		this.tested.setInterestFilter(filter);
		
		assertSame(filter, this.tested.getInterestFilter());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#kill()}.
	 */
	public void testKill() {
		BodyContainerEnvironmentStub env = new BodyContainerEnvironmentStub();
		this.tested.setEnvironment(env);
		
		this.tested.kill();
		
		Iterator<Influence> iterator = env.influences.iterator();
		Influence influence;
		int count = 0;
		while (iterator.hasNext()) {
			influence = iterator.next();
			if (influence instanceof KillInfluence) {
				count ++;
			}
		}
		
		assertEquals(1, count);
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#getViewDirection1D()}.
	 */
	public void testGetViewDirection1D() {
		try {
			this.tested.getViewDirection1D();
			fail("expected UnsupportedOperationException exception"); //$NON-NLS-1$
		}
		catch(Throwable _) {
			// Expected case
		}
	}

	/**
	 */
	public void testGetOrientation1D() {
		try {
			this.tested.getOrientation1D();
			fail("expected UnsupportedOperationException exception"); //$NON-NLS-1$
		}
		catch(Throwable _) {
			// Expected case
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#getViewDirection1D5()}.
	 */
	public void testGetViewDirection1D5() {
		try {
			this.tested.getViewDirection1D5();
			fail("expected UnsupportedOperationException exception"); //$NON-NLS-1$
		}
		catch(Throwable _) {
			// Expected case
		}
	}

	/**
	 */
	public void testOrientation1D5() {
		try {
			this.tested.getOrientation1D5();
			fail("expected UnsupportedOperationException exception"); //$NON-NLS-1$
		}
		catch(Throwable _) {
			// Expected case
		}
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#getViewDirection2D()}.
	 */
	public void testGetViewDirection2D() {
		assertEpsilonEquals(
				new Vector2d(
						JasimConstants.DEFAULT_VIEW_VECTOR_X,
						JasimConstants.DEFAULT_VIEW_VECTOR_Y),
				this.tested.getViewDirection2D5());
	}

	/**
	 */
	public void testOrientation2D() {
		assertEpsilonEquals(
				0.,
				this.tested.getOrientation2D5());
	}

	/**
	 */
	public void testGetViewDirection2D5() {
		assertEpsilonEquals(
				new Vector2d(
						JasimConstants.DEFAULT_VIEW_VECTOR_X,
						JasimConstants.DEFAULT_VIEW_VECTOR_Y),
				this.tested.getViewDirection2D5());
	}

	/**
	 */
	public void testGetOrientation2D5() {
		assertEpsilonEquals(
				0.,
				this.tested.getOrientation2D5());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#getViewDirection3D()}.
	 */
	public void testGetViewDirection3D() {
		assertEpsilonEquals(
				new AxisAngle4d(
						JasimConstants.DEFAULT_VIEW_VECTOR_X,
						JasimConstants.DEFAULT_VIEW_VECTOR_Y,
						JasimConstants.DEFAULT_VIEW_VECTOR_Z,
						0.),
				this.tested.getViewDirection3D());
	}

	/**
	 */
	public void testGetOrientation3D() {
		AxisAngle4d aa = new AxisAngle4d(
				JasimConstants.DEFAULT_UP_VECTOR_X,
				JasimConstants.DEFAULT_UP_VECTOR_Y,
				JasimConstants.DEFAULT_UP_VECTOR_Z,
				0.);
		Quat4d q = new Quat4d();
		q.set(aa);
		assertEpsilonEquals(
				q,
				this.tested.getOrientation3D());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#getPerceptions()}.
	 */
	public void testGetPerceptions() {
		BodyContainerEnvironmentStub env = new BodyContainerEnvironmentStub(this.tested);
		this.tested.setEnvironment(env);
		
		Perceptions<Perception3D> perceptions = this.tested.getPerceptions();
		assertNotNull(perceptions);
		
		Collection<? extends Perception> p = env.perceptions.get(this.tested);
		assertNotNull(p);

		assertEquals(p.size(), perceptions.getObjectPerceptCount());
		
		ArrayList<Perception3D> copy = new ArrayList<Perception3D>();
		
		for(Perception3D pt : perceptions) {
			copy.add(pt);
		}
		
		for(Perception3D pt : copy) {
			assertTrue(p.remove(pt));
		}
		
		assertTrue(p.isEmpty());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#getFrustums()}.
	 */
	public void testGetFrustums() {
		assertNotNull(this.tested.getFrustums());
		assertTrue(this.tested.getFrustums().isEmpty());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#setFrustums(java.util.List)}.
	 */
	public void testSetFrustumsList() {
		List<Frustum3D> frustums = new ArrayList<Frustum3D>();
		SphericalFrustum f1 = new SphericalFrustum(UUID.randomUUID());
		f1.set(randomPoint3D());
		f1.setRadius(this.RANDOM.nextDouble());
		
		SphericalFrustum f2 = new SphericalFrustum(UUID.randomUUID());
		f2.set(randomPoint3D());
		f2.setRadius(this.RANDOM.nextDouble());
		
		SphericalFrustum f3 = new SphericalFrustum(UUID.randomUUID());
		f3.set(randomPoint3D());
		f3.setRadius(this.RANDOM.nextDouble());
		
		frustums.add(f1);
		frustums.add(f2);
		frustums.add(f3);
		
		this.tested.setFrustums(frustums);
		
		Collection<? extends Frustum3D> list = this.tested.getFrustums();
		
		assertEpsilonEquals(frustums, list);
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D#setFrustums(fr.utbm.set.jasim.environment.model.perception.frustum.Frustum3D[])}.
	 */
	public void testSetFrustumsFrustum3DArray() {
		SphericalFrustum f1 = new SphericalFrustum(UUID.randomUUID());
		f1.set(randomPoint3D());
		f1.setRadius(this.RANDOM.nextDouble());

		SphericalFrustum f2 = new SphericalFrustum(UUID.randomUUID());
		f2.set(randomPoint3D());
		f2.setRadius(this.RANDOM.nextDouble());

		SphericalFrustum f3 = new SphericalFrustum(UUID.randomUUID());
		f3.set(randomPoint3D());
		f3.setRadius(this.RANDOM.nextDouble());

		Frustum3D[] frustums = new Frustum3D[] { f1, f2, f3 };
		
		this.tested.setFrustums(frustums);
		
		Collection<? extends Frustum3D> list = this.tested.getFrustums();
		
		assertEpsilonEquals(Arrays.asList(frustums), list);
	}

}
