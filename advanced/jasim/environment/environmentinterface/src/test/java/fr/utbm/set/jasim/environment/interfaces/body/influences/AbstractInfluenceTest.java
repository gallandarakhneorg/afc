/**
 * 
 */
package fr.utbm.set.jasim.environment.interfaces.body.influences;

import junit.framework.TestCase;

import org.arakhne.afc.jasim.environment.interfaces.body.influences.AbstractInfluence;
import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influencable;
import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influencer;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AbstractInfluenceTest extends TestCase {

	private Influencable influencable;
	private AbstractInfluence tested;
	
	/**
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.influencable = new InfluencableStub();
		this.tested = new AbstractInfluence(this.influencable) {
			//
		};
	}

	/**
	 */
	@Override
	protected void tearDown() throws Exception {
		this.influencable = null;
		this.tested = null;
		super.tearDown();
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.interfaces.body.influences.AbstractInfluence#getInfluencedObject()}.
	 */
	public void testGetInfluencedObject() {
		assertSame(this.influencable, this.tested.getInfluencedObject());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.interfaces.body.influences.AbstractInfluence#setInfluencedObject(org.arakhne.afc.jasim.environment.interfaces.body.influences.Influencable)}.
	 */
	public void testSetInfluencedObject() {
		assertSame(this.influencable, this.tested.getInfluencedObject());
		Influencable i = new InfluencableStub();
		this.tested.setInfluencedObject(i);
		assertSame(i, this.tested.getInfluencedObject());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.interfaces.body.influences.AbstractInfluence#getInfluencer()}.
	 */
	public void testGetInfluencer() {
		assertNull(this.tested.getInfluencer());
	}

	/**
	 * Test method for {@link org.arakhne.afc.jasim.environment.interfaces.body.influences.AbstractInfluence#setInfluencer(org.arakhne.afc.jasim.environment.interfaces.body.influences.Influencer)}.
	 */
	public void testSetInfluencer() {
		assertSame(this.influencable, this.tested.getInfluencedObject());
		Influencer i = new InfluencerStub();
		this.tested.setInfluencer(i);
		assertSame(i, this.tested.getInfluencer());
	}

}
