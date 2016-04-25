/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2013 Stephane GALLAND.
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

package org.arakhne.afc.ui;

import org.arakhne.afc.math.matrix.Transform2D;

/** This feature describes all the parameters that must
 * be used to center logical points on the screen view. 
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class CenteringTransform {

	/** Is the translation to add to the logical coordinate.
	 */
	private float translationX = 0f;

	/** Is the scaling to apply to the logical coordinate.
	 */
	private float scaleX = 1f;
	
	/** Indicates if the X axis is flipped.
	 */
	private boolean flipX;

	/** Is the translation to add to the logical coordinate.
	 */
	private float translationY = 0f;

	/** Is the scaling to apply to the logical coordinate.
	 */
	private float scaleY = 1f;

	/** Indicates if the Y axis is flipped.
	 */
	private boolean flipY;
	
	/**
	 */
	public CenteringTransform() {
		//
	}
	
	/** Set the centering parameters for the x coordinates.
	 * 
	 * @param flip indicates if the X axis is flipped.
	 * @param scale is the scaling to apply to the coordinate.
	 * @param translation is the translation to apply to the coordinate.
	 */
	public void setCenteringX(boolean flip, float scale, float translation) {
		this.flipX = flip;
		this.scaleX = scale;
		this.translationX = translation;
	}

	/** Set the centering parameters for the y coordinates.
	 * 
	 * @param flip indicates if the Y axis is flipped.
	 * @param scale is the scaling to apply to the coordinate.
	 * @param translation is the translation to apply to the coordinate.
	 */
	public void setCenteringY(boolean flip, float scale, float translation) {
		this.flipY = flip;
		this.scaleY = scale;
		this.translationY = translation;
	}
	
	/** Replies if the X axis is flipped.
	 * 
	 * @return <code>true</code> if the logical X axis is flipped than the screen coordinate system.
	 */
	public boolean isXAxisFlipped() {
		return this.flipX;
	}

	/** Replies if the Y axis is flipped.
	 * 
	 * @return <code>true</code> if the logical Y axis is flipped than the screen coordinate system.
	 */
	public boolean isYAxisFlipped() {
		return this.flipY;
	}

	/** Change the coordinate system of <var>x</var> from the
	 * global coordinate system to the "centered" coordinate system.
	 * 
	 * @param x
	 * @return the x coordinate in the "centered" coordinate system.
	 */
	public float toCenterX(float x) {
		return (this.scaleX * x + this.translationX);
	}
	
	/** Change the coordinate system of <var>y</var> from the
	 * global coordinate system to the "centered" coordinate system.
	 * 
	 * @param y
	 * @return the x coordinate in the "centered" coordinate system.
	 */
	public float toCenterY(float y) {
		return (this.scaleY * y + this.translationY);
	}

	/** Change the coordinate system of <var>x</var> from the
	 * "centered" coordinate system to the global coordinate system.
	 * 
	 * @param x
	 * @return the x coordinate in the global coordinate system.
	 */
	public float toGlobalX(float x) {
		return (x - this.translationX) / this.scaleX;
	}
	
	/** Change the coordinate system of <var>y</var> from the
	 * "centered" coordinate system to the global coordinate system.
	 * 
	 * @param y
	 * @return the x coordinate in the global coordinate system.
	 */
	public float toGlobalY(float y) {
		return (y - this.translationY) / this.scaleY;
	}

	/** Replies the transformation matrix which is corresponding to this
	 * centering transformation.
	 * 
	 * @return the transformation matrix.
	 */
	public Transform2D getMatrix() {
		return new Transform2D(
				this.scaleX, 0, this.translationX,
				0, this.scaleY, this.translationY);
	}	

	/** Replies the transformation matrix which is corresponding to this
	 * centering transformation and multiply by the given factor..
	 * 
	 * @param factor
	 * @return the transformation matrix.
	 */
	public Transform2D getMatrix(float factor) {
		return new Transform2D(
				this.scaleX*factor, 0, this.translationX*factor,
				0, this.scaleY*factor, this.translationY*factor);
	}
	
	/** Replies the X scaling factor in the transformation matrix of this object.
	 * 
	 * @return the m00 of the transformation matrix replied by {@link #getMatrix()}
	 */
	public float getScaleX() {
		return this.scaleX;
	}

	/** Replies the Y scaling factor in the transformation matrix of this object.
	 * 
	 * @return the m11 of the transformation matrix replied by {@link #getMatrix()}
	 */
	public float getScaleY() {
		return this.scaleY;
	}

	/** Replies the X translation factor in the transformation matrix of this object.
	 * 
	 * @return the m02 of the transformation matrix replied by {@link #getMatrix()}
	 */
	public float getTranslationX() {
		return this.translationX;
	}

	/** Replies the Y translation factor in the transformation matrix of this object.
	 * 
	 * @return the m12 of the transformation matrix replied by {@link #getMatrix()}
	 */
	public float getTranslationY() {
		return this.translationY;
	}

}