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
package org.arakhne.afc.math.geometry.d3.d;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.d.Segment2d;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.GeomFactory3afp;

/** Factory of geometrical elements.
 * 
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GeomFactory3d implements GeomFactory3afp<PathElement3d, Point3d, Vector3d, RectangularPrism3d> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory3d SINGLETON = new GeomFactory3d();
	
	@Override
	public Point3d convertToPoint(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		try {
			return (Point3d) p;
		} catch (Throwable exception) {
			return new Point3d(p);
		}
	}
	
	@Override
	public Vector3d convertToVector(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		return new Vector3d(p.getX(), p.getY(), p.getZ());
	}

	@Override
	public Point3d convertToPoint(Vector3D<?, ?> v) {
		assert (v != null) : "Vector must be not null"; //$NON-NLS-1$
		return new Point3d(v.getX(), v.getY(), v.getZ());
	}
	
	@Override
	public Vector3d convertToVector(Vector3D<?, ?> v) {
		assert (v != null) : "Vector must be not null"; //$NON-NLS-1$
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
	public Vector3d newVector(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}

	@Override
	public Point3d newPoint(int x, int y, int z) {
		return new Point3d(x, y, z);
	}

	@Override
	public Vector3d newVector(int x, int y, int z) {
		return new Vector3d(x, y, z);
	}

	@Override
	public Point3d newPoint() {
		return new Point3d();
	}

	@Override
	public Vector3d newVector() {
		return new Vector3d();
	}

	@Override
	public Path3d newPath(PathWindingRule rule) {
		assert (rule != null) : "Path winding rule must be not null"; //$NON-NLS-1$
		return new Path3d(rule);
	}
	
	@Override
	public RectangularPrism3d newBox() {
		return new RectangularPrism3d();
	}

	@Override
	public RectangularPrism3d newBox(double x, double y, double z, double width, double height, double depth) {
		assert (width >= 0.) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0.) : "Height must be positive or zero"; //$NON-NLS-1$
		assert (depth >= 0.) : "Depth must be positive or zero"; //$NON-NLS-1$
		return new RectangularPrism3d(x, y, z, width, height, depth);
	}
	
	@Override
	public PathElement3d newMovePathElement(double x, double y, double z) {
		return new PathElement3d.MovePathElement3d(x, y, z);
	}

	@Override
	public PathElement3d newLinePathElement(double startX, double startY, double startZ, double targetX, double targetY, double targetZ) {
		return new PathElement3d.LinePathElement3d(startX, startY, startZ, targetX, targetY, targetZ);
	}

	@Override
	public PathElement3d newClosePathElement(double lastPointX, double lastPointY, double lastPointZ, double firstPointX,
			double firstPointY, double firstPointZ) {
		return new PathElement3d.ClosePathElement3d(lastPointX, lastPointY, lastPointZ, firstPointX, firstPointY, firstPointZ);
	}

	@Override
	public PathElement3d newCurvePathElement(double startX, double startY, double startZ, double controlX, double controlY, double controlZ,
			double targetX, double targetY, double targetZ) {
		return new PathElement3d.QuadPathElement3d(startX, startY, startZ, controlX, controlY, controlZ, targetX, targetY, targetZ);
	}

	@Override
	public PathElement3d newCurvePathElement(double startX, double startY, double startZ, double controlX1, double controlY1, double controlZ1,
			double controlX2, double controlY2, double controlZ2, double targetX, double targetY, double targetZ) {
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
		throw new UnsupportedOperationException("Not yet implemented"); //$NON-NLS-1$ //TODO
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.GeomFactory3D#newQuaternion(double, double, double)
	 */
	@Override
	public Quaternion newQuaternion(double attitude, double bank, double heading) {
		throw new UnsupportedOperationException("Not yet implemented"); //$NON-NLS-1$ // TODO
	}
}