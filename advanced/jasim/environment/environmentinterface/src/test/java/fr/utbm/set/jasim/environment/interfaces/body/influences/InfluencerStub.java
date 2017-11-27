/**
 * 
 */
package fr.utbm.set.jasim.environment.interfaces.body.influences;

import org.arakhne.afc.jasim.environment.interfaces.body.influences.InfluenceApplicationStatus;
import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influencer;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
final class InfluencerStub implements Influencer {
	/**
	 */
	public InfluencerStub() {
		//
	}
	@Override
	public void setLastInfluenceStatus(InfluenceApplicationStatus status) {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setLastInfluenceStatus(InfluenceApplicationStatus status,
			Throwable e) {
		//
	}
	
}
