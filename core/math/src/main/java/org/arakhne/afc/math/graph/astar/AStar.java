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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.graph.GraphPath;
import org.arakhne.afc.math.graph.GraphPoint;
import org.arakhne.afc.math.graph.GraphPoint.GraphPointConnection;
import org.arakhne.afc.math.graph.GraphSegment;
import org.arakhne.afc.util.CollectionUtil;


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
 * @since 4.0
 */
public class AStar<GP extends GraphPath<GP,ST,PT>, ST extends GraphSegment<ST,PT>, PT extends GraphPoint<PT,ST>> {

	private AStarHeuristic<? super PT> h;
	private AStarPathFactory<GP,ST,PT> pathFactory;
	private AStarSegmentOrientation<ST,PT> segmentOrientation = null;
	private AStarCostComputer<? super ST, ? super PT> costComputer = null;
	private AStarSegmentReplacer<ST> segmentReplacer = null;
	
	private Collection<AStarListener<ST,PT>> listeners = null;
	
	private boolean enableClosedNodeReopening = true;
	
	/**
	 * @param heuristic is the heuristic to use by the A* algorithm.
	 * @param pathFactory is the factory to create new paths.
	 */
	public AStar(AStarHeuristic<? super PT> heuristic, AStarPathFactory<GP,ST,PT> pathFactory) {
		this.h = heuristic;
		this.pathFactory = pathFactory;
	}

	/**
	 * @param heuristic is the heuristic to use by the A* algorithm.
	 * @param pathType is the type of the path to create.
	 */
	public AStar(AStarHeuristic<? super PT> heuristic, Class<? extends GP> pathType) {
		this(heuristic, new AStarReflectionPathFactory<GP,ST,PT>(pathType));
	}
	
	/** Add listener on A* algorithm events.
	 * 
	 * @param listener
	 */
	public void addAStarListener(AStarListener<ST,PT> listener) {
		if (this.listeners==null) {
			this.listeners = new ArrayList<AStarListener<ST,PT>>();
		}
		this.listeners.add(listener);
	}
	
	/** Remove listener on A* algorithm events.
	 * 
	 * @param listener
	 */
	public void removeAStarListener(AStarListener<ST,PT> listener) {
		if (this.listeners!=null) {
			this.listeners.remove(listener);
			if (this.listeners.isEmpty()) {
				this.listeners = null;
			}
		}
	}

	private void fireAlgorithmStart(AStarNode<ST,PT> startPoint, PT endPoint) {
		if (this.listeners!=null) {
			for(AStarListener<ST,PT> listener : this.listeners) {
				listener.algorithmStarted(startPoint, endPoint);
			}
		}
	}
	
	private void fireNodeOpened(AStarNode<ST,PT> node, List<AStarNode<ST,PT>> openList) {
		if (this.listeners!=null) {
			for(AStarListener<ST,PT> listener : this.listeners) {
				listener.nodeOpened(node, openList);
			}
		}
	}

	private void fireNodeConsumed(AStarNode<ST,PT> node, List<AStarNode<ST,PT>> openList) {
		if (this.listeners!=null) {
			for(AStarListener<ST,PT> listener : this.listeners) {
				listener.nodeConsumed(node, openList);
			}
		}
	}

	private void fireNodeReopened(AStarNode<ST,PT> node, List<AStarNode<ST,PT>> openList) {
		if (this.listeners!=null) {
			for(AStarListener<ST,PT> listener : this.listeners) {
				listener.nodeReopened(node, openList);
			}
		}
	}

	private void fireNodeClosed(AStarNode<ST,PT> node, List<AStarNode<ST,PT>> closeList) {
		if (this.listeners!=null) {
			for(AStarListener<ST,PT> listener : this.listeners) {
				listener.nodeClosed(node, closeList);
			}
		}
	}

