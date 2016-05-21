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

package org.arakhne.afc.math.geometry.d2.d;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.afp.InnerComputationPoint2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.eclipse.xtext.xbase.lib.Pure;

/** Path with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: fozgul$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Path2d
	extends AbstractShape2d<Path2d>
	implements Path2afp<Shape2d<?>, Path2d, PathElement2d, Point2d, Vector2d, Rectangle2d> {

	private static final long serialVersionUID = 4567950736238157802L;

	/** Array of types.
	 */
	private PathElementType[] types;

	/** Array of coords.
	 */
	private double[] coords;

	/** Number of types in the array.
	 */
	private int numTypes = 0;

	/** Number of coords in the array.
	 */
	private int numCoords = 0;

	/** Winding rule for the path.
	 */
	private PathWindingRule windingRule;

	/** Indicates if the path is empty.
	 * The path is empty when there is no point inside, or
	 * all the points are at the same coordinate, or
	 * when the path does not represents a drawable path
	 * (a path with a line or a curve).
	 */
	private Boolean isEmpty = Boolean.TRUE;

	/** Indicates if the path is a polyline.
	 */
	private Boolean isPolyline = Boolean.FALSE;

	/** Indicates if the path is curved.
	 */
	private Boolean isCurved = Boolean.FALSE;

	/** Indicates if the path is a polygon
	 */
	private Boolean isPolygon = Boolean.FALSE;

	/** Indicates if the path is multipart
	 */
	private Boolean isMultipart = Boolean.FALSE;

	/** Buffer for the bounds of the path that corresponds
	 * to the points really on the path (eg, the pixels
	 * drawn). The control points of the curves are
	 * not considered in this bounds.
	 */
	private SoftReference<Rectangle2d> graphicalBounds = null;

	/** Buffer for the bounds of the path that corresponds
	 * to all the points added in the path.
	 */
	private SoftReference<Rectangle2d> logicalBounds = null;
	
	/** Buffer for the length of the path.
	 */
	private Double length;

	/**
	 */
	public Path2d() {
		this(DEFAULT_WINDING_RULE);
	}

	/**
	 * @param iterator
	 */
	public Path2d(Iterator<PathElement2d> iterator) {
		this(DEFAULT_WINDING_RULE, iterator);
	}

	/**
	 * @param windingRule
	 */
	public Path2d(PathWindingRule windingRule) {
		assert (windingRule != null) : "Path winding rule must be not null"; //$NON-NLS-1$
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = windingRule;
	}

	/**
	 * @param windingRule
	 * @param iterator
	 */
	public Path2d(PathWindingRule windingRule, Iterator<PathElement2d> iterator) {
		assert (windingRule != null) : "Path winding rule must be not null"; //$NON-NLS-1$
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = windingRule;
		add(iterator);
	}

	/**
	 * @param p
	 */
	public Path2d(Path2afp<?, ?, ?, ?, ?, ?> p) {
		set(p);
	}
	
	private void ensureSlots(boolean needMove, int n) {
		if (needMove && this.numTypes==0) {
			throw new IllegalStateException("missing initial moveto in path definition"); //$NON-NLS-1$
		}
		if (this.types.length==this.numTypes) {
			this.types = Arrays.copyOf(this.types, this.types.length+GROW_SIZE);
		}
		while ((this.numCoords+n)>=this.coords.length) {
			this.coords = Arrays.copyOf(this.coords, this.coords.length+GROW_SIZE);
		}
	}
	
	@Pure
	@Override
	public boolean containsControlPoint(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		double x, y;
		for(int i=0; i<this.numCoords;i+=2) {
			x = this.coords[i];
			y = this.coords[i+1];
			if (x==p.getX() && y==p.getY()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void clear() {
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = PathWindingRule.NON_ZERO;
		this.numCoords = 0;
		this.numTypes = 0;
		this.isEmpty = Boolean.TRUE;
		this.isPolyline = Boolean.FALSE;
		this.isPolygon = Boolean.FALSE;
		this.isCurved = Boolean.FALSE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
		this.length = null;
		fireGeometryChange();
	}

	@Pure
	@Override
	public Path2d clone() {
		Path2d clone = super.clone();
		clone.coords = this.coords.clone();
		clone.types = this.types.clone();
		clone.windingRule = this.windingRule;
		return clone;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + this.numCoords;
		bits = 31L * bits + this.numTypes;
		bits = 31L * bits + Arrays.hashCode(this.coords);
		bits = 31L * bits + Arrays.hashCode(this.types);
		bits = 31L * bits + this.windingRule.ordinal();
		return (int) (bits ^ (bits >> 32));
	}

	@Pure
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		if (this.numCoords>0) {
			b.append(this.coords[0]);
			for(int i=1; i<this.numCoords; ++i) {
				b.append(", "); //$NON-NLS-1$
				b.append(this.coords[i]);
			}
		}
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Override
	public void translate(double dx, double dy) {
		for(int i=0; i<this.numCoords;i+=2) {
			this.coords[i] += dx;
			this.coords[i+1] += dy;
		}
		Rectangle2d bb;
		bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb!=null) bb.translate(dx, dy);
		bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb!=null) bb.translate(dx, dy);
		fireGeometryChange();
	}
	
	@Override
	public void transform(Transform2D transform) {
		assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
		Point2D<?, ?> p = new InnerComputationPoint2afp();
		for(int i=0; i<this.numCoords;i+=2) {
			p.set(this.coords[i], this.coords[i+1]);
			transform.transform(p);
			this.coords[i] = p.getX();
			this.coords[i+1] = p.getY();
		}
		this.graphicalBounds = null;
		this.logicalBounds = null;
		this.length = null;
		fireGeometryChange();
	}

	@Override
	public boolean isEmpty() {
		if (this.isEmpty==null) {
			this.isEmpty = Boolean.TRUE;
			PathIterator2afp<PathElement2d> pi = getPathIterator();
			PathElement2d pe;
			while (this.isEmpty==Boolean.TRUE && pi.hasNext()) {
				pe = pi.next();
				if (pe.isDrawable()) { 
					this.isEmpty = Boolean.FALSE;
				}
			}
		}
		return this.isEmpty.booleanValue();
	}
	
	@Override
	public Rectangle2d toBoundingBox() {
		Rectangle2d bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = getGeomFactory().newBox();
			Path2afp.computeDrawableElementBoundingBox(
					getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
					bb);
			this.graphicalBounds = new SoftReference<>(bb);
		}
		return bb;

	}

	@Override
	public void toBoundingBox(Rectangle2d box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		Rectangle2d bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = getGeomFactory().newBox();
			Path2afp.computeDrawableElementBoundingBox(
					getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
					bb);
			this.graphicalBounds = new SoftReference<>(bb);
		}
		box.set(bb);
	}

	@Override
	public PathWindingRule getWindingRule() {
		return this.windingRule;
	}

	@Override
	public boolean isPolyline() {
		if (this.isPolyline == null) {
			PathIterator2afp<PathElement2d> pi = getPathIterator();
			PathElement2d pe;
			PathElementType t;
			boolean first = true;
			boolean hasOneLine = false;
			while (this.isPolyline == null && pi.hasNext()) {
				pe = pi.next();
				t = pe.getType();
				if (first) {
					if (t != PathElementType.MOVE_TO) {
						this.isPolyline = Boolean.FALSE;
					} else {
						first = false;
					}
				} else if (t != PathElementType.LINE_TO) {
					this.isPolyline = Boolean.FALSE;
				} else {
					hasOneLine = true;
				}
			}
			if (this.isPolyline == null) {
				this.isPolyline = hasOneLine;
			}
		}
		return this.isPolyline.booleanValue();
	}

	@Override
	public boolean isCurved() {
		if (this.isCurved == null) {
			this.isCurved = Boolean.FALSE;
			PathIterator2afp<PathElement2d> pi = getPathIterator();
			PathElement2d pe;
			PathElementType t;
			while (this.isCurved == Boolean.FALSE && pi.hasNext()) {
				pe = pi.next();
				t = pe.getType();
				if (t==PathElementType.CURVE_TO || t==PathElementType.QUAD_TO) { 
					this.isCurved = Boolean.TRUE;
				}
			}
		}
		return this.isCurved.booleanValue();
	}

	@Override
	public boolean isMultiParts() {
		if (this.isMultipart == null) {
			this.isMultipart = Boolean.FALSE;
			PathIterator2afp<PathElement2d> pi = getPathIterator();
			PathElement2d pe;
			PathElementType t;
			boolean foundOne = false;
			while (this.isMultipart == Boolean.FALSE && pi.hasNext()) {
				pe = pi.next();
				t = pe.getType();
				if (t==PathElementType.MOVE_TO) {
					if (foundOne) {
						this.isMultipart = Boolean.TRUE;
					} else {
						foundOne = true;
					}
				}
			}
		}
		return this.isMultipart.booleanValue();
	}

	@Override
	public boolean isPolygon() {
		if (this.isPolygon == null) {
			PathIterator2afp<PathElement2d> pi = getPathIterator();
			PathElement2d pe;
			PathElementType t;
			boolean first = true;
			boolean lastIsClose = false;
			while (this.isPolygon == null && pi.hasNext()) {
				pe = pi.next();
				t = pe.getType();
				lastIsClose = false;
				if (first) {
					if (t != PathElementType.MOVE_TO) {
						this.isPolygon = Boolean.FALSE;
					} else {
						first = false;
					}
				} else if (t == PathElementType.MOVE_TO) {
					this.isPolygon = Boolean.FALSE;
				} else if (t == PathElementType.CLOSE) {
					lastIsClose = true;
				}
			}
			if (this.isPolygon == null) {
				this.isPolygon = Boolean.valueOf(lastIsClose);
			}
		}
		return this.isPolygon.booleanValue();
	}

	@Override
	public void closePath() {
		if (this.numTypes<=0 ||
				(this.types[this.numTypes-1]!=PathElementType.CLOSE
				&&this.types[this.numTypes-1]!=PathElementType.MOVE_TO)) {
			ensureSlots(true, 0);
			this.types[this.numTypes++] = PathElementType.CLOSE;
			this.isPolyline = false;
			this.isPolygon = null;
			fireGeometryChange();
		}
	}

	@Override
	@Pure
	public Rectangle2d toBoundingBoxWithCtrlPoints() {
		Rectangle2d bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = getGeomFactory().newBox();
			Path2afp.computeControlPointBoundingBox(
					getPathIterator(),
					bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		return bb;
	}

	@Override
	@Pure
	public void toBoundingBoxWithCtrlPoints(Rectangle2d box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		Rectangle2d bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = getGeomFactory().newBox();
			Path2afp.computeControlPointBoundingBox(
					getPathIterator(),
					bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		box.set(bb);
	}

	@Override
	@Pure
	public int[] toIntArray(Transform2D transform) {
		int[] clone = new int[this.numCoords];
		if (transform == null || transform.isIdentity()) {
			for(int i=0; i<this.numCoords; ++i) {
				clone[i] = (int) this.coords[i];
			}
		}
		else {
			Point2D<?, ?> p = new InnerComputationPoint2afp();
			for(int i=0; i<clone.length;i+=2) {
				p.set(this.coords[i], this.coords[i+1]);
				transform.transform(p);
				clone[i] = p.ix();
				clone[i+1] = p.iy();
			}
		}
		return clone;
	}

	@Override
	@Pure
	public float[] toFloatArray(Transform2D transform) {
		float[] clone = new float[this.numCoords];
		if (transform == null || transform.isIdentity()) {
			for(int i=0; i<this.numCoords; ++i) {
				clone[i] = (float) this.coords[i];
			}
		}
		else {
			Point2D<?, ?> p = new InnerComputationPoint2afp();
			for(int i=0; i<clone.length;i+=2) {
				p.set(this.coords[i], this.coords[i+1]);
				transform.transform(p);
				clone[i] = (float) p.getX();
				clone[i+1] = (float) p.getY();
			}
		}
		return clone;
	}

	@Override
	@Pure
	public double[] toDoubleArray(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return Arrays.copyOf(this.coords, this.numCoords);
		}
		Point2D<?, ?> p = new InnerComputationPoint2afp();
		double[] clone = new double[this.numCoords];
		for(int i=0; i<clone.length;i+=2) {
			p.set(this.coords[i], this.coords[i+1]);
			transform.transform(p);
			clone[i] = p.getX();
			clone[i+1] = p.getY();
		}
		return clone;
	}

	@Override
	@Pure
	public Point2d[] toPointArray(Transform2D transform) {
		Point2d[] clone = new Point2d[this.numCoords/2];
		if (transform == null || transform.isIdentity()) {
			for(int i=0, j=0; j<this.numCoords; ++i,j+=2) {
				clone[i] = getGeomFactory().newPoint(
						this.coords[j],
						this.coords[j+1]);
			}
		}
		else {
			for(int i=0, j=0; j<clone.length; ++i,j+=2) {
				clone[i] = getGeomFactory().newPoint(
						this.coords[j],
						this.coords[j+1]);
				transform.transform(clone[i]);
			}
		}
		return clone;
	}

	@Override
	@Pure
	public Point2d getPointAt(int index) {
		assert (index >=0 && index < size()) : "Index must be in [0;" + size() + ")";  //$NON-NLS-1$ //$NON-NLS-2$
		return getGeomFactory().newPoint(
				this.coords[index*2],
				this.coords[index*2+1]);
	}

	@Override
	@Pure
	public double getCurrentX() {
		return this.coords[this.numCoords - 2];
	}
	
	@Override
	@Pure
	public double getCurrentY() {
		return this.coords[this.numCoords - 1];
	}

	@Override
	@Pure
	public int size() {
		return this.numCoords/2;
	}

	@Override
	public void removeLast() {
		if (this.numTypes>0) {
			switch(this.types[this.numTypes-1]) {
			case CLOSE:
				// no coord to remove
				this.isPolygon = null;
				this.isPolyline = null;
				break;
			case MOVE_TO:
				this.numCoords -= 2;
				this.isPolyline = null;
				this.isMultipart = null;
				break;
			case LINE_TO:
				this.numCoords -= 2;
				this.isPolyline = null;
				break;
			case CURVE_TO:
				this.numCoords -= 6;
				this.isPolyline = null;
				this.isCurved = null;
				break;
			case QUAD_TO:
				this.numCoords -= 4;
				this.isPolyline = null;
				this.isCurved = null;
				break;
			default:
				throw new IllegalStateException();
			}
			--this.numTypes;
			this.isEmpty = null;
			this.graphicalBounds = null;
			this.logicalBounds = null;
			this.length = null;
			fireGeometryChange();
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public void moveTo(double x, double y) {
		if (this.numTypes != 0) {
			this.isPolyline = Boolean.FALSE;
			this.isPolygon = Boolean.FALSE;
		}
		if (this.isMultipart != null && this.isMultipart != Boolean.TRUE) {
			this.isMultipart = null;
		}
		if (this.numTypes>0 && this.types[this.numTypes-1] == PathElementType.MOVE_TO) {
			this.coords[this.numCoords-2] = x;
			this.coords[this.numCoords-1] = y;
		}
		else {
			ensureSlots(false, 2);
			this.types[this.numTypes++] = PathElementType.MOVE_TO;
			this.coords[this.numCoords++] = x;
			this.coords[this.numCoords++] = y;
		}
		this.graphicalBounds = null;
		this.logicalBounds = null;
		this.length = null;
		fireGeometryChange();
	}

	@Override
	public void lineTo(double x, double y) {
		ensureSlots(true, 2);
		this.types[this.numTypes++] = PathElementType.LINE_TO;
		this.coords[this.numCoords++] = x;
		this.coords[this.numCoords++] = y;
		this.isEmpty = null;
		if (this.isPolyline != null && this.isPolyline == Boolean.FALSE) {
			this.isPolyline = null;
		}
		this.graphicalBounds = null;
		this.logicalBounds = null;
		this.length = null;
		fireGeometryChange();
	}

	@Override
	public void quadTo(double x1, double y1, double x2, double y2) {
		ensureSlots(true, 4);
		this.types[this.numTypes++] = PathElementType.QUAD_TO;
		this.coords[this.numCoords++] = x1;
		this.coords[this.numCoords++] = y1;
		this.coords[this.numCoords++] = x2;
		this.coords[this.numCoords++] = y2;
		this.isEmpty = null;
		this.isPolyline = Boolean.FALSE;
		this.isCurved = Boolean.TRUE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
		this.length = null;
		fireGeometryChange();
	}

	@Override
	public void curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
		ensureSlots(true, 6);
		this.types[this.numTypes++] = PathElementType.CURVE_TO;
		this.coords[this.numCoords++] = x1;
		this.coords[this.numCoords++] = y1;
		this.coords[this.numCoords++] = x2;
		this.coords[this.numCoords++] = y2;
		this.coords[this.numCoords++] = x3;
		this.coords[this.numCoords++] = y3;
		this.isEmpty = null;
		this.isPolyline = Boolean.FALSE;
		this.isCurved = Boolean.TRUE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
		this.length = null;
		fireGeometryChange();
	}

	@Override
	@Pure
	public double getCoordAt(int index) {
		return this.coords[index];
	}

	@Override
	public void setLastPoint(double x, double y) {
		if (this.numCoords>=2) {
			this.coords[this.numCoords-2] = x;
			this.coords[this.numCoords-1] = y;
			this.graphicalBounds = null;
			this.logicalBounds = null;
			this.length = null;
			fireGeometryChange();
		} else {
			throw new IllegalStateException();
		}
	}
	
	@Override
	public void setWindingRule(PathWindingRule r) {
		assert (r != null) : "Path winding rule must be not null"; //$NON-NLS-1$
		this.windingRule = r;
	}

	@Override
	public boolean remove(double x, double y) {
		for(int i=0, j=0; i<this.numCoords && j<this.numTypes;) {
			switch(this.types[j]) {
			case MOVE_TO:
				this.isMultipart = null;
				//$FALL-THROUGH$
			case LINE_TO:
				if (x==this.coords[i] && y==this.coords[i+1]) {
					this.numCoords -= 2;
					--this.numTypes;
					System.arraycopy(this.coords, i+2, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j+1, this.types, j, this.numTypes);
					this.isEmpty = null;
					this.length = null;
					this.graphicalBounds = null;
					this.logicalBounds = null;
					fireGeometryChange();
					return true;
				}
				i += 2;
				++j;
				break;
			case CURVE_TO:
				if ((x==this.coords[i] && y==this.coords[i+1])
						||(x==this.coords[i+2] && y==this.coords[i+3])
						||(x==this.coords[i+4] && y==this.coords[i+5])) {
					this.numCoords -= 6;
					--this.numTypes;
					System.arraycopy(this.coords, i+6, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j+1, this.types, j, this.numTypes);
					this.isEmpty = null;
					this.isPolyline = null;
					this.length = null;
					this.graphicalBounds = null;
					this.logicalBounds = null;
					fireGeometryChange();
					return true;
				}
				i += 6;
				++j;
				break;
			case QUAD_TO:
				if ((x==this.coords[i] && y==this.coords[i+1])
						||(x==this.coords[i+2] && y==this.coords[i+3])) {
					this.numCoords -= 4;
					--this.numTypes;
					System.arraycopy(this.coords, i+4, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j+1, this.types, j, this.numTypes);
					this.isEmpty = null;
					this.isPolyline = null;
					this.length = null;
					this.graphicalBounds = null;
					this.logicalBounds = null;
					fireGeometryChange();
					return true;
				}
				i += 4;
				++j;
				break;
			case CLOSE:
				++j;
				break;
			default:
				break;
			}
		}
		return false;
	}

	@Override
	public void set(Path2d s) {
		assert (s != null) : "Path must be not null"; //$NON-NLS-1$
		clear();
		add(s.getPathIterator());
	}

	@Override
	@Pure
	public int getPathElementCount() {
		return this.numTypes;
	}

	@Override
	@Pure
	public PathElementType getPathElementTypeAt(int index) {
		return this.types[index];
	}

	@Override
	@Pure
	public double getLength() {
		if (this.length == null) {
			this.length = Double.valueOf(Path2afp.computeLength(getPathIterator()));
		}
		return this.length.doubleValue();
	}

}
