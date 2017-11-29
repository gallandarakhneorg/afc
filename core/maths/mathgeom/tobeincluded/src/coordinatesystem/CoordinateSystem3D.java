/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.coordinatesystem;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.fpfx.Point2fx;
import org.arakhne.afc.math.geometry.d2.fpfx.Vector2fx;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Represents the different kind of 3D referencials
 * and provides the convertion utilities.
 * <p>
 * A referencial axis is expressed by the front, left and top directions.
 * For example <code>XYZ_LEFT_HAND</code> is for the coordinate system
 * with front direction along <code>+/-X</code> axis,
 * left direction along the <code>+/-Y</code> axis
 * and top direction along the <code>+/-Z</code> axis according to
 * a "left-hand" heuristic.
 * <p>
 * The default coordinate system is:
 * <ul>
 * <li>front: <code>(1,0,0)</code></li>
 * <li>left: <code>(0,1,0)</code></li>
 * <li>top: <code>(0,0,1)</code></li>
 * </ul>
 * 
 * <h3>Rotations</h3>
 * 
 * Rotations in a 3D coordinate system follow the right/left hand rules  
 * (assuming that <code>OX</code>, <code>OY</code> and <code>OZ</code> are the three axis of the coordinate system):
 * <table border="1" width="100%" summary="Rotations">
 * <tr>
 * <td>Right-handed rule:</td>
 * <td><ul>
 *     <li>axis cycle is: <code>OX</code> &gt; <code>OY</code> &gt; <code>OZ</code> &gt; <code>OX</code> &gt; <code>OY</code>;</li>
 *     <li>when rotating around <code>OX</code>, positive rotation angle is going from <code>OY</code> to <code>OZ</code></li>
 *     <li>when rotating around <code>OY</code>, positive rotation angle is going from <code>OZ</code> to <code>OX</code></li>
 *     <li>when rotating around <code>OZ</code>, positive rotation angle is going from <code>OX</code> to <code>OY</code></li>
 * 	   </ul><br>
 * 	   <a href=""><img border="0" width="200" src="doc-files/rotation_right.png" alt="[Right-handed Rotation Rule]"></a>
 * </td>
 * </tr><tr>
 * <td>Left-handed rule:</td>
 * <td><ul>
 *     <li>axis cycle is: <code>OX</code> &gt; <code>OY</code> &gt; <code>OZ</code> &gt; <code>OX</code> &gt; <code>OY</code>;</li>
 *     <li>when rotating around <code>OX</code>, positive rotation angle is going from <code>OY</code> to <code>OZ</code></li>
 *     <li>when rotating around <code>OY</code>, positive rotation angle is going from <code>OZ</code> to <code>OX</code></li>
 *     <li>when rotating around <code>OZ</code>, positive rotation angle is going from <code>OX</code> to <code>OY</code></li>
 * 	   </ul><br>
 * 	   <a href=""><img border="0" width="200" src="doc-files/rotation_left.png" alt="[Left-handed Rotation Rule]"></a>
 * </td>
 * </tr></table>
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public enum CoordinateSystem3D implements CoordinateSystem {

	/** Left handed XZY coordinate system.
	 * <p>
	 * <a hef="doc-files/xzy_left.png"><img src="doc-files/xzy_left.png" border="0" width="200" alt="[Left Handed XZY Coordinate System]"></a>
	 */
	XZY_LEFT_HAND(0,1,/* */1,0),

	/** Left handed XYZ coordinate system.
	 * <p>
	 * <a hef="doc-files/xyz_left.png"><img src="doc-files/xyz_left.png" border="0" width="200" alt="[Left Handed XYZ Coordinate System]"></a>
	 */
	XYZ_LEFT_HAND(-1,0,/* */0,1),

	/** Right handed XZY coordinate system.
	 * <p>
	 * <a hef="doc-files/xzy_right.png"><img src="doc-files/xzy_right.png" border="0" width="200" alt="[Right Handed XZY Coordinate System]"></a>
	 */
	XZY_RIGHT_HAND(0,-1,/* */1,0),	

	/** Right handed XYZ coordinate system.
	 * <p>
	 * <a hef="doc-files/xyz_right.png"><img src="doc-files/xyz_right.png" border="0" width="200" alt="[Right Handed XYZ Coordinate System]"></a>
	 */
	XYZ_RIGHT_HAND(1,0,/* */0,1);

	private static CoordinateSystem3D userDefault;



}
