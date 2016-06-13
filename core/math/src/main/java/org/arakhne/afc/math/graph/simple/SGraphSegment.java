/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.graph.simple;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.graph.GraphSegment;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** This class provides a simple implementation of a graph's segment
 * for a {@link SGraph}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class SGraphSegment implements GraphSegment<SGraphSegment, SGraphPoint> {

	private final WeakReference<SGraph> graph;

	private double length;

	private SGraphPoint startPoint;

	private SGraphPoint endPoint;

	private List<Object> userData;

	/**
	 * @param graph1 is the graph in which the segment is.
	 */
	public SGraphSegment(SGraph graph1) {
		this(graph1, Double.NaN);
	}

	/**
	 * @param graph1 is the graph in which the segment is.
	 * @param length1 is the length of the segment.
	 */
	public SGraphSegment(SGraph graph1, double length1) {
		assert graph1 != null;
		this.length = length1;
		graph1.add(this);
		this.graph = new WeakReference<>(graph1);
		this.startPoint = new SGraphPoint(graph1);
		this.endPoint = new SGraphPoint(graph1);
	}

	/** Replies the graph in which this segment is.
	 *
	 * @return the graph in which this segment is.
	 */
	@Pure
	public SGraph getGraph() {
		return this.graph.get();
	}

	private void setBegin(SGraphPoint pt) {
		if (this.startPoint != null) {
			pt.add(this.startPoint.getConnectedSegments());
			this.startPoint.clear();
		}
		this.startPoint = pt;
	}

	private void setEnd(SGraphPoint pt) {
		if (this.endPoint != null) {
			pt.add(this.endPoint.getConnectedSegments());
			this.endPoint.clear();
		}
		this.endPoint = pt;
	}

	/** Connect the begin point of this segment to the
	 * begin point of the given segment.
	 * This function change the connection points of the two segments.
	 *
	 * @param segment the segment.
	 * @throws IllegalArgumentException if the given segment is not in the same graph.
	 */
	public void connectBeginToBegin(SGraphSegment segment) {
		if (segment.getGraph() != getGraph()) {
			throw new IllegalArgumentException();
		}
		final SGraphPoint point = new SGraphPoint(getGraph());
		setBegin(point);
		segment.setBegin(point);

		final SGraph g = getGraph();
		assert g != null;
		g.updatePointCount(-1);
	}

	/** Connect the begin point of this segment to the
	 * end point of the given segment.
	 * This function change the connection points of the two segments.
	 *
	 * @param segment the segment.
	 * @throws IllegalArgumentException if the given segment is not in the same graph.
	 */
	public void connectBeginToEnd(SGraphSegment segment) {
		if (segment.getGraph() != getGraph()) {
			throw new IllegalArgumentException();
		}
		final SGraphPoint point = new SGraphPoint(getGraph());
		setBegin(point);
		segment.setEnd(point);

		final SGraph g = getGraph();
		assert g != null;
		g.updatePointCount(-1);
	}

	/** Connect the end point of this segment to the
	 * begin point of the given segment.
	 * This function change the connection points of the two segments.
	 *
	 * @param segment the segment.
	 * @throws IllegalArgumentException if the given segment is not in the same graph.
	 */
	public void connectEndToBegin(SGraphSegment segment) {
		if (segment.getGraph() != getGraph()) {
			throw new IllegalArgumentException();
		}
		final SGraphPoint point = new SGraphPoint(getGraph());
		setEnd(point);
		segment.setBegin(point);

		final SGraph g = getGraph();
		assert g != null;
		g.updatePointCount(-1);
	}

	/** Connect the end point of this segment to the
	 * end point of the given segment.
	 * This function change the connection points of the two segments.
	 *
	 * @param segment the segment.
	 * @throws IllegalArgumentException if the given segment is not in the samegraph.
	 */
	public void connectEndToEnd(SGraphSegment segment) {
		if (segment.getGraph() != getGraph()) {
			throw new IllegalArgumentException();
		}
		final SGraphPoint point = new SGraphPoint(getGraph());
		setEnd(point);
		segment.setEnd(point);

		final SGraph g = getGraph();
		assert g != null;
		g.updatePointCount(-1);
	}

	/** Disconnect the begin point of this segment.
	 */
	public void disconnectBegin() {
		if (this.startPoint != null) {
			this.startPoint.remove(this);
		}
		this.startPoint = new SGraphPoint(getGraph());
		this.startPoint.add(this);
	}

	/** Disconnect the end point of this segment.
	 */
	public void disconnectEnd() {
		if (this.endPoint != null) {
			this.endPoint.remove(this);
		}
		this.endPoint = new SGraphPoint(getGraph());
		this.endPoint.add(this);
	}

	@Override
	public SGraphPoint getBeginPoint() {
		return this.startPoint;
	}

	@Pure
	@Override
	public SGraphPoint getEndPoint() {
		return this.endPoint;
	}

	@Pure
	@Override
	public SGraphPoint getOtherSidePoint(SGraphPoint point) {
		if (point != null) {
			if (point.equals(this.startPoint)) {
				return this.endPoint;
			}
			if (point.equals(this.endPoint)) {
				return this.startPoint;
			}
		}
		return null;
	}

	@Pure
	@Override
	public double getLength() {
		return this.length;
	}

	/** Set the length of the segment.
	 *
	 * @param length is the length of the segment.
	 */
	public void setLength(double length) {
		assert length >= 0. : AssertMessages.positiveOrZeroParameter();
		this.length = length;
	}

	/** Add a user data in the data associated to this point.
	 *
	 * @param userData the user data to add.
	 * @return <code>true</code> if the data was added; otherwise <code>false</code>.
	 */
	public boolean addUserData(Object userData) {
		if (this.userData == null) {
			this.userData = new ArrayList<>();
		}
		return this.userData.add(userData);
	}

	/** Remove a user data from the data associated to this point.
	 *
	 * @param userData the user data to remove.
	 * @return <code>true</code> if the data was removed; otherwise <code>false</code>.
	 */
	public boolean removeUserData(Object userData) {
		return this.userData != null && this.userData.remove(userData);
	}

	/** Replies the number of user data.
	 *
	 * @return the number of user data.
	 */
	@Pure
	public int getUserDataCount() {
		return (this.userData == null) ? 0 : this.userData.size();
	}

	/** Replies the user data at the given index.
	 *
	 * @param index is the index of the data.
	 * @return the data
	 */
	@Pure
	public Object getUserDataAt(int index) {
		if (this.userData == null) {
			throw new IndexOutOfBoundsException();
		}
		return this.userData.get(index);
	}

	/** Set the user data at the given index.
	 *
	 * @param index is the index of the data.
	 * @param data is the data
	 */
	public void setUserDataAt(int index, Object data) {
		if (this.userData == null) {
			throw new IndexOutOfBoundsException();
		}
		this.userData.set(index, data);
	}

	/** Replies all the user data.
	 *
	 * @return an unmodifiable collection of user data.
	 */
	@Pure
	public Collection<Object> getAllUserData() {
		if (this.userData == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableCollection(this.userData);
	}

}
