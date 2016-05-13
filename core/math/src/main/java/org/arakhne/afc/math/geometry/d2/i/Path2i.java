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

package org.arakhne.afc.math.geometry.d2.i;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.InnerComputationPoint2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.eclipse.xtext.xbase.lib.Pure;

/** Path with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Path2i
extends AbstractShape2i<Path2i>
implements Path2ai<Shape2i<?>, Path2i, PathElement2i, Point2i, Vector2i, Rectangle2i> {

	private static final long serialVersionUID = 2542453596839860853L;

	/** Array of types.
	 */
	private PathElementType[] types;

	/** Array of coords.
	 */
	private int[] coords;

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
	private SoftReference<Rectangle2i> graphicalBounds = null;

	/** Buffer for the bounds of the path that corresponds
	 * to all the points added in the path.
	 */
	private SoftReference<Rectangle2i> logicalBounds = null;

	/**
	 */
	public Path2i() {
		this(DEFAULT_WINDING_RULE);
	}

	/**
	 * @param iterator
	 */
	public Path2i(Iterator<PathElement2i> iterator) {
		this(DEFAULT_WINDING_RULE, iterator);
	}

	/**
	 * @param windingRule
	 */
	public Path2i(PathWindingRule windingRule) {
		assert (windingRule != null) : "Path winding rule must be not null"; //$NON-NLS-1$
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new int[GROW_SIZE];
		this.windingRule = windingRule;
	}

	/**
	 * @param windingRule
	 * @param iterator
	 */
	public Path2i(PathWindingRule windingRule, Iterator<PathElement2i> iterator) {
		assert (windingRule != null) : "Path winding rule must be not null"; //$NON-NLS-1$
		assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new int[GROW_SIZE];
		this.windingRule = windingRule;
		add(iterator);
	}

	/**
	 * @param p
	 */
	public Path2i(Path2ai<?, ?, ?, ?, ?, ?> p) {
		set(p);
	}

	private boolean buildLogicalBoundingBox(Rectangle2i box) {
		if (this.numCoords>0) {
			int xmin = this.coords[0];
			int ymin = this.coords[1];
			int xmax = xmin;
			int ymax = ymin;
			for(int i=2; i<this.numCoords; i+= 2) {
				if (this.coords[i]<xmin) xmin = this.coords[i];
				if (this.coords[i+1]<ymin) ymin = this.coords[i+1];
				if (this.coords[i]>xmax) xmax = this.coords[i];
				if (this.coords[i+1]>ymax) ymax = this.coords[i+1];
			}
			box.setFromCorners(xmin,  ymin, xmax, ymax);
			return true;
		}
		return false;
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
		int px = p.ix();
		int py = p.iy();
		int x, y;
		for(int i=0; i<this.numCoords;) {
			x = this.coords[i++];
			y = this.coords[i++];
			if (x == px && y == py) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void clear() {
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new int[GROW_SIZE];
		this.windingRule = PathWindingRule.NON_ZERO;
		this.numCoords = 0;
		this.numTypes = 0;
		this.isEmpty = Boolean.TRUE;
		this.isPolyline = Boolean.FALSE;
		this.isPolygon = Boolean.FALSE;
		this.isCurved = Boolean.FALSE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
		fireGeometryChange();
	}

	@Pure
	@Override
	public Path2i clone() {
		Path2i clone = super.clone();
		clone.coords = this.coords.clone();
		clone.types = this.types.clone();
		clone.windingRule = this.windingRule;
		return clone;
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + this.numCoords;
		bits = 31 * bits + this.numTypes;
		bits = 31 * bits + Arrays.hashCode(this.coords);
		bits = 31 * bits + Arrays.hashCode(this.types);
		bits = 31 * bits + this.windingRule.ordinal();
		return (bits ^ (bits >> 32));
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
	public void translate(int dx, int dy) {
		for(int i=0; i<this.numCoords;) {
			this.coords[i++] += dx;
			this.coords[i++] += dy;
		}
		Rectangle2i bb;
		bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb!=null) bb.translate(dx, dy);
		bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb!=null) bb.translate(dx, dy);
		fireGeometryChange();
	}

	@Override
	public void transform(Transform2D transform) {
		assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
		Point2D<?, ?> p = new InnerComputationPoint2ai();
		for(int i=0; i<this.numCoords;) {
			p.set(this.coords[i], this.coords[i+1]);
			transform.transform(p);
			this.coords[i++] = p.ix();
			this.coords[i++] = p.iy();
		}
		this.graphicalBounds = null;
		this.logicalBounds = null;
		fireGeometryChange();
	}

	@Override
	@Pure
	public boolean isEmpty() {
		if (this.isEmpty==null) {
			this.isEmpty = Boolean.TRUE;
			PathIterator2ai<PathElement2i> pi = getPathIterator();
			PathElement2i pe;
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
	@Pure
	public Rectangle2i toBoundingBox() {
		Rectangle2i bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = getGeomFactory().newBox();
			Path2ai.computeDrawableElementBoundingBox(
					getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
					bb);
			this.graphicalBounds = new SoftReference<>(bb);
		}
		return bb;

	}

	@Override
	@Pure
	public void toBoundingBox(Rectangle2i box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		Rectangle2i bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = getGeomFactory().newBox();
			Path2ai.computeDrawableElementBoundingBox(
					getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
					bb);
			this.graphicalBounds = new SoftReference<>(bb);
		}
		box.set(bb);
	}

	@Override
	@Pure
	public PathWindingRule getWindingRule() {
		return this.windingRule;
	}

	@Override
	@Pure
	public boolean isPolyline() {
		if (this.isPolyline == null) {
			PathIterator2ai<PathElement2i> pi = getPathIterator();
			PathElement2i pe;
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
	@Pure
	public boolean isCurved() {
		if (this.isCurved == null) {
			this.isCurved = Boolean.FALSE;
			PathIterator2ai<PathElement2i> pi = getPathIterator();
			PathElement2i pe;
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
	@Pure
	public boolean isMultiParts() {
		if (this.isMultipart == null) {
			this.isMultipart = Boolean.FALSE;
			PathIterator2ai<PathElement2i> pi = getPathIterator();
			PathElement2i pe;
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
	@Pure
	public boolean isPolygon() {
		if (this.isPolygon == null) {
			PathIterator2ai<PathElement2i> pi = getPathIterator();
			PathElement2i pe;
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
	public Rectangle2i toBoundingBoxWithCtrlPoints() {
		Rectangle2i bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = getGeomFactory().newBox();
			buildLogicalBoundingBox(bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		return bb;
	}

	@Override
	@Pure
	public void toBoundingBoxWithCtrlPoints(Rectangle2i box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		Rectangle2i bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = getGeomFactory().newBox();
			buildLogicalBoundingBox(bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		box.set(bb);
	}

	@Override
	@Pure
	public int[] toIntArray(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return Arrays.copyOf(this.coords, this.numCoords);
		}
		Point2D<?, ?> p = new InnerComputationPoint2ai();
		int[] clone = new int[this.numCoords];
		for(int i=0; i<clone.length;) {
			p.set(this.coords[i], this.coords[i+1]);
			transform.transform(p);
			clone[i++] = p.ix();
			clone[i++] = p.iy();
		}
		return clone;
	}

	@Override
	@Pure
	public float[] toFloatArray(Transform2D transform) {
		float[] clone = new float[this.numCoords];
		if (transform == null || transform.isIdentity()) {
			for(int i=0; i<this.numCoords; ++i) {
				clone[i] = this.coords[i];
			}
		}
		else {
			Point2D<?, ?> p = new InnerComputationPoint2ai();
			for(int i=0; i<clone.length;) {
				p.set(this.coords[i], this.coords[i+1]);
				transform.transform(p);
				clone[i++] = p.ix();
				clone[i++] = p.iy();
			}
		}
		return clone;
	}

	@Override
	@Pure
	public double[] toDoubleArray(Transform2D transform) {
		double[] clone = new double[this.numCoords];
		if (transform == null || transform.isIdentity()) {
			for(int i=0; i<this.numCoords; ++i) {
				clone[i] = this.coords[i];
			}
		}
		else {
			Point2D<?, ?> p = new InnerComputationPoint2ai();
			for(int i=0; i<clone.length;) {
				p.set(this.coords[i], this.coords[i+1]);
				transform.transform(p);
				clone[i++] = p.ix();
				clone[i++] = p.iy();
			}
		}
		return clone;
	}

	@Override
	@Pure
	public Point2i[] toPointArray(Transform2D transform) {
		Point2i[] clone = new Point2i[this.numCoords/2];
		if (transform == null || transform.isIdentity()) {
			for(int i=0, j=0; j<this.numCoords; ++i) {
				clone[i] = getGeomFactory().newPoint(
						this.coords[j++],
						this.coords[j++]);
			}
		}
		else {
			for(int i=0, j=0; j<clone.length; ++i) {
				clone[i] = getGeomFactory().newPoint(
						this.coords[j++],
						this.coords[j++]);
				transform.transform(clone[i]);
			}
		}
		return clone;
	}

	@Override
	@Pure
	public Point2i getPointAt(int index) {
		return getGeomFactory().newPoint(
				this.coords[index*2],
				this.coords[index*2+1]);
	}

	@Override
	@Pure
	public Point2i getCurrentPoint() {
		return getGeomFactory().newPoint(
				this.coords[this.numCoords-2],
				this.coords[this.numCoords-1]);
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
			fireGeometryChange();
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public void moveTo(int x, int y) {
		if (this.numTypes != 0) {
			this.isPolyline = Boolean.FALSE;
			this.isPolygon = Boolean.FALSE;
		}
		if (this.isMultipart != null && this.isMultipart != Boolean.TRUE) {
			this.isMultipart = null;
		}
		if (this.numTypes>0 && this.types[this.numTypes-1]==PathElementType.MOVE_TO) {
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
		fireGeometryChange();
	}

	@Override
	public void lineTo(int x, int y) {
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
		fireGeometryChange();
	}

	@Override
	public void quadTo(int x1, int y1, int x2, int y2) {
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
		fireGeometryChange();
	}

	@Override
	public void curveTo(int x1, int y1, int x2, int y2, int x3, int y3) {
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
		fireGeometryChange();
	}

	@Override
	@Pure
	public int getCoordAt(int index) {
		return this.coords[index];
	}

	@Override
	public void setLastPoint(int x, int y) {
		if (this.numCoords>=2) {
			this.coords[this.numCoords-2] = x;
			this.coords[this.numCoords-1] = y;
			this.graphicalBounds = null;
			this.logicalBounds = null;
			fireGeometryChange();
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public void setWindingRule(PathWindingRule rule) {
		assert (rule != null) : "Path winding rule must be not null"; //$NON-NLS-1$
		this.windingRule = rule;
	}

	@Override
	public boolean remove(int x, int y) {
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
	public void set(Path2i s) {
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

}
