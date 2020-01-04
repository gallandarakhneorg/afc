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

package org.arakhne.afc.math.geometry.d3.dfx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 3D tuple with 3 double precision floating-point FX properties.
 *
 * @param <RT> is the type of the data returned by the tuple.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: tpiotrow$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Tuple3dfx<RT extends Tuple3dfx<? super RT>> implements Tuple3D<RT> {

	private static final long serialVersionUID = 2510506877090400718L;

	/** x coordinate.
	 */
	DoubleProperty x;

	/** y coordinate.
	 */
	DoubleProperty y;

	/** y coordinate.
	 */
	DoubleProperty z;

	/** Construct a zero tuple.
     */
	public Tuple3dfx() {
		//
	}

	/** Construct a tuple with the two given properties for its coordinates.
     * @param x the property for the x coordinate.
     * @param y the property for the y coordinate.
     * @param z the property for the z coordinate.
     */
	public Tuple3dfx(DoubleProperty x, DoubleProperty y, DoubleProperty z) {
		set(x, y, z);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Tuple3dfx(Tuple3D<?> tuple) {
	    assert tuple != null : AssertMessages.notNullParameter();
		set(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Tuple3dfx(int[] tuple) {
		this((double) tuple[0], (double) tuple[1], (double) tuple[2]);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Tuple3dfx(double[] tuple) {
		this(tuple[0], tuple[1], tuple[2]);
	}

	/** Construct a tuple with the three given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Tuple3dfx(int x, int y, int z) {
		this((double) x, (double) y, (double) z);
	}

	/** Construct a tuple with the three given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Tuple3dfx(double x, double y, double z) {
		xProperty().set(x);
		yProperty().set(y);
		zProperty().set(z);
	}

	/** Change the x, y and z properties.
	 *
	 * @param x the new x property.
	 * @param y the new y property.
	 * @param z the new z property.
	 */
	void set(DoubleProperty x, DoubleProperty y, DoubleProperty z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
			if (clone.z != null) {
				clone.z = null;
				clone.zProperty().set(getZ());
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
			return equals((Tuple3D<?>) t1);
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
		bits = 31 * bits + Double.hashCode(getZ());
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
		buffer.add("z", getZ()); //$NON-NLS-1$
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

	/** Replies the z property.
	 *
	 * @return the z property.
	 */
	@Pure
	public DoubleProperty zProperty() {
		if (this.z == null) {
			this.z = new SimpleDoubleProperty(this, MathFXAttributeNames.Z);
		}
		return this.z;
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

	@Override
	public double getZ() {
		return this.z == null ? 0 : this.z.doubleValue();
	}

	@Override
	public int iz() {
		return this.z == null ? 0 : this.z.intValue();
	}

	@Override
	public void setZ(int z) {
		zProperty().set(z);
	}

	@Override
	public void setZ(double z) {
		zProperty().set(z);
	}

}
