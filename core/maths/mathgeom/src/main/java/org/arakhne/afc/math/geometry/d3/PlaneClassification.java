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

package org.arakhne.afc.math.geometry.d3;

import org.arakhne.afc.vmutil.annotations.ScalaOperator;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This enumeration describes a classification from a plane.
 * <p>
 * The operation intersection is not commutative. So,
 * {@code classifies(A,B)} could not provides the
 * same intersection classification as
 * {@code classifies(B,A)}.
 * <p>
 * The call {@code classify(A,B)} replies the following values:
 * <ul>
 * <li>{@code IN_FRONT_OF}
 * <li>{@code BEHIND}
 * <li>{@code COINCIDENT}
 * </ul>
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public enum PlaneClassification {

	/**
	 * An object is in the front of the plane.
	 */
	IN_FRONT_OF {
		@Override
		public PlaneClassification invert() {
			return PlaneClassification.BEHIND;
		}

		@Override
		public PlaneClassification or(PlaneClassification f2) {
			if (f2 == IN_FRONT_OF) {
				return IN_FRONT_OF;
			}
			if (f2 == COINCIDENT) {
				return COINCIDENT;
			}
			return IN_FRONT_OF;
		}

		@Override
		public PlaneClassification and(PlaneClassification f2) {
			if (f2 == IN_FRONT_OF) {
				return IN_FRONT_OF;
			}
			return COINCIDENT;
		}
	},

	/**
	 * An object is behind the plane.
	 */
	BEHIND {
		@Override
		public PlaneClassification invert() {
			return PlaneClassification.IN_FRONT_OF;
		}

		@Override
		public PlaneClassification or(PlaneClassification f2) {
			if (f2 == BEHIND) {
				return BEHIND;
			}
			if (f2 == COINCIDENT) {
				return COINCIDENT;
			}
			return IN_FRONT_OF;
		}

		@Override
		public PlaneClassification and(PlaneClassification f2) {
			if (f2 == BEHIND) {
				return BEHIND;
			}
			return COINCIDENT;
		}
	},

	/**
	 * An object is intersecting the plane.
	 */
	COINCIDENT {
		@Override
		public PlaneClassification invert() {
			return COINCIDENT;
		}

		@Override
		public PlaneClassification or(PlaneClassification f2) {
			return COINCIDENT;
		}

		@Override
		public PlaneClassification and(PlaneClassification f2) {
			return COINCIDENT;
		}
	};

	/** Invert the intersection classification.
	 *
	 * <p><table border="1" width="100%" summary="Plane Classification">
	 * <tr><td><var>t</var></td><td>result</td></tr>
	 * 
	 * <tr><td>{@code IN_FRONT_OF}</td><td>{@code BEHIND}</td></tr>
	 * 
	 * <tr><td>{@code BEHIND}</td><td>{@code IN_FRONT_OF}</td></tr>
	 * 
	 * <tr><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td></tr>
	 * 
	 * </table>
	 * 
	 * @param t
	 * @return the inverted classification.
	 */
	@Pure
	public abstract PlaneClassification invert();

	/** Compute the OR-combinaison of two classification types.
	 * It could be used to compute the intersection type of a global object
	 * that is composed of two sub objects for which we have the classification
	 * respectively. This operator replies a positive intersection if at least
	 * one of the sub object intersections is positive.
	 * <p>
	 * <table border="1" width="100%" summary="Plane Classification">
	 * <tr><td><var>this</var></td><td><var>f2</var></td><td>result</td></tr>
	 * 
	 * <tr><td>{@code BEHIND}</td><td>{@code BEHIND}</td><td>{@code BEHIND}</td></tr>
	 * <tr><td>{@code BEHIND}</td><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code BEHIND}</td><td>{@code IN_FRONT_OF}</td><td>{@code IN_FRONT_OF}</td></tr>
	 * 
	 * <tr><td>{@code COINCIDENT}</td><td>{@code BEHIND}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code COINCIDENT}</td><td>{@code IN_FRONT_OF}</td><td>{@code COINCIDENT}</td></tr>
	 * 
	 * <tr><td>{@code IN_FRONT_OF}</td><td>{@code BEHIND}</td><td>{@code IN_FRONT_OF}</td></tr>
	 * <tr><td>{@code IN_FRONT_OF}</td><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code IN_FRONT_OF}</td><td>{@code IN_FRONT_OF}</td><td>{@code IN_FRONT_OF}</td></tr>
	 *  
	 * </table>
	 * 
	 * @param f2 is the classification of E against F2.
	 * @return the classification of E against the whole object composed of F1 and F2.
	 */
	@Pure
	public abstract PlaneClassification or(PlaneClassification f2);

	/** Compute the AND-combinaison of two classification types.
	 * It could be used to compute the intersection type of a global 2D object
	 * when we know the classification against each of the two sides of the global 2D object.
	 * <p>
	 * <table border="1" width="100%" summary="Plane Classification">
	 * <tr><td><var>this</var></td><td><var>f2</var></td><td>result</td></tr>
	 * 
	 * <tr><td>{@code BEHIND}</td><td>{@code BEHIND}</td><td>{@code BEHIND}</td></tr>
	 * <tr><td>{@code BEHIND}</td><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code BEHIND}</td><td>{@code IN_FRONT_OF}</td><td>{@code COINCIDENT}</td></tr>
	 * 
	 * <tr><td>{@code COINCIDENT}</td><td>{@code BEHIND}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code COINCIDENT}</td><td>{@code IN_FRONT_OF}</td><td>{@code COINCIDENT}</td></tr>
	 * 
	 * <tr><td>{@code IN_FRONT_OF}</td><td>{@code BEHIND}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code IN_FRONT_OF}</td><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code IN_FRONT_OF}</td><td>{@code IN_FRONT_OF}</td><td>{@code IN_FRONT_OF}</td></tr>
	 * 
	 * </table>
	 * 
	 * @param f2
	 * @return the result of the intersection.
	 */
	@Pure
	public abstract PlaneClassification and(PlaneClassification f2);

	/** Compute the OR-combinaison of two classification types.
	 * It could be used to compute the intersection type of a global object
	 * that is composed of two sub objects for which we have the classification
	 * respectively. This operator replies a positive intersection if at least
	 * one of the sub object intersections is positive.
	 * <p>
	 * <table border="1" width="100%" summary="Plane Classification">
	 * <tr><td><var>this</var></td><td><var>f2</var></td><td>result</td></tr>
	 * 
	 * <tr><td>{@code BEHIND}</td><td>{@code BEHIND}</td><td>{@code BEHIND}</td></tr>
	 * <tr><td>{@code BEHIND}</td><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code BEHIND}</td><td>{@code IN_FRONT_OF}</td><td>{@code IN_FRONT_OF}</td></tr>
	 * 
	 * <tr><td>{@code COINCIDENT}</td><td>{@code BEHIND}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code COINCIDENT}</td><td>{@code IN_FRONT_OF}</td><td>{@code COINCIDENT}</td></tr>
	 * 
	 * <tr><td>{@code IN_FRONT_OF}</td><td>{@code BEHIND}</td><td>{@code IN_FRONT_OF}</td></tr>
	 * <tr><td>{@code IN_FRONT_OF}</td><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code IN_FRONT_OF}</td><td>{@code IN_FRONT_OF}</td><td>{@code IN_FRONT_OF}</td></tr>
	 *  
	 * </table>
	 * 
	 * @param f2 is the classification of E against F2.
	 * @return the classification of E against the whole object composed of F1 and F2.
	 */
	@Pure
	@XtextOperator("||")
	@Inline("or($1)")
	public PlaneClassification operator_or(PlaneClassification f2) {
		return or(f2);
	}

	/** Compute the AND-combinaison of two classification types.
	 * It could be used to compute the intersection type of a global 2D object
	 * when we know the classification against each of the two sides of the global 2D object.
	 * <p>
	 * <table border="1" width="100%" summary="Plane Classification">
	 * <tr><td><var>this</var></td><td><var>f2</var></td><td>result</td></tr>
	 * 
	 * <tr><td>{@code BEHIND}</td><td>{@code BEHIND}</td><td>{@code BEHIND}</td></tr>
	 * <tr><td>{@code BEHIND}</td><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code BEHIND}</td><td>{@code IN_FRONT_OF}</td><td>{@code COINCIDENT}</td></tr>
	 * 
	 * <tr><td>{@code COINCIDENT}</td><td>{@code BEHIND}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code COINCIDENT}</td><td>{@code IN_FRONT_OF}</td><td>{@code COINCIDENT}</td></tr>
	 * 
	 * <tr><td>{@code IN_FRONT_OF}</td><td>{@code BEHIND}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code IN_FRONT_OF}</td><td>{@code COINCIDENT}</td><td>{@code COINCIDENT}</td></tr>
	 * <tr><td>{@code IN_FRONT_OF}</td><td>{@code IN_FRONT_OF}</td><td>{@code IN_FRONT_OF}</td></tr>
	 * 
	 * </table>
	 * 
	 * @param f2
	 * @return the result of the intersection.
	 */
	@Pure
	@XtextOperator("&&")
	@Inline("and($1)")
	public PlaneClassification operator_and(PlaneClassification f2) {
		return and(f2);
	}

	/** Replies the invert of this classification: {@code !this}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @return the transpose
	 * @see #invert()
	 */
	@Pure
	@XtextOperator("!")
	@Inline("invert()")
	public PlaneClassification operator_not() {
		return invert();
	}

	/** Replies the invert of this classification: {@code !this}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @return the transpose
	 * @see #invert()
	 */
	@Pure
	@ScalaOperator("!")
	public PlaneClassification $bang() {
		return invert();
	}

}