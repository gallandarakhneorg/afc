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
package org.arakhne.afc.math.geometry.d3.d;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;
import org.eclipse.xtext.xbase.lib.Pure;

/** An element of the path with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class PathElement3d implements PathElement3afp {
	
	private static final long serialVersionUID = -9217295344283468162L;

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
	public static PathElement3d newInstance(PathElementType type, double lastX, double lastY, double lastZ, double[] coords) {
		assert (type != null) : "Path element type must be not null"; //$NON-NLS-1$
		assert (coords != null) : "Coordinate array type must be not null"; //$NON-NLS-1$
		assert (coords.length >= 3) : "Size of the coordinate array type is too small"; //$NON-NLS-1$
		switch(type) {
		case MOVE_TO:
			return new MovePathElement3d(coords[0], coords[1], coords[2]);
		case LINE_TO:
			return new LinePathElement3d(lastX, lastY, lastZ, coords[0], coords[1], coords[2]);
		case QUAD_TO:
			assert (coords.length >= 6) : "Size of the coordinate array type is too small"; //$NON-NLS-1$
			return new QuadPathElement3d(lastX, lastY, lastZ, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
		case CURVE_TO:
			assert (coords.length >= 9) : "Size of the coordinate array type is too small"; //$NON-NLS-1$
			return new CurvePathElement3d(lastX, lastY, lastZ, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5], coords[6], coords[7], coords[8]);
		case CLOSE:
			return new ClosePathElement3d(lastX, lastY, lastZ, coords[0], coords[1], coords[2]);
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
	
	/** Target point.
	 */
	protected final double toZ;

	/**
	 * @param type is the type of the element.
	 * @param tox the x coordinate of the target point.
	 * @param toy the y coordinate of the target point.
	 * @param toz the z coordinate of the target point.
	 */
	PathElement3d(PathElementType type, double tox, double toy, double toz) {
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
	public final double getToX() {
		return this.toX;
	}
	
	@Override
	@Pure
	public final double getToY() {
		return this.toY;
	}

	@Override
	@Pure
	public final double getToZ() {
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
	static class MovePathElement3d extends PathElement3d {
		
		private static final long serialVersionUID = -399575136145167775L;

		/**
		 * @param x
		 * @param y
		 * @param z
		 */
		public MovePathElement3d(double x, double y, double z) {
			super(PathElementType.MOVE_TO, x, y, z);
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				PathElement3afp elt = (PathElement3afp) obj;
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
			long bits = 1L;
			bits = 31L * bits + this.type.ordinal();
			bits = 31L * bits + Double.doubleToLongBits(getToX());
			bits = 31L * bits + Double.doubleToLongBits(getToY());
			bits = 31L * bits + Double.doubleToLongBits(getToZ());
			return (int) (bits ^ (bits >> 32));
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
			array[0] = (int) this.toX;
			array[1] = (int) this.toY;
			array[2] = (int) this.toZ;
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
		public double[] toArray() {
			return new double[] {this.toX, this.toY, this.toZ};
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
		public double getFromX() {
			return 0.;
		}
		
		@Override
		public double getFromY() {
			return 0.;
		}

		@Override
		public double getFromZ() {
			return 0.;
		}

		@Override
		public double getCtrlX1() {
			return 0.;
		}
		
		@Override
		public double getCtrlY1() {
			return 0.;
		}

		@Override
		public double getCtrlZ1() {
			return 0.;
		}

		@Override
		public double getCtrlX2() {
			return 0.;
		}
		
		@Override
		public double getCtrlY2() {
			return 0.;
		}

		@Override
		public double getCtrlZ2() {
			return 0.;
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
	static class LinePathElement3d extends PathElement3d {
		
		private static final long serialVersionUID = 8423845888008307447L;

		private final double fromX;
		
		private final double fromY;

		private final double fromZ;
		
		private Boolean isEmpty;
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param fromz 
		 * @param tox
		 * @param toy
		 * @param toz 
		 */
		public LinePathElement3d(double fromx, double fromy, double fromz, double tox, double toy, double toz) {
			super(PathElementType.LINE_TO, tox, toy, toz);
			this.fromX = fromx;
			this.fromY = fromy;
			this.fromZ = fromz;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				PathElement3afp elt = (PathElement3afp) obj;
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
			long bits = 1L;
			bits = 31L * bits + this.type.ordinal();
			bits = 31L * bits + Double.doubleToLongBits(getToX());
			bits = 31L * bits + Double.doubleToLongBits(getToY());
			bits = 31L * bits + Double.doubleToLongBits(getToZ());
			bits = 31L * bits + Double.doubleToLongBits(getFromX());
			bits = 31L * bits + Double.doubleToLongBits(getFromY());
			bits = 31L * bits + Double.doubleToLongBits(getFromZ());
			return (int) (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			if (this.isEmpty == null) {
				this.isEmpty = MathUtil.isEpsilonEqual(this.fromX, this.toX)
						&& MathUtil.isEpsilonEqual(this.fromY, this.toY)
						&& MathUtil.isEpsilonEqual(this.fromZ, this.toZ);
			}
			return this.isEmpty.booleanValue();
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
			array[0] = (int) this.toX;
			array[1] = (int) this.toY;
			array[2] = (int) this.toZ;
		}
		
		@Override
		public void toArray(double[] array) {
			assert (array != null) : "Array must be not null"; //$NON-NLS-1$
			assert (array.length >= 3) : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
			array[2] = this.toY;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.toX, this.toY, this.toZ};
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
		public double getFromX() {
			return this.fromX;
		}
		
		@Override
		public double getFromY() {
			return this.fromY;
		}

		@Override
		public double getFromZ() {
			return this.fromZ;
		}

		@Override
		public double getCtrlX1() {
			return 0.;
		}
		
		@Override
		public double getCtrlY1() {
			return 0.;
		}

		@Override
		public double getCtrlZ1() {
			return 0.;
		}

		@Override
		public double getCtrlX2() {
			return 0.;
		}
		
		@Override
		public double getCtrlY2() {
			return 0.;
		}

		@Override
		public double getCtrlZ2() {
			return 0.;
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
	static class QuadPathElement3d extends PathElement3d {
		
		private static final long serialVersionUID = 6092391345111872481L;

		private final double fromX;
		
		private final double fromY;

		private final double fromZ;

		private final double ctrlX;
		
		private final double ctrlY;

		private final double ctrlZ;
		
		private Boolean isEmpty;

		/**
		 * @param fromx
		 * @param fromy
		 * @param fromZ
		 * @param ctrlx
		 * @param ctrly
		 * @param ctrlz 
		 * @param tox
		 * @param toy
		 * @param toz 
		 */
		public QuadPathElement3d(double fromx, double fromy, double fromZ, double ctrlx, double ctrly, double ctrlz, double tox, double toy, double toz) {
			super(PathElementType.QUAD_TO, tox, toy, toz);
			this.fromX = fromx;
			this.fromY = fromy;
			this.fromZ = fromy;
			this.ctrlX = ctrlx;
			this.ctrlY = ctrly;
			this.ctrlZ = ctrly;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				PathElement3afp elt = (PathElement3afp) obj;
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
			long bits = 1L;
			bits = 31L * bits + this.type.ordinal();
			bits = 31L * bits + Double.doubleToLongBits(getToX());
			bits = 31L * bits + Double.doubleToLongBits(getToY());
			bits = 31L * bits + Double.doubleToLongBits(getToZ());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlX1());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlY1());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlZ1());
			bits = 31L * bits + Double.doubleToLongBits(getFromX());
			bits = 31L * bits + Double.doubleToLongBits(getFromY());
			bits = 31L * bits + Double.doubleToLongBits(getFromZ());
			return (int) (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			if (this.isEmpty == null) {
				this.isEmpty = MathUtil.isEpsilonEqual(this.fromX, this.toX)
						&& MathUtil.isEpsilonEqual(this.fromY, this.toY)
						&& MathUtil.isEpsilonEqual(this.fromZ, this.toZ)
						&& MathUtil.isEpsilonEqual(this.ctrlX, this.toX)
						&& MathUtil.isEpsilonEqual(this.ctrlY, this.toY)
						&& MathUtil.isEpsilonEqual(this.ctrlZ, this.toZ);
			}
			return this.isEmpty.booleanValue();
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
			array[0] = (int) this.ctrlX;
			array[1] = (int) this.ctrlY;
			array[2] = (int) this.ctrlZ;
			array[3] = (int) this.toX;
			array[4] = (int) this.toY;
			array[5] = (int) this.toZ;
		}
		
		@Override
		public void toArray(double[] array) {
			assert (array != null) : "Array must be not null"; //$NON-NLS-1$
			assert (array.length >= 4) : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX;
			array[1] = this.ctrlY;
			array[2] = this.ctrlZ;
			array[3] = this.toX;
			array[4] = this.toY;
			array[5] = this.toZ;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.ctrlX, this.ctrlY, this.ctrlZ, this.toX, this.toY, this.toZ};
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
		public double getFromZ() {
			return this.fromZ;
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
		public double getCtrlZ1() {
			return this.ctrlZ;
		}

		@Override
		public double getCtrlX2() {
			return 0.;
		}
		
		@Override
		public double getCtrlY2() {
			return 0.;
		}

		@Override
		public double getCtrlZ2() {
			return 0.;
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
	static class CurvePathElement3d extends PathElement3d {
		
		private static final long serialVersionUID = -2831895270995173092L;

		private final double fromX;
		
		private final double fromY;

		private final double fromZ;

		private final double ctrlX1;
		
		private final double ctrlY1;

		private final double ctrlZ1;

		private final double ctrlX2;
		
		private final double ctrlY2;

		private final double ctrlZ2;
		
		private Boolean isEmpty;

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
		public CurvePathElement3d(double fromx, double fromy, double fromz, double ctrlx1, double ctrly1, double ctrlz1, double ctrlx2, double ctrly2, double ctrlz2, double tox, double toy, double toz) {
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
				PathElement3afp elt = (PathElement3afp) obj;
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
			long bits = 1L;
			bits = 31L * bits + this.type.ordinal();
			bits = 31L * bits + Double.doubleToLongBits(getToX());
			bits = 31L * bits + Double.doubleToLongBits(getToY());
			bits = 31L * bits + Double.doubleToLongBits(getToZ());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlX1());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlY1());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlZ1());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlX2());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlY2());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlZ2());
			bits = 31L * bits + Double.doubleToLongBits(getFromX());
			bits = 31L * bits + Double.doubleToLongBits(getFromY());
			bits = 31L * bits + Double.doubleToLongBits(getFromZ());
			return (int) (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			if (this.isEmpty == null) {
				this.isEmpty = MathUtil.isEpsilonEqual(this.fromX, this.toX)
						&& MathUtil.isEpsilonEqual(this.fromY, this.toY)
						&& MathUtil.isEpsilonEqual(this.fromZ, this.toZ)
						&& MathUtil.isEpsilonEqual(this.ctrlX1, this.toX)
						&& MathUtil.isEpsilonEqual(this.ctrlY1, this.toY)
						&& MathUtil.isEpsilonEqual(this.ctrlZ1, this.toZ)
						&& MathUtil.isEpsilonEqual(this.ctrlX2, this.toX)
						&& MathUtil.isEpsilonEqual(this.ctrlY2, this.toY)
						&& MathUtil.isEpsilonEqual(this.ctrlZ2, this.toZ);
			}
			return this.isEmpty.booleanValue();
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
			array[0] = (int) this.ctrlX1;
			array[1] = (int) this.ctrlY1;
			array[2] = (int) this.ctrlZ1;
			array[3] = (int) this.ctrlX2;
			array[4] = (int) this.ctrlY2;
			array[5] = (int) this.ctrlZ2;
			array[6] = (int) this.toX;
			array[7] = (int) this.toY;
			array[8] = (int) this.toZ;
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
		public double[] toArray() {
			return new double[] {this.ctrlX1, this.ctrlY1, this.ctrlZ1, this.ctrlX2, this.ctrlY2, this.ctrlZ2, this.toX, this.toY, this.toZ};
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
		public double getFromX() {
			return this.fromX;
		}
		
		@Override
		public double getFromY() {
			return this.fromY;
		}

		@Override
		public double getFromZ() {
			return this.fromZ;
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
		public double getCtrlZ1() {
			return this.ctrlZ1;
		}

		@Override
		public double getCtrlX2() {
			return this.ctrlX2;
		}
		
		@Override
		public double getCtrlY2() {
			return this.ctrlY2;
		}

		@Override
		public double getCtrlZ2() {
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
	static class ClosePathElement3d extends PathElement3d {
		
		private static final long serialVersionUID = 5324688417590599323L;

		private final double fromX;
		
		private final double fromY;

		private final double fromZ;
		
		private Boolean isEmpty;
		
		/**
		 * @param fromx
		 * @param fromy
		 * @param fromz 
		 * @param tox
		 * @param toy
		 * @param toz 
		 */
		public ClosePathElement3d(double fromx, double fromy, double fromz, double tox, double toy, double toz) {
			super(PathElementType.CLOSE, tox, toy, toz);
			this.fromX = fromx;
			this.fromY = fromy;
			this.fromZ = fromz;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				PathElement3afp elt = (PathElement3afp) obj;
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
			long bits = 1L;
			bits = 31L * bits + this.type.ordinal();
			bits = 31L * bits + Double.doubleToLongBits(getToX());
			bits = 31L * bits + Double.doubleToLongBits(getToY());
			bits = 31L * bits + Double.doubleToLongBits(getToZ());
			bits = 31L * bits + Double.doubleToLongBits(getFromX());
			bits = 31L * bits + Double.doubleToLongBits(getFromY());
			bits = 31L * bits + Double.doubleToLongBits(getFromZ());
			return (int) (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			if (this.isEmpty == null) {
				this.isEmpty = MathUtil.isEpsilonEqual(this.fromX, this.toX)
						&& MathUtil.isEpsilonEqual(this.fromY, this.toY)
						&& MathUtil.isEpsilonEqual(this.fromZ, this.toZ);
			}
			return this.isEmpty.booleanValue();
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
			array[0] = (int) this.toX;
			array[1] = (int) this.toY;
			array[2] = (int) this.toZ;
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
		public double[] toArray() {
			return new double[] { this.toX, this.toY, this.toZ };
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
		public double getFromZ() {
			return this.fromZ;
		}

		@Override
		public double getCtrlX1() {
			return 0.;
		}
		
		@Override
		public double getCtrlY1() {
			return 0.;
		}

		@Override
		public double getCtrlZ1() {
			return 0.;
		}

		@Override
		public double getCtrlX2() {
			return 0.;
		}
		
		@Override
		public double getCtrlY2() {
			return 0.;
		}

		@Override
		public double getCtrlZ2() {
			return 0.;
		}

	}

}