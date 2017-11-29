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

package org.arakhne.afc.math.geometry.d3.d;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.AbstractGeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.GeomFactory3afp;
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
public class GeomFactory3d extends AbstractGeomFactory3D<Vector3d, Point3d>
		implements GeomFactory3afp<PathElement3d, Point3d, Vector3d, RectangularPrism3d> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory3d SINGLETON = new GeomFactory3d();

	@Override
	public Point3d convertToPoint(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		try {
			return (Point3d) pt;
		} catch (Throwable exception) {
			return new Point3d(pt);
		}
	}

	@Override
	public Point3d convertToPoint(Vector3D<?, ?> v) {
	    assert v != null : AssertMessages.notNullParameter();
	    return new Point3d(v.getX(), v.getY(), v.getZ());
	}

	@Override
	public Vector3d convertToVector(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		return new Vector3d(pt.getX(), pt.getY(), pt.getZ());
	}

	@Override
	public Vector3d convertToVector(Vector3D<?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		Vector3d vv;
		try {
			vv = (Vector3d) v;
		} catch (Throwable exception) {
			vv = new Vector3d(v.getX(), v.getY(), v.getZ());
		}
		return vv;
	}

	@Override
	public Point3d newPoint(double x, double y, double z) {
		return new Point3d(x, y, z);
	}

	@Override
	public Point3d newPoint(int x, int y, int z) {
	    return new Point3d(x, y, z);
	}

	@Override
	public Point3d newPoint() {
	    return new Point3d();
	}

	@Override
	public Vector3d newVector(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}

	@Override
	public Vector3d newVector(int x, int y, int z) {
		return new Vector3d(x, y, z);
	}

	@Override
	public Vector3d newVector() {
		return new Vector3d();
	}

	@Override
	public Path3d newPath(PathWindingRule rule) {
		assert rule != null : AssertMessages.notNullParameter();
		return new Path3d(rule);
	}

	@Override
	public RectangularPrism3d newBox() {
		return new RectangularPrism3d();
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public RectangularPrism3d newBox(double x, double y, double z, double width, double height, double depth) {
		assert width >= 0. : AssertMessages.positiveOrZeroParameter(3);
		assert height >= 0. : AssertMessages.notNullParameter(4);
		assert depth >= 0. : AssertMessages.notNullParameter(5);
		return new RectangularPrism3d(x, y, z, width, height, depth);
	}

	@Override
	public PathElement3d newMovePathElement(double x, double y, double z) {
		return new PathElement3d.MovePathElement3d(x, y, z);
	}

	@Override
    public PathElement3d newLinePathElement(double startX, double startY, double startZ, double targetX, double targetY,
            double targetZ) {
		return new PathElement3d.LinePathElement3d(startX, startY, startZ, targetX, targetY, targetZ);
	}

	@Override
	public PathElement3d newClosePathElement(double lastPointX, double lastPointY, double lastPointZ, double firstPointX,
			double firstPointY, double firstPointZ) {
		return new PathElement3d.ClosePathElement3d(lastPointX, lastPointY, lastPointZ, firstPointX, firstPointY, firstPointZ);
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
    public PathElement3d newCurvePathElement(double startX, double startY, double startZ, double controlX, double controlY,
            double controlZ, double targetX, double targetY, double targetZ) {
        return new PathElement3d.QuadPathElement3d(startX, startY, startZ, controlX, controlY, controlZ, targetX, targetY,
                targetZ);
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
    public PathElement3d newCurvePathElement(double startX, double startY, double startZ, double controlX1, double controlY1,
            double controlZ1, double controlX2, double controlY2, double controlZ2, double targetX, double targetY,
            double targetZ) {
		return new PathElement3d.CurvePathElement3d(startX, startY, startZ, controlX1, controlY1, controlZ1,
				controlX2, controlY2, controlZ2, targetX, targetY, targetZ);
	}

	@Override
	public Segment3d newSegment(double x1, double y1, double z1, double x2, double y2, double z2) {
		return new Segment3d(x1, y1, z1, x2, y2, z2);
	}

	@Override
	public MultiShape3d<?> newMultiShape() {
		return new MultiShape3d<>();
	}

	@Override
	public Quaternion newQuaternion(Vector3D<?, ?> axis, double angle) {
		assert axis != null : AssertMessages.notNullParameter(0);
		return new Quaternion4d(new Quaternion.AxisAngle(axis.getX(), axis.getY(), axis.getZ(), angle));
	}

	@Override
	public Quaternion newQuaternion(double attitude, double bank, double heading) {
		return new Quaternion4d(new Quaternion.EulerAngles(attitude, bank, heading, CoordinateSystem3D.getDefaultCoordinateSystem()));
	}
}
