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

import java.io.Serializable;

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.vmutil.annotations.ScalaOperator;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonableObject;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/** This class represents a 3D plane.
 *
 * @param <PT> is the type of the plane.
 * @param <S> is the type of the of the geometric structure that represents the intersection of two planes.
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
public interface Plane3D<PT extends Plane3D<?, ?, P, V, Q>,
		S extends Shape3D<?, ?, ?, P, V, Q, ?>,
		P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>>
	extends PlaneClassifier, Serializable, Cloneable, JsonableObject {

	/** Reset this shape to be equivalent to
	 * an just-created instance of this shape type. 
	 */
	void clear();

	/**
	 * Returns the normal to this plane.
	 * 
	 * @return the normal of the plane.
	 */
	@Pure
	default V getNormal() {
		return getGeomFactory().newVector(getNormalX(), getNormalY(), getNormalZ());
	}

	/**
	 * Returns the x component of the normal to this plane.
	 * 
	 * @return x component of the normal of the plane.
	 */
	@Pure
	double getNormalX();

	/**
	 * Returns the y component of the normal to this plane.
	 * 
	 * @return y component of the normal of the plane.
	 */
	@Pure
	double getNormalY();

	/**
	 * Returns the z component of the normal to this plane.
	 * 
	 * @return z component of the normal of the plane.
	 */
	@Pure
	double getNormalZ();

	/**
	 * Replies the component a of the plane equation.
	 * It is usually the x component of the normal vector.
	 * 
	 * @return the component a of the plane equation.
	 */
	@Pure
	double getEquationComponentA();

	/**
	 * Replies the component b of the plane equation.
	 * It is usually the y component of the normal vector.
	 * 
	 * @return the component b of the plane equation.
	 */
	@Pure
	double getEquationComponentB();

	/**
	 * Replies the component c of the plane equation.
	 * It is usually the z component of the normal vector.
	 * 
	 * @return the component c of the plane equation.
	 */
	@Pure
	double getEquationComponentC();

	/**
	 * Replies the component d of the plane equation.
	 * It is usually equal to the scalar vector colinear to the plane normal vector and that points to the
	 * plane that contains the point {@code (0,0,0)}.
	 * 
	 * @return the component d of the plane equation.
	 */
	@Pure
	double getEquationComponentD();

	/**
	 * Negate the normal of the plane.
	 */
	void negate();

	/**
	 * Make the normal of the plane be absolute, ie. all their
	 * components are positive or nul.
	 */
	void absolute();

	/**
	 * Normalizes this plane (i.e. the vector (a,b,c) becomes unit length).
	 * 
	 * @return this plane
	 */
	PT normalize();

	/** Set the equation of the plane according to the specified plane.
	 * 
	 * @param plane is the plane to copy.
	 */
	default void set(Plane3D<?, ?, ?, ?, ?> plane) {
		assert plane != null : AssertMessages.notNullParameter();
		set(plane.getEquationComponentA(), plane.getEquationComponentB(), plane.getEquationComponentC(), plane.getEquationComponentD());
	}

	/** Set the equation of the plane according to be colinear (if possible)
	 * to the specified plane.
	 * 
	 * @param a the a component of the plane equation.
	 * @param b the b component of the plane equation.
	 * @param c the c component of the plane equation.
	 * @param d the d component of the plane equation.
	 */
	void set(double a, double b, double c, double d);

	/** Set this plane to be coplanar with all the three specified points.
	 * 
	 * @param p1x is the first point on the plane.
	 * @param p1y is the first point on the plane.
	 * @param p1z is the first point on the plane.
	 * @param p2x is the second point on the plane.
	 * @param p2y is the second point on the plane.
	 * @param p2z is the second point on the plane.
	 * @param p3x is the third point on the plane.
	 * @param p3y is the third point on the plane.
	 * @param p3z is the third point on the plane.
	 */
	void set(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y, double p3z);

	/** Set this plane to be coplanar with all the three specified points.
	 * 
	 * @param p1 is the first point.
	 * @param p2 is the second point.
	 * @param p3 is the third point.
	 */
	default void set(Point3D<?, ?, ?> p1, Point3D<?, ?, ?> p2, Point3D<?, ?, ?> p3) {
		assert p1 != null : AssertMessages.notNullParameter(0);
		assert p2 != null : AssertMessages.notNullParameter(1);
		assert p3 != null : AssertMessages.notNullParameter(2);
		set(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ());
	}

	/** Set this plane to contain the point and have the two vectors to be form the plane.
	 * 
	 * @param pivot is the point in the plane.
	 * @param vector1 is the first vector that represents a direction of tha plane.
	 * @param vector2 is the second vector that represents a direction of tha plane.
	 */
	void set(Point3D<?, ?, ?> pivot, Vector3D<?, ?, ?> vector1, Vector3D<?, ?, ?> vector2);

	/** Set this plane to contain the point and have the given normal vector.
	 * 
	 * @param pivot is the point in the plane.
	 * @param normal the normal of the plane.
	 */
	void set(Point3D<?, ?, ?> pivot, Vector3D<?, ?, ?> normal);

	/** Replies a clone of this plane.
	 * 
	 * @return a clone.
	 */
	@Pure
	PT clone();

	/**
	 * Replies the distance from the given point to the plane.
	 *
	 * @param point is the point.
	 * @return the distance from the plane to the point.
	 * It will be positive if the point is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 */
	@Pure
	default double getDistanceTo(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return getDistanceTo(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Replies the distance from the given point to the plane.
	 *
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param z z coordinate of the point.
	 * @return the distance from the plane to the point.
	 * It will be positive if the point is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 */
	@Pure
	double getDistanceTo(double x, double y, double z);

	/**
	 * Replies the distance between this plane and the given parallel plane.
	 *
	 * @param plane the plane.
	 * @return the distance from the plane to the parallel plane.
	 * It will be positive if the plane is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the planes are the same. 
	 * If the result is NaN, the planes aren't colinear.
	 */
	@Pure
	default double getDistanceTo(Plane3D<?, ?, ?, ?, ?> plane) {
		assert plane != null : AssertMessages.notNullParameter();
		return getDistanceTo(plane.getEquationComponentA(), plane.getEquationComponentB(), plane.getEquationComponentC(), plane.getEquationComponentD());
	}

	/**
	 * Replies the distance between this plane and the given parallel plane.
	 *
	 * @param p the plane.
	 * @return the distance from the plane to the parallel plane.
	 * It will be positive if the plane is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the planes are the same. 
	 * If the result is NaN, the planes aren't colinear.
	 */
	@Pure
	double getDistanceTo(double a, double b, double c, double d);

	/** Replies the intersection between this plane and the specified one.
	 * 
	 * @param plane is used to compute the intersection.
	 * @return the intersection segment or {@code null}
	 */
	@Pure
	default S getIntersection(Plane3D<?, ?, ?, ?, ?> plane) {
		assert plane != null : AssertMessages.notNullParameter();
		return getIntersection(plane.getEquationComponentA(), plane.getEquationComponentB(), plane.getEquationComponentC(), plane.getEquationComponentD());
	}

	/** Replies the intersection between this plane and the specified one.
	 * 
	 * @param a the a component of the plane equation.
	 * @param b the b component of the plane equation.
	 * @param c the c component of the plane equation.
	 * @param d the d component of the plane equation.
	 * @return the intersection segment or {@code null}
	 */
	@Pure
	S getIntersection(double a, double b, double c, double d);

	/** Replies the projection of the given point on the plane.
	 * 
	 * @param point the point to project on the plane.
	 * @return the projection point never {@code null}
	 */
	@Pure
	default P getProjection(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return getProjection(point.getX(), point.getY(), point.getZ());
	}

	/** Replies the projection of the given point on the plane.
	 * 
	 * @param x x coordinate of the point to project on the plane.
	 * @param y y coordinate of the point to project on the plane.
	 * @param z z coordinate of the point to project on the plane.
	 * @return the projection point never {@code null}
	 */
	@Pure
	P getProjection(double x, double y, double z);

	/** Set point that lies on the plane and is used a pivot point.
	 * 
	 * @param x x component of the pivot point.
	 * @param y y component of the pivot point.
	 * @param z z component of the pivot point.
	 */
	void setPivot(double x, double y, double z);

	/** Set point that lies on the plane and is used a pivot point.
	 * 
	 * @param point the new pivot point.
	 */
	default void setPivot(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		setPivot(point.getX(), point.getY(), point.getZ());
	}

	/** Replies the point that lies on the plane and is used a pivot point.
	 * 
	 * @return the pivot point.
	 */
	P getPivot();

	/** Translate this plane about the given vector.
	 * 
	 * @param x x component of the translation vector.
	 * @param y y component of the translation vector.
	 * @param z z component of the translation vector.
	 */
	void translate(double x, double y, double z);

	/** Translate this plane about the given vector.
	 * 
	 * @param vector the translation vector.
	 */
	default void translate(Vector3D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		translate(vector.getX(), vector.getY(), vector.getZ());
	}

	/** Translate this plane along the plane normal direction with the given distance.
	 * This function change the {@code d} component of the plane equation: {@code a.x + b.y + c.z + d = 0}.
	 * 
	 * @param distance the positive or negative distance to add to the {@code d} component of the plane equation.
	 * @since 18.0
	 */
	void translate(double distance);

	/** Rotate the plane around the given pivot point.
	 *
	 * @param rotation the rotation to apply.
	 * @param pivot the pivot point. If it is equal to {@code null}, the point replies by {@link #getPivot()} is used.
	 */
	void rotate(Quaternion<?, ?, ?> rotation, Point3D<?, ?, ?> pivot);

	/** Rotate the plane with the given angle around the given axis.
	 * The rotation is done around the pivot point.
	 *
	 * @param axis the rotation axis.
	 * @param angle the rotation angle.
	 */
	default void rotate(Vector3D<?, ?, ?> axis, double angle) {
		assert axis != null : AssertMessages.notNullParameter(0);
		final Quaternion<?, ?, ?> q = getGeomFactory().newQuaternionFromAxisAngle(axis.getX(), axis.getY(), axis.getZ(), angle);
		rotate(q, null);
	}

	/** Rotate the plane with the given angle around the given axis.
	 * The rotation is done around the pivot point.
	 *
	 * @param axisx x coordinate of the rotation axis.
	 * @param axisy y coordinate of the rotation axis.
	 * @param axisz z coordinate of the rotation axis.
	 * @param angle the rotation angle.
	 */
	default void rotate(double axisx, double axisy, double axisz, double angle) {
		final Quaternion<?, ?, ?> q = getGeomFactory().newQuaternionFromAxisAngle(axisx, axisy, axisz, angle);
		rotate(q, null);
	}

	/** Rotate the plane with the given angle around the given axis.
	 * The rotation is done around the pivot point.
	 *
	 * @param axis the rotation axis.
	 * @param angle the rotation angle.
	 * @param pivot the pivot point. If it is equal to {@code null}, the point replies by {@link #getPivot()} is used.
	 */
	default void rotate(Vector3D<?, ?, ?> axis, double angle, Point3D<?, ?, ?> pivot) {
		assert axis != null : AssertMessages.notNullParameter(0);
		final Quaternion<?, ?, ?> q = getGeomFactory().newQuaternionFromAxisAngle(axis.getX(), axis.getY(), axis.getZ(), angle);
		rotate(q, pivot);
	}

	/** Rotate the plane around the pivot point.
	 *
	 * @param rotation the rotation to apply.
	 */
	default void rotate(Quaternion<?, ?, ?> rotation) {
		rotate(rotation, null);
	}

	/** Apply the given transformation matrix on the plane.
	 * The pivot point used for transforming this plane with
	 * the given matrix is given by {@link #getPivot()}.
	 *
	 * @param transform the transformation matrix.
	 */
	default void transform(Transform3D transform) {
		transform(transform, null);
	}

	/** Apply the given transformation matrix on the plane.
	 *
	 * @param transform the transformation matrix.
	 * @param pivot the pivot point. If it is equal to {@code null}, the point replies by {@link #getPivot()} is used.
	 */
	void transform(Transform3D transform, Point3D<?, ?, ?> pivot);

	/** Replies the geometry factory associated to this plane.
	 *
	 * @return the factory.
	 */
	@Pure
	GeomFactory3D<V, P, Q> getGeomFactory();

	/** Replies this plane with a Geogebra-compatible form.
	 *
	 * @return the Geogebra representation of the plane.
	 * @since 18.0
	 */
	@Pure
	String toGeogebra();

	/** Create a new plane by translating this plane of the given vector: {@code this + v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @return the transformed plane.
	 * @see #translate(Vector3D)
	 */
	@Pure
	@XtextOperator("+")
	default PT operator_plus(Vector3D<?, ?, ?> v) {
		final PT clone = clone();
		clone.translate(v);
		return clone;
	}

	/** Create a new plane by translating this plane of the given distance along the plane normal
	 * vector: {@code this + distance N}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param distance the positive or negative distance to add to the {@code d} component of the plane equation.
	 * @return the transformed plane.
	 * @see #translate(double)
	 */
	@Pure
	@XtextOperator("+")
	default PT operator_plus(double distance) {
		final PT clone = clone();
		clone.translate(distance);
		return clone;
	}

	/** Create a new plane by translating this plane of the given vector: {@code this + v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param v the vector
	 * @return the transformed plane.
	 * @see #translate(Vector3D)
	 */
	@Pure
	@ScalaOperator("+")
	default PT $plus(Vector3D<?, ?, ?> v) {
		return operator_plus(v);
	}

	/** Create a new plane by translating this plane of the given distance along the plane normal
	 * vector: {@code this + distance N}
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param distance the positive or negative distance to add to the {@code d} component of the plane equation.
	 * @return the transformed plane.
	 * @see #translate(double)
	 */
	@Pure
	@ScalaOperator("+")
	default PT $plus(double distance) {
		final PT clone = clone();
		clone.translate(distance);
		return clone;
	}

	/** Create a new plane by translating this plane of the given vector: {@code this - v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @return the transformed plane.
	 * @see #translate(Vector3D)
	 */
	@Pure
	@XtextOperator("-")
	default PT operator_minus(Vector3D<?, ?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		final PT clone = clone();
		clone.translate(-v.getX(), -v.getY(), v.getZ());
		return clone;
	}

	/** Create a new plane by translating this plane of the given distance along the plane normal
	 * vector: {@code this - distance N}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param distance the positive or negative distance to subtract to the {@code d} component of the plane equation.
	 * @return the transformed plane.
	 * @see #translate(double)
	 */
	@Pure
	@XtextOperator("-")
	default PT operator_minus(double distance) {
		final PT clone = clone();
		clone.translate(-distance);
		return clone;
	}

	/** Create a new plane by translating this plane of the given vector: {@code this - v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param v the vector
	 * @return the transformed plane.
	 * @see #translate(Vector3D)
	 */
	@Pure
	@ScalaOperator("-")
	default PT $minus(Vector3D<?, ?, ?> v) {
		return operator_minus(v);
	}

	/** Create a new plane by translating this plane of the given distance along the plane normal
	 * vector: {@code this - distance N}
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param distance the positive or negative distance to subtract to the {@code d} component of the plane equation.
	 * @return the transformed plane.
	 * @see #translate(double)
	 */
	@Pure
	@ScalaOperator("-")
	default PT $minus(double distance) {
		return operator_minus(distance);
	}

	/** Translate this plane by adding the given vector: {@code this += v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @see #translate(Vector3D)
	 */
	@XtextOperator("+=")
	@Inline("translate($1)")
	default void operator_add(Vector3D<?, ?, ?> v) {
		translate(v);
	}

	/** Translate this plane by adding the given distance along the plane normal vector: {@code this += distance * N}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param distance the positive or negative distance to add to the {@code d} component of the plane equation.
	 * @see #translate(Vector3D)
	 */
	@XtextOperator("+=")
	@Inline("translate($1)")
	default void operator_add(double distance) {
		translate(distance);
	}

	/** Translate this plane by adding the given vector: {@code this -= v}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @see #translate(Vector3D)
	 */
	@XtextOperator("-=")
	default void operator_remove(Vector3D<?, ?, ?> v) {
		translate(-v.getX(), -v.getY(), -v.getZ());
	}

	/** Translate this plane by adding the given distance along the plane normal vector: {@code this += -distance * N}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param distance the positive or negative distance to subtract to the {@code d} component of the plane equation.
	 * @see #translate(Vector3D)
	 */
	@XtextOperator("-=")
	default void operator_remove(double distance) {
		translate(-distance);
	}

	/** Create a new plane by applying the given rotation: {@code this * q}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param q the rotation quaternion
	 * @return the transformed shape.
	 * @see #rotate(Quaternion)
	 */
	@Pure
	@XtextOperator("*")
	default PT operator_multiply(Quaternion<?, ?, ?> q) {
		final PT clone = clone();
		clone.rotate(q);
		return clone;
	}

	/** Create a new plane by applying the given rotation: {@code this * q}
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param q the rotation quaternion
	 * @return the transformed shape.
	 * @see #rotate(Quaternion)
	 */
	@Pure
	@ScalaOperator("*")
	default PT $times(Quaternion<?, ?, ?> q) {
		return operator_multiply(q);
	}

	/** Create a new plane by applying the given transformation: {@code this * t}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param t the 3D transformation matrix to apply to the plane.
	 * @return the transformed shape.
	 * @see #transform(Transform3D)
	 */
	@Pure
	@XtextOperator("*")
	default PT operator_multiply(Transform3D t) {
		final PT clone = clone();
		clone.transform(t);
		return clone;
	}

	/** Create a new plane by applying the given transformation: {@code this * t}
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @param t the 3D transformation matrix to apply to the plane.
	 * @return the transformed shape.
	 * @see #transform(Transform3D)
	 */
	@Pure
	@ScalaOperator("*")
	default PT $times(Transform3D t) {
		return operator_multiply(t);
	}

	/** Negation of this plane: {@code -this}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @return the result.
	 * @see #negate()
	 */
	@Pure
	@XtextOperator("(-)")
	default PT operator_minus() {
		final PT clone = clone();
		clone.negate();
		return clone;
	}

	/** Negation of this plane: {@code -this}
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * @return the result.
	 * @see #negate()
	 */
	@Pure
	@ScalaOperator("(-)")
	default PT $minus() {
		return operator_minus();
	}

	/** Replies if the given point is inside the plane: {@code this && point}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param point the point to test.
	 * @return {@code true} if the point is inside the plane. Otherwise, {@code false}.
	 * @see #intersects(Point3D)
	 */
	@Pure
	@XtextOperator("&&")
	@Inline("intersects($1)")
	default boolean operator_and(Point3D<?, ?, ?> point) {
		return intersects(point);
	}

	/** Replies if the given plane has an intersection with this plane: {@code this && plane}
	 *
	 * <p>You must use the intersection functions with a specific parameter type in place of
	 * this general function. Indeed, the implementation of this function is unefficient due
	 * to the tests against the types of the given shape, and the cast operators.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param shape the shape to test.
	 * @return {@code true} if the shapes are intersecting. Otherwise, {@code false}.
	 * @see #intersects(Shape3D)
	 */
	@Pure
	@Unefficient
	@XtextOperator("&&")
	default boolean operator_and(Plane3D<?, ?, ?, ?, ?> plane) {
		return intersects(plane);
	}

	/** Replies if this plane and the given plane are equal: {@code this == plane}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param plane the plane to test.
	 * @return test result.
	 * @see #equals(Object)
	 */
	@Pure
	@XtextOperator("==")
	@Inline("equals($1)")
	default boolean operator_equals(Plane3D<?, ?, ?, ?, ?> plane) {
		return equals(plane);
	}

	/** Replies if this plane and the given plane are different: {@code this != plane}
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param plane the plane to test.
	 * @return test result.
	 * @see #equals(Object)
	 */
	@Pure
	@XtextOperator("!=")
	default boolean operator_notEquals(Plane3D<?, ?, ?, ?, ?> plane) {
		return !equals(plane);
	}

	/** Replies if the distance between this plane and the given point: {@code this .. point}.
	 * 
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param point the point to test.
	 * @return the distance from the plane to the point.
	 * It will be positive if the point is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 * @see #getDistanceTo(Point3D)
	 */
	@Pure
	@XtextOperator("..")
	@Inline("getDistanceTo($1)")
	default double operator_upTo(Point3D<?, ?, ?> point) {
		return getDistanceTo(point);
	}

	/** Replies if the distance between this plane and the given plane: {@code this .. plane}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param plane the plane to test.
	 * @return the distance from the plane to the point.
	 * It will be positive if the point is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 * @see #getDistanceTo(Plane3D)
	 */
	@Pure
	@XtextOperator("..")
	@Inline("getDistanceTo($1)")
	default double operator_upTo(Plane3D<?, ?, ?, ?, ?> plane) {
		return getDistanceTo(plane);
	}

}
