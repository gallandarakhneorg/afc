/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.gis.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/** This feature describes all the parameters that must
 * be used to center logical points on the screen view.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class CenteringTransform {

	private static final String TRANSLATION_X_PROPERTY = "translationX"; //$NON-NLS-1$

	private static final String TRANSLATION_Y_PROPERTY = "translationY"; //$NON-NLS-1$

	/** Is the translation to add to the logical coordinate.
	 */
	private final DoubleProperty translationX;

	/** Indicates if the X axis is flipped.
	 */
	private final BooleanProperty flipX;

	/** Is the translation to add to the logical coordinate.
	 */
	private final DoubleProperty translationY;

	/** Indicates if the Y axis is flipped.
	 */
	private final BooleanProperty flipY;

	/** Constructor.
	 *
	 * @param invertedAxisX indicates if the x axis of the GIS is inverted regarding the JavaFX x axis.
	 * @param invertedAxisY indicates if the y axis of the GIS is inverted regarding the JavaFX y axis.
	 * @param visibleArea the bounds of the viewport in GIS coordinates.
	 */
	public CenteringTransform(
			BooleanProperty invertedAxisX,
			BooleanProperty invertedAxisY,
			ReadOnlyObjectProperty<Rectangle2d> visibleArea) {
		this.flipX = invertedAxisX;
		this.flipY = invertedAxisY;

		this.translationX = new SimpleDoubleProperty(this, TRANSLATION_X_PROPERTY);
		this.translationX.bind(Bindings.createDoubleBinding(
			() -> {
				final double center = visibleArea.get().getCenterX();
				return this.flipX.get() ? center : -center;
			}, visibleArea, this.flipX));
		this.translationY = new SimpleDoubleProperty(this, TRANSLATION_Y_PROPERTY);
		this.translationY.bind(Bindings.createDoubleBinding(
			() -> {
				final double center = visibleArea.get().getCenterY();
				return this.flipY.get() ? center : -center;
			}, visibleArea));
	}

	/** Replies if the x axis of the displayed data is inverted regarding to the
	 * standard JavaFX coordinate system.
	 *
	 * @return the x coordinate of the viewport center in GIS coordinates.
	 */
	@Pure
	public boolean isInvertedAxisX() {
		return this.flipX.get();
	}

	/** Replies if the Y axis of the displayed data is inverted regarding to the
	 * standard JavaFX coordinate system.
	 *
	 * @return the Y coordinate of the viewport center in GIS coordinates.
	 */
	@Pure
	public boolean isInvertedAxisY() {
		return this.flipY.get();
	}

	/** Change the coordinate system of {@code x} from the
	 * global gis coordinate system to the "centered" graphical coordinate system.
	 *
	 * @param x the x gis coordinate to convert.
	 * @return the x coordinate in the "centered" graphical coordinate system.
	 */
	@Pure
	public double toCenterX(double x) {
		final double adjustedX = this.flipX.get() ? -x : x;
		return adjustedX + this.translationX.get();
	}

	/** Change the coordinate system of {@code y} from the
	 * global gis coordinate system to the "centered" graphical coordinate system.
	 *
	 * @param y the y gis coordinate to convert.
	 * @return the x coordinate in the "centered" graphical coordinate system.
	 */
	@Pure
	public double toCenterY(double y) {
		final double adjustedY = this.flipY.get() ? -y : y;
		return adjustedY + this.translationY.get();
	}

	/** Change the coordinate system of {@code x} from the
	 * "centered" graphical coordinate system to the global gis coordinate system.
	 *
	 * @param x the x graphical coordinate to convert.
	 * @return the x coordinate in the global gis coordinate system.
	 */
	@Pure
	public double toGlobalX(double x) {
		final double adjustedX = x - this.translationX.get();
		return this.flipX.get() ? adjustedX : -adjustedX;
	}

	/** Change the coordinate system of {@code y} from the
	 * "centered" graphical coordinate system to the global gis coordinate system.
	 *
	 * @param y the y graphical coordinate to convert.
	 * @return the x coordinate in the global coordinate system.
	 */
	@Pure
	public double toGlobalY(double y) {
		final double adjustedY = y - this.translationY.get();
		return this.flipY.get() ? adjustedY : -adjustedY;
	}

}
