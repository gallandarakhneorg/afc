/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
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

package org.arakhne.afc.math.geometry2d.continuous;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Matrix2f;
import org.arakhne.afc.math.geometry.GeometryUtil;
import org.arakhne.afc.math.geometry.IntersectionUtil;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry2d.Point2D;
import org.arakhne.afc.math.geometry2d.Vector2D;



/**
 * Definition of a fixed Oriented Bounding Rectangle (OBR),
 * at least a 2D oriented bounding box.
 * <p>
 * Algo inspired from Mathematics for 3D Game Programming and Computer Graphics (MGPCG)
 * and from 3D Game Engine Design (GED)
 * and from Real Time Collision Detection (RTCD).
 * <p>
 * Rotations are not managed yet.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see <a href="http://www.terathon.com/books/mathgames2.html">Mathematics for 3D Game Programming &amp; Computer Graphics</a>
 */
public class OrientedRectangle2f extends AbstractShape2f<OrientedRectangle2f>{	

	/**
	 * 
	 */
	private static final long serialVersionUID = -38383714233472426L;

	
	/**
	 * Compute the oriented bounding box axis.
	 * 
	 * @param <P> is the type of the points.
	 * @param points is the list of the points enclosed by the OBR
	 * @param R is the vector where the R axis of the OBR is put
	 * @param S is the vector where the S axis of the OBR is put
	 * @see "MGPCG pages 219-221"
	 */
	public static <P extends Point2f> void computeOBRAxis(P[] points, Vector2f R, Vector2f S) {
		computeOBRAxis(Arrays.asList(points), R, S);
	}

	/**
	 * Compute the oriented bounding box axis.
	 * 
	 * @param points is the list of the points enclosed by the OBR
	 * @param R is the vector where the R axis of the OBR is put
	 * @param S is the vector where the S axis of the OBR is put
	 * @see "MGPCG pages 219-221"
	 */
	public static void computeOBRAxis(Iterable<? extends Point2f> points, Vector2f R, Vector2f S) {
		// Determining the covariance matrix of the points
		// and set the center of the box
		Matrix2f cov = new Matrix2f();
		GeometryUtil.cov(cov, points);
		
		//Determining eigenvectors of covariance matrix and defines RS axis
		Matrix2f rs = new Matrix2f();//eigenvectors                         
		
		MathUtil.eigenVectorsOfSymmetricMatrix(cov, rs);
		
		rs.getColumn(0,R);
		rs.getColumn(1,S);
	}
	
	/**
	 * Compute the OBR center and extents.
	 * 
	 * @param <P> is the type of the points.
	 * @param points is the list of the points enclosed by the OBR
	 * @param R is the R axis of the OBR
	 * @param S is the S axis of the OBR
	 * @param center is the point which is set with the OBR's center coordinates.
	 * @param extents are the extents of the OBR for the R and S axis.
	 * @see "MGPCG pages 222-223"
	 */
	public static <P extends Point2f> void computeOBRCenterExtents(
			P[] points,
			Vector2f R, Vector2f S,
			Point2f center, float[] extents) {
		computeOBRCenterExtents(Arrays.asList(points), R, S, center, extents);
	}

