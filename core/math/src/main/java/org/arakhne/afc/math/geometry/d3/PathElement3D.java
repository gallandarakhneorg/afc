package org.arakhne.afc.math.geometry.d3;

import java.io.Serializable;

import org.arakhne.afc.math.geometry.PathElementType;

public interface PathElement3D extends Serializable {

	/** Replies the type of the element.
	 * 
	 * @return <code>true</code> if the points are
	 * the same; otherwise <code>false</code>.
	 */
	public PathElementType getType();

	/** Replies if the element is empty, ie. the points are the same.
	 * 
	 * @return <code>true</code> if the points are
	 * the same; otherwise <code>false</code>.
	 */
	public boolean isEmpty();
	
	/** Replies if the element is not empty and its drawable.
	 * Only the path elements that may produce pixels on the screen
	 * must reply <code>true</code> in this function.
	 * 
	 * @return <code>true</code> if the path element
	 * is drawable; otherwise <code>false</code>.
	 */
	public boolean isDrawable();
	
}
