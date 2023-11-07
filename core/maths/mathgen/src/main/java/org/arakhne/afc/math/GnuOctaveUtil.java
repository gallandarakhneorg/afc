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

import org.eclipse.xtext.xbase.lib.Pure;

/** Utilities for GNU Octave or (or Matlab).
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public final class GnuOctaveUtil {

	private static final String OCTAVE_START = "[ "; //$NON-NLS-1$

	private static final String OCTAVE_SEP = ","; //$NON-NLS-1$

	private static final String OCTAVE_NL = ";"; //$NON-NLS-1$

	private static final String OCTAVE_END = "]"; //$NON-NLS-1$

	private GnuOctaveUtil() {
		//
	}

	/** Replies the representation of this matrix.
	 *
	 * @param size the size of the matrix, i.e. the number of components in the tuple.
	 * @param values the matrix values.
	 * @return the GNU octave matrix.
	 */
	@Pure
	public static String toMatrixDefinition(int size, double... values) {
		assert values.length == size * size;
		final StringBuilder value = new StringBuilder(OCTAVE_START);
		int k = 0;
		for (int i = 0; i < size; ++i) {
			if (i > 0) {
				value.append(OCTAVE_NL);
			}
			for (int j = 0; j < size; ++j, ++k) {
				if (j > 0) {
					value.append(OCTAVE_SEP);
				}
				value.append(values[k]);
			}
		}
		value.append(OCTAVE_END);
		return value.toString();
	}

	/** Replies the tuple representation.
	 *
	 * @param size the size of the tuple, i.e. the number of components in the tuple.
	 * @param values the tuple values.
	 * @return the GNU octave representation of the tuple.
	 */
	public static String toTupleDefinition(int size, double... values) {
		assert values.length == size;
		final StringBuilder value = new StringBuilder(OCTAVE_START);
		int k = 0;
		for (int i = 0; i < size; ++i) {
			if (i > 0) {
				value.append(OCTAVE_SEP);
			}
			value.append(values[k]);
		}
		value.append(OCTAVE_END);
		return value.toString();
	}

	/** Replies the representation of the quaternion for GNU octave (or Matlab).
	 *
	 * @param x the x component of the quaternion.
	 * @param y the y component of the quaternion.
	 * @param z the z component of the quaternion.
	 * @param w the w component of the quaternion.
	 * @return the octave representation.
	 * @since 18.0
	 */
	public static String toQuaternionDefinition(double x, double y, double z, double w) {
		final StringBuilder output = new StringBuilder();
		output.append(w);
		if (x < 0.) {
			output.append(" - ").append(Math.abs(x)); //$NON-NLS-1$
		} else {
			output.append(" + ").append(Math.abs(x)); //$NON-NLS-1$
		}
		output.append("i"); //$NON-NLS-1$
		if (y < 0.) {
			output.append(" - ").append(Math.abs(y)); //$NON-NLS-1$
		} else {
			output.append(" + ").append(Math.abs(y)); //$NON-NLS-1$
		}
		output.append("j"); //$NON-NLS-1$
		if (z < 0.) {
			output.append(" - ").append(Math.abs(z)); //$NON-NLS-1$
		} else {
			output.append(" + ").append(Math.abs(z)); //$NON-NLS-1$
		}
		output.append("k"); //$NON-NLS-1$
		return output.toString();
	}

}
