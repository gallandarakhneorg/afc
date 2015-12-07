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
package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.d2.FunctionalPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.eclipse.xtext.xbase.lib.Pure;



/** Abstract implementation of 2D rectangular shapes.
 * 
 * @param <T> is the type of the shape implemented by the instance of this class.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractRectangularShape2F<T extends AbstractRectangularShape2F<T>> extends AbstractShape2F<T> {

	private static final long serialVersionUID = -2330319571109966087L;
	
	
	/**
	 */
	public AbstractRectangularShape2F() {
		//
	}
	
	/**
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	public AbstractRectangularShape2F(FunctionalPoint2D min, FunctionalPoint2D max) {
		setInitiallyFromCorners(min.getX(), min.getY(), max.getX(), max.getY());
	}
	
	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public AbstractRectangularShape2F(double x, double y, double width, double height) {
		setInitiallyFromCorners(x, y, x+width, y+height);
	}

	/**
	 * @param s
	 */
	public AbstractRectangularShape2F(AbstractRectangularShape2F<?> s) {
		setInitiallyFromCorners( s.getMinX(),s.getMinY(),s.getMaxX(),s.getMaxY());
	}

	/** {@inheritDoc}
	 * @return 
	 */
	@Pure
	@Override
	public Rectangle2f toBoundingBox() {
		return new Rectangle2f(this.getMinX(), this.getMinY(), this.getMaxX()-this.getMinX(), this.getMaxY()-this.getMinY());
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void toBoundingBox(Rectangle2f box) {
		box.setFromCorners(this.getMinX(), this.getMinY(), this.getMaxX(), this.getMaxY());
	}


	@Override
	abstract public void clear();
	
	/** Change the frame of the rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	abstract public void set(double x, double y, double width, double height);
	
	/** Change the frame of the rectangle.
	 * 
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	abstract public void set(Point2f min, Point2f max);
	
	/** Change the width of the rectangle, not the min corner.
	 * 
	 * @param width
	 */
	abstract public void setWidth(double width);

	/** Change the height of the rectangle, not the min corner.
	 * 
	 * @param height
	 */
	abstract public void setHeight(double height);
	
	
	/** Initialize the frame of the rectangle.
	 * 
	 * @param p1 is the coordinate of the first corner.
	 * @param p2 is the coordinate of the second corner.
	 */
	abstract public void setInitiallyFromCorners(Point2D p1, Point2D p2);
	
	/** Initialize the frame of the rectangle.
	 * 
	 * @param x1 is the coordinate of the first corner.
	 * @param y1 is the coordinate of the first corner.
	 * @param x2 is the coordinate of the second corner.
	 * @param y2 is the coordinate of the second corner.
	 */
	abstract public void setInitiallyFromCorners(double x1, double y1, double x2, double y2);
	
	
	/** Change the frame of the rectangle.
	 * 
	 * @param p1 is the coordinate of the first corner.
	 * @param p2 is the coordinate of the second corner.
	 */
	abstract public void setFromCorners(Point2D p1, Point2D p2);

	/** Change the frame of the rectangle.
	 * 
	 * @param x1 is the coordinate of the first corner.
	 * @param y1 is the coordinate of the first corner.
	 * @param x2 is the coordinate of the second corner.
	 * @param y2 is the coordinate of the second corner.
	 */
	abstract public void setFromCorners(double x1, double y1, double x2, double y2);
	
	/**
     * Sets the framing rectangle of this <code>Shape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectangle is used by the subclasses of
     * <code>RectangularShape</code> to define their geometry.
     *
     * @param centerX the X coordinate of the specified center point
     * @param centerY the Y coordinate of the specified center point
     * @param cornerX the X coordinate of the specified corner point
     * @param cornerY the Y coordinate of the specified corner point
     */
	abstract public void setFromCenter(double centerX, double centerY, double cornerX, double cornerY);
	
	/** Replies the min X.
	 * 
	 * @return the min x.
	 */
	@Pure
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
	@Pure
	abstract public double getCenterX();

	/** Replies the max x.
	 * 
	 * @return the max x.
	 */
	@Pure
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
	@Pure
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
	@Pure
	abstract public double getCenterY();

	/** Replies the max y.
	 * 
	 * @return the max y.
	 */
	@Pure
	abstract public double getMaxY();
	
	/** Set the max Y.
	 * 
	 * @param y the max y.
	 */
	abstract public void setMaxY(double y);

	/** Replies the width.
	 * 
	 * @return the width.
	 */
	@Pure
	abstract public double getWidth();

	/** Replies the height.
	 * 
	 * @return the height.
	 */
	@Pure
	abstract public double getHeight();
	
	@Override
	public void translate(double dx, double dy) {
		this.setMinX(this.getMinX()+dx);
		this.setMinY(this.getMinY()+dy);
		this.setMaxX(this.getMaxX()+dx);
		this.setMaxY(this.getMaxY()+dy); 
	}

	/** Replies if this rectangular shape is empty.
	 * The rectangular shape is empty when the
	 * two corners are at the same location.
	 * 
	 * @return <code>true</code> if the rectangular shape is empty;
	 * otherwise <code>false</code>.
	 */
	@Pure
	@Override
	public boolean isEmpty() {
		return this.getMinX()==this.getMaxX() && this.getMinY()==this.getMaxY(); 
	}
	
	/** Inflate this rectangle with the given amounts.
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void inflate(double left, double top, double right, double bottom) {
		this.setMinX(this.getMinX()+left);
		this.setMinY(this.getMinY()+top);
		this.setMaxX(this.getMaxX()+right);
		this.setMaxY(this.getMaxY()+bottom);
	}

}