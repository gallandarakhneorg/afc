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

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.AbstractGeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.GeomFactory3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
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
public class GeomFactory3dfx extends AbstractGeomFactory3D<Vector3dfx, Point3dfx>
		implements GeomFactory3afp<PathElement3dfx, Point3dfx, Vector3dfx, RectangularPrism3dfx> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory3dfx SINGLETON = new GeomFactory3dfx();

	@Override
	public Point3dfx convertToPoint(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		try {
			return (Point3dfx) pt;
		} catch (Throwable exception) {
			return new Point3dfx(pt);
		}
	}

	@Override
	public Point3dfx convertToPoint(Vector3D<?, ?> v) {
	    assert v != null : AssertMessages.notNullParameter();
	    Point3dfx pt;
	    try {
	        final Vector3dfx pp = (Vector3dfx) v;
	        pt = new Point3dfx(pp.xProperty(), pp.yProperty(), pp.z);
	    } catch (Throwable exception) {
	        pt = new Point3dfx(v.getX(), v.getY(), v.getZ());
	    }
	    return pt;
	}

	@Override
	public Vector3dfx convertToVector(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		Vector3dfx v;
		try {
			final Point3dfx pp = (Point3dfx) pt;
			v = new Vector3dfx(pp.xProperty(), pp.yProperty(), pp.zProperty());
		} catch (Throwable exception) {
			v = new Vector3dfx(pt.getX(), pt.getY(), pt.getZ());
		}
		return v;
	}

	@Override
	public Vector3dfx convertToVector(Vector3D<?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		Vector3dfx vv;
		try {
			vv = (Vector3dfx) v;
		} catch (Throwable exception) {
			vv = new Vector3dfx(v.getX(), v.getY(), v.getZ());
		}
		return vv;
	}

	@Override
	public Point3dfx newPoint(double x, double y, double z) {
		return new Point3dfx(x, y, z);
	}

	@Override
	public Point3dfx newPoint(int x, int y, int z) {
		return new Point3dfx(x, y, z);
	}

	/** Create a point with properties.
	 *
	 * @param x the x property.
	 * @param y the y property.
	 * @param z the z property.
	 * @return the vector.
	 */
	@SuppressWarnings("static-method")
	public Point3dfx newPoint(DoubleProperty x, DoubleProperty y, DoubleProperty z) {
		return new Point3dfx(x, y, z);
	}

	@Override
	public Point3dfx newPoint() {
	    return new Point3dfx();
	}

	@Override
	public Vector3dfx newVector(double x, double y, double z) {
		return new Vector3dfx(x, y, z);
	}

	@Override
	public Vector3dfx newVector(int x, int y, int z) {
		return new Vector3dfx(x, y, z);
	}

	@Override
	public Vector3dfx newVector() {
		return new Vector3dfx();
	}

	@Override
	public RectangularPrism3dfx newBox() {
		return new RectangularPrism3dfx();
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public RectangularPrism3dfx newBox(double x, double y, double z, double width, double height, double depth) {
		assert width >= 0. : AssertMessages.positiveOrZeroParameter(3);
		assert height >= 0. : AssertMessages.positiveOrZeroParameter(4);
		assert depth >= 0. : AssertMessages.positiveOrZeroParameter(5);
		return new RectangularPrism3dfx(x, y, z, width, height, depth);
	}

	@Override
	public Path3afp<?, ?, PathElement3dfx, Point3dfx, Vector3dfx, RectangularPrism3dfx> newPath(PathWindingRule rule) {
		assert rule != null : AssertMessages.notNullParameter();
		return new Path3dfx(rule);
	}

	@Override
	public PathElement3dfx newMovePathElement(double x, double y, double z) {
		return new PathElement3dfx.MovePathElement3dfx(
				new SimpleDoubleProperty(x),
				new SimpleDoubleProperty(y),
				new SimpleDoubleProperty(z));
	}

	@Override
    public PathElement3dfx newLinePathElement(double startX, double startY, double startZ, double targetX, double targetY,
            double targetZ) {
		return new PathElement3dfx.LinePathElement3dfx(
				new SimpleDoubleProperty(startX),
				new SimpleDoubleProperty(startY),
				new SimpleDoubleProperty(startZ),
				new SimpleDoubleProperty(targetX),
				new SimpleDoubleProperty(targetY),
				new SimpleDoubleProperty(targetZ));
	}

	@Override
	public PathElement3dfx newClosePathElement(double lastPointX, double lastPointY, double lastPointZ,
			double firstPointX, double firstPointY, double firstPointZ) {
		return new PathElement3dfx.ClosePathElement3dfx(
				new SimpleDoubleProperty(lastPointX),
				new SimpleDoubleProperty(lastPointY),
				new SimpleDoubleProperty(lastPointZ),
				new SimpleDoubleProperty(firstPointX),
				new SimpleDoubleProperty(firstPointY),
				new SimpleDoubleProperty(firstPointZ));
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
    public PathElement3dfx newCurvePathElement(double startX, double startY, double startZ, double controlX, double controlY,
            double controlZ, double targetX, double targetY, double targetZ) {
		return new PathElement3dfx.QuadPathElement3dfx(
				new SimpleDoubleProperty(startX),
				new SimpleDoubleProperty(startY),
				new SimpleDoubleProperty(startZ),
				new SimpleDoubleProperty(controlX),
				new SimpleDoubleProperty(controlY),
				new SimpleDoubleProperty(controlZ),
				new SimpleDoubleProperty(targetX),
				new SimpleDoubleProperty(targetY),
				new SimpleDoubleProperty(targetZ));
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
    public PathElement3dfx newCurvePathElement(double startX, double startY, double startZ, double controlX1, double controlY1,
            double controlZ1, double controlX2, double controlY2, double controlZ2, double targetX, double targetY,
            double targetZ) {
		return new PathElement3dfx.CurvePathElement3dfx(
				new SimpleDoubleProperty(startX),
				new SimpleDoubleProperty(startY),
				new SimpleDoubleProperty(startZ),
				new SimpleDoubleProperty(controlX1),
				new SimpleDoubleProperty(controlY1),
				new SimpleDoubleProperty(controlZ1),
				new SimpleDoubleProperty(controlX2),
				new SimpleDoubleProperty(controlY2),
				new SimpleDoubleProperty(controlZ2),
				new SimpleDoubleProperty(targetX),
				new SimpleDoubleProperty(targetY),
				new SimpleDoubleProperty(targetZ));
	}

	@Override
	public MultiShape3dfx<?> newMultiShape() {
		return new MultiShape3dfx<>();
	}

	@Override
	public Segment3dfx newSegment(double x1, double y1, double z1, double x2, double y2, double z2) {
		return new Segment3dfx(x1, y1, z1, x2, y2, z2);
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