	private void fireAlgorithmEnd(List<AStarNode<ST,PT>> closeList) {
		if (this.listeners!=null) {
			for(AStarListener<ST,PT> listener : this.listeners) {
				listener.algorithmEnded(closeList);
			}
		}
	}
	
	/** Change the flag that permits to reopen the closed A* nodes.
	 * 
	 * @param enableClosedNodeReopening is <code>true</code> to enable the closed
	 * A* nodes; <code>false</code> to never reopen the closed A* nodes.
	 * @since 4.1
	 */
	public void setClosedNodeReopeningEnabled(boolean enableClosedNodeReopening) {
		this.enableClosedNodeReopening = enableClosedNodeReopening;
	}
	
	/** Replies the flag that permits to reopen the closed A* nodes.
	 * 
	 * @return <code>true</code> if the closed
	 * A* nodes could be reopened; <code>false</code> if the closed A* nodes
	 * will be never reopened.
	 * @since 4.1
	 */
	public boolean isClosedNodeReopeningEnabled() {
		return this.enableClosedNodeReopening;
	}

	/** Set the path factory used by the A* algorithm.
	 * 
	 * @param factory is the new factory.
	 * @return the old factory
	 * @see #setPathType(Class)
	 */
	public AStarPathFactory<GP,ST,PT> setPathFactory(AStarPathFactory<GP,ST,PT> factory) {
		AStarPathFactory<GP,ST,PT> old = this.pathFactory;
		this.pathFactory = factory;
		return old;
	}
	
	/** Set the path factory used by the A* algorithm.
	 * 
	 * @param type is the type of path to instance with a reflection-based factory.
	 * @return the old factory
	 * @see #setPathFactory(AStarPathFactory)
	 */
	public AStarPathFactory<GP,ST,PT> setPathType(Class<? extends GP> type) {
		AStarPathFactory<GP,ST,PT> old = this.pathFactory;
		this.pathFactory = new AStarReflectionPathFactory<GP,ST,PT>(type);
		return old;
	}

	/** Replies the path factory used by the A* algorithm.
	 * 
	 * @return the factory
	 */
	public AStarPathFactory<GP,ST,PT> getPathFactory() {
		return this.pathFactory;
	}

	/** Set the evaluation heuristic used by the A* algorithm.
	 * 
	 * @param heuristic is the evaluation heuristic.
	 * @return the old heurisstic.
	 */
	public AStarHeuristic<? super PT> setEvaluationHeuristic(AStarHeuristic<? super PT> heuristic) {
		AStarHeuristic<? super PT> old = this.h;
		this.h = heuristic;
		return old;
	}

	/** Replies the evaluation heuristic used by the A* algorithm.
	 * 
	 * @return the heurisstic.
	 */
	public AStarHeuristic<? super PT> getEvaluationHeuristic() {
		return this.h;
	}

	/** Set the object to use to replace the segments in the shortest path.
	 * 
	 * @param replacer is the new replacer.
	 * @return the old replacer
	 */
	public AStarSegmentReplacer<ST> setSegmentReplacer(AStarSegmentReplacer<ST> replacer) {
		AStarSegmentReplacer<ST> old = this.segmentReplacer;
		this.segmentReplacer = replacer;
		return old;
	}

	/** Replies the object used to replace the segments in the shortest path.
	 * 
	 * @return the segment replacer, or <code>null</code> if none.
	 */
	public AStarSegmentReplacer<ST> getSegmentReplacer() {
		return this.segmentReplacer;
	}

	/** Set the tool that permits to retreive the orinetation of the segments..
	 * 
	 * @param tool
	 * @return the old tool.
	 */
	public AStarSegmentOrientation<ST,PT> setSegmentOrientationTool(AStarSegmentOrientation<ST,PT> tool) {
		AStarSegmentOrientation<ST,PT> old = this.segmentOrientation;
		this.segmentOrientation = tool;
		return old;
	}

