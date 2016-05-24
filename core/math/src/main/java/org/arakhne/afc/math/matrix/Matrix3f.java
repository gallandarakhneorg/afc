/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.matrix;

/**
 * Is represented internally as a 3x3 floating point matrix. The mathematical
 * representation is row major, as in traditional matrix mathematics.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link Matrix3d}
 */
@Deprecated
@SuppressWarnings("all")
public class Matrix3f extends Matrix3d {

	private static final long serialVersionUID = -7386754038391115819L;

	public Matrix3f() {
		super();
	}

	public Matrix3f(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21,
			double m22) {
		super(m00, m01, m02, m10, m11, m12, m20, m21, m22);
	}

	public Matrix3f(double[] values) {
		super(values);
	}

	public Matrix3f(Matrix3d matrix) {
		super(matrix);
	}

}
