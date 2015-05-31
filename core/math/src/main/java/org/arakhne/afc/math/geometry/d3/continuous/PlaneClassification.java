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
package org.arakhne.afc.math.geometry.d3.continuous;

/**
 * This enumeration describes a classification from a plane.
 * <p>
 * The operation intersection is not commutative. So,
 * <code>classifies(A,B)</code> could not provides the
 * same intersection classification as
 * <code>classifies(B,A)</code>.
 * <p>
 * The call <code>classify(A,B)</code> replies the following values:
 * <ul>
 * <li><code>IN_FRONT_OF</code>
 * <li><code>BEHIND</code>
 * <li><code>COINCIDENT</code>
 * </ul>
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum PlaneClassification {

	/**
	 * An object is in the front of the plane.
	 */
	IN_FRONT_OF,
	/**
	 * An object is behind the plane.
	 */
	BEHIND,
	/**
	 * An object is intersecting the plane.
	 */
	COINCIDENT;

	/** Invert the intersection classification.
	 * <p>
	 * <table border="1" width="100%">
	 * <tr><td><var>t</var></td><td>result</td></tr>
	 * 
	 * <tr><td><code>IN_FRONT_OF</code></td><td><code>BEHIND</code></td></tr>
	 * 
	 * <tr><td><code>BEHIND</code></td><td><code>IN_FRONT_OF</code></td></tr>
	 * 
	 * <tr><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td></tr>
	 * 
	 * </table>
	 * 
	 * @param t
	 * @return the inverted classification.
	 */
	public static PlaneClassification invert(PlaneClassification t) {
		switch (t) {
		case IN_FRONT_OF:
			return PlaneClassification.BEHIND;
		case BEHIND:
			return PlaneClassification.IN_FRONT_OF;
		default:
			return t;
		}
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
	 * <tr><td><code>BEHIND</code></td><td><code>BEHIND</code></td><td><code>BEHIND</code></td></tr>
	 * <tr><td><code>BEHIND</code></td><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>BEHIND</code></td><td><code>IN_FRONT_OF</code></td><td><code>IN_FRONT_OF</code></td></tr>
	 * 
	 * <tr><td><code>COINCIDENT</code></td><td><code>BEHIND</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>COINCIDENT</code></td><td><code>IN_FRONT_OF</code></td><td><code>COINCIDENT</code></td></tr>
	 * 
	 * <tr><td><code>IN_FRONT_OF</code></td><td><code>BEHIND</code></td><td><code>IN_FRONT_OF</code></td></tr>
	 * <tr><td><code>IN_FRONT_OF</code></td><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>IN_FRONT_OF</code></td><td><code>IN_FRONT_OF</code></td><td><code>IN_FRONT_OF</code></td></tr>
	 *  
	 * </table>
	 * 
	 * @param f1 is the classification of E against F1.
	 * @param f2 is the classification of E against F2.
	 * @return the classification of E against the whole object composed of F1 and F2.
	 */
	public static PlaneClassification or(PlaneClassification f1, PlaneClassification f2) {
		if (f1==f2) return f1;
		if (f1==COINCIDENT || f2==COINCIDENT) return COINCIDENT;
		return IN_FRONT_OF;
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
	 * <tr><td><code>BEHIND</code></td><td><code>BEHIND</code></td><td><code>BEHIND</code></td></tr>
	 * <tr><td><code>BEHIND</code></td><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>BEHIND</code></td><td><code>IN_FRONT_OF</code></td><td><code>IN_FRONT_OF</code></td></tr>
	 * 
	 * <tr><td><code>COINCIDENT</code></td><td><code>BEHIND</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>COINCIDENT</code></td><td><code>IN_FRONT_OF</code></td><td><code>COINCIDENT</code></td></tr>
	 * 
	 * <tr><td><code>IN_FRONT_OF</code></td><td><code>BEHIND</code></td><td><code>IN_FRONT_OF</code></td></tr>
	 * <tr><td><code>IN_FRONT_OF</code></td><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>IN_FRONT_OF</code></td><td><code>IN_FRONT_OF</code></td><td><code>IN_FRONT_OF</code></td></tr>
	 *  
	 * </table>
	 * 
	 * @param f2 is the classification of E against F2.
	 * @return the classification of E against the whole object composed of F1 and F2.
	 */
	public PlaneClassification or(PlaneClassification f2) {
		return or(this,f2);
	}

	/** Compute the AND-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global 2D object
	 * when we know the classification against each of the two sides of the global 2D object.
	 * <p>
	 * <table border="1" width="100%">
	 * <tr><td><var>f1</var></td><td><var>f2</var></td><td>result</td></tr>
	 * 
	 * <tr><td><code>BEHIND</code></td><td><code>BEHIND</code></td><td><code>BEHIND</code></td></tr>
	 * <tr><td><code>BEHIND</code></td><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>BEHIND</code></td><td><code>IN_FRONT_OF</code></td><td><code>COINCIDENT</code></td></tr>
	 * 
	 * <tr><td><code>COINCIDENT</code></td><td><code>BEHIND</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>COINCIDENT</code></td><td><code>IN_FRONT_OF</code></td><td><code>COINCIDENT</code></td></tr>
	 * 
	 * <tr><td><code>IN_FRONT_OF</code></td><td><code>BEHIND</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>IN_FRONT_OF</code></td><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>IN_FRONT_OF</code></td><td><code>IN_FRONT_OF</code></td><td><code>IN_FRONT_OF</code></td></tr>
	 * 
	 * </table>
	 * 
	 * @param f1
	 * @param f2
	 * @return the result of the intersection.
	 */
	public static PlaneClassification and(PlaneClassification f1, PlaneClassification f2) {
		if (f1==f2) return f1;
		return COINCIDENT;
	}

	/** Compute the AND-combinaison of two intersection types.
	 * It could be used to compute the intersection type of a global 2D object
	 * when we know the classification against each of the two sides of the global 2D object.
	 * <p>
	 * <table border="1" width="100%">
	 * <tr><td><var>this</var></td><td><var>f2</var></td><td>result</td></tr>
	 * 
	 * <tr><td><code>BEHIND</code></td><td><code>BEHIND</code></td><td><code>BEHIND</code></td></tr>
	 * <tr><td><code>BEHIND</code></td><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>BEHIND</code></td><td><code>IN_FRONT_OF</code></td><td><code>COINCIDENT</code></td></tr>
	 * 
	 * <tr><td><code>COINCIDENT</code></td><td><code>BEHIND</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>COINCIDENT</code></td><td><code>IN_FRONT_OF</code></td><td><code>COINCIDENT</code></td></tr>
	 * 
	 * <tr><td><code>IN_FRONT_OF</code></td><td><code>BEHIND</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>IN_FRONT_OF</code></td><td><code>COINCIDENT</code></td><td><code>COINCIDENT</code></td></tr>
	 * <tr><td><code>IN_FRONT_OF</code></td><td><code>IN_FRONT_OF</code></td><td><code>IN_FRONT_OF</code></td></tr>
	 * 
	 * </table>
	 * 
	 * @param f2
	 * @return the result of the intersection.
	 */
	public PlaneClassification and(PlaneClassification f2) {
		return and(this,f2);
	}

}