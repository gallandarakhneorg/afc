/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.ai;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.GeomFactory2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiableVector2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** A vector that is used for internal computations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class InnerComputationVector2ai implements Vector2D<InnerComputationVector2ai, InnerComputationPoint2ai> {

	private static final long serialVersionUID = -9075498295363704480L;

	private int x;

	private int y;

	/** Construct vector.
	 */
	public InnerComputationVector2ai() {
		//
	}

	/** Construct vector.
	 * @param x x coordinate of the vector.
	 * @param y y coordinate of the vector.
	 */
	public InnerComputationVector2ai(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public InnerComputationVector2ai clone() {
		try {
			return (InnerComputationVector2ai) super.clone();
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
		return this.x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setX(double x) {
		this.x = (int) Math.round(x);
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public int iy() {
		return this.y;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setY(double y) {
		this.y = (int) Math.round(y);
	}

	@Override
	public GeomFactory2D<InnerComputationVector2ai, InnerComputationPoint2ai> getGeomFactory() {
		return InnerComputationGeomFactory.SINGLETON;
	}

	@Override
	public InnerComputationVector2ai toOrthogonalVector() {
		return null;
	}

	@Override
	public InnerComputationVector2ai toUnitVector() {
		final double length = getLength();
		if (length == 0) {
			return new InnerComputationVector2ai();
		}
		return new InnerComputationVector2ai(
				(int) Math.round(getX() / length),
				(int) Math.round(getY() / length));
	}

	@Override
	public UnmodifiableVector2D<InnerComputationVector2ai, InnerComputationPoint2ai> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	public String toString() {
		final JsonBuffer objectDescription = new JsonBuffer();
		toJson(objectDescription);
        return objectDescription.toString();
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add("x", getX()); //$NON-NLS-1$
		buffer.add("y", getY()); //$NON-NLS-1$
	}

}
