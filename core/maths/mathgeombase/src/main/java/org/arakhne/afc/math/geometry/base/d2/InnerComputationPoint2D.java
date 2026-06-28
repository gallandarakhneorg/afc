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

package org.arakhne.afc.math.geometry.base.d2;

import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.eclipse.xtext.xbase.lib.Pure;

/** A point that is used for internal computations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class InnerComputationPoint2D implements Point2D<InnerComputationPoint2D, InnerComputationVector2D> {

	private static final long serialVersionUID = 8578192819251519051L;

	private double x;

	private double y;

	/** Construct the point.
	 */
	public InnerComputationPoint2D() {
		//
	}

	/** Construct the point.
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public InnerComputationPoint2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/** Construct the point.
	 * @param tuple the tuple to copy.
	 */
	public InnerComputationPoint2D(Tuple2D<?> tuple) {
		this.x = tuple.getX();
		this.y = tuple.getY();
	}

	@Override
	public boolean equals(Object obj) {
		try {
			return equals((Tuple2D<?>) obj);
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable e2) {
			return false;
		}
	}

	@Pure
	@Override
	public int hashCode() {
		var bits = 1L;
		bits = 31 * bits + Double.hashCode(this.x);
		bits = 31 * bits + Double.hashCode(this.y);
		return (int) (bits ^ (bits >> 31));
	}

	@Pure
	@Override
	public String toString() {
		return toGeogebra();
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add("x", Double.valueOf(getX())); //$NON-NLS-1$
		buffer.add("y", Double.valueOf(getY())); //$NON-NLS-1$
	}

	@Override
	public InnerComputationPoint2D clone() {
		try {
			return (InnerComputationPoint2D) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public int ix() {
		return (int) Math.round(this.x);
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public int iy() {
		return (int) Math.round(this.y);
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public GeomFactory2D<InnerComputationVector2D, InnerComputationPoint2D> getGeomFactory() {
		return InnerComputationGeomFactory2D.SINGLETON;
	}

	@Override
	public UnmodifiablePoint2D<?, ?> toUnmodifiable() {
		return new ImmutablePoint2D(getX(), getY());
	}

}
