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

package org.arakhne.afc.math.geometry.d1.d;

import java.lang.ref.WeakReference;
import java.util.Objects;

import org.arakhne.afc.math.geometry.base.d1.Point1D;
import org.arakhne.afc.math.geometry.base.d1.Segment1D;
import org.arakhne.afc.math.geometry.base.d1.Tuple1D;
import org.arakhne.afc.math.geometry.base.d1.Vector1D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.d.Tuple2d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.eclipse.xtext.xbase.lib.Pure;

/** 1.5D tuple with 2 double precision floating-point numbers.
 *
 * @param <RT> is the type of the data returned by the tuple.
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @param <RP> is the type of point that can be returned by this tuple.
 * @param <RS> is the type of segment that can be returned by this point.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class Tuple1d<
		RT extends Tuple1d<? super RT, ? super RV, ? super RP, ? super RS>,
		RV extends Vector1d, RP extends Point1d,
		RS extends Segment1D<?, ?>>
		extends Tuple2d<RT>
		implements Tuple1D<RT, RV, RP, RS> {

	private static final long serialVersionUID = 8315462624141196448L;

	/** x coordinate.
	 */
	protected WeakReference<RS> segment;

	/** Construct a zero tuple.
	 */
	public Tuple1d() {
		this.segment = new WeakReference<>(null);
	}

	/** Construct a zero tuple.
	 *
	 * @param segment the segment.
	 */
	public Tuple1d(RS segment) {
		assert segment != null : AssertMessages.notNullParameter();
		this.segment = new WeakReference<>(segment);
	}

	/** Constructor.
	 * @param segment the segment.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple1d(RS segment, Tuple2D<?> tuple) {
		super(tuple);
		assert tuple != null : AssertMessages.notNullParameter();
		assert segment != null : AssertMessages.notNullParameter();
		this.segment = new WeakReference<>(segment);
		this.x = tuple.getX();
		this.y = tuple.getY();
	}

	/** Constructor.
	 * @param segment the segment.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple1d(RS segment, int[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		assert segment != null : AssertMessages.notNullParameter();
		this.segment = new WeakReference<>(segment);
		this.x = tuple[0];
		this.y = tuple[1];
	}

	/** Constructor.
	 * @param segment the segment.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple1d(RS segment, double[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		assert segment != null : AssertMessages.notNullParameter();
		this.segment = new WeakReference<>(segment);
		this.x = tuple[0];
		this.y = tuple[1];
	}

	/** Construct a tuple with the given coordinates.
	 * @param segment the segment.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Tuple1d(RS segment, int x, int y) {
		assert segment != null : AssertMessages.notNullParameter();
		this.segment = new WeakReference<>(segment);
		this.x = x;
		this.y = y;
	}

	/** Construct a tuple with the given coordinates.
	 * @param segment the segment.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Tuple1d(RS segment, double x, double y) {
		assert segment != null : AssertMessages.notNullParameter();
		this.segment = new WeakReference<>(segment);
		this.x = x;
		this.y = y;
	}

	@Pure
	@Override
	public RS getSegment() {
		return this.segment.get();
	}

	@Override
	public void setSegment(RS segment) {
		this.segment = new WeakReference<>(segment);
	}

	@Override
	public void set(RS segment, double curviline, double shift) {
		assert segment != null : AssertMessages.notNullParameter(0);
		this.segment = new WeakReference<>(segment);
		set(curviline, shift);
	}

	@Pure
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!super.equals(object)) {
			return false;
		}
		if (object instanceof Vector1D tuple) {
			return tuple.getSegment() == getSegment();
		}
		if (object instanceof Point1D tuple) {
			return tuple.getSegment() == getSegment();
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		var bits = 1L;
		bits = 31 * bits + super.hashCode();
		bits = 31 * bits + Objects.hashCode(this.segment.get());
		return (int) (bits ^ (bits >> 31));
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("segment", getSegment()); //$NON-NLS-1$
	}

}
