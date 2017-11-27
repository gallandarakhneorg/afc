/**
 * 
 */
package fr.utbm.set.jasim.environment.interfaces.body;

import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencable;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencer;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class InfluenceStub implements Influence {

	/**
	 */
	public InfluenceStub() {
		//
	}

	@Override
	public Influencable getInfluencedObject() {
		return null;
	}

	@Override
	public Influencer getInfluencer() {
		return null;
	}

	@Override
	public void setInfluencedObject(Influencable influencedObject) {
		//
	}

	@Override
	public void setInfluencer(Influencer influencer) {
		//
	}
	
}
