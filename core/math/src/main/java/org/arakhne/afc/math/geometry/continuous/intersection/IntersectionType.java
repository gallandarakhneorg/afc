/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.continuous.intersection;

/**
 * This enumeration describes a intersection classification.
 * <p>
 * The operation intersection is not commutative. So,
 * <code>classify(A,B)</code> could not provides the
 * same intersection classification as
 * <code>classify(B,A)</code>.
 * <p>
 * The call <code>classify(A,B)</code> replies the following values:
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
	 * <p>
	 * <table border="1" width="100%">
	 * <tr><td><var>t</var></td><td>result</td></tr>
	 * 
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td></tr>
	 * 
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * 
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * 
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * 
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 * 
	 * </table>
	 * 
	 * @param t
	 * @return the inverted classification
	 */
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
		default:
			return t;
		}
	}
	
	/** Invert the intersection classification.
	 * <p>
	 * <table border="1" width="100%">
	 * <tr><td><var>t</var></td><td>result</td></tr>
	 * 
	 * <tr><td><code>INSIDE</code></td><td><code>ENCLOSING</code></td></tr>
	 * 
	 * <tr><td><code>OUTSIDE</code></td><td><code>OUTSIDE</code></td></tr>
	 * 
	 * <tr><td><code>SPANNING</code></td><td><code>SPANNING</code></td></tr>
	 * 
	 * <tr><td><code>ENCLOSING</code></td><td><code>INSIDE</code></td></tr>
	 * 
	 * <tr><td><code>SAME</code></td><td><code>SAME</code></td></tr>
	 * 
	 * </table>
	 * 
	 * @return the inverted classification
	 */
	public IntersectionType invert() {
		return invert(this);
	}

	/** Compute the OR-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global object
	 * that is composed of two sub objects for which we have the classitification
	 * respectively. This operator replies a positive intersection if at least
	 * one of the sub object intersections is positive.
	 * <p>
	 * <table border="1" width="100%">
	 * <tr><td><var>f1</var></td><td><var>f2</var></td><td>result</td></tr>
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
	public static IntersectionType or(IntersectionType f1, IntersectionType f2) {
		if (f1==f2) return f1;
		if (f1==INSIDE || f2==INSIDE) return INSIDE;
		return SPANNING;
	}
	
	/** Compute the OR-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global object
	 * that is composed of two sub objects for which we have the classitification
	 * respectively. This operator replies a positive intersection if at least
	 * one of the sub object intersections is positive.
	 * <p>
	 * <table border="1" width="100%">
	 * <tr><td><var>this</var></td><td><var>f2</var></td><td>result</td></tr>
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
	public IntersectionType or(IntersectionType f2) {
		return or(this,f2);
	}

	/** Compute the AND-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global 2D object
	 * when we know the classification against each of the two sides of the global 2D object.
	 * <p>
	 * <table border="1" width="100%">
	 * <tr><td><var>t</var></td><td><var>c</var></td><td>result</td></tr>
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
	 * @param t
	 * @param c
	 * @return the result of the intersection.
	 */
	public static IntersectionType and(IntersectionType t, IntersectionType c) {
		if (t==c) return t;
		if ((t==INSIDE && c==ENCLOSING)||
			(t==ENCLOSING && c==INSIDE)) return SPANNING;
		return (t.ordinal()>c.ordinal()) ? t : c;
	}

	/** Compute the AND-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global 2D object
	 * when we know the classification against each of the two sides of the global 2D object.
	 * <p>
	 * <table border="1" width="100%">
	 * <tr><td><var>this</var></td><td><var>c</var></td><td>result</td></tr>
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
	 * @param c
	 * @return the result of the intersection.
	 */
	public IntersectionType and(IntersectionType c) {
		return and(this,c);
	}

}