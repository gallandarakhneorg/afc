/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.gis.road.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.d1.Direction1D;

/**
 * This class describes a road path with is clustered into
 * several unconnected paths. It is assumed that the
 * ClusteredRoadPath does nto allow multiple connections
 * at the same point, as for a standard {@link RoadPath}.
 * The difference between a RoadPath and a ClusteredRoadPath
 * is the fact that a ClusteredRoadPath allows to have
 * holes in the path.
 *
 * <p><strong>Caution:</strong> ClusteredRoadPath may change the
 * content of the RoadPath added inside. Be sure that you
 * pass to CluseteredRoadPath instances that you are agree to
 * be changed.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see RoadPath
 */
public class ClusteredRoadPath implements Collection<RoadPath> {

	private final List<RoadPath> paths = new ArrayList<>();

	private int segmentCount;

	/** Constructor.
	 */
	public ClusteredRoadPath() {
		//
	}

	/** Constructor.
	 * @param paths are the path to add in the cluster.
	 */
	public ClusteredRoadPath(RoadPath... paths) {
		addAll(Arrays.asList(paths));
	}

	/** Constructor.
	 * @param paths are the path to add in the cluster.
	 */
	public ClusteredRoadPath(Collection<? extends RoadPath> paths) {
		addAll(paths);
	}

	/** Replies the length of the path.
	 *
	 * @return the length of the path.
	 */
	@Pure
	public double getLength() {
		double length  = 0;
		for (final RoadPath p : this.paths) {
			length += p.getLength();
		}
		return length;
	}

	/** Invert the order of the road paths in this ClusteredRoadPath.
	 *
	 * @see RoadPath#invert()
	 */
	public void invert() {
		for (final RoadPath p : this.paths) {
			p.invert();
		}
	}

	/** Replies the first road path in this clustered road-path.
	 *
	 * @return the first road path in this clustered road-path.
	 */
	@Pure
	public RoadPath first() {
		return this.paths.get(0);
	}

	/** Replies the last road path in this clustered road-path.
	 *
	 * @return the last road path in this clustered road-path.
	 */
	@Pure
	public RoadPath last() {
		return this.paths.get(this.paths.size() - 1);
	}

	/** Replies the number of road segments in this clustered path.
	 *
	 * @return the number of road segments in this clustered path.
	 */
	@Pure
	public int getRoadSegmentCount() {
		return this.segmentCount;
	}

	/** Replies the road path at the given position.
	 *
	 * @param index an index.
	 * @return the road path at the given position.
	 */
	@Pure
	public RoadPath getRoadPathAt(int index) {
		return this.paths.get(index);
	}

	/** Replies the index of the first occurrence. of the given road segment.
	 *
	 * @param segment a segment.
	 * @return the index of the first occurrence. of the given road segment or
	 * <code>-1</code> if the road segment was not found.
	 */
	@Pure
	public int indexOf(RoadSegment segment) {
		int count = 0;
		int index;
		for (final RoadPath p : this.paths) {
			index = p.indexOf(segment);
			if (index >= 0) {
				return count + index;
			}
			count += p.size();
		}
		return -1;
	}

	/** Replies the index of the last occurrence. of the given road segment.
	 *
	 * @param segment a segment.
	 * @return the index of the last occurrence. of the given road segment or
	 * <code>-1</code> if the road segment was not found.
	 */
	@Pure
	public int lastIndexOf(RoadSegment segment) {
		int count = 0;
		int index;
		int lastIndex = -1;
		for (final RoadPath p : this.paths) {
			index = p.lastIndexOf(segment);
			if (index >= 0) {
				lastIndex = count + index;
			}
			count += p.size();
		}
		return lastIndex;
	}

	/** Replies the road segment at the given index.
	 *
	 * @param index an index.
	 * @return the road segment.
	 */
	@Pure
	public RoadSegment getRoadSegmentAt(int index) {
		if (index >= 0) {
			int b = 0;
			for (final RoadPath p : this.paths) {
				final int e = b + p.size();
				if (index < e) {
					return p.get(index - b);
				}
				b = e;
			}
		}
		throw new IndexOutOfBoundsException();
	}

	/** Replies the road path which is containing the road segment
	 * at the given index.
	 *
	 * @param index is the index of the road segment.
	 * @return the road path, never <code>null</code>.
	 */
	@Pure
	public RoadPath getPathForRoadSegmentAt(int index) {
		if (index >= 0) {
			int b = 0;
			for (final RoadPath p : this.paths) {
				final int e = b + p.size();
				if (index < e) {
					return p;
				}
				b = e;
			}
		}
		throw new IndexOutOfBoundsException();
	}


