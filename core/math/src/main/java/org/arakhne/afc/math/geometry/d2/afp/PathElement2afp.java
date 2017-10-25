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

package org.arakhne.afc.math.geometry.d2.afp;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.PathElement2D;

/** An element of the path.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface PathElement2afp extends PathElement2D {

	/** Replies the x coordinate of the starting point.
	 *
	 * @return the x coordinate, or <code>0</code> if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	double getFromX();

	/** Replies the y coordinate of the starting point.
	 *
	 * @return the y coordinate, or <code>0</code> if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	double getFromY();

	/** Replies the x coordinate of the first control point.
	 *
	 * @return the x coordinate, or <code>0</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	double getCtrlX1();

	/** Replies the y coordinate of the first control point.
	 *
	 * @return the y coordinate, or <code>0</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	double getCtrlY1();

	/** Replies the x coordinate of the second control point.
	 *
	 * @return the x coordinate, or <code>0</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	double getCtrlX2();

	/** Replies the y coordinate of the second  control point.
	 *
	 * @return the y coordinate, or <code>0</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	double getCtrlY2();

	/** Replies the x coordinate of the target point.
	 *
	 * @return the x coordinate.
	 */
	@Pure
	double getToX();

	/** Replies the y coordinate of the target point.
	 *
	 * @return the y coordinate.
	 */
	@Pure
	double getToY();

	@Pure
	@Override
	PathElementType getType();


	/** Copy the coords into the given array, except the source point.
	 *
	 * @param array the output array.
	 */
	@Pure
	void toArray(int[] array);

	/** Copy the coords into the given array, except the source point.
	 *
	 * @param array the output array.
	 */
	@Pure
	void toArray(double[] array);

	/** Replies the x radius of the arc-to ellipse.
	 *
	 * @return the x radius, or <code>0</code> if the type is not {@link PathElementType#ARC_TO}.
	 */
	@Pure
	double getRadiusX();

	/** Replies the y radius of the arc-to ellipse.
	 *
	 * @return the y radius, or <code>0</code> if the type is not {@link PathElementType#ARC_TO}.
	 */
	@Pure
	double getRadiusY();

	/** Replies the rotation of the x axis of the arc-to ellipse.
	 *
	 * @return the x axis rotation, or <code>0</code> if the type is not {@link PathElementType#ARC_TO}.
	 */
	@Pure
	double getRotationX();

	/** Replies if the arc-to will sweep the long way around the ellipse.
	 *
	 * @return <code>true</code> iff the element will sweep clockwise around the ellipse,
	 *     or <code>0</code> if the type is not {@link PathElementType#ARC_TO}.
	 */
	@Pure
	boolean getSweepFlag();

	/** Replies if the arc-to will sweep clockwise around the ellipse.
	 *
	 * @return <code>true</code> iff the element will sweep clockwise around the ellipse,
	 *     or <code>0</code> if the type is not {@link PathElementType#ARC_TO}.
	 */
	@Pure
	boolean getLargeArcFlag();

}
