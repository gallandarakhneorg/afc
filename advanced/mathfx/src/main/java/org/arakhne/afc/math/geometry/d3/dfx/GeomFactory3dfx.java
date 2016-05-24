/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d3.dfx;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ad.GeomFactory3ad;
import org.arakhne.afc.math.geometry.d3.ad.Path3ad;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/** Factory of geometrical elements.
 * 
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GeomFactory3dfx implements GeomFactory3ad<PathElement3dfx, Point3dfx, Vector3dfx, RectangularPrism3dfx> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory3dfx SINGLETON = new GeomFactory3dfx();
	
	@Override
	public Point3dfx convertToPoint(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		try {
			return (Point3dfx) p;
		} catch (Throwable exception) {
			return new Point3dfx(p);
		}
	}
	
	@Override
	public Vector3dfx convertToVector(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Vector3dfx v;
		try {
			Point3dfx pp = (Point3dfx) p;
			v = new Vector3dfx(pp.xProperty(), pp.yProperty(), pp.zProperty());
		} catch (Throwable exception) {
			v = new Vector3dfx(p.getX(), p.getY(), p.getZ());
		}
		return v;
	}

	@Override
	public Point3dfx convertToPoint(Vector3D<?, ?> v) {
		assert (v != null) : "Vector must be not null"; //$NON-NLS-1$
		Point3dfx p;
		try {
			Vector3dfx pp = (Vector3dfx) v;
			p = new Point3dfx(pp.xProperty(), pp.yProperty(), pp.z);
		} catch (Throwable exception) {
			p = new Point3dfx(v.getX(), v.getY(), v.getZ());
		}
		return p;
	}
	
	@Override
	public Vector3dfx convertToVector(Vector3D<?, ?> v) {
		assert (v != null) : "Vector must be not null"; //$NON-NLS-1$
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
	public Vector3dfx newVector(double x, double y, double z) {
		return new Vector3dfx(x, y, z);
	}

	@Override
	public Vector3dfx newVector(int x, int y, int z) {
		return new Vector3dfx(x, y, z);
	}

	@Override
	public Point3dfx newPoint() {
		return new Point3dfx();
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
	public RectangularPrism3dfx newBox(double x, double y, double z, double width, double height, double depth) {
		assert (width >= 0.) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0.) : "Height must be positive or zero"; //$NON-NLS-1$
		assert (depth >= 0.) : "Height must be positive or zero"; //$NON-NLS-1$
		return new RectangularPrism3dfx(x, y, z, width, height, depth);
	}

	@Override
	public Path3ad<?, ?, PathElement3dfx, Point3dfx, Vector3dfx, RectangularPrism3dfx> newPath(PathWindingRule rule) {
		assert (rule != null) : "Path winding rule must be not null"; //$NON-NLS-1$
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
	public PathElement3dfx newLinePathElement(double startX, double startY, double startZ, double targetX, double targetY, double targetZ) {
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
	public PathElement3dfx newCurvePathElement(double startX, double startY, double startZ, double controlX, double controlY, double controlZ,
			double targetX, double targetY, double targetZ) {
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
	public PathElement3dfx newCurvePathElement(double startX, double startY, double startZ, double controlX1, double controlY1, double controlZ1,
			double controlX2, double controlY2, double controlZ2, double targetX, double targetY, double targetZ) {
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
		throw new UnsupportedOperationException("Not yet implemented");  //$NON-NLS-1$ // TODO
	}

	@Override
	public Quaternion newQuaternion(double attitude, double bank, double heading) {
		throw new UnsupportedOperationException("Not yet implemented");  //$NON-NLS-1$ // TODO
	}

}