package org.arakhne.afc.jasim.environment.interfaces.body.perceptions;

import fr.utbm.set.collection.SizedIterator;

/**
 * This class describes a set of perceptions.
 * 
 * @param <PT> The type of perception
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Perceptions<PT extends Perception> extends Iterable<PT> {
	
	/** Replies the set of mobile objects.
	 * 
	 * @return the set of dynamic objects that are inside the associated frustum.
	 */
	public SizedIterator<PT> getDynamicPercepts();

	/** Replies the set of static objects.
	 * 
	 * @return the set of static objects that are inside the associated frustum.
	 */
	public SizedIterator<PT> getStaticPercepts();

	/** Replies the count of dynamic entities.
	 * 
	 * @return the count of dynamic entities.
	 */
	public int getDynamicPerceptCount();
	
	/** Replies the count of static entities.
	 * 
	 * @return the count of static entities.
	 */
	public int getStaticPerceptCount();

	/** Replies the perception of the ground.
	 *
	 * @return the perception of the ground.
	 */
	public GroundPerception getGroundPerception();
	
	/** Replies the count of object perceptions in this list.
	 * 
	 * @return the count of perceptions of static and mobile objects. 
	 */
	public int getObjectPerceptCount();
	
	/** Replies if an object perception is in this list.
	 * 
	 * @return <code>true</code> if at least one object perception
	 * is in this list, <code>false</code> otherwise. 
	 */
	public boolean hasObjectPercept();

	/** Replies if the ground is perceived.
	 * 
	 * @return <code>true</code> if the ground is perceived,
	 * <code>false</code> otherwise. 
	 */
	public boolean hasGroundPercept();

}
