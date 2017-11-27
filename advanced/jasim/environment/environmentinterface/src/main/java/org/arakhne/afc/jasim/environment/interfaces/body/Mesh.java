/* 
 * $Id$
 * 
 * Copyright (c) 2011, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.jasim.environment.interfaces.body;

import java.io.Serializable;

/** This class contains a mesh.
 * <p>
 * The origin from which point coordinates are computed is
 * outside the scope of this class.
 *
 * @param <P> is the type for the points in the mesh.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Mesh<P/* extends EuclidianPoint*/> extends Serializable, Cloneable {
	
	/** Replies the points in this mesh relatively to the internal pivot.
	 * 
	 * @return the points in this mesh.
	 */
	public P[] getPoints();
	
	/** Make the points of this mesh from global to local.
	 * 
	 * @param localReference
	 */
	public void makeLocal(P localReference);

	/** Make the points of this mesh from local to global.
	 * 
	 * @param localReference
	 */
	public void makeGlobal(P localReference);
	
	/** Replies if the coordinates in this mesh are assumed to
	 * be local.
	 * 
	 * @return <code>true</code> if the mesh's coordinates are local,
	 * <code>false</code> if coordinates are global.
	 */
	public boolean isLocalMesh();

	/** Replies if the coordinates in this mesh are assumed to
	 * be global.
	 * 
	 * @return <code>true</code> if the mesh's coordinates are global,
	 * <code>false</code> if coordinates are local.
	 */
	public boolean isGlobalMesh();
	
}