	/**
	 * Compute the OBR center and extents.
	 * 
	 * @param points is the list of the points enclosed by the OBR
	 * @param R is the R axis of the OBR
	 * @param S is the S axis of the OBR
	 * @param center is the point which is set with the OBR's center coordinates.
	 * @param extents are the extents of the OBR for the R and S axis.
	 * @see "MGPCG pages 222-223"
	 */
	public static void computeOBRCenterExtents(
			Iterable<? extends Point2f> points,
			Vector2f R, Vector2f S,
			Point2f center, float[] extents) {
		assert(points!=null);
		assert(center!=null);
		assert(extents!=null && extents.length>=2);
		
		float minR = Float.POSITIVE_INFINITY;
		float maxR = Float.NEGATIVE_INFINITY;
		float minS = Float.POSITIVE_INFINITY;
		float maxS = Float.NEGATIVE_INFINITY;
		
		float PdotR;
		float PdotS;
		Vector2f v = new Vector2f();
		
		for(Point2f tuple : points) {
			v.set(tuple);

			PdotR = v.dot(R);
			PdotS = v.dot(S);
			
			if (PdotR < minR) minR = PdotR;			
			if (PdotR > maxR) maxR = PdotR;			
			if (PdotS < minS) minS = PdotS;			
			if (PdotS > maxS) maxS = PdotS;			
		}
		
		float a = (maxR + minR) / 2.f;
		float b = (maxS + minS) / 2.f;
		
		// Set the center of the OBR
		center.set(
				a*R.getX()
				+b*S.getX(),
				
				a*R.getY()
				+b*S.getY());
		
		// Compute extents
		extents[0] = (maxR - minR) / 2.f;
		extents[1] = (maxS - minS) / 2.f;
	}
	
	
	/**
	 * Center of the OBR
	 */
	private float cx;
	
	/**
	 * Center of the OBR
	 */
	private float cy;
	
	/**
	 * X coordinate of the first axis of the OBR
	 */
	private float rx;
	
	/**
	 * Y coordinate of the first axis of the OBR
	 */
	private float ry;
	
	/**
	 * X coordinate of the second axis of the OBR
	 */
	private float sx;
	
	/**
	 * Y coordinate of the second axis of the OBR
	 */
	private float sy;
	
	/**
	 * Half-size of the first axis of the OBR
	 */
	private float extentR;
	
	/**
	 * Half-size of the second axis of the OBR
	 */
	private float extentS;
	
	/**
	 */
	public OrientedRectangle2f() {
		//
	}
	
	/**
	 */
	public OrientedRectangle2f(OrientedRectangle2f r) {
		this.cx = r.getCx();
		this.cy = r.getCy();
		this.rx = r.getRx();
		this.ry = r.getRy();
		this.sx = r.getSx();
		this.sy = r.getSy();
		this.extentR = r.getExtentR();
		this.extentS = r.getExtentS();
	}
	
	public OrientedRectangle2f(Point2f center, Vector2f axis1, float axis1Extent, float axis2Extent){
		
		//assert(axis1.isUnitVector()); //TODO:
		assert(axis1Extent > 0 && axis2Extent > 0);
		
		if(axis1 != null && center != null){
			this.cx = center.getX();
			this.cy = center.getY();
			this.rx = axis1.getX();
			this.ry = axis1.getY();
			Vector2f axis2 = new Vector2f(axis1);
			
			axis2.perpendicularize();
			
			this.sx = axis2.getX();
			this.sy= axis2.getY();
			
			this.extentR = axis1Extent;
			this.extentS = axis2Extent;
		}

	}
	
	/**the vector (rx,ry) must be normalized
	 * 
	 * @param centerx
	 * @param centery
	 * @param rx
	 * @param ry
	 * @param axis1Extent
	 * @param axis2Extent
	 */
	public OrientedRectangle2f(float centerx, float centery, float rx, float ry, float axis1Extent, float axis2Extent){
		
		assert(axis1Extent > 0 && axis2Extent > 0);
		
		this.cx = centerx;
		this.cy = centery;
		this.rx = rx;
		this.ry = ry;
		
		Vector2f axis2 = new Vector2f(rx, ry);
		
		assert(axis2.lengthSquared() == 1);
		
		axis2.perpendicularize();
		
		this.sx = axis2.getX();
		this.sy= axis2.getY();
		
		this.extentR = axis1Extent;
		this.extentS = axis2Extent;
	}
	
	public OrientedRectangle2f(Point2f... p){
		Vector2f R,S;
		
		R = new Vector2f();
		S = new Vector2f();
		
		OrientedRectangle2f.computeOBRAxis(p,R , S);
		
		Point2f center = new Point2f();
		float extents[] = new float[2];
		
		OrientedRectangle2f.computeOBRCenterExtents(p, R, S, center, extents);
		
		this.cx = center.getX();
		this.cy = center.getY();
		this.rx = R.getX();
		this.ry = R.getY();
		this.sx = S.getX();
		this.sy = S.getY();
		this.extentR = extents[0];
		this.extentS = extents[1];
	}

