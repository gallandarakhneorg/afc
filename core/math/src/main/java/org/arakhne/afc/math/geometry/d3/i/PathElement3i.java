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
package org.arakhne.afc.math.geometry.d3.i;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.ai.PathElement3ai;
import org.eclipse.xtext.xbase.lib.Pure;

/** An element of the path with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class PathElement3i implements PathElement3ai {
	
	private static final long serialVersionUID = -7762354100984227855L;

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
	public static PathElement3i newInstance(PathElementType type, int lastX, int lastY, int lastZ, int[] coords) {
		assert (type != null) : "Path element type must be not null"; //$NON-NLS-1$
		assert (coords != null) : "Array of coordinates must be not null"; //$NON-NLS-1$
		assert (coords.length >= 3) : "Size of the array of coordinates is too small"; //$NON-NLS-1$
		switch(type) {
		case MOVE_TO:
			return new MovePathElement3i(coords[0], coords[1], coords[2]);
		case LINE_TO:
			return new LinePathElement3i(lastX, lastY, lastZ, coords[0], coords[1], coords[2]);
		case QUAD_TO:
			assert (coords.length >= 6) : "Size of the array of coordinates is too small"; //$NON-NLS-1$
			return new QuadPathElement3i(lastX, lastY, lastZ, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
		case CURVE_TO:
			assert (coords.length >= 9) : "Size of the array of coordinates is too small"; //$NON-NLS-1$
			return new CurvePathElement3i(lastX, lastY, lastZ, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5], coords[6], coords[7], coords[8]);
		case CLOSE:
			return new ClosePathElement3i(lastX, lastY, lastZ, coords[0], coords[1], coords[3]);
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
	
	/** Target point.
	 */
	protected final int toZ;

	/**
	 * @param type is the type of the element.
	 * @param tox the x coordinate of the target point.
	 * @param toy the x coordinate of the target point.
	 * @param toy the z coordinate of the target point.
	 */
	PathElement3i(PathElementType type, int tox, int toy, int toz) {
		assert (type != null) : "Path element type must be not null"; //$NON-NLS-1$
		this.type = type;
		this.toX = tox;
		this.toY = toy;
		this.toZ = toz;
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

	@Override
	@Pure
	public final int getToZ() {
		return this.toZ;
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
	static class MovePathElement3i extends PathElement3i {
		
		private static final long serialVersionUID = -574266368740822686L;

		/**
		 * @param x
		 * @param y
		 * @param z
		 */
		public MovePathElement3i(int x, int y, int z) {
			super(PathElementType.MOVE_TO, x, y, z);
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				PathElement3ai elt = (PathElement3ai) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getToZ() == elt.getToZ();
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
			bits = 31 * bits + getToZ();
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
			assert (array != null) : "Array must be not null"; //$NON-NLS-1$
			assert (array.length >= 3) : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
			array[2] = this.toZ;
		}
		
		@Override
		public void toArray(double[] array) {
			assert (array != null) : "Array must be not null"; //$NON-NLS-1$
			assert (array.length >= 3) : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
			array[2] = this.toZ;
		}

		@Pure
		@Override
		public int[] toArray() {
			return new int[] {this.toX, this.toY, this.toZ};
		}

		@Pure
		@Override
		public String toString() {
			return "MOVE("+ //$NON-NLS-1$
					this.toX+"x"+ //$NON-NLS-1$
					this.toY+"x"+ //$NON-NLS-1$
					this.toZ+")"; //$NON-NLS-1$
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
		public int getFromZ() {
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
		public int getCtrlZ1() {
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

		@Override
		public int getCtrlZ2() {
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
	static class LinePathElement3i extends PathElement3i {
		
		private static final long serialVersionUID = 7733931118894880566L;

		private final int fromX;
		
		private final int fromY;
		
		private final int fromZ;
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param tox
		 * @param toy
		 */
		public LinePathElement3i(int fromx, int fromy, int fromz, int tox, int toy, int toz) {
			super(PathElementType.LINE_TO, tox, toy, toz);
			this.fromX = fromx;
			this.fromY = fromy;
			this.fromZ = fromz;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				PathElement3ai elt = (PathElement3ai) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getToZ() == elt.getToZ()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY()
						&& getFromZ() == elt.getFromZ();
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
			bits = 31 * bits + getToZ();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			bits = 31 * bits + getFromZ();
			return (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.fromX==this.toX) && (this.fromY==this.toY) && (this.fromZ==this.toZ);
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			assert (array != null) : "Array must be not null"; //$NON-NLS-1$
			assert (array.length >= 3) : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
			array[2] = this.toZ;
		}
		
		@Override
		public void toArray(double[] array) {
			assert (array != null) : "Array must be not null"; //$NON-NLS-1$
			assert (array.length >= 3) : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
			array[2] = this.toZ;
		}

		@Pure
		@Override
		public int[] toArray() {
			return new int[] {this.toX, this.toY, this.toZ};
		}

		@Pure
		@Override
		public String toString() {
			return "LINE("+ //$NON-NLS-1$
					this.toX+"x"+ //$NON-NLS-1$
					this.toY+"x"+ //$NON-NLS-1$
					this.toZ+")"; //$NON-NLS-1$
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
		public int getFromZ() {
			return this.fromZ;
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
		public int getCtrlZ1() {
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

		@Override
		public int getCtrlZ2() {
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
	static class QuadPathElement3i extends PathElement3i {
		
		private static final long serialVersionUID = 3335394189817197203L;

		private final int fromX;
		
		private final int fromY;

		private final int fromZ;

		private final int ctrlX;
		
		private final int ctrlY;
		
		private final int ctrlZ;

		/**
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
		public QuadPathElement3i(int fromx, int fromy, int fromz, int ctrlx, int ctrly, int ctrlz, int tox, int toy, int toz) {
			super(PathElementType.QUAD_TO, tox, toy, toz);
			this.fromX = fromx;
			this.fromY = fromy;
			this.fromZ = fromz;
			this.ctrlX = ctrlx;
			this.ctrlY = ctrly;
			this.ctrlZ = ctrlz;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				PathElement3ai elt = (PathElement3ai) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getToZ() == elt.getToZ()
						&& getCtrlX1() == elt.getCtrlX1()
						&& getCtrlY1() == elt.getCtrlY1()
						&& getCtrlZ1() == elt.getCtrlZ1()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY()
						&& getFromZ() == elt.getFromZ();
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
			bits = 31 * bits + getToZ();
			bits = 31 * bits + getCtrlX1();
			bits = 31 * bits + getCtrlY1();
			bits = 31 * bits + getCtrlZ1();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			bits = 31 * bits + getFromZ();
			return (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.fromX==this.toX) && (this.fromY==this.toY) &&
					(this.ctrlX==this.toX) && (this.ctrlY==this.toY) &&
					(this.ctrlZ==this.toZ) && (this.ctrlZ==this.toZ);
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			assert (array != null) : "Array must be not null"; //$NON-NLS-1$
			assert (array.length >= 6) : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX;
			array[1] = this.ctrlY;
			array[2] = this.ctrlZ;
			array[3] = this.toX;
			array[4] = this.toY;
			array[5] = this.toZ;
		}
		
		@Override
		public void toArray(double[] array) {
			assert (array != null) : "Array must be not null"; //$NON-NLS-1$
			assert (array.length >= 6) : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX;
			array[1] = this.ctrlY;
			array[2] = this.ctrlZ;
			array[3] = this.toX;
			array[4] = this.toY;
			array[5] = this.toZ;
		}

		@Pure
		@Override
		public int[] toArray() {
			return new int[] {this.ctrlX, this.ctrlY, this.ctrlZ, this.toX, this.toY, this.toZ};
		}

		@Pure
		@Override
		public String toString() {
			return "QUAD("+ //$NON-NLS-1$
					this.ctrlX+"x"+ //$NON-NLS-1$
					this.ctrlY+"x"+ //$NON-NLS-1$
					this.ctrlZ+"|"+ //$NON-NLS-1$
					this.toX+"x"+ //$NON-NLS-1$
					this.toY+"x"+ //$NON-NLS-1$
					this.toZ+")"; //$NON-NLS-1$
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
		public int getFromZ() {
			return this.fromZ;
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
		public int getCtrlZ1() {
			return this.ctrlZ;
		}

		@Override
		public int getCtrlX2() {
			return 0;
		}
		
		@Override
		public int getCtrlY2() {
			return 0;
		}

		@Override
		public int getCtrlZ2() {
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
	static class CurvePathElement3i extends PathElement3i {
		
		private static final long serialVersionUID = 7009674542781709373L;

		private final int fromX;
		
		private final int fromY;
		
		private final int fromZ;

		private final int ctrlX1;
		
		private final int ctrlY1;
		
		private final int ctrlZ1;

		private final int ctrlX2;
		
		private final int ctrlY2;
		
		private final int ctrlZ2;

		/**
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
		public CurvePathElement3i(int fromx, int fromy, int fromz, int ctrlx1, int ctrly1, int ctrlz1, int ctrlx2, int ctrly2, int ctrlz2, int tox, int toy, int toz) {
			super(PathElementType.CURVE_TO, tox, toy, toz);
			this.fromX = fromx;
			this.fromY = fromy;
			this.fromZ = fromz;
			this.ctrlX1 = ctrlx1;
			this.ctrlY1 = ctrly1;
			this.ctrlZ1 = ctrlz1;
			this.ctrlX2 = ctrlx2;
			this.ctrlY2 = ctrly2;
			this.ctrlZ2 = ctrlz2;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				PathElement3ai elt = (PathElement3ai) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getToZ() == elt.getToZ()
						&& getCtrlX1() == elt.getCtrlX1()
						&& getCtrlY1() == elt.getCtrlY1()
						&& getCtrlZ1() == elt.getCtrlZ1()
						&& getCtrlX2() == elt.getCtrlX2()
						&& getCtrlY2() == elt.getCtrlY2()
						&& getCtrlZ2() == elt.getCtrlZ2()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY()
						&& getFromZ() == elt.getFromZ();
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
			bits = 31 * bits + getToZ();
			bits = 31 * bits + getCtrlX1();
			bits = 31 * bits + getCtrlY1();
			bits = 31 * bits + getCtrlZ1();
			bits = 31 * bits + getCtrlX2();
			bits = 31 * bits + getCtrlY2();
			bits = 31 * bits + getCtrlZ2();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			bits = 31 * bits + getFromZ();
			return (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.fromX==this.toX) && (this.fromY==this.toY) &&
					(this.ctrlX1==this.toX) && (this.ctrlY1==this.toY) && (this.ctrlZ1==this.toZ) &&
					(this.ctrlX2==this.toX) && (this.ctrlY2==this.toY) && (this.ctrlZ2==this.toZ);
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			assert (array != null) : "Array must be not null"; //$NON-NLS-1$
			assert (array.length >= 9) : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX1;
			array[1] = this.ctrlY1;
			array[2] = this.ctrlZ1;
			array[3] = this.ctrlX2;
			array[4] = this.ctrlY2;
			array[5] = this.ctrlZ2;
			array[6] = this.toX;
			array[7] = this.toY;
			array[8] = this.toZ;
		}
		
		@Override
		public void toArray(double[] array) {
			assert (array != null) : "Array must be not null"; //$NON-NLS-1$
			assert (array.length >= 9) : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX1;
			array[1] = this.ctrlY1;
			array[2] = this.ctrlZ1;
			array[3] = this.ctrlX2;
			array[4] = this.ctrlY2;
			array[5] = this.ctrlZ2;
			array[6] = this.toX;
			array[7] = this.toY;
			array[8] = this.toZ;
		}

		@Pure
		@Override
		public int[] toArray() {
			return new int[] {this.ctrlX1, this.ctrlY1, this.ctrlZ1, this.ctrlX2, this.ctrlY2, this.ctrlZ2, this.toX, this.toY, this.toZ};
		}

		@Pure
		@Override
		public String toString() {
			return "CURVE("+ //$NON-NLS-1$
					this.ctrlX1+"x"+ //$NON-NLS-1$
					this.ctrlY1+"x"+ //$NON-NLS-1$
					this.ctrlZ1+"|"+ //$NON-NLS-1$
					this.ctrlX2+"x"+ //$NON-NLS-1$
					this.ctrlY2+"x"+ //$NON-NLS-1$
					this.ctrlZ2+"|"+ //$NON-NLS-1$
					this.toX+"x"+ //$NON-NLS-1$
					this.toY+"x"+ //$NON-NLS-1$
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
		public int getFromZ() {
			return this.fromZ;
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
		public int getCtrlZ1() {
			return this.ctrlZ1;
		}

		@Override
		public int getCtrlX2() {
			return this.ctrlX2;
		}
		
		@Override
		public int getCtrlY2() {
			return this.ctrlY2;
		}

		@Override
		public int getCtrlZ2() {
			return this.ctrlZ2;
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
	static class ClosePathElement3i extends PathElement3i {
		
		private static final long serialVersionUID = -8709004906872207794L;

		private final int fromX;
		
		private final int fromY;

		private final int fromZ;
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param fromz
		 * @param tox
		 * @param toy
		 * @param toz
		 */
		public ClosePathElement3i(int fromx, int fromy, int fromz, int tox, int toy, int toz) {
			super(PathElementType.CLOSE, tox, toy, toz);
			this.fromX = fromx;
			this.fromY = fromy;
			this.fromZ = fromz;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				PathElement3ai elt = (PathElement3ai) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getToZ() == elt.getToZ()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY()
						&& getFromZ() == elt.getFromZ();
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
			bits = 31 * bits + getToZ();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			bits = 31 * bits + getFromZ();
			return (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.fromX==this.toX) && (this.fromY==this.toY) && (this.fromZ==this.toZ);
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			assert (array != null) : "Array must be not null"; //$NON-NLS-1$
			assert (array.length >= 3) : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
			array[2] = this.toZ;
		}
		
		@Override
		public void toArray(double[] array) {
			assert (array != null) : "Array must be not null"; //$NON-NLS-1$
			assert (array.length >= 3) : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
			array[2] = this.toZ;
		}

		@Pure
		@Override
		public int[] toArray() {
			return new int[] {this.toX, this.toY, this.toZ};
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
		public int getFromZ() {
			return this.fromZ;
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
		public int getCtrlZ1() {
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

		@Override
		public int getCtrlZ2() {
			return 0;
		}

	}

}