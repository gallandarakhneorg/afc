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
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AlignedBox3f extends AbstractBoxedShape3f<AlignedBox3f> {

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
		super(min, max);
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
		super(x, y, z, sizex, sizey, sizez);
	}

	/**
	 * @param s
	 */
	public AlignedBox3f(AlignedBox3f s) {
		super(s);
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
		return Triangle3f.intersectsTriangleAlignedBox(
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

}