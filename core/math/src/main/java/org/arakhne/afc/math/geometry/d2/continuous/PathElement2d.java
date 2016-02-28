/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
 * Copyright (C) 2015 Hamza JAFFALI.
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

import org.arakhne.afc.math.geometry.PathElementType;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/** An element of the path.
 *
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class PathElement2d extends AbstractPathElement2D {

	private static final long serialVersionUID = 5550697627846953346L;

	/** Source point.
	 */
	private DoubleProperty fromXProperty;

	/** Source point.
	 */
	private DoubleProperty fromYProperty;

	/** Target point.
	 */
	private DoubleProperty toXProperty;

	/** Target point.
	 */
	private DoubleProperty toYProperty;

	/** First control point.
	 */
	private DoubleProperty ctrlX1Property;

	/** First control point.
	 */
	private DoubleProperty ctrlY1Property;

	/** Second control point.
	 */
	private DoubleProperty ctrlX2Property;

	/** Second control point.
	 */
	private DoubleProperty ctrlY2Property;


	public PathElement2d(PathElementType type1, double fromX, double fromy, double ctrlx1, double ctrly1, double ctrlx2,
			double ctrly2, double tox, double toy) {

		super(type1);

		this.fromXProperty = new SimpleDoubleProperty(fromX);
		this.fromYProperty = new SimpleDoubleProperty(fromy);
		this.toXProperty = new SimpleDoubleProperty(tox);
		this.toYProperty = new SimpleDoubleProperty(toy);
		this.ctrlX1Property = new SimpleDoubleProperty(ctrlx1);
		this.ctrlY1Property = new SimpleDoubleProperty(ctrly1);
		this.ctrlX2Property = new SimpleDoubleProperty(ctrlx2);
		this.ctrlY2Property = new SimpleDoubleProperty(ctrly2);

	}

	public PathElement2d(PathElementType type1, Point2d from, Point2d ctrl1, Point2d ctrl2, Point2d to) {

		super(type1);

		this.fromXProperty = new SimpleDoubleProperty(Double.NaN);
		this.fromYProperty = new SimpleDoubleProperty(Double.NaN);
		this.toXProperty = new SimpleDoubleProperty(Double.NaN);
		this.toYProperty = new SimpleDoubleProperty(Double.NaN);
		this.ctrlX1Property = new SimpleDoubleProperty(Double.NaN);
		this.ctrlY1Property = new SimpleDoubleProperty(Double.NaN);
		this.ctrlX2Property = new SimpleDoubleProperty(Double.NaN);
		this.ctrlY2Property = new SimpleDoubleProperty(Double.NaN);

		if(from!=null) {
			this.fromXProperty = from.xProperty;
			this.fromYProperty = from.yProperty;
		}
		if(to!=null) {
			this.toXProperty = to.xProperty;
			this.toYProperty = to.yProperty;
		}
		if(ctrl1!=null) {
			this.ctrlX1Property = ctrl1.xProperty;
			this.ctrlY1Property = ctrl1.yProperty;
		}
		if(ctrl2!=null) {
			this.ctrlX2Property = ctrl2.xProperty;
			this.ctrlY2Property = ctrl2.yProperty;
		}

	}

	@Pure
	public double getFromX() {
		return this.fromXProperty.doubleValue();
	}

	@Pure
	public double getFromY() {
		return this.fromYProperty.doubleValue();
	}

	@Pure
	public double getToX() {
		return this.toXProperty.doubleValue();
	}

	@Pure
	public double getToY() {
		return this.toYProperty.doubleValue();
	}

	@Pure
	public double getCtrlX1() {
		return this.ctrlX1Property.doubleValue();
	}

	@Pure
	public double getCtrlY1() {
		return this.ctrlY1Property.doubleValue();
	}

	@Pure
	public double getCtrlX2() {
		return this.ctrlX2Property.doubleValue();
	}

	@Pure
	public double getCtrlY2() {
		return this.ctrlY2Property.doubleValue();
	}


	public void setFromX(double fromX1) {
		this.fromXProperty.set(fromX1);
	}


	public void setFromY(double fromY1) {
		this.fromYProperty.set(fromY1);
	}

	public void setFrom(Point2d from) {
		if(from!=null) {
			this.fromXProperty = from.xProperty;
			this.fromYProperty = from.yProperty;
		}
	}

	public void setToX(double toX1) {
		this.toXProperty.set(toX1);
	}


	public void setToY(double toY1) {
		this.toYProperty.set(toY1);
	}

	public void setTo(Point2d to) {
		if(to!=null) {
			this.toXProperty = to.xProperty;
			this.toYProperty = to.yProperty;
		}
	}

	public void setCtrlX1(double ctrlX11) {
		this.ctrlX1Property.set(ctrlX11);
	}


	public void setCtrlY1(double ctrlY11) {
		this.ctrlY1Property.set(ctrlY11);
	}

	public void setCtrl1(Point2d ctrl1) {
		if(ctrl1!=null) {
			this.ctrlX1Property = ctrl1.xProperty;
			this.ctrlY1Property = ctrl1.yProperty;
		}
	}

	public void setCtrlX2(double ctrlX21) {
		this.ctrlX2Property.set(ctrlX21);
	}


	public void setCtrlY2(double ctrlY21) {
		this.ctrlY2Property.set(ctrlY21);

	}

	public void setCtrl2(Point2d ctrl2) {
		if(ctrl2!=null) {
			this.ctrlX2Property = ctrl2.xProperty;
			this.ctrlY2Property = ctrl2.yProperty;
		}
	}

}