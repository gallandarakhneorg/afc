/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.object.Direction1D;
import org.arakhne.afc.math.object.Point1D;
import org.arakhne.afc.math.object.Segment1D;
import org.arakhne.afc.math.system.CoordinateSystem2D;



/**
 * Is represented internally as curviline translation.
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
 * @see Point1D
 */
public class Transform1D<S extends Segment1D> {

	private List<S> path;
	private Direction1D singleSegmentDirection;
	private float curvilineTranslation;

	/**
	 * Constructs a new Transform1D object and sets it to the identity.
	 */
	public Transform1D() {
		setIdentity();
	}

	/**
	 * Constructs a new Transform1D object and initializes it from the
	 * specified transform.
	 * 
	 * @param t
	 */
	public Transform1D(Transform1D<? extends S> t) {
		assert(t!=null);
		List<? extends S> p = t.getPath();
		this.path = (p==null || p.isEmpty()) ? null : new ArrayList<S>(p);
		this.singleSegmentDirection = t.singleSegmentDirection;
		this.curvilineTranslation = t.curvilineTranslation;
	}

	/**
	 * Constructs a new Transform1D object and initializes it from the
	 * specified parameters.
	 * 
	 * @param curvilineMove is the distance along the segments.
	 */
	public Transform1D(float curvilineMove) {
		this.path = null;
		this.singleSegmentDirection = Direction1D.SEGMENT_DIRECTION;
		this.curvilineTranslation = curvilineMove;
	}
	
