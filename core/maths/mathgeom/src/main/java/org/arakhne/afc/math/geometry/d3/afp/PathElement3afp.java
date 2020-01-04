/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.afp;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.PathElement3D;

/** An element of the path.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface PathElement3afp extends PathElement3D {

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

	/** Replies the z coordinate of the starting point.
	 *
	 * @return the z coordinate, or <code>0</code> if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	double getFromZ();

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

	/** Replies the z coordinate of the first control point.
	 *
	 * @return the z coordinate, or <code>0</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	double getCtrlZ1();

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

	/** Replies the z coordinate of the second  control point.
	 *
	 * @return the z coordinate, or <code>0</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	double getCtrlZ2();

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

	/** Replies the z coordinate of the target point.
	 *
	 * @return the z coordinate.
	 */
	@Pure
	double getToZ();

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

}