	/** Replies the direction of this path
	 * on the road segment at the given
	 * index.
	 *
	 * @param index is the index of the road segment.
	 * @return the direction of the segment at the given index
	 *     for this path.
	 */
	@Pure
	public Direction1D getRoadSegmentDirectionAt(int index) {
		if (index >= 0) {
			int b = 0;
			for (final RoadPath p : this.paths) {
				final int e = b + p.size();
				if (index < e) {
					return p.getSegmentDirectionAt(index - b);
				}
				b = e;
			}
		}
		throw new IndexOutOfBoundsException();
	}

	/** Remove the road segment at the given index.
	 *
	 * @param index an index.
	 * @return the removed road segment.
	 */
	public RoadSegment removeRoadSegmentAt(int index) {
		if (index >= 0) {
			int b = 0;
			for (final RoadPath p : this.paths) {
				int end = b + p.size();
				if (index < end) {
					end = index - b;
					return removeRoadSegmentAt(p, end, null);
				}
				b = end;
			}
		}
		throw new IndexOutOfBoundsException();
	}

	/** Remove the road segment at the given index in the given path.
	 *
	 * <p>The given path must be inside the clustered road-path.
	 * The given index is inside the path's segments.
	 *
	 * @param path a path.
	 * @param index an index.
	 * @param iterator an iterator.
	 * @return the removed segment, never <code>null</code>.
	 */
	private RoadSegment removeRoadSegmentAt(RoadPath path, int index, PathIterator iterator) {
		final int pathIndex = this.paths.indexOf(path);
		assert pathIndex >= 0 && pathIndex < this.paths.size();
		assert index >= 0 && index < path.size();
		final RoadPath syncPath;
		final RoadSegment sgmt;
		if (index == 0 || index == path.size() - 1) {
			// Remove one of the bounds of the path. if cause
			// minimal insertion changes in the clustered road-path
			sgmt = path.remove(index);
		} else {
			// Split the path somewhere between the first and last positions
			sgmt = path.get(index);
			assert sgmt != null;
			final RoadPath rest = path.splitAfter(sgmt);
			path.remove(sgmt);
			if (rest != null && !rest.isEmpty()) {
				// put back the rest of the segments
				this.paths.add(pathIndex + 1, rest);
			}
		}
		--this.segmentCount;
		if (path.isEmpty()) {
			this.paths.remove(path);
			if (pathIndex > 0) {
				syncPath = this.paths.get(pathIndex - 1);
			} else {
				syncPath = null;
			}
		} else {
			syncPath = path;
		}
		if (iterator != null) {
			iterator.reset(syncPath);
		}
		return sgmt;
	}

	@Override
	public final boolean add(RoadPath path) {
		return addAndGetPath(path) != null;
	}

