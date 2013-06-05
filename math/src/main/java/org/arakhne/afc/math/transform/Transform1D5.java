/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.object.Direction1D;
import org.arakhne.afc.math.object.Point1D5;
import org.arakhne.afc.math.object.Segment1D;
import org.arakhne.afc.math.system.CoordinateSystem2D;

/**
 * Is represented internally as curviline translation and a jutting/shifting translation.
 * <p>
 * The jutting distance is positive or
 * negative according to the side vector of the current {@link CoordinateSystem2D}.
 * <p>
 * If a path was given, this transformation uses this list of the segments to translate
 * a point and change its associated segment if required. If no path was given to
 * this transformation, all the translation will be located on the current segment
 * space (even if the coordinates are too big or too low to be on the segment).
 * The transformed entities (<code>Point1D5</code>...) is supporsed to be on
 * the segments of the path if it was supplied. The path is the list of the segments
 * which must follow the current segment of the entity.
 * <p>
 * Caution, the stored path is the reference passed to the functions, not a copy.
 * <p>
 * If a path was given, the translation is positive along the path. If a path
 * was not given, the translation will be directly applied to the coordinates. 
 *
 * @param <S> is the type of the segments supported by this transformation.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see Point1D5
 */
public class Transform1D5<S extends Segment1D> {

	private static Direction1D detectFirstSegmentDirection(List<? extends Segment1D> path) {
		if (path!=null && path.size()>1) {
			int i = 1;
			Segment1D s1 = path.get(0);
			Segment1D s2 = path.get(i);
			boolean eq;
			while ((eq=s1.equals(s2)) && i<(path.size()-1)) {
				++i;
				s1 = s2;
				s2 = path.get(i);
			}
			if (!eq) {
				boolean sgDir = s1.isLastPointConnectedTo(s2);
				if ((i%2)==0) sgDir = !sgDir;
				return sgDir ? Direction1D.SEGMENT_DIRECTION : Direction1D.REVERTED_DIRECTION;
			}
		}
		return Direction1D.BOTH_DIRECTIONS;
	}

	private List<S> path;
	private Direction1D firstSegmentDirection;
	private float curvilineTranslation;
	private float juttingTranslation;

	/**
	 * Constructs a new Transform1D5 object and sets it to the identity.
	 */
	public Transform1D5() {
		setIdentity();
	}

	/**
	 * Constructs a new Transform1D5 object and initializes it from the
	 * specified transform.
	 * 
	 * @param t
	 */
	public Transform1D5(Transform1D5<? extends S> t) {
		assert(t!=null);
		List<? extends S> p = t.getPath();
		this.path = (p==null || p.isEmpty()) ? null : new ArrayList<S>(p);
		this.firstSegmentDirection = t.firstSegmentDirection;
		this.curvilineTranslation = t.curvilineTranslation;
		this.juttingTranslation = t.juttingTranslation;
	}

	/**
	 * Constructs a new Transform1D5 object and initializes it from the
	 * specified parameters.
	 * 
	 * @param curvilineMove is the distance along the segments.
	 * @param juttingMove is the distance to move on the left or the right
	 */
	public Transform1D5(float curvilineMove, float juttingMove) {
		this.path = null;
		this.firstSegmentDirection = detectFirstSegmentDirection(this.path);
		this.curvilineTranslation = curvilineMove;
		this.juttingTranslation = juttingMove;
	}
	