	/** Replies the tool that permits to retreive the orinetation of the segments..
	 * 
	 * @return the tool.
	 */
	public AStarSegmentOrientation<ST,PT> getSegmentOrientationTool() {
		return this.segmentOrientation;
	}

	/** Set the tool that permits to compute the costs of the nodes and the edges.
	 * 
	 * @param costComputer is the object that permits to compute the costs.
	 * @return the old cost computer.
	 */
	public AStarCostComputer<? super ST, ? super PT> setCostComputer(AStarCostComputer<? super ST, ? super PT> costComputer) {
		AStarCostComputer<? super ST, ? super PT> old = this.costComputer;
		this.costComputer = costComputer;
		return old;
	}

	/** Replies the tool that permits to compute the costs of the nodes and edges.
	 * 
	 * @return the cost computer
	 */
	public AStarCostComputer<? super ST, ? super PT> getCostComputer() {
		return this.costComputer;
	}

	/** Evaluate the distance between two points in the graph.
	 * <p>
	 * By default, this function uses the heuristic passed as parameter
	 * of the constructor.
	 * 
	 * @param p1
	 * @param p2
	 * @return the evaluated distance between <var>p1</var> and <var>p2</var>.
	 */
	protected float estimate(PT p1, PT p2) {
		assert(p1!=null && p2!=null);
		if (this.h==null) throw new IllegalStateException("no heuristic found"); //$NON-NLS-1$
		return this.h.evaluate(p1, p2);
	}

	/** Compute and replies the cost to traverse the given graph point.
	 * 
	 * @param p
	 * @return the cost to traverse the point.
	 */
	protected float computeCostFor(PT p) {
		if (this.costComputer!=null)
			return this.costComputer.computeCostFor(p);
		return 0f;
	}

	/** Compute and replies the cost to traverse the given graph segment.
	 * 
	 * @param s
	 * @return the cost to traverse the segment.
	 */
	protected float computeCostFor(ST s) {
		if (this.costComputer!=null)
			return this.costComputer.computeCostFor(s);
		return (float)s.getLength();
	}

	/** Translate the A* node.
	 * This function is invoked prior to any treatment with
	 * the given A* node. It is assumed that this
	 * function replies a good translation of the given node
	 * for the A* algorithm; or it replies <code>null</code>
	 * if the given node is the target node of the A* algorithm,
	 * ie. it corresponds to the given endPoint.
	 * <p>
	 * By default, this function replies the node itself.
	 * 
	 * @param endPoint is the end point given to solve function.
	 * @param node is the current A* node to translate.
	 * @return the translation of the node, or <code>null</code> if
	 * the node corresponds to the endPoint.
	 */
	protected AStarNode<ST,PT> translateCandidate(PT endPoint, AStarNode<ST,PT> node) {
		if (endPoint.equals(node.getGraphPoint())) return null;
		return node;
	}

	/** Create an empty path.
	 * <p>
	 * By default this function invokes the path factory
	 * passed as parameter of the constructor.
	 * 
	 * @param startPoint is the first point in the path.
	 * @param segment is the first connection to follow.
	 * @return the path instance.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected GP newPath(PT startPoint, ST segment) {
		if (this.pathFactory!=null) {
			return this.pathFactory.newPath(startPoint, segment);
		}
		try {
			return (GP)new GraphPath(segment, startPoint);
		}
		catch(Throwable e) {
			throw new IllegalStateException("no path factory found", e); //$NON-NLS-1$
		}
	}

	/** Add the given segment into the given path.
	 * <p>
	 * By default this function invokes the path factory
	 * passed as parameter of the constructor.
	 * 
	 * @param path is the path to build.
	 * @param segment is the segment to add.
	 * @return <code>true</code> if the segment was added;
	 * otherwise <code>false</code>.
	 */
	protected boolean addToPath(GP path, ST segment) {
		if (this.pathFactory!=null) {
			return this.pathFactory.addToPath(path, segment);
		}
		assert(path!=null);
		assert(segment!=null);
		try {
			return path.add(segment);
		}
		catch(Throwable e) {
			throw new IllegalStateException("no path factory found", e); //$NON-NLS-1$
		}
	}

