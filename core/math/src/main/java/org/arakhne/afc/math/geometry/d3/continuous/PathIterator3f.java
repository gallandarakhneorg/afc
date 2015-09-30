package org.arakhne.afc.math.geometry.d3.continuous;

import java.util.Iterator;

import org.arakhne.afc.math.geometry.PathWindingRule;

public interface PathIterator3f extends Iterator<AbstractPathElement3F> {

	/** Replies the winding rule for the path.
	 * 
	 * @return the winding rule for the path.
	 */
	public PathWindingRule getWindingRule();
	
	/** Replies the iterator may reply only elements of type
	 * <code>MOVE_TO</code>, <code>LINE_TO</code>, or
	 * <code>CLOSE</code> (no curve).
	 * 
	 * @return <code>true</code> if the iterator does not
	 * contain curve primitives, <code>false</code>
	 * otherwise.
	 */
	public boolean isPolyline();

}
