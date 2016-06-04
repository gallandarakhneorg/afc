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

package org.arakhne.afc.math.geometry.d2.ifx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.Tuple2D;

/** 2D tuple with 2 integer FX properties.
 *
 * @param <RT> is the type of return tuples by the tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Tuple2ifx<RT extends Tuple2ifx<? super RT>> implements Tuple2D<RT> {

	private static final long serialVersionUID = 3136314939750740492L;

	/** x coordinate.
	 */
	IntegerProperty x;

	/** y coordinate.
	 */
	IntegerProperty y;

	/** Construct a zero tuple.
	 */
	public Tuple2ifx() {
		this(0, 0);
	}

	/** Construct a tuple with the given properties for the coordinates.
	 * @param xProperty property for the x coordinate.
	 * @param yProperty property for the y coordinate.
	 */
	public Tuple2ifx(IntegerProperty xProperty, IntegerProperty yProperty) {
		this.x = xProperty;
		this.y = yProperty;
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2ifx(Tuple2D<?> tuple) {
		this(tuple.ix(), tuple.iy());
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2ifx(int[] tuple) {
		this(tuple[0], tuple[1]);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2ifx(double[] tuple) {
		this((int) Math.round(tuple[0]), (int) Math.round(tuple[1]));
	}

	/** Construct a tuple with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Tuple2ifx(double x, double y) {
		this((int) Math.round(x), (int) Math.round(y));
	}

	/** Construct a tuple with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Tuple2ifx(int x, int y) {
		this(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public RT clone() {
		try {
			final RT clone = (RT) super.clone();
			if (this.x != null) {
				clone.x = null;
				clone.xProperty().set(ix());
			}
			if (this.y != null) {
				clone.y = null;
				clone.yProperty().set(iy());
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public boolean equals(Object object) {
		try {
			return equals((RT) object);
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
		bits = 31 * bits + Integer.hashCode(ix());
		bits = 31 * bits + Integer.hashCode(iy());
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public String toString() {
		return "(" //$NON-NLS-1$
				+ ix()
				+ ", " //$NON-NLS-1$
				+ iy()
				+ ")"; //$NON-NLS-1$
	}

	/** Replies the x property.
	 *
	 * @return the x property.
	 */
	@Pure
	public IntegerProperty xProperty() {
		if (this.x == null) {
			this.x = new SimpleIntegerProperty(this, MathFXAttributeNames.X);
		}
		return this.x;
	}

	/** Replies the y property.
	 *
	 * @return the y property.
	 */
	@Pure
	public IntegerProperty yProperty() {
		if (this.y == null) {
			this.y = new SimpleIntegerProperty(this, MathFXAttributeNames.Y);
		}
		return this.y;
	}

	@Override
	public double getX() {
		return this.x == null ? 0 : this.x.get();
	}

	@Override
	public int ix() {
		return this.x == null ? 0 : this.x.get();
	}

	@Override
	public void setX(int x) {
		xProperty().set(x);
	}

	@Override
	public void setX(double x) {
		xProperty().set((int) Math.round(x));
	}

	@Override
	public double getY() {
		return this.y == null ? 0 : this.y.get();
	}

	@Override
	public int iy() {
		return this.y == null ? 0 : this.y.get();
	}

	@Override
	public void setY(int y) {
		yProperty().set(y);
	}

	@Override
	public void setY(double y) {
		yProperty().set((int) Math.round(y));
	}

}
