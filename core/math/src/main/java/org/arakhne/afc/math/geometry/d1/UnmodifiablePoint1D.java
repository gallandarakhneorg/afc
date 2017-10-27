/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d1;

import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiableTuple2D;

/** Unmodifiable Point.
 *
 * @param <RP> is the type of point that can be returned by this point.
 * @param <RV> is the type of vector that can be returned by this point.
 * @param <RS> is the type of segment that can be returned by this point.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface UnmodifiablePoint1D<
		RP extends Point1D<? super RP, ? super RV, ? super RS>,
		RV extends Vector1D<? super RV, ? super RP, ? super RS>,
		RS extends Segment1D<?, ?>>
		extends UnmodifiableTuple2D<RP>, Point1D<RP, RV, RS> {

	@Override
	default double clamp() {
		throw new UnsupportedOperationException();
	}

	@Override
	default void add(double curvilineMove) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void sub(double curvilineMove) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setCurvilineCoordinate(double curviline) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setShiftDistance(double shift) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setSegment(RS segment) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void set(RS segment, double curviline, double shift) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void set(RS segment, Tuple2D<?> tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void set(Point1D<? extends RP, ? extends RV, ? extends RS> point) {
		throw new UnsupportedOperationException();
	}

	@Override
	default UnmodifiablePoint1D<RP, RV, RS> toUnmodifiable() {
		return this;
	}

}