	/** Add the given road path into this cluster of road paths.
	 *
	 * @param end is the road path to add
	 * @return the road path inside which the road segments are added; it could
	 *     be <var>e</var> itself; return <code>null</code> if the elements cannot be
	 *     added.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity",
		"checkstyle:nestedifdepth"})
	public RoadPath addAndGetPath(RoadPath end) {
		RoadPath selectedPath = null;
		if (end != null && !end.isEmpty()) {
			RoadConnection first = end.getFirstPoint();
			RoadConnection last = end.getLastPoint();
			assert first != null;
			assert last != null;
			first = first.getWrappedRoadConnection();
			last = last.getWrappedRoadConnection();
			assert first != null;
			assert last != null;

			if (this.paths.isEmpty()) {
				this.paths.add(end);
				selectedPath = end;
			} else {
				RoadPath connectToFirst = null;
				RoadPath connectToLast = null;
				final Iterator<RoadPath> pathIterator = this.paths.iterator();
				RoadPath path;
				while ((connectToFirst == null || connectToLast == null) && pathIterator.hasNext()) {
					path = pathIterator.next();
					if (connectToFirst == null) {
						if (first.equals(path.getFirstPoint())) {
							connectToFirst = path;
						} else if (first.equals(path.getLastPoint())) {
							connectToFirst = path;
						}
					}
					if (connectToLast == null) {
						if (last.equals(path.getFirstPoint())) {
							connectToLast = path;
						} else if (last.equals(path.getLastPoint())) {
							connectToLast = path;
						}
					}
				}

				if (connectToFirst != null && connectToLast != null && !connectToLast.equals(connectToFirst)) {
					// a) Select the biggest path as reference
					// b) Remove the nonreference path that is connected to the new path
					// c) Add the components of the new path into the reference path
					// d) Reinject the components of the nonreference path.
					// --
					// a)
					final RoadPath reference;
					final RoadPath nonreference;
					if (connectToFirst.size() > connectToLast.size()) {
						reference = connectToFirst;
						nonreference = connectToLast;
					} else {
						reference = connectToLast;
						nonreference = connectToFirst;
					}
					// b)
					if (this.paths.remove(nonreference)) {
						// c)
						if (!RoadPath.addPathToPath(reference, end)) {
							// Reinject the remove elements.
							this.paths.add(nonreference);
						} else {
							// d)
							if (!RoadPath.addPathToPath(reference, nonreference)) {
								// Reinject the remove elements.
								this.paths.add(nonreference);
							} else {
								selectedPath = reference;
							}
						}
					}
				} else if (connectToFirst != null) {
					if (RoadPath.addPathToPath(connectToFirst, end)) {
						selectedPath = connectToFirst;
					}
				} else if (connectToLast != null) {
					if (RoadPath.addPathToPath(connectToLast, end)) {
						selectedPath = connectToLast;
					}
				} else if (this.paths.add(end)) {
					selectedPath = end;
				}

			}

			if (selectedPath != null) {
				this.segmentCount += end.size();
			}
		}

		return selectedPath;
	}

	@Override
	public boolean addAll(Collection<? extends RoadPath> col) {
		if (col == null || col.isEmpty()) {
			return false;
		}
		boolean changed = false;
		for (final RoadPath p : col) {
			if (add(p)) {
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public void clear() {
		this.paths.clear();
		this.segmentCount = 0;
	}

	@Override
	@Pure
	public boolean contains(Object obj) {
		if (obj instanceof RoadPath) {
			return this.paths.contains(obj);
		} else if (obj instanceof RoadConnection) {
			for (final RoadPath p : this.paths) {
				if (obj.equals(p.getFirstPoint()) || obj.equals(p.getLastPoint())) {
					return true;
				}
			}
		} else if (obj instanceof RoadSegment) {
			for (final RoadPath p : this.paths) {
				if (p.contains(obj)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Pure
	public boolean containsAll(Collection<?> col) {
		if (col == null || col.isEmpty()) {
			return false;
		}
		for (final Object o : col) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	@Pure
	public boolean isEmpty() {
		return this.paths.isEmpty();
	}

	@Override
	@Pure
	public Iterator<RoadPath> iterator() {
		return Collections.unmodifiableList(this.paths).iterator();
	}

	@Override
	public boolean remove(Object obj) {
		if (obj instanceof RoadPath) {
			if (this.paths.remove(obj)) {
				this.segmentCount -= ((RoadPath) obj).size();
				return true;
			}
		} else if (obj instanceof RoadSegment) {
			final int index = indexOf((RoadSegment) obj);
			if (index >= 0) {
				return removeRoadSegmentAt(index) != null;
			}
		}
		return false;
	}

	/** Force this clustered path to count the number
	 * of road segments in the clusters.
	 * This function may be invoke when you have change
	 * the road paths in this clustered road path outside
	 * the control of the clustered road path.
	 * @since 4.0
	 */
	public void sync() {
		this.segmentCount = 0;
		for (final RoadPath p : this.paths) {
			this.segmentCount += p.size();
		}
	}

	@Override
	public boolean removeAll(Collection<?> col) {
		if (col == null || col.isEmpty()) {
			return false;
		}
		boolean allRemoved = true;
		for (final Object o : col) {
			allRemoved &= remove(o);
		}
		return allRemoved;
	}

	@Override
	public boolean retainAll(Collection<?> col) {
		if (col == null) {
			return false;
		}
		if (col.isEmpty()) {
			if (isEmpty()) {
				return false;
			}
			clear();
			return true;
		}
		boolean changed = false;
		final Collection<RoadPath> retained = new ArrayList<>();
		for (final Object o : col) {
			if (o instanceof RoadPath) {
				final RoadPath p = (RoadPath) o;
				if (this.paths.remove(o)) {
					retained.add(p);
				} else {
					changed = true;
				}
			}
		}
		if (changed) {
			this.paths.clear();
			this.segmentCount = 0;
			for (final RoadPath p : retained) {
				this.paths.add(p);
				this.segmentCount += p.size();
			}
		}
		return changed;
	}

	@Override
	@Pure
	public int size() {
		return this.paths.size();
	}

	@Override
	@Pure
	public Object[] toArray() {
		return this.paths.toArray();
	}

	@Override
	@Pure
	public <T> T[] toArray(T[] array) {
		return this.paths.toArray(array);
	}

