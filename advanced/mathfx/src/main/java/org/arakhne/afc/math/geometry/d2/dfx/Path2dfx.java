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

package org.arakhne.afc.math.geometry.d2.dfx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Objects;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.afp.InnerComputationPoint2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;

/** Path with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public class Path2dfx extends AbstractShape2dfx<Path2dfx>
		implements Path2afp<Shape2dfx<?>, Path2dfx, PathElement2dfx, Point2dfx, Vector2dfx, Rectangle2dfx> {
	private static final long serialVersionUID = 6051061640155091109L;

	private static final String PATH_WINDING_RULE = "Path winding rule must be not null"; //$NON-NLS-1$

	/** Array of types.
	 */
	private ReadOnlyListWrapper<PathElementType> types;

	/** Array of coords.
	 */
	private ReadOnlyListWrapper<Double> coords;

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
	private BooleanProperty isMultiparts;

	/** Buffer for the bounds of the path that corresponds
	 * to all the points added in the path.
	 */
	private ObjectProperty<Rectangle2dfx> logicalBounds;

	/** Buffer for the squared length of the path.
	 */
	private DoubleProperty length;

	/** Construct an empty path.
	 */
	public Path2dfx() {
		this(DEFAULT_WINDING_RULE);
	}

	/** Construct a path by copying the given elements.
	 * @param iterator the iterator that provides the elements to copy.
	 */
	public Path2dfx(Iterator<PathElement2dfx> iterator) {
		this(DEFAULT_WINDING_RULE, iterator);
	}

	/** Construct a path with the given path winding rule.
	 * @param windingRule the path winding rule.
	 */
	public Path2dfx(PathWindingRule windingRule) {
		assert windingRule != null : PATH_WINDING_RULE;
		if (windingRule != DEFAULT_WINDING_RULE) {
			windingRuleProperty().set(windingRule);
		}
	}

	/** Construct a path by copying the given elements, and the given path winding rule.
	 * @param windingRule the path winding rule.
	 * @param iterator the iterator that provides the elements to copy.
	 */
	public Path2dfx(PathWindingRule windingRule, Iterator<PathElement2dfx> iterator) {
		assert windingRule != null : PATH_WINDING_RULE;
		assert iterator != null : "Iterator must be not null"; //$NON-NLS-1$
		if (windingRule != DEFAULT_WINDING_RULE) {
			windingRuleProperty().set(windingRule);
		}
		add(iterator);
	}

	/** Constructor by copy.
	 * @param path the path to copy.
	 */
	public Path2dfx(Path2afp<?, ?, ?, ?, ?, ?> path) {
		set(path);
	}

	@Pure
	@Override
	public boolean containsControlPoint(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; //$NON-NLS-1$
		if (this.coords != null && !this.coords.isEmpty()) {
			for (int i = 0; i < this.coords.size(); i += 2) {
				final double x = this.coords.get(i);
				final double y = this.coords.get(i + 1);
				if (x == pt.getX() && y == pt.getY()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void clear() {
		if (this.coords != null) {
			this.coords.clear();
		}
		if (this.types != null) {
			this.types.clear();
		}
	}

	@Pure
	@Override
	public Path2dfx clone() {
		final Path2dfx clone = super.clone();
		clone.coords = null;
		if (this.coords != null && !this.coords.isEmpty()) {
			clone.innerCoordinatesProperty().addAll(this.coords);
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
		clone.isMultiparts = null;
		clone.isPolyline = null;
		clone.isPolygon = null;
		clone.isEmpty = null;
		clone.length = null;

		return clone;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + Objects.hashCode(this.coords);
		bits = 31L * bits + Objects.hashCode(this.types);
		bits = 31L * bits + Objects.hashCode(this.windingRule);
		return (int) (bits ^ (bits >> 31));
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		if (this.coords != null && !this.coords.isEmpty()) {
			final Iterator<Double> iterator = this.coords.iterator();
			b.append(iterator.next());
			while (iterator.hasNext()) {
				b.append(", "); //$NON-NLS-1$
				b.append(iterator.next());
			}
		}
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	@Override
	public void translate(double dx, double dy) {
		if (this.coords != null && !this.coords.isEmpty()) {
			final ListIterator<Double> li = this.coords.listIterator();
	        while (li.hasNext()) {
	            li.set(li.next() + dx);
	            li.set(li.next() + dy);
	        }
		}
	}

	@Override
	public void transform(Transform2D transform) {
		assert transform != null : "Transformation must be not null"; //$NON-NLS-1$
		final Point2D<?, ?> p = new InnerComputationPoint2afp();
		if (this.coords != null && !this.coords.isEmpty()) {
			final ListIterator<Double> li = this.coords.listIterator();
			int i = 0;
	        while (li.hasNext()) {
	        	p.set(li.next(), li.next());
				transform.transform(p);
				this.coords.set(i++, p.getX());
				this.coords.set(i++, p.getY());
	        }
		}
	}

	/** Replies the isEmpty property.
	 *
	 * @return the isEmpty property.
	 */
	public BooleanProperty isEmptyProperty() {
		if (this.isEmpty == null) {
			this.isEmpty = new SimpleBooleanProperty(this, "isEmpty"); //$NON-NLS-1$
			this.isEmpty.bind(Bindings.createBooleanBinding(() -> {
				final PathIterator2afp<PathElement2dfx> pi = getPathIterator();
				while (pi.hasNext()) {
					final PathElement2dfx pe = pi.next();
					if (pe.isDrawable()) {
						return false;
					}
				}
				return true;
			},
					innerTypesProperty(), innerCoordinatesProperty()));
		}
		return this.isEmpty;
	}

	@Override
	public boolean isEmpty() {
		return isEmptyProperty().get();
	}

	@Override
	public Rectangle2dfx toBoundingBox() {
		return boundingBoxProperty().get().clone();
	}

	@Override
	public void toBoundingBox(Rectangle2dfx box) {
		assert box != null : "Rectangle must be not null"; //$NON-NLS-1$
		box.set(boundingBoxProperty().get());
	}

	/** Replies the windingRule property.
	 *
	 * @return the windingRule property.
	 */
	public ObjectProperty<PathWindingRule> windingRuleProperty() {
		if (this.windingRule == null) {
			this.windingRule = new SimpleObjectProperty<>(this, "windingRule", DEFAULT_WINDING_RULE); //$NON-NLS-1$
		}
		return this.windingRule;
	}

	@Override
	public PathWindingRule getWindingRule() {
		return (this.windingRule == null) ? DEFAULT_WINDING_RULE : windingRuleProperty().get();
	}

	@Override
	public void setWindingRule(PathWindingRule rule) {
		assert rule != null : PATH_WINDING_RULE;
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
			this.isPolyline = new ReadOnlyBooleanWrapper(this, "isPolyline", false); //$NON-NLS-1$
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
			},
					innerTypesProperty()));
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
			this.isCurved = new ReadOnlyBooleanWrapper(this, "isCurved", false); //$NON-NLS-1$
			this.isCurved.bind(Bindings.createBooleanBinding(() -> {
				for (final PathElementType type : innerTypesProperty()) {
					if (type == PathElementType.CURVE_TO || type == PathElementType.QUAD_TO) {
						return true;
					}
				}
				return false;
			},
					innerTypesProperty()));
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
		if (this.isMultiparts == null) {
			this.isMultiparts = new ReadOnlyBooleanWrapper(this, "isMultiParts", false); //$NON-NLS-1$
			this.isMultiparts.bind(Bindings.createBooleanBinding(() -> {
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
			},
					innerTypesProperty()));
		}
		return this.isMultiparts;
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
			this.isPolygon = new ReadOnlyBooleanWrapper(this, "isPolygon", false); //$NON-NLS-1$
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
			},
					innerTypesProperty()));
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
	public Rectangle2dfx toBoundingBoxWithCtrlPoints() {
		Rectangle2dfx bb = null;
		if (this.logicalBounds != null) {
			bb = controlPointBoundingBoxProperty().get();
		}
		if (bb == null) {
			return getGeomFactory().newBox();
		}
		return bb;
	}

	@Override
	public void toBoundingBoxWithCtrlPoints(Rectangle2dfx box) {
		assert box != null : "Rectangle must be not null"; //$NON-NLS-1$
		if (this.logicalBounds != null) {
			box.set(controlPointBoundingBoxProperty().get());
		} else {
			box.clear();
		}
	}

	@Override
	public int[] toIntArray(Transform2D transform) {
		final int n = (this.coords != null) ? this.coords.size() : 0;
		final int[] clone = new int[n];
		if (n > 0) {
			if (transform == null || transform.isIdentity()) {
				final Iterator<Double> iterator = this.coords.iterator();
				for (int i = 0; i < n; ++i) {
					clone[i] = iterator.next().intValue();
				}
			} else {
				final Point2D<?, ?> p = new InnerComputationPoint2afp();
				final Iterator<Double> iterator = this.coords.iterator();
				for (int i = 0; i < n; i += 2) {
					p.set(iterator.next(), iterator.next());
					transform.transform(p);
					clone[i] = p.ix();
					clone[i + 1] = p.iy();
				}
			}
		}
		return clone;
	}

	@Override
	public float[] toFloatArray(Transform2D transform) {
		final int n = (this.coords != null) ? this.coords.size() : 0;
		final float[] clone = new float[n];
		if (n > 0) {
			if (transform == null || transform.isIdentity()) {
				final Iterator<Double> iterator = this.coords.iterator();
				for (int i = 0; i < n; ++i) {
					clone[i] = iterator.next().floatValue();
				}
			} else {
				final Point2D<?, ?> p = new InnerComputationPoint2afp();
				final Iterator<Double> iterator = this.coords.iterator();
				for (int i = 0; i < n; i += 2) {
					p.set(iterator.next(), iterator.next());
					transform.transform(p);
					clone[i] = (float) p.getX();
					clone[i + 1] = (float) p.getY();
				}
			}
		}
		return clone;
	}

	@Override
	public double[] toDoubleArray(Transform2D transform) {
		final int n = (this.coords != null) ? this.coords.size() : 0;
		final double[] clone = new double[n];
		if (n > 0) {
			if (transform == null || transform.isIdentity()) {
				final Iterator<Double> iterator = this.coords.iterator();
				for (int i = 0; i < n; ++i) {
					clone[i] = iterator.next().doubleValue();
				}
			} else {
				final Point2D<?, ?> p = new InnerComputationPoint2afp();
				final Iterator<Double> iterator = this.coords.iterator();
				for (int i = 0; i < n; i += 2) {
					p.set(iterator.next(), iterator.next());
					transform.transform(p);
					clone[i] = p.getX();
					clone[i + 1] = p.getY();
				}
			}
		}
		return clone;
	}

	@Override
	public Point2dfx[] toPointArray(Transform2D transform) {
		final int n = (this.coords != null) ? this.coords.size() / 2 : 0;
		final Point2dfx[] clone = new Point2dfx[n];
		if (n > 0) {
			if (transform == null || transform.isIdentity()) {
				final Iterator<Double> iterator = this.coords.iterator();
				for (int i = 0; i < n; ++i) {
					clone[i] = getGeomFactory().newPoint(
							iterator.next().doubleValue(),
							iterator.next().doubleValue());
				}
			} else {
				final Iterator<Double> iterator = this.coords.iterator();
				for (int i = 0; i < n; ++i) {
					final Point2dfx p = getGeomFactory().newPoint(iterator.next(), iterator.next());
					transform.transform(p);
					clone[i] = p;
				}
			}
		}
		return clone;
	}

	@Override
	public Point2dfx getPointAt(int index) {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		final int baseIdx = index * 2;
		return getGeomFactory().newPoint(
				this.coords.get(baseIdx),
				this.coords.get(baseIdx + 1));
	}

	@Pure
	@Override
	public double getCurrentX() {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		final int baseIdx = this.coords.size() - 1;
		return this.coords.get(baseIdx - 1);
	}

	@Pure
	@Override
	public double getCurrentY() {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		final int baseIdx = this.coords.size() - 1;
		return this.coords.get(baseIdx);
	}

	@Pure
	@Override
	public Point2dfx getCurrentPoint() {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		final int baseIdx = this.coords.size() - 1;
		return getGeomFactory().newPoint(
				this.coords.get(baseIdx - 1),
				this.coords.get(baseIdx));
	}

	@Override
	public int size() {
		return (this.coords == null) ? 0 : this.coords.size() / 2;
	}

	@Override
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
				coordIndex = coordSize - 2;
				break;
			case LINE_TO:
				coordIndex = coordSize - 2;
				break;
			case CURVE_TO:
				coordIndex = coordSize - 6;
				break;
			case QUAD_TO:
				coordIndex = coordSize - 4;
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
	public void moveTo(double x, double y) {
		if (this.types != null && !this.types.isEmpty()
				&& this.types.get(this.types.size() - 1) == PathElementType.MOVE_TO) {
			assert this.coords != null && this.coords.size() >= 2;
			final int idx = this.coords.size() - 1;
			this.coords.set(idx - 1, x);
			this.coords.set(idx, y);
		} else {
			innerTypesProperty().add(PathElementType.MOVE_TO);
			final ReadOnlyListWrapper<Double> coords = innerCoordinatesProperty();
			coords.add(x);
			coords.add(y);
		}
	}

	private void ensureMoveTo() {
		if (this.types == null || this.types.isEmpty()) {
			throw new IllegalStateException("missing initial moveto in path definition"); //$NON-NLS-1$
		}
	}

	@Override
	public void lineTo(double x, double y) {
		ensureMoveTo();
		innerTypesProperty().add(PathElementType.LINE_TO);
		final ReadOnlyListWrapper<Double> coords = innerCoordinatesProperty();
		coords.add(x);
		coords.add(y);
	}

	@Override
	public void quadTo(double x1, double y1, double x2, double y2) {
		ensureMoveTo();
		innerTypesProperty().add(PathElementType.QUAD_TO);
		final ReadOnlyListWrapper<Double> coords = innerCoordinatesProperty();
		coords.add(x1);
		coords.add(y1);
		coords.add(x2);
		coords.add(y2);
	}

	@Override
	public void curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
		ensureMoveTo();
		innerTypesProperty().add(PathElementType.CURVE_TO);
		final ReadOnlyListWrapper<Double> coords = innerCoordinatesProperty();
		coords.add(x1);
		coords.add(y1);
		coords.add(x2);
		coords.add(y2);
		coords.add(x3);
		coords.add(y3);
	}

	/** Replies the private coordinates property.
	 *
	 * @return the private coordinates property.
	 */
	protected ReadOnlyListWrapper<Double> innerCoordinatesProperty() {
		if (this.coords == null) {
			this.coords = new ReadOnlyListWrapper<>(this, "coordinates", //$NON-NLS-1$
					FXCollections.observableList(new ArrayList<>()));
		}
		return this.coords;
	}

	/** Replies the coordinates property.
	 *
	 * @return the coordinates property.
	 */
	public ReadOnlyListProperty<Double> coordinatesProperty() {
		return innerCoordinatesProperty().getReadOnlyProperty();
	}

	@Override
	public double getCoordAt(int index) {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		return this.coords.get(index);
	}

	@Override
	public void setLastPoint(double x, double y) {
		if (this.coords != null && this.coords.size() >= 2) {
			final int idx = this.coords.size() - 1;
			this.coords.set(idx - 1, x);
			this.coords.set(idx, y);
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	public boolean remove(double x, double y) {
		if (this.types != null && !this.types.isEmpty() && this.coords != null && !this.coords.isEmpty()) {
			for (int i = 0, j = 0; i < this.coords.size() && j < this.types.size();) {
				switch (this.types.get(j)) {
				case MOVE_TO:
					//$FALL-THROUGH$
				case LINE_TO:
					if (x == this.coords.get(i) && y == this.coords.get(i + 1)) {
						this.coords.remove(i, i + 2);
						this.types.remove(j);
						return true;
					}
					i += 2;
					++j;
					break;
				case CURVE_TO:
					if ((x == this.coords.get(i) && y == this.coords.get(i + 1))
							|| (x == this.coords.get(i + 2) && y == this.coords.get(i + 3))
							|| (x == this.coords.get(i + 4) && y == this.coords.get(i + 5))) {
						this.coords.remove(i, i + 6);
						this.types.remove(j);
						return true;
					}
					i += 6;
					++j;
					break;
				case QUAD_TO:
					if ((x == this.coords.get(i) && y == this.coords.get(i + 1))
							|| (x == this.coords.get(i + 2) && y == this.coords.get(i + 3))) {
						this.coords.remove(i, i + 4);
						this.types.remove(j);
						return true;
					}
					i += 4;
					++j;
					break;
				case CLOSE:
					++j;
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
	public void set(Path2dfx path) {
		assert path != null : "Shape must be not null"; //$NON-NLS-1$
		clear();
		add(path.getPathIterator());
	}

	/** Replies the private types property.
	 *
	 * @return the private types property.
	 */
	protected ReadOnlyListWrapper<PathElementType> innerTypesProperty() {
		if (this.types == null) {
			this.types = new ReadOnlyListWrapper<>(this, "types", //$NON-NLS-1$
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

	@Override
	public double getLength() {
		return lengthProperty().get();
	}

	/** Replies the path length property.
	 *
	 * @return the length property.
	 */
	public DoubleProperty lengthProperty() {
		if (this.length == null) {
			this.length = new ReadOnlyDoubleWrapper();
			this.length.bind(Bindings.createDoubleBinding(() -> {
				return Path2afp.computeLength(getPathIterator());
			},
					innerTypesProperty(), innerCoordinatesProperty()));
		}
		return this.length;
	}

	@Override
	public ObjectProperty<Rectangle2dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new ReadOnlyObjectWrapper<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(() -> {
				final Rectangle2dfx bb = getGeomFactory().newBox();
				Path2afp.computeDrawableElementBoundingBox(
						getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						bb);
				return bb;
			},
					innerCoordinatesProperty()));
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
	public ObjectProperty<Rectangle2dfx> controlPointBoundingBoxProperty() {
		if (this.logicalBounds == null) {
			this.logicalBounds = new ReadOnlyObjectWrapper<>(this, "controlPointBoundingBox"); //$NON-NLS-1$
			this.logicalBounds.bind(Bindings.createObjectBinding(() -> {
				final Rectangle2dfx bb = getGeomFactory().newBox();
				Path2afp.computeControlPointBoundingBox(
						getPathIterator(),
						bb);
				return bb;
			},
					innerCoordinatesProperty()));
		}
		return this.logicalBounds;
	}

}
