/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.PathElementType;

/** An element of the path.
 *
 * @author $Author: galland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPathElement2F implements AbstractPathElement2X {
	
	private static final long serialVersionUID = 8963281073370254033L;
	
	/** Create an instance of path element.
	 * 
	 * @param type is the type of the new element.
	 * @param lastX is the coordinate of the last point.
	 * @param lastY is the coordinate of the last point.
	 * @param coords are the coordinates.
	 * @return the instance of path element.
	 */
	public static AbstractPathElement2F newInstance(PathElementType type, double lastX, double lastY, double[] coords) {
		switch(type) {
		case MOVE_TO:
			return new MovePathElement2f(coords[0], coords[1]);
		case LINE_TO:
			return new LinePathElement2f(lastX, lastY, coords[0], coords[1]);
		case QUAD_TO:
			return new QuadPathElement2f(lastX, lastY, coords[0], coords[1], coords[2], coords[3]);
		case CURVE_TO:
			return new CurvePathElement2f(lastX, lastY, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
		case CLOSE:
			return new ClosePathElement2f(lastX, lastY, coords[0], coords[1]);
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
	 * @param ctrlx1 is the first control point.
	 * @param ctrly1 is the first control point.
	 * @param ctrlx2 is the first control point.
	 * @param ctrly2 is the first control point.
	 * @param tox is the target point.
	 * @param toy is the target point.
	 */
	public AbstractPathElement2F(PathElementType type1, double fromx, double fromy, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double tox, double toy) {
		assert(type1!=null);
		this.type = type1;
		this.setFromX(fromx);
		this.setFromY(fromy);
		this.setCtrlX1(ctrlx1);
		this.setCtrlY1(ctrly1);
		this.setCtrlX2(ctrlx2);
		this.setCtrlY2(ctrly2);
		this.setToX(tox);
		this.setToY(toy);
	}

	
	
	

	/** An element of the path that represents a <code>MOVE_TO</code>.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class MovePathElement2f extends PathElement2f {
		
		private static final long serialVersionUID = -5596181248741970433L;
		
		/**
		 * @param x
		 * @param y
		 */
		public MovePathElement2f(double x, double y) {
			super(PathElementType.MOVE_TO,
					Double.NaN, Double.NaN,
					Double.NaN, Double.NaN,
					Double.NaN, Double.NaN,
					x, y);
		}

		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY());
		}

		@Override
		public boolean isDrawable() {
			return false;
		}
		
		@Override
		public void toArray(double[] array) {
			array[0] = this.getToX();
			array[1] = this.getToY();
		}
		
		@Override
		public double[] toArray() {
			return new double[] {this.getToX(), this.getToY()};
		}

		@Override
		public String toString() {
			return "MOVE("+ //$NON-NLS-1$
					this.getToX()+"x"+ //$NON-NLS-1$
					this.getToY()+")"; //$NON-NLS-1$
		}

		@Override
		public final PathElementType getType() {
			return PathElementType.MOVE_TO;
		}


	}
	
	/** An element of the path that represents a <code>LINE_TO</code>.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class LinePathElement2f extends PathElement2f {
		
		private static final long serialVersionUID = -5878571187312098882L;
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param tox
		 * @param toy
		 */
		public LinePathElement2f(double fromx, double fromy, double tox, double toy) {
			super(PathElementType.LINE_TO,
					fromx, fromy,
					Double.NaN, Double.NaN,
					Double.NaN, Double.NaN,
					tox, toy);
		}
		
		@Override
		public final PathElementType getType() {
			return PathElementType.LINE_TO;
		}

		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY());
		}

		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(double[] array) {
			array[0] = this.getToX();
			array[1] = this.getToY();
		}
		
		@Override
		public double[] toArray() {
			return new double[] {this.getToX(), this.getToY()};
		}

		@Override
		public String toString() {
			return "LINE("+ //$NON-NLS-1$
					this.getToX()+"x"+ //$NON-NLS-1$
					this.getToY()+")"; //$NON-NLS-1$
		}


	}
	
	/** An element of the path that represents a <code>QUAD_TO</code>.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class QuadPathElement2f extends PathElement2f {
		
		private static final long serialVersionUID = 5641358330446739160L;
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param ctrlx
		 * @param ctrly
		 * @param tox
		 * @param toy
		 */
		public QuadPathElement2f(double fromx, double fromy, double ctrlx, double ctrly, double tox, double toy) {
			super(PathElementType.QUAD_TO,
					fromx, fromy,
					ctrlx, ctrly,
					Double.NaN, Double.NaN,
					tox, toy);
		}
		
		@Override
		public final PathElementType getType() {
			return PathElementType.QUAD_TO;
		}

		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY()) &&
					(this.getCtrlX1()==this.getToX()) && (this.getCtrlY1()==this.getToY());
		}

		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(double[] array) {
			array[0] = this.getCtrlX1();
			array[1] = this.getCtrlY1();
			array[2] = this.getToX();
			array[3] = this.getToY();
		}
		
		@Override
		public double[] toArray() {
			return new double[] {this.getCtrlX1(), this.getCtrlY1(), this.getToX(), this.getToY()};
		}
		
		@Override
		public String toString() {
			return "QUAD("+ //$NON-NLS-1$
					this.getCtrlX1()+"x"+ //$NON-NLS-1$
					this.getCtrlY1()+"|"+ //$NON-NLS-1$
					this.getToX()+"x"+ //$NON-NLS-1$
					this.getToY()+")"; //$NON-NLS-1$
		}

		

	}

	/** An element of the path that represents a <code>CURVE_TO</code>.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class CurvePathElement2f extends PathElement2f {
		
		private static final long serialVersionUID = -1449309552719221756L;

		
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param ctrlx1
		 * @param ctrly1
		 * @param ctrlx2
		 * @param ctrly2
		 * @param tox
		 * @param toy
		 */
		public CurvePathElement2f(double fromx, double fromy, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double tox, double toy) {
			super(PathElementType.CURVE_TO,
					fromx, fromy,
					ctrlx1, ctrly1,
					ctrlx2, ctrly2,
					tox, toy);
		}
		
		@Override
		public final PathElementType getType() {
			return PathElementType.CURVE_TO;
		}

		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY()) &&
					(this.getCtrlX1()==this.getToX()) && (this.getCtrlY1()==this.getToY()) &&
					(this.getCtrlX2()==this.getToX()) && (this.getCtrlY2()==this.getToY());
		}

		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(double[] array) {
			array[0] = this.getCtrlX1();
			array[1] = this.getCtrlY1();
			array[2] = this.getCtrlX2();
			array[3] = this.getCtrlY2();
			array[4] = this.getToX();
			array[5] = this.getToY();
		}
		
		@Override
		public double[] toArray() {
			return new double[] {this.getCtrlX1(), this.getCtrlY1(), this.getCtrlX2(), this.getCtrlY2(), this.getToX(), this.getToY()};
		}

		@Override
		public String toString() {
			return "CURVE("+ //$NON-NLS-1$
					this.getCtrlX1()+"x"+ //$NON-NLS-1$
					this.getCtrlY1()+"|"+ //$NON-NLS-1$
					this.getCtrlX2()+"x"+ //$NON-NLS-1$
					this.getCtrlY2()+"|"+ //$NON-NLS-1$
					this.getToX()+"x"+ //$NON-NLS-1$
					this.getToY()+")"; //$NON-NLS-1$
		}

		

	}

	/** An element of the path that represents a <code>CLOSE</code>.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class ClosePathElement2f extends PathElement2f {
		
		private static final long serialVersionUID = 4643537091880303796L;

		/**
		 * @param fromx
		 * @param fromy
		 * @param tox
		 * @param toy
		 */
		public ClosePathElement2f(double fromx, double fromy, double tox, double toy) {
			super(PathElementType.CLOSE,
					fromx, fromy,
					Double.NaN, Double.NaN,
					Double.NaN, Double.NaN,
					tox, toy);
		}
		
		@Override
		public final PathElementType getType() {
			return PathElementType.CLOSE;
		}

		@Override
		public boolean isEmpty() {
			return (this.getFromX()==this.getToX()) && (this.getFromY()==this.getToY());
		}
		
		@Override
		public boolean isDrawable() {
			return false;
		}

		@Override
		public void toArray(double[] array) {
			//
		}
		
		@Override
		public double[] toArray() {
			return new double[0];
		}
		
		@Override
		public String toString() {
			return "CLOSE"; //$NON-NLS-1$
		}

	}

}