/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiablePoint2D;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** A point that is used for internal computations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class InnerComputationPoint2ai implements Point2D<InnerComputationPoint2ai, InnerComputationVector2ai> {

    private static final long serialVersionUID = 8578192819251519051L;

    private int x;

    private int y;

    /** Construct point.
     */
    public InnerComputationPoint2ai() {
        //
    }

    /** Construct point.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    public InnerComputationPoint2ai(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public InnerComputationPoint2ai clone() {
        try {
            return (InnerComputationPoint2ai) super.clone();
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
    public UnmodifiablePoint2D<InnerComputationPoint2ai, InnerComputationVector2ai> toUnmodifiable() {
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
