/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.ai;

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiableVector3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/** A vector that is used for internal computations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class InnerComputationVector3ai implements Vector3D<InnerComputationVector3ai, InnerComputationPoint3ai> {

	private static final long serialVersionUID = -9075498295363704480L;

	private int x;

	private int y;

	private int z;

	/** Construct vector.
     */
	public InnerComputationVector3ai() {
		//
	}

	/** Construct vector.
     * @param x x coordinate of the vector.
     * @param y y coordinate of the vector.
     * @param z z coordinate of the vector.
     */
	public InnerComputationVector3ai(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public InnerComputationVector3ai clone() {
	    try {
            return (InnerComputationVector3ai) super.clone();
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
	public double getZ() {
		return this.z;
	}

	@Override
	public int iz() {
		return this.z;
	}

	@Override
	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public void setZ(double z) {
		this.z = (int) Math.round(z);
	}

	@Override
	public GeomFactory3D<InnerComputationVector3ai, InnerComputationPoint3ai> getGeomFactory() {
		return InnerComputationGeomFactory.SINGLETON;
	}

	@Override
	public InnerComputationVector3ai toUnitVector() {
		final double length = getLength();
		if (length == 0) {
			return new InnerComputationVector3ai();
		}
		return new InnerComputationVector3ai(
				(int) Math.round(getX() / length),
				(int) Math.round(getY() / length),
				(int) Math.round(getZ() / length));
	}

	@Override
	public UnmodifiableVector3D<InnerComputationVector3ai, InnerComputationPoint3ai> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}

}
