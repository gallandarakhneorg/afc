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

/** An element of the path.
*
* @author $Author: hjaffali$
* @version $FullVersion$
* @mavengroupid $GroupId$
* @mavenartifactid $ArtifactId$
*/
public abstract class PathElement3f extends AbstractPathElement3F {

	private static final long serialVersionUID = -5701887424817265729L;

	/** Source point.
	 */
	private double fromX;
	
	/** Source point.
	 */
	private double fromY;
	
	/** Source point.
	 */
	private double fromZ;

	/** Target point.
	 */
	private double toX;
	
	/** Target point.
	 */
	private double toY;
	
	/** Target point.
	 */
	private double toZ;

	/** First control point.
	 */
	private double ctrlX1;
	
	/** First control point.
	 */
	private double ctrlY1;
	
	/** First control point.
	 */
	private double ctrlZ1;

	/** Second control point.
	 */
	private double ctrlX2;
	
	/** Second control point.
	 */
	private double ctrlY2;
	
	/** Second control point.
	 */
	private double ctrlZ2;
	
	
	public PathElement3f(PathElementType type1, double fromx, double fromy, double fromz, double ctrlx1, double ctrly1, double ctrlz1, double ctrlx2,
			double ctrly2, double ctrlz2, double tox, double toy, double toz) {
		super(type1, fromx, fromy, fromz, ctrlx1, ctrly1, ctrlz1, ctrlx2, ctrly2, ctrlz2, tox, toy, toz);
	}

	
	public double getFromX() {
		return this.fromX;
	}

	public double getFromY() {
		return this.fromY;
	}

	
	public double getToX() {
		return this.toX;
	}

	
	public double getToY() {
		return this.toY;
	}

	
	public double getCtrlX1() {
		return this.ctrlX1;
	}

	
	public double getCtrlY1() {
		return this.ctrlY1;
	}

	
	public double getCtrlX2() {
		return this.ctrlX2;
	}

	
	public double getCtrlY2() {
		return this.ctrlY2;
	}


	
	public void setFromX(double fromX1) {
		this.fromX = fromX1;
	}

	
	public void setFromY(double fromY1) {
		this.fromY = fromY1;
	}

	
	public void setToX(double toX1) {
		this.toX = toX1;
	}

	
	public void setToY(double toY1) {
		this.toY = toY1;
	}

	
	public void setCtrlX1(double ctrlX11) {
		this.ctrlX1 = ctrlX11;
	}

	
	public void setCtrlY1(double ctrlY11) {
		this.ctrlY1 = ctrlY11;
	}

	
	public void setCtrlX2(double ctrlX21) {
		this.ctrlX2 = ctrlX21;
	}

	
	public void setCtrlY2(double ctrlY21) {
		this.ctrlY2 = ctrlY21;
	
	}


	public double getFromZ() {
		return this.fromZ;
	}


	public void setFromZ(double fromZ1) {
		this.fromZ = fromZ1;
	}


	public double getToZ() {
		return this.toZ;
	}


	public void setToZ(double toZ1) {
		this.toZ = toZ1;
	}


	public double getCtrlZ1() {
		return this.ctrlZ1;
	}


	public void setCtrlZ1(double ctrlZ11) {
		this.ctrlZ1 = ctrlZ11;
	}


	public double getCtrlZ2() {
		return this.ctrlZ2;
	}


	public void setCtrlZ2(double ctrlZ21) {
		this.ctrlZ2 = ctrlZ21;
	}


	
	
}
