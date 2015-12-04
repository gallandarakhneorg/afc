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

/** An element of the path.
*
* @author $Author: hjaffali$
* @version $FullVersion$
* @mavengroupid $GroupId$
* @mavenartifactid $ArtifactId$
*/
public abstract class PathElement2f extends AbstractPathElement2F {

	private static final long serialVersionUID = -5701887424817265729L;

	/** Source point.
	 */
	private double fromX;
	
	/** Source point.
	 */
	private double fromY;

	/** Target point.
	 */
	private double toX;
	
	/** Target point.
	 */
	private double toY;

	/** First control point.
	 */
	private double ctrlX1;
	
	/** First control point.
	 */
	private double ctrlY1;

	/** Second control point.
	 */
	private double ctrlX2;
	
	/** Second control point.
	 */
	private double ctrlY2;
	
	
	public PathElement2f(PathElementType type1, double fromx, double fromy, double ctrlx1, double ctrly1, double ctrlx2,
			double ctrly2, double tox, double toy) {
		super(type1, fromx, fromy, ctrlx1, ctrly1, ctrlx2, ctrly2, tox, toy);
	}

	@Pure
	public double getFromX() {
		return this.fromX;
	}

	public double getFromY() {
		return this.fromY;
	}

	@Pure
	public double getToX() {
		return this.toX;
	}

	@Pure
	public double getToY() {
		return this.toY;
	}

	@Pure
	public double getCtrlX1() {
		return this.ctrlX1;
	}

	@Pure
	public double getCtrlY1() {
		return this.ctrlY1;
	}

	@Pure
	public double getCtrlX2() {
		return this.ctrlX2;
	}

	@Pure
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
	
}
