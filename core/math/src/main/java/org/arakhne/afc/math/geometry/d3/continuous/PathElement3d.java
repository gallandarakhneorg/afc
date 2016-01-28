/* 
 * $Id$
 * 
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
package org.arakhne.afc.math.geometry.d3.continuous;

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
public abstract class PathElement3d extends AbstractPathElement3D {

	private static final long serialVersionUID = -5701887424817265729L;

	/** Source point.
	 */
	private DoubleProperty fromXProperty = new SimpleDoubleProperty(0f);
	
	/** Source point.
	 */
	private DoubleProperty fromYProperty = new SimpleDoubleProperty(0f);
	
	/** Source point.
	 */
	private DoubleProperty fromZProperty = new SimpleDoubleProperty(0f);

	/** Target point.
	 */
	private DoubleProperty toXProperty = new SimpleDoubleProperty(0f);
	
	/** Target point.
	 */
	private DoubleProperty toYProperty = new SimpleDoubleProperty(0f);
	
	/** Target point.
	 */
	private DoubleProperty toZProperty = new SimpleDoubleProperty(0f);

	/** First control point.
	 */
	private DoubleProperty ctrlX1Property = new SimpleDoubleProperty(0f);
	
	/** First control point.
	 */
	private DoubleProperty ctrlY1Property = new SimpleDoubleProperty(0f);
	
	/** First control point.
	 */
	private DoubleProperty ctrlZ1Property = new SimpleDoubleProperty(0f);

	/** Second control point.
	 */
	private DoubleProperty ctrlX2Property = new SimpleDoubleProperty(0f);
	
	/** Second control point.
	 */
	private DoubleProperty ctrlY2Property = new SimpleDoubleProperty(0f);
	
	/** Second control point.
	 */
	private DoubleProperty ctrlZ2Property = new SimpleDoubleProperty(0f);
	
	
	public PathElement3d(PathElementType type1, double fromx, double fromy, double fromz, double ctrlx1, double ctrly1, double ctrlz1, double ctrlx2,
			double ctrly2, double ctrlz2, double tox, double toy, double toz) {
		super(type1);
		
		this.fromXProperty = new SimpleDoubleProperty(fromx);
		this.fromYProperty = new SimpleDoubleProperty(fromy);
		this.fromZProperty = new SimpleDoubleProperty(fromz);
		this.toXProperty = new SimpleDoubleProperty(tox);
		this.toYProperty = new SimpleDoubleProperty(toy);
		this.toZProperty = new SimpleDoubleProperty(toz);
		this.ctrlX1Property = new SimpleDoubleProperty(ctrlx1);
		this.ctrlY1Property = new SimpleDoubleProperty(ctrly1);
		this.ctrlZ1Property = new SimpleDoubleProperty(ctrlz1);
		this.ctrlX2Property = new SimpleDoubleProperty(ctrlx2);
		this.ctrlY2Property = new SimpleDoubleProperty(ctrly2);
		this.ctrlZ2Property = new SimpleDoubleProperty(ctrlz2);
		
	}


	@Pure
	public double getFromX() {
		return this.fromXProperty.get();
	}

	@Pure
	public double getFromY() {
		return this.fromYProperty.get();
	}


	@Pure
	public double getToX() {
		return this.toXProperty.get();
	}


	@Pure
	public double getToY() {
		return this.toYProperty.get();
	}


	@Pure
	public double getCtrlX1() {
		return this.ctrlX1Property.get();
	}


	@Pure
	public double getCtrlY1() {
		return this.ctrlY1Property.get();
	}


	@Pure
	public double getCtrlX2() {
		return this.ctrlX2Property.get();
	}


	@Pure
	public double getCtrlY2() {
		return this.ctrlY2Property.get();
	}


	
	public void setFromX(double fromX1) {
		this.fromXProperty.set(fromX1);
	}

	
	public void setFromY(double fromY1) {
		this.fromYProperty.set(fromY1);
	}

	
	public void setToX(double toX1) {
		this.toXProperty.set(toX1);
	}

	
	public void setToY(double toY1) {
		this.toYProperty.set(toY1);
	}

	
	public void setCtrlX1(double ctrlX11) {
		this.ctrlX1Property.set(ctrlX11);
	}

	
	public void setCtrlY1(double ctrlY11) {
		this.ctrlY1Property.set(ctrlY11);
	}

	
	public void setCtrlX2(double ctrlX21) {
		this.ctrlX2Property.set(ctrlX21);
	}

	
	public void setCtrlY2(double ctrlY21) {
		this.ctrlY2Property.set(ctrlY21);
	
	}


	@Pure
	public double getFromZ() {
		return this.fromZProperty.get();
	}


	public void setFromZ(double fromZ1) {
		this.fromZProperty.set(fromZ1);
	}


	@Pure
	public double getToZ() {
		return this.toZProperty.get();
	}


	public void setToZ(double toZ1) {
		this.toZProperty.set(toZ1);
	}


	@Pure
	public double getCtrlZ1() {
		return this.ctrlZ1Property.get();
	}


	public void setCtrlZ1(double ctrlZ11) {
		this.ctrlZ1Property.set(ctrlZ11);
	}


	@Pure
	public double getCtrlZ2() {
		return this.ctrlZ2Property.get();
	}


	public void setCtrlZ2(double ctrlZ21) {
		this.ctrlZ2Property.set(ctrlZ21);
	}

}
