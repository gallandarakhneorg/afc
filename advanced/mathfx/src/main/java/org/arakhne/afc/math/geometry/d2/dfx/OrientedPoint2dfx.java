/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.OrientedPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** 2D oriented point with double precison floating point FX properties.
 *
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OrientedPoint2dfx extends Point2dfx implements OrientedPoint2D<Point2dfx, Vector2dfx> {

	private static final long serialVersionUID = 1696624733007552173L;

	/**
	 * Tangent vector to this point.
	 */
	private Vector2dfx tangent = new Vector2dfx(1, 0);

	/**
	 * Normal vector to the point.
	 */
	private ObjectProperty<Vector2dfx> normalProperty;

	/** Construct an empty oriented point.
	 */
	public OrientedPoint2dfx() {
		//
	}

	/** Constructor by copy.
	 * @param tuple the tuple to copy.
	 */
	public OrientedPoint2dfx(Tuple2D<?> tuple) {
		super(tuple);
	}

	/** Constructor by setting.
	 * @param tuple the tuple to set.
	 */
	public OrientedPoint2dfx(Tuple2dfx<?> tuple) {
		assert tuple != null : AssertMessages.notNullParameter();
		this.x = tuple.x;
		this.y = tuple.y;
	}

	/** Construct an oriented point from the two given coordinates.
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 */
	public OrientedPoint2dfx(double x, double y) {
		super(x, y);
	}

	/** Construct an oriented point from the two given coordinates.
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 */
	public OrientedPoint2dfx(DoubleProperty x, DoubleProperty y) {
		super(x, y);
	}

	/** Construct an oriented point from a point and a tangent vector.
	 * @param point the point.
	 * @param tangent the tangent vector.
	 */
	public OrientedPoint2dfx(Point2D<?, ?> point, Vector2D<?, ?> tangent) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert tangent != null : AssertMessages.notNullParameter(1);
		set(point.getX(), point.getY(), tangent.getX(), tangent.getY());
	}

	/** Constructor by setting from a point and a tangent vector.
	 * @param point the point.
	 * @param tangent the tangent vector.
	 */
	public OrientedPoint2dfx(Point2dfx point, Vector2dfx tangent) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert tangent != null : AssertMessages.notNullParameter(1);
		this.x = point.x;
		this.y = point.y;
		this.tangent = tangent;
	}

	/** Construct an oriented point from the two given coordinates.
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param tanX x coordinate of the vector.
	 * @param tanY y coordinate of the vector.
	 */
	public OrientedPoint2dfx(double x, double y, double tanX, double tanY) {
		super(x, y);
		tanXProperty().set(tanX);
		tanYProperty().set(tanY);
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + super.hashCode();
		bits = 31 * bits + Double.hashCode(getTangentX());
		bits = 31 * bits + Double.hashCode(getTangentY());
		return bits ^ (bits >> 31);
	}

	@Override
	public OrientedPoint2dfx clone() {
		final OrientedPoint2dfx clone = (OrientedPoint2dfx) super.clone();
		if (clone.tangent != null) {
			clone.tangent = null;
			clone.tangent = this.tangent.clone();
		}
		return clone;
	}

	@Override
	public double getTangentX() {
		return this.tangent.getX();
	}

	@Override
	public int itx() {
		return this.tangent.ix();
	}

	@Override
	public void setTangentX(int tanX) {
		this.tangent.setX(tanX);
	}

	@Override
	public void setTangentX(double tanX) {
		this.tangent.setX(tanX);
	}

	@Override
	public double getTangentY() {
		return this.tangent.getY();
	}

	@Override
	public int ity() {
		return this.tangent.iy();
	}

	@Override
	public void setTangentY(int tanY) {
		this.tangent.setY(tanY);
	}

	@Override
	public void setTangentY(double tanY) {
		this.tangent.setY(tanY);
	}

	/** Replies the property that is the x coordinate of the tangent vector.
	 *
	 * @return the tangent vector x property.
	 */
	@Pure
	public DoubleProperty tanXProperty() {
		return this.tangent.xProperty();
	}

	/** Replies the property that is the y coordinate of the tangent vector.
	 *
	 * @return the tangent vector y property.
	 */
	@Pure
	public DoubleProperty tanYProperty() {
		return this.tangent.yProperty();
	}

	@Override
	public Vector2dfx getTangent() {
		return this.tangent;
	}

	@Override
	public void setTangent(Vector2dfx tangent) {
		this.tangent = tangent;
	}

	@Override
	public Vector2dfx getNormal() {
		if (this.normalProperty == null) {
			this.normalProperty = new SimpleObjectProperty<>(this, MathFXAttributeNames.NORMAL);
			this.normalProperty.bind(Bindings.createObjectBinding(() -> this.tangent.toOrthogonalVector(),
					this.tangent.xProperty(), this.tangent.yProperty()));
		}
		return this.normalProperty.get();
	}

	/** Replies the property that is the x coordinate of the normal vector.
	 *
	 * @return the normal vector x property.
	 */
	@Pure ReadOnlyDoubleProperty norXProperty() {
		return getNormal().xProperty();
	}

	/** Replies the property that is the y coordinate of the normal vector.
	 *
	 * @return the normal vector y property.
	 */
	@Pure ReadOnlyDoubleProperty norYProperty() {
		return getNormal().yProperty();
	}

	@Override
	public double getNormalX() {
		return getNormal().getX();
	}

	@Override
	public int inx() {
		return getNormal().ix();
	}

	@Override
	public double getNormalY() {
		return getNormal().getY();
	}

	@Override
	public int iny() {
		return getNormal().iy();
	}
}
