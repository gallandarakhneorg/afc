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

/** An element of the path.
 *
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPathElement3F implements AbstractPathElement3X {

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
	public static AbstractPathElement3F newInstance(PathElementType type, double lastX, double lastY, double lastZ, double[] coords) {
		switch(type) {
		case MOVE_TO:
			return new MovePathElement3f(coords[0], coords[1], coords[2]);
		case LINE_TO:
			return new LinePathElement3f(lastX, lastY, lastZ, coords[0], coords[1], coords[2]);
		case QUAD_TO:
			return new QuadPathElement3f(lastX, lastY, lastZ, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
		case CURVE_TO:
			return new CurvePathElement3f(lastX, lastY, lastZ, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5], coords[6], coords[7], coords[8]);
		case CLOSE:
			return new ClosePathElement3f(lastX, lastY, lastZ, coords[0], coords[1], coords[2]);
		default:
		}
		throw new IllegalArgumentException();
	}
	
	/** Type of the path element.
	 */
	protected final PathElementType type;
	
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
	public AbstractPathElement3F(PathElementType type1, double fromx, double fromy, double fromz, double ctrlx1, double ctrly1, double ctrlz1, double ctrlx2, double ctrly2, double ctrlz2, double tox, double toy, double toz) {
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
	public static class MovePathElement3f extends PathElement3f {
		
		private static final long serialVersionUID = -5596181248741970433L;
		
		/**
		 * 
		 * @param x
		 * @param y
		 * @param z
		 */
		public MovePathElement3f(double x, double y,  double z) {
			super(PathElementType.MOVE_TO,
					Double.NaN, Double.NaN, Double.NaN,
					Double.NaN, Double.NaN, Double.NaN, 
					Double.NaN, Double.NaN, Double.NaN,
					x, y, z);
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
			return "MOVE("+ //$NON-NLS-1$
					this.getToX()+"x"+ //$NON-NLS-1$
					this.getToY()+"x"+ //$NON-NLS-1$
					this.getToZ()+")"; //$NON-NLS-1$
		}

		@Pure
		@Override
		public final PathElementType getType() {
			return PathElementType.MOVE_TO;
		}


	}
	
	/** An element of the path that represents a <code>LINE_TO</code>.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class LinePathElement3f extends PathElement3f {
		
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
		public LinePathElement3f(double fromx, double fromy, double fromz, double tox, double toy, double toz) {
			super(PathElementType.LINE_TO,
					fromx, fromy, fromz,
					Double.NaN, Double.NaN, Double.NaN,
					Double.NaN, Double.NaN, Double.NaN,
					tox, toy, toz);
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
			return "MOVE("+ //$NON-NLS-1$
					this.getToX()+"x"+ //$NON-NLS-1$
					this.getToY()+"x"+ //$NON-NLS-1$
					this.getToZ()+")"; //$NON-NLS-1$
		}


	}
	
	/** An element of the path that represents a <code>QUAD_TO</code>.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class QuadPathElement3f extends PathElement3f {
		
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
		public QuadPathElement3f(double fromx, double fromy, double fromz, double ctrlx, double ctrly, double ctrlz, double tox, double toy, double toz) {
			super(PathElementType.QUAD_TO,
					fromx, fromy, fromz,
					ctrlx, ctrly, ctrlz,
					Double.NaN, Double.NaN, Double.NaN,
					tox, toy, toz);
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
			return "QUAD("+ //$NON-NLS-1$
					this.getCtrlX1()+"x"+ //$NON-NLS-1$
					this.getCtrlY1()+"x"+ //$NON-NLS-1$
					this.getCtrlZ1()+"|"+ //$NON-NLS-1$
					this.getToX()+"x"+ //$NON-NLS-1$
					this.getToY()+"x"+ //$NON-NLS-1$
					this.getToZ()+")"; //$NON-NLS-1$
		}

	}

	/** An element of the path that represents a <code>CURVE_TO</code>.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class CurvePathElement3f extends PathElement3f {
		
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
		public CurvePathElement3f(double fromx, double fromy, double fromz, double ctrlx1, double ctrly1, double ctrlz1, double ctrlx2, double ctrly2, double ctrlz2, double tox, double toy, double toz) {
			super(PathElementType.CURVE_TO,
					fromx, fromy, fromz,
					ctrlx1, ctrly1, ctrlz1,
					ctrlx2, ctrly2, ctrlz2,
					tox, toy, toz);
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
			return "CURVE("+ //$NON-NLS-1$
					this.getCtrlX1()+"x"+ //$NON-NLS-1$
					this.getCtrlY1()+"x"+ //$NON-NLS-1$
					this.getCtrlZ1()+"|"+ //$NON-NLS-1$
					this.getCtrlX2()+"x"+ //$NON-NLS-1$
					this.getCtrlY2()+"x"+ //$NON-NLS-1$
					this.getCtrlZ2()+"|"+ //$NON-NLS-1$
					this.getToX()+"x"+ //$NON-NLS-1$
					this.getToY()+"x"+ //$NON-NLS-1$
					this.getToZ()+")"; //$NON-NLS-1$
		}

	}

	/** An element of the path that represents a <code>CLOSE</code>.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class ClosePathElement3f extends PathElement3f {
		
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
		public ClosePathElement3f(double fromx, double fromy, double fromz, double tox, double toy, double toz) {
			super(PathElementType.CLOSE,
					fromx, fromy, fromz,
					Double.NaN, Double.NaN, Double.NaN,
					Double.NaN, Double.NaN, Double.NaN,
					tox, toy, toz);
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
			return "CLOSE"; //$NON-NLS-1$
		}

	}


}
