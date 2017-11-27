/**
 * 
 */
package fr.utbm.set.jasim.environment.model.perception.percepts;

import java.util.Iterator;

import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.GroundPerception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.model.perception.frustum.CullingResult;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.unittest.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PerceptionListTest extends AbstractTestCase {

	private PerceptionListStub tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.tested = new PerceptionListStub();
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.tested = null;
		super.tearDown();
	}

	/**
	 */
	public void testAddStaticPerception() {
		CullingResult<WorldEntity<?>> result = new CullingResult<WorldEntity<?>>(null, null, null);
		this.tested.addStaticPerception(result);
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList#addDynamicPerception(fr.utbm.set.jasim.environment.model.perception.frustum.CullingResult)}.
	 */
	public void testAddDynamicPerception() {
		CullingResult<WorldEntity<?>> result = new CullingResult<WorldEntity<?>>(null, null, null);
		this.tested.addDynamicPerception(result);
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList#clear()}.
	 */
	public void testClear() {
		assertFalse(this.tested.iterator().hasNext());
		
		CullingResult<WorldEntity<?>> mresult;
		CullingResult<WorldEntity<?>> sresult;

		sresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addStaticPerception(sresult);
		assertTrue(this.tested.iterator().hasNext());
		
		this.tested.clear();
		assertFalse(this.tested.iterator().hasNext());

		mresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.INSIDE, null);
		this.tested.addDynamicPerception(mresult);
		assertTrue(this.tested.iterator().hasNext());
		
		this.tested.clear();
		assertFalse(this.tested.iterator().hasNext());

		sresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.SPANNING, null);
		this.tested.addStaticPerception(sresult);
		mresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addDynamicPerception(mresult);
		assertTrue(this.tested.iterator().hasNext());
		
		this.tested.clear();
		assertFalse(this.tested.iterator().hasNext());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList#getDynamicPerceptCount()}.
	 */
	public void testGetDynamicPerceptCount() {
		assertEquals(0, this.tested.getDynamicPerceptCount());
		
		CullingResult<WorldEntity<?>> mresult;
		CullingResult<WorldEntity<?>> sresult;

		sresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addStaticPerception(sresult);
		assertEquals(0, this.tested.getDynamicPerceptCount());
		
		mresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addDynamicPerception(mresult);
		assertEquals(1, this.tested.getDynamicPerceptCount());

		mresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addDynamicPerception(mresult);
		assertEquals(2, this.tested.getDynamicPerceptCount());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList#getGroundPerception()}.
	 */
	public void testGetGroundPerception() {
		assertNull(this.tested.getGroundPerception());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList#setGroundPerception(fr.utbm.set.jasim.environment.interfaces.body.perceptions.GroundPerception)}.
	 */
	public void testSetGroundPerceptionGroundPerception() {
		assertNull(this.tested.getGroundPerception());
		
		GroundPerception ground = new GroundPerceptionStub();
		
		this.tested.setGroundPerception(ground);

		assertSame(ground, this.tested.getGroundPerception());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList#getStaticPerceptCount()}.
	 */
	public void testGetStaticPerceptCount() {
		assertEquals(0, this.tested.getStaticPerceptCount());
		
		CullingResult<WorldEntity<?>> mresult;
		CullingResult<WorldEntity<?>> sresult;
		
		mresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addDynamicPerception(mresult);
		assertEquals(0, this.tested.getStaticPerceptCount());
		
		sresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addStaticPerception(sresult);
		assertEquals(1, this.tested.getStaticPerceptCount());

		sresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addStaticPerception(sresult);
		assertEquals(2, this.tested.getStaticPerceptCount());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList#iterator()}.
	 */
	public void testIterator() {
		Perception p;
		Iterator<Perception> iterator = this.tested.iterator();
		assertFalse(iterator.hasNext());
		
		CullingResult<WorldEntity<?>> mresult;
		CullingResult<WorldEntity<?>> sresult;

		mresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addDynamicPerception(mresult);
		iterator = this.tested.iterator();
		assertTrue(iterator.hasNext());
		p = iterator.next();
		assertEquals(mresult.getIntersectionType(), p.getClassification());
		assertSame(mresult.getCulledObject(), p.getPerceivedObject());
		assertFalse(iterator.hasNext());
		
		this.tested.clear();
		
		sresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.INSIDE, null);
		this.tested.addStaticPerception(sresult);
		iterator = this.tested.iterator();
		assertTrue(iterator.hasNext());
		p = iterator.next();
		assertEquals(sresult.getIntersectionType(), p.getClassification());
		assertSame(sresult.getCulledObject(), p.getPerceivedObject());
		assertFalse(iterator.hasNext());

		this.tested.clear();

		sresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addStaticPerception(sresult);
		mresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.INSIDE, null);
		this.tested.addDynamicPerception(mresult);
		iterator = this.tested.iterator();
		assertTrue(iterator.hasNext());
		p = iterator.next();
		assertEquals(sresult.getIntersectionType(), p.getClassification());
		assertSame(sresult.getCulledObject(), p.getPerceivedObject());
		assertTrue(iterator.hasNext());
		p = iterator.next();
		assertEquals(mresult.getIntersectionType(), p.getClassification());
		assertSame(mresult.getCulledObject(), p.getPerceivedObject());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList#getStaticPercepts()}.
	 */
	public void testGetStaticPercepts() {
		Perception p;
		Iterator<Perception> iterator = this.tested.iterator();
		assertFalse(iterator.hasNext());
		
		CullingResult<WorldEntity<?>> mresult;
		CullingResult<WorldEntity<?>> sresult;

		mresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addDynamicPerception(mresult);
		iterator = this.tested.getStaticPercepts();
		assertFalse(iterator.hasNext());
		
		this.tested.clear();

		sresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.INSIDE, null);
		this.tested.addStaticPerception(sresult);
		iterator = this.tested.getStaticPercepts();
		assertTrue(iterator.hasNext());
		p = iterator.next();
		assertEquals(sresult.getIntersectionType(), p.getClassification());
		assertSame(sresult.getCulledObject(), p.getPerceivedObject());
		assertFalse(iterator.hasNext());

		this.tested.clear();

		sresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addStaticPerception(sresult);
		mresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.INSIDE, null);
		this.tested.addDynamicPerception(mresult);
		iterator = this.tested.getStaticPercepts();
		assertTrue(iterator.hasNext());
		p = iterator.next();
		assertEquals(sresult.getIntersectionType(), p.getClassification());
		assertSame(sresult.getCulledObject(), p.getPerceivedObject());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Test method for {@link fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList#getDynamicPercepts()}.
	 */
	public void testGetDynamicPercepts() {
		Perception p;
		Iterator<Perception> iterator = this.tested.iterator();
		assertFalse(iterator.hasNext());
		
		CullingResult<WorldEntity<?>> mresult;
		CullingResult<WorldEntity<?>> sresult;

		mresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.INSIDE, null);
		this.tested.addDynamicPerception(mresult);
		iterator = this.tested.getDynamicPercepts();
		assertTrue(iterator.hasNext());
		p = iterator.next();
		assertEquals(mresult.getIntersectionType(), p.getClassification());
		assertSame(mresult.getCulledObject(), p.getPerceivedObject());
		assertFalse(iterator.hasNext());
		
		this.tested.clear();
		
		sresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addStaticPerception(sresult);
		iterator = this.tested.getDynamicPercepts();
		assertFalse(iterator.hasNext());

		this.tested.clear();

		sresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.INSIDE, null);
		this.tested.addStaticPerception(sresult);
		mresult = new CullingResult<WorldEntity<?>>(null, IntersectionType.ENCLOSING, null);
		this.tested.addDynamicPerception(mresult);
		iterator = this.tested.getDynamicPercepts();
		assertTrue(iterator.hasNext());
		p = iterator.next();
		assertEquals(mresult.getIntersectionType(), p.getClassification());
		assertSame(mresult.getCulledObject(), p.getPerceivedObject());
		assertFalse(iterator.hasNext());
	}

}
