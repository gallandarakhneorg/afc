/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.base.tests;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.function.Supplier;

import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationVector3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Tuple3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.base.matrix.Matrix2d;
import org.arakhne.afc.math.geometry.base.matrix.Matrix3d;
import org.arakhne.afc.math.geometry.base.matrix.Matrix4d;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.opentest4j.TestAbortedException;

/** Root utility class for tests.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public abstract class AbstractMathTestCase extends AbstractTestCase {
	
	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 */
	public void assertEpsilonEquals(Tuple2D<?> expected, Tuple2D<?> actual) {
		assertEpsilonEquals(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 */
	public void assertNotEpsilonEquals(Tuple2D<?> expected, Tuple2D<?> actual) {
		assertNotEpsilonEquals(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 17.0
	 */
	public void assertEpsilonEquals(Tuple2D<?> expected, Tuple2D<?> actual, String message) {
		assertEpsilonEquals(expected, actual, () -> message);
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 17.0
	 */
	public void assertEpsilonEquals(Tuple2D<?> expected, Tuple2D<?> actual, Supplier<String> message) {
		if (!isEpsilonEquals(expected.getX(), actual.getX())) {
			fail(formatFailMessage(message, "not same x value", expected, actual)); //$NON-NLS-1$
		}
		if (!isEpsilonEquals(expected.getY(), actual.getY())) {
			fail(formatFailMessage(message, "not same y value", expected, actual));  //$NON-NLS-1$
		}
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 17.0
	 */
	public void assertNotEpsilonEquals(Tuple2D<?> expected, Tuple2D<?> actual, String message) {
		assertNotEpsilonEquals(expected, actual, () -> message);
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 17.0
	 */
	public void assertNotEpsilonEquals(Tuple2D<?> expected, Tuple2D<?> actual, Supplier<String> message) {
		if (isEpsilonEquals(expected.getX(), actual.getX(), false)) {
			fail(formatFailMessage(message, "not same x value", expected, actual)); //$NON-NLS-1$
		}
		if (isEpsilonEquals(expected.getY(), actual.getY(), false)) {
			fail(formatFailMessage(message, "not same y value", expected, actual)); //$NON-NLS-1$
		}
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 */
	public void assertEpsilonEquals(Tuple3D<?> expected, Tuple3D<?> actual) {
		assertEpsilonEquals(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 */
	public void assertNotEpsilonEquals(Tuple3D<?> expected, Tuple3D<?> actual) {
		assertNotEpsilonEquals(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 17.0
	 */
	public void assertEpsilonEquals(Tuple3D<?> expected, Tuple3D<?> actual, String message) {
		assertEpsilonEquals(expected, actual, () -> message);
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 17.0
	 */
	public void assertEpsilonEquals(Tuple3D<?> expected, Tuple3D<?> actual, Supplier<String> message) {
		if (!isEpsilonEquals(expected.getX(), actual.getX())) {
			failCompare(
					formatFailMessage(message, "not same x value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getY(), actual.getY())) {
			failCompare(
					formatFailMessage(message, "not same y value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getZ(), actual.getZ())) {
			failCompare(
					formatFailMessage(message, "not same z value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
	}

	/** Replies if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @return the test result.
	 */
	public boolean isEpsilonEquals(Tuple3D<?> expected, Tuple3D<?> actual) {
		return isEpsilonEquals(expected.getX(), actual.getX())
				&& isEpsilonEquals(expected.getY(), actual.getY())
				&& isEpsilonEquals(expected.getZ(), actual.getZ());
	}

	/** Test if the actual vector is colinear to the expected vector with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @since 18.0
	 */
	public void assertEpsilonColinear(Vector3D<?, ?, ?> expected, Vector3D<?, ?, ?> actual) {
		assertEpsilonColinear(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual vector is colinear to the expected vector with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 18.0
	 */
	public void assertEpsilonColinear(Vector3D<?, ?, ?> expected, Vector3D<?, ?, ?> actual, String message) {
		assertEpsilonColinear(expected, actual, () -> message);
	}

	/** Test if the actual vector is colinear to the expected vector with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 18.0
	 */
	public void assertEpsilonColinear(Vector3D<?, ?, ?> expected, Vector3D<?, ?, ?> actual, Supplier<String> message) {
		final Vector3D<?, ?, ?> cross = expected.cross(actual);
		if (!isEpsilonEquals(cross.getLengthSquared(), 0.)) {
			failCompare(
					formatFailMessage(message, "not colinear vectors", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
	}
	
	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 17.0
	 */
	public void assertNotEpsilonEquals(Tuple3D<?> expected, Tuple3D<?> actual, String message) {
		assertNotEpsilonEquals(expected, actual, () -> message);
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 17.0
	 */
	public void assertNotEpsilonEquals(Tuple3D<?> expected, Tuple3D<?> actual, Supplier<String> message) {
		if (isEpsilonEquals(expected.getX(), actual.getX(), false)) {
			failCompare(
					formatFailMessage(message, "not same x value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getY(), actual.getY(), false)) {
			failCompare(
					formatFailMessage(message, "not same y value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getZ(), actual.getZ(), false)) {
			failCompare(
					formatFailMessage(message, "not same z value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 */
	public void assertEpsilonEquals(Quaternion expected, Quaternion actual) {
		assertEpsilonEquals(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon and independently of the sign.
	 * So that, {@code expected == actual} or {@code -expected == actual}.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @since 18.0
	 */
	public void assertEpsilonEqualsAbs(Quaternion expected, Quaternion actual) {
		assertEpsilonEqualsAbs(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 */
	public void assertNotEpsilonEquals(Quaternion expected, Quaternion actual) {
		assertNotEpsilonEquals(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 17.0
	 */
	public void assertEpsilonEquals(Quaternion expected, Quaternion actual, String message) {
		assertEpsilonEquals(expected, actual, () -> message);
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 17.0
	 */
	public void assertEpsilonEquals(Quaternion expected, Quaternion actual, Supplier<String> message) {
		if (!isEpsilonEquals(expected.getX(), actual.getX())) {
			failCompare(
					formatFailMessage(message, "not same x value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getY(), actual.getY())) {
			failCompare(
					formatFailMessage(message, "not same y value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getZ(), actual.getZ())) {
			failCompare(
					formatFailMessage(message, "not same z value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getW(), actual.getW())) {
			failCompare(
					formatFailMessage(message, "not same w value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon, independently of the sign.
	 * So that, {@code expected == actual} or {@code -expected == actual}.
	 * 
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 18.0
	 */
	public void assertEpsilonEqualsAbs(Quaternion expected, Quaternion actual, Supplier<String> message) {
		final boolean sx1 = isEpsilonEquals(expected.getX(), actual.getX());
		final boolean sy1 = isEpsilonEquals(expected.getY(), actual.getY());
		final boolean sz1 = isEpsilonEquals(expected.getZ(), actual.getZ());
		final boolean sw1 = isEpsilonEquals(expected.getW(), actual.getW());
		final boolean success1 = sx1 && sy1 && sz1 && sw1;
		
		final boolean sx2 = isEpsilonEquals(-expected.getX(), actual.getX());
		final boolean sy2 = isEpsilonEquals(-expected.getY(), actual.getY());
		final boolean sz2 = isEpsilonEquals(-expected.getZ(), actual.getZ());
		final boolean sw2 = isEpsilonEquals(-expected.getW(), actual.getW());
		final boolean success2 = sx2 && sy2 && sz2 && sw2;

		if (!success1 && !success2) {
			failCompare(
					formatFailMessage(message, "not same abs value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message the error message.
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @since 17.0
	 */
	public void assertNotEpsilonEquals(Quaternion expected, Quaternion actual, String message) {
		assertNotEpsilonEquals(expected, actual, () -> message);
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message the error message.
	 * @param expected the expected value.
	 * @param actual the actual value.
	 * @since 17.0
	 */
	public void assertNotEpsilonEquals(Quaternion expected, Quaternion actual, Supplier<String> message) {
		if (isEpsilonEquals(expected.getX(), actual.getX(), false)) {
			failCompare(
					formatFailMessage(message, "not same x value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getY(), actual.getY(), false)) {
			failCompare(
					formatFailMessage(message, "not same y value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getZ(), actual.getZ(), false)) {
			failCompare(
					formatFailMessage(message, "not same z value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getW(), actual.getW(), false)) {
			failCompare(
					formatFailMessage(message, "not same w value", expected, actual),  //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
	}
	
	/** Create a random point.
	 *
	 * @return the random point.
	 * @since 17.0
	 */
	public Point2D randomPoint2d() {
		return new InnerComputationPoint2D(
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500);
	}

	/** Create a random vector.
	 *
	 * @return the random vector.
	 * @since 17.0
	 */
	public Vector2D randomVector2d() {
		return new InnerComputationVector2D(
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500);
	}
	
	/** Create a random point.
	 *
	 * @return the random point.
	 * @since 17.0
	 */
	public Point3D randomPoint3d() {
		return new InnerComputationPoint3D(
		        getRandom().nextDouble() * 1000 - 500,
		        getRandom().nextDouble() * 1000 - 500,
		        getRandom().nextDouble() * 1000 - 500);
	}

	/** Create a random vector.
	 *
	 * @return the random vector.
	 */
	public Vector3D randomVector3d() {
	    return new InnerComputationVector3D(
                getRandom().nextDouble() * 1000 - 500,
                getRandom().nextDouble() * 1000 - 500,
                getRandom().nextDouble() * 1000 - 500);
	}

	/** Create a random matrix.
	 *
	 * @return the random matrix.
	 * @since 17.0
	 */
	public Matrix2d randomMatrix2d() {
		return new Matrix2d(
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500);
	}
	
	
	/** Create a random matrix.
	 *
	 * @return the random matrix.
	 * @since 17.0
	 */
	public Matrix3d randomMatrix3d() {
		return new Matrix3d(
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500);
	}
	
	
	/** Create a random matrix.
	 *
	 * @return the random matrix.
	 * @since 17.0
	 */
	public Matrix4d randomMatrix4d() {
		return new Matrix4d(
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500,
				getRandom().nextDouble() * 1000 - 500);
	}

	/** Test if the actual vector is equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 */
	public void assertFpVectorEquals(double x, double y, Vector2D<?, ?> v) {
		double dx = x - v.getX();
		double dy = y - v.getY();
		double distSq = dx * dx + dy * dy;
		if (!isEpsilonEquals(distSq, 0.)) {
			final String str1 = "(" + x + ", " + y + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			final String str2 = "(" + v.getX() + ", " + v.getY() + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			failCompare(formatFailMessage(NO_MESSAGE, "not same vector", str1, str2),  //$NON-NLS-1$
					str1, str2);   
		}
	}

	/** Test if the actual vector is equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 */
	public void assertIntVectorEquals(int x, int y, Vector2D<?, ?> v) {
		if (x != v.ix() || y != v.iy()) {
			final String str1 = "(" + x + ", " + y + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			final String str2 = "(" + v.ix() + ", " + v.iy() + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			failCompare(formatFailMessage(NO_MESSAGE, "not same vector", str1, str2),  //$NON-NLS-1$
					str1, str2);   
		}
	}

	/** Test if the actual vector is not equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 */
	public void assertFpVectorNotEquals(double x, double y, Vector2D<?, ?> v) {
		double dx = x - v.getX();
		double dy = y - v.getY();
		double distSq = dx * dx + dy * dy;
		if (isEpsilonEquals(distSq, 0.)) {
			final String str1 = "(" + x + ", " + y + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			final String str2 = "(" + v.getX() + ", " + v.getY() + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			failCompare(formatFailMessage(NO_MESSAGE, "same vector", str1, str2),  //$NON-NLS-1$
					str1, str2);   
		}
	}

	/** Test if the actual vector is not equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 */
	public void assertIntVectorNotEquals(int x, int y, Vector2D<?, ?> v) {
		if (x == v.ix() && y == v.iy()) {
			final String str1 = "(" + x + ", " + y + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			final String str2 = "(" + v.ix() + ", " + v.iy() + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			failCompare(formatFailMessage(NO_MESSAGE, "same vector", str1, str2),  //$NON-NLS-1$
					str1, str2);   
		}
	}

	/** Test if the actual vector is equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 * @param z the expected z.
	 */
	public void assertFpVectorEquals(double x, double y, double z, Vector3D<?, ?, ?> v) {
		double dx = x - v.getX();
		double dy = y - v.getY();
		double dz = z - v.getZ();
		double distSq = dx * dx + dy * dy + dz * dz;
		if (!isEpsilonEquals(distSq, 0.)) {
			final String str1 = "(" + x + ", " + y + ", " + z + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			final String str2 = "(" + v.getX() + ", " + v.getY() + ", " + v.getZ() + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			failCompare(formatFailMessage(NO_MESSAGE, "not same vector", str1, str2),  //$NON-NLS-1$
					str1, str2);   
		}
	}

	/** Test if the actual vector is equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 * @param z the expected z.
	 */
	public void assertIntVectorEquals(int x, int y, int z, Vector3D<?, ?, ?> v) {
		if (x != v.ix() || y != v.iy() || z != v.iz()) {
			final String str1 = "(" + x + ", " + y + ", " + z + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			final String str2 = "(" + v.ix() + ", " + v.iy() + ", " + v.iz() + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			failCompare(formatFailMessage(NO_MESSAGE, "not same vector", str1, str2),  //$NON-NLS-1$
					str1, str2);   
		}
	}

	/** Test if the actual vector is not equal to the expected values.
	 * 
	 * @param x the expected x.
	 * @param y the expected y.
	 * @param z the expected z.
	 * @param v the actual value.
	 */
	public void assertFpVectorNotEquals(double x, double y, double z, Vector3D<?, ?, ?> v) {
		double dx = x - v.getX();
		double dy = y - v.getY();
		double dz = z - v.getZ();
		double distSq = dx * dx + dy * dy + dz * dz;
		if (isEpsilonEquals(distSq, 0.)) {
			final String str1 = "(" + x + ", " + y + ", " + 7 + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			final String str2 = "(" + v.getX() + ", " + v.getY() + ", " + v.getZ() + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			failCompare(formatFailMessage(NO_MESSAGE, "same vector", str1, str2),  //$NON-NLS-1$
					str1, str2);   
		}
	}

	/** Test if the actual vector is not equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 * @param z the expected z.
	 */
	public void assertIntVectorNotEquals(int x, int y, int z, Vector3D<?, ?, ?> v) {
		if (x == v.ix() && y == v.iy() && z == v.iz()) {
			final String str1 = "(" + x + ", " + y + ", " + z + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			final String str2 = "(" + v.ix() + ", " + v.iy() + ", " + v.iz() + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			failCompare(formatFailMessage(NO_MESSAGE, "same vector", str1, str2),  //$NON-NLS-1$
					str1, str2);   
		}
	}

	/** Test if the actual point is equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 */
	public void assertFpPointEquals(double x, double y, Point2D<?, ?> v) {
		double dx = x - v.getX();
		double dy = y - v.getY();
		double distSq = dx * dx + dy * dy;
		if (!isEpsilonEquals(distSq, 0.)) {
			final String str1 = "(" + x + ", " + y + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			final String str2 = "(" + v.getX() + ", " + v.getY() + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			failCompare(formatFailMessage(NO_MESSAGE, "not same point", str1, str2),  //$NON-NLS-1$
					str1, str2);   
		}
	}
	
	/** Test if the actual point is equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 */
	public void assertIntPointEquals(int x, int y, Point2D<?, ?> v) {
		if (x != v.ix() || y != v.iy()) {
			final String str1 = "(" + x + ", " + y + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			final String str2 = "(" + v.ix() + ", " + v.iy() + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			failCompare(formatFailMessage(NO_MESSAGE, "not same point", str1, str2),  //$NON-NLS-1$
					str1, str2);   
		}
	}
	
	/** Test if the actual point is not equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 */
	public void assertFpPointNotEquals(double x, double y, Point2D<?, ?> v) {
		double dx = x - v.getX();
		double dy = y - v.getY();
		double distSq = dx * dx + dy * dy;
		if (isEpsilonEquals(distSq, 0.)) {
			final String str1 = "(" + x + ", " + y + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			final String str2 = "(" + v.getX() + ", " + v.getY() + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			failCompare(formatFailMessage(NO_MESSAGE, "same point", str1, str2),  //$NON-NLS-1$
					str1, str2);   
		}
	}
	
	/** Test if the actual point is not equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 */
	public void assertIntPointNotEquals(int x, int y, Point2D<?, ?> v) {
		if (x == v.ix() && y == v.iy()) {
			final String str1 = "(" + x + ", " + y + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			final String str2 = "(" + v.ix() + ", " + v.iy() + ")";    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			failCompare(formatFailMessage(NO_MESSAGE, "same point", str1, str2),  //$NON-NLS-1$
					str1, str2);   
		}
	}
	/** Test if the actual point is equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 * @param z the expected z.
	 */
	public void assertFpPointEquals(double x, double y, double z, Point3D<?, ?, ?> v) {
		double dx = x - v.getX();
		double dy = y - v.getY();
		double dz = z - v.getZ();
		double distSq = dx * dx + dy * dy + dz * dz;
		if (!isEpsilonEquals(distSq, 0.)) {
			failCompare("Not same point", //$NON-NLS-1$
					"(" + x + "; " + y + "; " + z  + ")", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					"(" + v.getX() + "; " + v.getY() + "; " + v.getZ() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	/** Test if the actual point is equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 * @param z the expected z.
	 */
	public void assertIntPointEquals(int x, int y, int z, Point3D<?, ?, ?> v) {
		if (x != v.ix() || y != v.iy() || z != v.iz()) {
			failCompare("Not same point", //$NON-NLS-1$
					"(" + x + "; " + y + "; " + z + ")", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					"(" + v.ix() + "; " + v.iy() + "; " + v.iz() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	/** Test if the actual point is not equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 * @param z the expected z.
	 */
	public void assertFpPointNotEquals(double x, double y, double z, Point3D<?, ?, ?> v) {
		double dx = x - v.getX();
		double dy = y - v.getY();
		double dz = z - v.getZ();
		double distSq = dx * dx + dy * dy + dz * dz;
		if (isEpsilonEquals(distSq, 0.)) {
			failCompare("Same point", //$NON-NLS-1$
					"(" + x + "; " + y + "; " + z + ")", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					"(" + v.getX() + "; " + v.getY() + "; " + v.getZ() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	/** Test if the actual point is not equal to the expected values.
	 * 
	 * @param v the actual value.
	 * @param x the expected x.
	 * @param y the expected y.
	 * @param z the expected z.
	 */
	public void assertIntPointNotEquals(int x, int y, int z, Point3D<?, ?, ?> v) {
		if (x == v.ix() && y == v.iy() && z == v.iz()) {
			failCompare("Same point", //$NON-NLS-1$
					"(" + x + "; " + y + "; " + z + ")", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					"(" + v.ix() + "; " + v.iy() + "; " + v.iz() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	/** Test if the actual matrix is equal to the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 */
	public void assertEpsilonEquals(Matrix2d expected, Matrix2d actual) {
		assertEpsilonEquals(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual matrix is equal to the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 * @param message the error message.
	 */
	public void assertEpsilonEquals(Matrix2d expected, Matrix2d actual, String message) {
		assertEpsilonEquals(expected, actual, () -> message);
	}

	/** Test if the actual matrix is equal to the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 * @param message the provider of the error message.
	 */
	public void assertEpsilonEquals(Matrix2d expected, Matrix2d actual, Supplier<String> message) {
		if ((!isEpsilonEquals(expected.getM00(), actual.getM00()))
			||(!isEpsilonEquals(expected.getM01(), actual.getM01()))
			||(!isEpsilonEquals(expected.getM10(), actual.getM10()))
			||(!isEpsilonEquals(expected.getM11(), actual.getM11()))) {
			failCompare(
					formatFailMessage(message, "Not same matrices", expected, actual),  //$NON-NLS-1$
					expected.toString(), actual.toString());
		}
	}

	/** Test if the actual matrix is different from the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 */
	public void assertNotEpsilonEquals(Matrix2d expected, Matrix2d actual) {
		assertNotEpsilonEquals(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual matrix is different from the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 * @param message the error message.
	 */
	public void assertNotEpsilonEquals(Matrix2d expected, Matrix2d actual, String message) {
		assertNotEpsilonEquals(expected, actual, () -> message);
	}

	/** Test if the actual matrix is different from the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 * @param message the provider of the error message.
	 */
	public void assertNotEpsilonEquals(Matrix2d expected, Matrix2d actual, Supplier<String> message) {
		if ((isEpsilonEquals(expected.getM00(), actual.getM00()))
			&&(isEpsilonEquals(expected.getM01(), actual.getM01()))
			&&(isEpsilonEquals(expected.getM10(), actual.getM10()))
			&&(isEpsilonEquals(expected.getM11(), actual.getM11()))) {
			failCompare(
					formatFailMessage(message, "Not same matrices", expected, actual),  //$NON-NLS-1$
					expected.toString(), actual.toString());
		}
	}

	/** Test if the actual matrix is equal to the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 */
	public void assertEpsilonEquals(Matrix3d expected, Matrix3d actual) {
		assertEpsilonEquals(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual matrix is equal to the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 * @param message the error message.
	 */
	public void assertEpsilonEquals(Matrix3d expected, Matrix3d actual, String message) {
		assertEpsilonEquals(expected, actual, () -> message);
	}

	/** Test if the actual matrix is equal to the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 * @param message the provider of the error message.
	 */
	public void assertEpsilonEquals(Matrix3d expected, Matrix3d actual, Supplier<String> message) {
		if ((!isEpsilonEquals(expected.getM00(), actual.getM00()))
			||(!isEpsilonEquals(expected.getM01(), actual.getM01()))
			||(!isEpsilonEquals(expected.getM02(), actual.getM02()))
			||(!isEpsilonEquals(expected.getM10(), actual.getM10()))
			||(!isEpsilonEquals(expected.getM11(), actual.getM11()))
			||(!isEpsilonEquals(expected.getM12(), actual.getM12()))
			||(!isEpsilonEquals(expected.getM20(), actual.getM20()))
			||(!isEpsilonEquals(expected.getM21(), actual.getM21()))
			||(!isEpsilonEquals(expected.getM22(), actual.getM22()))) {
			failCompare(
					formatFailMessage(message, "Not same matrices", expected, actual),  //$NON-NLS-1$
					expected.toString(), actual.toString());
		}
	}

	/** Test if the actual matrix is different from the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 */
	public void assertNotEpsilonEquals(Matrix3d expected, Matrix3d actual) {
		assertNotEpsilonEquals(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual matrix is different from the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 * @param message the error message.
	 */
	public void assertNotEpsilonEquals(Matrix3d expected, Matrix3d actual, String message) {
		assertNotEpsilonEquals(expected, actual, () -> message);
	}

	/** Test if the actual matrix is different from the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 * @param message the provider of the error message.
	 */
	public void assertNotEpsilonEquals(Matrix3d expected, Matrix3d actual, Supplier<String> message) {
		if ((isEpsilonEquals(expected.getM00(), actual.getM00()))
			&&(isEpsilonEquals(expected.getM01(), actual.getM01()))
			&&(isEpsilonEquals(expected.getM02(), actual.getM02()))
			&&(isEpsilonEquals(expected.getM10(), actual.getM10()))
			&&(isEpsilonEquals(expected.getM11(), actual.getM11()))
			&&(isEpsilonEquals(expected.getM12(), actual.getM12()))
			&&(isEpsilonEquals(expected.getM20(), actual.getM20()))
			&&(isEpsilonEquals(expected.getM21(), actual.getM21()))
			&&(isEpsilonEquals(expected.getM22(), actual.getM22()))) {
			failCompare(
					formatFailMessage(message, "Not same matrices", expected, actual),  //$NON-NLS-1$
					expected.toString(), actual.toString());
		}
	}

	/** Test if the actual matrix is equal to the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 */
	public void assertEpsilonEquals(Matrix4d expected, Matrix4d actual) {
		assertEpsilonEquals(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual matrix is equal to the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 * @param message the error message.
	 */
	public void assertEpsilonEquals(Matrix4d expected, Matrix4d actual, String message) {
		assertEpsilonEquals(expected, actual, () -> message);
	}

	private void assertEpsilonEqualsMatrixElement(double expected, double actual, Supplier<String> message) {
		if (!isEpsilonEquals(expected, actual)) {
			failCompare(
					formatFailMessage(message, "Not same matrix element at (0,0)", expected, actual), //$NON-NLS-1$
					Double.toString(expected), Double.toString(actual));
		}
	}
	
	/** Test if the actual matrix is equal to the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 * @param message the provider of the error message.
	 */
	public void assertEpsilonEquals(Matrix4d expected, Matrix4d actual, Supplier<String> message) {
		assertEpsilonEqualsMatrixElement(expected.getM00(), actual.getM00(), message);
		assertEpsilonEqualsMatrixElement(expected.getM01(), actual.getM01(), message);
		assertEpsilonEqualsMatrixElement(expected.getM02(), actual.getM02(), message);
		assertEpsilonEqualsMatrixElement(expected.getM03(), actual.getM03(), message);

		assertEpsilonEqualsMatrixElement(expected.getM10(), actual.getM10(), message);
		assertEpsilonEqualsMatrixElement(expected.getM11(), actual.getM11(), message);
		assertEpsilonEqualsMatrixElement(expected.getM12(), actual.getM12(), message);
		assertEpsilonEqualsMatrixElement(expected.getM13(), actual.getM13(), message);

		assertEpsilonEqualsMatrixElement(expected.getM20(), actual.getM20(), message);
		assertEpsilonEqualsMatrixElement(expected.getM21(), actual.getM21(), message);
		assertEpsilonEqualsMatrixElement(expected.getM22(), actual.getM22(), message);
		assertEpsilonEqualsMatrixElement(expected.getM23(), actual.getM23(), message);

		assertEpsilonEqualsMatrixElement(expected.getM30(), actual.getM30(), message);
		assertEpsilonEqualsMatrixElement(expected.getM31(), actual.getM31(), message);
		assertEpsilonEqualsMatrixElement(expected.getM32(), actual.getM32(), message);
		assertEpsilonEqualsMatrixElement(expected.getM33(), actual.getM33(), message);
	}

	/** Test if the actual matrix is different from the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 */
	public void assertNotEpsilonEquals(Matrix4d expected, Matrix4d actual) {
		assertNotEpsilonEquals(expected, actual, NO_MESSAGE);
	}

	/** Test if the actual matrix is different from the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 * @param message the error message.
	 */
	public void assertNotEpsilonEquals(Matrix4d expected, Matrix4d actual, String message) {
		assertNotEpsilonEquals(expected, actual, () -> message);
	}

	/** Test if the actual matrix is different from the expected values.
	 * 
	 * @param expected the expected value. 
	 * @param actual the actual value.
	 * @param message the provider of the error message.
	 */
	public void assertNotEpsilonEquals(Matrix4d expected, Matrix4d actual, Supplier<String> message) {
		if ((isEpsilonEquals(expected.getM00(), actual.getM00()))
			&&(isEpsilonEquals(expected.getM01(), actual.getM01()))
			&&(isEpsilonEquals(expected.getM02(), actual.getM02()))
			&&(isEpsilonEquals(expected.getM03(), actual.getM03()))
			&&(isEpsilonEquals(expected.getM10(), actual.getM10()))
			&&(isEpsilonEquals(expected.getM11(), actual.getM11()))
			&&(isEpsilonEquals(expected.getM12(), actual.getM12()))
			&&(isEpsilonEquals(expected.getM13(), actual.getM13()))
			&&(isEpsilonEquals(expected.getM20(), actual.getM20()))
			&&(isEpsilonEquals(expected.getM21(), actual.getM21()))
			&&(isEpsilonEquals(expected.getM22(), actual.getM22()))
			&&(isEpsilonEquals(expected.getM23(), actual.getM23()))
			&&(isEpsilonEquals(expected.getM30(), actual.getM30()))
			&&(isEpsilonEquals(expected.getM31(), actual.getM31()))
			&&(isEpsilonEquals(expected.getM32(), actual.getM32()))
			&&(isEpsilonEquals(expected.getM33(), actual.getM33()))) {
			failCompare(
					formatFailMessage(message, "Not same matrices", expected, actual),  //$NON-NLS-1$
					expected.toString(), actual.toString());
		}
	}
	
	/** Assume that the given tuple is mutable.
	 * 
	 * @param tuple the tuple.
	 */
	public void assumeMutable(Tuple2D<?> tuple) {
	    try {
	        tuple.add(0, 0);
	    } catch (UnsupportedOperationException exception) {
	        throw new TestAbortedException("Object is immutable"); //$NON-NLS-1$
	    }
	}
	
	/** Assume that the given tuple is mutable.
	 * 
	 * @param tuple the tuple.
	 */
	public void assumeImmutable(Tuple2D<?> tuple) {
	    try {
	        tuple.add(0, 0);
	    } catch (UnsupportedOperationException exception) {
	        return;
	    }
	    throw new TestAbortedException("Object is mutable"); //$NON-NLS-1$
	}

	/** Assume that the given tuple is mutable.
	 * 
	 * @param tuple the tuple.
	 */
	public void assumeMutable(Tuple3D<?> tuple) {
		try {
			tuple.add(0, 0, 0);
		} catch (UnsupportedOperationException exception) {
			throw new TestAbortedException("Object is immutable"); //$NON-NLS-1$
		}
	}
	
	/** Assume that the given tuple is mutable.
	 * 
	 * @param tuple the tuple.
	 */
	public void assumeImmutable(Tuple3D<?> tuple) {
		try {
			tuple.add(0, 0, 0);
		} catch (UnsupportedOperationException exception) {
			return;
		}
		throw new TestAbortedException("Object is mutable"); //$NON-NLS-1$
	}

	/** Test if the actual value is between the given range values, inclusive.
	 * 
	 * @param min the minimum value.
	 * @param max the maximum value.
	 * @param actual the actual value.
	 * @since 18.0
	 */
	public void assertEpsilonInRange(double min, double max, double actual) {
		assertEpsilonInRange(min, max, actual, () -> null);
	}

	/** Test if the actual value is between the given range values, inclusive.
	 * 
	 * @param min the minimum value.
	 * @param max the maximum value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 18.0
	 */
	public void assertEpsilonInRange(double min, double max, double actual, String message) {
		assertEpsilonInRange(min, max, actual, () -> message);
	}

	/** Test if the actual value is between the given range values, inclusive.
	 * 
	 * @param min the minimum value.
	 * @param max the maximum value.
	 * @param actual the actual value.
	 * @param message the error message.
	 * @since 18.0
	 */
	public void assertEpsilonInRange(double min, double max, double actual, Supplier<String> message) {
		if (actual < min && !isEpsilonEquals(min, actual)) {
			failCompare(
					formatFailMessage(message, "the value must be greater or equal to " + min, min, actual),  //$NON-NLS-1$
					Double.toString(min),
					Double.toString(actual));
		}
		if (actual > max && !isEpsilonEquals(max, actual)) {
			failCompare(
					formatFailMessage(message, "the value must be lower or equal to " + max, max, actual),  //$NON-NLS-1$
					Double.toString(max),
					Double.toString(actual));
		}
	}

}
