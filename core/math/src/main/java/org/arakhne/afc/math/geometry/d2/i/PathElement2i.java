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
package org.arakhne.afc.math.geometry.d2.i;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.ai.PathElement2ai;
import org.eclipse.xtext.xbase.lib.Pure;

/** An element of the path with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class PathElement2i implements PathElement2ai {
	
	private static final long serialVersionUID = -7762354100984227855L;

	/** Create an instance of path element.
	 * 
	 * @param type is the type of the new element.
	 * @param lastX is the coordinate of the last point.
	 * @param lastY is the coordinate of the last point.
	 * @param coords are the coordinates.
	 * @return the instance of path element.
	 */
	@Pure
	public static PathElement2i newInstance(PathElementType type, int lastX, int lastY, int[] coords) {
		switch(type) {
		case MOVE_TO:
			return new MovePathElement2i(coords[0], coords[1]);
		case LINE_TO:
			return new LinePathElement2i(lastX, lastY, coords[0], coords[1]);
		case QUAD_TO:
			return new QuadPathElement2i(lastX, lastY, coords[0], coords[1], coords[2], coords[3]);
		case CURVE_TO:
			return new CurvePathElement2i(lastX, lastY, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
		case CLOSE:
			return new ClosePathElement2i(lastX, lastY, coords[0], coords[1]);
		default:
		}
		throw new IllegalArgumentException();
	}
	
	/** Type of the element.
	 */
	protected final PathElementType type;
	
	/** Target point.
	 */
	protected final int toX;
	
	/** Target point.
	 */
	protected final int toY;

	/**
	 * @param type is the type of the element.
	 * @param tox the x coordinate of the target point.
	 * @param toy the x coordinate of the target point.
	 */
	PathElement2i(PathElementType type, int tox, int toy) {
		assert(type!=null);
		this.type = type;
		this.toX = tox;
		this.toY = toy;
	}
	
	@Pure
	@Override
	public abstract boolean equals(Object obj);
	
	@Pure
	@Override
	public abstract int hashCode();

	@Override
	@Pure
	public final int getToX() {
		return this.toX;
	}

	@Override
	@Pure
	public final int getToY() {
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
	public abstract int[] toArray();

	/** An element of the path that represents a <code>MOVE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class MovePathElement2i extends PathElement2i {
		
		private static final long serialVersionUID = -574266368740822686L;

		/**
		 * @param x
		 * @param y
		 */
		public MovePathElement2i(int x, int y) {
			super(PathElementType.MOVE_TO, x, y);
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				PathElement2ai elt = (PathElement2ai) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY();
			} catch (Throwable exception) {
				//
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			return (bits ^ (bits >> 32));
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
			array[0] = this.toX;
			array[1] = this.toY;
		}
		
		@Override
		public void toArray(double[] array) {
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public int[] toArray() {
			return new int[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "MOVE("+ //$NON-NLS-1$
					this.toX+"x"+ //$NON-NLS-1$
					this.toY+")"; //$NON-NLS-1$
		}

		@Override
		public int getFromX() {
			return 0;
		}

		@Override
		public int getFromY() {
			return 0;
		}

		@Override
		public int getCtrlX1() {
			return 0;
		}

		@Override
		public int getCtrlY1() {
			return 0;
		}

		@Override
		public int getCtrlX2() {
			return 0;
		}

		@Override
		public int getCtrlY2() {
			return 0;
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
	static class LinePathElement2i extends PathElement2i {
		
		private static final long serialVersionUID = 7733931118894880566L;

		private final int fromX;
		
		private final int fromY;
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param tox
		 * @param toy
		 */
		public LinePathElement2i(int fromx, int fromy, int tox, int toy) {
			super(PathElementType.LINE_TO, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				PathElement2ai elt = (PathElement2ai) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
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
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			return (bits ^ (bits >> 32));
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
			array[0] = this.toX;
			array[1] = this.toY;
		}
		
		@Override
		public void toArray(double[] array) {
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public int[] toArray() {
			return new int[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "LINE("+ //$NON-NLS-1$
					this.toX+"x"+ //$NON-NLS-1$
					this.toY+")"; //$NON-NLS-1$
		}

		@Override
		public int getFromX() {
			return this.fromX;
		}

		@Override
		public int getFromY() {
			return this.fromY;
		}

		@Override
		public int getCtrlX1() {
			return 0;
		}

		@Override
		public int getCtrlY1() {
			return 0;
		}

		@Override
		public int getCtrlX2() {
			return 0;
		}

		@Override
		public int getCtrlY2() {
			return 0;
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
	static class QuadPathElement2i extends PathElement2i {
		
		private static final long serialVersionUID = 3335394189817197203L;

		private final int fromX;
		
		private final int fromY;

		private final int ctrlX;
		
		private final int ctrlY;

		/**
		 * @param fromx
		 * @param fromy
		 * @param ctrlx
		 * @param ctrly
		 * @param tox
		 * @param toy
		 */
		public QuadPathElement2i(int fromx, int fromy, int ctrlx, int ctrly, int tox, int toy) {
			super(PathElementType.QUAD_TO, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
			this.ctrlX = ctrlx;
			this.ctrlY = ctrly;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				PathElement2ai elt = (PathElement2ai) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getCtrlX1() == elt.getCtrlX1()
						&& getCtrlY1() == elt.getCtrlY1()
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
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			bits = 31 * bits + getCtrlX1();
			bits = 31 * bits + getCtrlY1();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			return (bits ^ (bits >> 32));
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
			array[0] = this.ctrlX;
			array[1] = this.ctrlY;
			array[2] = this.toX;
			array[3] = this.toY;
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
		public int[] toArray() {
			return new int[] {this.ctrlX, this.ctrlY, this.toX, this.toY};
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
		public int getFromX() {
			return this.fromX;
		}

		@Override
		public int getFromY() {
			return this.fromY;
		}

		@Override
		public int getCtrlX1() {
			return this.ctrlX;
		}

		@Override
		public int getCtrlY1() {
			return this.ctrlY;
		}

		@Override
		public int getCtrlX2() {
			return 0;
		}

		@Override
		public int getCtrlY2() {
			return 0;
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
	static class CurvePathElement2i extends PathElement2i {
		
		private static final long serialVersionUID = 7009674542781709373L;

		private final int fromX;
		
		private final int fromY;

		private final int ctrlX1;
		
		private final int ctrlY1;

		private final int ctrlX2;
		
		private final int ctrlY2;

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
		public CurvePathElement2i(int fromx, int fromy, int ctrlx1, int ctrly1, int ctrlx2, int ctrly2, int tox, int toy) {
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
		public boolean equals(Object obj) {
			try {
				PathElement2ai elt = (PathElement2ai) obj;
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
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			bits = 31 * bits + getCtrlX1();
			bits = 31 * bits + getCtrlY1();
			bits = 31 * bits + getCtrlX2();
			bits = 31 * bits + getCtrlY2();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			return (bits ^ (bits >> 32));
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
			array[0] = this.ctrlX1;
			array[1] = this.ctrlY1;
			array[2] = this.ctrlX2;
			array[3] = this.ctrlY2;
			array[4] = this.toX;
			array[5] = this.toY;
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
		public int[] toArray() {
			return new int[] {this.ctrlX1, this.ctrlY1, this.ctrlX2, this.ctrlY2, this.toX, this.toY};
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
		public int getFromX() {
			return this.fromX;
		}

		@Override
		public int getFromY() {
			return this.fromY;
		}

		@Override
		public int getCtrlX1() {
			return this.ctrlX1;
		}

		@Override
		public int getCtrlY1() {
			return this.ctrlY1;
		}

		@Override
		public int getCtrlX2() {
			return this.ctrlX2;
		}

		@Override
		public int getCtrlY2() {
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
	static class ClosePathElement2i extends PathElement2i {
		
		private static final long serialVersionUID = -8709004906872207794L;

		private final int fromX;
		
		private final int fromY;
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param tox
		 * @param toy
		 */
		public ClosePathElement2i(int fromx, int fromy, int tox, int toy) {
			super(PathElementType.CLOSE, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
			
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				PathElement2ai elt = (PathElement2ai) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
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
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			return (bits ^ (bits >> 32));
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
		public int[] toArray() {
			return new int[0];
		}

		@Pure
		@Override
		public String toString() {
			return "CLOSE"; //$NON-NLS-1$
		}

		@Override
		public int getFromX() {
			return this.fromX;
		}

		@Override
		public int getFromY() {
			return this.fromY;
		}

		@Override
		public int getCtrlX1() {
			return 0;
		}

		@Override
		public int getCtrlY1() {
			return 0;
		}

		@Override
		public int getCtrlX2() {
			return 0;
		}

		@Override
		public int getCtrlY2() {
			return 0;
		}

	}

}