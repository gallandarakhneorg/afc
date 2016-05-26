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
package org.arakhne.afc.math.geometry.d3.i;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ai.GeomFactory3ai;

/** Factory of geometrical elements.
 * 
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GeomFactory3i implements GeomFactory3ai<PathElement3i, Point3i, Vector3i, RectangularPrism3i> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory3i SINGLETON = new GeomFactory3i();
	
	@Override
	public Point3i convertToPoint(Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		try {
			return (Point3i) point;
		} catch (Throwable exception) {
			return new Point3i(point);
		}
	}
	
	@Override
	public Vector3i convertToVector(Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		return new Vector3i(point.ix(), point.iy(), point.iz());
	}

	@Override
	public Point3i convertToPoint(Vector3D<?, ?> vector) {
		assert (vector != null) : "Point must be not null"; //$NON-NLS-1$
		return new Point3i(vector.ix(), vector.iy(), vector.iz());
	}
	
	@Override
	public Vector3i convertToVector(Vector3D<?, ?> vector) {
		assert (vector != null) : "Point must be not null"; //$NON-NLS-1$
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
	public Vector3i newVector(int x, int y, int z) {
		return new Vector3i(x, y, z);
	}

	@Override
	public Point3i newPoint(double x, double y, double z) {
		return new Point3i(x, y, z);
	}

	@Override
	public Vector3i newVector(double x, double y, double z) {
		return new Vector3i(x, y, z);
	}

	@Override
	public Point3i newPoint() {
		return new Point3i();
	}

	@Override
	public Vector3i newVector() {
		return new Vector3i();
	}

	@Override
	public Path3i newPath(PathWindingRule rule) {
		assert (rule != null) : "Path winding rule must be not null"; //$NON-NLS-1$
		return new Path3i(rule);
	}
	
	@Override
	public RectangularPrism3i newBox() {
		return new RectangularPrism3i();
	}
	
	@Override
	public RectangularPrism3i newBox(int x, int y, int z, int width, int height, int depth) {
		assert (width >= 0) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0) : "Height must be positive or zero"; //$NON-NLS-1$
		assert (depth >= 0) : "Depth must be positive or zero"; //$NON-NLS-1$
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
	public PathElement3i newCurvePathElement(int startX, int startY, int startZ, int controlX, int controlY, int controlZ,
			int targetX, int targetY, int targerZ) {
		return new PathElement3i.QuadPathElement3i(startX, startY, startZ, controlX, controlY, controlZ, targetX, targetY, targerZ);
	}

	@Override
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
		throw new UnsupportedOperationException("Not yet implemented"); //$NON-NLS-1$ // TODO
	}

	@Override
	public Quaternion newQuaternion(double attitude, double bank, double heading) {
		throw new UnsupportedOperationException("Not yet implemented"); //$NON-NLS-1$ // TODO
	}

}