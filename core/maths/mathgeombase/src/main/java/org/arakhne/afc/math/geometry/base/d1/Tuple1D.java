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

package org.arakhne.afc.math.geometry.base.d1;

import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/** 1.5D tuple.
 *
 * @param <RT> is the type of the data returned by the tuple.
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @param <RP> is the type of point that can be returned by this tuple.
 * @param <RS> is the type of segment that can be returned by this point.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public interface Tuple1D<
		RT extends Tuple1D<? super RT, ? super RV, ? super RP, ? super RS>,
		RV extends Vector1D<? super RV, ? super RP, ? super RS>,
		RP extends Point1D<? super RP, ? super RV, ? super RS>,
		RS extends Segment1D<?, ?>> extends Tuple2D<RT> {

	/** Replies the segment.
	 *
	 * @return the segment or {@code null} if the weak reference has lost the segment.
	 */
	@Pure
	RS getSegment();

	/** Set the segment.
	 *
	 * @param segment is the segment.
	 */
	@Inline(value = "set($1, $0.getX(), $0.getY()")
	default void setSegment(RS segment) {
		set(segment, getX(), getY());
	}

	/** Change the attributes of the tuple.
	 *
	 * @param segment the segment.
	 * @param curviline the curviline coordinate.
	 * @param shift the shift distance.
	 */
	void set(RS segment, double curviline, double shift);

	/**
	 * Set this point from the given informations.
	 *
	 * @param segment
	 *            the segment
	 * @param tuple are the coordinates of the point.
	 */
	@Inline(value = "set($1, ($2).getX(), ($2).getY())")
	default void set(RS segment, Tuple2D<?> tuple) {
		set(segment, tuple.getX(), tuple.getY());
	}

	/** Replies the curviline coordinate.
	 *
	 * @return the curviline coordinate.
	 */
	@Pure
	@Inline(value = "getX()")
	default double getCurvilineCoordinate() {
		return getX();
	}

	/** Set the curviline coordinate.
	 *
	 * @param curviline is the curviline coordinate.
	 */
	@Inline(value = "setX($1)")
	default void setCurvilineCoordinate(double curviline) {
		setX(curviline);
	}

	/** Replies the lateral distance.
	 *
	 * @return the lateral distance.
	 */
	@Pure
	@Inline(value = "getY()")
	default double getLateralDistance() {
		return getY();
	}

	/** Set the shift distance.
	 *
	 * @param distance is the shift distance.
	 */
	@Inline(value = "setY($1)")
	default void setLateralDistance(double distance) {
		setY(distance);
	}

	/** Add the given values to this point.
	 *
	 * @param curvilineMove is the quantity to add to the curviline coordinate.
	 */
	default void add(double curvilineMove) {
		setX(getX() + curvilineMove);
	}

	/** Substract the given values to this point.
	 *
	 * @param curvilineMove is the quantity to substract to the curviline coordinate.
	 */
	default void sub(double curvilineMove) {
		setX(getX() - curvilineMove);
	}

	/** Replies if the given vector is equal to this vector: {@code this == v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return test result.
	 * @see #equals(Tuple2D)
	 */
	@Pure
	@XtextOperator("==")
	@Inline("equals($1)")
	default boolean operator_equals(Tuple2D<?> v) {
		return equals(v);
	}

	/** Replies if the given vector is different than this vector: {@code this != v}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector.
	 * @return test result.
	 * @see #equals(Tuple2D)
	 */
	@Pure
	@XtextOperator("!=")
	default boolean operator_notEquals(Tuple2D<?> v) {
		return !equals(v);
	}

}
