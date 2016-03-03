/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math;

/** Several mathematical constants.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface MathConstants {

	/** Inside zone according to the Cohen-Sutherland algorithm.
	 */
	public static final int COHEN_SUTHERLAND_INSIDE = 0; // 0000
	/** Left zone according to the Cohen-Sutherland algorithm.
	 */
	public static final int COHEN_SUTHERLAND_LEFT = 1;   // 0001
	/** Right zone according to the Cohen-Sutherland algorithm.
	 */
	public static final int COHEN_SUTHERLAND_RIGHT = 2;  // 0010
	/** Bottom zone according to the Cohen-Sutherland algorithm.
	 */
	public static final int COHEN_SUTHERLAND_BOTTOM = 4; // 0100
	/** Top zone according to the Cohen-Sutherland algorithm.
	 */
	public static final int COHEN_SUTHERLAND_TOP = 8;    // 1000
	
	/** PI
	 */
	public static final double PI = Math.PI;

	/** E
	 */
	public static final double E = Math.E;

	/** 2 * PI
	 */
	public static final double TWO_PI = 2. * PI;

	/** PI + PI/2
	 */
	public static final double ONE_HALF_PI = 1.5 * PI;

	/** PI/2
	 */
	public static final double DEMI_PI = .5 * PI;

	/** PI/4
	 */
	public static final double QUARTER_PI = .25 * PI;

	/** 3*PI/4
	 */
	public static final double THREE_QUARTER_PI = .75 * PI;

	/** Square root of 2
	 */
	public static final double SQRT_2 = 1.*Math.sqrt(2.);

	/**
	 * Max sweeps in the Jacoby's algorithms.
	 */
	public static final int JACOBI_MAX_SWEEPS = 32;
	
	/**
	 * Max sweeps in the Ellipse's algorithms.
	 */
	public static final int ELLIPSE_MAX_SWEEPS = 32;

	/** This is the maximale distance that
	 *  permits to detect hits.
	 */
	public static final double HIT_DISTANCE = 5 ;

	/** The maximum distance that the line 
	 *  segments used to approximate the 
	 *  curved segments are allowed to 
	 *  deviate from any point on the 
	 *  original curve.
	 *  <p>
	 *  This attributes is used to parameter the approximation
	 *  of the curve rendering.
	 */
	public static final double SPLINE_APPROXIMATION_RATIO = .1;
	
	/**
     * The rectangle intersection test counts the number of times
     * that the path crosses through the shadow that the rectangle
     * projects to the right towards (x => +INFINITY).
     *
     * During processing of the path it actually counts every time
     * the path crosses either or both of the top and bottom edges
     * of that shadow.  If the path enters from the top, the count
     * is incremented.  If it then exits back through the top, the
     * same way it came in, the count is decremented and there is
     * no impact on the winding count.  If, instead, the path exits
     * out the bottom, then the count is incremented again and a
     * full pass through the shadow is indicated by the winding count
     * having been incremented by 2.
     *
     * Thus, the winding count that it accumulates is actually double
     * the real winding count.  Since the path is continuous, the
     * final answer should be a multiple of 2, otherwise there is a
     * logic error somewhere.
     *
     * If the path ever has a direct hit on the rectangle, then a
     * special value is returned.  This special value terminates
     * all ongoing accumulation on up through the call chain and
     * ends up getting returned to the calling function which can
     * then produce an answer directly.  For intersection tests,
     * the answer is always "true" if the path intersects the
     * rectangle.  For containment tests, the answer is always
     * "false" if the path intersects the rectangle.  Thus, no
     * further processing is ever needed if an intersection occurs.
     */
    public static final int SHAPE_INTERSECTS = 0x80000000;

}