	/**
	 * @return the cx
	 */
	public float getCx() {
		return cx;
	}

	/**
	 * @param cx the cx to set
	 */
	public void setCx(float cx) {
		this.cx = cx;
	}

	/**
	 * @return the cy
	 */
	public float getCy() {
		return cy;
	}

	/**
	 * @param cy the cy to set
	 */
	public void setCy(float cy) {
		this.cy = cy;
	}

	/**
	 * @return the rx
	 */
	public float getRx() {
		return rx;
	}

	/**
	 * @return the ry
	 */
	public float getRy() {
		return ry;
	}

	/**
	 * @return the sx
	 */
	public float getSx() {
		return sx;
	}

	/**
	 * @return the sy
	 */
	public float getSy() {
		return sy;
	}

	/**
	 * @return the extentR
	 */
	public float getExtentR() {
		return extentR;
	}

	/**
	 * @param extentR the extentR to set
	 */
	public void setExtentR(float extentR) {
		if(extentR > 0)
			this.extentR = extentR;
	}

	/**
	 * @return the extentS
	 */
	public float getExtentS() {
		return extentS;
	}

	/**
	 * @param extentS the extentS to set
	 */
	public void setExtentS(float extentS) {
		if(extentS > 0)
			this.extentS = extentS;
	}
	
	public Vector2f getR(){
		return new Vector2f(this.rx, this.ry);
	}
	
	public Vector2f getS(){
		return new Vector2f(this.sx, this.sy);
	}
	
	
	/**Set the R(rx,ry) vector with the vector newR this function also recompute Sx
	 * 
	 * @param newR a non-zero normalized vector
	 */
	public void setR(Vector2f newR){
		assert(newR.lengthSquared() == 1);
		if(newR != null){
			this.rx = newR.getX();
			this.ry = newR.getY();
			Vector2f axis2 = new Vector2f(newR);
			
			axis2.perpendicularize();
			
			this.sx = axis2.getX();
			this.sy= axis2.getY();
		}
	}

	@Override
	public Rectangle2f toBoundingBox() {
		Rectangle2f rect = new Rectangle2f();
		toBoundingBox(rect);
		return rect;
	}

	@Override
	public void toBoundingBox(Rectangle2f box) {
		Point2f minCorner, maxCorner;
		
		minCorner = new Point2f(cx,cy);
		maxCorner = new Point2f(cx,cy);
		
		float srx, sry, ssx, ssy;
		
		srx = this.rx* this.extentR;
		sry = this.ry* this.extentR;
		ssx = this.sx* this.extentS;
		ssy = this.sy* this.extentS;
		
		if(rx >= 0)
			if(ry >= 0){
				minCorner.add(-srx + ssx, -sry - ssy);
				maxCorner.sub(-srx + ssx, -sry - ssy);
			}
			else{
				minCorner.add(-srx - ssx, sry - ssy);
				maxCorner.sub(-srx - ssx, sry - ssy);
			}
		else{
			if(ry >= 0){
				minCorner.add( srx + ssx, -sry + ssy);
				maxCorner.sub( srx + ssx, -sry + ssy);
			}else{
				minCorner.add( srx - ssx, sry + ssy);
				maxCorner.sub( srx - ssx, sry + ssy);
			}
		}
		box.setFromCorners(minCorner, maxCorner);
	}

	@Override
	public float distanceSquared(Point2D p) {
		Point2f closest = new Point2f();
		GeometryUtil.closestFarthestPointsPointOBR(p.getX(), p.getY(), cx, cx, rx, ry, sx, sx, extentR, extentS, closest, (Point2f) null);
		return GeometryUtil.distanceSquaredPointPoint(p.getX(), p.getY(), closest.getX(), closest.getY());
	}

