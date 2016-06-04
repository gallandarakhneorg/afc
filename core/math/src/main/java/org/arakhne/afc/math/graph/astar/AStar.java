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

package org.arakhne.afc.math.graph.astar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.graph.GraphPath;
import org.arakhne.afc.math.graph.GraphPoint;
import org.arakhne.afc.math.graph.GraphPoint.GraphPointConnection;
import org.arakhne.afc.math.graph.GraphSegment;
import org.arakhne.afc.util.ListUtil;
import org.arakhne.afc.vmutil.locale.Locale;


/** This class provides an implementation of the
 * famous A* algorithm.
 *
 * @param <GP> is the type of the graph graph itself.
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class AStar<GP extends GraphPath<GP, ST, PT>, ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>> {

	private AStarHeuristic<? super PT> heuristic;

	private AStarPathFactory<GP, ST, PT> pathFactory;

	private AStarSegmentOrientation<ST, PT> segmentOrientation;

	private AStarCostComputer<? super ST, ? super PT> costComputer;

	private AStarSegmentReplacer<ST> segmentReplacer;

	private Collection<AStarListener<ST, PT>> listeners;

	private boolean enableClosedNodeReopening = true;

	/**
	 * @param heuristic is the heuristic to use by the A* algorithm.
	 * @param pathFactory1 is the factory to create new paths.
	 */
	public AStar(AStarHeuristic<? super PT> heuristic, AStarPathFactory<GP, ST, PT> pathFactory1) {
		this.heuristic = heuristic;
		this.pathFactory = pathFactory1;
	}

	/**
	 * @param heuristic is the heuristic to use by the A* algorithm.
	 * @param pathType is the type of the path to create.
	 */
	public AStar(AStarHeuristic<? super PT> heuristic, Class<? extends GP> pathType) {
		this(heuristic, new AStarReflectionPathFactory<>(pathType));
	}

	/** Add listener on A* algorithm events.
	 *
	 * @param listener the listener.
	 */
	public void addAStarListener(AStarListener<ST, PT> listener) {
		if (this.listeners == null) {
			this.listeners = new ArrayList<>();
		}
		this.listeners.add(listener);
	}

	/** Remove listener on A* algorithm events.
	 *
	 * @param listener the listener.
	 */
	public void removeAStarListener(AStarListener<ST, PT> listener) {
		if (this.listeners != null) {
			this.listeners.remove(listener);
			if (this.listeners.isEmpty()) {
				this.listeners = null;
			}
		}
	}

	private void fireAlgorithmStart(AStarNode<ST, PT> startPoint, PT endPoint) {
		if (this.listeners != null) {
			for (final AStarListener<ST, PT> listener : this.listeners) {
				listener.algorithmStarted(startPoint, endPoint);
			}
		}
	}

	private void fireNodeOpened(AStarNode<ST, PT> node, List<AStarNode<ST, PT>> openList) {
		if (this.listeners != null) {
			for (final AStarListener<ST, PT> listener : this.listeners) {
				listener.nodeOpened(node, openList);
			}
		}
	}

	private void fireNodeConsumed(AStarNode<ST, PT> node, List<AStarNode<ST, PT>> openList) {
		if (this.listeners != null) {
			for (final AStarListener<ST, PT> listener : this.listeners) {
				listener.nodeConsumed(node, openList);
			}
		}
	}

	private void fireNodeReopened(AStarNode<ST, PT> node, List<AStarNode<ST, PT>> openList) {
		if (this.listeners != null) {
			for (final AStarListener<ST, PT> listener : this.listeners) {
				listener.nodeReopened(node, openList);
			}
		}
	}

	private void fireNodeClosed(AStarNode<ST, PT> node, List<AStarNode<ST, PT>> closeList) {
		if (this.listeners != null) {
			for (final AStarListener<ST, PT> listener : this.listeners) {
				listener.nodeClosed(node, closeList);
			}
		}
	}

	private void fireAlgorithmEnd(List<AStarNode<ST, PT>> closeList) {
		if (this.listeners != null) {
			for (final AStarListener<ST, PT> listener : this.listeners) {
				listener.algorithmEnded(closeList);
			}
		}
	}

	/** Change the flag that permits to reopen the closed A* nodes.
	 *
	 * @param enableClosedNodeReopening1 is <code>true</code> to enable the closed
	 *     A* nodes; <code>false</code> to never reopen the closed A* nodes.
	 */
	public void setClosedNodeReopeningEnabled(boolean enableClosedNodeReopening1) {
		this.enableClosedNodeReopening = enableClosedNodeReopening1;
	}

	/** Replies the flag that permits to reopen the closed A* nodes.
	 *
	 * @return <code>true</code> if the closed
	 *     A* nodes could be reopened; <code>false</code> if the closed A* nodes
	 *     will be never reopened.
	 */
	@Pure
	public boolean isClosedNodeReopeningEnabled() {
		return this.enableClosedNodeReopening;
	}

	/** Set the path factory used by the A* algorithm.
	 *
	 * @param factory is the new factory.
	 * @return the old factory
	 * @see #setPathType(Class)
	 */
	public AStarPathFactory<GP, ST, PT> setPathFactory(AStarPathFactory<GP, ST, PT> factory) {
		final AStarPathFactory<GP, ST, PT> old = this.pathFactory;
		this.pathFactory = factory;
		return old;
	}

	/** Set the path factory used by the A* algorithm.
	 *
	 * @param type is the type of path to instance with a reflection-based factory.
	 * @return the old factory
	 * @see #setPathFactory(AStarPathFactory)
	 */
	public AStarPathFactory<GP, ST, PT> setPathType(Class<? extends GP> type) {
		final AStarPathFactory<GP, ST, PT> old = this.pathFactory;
		this.pathFactory = new AStarReflectionPathFactory<>(type);
		return old;
	}

	/** Replies the path factory used by the A* algorithm.
	 *
	 * @return the factory
	 */
	@Pure
	public AStarPathFactory<GP, ST, PT> getPathFactory() {
		return this.pathFactory;
	}

	/** Set the evaluation heuristic used by the A* algorithm.
	 *
	 * @param heuristic is the evaluation heuristic.
	 * @return the old heurisstic.
	 */
	public AStarHeuristic<? super PT> setEvaluationHeuristic(AStarHeuristic<? super PT> heuristic) {
		final AStarHeuristic<? super PT> old = this.heuristic;
		this.heuristic = heuristic;
		return old;
	}

	/** Replies the evaluation heuristic used by the A* algorithm.
	 *
	 * @return the heurisstic.
	 */
	@Pure
	public AStarHeuristic<? super PT> getEvaluationHeuristic() {
		return this.heuristic;
	}

	/** Set the object to use to replace the segments in the shortest path.
	 *
	 * @param replacer is the new replacer.
	 * @return the old replacer
	 */
	public AStarSegmentReplacer<ST> setSegmentReplacer(AStarSegmentReplacer<ST> replacer) {
		final AStarSegmentReplacer<ST> old = this.segmentReplacer;
		this.segmentReplacer = replacer;
		return old;
	}

	/** Replies the object used to replace the segments in the shortest path.
	 *
	 * @return the segment replacer, or <code>null</code> if none.
	 */
	@Pure
	public AStarSegmentReplacer<ST> getSegmentReplacer() {
		return this.segmentReplacer;
	}

	/** Set the tool that permits to retreive the orinetation of the segments.
	 *
	 * @param tool the tool for retreiving the orientation of the segments.
	 * @return the old tool.
	 */
	public AStarSegmentOrientation<ST, PT> setSegmentOrientationTool(AStarSegmentOrientation<ST, PT> tool) {
		final AStarSegmentOrientation<ST, PT> old = this.segmentOrientation;
		this.segmentOrientation = tool;
		return old;
	}

	/** Replies the tool that permits to retreive the orinetation of the segments..
	 *
	 * @return the tool.
	 */
	@Pure
	public AStarSegmentOrientation<ST, PT> getSegmentOrientationTool() {
		return this.segmentOrientation;
	}

	/** Set the tool that permits to compute the costs of the nodes and the edges.
	 *
	 * @param costComputer is the object that permits to compute the costs.
	 * @return the old cost computer.
	 */
	public AStarCostComputer<? super ST, ? super PT> setCostComputer(AStarCostComputer<? super ST, ? super PT> costComputer) {
		final AStarCostComputer<? super ST, ? super PT> old = this.costComputer;
		this.costComputer = costComputer;
		return old;
	}

	/** Replies the tool that permits to compute the costs of the nodes and edges.
	 *
	 * @return the cost computer
	 */
	@Pure
	public AStarCostComputer<? super ST, ? super PT> getCostComputer() {
		return this.costComputer;
	}

	/** Evaluate the distance between two points in the graph.
	 *
	 * <p>By default, this function uses the heuristic passed as parameter
	 * of the constructor.
	 *
	 * @param p1 the first point.
	 * @param p2 the second point.
	 * @return the evaluated distance between {@code p1} and {@code p2}.
	 */
	@Pure
	protected double estimate(PT p1, PT p2) {
		assert p1 != null && p2 != null;
		if (this.heuristic == null) {
			throw new IllegalStateException(Locale.getString("E1")); //$NON-NLS-1$
		}
		return this.heuristic.evaluate(p1, p2);
	}

	/** Compute and replies the cost to traverse the given graph point.
	 *
	 * @param pt the point.
	 * @return the cost to traverse the point.
	 */
	@Pure
	protected double computeCostFor(PT pt) {
		if (this.costComputer != null) {
			return this.costComputer.computeCostFor(pt);
		}
		return 0;
	}

	/** Compute and replies the cost to traverse the given graph segment.
	 *
	 * @param segment the segment.
	 * @return the cost to traverse the segment.
	 */
	@Pure
	protected double computeCostFor(ST segment) {
		if (this.costComputer != null) {
			return this.costComputer.computeCostFor(segment);
		}
		return segment.getLength();
	}

	/** Translate the A* node.
	 * This function is invoked prior to any treatment with
	 * the given A* node. It is assumed that this
	 * function replies a good translation of the given node
	 * for the A* algorithm; or it replies <code>null</code>
	 * if the given node is the target node of the A* algorithm,
	 * ie. it corresponds to the given endPoint.
	 *
	 * <p>By default, this function replies the node itself.
	 *
	 * @param endPoint is the end point given to solve function.
	 * @param node is the current A* node to translate.
	 * @return the translation of the node, or <code>null</code> if
	 *     the node corresponds to the endPoint.
	 */
	@Pure
	protected AStarNode<ST, PT> translateCandidate(PT endPoint, AStarNode<ST, PT> node) {
		if (endPoint.equals(node.getGraphPoint())) {
			return null;
		}
		return node;
	}

	/** Create an empty path.
	 *
	 * <p>By default this function invokes the path factory
	 * passed as parameter of the constructor.
	 *
	 * @param startPoint is the first point in the path.
	 * @param segment is the first connection to follow.
	 * @return the path instance.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Pure
	protected GP newPath(PT startPoint, ST segment) {
		if (this.pathFactory != null) {
			return this.pathFactory.newPath(startPoint, segment);
		}
		try {
			return (GP) new GraphPath(segment, startPoint);
		} catch (Throwable e) {
			throw new IllegalStateException(Locale.getString("E2"), e); //$NON-NLS-1$
		}
	}

	/** Add the given segment into the given path.
	 *
	 * <p>By default this function invokes the path factory
	 * passed as parameter of the constructor.
	 *
	 * @param path is the path to build.
	 * @param segment is the segment to add.
	 * @return <code>true</code> if the segment was added;
	 *     otherwise <code>false</code>.
	 */
	protected boolean addToPath(GP path, ST segment) {
		if (this.pathFactory != null) {
			return this.pathFactory.addToPath(path, segment);
		}
		assert path != null;
		assert segment != null;
		try {
			return path.add(segment);
		} catch (Throwable e) {
			throw new IllegalStateException("no path factory found", e); //$NON-NLS-1$
		}
	}

	/** Run the A* algorithm assuming that the graph is oriented is
	 * an orientation tool was passed to the constructor.
	 *
	 * <p>The orientation of the graph may also be overridden by the implementations
	 * of the {@link AStarNode A* nodes}.
	 *
	 * @param startPoint is the starting point.
	 * @param endPoint is the point to reach.
	 * @return the found path, or <code>null</code> if none found.
	 */
	@Pure
	public final GP solve(PT startPoint, PT endPoint) {
		return solve(
				node(startPoint,
						0f,
						estimate(startPoint, endPoint), null),
				endPoint);
	}

	/** Run the A* algorithm assuming that the graph is oriented is
	 * an orientation tool was passed to the constructor.
	 *
	 * <p>The orientation of the graph may also be overridden by the implementations
	 * of the {@link AStarNode A* nodes}.
	 *
	 * @param startPoint is the starting point.
	 * @param endPoint is the point to reach.
	 * @return the found path, or <code>null</code> if none found.
	 */
	protected GP solve(AStarNode<ST, PT> startPoint, PT endPoint) {
		final List<AStarNode<ST, PT>> closeList;

		fireAlgorithmStart(startPoint, endPoint);

		// Run A*
		closeList = findPath(startPoint, endPoint);
		if (closeList == null || closeList.isEmpty()) {
			return null;
		}

		fireAlgorithmEnd(closeList);

		// Create the path
		return createPath(startPoint, endPoint, closeList);
	}

	/** Create a instance of {@link AStarNode A* node}.
	 *
	 * @param node is the node of the graph to put in the A* node.
	 * @param cost is the cost to reach the node.
	 * @param estimatedCost is the estimated cost to reach the target.
	 * @param arrival is the segment, which permits to arrive at the node.
	 * @return the A* node.
	 */
	@SuppressWarnings("unchecked")
	private AStarNode<ST, PT> node(PT node, double cost, double estimatedCost, ST arrival) {
		final AStarNode<ST, PT> aNode;

		if (node instanceof AStarNode<?, ?>) {
			aNode = (AStarNode<ST, PT>) node;
			aNode.setArrivalConnection(arrival);
			aNode.setCost(cost);
			aNode.setEstimatedCost(estimatedCost);
		} else {
			aNode = newAStarNode(node, cost, estimatedCost, arrival);
		}

		return aNode;
	}

	/** Create a instance of {@link AStarNode A* node}.
	 *
	 * @param node is the node of the graph to put in the A* node.
	 * @param cost is the cost to reach the node.
	 * @param estimatedCost is the estimated cost to reach the target.
	 * @param arrival is the segment, which permits to arrive at the node.
	 * @return the A* node.
	 */
	@Pure
	protected AStarNode<ST, PT> newAStarNode(PT node, double cost, double estimatedCost, ST arrival) {
		return new Candidate(arrival, node, cost, estimatedCost);
	}

	/** Run the A* algorithm and tries to find a path from
	 *  the startPoint to the endPoint.
	 *
	 * @param startPoint is the starting point.
	 * @param endPoint is the point to reach.
	 * @return the close list of the A* algorithm.
	 */
	@Pure
	@SuppressWarnings("checkstyle:nestedifdepth")
	List<AStarNode<ST, PT>> findPath(AStarNode<ST, PT> startPoint, PT endPoint) {
		final CloseComparator<ST, PT> cComparator = new CloseComparator<>();
		final OpenComparator<ST, PT> oComparatorWithoutRef = new OpenComparator<>();
		final List<AStarNode<ST, PT>> openList = new ArrayList<>();
		final List<AStarNode<ST, PT>> closeList = new ArrayList<>();

		openList.add(startPoint);
		fireNodeOpened(startPoint, openList);

		int idx;
		AStarNode<ST, PT> candidate;
		AStarNode<ST, PT> ocandidate;
		AStarNode<ST, PT> reachableCandidate;
		AStarNode<ST, PT> reachedCandidate;
		PT reachableNode;
		PT node;
		double gCost;
		double h1;
		boolean foundTarget = false;

		while (!foundTarget && !openList.isEmpty()) {

			ocandidate = openList.remove(0);
			fireNodeConsumed(ocandidate, openList);

			candidate = translateCandidate(endPoint, ocandidate);
			foundTarget = candidate == null;

			if (!foundTarget) {
				assert candidate != null;
				node = candidate.getGraphPoint();
				// Update the nodes that are reachable from the current candidate.
				for (final ST segment : candidate.getGraphSegments()) {
					reachableNode = segment.getOtherSidePoint(node);
					if (reachableNode != null && !reachableNode.equals(node)) {
						gCost = candidate.getCost()
								+ computeCostFor(node)
								+ computeCostFor(segment);
						h1 = estimate(reachableNode, endPoint);
						reachableCandidate = newAStarNode(reachableNode, gCost, h1, segment);

						// Reopen node if better cost
						idx = (isClosedNodeReopeningEnabled())
								? ListUtil.indexOf(closeList, cComparator, reachableCandidate)
										: -1;
						if (idx >= 0) {
							reachedCandidate = closeList.get(idx);
							if (gCost < reachedCandidate.getCost()) {
								closeList.remove(idx);
								final AStarNode<ST, PT> nn = node(
										reachableNode,
										gCost,
										h1,
										segment);
								ListUtil.add(
										openList,
										oComparatorWithoutRef,
										nn,
										true, false);
								fireNodeOpened(nn, openList);
							}
						} else {
							idx = openList.indexOf(reachableCandidate);
							if (idx >= 0) {
								// Rearrange open list if better cost
								reachedCandidate = openList.get(idx);
								if ((gCost + h1) < reachedCandidate.getPathCost()) {
									openList.remove(idx);
									final AStarNode<ST, PT> nn = node(
											reachableNode,
											gCost,
											h1,
											segment);
									ListUtil.add(openList,
											oComparatorWithoutRef,
											nn,
											true, false);
									fireNodeReopened(nn, openList);
								}
							} else {
								// Node was neither treated nor seen, add it
								final AStarNode<ST, PT> nn = node(
										reachableNode,
										gCost,
										h1,
										segment);
								ListUtil.add(openList,
										oComparatorWithoutRef,
										nn,
										true, false);
								fireNodeOpened(nn, openList);
							}
						}
					}
				}
			}

			// Refresh the close list
			idx = ListUtil.add(closeList, cComparator, ocandidate, false, true);
			fireNodeClosed(ocandidate, closeList);
			assert idx >= 0;
		}

		return closeList;
	}

	/** Create the path from the given close list.
	 *
	 * @param startPoint is the starting point.
	 * @param endPoint is the ending point.
	 * @param closeList is the close list.
	 * @return the path, or <code>null</code> if no path found.
	 */
	@Pure
	GP createPath(AStarNode<ST, PT> startPoint, PT endPoint, List<AStarNode<ST, PT>> closeList) {
		int idx;
		ST segment;
		PT point;
		AStarNode<ST, PT> node;
		GP path = null;
		final CloseComparator<ST, PT> cComparator = new CloseComparator<>();

		node = newAStarNode(endPoint, Double.NaN, Double.NaN, null);
		idx = ListUtil.indexOf(closeList, cComparator, node);

		if (idx >= 0) {
			node = closeList.remove(idx);
			point = node.getGraphPoint();
			segment = node.getArrivalConnection();
			if (point != null && segment != null) {
				final LinkedList<ST> pathSegments = new LinkedList<>();
				pathSegments.add(segment);
				do {
					point = segment.getOtherSidePoint(point);
					node = newAStarNode(point, Double.NaN, Double.NaN, null);
					idx = ListUtil.indexOf(closeList, cComparator, node);
					if (idx >= 0) {
						node = closeList.remove(idx);
						segment = node.getArrivalConnection();
						if (segment != null) {
							pathSegments.add(segment);
						}
					} else {
						point = null;
						segment = null;
					}
				}
				while (point != null && segment != null);

				// Building of the path is done is a second step
				// because the oriented graph cannot enable to
				// built it during the previous step loop:
				// isConnectedSegment replies false when tested
				// with an arrival segment and cannot
				// enable to go backward along the path.
				final Iterator<ST> iterator = pathSegments.descendingIterator();
				if (iterator.hasNext()) {
					segment = iterator.next();
					if (startPoint.getGraphPoint().isConnectedSegment(segment)) {
						path = newPath(startPoint.getGraphPoint(), segment);
						while (iterator.hasNext()) {
							addToPath(path, iterator.next());
						}
					}
				}
			}
		}

		return path;
	}

	/** Invoked to replace a segment before adding it to the shortest path.
	 *
	 * <p>* By default, this function invoked the {@link AStarSegmentReplacer}
	 * associated to this AStar algorithm.
	 *
	 * @param index is the position of the segment in the path.
	 * @param segment is the segment to replace.
	 * @return the replacement segment or the {@code segment} itself.
	 */
	@Pure
	protected ST replaceSegment(int index, ST segment) {
		ST rep = null;
		if (this.segmentReplacer != null) {
			rep = this.segmentReplacer.replaceSegment(index, segment);
		}
		if (rep == null) {
			rep = segment;
		}
		return rep;
	}

	/** Invoked when a segment could not be added into the
	 * path.
	 *
	 * <p>In standard AStar implementation, this function replies <code>false</code>.
	 *
	 * @param index is the index of the invalid segment.
	 * @param segment is the segment that cannot be added
	 * @param path is the current state of the path.
	 * @return <code>true</code> if the path building should continue,
	 *     <code>false</code> if the path building should stop and
	 *     replies a <code>null</code> path.
	 */
	@Pure
	protected boolean invalidPathSegmentFound(int index, ST segment, GP path) {
		return false;
	}

	/** Candidate node in the A* algorithm, and its related information.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	protected class Candidate implements AStarNode<ST, PT> {

		private ST segment;

		private double costToReach;

		private double estimatedCost;

		private final PT entryPoint;

		/** Construct a candidate.
		 *
		 * @param segment the segment
		 * @param entryPoint the entry point.
		 * @param costToReach the cost to reach the entry point.
		 * @param estimatedCost the estimated cost between the entry point and the target point.
		 */
		public Candidate(ST segment, PT entryPoint, double costToReach, double estimatedCost) {
			this.segment = segment;
			this.entryPoint = entryPoint;
			this.costToReach = costToReach;
			this.estimatedCost = estimatedCost;
		}

		@Pure
		@Override
		public String toString() {
			final StringBuilder b = new StringBuilder();
			b.append("{"); //$NON-NLS-1$
			b.append(this.entryPoint.getClass().getName());
			b.append("|"); //$NON-NLS-1$
			b.append(System.identityHashCode(this.entryPoint));
			b.append("|"); //$NON-NLS-1$
			b.append(this.costToReach);
			b.append("|"); //$NON-NLS-1$
			b.append(this.estimatedCost);
			b.append("|"); //$NON-NLS-1$
			b.append(this.costToReach + this.estimatedCost);
			b.append("|"); //$NON-NLS-1$
			b.append(this.entryPoint.toString());
			b.append("}"); //$NON-NLS-1$
			return b.toString();
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof AStarNode<?, ?>) {
				return this.entryPoint.equals(((AStarNode<?, ?>) obj).getGraphPoint());
			}
			if (obj instanceof GraphPoint<?, ?>) {
				return this.entryPoint.equals(obj);
			}
			return super.equals(obj);
		}

		@Pure
		@Override
		public int hashCode() {
			return this.entryPoint.hashCode();
		}

		@Pure
		@Override
		public PT getGraphPoint() {
			return this.entryPoint;
		}

		@Pure
		@Override
		public ST getArrivalConnection() {
			return this.segment;
		}

		@Override
		public ST setArrivalConnection(ST connection) {
			this.segment = connection;
			return this.segment;
		}

		@Pure
		@Override
		public double getCost() {
			return this.costToReach;
		}

		@Override
		public double setCost(double cost) {
			this.costToReach = cost;
			return this.costToReach;
		}

		@Pure
		@Override
		public double getEstimatedCost() {
			return this.estimatedCost;
		}

		@Override
		public double setEstimatedCost(double cost) {
			this.estimatedCost = cost;
			return this.estimatedCost;
		}

		@Pure
		@Override
		public double getPathCost() {
			return this.costToReach + this.estimatedCost;
		}

		@Pure
		@Override
		public Iterable<ST> getGraphSegments() {
			final AStarSegmentOrientation<ST, PT> tool = getSegmentOrientationTool();
			if (tool != null) {
				return new OrientedConnectionCollection(this.segment, this.entryPoint.getConnections());
			}
			return this.entryPoint.getConnectedSegments();
		}

	} // class Candidate

	/** Comparator used to sort the open list of A* algorithm.
	 *
	 * @param <PT> is the type of node in the graph
	 * @param <ST> is the type of edge in the graph
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	protected static class OpenComparator<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>>
			implements Comparator<AStarNode<ST, PT>> {

		/** Construct the comparator for the A* open list.
		 */
		public OpenComparator() {
			//
		}

		@Pure
		@Override
		public int compare(AStarNode<ST, PT> o1, AStarNode<ST, PT> o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return Integer.MIN_VALUE;
			}
			if (o2 == null) {
				return Integer.MAX_VALUE;
			}
			final int cmp = Double.compare(o1.getPathCost(), o2.getPathCost());
			if (cmp != 0) {
				return cmp;
			}
			return o1.getGraphPoint().compareTo(o2.getGraphPoint());
		}

	}

	/** Comparator used to sort the close list of A* algorithm.
	 *
	 * @param <PT> is the type of node in the graph
	 * @param <ST> is the type of edge in the graph
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	protected static class CloseComparator<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>>
			implements Comparator<AStarNode<ST, PT>> {

		/** Construct the comparator for the A* close list.
		 */
		public CloseComparator() {
			//
		}

		@Pure
		@Override
		public int compare(AStarNode<ST, PT> o1, AStarNode<ST, PT> o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return Integer.MIN_VALUE;
			}
			if (o2 == null) {
				return Integer.MAX_VALUE;
			}
			return o1.getGraphPoint().compareTo(o2.getGraphPoint());
		}

	}

	/** Iterable that contains the oriented connection from a point.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class OrientedConnectionCollection implements Iterable<ST> {

		private final Iterable<? extends GraphPointConnection<PT, ST>> connections;

		private final ST entry;

		/**
		 * @param entry the entry connection.
		 * @param connections the exit connections.
		 */
		OrientedConnectionCollection(ST entry, Iterable<? extends GraphPointConnection<PT, ST>> connections) {
			this.connections = connections;
			this.entry = entry;
		}

		@Pure
		@Override
		public Iterator<ST> iterator() {
			return new OrientedConnectionIterator(this.entry, this.connections.iterator());
		}

	}

	/** Iterator on oriented connections.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class OrientedConnectionIterator implements Iterator<ST> {

		private final ST entry;

		private final Iterator<? extends GraphPointConnection<PT, ST>> iterator;

		private ST next;

		/**
		 * @param entry the entry connection.
		 * @param iterator the exit connections.
		 */
		OrientedConnectionIterator(ST entry, Iterator<? extends GraphPointConnection<PT, ST>> iterator) {
			this.entry = entry;
			this.iterator = iterator;
			searchNext();
		}

		private void searchNext() {
			final AStarSegmentOrientation<ST, PT> orientation = getSegmentOrientationTool();
			assert orientation != null;
			this.next = null;
			GraphPointConnection<PT, ST> connection;
			while (this.next == null && this.iterator.hasNext()) {
				connection = this.iterator.next();
				if (orientation.isTraversable(this.entry, connection)) {
					this.next = connection.getGraphSegment();
				}
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public ST next() {
			final ST n = this.next;
			if (n == null) {
				throw new NoSuchElementException();
			}
			searchNext();
			return n;
		}

	}

	/**
	 * @param <GP> is the type of the graph graph itself.
	 * @param <PT> is the type of node in the graph
	 * @param <ST> is the type of edge in the graph
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private static class AStarReflectionPathFactory<GP extends GraphPath<GP, ST, PT>, ST extends GraphSegment<ST, PT>,
			PT extends GraphPoint<PT, ST>> implements AStarPathFactory<GP, ST, PT> {

		private final Class<? extends GP> type;

		/**
		 * @param type type of the graph path to create.
		 */
		AStarReflectionPathFactory(Class<? extends GP> type) {
			this.type = type;
		}

		@Pure
		@Override
		public GP newPath(PT startPoint, ST segment) {
			try {
				final GP path = this.type.newInstance();
				path.add(segment, startPoint);
				path.setFirstSegmentReversable(false);
				return path;
			} catch (InstantiationException e) {
				throw new IllegalArgumentException(e);
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			}
		}

	}

}
