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

package org.arakhne.afc.math.geometry;

import org.eclipse.xtext.xbase.lib.Pure;

/** Abstract implementation of a factory of geometric primitives.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class AbstractGeomFactoryBase implements GeomFactoryBase {

	private static double globalSplineApproximation = GeomConstants.SPLINE_APPROXIMATION_RATIO;

	private Double splineApproximation;

	/** The maximum distance that the line segments used to approximate the
	 *  curved segments are allowed to deviate from any point on the
	 *  original curve.
	 *
	 * <p>The replied value is the one shared (and may be overridden) by the
	 * geometry factories.
	 *
	 * <p>The default value of the ratio is specified by
	 * {@link GeomConstants#SPLINE_APPROXIMATION_RATIO}.
	 *
	 * @return the maximal distance for approximating curves with segments.
	 * @since 14.0
	 * @see GeomConstants#SPLINE_APPROXIMATION_RATIO
	 */
	@Pure
	public static double getGlobalSplineApproximationRatio() {
		return globalSplineApproximation;
	}

	/** Change the maximum distance that the line segments used to approximate the
	 *  curved segments are allowed to deviate from any point on the
	 *  original curve.
	 *
	 * <p>The replied value is the one shared (and may be overridden) by the
	 * geometry factories.
	 *
	 * <p>The default value of the ratio is specified by
	 * {@link GeomConstants#SPLINE_APPROXIMATION_RATIO}.
	 *
	 * @param distance the maximal distance for approximating curves with segments.
	 *     If the argument value is {@code null} or NaN, the default value is assumed.
	 * @since 14.0
	 * @see GeomConstants#SPLINE_APPROXIMATION_RATIO
	 */
	public static void setGlobalSplineApproximationRatio(Double distance) {
		if (distance == null || Double.isNaN(distance.doubleValue())) {
			globalSplineApproximation = GeomConstants.SPLINE_APPROXIMATION_RATIO;
		} else {
			globalSplineApproximation = Math.max(0, distance);
		}
	}

	@Pure
	@Override
	public double getSplineApproximationRatio() {
		final Double value = this.splineApproximation;
		if (value != null) {
			return value.doubleValue();
		}
		return globalSplineApproximation;
	}

	@Override
	public void setSplineApproximationRatio(Double distance) {
		if (distance == null || Double.isNaN(distance)) {
			this.splineApproximation = null;
		} else {
			this.splineApproximation = distance;
		}
	}

}
