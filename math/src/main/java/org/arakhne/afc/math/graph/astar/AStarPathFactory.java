/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.graph.astar;

import org.arakhne.afc.math.graph.GraphPath;
import org.arakhne.afc.math.graph.GraphPoint;
import org.arakhne.afc.math.graph.GraphSegment;

/** This interface provides a mean to create a path factory
 * for the {@link AStar A* algorithm}.
 * 
 * @param <GP> is the type of the path to create.
 * @param <ST> is the type of the segments in the path.
 * @param <PT> is the type of the nodes in the path.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see AStar
 * @since 4.0
 */
public interface AStarPathFactory<GP extends GraphPath<GP,ST,PT>, ST extends GraphSegment<ST,PT>, PT extends GraphPoint<PT,ST>> {

	/** Create an empty path.
	 * 
	 * @param startPoint is the first point in the path.
	 * @param segment is the first connection to follow.
	 * @return the path instance.
	 */
	public GP newPath(PT startPoint, ST segment);
	
	/** Add the given segment into the given path.
	 * 
	 * @param path is the path to build.
	 * @param segment is the segment to add.
	 * @return <code>true</code> if the segment was added;
	 * otherwise <code>false</code>.
	 */
	public boolean addToPath(GP path, ST segment);

}
