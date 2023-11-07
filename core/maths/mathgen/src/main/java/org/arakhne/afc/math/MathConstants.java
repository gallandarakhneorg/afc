/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math;

/** Several mathematical constants.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public final class MathConstants {

	/** Inside zone according to the Cohen-Sutherland algorithm: {@code x in [xmin;xmax] and y in [ymin;ymax]}.
	 */
	public static final int COHEN_SUTHERLAND_INSIDE = 0;

	/** Left zone according to the Cohen-Sutherland algorithm: {@code x < xmin}.
	 */
	public static final int COHEN_SUTHERLAND_LEFT = 1;

	/** Right zone according to the Cohen-Sutherland algorithm: {@code x > xmax}.
	 */
	public static final int COHEN_SUTHERLAND_RIGHT = 2;

	/** Bottom zone according to the Cohen-Sutherland algorithm: {@code y < ymin}.
	 */
	public static final int COHEN_SUTHERLAND_BOTTOM = 4;

	/** Top zone according to the Cohen-Sutherland algorithm: {@code y > ymax}.
	 */
	public static final int COHEN_SUTHERLAND_TOP = 8;

	/** Front zone according to the Cohen-Sutherland algorithm.
	 */
	public static final int COHEN_SUTHERLAND_FRONT = 16;

	/** Behind zone according to the Cohen-Sutherland algorithm.
	 */
	public static final int COHEN_SUTHERLAND_BACK = 32;

	/** PI.
	 */
	public static final double PI = Math.PI;

	/** E.
	 */
	public static final double E = Math.E;

	/** 2 * PI.
	 */
	public static final double TWO_PI = 2. * PI;

	/** PI + PI/2.
	 */
	public static final double ONE_HALF_PI = 1.5 * PI;

	/** PI/2.
	 */
	public static final double DEMI_PI = .5 * PI;

	/** PI/4.
	 */
	public static final double QUARTER_PI = .25 * PI;

	/** 3*PI/4.
	 */
	public static final double THREE_QUARTER_PI = .75 * PI;

	/** Square root of 2.
	 */
	public static final double SQRT_2 = Math.sqrt(2.);

	/**
	 * Max sweeps in the Jacoby's algorithms.
	 */
	public static final int JACOBI_MAX_SWEEPS = 32;

	/**
	 * Number precision for the Jacoby's algorithms.
	 *
	 * <p>Given by the Java3D's implementation of the Matrix3d class.
	 */
	public static final double JACOBI_EPSILON = 1.110223024E-16;

	private MathConstants() {
		//
	}

}
