/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d3.Point3D;



/** 3D axis-aligned box with floating-point points.
 * 
 * @author $Author: galland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AlignedBox3f extends AbstractBoxedShape3F<AlignedBox3f> {

	private static final long serialVersionUID = 6839969099242151134L;

	/** Replies the point on the shape that is closest to the given point.
	 * 
	 * @param minx x coordinate of the lower point of the box.
	 * @param miny y coordinate of the lower point of the box.
	 * @param minz z coordinate of the lower point of the box.
	 * @param maxx x coordinate of the upper point of the box.
	 * @param maxy y coordinate of the upper point of the box.
	 * @param maxz z coordinate of the upper point of the box.
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param z z coordinate of the point.
	 * @return the closest point on the shape; or the point itself
	 * if it is inside the shape.
	 */
	public static Point3f computeClosestPoint(
			double minx, double miny, double minz,
			double maxx, double maxy, double maxz,
			double x, double y, double z) {
		Point3f closest = new Point3f();
		if (x < minx) {
			closest.setX(minx);
		}
		else if (x > maxx) {
			closest.setX(maxx);
		}
		else {
			closest.setX(x);
		}
		if (y < miny) {
			closest.setY(miny);
		}
		else if (y > maxy) {
			closest.setY(maxy);
		}
		else {
			closest.setY(y);
		}
		if (z < minz) {
			closest.setZ(minz);
		}
		else if (z > maxz) {
			closest.setZ(maxz);
		}
		else {
			closest.setZ(z);
		}
		return closest;
	}

	/**
	 * Tests if the two 3D axis-aligned boxes are intersecting.
	 * <p>
	 * This function is assuming that <var>lx1</var> is lower
	 * or equal to <var>ux1</var>, <var>ly1</var> is lower
	 * or equal to <var>uy1</var>, and so on.
	 *
	 * @param lower1x coordinates of the lowest point of the first box.
	 * @param lower1y coordinates of the lowest point of the first box.
	 * @param lower1z coordinates of the lowest point of the first box.
	 * @param upper1x coordinates of the uppermost point of the first box.
	 * @param upper1y coordinates of the uppermost point of the first box.
	 * @param upper1z coordinates of the uppermost point of the first box.
	 * @param lower2x coordinates of the lowest point of the second box.
	 * @param lower2y coordinates of the lowest point of the second box.
	 * @param lower2z coordinates of the lowest point of the second box.
	 * @param upper2x coordinates of the uppermost point of the second box.
	 * @param upper2y coordinates of the uppermost point of the second box.
	 * @param upper2z coordinates of the uppermost point of the second box.
	 * @return <code>true</code> if the two 3D boxes intersect each 
	 * other; <code>false</code> otherwise.
	 */
	public static boolean intersectsAlignedBoxAlignedBox(
			double lower1x, double lower1y, double lower1z, double upper1x, double upper1y, double upper1z,
			double lower2x, double lower2y, double lower2z, double upper2x, double upper2y, double upper2z) {
		assert(lower1x<=upper1x);
		assert(lower1y<=upper1y);
		assert(lower1z<=upper1z);
		assert(lower2x<=upper2x);
		assert(lower2y<=upper2y);
		assert(lower2z<=upper2z);

		boolean intersects;
		if (lower1x<lower2x) intersects = upper1x>lower2x;
		else intersects = upper2x>lower1x;

		if (intersects) {
			if (lower1y<lower2y) intersects = upper1y>lower2y;
			else intersects = upper2y>lower1y;

			if (intersects) {
				if (lower1z<lower2z) intersects = upper1z>lower2z;
				else intersects = upper2z>lower1z;
			}
		}

		return intersects;
	}

	/** Replies if the given point is inside this shape.
	 * 
	 * @param minx coordinates of the lowest point of the box.
	 * @param miny coordinates of the lowest point of the box.
	 * @param minz coordinates of the lowest point of the box.
	 * @param maxx coordinates of the uppermost point of the box.
	 * @param maxy coordinates of the uppermost point of the box.
	 * @param maxz coordinates of the uppermost point of the box.
	 * @param px x coordinate of the point.
	 * @param py y coordinate of the point.
	 * @param pz z coordinate of the point.
	 * @return <code>true</code> if the given point is inside this
	 * shape, otherwise <code>false</code>.
	 */
	public static boolean containsAlignedBoxPoint(
			double minx, double miny, double minz, double maxx, double maxy, double maxz,
			double px, double py, double pz) {
		return (minx<=px && px<=maxx
				&&
				miny<=py && py<=maxy
				&&
				minz<=pz && pz<=maxz);
	}

	
	/** Lowest x-coordinate covered by this rectangular shape. */
	protected double minx = 0f;
	/** Lowest y-coordinate covered by this rectangular shape. */
	protected double miny = 0f;
	/** Lowest z-coordinate covered by this rectangular shape. */
	protected double minz = 0f;
	/** Highest x-coordinate covered by this rectangular shape. */
	protected double maxx = 0f;
	/** Highest y-coordinate covered by this rectangular shape. */
	protected double maxy = 0f;
	/** Highest z-coordinate covered by this rectangular shape. */
	protected double maxz = 0f;
	
	/**
	 */
	public AlignedBox3f() {
		//
	}
	
	/**
	 * @param min is the min corner of the box.
	 * @param max is the max corner of the box.
	 */
	public AlignedBox3f(Point3f min, Point3f max) {
		setFromCorners(
				min.getX(), min.getY(), min.getZ(),
				max.getX(), max.getY(), max.getZ());
	}
	
	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param sizex
	 * @param sizey
	 * @param sizez
	 */
	public AlignedBox3f(double x, double y, double z, double sizex, double sizey, double sizez) {
		setFromCorners(x, y, z, x+sizex, y+sizey, z+sizez);
	}
	
	/**
	 * @param s
	 */
	public AlignedBox3f(AbstractBoxedShape3F<?> s) {
		this.minx = s.getMinX();
		this.miny = s.getMinY();
		this.minz = s.getMinZ();
		this.maxx = s.getMaxX();
		this.maxy = s.getMaxY();
		this.maxz = s.getMaxZ();
	}

	
	/** Change the frame of the box.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param sizex
	 * @param sizey
	 * @param sizez
	 */
	@Override
	public void set(double x, double y, double z, double sizex, double sizey, double sizez) {
		setFromCorners(x, y, z, x+sizex, y+sizey, z+sizez);
	}
	
	/** Change the frame of te box.
	 * 
	 * @param min is the min corner of the box.
	 * @param max is the max corner of the box.
	 */
	@Override
	public void set(Point3f min, Point3f max) {
		setFromCorners(
				min.getX(), min.getY(), min.getZ(), 
				max.getX(), max.getY(), max.getZ());
	}
	
	/** Change the X-size of the box, not the min corner.
	 * 
	 * @param size
	 */
	@Override
	public void setSizeX(double size) {
		this.maxx = this.minx + Math.max(0f, size);
	}

	/** Change the Y-size of the box, not the min corner.
	 * 
	 * @param size
	 */
	@Override
	public void setSizeY(double size) {
		this.maxy = this.miny + Math.max(0f, size);
	}

	/** Change the Z-size of the box, not the min corner.
	 * 
	 * @param size
	 */
	@Override
	public void setSizeZ(double size) {
		this.maxz = this.minz + Math.max(0f, size);
	}

	/** Change the frame of the box.
	 * 
	 * @param p1 is the coordinate of the first corner.
	 * @param p2 is the coordinate of the second corner.
	 */
	@Override
	public void setFromCorners(Point3D p1, Point3D p2) {
		setFromCorners(
				p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ());
	}

	/** Change the frame of the box.
	 * 
	 * @param x1 is the coordinate of the first corner.
	 * @param y1 is the coordinate of the first corner.
	 * @param z1 is the coordinate of the first corner.
	 * @param x2 is the coordinate of the second corner.
	 * @param y2 is the coordinate of the second corner.
	 * @param z2 is the coordinate of the second corner.
	 */
	@Override
	public void setFromCorners(double x1, double y1, double z1, double x2, double y2, double z2) {
		if (x1<x2) {
			this.minx = x1;
			this.maxx = x2;
		}
		else {
			this.minx = x2;
			this.maxx = x1;
		}
		if (y1<y2) {
			this.miny = y1;
			this.maxy = y2;
		}
		else {
			this.miny = y2;
			this.maxy = y1;
		}
		if (z1<z2) {
			this.minz = z1;
			this.maxz = z2;
		}
		else {
			this.minz = z2;
			this.maxz = z1;
		}
	}
	
	/**
     * Sets the framing box of this <code>Shape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing box is used by the subclasses of
     * <code>BoxedShape</code> to define their geometry.
     *
     * @param centerX the X coordinate of the specified center point
     * @param centerY the Y coordinate of the specified center point
     * @param centerZ the Z coordinate of the specified center point
     * @param cornerX the X coordinate of the specified corner point
     * @param cornerY the Y coordinate of the specified corner point
     * @param cornerZ the Z coordinate of the specified corner point
     */
	@Override
	public void setFromCenter(double centerX, double centerY, double centerZ, double cornerX, double cornerY, double cornerZ) {
		double dx = centerX - cornerX;
		double dy = centerY - cornerY;
		double dz = centerZ - cornerZ;
		setFromCorners(cornerX, cornerY, cornerZ, centerX + dx, centerY + dy, centerZ + dz);
	}
	
	/** Replies the min point.
	 * 
	 * @return the min point.
	 */
	@Override
	public Point3f getMin() {
		return new Point3f(this.minx, this.miny, this.minz);
	}

	/** Replies the min point.
	 * 
	 * @return the min point.
	 */
	@Override
	public Point3f getMax() {
		return new Point3f(this.maxx, this.maxy, this.maxz);
	}

	/** Replies the center point.
	 * 
	 * @return the center point.
	 */
	@Override
	public Point3f getCenter() {
		return new Point3f(
				(this.minx + this.maxx) / 2.,
				(this.miny + this.maxy) / 2.,
				(this.minz + this.maxz) / 2.);
	}

	/** Replies the min X.
	 * 
	 * @return the min x.
	 */
	@Override
	public double getMinX() {
		return this.minx;
	}

	/** Set the min X.
	 * 
	 * @param x the min x.
	 */
	@Override
	public void setMinX(double x) {
		double o = this.maxx;
		if (o<x) {
			this.minx = o;
			this.maxx = x;
		}
		else {
			this.minx = x;
		}
	}

	/** Replies the center x.
	 * 
	 * @return the center x.
	 */
	@Override
	public double getCenterX() {
		return (this.minx + this.maxx) / 2f;
	}

	/** Replies the max x.
	 * 
	 * @return the max x.
	 */
	@Override
	public double getMaxX() {
		return this.maxx;
	}

	/** Set the max X.
	 * 
	 * @param x the max x.
	 */
	@Override
	public void setMaxX(double x) {
		double o = this.minx;
		if (o>x) {
			this.maxx = o;
			this.minx = x;
		}
		else {
			this.maxx = x;
		}
	}

	/** Replies the min y.
	 * 
	 * @return the min y.
	 */
	@Override
	public double getMinY() {
		return this.miny;
	}

	/** Set the min Y.
	 * 
	 * @param y the min y.
	 */
	@Override
	public void setMinY(double y) {
		double o = this.maxy;
		if (o<y) {
			this.miny = o;
			this.maxy = y;
		}
		else {
			this.miny = y;
		}
	}

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	@Override
	public double getCenterY() {
		return (this.miny + this.maxy) / 2f;
	}

	/** Replies the max y.
	 * 
	 * @return the max y.
	 */
	@Override
	public double getMaxY() {
		return this.maxy;
	}
	
	/** Set the max Y.
	 * 
	 * @param y the max y.
	 */
	@Override
	public void setMaxY(double y) {
		double o = this.miny;
		if (o>y) {
			this.maxy = o;
			this.miny = y;
		}
		else {
			this.maxy = y;
		}
	}

	/** Replies the min z.
	 * 
	 * @return the min z.
	 */
	@Override
	public double getMinZ() {
		return this.minz;
	}

	/** Set the min Z.
	 * 
	 * @param z the min z.
	 */
	@Override
	public void setMinZ(double z) {
		double o = this.maxz;
		if (o<z) {
			this.minz = o;
			this.maxz = z;
		}
		else {
			this.minz = z;
		}
	}

	/** Replies the center z.
	 * 
	 * @return the center z.
	 */
	@Override
	public double getCenterZ() {
		return (this.minz + this.maxz) / 2f;
	}

	/** Replies the max z.
	 * 
	 * @return the max z.
	 */
	@Override
	public double getMaxZ() {
		return this.maxz;
	}
	
	/** Set the max Z.
	 * 
	 * @param z the max z.
	 */
	@Override
	public void setMaxZ(double z) {
		double o = this.minz;
		if (o>z) {
			this.maxz = o;
			this.minz = z;
		}
		else {
			this.maxz = z;
		}
	}

	/** Replies the x-size.
	 * 
	 * @return the x-size.
	 */
	@Override
	public double getSizeX() {
		return this.maxx - this.minx;
	}

	/** Replies the y-size.
	 * 
	 * @return the y-size.
	 */
	@Override
	public double getSizeY() {
		return this.maxy - this.miny;
	}
	
	/** Replies the z-size.
	 * 
	 * @return the z-size.
	 */
	@Override
	public double getSizeZ() {
		return this.maxz - this.minz;
	}
	
	/** Set the x bounds of the box.
	 * 
	 * @param min the min value for the x axis.
	 * @param max the max value for the x axis.
	 */
	@Override
	public void setX(double min, double max) {
		if (min <= max) {
			this.minx = min;
			this.maxx = max;
		} else {
			this.minx = max;
			this.maxx = min;
		}
	}


	/** Set the y bounds of the box.
	 * 
	 * @param min the min value for the y axis.
	 * @param max the max value for the y axis.
	 */
	@Override
	public void setY(double min, double max) {
		if (min <= max) {
			this.miny = min;
			this.maxy = max;
		} else {
			this.miny = max;
			this.maxy = min;
		}
	}


	/** Set the z bounds of the box.
	 * 
	 * @param min the min value for the z axis.
	 * @param max the max value for the z axis.
	 */
	@Override
	public void setZ(double min, double max) {
		if (min <= max) {
			this.minz = min;
			this.maxz = max;
		} else {
			this.minz = max;
			this.maxz = min;
		}
	}
	
	
	/** {@inheritDoc}
	 */
	@Override
	public AlignedBox3f toBoundingBox() {
		return this;
	}

	@Override
	public double distanceSquared(Point3D p) {
		double d1 = 0;
		if (p.getX()<this.minx) {
			d1 = this.minx - p.getX();
			d1 = d1*d1;
		}
		else if (p.getX()>this.maxx) {
			d1 = p.getX() - this.maxx;
			d1 = d1*d1;
		}
		double d2 = 0;
		if (p.getY()<this.miny) {
			d2 = this.miny - p.getY();
			d2 = d2*d2;
		}
		else if (p.getY()>this.maxy) {
			d2 = p.getY() - this.maxy;
			d2 = d2*d2;
		}
		double d3 = 0;
		if (p.getZ()<this.minz) {
			d3 = this.minz - p.getZ();
			d3 = d3*d3;
		}
		else if (p.getZ()>this.maxz) {
			d3 = p.getZ() - this.maxz;
			d3 = d3*d3;
		}
		return d1+d2+d3;
	}

	@Override
	public double distanceL1(Point3D p) {
		double d1 = 0;
		if (p.getX()<this.minx) {
			d1 = this.minx - p.getX();
		}
		else if (p.getX()>this.maxx) {
			d1 = p.getX() - this.maxx;
		}
		double d2 = 0;
		if (p.getY()<this.miny) {
			d2 = this.miny - p.getY();
		}
		else if (p.getY()>this.maxy) {
			d2 = p.getY() - this.maxy;
		}
		double d3 = 0;
		if (p.getZ()<this.minz) {
			d3 = this.minz - p.getZ();
		}
		else if (p.getZ()>this.maxz) {
			d3 = p.getZ() - this.maxz;
		}
		return d1+d2+d3;
	}

	@Override
	public double distanceLinf(Point3D p) {
		double d1 = 0;
		if (p.getX()<this.minx) {
			d1 = this.minx - p.getX();
		}
		else if (p.getX()>this.maxx) {
			d1 = p.getX() - this.maxx;
		}
		double d2 = 0;
		if (p.getY()<this.miny) {
			d2 = this.miny - p.getY();
		}
		else if (p.getY()>this.maxy) {
			d2 = p.getY() - this.maxy;
		}
		double d3 = 0;
		if (p.getZ()<this.minz) {
			d3 = this.minz - p.getZ();
		}
		else if (p.getZ()>this.maxz) {
			d3 = p.getZ() - this.maxz;
		}
		return MathUtil.max(d1, d2, d3);
	}

	@Override
	public boolean contains(double x, double y, double z) {
		return containsAlignedBoxPoint(
				this.minx, this.miny, this.minz, this.maxx, this.maxy, this.maxz,
				x, y, z);
	}

	@Override
	public boolean intersects(AlignedBox3f s) {
		return intersectsAlignedBoxAlignedBox(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				s.getMinX(), s.getMinY(), s.getMinZ(),
				s.getMaxX(), s.getMaxY(), s.getMaxZ());
	}

	@Override
	public boolean intersects(AbstractSphere3F s) {
		return AbstractSphere3F.intersectsSolidSphereSolidAlignedBox(
				s.getX(), s.getY(), s.getZ(), s.getRadius(),
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ());
	}

	@Override
	public boolean intersects(AbstractSegment3F s) {
		return AbstractSegment3F.intersectsSegmentAlignedBox(
				s.getX1(), s.getY1(), s.getZ1(),
				s.getX2(), s.getY2(), s.getZ2(),
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ());
	}

	@Override
	public boolean intersects(Triangle3f s) {
		return AbstractTriangle3F.intersectsTriangleAlignedBox(
				s.getX1(), s.getY1(), s.getZ1(),
				s.getX2(), s.getY2(), s.getZ2(),
				s.getX3(), s.getY3(), s.getZ3(),
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ());
	}

	@Override
	public boolean intersects(AbstractCapsule3F s) {
		return AbstractCapsule3F.intersectsCapsuleAlignedBox(
				s.getMedialX1(), s.getMedialY1(), s.getMedialY1(),
				s.getMedialX2(), s.getMedialY2(), s.getMedialY2(),
				s.getRadius(),
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ());
	}

	@Override
	public boolean intersects(AbstractOrientedBox3F s) {
		return AbstractOrientedBox3F.intersectsOrientedBoxAlignedBox(
				s.getCenterX(), s.getCenterY(), s.getCenterZ(),
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisZ(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisZ(),
				s.getThirdAxisX(), s.getThirdAxisY(), s.getThirdAxisZ(),
				s.getFirstAxisExtent(), s.getSecondAxisExtent(), s.getThirdAxisExtent(),
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ());
	}

	@Override
	public boolean intersects(Plane3D<?> p) {
		return p.intersects(this);
	}

	@Override
	public Point3D getClosestPointTo(Point3D p) {
		return computeClosestPoint(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				p.getX(), p.getY(), p.getZ());
	}

	@Override
	public Point3D getFarthestPointTo(Point3D p) {
		Point3f farthest = new Point3f();
		if (p.getX()<this.minx) {
			farthest.setX(this.maxx);
		}
		else if (p.getX()>this.maxx) {
			farthest.setX(this.minx);
		}
		else {
			double dl = Math.abs(p.getX()-this.minx);
			double du = Math.abs(p.getX()-this.maxx);
			if (dl>du) {
				farthest.setX(this.minx);
			}
			else {
				farthest.setX(this.maxx);
			}
		}
		if (p.getY()<this.miny) {
			farthest.setY(this.maxy);
		}
		else if (p.getY()>this.maxy) {
			farthest.setY(this.miny);
		}
		else {
			double dl = Math.abs(p.getY()-this.miny);
			double du = Math.abs(p.getY()-this.maxy);
			if (dl>du) {
				farthest.setY(this.miny);
			}
			else {
				farthest.setY(this.maxy);
			}
		}
		if (p.getZ()<this.minz) {
			farthest.setZ(this.maxz);
		}
		else if (p.getZ()>this.maxz) {
			farthest.setZ(this.minz);
		}
		else {
			double dl = Math.abs(p.getZ()-this.minz);
			double du = Math.abs(p.getZ()-this.maxz);
			if (dl>du) {
				farthest.setZ(this.minz);
			}
			else {
				farthest.setZ(this.maxz);
			}
		}
		return farthest;
	}

	@Override
	public void set(Shape3F s) {
		s.toBoundingBox(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof AlignedBox3f) {
			AlignedBox3f ab3f = (AlignedBox3f) obj;
			return ((getMinX() == ab3f.getMinX()) &&
					(getMinY() == ab3f.getMinY()) &&
					(getMinZ() == ab3f.getMinZ()) &&
					(getMaxX() == ab3f.getMaxX()) &&
					(getMaxY() == ab3f.getMaxY()) &&
					(getMaxZ() == ab3f.getMaxZ()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + doubleToLongBits(getMinX());
		bits = 31L * bits + doubleToLongBits(getMinY());
		bits = 31L * bits + doubleToLongBits(getMinZ());
		bits = 31L * bits + doubleToLongBits(getMaxX());
		bits = 31L * bits + doubleToLongBits(getMaxY());
		bits = 31L * bits + doubleToLongBits(getMaxZ());
		return (int) (bits ^ (bits >> 32));
	}

	@Override
	public PathIterator3f getPathIterator(Transform3D transform) {
		// TODO Auto-generated method stub
		return null;
	}


}