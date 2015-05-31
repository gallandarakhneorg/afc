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

import org.arakhne.afc.math.geometry.d3.Point3D;



/** Abstract implementation of 3D box.
 * 
 * @param <T> is the type of the shape implemented by the instance of this class.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractBoxedShape3f<T extends AbstractBoxedShape3f<T>>
extends AbstractShape3f<T> {

	private static final long serialVersionUID = -1771954485508877728L;

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
	public AbstractBoxedShape3f() {
		//
	}
	
	/**
	 * @param min is the min corner of the box.
	 * @param max is the max corner of the box.
	 */
	public AbstractBoxedShape3f(Point3f min, Point3f max) {
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
	public AbstractBoxedShape3f(double x, double y, double z, double sizex, double sizey, double sizez) {
		setFromCorners(x, y, z, x+sizex, y+sizey, z+sizez);
	}
	
	/**
	 * @param s
	 */
	public AbstractBoxedShape3f(AbstractBoxedShape3f<?> s) {
		this.minx = s.minx;
		this.miny = s.miny;
		this.minz = s.minz;
		this.maxx = s.maxx;
		this.maxy = s.maxy;
		this.maxz = s.maxz;
	}

	/** {@inheritDoc}
	 */
	@Override
	public AlignedBox3f toBoundingBox() {
		return new AlignedBox3f(
				this.minx, this.miny, this.minz,
				this.maxx-this.minx, this.maxy-this.miny, this.maxz-this.minz);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void toBoundingBox(AlignedBox3f box) {
		box.setFromCorners(
				this.minx, this.miny, this.minz,
				this.maxx, this.maxy, this.maxz);
	}


	@Override
	public void clear() {
		this.minx = this.miny = this.minz = 0;
		this.maxx = this.maxy = this.maxz = 0;
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
	public void set(double x, double y, double z, double sizex, double sizey, double sizez) {
		setFromCorners(x, y, z, x+sizex, y+sizey, z+sizez);
	}
	
	/** Change the frame of te box.
	 * 
	 * @param min is the min corner of the box.
	 * @param max is the max corner of the box.
	 */
	public void set(Point3f min, Point3f max) {
		setFromCorners(
				min.getX(), min.getY(), min.getZ(), 
				max.getX(), max.getY(), max.getZ());
	}
	
	/** Change the X-size of the box, not the min corner.
	 * 
	 * @param size
	 */
	public void setSizeX(double size) {
		this.maxx = this.minx + Math.max(0f, size);
	}

	/** Change the Y-size of the box, not the min corner.
	 * 
	 * @param size
	 */
	public void setSizeY(double size) {
		this.maxy = this.miny + Math.max(0f, size);
	}

	/** Change the Z-size of the box, not the min corner.
	 * 
	 * @param size
	 */
	public void setSizeZ(double size) {
		this.maxz = this.minz + Math.max(0f, size);
	}

	/** Change the frame of the box.
	 * 
	 * @param p1 is the coordinate of the first corner.
	 * @param p2 is the coordinate of the second corner.
	 */
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
	public Point3f getMin() {
		return new Point3f(this.minx, this.miny, this.minz);
	}

	/** Replies the min point.
	 * 
	 * @return the min point.
	 */
	public Point3f getMax() {
		return new Point3f(this.maxx, this.maxy, this.maxz);
	}

	/** Replies the center point.
	 * 
	 * @return the center point.
	 */
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
	public double getMinX() {
		return this.minx;
	}

	/** Set the min X.
	 * 
	 * @param x the min x.
	 */
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
	public double getCenterX() {
		return (this.minx + this.maxx) / 2f;
	}

	/** Replies the max x.
	 * 
	 * @return the max x.
	 */
	public double getMaxX() {
		return this.maxx;
	}

	/** Set the max X.
	 * 
	 * @param x the max x.
	 */
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
	public double getMinY() {
		return this.miny;
	}

	/** Set the min Y.
	 * 
	 * @param y the min y.
	 */
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
	public double getCenterY() {
		return (this.miny + this.maxy) / 2f;
	}

	/** Replies the max y.
	 * 
	 * @return the max y.
	 */
	public double getMaxY() {
		return this.maxy;
	}
	
	/** Set the max Y.
	 * 
	 * @param y the max y.
	 */
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
	public double getMinZ() {
		return this.minz;
	}

	/** Set the min Z.
	 * 
	 * @param z the min z.
	 */
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
	public double getCenterZ() {
		return (this.minz + this.maxz) / 2f;
	}

	/** Replies the max z.
	 * 
	 * @return the max z.
	 */
	public double getMaxZ() {
		return this.maxz;
	}
	
	/** Set the max Z.
	 * 
	 * @param z the max z.
	 */
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
	public double getSizeX() {
		return this.maxx - this.minx;
	}

	/** Replies the y-size.
	 * 
	 * @return the y-size.
	 */
	public double getSizeY() {
		return this.maxy - this.miny;
	}
	
	/** Replies the z-size.
	 * 
	 * @return the z-size.
	 */
	public double getSizeZ() {
		return this.maxz - this.minz;
	}

	@Override
	public void translate(double dx, double dy, double dz) {
		this.minx += dx;
		this.miny += dy;
		this.minz += dz;
		this.maxx += dx;
		this.maxy += dy;
		this.maxz += dz;
	}

	@Override
	public void transform(Transform3D transformationMatrix) {
		// TODO Auto-generated method stub
		
	}

	/** Replies if this rectangular shape is empty.
	 * The rectangular shape is empty when the
	 * two corners are at the same location.
	 * 
	 * @return <code>true</code> if the rectangular shape is empty;
	 * otherwise <code>false</code>.
	 */
	@Override
	public boolean isEmpty() {
		return this.minx==this.maxx && this.miny==this.maxy && this.minz==this.maxz; 
	}
	
	/** Inflate this box with the given amounts.
	 * 
	 * @param minx
	 * @param miny
	 * @param minz
	 * @param maxx
	 * @param maxy
	 * @param maxz
	 */
	public void inflate(double minx, double miny, double minz, double maxx, double maxy, double maxz) {
		this.minx -= minx;
		this.miny -= miny;
		this.minz -= minz;
		this.maxx += maxx;
		this.maxy += maxy;
		this.maxz += maxz;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append(getClass().getSimpleName());
		sb.append("["); //$NON-NLS-1$
		sb.append(this.minx);
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.miny);
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.minz);
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.maxx);
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.maxy);
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.maxz);
		sb.append(']');
		return sb.toString();
	}

	/** Set the x bounds of the box.
	 * 
	 * @param min the min value for the x axis.
	 * @param max the max value for the x axis.
	 */
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
	public void setZ(double min, double max) {
		if (min <= max) {
			this.minz = min;
			this.maxz = max;
		} else {
			this.minz = max;
			this.maxz = min;
		}
	}

}