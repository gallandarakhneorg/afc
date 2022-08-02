/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class represents a direction in a 1D or 1.5D coordinate space.
 *
 * <p>A 1D direction is related to a {@link Segment1D}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public enum Direction1D {

	/** Same direction as a {@link Segment1D}.
	 */
	SEGMENT_DIRECTION,

	/** Reverted direction then a {@link Segment1D}.
	 */
	REVERTED_DIRECTION,

	/** Both directions of a {@link Segment1D}.
	 */
	BOTH_DIRECTIONS;

	/** Replies if the segment direction is allowed.
	 *
	 * @return <code>true</code> if the segment direction is allowed, otherwise <code>false</code>
	 */
	@Pure
	public boolean isSegmentDirection() {
		return this == SEGMENT_DIRECTION || this == BOTH_DIRECTIONS;
	}

	/** Replies if the reverted segment direction is allowed.
	 *
	 * @return <code>true</code> if the reverted segment direction is allowed, otherwise <code>false</code>
	 */
	@Pure
	public boolean isRevertedSegmentDirection() {
		return this == REVERTED_DIRECTION || this == BOTH_DIRECTIONS;
	}

	/** Replies the direction according to the given integer value.
	 *
	 * <p>The <var>value</var> is mapped to <code>SEGMENT_DIRECTION</code> if positive.
	 * It is mapped to <code>REVERSED_SEGMENT_DIRECTION</code> if negative. And it
	 * is mapped to <code>BOTH_DIRECTIONS</code> if nul.
	 *
	 * @param value the value to convert.
	 * @return the direction which is corresponding to the given <var>value</var>.
	 */
	@Pure
	public static Direction1D fromInteger(int value) {
		if (value < 0) {
			return REVERTED_DIRECTION;
		}
		if (value > 0) {
			return SEGMENT_DIRECTION;
		}
		return BOTH_DIRECTIONS;
	}

	/** Replies integer value that is corresponding to the direction.
	 *
	 * <p>The returned value is positive if <code>SEGMENT_DIRECTION</code>.
	 * It is negative if <code>REVERSED_DIRECTION</code> if negative. And it
	 * is zero if <code>BOTH_DIRECTIONS</code>.
	 *
	 * @return the direction which is corresponding to the given <var>value</var>.
	 */
	@Pure
	public int toInteger() {
		switch (this) {
		case BOTH_DIRECTIONS:
			return 0;
		case SEGMENT_DIRECTION:
			return 1;
		case REVERTED_DIRECTION:
			return -1;
		default:
		}
		return 0;
	}

}
