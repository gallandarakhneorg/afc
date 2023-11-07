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

package org.arakhne.afc.math.geometry.d3.afp;

import org.arakhne.afc.math.GeogebraUtil;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.GeomConstants;
import org.arakhne.afc.math.geometry.d3.Plane3D;
import org.arakhne.afc.math.geometry.d3.PlaneClassification;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.PointVector3DReceiver;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/** This class represents a 3D plane with double floating point coordinates.
 *
 * @param <PT> is the type of the plane.
 * @param <S> is the type of the geometric structure that represents the intersection of two planes.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <Q> is the type of the quaternions.
 * @author $Author: sgalland$
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public interface Plane3afp<PT extends Plane3afp<?, S, P, V, Q>,
S extends Segment3afp<?, ?, ?, P, V, Q, ?>,
P extends Point3D<? super P, ? super V, ? super Q>,
V extends Vector3D<? super V, ? super P, ? super Q>,
Q extends Quaternion<? super P, ? super V, ? super Q>>
extends Plane3D<PT, S, P, V, Q> {

	/** {@inheritDoc}
	 * <p>This function is overridden to refine the return to the the 3D AFP API.
	 */
	@Pure
	@Override
	GeomFactory3afp<?, P, V, Q, ?> getGeomFactory();

	/**
	 * Replies the projection on the plane of a point with {@code (a,b,c)} not necessary a unit vector.
	 * This function computes the unit vector of the plane normal. The function
	 * {@link #calculatesPlanePointProjectionWithPlaneNormal(double, double, double, double, double, double, double, Point3D)}
	 * calculates the same projection point assuming {@code (a,b,c)} is already a unit normal vector. 
	 * <p>
	 * <strong>First Approach: arithmetic resolution</strong>
	 * <p>
	 * Let <math xmlns="http://www.w3.org/1998/Math/MathML"><mover><mrow><mi>u</mi></mrow><mo stretchy="false">&#x02192;</mo></mover></math> a vector colinear to the line <math><mi mathvariant="normal">&#x00394;</mi></math> with components <math><mo stretchy="false">(</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002C;</mo><msub><mi>y</mi><mi>u</mi></msub> <mo>&#x0002C;</mo><msub><mi>z</mi><mi>u</mi></msub><mo stretchy="false">)</mo></math>.<br>
	 * Let the equation of the plane <math><mi mathvariant="normal">&#x003A0;</mi></math> as <math><mi>a</mi><mo>.</mo><mi>x</mi><mo>&#x0002B;</mo><mi>b</mi><mo>.</mo><mi>y</mi><mo>&#x0002B;</mo><mi>c</mi><mo>.</mo> <mi>z</mi><mo>&#x0002B;</mo><mi>d</mi><mo>&#x0003D;</mo><mn>0</mn></math><br>
	 * Let the point <math><mi>A</mi></math> at coordinates <math><mo stretchy="false">(</mo><msub><mi>x</mi><mi>A</mi></msub><mo>&#x0002C;</mo><msub><mi>y</mi><mi>A</mi></msub> <mo>&#x0002C;</mo><msub><mi>z</mi><mi>A</mi></msub><mo stretchy="false">)</mo></math> and its projection point <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> at coordinates <math><mo stretchy="false">(</mo><msub><mi>x</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub>
	 * <mo>&#x0002C;</mo><msub><mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub> <mo>&#x0002C;</mo><msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><mo stretchy="false">)</mo></math>.
	 * <p>
	 * Since <math><mo stretchy="false">(</mo><mi>A</mi><msup><mi>A</mi><mo>&#x02032;</mo></msup><mo stretchy="false">)</mo></math> is parallel to <math><mi mathvariant="normal">&#x00394;</mi></math>, a scalar <math><mi>k</mi></math> exists such as <math><mover><mrow><mi>A</mi><mrow><mi>A</mi><msup><mo>&#x02032;</mo></msup></mrow></mrow><mo stretchy="true">&#x02192;</mo></mover> <mo>&#x0003D;</mo><mi>k</mi><mo>&#x022C5;</mo><mover><mi>u</mi><mo
	 * stretchy="false">&#x02192;</mo></mover></math><br>
	 * that <math><mrow><mo>&#x0007B;</mo><mtable><mtr><mtd><msub><mi>x</mi> <mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub> <mo>&#x02212;</mo><msub><mi>x</mi><mi>A</mi></msub><mo>&#x0003D;</mo> <mi>k</mi><mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub> </mtd></mtr><mtr><mtd><msub><mi>y</mi><mrow><msup><mi>A</mi> <mo>&#x02032;</mo></msup></mrow></msub><mo>&#x02212;</mo><msub> <mi>y</mi><mi>A</mi></msub><mo>&#x0003D;</mo><mi>k</mi>
	 * <mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub></mtd></mtr> <mtr><mtd><msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo> </msup></mrow></msub><mo>&#x02212;</mo><msub><mi>z</mi><mi>A</mi> </msub><mo>&#x0003D;</mo><mi>k</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mtd></mtr></mtable></mrow></math>.
	 * <p>
	 * 
	 * In addition, <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> is on <math><mi mathvariant="normal">&#x003A0;</mi></math>, what means that <math> <mi>a</mi><mo>.</mo><msub><mi>x</mi><mrow><msup><mi>A</mi> <mo>&#x02032;</mo></msup></mrow></msub><mo>&#x0002B;</mo><mi>b</mi> <mo>.</mo><msub><mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo> </msup></mrow></msub><mo>&#x0002B;</mo><mi>c</mi><mo>.</mo> <msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup>
	 * </mrow></msub><mo>&#x0002B;</mo><mi>d</mi><mo>&#x0003D;</mo><mn>0</mn> </math>.
	 * <p>
	 * The result is a system of 4 equations with 4 unknown variables: <math><msub><mi>x</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><math>, <math><msub><mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><math>, <math><msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><math> and <math><mi>k</mi><math>.
	 * <p>
	 * <math> <mrow><mo>&#x0007B;</mo><mtable><mtr><mtd><msub><mi>x</mi><mrow> <msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub> <mo>&#x0003D;</mo><mfrac><mrow><mi>b</mi><mo>&#x022C5;</mo><mo stretchy="false">(</mo><msub><mi>x</mi><mi>A</mi></msub><msub> <mi>y</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>y</mi> <mi>A</mi></msub><msub><mi>x</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo> <mo
	 * stretchy="false">(</mo><msub><mi>x</mi><mi>A</mi></msub> <msub><mi>z</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>z</mi> <mi>A</mi></msub><msub><mi>x</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo> <msub><mi>x</mi><mi>u</mi></msub></mrow><mrow><mi>a</mi> <mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002B;</mo> <mi>b</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub>
	 * <mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mrow></mfrac></mtd></mtr><mtr><mtd><msub> <mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow> </msub><mo>&#x0003D;</mo><mfrac><mrow><mi>a</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>y</mi><mi>A</mi></msub> <msub><mi>x</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>x</mi> <mi>A</mi></msub><msub><mi>y</mi><mi>u</mi></msub><mo
	 * stretchy="false">)</mo><mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>y</mi><mi>A</mi></msub> <msub><mi>z</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>z</mi> <mi>A</mi></msub><msub><mi>y</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo> <msub><mi>y</mi><mi>u</mi></msub></mrow><mrow><mi>a</mi> <mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002B;</mo>
	 * <mi>b</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub> <mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mrow></mfrac></mtd></mtr><mtr><mtd><msub> <mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow> </msub><mo>&#x0003D;</mo><mfrac><mrow><mi>a</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>z</mi><mi>A</mi></msub> <msub><mi>x</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>x</mi>
	 * <mi>A</mi></msub><msub><mi>z</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x0002B;</mo><mi>b</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>z</mi><mi>A</mi></msub> <msub><mi>y</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>y</mi> <mi>A</mi></msub><msub><mi>z</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo> <msub><mi>z</mi><mi>u</mi></msub></mrow><mrow><mi>a</mi>
	 * <mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002B;</mo> <mi>b</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub> <mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mrow></mfrac></mtd></mtr></mtable></mrow> </math>
	 * <p>
	 * In the case of an orthogonal projection and if the reference axis are orthonormal, one can choose <math><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0003D;</mo><mi>a</mi></math>, <math><msub><mi>y</mi><mi>u</mi></msub><mo>&#x0003D;</mo><mi>b</mi></math>, <math><msub><mi>z</mi><mi>u</mi></msub><mo>&#x0003D;</mo><mi>c</mi></math>, then:
	 * <p>
	 * <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"><mrow> <mo>&#x0007B;</mo><mtable><mtr><mtd><msub><mi>x</mi><mrow><msup> <mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><mo>&#x0003D;</mo> <mfrac><mrow><mo stretchy="false">(</mo><msup><mi>b</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>c</mi><mn>2</mn></msup><mo stretchy="false">)</mo><mo>&#x022C5;</mo><msub><mi>x</mi><mi>A</mi> </msub><mo>&#x02212;</mo><mi>a</mi><mi>b</mi><mo>&#x022C5;</mo>
	 * <msub><mi>y</mi><mi>A</mi></msub><mo>&#x02212;</mo><mi>a</mi> <mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi><mi>A</mi></msub> <mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo><mi>a</mi></mrow> <mrow><msup><mi>a</mi><mn>2</mn></msup><mo>&#x0002B;</mo><msup> <mi>b</mi><mn>2</mn></msup><mo>&#x0002B;</mo><msup><mi>c</mi> <mn>2</mn></msup></mrow></mfrac></mtd></mtr><mtr><mtd><msub> <mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow>
	 * </msub><mo>&#x0003D;</mo><mfrac><mrow><mo>&#x02212;</mo><mi>a</mi> <mi>b</mi><mo>&#x022C5;</mo><msub><mi>x</mi><mi>A</mi></msub> <mo>&#x0002B;</mo><mo stretchy="false">(</mo><msup><mi>a</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>c</mi><mn>2</mn></msup><mo stretchy="false">)</mo><mo>&#x022C5;</mo><msub><mi>y</mi><mi>A</mi> </msub><mo>&#x02212;</mo><mi>b</mi><mi>c</mi><mo>&#x022C5;</mo> <msub><mi>z</mi><mi>A</mi></msub><mo>&#x02212;</mo><mi>d</mi>
	 * <mo>&#x022C5;</mo><mi>b</mi></mrow><mrow><msup><mi>a</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>b</mi><mn>2</mn></msup> <mo>&#x0002B;</mo><msup><mi>c</mi><mn>2</mn></msup></mrow> </mfrac></mtd></mtr><mtr><mtd><msub><mi>z</mi><mrow><msup><mi>A</mi> <mo>&#x02032;</mo></msup></mrow></msub><mo>&#x0003D;</mo><mfrac> <mrow><mo>&#x02212;</mo><mi>a</mi><mi>c</mi><mo>&#x022C5;</mo> <msub><mi>x</mi><mi>A</mi></msub><mo>&#x02212;</mo><mi>b</mi>
	 * <mi>c</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>A</mi></msub> <mo>&#x0002B;</mo><mo stretchy="false">(</mo><msup><mi>a</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>b</mi><mn>2</mn></msup><mo stretchy="false">)</mo><mo>&#x022C5;</mo><msub><mi>z</mi><mi>A</mi> </msub><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo><mi>c</mi> </mrow><mrow><msup><mi>a</mi><mn>2</mn></msup><mo>&#x0002B;</mo> <msup><mi>b</mi><mn>2</mn></msup><mo>&#x0002B;</mo><msup><mi>c</mi>
	 * <mn>2</mn></msup></mrow></mfrac></mtd></mtr></mtable></mrow> </math>
	 * <p>
	 * <strong>Second Approach: vectorial resolution</strong>
	 * <p>
	 * Let the normal of the plane be <math><mover><mi>n</mi><mo stretchy="false">&#x02192;</mo></mover></math> with the coordinates <math><mo stretchy="false">(</mo><msub><mi>n</mi><mi>x</mi></msub><mo>&#x0002C;</mo> <msub><mi>n</mi><mi>y</mi></msub><mo>&#x0002C;</mo><msub><mi>n</mi><mi>z</mi></msub> <mo stretchy="false">)</mo></math>.<br>
	 * Let <math><mi>A</mi></math> the point of coordinates <math><mo stretchy="false">(</mo><msub><mi>A</mi><mi>x</mi></msub><mo>&#x0002C;</mo> <msub><mi>A</mi><mi>y</mi></msub><mo>&#x0002C;</mo><msub><mi>A</mi><mi>z</mi></msub> <mo stretchy="false">)</mo></math> and its projection on the plane <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> with the coordinates <math><mo
	 * stretchy="false">(</mo><msub><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow><mi>x</mi></msub> <mo>&#x0002C;</mo><msub><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow><mi>y</mi></msub> <mo>&#x0002C;</mo><msub><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow><mi>z</mi></msub> <mo stretchy="false">)</mo></math>.
	 * <p>
	 * Let <math><mi>s</mi></math> the distance between the point <math><mi>A</mi></math> and the nearest point on the plane, ie. the point <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> such as: <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"><mi>s</mi><mo>&#x0003D;</mo> <mi>a</mi><mo>.</mo><msub><mi>A</mi><mi>x</mi></msub> <mo>&#x0002B;</mo><mi>b</mi><mo>.</mo><msub><mi>A</mi><mi>y</mi> </msub><mo>&#x0002B;</mo><mi>c</mi><mo>.</mo><msub><mi>A</mi>
	 * <mi>z</mi></msub><mo>&#x0002B;</mo><mi>d</mi></math>. If <math><mi>s</mi></math> is positive, the point is in the front of the plane. If <math><mi>s</mi></math> is negative, the point is behind the plane.
	 * <p>
	 * Consequently <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"><msup><mi>A</mi> <mo>&#x02032;</mo></msup><mo>&#x0003D;</mo><mi>A</mi><mo>&#x02212;</mo> <mfrac><mrow><mi>s</mi></mrow><mrow><mo stretchy="false">&#x0007C;</mo> <mover><mrow><mrow><mi>n</mi></mrow></mrow><mo stretchy="false">&#x02192;</mo></mover><mo stretchy="false">&#x0007C;</mo> </mrow></mfrac><mover><mrow><mrow><mi>n</mi></mrow></mrow><mo stretchy="false">&#x02192;</mo></mover></math>.
	 * <p>
	 * <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"> <mrow> <mo>&#x0007B;</mo> <mtable> <mtr> <mtd> <msub> <mi>x</mi> <mrow> <msup> <mi>A</mi> <mo>&#x02032;</mo> </msup> </mrow> </msub> <mo>&#x0003D;</mo> <msub> <mi>x</mi> <mi>A</mi> </msub> <mo>&#x02212;</mo> <mfrac> <mrow> <mi>s</mi> </mrow> <mrow> <mo stretchy="false">&#x0007C;</mo> <mover> <mrow> <mrow> <mi>n</mi> </mrow> </mrow> <mo stretchy="false">&#x02192;</mo> </mover> <mo
	 * stretchy="false">&#x0007C;</mo> </mrow> </mfrac> <msub> <mi>n</mi> <mi>x</mi> </msub> </mtd> </mtr> <mtr> <mtd> <msub> <mi>y</mi> <mrow> <msup> <mi>A</mi> <mo>&#x02032;</mo> </msup> </mrow> </msub> <mo>&#x0003D;</mo> <msub> <mi>y</mi> <mi>A</mi> </msub> <mo>&#x02212;</mo> <mfrac> <mrow> <mi>s</mi> </mrow> <mrow> <mo stretchy="false">&#x0007C;</mo> <mover> <mrow> <mrow> <mi>n</mi> </mrow> </mrow> <mo stretchy="false">&#x02192;</mo> </mover> <mo
	 * stretchy="false">&#x0007C;</mo> </mrow> </mfrac> <msub> <mi>n</mi> <mi>y</mi> </msub> </mtd> </mtr> <mtr> <mtd> <msub> <mi>z</mi> <mrow> <msup> <mi>A</mi> <mo>&#x02032;</mo> </msup> </mrow> </msub> <mo>&#x0003D;</mo> <msub> <mi>z</mi> <mi>A</mi> </msub> <mo>&#x02212;</mo> <mfrac> <mrow> <mi>s</mi> </mrow> <mrow> <mo stretchy="false">&#x0007C;</mo> <mover> <mrow> <mrow> <mi>n</mi> </mrow> </mrow> <mo stretchy="false">&#x02192;</mo> </mover> <mo
	 * stretchy="false">&#x0007C;</mo> </mrow> </mfrac> <msub> <mi>n</mi> <mi>z</mi> </msub> </mtd> </mtr> </mtable> </mrow> </math>
	 * 
	 * @param a the a component of the plane equation.
	 * @param b the b component of the plane equation.
	 * @param c the c component of the plane equation.
	 * @param d the d component of the plane equation.
	 * @param x the x coordinate of the project to project on the plane.
	 * @param y the y coordinate of the project to project on the plane.
	 * @param z the z coordinate of the project to project on the plane.
	 * @param result the projection point.
	 * @see #calculatesPlanePointProjectionWithPlaneNormal(double, double, double, double, double, double, double, Point3D)
	 */
	static void calculatesPlanePointProjection(
			double a, double b, double c, double d,
			double x, double y, double z,
			Point3D<?, ?, ?> result) {
		assert result != null : AssertMessages.notNullParameter(7);

		final double normalSqLength = a * a + b * b + c * c;
		final double nx;
		final double ny;
		final double nz;
		if (MathUtil.isEpsilonEqual(normalSqLength, 1., GeomConstants.UNIT_VECTOR_EPSILON)) {
			nx = a;
			ny = b;
			nz = c;
		} else {
			final double normalLength = Math.sqrt(normalSqLength);
			nx = a / normalLength;
			ny = b / normalLength;
			nz = c / normalLength;
		}

		calculatesPlanePointProjectionWithPlaneNormal(nx, ny, nz, d, x, y, z, result);
	}

	/**
	 * Replies the projection on the plane of a point with {@code (a,b,c)} the unit normal of the plane.
	 * <p>
	 * <strong>First Approach: arithmetic resolution</strong>
	 * <p>
	 * Let <math xmlns="http://www.w3.org/1998/Math/MathML"><mover><mrow><mi>u</mi></mrow><mo stretchy="false">&#x02192;</mo></mover></math> a vector colinear to the line <math><mi mathvariant="normal">&#x00394;</mi></math> with components <math><mo stretchy="false">(</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002C;</mo><msub><mi>y</mi><mi>u</mi></msub> <mo>&#x0002C;</mo><msub><mi>z</mi><mi>u</mi></msub><mo stretchy="false">)</mo></math>.<br>
	 * Let the equation of the plane <math><mi mathvariant="normal">&#x003A0;</mi></math> as <math><mi>a</mi><mo>.</mo><mi>x</mi><mo>&#x0002B;</mo><mi>b</mi><mo>.</mo><mi>y</mi><mo>&#x0002B;</mo><mi>c</mi><mo>.</mo> <mi>z</mi><mo>&#x0002B;</mo><mi>d</mi><mo>&#x0003D;</mo><mn>0</mn></math><br>
	 * Let the point <math><mi>A</mi></math> at coordinates <math><mo stretchy="false">(</mo><msub><mi>x</mi><mi>A</mi></msub><mo>&#x0002C;</mo><msub><mi>y</mi><mi>A</mi></msub> <mo>&#x0002C;</mo><msub><mi>z</mi><mi>A</mi></msub><mo stretchy="false">)</mo></math> and its projection point <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> at coordinates <math><mo stretchy="false">(</mo><msub><mi>x</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub>
	 * <mo>&#x0002C;</mo><msub><mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub> <mo>&#x0002C;</mo><msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><mo stretchy="false">)</mo></math>.
	 * <p>
	 * Since <math><mo stretchy="false">(</mo><mi>A</mi><msup><mi>A</mi><mo>&#x02032;</mo></msup><mo stretchy="false">)</mo></math> is parallel to <math><mi mathvariant="normal">&#x00394;</mi></math>, a scalar <math><mi>k</mi></math> exists such as <math><mover><mrow><mi>A</mi><mrow><mi>A</mi><msup><mo>&#x02032;</mo></msup></mrow></mrow><mo stretchy="true">&#x02192;</mo></mover> <mo>&#x0003D;</mo><mi>k</mi><mo>&#x022C5;</mo><mover><mi>u</mi><mo
	 * stretchy="false">&#x02192;</mo></mover></math><br>
	 * that <math><mrow><mo>&#x0007B;</mo><mtable><mtr><mtd><msub><mi>x</mi> <mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub> <mo>&#x02212;</mo><msub><mi>x</mi><mi>A</mi></msub><mo>&#x0003D;</mo> <mi>k</mi><mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub> </mtd></mtr><mtr><mtd><msub><mi>y</mi><mrow><msup><mi>A</mi> <mo>&#x02032;</mo></msup></mrow></msub><mo>&#x02212;</mo><msub> <mi>y</mi><mi>A</mi></msub><mo>&#x0003D;</mo><mi>k</mi>
	 * <mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub></mtd></mtr> <mtr><mtd><msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo> </msup></mrow></msub><mo>&#x02212;</mo><msub><mi>z</mi><mi>A</mi> </msub><mo>&#x0003D;</mo><mi>k</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mtd></mtr></mtable></mrow></math>.
	 * <p>
	 * 
	 * In addition, <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> is on <math><mi mathvariant="normal">&#x003A0;</mi></math>, what means that <math> <mi>a</mi><mo>.</mo><msub><mi>x</mi><mrow><msup><mi>A</mi> <mo>&#x02032;</mo></msup></mrow></msub><mo>&#x0002B;</mo><mi>b</mi> <mo>.</mo><msub><mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo> </msup></mrow></msub><mo>&#x0002B;</mo><mi>c</mi><mo>.</mo> <msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup>
	 * </mrow></msub><mo>&#x0002B;</mo><mi>d</mi><mo>&#x0003D;</mo><mn>0</mn> </math>.
	 * <p>
	 * The result is a system of 4 equations with 4 unknown variables: <math><msub><mi>x</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><math>, <math><msub><mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><math>, <math><msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><math> and <math><mi>k</mi><math>.
	 * <p>
	 * <math> <mrow><mo>&#x0007B;</mo><mtable><mtr><mtd><msub><mi>x</mi><mrow> <msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub> <mo>&#x0003D;</mo><mfrac><mrow><mi>b</mi><mo>&#x022C5;</mo><mo stretchy="false">(</mo><msub><mi>x</mi><mi>A</mi></msub><msub> <mi>y</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>y</mi> <mi>A</mi></msub><msub><mi>x</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo> <mo
	 * stretchy="false">(</mo><msub><mi>x</mi><mi>A</mi></msub> <msub><mi>z</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>z</mi> <mi>A</mi></msub><msub><mi>x</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo> <msub><mi>x</mi><mi>u</mi></msub></mrow><mrow><mi>a</mi> <mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002B;</mo> <mi>b</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub>
	 * <mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mrow></mfrac></mtd></mtr><mtr><mtd><msub> <mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow> </msub><mo>&#x0003D;</mo><mfrac><mrow><mi>a</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>y</mi><mi>A</mi></msub> <msub><mi>x</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>x</mi> <mi>A</mi></msub><msub><mi>y</mi><mi>u</mi></msub><mo
	 * stretchy="false">)</mo><mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>y</mi><mi>A</mi></msub> <msub><mi>z</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>z</mi> <mi>A</mi></msub><msub><mi>y</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo> <msub><mi>y</mi><mi>u</mi></msub></mrow><mrow><mi>a</mi> <mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002B;</mo>
	 * <mi>b</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub> <mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mrow></mfrac></mtd></mtr><mtr><mtd><msub> <mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow> </msub><mo>&#x0003D;</mo><mfrac><mrow><mi>a</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>z</mi><mi>A</mi></msub> <msub><mi>x</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>x</mi>
	 * <mi>A</mi></msub><msub><mi>z</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x0002B;</mo><mi>b</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>z</mi><mi>A</mi></msub> <msub><mi>y</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>y</mi> <mi>A</mi></msub><msub><mi>z</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo> <msub><mi>z</mi><mi>u</mi></msub></mrow><mrow><mi>a</mi>
	 * <mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002B;</mo> <mi>b</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub> <mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mrow></mfrac></mtd></mtr></mtable></mrow> </math>
	 * <p>
	 * In the case of an orthogonal projection and if the reference axis are orthonormal, one can choose <math><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0003D;</mo><mi>a</mi></math>, <math><msub><mi>y</mi><mi>u</mi></msub><mo>&#x0003D;</mo><mi>b</mi></math>, <math><msub><mi>z</mi><mi>u</mi></msub><mo>&#x0003D;</mo><mi>c</mi></math>, then:
	 * <p>
	 * <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"><mrow> <mo>&#x0007B;</mo><mtable><mtr><mtd><msub><mi>x</mi><mrow><msup> <mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><mo>&#x0003D;</mo> <mfrac><mrow><mo stretchy="false">(</mo><msup><mi>b</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>c</mi><mn>2</mn></msup><mo stretchy="false">)</mo><mo>&#x022C5;</mo><msub><mi>x</mi><mi>A</mi> </msub><mo>&#x02212;</mo><mi>a</mi><mi>b</mi><mo>&#x022C5;</mo>
	 * <msub><mi>y</mi><mi>A</mi></msub><mo>&#x02212;</mo><mi>a</mi> <mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi><mi>A</mi></msub> <mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo><mi>a</mi></mrow> <mrow><msup><mi>a</mi><mn>2</mn></msup><mo>&#x0002B;</mo><msup> <mi>b</mi><mn>2</mn></msup><mo>&#x0002B;</mo><msup><mi>c</mi> <mn>2</mn></msup></mrow></mfrac></mtd></mtr><mtr><mtd><msub> <mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow>
	 * </msub><mo>&#x0003D;</mo><mfrac><mrow><mo>&#x02212;</mo><mi>a</mi> <mi>b</mi><mo>&#x022C5;</mo><msub><mi>x</mi><mi>A</mi></msub> <mo>&#x0002B;</mo><mo stretchy="false">(</mo><msup><mi>a</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>c</mi><mn>2</mn></msup><mo stretchy="false">)</mo><mo>&#x022C5;</mo><msub><mi>y</mi><mi>A</mi> </msub><mo>&#x02212;</mo><mi>b</mi><mi>c</mi><mo>&#x022C5;</mo> <msub><mi>z</mi><mi>A</mi></msub><mo>&#x02212;</mo><mi>d</mi>
	 * <mo>&#x022C5;</mo><mi>b</mi></mrow><mrow><msup><mi>a</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>b</mi><mn>2</mn></msup> <mo>&#x0002B;</mo><msup><mi>c</mi><mn>2</mn></msup></mrow> </mfrac></mtd></mtr><mtr><mtd><msub><mi>z</mi><mrow><msup><mi>A</mi> <mo>&#x02032;</mo></msup></mrow></msub><mo>&#x0003D;</mo><mfrac> <mrow><mo>&#x02212;</mo><mi>a</mi><mi>c</mi><mo>&#x022C5;</mo> <msub><mi>x</mi><mi>A</mi></msub><mo>&#x02212;</mo><mi>b</mi>
	 * <mi>c</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>A</mi></msub> <mo>&#x0002B;</mo><mo stretchy="false">(</mo><msup><mi>a</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>b</mi><mn>2</mn></msup><mo stretchy="false">)</mo><mo>&#x022C5;</mo><msub><mi>z</mi><mi>A</mi> </msub><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo><mi>c</mi> </mrow><mrow><msup><mi>a</mi><mn>2</mn></msup><mo>&#x0002B;</mo> <msup><mi>b</mi><mn>2</mn></msup><mo>&#x0002B;</mo><msup><mi>c</mi>
	 * <mn>2</mn></msup></mrow></mfrac></mtd></mtr></mtable></mrow> </math>
	 * <p>
	 * <strong>Second Approach: vectorial resolution</strong>
	 * <p>
	 * Let the normal of the plane be <math><mover><mi>n</mi><mo stretchy="false">&#x02192;</mo></mover></math> with the coordinates <math><mo stretchy="false">(</mo><msub><mi>n</mi><mi>x</mi></msub><mo>&#x0002C;</mo> <msub><mi>n</mi><mi>y</mi></msub><mo>&#x0002C;</mo><msub><mi>n</mi><mi>z</mi></msub> <mo stretchy="false">)</mo></math>.<br>
	 * Let <math><mi>A</mi></math> the point of coordinates <math><mo stretchy="false">(</mo><msub><mi>A</mi><mi>x</mi></msub><mo>&#x0002C;</mo> <msub><mi>A</mi><mi>y</mi></msub><mo>&#x0002C;</mo><msub><mi>A</mi><mi>z</mi></msub> <mo stretchy="false">)</mo></math> and its projection on the plane <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> with the coordinates <math><mo
	 * stretchy="false">(</mo><msub><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow><mi>x</mi></msub> <mo>&#x0002C;</mo><msub><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow><mi>y</mi></msub> <mo>&#x0002C;</mo><msub><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow><mi>z</mi></msub> <mo stretchy="false">)</mo></math>.
	 * <p>
	 * Let <math><mi>s</mi></math> the distance between the point <math><mi>A</mi></math> and the nearest point on the plane, ie. the point <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> such as: <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"><mi>s</mi><mo>&#x0003D;</mo> <mi>a</mi><mo>.</mo><msub><mi>A</mi><mi>x</mi></msub> <mo>&#x0002B;</mo><mi>b</mi><mo>.</mo><msub><mi>A</mi><mi>y</mi> </msub><mo>&#x0002B;</mo><mi>c</mi><mo>.</mo><msub><mi>A</mi>
	 * <mi>z</mi></msub><mo>&#x0002B;</mo><mi>d</mi></math>. If <math><mi>s</mi></math> is positive, the point is in the front of the plane. If <math><mi>s</mi></math> is negative, the point is behind the plane.
	 * <p>
	 * Consequently <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"><msup><mi>A</mi> <mo>&#x02032;</mo></msup><mo>&#x0003D;</mo><mi>A</mi><mo>&#x02212;</mo> <mfrac><mrow><mi>s</mi></mrow><mrow><mo stretchy="false">&#x0007C;</mo> <mover><mrow><mrow><mi>n</mi></mrow></mrow><mo stretchy="false">&#x02192;</mo></mover><mo stretchy="false">&#x0007C;</mo> </mrow></mfrac><mover><mrow><mrow><mi>n</mi></mrow></mrow><mo stretchy="false">&#x02192;</mo></mover></math>.
	 * <p>
	 * <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"> <mrow> <mo>&#x0007B;</mo> <mtable> <mtr> <mtd> <msub> <mi>x</mi> <mrow> <msup> <mi>A</mi> <mo>&#x02032;</mo> </msup> </mrow> </msub> <mo>&#x0003D;</mo> <msub> <mi>x</mi> <mi>A</mi> </msub> <mo>&#x02212;</mo> <mfrac> <mrow> <mi>s</mi> </mrow> <mrow> <mo stretchy="false">&#x0007C;</mo> <mover> <mrow> <mrow> <mi>n</mi> </mrow> </mrow> <mo stretchy="false">&#x02192;</mo> </mover> <mo
	 * stretchy="false">&#x0007C;</mo> </mrow> </mfrac> <msub> <mi>n</mi> <mi>x</mi> </msub> </mtd> </mtr> <mtr> <mtd> <msub> <mi>y</mi> <mrow> <msup> <mi>A</mi> <mo>&#x02032;</mo> </msup> </mrow> </msub> <mo>&#x0003D;</mo> <msub> <mi>y</mi> <mi>A</mi> </msub> <mo>&#x02212;</mo> <mfrac> <mrow> <mi>s</mi> </mrow> <mrow> <mo stretchy="false">&#x0007C;</mo> <mover> <mrow> <mrow> <mi>n</mi> </mrow> </mrow> <mo stretchy="false">&#x02192;</mo> </mover> <mo
	 * stretchy="false">&#x0007C;</mo> </mrow> </mfrac> <msub> <mi>n</mi> <mi>y</mi> </msub> </mtd> </mtr> <mtr> <mtd> <msub> <mi>z</mi> <mrow> <msup> <mi>A</mi> <mo>&#x02032;</mo> </msup> </mrow> </msub> <mo>&#x0003D;</mo> <msub> <mi>z</mi> <mi>A</mi> </msub> <mo>&#x02212;</mo> <mfrac> <mrow> <mi>s</mi> </mrow> <mrow> <mo stretchy="false">&#x0007C;</mo> <mover> <mrow> <mrow> <mi>n</mi> </mrow> </mrow> <mo stretchy="false">&#x02192;</mo> </mover> <mo
	 * stretchy="false">&#x0007C;</mo> </mrow> </mfrac> <msub> <mi>n</mi> <mi>z</mi> </msub> </mtd> </mtr> </mtable> </mrow> </math>
	 * 
	 * @param a the a component of the plane equation.
	 * @param b the b component of the plane equation.
	 * @param c the c component of the plane equation.
	 * @param d the d component of the plane equation.
	 * @param x the x coordinate of the project to project on the plane.
	 * @param y the y coordinate of the project to project on the plane.
	 * @param z the z coordinate of the project to project on the plane.
	 * @param result the projection point.
	 * @see #calculatesPlanePointProjection(double, double, double, double, double, double, double, Point3D)
	 */
	static void calculatesPlanePointProjectionWithPlaneNormal(
			double a, double b, double c, double d,
			double x, double y, double z,
			Point3D<?, ?, ?> result) {
		assert result != null : AssertMessages.notNullParameter(7);
		assert MathUtil.isEpsilonEqual(a * a + b * b + c * c, 1., GeomConstants.UNIT_VECTOR_EPSILON) : AssertMessages.invalidValue(0);

		// Plan is defined by : a * u + b * v + c * w + d = 0
		// So that the distance to the plane is: a * x + b * y + c * z + d
		final double k = a * x + b * y + c * z + d;

		// Distance k may be negative or positive. It may be used to multiply the normal vector
		// for obtaining the vector from the projection point P' and the projected point P.
		// Consequently, P = P' + k (a,b,c) and P' = P - k (a,b,c)
		final double px = x - k * a;
		final double py = y - k * b;
		final double pz = z - k * c;

		result.set(px, py, pz);
	}

	/** Calculates the distance between the given plane and this plane.
	 * 
	 * @param a1 the a coordinate of the first plane.
	 * @param b1 the b coordinate of the first plane.
	 * @param c1 the c coordinate of the first plane.
	 * @param d1 the d coordinate of the first plane.
	 * @param a2 the a coordinate of the other plane.
	 * @param b2 the b coordinate of the other plane.
	 * @param c2 the c coordinate of the other plane.
	 * @param d2 the d coordinate of the other plane.
	 * @return the distance between the two planes.
	 */
	@Pure
	static double calculatesPlanePlaneDistance(
			double a1, double b1, double c1, double d1,
			double a2, double b2, double c2, double d2) {
		final double length1 = Math.sqrt(a1 * a1 + b1 * b1 + c1 * c1);
		final double nx1;
		final double ny1;
		final double nz1;
		if (MathUtil.isEpsilonEqual(length1, 1., GeomConstants.UNIT_VECTOR_EPSILON)) {
			nx1 = a1;
			ny1 = b1;
			nz1 = c1;
		} else {
			nx1 = a1 / length1;
			ny1 = b1 / length1;
			nz1 = c1 / length1;
		}

		final double length2 = Math.sqrt(a2 * a2 + b2 * b2 + c2 * c2);
		final double nx2;
		final double ny2;
		final double nz2;
		if (MathUtil.isEpsilonEqual(length2, 1., GeomConstants.UNIT_VECTOR_EPSILON)) {
			nx2 = a2;
			ny2 = b2;
			nz2 = c2;
		} else {
			nx2 = a2 / length2;
			ny2 = b2 / length2;
			nz2 = c2 / length2;
		}

		final double dotProduct = Vector3D.dotProduct(nx1, ny1, nz1, nx2, ny2, nz2);

		if (MathUtil.isEpsilonEqual(Math.abs(dotProduct), 1., GeomConstants.UNIT_VECTOR_EPSILON)) {
			// Planes are coplanar.
			// The problem could be restricted to a 1D problem.

			// Distance of the first pane to the origin (0,0,0) is d1.
			// Distance of the second pane to the origin (0,0,0) is d2.
			// Invert the sign of d2 if the normal vectors of the two planes are not directed in the same direction.
			final double dist2 = dotProduct < 0. ? -d2 : d2;
			if (d1 <= dist2) {
				return dist2 - d1;
			}
			return d1 - dist2;
		}
		return 0.;
	}

	/** Calculates the distance between the given plane and this plane.
	 * 
	 * @param a first component of the plane equation.
	 * @param b second component of the plane equation.
	 * @param c third component of the plane equation.
	 * @param d fourth component of the plane equation.
	 * @param px the x coordinate of the point.
	 * @param py the y coordinate of the point.
	 * @param pz the c coordinate of the point.
	 * @return the distance between the two planes.
	 */
	@Pure
	static double calculatesPlanePointDistance(
			double a, double b, double c, double d,
			double px, double py, double pz) {
		final double normalSqLength = a * a + b * b + c * c;
		final double nx;
		final double ny;
		final double nz;
		if (MathUtil.isEpsilonEqual(normalSqLength, 1., GeomConstants.UNIT_VECTOR_EPSILON)) {
			nx = a;
			ny = b;
			nz = c;
		} else {
			final double normalLength = Math.sqrt(normalSqLength);
			nx = a / normalLength;
			ny = b / normalLength;
			nz = c / normalLength;
		}
		return nx * px + ny * py + nz * pz + d;
	}

	/** Classifies the given point against to the plane.
	 * 
	 * @param a first component of the plane equation.
	 * @param b second component of the plane equation.
	 * @param c third component of the plane equation.
	 * @param d fourth component of the plane equation.
	 * @param px the x coordinate of the point.
	 * @param py the y coordinate of the point.
	 * @param pz the c coordinate of the point.
	 * @return the classification. 
	 */
	@Pure
	static PlaneClassification classifiesPlanePoint(
			double a, double b, double c, double d,
			double px, double py, double pz) {
		final double k = calculatesPlanePointDistance(a, b, c, d, px, py ,pz);
		if (MathUtil.isEpsilonZero(k, GeomConstants.UNIT_VECTOR_EPSILON)) {
			return PlaneClassification.COINCIDENT;
		}
		if (k < 0.) {
			return PlaneClassification.BEHIND;
		}
		return PlaneClassification.IN_FRONT_OF;
	}

	/** Calculates the distance between the given sphere and this plane.
	 * 
	 * @param a first component of the plane equation.
	 * @param b second component of the plane equation.
	 * @param c third component of the plane equation.
	 * @param d fourth component of the plane equation.
	 * @param sx the x coordinate of the sphere center.
	 * @param sy the y coordinate of the sphere center.
	 * @param sz the c coordinate of the sphere center.
	 * @return the distance between the plane and the sphere.
	 */
	@Pure
	static double calculatesPlaneSphereDistance(
			double a, double b, double c, double d,
			double sx, double sy, double sz, double radius) {
		final double dist = calculatesPlanePointDistance(a, b, c, d, sx, sy, sz);
		if (Math.abs(dist) <= radius) {
			return 0.;
		}
		if (dist < 0.) {
			return dist + radius;
		}
		return dist - radius;
	}

	/** Classifies the given sphere against to the plane.
	 * 
	 * @param a first component of the plane equation.
	 * @param b second component of the plane equation.
	 * @param c third component of the plane equation.
	 * @param d fourth component of the plane equation.
	 * @param sx the x coordinate of the sphere center.
	 * @param sy the y coordinate of the sphere center.
	 * @param sz the c coordinate of the sphere center.
	 * @param radius the radius of the sphere.
	 * @return the classification. 
	 */
	@Pure
	static PlaneClassification classifiesPlaneSphere(
			double a, double b, double c, double d,
			double sx, double sy, double sz, double radius) {
		final double dist = calculatesPlanePointDistance(a, b, c, d, sx, sy, sz);
		if (!MathUtil.isEpsilonEqual(dist, radius, GeomConstants.UNIT_VECTOR_EPSILON) && Math.abs(dist) < radius) {
			return PlaneClassification.COINCIDENT;
		}
		if (dist < 0.) {
			return PlaneClassification.BEHIND;
		}
		return PlaneClassification.IN_FRONT_OF;
	}

	/** Calculates the distance between the given aligned box and this plane.
	 * 
	 * @param a first component of the plane equation.
	 * @param b second component of the plane equation.
	 * @param c third component of the plane equation.
	 * @param d fourth component of the plane equation.
	 * @param minx the minimal x coordinate of the box.
	 * @param miny the minimal y coordinate of the box.
	 * @param minz the minimal z coordinate of the box.
	 * @param maxx the maximal x coordinate of the box.
	 * @param maxy the maximal y coordinate of the box.
	 * @param maxz the maximal z coordinate of the box.
	 * @return the distance between the plane and the box.
	 */
	@Pure
	static double calculatesPlaneAlignedBoxDistance(
			double a, double b, double c, double d,
			double minx, double miny, double minz, double maxx, double maxy, double maxz) {
		assert minx <= maxx : AssertMessages.lowerEqualParameters(2, minx, 5, maxx);
		assert miny <= maxy : AssertMessages.lowerEqualParameters(3, miny, 6, maxy);
		assert minz <= maxz : AssertMessages.lowerEqualParameters(4, minz, 7, maxz);

		final double normalSqLength = a * a + b * b + c * c;
		final double nx;
		final double ny;
		final double nz;
		if (MathUtil.isEpsilonEqual(normalSqLength, 1., GeomConstants.UNIT_VECTOR_EPSILON)) {
			nx = a;
			ny = b;
			nz = c;
		} else {
			final double normalLength = Math.sqrt(normalSqLength);
			nx = a / normalLength;
			ny = b / normalLength;
			nz = c / normalLength;
		}

		double minx2;
		double miny2;
		double minz2;
		double maxx2;
		double maxy2;
		double maxz2;

		// X axis 
		if (nx >= 0.) { 
			minx2 = minx; 
			maxx2 = maxx; 
		} else { 
			minx2 = maxx; 
			maxx2 = minx; 
		} 

		// Y axis 
		if (ny >= 0.) { 
			miny2 = miny; 
			maxy2 = maxy; 
		} else { 
			miny2 = maxy; 
			maxy2 = miny; 
		} 

		// Z axis 
		if (nz >= 0.) { 
			minz2 = minz; 
			maxz2 = maxz; 
		} else { 
			minz2 = maxz; 
			maxz2 = minz; 
		}

		double dist = Vector3D.dotProduct(nx, ny, nz, minx2, miny2, minz2) + d;
		if (dist > 0.) {
			return dist; 
		}

		dist = Vector3D.dotProduct(nx, ny, nz, maxx2, maxy2, maxz2) + d;
		if (dist >= 0.) {
			return 0.;
		}

		return dist;
	}

	/** Classifies the given segment against to the plane.
	 * 
	 * @param a first component of the plane equation.
	 * @param b second component of the plane equation.
	 * @param c third component of the plane equation.
	 * @param d fourth component of the plane equation.
	 * @param x1 the x coordinate of the first point of the segment.
	 * @param y1 the y coordinate of the first point of the segment.
	 * @param z1 the z coordinate of the first point of the segment.
	 * @param x2 the x coordinate of the second point of the segment.
	 * @param y2 the y coordinate of the second point of the segment.
	 * @param z2 the z coordinate of the second point of the segment.
	 * @return the classification. 
	 */
	@Pure
	static PlaneClassification classifiesPlaneSegment(
			double a, double b, double c, double d,
			double x1, double y1, double z1,
			double x2, double y2, double z2) {
		final double normalSqLength = a * a + b * b + c * c;
		final double nx;
		final double ny;
		final double nz;
		if (MathUtil.isEpsilonEqual(normalSqLength, 1., GeomConstants.UNIT_VECTOR_EPSILON)) {
			nx = a;
			ny = b;
			nz = c;
		} else {
			final double normalLength = Math.sqrt(normalSqLength);
			nx = a / normalLength;
			ny = b / normalLength;
			nz = c / normalLength;
		}

		final double distp1 = nx * x1 + ny * y1 + nz * z1 + d;
		if (MathUtil.isEpsilonZero(distp1, GeomConstants.UNIT_VECTOR_EPSILON)) {
			return PlaneClassification.COINCIDENT;
		}

		final double distp2 = nx * x2 + ny * y2 + nz * z2 + d;
		if (MathUtil.isEpsilonZero(distp2, GeomConstants.UNIT_VECTOR_EPSILON)) {
			return PlaneClassification.COINCIDENT;
		}

		if (distp1 > 0.) {
			if (distp2 < 0.) {
				return PlaneClassification.COINCIDENT;
			}
			return PlaneClassification.IN_FRONT_OF;
		}
		if (distp2 > 0.) {
			return PlaneClassification.COINCIDENT;
		}
		return PlaneClassification.BEHIND;
	}


	/** Classifies the given segment against to the plane.
	 * 
	 * @param a first component of the plane equation.
	 * @param b second component of the plane equation.
	 * @param c third component of the plane equation.
	 * @param d fourth component of the plane equation.
	 * @param minx the x coordinate of the minimum point of the aligned box.
	 * @param miny the y coordinate of the minimum point of the aligned box.
	 * @param minz the z coordinate of the minimum point of the aligned box.
	 * @param maxx the x coordinate of the maximum point of the aligned box.
	 * @param maxy the y coordinate of the maximum point of the aligned box.
	 * @param maxz the z coordinate of the maximum point of the aligned box.
	 * @return the classification. 
	 */
	@Pure
	static PlaneClassification classifiesPlaneAlignedBox(
			double a, double b, double c, double d,
			double minx, double miny, double minz,
			double maxx, double maxy, double maxz) {
		assert minx <= maxx : AssertMessages.lowerEqualParameters(2, minx, 5, maxx);
		assert miny <= maxy : AssertMessages.lowerEqualParameters(3, miny, 6, maxy);
		assert minz <= maxz : AssertMessages.lowerEqualParameters(4, minz, 7, maxz);
		final double normalSqLength = a * a + b * b + c * c;
		final double nx;
		final double ny;
		final double nz;
		if (MathUtil.isEpsilonEqual(normalSqLength, 1., GeomConstants.UNIT_VECTOR_EPSILON)) {
			nx = a;
			ny = b;
			nz = c;
		} else {
			final double normalLength = Math.sqrt(normalSqLength);
			nx = a / normalLength;
			ny = b / normalLength;
			nz = c / normalLength;
		}

		double minx2;
		double miny2;
		double minz2;
		double maxx2;
		double maxy2;
		double maxz2;

		// X axis 
		if (nx >= 0.) { 
			minx2 = minx; 
			maxx2 = maxx; 
		} else { 
			minx2 = maxx; 
			maxx2 = minx; 
		} 

		// Y axis 
		if (ny >= 0.) { 
			miny2 = miny; 
			maxy2 = maxy; 
		} else { 
			miny2 = maxy; 
			maxy2 = miny; 
		} 

		// Z axis 
		if (nz >= 0.) { 
			minz2 = minz; 
			maxz2 = maxz; 
		} else { 
			minz2 = maxz; 
			maxz2 = minz; 
		} 

		if ((Vector3D.dotProduct(nx, ny, nz, minx2, miny2, minz2) + d) > 0.) {
			return PlaneClassification.IN_FRONT_OF; 
		}

		if ((Vector3D.dotProduct(nx, ny, nz, maxx2, maxy2, maxz2) + d) >= 0.) {
			return PlaneClassification.COINCIDENT;
		}

		return PlaneClassification.BEHIND;
	}

	/** Replies the intersection factor of the given segment when it is intersecting the plane.
	 *
	 * <p>If the segment and the plane are not intersecting, this
	 * function replies {@link Double#NaN}.
	 * If the segment and the plane are intersecting, two cases:<ol>
	 * <li>the segment is coplanar to the plane, then this function replies
	 * {@link Double#POSITIVE_INFINITY}.</li>
	 * <li>the segment and the plane have a single point of intersection,
	 * then this function replies the factor of the line's equation that
	 * permits to retrieve the intersection point from the segment definition.
	 * </ol>
	 * 
	 * @param a first component of the plane equation.
	 * @param b second component of the plane equation.
	 * @param c third component of the plane equation.
	 * @param d fourth component of the plane equation.
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first axis of the oriented box.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second axis of the oriented box.
	 * @return the factor that permits to compute the intersection point,
	 * {@link Double#NaN} when no intersection, {@link Double#POSITIVE_INFINITY}
	 * when an infinite number of intersection points.
	 */
	@Pure
	static double calculatesPlaneSegmentIntersectionFactor(
			double a, double b, double c, double d,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2) {
		final double denom = a * (sx2 - sx1) + b * (sy2 - sy1) + c * (sz2 - sz1);
		if (MathUtil.isEpsilonZero(denom, GeomConstants.UNIT_VECTOR_EPSILON)) {
			// Segment and plane are parallel
			// Compute the distance between a point of the segment and the plane.
			final double dist = a * sx1 + b * sy1 + c * sz1 + d;
			if (MathUtil.isEpsilonZero(dist, GeomConstants.UNIT_VECTOR_EPSILON)) {
				return Double.POSITIVE_INFINITY;
			}
		} else {
			final double factor = (-a * sx1 - b * sy1 - c * sz1 - d) / denom;
			if (factor >= 0. && factor <= 1.) {
				return factor;
			}
		}
		return Double.NaN;
	}

	/** Replies the intersection point of the given segment when it is intersecting the plane.
	 *
	 * <p>If the segment and the plane are not intersecting, this
	 * function replies {@code false}. If the plane and thge segment are coplanar, the first point of the segment is replied.
	 * 
	 * @param a first component of the plane equation.
	 * @param b second component of the plane equation.
	 * @param c third component of the plane equation.
	 * @param d fourth component of the plane equation.
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first axis of the oriented box.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second axis of the oriented box.
	 * @param result the receiver of the intersection coordinates if an intersection exists.
	 * @return {@code true} if an intersection exists; otherwise {@code false}.
	 */
	@Pure
	static boolean calculatesPlaneSegmentIntersection(
			double a, double b, double c, double d,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2,
			Point3D<?, ?, ?> result) {
		final double factor = calculatesPlaneSegmentIntersectionFactor(a, b, c, d, sx1, sy1, sz1, sx2, sy2, sz2);
		if (Double.isNaN(factor)) {
			return false;
		}
		if (result != null) {
			if (Double.isInfinite(factor)) {
				result.set(sx1, sy1, sz1);
			} else {
				result.set(
						sx1 + factor * (sx2 - sx1),
						sy1 + factor * (sy2 - sy1),
						sz1 + factor * (sz2 - sz1));
			}
		}
		return true;
	}

	/** Calculates the line that corresponds to the intersection between two general planes.
	 * 
	 * @param a1 the a coordinate of the first plane.
	 * @param b1 the b coordinate of the first plane.
	 * @param c1 the c coordinate of the first plane.
	 * @param d1 the d coordinate of the first plane.
	 * @param a2 the a coordinate of the other plane.
	 * @param b2 the b coordinate of the other plane.
	 * @param c2 the c coordinate of the other plane.
	 * @param d2 the d coordinate of the other plane.
	 * @param result the receiver of the intersection values.
	 * @return {@code true} if there is a line intersection, {@code false} if there is no intersection or
	 *     the planes are coplanar.
	 */
	static boolean calculatesPlanePlaneIntersection(
			double a1, double b1, double c1, double d1,
			double a2, double b2, double c2, double d2,
			Segment3afp<?, ?, ?, ?, ?, ?, ?> result) {
		return calculatesPlanePlaneIntersection(
				a1, b1, c1, d1,
				a2, b2, c2, d2,
				(px, py, pz, vx, vy, vz) -> {
					if (result != null) {
						result.set(px, py, pz, px + vx, py + vy, pz + vz);
					}
				});
	}

	/** Calculates the line that corresponds to the intersection between the two planes.
	 * 
	 * @param a1 the a coordinate of the first plane.
	 * @param b1 the b coordinate of the first plane.
	 * @param c1 the c coordinate of the first plane.
	 * @param d1 the d coordinate of the first plane.
	 * @param a2 the a coordinate of the other plane.
	 * @param b2 the b coordinate of the other plane.
	 * @param c2 the c coordinate of the other plane.
	 * @param d2 the d coordinate of the other plane.
	 * @param firstSegmentPoint the receiver of the coordinates of the first point of the intersection line.
	 * @param secondSegmentPoint the receiver of the coordinates of the second point of the intersection line.
	 * @return {@code true} if there is a line intersection, {@code false} if there is no intersection or
	 *     the planes are coplanar.
	 */
	static boolean calculatesPlanePlaneIntersection(
			double a1, double b1, double c1, double d1,
			double a2, double b2, double c2, double d2,
			Point3D<?, ?, ?> firstSegmentPoint,
			Point3D<?, ?, ?> secondSegmentPoint) {
		return calculatesPlanePlaneIntersection(
				a1, b1, c1, d1,
				a2, b2, c2, d2,
				(px, py, pz, vx, vy, vz) -> {
					if (firstSegmentPoint != null) {
						firstSegmentPoint.set(px, py, pz);
					}
					if (secondSegmentPoint != null) {
						secondSegmentPoint.set(px + vx, py + vy, pz + vz);
					}
				});
	}

	/** Calculates the line that corresponds to the intersection between the two planes.
	 * 
	 * @param a1 the a coordinate of the first plane.
	 * @param b1 the b coordinate of the first plane.
	 * @param c1 the c coordinate of the first plane.
	 * @param d1 the d coordinate of the first plane.
	 * @param a2 the a coordinate of the other plane.
	 * @param b2 the b coordinate of the other plane.
	 * @param c2 the c coordinate of the other plane.
	 * @param d2 the d coordinate of the other plane.
	 * @param intersectionPoint the receiver of the coordinates of a point of the intersection line.
	 * @param intersectionDirection the receiver of the coordinates of the direction vector of the intersection line.
	 * @return {@code true} if there is a line intersection, {@code false} if there is no intersection or
	 *     the planes are coplanar.
	 */
	static boolean calculatesPlanePlaneIntersection(
			double a1, double b1, double c1, double d1,
			double a2, double b2, double c2, double d2,
			Point3D<?, ?, ?> intersectionPoint,
			Vector3D<?, ?, ?> intersectionDirection) {
		return calculatesPlanePlaneIntersection(
				a1, b1, c1, d1,
				a2, b2, c2, d2,
				(px, py, pz, vx, vy, vz) -> {
					if (intersectionPoint != null) {
						intersectionPoint.set(px, py, pz);
					}
					if (intersectionDirection != null) {
						intersectionDirection.set(vx, vy, vz);
					}
				});
	}

	/** Calculates the line that corresponds to the intersection between the two planes.
	 * 
	 * @param a1 the a coordinate of the first plane.
	 * @param b1 the b coordinate of the first plane.
	 * @param c1 the c coordinate of the first plane.
	 * @param d1 the d coordinate of the first plane.
	 * @param a2 the a coordinate of the other plane.
	 * @param b2 the b coordinate of the other plane.
	 * @param c2 the c coordinate of the other plane.
	 * @param d2 the d coordinate of the other plane.
	 * @param coordinateReceiver the receiver of the segment coordinate. The arguments are
	 *     the x, y and z coordinates of the first point of the segment, and the
	 *     x, y and z coordinates of the second point of the segment.
	 * @return {@code true} if there is a line intersection, {@code false} if there is no intersection or
	 *     the planes are coplanar.
	 */
	static boolean calculatesPlanePlaneIntersection(
			double a1, double b1, double c1, double d1,
			double a2, double b2, double c2, double d2,
			PointVector3DReceiver coordinateReceiver) {
		assert coordinateReceiver != null : AssertMessages.notNullParameter(6);

		final Vector3D<?, ?, ?> u = new InnerComputationVector3afp();
		Vector3D.crossProduct(a1, b1, c1, a2, b2, c2, u);
		final double ulengthSq = u.getLengthSquared();
		if (MathUtil.isEpsilonZero(ulengthSq)) {
			// planes are parallel
			return false;
		}

		// u is both perpendicular to the two normals,
		// so it is parallel to both planes,
		// i.e., it is the direction of the intersection line
		final double ux = u.getX();
		final double uy = u.getY();
		final double uz = u.getZ();

		// Intersection point is:
		// ((d2 n1 - d1 n2) x (n1 x n2))
		// -----------------------------
		//       | n1 x n2 | ^2
		//
		// same as:
		//
		// ((d2 n1 - d1 n2) x u)
		// ---------------------
		//      ulengthSq

		final double vx = d2 * a1 - d1 * a2;
		final double vy = d2 * b1 - d1 * b2;
		final double vz = d2 * c1 - d1 * c2;
		final Vector3D<?, ?, ?> i = new InnerComputationVector3afp();
		Vector3D.crossProduct(vx, vy, vz, ux, uy, uz, i);
		final double f = 1. / ulengthSq;
		i.scale(f);

		coordinateReceiver.set(
				i.getX(), i.getY(), i.getZ(),
				ux, uy, uz);

		return true;
	}

	/**
	 * Classifies a sphere with respect to the plane.
	 * 
	 * @param sphere the sphere to classify.
	 * @return the classification
	 */
	@Pure
	default PlaneClassification classifies(Sphere3afp<?, ?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		return classifies(sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius());
	}

	/**
	 * Classifies an aligned box with respect to the plane.
	 * 
	 * @param box the box to classify.
	 * @return the classification
	 */
	@Pure
	default PlaneClassification classifies(Box3afp<?, ?, ?, ?, ?, ?, ?> box) {
		assert box != null : AssertMessages.notNullParameter();
		return classifies(box.getMinX(), box.getMinY(), box.getMinZ(), box.getMaxX(), box.getMaxY(), box.getMaxZ());
	}

	/**
	 * Classifies a segment  with respect to the plane.
	 * 
	 * @param segment the segment to classify.
	 * @return the classification
	 */
	@Pure
	default PlaneClassification classifies(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		return classifiesPlaneSegment(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC(), getEquationComponentD(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2());
	}

	/**
	 * Replies if the given sphere is intersecting the plane.
	 * 
	 * @param sphere the sphere to test.
	 * @return {@code true} if intersection, otherwise {@code false}
	 */
	@Pure
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?, ?> sphere) {
		return classifies(sphere) == PlaneClassification.COINCIDENT;
	}

	/**
	 * Replies if the given sphere is intersecting the plane.
	 * 
	 * @param x x coordinate of the sphere center.
	 * @param y y coordinate of the sphere center.
	 * @param z z coordinate of the sphere center.
	 * @param radius the radius of the sphere.
	 * @return {@code true} if intersection, otherwise {@code false}
	 */
	@Pure
	default boolean intersects(double x, double y, double z, double radius) {
		return classifies(x, y, z, radius) == PlaneClassification.COINCIDENT;
	}

	/**
	 * Replies if the given axis-aligned box is intersecting the plane.
	 * 
	 * @param box the box to test.
	 * @return {@code true} if intersection, otherwise {@code false}
	 */
	@Pure
	default boolean intersects(Box3afp<?, ?, ?, ?, ?, ?, ?> box) {
		return classifies(box) == PlaneClassification.COINCIDENT;
	}

	/**
	 * Replies if the given axis-aligned box is intersecting the plane.
	 * 
	 * @param minx x coordinate of the minimum point of the box.
	 * @param miny y coordinate of the minimum point of the box.
	 * @param minz z coordinate of the minimum point of the box.
	 * @param maxx x coordinate of the maximum point of the box.
	 * @param maxy y coordinate of the maximum point of the box.
	 * @param maxz z coordinate of the maximum point of the box.
	 * @return {@code true} if intersection, otherwise {@code false}
	 */
	@Pure
	default boolean intersects(double minx, double miny, double minz, double maxx, double maxy, double maxz) {
		return classifies(minx, miny, minz, maxx, maxy, maxz) == PlaneClassification.COINCIDENT;
	}

	/**
	 * Replies if the given sphere is intersecting the segment.
	 * If the intersection point between the point and the line that is
	 * colinear to the given segment lies on the segment, then an intersection
	 * exists. Otherwise there is no intersection.
	 * 
	 * @param segment the segment to test.
	 * @return {@code true} if intersection, otherwise {@code false}
	 */
	@Pure
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final double factor = Plane3afp.calculatesPlaneSegmentIntersectionFactor(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC(), getEquationComponentD(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2());
		return !Double.isNaN(factor);
	}

	@Override
	default PlaneClassification classifies(double x, double y, double z) {
		return classifiesPlanePoint(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC(), getEquationComponentD(),
				x, y, z);
	}

	@Override
	default PlaneClassification classifies(double x, double y, double z, double radius) {
		return classifiesPlaneSphere(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC(), getEquationComponentD(),
				x, y, z, radius);
	}

	@Override
	default PlaneClassification classifies(double lx, double ly, double lz, double ux, double uy, double uz) {
		return classifiesPlaneAlignedBox(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC(), getEquationComponentD(),
				lx, ly, lz, ux, uy, uz);
	}

	/** Replies this plane with a Geogebra-compatible form.
	 *
	 * @return the Geogebra representation of the plane.
	 * @since 18.0
	 */
	@Override
	default String toGeogebra() {
		return GeogebraUtil.toPlaneDefinition(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC(), getEquationComponentD());
	}

	@Override
	default void clear() {
		set(1., 0., 0., 0.);
	}

	@Override
	default void negate() {
		set(-getEquationComponentA(), -getEquationComponentB(), -getEquationComponentC(), -getEquationComponentD());
	}

	@Override
	default void absolute() {
		set(	Math.abs(getEquationComponentA()),
				Math.abs(getEquationComponentB()),
				Math.abs(getEquationComponentC()),
				getEquationComponentD());
	}

	@Override
	default void set(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y, double p3z) {
		final InnerComputationVector3afp v = new InnerComputationVector3afp();
		Vector3D.crossProduct(
				p2x - p1x, p2y - p1y, p2z - p1z,
				p3x - p1x, p3y - p1y, p3z - p1z,
				v);
		final double a = v.getX();
		final double b = v.getY();
		final double c = v.getZ();
		final double d = -(a * p1x + b * p1y + c * p1z);
		set(a, b, c, d);
		setPivot(
				(p1x + p2x + p3x) / 3.,
				(p1y + p2y + p3y) / 3.,
				(p1z + p2z + p3z) / 3.);
	}

	/** Set this plane to contain the point and have the two vectors to be form the plane.
	 * 
	 * @param pivot is the point in the plane.
	 * @param vector1 is the first vector that represents a direction of tha plane.
	 * @param vector2 is the second vector that represents a direction of tha plane.
	 */
	default void set(Point3D<?, ?, ?> pivot, Vector3D<?, ?, ?> vector1, Vector3D<?, ?, ?> vector2) {
		assert pivot != null : AssertMessages.notNullParameter(0);
		assert vector1 != null : AssertMessages.notNullParameter(1);
		assert vector2 != null : AssertMessages.notNullParameter(2);
		final InnerComputationVector3afp v = new InnerComputationVector3afp();
		Vector3D.crossProduct(
				vector1.getX(), vector1.getY(), vector1.getZ(),
				vector2.getX(), vector2.getY(), vector2.getZ(),
				v);
		final double a = v.getX();
		final double b = v.getY();
		final double c = v.getZ();
		final double d = -(a * pivot.getX() + b * pivot.getY() + c * pivot.getZ());
		set(a, b, c, d);
		setPivot(pivot);
	}

	/** Set this plane to contain the point and have the given normal vector.
	 * 
	 * @param pivot is the point in the plane.
	 * @param normal the normal of the plane.
	 */
	default void set(Point3D<?, ?, ?> pivot, Vector3D<?, ?, ?> normal) {
		assert pivot != null : AssertMessages.notNullParameter(0);
		assert normal != null : AssertMessages.notNullParameter(1);
		final double length = normal.getLength();
		final double a = normal.getX() / length;
		final double b = normal.getY() / length;
		final double c = normal.getZ() / length;
		final double d = -(a * pivot.getX() + b * pivot.getY() + c * pivot.getZ());
		set(a, b, c, d);
		setPivot(pivot);
	}

	@Override
	default P getProjection(double x, double y, double z) {
		final P point = getGeomFactory().newPoint();
		calculatesPlanePointProjection(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC(), getEquationComponentD(),
				x, y, z, point);
		return point;
	}

	@Override
	default double getDistanceTo(double x, double y, double z) {
		return calculatesPlanePointDistance(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC(), getEquationComponentD(),
				x, y, z);
	}

	@Override
	default double getDistanceTo(double a, double b, double c, double d) {
		return calculatesPlanePlaneDistance(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC(), getEquationComponentD(),
				a, b, c, d);
	}

	@SuppressWarnings("unchecked")
	@Override
	default S getIntersection(double a, double b, double c, double d) {
		final S segment = (S) getGeomFactory().newSegment();
		if (calculatesPlanePlaneIntersection(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC(), getEquationComponentD(),
				a, b, c, d,
				segment)) {
			return segment;
		}
		return null;
	}

	/** Replies the intersection between this plane and the specified line.
	 * 
	 * @param line is used to compute the intersection.
	 * @return the intersection point or {@code null}
	 */
	@Pure
	default P getIntersection(Segment3afp<?, ?, ?, ?, ?, ?, ?> line) {
		final P point = getGeomFactory().newPoint();
		if (calculatesPlaneSegmentIntersection(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC(), getEquationComponentD(),
				line.getX1(), line.getY1(), line.getZ1(),
				line.getX2(), line.getY2(), line.getZ2(),
				point)) {
			return point;
		}
		return null;
	}

	@Override
	default void rotate(Quaternion<?, ?, ?> rotation, Point3D<?, ?, ?> pivot) {
		assert rotation != null : AssertMessages.notNullParameter(0);

		final Point3D<?, ?, ?> reference = getPivot();
		final Point3D<?, ?, ?> realPivot = pivot == null ? reference : pivot;

		final Transform3D m = new Transform3D();
		m.makeRotationMatrix(rotation);

		final Vector3D<?, ?, ?> newNormal = new InnerComputationVector3afp(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC());
		m.transform(newNormal);

		final Vector3D<?, ?, ?> newReference = new InnerComputationVector3afp(
				reference.getX() - realPivot.getX(),
				reference.getY() - realPivot.getY(),
				reference.getZ() - realPivot.getZ());
		m.transform(newReference);
		
		final double nrx = realPivot.getX() + newReference.getX();
		final double nry = realPivot.getY() + newReference.getY();
		final double nrz = realPivot.getZ() + newReference.getZ();

		final double d = -(newNormal.getX() * nrx + newNormal.getY() * nry + newNormal.getZ() * nrz);

		set(newNormal.getX(), newNormal.getY(), newNormal.getZ(), d);
	}

	@Override
	default void transform(Transform3D transform, Point3D<?, ?, ?> pivot) {
		assert transform != null : AssertMessages.notNullParameter(0);

		final Point3D<?, ?, ?> reference = getPivot();
		final Point3D<?, ?, ?> realPivot = pivot == null ? reference : pivot;

		final Vector3D<?, ?, ?> newNormal = new InnerComputationVector3afp(
				getEquationComponentA(), getEquationComponentB(), getEquationComponentC());
		transform.transform(newNormal);

		final Point3D<?, ?, ?> newReference = new InnerComputationPoint3afp(
				reference.getX() - realPivot.getX(),
				reference.getY() - realPivot.getY(),
				reference.getZ() - realPivot.getZ());
		transform.transform(newReference);
		
		final double nrx = realPivot.getX() + newReference.getX();
		final double nry = realPivot.getY() + newReference.getY();
		final double nrz = realPivot.getZ() + newReference.getZ();

		final double d = -(newNormal.getX() * nrx + newNormal.getY() * nry + newNormal.getZ() * nrz);

		set(newNormal.getX(), newNormal.getY(), newNormal.getZ(), d);
	}

	/** Replies if the given sphere is intersecting the plane.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param sphere the sphere to test.
	 * @return {@code true} if the elements are intersecting. Otherwise, {@code false}.
	 * @see #intersects(Sphere3afp)
	 */
	@Pure
	@XtextOperator("&&")
	@Inline("intersects($1)")
	default boolean operator_and(Sphere3afp<?, ?, ?, ?, ?, ?, ?> sphere) {
		return intersects(sphere);
	}

	/** Replies if the given segment is intersecting the plane.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param segment the segment to test.
	 * @return {@code true} if the elements are intersecting. Otherwise, {@code false}.
	 * @see #intersects(Segment3afp)
	 */
	@Pure
	@XtextOperator("&&")
	@Inline("intersects($1)")
	default boolean operator_and(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		return intersects(segment);
	}

	/** Replies if the given axis-aligned box is intersecting the plane.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param box the box to test.
	 * @return {@code true} if the elements are intersecting. Otherwise, {@code false}.
	 * @see #intersects(Box3afp)
	 */
	@Pure
	@XtextOperator("&&")
	@Inline("intersects($1)")
	default boolean operator_and(Box3afp<?, ?, ?, ?, ?, ?, ?> box) {
		return intersects(box);
	}

}