	/**
	 * Constructs a new Transform1D5 object and initializes it from the
	 * specified parameters.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 * 
	 * @param path is the path to use.
	 * @param curvilineMove is the distance along the segments.
	 * @param juttingMove is the distance to move on the left or the right
	 */
	public Transform1D5(List<? extends S> path, float curvilineMove, float juttingMove) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.firstSegmentDirection = detectFirstSegmentDirection(this.path);
		this.curvilineTranslation = curvilineMove;
		this.juttingTranslation = juttingMove;
	}

	/**
	 * Constructs a new Transform1D5 object and initializes it from the
	 * specified parameters.
	 * 
	 * @param path is the path to use.
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param curvilineMove is the distance along the segments.
	 * @param juttingMove is the distance to move on the left or the right
	 */
	public Transform1D5(List<? extends S> path, Direction1D direction, float curvilineMove, float juttingMove) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.firstSegmentDirection = direction;
		this.curvilineTranslation = curvilineMove;
		this.juttingTranslation = juttingMove;
	}

	/**
	 * Constructs a new Transform1D5 object and initializes it from the
	 * specified parameters.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 * 
	 * @param path is the path to use.
	 */
	public Transform1D5(List<? extends S> path) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.firstSegmentDirection = detectFirstSegmentDirection(this.path);
		this.curvilineTranslation = 0.f;
		this.juttingTranslation = 0.f;
	}

	/**
	 * Constructs a new Transform1D5 object and initializes it from the
	 * specified parameters.
	 * 
	 * @param path is the path to use.
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 */
	public Transform1D5(List<? extends S> path, Direction1D direction) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.firstSegmentDirection = direction;
		this.curvilineTranslation = 0.f;
		this.juttingTranslation = 0.f;
	}
	
	/** Replies if this transformation has a path.
	 * 
	 * @return <code>true</code> if this transformation has a path,
	 * otherwise <code>false</code>.
	 */
	public boolean hasPath() {
		return (this.path!=null && !this.path.isEmpty());
	}

	/** Replies the path used by this transformation.
	 * 
	 * @return the path.
	 */
	public List<S> getPath() {
		if (this.path==null) return Collections.emptyList();
		return Collections.unmodifiableList(this.path);
	}
	
	/** Replies the direction to follow if the path contains
	 * only one segment.
	 * 
	 * @return the direction in case of a one-segment path.
	 */
	public Direction1D getFirstSegmentPathDirection() {
		return this.firstSegmentDirection;
	}

	/** Set the path used by this transformation.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 * 
	 * @param path is the new path
	 */
	public void setPath(List<? extends S> path) {
		setPath(path, detectFirstSegmentDirection(path));
	}

	/** Set the path used by this transformation.
	 * 
	 * @param path is the new path
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 */
	public void setPath(List<? extends S> path, Direction1D direction) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.firstSegmentDirection = direction;
	}

	/** Set this transformation as identify, ie all set to zero and no path.
	 */
	public void setIdentity() {
		this.path = null;
		this.firstSegmentDirection = Direction1D.BOTH_DIRECTIONS;
		this.curvilineTranslation = 0.f;
		this.juttingTranslation = 0.f;
	}

	/** Set the position.  This function does not change the path.
	 * 
	 * @param curvilineCoord
	 * @param juttingCoord
	 */
	public void setTranslation(float curvilineCoord, float juttingCoord) {
		this.curvilineTranslation = curvilineCoord;
		this.juttingTranslation = juttingCoord;
	}
	
	/** Set the position.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 * 
	 * @param path
	 * @param curvilineCoord
	 * @param juttingCoord
	 */
	public void setTranslation(List<? extends S> path, float curvilineCoord, float juttingCoord) {
		setTranslation(path, detectFirstSegmentDirection(path), curvilineCoord, juttingCoord);
	}

	/** Set the position.
	 * 
	 * @param path
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param curvilineCoord
	 * @param juttingCoord
	 */
	public void setTranslation(List<? extends S> path, Direction1D direction, float curvilineCoord, float juttingCoord) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.firstSegmentDirection = direction;
		this.curvilineTranslation = curvilineCoord;
		this.juttingTranslation = juttingCoord;
	}

	/** Set the position. This function does not change the path.
	 * 
	 * @param position where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void setTranslation(Tuple2f<?> position) {
		this.curvilineTranslation = position.getX();
		this.juttingTranslation = position.getY();
	}

	/** Set the position.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 * 
	 * @param path
	 * @param position where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void setTranslation(List<? extends S> path, Tuple2f<?> position) {
		setTranslation(path, detectFirstSegmentDirection(path), position);
	}

	/** Set the position.
	 * 
	 * @param path
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param position where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void setTranslation(List<? extends S> path, Direction1D direction, Tuple2f<?> position) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.firstSegmentDirection = direction;
		this.curvilineTranslation = position.getX();
		this.juttingTranslation = position.getY();
	}

	/** Set the position. This function does not change the path.
	 * 
	 * @param position where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void setTranslation(Point1D5 position) {
		this.curvilineTranslation = position.getCurvilineCoordinate();
		this.juttingTranslation = position.getJuttingDistance();
	}

	/** Set the position.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 * 
	 * @param path
	 * @param position where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void setTranslation(List<? extends S> path, Point1D5 position) {
		setTranslation(path, detectFirstSegmentDirection(path), position);
	}

	/** Set the position.
	 * 
	 * @param path
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param position where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void setTranslation(List<? extends S> path, Direction1D direction, Point1D5 position) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.firstSegmentDirection = direction;
		this.curvilineTranslation = position.getCurvilineCoordinate();
		this.juttingTranslation = position.getJuttingDistance();
	}

	/** Translate the coordinates. This function does not change the path.
	 * 
	 * @param curvilineCoord
	 * @param juttingCoord
	 */
	public void translate(float curvilineCoord, float juttingCoord) {
		this.curvilineTranslation += curvilineCoord;
		this.juttingTranslation += juttingCoord;
	}

	/** Translate.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 *
	 * @param thePath
	 * @param curvilineCoord
	 * @param juttingCoord
	 */
	public void translate(List<? extends S> thePath, float curvilineCoord, float juttingCoord) {
		translate(thePath, detectFirstSegmentDirection(thePath), curvilineCoord, juttingCoord);
	}

	/** Translate.
	 *
	 * @param thePath
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param curvilineCoord
	 * @param juttingCoord
	 */
	public void translate(List<? extends S> thePath, Direction1D direction, float curvilineCoord, float juttingCoord) {
		this.path = (thePath==null || thePath.isEmpty()) ? null : new ArrayList<S>(thePath);
		this.firstSegmentDirection = direction;
		this.curvilineTranslation += curvilineCoord;
		this.juttingTranslation += juttingCoord;
	}

	/** Translate the coordinates. This function does not change the path.
	 * 
	 * @param move where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void translate(Tuple2f<?> move) {
		this.curvilineTranslation += move.getX();
		this.juttingTranslation += move.getY();
	}

	/** Translate.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 *
	 * @param thePath
	 * @param move where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void translate(List<? extends S> thePath, Tuple2f<?> move) {
		translate(thePath, detectFirstSegmentDirection(thePath), move);
	}

	/** Translate.
	 *
	 * @param thePath
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param move where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void translate(List<? extends S> thePath, Direction1D direction, Tuple2f<?> move) {
		this.path = (thePath==null || thePath.isEmpty()) ? null : new ArrayList<S>(thePath);
		this.firstSegmentDirection = direction;
		this.curvilineTranslation += move.getX();
		this.juttingTranslation += move.getY();
	}

	/** Translate the coordinates. This function does not change the path.
	 * 
	 * @param move where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void translate(Point1D5 move) {
		this.curvilineTranslation += move.getCurvilineCoordinate();
		this.juttingTranslation += move.getJuttingDistance();
	}

	/** Translate.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 *
	 * @param thePath
	 * @param move where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void translate(List<? extends S> thePath, Point1D5 move) {
		translate(thePath, detectFirstSegmentDirection(thePath), move);
	}

	/** Translate.
	 *
	 * @param thePath
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param move where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void translate(List<? extends S> thePath, Direction1D direction, Point1D5 move) {
		this.path = (thePath==null || thePath.isEmpty()) ? null : new ArrayList<S>(thePath);
		this.firstSegmentDirection = direction;
		this.curvilineTranslation += move.getCurvilineCoordinate();
		this.juttingTranslation += move.getJuttingDistance();
	}

	/** Replies the curviline transformation.
	 * 
	 * @return the amount
	 */
	public float getCurvilineTransformation() {
		return this.curvilineTranslation;
	}

	/** Replies the jutting transformation.
	 * 
	 * @return the amount
	 */
	public float getJuttingTransformation() {
		return this.juttingTranslation;
	}

	/** Replies the translation.
	 * 
	 * @return the amount
	 */
	public Vector2f getTranslationVector() {
		return new Vector2f(this.curvilineTranslation, this.juttingTranslation);
	}
	
	/** Transform the specified 1.5D point.
	 * If the point is on a segment from the Transform1D5 path, it will be transformed
	 * to follow the path. If the point is not on a segment from the Transform1D5 path,
	 * the curviline and jutting transformations will be simply added.
	 *
	 * @param point
	 * @return the index of the segment on which the point lies in the segment path;
	 * or <code>-1</code> if the segment of the point is not the first segment in the path.
	 */
	public int transform(Point1D5 point) {
		assert(point!=null);
		boolean invertJutting = false;
		float curviline = point.getCurvilineCoordinate();
		float jutting = point.getJuttingDistance();
		Segment1D currentSegment = point.getSegment();

		if (this.firstSegmentDirection.isSegmentDirection())
			jutting += this.juttingTranslation;
		else
			jutting -= this.juttingTranslation;
		
		int idx;
		
		if (this.path!=null && !this.path.isEmpty()) {

			boolean onFirstSegment = (this.path.get(0).equals(currentSegment));
	
			if (this.curvilineTranslation>0.
				&& currentSegment!=null) {
				if (this.path.size()>1 && onFirstSegment) {
					
					idx = 1;
		
					Segment1D previousSegment = null;
					S nextSegment = (idx<this.path.size()) ? this.path.get(idx) : null;
					boolean previousSameSegment;
					boolean sameSegment = nextSegment!=null && currentSegment.equals(nextSegment);
					
					float rest = this.curvilineTranslation;
					
					boolean firstLastOrder;
					if (sameSegment)
						firstLastOrder = this.firstSegmentDirection.isSegmentDirection();
					else
						firstLastOrder = currentSegment.isLastPointConnectedTo(nextSegment);
					
					float restOnCurrentSegment = firstLastOrder  ? currentSegment.getLength() - curviline : curviline;
					boolean connectedByFirst, connectedByLast;
					boolean connectedByFirst2, connectedByLast2;

					if (nextSegment!=null) {
						if (sameSegment) {
							connectedByFirst = connectedByLast = true;
						}
						else {
							connectedByFirst = currentSegment.isFirstPointConnectedTo(nextSegment);
							connectedByLast = currentSegment.isLastPointConnectedTo(nextSegment);
						}
					}
					else {
						connectedByFirst = connectedByLast = false;
					}

					while (nextSegment!=null && rest>restOnCurrentSegment) {
						rest -= restOnCurrentSegment;
						
						if (sameSegment) {
							connectedByFirst2 = connectedByLast2 = true;
						}
						else {
							connectedByFirst2 = nextSegment.isFirstPointConnectedTo(currentSegment);
							connectedByLast2 = nextSegment.isLastPointConnectedTo(currentSegment);
						}
						
						if ((connectedByFirst && connectedByFirst2)
							 ||
							 (connectedByLast && connectedByLast2)) {
							invertJutting = !invertJutting;
						}
						
						idx ++;
						previousSegment = currentSegment;
						previousSameSegment = sameSegment;
						currentSegment = nextSegment;
						
						nextSegment = (idx<this.path.size()) ? this.path.get(idx) : null;						
						if (nextSegment!=null) {
							connectedByFirst2 = currentSegment.isFirstPointConnectedTo(nextSegment);
							connectedByLast2 = currentSegment.isLastPointConnectedTo(nextSegment);
							if (!connectedByFirst2 && !connectedByLast2) {
								nextSegment = null;
							}
						}
						restOnCurrentSegment = currentSegment.getLength();

						connectedByFirst = connectedByFirst2;
						connectedByLast = connectedByLast2;
						
						sameSegment = nextSegment!=null && currentSegment.equals(nextSegment);
						
						if (sameSegment) {
							firstLastOrder = !firstLastOrder;
						}
						else if (nextSegment!=null) {
							firstLastOrder = currentSegment.isLastPointConnectedTo(nextSegment);
						}
						else if (previousSegment!=null) {
							if (previousSameSegment)
								firstLastOrder = !firstLastOrder;
							else
								firstLastOrder = currentSegment.isFirstPointConnectedTo(previousSegment);
						}
					}
					
					if (idx==1) {
						// Target segment is the first segment from the path.
						// firstLastOrder has never changed from its initial value.
						if (firstLastOrder)
							curviline += rest;
						else 
							curviline -= rest;
					}
					else {
						curviline = firstLastOrder ? rest : currentSegment.getLength() - rest;
					}
					
					// compute the index of the current segment
					--idx;
				}
				else if (onFirstSegment) {
					// Move according to the given preferred direction
					if (this.firstSegmentDirection.isSegmentDirection()) {
						curviline += this.curvilineTranslation;
					}
					else {
						curviline -= this.curvilineTranslation;
					}
					idx = 0;
				}
				else {
					curviline += this.curvilineTranslation;
					idx = -1;
				}
			}
			else {
				curviline += this.curvilineTranslation;
				idx = -1;
			}
		}
		else {
			curviline += this.curvilineTranslation;
			idx = -1;
		}
		
		if (invertJutting) {
			jutting = -jutting;
		}

		point.set(currentSegment, curviline, jutting);
		
		return idx;
	}

	/** Replies a 2D transformation that is corresponding to this transformation.
	 * <p>
	 * The used coordinate system is replied by {@link CoordinateSystem2D#getDefaultCoordinateSystem()}.
	 * 
	 * @param segment is the segment.
	 * @return the 2D transformation or <code>null</code> if the segment could not be mapped
	 * to 2D.
	 */
	public final Transform2D toTransform2D(Segment1D segment) {
		assert(segment!=null);
		Point2f a = segment.getFirstPoint();
		if (a==null) return null;
		Point2f b = segment.getLastPoint();
		if (b==null) return null;
		return toTransform2D(a.getX(), a.getY(), b.getX(), b.getY(), CoordinateSystem2D.getDefaultCoordinateSystem());
	}

	/** Replies a 2D transformation that is corresponding to this transformation.
	 * <p>
	 * The used coordinate system is replied by {@link CoordinateSystem2D#getDefaultCoordinateSystem()}.
	 * 
	 * @param segment is the segment.
	 * @return the 2D transformation or <code>null</code> if the segment could not be mapped
	 * @param system is the coordinate system to use.
	 * to 2D.
	 */
	public final Transform2D toTransform2D(Segment1D segment, CoordinateSystem2D system) {
		assert(segment!=null);
		Point2f a = segment.getFirstPoint();
		if (a==null) return null;
		Point2f b = segment.getLastPoint();
		if (b==null) return null;
		return toTransform2D(a.getX(), a.getY(), b.getX(), b.getY(), system);
	}

	/** Replies a 2D transformation that is corresponding to this transformation.
	 * <p>
	 * The used coordinate system is replied by {@link CoordinateSystem2D#getDefaultCoordinateSystem()}.
	 * 
	 * @param startPoint is the 2D position of the start point of the segment 
	 * @param endPoint is the 2D position of the end point of the segment
	 * @return the 2D transformation.
	 */
	public final Transform2D toTransform2D(Point2f startPoint, Point2f endPoint) {
		assert(startPoint!=null);
		assert(endPoint!=null);
		return toTransform2D(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(), CoordinateSystem2D.getDefaultCoordinateSystem());
	}
    
	/** Replies a 2D transformation that is corresponding to this transformation.
	 * 
	 * @param startPoint is the 2D position of the start point of the segment 
	 * @param endPoint is the 2D position of the end point of the segment
	 * @param system is the coordinate system to use.
	 * @return the 2D transformation.
	 */
	public final Transform2D toTransform2D(Point2f startPoint, Point2f endPoint, CoordinateSystem2D system) {
		assert(startPoint!=null);
		assert(endPoint!=null);
		return toTransform2D(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(), system);
	}

	/** Replies a 2D transformation that is corresponding to this transformation.
	 * <p>
	 * The used coordinate system is replied by {@link CoordinateSystem2D#getDefaultCoordinateSystem()}.
	 * 
	 * @param startX is the 2D x coordinate of the start point of the segment 
	 * @param startY is the 2D y coordinate of the start point of the segment 
	 * @param endX is the 2D x coordinate of the end point of the segment
	 * @param endY is the 2D y coordinate of the end point of the segment
	 * @return the 2D transformation.
	 */
	public final Transform2D toTransform2D(float startX, float startY, float endX, float endY) {
		return toTransform2D(startX, startY, endX, endY, CoordinateSystem2D.getDefaultCoordinateSystem());
	}

	/** Replies a 2D transformation that is corresponding to this transformation.
	 * 
	 * @param startX is the 2D x coordinate of the start point of the segment 
	 * @param startY is the 2D y coordinate of the start point of the segment 
	 * @param endX is the 2D x coordinate of the end point of the segment
	 * @param endY is the 2D y coordinate of the end point of the segment
	 * @param system is the coordinate system to use.
	 * @return the 2D transformation.
	 */
	public Transform2D toTransform2D(float startX, float startY, float endX, float endY, CoordinateSystem2D system) {
		Transform2D trans = new Transform2D();

		Vector2f direction = new Vector2f(endX-startX, endY-startY);
		
		Vector2f juttingVector;
		juttingVector = MathUtil.perpendicularVector(direction.getX(), direction.getY(), system);

		float c = this.curvilineTranslation;
		float j = this.juttingTranslation;
		
		if (!this.firstSegmentDirection.isSegmentDirection()) {
			c = -c;
			j = -j;
		}
		
		juttingVector.scale(j);
		
		direction.normalize();
		direction.scale(c);
		direction.add(juttingVector);
		
		trans.setTranslation(direction);
		
		return trans;
	}

	/** Replies a 1D transformation that is corresponding to this transformation.
	 * 
	 * @return the 1D transformation.
	 */
	public final Transform1D<S> toTransform1D() {
		return new Transform1D<S>(getPath(), getCurvilineTransformation());
	}

}