	/** Run the A* algorithm assuming that the graph is oriented is
	 * an orientation tool was passed to the constructor.
	 * <p>
	 * The orientation of the graph may also be overridden by the implementations
	 * of the {@link AStarNode A* nodes}.
	 * 
	 * @param startPoint is the starting point.
	 * @param endPoint is the point to reach.
	 * @return the found path, or <code>null</code> if none found.
	 */
	public final GP solve(PT startPoint, PT endPoint) {
		return solve(
				node(startPoint,
					0f,
					estimate(startPoint, endPoint), null),
					endPoint);
	}
	
	/** Run the A* algorithm assuming that the graph is oriented is
	 * an orientation tool was passed to the constructor.
	 * <p>
	 * The orientation of the graph may also be overridden by the implementations
	 * of the {@link AStarNode A* nodes}.
	 * 
	 * @param startPoint is the starting point.
	 * @param endPoint is the point to reach.
	 * @return the found path, or <code>null</code> if none found.
	 */
	protected GP solve(AStarNode<ST,PT> startPoint, PT endPoint) {
		List<AStarNode<ST,PT>> closeList;
		
		fireAlgorithmStart(startPoint, endPoint);
		
		// Run A*
		closeList = findPath(startPoint, endPoint);
		if (closeList==null || closeList.isEmpty()) return null;
		
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
	private AStarNode<ST,PT> node(PT node, float cost, float estimatedCost, ST arrival) {
		AStarNode<ST,PT> aNode;
		
		if (node instanceof AStarNode<?,?>) {
			aNode = (AStarNode<ST,PT>)node;
			aNode.setArrivalConnection(arrival);
			aNode.setCost(cost);
			aNode.setEstimatedCost(estimatedCost);
		}
		else {
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
	protected AStarNode<ST,PT> newAStarNode(PT node, float cost, float estimatedCost, ST arrival) {
		return new Candidate(arrival, node, cost, estimatedCost);
	}

	/** Run the A* algorithm and tries to find a path from
	 *  the startPoint to the endPoint.
	 *  
	 * @param startPoint is the starting point.
	 * @param endPoint is the point to reach.
	 * @return the close list of the A* algorithm.
	 */
	List<AStarNode<ST,PT>> findPath(AStarNode<ST,PT> startPoint, PT endPoint) {
		CloseComparator<ST,PT> cComparator = new CloseComparator<ST,PT>();
		OpenComparator<ST,PT> oComparatorWithoutRef = new OpenComparator<ST,PT>();
		List<AStarNode<ST,PT>> openList = new ArrayList<AStarNode<ST,PT>>();
		List<AStarNode<ST,PT>> closeList = new ArrayList<AStarNode<ST,PT>>();

		openList.add(startPoint);
		fireNodeOpened(startPoint, openList);
		
		int idx;
		AStarNode<ST,PT> candidate, ocandidate, reachableCandidate, reachedCandidate;
		PT reachableNode, node;
		float g, h;
		boolean foundTarget = false;
		
		while (!foundTarget && !openList.isEmpty()) {
			
			ocandidate = openList.remove(0);
			fireNodeConsumed(ocandidate, openList);
			
			candidate = translateCandidate(endPoint, ocandidate);
			foundTarget = (candidate==null);
			
			if (!foundTarget) {
				assert(candidate!=null);
				node = candidate.getGraphPoint();
				// Update the nodes that are reachable from the current candidate.
				for(ST segment : candidate.getGraphSegments()) {
					reachableNode = segment.getOtherSidePoint(node);
					if (reachableNode!=null && !reachableNode.equals(node)) {
						g = candidate.cost()
							+ computeCostFor(node)
							+ computeCostFor(segment);
						h = estimate(reachableNode, endPoint);
						reachableCandidate = newAStarNode(reachableNode, g, h, segment); 
	
						// Reopen node if better cost
						idx = (isClosedNodeReopeningEnabled())
								? CollectionUtil.indexOf(cComparator, reachableCandidate, closeList)
								: -1;
						if (idx>=0) {
							reachedCandidate = closeList.get(idx);
							if (g < reachedCandidate.cost()) {
								closeList.remove(idx);
								AStarNode<ST,PT> nn = node(
										reachableNode,
										g,
										h,
										segment); 
								CollectionUtil.add(oComparatorWithoutRef,
										nn,
										openList);
								fireNodeOpened(nn, openList);
							}
						}
						else {
							idx = openList.indexOf(reachableCandidate);
							if (idx>=0) {
								// Rearrange open list if better cost
								reachedCandidate = openList.get(idx);
								if ((g+h) < reachedCandidate.pathCost()) {
									openList.remove(idx);
									AStarNode<ST,PT> nn = node(
											reachableNode,
											g,
											h,
											segment);
									CollectionUtil.add(oComparatorWithoutRef,
											nn,
											openList);
									fireNodeReopened(nn, openList);
								}
							}
							else {
								// Node was neither treated nor seen, add it
								AStarNode<ST,PT> nn = node(
										reachableNode,
										g,
										h,
										segment);
								CollectionUtil.add(oComparatorWithoutRef,
										nn,
										openList);
								fireNodeOpened(nn, openList);
							}
						}
					}
				}
			}
				
			// Refresh the close list
			idx = CollectionUtil.add(cComparator, ocandidate, closeList, false, true);
			fireNodeClosed(ocandidate, closeList);
			assert(idx>=0);
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
	GP createPath(AStarNode<ST,PT> startPoint, PT endPoint, List<AStarNode<ST,PT>> closeList) {
		int idx;
		ST segment;
		PT point;
		AStarNode<ST,PT> node;
		GP path = null;
		CloseComparator<ST,PT> cComparator = new CloseComparator<ST,PT>();

		node = newAStarNode(endPoint, Float.NaN, Float.NaN, null);
		idx = CollectionUtil.indexOf(cComparator, node, closeList);
		
		if (idx>=0) {
			node = closeList.remove(idx);
			point = node.getGraphPoint();
			segment = node.getArrivalConnection();
			if (point!=null && segment!=null) {
				LinkedList<ST> pathSegments = new LinkedList<ST>();
				pathSegments.add(segment);
				do {
					point = segment.getOtherSidePoint(point);
					node = newAStarNode(point, Float.NaN, Float.NaN, null);
					idx = CollectionUtil.indexOf(cComparator, node, closeList);
					if (idx>=0) {
						node = closeList.remove(idx);
						segment = node.getArrivalConnection();
						if (segment!=null) {
							pathSegments.add(segment);
						}
					}
					else {
						point = null;
						segment = null;
					}
				}
				while (point!=null && segment!=null);
				
				// Building of the path is done is a second step
				// because the oriented graph cannot enable to
				// built it during the previous step loop:
				// isConnectedSegment replies false when tested
				// with an arrival segment and cannot
				// enable to go backward along the path.
				Iterator<ST> iterator = pathSegments.descendingIterator();
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
	 * <p>
	 * By default, this function invoked the {@link AStarSegmentReplacer}
	 * associated to this AStar algorithm.
	 * 
	 * @param index is the position of the segment in the path.
	 * @param segment is the segment to replace.
	 * @return the replacement segment or the <var>segment</var> itself.
	 */
	protected ST replaceSegment(int index, ST segment) {
		ST rep = null;
		if (this.segmentReplacer!=null) {
			rep = this.segmentReplacer.replaceSegment(index, segment);
		}
		if (rep==null) rep = segment;
		return rep;
	}
	
	/** Invoked when a segment could not be added into the
	 * path.
	 * <p>
	 * In standard AStar implementation, this function replies <code>false</code>.
	 * 
	 * @param index is the index of the invalid segment.
	 * @param segment is the segment that cannot be added
	 * @param path is the current state of the path.
	 * @return <code>true</code> if the path building should continue,
	 * <code>false</code> if the path building should stop and
	 * replies a <code>null</code> path.
	 */
	protected boolean invalidPathSegmentFound(int index, ST segment, GP path) {
		return false;
	}
	
	/** Candidate node in the A* algorithm, and its related information.
	 *  
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected class Candidate implements AStarNode<ST,PT> {

		private ST entryConnection;
		private float costToReach;
		private float estimatedCost;
		private final PT node;

		/**
		 * @param entryConnection
		 * @param node
		 * @param costToReach
		 * @param estimatedCost
		 */
		public Candidate(ST entryConnection, PT node, float costToReach, float estimatedCost) {
			this.entryConnection = entryConnection;
			this.node = node;
			this.costToReach = costToReach;
			this.estimatedCost = estimatedCost;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder b = new StringBuilder();
			b.append("{"); //$NON-NLS-1$
			b.append(this.node.getClass().getName());
			b.append("|"); //$NON-NLS-1$
			b.append(System.identityHashCode(this.node));
			b.append("|"); //$NON-NLS-1$
			b.append(this.costToReach);
			b.append("|"); //$NON-NLS-1$
			b.append(this.estimatedCost);
			b.append("|"); //$NON-NLS-1$
			b.append(this.costToReach+this.estimatedCost);
			b.append("|"); //$NON-NLS-1$
			b.append(this.node.toString());
			b.append("}"); //$NON-NLS-1$
			return b.toString();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (this==obj) return true;
			if (obj instanceof AStarNode<?,?>) {
				return this.node.equals(((AStarNode<?,?>)obj).getGraphPoint());
			}
			if (obj instanceof GraphPoint<?,?>) {
				return this.node.equals(obj);
			}
			return super.equals(obj);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return this.node.hashCode();
		}

		/** {@inheritDoc}
		 */
		@Override
		public PT getGraphPoint() {
			return this.node;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public ST getArrivalConnection() {
			return this.entryConnection;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public ST setArrivalConnection(ST connection) {
			return this.entryConnection = connection;
		}

		/** {@inheritDoc}
		 */
		@Override
		public float cost() {
			return this.costToReach;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public float setCost(float cost) {
			return this.costToReach = cost;
		}

		/** {@inheritDoc}
		 */
		@Override
		public float estimatedCost() {
			return this.estimatedCost;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public float setEstimatedCost(float cost) {
			return this.estimatedCost = cost;
		}

		/** {@inheritDoc}
		 */
		@Override
		public float pathCost() {
			return this.costToReach + this.estimatedCost;
		}

		/** {@inheritDoc}
		 */
		@Override
		public Iterable<ST> getGraphSegments() {
			AStarSegmentOrientation<ST,PT> tool = getSegmentOrientationTool();
			if (tool!=null) {
				return new OrientedConnectionCollection(this.entryConnection, this.node.getConnections());
			}
			return this.node.getConnectedSegments();
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
	 * @since 4.0
	 */
	protected static class OpenComparator<ST extends GraphSegment<ST,PT>, PT extends GraphPoint<PT,ST>> implements Comparator<AStarNode<ST,PT>> {

		/**
		 */
		public OpenComparator() {
			//
		}

		/** {@inheritDoc}
		 */
		@Override
		public int compare(AStarNode<ST,PT> o1, AStarNode<ST,PT> o2) {
			if (o1==o2) return 0;
			if (o1==null) return Integer.MIN_VALUE;
			if (o2==null) return Integer.MAX_VALUE;
			int cmp = Float.compare(o1.pathCost(), o2.pathCost());
			if (cmp!=0) return cmp;
			return o1.getGraphPoint().compareTo(o2.getGraphPoint());
		}
		
	} // class OpenComparator
	
	/** Comparator used to sort the close list of A* algorithm.
	 * 
	 * @param <PT> is the type of node in the graph
	 * @param <ST> is the type of edge in the graph
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class CloseComparator<ST extends GraphSegment<ST,PT>, PT extends GraphPoint<PT,ST>> implements Comparator<AStarNode<ST,PT>> {

		/**
		 */
		public CloseComparator() {
			//
		}

		/** {@inheritDoc}
		 */
		@Override
		public int compare(AStarNode<ST,PT> o1, AStarNode<ST,PT> o2) {
			if (o1==o2) return 0;
			if (o1==null) return Integer.MIN_VALUE;
			if (o2==null) return Integer.MAX_VALUE;
			return o1.getGraphPoint().compareTo(o2.getGraphPoint());
		}
		
	} // class CloseComparator

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	private class OrientedConnectionCollection implements Iterable<ST> {
		
		private final Iterable<? extends GraphPointConnection<PT,ST>> connections;
		private final ST entry;
		
		/**
		 * @param entry
		 * @param connections
		 */
		public OrientedConnectionCollection(ST entry, Iterable<? extends GraphPointConnection<PT,ST>> connections) {
			this.connections = connections;
			this.entry = entry;
		}

		/** {@inheritDoc}
		 */
		@Override
		public Iterator<ST> iterator() {
			return new OrientedConnectionIterator(this.entry, this.connections.iterator());
		}
		
	} // class OrientedConnectionCollection

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	private class OrientedConnectionIterator implements Iterator<ST> {
		
		private final ST entry;
		private final Iterator<? extends GraphPointConnection<PT,ST>> iterator;
		private ST next;
		
		/**
		 * @param entry
		 * @param iterator
		 */
		public OrientedConnectionIterator(ST entry, Iterator<? extends GraphPointConnection<PT,ST>> iterator) {
			this.entry = entry;
			this.iterator = iterator;
			searchNext();
		}
		
		private void searchNext() {
			AStarSegmentOrientation<ST,PT> orientation = getSegmentOrientationTool();
			assert(orientation!=null);
			this.next = null;
			GraphPointConnection<PT,ST> n;
			while (this.next==null && this.iterator.hasNext()) {
				n = this.iterator.next();
				if (orientation.isTraversable(this.entry, n)) {
					this.next = n.getGraphSegment();
				}
			}
		}

		/** {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		/** {@inheritDoc}
		 */
		@Override
		public ST next() {
			ST n = this.next;
			if (n==null) throw new NoSuchElementException();
			searchNext();
			return n;
		}

		/** {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	} // class OrientedConnectionIterator

	/**
	 * @param <GP> is the type of the graph graph itself.
	 * @param <PT> is the type of node in the graph
	 * @param <ST> is the type of edge in the graph
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	private static class AStarReflectionPathFactory<GP extends GraphPath<GP,ST,PT>, ST extends GraphSegment<ST,PT>, PT extends GraphPoint<PT,ST>>
	implements AStarPathFactory<GP,ST,PT> {
		
		private final Class<? extends GP> type;
		
		/**
		 * @param type
		 */
		public AStarReflectionPathFactory(Class<? extends GP> type) {
			this.type = type;
		}

		/** {@inheritDoc}
		 */
		@Override
		public GP newPath(PT startPoint, ST segment) {
			try {
				GP path = this.type.newInstance();
				path.add(segment, startPoint);
				path.setFirstSegmentReversable(false);
				return path;
			}
			catch (InstantiationException e) {
				throw new IllegalArgumentException(e);
			}
			catch (IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			}
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public boolean addToPath(GP path, ST segment) {
			assert(path!=null);
			assert(segment!=null);
			return path.add(segment);
		}
		
	} // class AStarReflectionPathFactory

}
