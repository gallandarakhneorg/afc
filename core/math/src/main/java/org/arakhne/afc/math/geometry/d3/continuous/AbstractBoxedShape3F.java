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
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractBoxedShape3F<T extends AbstractBoxedShape3F<T>>   extends AbstractShape3F<T> {

	private static final long serialVersionUID = -1771954485508877728L;


	/** {@inheritDoc}
	 */
	@Override
	public AbstractBoxedShape3F<?> toBoundingBox() {
		return new AlignedBox3f(
				this.getMinX(), this.getMinY(), this.getMinZ(),
				this.getMaxX()-this.getMinX(), this.getMaxY()-this.getMinY(), this.getMaxZ()-this.getMinZ());
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void toBoundingBox(AbstractBoxedShape3F<?> box) {
		box.setFromCorners(
				this.getMinX(), this.getMinY(), this.getMinZ(),
				this.getMaxX(), this.getMaxY(), this.getMaxZ());
	}


	@Override
	public void clear() {
		this.setMaxX(0f);
		this.setMaxY(0f);
		this.setMaxZ(0f);
		this.setMinX(0f);
		this.setMinY(0f);
		this.setMinZ(0f);
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
	abstract public void set(double x, double y, double z, double sizex, double sizey, double sizez);
	
	/** Change the frame of te box.
	 * 
	 * @param min is the min corner of the box.
	 * @param max is the max corner of the box.
	 */
	abstract public void set(Point3f min, Point3f max);
	
	/** Change the X-size of the box, not the min corner.
	 * 
	 * @param size
	 */
	abstract public void setSizeX(double size);

	/** Change the Y-size of the box, not the min corner.
	 * 
	 * @param size
	 */
	abstract public void setSizeY(double size);

	/** Change the Z-size of the box, not the min corner.
	 * 
	 * @param size
	 */
	abstract public void setSizeZ(double size);

	/** Change the frame of the box.
	 * 
	 * @param p1 is the coordinate of the first corner.
	 * @param p2 is the coordinate of the second corner.
	 */
	abstract public void setFromCorners(Point3D p1, Point3D p2);

	/** Change the frame of the box.
	 * 
	 * @param x1 is the coordinate of the first corner.
	 * @param y1 is the coordinate of the first corner.
	 * @param z1 is the coordinate of the first corner.
	 * @param x2 is the coordinate of the second corner.
	 * @param y2 is the coordinate of the second corner.
	 * @param z2 is the coordinate of the second corner.
	 */
	abstract public void setFromCorners(double x1, double y1, double z1, double x2, double y2, double z2);
	
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
	abstract public void setFromCenter(double centerX, double centerY, double centerZ, double cornerX, double cornerY, double cornerZ);
	
	/** Replies the min point.
	 * 
	 * @return the min point.
	 */
	abstract public Point3f getMin();

	/** Replies the min point.
	 * 
	 * @return the min point.
	 */
	abstract public Point3f getMax();

	/** Replies the center point.
	 * 
	 * @return the center point.
	 */
	abstract public Point3f getCenter();

	/** Replies the min X.
	 * 
	 * @return the min x.
	 */
	abstract public double getMinX();

	/** Set the min X.
	 * 
	 * @param x the min x.
	 */
	abstract public void setMinX(double x);

	/** Replies the center x.
	 * 
	 * @return the center x.
	 */
	abstract public double getCenterX();

	/** Replies the max x.
	 * 
	 * @return the max x.
	 */
	abstract public double getMaxX();

	/** Set the max X.
	 * 
	 * @param x the max x.
	 */
	abstract public void setMaxX(double x);

	/** Replies the min y.
	 * 
	 * @return the min y.
	 */
	abstract public double getMinY();

	/** Set the min Y.
	 * 
	 * @param y the min y.
	 */
	abstract public void setMinY(double y);

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	abstract public double getCenterY();

	/** Replies the max y.
	 * 
	 * @return the max y.
	 */
	abstract public double getMaxY();
	
	/** Set the max Y.
	 * 
	 * @param y the max y.
	 */
	abstract public void setMaxY(double y);

	/** Replies the min z.
	 * 
	 * @return the min z.
	 */
	abstract public double getMinZ();

	/** Set the min Z.
	 * 
	 * @param z the min z.
	 */
	abstract public void setMinZ(double z);

	/** Replies the center z.
	 * 
	 * @return the center z.
	 */
	abstract public double getCenterZ();

	/** Replies the max z.
	 * 
	 * @return the max z.
	 */
	abstract public double getMaxZ();
	
	/** Set the max Z.
	 * 
	 * @param z the max z.
	 */
	abstract public void setMaxZ(double z);

	/** Replies the x-size.
	 * 
	 * @return the x-size.
	 */
	abstract public double getSizeX();

	/** Replies the y-size.
	 * 
	 * @return the y-size.
	 */
	abstract public double getSizeY();
	
	/** Replies the z-size.
	 * 
	 * @return the z-size.
	 */
	abstract public double getSizeZ();

	@Override
	public void translate(double dx, double dy, double dz) {
		this.setFromCorners(this.getMinX()+ dx,this.getMinY()+ dy,this.getMinZ()+ dz,this.getMaxX()+ dx,this.getMaxY()+ dy,this.getMaxZ()+ dz);
	}

	@Override
	public void transform(Transform3D transformationMatrix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Shape3F createTransformedShape(Transform3D transformationMatrix) {
		AbstractBoxedShape3F<T> newB = this.clone();
		newB.transform(transformationMatrix);
		return newB;
		
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
		return this.getMinX()==this.getMaxX() && this.getMinY()==this.getMaxY() && this.getMinZ()==this.getMaxZ(); 
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
		this.setFromCorners(this.getMinX()+ minx,this.getMinY()+ miny,this.getMinZ()+ minz,this.getMaxX()+ maxx,this.getMaxY()+ maxy,this.getMaxZ()+ maxz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append(getClass().getSimpleName());
		sb.append("["); //$NON-NLS-1$
		sb.append(this.getMinX());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.getMinY());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.getMinZ());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.getMaxX());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.getMaxY());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.getMaxZ());
		sb.append(']');
		return sb.toString();
	}

	/** Set the x bounds of the box.
	 * 
	 * @param min the min value for the x axis.
	 * @param max the max value for the x axis.
	 */
	abstract public void setX(double min, double max);


	/** Set the y bounds of the box.
	 * 
	 * @param min the min value for the y axis.
	 * @param max the max value for the y axis.
	 */
	abstract public void setY(double min, double max);


	/** Set the z bounds of the box.
	 * 
	 * @param min the min value for the z axis.
	 * @param max the max value for the z axis.
	 */
	abstract public void setZ(double min, double max);

}