	@Override
	@Pure
	public String toString() {
		return this.paths.toString();
	}

	/**
	 * Replies the standard road path which
	 * is inside this ClusteredRoadPath.
	 * If this ClusteredRoadPath does not
	 * contains exactly one road path, this
	 * function returns <code>null</code>.
	 *
	 * @return the road path inside this ClusteredRoadPath,
	 *     or <code>null</code> if no road path or too many
	 *     road path in this ClusteredRoadPath.
	 */
	@Pure
	public RoadPath toRoadPath() {
		if (this.paths.size() == 1) {
			return this.paths.iterator().next();
		}
		return null;
	}

	/** Replies an iterator on the road segments in this ClusteredRoadPath.
	 *
	 * @return an iterator on the road segments.
	 */
	@Pure
	public Iterator<RoadSegment> roadSegments() {
		return new ClusteredSegmentIterator();
	}

	/** Internal iterator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class ClusteredSegmentIterator implements Iterator<RoadSegment> {

		private final PathIterator pathIterator;

		private SegmentIterator segmentIterator;

		private boolean isNextComputed;

		private RoadPath nextPath;

		private RoadSegment next;

		/** Constructor.
		 */
		ClusteredSegmentIterator() {
			this.pathIterator = new PathIterator();
		}

		private void searchNext() {
			this.isNextComputed = true;
			this.next = null;
			while ((this.segmentIterator == null || !this.segmentIterator.hasNext())
				&& this.pathIterator.hasNext()) {
				this.nextPath = this.pathIterator.next();
				this.segmentIterator = new SegmentIterator(this.pathIterator, this.nextPath);
			}
			if (this.segmentIterator != null && this.segmentIterator.hasNext()) {
				this.next = this.segmentIterator.next();
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			if (!this.isNextComputed) {
				searchNext();
			}
			return this.next != null;
		}

		@Override
		public RoadSegment next() {
			if (!this.isNextComputed) {
				searchNext();
			}
			final RoadSegment n = this.next;
			if (n == null) {
				throw new NoSuchElementException();
			}
			this.isNextComputed = false;
			return n;
		}

		@Override
		public void remove() {
			if (this.next == null || this.segmentIterator == null || this.nextPath == null
				|| getRoadSegmentCount() <= 0) {
				throw new NoSuchElementException();
			}
			this.segmentIterator.remove();
			/*if (this.nextPath.isEmpty()) {
				this.segmentIterator = null;
				this.pathIterator.remove();
			}*/
			this.isNextComputed = false;
			this.next = null;
		}

	} // class ClusteredSegmentIterator

	/** Internal iterator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class SegmentIterator implements Iterator<RoadSegment> {

		private final PathIterator pathIterator;

		private final RoadPath path;

		private int next;

		private RoadSegment repliedSegment;

		/** Constructor.
		 *
		 * @param pathIterator the path iterator to use.
		 * @param path the path.
		 */
		SegmentIterator(PathIterator pathIterator, RoadPath path) {
			this.pathIterator = pathIterator;
			this.path = path;
			this.next = 0;
			this.repliedSegment = null;
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.next >= 0 && this.next < this.path.size();
		}

		@Override
		public RoadSegment next() {
			if (this.next < 0 || this.next >= this.path.size()) {
				throw new NoSuchElementException();
			}
			final RoadSegment s = this.path.get(this.next);
			if (s == null) {
				throw new NoSuchElementException();
			}
			++this.next;
			this.repliedSegment = s;
			return s;
		}

		@Override
		public void remove() {
			if (this.repliedSegment == null) {
				throw new NoSuchElementException();
			}
			final int n = --this.next;
			this.repliedSegment = null;
			ClusteredRoadPath.this.removeRoadSegmentAt(this.path, n, this.pathIterator);
		}

	} // class SegmentIterator

	/** Internal iterator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class PathIterator implements Iterator<RoadPath> {

		private Iterator<RoadPath> iterator;

		/** Constructor.
		 */
		PathIterator() {
			this.iterator = ClusteredRoadPath.this.paths.iterator();
		}

		/** Reset this iterator.
		 *
		 * @param path a path.
		 */
		public void reset(RoadPath path) {
			this.iterator = ClusteredRoadPath.this.paths.iterator();
			RoadPath pth = null;
			while (this.iterator.hasNext() && (pth == null || (path != null && pth != path))) {
				pth = this.iterator.next();
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public RoadPath next() {
			return this.iterator.next();
		}

		@Override
		public void remove() {
			this.iterator.remove();
		}

	} // class PathIterator

}
