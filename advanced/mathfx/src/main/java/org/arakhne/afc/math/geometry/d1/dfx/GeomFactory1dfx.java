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

package org.arakhne.afc.math.geometry.d1.dfx;

import java.lang.ref.WeakReference;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;

import org.arakhne.afc.math.geometry.d1.AbstractGeomFactory1D;
import org.arakhne.afc.math.geometry.d1.Point1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.Vector1D;
import org.arakhne.afc.math.geometry.d1.afp.GeomFactory1afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Factory of geometrical elements.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class GeomFactory1dfx extends AbstractGeomFactory1D<Vector1dfx, Point1dfx>
		implements GeomFactory1afp<Point1dfx, Vector1dfx, Segment1D<?, ?>, Rectangle1dfx> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory1dfx SINGLETON = new GeomFactory1dfx();

	@Override
	public Point1dfx convertToPoint(Point1D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		try {
			return (Point1dfx) pt;
		} catch (Throwable exception) {
			return new Point1dfx(pt);
		}
	}

	@Override
	public Point1dfx convertToPoint(Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		Point1dfx pt;
		try {
			final Vector1dfx pp = (Vector1dfx) vector;
			pt = new Point1dfx(pp.segmentProperty(), pp.xProperty(), pp.yProperty());
		} catch (Throwable exception) {
			pt = new Point1dfx(vector.getSegment(), vector.getX(), vector.getY());
		}
		return pt;
	}

	@Override
	public Vector1dfx convertToVector(Point1D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		Vector1dfx vector;
		try {
			final Point1dfx pp = (Point1dfx) pt;
			vector = new Vector1dfx(pp.segmentProperty(), pp.xProperty(), pp.yProperty());
		} catch (Throwable exception) {
			vector = new Vector1dfx(pt.getSegment(), pt.getX(), pt.getY());
		}
		return vector;
	}

	@Override
	public Vector1dfx convertToVector(Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		try {
			return (Vector1dfx) vector;
		} catch (Throwable exception) {
			return new Vector1dfx(vector);
		}
	}

	/** Create a point with properties.
	 *
	 * @param segment the segment property.
	 * @param x the x property.
	 * @param y the y property.
	 * @return the point.
	 */
	@SuppressWarnings("static-method")
	public Point1dfx newPoint(ObjectProperty<WeakReference<Segment1D<?, ?>>> segment, DoubleProperty x, DoubleProperty y) {
		return new Point1dfx(segment, x, y);
	}

	@Override
	public Point1dfx newPoint(Segment1D<?, ?> segment) {
		return new Point1dfx(segment);
	}

	@Override
	public Point1dfx newPoint(Segment1D<?, ?> segment, double x, double y) {
		return new Point1dfx(segment, x, y);
	}

	@Override
	public Point1dfx newPoint(Segment1D<?, ?> segment, int x, int y) {
		return new Point1dfx(segment, x, y);
	}

	/** Create a vector with properties.
	 *
	 * @param segment the segment property.
	 * @param x the x property.
	 * @param y the y property.
	 * @return the vector.
	 */
	@SuppressWarnings("static-method")
	public Vector1dfx newVector(ObjectProperty<WeakReference<Segment1D<?, ?>>> segment, DoubleProperty x, DoubleProperty y) {
		return new Vector1dfx(segment, x, y);
	}

	@Override
	public Vector1dfx newVector(Segment1D<?, ?> segment) {
		return new Vector1dfx(segment);
	}

	@Override
	public Vector1dfx newVector(Segment1D<?, ?> segment, double x, double y) {
		return new Vector1dfx(segment, x, y);
	}

	@Override
	public Vector1dfx newVector(Segment1D<?, ?> segment, int x, int y) {
		return new Vector1dfx(segment, x, y);
	}

	@Override
	public Rectangle1dfx newBox(Segment1D<?, ?> segment) {
		return new Rectangle1dfx(segment, 0, 0, 0, 0);
	}

	@Override
	public Rectangle1dfx newBox(Segment1D<?, ?> segment, double x, double y, double width, double height) {
		return new Rectangle1dfx(segment, x, y, width, height);
	}

}
