/* 
 * $Id$
 * 
 * Copyright (C) 2015 Hamza JAFFALI.
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
package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.PathElementType;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/** An element of the path.
 *
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPathElement3D implements AbstractPathElement3X {

	private static final long serialVersionUID = 5926100640197911368L;
	
	/** Create an instance of path element.
	 * 
	 * @param type is the type of the new element.
	 * @param lastX is the coordinate of the last point.
	 * @param lastY is the coordinate of the last point.
	 * @param lastZ is the coordinate of the last point.
	 * @param coords are the coordinates.
	 * @return the instance of path element.
	 */
	@Pure
	public static AbstractPathElement3D newInstance(PathElementType type, double lastX, double lastY, double lastZ, double[] coords) {
		switch(type) {
		case MOVE_TO:
			return new MovePathElement3d(coords[0], coords[1], coords[2]);
		case LINE_TO:
			return new LinePathElement3d(lastX, lastY, lastZ, coords[0], coords[1], coords[2]);
		case QUAD_TO:
			return new QuadPathElement3d(lastX, lastY, lastZ, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
		case CURVE_TO:
			return new CurvePathElement3d(lastX, lastY, lastZ, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5], coords[6], coords[7], coords[8]);
		case CLOSE:
			return new ClosePathElement3d(lastX, lastY, lastZ, coords[0], coords[1], coords[2]);
		default:
		}
		throw new IllegalArgumentException();
	}
	
	/** Create an instance of path element, associating properties of points to the PathElement.
	 * 
	 * When the point in parameter are changed, the PathElement will change also.
	 * 
	 * @param type is the type of the new element.
	 * @param last is the last point.
	 * @param coords are the eventual other points.
	 * @return the instance of path element.
	 */
	@Pure
	public static AbstractPathElement3D newInstance(PathElementType type, Point3d last, Point3d[] coords) {
		switch(type) {
		case MOVE_TO:
			return new MovePathElement3d(coords[0]);
		case LINE_TO:
			return new LinePathElement3d(last, coords[0]);
		case QUAD_TO:
			return new QuadPathElement3d(last, coords[0], coords[1]);
		case CURVE_TO:
			return new CurvePathElement3d(last, coords[0], coords[1], coords[2]);
		case CLOSE:
			return new ClosePathElement3d(last, coords[0]);
		default:
		}
		throw new IllegalArgumentException();
	}
	
	
	/** Copy the coords into the given array, except the source point.
	 * 
	 * @param array
	 */
	public abstract void toArray(DoubleProperty[] array);
	
	
	/** Type of the path element.
	 */
	protected final PathElementType type;
	
	public AbstractPathElement3D(PathElementType type1) {
		assert(type1!=null);
		this.type = type1;
	}
	
	/**
	 * @param type1 is the type of the element.
	 * @param fromx is the source point.
	 * @param fromy is the source point.
	 * @param fromz is the source point.
	 * @param ctrlx1 is the first control point.
	 * @param ctrly1 is the first control point.
	 * @param ctrlz1 is the first control point.
	 * @param ctrlx2 is the first control point.
	 * @param ctrly2 is the first control point.
	 * @param ctrlz2 is the first control point.
	 * @param tox is the target point.
	 * @param toy is the target point.
	 * @param toz is the target point.
	 */
	public AbstractPathElement3D(PathElementType type1, double fromx, double fromy, double fromz, double ctrlx1, double ctrly1, double ctrlz1, double ctrlx2, double ctrly2, double ctrlz2, double tox, double toy, double toz) {
		assert(type1!=null);
		this.type = type1;
		this.setFromX(fromx);
		this.setFromY(fromy);
		this.setFromZ(fromz);
		this.setCtrlX1(ctrlx1);
		this.setCtrlY1(ctrly1);
		this.setCtrlZ1(ctrlz1);
		this.setCtrlX2(ctrlx2);
		this.setCtrlY2(ctrly2);
		this.setCtrlZ2(ctrlz2);
		this.setToX(tox);
		this.setToY(toy);
		this.setToZ(toz);
	}

	
	
	

	/** An element of the path that represents a <code>MOVE_TO</code>.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class MovePathElement3d extends PathElement3d {
		
		private static final long serialVersionUID = -5596181248741970433L;
		
		/**
		 * 
		 * @param x
		 * @param y
		 * @param z
		 */
		public MovePathElement3d(double x, double y,  double z) {
			super(PathElementType.MOVE_TO,
					Double.NaN, Double.NaN, Double.NaN,
					Double.NaN, Double.NaN, Double.NaN, 
					Double.NaN, Double.NaN, Double.NaN,
					x, y, z);
		}
		
		public MovePathElement3d(Point3d point) {
			super(PathElementType.MOVE_TO,
					null,
					null, 
					null,
					point);
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY()) && (this.getFromZ()==this.getToZ());
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return false;
		}
		
		@Override
		public void toArray(double[] array) {
			array[0] = this.getToX();
			array[1] = this.getToY();
			array[2] = this.getToZ();
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.getToX(), this.getToY(), this.getToZ()};
		}

		@Pure
		@Override
		public String toString() {
			return "MOVE("+ 
					this.getToX()+"x"+ 
					this.getToY()+"x"+ 
					this.getToZ()+")"; 
		}

		@Pure
		@Override
		public final PathElementType getType() {
			return PathElementType.MOVE_TO;
		}

		@Override
		public void toArray(DoubleProperty[] array) {
			array[0] = new SimpleDoubleProperty(this.getToX());
			array[1] = new SimpleDoubleProperty(this.getToY());
			array[2] = new SimpleDoubleProperty(this.getToZ());
		}


	}
	
	/** An element of the path that represents a <code>LINE_TO</code>.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class LinePathElement3d extends PathElement3d {
		
		private static final long serialVersionUID = -5878571187312098882L;
		
		/**
		 * 
		 * @param fromx
		 * @param fromy
		 * @param fromz
		 * @param tox
		 * @param toy
		 * @param toz
		 */
		public LinePathElement3d(double fromx, double fromy, double fromz, double tox, double toy, double toz) {
			super(PathElementType.LINE_TO,
					fromx, fromy, fromz,
					Double.NaN, Double.NaN, Double.NaN,
					Double.NaN, Double.NaN, Double.NaN,
					tox, toy, toz);
		}
		
		public LinePathElement3d(Point3d from, Point3d to) {
			super(PathElementType.LINE_TO,
					from,
					null,
					null,
					to);
		}

		@Pure
		@Override
		public final PathElementType getType() {
			return PathElementType.LINE_TO;
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY()) && (this.getFromZ()==this.getToZ());
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(double[] array) {
			array[0] = this.getToX();
			array[1] = this.getToY();
			array[2] = this.getToZ();
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.getToX(), this.getToY(), this.getToZ()};
		}

		@Pure
		@Override
		public String toString() {
			return "MOVE("+ 
					this.getToX()+"x"+ 
					this.getToY()+"x"+ 
					this.getToZ()+")"; 
		}

		@Override
		public void toArray(DoubleProperty[] array) {
			array[0] = new SimpleDoubleProperty(this.getToX());
			array[1] = new SimpleDoubleProperty(this.getToY());
			array[2] = new SimpleDoubleProperty(this.getToZ());
		}


	}
	
	/** An element of the path that represents a <code>QUAD_TO</code>.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class QuadPathElement3d extends PathElement3d {
		
		private static final long serialVersionUID = 5641358330446739160L;
		
		/**
		 * 
		 * @param fromx
		 * @param fromy
		 * @param fromz
		 * @param ctrlx
		 * @param ctrly
		 * @param ctrlz
		 * @param tox
		 * @param toy
		 * @param toz
		 */
		public QuadPathElement3d(double fromx, double fromy, double fromz, double ctrlx, double ctrly, double ctrlz, double tox, double toy, double toz) {
			super(PathElementType.QUAD_TO,
					fromx, fromy, fromz,
					ctrlx, ctrly, ctrlz,
					Double.NaN, Double.NaN, Double.NaN,
					tox, toy, toz);
		}
		
		public QuadPathElement3d(Point3d from, Point3d ctrl, Point3d to) {
			super(PathElementType.QUAD_TO,
					from,
					ctrl,
					null,
					to);
		}

		@Pure
		@Override
		public final PathElementType getType() {
			return PathElementType.QUAD_TO;
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY()) && (this.getFromZ()==this.getToZ()) &&
					(this.getCtrlX1()==this.getToX()) && (this.getCtrlY1()==this.getToY()) && (this.getCtrlZ1()==this.getToZ());
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(double[] array) {
			array[0] = this.getCtrlX1();
			array[1] = this.getCtrlY1();
			array[2] = this.getCtrlZ1();
			array[3] = this.getToX();
			array[4] = this.getToY();
			array[5] = this.getToZ();
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.getCtrlX1(), this.getCtrlY1(), this.getCtrlZ1(), this.getToX(), this.getToY(), this.getToZ()};
		}

		@Pure
		@Override
		public String toString() {
			return "QUAD("+ 
					this.getCtrlX1()+"x"+ 
					this.getCtrlY1()+"x"+ 
					this.getCtrlZ1()+"|"+ 
					this.getToX()+"x"+ 
					this.getToY()+"x"+ 
					this.getToZ()+")"; 
		}

		@Override
		public void toArray(DoubleProperty[] array) {
			array[0] = new SimpleDoubleProperty(this.getCtrlX1());
			array[1] = new SimpleDoubleProperty(this.getCtrlY1());
			array[2] = new SimpleDoubleProperty(this.getCtrlZ1());
			array[3] = new SimpleDoubleProperty(this.getToX());
			array[4] = new SimpleDoubleProperty(this.getToY());
			array[5] = new SimpleDoubleProperty(this.getToZ());
		}

	}

	/** An element of the path that represents a <code>CURVE_TO</code>.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class CurvePathElement3d extends PathElement3d {
		
		private static final long serialVersionUID = -1449309552719221756L;
		
		/**
		 * 
		 * @param fromx
		 * @param fromy
		 * @param fromz
		 * @param ctrlx1
		 * @param ctrly1
		 * @param ctrlz1
		 * @param ctrlx2
		 * @param ctrly2
		 * @param ctrlz2
		 * @param tox
		 * @param toy
		 * @param toz
		 */
		public CurvePathElement3d(double fromx, double fromy, double fromz, double ctrlx1, double ctrly1, double ctrlz1, double ctrlx2, double ctrly2, double ctrlz2, double tox, double toy, double toz) {
			super(PathElementType.CURVE_TO,
					fromx, fromy, fromz,
					ctrlx1, ctrly1, ctrlz1,
					ctrlx2, ctrly2, ctrlz2,
					tox, toy, toz);
		}
		
		public CurvePathElement3d(Point3d from, Point3d ctrl1, Point3d ctrl2, Point3d to) {
			super(PathElementType.CURVE_TO,
					from,
					ctrl1,
					ctrl2,
					to);
		}

		@Pure
		@Override
		public final PathElementType getType() {
			return PathElementType.CURVE_TO;
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY()) && (this.getFromZ()==this.getToZ()) &&
					(this.getCtrlX1()==this.getToX()) && (this.getCtrlY1()==this.getToY()) && (this.getCtrlZ1()==this.getToZ()) &&
					(this.getCtrlX2()==this.getToX()) && (this.getCtrlY2()==this.getToY()) && (this.getCtrlZ2()==this.getToZ());
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(double[] array) {
			array[0] = this.getCtrlX1();
			array[1] = this.getCtrlY1();
			array[2] = this.getCtrlZ1();
			array[3] = this.getCtrlX2();
			array[4] = this.getCtrlY2();
			array[5] = this.getCtrlZ2();
			array[6] = this.getToX();
			array[7] = this.getToY();
			array[8] = this.getToZ();
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.getCtrlX1(), this.getCtrlY1(), this.getCtrlZ1(), this.getCtrlX2(), this.getCtrlY2(), this.getCtrlZ2(), this.getToX(), this.getToY(), this.getToZ()};
		}

		@Pure
		@Override
		public String toString() {
			return "CURVE("+ 
					this.getCtrlX1()+"x"+ 
					this.getCtrlY1()+"x"+ 
					this.getCtrlZ1()+"|"+ 
					this.getCtrlX2()+"x"+ 
					this.getCtrlY2()+"x"+ 
					this.getCtrlZ2()+"|"+ 
					this.getToX()+"x"+ 
					this.getToY()+"x"+ 
					this.getToZ()+")"; 
		}

		@Override
		public void toArray(DoubleProperty[] array) {
			array[0] = new SimpleDoubleProperty(this.getCtrlX1());
			array[1] = new SimpleDoubleProperty(this.getCtrlY1());
			array[2] = new SimpleDoubleProperty(this.getCtrlZ1());
			array[3] = new SimpleDoubleProperty(this.getCtrlX2());
			array[4] = new SimpleDoubleProperty(this.getCtrlY2());
			array[5] = new SimpleDoubleProperty(this.getCtrlZ2());
			array[6] = new SimpleDoubleProperty(this.getToX());
			array[7] = new SimpleDoubleProperty(this.getToY());
			array[8] = new SimpleDoubleProperty(this.getToZ());
		}

	}

	/** An element of the path that represents a <code>CLOSE</code>.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class ClosePathElement3d extends PathElement3d {
		
		private static final long serialVersionUID = 4643537091880303796L;

		/**
		 * 
		 * @param fromx
		 * @param fromy
		 * @param fromz
		 * @param tox
		 * @param toy
		 * @param toz
		 */
		public ClosePathElement3d(double fromx, double fromy, double fromz, double tox, double toy, double toz) {
			super(PathElementType.CLOSE,
					fromx, fromy, fromz,
					Double.NaN, Double.NaN, Double.NaN,
					Double.NaN, Double.NaN, Double.NaN,
					tox, toy, toz);
		}
		
		public ClosePathElement3d(Point3d from, Point3d to) {
			super(PathElementType.CLOSE,
					from,
					null,
					null,
					to);
		}

		@Pure
		@Override
		public final PathElementType getType() {
			return PathElementType.CLOSE;
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY()) && (this.getFromZ()==this.getToZ());
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return false;
		}

		@Override
		public void toArray(double[] array) {
			//
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[0];
		}

		@Pure
		@Override
		public String toString() {
			return "CLOSE"; 
		}

		@Override
		public void toArray(DoubleProperty[] array) {
			// 
			
		}

	}


}