	@Override
	public float distanceL1(Point2D p) {
		Point2f closest = new Point2f();
		GeometryUtil.closestFarthestPointsPointOBR(p.getX(), p.getY(), cx, cx, rx, ry, sx, sx, extentR, extentS, closest, null);
	
		return 	GeometryUtil.distanceL1PointPoint(p.getX(), p.getY(), closest.getX(), closest.getY());
	}

	@Override
	public float distanceLinf(Point2D p) {
		Point2f closest = new Point2f();
		GeometryUtil.closestFarthestPointsPointOBR(p.getX(), p.getY(), cx, cx, rx, ry, sx, sx, extentR, extentS, closest, null);
	
		return 	GeometryUtil.distanceLInfPointPoint(p.getX(), p.getY(), closest.getX(), closest.getY());
	}

	@Override
	public void translate(float dx, float dy) {
		cx += dx;
		cy += dy;
	}

	@Override
	public boolean contains(float x, float y) {
		
		return GeometryUtil.isInsidePointOrientedRectangle(x, y, this.cx, this.cy, this.rx, this.ry, this.sx, this.sy, extentR, extentS);
	}

	@Override
	public boolean contains(Rectangle2f r) {
		
		GeometryUtil.isInsideRectangleOrientedRectangle(
			r.minx, r.miny, r.maxy, r.maxy,
			this.cx, this.cy, this.rx, this.ry, this.sx, this.sy, extentR, extentS);
		return false;
	}

	@Override
	public PathIterator2f getPathIterator(Transform2D transform) {
		if (transform==null) {
			return new CopyPathIterator(
					this.cx, this.cy, this.rx, this.ry, extentR, extentS);
		}
		return new TransformPathIterator(
				this.cx, this.cy, this.rx, this.ry, extentR, extentS,
				transform);
	}

	@Override
	public boolean intersects(Rectangle2f s) {
		return IntersectionUtil.intersectsAlignedRectangleOrientedRectangle(
				s.minx, s.miny, s.maxy, s.maxy,
				this.cx, this.cy, this.rx, this.ry, this.sx, this.sy, extentR, extentS);
	}

	@Override
	public boolean intersects(Ellipse2f s) {
		return IntersectionUtil.intersectsEllipseOrientedRectangle(
				s.minx, s.miny, s.maxy - s.minx, s.maxy - s.maxy,
				this.cx, this.cy, this.rx, this.ry, this.sx, this.sy, extentR, extentS);
	}

	@Override
	public boolean intersects(Circle2f s) {
		return IntersectionUtil.intersectsSolidCircleOrientedRectangle(
				s.cx,s.cy, s.radius,
				this.cx, this.cy, this.rx, this.ry, this.sx, this.sy, extentR, extentS, 0);
	}

	@Override
	public boolean intersects(Segment2f s) {
		return IntersectionUtil.intersectsSegmentOrientedRectangle(
				s.ax, s.ay, s.bx, s.by,
				this.cx, this.cy, this.rx, this.ry, this.sx, this.sy, extentR, extentS);
	}
	
	@Override
	public boolean intersects(OrientedRectangle2f s) {
		return IntersectionUtil.intersectsOrientedRectangleOrientedRectangle(
				cx, cy, rx, ry, sx, sy, extentR, extentS,
				s.getCx(), s.getCy(), s.getRx(), s.getRy(), s.getSx(), s.getSy(), s.getExtentR(), s.getExtentS());
	}

