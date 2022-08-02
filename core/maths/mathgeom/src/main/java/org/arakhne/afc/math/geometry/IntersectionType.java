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

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.annotations.ScalaOperator;
import org.arakhne.afc.vmutil.annotations.XtextOperator;

/**
 * This enumeration describes a intersection classification.
 *
 * <p>The operation intersection is not commutative. So,
 * <code>classify(A, B)</code> could not provides the
 * same intersection classification as
 * <code>classify(B, A)</code>.
 *
 * <p>The call <code>classify(A, B)</code> replies the following values:
 * <ul>
 * <li><code>INSIDE</code>: <code>A</code> is entirely inside <code>B</code>,</li>
 * <li><code>OUTSIDE</code>: <code>A</code> and <code>B</code> have not intersection,</li>
 * <li><code>SPANNING</code>: <code>A</code> and <code>B</code> have an intersection but
 * <code>A</code> is not entirely inside <code>B</code> nor <code>B</code> is not
 * entirely inside <code>A</code>,</li>
 * <li><code>ENCLOSING</code>: <code>B</code>  is entirely inside <code>A</code>,</li>
 * </ul>
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum IntersectionType {

	/**
	 * The second object is the same as the first one.
	 */
	SAME,
	/**
	 * The second object is entirely inside the first one.
	 */
	ENCLOSING,
	/**
	 * The first object is entirely inside the second one.
	 */
	INSIDE,
	/**
	 * The first object is intersecting the second one.
	 */
	SPANNING,
	/**
	 * The first object is entirely outside the second one.
	 */
	OUTSIDE;

	/** Invert the intersection classification.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code t}</td><td>result</td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 * </table>
	 *
	 * @param t the type.
	 * @return the inverted classification
	 */
	@Pure
	public static IntersectionType invert(IntersectionType t) {
		switch (t) {
		case INSIDE:
			return ENCLOSING;
		case OUTSIDE:
			return OUTSIDE;
		case SPANNING:
			return SPANNING;
		case ENCLOSING:
			return INSIDE;
			//$CASES-OMITTED$
		default:
			return t;
		}
	}

	/** Invert the intersection classification.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code t}</td><td>result</td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 * </table>
	 *
	 * @return the inverted classification
	 */
	@Pure
	public IntersectionType invert() {
		return invert(this);
	}

	/** Compute the OR-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global object
	 * that is composed of two sub objects for which we have the classitification
	 * respectively. This operator replies a positive intersection if at least
	 * one of the sub object intersections is positive.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code f1}</td><td>{@code f2}</td><td>result</td></tr>
	 *
	 * <tr><td><code>INSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SPANNING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SAME</code></td><td><code>INSIDE</code></td></tr>
	 *
	 * <tr><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>SPANNING</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>SAME</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 *
	 * </table>
	 *
	 * @param f1 is the classification of E against F1.
	 * @param f2 is the classification of E against F2.
	 * @return the classification of E against the whole object composed of F1 and F2.
	 */
	@Pure
	public static IntersectionType or(IntersectionType f1, IntersectionType f2) {
		if (f1 == f2) {
			return f1;
		}
		if (f1 == INSIDE || f2 == INSIDE) {
			return INSIDE;
		}
		return SPANNING;
	}

	/** Compute the OR-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global object
	 * that is composed of two sub objects for which we have the classitification
	 * respectively. This operator replies a positive intersection if at least
	 * one of the sub object intersections is positive.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code this}</td><td>{@code f2}</td><td>result</td></tr>
	 *
	 * <tr><td><code>INSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SPANNING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SAME</code></td><td><code>INSIDE</code></td></tr>
	 *
	 * <tr><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>SPANNING</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SAME</code></td><td><code>ENCLOSING</code></td></tr>
	 *
	 * <tr><td><code>SAME</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 *
	 * </table>
	 *
	 * @param f2 is the classification of E against F2.
	 * @return the classification of E against the whole object composed of F1 and F2.
	 */
	@Pure
	public IntersectionType or(IntersectionType f2) {
		return or(this, f2);
	}

	/** Compute the AND-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global 2D object
	 * when we know the classification against each of the two sides of the global 2D object.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code left}</td><td>{@code right}</td><td>result</td></tr>
	 *
	 * <tr><td><code>INSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SAME</code></td><td><code>INSIDE</code></td></tr>
	 *
	 * <tr><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SAME</code></td><td><code>OUTSIDE</code></td></tr>
	 *
	 * <tr><td><code>SPANNING</code></td><td><code>INSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SAME</code></td><td><code>ENCLOSING</code></td></tr>
	 *
	 * <tr><td><code>SAME</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 *
	 * </table>
	 *
	 * @param left the left operand.
	 * @param right the right operand.
	 * @return the result of the intersection.
	 */
	@Pure
	public static IntersectionType and(IntersectionType left, IntersectionType right) {
		if (left == right) {
			return left;
		}
		if ((left == INSIDE && right == ENCLOSING)
				|| (left == ENCLOSING && right == INSIDE)) {
			return SPANNING;
		}
		return (left.ordinal() > right.ordinal()) ? left : right;
	}

	/** Compute the AND-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global 2D object
	 * when we know the classification against each of the two sides of the global 2D object.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code this}</td><td>{@code type}</td><td>result</td></tr>
	 *
	 * <tr><td><code>INSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SAME</code></td><td><code>OUTSIDE</code></td></tr>
	 *
	 * <tr><td><code>SPANNING</code></td><td><code>INSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SAME</code></td><td><code>ENCLOSING</code></td></tr>
	 *
	 * <tr><td><code>SAME</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 *
	 * </table>
	 *
	 * @param type the type.
	 * @return the result of the intersection.
	 */
	@Pure
	public IntersectionType and(IntersectionType type) {
		return and(this, type);
	}

	/** Compute the AND-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global 2D object
	 * when we know the classification against each of the two sides of the global 2D object.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code this}</td><td>{@code type}</td><td>result</td></tr>
	 *
	 * <tr><td><code>INSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SAME</code></td><td><code>OUTSIDE</code></td></tr>
	 *
	 * <tr><td><code>SPANNING</code></td><td><code>INSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SAME</code></td><td><code>ENCLOSING</code></td></tr>
	 *
	 * <tr><td><code>SAME</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 *
	 * </table>
	 *
	 * @param right the type.
	 * @return the result of the intersection.
	 * @since 16.0
	 */
	@Pure
	@XtextOperator("&&")
	public  IntersectionType operator_and(IntersectionType right) {
		return and(this, right);
	}

	/** Compute the AND-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global 2D object
	 * when we know the classification against each of the two sides of the global 2D object.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code left}</td><td>{@code right}</td><td>result</td></tr>
	 *
	 * <tr><td><code>INSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SAME</code></td><td><code>INSIDE</code></td></tr>
	 *
	 * <tr><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SAME</code></td><td><code>OUTSIDE</code></td></tr>
	 *
	 * <tr><td><code>SPANNING</code></td><td><code>INSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SAME</code></td><td><code>ENCLOSING</code></td></tr>
	 *
	 * <tr><td><code>SAME</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 *
	 * </table>
	 *
	 * @param left the left operand.
	 * @param right the right operand.
	 * @return the result of the intersection.
	 * @since 16.0
	 */
	@Pure
	@XtextOperator("&&")
	@Inline(value = "$2.and($1, $2)", imported = IntersectionType.class, constantExpression = true)
	public static IntersectionType operator_and(IntersectionType left, IntersectionType right) {
		return and(left, right);
	}

	/** Compute the AND-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global 2D object
	 * when we know the classification against each of the two sides of the global 2D object.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code this}</td><td>{@code type}</td><td>result</td></tr>
	 *
	 * <tr><td><code>INSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SAME</code></td><td><code>OUTSIDE</code></td></tr>
	 *
	 * <tr><td><code>SPANNING</code></td><td><code>INSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SAME</code></td><td><code>ENCLOSING</code></td></tr>
	 *
	 * <tr><td><code>SAME</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 *
	 * </table>
	 *
	 * @param right the type.
	 * @return the result of the intersection.
	 * @since 16.0
	 */
	@Pure
	@ScalaOperator("&&")
	public  IntersectionType $and(IntersectionType right) {
		return and(this, right);
	}

	/** Compute the AND-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global 2D object
	 * when we know the classification against each of the two sides of the global 2D object.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code left}</td><td>{@code right}</td><td>result</td></tr>
	 *
	 * <tr><td><code>INSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SAME</code></td><td><code>INSIDE</code></td></tr>
	 *
	 * <tr><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SAME</code></td><td><code>OUTSIDE</code></td></tr>
	 *
	 * <tr><td><code>SPANNING</code></td><td><code>INSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SAME</code></td><td><code>ENCLOSING</code></td></tr>
	 *
	 * <tr><td><code>SAME</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 *
	 * </table>
	 *
	 * @param left the left operand.
	 * @param right the right operand.
	 * @return the result of the intersection.
	 * @since 16.0
	 */
	@Pure
	@ScalaOperator("&&")
	public static IntersectionType $and(IntersectionType left, IntersectionType right) {
		return and(left, right);
	}

	/** Compute the OR-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global object
	 * that is composed of two sub objects for which we have the classitification
	 * respectively. This operator replies a positive intersection if at least
	 * one of the sub object intersections is positive.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code this}</td><td>{@code f2}</td><td>result</td></tr>
	 *
	 * <tr><td><code>INSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SPANNING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SAME</code></td><td><code>INSIDE</code></td></tr>
	 *
	 * <tr><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>SPANNING</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SAME</code></td><td><code>ENCLOSING</code></td></tr>
	 *
	 * <tr><td><code>SAME</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 *
	 * </table>
	 *
	 * @param f2 is the classification of E against F2.
	 * @return the classification of E against the whole object composed of F1 and F2.
	 * @since 16.0
	 */
	@Pure
	@XtextOperator("||")
	public IntersectionType operator_or(IntersectionType f2) {
		return or(this, f2);
	}

	/** Compute the OR-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global object
	 * that is composed of two sub objects for which we have the classitification
	 * respectively. This operator replies a positive intersection if at least
	 * one of the sub object intersections is positive.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code f1}</td><td>{@code f2}</td><td>result</td></tr>
	 *
	 * <tr><td><code>INSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SPANNING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SAME</code></td><td><code>INSIDE</code></td></tr>
	 *
	 * <tr><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>SPANNING</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>SAME</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 *
	 * </table>
	 *
	 * @param f1 is the classification of E against F1.
	 * @param f2 is the classification of E against F2.
	 * @return the classification of E against the whole object composed of F1 and F2.
	 * @since 16.0
	 */
	@Pure
	@XtextOperator("||")
	public static IntersectionType operator_or(IntersectionType f1, IntersectionType f2) {
		return or(f1, f2);
	}

	/** Compute the OR-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global object
	 * that is composed of two sub objects for which we have the classitification
	 * respectively. This operator replies a positive intersection if at least
	 * one of the sub object intersections is positive.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code f1}</td><td>{@code f2}</td><td>result</td></tr>
	 *
	 * <tr><td><code>INSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SPANNING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SAME</code></td><td><code>INSIDE</code></td></tr>
	 *
	 * <tr><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>SPANNING</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>SAME</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 *
	 * </table>
	 *
	 * @param f1 is the classification of E against F1.
	 * @param f2 is the classification of E against F2.
	 * @return the classification of E against the whole object composed of F1 and F2.
	 * @since 16.0
	 */
	@Pure
	@ScalaOperator("||")
	public static IntersectionType $or(IntersectionType f1, IntersectionType f2) {
		return or(f1, f2);
	}

	/** Compute the OR-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global object
	 * that is composed of two sub objects for which we have the classitification
	 * respectively. This operator replies a positive intersection if at least
	 * one of the sub object intersections is positive.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code this}</td><td>{@code f2}</td><td>result</td></tr>
	 *
	 * <tr><td><code>INSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SPANNING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>SAME</code></td><td><code>INSIDE</code></td></tr>
	 *
	 * <tr><td><code>OUTSIDE</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>SPANNING</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SAME</code></td><td><code>SPANNING</code></td></tr>
	 *
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>SAME</code></td><td><code>ENCLOSING</code></td></tr>
	 *
	 * <tr><td><code>SAME</code></td><td><code>INSIDE</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>OUTSIDE</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>ENCLOSING</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 *
	 * </table>
	 *
	 * @param f2 is the classification of E against F2.
	 * @return the classification of E against the whole object composed of F1 and F2.
	 * @since 16.0
	 */
	@Pure
	@ScalaOperator("||")
	public IntersectionType $or(IntersectionType f2) {
		return or(this, f2);
	}

	/** Invert the intersection classification.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code t}</td><td>result</td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 * </table>
	 *
	 * @return the inverted classification
	 * @since 16.0
	 */
	@Pure
	@XtextOperator("!")
	public IntersectionType operator_not() {
		return invert(this);
	}

	/** Invert the intersection classification.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code t}</td><td>result</td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 * </table>
	 *
	 * @param t the type.
	 * @return the inverted classification
	 * @since 16.0
	 */
	@Pure
	@XtextOperator("!")
	@Inline(value = "$2.invert($1)", imported = IntersectionType.class, constantExpression = true)
	public static IntersectionType operator_not(IntersectionType t) {
		return invert(t);
	}

	/** Invert the intersection classification.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code t}</td><td>result</td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 * </table>
	 *
	 * @return the inverted classification
	 * @since 16.0
	 */
	@Pure
	@ScalaOperator("!")
	public IntersectionType $bang() {
		return invert(this);
	}


	/** Invert the intersection classification.
	 *
	 * <table border="1" width="100%" summary="Intersection Classification">
	 * <tr><td>{@code t}</td><td>result</td></tr>
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td></tr>
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 * </table>
	 *
	 * @param t the type.
	 * @return the inverted classification
	 * @since 16.0
	 */
	@Pure
	@ScalaOperator("!")
	public static IntersectionType $bang(IntersectionType t) {
		return invert(t);
	}

}
