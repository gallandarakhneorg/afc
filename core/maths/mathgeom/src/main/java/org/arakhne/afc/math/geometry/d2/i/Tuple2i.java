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

package org.arakhne.afc.math.geometry.d2.i;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** 2D tuple with 2 integer numbers.
 *
 * @param <RT> is the replied type by the tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Tuple2i<RT extends Tuple2i<? super RT>> implements Tuple2D<RT> {

	private static final long serialVersionUID = 3136314939750740492L;

	/** x coordinate.
	 */
	int x;

	/** y coordinate.
	 */
	int y;

	/** Construct aa zero tuple.
	 */
	public Tuple2i() {
		//
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2i(Tuple2i<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		this.x = tuple.x;
		this.y = tuple.y;
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2i(Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		this.x = tuple.ix();
		this.y = tuple.iy();
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2i(int[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		this.x = tuple[0];
		this.y = tuple[1];
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2i(double[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		this.x = (int) Math.round(tuple[0]);
		this.y = (int) Math.round(tuple[1]);
	}

	/** Construct a tuple with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Tuple2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/** Construct a tuple with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Tuple2i(double x, double y) {
		this.x = (int) Math.round(x);
		this.y = (int) Math.round(y);
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public RT clone() {
		try {
			return (RT) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public void absolute() {
		this.x = Math.abs(this.x);
		this.y = Math.abs(this.y);
	}

	@Override
	public void absolute(Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		tuple.set(Math.abs(this.x), Math.abs(this.y));
	}

	@Override
	public void add(int x, int y) {
		this.x += x;
		this.y += y;
	}

	@Override
	public void add(double x, double y) {
		this.x = (int) Math.round(this.x + x);
		this.y = (int) Math.round(this.y + y);
	}

	@Override
	public void addX(int x) {
		this.x += x;
	}

	@Override
	public void addX(double x) {
		this.x = (int) Math.round(this.x + x);
	}

	@Override
	public void addY(int y) {
		this.y += y;
	}

	@Override
	public void addY(double y) {
		this.y = (int) Math.round(this.y + y);
	}

	@Override
	public void negate(Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		this.x = -tuple.ix();
		this.y = -tuple.iy();
	}

	@Override
	public void negate() {
		this.x = -this.x;
		this.y = -this.y;
	}

	@Override
	public void scale(int scale, Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		this.x = (int) Math.round(scale * tuple.getX());
		this.y = (int) Math.round(scale * tuple.getY());
	}

	@Override
	public void scale(double scale, Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		this.x = (int) Math.round(scale * tuple.getX());
		this.y = (int) Math.round(scale * tuple.getY());
	}

	@Override
	public void scale(int scale) {
		this.x *= scale;
		this.y *= scale;
	}

	@Override
	public void scale(double scale) {
		this.x = (int) Math.round(scale * this.x);
		this.y = (int) Math.round(scale * this.y);
	}

	@Override
	public void set(Tuple2D<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		this.x = tuple.ix();
		this.y = tuple.iy();
	}

	@Override
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void set(double x, double y) {
		this.x = (int) Math.round(x);
		this.y = (int) Math.round(y);
	}

	@Override
	public void set(int[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		this.x = tuple[0];
		this.y = tuple[1];
	}

	@Override
	public void set(double[] tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		assert tuple.length >= 2 : AssertMessages.tooSmallArrayParameter(tuple.length, 2);
		this.x = (int) Math.round(tuple[0]);
		this.y = (int) Math.round(tuple[1]);
	}

	@Pure
	@Override
	public double getX() {
		return this.x;
	}

	@Pure
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

	@Pure
	@Override
	public double getY() {
		return this.y;
	}

	@Pure
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
	public void sub(int x, int y) {
		this.x -= x;
		this.y -= y;
	}

	@Override
	public void sub(double x, double y) {
		this.x = (int) Math.round(this.x - x);
		this.y = (int) Math.round(this.y - y);
	}

	@Override
	public void subX(int x) {
		this.x -= x;
	}

	@Override
	public void subX(double x) {
		this.x = (int) Math.round(this.x - x);
	}

	@Override
	public void subY(int y) {
		this.y -= y;
	}

	@Override
	public void subY(double y) {
		this.y = (int) Math.round(this.y - y);
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
		bits = 31 * bits + Integer.hashCode(this.x);
		bits = 31 * bits + Integer.hashCode(this.y);
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

}
