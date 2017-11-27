/**
 * 
 */
package fr.utbm.set.jasim.environment.model.influencereaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.utbm.set.collection.CollectionUtil;
import fr.utbm.set.collection.SizedIterable;
import fr.utbm.set.jasim.environment.model.actions.EnvironmentalActionCollector;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class ActionCollectorStub implements EnvironmentalActionCollector<EnvironmentalAction3D> {

	/**
	 * Collected actions
	 */
	public List<EnvironmentalAction3D> collectedActions = new ArrayList<EnvironmentalAction3D>();
	
	/**
	 */
	public ActionCollectorStub() {
		//
	}

	@Override
	public void addAction(EnvironmentalAction3D action) {
		this.collectedActions.add(action);
	}

	@Override
	public void addActions(Collection<? extends EnvironmentalAction3D> actions) {
		this.collectedActions.addAll(actions);
	}

	@Override
	public SizedIterable<EnvironmentalAction3D> consumeActions() {
		Collection<EnvironmentalAction3D> col = this.collectedActions;
		this.collectedActions = new ArrayList<EnvironmentalAction3D>();
		return CollectionUtil.toSizedIterable(col);
	}
	
}
