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

package org.arakhne.afc.math.geometry.d2.ifx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Objects;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.locale.Locale;

/** Path with 2 integer FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Path2ifx extends AbstractShape2ifx<Path2ifx>
		implements Path2ai<Shape2ifx<?>, Path2ifx, PathElement2ifx, Point2ifx, Vector2ifx, Rectangle2ifx> {

	private static final long serialVersionUID = -5410743023218999966L;

	/** Array of types.
	 */
	private ReadOnlyListWrapper<PathElementType> types;

	/** Array of coords.
	 */
	private ReadOnlyListWrapper<Point2ifx> coords;

	/** Winding rule for the path.
	 */
	private ObjectProperty<PathWindingRule> windingRule;

	/** Indicates if the path is empty.
	 * The path is empty when there is no point inside, or
	 * all the points are at the same coordinate, or
	 * when the path does not represents a drawable path
	 * (a path with a line or a curve).
	 */
	private BooleanProperty isEmpty;

	/** Indicates if the path is a polyline.
	 */
	private BooleanProperty isPolyline;

	/** Indicates if the path is curved.
	 */
	private BooleanProperty isCurved;

	/** Indicates if the path is a polygon.
	 */
	private BooleanProperty isPolygon;

	/** Indicates if the path is multipart.
	 */
	private BooleanProperty isMultipart;

	/** Buffer for the bounds of the path that corresponds
	 * to all the points added in the path.
	 */
	private ObjectProperty<Rectangle2ifx> logicalBounds;

	/** Construct an empty path.
	 */
	public Path2ifx() {
		this(DEFAULT_WINDING_RULE);
	}

	/** Construct a path by copying the given elements.
	 * @param iterator the iterator that provides the elements to copy.
	 */
	public Path2ifx(Iterator<PathElement2ifx> iterator) {
		this(DEFAULT_WINDING_RULE, iterator);
	}

	/** Construct an empty path with the given path winding rule.
	 * @param windingRule the path winding rule.
	 */
	public Path2ifx(PathWindingRule windingRule) {
		assert windingRule != null : AssertMessages.notNullParameter();
		if (windingRule != DEFAULT_WINDING_RULE) {
			windingRuleProperty().set(windingRule);
		}
	}

	/** Construct a path by copying the given elements, and the given path winding rule.
	 * @param windingRule the path winding rule.
	 * @param iterator the iterator that provides the elements to copy.
	 */
	public Path2ifx(PathWindingRule windingRule, Iterator<PathElement2ifx> iterator) {
		assert windingRule != null : AssertMessages.notNullParameter(0);
		assert iterator != null : AssertMessages.notNullParameter(1);
		if (windingRule != DEFAULT_WINDING_RULE) {
			windingRuleProperty().set(windingRule);
		}
		add(iterator);
	}

	/** Constructor by copy.
	 * @param path the path to copy.
	 */
	public Path2ifx(Path2ai<?, ?, ?, ?, ?, ?> path) {
		set(path);
	}

	@Pure
	@Override
	public boolean containsControlPoint(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		if (this.coords != null && !this.coords.isEmpty()) {
			final int size = this.coords.size();
			for (int i = 0; i < size; i++) {
			    final Point2ifx point = this.coords.get(i);
				if (point.ix() == pt.ix() && point.iy() == pt.iy()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void clear() {
		if (this.types != null) {
			this.types.clear();
		}
		if (this.coords != null) {
			this.coords.clear();
		}
	}

	@Pure
	@Override
	public Path2ifx clone() {
		final Path2ifx clone = super.clone();
		clone.coords = null;
		if (this.coords != null && !this.coords.isEmpty()) {
			clone.innerPointsProperty().addAll(this.coords);
		}
		clone.types = null;
		if (this.types != null && !this.types.isEmpty()) {
			clone.innerTypesProperty().addAll(this.types);
		}
		clone.windingRule = null;
		if (this.windingRule != null) {
			clone.windingRuleProperty().set(this.windingRule.get());
		}
		clone.boundingBox = null;
		clone.logicalBounds = null;
		clone.isCurved = null;
		clone.isMultipart = null;
		clone.isPolyline = null;
		clone.isPolygon = null;
		clone.isEmpty = null;

		return clone;
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Objects.hashCode(this.coords);
		bits = 31 * bits + Objects.hashCode(this.types);
		bits = 31 * bits + Objects.hashCode(this.windingRule);
        return bits ^ (bits >> 31);
	}

	@Override
	public void translate(int dx, int dy) {
		if (this.coords != null && !this.coords.isEmpty()) {
			final ListIterator<Point2ifx> li = this.coords.listIterator();
			while (li.hasNext()) {
			    final Point2ifx point = li.next();
				point.add(dx, dy);
			}
		}
	}

	@Override
	public void transform(Transform2D transform) {
		assert transform != null : AssertMessages.notNullParameter();
		if (this.coords != null && !this.coords.isEmpty()) {
			final ListIterator<Point2ifx> li = this.coords.listIterator();
			while (li.hasNext()) {
				transform.transform(li.next());
			}
		}
	}

	/** Replies the isEmpty property.
	 *
	 * @return the isEmpty property.
	 */
	public BooleanProperty isEmptyProperty() {
		if (this.isEmpty == null) {
			this.isEmpty = new SimpleBooleanProperty(this, MathFXAttributeNames.IS_EMPTY);
			this.isEmpty.bind(Bindings.createBooleanBinding(() -> {
				final PathIterator2ai<PathElement2ifx> pi = getPathIterator();
				while (pi.hasNext()) {
					final PathElement2ifx pe = pi.next();
					if (pe.isDrawable()) {
						return false;
					}
				}
				return true;
			}, innerTypesProperty(), innerPointsProperty()));
		}
		return this.isEmpty;
	}

	@Override
	public boolean isEmpty() {
		return isEmptyProperty().get();
	}

	@Override
	public ObjectProperty<Rectangle2ifx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new ReadOnlyObjectWrapper<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() -> {
				final Rectangle2ifx bb = getGeomFactory().newBox();
				Path2ai.calculatesDrawableElementBoundingBox(
						getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						bb);
				return bb;
			}, innerPointsProperty()));
		}
		return this.boundingBox;
	}

	/** Replies the property that corresponds to the bounding box of the control points.
	 *
	 * <p>The replied box is not the one corresponding to the drawable elements, as replied
	 * by {@link #boundingBoxProperty()}.
	 *
	 * @return the bounding box of the control points.
	 */
	public ObjectProperty<Rectangle2ifx> controlPointBoundingBoxProperty() {
		if (this.logicalBounds == null) {
			this.logicalBounds = new ReadOnlyObjectWrapper<>(this, MathFXAttributeNames.CONTROL_POINT_BOUNDING_BOX);
			this.logicalBounds.bind(Bindings.createObjectBinding(() -> {
				final Rectangle2ifx bb = getGeomFactory().newBox();
				Path2ai.calculatesControlPointBoundingBox(
						getPathIterator(),
						bb);
				return bb;
			}, innerPointsProperty()));
		}
		return this.logicalBounds;
	}

	@Override
	public Rectangle2ifx toBoundingBox() {
		return boundingBoxProperty().get().clone();
	}

	@Override
	public void toBoundingBox(Rectangle2ifx box) {
		assert box != null : AssertMessages.notNullParameter();
		box.set(boundingBoxProperty().get());
	}

	/** Replies the windingRule property.
	 *
	 * @return the windingRule property.
	 */
	public ObjectProperty<PathWindingRule> windingRuleProperty() {
		if (this.windingRule == null) {
			this.windingRule = new SimpleObjectProperty<>(this, MathFXAttributeNames.WINDING_RULE, DEFAULT_WINDING_RULE);
		}
		return this.windingRule;
	}

	@Override
	public PathWindingRule getWindingRule() {
		return this.windingRule == null ? DEFAULT_WINDING_RULE : this.windingRule.get();
	}

	@Override
	public void setWindingRule(PathWindingRule rule) {
		assert rule != null : AssertMessages.notNullParameter();
		if (this.windingRule != null || rule != DEFAULT_WINDING_RULE) {
			windingRuleProperty().set(rule);
		}
	}

	/** Replies the isPolyline property.
	 *
	 * @return the isPolyline property.
	 */
	public BooleanProperty isPolylineProperty() {
		if (this.isPolyline == null) {
			this.isPolyline = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_POLYLINE, false);
			this.isPolyline.bind(Bindings.createBooleanBinding(() -> {
				boolean first = true;
				boolean hasOneLine = false;
				for (final PathElementType type : innerTypesProperty()) {
					if (first) {
						if (type != PathElementType.MOVE_TO) {
							return false;
						}
						first = false;
					} else if (type != PathElementType.LINE_TO) {
						return false;
					} else {
						hasOneLine = true;
					}
				}
				return hasOneLine;
			}, innerTypesProperty()));
		}
		return this.isPolyline;
	}

	@Override
	public boolean isPolyline() {
		return isPolylineProperty().get();
	}

	/** Replies the isCurved property.
	 *
	 * @return the isCurved property.
	 */
	public  BooleanProperty isCurvedProperty() {
		if (this.isCurved == null) {
			this.isCurved = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_CURVED, false);
			this.isCurved.bind(Bindings.createBooleanBinding(() -> {
				for (final PathElementType type : innerTypesProperty()) {
					if (type == PathElementType.CURVE_TO || type == PathElementType.QUAD_TO) {
						return true;
					}
				}
				return false;
			}, innerTypesProperty()));
		}
		return this.isCurved;
	}

	@Override
	public boolean isCurved() {
		return isCurvedProperty().get();
	}

	/** Replies the isMultiParts property.
	 *
	 * @return the isMultiParts property.
	 */
	public BooleanProperty isMultiPartsProperty() {
		if (this.isMultipart == null) {
			this.isMultipart = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_MULTIPARTS, false);
			this.isMultipart.bind(Bindings.createBooleanBinding(() -> {
				boolean foundOne = false;
				for (final PathElementType type : innerTypesProperty()) {
					if (type == PathElementType.MOVE_TO) {
						if (foundOne) {
							return true;
						}
						foundOne = true;
					}
				}
				return false;
			}, innerTypesProperty()));
		}
		return this.isMultipart;
	}

	@Override
	public boolean isMultiParts() {
		return isMultiPartsProperty().get();
	}

	/** Replies the isPolygon property.
	 *
	 * @return the isPolygon property.
	 */
	public BooleanProperty isPolygonProperty() {
		if (this.isPolygon == null) {
			this.isPolygon = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_POLYGON, false);
			this.isPolygon.bind(Bindings.createBooleanBinding(() -> {
				boolean first = true;
				boolean lastIsClose = false;
				for (final PathElementType type : innerTypesProperty()) {
					lastIsClose = false;
					if (first) {
						if (type != PathElementType.MOVE_TO) {
							return false;
						}
						first = false;
					} else if (type == PathElementType.MOVE_TO) {
						return false;
					} else if (type == PathElementType.CLOSE) {
						lastIsClose = true;
					}
				}
				return lastIsClose;
			}, innerTypesProperty()));
		}
		return this.isPolygon;
	}

	@Override
	public boolean isPolygon() {
		return isPolygonProperty().get();
	}

	@Override
	public void closePath() {
		if (this.types == null
				|| this.types.isEmpty()
				|| (this.types.get(this.types.size() - 1) != PathElementType.CLOSE
				&& this.types.get(this.types.size() - 1) != PathElementType.MOVE_TO)) {
			this.types.add(PathElementType.CLOSE);
		}
	}

	@Override
	public Rectangle2ifx toBoundingBoxWithCtrlPoints() {
		return controlPointBoundingBoxProperty().get().clone();
	}

	@Override
	public void toBoundingBoxWithCtrlPoints(Rectangle2ifx box) {
		assert box != null : AssertMessages.notNullParameter();
		box.set(controlPointBoundingBoxProperty().get());
	}

	@Override
	public int[] toIntArray(Transform2D transform) {
		final int n = (this.coords != null) ? this.coords.size() * 2 : 0;
		final int[] clone = new int[n];
		if (n > 0) {
		    final Iterator<Point2ifx> iterator = this.coords.iterator();
		    Point2ifx point = iterator.next();
		    for (int i = 0; i < n; i += 2) {
		        point = iterator.next();
		        if (!(transform == null || transform.isIdentity())) {
		            transform.transform(point);
		        }
		        clone[i] = point.ix();
		        clone[i + 1] = point.iy();
		    }
		}
		return clone;
	}

	@Override
	public float[] toFloatArray(Transform2D transform) {
		final int n = (this.coords != null) ? this.coords.size() * 2 : 0;
		final float[] clone = new float[n];
		if (n > 0) {
		    final Iterator<Point2ifx> iterator = this.coords.iterator();
            Point2ifx point = iterator.next();
            for (int i = 0; i < n; i += 2) {
                point = iterator.next();
                if (!(transform == null || transform.isIdentity())) {
                    transform.transform(point);
                }
                clone[i] = point.ix();
                clone[i + 1] = point.iy();
            }
		}
		return clone;
	}

	@Override
	public double[] toDoubleArray(Transform2D transform) {
		final int n = (this.coords != null) ? this.coords.size() * 2 : 0;
		final double[] clone = new double[n];
		if (n > 0) {
		    final Iterator<Point2ifx> iterator = this.coords.iterator();
            Point2ifx point = iterator.next();
            for (int i = 0; i < n; i += 2) {
                point = iterator.next();
                if (!(transform == null || transform.isIdentity())) {
                    transform.transform(point);
                }
                clone[i] = point.ix();
                clone[i + 1] = point.iy();
            }
		}
		return clone;
	}

	@Override
	public Point2ifx[] toPointArray(Transform2D transform) {
		final int n = (this.coords != null) ? this.coords.size() : 0;
		final Point2ifx[] clone = new Point2ifx[n];
		if (n > 0) {
		    final Iterator<Point2ifx> iterator = this.coords.iterator();
            Point2ifx point = iterator.next();
            for (int i = 0; i < n; ++i) {
                point = iterator.next();
                if (!(transform == null || transform.isIdentity())) {
                    transform.transform(point);
                }
                clone[i] = point;
            }
		}
		return clone;
	}

	@Override
	public Point2ifx getPointAt(int index) {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		return this.coords.get(index);
	}

	@Override
	@Pure
	public int getCurrentX() {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		final int index = this.coords.size() - 1;
		return this.coords.get(index).ix();
	}

	@Override
	@Pure
	public int getCurrentY() {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		final int index = this.coords.size() - 1;
		return this.coords.get(index).iy();
	}

	@Override
	@Pure
	public Point2ifx getCurrentPoint() {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		final int baseIdx = this.coords.size() - 1;
		return this.coords.get(baseIdx);
	}

	@Override
	public int size() {
		return (this.coords == null) ? 0 : this.coords.size();
	}

	@Override
	@SuppressWarnings("checkstyle:magicnumber")
	public void removeLast() {
		if (this.types != null && !this.types.isEmpty() && this.coords != null && !this.coords.isEmpty()) {
			final int lastIndex = this.types.size() - 1;
			final int coordSize = this.coords.size();
			final int coordIndex;
			switch (this.types.get(lastIndex)) {
			case CLOSE:
				// no coord to remove
				coordIndex = coordSize;
				break;
			case MOVE_TO:
				coordIndex = coordSize - 1;
				break;
			case LINE_TO:
				coordIndex = coordSize - 1;
				break;
			case CURVE_TO:
				coordIndex = coordSize - 3;
				break;
			case QUAD_TO:
				coordIndex = coordSize - 2;
				break;
			case ARC_TO:
			default:
				throw new IllegalStateException();
			}
			this.coords.remove(coordIndex, coordSize);
			this.types.remove(lastIndex);
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public void moveTo(int x, int y) {
	    if (this.types != null && !this.types.isEmpty()
	            && this.types.get(this.types.size() - 1) == PathElementType.MOVE_TO) {
	        assert this.coords != null && this.coords.size() >= 1;
	        final int idx = this.coords.size() - 1;
	        this.coords.set(idx, getGeomFactory().newPoint(x, y));
	    } else {
	        innerTypesProperty().add(PathElementType.MOVE_TO);
	        final ReadOnlyListWrapper<Point2ifx> coords = innerPointsProperty();
	        coords.add(getGeomFactory().newPoint(x, y));
	    }
	}

	@Override
	public void moveTo(Point2D<?, ?> position) {
		if (this.types != null && !this.types.isEmpty()
				&& this.types.get(this.types.size() - 1) == PathElementType.MOVE_TO) {
			assert this.coords != null && this.coords.size() >= 1;
			final int idx = this.coords.size() - 1;
			this.coords.set(idx, getGeomFactory().convertToPoint(position));
		} else {
			innerTypesProperty().add(PathElementType.MOVE_TO);
			final ReadOnlyListWrapper<Point2ifx> coords = innerPointsProperty();
			coords.add(getGeomFactory().convertToPoint(position));
		}
	}

	private void ensureMoveTo() {
		if (this.types == null || this.types.isEmpty()) {
			throw new IllegalStateException(Locale.getString("E1")); //$NON-NLS-1$
		}
	}

	@Override
	public void lineTo(int x, int y) {
		ensureMoveTo();
		innerTypesProperty().add(PathElementType.LINE_TO);
		final ReadOnlyListWrapper<Point2ifx> coords = innerPointsProperty();
		coords.add(getGeomFactory().newPoint(x, y));
	}

	@Override
	public void quadTo(int x1, int y1, int x2, int y2) {
		ensureMoveTo();
		innerTypesProperty().add(PathElementType.QUAD_TO);
		final ReadOnlyListWrapper<Point2ifx> coords = innerPointsProperty();
		coords.add(getGeomFactory().newPoint(x1, y1));
		coords.add(getGeomFactory().newPoint(x2, y2));
	}

	@Override
	public void curveTo(int x1, int y1, int x2, int y2, int x3, int y3) {
		ensureMoveTo();
		innerTypesProperty().add(PathElementType.CURVE_TO);
		final ReadOnlyListWrapper<Point2ifx> coords = innerPointsProperty();
		coords.add(getGeomFactory().newPoint(x1, y1));
		coords.add(getGeomFactory().newPoint(x2, y2));
		coords.add(getGeomFactory().newPoint(x3, y3));
	}

	/** Replies the private coordinates property.
	 *
	 * @return the private coordinates property.
	 */
	protected ReadOnlyListWrapper<Point2ifx> innerPointsProperty() {
		if (this.coords == null) {
			this.coords = new ReadOnlyListWrapper<>(this, MathFXAttributeNames.COORDINATES,
					FXCollections.observableList(new ArrayList<>()));
		}
		return this.coords;
	}

	/** Replies the coordinates property.
	 *
	 * @return the coordinates property.
	 */
	public ReadOnlyListProperty<Point2ifx> coordinatesProperty() {
		return innerPointsProperty().getReadOnlyProperty();
	}

	@Override
	public int getCoordAt(int index) {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		final Point2ifx point = this.coords.get(index / 2);
		return index % 2 == 0 ? point.ix() : point.iy();
	}

	@Override
	public void setLastPoint(int x, int y) {
	    if (this.coords != null && this.coords.size() >= 1) {
	        final int idx = this.coords.size() - 1;
	        final Point2ifx point = this.coords.get(idx);
	        point.setX(x);
	        point.setY(y);
	    } else {
	        throw new IllegalStateException();
	    }
	}

	@Override
	public void setLastPoint(Point2D<?, ?> point) {
		if (this.coords != null && this.coords.size() >= 1) {
			final int idx = this.coords.size() - 1;
			this.coords.get(idx).set(point.ix(), point.iy());
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:cyclomaticcomplexity"})
	public boolean remove(int x, int y) {
	    if (this.types != null && !this.types.isEmpty() && this.coords != null && !this.coords.isEmpty()) {
	        for (int i = 0, j = 0; i < this.coords.size() && j < this.types.size(); i++, j++) {
	            final Point2ifx point = this.coords.get(i);
	            switch (this.types.get(j)) {
	            case MOVE_TO:
	                //$FALL-THROUGH$
	            case LINE_TO:
	                if (x == point.ix() && y == point.iy()) {
	                    this.coords.remove(i);
	                    this.types.remove(j);
	                    return true;
	                }
	                break;
	            case CURVE_TO:
	                final Point2ifx p2 = this.coords.get(i + 1);
	                final Point2ifx p3 = this.coords.get(i + 2);
	                if ((x == point.ix() && y == point.iy())
	                        || (x == p2.ix() && y == p2.iy())
	                        || (x == p3.ix() && y == p3.iy())) {
	                    this.coords.remove(i, i + 3);
	                    this.types.remove(j);
	                    return true;
	                }
	                break;
	            case QUAD_TO:
	                final Point2ifx pt = this.coords.get(i + 1);
	                if ((x == point.ix() && y == point.iy())
	                        || (x == pt.ix() && y == pt.iy())) {
	                    this.coords.remove(i, i + 2);
	                    this.types.remove(j);
	                    return true;
	                }
	                break;
	            case CLOSE:
	                break;
	            case ARC_TO:
	                throw new IllegalStateException();
	            default:
	                break;
	            }
	        }
	    }
	    return false;
	}

	/** Remove the point from this path.
	 *
     * <p>If the given point do not match exactly a point in the path, nothing is removed.
     *
     * @param point the point to remove.
     * @return <code>true</code> if the point was removed; <code>false</code> otherwise.
	 */
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:cyclomaticcomplexity"})
	public boolean remove(Point2D<?, ?> point) {
		if (this.types != null && !this.types.isEmpty() && this.coords != null && !this.coords.isEmpty()) {
			for (int i = 0, j = 0; i < this.coords.size() && j < this.types.size(); i++, j++) {
			    final Point2ifx currentPoint = this.coords.get(i);
				switch (this.types.get(j)) {
				case MOVE_TO:
					//$FALL-THROUGH$
				case LINE_TO:
					if (point.equals(currentPoint)) {
						this.coords.remove(i);
						this.types.remove(j);
						return true;
					}
					break;
				case CURVE_TO:
				    final Point2ifx p2 = this.coords.get(i + 1);
				    final Point2ifx p3 = this.coords.get(i + 2);
					if ((point.equals(currentPoint))
							|| (point.equals(p2))
							|| (point.equals(p3))) {
						this.coords.remove(i, i + 3);
						this.types.remove(j);
						return true;
					}
					break;
				case QUAD_TO:
				    final Point2ifx pt = this.coords.get(i + 1);
					if ((point.equals(currentPoint))
							|| (point.equals(pt))) {
						this.coords.remove(i, i + 2);
						this.types.remove(j);
						return true;
					}
					break;
				case CLOSE:
					break;
				case ARC_TO:
					throw new IllegalStateException();
				default:
					break;
				}
			}
		}
		return false;
	}

	@Override
	public void set(Path2ifx path) {
		assert path != null : AssertMessages.notNullParameter();
		clear();
		add(path.getPathIterator());
	}

	/** Replies the private types property.
	 *
	 * @return the private types property.
	 */
	protected ReadOnlyListWrapper<PathElementType> innerTypesProperty() {
		if (this.types == null) {
			this.types = new ReadOnlyListWrapper<>(this, MathFXAttributeNames.TYPES,
					FXCollections.observableList(new ArrayList<>()));
		}
		return this.types;
	}

	/** Replies the types property.
	 *
	 * @return the types property.
	 */
	public ReadOnlyListProperty<PathElementType> typesProperty() {
		return innerTypesProperty().getReadOnlyProperty();
	}

	@Override
	public int getPathElementCount() {
		return this.types == null ? 0 : innerTypesProperty().size();
	}

	@Override
	public PathElementType getPathElementTypeAt(int index) {
		if (this.types == null) {
			throw new IndexOutOfBoundsException();
		}
		return this.types.get(index);
	}

}
