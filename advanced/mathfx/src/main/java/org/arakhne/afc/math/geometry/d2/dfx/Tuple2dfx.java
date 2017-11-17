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

package org.arakhne.afc.math.geometry.d2.dfx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 2D tuple with 2 double precision floating-point FX properties.
 *
 * @param <RT> is the type of the data returned by the tuple.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Tuple2dfx<RT extends Tuple2dfx<? super RT>> implements Tuple2D<RT> {

	private static final long serialVersionUID = 2510506877090400718L;

	/** x coordinate.
	 */
	DoubleProperty x;

	/** y coordinate.
	 */
	DoubleProperty y;

	/** Construct a zero tuple.
	 */
	public Tuple2dfx() {
		//
	}

	/** Construct a tuple with the two given properties for its coordinates.
	 * @param x the property for the x coordinate.
	 * @param y the property for the y coordinate.
	 */
	public Tuple2dfx(DoubleProperty x, DoubleProperty y) {
		set(x, y);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2dfx(Tuple2D<?> tuple) {
	    assert tuple != null : AssertMessages.notNullParameter();
		set(tuple.getX(), tuple.getY());
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2dfx(int[] tuple) {
		this((double) tuple[0], (double) tuple[1]);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2dfx(double[] tuple) {
		this(tuple[0], tuple[1]);
	}

	/** Construct a tuple with the two given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Tuple2dfx(int x, int y) {
		this((double) x, (double) y);
	}

	/** Construct a tuple with the two given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Tuple2dfx(double x, double y) {
		xProperty().set(x);
		yProperty().set(y);
	}

	/** Change the x and y properties.
	 *
	 * @param x the new x property.
	 * @param y the new y property.
	 */
	void set(DoubleProperty x, DoubleProperty y) {
		this.x = x;
		this.y = y;
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public RT clone() {
		try {
			final RT clone = (RT) super.clone();
			if (clone.x != null) {
				clone.x = null;
				clone.xProperty().set(getX());
			}
			if (clone.y != null) {
				clone.y = null;
				clone.yProperty().set(getY());
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Pure
	@Override
	public boolean equals(Object t1) {
		try {
			return equals((Tuple2D<?>) t1);
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable e2) {
			return false;
		}
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(getX());
		bits = 31 * bits + Double.hashCode(getY());
		return bits ^ (bits >> 31);
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

	/** Replies the x property.
	 *
	 * @return the x property.
	 */
	@Pure
	public DoubleProperty xProperty() {
		if (this.x == null) {
			this.x = new SimpleDoubleProperty(this, MathFXAttributeNames.X);
		}
		return this.x;
	}

	/** Replies the y property.
	 *
	 * @return the y property.
	 */
	@Pure
	public DoubleProperty yProperty() {
		if (this.y == null) {
			this.y = new SimpleDoubleProperty(this, MathFXAttributeNames.Y);
		}
		return this.y;
	}

	@Override
	public double getX() {
		return this.x == null ? 0 : this.x.doubleValue();
	}

	@Override
	public int ix() {
		return this.x == null ? 0 : this.x.intValue();
	}

	@Override
	public void setX(int x) {
		xProperty().set(x);
	}

	@Override
	public void setX(double x) {
		xProperty().set(x);
	}

	@Override
	public double getY() {
		return this.y == null ? 0 : this.y.doubleValue();
	}

	@Override
	public int iy() {
		return this.y == null ? 0 : this.y.intValue();
	}

	@Override
	public void setY(int y) {
		yProperty().set(y);
	}

	@Override
	public void setY(double y) {
		yProperty().set(y);
	}

}
