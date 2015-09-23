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
package org.arakhne.afc.math.continous.object2d;

import org.arakhne.afc.math.generic.PathElement2D;
import org.arakhne.afc.math.generic.PathElementType;

/** An element of the path.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link org.arakhne.afc.math.geometry.d2.continuous.AbstractPathElement2F}
 */
@Deprecated
public abstract class PathElement2f implements PathElement2D {
	
	private static final long serialVersionUID = 8963281073370254033L;
	
	/** Create an instance of path element.
	 * 
	 * @param type is the type of the new element.
	 * @param lastX is the coordinate of the last point.
	 * @param lastY is the coordinate of the last point.
	 * @param coords are the coordinates.
	 * @return the instance of path element.
	 */
	public static PathElement2f newInstance(PathElementType type, double lastX, double lastY, double[] coords) {
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
	public final PathElementType type;
	
	/** Source point.
	 */
	public final double fromX;
	
	/** Source point.
	 */
	public final double fromY;

	/** Target point.
	 */
	public final double toX;
	
	/** Target point.
	 */
	public final double toY;

	/** First control point.
	 */
	public final double ctrlX1;
	
	/** First control point.
	 */
	public final double ctrlY1;

	/** Second control point.
	 */
	public final double ctrlX2;
	
	/** Second control point.
	 */
	public final double ctrlY2;

	/**
	 * @param type is the type of the element.
	 * @param fromx is the source point.
	 * @param fromy is the source point.
	 * @param ctrlx1 is the first control point.
	 * @param ctrly1 is the first control point.
	 * @param ctrlx2 is the first control point.
	 * @param ctrly2 is the first control point.
	 * @param tox is the target point.
	 * @param toy is the target point.
	 */
	public PathElement2f(PathElementType type, double fromx, double fromy, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double tox, double toy) {
		assert(type!=null);
		this.type = type;
		this.fromX = fromx;
		this.fromY = fromy;
		this.ctrlX1 = ctrlx1;
		this.ctrlY1 = ctrly1;
		this.ctrlX2 = ctrlx2;
		this.ctrlY2 = ctrly2;
		this.toX = tox;
		this.toY = toy;
	}

	/** Copy the coords into the given array, except the source point.
	 * 
	 * @param array
	 */
	public abstract void toArray(double[] array);

	/** Copy the coords into an array, except the source point.
	 * 
	 * @return the array of the points, except the source point.
	 */
	public abstract double[] toArray();

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
			return (this.fromX==this.toX) && (this.fromY==this.toY);
		}

		@Override
		public boolean isDrawable() {
			return false;
		}
		
		@Override
		public void toArray(double[] array) {
			array[0] = this.toX;
			array[1] = this.toY;
		}
		
		@Override
		public double[] toArray() {
			return new double[] {this.toX, this.toY};
		}

		@Override
		public String toString() {
			return "MOVE("+ //$NON-NLS-1$
					this.toX+"x"+ //$NON-NLS-1$
					this.toY+")"; //$NON-NLS-1$
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
			return (this.fromX==this.toX) && (this.fromY==this.toY);
		}

		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(double[] array) {
			array[0] = this.toX;
			array[1] = this.toY;
		}
		
		@Override
		public double[] toArray() {
			return new double[] {this.toX, this.toY};
		}

		@Override
		public String toString() {
			return "LINE("+ //$NON-NLS-1$
					this.toX+"x"+ //$NON-NLS-1$
					this.toY+")"; //$NON-NLS-1$
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
			return (this.fromX==this.toX) && (this.fromY==this.toY) &&
					(this.ctrlX1==this.toX) && (this.ctrlY1==this.toY);
		}

		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(double[] array) {
			array[0] = this.ctrlX1;
			array[1] = this.ctrlY1;
			array[2] = this.toX;
			array[3] = this.toY;
		}
		
		@Override
		public double[] toArray() {
			return new double[] {this.ctrlX1, this.ctrlY1, this.toX, this.toY};
		}
		
		@Override
		public String toString() {
			return "QUAD("+ //$NON-NLS-1$
					this.ctrlX1+"x"+ //$NON-NLS-1$
					this.ctrlY1+"|"+ //$NON-NLS-1$
					this.toX+"x"+ //$NON-NLS-1$
					this.toY+")"; //$NON-NLS-1$
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
			return (this.fromX==this.toX) && (this.fromY==this.toY) &&
					(this.ctrlX1==this.toX) && (this.ctrlY1==this.toY) &&
					(this.ctrlX2==this.toX) && (this.ctrlY2==this.toY);
		}

		@Override
		public boolean isDrawable() {
			return !isEmpty();
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
		
		@Override
		public double[] toArray() {
			return new double[] {this.ctrlX1, this.ctrlY1, this.ctrlX2, this.ctrlY2, this.toX, this.toY};
		}

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
			return (this.fromX==this.toX) && (this.fromY==this.toY);
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