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

import org.arakhne.afc.math.graph.GraphSegment;

/** This interface provides a way to replace the segments of the
 * paths found by the {@link AStar A* algorithm}.
 * 
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see AStar
 * @since 13.0
 */
public interface AStarSegmentReplacer<ST extends GraphSegment<ST,?>> {

	/** Replies a instance of segment to add inside the shortest path
	 * in place of the given segment. If this function replies, <code>null</code>
	 * the given segment is added.
	 * 
	 * @param index is the position in the shortest path of the segment to replace
	 * @param segment is the segment to replace.
	 * @return the replacement, or <code>null</code>.
	 */
	public ST replaceSegment(int index, ST segment);

}
