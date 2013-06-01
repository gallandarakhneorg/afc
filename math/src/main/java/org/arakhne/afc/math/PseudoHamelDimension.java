/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
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

package org.arakhne.afc.math;

/**
 * In mathematics, the dimension of a vector space V is the 
 * cardinality (i.e. the number of vectors) of a basis of V. 
 * It is sometimes called Hamel dimension or algebraic dimension to
 * distinguish it from other types of dimension. All bases of a 
 * vector space have equal cardinality and so the dimension of a 
 * vector space is uniquely defined. The dimension of the vector 
 * space V over the field F can be written as dimF(V) or as 
 * [V : F], read "dimension of V over F". 
 * When F can be inferred from context, often just 
 * dim(V) is written.
 * <p>
 * This enumeration describes the classical world dimensions
 * used in virtual life simulation.
 * <p>
 * Two fields are implicitly defined by this enumeration: the position type
 * and the rotation type.
 * <table>
 * <thead>
 * <tr><td>{@code PseudoHamelDimension}</td><td>Position type</td><td>Rotation type</td></tr>
 * </thead>
 * <tbody>
 * <tr><td>{@code DIMENSION_1D}</td><td>{@link Point1D}</td><td>{@code double}</td></tr>
 * <tr><td>{@code DIMENSION_1D5}</td><td>{@link Point1D5}</td><td>{@code double}</td></tr>
 * <tr><td>{@code DIMENSION_2D}</td><td>{@link Point2d}</td><td>{@code Vector2d} or {@code double}</td></tr>
 * <tr><td>{@code DIMENSION_2D5}</td><td>{@link Point3d}</td><td>{@link Vector2d} or {@code double}</td></tr>
 * <tr><td>{@code DIMENSION_3D}</td><td>{@link Point3d}</td><td>{@link Quat4d} or {@link AxisAngle4d}</td></tr>
 * </tbody>
 * </table>
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum PseudoHamelDimension {

	/** 1-dimensional.
	 * This dimension is used to describe euclidian spaces defined by:
	 * <ul>
	 * <li>point coordinates: 1-dimensional cartesian coordinates (curviline 
	 * coordinate), and</li>
	 * <li>rotations: rotation angle from the curve/graph segment (commonly ignored).</li>
	 * </ul>
	 */
	DIMENSION_1D(1f,1),
	
	/** 1.5-dimensional.
	 * This dimension is used to describe euclidian spaces defined by:
	 * <ul>
	 * <li>point coordinates: 1-dimensional cartesian coordinates (curviline 
	 * coordinate) and 1-dimension distance to the curve/graph segment, and</li>
	 * <li>rotations: rotation angle from the curve/graph segment (commonly ignored).</li>
	 * </ul>
	 */
	DIMENSION_1D5(1.5f,2),

	/** 2-dimensional.
	 * This dimension is used to describe euclidian spaces defined by:
	 * <ul>
	 * <li>point coordinates: 2-dimensional cartesian coordinates, and</li>
	 * <li>rotations: rotation angle around the &laquo;up&raquo;-basis vector,
	 * or 2-dimensional orientation vectors.</li>
	 * </ul>
	 */
	DIMENSION_2D(2f,2),

	/** 2.5-dimensional.
	 * This dimension is used to describe euclidian spaces defined by:
	 * <ul>
	 * <li>point coordinates: 3-dimensional cartesian coordinates, and</li>
	 * <li>rotations: rotation angle around the &laquo;up&raquo;-basis vector,
	 * or 2-dimensional orientation vectors.</li>
	 * </ul>
	 */
	DIMENSION_2D5(2.5f,3),

	/** 3-dimensional.
	 * This dimension is used to describe euclidian spaces defined by:
	 * <ul>
	 * <li>point coordinates: 3-dimensional cartesian coordinates, and</li>
	 * <li>rotations: 4-dimensional vectors ie. quaternions.</li>
	 * </ul>
	 */
	DIMENSION_3D(3f,3);
	
	/** Is the floating-point value which is corresponding to the dimension.
	 */
	public final float floatValue;
	
	/** Is the minimal count of coordinates required to specify a point
	 * in this dimension.
	 */
	public final byte mathematicalDimension;

	/**
	 * @param d
	 */
	private PseudoHamelDimension(float d, int m) {
		this.floatValue = d;
		this.mathematicalDimension = (byte)m;
	}
	
}
