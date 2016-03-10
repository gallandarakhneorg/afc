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
package org.arakhne.afc.math.geometry.d2.fp;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.afp.PathElement2afp;
import org.eclipse.xtext.xbase.lib.Pure;

/** An element of the path with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class PathElement2fp implements PathElement2afp {
	
	private static final long serialVersionUID = -9217295344283468162L;

	/** Create an instance of path element.
	 * 
	 * @param type is the type of the new element.
	 * @param lastX is the coordinate of the last point.
	 * @param lastY is the coordinate of the last point.
	 * @param coords are the coordinates.
	 * @return the instance of path element.
	 */
	@Pure
	public static PathElement2fp newInstance(PathElementType type, double lastX, double lastY, double[] coords) {
		switch(type) {
		case MOVE_TO:
			return new MovePathElement2fp(coords[0], coords[1]);
		case LINE_TO:
			return new LinePathElement2fp(lastX, lastY, coords[0], coords[1]);
		case QUAD_TO:
			return new QuadPathElement2fp(lastX, lastY, coords[0], coords[1], coords[2], coords[3]);
		case CURVE_TO:
			return new CurvePathElement2fp(lastX, lastY, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
		case CLOSE:
			return new ClosePathElement2fp(lastX, lastY, coords[0], coords[1]);
		default:
		}
		throw new IllegalArgumentException();
	}
	
	/** Type of the element.
	 */
	protected final PathElementType type;
	
	/** Target point.
	 */
	protected final double toX;
	
	/** Target point.
	 */
	protected final double toY;

	/**
	 * @param type is the type of the element.
	 * @param tox the x coordinate of the target point.
	 * @param toy the x coordinate of the target point.
	 */
	PathElement2fp(PathElementType type, double tox, double toy) {
		assert(type!=null);
		this.type = type;
		this.toX = tox;
		this.toY = toy;
	}
	
	@Pure
	@Override
	public boolean equals(Object obj) {
		try {
			PathElement2afp elt = (PathElement2afp) obj;
			return getType() == elt.getType()
					&& getToX() == elt.getToX()
					&& getToY() == elt.getToY()
					&& getCtrlX1() == elt.getCtrlX1()
					&& getCtrlY1() == elt.getCtrlY1()
					&& getCtrlX2() == elt.getCtrlX2()
					&& getCtrlY2() == elt.getCtrlY2()
					&& getFromX() == elt.getFromX()
					&& getFromY() == elt.getFromY();
		} catch (Throwable exception) {
			//
		}
		return false;
	}
	
	@Pure
	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + this.type.ordinal();
		bits = 31L * bits + Double.doubleToLongBits(getToX());
		bits = 31L * bits + Double.doubleToLongBits(getToY());
		bits = 31L * bits + Double.doubleToLongBits(getCtrlX1());
		bits = 31L * bits + Double.doubleToLongBits(getCtrlY1());
		bits = 31L * bits + Double.doubleToLongBits(getCtrlX2());
		bits = 31L * bits + Double.doubleToLongBits(getCtrlY2());
		bits = 31L * bits + Double.doubleToLongBits(getFromX());
		bits = 31L * bits + Double.doubleToLongBits(getFromY());
		return (int) (bits ^ (bits >> 32));
	}

	@Override
	@Pure
	public final double getToX() {
		return this.toX;
	}

	@Override
	@Pure
	public final double getToY() {
		return this.toY;
	}

	@Pure
	@Override
	public final PathElementType getType() {
		return this.type;
	}


	/** Copy the coords into an array, except the source point.
	 * 
	 * @return the array of the points, except the source point.
	 */
	@Pure
	public abstract double[] toArray();

	/** An element of the path that represents a <code>MOVE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class MovePathElement2fp extends PathElement2fp {
		
		private static final long serialVersionUID = -399575136145167775L;

		/**
		 * @param x
		 * @param y
		 */
		public MovePathElement2fp(double x, double y) {
			super(PathElementType.MOVE_TO, x, y);
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return true;
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return false;
		}
		
		@Override
		public void toArray(int[] array) {
			array[0] = (int) this.toX;
			array[1] = (int) this.toY;
		}
		
		@Override
		public void toArray(double[] array) {
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "MOVE("+ //$NON-NLS-1$
					this.toX+"x"+ //$NON-NLS-1$
					this.toY+")"; //$NON-NLS-1$
		}

		@Override
		public double getFromX() {
			return Double.NaN;
		}

		@Override
		public double getFromY() {
			return Double.NaN;
		}

		@Override
		public double getCtrlX1() {
			return Double.NaN;
		}

		@Override
		public double getCtrlY1() {
			return Double.NaN;
		}

		@Override
		public double getCtrlX2() {
			return Double.NaN;
		}

		@Override
		public double getCtrlY2() {
			return Double.NaN;
		}

	}
	
	/** An element of the path that represents a <code>LINE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class LinePathElement2fp extends PathElement2fp {
		
		private static final long serialVersionUID = 8423845888008307447L;

		private final double fromX;
		
		private final double fromY;
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param tox
		 * @param toy
		 */
		public LinePathElement2fp(double fromx, double fromy, double tox, double toy) {
			super(PathElementType.LINE_TO, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.fromX==this.toX) && (this.fromY==this.toY);
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			array[0] = (int) this.toX;
			array[1] = (int) this.toY;
		}
		
		@Override
		public void toArray(double[] array) {
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "LINE("+ //$NON-NLS-1$
					this.toX+"x"+ //$NON-NLS-1$
					this.toY+")"; //$NON-NLS-1$
		}

		@Override
		public double getFromX() {
			return this.fromX;
		}

		@Override
		public double getFromY() {
			return this.fromY;
		}

		@Override
		public double getCtrlX1() {
			return Double.NaN;
		}

		@Override
		public double getCtrlY1() {
			return Double.NaN;
		}

		@Override
		public double getCtrlX2() {
			return Double.NaN;
		}

		@Override
		public double getCtrlY2() {
			return Double.NaN;
		}

	}
	
	/** An element of the path that represents a <code>QUAD_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class QuadPathElement2fp extends PathElement2fp {
		
		private static final long serialVersionUID = 6092391345111872481L;

		private final double fromX;
		
		private final double fromY;

		private final double ctrlX;
		
		private final double ctrlY;

		/**
		 * @param fromx
		 * @param fromy
		 * @param ctrlx
		 * @param ctrly
		 * @param tox
		 * @param toy
		 */
		public QuadPathElement2fp(double fromx, double fromy, double ctrlx, double ctrly, double tox, double toy) {
			super(PathElementType.QUAD_TO, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
			this.ctrlX = ctrlx;
			this.ctrlY = ctrly;
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.fromX==this.toX) && (this.fromY==this.toY) &&
					(this.ctrlX==this.toX) && (this.ctrlY==this.toY);
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			array[0] = (int) this.ctrlX;
			array[1] = (int) this.ctrlY;
			array[2] = (int) this.toX;
			array[3] = (int) this.toY;
		}
		
		@Override
		public void toArray(double[] array) {
			array[0] = this.ctrlX;
			array[1] = this.ctrlY;
			array[2] = this.toX;
			array[3] = this.toY;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.ctrlX, this.ctrlY, this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "QUAD("+ //$NON-NLS-1$
					this.ctrlX+"x"+ //$NON-NLS-1$
					this.ctrlY+"|"+ //$NON-NLS-1$
					this.toX+"x"+ //$NON-NLS-1$
					this.toY+")"; //$NON-NLS-1$
		}

		@Override
		public double getFromX() {
			return this.fromX;
		}

		@Override
		public double getFromY() {
			return this.fromY;
		}

		@Override
		public double getCtrlX1() {
			return this.ctrlX;
		}

		@Override
		public double getCtrlY1() {
			return this.ctrlY;
		}

		@Override
		public double getCtrlX2() {
			return Double.NaN;
		}

		@Override
		public double getCtrlY2() {
			return Double.NaN;
		}

	}

	/** An element of the path that represents a <code>CURVE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class CurvePathElement2fp extends PathElement2fp {
		
		private static final long serialVersionUID = -2831895270995173092L;

		private final double fromX;
		
		private final double fromY;

		private final double ctrlX1;
		
		private final double ctrlY1;

		private final double ctrlX2;
		
		private final double ctrlY2;

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
		public CurvePathElement2fp(double fromx, double fromy, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double tox, double toy) {
			super(PathElementType.CURVE_TO, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
			this.ctrlX1 = ctrlx1;
			this.ctrlY1 = ctrly1;
			this.ctrlX2 = ctrlx2;
			this.ctrlY2 = ctrly2;
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.fromX==this.toX) && (this.fromY==this.toY) &&
					(this.ctrlX1==this.toX) && (this.ctrlY1==this.toY) &&
					(this.ctrlX2==this.toX) && (this.ctrlY2==this.toY);
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			array[0] = (int) this.ctrlX1;
			array[1] = (int) this.ctrlY1;
			array[2] = (int) this.ctrlX2;
			array[3] = (int) this.ctrlY2;
			array[4] = (int) this.toX;
			array[5] = (int) this.toY;
		}
		
		@Override
		public void toArray(double[] array) {
			array[0] = this.ctrlX1;
			array[1] = this.ctrlY1;
			array[2] = this.ctrlX2;
			array[3] = this.ctrlY2;
			array[4] = this.toX;
			array[5] = this.toY;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.ctrlX1, this.ctrlY1, this.ctrlX2, this.ctrlY2, this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "CURVE("+ //$NON-NLS-1$
					this.ctrlX1+"x"+ //$NON-NLS-1$
					this.ctrlY1+"|"+ //$NON-NLS-1$
					this.ctrlX2+"x"+ //$NON-NLS-1$
					this.ctrlY2+"|"+ //$NON-NLS-1$
					this.toX+"x"+ //$NON-NLS-1$
					this.toY+")"; //$NON-NLS-1$
		}

		@Override
		public double getFromX() {
			return this.fromX;
		}

		@Override
		public double getFromY() {
			return this.fromY;
		}

		@Override
		public double getCtrlX1() {
			return this.ctrlX1;
		}

		@Override
		public double getCtrlY1() {
			return this.ctrlY1;
		}

		@Override
		public double getCtrlX2() {
			return this.ctrlX1;
		}

		@Override
		public double getCtrlY2() {
			return this.ctrlY2;
		}

	}

	/** An element of the path that represents a <code>CLOSE</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class ClosePathElement2fp extends PathElement2fp {
		
		private static final long serialVersionUID = 5324688417590599323L;

		private final double fromX;
		
		private final double fromY;
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param tox
		 * @param toy
		 */
		public ClosePathElement2fp(double fromx, double fromy, double tox, double toy) {
			super(PathElementType.CLOSE, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
			
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.fromX==this.toX) && (this.fromY==this.toY);
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return false;
		}

		@Override
		public void toArray(int[] array) {
			//
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

		@Override
		public double getFromX() {
			return this.fromX;
		}

		@Override
		public double getFromY() {
			return this.fromY;
		}

		@Override
		public double getCtrlX1() {
			return Double.NaN;
		}

		@Override
		public double getCtrlY1() {
			return Double.NaN;
		}

		@Override
		public double getCtrlX2() {
			return Double.NaN;
		}

		@Override
		public double getCtrlY2() {
			return Double.NaN;
		}

	}

}