	@Override
	public boolean intersects(Path2f s) {
				
		return intersects(s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}

	
	@Override
	public boolean intersects(PathIterator2f s) {
		if (isEmpty()) return false;
		int mask = (s.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2);
		
		
		int crossings = Path2f.computeCrossingsFromRect(
				new TranformPathIteratorWrapper(s),  extentR/-2, extentS/-2, extentR/2, extentS/2,
				false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	private class TranformPathIteratorWrapper implements PathIterator2f{

		private final PathIterator2f p;
		
		public TranformPathIteratorWrapper(PathIterator2f p){
			this.p = p;
		}
		
		@Override
		public boolean hasNext() {
			return this.p.hasNext();
		}

		@Override
		public PathElement2f next() {
			PathElement2f elem = this.p.next();
			switch(elem.type){
			case CURVE_TO:
				return new PathElement2f.CurvePathElement2f(
						(elem.fromX-cx) * sy - (elem.fromY-cy) * sx, (elem.fromY-cy) * rx - (elem.fromX-cx) * ry,
						(elem.ctrlX1-cx) * sy - (elem.ctrlY1-cy) * sx, (elem.ctrlY1-cy) * rx - (elem.ctrlX1-cx) * ry,
						(elem.ctrlX2-cx) * sy - (elem.ctrlY2-cy) * sx, (elem.ctrlY2-cy) * rx - (elem.ctrlX2-cx) * ry,
						(elem.toX-cx) * sy - (elem.toY-cy) * sx, (elem.toY-cy) * rx - (elem.toX-cx) * ry);
			case LINE_TO:
				return new PathElement2f.LinePathElement2f(
						(elem.fromX-cx) * sy - (elem.fromY-cy) * sx, (elem.fromY-cy) * rx - (elem.fromX-cx) * ry,
						(elem.toX-cx) * sy - (elem.toY-cy) * sx, (elem.toY-cy) * rx - (elem.toX-cx) * ry);
			case MOVE_TO:
				return new PathElement2f.MovePathElement2f((elem.toX-cx) * sy - (elem.toY-cy) * sx, (elem.toY-cy) * rx - (elem.toX-cx) * ry);
			case QUAD_TO:
				return new PathElement2f.QuadPathElement2f(
						(elem.fromX-cx) * sy - (elem.fromY-cy) * sx, (elem.fromY-cy) * rx - (elem.fromX-cx) * ry,
						(elem.ctrlX1-cx) * sy - (elem.ctrlY1-cy) * sx, (elem.ctrlY1-cy) * rx - (elem.ctrlX1-cx) * ry,
						(elem.toX-cx) * sy - (elem.toY-cy) * sx, (elem.toY-cy) * rx - (elem.toX-cx) * ry);
			case CLOSE:
			default:
				break;
			
			}
			return null;
		}

		@Override
		public void remove() {
			this.p.remove();			
		}

		@Override
		public PathWindingRule getWindingRule() {
			return this.p.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return this.p.isPolyline();
		}
		
		
		
	}
	@Override
	public boolean isEmpty() {
		return(this.extentR == 0 || this.extentS == 0 || (this.rx ==0 && this.ry == 0) || (this.sx == 0 && this.sy ==0));
	}

	@Override
	public void clear() {
		
		this.extentR = this.extentS = this.rx = this.ry = this.sx = this.sy = this.cx = this.cy = 0;
	}

	@Override
	public Point2D getClosestPointTo(Point2D p) {
		Point2f closest = new Point2f();
		GeometryUtil.closestFarthestPointsPointOBR(
				p.getX(), p.getY(),this.cx, this.cy, this.rx, this.ry, this.sx, this.sy, extentR, extentS, closest, null);
		return closest;
	}

	@Override
	public boolean equals(Object obj) {

		if (this==obj) return true;
		if (obj instanceof OrientedRectangle2f) {
			OrientedRectangle2f s = (OrientedRectangle2f)obj;
			if(s.cx == this.cx &&
			   s.cy == this.cy &&
			   s.rx == this.rx &&
			   s.ry == this.ry &&
			   s.sx == this.sx &&
			   s.sy == this.sy &&
			   s.extentR == this.extentR &&
			   s.extentS == this.extentS)
				return true;
		}
		return false;

	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + floatToIntBits(getCx());
		bits = 31L * bits + floatToIntBits(getCy());
		bits = 31L * bits + floatToIntBits(getRx());
		bits = 31L * bits + floatToIntBits(getRy());
		bits = 31L * bits + floatToIntBits(getSx());
		bits = 31L * bits + floatToIntBits(getSy());
		bits = 31L * bits + floatToIntBits(getExtentR());
		bits = 31L * bits + floatToIntBits(getExtentS());
		return (int) (bits ^ (bits >> 32));
	}
	
	/** Iterator on the path elements of the rectangle.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CopyPathIterator implements PathIterator2f {

		private float x1;
		private float y1;
		private Vector2D r;
		private Vector2D s;
		private int index = 0;

		
		public CopyPathIterator(float centerx, float centery, float rx, float ry, float axis1Extent, float axis2Extent) {
			assert(axis1Extent > 0 && axis2Extent > 0);
			
			this.r = new Vector2f(rx, ry);
			
			assert(this.r.lengthSquared() == 1);
			
			this.s = new Vector2f(rx, ry);
			s.perpendicularize();
			
			r.scale(axis1Extent);
			s.scale(axis2Extent);
			
			this.x1 = centerx - (r.getX() + s.getX());
			this.y1 = centerx - (r.getY() + s.getY());
			
			this.index = 6;
			
			r.scale(2);
			s.scale(2);
		}

		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public PathElement2f next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				return new PathElement2f.MovePathElement2f(
						this.x1, this.y1);
			case 1:
				return new PathElement2f.LinePathElement2f(
						this.x1, this.y1,
						this.x1 + r.getX(), this.y1 + r.getY());
			case 2:
				return new PathElement2f.LinePathElement2f(
						this.x1 + r.getX(), this.y1 + r.getY(),
						this.x1 + r.getX() + s.getX(), this.y1 + r.getY() + s.getY());
			case 3:
				return new PathElement2f.LinePathElement2f(
						this.x1 + r.getX() + s.getX(), this.y1 + r.getY() + s.getY(),
						this.x1 + s.getX(), this.y1 + s.getY());
			case 4:
				return new PathElement2f.LinePathElement2f(
						this.x1 + s.getX(), this.y1 + s.getY(),
						this.x1, this.y1);
			case 5:
				return new PathElement2f.ClosePathElement2f(
						this.x1, this.y1,
						this.x1, this.y1);
			default:
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}
		
		@Override
		public boolean isPolyline() {
			return true;
		}
	}

	/** Iterator on the path elements of the rectangle.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TransformPathIterator implements PathIterator2f {

		private final Transform2D transform;
		private float x1;
		private float y1;
		private Vector2D r;
		private Vector2D s;

		private int index = 0;

		private final Point2D p1 = new Point2f();
		private final Point2D p2 = new Point2f();
		
		public TransformPathIterator(float centerx, float centery, float rx, float ry, float axis1Extent, float axis2Extent, Transform2D transform) {
			
			assert(axis1Extent > 0 && axis2Extent > 0);
			
			this.r = new Vector2f(rx, ry);
			
			assert(this.r.lengthSquared() == 1);
			
			this.s = new Vector2f(rx, ry);
			s.perpendicularize();
			
			r.scale(axis1Extent);
			s.scale(axis2Extent);
						
			this.transform = transform;
			
			this.x1 = centerx - 1/2*(r.getX() + s.getX());
			this.y1 = centerx - 1/2*(r.getY() + s.getY());
			
			this.index = 6;
			
		}

		@Override
		public boolean hasNext() {
			return this.index<=5;
		}

		@Override
		public PathElement2f next() {
			int idx = this.index;
			++this.index;
			switch(idx) {
			case 0:
				this.p2.set(this.x1, this.y1);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.MovePathElement2f(
						this.p2.getX(), this.p2.getY());
			case 1:
				this.p1.set(this.p2);
				this.p2.add(this.r);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 2:
				this.p1.set(this.p2);
				this.p2.add(this.s);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 3:
				this.p1.set(this.p2);
				this.p2.sub(this.r);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 4:
				this.p1.set(this.p2);
				this.p2.sub(this.s);
				if (this.transform!=null) {
					this.transform.transform(this.p2);
				}
				return new PathElement2f.LinePathElement2f(
						this.p1.getX(), this.p1.getY(),
						this.p2.getX(), this.p2.getY());
			case 5:
				return new PathElement2f.ClosePathElement2f(
						this.p2.getX(), this.p2.getY(),
						this.p2.getX(), this.p2.getY());
			default:
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}
		
		@Override
		public boolean isPolyline() {
			return true;
		}

	}
}
