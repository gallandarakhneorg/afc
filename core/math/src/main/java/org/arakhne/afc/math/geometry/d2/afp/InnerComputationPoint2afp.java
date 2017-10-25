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

package org.arakhne.afc.math.geometry.d2.afp;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiablePoint2D;

/** A point that is used for internal computations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class InnerComputationPoint2afp implements Point2D<InnerComputationPoint2afp, InnerComputationVector2afp> {

	private static final long serialVersionUID = 8578192819251519051L;

	private double x;

	private double y;

	/** Construct the point.
	 */
	public InnerComputationPoint2afp() {
		//
	}

	/** Construct the point.
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public InnerComputationPoint2afp(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public InnerComputationGeomFactory getGeomFactory() {
		return InnerComputationGeomFactory.SINGLETON;
	}

	@Override
	public String toString() {
		return Tuple2D.toString(this.x, this.y);
	}

	@Override
	public InnerComputationPoint2afp clone() {
		try {
			return (InnerComputationPoint2afp) super.clone();
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
	public UnmodifiablePoint2D<InnerComputationPoint2afp, InnerComputationVector2afp> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}

}
