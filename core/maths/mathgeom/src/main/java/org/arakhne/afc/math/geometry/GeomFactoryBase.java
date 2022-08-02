/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry;

import org.eclipse.xtext.xbase.lib.Pure;

/** Factory of geometrical primitives.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface GeomFactoryBase {

	/** The maximum distance that the line segments used to approximate the
	 *  curved segments are allowed to deviate from any point on the
	 *  original curve.
	 *
	 * <p>The replied value is one of:<ul>
	 * <li>the ratio specifically defined within this factory, by a call to
	 * {@link #setSplineApproximationRatio(Double)}.</li>
	 * <li>the global ratio (shared among all the factories) replied by
	 * {@link AbstractGeomFactoryBase#getGlobalSplineApproximationRatio()} and
	 * defined by a call to {@link AbstractGeomFactoryBase#setGlobalSplineApproximationRatio(Double)}.
	 * <li>the value defined by {@link GeomConstants#SPLINE_APPROXIMATION_RATIO}.<li>
	 * </ul>.
	 *
	 * @return the maximal distance for approximating curves with segments.
	 * @since 14.0
	 */
	@Pure
	double getSplineApproximationRatio();

	/** Change the maximum distance that the line segments used to approximate the
	 *  curved segments are allowed to deviate from any point on the
	 *  original curve.
	 *
	 * <p>This function changes the ratio defined within the current factory.
	 * The other factories and the global value are not changed.
	 * For changing the global value, you must call
	 * {@link AbstractGeomFactoryBase#setGlobalSplineApproximationRatio(Double)}.
	 *
	 * @param distance the maximal distance for approximating curves with segments.
	 *     If the argument value is {@code null} or NaN, the global definition of
	 *     the ratio is used by this factory.
	 * @since 14.0
	 */
	void setSplineApproximationRatio(Double distance);

}
