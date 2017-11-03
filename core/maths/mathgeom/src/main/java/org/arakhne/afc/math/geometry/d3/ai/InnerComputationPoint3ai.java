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

package org.arakhne.afc.math.geometry.d3.ai;

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiablePoint3D;

/** A point that is used for internal computations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class InnerComputationPoint3ai implements Point3D<InnerComputationPoint3ai, InnerComputationVector3ai> {

	private static final long serialVersionUID = 8578192819251519051L;

	private int x;

	private int y;

	private int z;

	/** Construct point.
     */
	public InnerComputationPoint3ai() {
		//
	}

	/** Construct point.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     */
	public InnerComputationPoint3ai(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public InnerComputationPoint3ai clone() {
	    try {
            return (InnerComputationPoint3ai) super.clone();
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
	public UnmodifiablePoint3D<InnerComputationPoint3ai, InnerComputationVector3ai> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}

}
