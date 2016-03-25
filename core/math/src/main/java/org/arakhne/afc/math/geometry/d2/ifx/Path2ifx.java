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

package org.arakhne.afc.math.geometry.d2.ifx;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.arakhne.afc.math.geometry.d2.fp.Point2fp;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;

/** Path with 2 integer FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Path2ifx
	extends AbstractShape2ifx<Path2ifx>
	implements Path2ai<Shape2ifx<?>, Path2ifx, PathElement2ifx, Point2ifx, Rectangle2ifx> {

	private static final long serialVersionUID = -5410743023218999966L;

	/** Array of types.
	 */
	private PathElementType[] types;

	/** Array of coords.
	 */
	private IntegerProperty[] coords;

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
	private SoftReference<Rectangle2ifx> graphicalBounds = null;

	/** Buffer for the bounds of the path that corresponds
	 * to all the points added in the path.
	 */
	private SoftReference<Rectangle2ifx> logicalBounds = null;

	/**
	 */
	public Path2ifx() {
		this(DEFAULT_WINDING_RULE);
	}

	/**
	 * @param iterator
	 */
	public Path2ifx(Iterator<PathElement2ifx> iterator) {
		this(DEFAULT_WINDING_RULE, iterator);
	}

	/**
	 * @param windingRule
	 */
	public Path2ifx(PathWindingRule windingRule) {
		assert(windingRule != null);
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new IntegerProperty[GROW_SIZE];
		this.windingRule = windingRule;
	}

	/**
	 * @param windingRule
	 * @param iterator
	 */
	public Path2ifx(PathWindingRule windingRule, Iterator<PathElement2ifx> iterator) {
		assert(windingRule!=null);
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new IntegerProperty[GROW_SIZE];
		this.windingRule = windingRule;
		add(iterator);
	}

	/**
	 * @param p
	 */
	public Path2ifx(Path2ai<?, ?, ?, ?, ?> p) {
		set(p);
	}

	private boolean buildLogicalBoundingBox(Rectangle2ifx box) {
		if (this.numCoords>0) {
			int xmin = getCoordAt(0);
			int ymin = getCoordAt(1);
			int xmax = xmin;
			int ymax = ymin;
			int x, y;
			for(int i = 2; i < this.numCoords; i += 2) {
				x = getCoordAt(i);
				y = getCoordAt(i + 1);
				if (x < xmin) xmin = x;
				if (y < ymin) ymin = y;
				if (x > xmax) xmax = x;
				if (y > ymax) ymax = y;
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
	public boolean containsControlPoint(Point2D p) {
		int x, y;
		for(int i=0; i<this.numCoords;) {
			x = getCoordAt(i++);
			y = getCoordAt(i++);
			if (x==p.ix() && y==p.iy()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void clear() {
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new IntegerProperty[GROW_SIZE];
		this.windingRule = PathWindingRule.NON_ZERO;
		this.numCoords = 0;
		this.numTypes = 0;
		this.isEmpty = Boolean.TRUE;
		this.isPolyline = Boolean.FALSE;
		this.isPolygon = Boolean.FALSE;
		this.isCurved = Boolean.FALSE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	@Pure
	@Override
	public Path2ifx clone() {
		Path2ifx clone = super.clone();
		clone.coords = new IntegerProperty[this.coords.length];
		for (int i = 0; i < this.coords.length; i++) {
			if (this.coords[i] != null) {
				clone.coordPropertyAt(i).set(this.coords[i].get());
			}
		}
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
			b.append(getCoordAt(0));
			for(int i=1; i<this.numCoords; ++i) {
				b.append(", "); //$NON-NLS-1$
				b.append(getCoordAt(i));
			}
		}
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Override
	public void translate(int dx, int dy) {
		IntegerProperty prop;
		for(int i=0; i<this.numCoords;) {
			prop = coordPropertyAt(i++);
			prop.set(prop.get() + dx);
			prop = coordPropertyAt(i++);
			prop.set(prop.get() + dy);
		}
		Rectangle2ifx bb;
		bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb!=null) bb.translate(dx, dy);
		bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb!=null) bb.translate(dx, dy);
	}
	
	@Override
	public void transform(Transform2D transform) {
		if (transform != null) {
			for(int i=0; i<this.numCoords;) {
				Point2D p = new Point2ifx(coordPropertyAt(i++), coordPropertyAt(i++));
				transform.transform(p);
			}
			this.graphicalBounds = null;
			this.logicalBounds = null;
		}
	}

	@Override
	public boolean isEmpty() {
		if (this.isEmpty==null) {
			this.isEmpty = Boolean.TRUE;
			PathIterator2ai<PathElement2ifx> pi = getPathIterator();
			PathElement2ifx pe;
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
	public Rectangle2ifx toBoundingBox() {
		Rectangle2ifx bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = new Rectangle2ifx();
			Path2ai.computeDrawableElementBoundingBox(
					getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
					bb);
			this.graphicalBounds = new SoftReference<>(bb);
		}
		return bb;

	}

	@Override
	public void toBoundingBox(Rectangle2ifx box) {
		Rectangle2ifx bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = new Rectangle2ifx();
			Path2ai.computeDrawableElementBoundingBox(
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
			PathIterator2ai<PathElement2ifx> pi = getPathIterator();
			PathElement2ifx pe;
			PathElementType t;
			boolean first = true;
			boolean lastIsClose = false;
			while (this.isPolyline == null && pi.hasNext()) {
				pe = pi.next();
				t = pe.getType();
				lastIsClose = false;
				if (first) {
					if (t != PathElementType.MOVE_TO) {
						this.isPolyline = Boolean.FALSE;
					} else {
						first = false;
					}
				} else if (t != PathElementType.LINE_TO) {
					this.isPolyline = Boolean.FALSE;
				} else if (t == PathElementType.CLOSE) {
					lastIsClose = true;
				}
			}
			if (this.isPolyline == null) {
				this.isPolyline = Boolean.valueOf(!lastIsClose);
			}
		}
		return this.isPolyline.booleanValue();
	}

	@Override
	public boolean isCurved() {
		if (this.isCurved == null) {
			this.isCurved = Boolean.FALSE;
			PathIterator2ai<PathElement2ifx> pi = getPathIterator();
			PathElement2ifx pe;
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
			PathIterator2ai<PathElement2ifx> pi = getPathIterator();
			PathElement2ifx pe;
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
			PathIterator2ai<PathElement2ifx> pi = getPathIterator();
			PathElement2ifx pe;
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
		}
	}

	@Override
	public Rectangle2ifx toBoundingBoxWithCtrlPoints() {
		Rectangle2ifx bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = new Rectangle2ifx();
			buildLogicalBoundingBox(bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		return bb;
	}

	@Override
	public void toBoundingBoxWithCtrlPoints(Rectangle2ifx box) {
		Rectangle2ifx bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = new Rectangle2ifx();
			buildLogicalBoundingBox(bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		box.set(bb);
	}

	@Override
	public int[] toIntArray(Transform2D transform) {
		int[] clone = new int[this.numCoords];
		if (transform==null) {
			for(int i=0; i<clone.length;) {
				clone[i] = getCoordAt(i);
				++i;
				clone[i] = getCoordAt(i);
				++i;
			}
		} else {
			Point2fp p = new Point2fp();
			for(int i=0; i<clone.length;) {
				p.set(getCoordAt(i), getCoordAt(i + 1));
				transform.transform(p);
				clone[i++] = p.ix();
				clone[i++] = p.ix();
			}
		}
		return clone;
	}

	@Override
	public float[] toFloatArray(Transform2D transform) {
		float[] clone = new float[this.numCoords];
		if (transform==null) {
			for(int i=0; i<clone.length;) {
				clone[i] = getCoordAt(i);
				++i;
				clone[i] = getCoordAt(i);
				++i;
			}
		} else {
			Point2fp p = new Point2fp();
			for(int i=0; i<clone.length;) {
				p.set(getCoordAt(i), getCoordAt(i + 1));
				transform.transform(p);
				clone[i++] = (float) p.getX();
				clone[i++] = (float) p.getY();
			}
		}
		return clone;
	}

	@Override
	public double[] toDoubleArray(Transform2D transform) {
		double[] clone = new double[this.numCoords];
		if (transform==null) {
			for(int i=0; i<clone.length;) {
				clone[i] = getCoordAt(i);
				++i;
				clone[i] = getCoordAt(i);
				++i;
			}
		} else {
			Point2fp p = new Point2fp();
			for(int i=0; i<clone.length;) {
				p.set(getCoordAt(i), getCoordAt(i + 1));
				transform.transform(p);
				clone[i++] = p.getX();
				clone[i++] = p.getY();
			}
		}
		return clone;
	}

	@Override
	public Point2D[] toPointArray(Transform2D transform) {
		Point2D[] points = new Point2D[this.numCoords / 2];
		if (transform==null) {
			for(int i = 0, j = 0; i<points.length; ++i) {
				int x = getCoordAt(j);
				++j;
				int y = getCoordAt(j);
				++j;
				points[i] = new Point2ifx(x, y);
			}
		} else {
			Point2fp p = new Point2fp();
			for(int i = 0, j = 0; i<points.length; ++i) {
				p.set(getCoordAt(j++), getCoordAt(j++));
				transform.transform(p);
				points[i] = new Point2ifx(p.getX(), p.getY());
			}
		}
		return points;
	}

	@Override
	public Point2ifx getPointAt(int index) {
		return new Point2ifx(
				this.coords[index*2],
				this.coords[index*2+1]);
	}

	@Override
	public Point2ifx getCurrentPoint() {
		return new Point2ifx(
				this.coords[this.numCoords-2],
				this.coords[this.numCoords-1]);
	}

	@Override
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
		}
	}

	@Override
	public void moveTo(int x, int y) {
		if (this.numTypes != 0) {
			this.isPolyline = Boolean.FALSE;
			this.isPolygon = Boolean.FALSE;
		}
		this.isMultipart = Boolean.valueOf(this.isMultipart == Boolean.TRUE);
		if (this.numTypes>0 && this.types[this.numTypes-1]==PathElementType.MOVE_TO) {
			coordPropertyAt(this.numCoords-2).set(x);
			coordPropertyAt(this.numCoords-1).set(y);
		}
		else {
			ensureSlots(false, 2);
			this.types[this.numTypes++] = PathElementType.MOVE_TO;
			coordPropertyAt(this.numCoords++).set(x);
			coordPropertyAt(this.numCoords++).set(y);
		}
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	@Override
	public void lineTo(int x, int y) {
		ensureSlots(true, 2);
		this.types[this.numTypes++] = PathElementType.LINE_TO;
		coordPropertyAt(this.numCoords++).set(x);
		coordPropertyAt(this.numCoords++).set(y);
		this.isEmpty = null;
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	@Override
	public void quadTo(int x1, int y1, int x2, int y2) {
		ensureSlots(true, 4);
		this.types[this.numTypes++] = PathElementType.QUAD_TO;
		coordPropertyAt(this.numCoords++).set(x1);
		coordPropertyAt(this.numCoords++).set(y1);
		coordPropertyAt(this.numCoords++).set(x2);
		coordPropertyAt(this.numCoords++).set(y2);
		this.isEmpty = null;
		this.isPolyline = Boolean.FALSE;
		this.isCurved = Boolean.TRUE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	@Override
	public void curveTo(int x1, int y1, int x2, int y2, int x3, int y3) {
		ensureSlots(true, 6);
		this.types[this.numTypes++] = PathElementType.CURVE_TO;
		coordPropertyAt(this.numCoords++).set(x1);
		coordPropertyAt(this.numCoords++).set(y1);
		coordPropertyAt(this.numCoords++).set(x2);
		coordPropertyAt(this.numCoords++).set(y2);
		coordPropertyAt(this.numCoords++).set(x3);
		coordPropertyAt(this.numCoords++).set(y3);
		this.isEmpty = null;
		this.isPolyline = Boolean.FALSE;
		this.isCurved = Boolean.TRUE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	@Override
	public int getCoordAt(int index) {
		return this.coords[index] == null ? 0 : this.coords[index].get();
	}
	
	/** Replies the property that is the coordinate at the given index.
	 *
	 * @param index the position of the coordinate.
	 * @return the coordinate property.
	 */
	@Pure
	public IntegerProperty coordPropertyAt(int index) {
		if (this.coords[index] == null) {
			final String label = "coord[" + index + "]"; //$NON-NLS-1$ //$NON-NLS-2$
			this.coords[index] = new IntegerPropertyBase(0) {
				@Override
				public String getName() {
					return label;
				}
				
				@Override
				public Object getBean() {
					return Path2ifx.this;
				}
			};
		}
		return this.coords[index];
	}

	@Override
	public void setLastPoint(int x, int y) {
		if (this.numCoords>=2) {
			coordPropertyAt(this.numCoords-2).set(x);
			coordPropertyAt(this.numCoords-1).set(y);
			this.graphicalBounds = null;
			this.logicalBounds = null;
		}
	}
	
	@Override
	public void setWindingRule(PathWindingRule r) {
		assert(r!=null);
		this.windingRule = r;
	}

	@Override
	public boolean remove(int x, int y) {
		for(int i=0, j=0; i<this.numCoords && j<this.numTypes;) {
			switch(this.types[j]) {
			case MOVE_TO:
				this.isMultipart = null;
				//$FALL-THROUGH$
			case LINE_TO:
				if (x==getCoordAt(i) && y==getCoordAt(i+1)) {
					this.numCoords -= 2;
					--this.numTypes;
					System.arraycopy(this.coords, i+2, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j+1, this.types, j, this.numTypes);
					this.isEmpty = null;
					return true;
				}
				i += 2;
				++j;
				break;
			case CURVE_TO:
				if ((x==getCoordAt(i) && y==getCoordAt(i+1))
						||(x==getCoordAt(i+2) && y==getCoordAt(i+3))
						||(x==getCoordAt(i+4) && y==getCoordAt(i+5))) {
					this.numCoords -= 6;
					--this.numTypes;
					System.arraycopy(this.coords, i+6, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j+1, this.types, j, this.numTypes);
					this.isEmpty = null;
					this.isPolyline = null;
					return true;
				}
				i += 6;
				++j;
				break;
			case QUAD_TO:
				if ((x==getCoordAt(i) && y==getCoordAt(i+1))
						||(x==getCoordAt(i+2) && y==getCoordAt(i+3))) {
					this.numCoords -= 4;
					--this.numTypes;
					System.arraycopy(this.coords, i+4, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j+1, this.types, j, this.numTypes);
					this.isEmpty = null;
					this.isPolyline = null;
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
	public void set(Path2ifx s) {
		clear();
		add(s.getPathIterator());
	}

	@Override
	public int getPathElementCount() {
		return this.numTypes;
	}

	@Override
	public PathElementType getPathElementTypeAt(int index) {
		return this.types[index];
	}

}
