/* 
 * $Id$
 * 
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.ui.vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object2d.Path2f;
import org.arakhne.afc.math.continous.object2d.PathElement2f;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Vector2D;
import org.arakhne.afc.util.Pair;

/** Vector-oriented utilities for splines. 
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PathUtil {

	/** The ratio used to compute the length of the tangent vectors
	 * at the control points of a spline. The ratio is in
	 * {@code (0;1]} and represents the ratio of the length of
	 * the vector between two control points.
	 */
	public static final float SPLINE_TANGENT_SIZE_FACTOR = .25f ;

	private static void computeBezierCtrlPoints(Point2D pts0, Point2D pts1, Point2D pts2, Point2D ctrl1, Point2D ctrl2) {		
		float v0x = (pts0.getX() - pts1.getX());
		float v0y = (pts0.getY() - pts1.getY());

		float v1x = (pts2.getX() - pts1.getX());
		float v1y = (pts2.getY() - pts1.getY());

		float tangentx = (pts2.getX() - pts0.getX());
		float tangenty = (pts2.getY() - pts0.getY());

		float length = (float)Math.sqrt(tangentx*tangentx + tangenty*tangenty);
		if (length==0f) {
			// Assume left-handed coordinate system, then invert the perpendicular vector to obtain the tangent
			tangentx = v0y;
			tangenty = -v0x;
			length = (float)Math.sqrt(tangentx*tangentx + tangenty*tangenty);
			if (length==0f) {
				ctrl1.set(pts0);
				ctrl2.set(pts0);
				return ;
			}
			tangentx /= length;
			tangenty /= length;
		}
		else {
			tangentx /= length;
			tangenty /= length;
		}

		float length1 = (float)Math.sqrt(v0x*v0x + v0y*v0y) * SPLINE_TANGENT_SIZE_FACTOR;
		float length2 = (float)Math.sqrt(v1x*v1x + v1y*v1y) * SPLINE_TANGENT_SIZE_FACTOR;

		ctrl1.set(pts1.getX() - tangentx * length1, pts1.getY() - tangenty * length1);
		ctrl2.set(pts1.getX() + tangentx * length2, pts1.getY() + tangenty * length2);
	}
	
	/** Create a cubic spline which is passing at the specified
	 * control points.
	 * 
	 * @param path is the path to build.
	 * @param startTangent is set with the tangent to the first point.
	 * @param endTangent is set with the tangent to the last point.
	 * @param controlPath is, if not <code>null</code>, set with the path of 
	 * all the specified control points of the splines and the intermeditate
	 * control points.
	 * @param segmentPath is, if not <code>null</code>, set with the path 
	 * the specified control points of the splines.
	 * @param controlPoints are the control point at which the spline must pass.
	 */
	public static void createCubicSpline(Path2f path, Vector2D startTangent, Vector2D endTangent, Path2f controlPath, Path2f segmentPath, List<? extends Point2D> controlPoints) {
		assert(path!=null);
		if (controlPoints.size()>2) {
			Point2D pts0, pts1, pts2;
			Point2D ctrl1, ctrl2;

			// Compute the control points
			pts0 = controlPoints.get(0);
			pts1 = controlPoints.get(1);
			Point2D pts00 = pts0;

			Point2D[] ctrls = new Point2D[(controlPoints.size()-1)*2];

			for(int i=2, j=1; i<controlPoints.size(); ++i, j+=2) {
				pts2 = controlPoints.get(i);

				ctrl1 = new Point2f();
				ctrl2 = new Point2f();
				computeBezierCtrlPoints(pts0, pts1, pts2, ctrl1, ctrl2);
				assert(ctrls[j]==null);
				ctrls[j] = ctrl1;
				assert(ctrls[j+1]==null);
				ctrls[j+1] = ctrl2;
				
				pts0 = pts1;
				pts1 = pts2;
			}

			{
				float vx = (ctrls[1].getX() - pts00.getX());
				float vy = (ctrls[1].getY() - pts00.getY());
				float length = (float)Math.sqrt(vx*vx + vy*vy);
				ctrls[0] = new Point2f(
						pts00.getX() + vx*SPLINE_TANGENT_SIZE_FACTOR,
						pts00.getY() + vy*SPLINE_TANGENT_SIZE_FACTOR);
				if (startTangent!=null) startTangent.set(vx/length, vy/length);
			}

			{
				pts0 = controlPoints.get(controlPoints.size()-1);
				pts1 = ctrls[ctrls.length-2];
				float vx = (pts1.getX() - pts0.getX());
				float vy = (pts1.getY() - pts0.getY());
				float length = (float)Math.sqrt(vx*vx + vy*vy);
				ctrls[ctrls.length-1] = new Point2f(
						pts0.getX() + vx*SPLINE_TANGENT_SIZE_FACTOR,
						pts0.getY() + vy*SPLINE_TANGENT_SIZE_FACTOR);
				if (endTangent!=null) endTangent.set(vx/length, vy/length);
			}

			// Draw the spline
			pts0 = controlPoints.get(0);
			path.moveTo(pts0.getX(), pts0.getY());
			if (controlPath!=null) {
				controlPath.moveTo(pts0.getX(), pts0.getY());
			}
			if (segmentPath!=null) {
				segmentPath.moveTo(pts0.getX(), pts0.getY());
			}
			
			for(int i=1, j=0; i<controlPoints.size() && j<ctrls.length; ++i, j+=2) {
				pts0 = controlPoints.get(i);
				path.curveTo(
						ctrls[j].getX(), ctrls[j].getY(),
						ctrls[j+1].getX(), ctrls[j+1].getY(),
						pts0.getX(), pts0.getY());
				if (controlPath!=null) {
					controlPath.lineTo(ctrls[j].getX(), ctrls[j].getY());
					controlPath.lineTo(ctrls[j+1].getX(), ctrls[j+1].getY());
					controlPath.lineTo(pts0.getX(), pts0.getY());
				}
				if (segmentPath!=null) {
					segmentPath.lineTo(pts0.getX(), pts0.getY());
				}
			}
		}
		else if (controlPoints.size()==2) {
			Point2D pts0, pts1;
			pts0 = controlPoints.get(0);
			pts1 = controlPoints.get(1);
			path.moveTo(pts0.getX(), pts0.getY());
			path.lineTo(pts1.getX(), pts1.getY());
			startTangent.sub(pts1, pts0);
			startTangent.normalize();
			endTangent.sub(pts0, pts1);
			endTangent.normalize();
		}
	}
	
	private static Vector2D computeTangent(Point2D p1, Point2D p2, Point2D p3) {
		double x = ((p3.getX() - p1.getX()) * .5) + p1.getX() - p2.getX();
		double y = ((p3.getY() - p1.getY()) * .5) + p1.getY() - p2.getY();
		Vector2D v = new Vector2f((float)x, (float)y);
		v.normalize();
		v.perpendicularize();
		return v;
	}
	
	/** Create a quadratic spline which is passing at the specified
	 * control points.
	 * 
	 * @param path is the path to build.
	 * @param startTangent is set with the tangent to the first point.
	 * @param endTangent is set with the tangent to the last point.
	 * @param controlPath is, if not <code>null</code>, set with the path of 
	 * all the specified control points of the splines and the intermeditate
	 * control points.
	 * @param segmentPath is, if not <code>null</code>, set with the path 
	 * the specified control points of the splines.
	 * @param controlPoints are the control point at which the spline must pass.
	 */
	public static void createQuadraticSpline(Path2f path, Vector2D startTangent, Vector2D endTangent, Path2f controlPath, Path2f segmentPath, List<? extends Point2D> controlPoints) {
		if (controlPoints.size()>2) {
			Point2D pts0, pts1, pts2;

			// Compute the control points
			pts0 = controlPoints.get(0);
			pts1 = controlPoints.get(1);

			Vector2D[] tangents = new Vector2D[controlPoints.size()-2];
			Point2D[] ctrls = new Point2D[controlPoints.size()-1];

			for(int i=2, j=0; i<controlPoints.size(); ++i, ++j) {
				pts2 = controlPoints.get(i);
				
				tangents[i-2] = computeTangent(pts0, pts1, pts2);
				
				if (i>2) {
					ctrls[j] = MathUtil.computeLineIntersection(pts0, tangents[i-3], pts1, tangents[i-2]);
				}
				
				pts0 = pts1;
				pts1 = pts2;
			}
			
			{
				pts0 = controlPoints.get(0);
				pts1 = controlPoints.get(1);
				Vector2D v = new Vector2f();
				v.sub(pts1, pts0);
				v.scale(.5f);
				Point2D c = new Point2f(
						pts0.getX()+v.getX(),
						pts0.getY()+v.getY());
				v.perpendicularize();
				ctrls[0] = MathUtil.computeLineIntersection(c, v, pts1, tangents[0]);

				startTangent.sub(ctrls[0], pts0);
				startTangent.normalize();
			}
			
			{
				pts0 = controlPoints.get(controlPoints.size()-1);
				pts1 = controlPoints.get(controlPoints.size()-2);
				Vector2D v = new Vector2f();
				v.sub(pts1, pts0);
				v.scale(.5f);
				Point2D c = new Point2f(
						pts0.getX()+v.getX(),
						pts0.getY()+v.getY());
				v.perpendicularize();
				ctrls[ctrls.length-1] = MathUtil.computeLineIntersection(c, v, pts1, tangents[tangents.length-1]);

				endTangent.sub(ctrls[ctrls.length-1], pts0);
				endTangent.normalize();
			}

			// Draw the spline
			pts0 = controlPoints.get(0);
			path.moveTo(pts0.getX(), pts0.getY());
			if (controlPath!=null) {
				controlPath.moveTo(pts0.getX(), pts0.getY());
			}
			if (segmentPath!=null) {
				segmentPath.moveTo(pts0.getX(), pts0.getY());
			}
			
			for(int i=1, j=0; i<controlPoints.size() && j<ctrls.length; ++i, ++j) {
				pts0 = controlPoints.get(i);
				path.quadTo(ctrls[j].getX(), ctrls[j].getY(), pts0.getX(), pts0.getY());
				if (controlPath!=null) {
					controlPath.lineTo(ctrls[j].getX(), ctrls[j].getY());
					controlPath.lineTo(pts0.getX(), pts0.getY());
				}
				if (segmentPath!=null) {
					segmentPath.lineTo(pts0.getX(), pts0.getY());
				}
			}
		}
		else if (controlPoints.size()==2) {
			Point2D pts0, pts1;
			pts0 = controlPoints.get(0);
			pts1 = controlPoints.get(1);
			path.moveTo(pts0.getX(), pts0.getY());
			path.lineTo(pts1.getX(), pts1.getY());
			startTangent.sub(pts1, pts0);
			startTangent.normalize();
			endTangent.sub(pts0, pts1);
			endTangent.normalize();
		}
	}

	/** Create the polyline that is passing at the specified control points.
	 * 
	 * @param path is the path to fill.
	 * @param startTangent is, if not <code>null</code>, set to the tangent at the first point.
	 * @param endTangent is, if not <code>null</code>, set to the tangent at the last point.
	 * @param controlPoints are the control points to pass at.
	 */
	public static void createSegments(Path2f path, Vector2D startTangent, Vector2D endTangent, List<? extends Point2D> controlPoints) {
		Point2D pts = controlPoints.get(0);
		Point2D ppts = pts;
		path.moveTo(pts.getX(), pts.getY());
		for(int i=1; i<controlPoints.size(); ++i) {
			pts = controlPoints.get(i);
			if (i==1 && startTangent!=null) {
				startTangent.sub(pts, ppts);
				startTangent.normalize();
			}
			if (i==controlPoints.size()-1 && endTangent!=null) {
				endTangent.sub(ppts, pts);
				endTangent.normalize();
			}
			path.lineTo(pts.getX(), pts.getY());
			ppts = pts;
		}
	}

	/** Compute the length of the segments along the specified path.
	 * The size of the array <var>lengths</var> must be the same as
	 * the size of the list <var>controlPoints</var>.
	 * 
	 * @param path is the path to traverse.
	 * @param lengths is the array to fill with the lengths.
	 * @param controlPoints are the control points between the segments.
	 */
	public static void computeSegmentLengths(Path2f path, float[] lengths, List<? extends Point2D> controlPoints) {
		assert(lengths.length==controlPoints.size());
		Iterator<PathElement2f> pathIterator = path.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
		int index = 0 ;
		
		PathElement2f pathElement;

		// Checks all segments
		while (pathIterator.hasNext()) {
			pathElement = pathIterator.next();
			
			if ( ( index < 0 ) || ( index >= controlPoints.size() ) ) {
				index = -1 ;
			}
			else if( controlPoints.get(index+1).equals( 
					new Point2f( pathElement.toX, pathElement.toY ) ) ) {
				index ++ ;
				if (index<lengths.length)
					lengths[index] = 0;
			}
			switch( pathElement.type ) {
			case MOVE_TO:
				break;
			case LINE_TO:
				lengths[index] += MathUtil.distancePointToPoint(
							pathElement.fromX, pathElement.fromY,
							pathElement.toX, pathElement.toY);
				break;
			case CLOSE:
				lengths[index] += MathUtil.distancePointToPoint(
						pathElement.fromX, pathElement.fromY,
						pathElement.toX, pathElement.toY);
				break;
			default:
				throw new IllegalStateException();
			}		
		}
	}
	
	/** Compute the point along the specified path and which
	 * is located at the specified position.
	 * 
	 * @param path is the path.
	 * @param factor is the position factor, between 0 and 1.
	 * @return the point; never <code>null</code>
	 */
	public static Point2D interpolate(Path2f path, float factor) {
		return interpolate(path, factor, null);
	}

	/** Compute the point along the specified path and which
	 * is located at the specified position.
	 * 
	 * @param path is the path.
	 * @param factor is the position factor, between 0 and 1.
	 * @param normal will contain the normal at the specified point.
	 * @return the point; never <code>null</code>
	 */
	public static Point2D interpolate(Path2f path, float factor, Vector2D normal) {
		Iterator<PathElement2f> iterator = path.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
		PathElement2f pathElement;

		Point2D firstPoint = null;
		Point2D lastPoint = null;
		
		float length = 0;
		List<Pair<Float,Point2D>> points = new ArrayList<Pair<Float,Point2D>>();
		float d;
		while (iterator.hasNext()) {
			pathElement = iterator.next();
			switch(pathElement.type) {
			case MOVE_TO:
				lastPoint = new Point2f(pathElement.toX, pathElement.toY);
				if (firstPoint==null) firstPoint = lastPoint;
				points.add(new Pair<Float,Point2D>(length, lastPoint));
				break;
			case LINE_TO:
			case CLOSE:
				d = MathUtil.distanceSquaredPointToPoint(
						pathElement.fromX, pathElement.fromY,
						pathElement.toX, pathElement.toY);
				length += d;
				lastPoint = new Point2f(pathElement.toX, pathElement.toY);
				points.add(new Pair<Float,Point2D>(length, lastPoint));
				break;
			default:
				throw new IllegalStateException();
			}
		}
		
		float distance = factor * length;
		
		Pair<Float,Point2D> pair;
		int l = 0;
		int r = points.size()-1;
		int c;
		while (l<r) {
			c = (l+r)/2;
			pair = points.get(c);
			if (distance==pair.getA()) {
				return pair.getB();
			}
			if (distance<pair.getA()) {
				r = c-1;
			}
			else {
				l = c+1;
			}
		}
		
		if (l<=0) return firstPoint;
		if (l>=points.size()) return lastPoint;
		
		pair = points.get(l-1);
		distance -= pair.getA();
		lastPoint = points.get(l).getB();
		
		distance /= pair.getB().distanceSquared(lastPoint);
		
		if (normal!=null) {
			normal.set(lastPoint.getX(), lastPoint.getY());
			normal.sub(pair.getB().getX(), pair.getB().getY());
			normal.perpendicularize();
			normal.normalize();
		}
		org.arakhne.afc.math.generic.Point2D p = MathUtil.interpolate(
				pair.getB().getX(),
				pair.getB().getY(),
				lastPoint.getX(),
				lastPoint.getY(),
				distance);
		return new Point2f(p.getX(), p.getY());
	}

}
