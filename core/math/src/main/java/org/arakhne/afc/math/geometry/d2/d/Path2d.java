/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.d2.d;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.afp.InnerComputationPoint2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.locale.Locale;

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
	private int numTypes;

	/** Number of coords in the array.
	 */
	private int numCoords;

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

	/** Indicates if the path is a polygon.
	 */
	private Boolean isPolygon = Boolean.FALSE;

	/** Indicates if the path is multipart.
	 */
	private Boolean isMultipart = Boolean.FALSE;

	/** Buffer for the bounds of the path that corresponds
	 * to the points really on the path (eg, the pixels
	 * drawn). The control points of the curves are
	 * not considered in this bounds.
	 */
	private SoftReference<Rectangle2d> graphicalBounds;

	/** Buffer for the bounds of the path that corresponds
	 * to all the points added in the path.
	 */
	private SoftReference<Rectangle2d> logicalBounds;

	/** Buffer for the length of the path.
	 */
	private Double length;

	/** Construct an empty path.
	 */
	public Path2d() {
		this(DEFAULT_WINDING_RULE);
	}

	/** Construct a path by copying the given elements.
	 * @param iterator the iterator that provides the elements to copy.
	 */
	public Path2d(Iterator<PathElement2d> iterator) {
		this(DEFAULT_WINDING_RULE, iterator);
	}

	/** Create an empty path with the given path winding rule.
	 * @param windingRule the path winding rule.
	 */
	public Path2d(PathWindingRule windingRule) {
		assert windingRule != null : AssertMessages.notNullParameter();
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = windingRule;
	}

	/** Create an empty path with the given path winding rule, and by copying the given elements.
	 * @param windingRule the path winding rule.
	 * @param iterator the iterator that provides the elements to copy.
	 */
	public Path2d(PathWindingRule windingRule, Iterator<PathElement2d> iterator) {
		assert windingRule != null : AssertMessages.notNullParameter(0);
		assert iterator != null : AssertMessages.notNullParameter(1);
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = windingRule;
		add(iterator);
	}

	/** Constructor by copy.
	 * @param path the path to copy.
	 */
	public Path2d(Path2afp<?, ?, ?, ?, ?, ?> path) {
		set(path);
	}

	private void ensureSlots(boolean needMove, int nbSlots) {
		if (needMove && this.numTypes == 0) {
			throw new IllegalStateException(Locale.getString("E1")); //$NON-NLS-1$
		}
		if (this.types.length == this.numTypes) {
			this.types = Arrays.copyOf(this.types, this.types.length + GROW_SIZE);
		}
		while ((this.numCoords + nbSlots) >= this.coords.length) {
			this.coords = Arrays.copyOf(this.coords, this.coords.length + GROW_SIZE);
		}
	}

	@Pure
	@Override
	public boolean containsControlPoint(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		for (int i = 0; i < this.numCoords; i += 2) {
			final double x = this.coords[i];
			final double y = this.coords[i + 1];
			if (x == pt.getX() && y == pt.getY()) {
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
		final Path2d clone = super.clone();
		clone.coords = this.coords.clone();
		clone.types = this.types.clone();
		clone.windingRule = this.windingRule;
		return clone;
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Integer.hashCode(this.numCoords);
		bits = 31 * bits + Integer.hashCode(this.numTypes);
		bits = 31 * bits + Arrays.hashCode(this.coords);
		bits = 31 * bits + Arrays.hashCode(this.types);
		bits = 31 * bits + this.windingRule.hashCode();
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		if (this.numCoords > 0) {
			b.append(this.coords[0]);
			for (int i = 1; i < this.numCoords; ++i) {
				b.append(", "); //$NON-NLS-1$
				b.append(this.coords[i]);
			}
		}
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Override
	public void translate(double dx, double dy) {
		for (int i = 0; i < this.numCoords; i += 2) {
			this.coords[i] += dx;
			this.coords[i + 1] += dy;
		}
		Rectangle2d bb;
		bb = this.logicalBounds == null ? null : this.logicalBounds.get();
		if (bb != null) {
			bb.translate(dx, dy);
		}
		bb = this.graphicalBounds == null ? null : this.graphicalBounds.get();
		if (bb != null) {
			bb.translate(dx, dy);
		}
		fireGeometryChange();
	}

	@Override
	public void transform(Transform2D transform) {
		assert transform != null : AssertMessages.notNullParameter();
		final Point2D<?, ?> p = new InnerComputationPoint2afp();
		for (int i = 0; i < this.numCoords; i += 2) {
			p.set(this.coords[i], this.coords[i + 1]);
			transform.transform(p);
			this.coords[i] = p.getX();
			this.coords[i + 1] = p.getY();
		}
		this.graphicalBounds = null;
		this.logicalBounds = null;
		this.length = null;
		fireGeometryChange();
	}

	@Override
	public boolean isEmpty() {
		if (this.isEmpty == null) {
			this.isEmpty = Boolean.TRUE;
			final PathIterator2afp<PathElement2d> pi = getPathIterator();
			while (this.isEmpty == Boolean.TRUE && pi.hasNext()) {
				final PathElement2d pe = pi.next();
				if (pe.isDrawable()) {
					this.isEmpty = Boolean.FALSE;
				}
			}
		}
		return this.isEmpty.booleanValue();
	}

	@Override
	public Rectangle2d toBoundingBox() {
		Rectangle2d bb = this.graphicalBounds == null ? null : this.graphicalBounds.get();
		if (bb == null) {
			bb = getGeomFactory().newBox();
			Path2afp.calculatesDrawableElementBoundingBox(
					getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
					bb);
			this.graphicalBounds = new SoftReference<>(bb);
		}
		return bb;

	}

	@Override
	public void toBoundingBox(Rectangle2d box) {
		assert box != null : AssertMessages.notNullParameter();
		Rectangle2d bb = this.graphicalBounds == null ? null : this.graphicalBounds.get();
		if (bb == null) {
			bb = getGeomFactory().newBox();
			Path2afp.calculatesDrawableElementBoundingBox(
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
			final PathIterator2afp<PathElement2d> pi = getPathIterator();
			boolean first = true;
			boolean hasOneLine = false;
			while (this.isPolyline == null && pi.hasNext()) {
				final PathElement2d pe = pi.next();
				final PathElementType t = pe.getType();
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
			final PathIterator2afp<PathElement2d> pi = getPathIterator();
			while (this.isCurved == Boolean.FALSE && pi.hasNext()) {
				final PathElement2d pe = pi.next();
				final PathElementType t = pe.getType();
				if (t == PathElementType.CURVE_TO || t == PathElementType.QUAD_TO) {
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
			final PathIterator2afp<PathElement2d> pi = getPathIterator();
			boolean foundOne = false;
			while (this.isMultipart == Boolean.FALSE && pi.hasNext()) {
				final PathElement2d pe = pi.next();
				final PathElementType t = pe.getType();
				if (t == PathElementType.MOVE_TO) {
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
			final PathIterator2afp<PathElement2d> pi = getPathIterator();
			boolean first = true;
			boolean lastIsClose = false;
			while (this.isPolygon == null && pi.hasNext()) {
				final PathElement2d pe = pi.next();
				final PathElementType t = pe.getType();
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
		if (this.numTypes <= 0
				|| (this.types[this.numTypes - 1] != PathElementType.CLOSE
				&& this.types[this.numTypes - 1] != PathElementType.MOVE_TO)) {
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
		Rectangle2d bb = this.logicalBounds == null ? null : this.logicalBounds.get();
		if (bb == null) {
			bb = getGeomFactory().newBox();
			Path2afp.calculatesControlPointBoundingBox(
					getPathIterator(),
					bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		return bb;
	}

	@Override
	@Pure
	public void toBoundingBoxWithCtrlPoints(Rectangle2d box) {
		assert box != null : AssertMessages.notNullParameter();
		Rectangle2d bb = this.logicalBounds == null ? null : this.logicalBounds.get();
		if (bb == null) {
			bb = getGeomFactory().newBox();
			Path2afp.calculatesControlPointBoundingBox(
					getPathIterator(),
					bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		box.set(bb);
	}

	@Override
	@Pure
	public int[] toIntArray(Transform2D transform) {
		final int[] clone = new int[this.numCoords];
		if (transform == null || transform.isIdentity()) {
			for (int i = 0; i < this.numCoords; ++i) {
				clone[i] = (int) this.coords[i];
			}
		} else {
			final Point2D<?, ?> p = new InnerComputationPoint2afp();
			for (int i = 0; i < clone.length; i += 2) {
				p.set(this.coords[i], this.coords[i + 1]);
				transform.transform(p);
				clone[i] = p.ix();
				clone[i + 1] = p.iy();
			}
		}
		return clone;
	}

	@Override
	@Pure
	public float[] toFloatArray(Transform2D transform) {
		final float[] clone = new float[this.numCoords];
		if (transform == null || transform.isIdentity()) {
			for (int i = 0; i < this.numCoords; ++i) {
				clone[i] = (float) this.coords[i];
			}
		} else {
			final Point2D<?, ?> p = new InnerComputationPoint2afp();
			for (int i = 0; i < clone.length; i += 2) {
				p.set(this.coords[i], this.coords[i + 1]);
				transform.transform(p);
				clone[i] = (float) p.getX();
				clone[i + 1] = (float) p.getY();
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
		final Point2D<?, ?> p = new InnerComputationPoint2afp();
		final double[] clone = new double[this.numCoords];
		for (int i = 0; i < clone.length; i += 2) {
			p.set(this.coords[i], this.coords[i + 1]);
			transform.transform(p);
			clone[i] = p.getX();
			clone[i + 1] = p.getY();
		}
		return clone;
	}

	@Override
	@Pure
	public Point2d[] toPointArray(Transform2D transform) {
		final Point2d[] clone = new Point2d[this.numCoords / 2];
		if (transform == null || transform.isIdentity()) {
			for (int i = 0, j = 0; j < this.numCoords; ++i, j += 2) {
				clone[i] = getGeomFactory().newPoint(
						this.coords[j],
						this.coords[j + 1]);
			}
		} else {
			for (int i = 0, j = 0; j < clone.length; ++i, j += 2) {
				clone[i] = getGeomFactory().newPoint(
						this.coords[j],
						this.coords[j + 1]);
				transform.transform(clone[i]);
			}
		}
		return clone;
	}

	@Override
	@Pure
	public Point2d getPointAt(int index) {
		final int didx = index * 2;
		return getGeomFactory().newPoint(
				this.coords[didx],
				this.coords[didx + 1]);
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
		return this.numCoords / 2;
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public void removeLast() {
		if (this.numTypes > 0) {
			switch (this.types[this.numTypes - 1]) {
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
			case ARC_TO:
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
		if (this.numTypes > 0 && this.types[this.numTypes - 1] == PathElementType.MOVE_TO) {
			this.coords[this.numCoords - 2] = x;
			this.coords[this.numCoords - 1] = y;
		} else {
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
	@SuppressWarnings("checkstyle:magicnumber")
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
	@SuppressWarnings("checkstyle:magicnumber")
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
		if (this.numCoords >= 2) {
			this.coords[this.numCoords - 2] = x;
			this.coords[this.numCoords - 1] = y;
			this.graphicalBounds = null;
			this.logicalBounds = null;
			this.length = null;
			fireGeometryChange();
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public void setWindingRule(PathWindingRule rule) {
		assert rule != null : AssertMessages.notNullParameter();
		this.windingRule = rule;
	}

	@Override
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:fallthrough",
			"checkstyle:cyclomaticcomplexity"})
	public boolean remove(double x, double y) {
		for (int i = 0, j = 0; i < this.numCoords && j < this.numTypes;) {
			switch (this.types[j]) {
			case MOVE_TO:
				this.isMultipart = null;
				//$FALL-THROUGH$
			case LINE_TO:
				if (x == this.coords[i] && y == this.coords[i + 1]) {
					this.numCoords -= 2;
					--this.numTypes;
					System.arraycopy(this.coords, i + 2, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j + 1, this.types, j, this.numTypes);
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
				if ((x == this.coords[i] && y == this.coords[i + 1])
						|| (x == this.coords[i + 2] && y == this.coords[i + 3])
						|| (x == this.coords[i + 4] && y == this.coords[i + 5])) {
					this.numCoords -= 6;
					--this.numTypes;
					System.arraycopy(this.coords, i + 6, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j + 1, this.types, j, this.numTypes);
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
				if ((x == this.coords[i] && y == this.coords[i + 1])
						|| (x == this.coords[i + 2] && y == this.coords[i + 3])) {
					this.numCoords -= 4;
					--this.numTypes;
					System.arraycopy(this.coords, i + 4, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j + 1, this.types, j, this.numTypes);
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
			case ARC_TO:
			default:
				throw new IllegalStateException();
			}
		}
		return false;
	}

	@Override
	public void set(Path2d path) {
		assert path != null : AssertMessages.notNullParameter();
		clear();
		add(path.getPathIterator());
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
			this.length = Double.valueOf(Path2afp.calculatesPathLength(getPathIterator()));
		}
		return this.length.doubleValue();
	}

}
