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

package org.arakhne.afc.math.geometry.d3.ifx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.AbstractGeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ai.GeomFactory3ai;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Factory of geometrical elements.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GeomFactory3ifx extends AbstractGeomFactory3D<Vector3ifx, Point3ifx>
		implements GeomFactory3ai<PathElement3ifx, Point3ifx, Vector3ifx, RectangularPrism3ifx> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory3ifx SINGLETON = new GeomFactory3ifx();

	@Override
	public Point3ifx convertToPoint(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		try {
			return (Point3ifx) point;
		} catch (Throwable exception) {
			return new Point3ifx(point);
		}
	}

	@Override
	public Point3ifx convertToPoint(Vector3D<?, ?> vector) {
	    assert vector != null : AssertMessages.notNullParameter();
	    return new Point3ifx(vector.ix(), vector.iy(), vector.iz());
	}

	@Override
	public Vector3ifx convertToVector(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return new Vector3ifx(point.ix(), point.iy(), point.iz());
	}

	@Override
	public Vector3ifx convertToVector(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		Vector3ifx vv;
		try {
			vv = (Vector3ifx) vector;
		} catch (Throwable exception) {
			vv = new Vector3ifx(vector.ix(), vector.iy(), vector.iz());
		}
		return vv;
	}

	@Override
	public Point3ifx newPoint(int x, int y, int z) {
		return new Point3ifx(x, y, z);
	}

	@Override
	public Point3ifx newPoint(double x, double y, double z) {
	    return new Point3ifx(x, y, z);
	}

	@Override
	public Point3ifx newPoint() {
	    return new Point3ifx();
	}

	/** Create a point with properties.
	 *
	 * @param x the x property.
	 * @param y the y property.
	 * @param z the z property.
	 * @return the vector.
	 */
	@SuppressWarnings("static-method")
	public Point3ifx newPoint(IntegerProperty x, IntegerProperty y, IntegerProperty z) {
	    return new Point3ifx(x, y, z);
	}

	@Override
	public Vector3ifx newVector(int x, int y, int z) {
		return new Vector3ifx(x, y, z);
	}

	@Override
	public Vector3ifx newVector(double x, double y, double z) {
		return new Vector3ifx(x, y, z);
	}

	@Override
	public Vector3ifx newVector() {
		return new Vector3ifx();
	}

	@Override
	public Path3ifx newPath(PathWindingRule rule) {
		assert rule != null : AssertMessages.notNullParameter();
		return new Path3ifx(rule);
	}

	@Override
	public RectangularPrism3ifx newBox() {
		return new RectangularPrism3ifx();
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public RectangularPrism3ifx newBox(int x, int y, int z, int width, int height, int depth) {
		assert width >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert height >= 0 : AssertMessages.positiveOrZeroParameter(4);
		assert depth >= 0 : AssertMessages.positiveOrZeroParameter(5);
		return new RectangularPrism3ifx(x, y, z, width, height, depth);
	}

	@Override
	public PathElement3ifx newMovePathElement(int x, int y, int z) {
		return new PathElement3ifx.MovePathElement3ifx(
				new SimpleIntegerProperty(x),
				new SimpleIntegerProperty(y),
				new SimpleIntegerProperty(z));
	}

	@Override
	public PathElement3ifx newLinePathElement(int startX, int startY, int startZ, int targetX, int targetY, int targetZ) {
		return new PathElement3ifx.LinePathElement2ifx(
				new SimpleIntegerProperty(startX),
				new SimpleIntegerProperty(startY),
				new SimpleIntegerProperty(startZ),
				new SimpleIntegerProperty(targetX),
				new SimpleIntegerProperty(targetY),
				new SimpleIntegerProperty(targetZ));
	}

	@Override
	public PathElement3ifx newClosePathElement(int lastPointX, int lastPointY, int lastPointZ, int firstPointX,
			int firstPointY, int firstPointZ) {
		return new PathElement3ifx.ClosePathElement3ifx(
				new SimpleIntegerProperty(lastPointX),
				new SimpleIntegerProperty(lastPointY),
				new SimpleIntegerProperty(lastPointZ),
				new SimpleIntegerProperty(firstPointX),
				new SimpleIntegerProperty(firstPointY),
				new SimpleIntegerProperty(firstPointZ));
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
    public PathElement3ifx newCurvePathElement(int startX, int startY, int startZ, int controlX, int controlY, int controlZ,
            int targetX, int targetY, int targetZ) {
		return new PathElement3ifx.QuadPathElement3ifx(
				new SimpleIntegerProperty(startX),
				new SimpleIntegerProperty(startY),
				new SimpleIntegerProperty(startZ),
				new SimpleIntegerProperty(controlX),
				new SimpleIntegerProperty(controlY),
				new SimpleIntegerProperty(controlZ),
				new SimpleIntegerProperty(targetX),
				new SimpleIntegerProperty(targetY),
				new SimpleIntegerProperty(targetZ));
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
	public PathElement3ifx newCurvePathElement(int startX, int startY, int startZ, int controlX1, int controlY1, int controlZ1,
			int controlX2, int controlY2, int controlZ2, int targetX, int targetY, int targetZ) {
		return new PathElement3ifx.CurvePathElement3ifx(
				new SimpleIntegerProperty(startX),
				new SimpleIntegerProperty(startY),
				new SimpleIntegerProperty(startZ),
				new SimpleIntegerProperty(controlX1),
				new SimpleIntegerProperty(controlY1),
				new SimpleIntegerProperty(controlZ1),
				new SimpleIntegerProperty(controlX2),
				new SimpleIntegerProperty(controlY2),
				new SimpleIntegerProperty(controlZ2),
				new SimpleIntegerProperty(targetX),
				new SimpleIntegerProperty(targetY),
				new SimpleIntegerProperty(targetZ));
	}

	@Override
	public Segment3ifx newSegment(int x1, int y1, int z1, int x2, int y2, int z2) {
		return new Segment3ifx(x1, y1, z1, x2, y2, z2);
	}

	@Override
	public MultiShape3ifx<?> newMultiShape() {
		return new MultiShape3ifx<>();
	}

	@Override
	public Quaternion newQuaternion(Vector3D<?, ?> axis, double angle) {
		throw new UnsupportedOperationException("Not yet implemented"); //$NON-NLS-1$
		// TODO
	}

	@Override
	public Quaternion newQuaternion(double attitude, double bank, double heading) {
		throw new UnsupportedOperationException("Not yet implemented"); //$NON-NLS-1$
		// TODO
	}

}