	/**
	 * Constructs a new Transform1D object and initializes it from the
	 * specified parameters.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 * 
	 * @param path is the path to use.
	 * @param curvilineMove is the distance along the segments.
	 */
	public Transform1D(List<? extends S> path, float curvilineMove) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.singleSegmentDirection = Direction1D.SEGMENT_DIRECTION;
		this.curvilineTranslation = curvilineMove;
	}

	/**
	 * Constructs a new Transform1D object and initializes it from the
	 * specified parameters.
	 * 
	 * @param path is the path to use.
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param curvilineMove is the distance along the segments.
	 */
	public Transform1D(List<? extends S> path, Direction1D direction, float curvilineMove) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.singleSegmentDirection = direction;
		this.curvilineTranslation = curvilineMove;
	}

	/**
	 * Constructs a new Transform1D object and initializes it from the
	 * specified parameters.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 * 
	 * @param path is the path to use.
	 */
	public Transform1D(List<? extends S> path) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.singleSegmentDirection = Direction1D.SEGMENT_DIRECTION;
		this.curvilineTranslation = 0.f;
	}

	/**
	 * Constructs a new Transform1D object and initializes it from the
	 * specified parameters.
	 * 
	 * @param path is the path to use.
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 */
	public Transform1D(List<? extends S> path, Direction1D direction) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.singleSegmentDirection = direction;
		this.curvilineTranslation = 0.f;
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
	public Direction1D getSingleSegmentPathDirection() {
		return this.singleSegmentDirection;
	}

	/** Set the path used by this transformation.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 * 
	 * @param path is the new path
	 */
	public void setPath(List<? extends S> path) {
		setPath(path, Direction1D.SEGMENT_DIRECTION);
	}

	/** Set the path used by this transformation.
	 * 
	 * @param path is the new path
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 */
	public void setPath(List<? extends S> path, Direction1D direction) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.singleSegmentDirection = direction;
	}

	/** Set this transformation as identify, ie all set to zero and no path.
	 */
	public void setIdentity() {
		this.path = null;
		this.singleSegmentDirection = Direction1D.SEGMENT_DIRECTION;
		this.curvilineTranslation = 0.f;
	}

	/** Set the position.  This function does not change the path.
	 * 
	 * @param curvilineCoord
	 */
	public void setTranslation(float curvilineCoord) {
		this.curvilineTranslation = curvilineCoord;
	}
	
	/** Set the position.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 * 
	 * @param path
	 * @param curvilineCoord
	 */
	public void setTranslation(List<? extends S> path, float curvilineCoord) {
		setTranslation(path, Direction1D.SEGMENT_DIRECTION, curvilineCoord);
	}

	/** Set the position.
	 * 
	 * @param path
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param curvilineCoord
	 */
	public void setTranslation(List<? extends S> path, Direction1D direction, float curvilineCoord) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.singleSegmentDirection = direction;
		this.curvilineTranslation = curvilineCoord;
	}

	/** Set the position. This function does not change the path.
	 * 
	 * @param position where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void setTranslation(Point1D position) {
		this.curvilineTranslation = position.getCurvilineCoordinate();
	}

	/** Set the position.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 * 
	 * @param path
	 * @param position where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void setTranslation(List<? extends S> path, Point1D position) {
		setTranslation(path, Direction1D.SEGMENT_DIRECTION, position);
	}

	/** Set the position.
	 * 
	 * @param path
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param position where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void setTranslation(List<? extends S> path, Direction1D direction, Point1D position) {
		this.path = (path==null || path.isEmpty()) ? null : new ArrayList<S>(path);
		this.singleSegmentDirection = direction;
		this.curvilineTranslation = position.getCurvilineCoordinate();
	}

	/** Translate the coordinates. This function does not change the path.
	 * 
	 * @param curvilineCoord
	 */
	public void translate(float curvilineCoord) {
		this.curvilineTranslation += curvilineCoord;
	}

	/** Translate.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 *
	 * @param thePath
	 * @param curvilineCoord
	 */
	public void translate(List<? extends S> thePath, float curvilineCoord) {
		translate(thePath, Direction1D.SEGMENT_DIRECTION, curvilineCoord);
	}

	/** Translate.
	 *
	 * @param thePath
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param curvilineCoord
	 */
	public void translate(List<? extends S> thePath, Direction1D direction, float curvilineCoord) {
		this.path = (thePath==null || thePath.isEmpty()) ? null : new ArrayList<S>(thePath);
		this.singleSegmentDirection = direction;
		this.curvilineTranslation += curvilineCoord;
	}

	/** Translate the coordinates. This function does not change the path.
	 * 
	 * @param move where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void translate(Point1D move) {
		this.curvilineTranslation += move.getCurvilineCoordinate();
	}

	/** Translate.
	 * <p>
	 * If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 *
	 * @param thePath
	 * @param move where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void translate(List<? extends S> thePath, Point1D move) {
		translate(thePath, Direction1D.SEGMENT_DIRECTION, move);
	}

	/** Translate.
	 *
	 * @param thePath
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param move where <code>x</code> is the curviline coordinate and <code>y</code> is the jutting coordinate.
	 */
	public void translate(List<? extends S> thePath, Direction1D direction, Point1D move) {
		this.path = (thePath==null || thePath.isEmpty()) ? null : new ArrayList<S>(thePath);
		this.singleSegmentDirection = direction;
		this.curvilineTranslation += move.getCurvilineCoordinate();
	}

	/** Replies the curviline transformation.
	 * 
	 * @return the amount
	 */
	public float getCurvilineTransformation() {
		return this.curvilineTranslation;
	}

	/** Transform the specified 1D point.
	 * If the point is on a segment from the Transform1D path, it will be transformed
	 * to follow the path. If the point is not on a segment from the Transform1D path,
	 * the curviline transformation will be simply added.
	 *
	 * @param point
	 * @return the index of the segment on which the point lies in the segment path;
	 * or <code>-1</code> if the segment of the point is not the first segment in the path.
	 */
	public int transform(Point1D point) {
		assert(point!=null);
		float curviline = point.getCurvilineCoordinate();
		Segment1D currentSegment = point.getSegment();

		int idx;
		
		if (this.path!=null && !this.path.isEmpty()) {

			boolean onFirstSegment = (this.path.get(0).equals(currentSegment));
	
			if (this.curvilineTranslation>0.
				&& currentSegment!=null) {
				if (this.path.size()>1 && onFirstSegment) {
					
					idx = 1;
		
					Segment1D previousSegment = null;
					S nextSegment = (idx<this.path.size()) ? this.path.get(idx) : null;
					
					float rest = this.curvilineTranslation;
					boolean firstLastOrder = currentSegment.isLastPointConnectedTo(nextSegment);
					float restOnCurrentSegment = firstLastOrder  ? currentSegment.getLength() - curviline : curviline;

					while (nextSegment!=null && rest>restOnCurrentSegment) {
						rest -= restOnCurrentSegment;
						idx ++;
						previousSegment = currentSegment;
						currentSegment = nextSegment;
						nextSegment = (idx<this.path.size()) ? this.path.get(idx) : null;
						if ((nextSegment!=null)&&
							(!currentSegment.isLastPointConnectedTo(nextSegment))&&
							(!currentSegment.isFirstPointConnectedTo(nextSegment))) {
							nextSegment = null;
						}
						restOnCurrentSegment = currentSegment.getLength();
					}
					
					if (idx==1) {
						// target segment is the first segment from the path
						assert(nextSegment!=null);
						firstLastOrder = currentSegment.isLastPointConnectedTo(nextSegment);
						if (firstLastOrder)
							curviline += rest;
						else 
							curviline -= rest;
					}
					else {
						if (previousSegment!=null) {
							firstLastOrder = currentSegment.isFirstPointConnectedTo(previousSegment);
						}
						else {
							firstLastOrder = currentSegment.isLastPointConnectedTo(nextSegment);
						}
						curviline = firstLastOrder ? rest : currentSegment.getLength() - rest;
					}
					
					// compute the index of the current segment
					--idx;
				}
				else if (onFirstSegment) {
					// Move according to the given preferred direction
					if (this.singleSegmentDirection.isRevertedSegmentDirection()) {
						curviline -= this.curvilineTranslation;
					}
					else {
						curviline += this.curvilineTranslation;
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
		
		point.set(currentSegment, curviline);
				
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
	public Transform2D toTransform2D(float startX, float startY, float endX, float endY) {
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
		direction.normalize();
		
		if (this.singleSegmentDirection.isRevertedSegmentDirection()) 
			direction.scale(-this.curvilineTranslation);
		else
			direction.scale(this.curvilineTranslation);
		
		trans.setTranslation(direction);
		
		return trans;
	}

	/** Replies a 1.5D transformation that is corresponding to this transformation.
	 * 
	 * @return the 1.5D transformation.
	 */
	public final Transform1D5<S> toTransform1D5() {
		return new Transform1D5<S>(getPath(), getCurvilineTransformation(), 0.f);
	}

}