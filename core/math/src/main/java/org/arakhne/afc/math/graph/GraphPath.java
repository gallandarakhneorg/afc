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

package org.arakhne.afc.math.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class describes a path inside a graph.
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
@SuppressWarnings("checkstyle:methodcount")
public class GraphPath<GP extends GraphPath<GP, ST, PT>, ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>>
		implements GraphSegmentList<ST, PT>, Cloneable {

	/** Package access to avoid comiplation error.
	 */
	List<ST> segmentList = new LinkedList<>();

	private PT startingPoint;

	private PT endingPoint;

	private boolean isReversable;

	private double length;

	/** Construct a path.
	 */
	public GraphPath() {
		this.isReversable = true;
	}

	/**
	 * @param segment is the segment from which to start.
	 * @param startingPoint1 is the segment's point indicating the direction.
	 */
	public GraphPath(ST segment, PT startingPoint1) {
		this.segmentList.add(segment);
		this.startingPoint = startingPoint1;
		this.endingPoint = segment.getOtherSidePoint(startingPoint1);
		this.isReversable = false;
	}

	/** Replies if this first segment could be reversed
	 * when the second segment is inserted to fit the
	 * order of the insertions.
	 *
	 * <p>Let s1 and s2 two segments respectively linked
	 * to the points [p1, p2] and [p1, p3].
	 * Let the following code:<pre><code>
	 * GraphPath path = new GraphPath();
	 * path.add(s1);
	 * path.add(s2);</code></pre>
	 * If the path is not reversable, it is becoming
	 * {@code [s2, s1]} because the order of s1
	 * is preserved. If the path is reversable, it
	 * is becoming {@code  [s1, s2]} because the first
	 * segment is reverted to fit the order of the calls
	 * to the add function.
	 *
	 * <p>Let s1 and s2 the same segments as previously.
	 * Let the following code:<pre><code>
	 * GraphPath path = new GraphPath();
	 * path.add(s1, p2);
	 * path.add(s2);</code></pre>
	 * The first segment is not reversable because of
	 * the call to the add function with the connection
	 * as parameter. The path is becoming
	 * {@code s1, s2}, and nothing else.
	 *
	 * @return <code>true</code> if the first segment
	 *     could be reversed; otherwise <code>false</code>.
	 */
	@Pure
	public boolean isFirstSegmentReversable() {
		return this.isReversable;
	}

	/** Set if this first segment could be reversed
	 * when the second segment is inserted to fit the
	 * order of the insertions.
	 *
	 * <p>Let s1 and s2 two segments respectively linked
	 * to the points [p1, p2] and [p1, p3].
	 * Let the following code:<pre><code>
	 * GraphPath path = new GraphPath();
	 * path.add(s1);
	 * path.add(s2);</code></pre>
	 * If the path is not reversable, it is becoming
	 * {@code [s2, s1]} because the order of s1
	 * is preserved. If the path is reversable, it
	 * is becoming {@code  [s1, s2]} because the first
	 * segment is reverted to fit the order of the calls
	 * to the add function.
	 *
	 * <p>Let s1 and s2 the same segments as previously.
	 * Let the following code:<pre><code>
	 * GraphPath path = new GraphPath();
	 * path.add(s1, p2);
	 * path.add(s2);</code></pre>
	 * The first segment is not reversable because of
	 * the call to the add function with the connection
	 * as parameter. The path is becoming
	 * {@code s1, s2}, and nothing else.
	 *
	 * @param isReversable1 is <code>true</code> if the first
	 *     segment could be reversed; otherwise <code>false</code>.
	 */
	public void setFirstSegmentReversable(boolean isReversable1) {
		this.isReversable = isReversable1;
	}

	@Pure
	@Override
	public int size() {
		return this.segmentList.size();
	}

	@Pure
	@Override
	public boolean isEmpty() {
		return this.segmentList.isEmpty();
	}

	@Pure
	@Override
	public boolean contains(Object obj) {
		return this.segmentList.contains(obj);
	}

	@Pure
	@Override
	public Iterator<ST> iterator() {
		return this.segmentList.iterator();
	}

	@Pure
	@Override
	public Iterator<PT> pointIterator() {
		return new PointIterator<>(this.startingPoint, this.segmentList.iterator());
	}

	@Pure
	@Override
	public Iterable<PT> points() {
		return new PointIterable();
	}

	@Pure
	@Override
	public Object[] toArray() {
		return this.segmentList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return this.segmentList.toArray(array);
	}

	@Pure
	@Override
	public boolean containsAll(Collection<?> collection) {
		return this.segmentList.containsAll(collection);
	}

	@Override
	public boolean add(ST segment, PT point) {
		assert segment != null;
		assert point != null;

		// This is the first segment in the path.
		if (this.segmentList.isEmpty()) {
			this.startingPoint = point;
			this.endingPoint = segment.getOtherSidePoint(point);
			this.segmentList.clear();
			this.segmentList.add(segment);
			this.length = segment.getLength();
			return true;
		}

		// The segment should be added to the end of the path
		if (this.endingPoint.equals(point)) {
			this.endingPoint = segment.getOtherSidePoint(point);
			this.segmentList.add(segment);
			this.length += segment.getLength();
			return true;
		}

		// The segment should be added to the beginning of the path
		if (this.startingPoint.equals(point)) {
			if (this.segmentList.size() == 1 && this.isReversable) {
				this.segmentList.add(segment);
				this.startingPoint = this.endingPoint;
				this.endingPoint = segment.getOtherSidePoint(point);
				this.isReversable = false;
			} else {
				this.startingPoint = segment.getOtherSidePoint(point);
				this.segmentList.add(0, segment);
			}
			this.length += segment.getLength();
			return true;
		}

		// The segment is not connected to the ends of the path.
		return false;
	}

	/** {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if the index is invalid.
	 * @throws IllegalArgumentException is the given segment cannot be connected.
	 */
	@Override
	public void add(int index, ST segment) {
		if (index < 0 || index > this.segmentList.size()) {
			throw new IndexOutOfBoundsException();
		}
		// The path is empty, so that the segment is the first one.
		if (this.segmentList.isEmpty()) {
			if (this.segmentList.add(segment)) {
				this.startingPoint = segment.getBeginPoint();
				this.endingPoint = segment.getEndPoint();
				this.length += segment.getLength();
				return;
			}
			throw new IllegalArgumentException();
		}

		// Detect the candidates to the connection
		final PT candidate;

		this.isReversable = false;

		if (index < this.segmentList.size()) {
			candidate = getStartingPointFor(index);
		} else {
			candidate = this.endingPoint;
		}

		// Check the connection validity
		if (candidate != null) {
			final PT first = segment.getBeginPoint();
			final PT last = segment.getEndPoint();
			if (index == 0 && (candidate.equals(first) || candidate.equals(last))) {
				this.segmentList.add(0, segment);
				this.startingPoint = segment.getOtherSidePoint(this.startingPoint);
				this.length += segment.getLength();
				return;
			} else if (index == this.segmentList.size() && (candidate.equals(first) || candidate.equals(last))) {
				this.segmentList.add(segment);
				this.endingPoint = segment.getOtherSidePoint(this.endingPoint);
				this.length += segment.getLength();
				return;
			} else if (candidate.equals(first) || candidate.equals(last)) {
				this.segmentList.add(index, segment);
				this.length += segment.getLength();
				return;
			}
		}

		throw new IllegalArgumentException();
	}

	@Override
	public boolean add(ST segment) {
		// The path is empty, so that the segment is the first one.
		if (this.startingPoint == null) {
			this.segmentList.clear();
			if (this.segmentList.add(segment)) {
				this.startingPoint = segment.getBeginPoint();
				this.endingPoint = segment.getEndPoint();
				this.length += segment.getLength();
				return true;
			}
			return false;
		}

		// The segment is connectable to the last point
		PT endPoint = this.endingPoint;
		if (endPoint != null && endPoint.isConnectedSegment(segment)) {
			if (this.segmentList.add(segment)) {
				this.endingPoint = segment.getOtherSidePoint(endPoint);
				this.length += segment.getLength();
				return true;
			}
		}

		// The segment is connectable to the first point
		endPoint = this.startingPoint;
		if (endPoint != null && endPoint.isConnectedSegment(segment)) {
			try {
				if (this.segmentList.size() == 1 && this.isReversable) {
					this.segmentList.add(segment);
					this.startingPoint = this.endingPoint;
					this.endingPoint = segment.getOtherSidePoint(endPoint);
					this.isReversable = false;
				} else {
					this.segmentList.add(0, segment);
					this.startingPoint = segment.getOtherSidePoint(endPoint);
				}
				this.length += segment.getLength();
				return true;
			} catch (IndexOutOfBoundsException e) {
				//
			}
		}

		// Unable to connect the segment to the ends.
		return false;
	}

	@Pure
	@Override
	public PT getLastPoint() {
		return this.endingPoint;
	}

	@Pure
	@Override
	public PT getFirstPoint() {
		return this.startingPoint;
	}

	/** Replies the starting point for the segment at the given index.
	 *
	 * @param index is the index of the segment.
	 * @return the starting point for the segment at the given index.
	 */
	@Pure
	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	public PT getStartingPointFor(int index) {
		if ((index < 1) || (this.segmentList.size() <= 1)) {
			if (this.startingPoint != null) {
				return this.startingPoint;
			}
		} else {
			int idx = index;
			ST currentSegment = this.segmentList.get(idx);
			ST previousSegment = this.segmentList.get(--idx);

			// Because the two segments are the same
			// we must go deeper in the path elements
			// to detect the right segment
			int count = 0;
			while ((previousSegment != null) && (previousSegment.equals(currentSegment))) {
				currentSegment = previousSegment;
				idx--;
				previousSegment = (idx >= 0) ? this.segmentList.get(idx) : null;
				count++;
			}

			if (count > 0) {
				PT sp = null;
				if (previousSegment != null) {
					final PT p1 = currentSegment.getBeginPoint();
					final PT p2 = currentSegment.getEndPoint();
					final PT p3 = previousSegment.getBeginPoint();
					final PT p4 = previousSegment.getEndPoint();

					assert p1 != null && p2 != null && p3 != null && p4 != null;
					if (p1.equals(p3) || p1.equals(p4)) {
						sp = p1;
					} else if (p2.equals(p3) || p2.equals(p4)) {
						sp = p2;
					}

				} else {
					sp = this.startingPoint;
				}
				if (sp != null) {
					return ((count % 2) == 0) ? sp : currentSegment.getOtherSidePoint(sp);
				}

			} else if ((currentSegment != null) && (previousSegment != null)) {
				// if the two segments are different
				// it is simple to detect the
				// common point
				final PT p1 = currentSegment.getBeginPoint();
				final PT p2 = currentSegment.getEndPoint();
				final PT p3 = previousSegment.getBeginPoint();
				final PT p4 = previousSegment.getEndPoint();

				assert p1 != null && p2 != null && p3 != null && p4 != null;
				if (p1.equals(p3) || p1.equals(p4)) {
					return p1;
				}
				if (p2.equals(p3) || p2.equals(p4)) {
					return p2;
				}
			}
		}

		return null;
	}

	@Pure
	@Override
	public ST getLastSegment() {
		if (!this.segmentList.isEmpty()) {
			return this.segmentList.get(this.segmentList.size() - 1);
		}
		return null;
	}

	@Pure
	@Override
	public ST getAntepenulvianSegment() {
		if (this.segmentList.size() >= 2) {
			return this.segmentList.get(this.segmentList.size() - 2);
		}
		return null;
	}

	@Pure
	@Override
	public ST getSecondSegment() {
		if (this.segmentList.size() > 1) {
			return this.segmentList.get(1);
		}
		return null;
	}

	@Pure
	@Override
	public ST getFirstSegment() {
		if (!this.segmentList.isEmpty()) {
			return this.segmentList.get(0);
		}
		return null;
	}

	@Override
	public boolean addAll(Collection<? extends ST> collection) {
		boolean listChanged = false;
		final Iterator<? extends ST> iterator1 = collection.iterator();
		ST element;

		while (iterator1.hasNext()) {

			element = iterator1.next();
			if (add(element)) {
				listChanged = true;
			}

		}

		return listChanged;
	}

	@Override
	public boolean addAll(int index, Collection<? extends ST> collection) {
		boolean changed = false;
		int idx = index;
		for (final ST s : collection) {
			try {
				add(idx, s);
				changed = true;
				++idx;
			} catch (IndexOutOfBoundsException e) {
				//
			} catch (IllegalArgumentException e2) {
				//
			}
		}
		return changed;
	}

	@Override
	public boolean remove(Object obj) {
		if ((obj != null) && (obj instanceof GraphSegment<?, ?>)) {
			remove(this.segmentList.indexOf(obj));
			return true;
		}
		return false;
	}

	@Override
	public ST remove(int index) {
		if ((index > 0) && (index < this.segmentList.size())) {
			PT toConnect = getStartingPointFor(index);
			PT current = toConnect;
			final ST oldSegment = this.segmentList.remove(index);
			ST segment;

			this.length -= oldSegment.getLength();
			if (this.length < 0) {
				this.length = 0;
			}

			while (toConnect != null && index < this.segmentList.size()) {
				segment = this.get(index);
				current = segment.getOtherSidePoint(current);
				if (toConnect.equals(current)) {
					toConnect = null;
				} else {
					final ST oldSegment2 = this.segmentList.remove(index);
					this.length -= oldSegment2.getLength();
					if (this.length < 0) {
						this.length = 0;
					}
				}
			}

			if (toConnect != null) {
				this.endingPoint = toConnect;
			}

			return oldSegment;
		} else if (index == 0 && !this.segmentList.isEmpty()) {
			final ST oldSegment = this.segmentList.remove(index);
			this.length -= oldSegment.getLength();
			if (this.length < 0) {
				this.length = 0;
			}
			if (this.segmentList.isEmpty()) {
				this.startingPoint = null;
				this.endingPoint = null;
				this.isReversable = true;
			} else {
				this.startingPoint = oldSegment.getOtherSidePoint(this.startingPoint);
			}
			return oldSegment;
		}
		return null;
	}

	/**
	 * Package access to avoid compilation error.
	 * You must not call this function directly.
	 *
	 * @param index the reference index.
	 * @param inclusive indicates if the element at the reference index is included in the removed elements.
	 * @return <code>true</code> or <code>false</code>
	 */
	boolean removeUntil(int index, boolean inclusive) {
		if (index >= 0) {
			boolean changed = false;
			PT startPoint = this.startingPoint;
			ST segment;
			int limit = index;
			if (inclusive) {
				++limit;
			}
			for (int i = 0; i < limit; ++i) {
				segment = this.segmentList.remove(0);
				this.length -= segment.getLength();
				if (this.length < 0) {
					this.length = 0;
				}
				startPoint = segment.getOtherSidePoint(startPoint);
				changed = true;
			}
			if (changed) {
				if (this.segmentList.isEmpty()) {
					this.startingPoint = null;
					this.endingPoint = null;
					this.isReversable = true;
				} else {
					this.startingPoint = startPoint;
				}
				return true;
			}
		}
		return false;
	}

	/** Remove the path's elements before the
	 * specified one which is starting
	 * at the specified point. The specified element will
	 * be removed.
	 *
	 * <p>This function removes until the <i>first occurence</i>
	 * of the given object.
	 *
	 * @param obj is the segment to remove
	 * @param pt is the point on which the segment was connected
	 *     as its first point.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeUntil(ST obj, PT pt) {
		return removeUntil(indexOf(obj, pt), true);
	}

	/** Remove the path's elements before the
	 * specified one. The specified element will
	 * also be removed.
	 *
	 * <p>This function removes until the <i>first occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference segment.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	@Override
	public boolean removeUntil(ST obj) {
		return removeUntil(this.segmentList.indexOf(obj), true);
	}

	/** Remove the path's elements before the
	 * specified one. The specified element will
	 * not be removed.
	 *
	 * <p>This function removes until the <i>first occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference element.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	@Override
	public boolean removeBefore(ST obj) {
		return removeUntil(this.segmentList.indexOf(obj), false);
	}

	/** Remove the path's elements before the
	 * specified one which is starting
	 * at the specified point. The specified element will
	 * not be removed.
	 *
	 * <p>This function removes until the <i>first occurence</i>
	 * of the given object.
	 *
	 * @param obj is the segment to remove
	 * @param pt is the point on which the segment was connected
	 *     as its first point.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	@Override
	public boolean removeBefore(ST obj, PT pt) {
		return removeUntil(indexOf(obj, pt), false);
	}

	/** Remove the path's elements before the
	 * specified one. The specified element will
	 * not be removed.
	 *
	 * <p>This function removes until the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference element.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	@Override
	public boolean removeBeforeLast(ST obj) {
		return removeUntil(this.segmentList.lastIndexOf(obj), false);
	}

	/** Remove the path's elements before the
	 * specified one which is starting
	 * at the specified point. The specified element will
	 * not be removed.
	 *
	 * <p>This function removes until the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj is the segment to remove
	 * @param pt is the point on which the segment was connected
	 *     as its first point.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	@Override
	public boolean removeBeforeLast(ST obj, PT pt) {
		return removeUntil(lastIndexOf(obj, pt), false);
	}

	/** Remove the path's elements before the
	 * specified one which is starting
	 * at the specified point. The specified element will
	 * be removed.
	 *
	 * <p>This function removes until the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj is the segment to remove
	 * @param pt is the point on which the segment was connected
	 *     as its first point.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeUntilLast(ST obj, PT pt) {
		return removeUntil(lastIndexOf(obj, pt), true);
	}

	/** Remove the path's elements before the
	 * specified one. The specified element will
	 * also be removed.
	 *
	 * <p>This function removes until the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference segment.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	@Override
	public boolean removeUntilLast(ST obj) {
		return removeUntil(this.segmentList.lastIndexOf(obj), true);
	}

	private boolean removeAfter(int index, boolean inclusive) {
		if (index >= 0) {
			boolean changed = false;
			PT startPoint = this.startingPoint;
			ST segment;
			int limit = index;
			if (!inclusive) {
				++limit;
			}
			if (limit == 0) {
				if (!this.segmentList.isEmpty()) {
					changed = true;
					this.segmentList.clear();
					this.endingPoint = null;
					this.startingPoint = null;
					this.isReversable = true;
					this.length = 0;
				}
			} else {
				this.length = 0;
				final int segmentCount = this.segmentList.size();
				for (int i = 0; i < segmentCount && i < limit; ++i) {
					segment = this.segmentList.get(i);
					this.length += segment.getLength();
					startPoint = segment.getOtherSidePoint(startPoint);
				}
				this.endingPoint = startPoint;
				for (int i = segmentCount - 1; i >= limit; --i) {
					this.segmentList.remove(i);
					changed = true;
				}
			}
			return changed;
		}
		return false;
	}

	/** Remove the path's elements after the
	 * specified one which is starting
	 * at the specified point. The specified element will
	 * not be removed.
	 *
	 * <p>This function removes after the <i>first occurence</i>
	 * of the given object.
	 *
	 * @param obj is the segment to remove
	 * @param pt is the point on which the segment was connected
	 *     as its first point.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeAfter(ST obj, PT pt) {
		return removeAfter(indexOf(obj, pt), false);
	}

	/** Remove the path's elements after the
	 * specified one. The specified element will
	 * not be removed.
	 *
	 * <p>This function removes after the <i>first occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference segment.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeAfter(ST obj) {
		return removeAfter(this.segmentList.indexOf(obj), false);
	}

	/** Remove the path's elements after the
	 * specified one. The specified element will
	 * not be removed.
	 *
	 * <p>This function removes after the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference segment.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeAfterLast(ST obj) {
		return removeAfter(this.segmentList.lastIndexOf(obj), false);
	}

	/** Remove the path's elements after the
	 * specified one which is starting
	 * at the specified point. The specified element will
	 * not be removed.
	 *
	 * <p>This function removes after the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj is the segment to remove
	 * @param pt is the point on which the segment was connected
	 *     as its first point.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeAfterLast(ST obj, PT pt) {
		return removeAfter(lastIndexOf(obj, pt), false);
	}

	/** Remove the path's elements after the
	 * specified one which is starting
	 * at the specified point. The specified element will
	 * be removed.
	 *
	 * <p>This function removes after the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj is the segment to remove
	 * @param pt is the point on which the segment was connected
	 *     as its first point.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeFromLast(ST obj, PT pt) {
		return removeAfter(lastIndexOf(obj, pt), true);
	}

	/** Remove the path's elements after the
	 * specified one. The specified element will
	 * also be removed.
	 *
	 * <p>This function removes until the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference segment.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeFromLast(ST obj) {
		return removeAfter(this.segmentList.lastIndexOf(obj), true);
	}

	/** Remove the path's elements after the
	 * specified one which is starting
	 * at the specified point. The specified element will
	 * be removed.
	 *
	 * <p>This function removes after the <i>first occurence</i>
	 * of the given object.
	 *
	 * @param obj is the segment to remove
	 * @param pt is the point on which the segment was connected
	 *     as its first point.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeFrom(ST obj, PT pt) {
		return removeAfter(indexOf(obj, pt), true);
	}

	/** Remove the path's elements after the
	 * specified one. The specified element will
	 * also be removed.
	 *
	 * <p>This function removes until the <i>first occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference segment.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeFrom(ST obj) {
		return removeAfter(this.segmentList.indexOf(obj), true);
	}

	private int indexOf(ST obj, PT startPoint) {
		final Iterator<ST> iterator = this.segmentList.iterator();
		PT current = this.startingPoint;
		ST segment;
		int i = 0;

		while (iterator.hasNext()) {
			segment = iterator.next();
			if (segment.equals(obj) && current.equals(startPoint)) {
				return i;
			}
			++i;
			current = segment.getOtherSidePoint(current);
		}

		return -1;
	}

	@Pure
	@Override
	public int indexOf(Object obj) {
		return this.segmentList.indexOf(obj);
	}

	private int lastIndexOf(ST obj, PT startPoint) {
		PT current = this.endingPoint;
		ST segment;
		int i = this.segmentList.size() - 1;

		while (i >= 0) {
			segment = this.segmentList.get(i);
			current = segment.getOtherSidePoint(current);
			if (segment.equals(obj) && current.equals(startPoint)) {
				return i;
			}
			--i;
		}

		return -1;
	}

	@Pure
	@Override
	public int lastIndexOf(Object obj) {
		return this.segmentList.lastIndexOf(obj);
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return filterList(collection, true);
	}

	private boolean filterList(Collection<?> collection, boolean pushOutside) {
		boolean listChanged = false;

		boolean removed;
		ST segment;
		ST previous = null;
		PT pt = this.startingPoint;
		PT first = null;
		PT last = null;
		final Iterator<ST> iterator = this.segmentList.iterator();

		while (iterator.hasNext()) {
			removed = false;
			segment = iterator.next();
			if (pushOutside == collection.contains(segment)) {
				iterator.remove();
				this.length -= segment.getLength();
				if (this.length < 0) {
					this.length = 0;
				}
				removed = true;
				listChanged = true;
			} else if (previous != null) {
				if (last != null && last.equals(pt)) {
					previous = segment;
				} else {
					iterator.remove();
					this.length -= segment.getLength();
					if (this.length < 0) {
						this.length = 0;
					}
					removed = true;
					listChanged = true;
				}
			} else {
				previous = segment;
			}
			if (!removed && first == null) {
				first = pt;
			}
			pt = segment.getOtherSidePoint(pt);
			if (!removed) {
				last = pt;
			}
		}

		if (this.segmentList.isEmpty()) {
			this.startingPoint = null;
			this.endingPoint = null;
			this.isReversable = true;
		} else {
			this.startingPoint = first;
			this.endingPoint = last;
		}

		return listChanged;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		return filterList(collection, false);
	}

	@Override
	public void clear() {
		this.segmentList.clear();
		this.startingPoint = null;
		this.endingPoint = null;
		this.isReversable = true;
		this.length = 0;
	}

	@Override
	public ST get(int index) {
		return this.segmentList.get(index);
	}

	@Override
	public ST set(int index, ST element) {
		if (index >= 0 && index < this.segmentList.size()) {
			final ST oldSegment = this.segmentList.get(index);
			if (!oldSegment.equals(element)) {
				ST segment;
				for (int i = this.segmentList.size() - 1; i >= index; --i) {
					segment = this.segmentList.remove(i);
					this.length -= segment.getLength();
					if (this.length < 0) {
						this.length = 0;
					}
					this.endingPoint = segment.getOtherSidePoint(this.endingPoint);
				}
				add(element);
				return oldSegment;
			}
		}
		return null;
	}

	@Pure
	@Override
	public ListIterator<ST> listIterator() {
		return this.segmentList.listIterator();
	}

	@Pure
	@Override
	public ListIterator<ST> listIterator(int index) {
		return this.segmentList.listIterator(index);
	}

	@Pure
	@Override
	public List<ST> subList(int fromIndex, int toIndex) {
		return this.segmentList.subList(fromIndex, toIndex);
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append("["); 
		if (!isEmpty()) {
			boolean join = false;
			for (final ST segment : this.segmentList) {
				if (join) {
					buffer.append(", "); 
				} else {
					join = true;
				}
				buffer.append(segment.toString());
			}
		}
		buffer.append("]"); 
		return buffer.toString();
	}

	/** Revert the order of the graph segment in this path.
	 */
	public void invert() {
		final PT p = this.startingPoint;
		this.startingPoint = this.endingPoint;
		this.endingPoint = p;
		final int middle = this.segmentList.size() / 2;
		ST segment;
		for (int i = 0, j = this.segmentList.size() - 1; i < middle; ++i, --j) {
			segment = this.segmentList.get(i);
			this.segmentList.set(i, this.segmentList.get(j));
			this.segmentList.set(j, segment);
		}
	}

	@Pure
	@SuppressWarnings("unchecked")
	@Override
	public GP clone() {
		try {
			final GP clone = (GP) super.clone();
			clone.segmentList = new LinkedList<>();
			clone.segmentList.addAll(this.segmentList);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	private GP splitAt(int index, boolean inclusive) {
		final GP secondPath = clone();
		if (index >= 0) {
			removeAfter(index, inclusive);
			secondPath.removeUntil(index, !inclusive);
		} else {
			secondPath.clear();
		}
		return secondPath;
	}

	/** Split this path and retains the first part of the
	 * part in this object and reply the second part.
	 * The first occurrence of this specified element
	 * will be in the second part.
	 *
	 * <p>This function removes until the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference segment.
	 * @return the rest of the path after the first occurence of the given element.
	 */
	public GP splitAt(ST obj) {
		return splitAt(this.segmentList.indexOf(obj), true);
	}

	/** Split this path and retains the first part of the
	 * part in this object and reply the second part.
	 * The first occurrence of this specified element
	 * will be in the second part.
	 *
	 * <p>This function removes until the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference segment.
	 * @param startPoint is the starting point of the searched segment.
	 * @return the rest of the path after the first occurence of the given element.
	 */
	public GP splitAt(ST obj, PT startPoint) {
		return splitAt(indexOf(obj, startPoint), true);
	}

	/** Split this path and retains the first part of the
	 * part in this object and reply the second part.
	 * The segment at the given position will be in the second part.
	 *
	 * @param position the segment index in the path.
	 * @return the rest of the path after the element at the given position.
	 */
	public GP splitAt(int position) {
		return splitAt(position, true);
	}

	/** Split this path and retains the first part of the
	 * part in this object and reply the second part.
	 * The last occurence of the specified element
	 * will be in the first part.
	 *
	 * <p>This function removes until the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference segment.
	 * @return the rest of the path after the last occurence of the given element.
	 */
	public GP splitAfterLast(ST obj) {
		return splitAt(this.segmentList.lastIndexOf(obj), false);
	}

	/** Split this path and retains the first part of the
	 * part in this object and reply the second part.
	 * The last occurence of the specified element
	 * will be in the first part.
	 *
	 * <p>This function removes until the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj is the segment to search for.
	 * @param startPoint is the starting point of the searched segment.
	 * @return the rest of the path after the last occurence of the given element.
	 */
	public GP splitAfterLast(ST obj, PT startPoint) {
		return splitAt(lastIndexOf(obj, startPoint), false);
	}

	/** Split this path and retains the first part of the
	 * part in this object and reply the second part.
	 * The last occurence of the specified element
	 * will be in the second part.
	 *
	 * <p>This function removes until the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference segment.
	 * @param startPoint is the starting point of the searched segment.
	 * @return the rest of the path after the last occurence of the given element.
	 */
	public GP splitAtLast(ST obj, PT startPoint) {
		return splitAt(lastIndexOf(obj, startPoint), true);
	}

	/** Split this path and retains the first part of the
	 * part in this object and reply the second part.
	 * The last occurence of the specified element
	 * will be in the second part.
	 *
	 * <p>This function removes until the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference segment.
	 * @return the rest of the path after the last occurence of the given element.
	 */
	public GP splitAtLast(ST obj) {
		return splitAt(this.segmentList.lastIndexOf(obj), true);
	}

	/** Split this path and retains the first part of the
	 * part in this object and reply the second part.
	 * The first occurrence of specified element will be
	 * in the first part.
	 *
	 * <p>This function removes until the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference segment.
	 * @return the rest of the path after the first occurence of the given element.
	 */
	public GP splitAfter(ST obj) {
		return splitAt(this.segmentList.indexOf(obj), false);
	}

	/** Split this path and retains the first part of the
	 * part in this object and reply the second part.
	 * The first occurrence of specified element will be
	 * in the first part.
	 *
	 * <p>This function removes until the <i>last occurence</i>
	 * of the given object.
	 *
	 * @param obj the reference segment.
	 * @param startPoint is the starting point of the searched segment.
	 * @return the rest of the path after the first occurence of the given element.
	 */
	public GP splitAfter(ST obj, PT startPoint) {
		return splitAt(indexOf(obj, startPoint), false);
	}

	/** Replies the length of the path.
	 *
	 * @return the length of the path.
	 */
	@Pure
	public double getLength() {
		return this.length;
	}

	/**
	 * Iterable on points.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class PointIterable implements Iterable<PT> {

		/** Construct the iterable of points.
		 */
		PointIterable() {
			//
		}

		@Pure
		@SuppressWarnings("synthetic-access")
		@Override
		public Iterator<PT> iterator() {
			return new PointIterator<>(GraphPath.this.startingPoint, GraphPath.this.segmentList.iterator());
		}
	}

	/**
	 * Iterator on points.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private static class PointIterator<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>> implements Iterator<PT> {

		private PT point;

		private final Iterator<ST> sgmtIterator;

		/**
		 * @param startPoint the starting point.
		 * @param it the segment iterator to use.
		 */
		PointIterator(PT startPoint, Iterator<ST> it) {
			this.sgmtIterator = it;
			this.point = startPoint;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.point != null;
		}

		@Override
		public PT next() {
			final PT toReturn = this.point;
			if (toReturn == null) {
				throw new NoSuchElementException();
			}

			if (this.sgmtIterator.hasNext()) {
				final ST sgmt = this.sgmtIterator.next();
				this.point = sgmt.getOtherSidePoint(this.point);
			} else {
				this.point = null;
			}

			return toReturn;
		}

	}

}
