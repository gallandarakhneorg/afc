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
package org.arakhne.afc.math.graph.simple;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.arakhne.afc.math.graph.GraphSegment;

/** This class provides a simple implementation of a graph's segment
 * for a {@link SGraph}.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.1
 */
public class SGraphSegment implements GraphSegment<SGraphSegment,SGraphPoint> {

	private final WeakReference<SGraph> graph;
	private float length;
	private SGraphPoint startPoint;
	private SGraphPoint endPoint;
	private List<Object> userData = null;
	
	/**
	 * @param graph is the graph in which the segment is.
	 */
	public SGraphSegment(SGraph graph) {
		this(graph, Float.NaN);
	}
	
	/**
	 * @param graph is the graph in which the segment is.
	 * @param length is the length of the segment.
	 */
	public SGraphSegment(SGraph graph, float length) {
		assert(graph!=null);
		this.length = length;
		graph.add(this);
		this.graph = new WeakReference<SGraph>(graph);
		this.startPoint = new SGraphPoint(graph);
		this.endPoint = new SGraphPoint(graph);
	}

	/** Replies the graph in which this segment is.
	 * 
	 * @return the graph in which this segment is.
	 */
	public SGraph getGraph() {
		return this.graph.get();
	}
	
	private void setBegin(SGraphPoint p) {
		if (this.startPoint!=null) {
			p.add(this.startPoint.getConnectedSegments());
			this.startPoint.clear();
		}
		this.startPoint = p;
	}
	
	private void setEnd(SGraphPoint p) {
		if (this.endPoint!=null) {
			p.add(this.endPoint.getConnectedSegments());
			this.endPoint.clear();
		}
		this.endPoint = p;
	}

	/** Connect the begin point of this segment to the
	 * begin point of the given segment.
	 * This function change the connection points of the two segments.
	 * 
	 * @param segment
	 * @throws IllegalArgumentException if the given segment is not in the same
	 * graph.
	 */
	public void connectBeginToBegin(SGraphSegment segment) {
		if (segment.getGraph()!=getGraph())
			throw new IllegalArgumentException();
		SGraphPoint point = new SGraphPoint(getGraph());
		setBegin(point);
		segment.setBegin(point);
		
		SGraph g = getGraph();
		assert(g!=null);
		g.updatePointCount(-1);
	}

	/** Connect the begin point of this segment to the
	 * end point of the given segment.
	 * This function change the connection points of the two segments.
	 * 
	 * @param segment
	 * @throws IllegalArgumentException if the given segment is not in the same
	 * graph.
	 */
	public void connectBeginToEnd(SGraphSegment segment) {
		if (segment.getGraph()!=getGraph())
			throw new IllegalArgumentException();
		SGraphPoint point = new SGraphPoint(getGraph());
		setBegin(point);
		segment.setEnd(point);

		SGraph g = getGraph();
		assert(g!=null);
		g.updatePointCount(-1);
	}

	/** Connect the end point of this segment to the
	 * begin point of the given segment.
	 * This function change the connection points of the two segments.
	 * 
	 * @param segment
	 * @throws IllegalArgumentException if the given segment is not in the same
	 * graph.
	 */
	public void connectEndToBegin(SGraphSegment segment) {
		if (segment.getGraph()!=getGraph())
			throw new IllegalArgumentException();
		SGraphPoint point = new SGraphPoint(getGraph());
		setEnd(point);
		segment.setBegin(point);

		SGraph g = getGraph();
		assert(g!=null);
		g.updatePointCount(-1);
	}

	/** Connect the end point of this segment to the
	 * end point of the given segment.
	 * This function change the connection points of the two segments.
	 * 
	 * @param segment
	 * @throws IllegalArgumentException if the given segment is not in the same
	 * graph.
	 */
	public void connectEndToEnd(SGraphSegment segment) {
		if (segment.getGraph()!=getGraph())
			throw new IllegalArgumentException();
		SGraphPoint point = new SGraphPoint(getGraph());
		setEnd(point);
		segment.setEnd(point);

		SGraph g = getGraph();
		assert(g!=null);
		g.updatePointCount(-1);
	}
	
	/** Disconnect the begin point of this segment.
	 */
	public void disconnectBegin() {
		if (this.startPoint!=null) {
			this.startPoint.remove(this);
		}
		this.startPoint = new SGraphPoint(getGraph());
		this.startPoint.add(this);
	}

	/** Disconnect the end point of this segment.
	 */
	public void disconnectEnd() {
		if (this.endPoint!=null) {
			this.endPoint.remove(this);
		}
		this.endPoint = new SGraphPoint(getGraph());
		this.endPoint.add(this);
	}

	/** {@inheritDoc}
     */
	@Override
	public SGraphPoint getBeginPoint() {
		return this.startPoint;
	}

    /** {@inheritDoc}
     */
	@Override
	public SGraphPoint getEndPoint() {
		return this.endPoint;
	}

    /** {@inheritDoc}
     */
	@Override
	public SGraphPoint getOtherSidePoint(SGraphPoint point) {
		if (point!=null) {
			if (point.equals(this.startPoint)) {
				return this.endPoint;
			}
			if (point.equals(this.endPoint)) {
				return this.startPoint;
			}
		}
		return null;
	}
	
    /** {@inheritDoc}
     */
	@Override
	public float getLength() {
		return this.length;
	}

    /** Set the length of the segment.
     * 
     * @param length is the length of the segment.
     */
	public void setLength(float length) {
		this.length = length;
	}

	/** Add a user data in the data associated to this point.
	 * 
	 * @param userData
	 * @return <code>true</code> if the data was added; otherwise <code>false</code>.
	 */
	public boolean addUserData(Object userData) {
		if (this.userData==null) {
			this.userData = new ArrayList<Object>();
		}
		return this.userData.add(userData);
	}

	/** Remove a user data from the data associated to this point.
	 * 
	 * @param userData
	 * @return <code>true</code> if the data was removed; otherwise <code>false</code>.
	 */
	public boolean removeUserData(Object userData) {
		return (this.userData!=null && this.userData.remove(userData));
	}
	
	/** Replies the number of user data.
	 * 
	 * @return the number of user data.
	 */
	public int getUserDataCount() {
		return (this.userData==null) ? 0 : this.userData.size();
	}
	
	/** Replies the user data at the given index.
	 * 
	 * @param index is the index of the data.
	 * @return the data
	 * @throws IndexOutOfBoundsException
	 */
	public Object getUserDataAt(int index) {
		if (this.userData==null) throw new IndexOutOfBoundsException();
		return this.userData.get(index);
	}
	
	/** Set the user data at the given index.
	 * 
	 * @param index is the index of the data.
	 * @param data is the data
	 * @throws IndexOutOfBoundsException
	 */
	public void setUserDataAt(int index, Object data) {
		if (this.userData==null) throw new IndexOutOfBoundsException();
		this.userData.set(index, data);
	}

	/** Replies all the user data.
	 * 
	 * @return an unmodifiable collection of user data.
	 */
	public Collection<Object> getAllUserData() {
		if (this.userData==null) return Collections.emptyList();
		return Collections.unmodifiableCollection(this.userData);
	}

}
