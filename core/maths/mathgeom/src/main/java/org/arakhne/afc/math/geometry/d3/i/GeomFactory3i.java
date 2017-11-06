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

package org.arakhne.afc.math.geometry.d3.i;

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
public class GeomFactory3i extends AbstractGeomFactory3D<Vector3i, Point3i>
		implements GeomFactory3ai<PathElement3i, Point3i, Vector3i, RectangularPrism3i> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory3i SINGLETON = new GeomFactory3i();

	@Override
	public Point3i convertToPoint(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		try {
			return (Point3i) point;
		} catch (Throwable exception) {
			return new Point3i(point);
		}
	}

    @Override
    public Point3i convertToPoint(Vector3D<?, ?> vector) {
        assert vector != null : AssertMessages.notNullParameter();
        return new Point3i(vector.ix(), vector.iy(), vector.iz());
    }

	@Override
	public Vector3i convertToVector(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return new Vector3i(point.ix(), point.iy(), point.iz());
	}

	@Override
	public Vector3i convertToVector(Vector3D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		Vector3i vv;
		try {
			vv = (Vector3i) vector;
		} catch (Throwable exception) {
			vv = new Vector3i(vector.ix(), vector.iy(), vector.iz());
		}
		return vv;
	}

	@Override
	public Point3i newPoint(int x, int y, int z) {
		return new Point3i(x, y, z);
	}

    @Override
    public Point3i newPoint() {
        return new Point3i();
    }

    @Override
    public Point3i newPoint(double x, double y, double z) {
        return new Point3i(x, y, z);
    }

	@Override
	public Vector3i newVector(int x, int y, int z) {
		return new Vector3i(x, y, z);
	}

	@Override
	public Vector3i newVector(double x, double y, double z) {
		return new Vector3i(x, y, z);
	}

	@Override
	public Vector3i newVector() {
		return new Vector3i();
	}

	@Override
	public Path3i newPath(PathWindingRule rule) {
		assert rule != null : AssertMessages.notNullParameter();
		return new Path3i(rule);
	}

	@Override
	public RectangularPrism3i newBox() {
		return new RectangularPrism3i();
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public RectangularPrism3i newBox(int x, int y, int z, int width, int height, int depth) {
		assert width >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert height >= 0 : AssertMessages.positiveOrZeroParameter(4);
		assert depth >= 0 : AssertMessages.positiveOrZeroParameter(5);
		return new RectangularPrism3i(x, y, z, width, height, depth);
	}

	@Override
	public PathElement3i newMovePathElement(int x, int y, int z) {
		return new PathElement3i.MovePathElement3i(x, y, z);
	}

	@Override
	public PathElement3i newLinePathElement(int startX, int startY, int startZ, int targetX, int targetY, int targetZ) {
		return new PathElement3i.LinePathElement3i(startX, startY, startZ, targetX, targetY, targetZ);
	}

	@Override
	public PathElement3i newClosePathElement(int lastPointX, int lastPointY, int lastPointZ, int firstPointX,
			int firstPointY, int firstPointZ) {
		return new PathElement3i.ClosePathElement3i(lastPointX, lastPointY, lastPointZ, firstPointX, firstPointY, firstPointZ);
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
	public PathElement3i newCurvePathElement(int startX, int startY, int startZ, int controlX, int controlY, int controlZ,
			int targetX, int targetY, int targerZ) {
        return new PathElement3i.QuadPathElement3i(startX, startY, startZ, controlX, controlY, controlZ, targetX, targetY,
                targerZ);
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
	public PathElement3i newCurvePathElement(int startX, int startY, int startZ, int controlX1, int controlY1, int controlZ1,
			int controlX2, int controlY2, int controlZ2, int targetX, int targetY, int targetZ) {
		return new PathElement3i.CurvePathElement3i(startX, startY, startZ, controlX1, controlY1, controlZ1,
				controlX2, controlY2, controlZ2, targetX, targetY, targetZ);
	}

	@Override
	public Segment3i newSegment(int x1, int y1, int z1, int x2, int y2, int z2) {
		return new Segment3i(x1, y1, z1, x2, y2, z2);
	}

	@Override
	public MultiShape3i<?> newMultiShape() {
		return new MultiShape3i<>();
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
