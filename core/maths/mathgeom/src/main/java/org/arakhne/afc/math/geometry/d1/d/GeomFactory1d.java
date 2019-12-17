/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d1.d;

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
public class GeomFactory1d extends AbstractGeomFactory1D<Vector1d, Point1d>
		implements GeomFactory1afp<Point1d, Vector1d, Segment1D<?, ?>, Rectangle1d> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory1d SINGLETON = new GeomFactory1d();

	@Override
	public Point1d convertToPoint(Point1D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		try {
			return (Point1d) pt;
		} catch (Throwable exception) {
			return new Point1d(pt);
		}
	}

	@Override
	public Point1d convertToPoint(Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		return new Point1d(vector);
	}

	@Override
	public Vector1d convertToVector(Point1D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		return new Vector1d(pt);
	}

	@Override
	public Vector1d convertToVector(Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		try {
			return (Vector1d) vector;
		} catch (Throwable exception) {
			return new Vector1d(vector);
		}
	}

	@Override
	public Point1d newPoint(Segment1D<?, ?> segment) {
		return new Point1d(segment);
	}

	@Override
	public Point1d newPoint(Segment1D<?, ?> segment, double x, double y) {
		return new Point1d(segment, x, y);
	}

	@Override
	public Point1d newPoint(Segment1D<?, ?> segment, int x, int y) {
		return new Point1d(segment, x, y);
	}

	@Override
	public Vector1d newVector(Segment1D<?, ?> segment) {
		return new Vector1d(segment);
	}

	@Override
	public Vector1d newVector(Segment1D<?, ?> segment, double x, double y) {
		return new Vector1d(segment, x, y);
	}

	@Override
	public Vector1d newVector(Segment1D<?, ?> segment, int x, int y) {
		return new Vector1d(segment, x, y);
	}

	@Override
	public Rectangle1d newBox(Segment1D<?, ?> segment) {
		return new Rectangle1d(segment, 0, 0, 0, 0);
	}

	@Override
	public Rectangle1d newBox(Segment1D<?, ?> segment, double x, double y, double width, double height) {
		return new Rectangle1d(segment, x, y, width, height);
	}

}
