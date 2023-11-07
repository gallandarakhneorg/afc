/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d3.dfx;

import java.util.ArrayList;
import java.util.Iterator;

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

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.locale.Locale;

/** Path with 3 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Path3dfx extends AbstractShape3dfx<Path3dfx>
		implements Path3afp<Shape3dfx<?>, Path3dfx, PathElement3dfx, Point3dfx, Vector3dfx, AlignedBox3dfx> {

	private static final long serialVersionUID = 6051061640155091109L;

	/** Array of types.
	 */
	private ReadOnlyListWrapper<PathElementType> types;

	/** Array of coords.
	 */
	private ReadOnlyListWrapper<Point3dfx> coords;

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
	private ObjectProperty<AlignedBox3dfx> logicalBounds;

	/** Buffer for the squared length of the path.
	 */
	private DoubleProperty length;

	/** Construct an empty path.
	 */
	public Path3dfx() {
		this(DEFAULT_WINDING_RULE);
	}

	/** Construct a path by copying the given elements.
	 * @param iterator the iterator that provides the elements to copy.
	 */
	public Path3dfx(Iterator<PathElement3dfx> iterator) {
		this(DEFAULT_WINDING_RULE, iterator);
	}

	/** Construct a path with the given path winding rule.
	 * @param windingRule the path winding rule.
	 */
	public Path3dfx(PathWindingRule windingRule) {
		assert windingRule != null : AssertMessages.positiveOrZeroParameter();
		if (windingRule != DEFAULT_WINDING_RULE) {
			windingRuleProperty().set(windingRule);
		}
	}

	/** Construct a path by copying the given elements, and the given path winding rule.
	 * @param windingRule the path winding rule.
	 * @param iterator the iterator that provides the elements to copy.
	 */
	public Path3dfx(PathWindingRule windingRule, Iterator<PathElement3dfx> iterator) {
		assert windingRule != null : AssertMessages.positiveOrZeroParameter(0);
		assert iterator != null : AssertMessages.positiveOrZeroParameter(1);
		if (windingRule != DEFAULT_WINDING_RULE) {
			windingRuleProperty().set(windingRule);
		}
		add(iterator);
	}

	/** Constructor by copy.
	 * @param path the path to copy.
	 */
	public Path3dfx(Path3afp<?, ?, ?, ?, ?, ?> path) {
		set(path);
	}

	@Pure
	@Override
	public boolean containsControlPoint(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		for (final Point3dfx point : this.coords) {
			if (point.getX() == pt.getX() && point.getY() == pt.getY() && point.getZ() == pt.getZ()) {
				return true;
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
	public Path3dfx clone() {
		final Path3dfx clone = super.clone();
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
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + ((this.coords == null) ? 0 : this.coords.hashCode());
		bits = 31 * bits + ((this.types == null) ? 0 : this.types.hashCode());
		bits = 31 * bits + ((this.windingRule == null) ? 0 : this.windingRule.hashCode());
		return bits ^ (bits >> 31);
	}

	@Override
	public void translate(double dx, double dy, double dz) {
		for (final Point3dfx point : this.coords) {
			point.add(dx, dy, dz);
		}
	}

	@Override
	public void transform(Transform3D transform) {
		assert transform != null : AssertMessages.positiveOrZeroParameter();
		for (final Point3dfx point : this.coords) {
			transform.transform(point);
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
				final PathIterator3afp<PathElement3dfx> pi = getPathIterator();
				PathElement3dfx pe;
				while (pi.hasNext()) {
					pe = pi.next();
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
	public AlignedBox3dfx toBoundingBox() {
		return boundingBoxProperty().get().clone();
	}

	@Override
	public void toBoundingBox(AlignedBox3dfx box) {
		assert box != null : AssertMessages.positiveOrZeroParameter();
		box.set(boundingBoxProperty().get());
	}

	/** Replies the windingRule property.
	 *
	 * @return the windingRule property.
	 */
	public ObjectProperty<PathWindingRule> windingRuleProperty() {
		if (this.windingRule == null) {
			this.windingRule = new SimpleObjectProperty<>(this,
					MathFXAttributeNames.WINDING_RULE, DEFAULT_WINDING_RULE);
		}
		return this.windingRule;
	}

	@Override
	public PathWindingRule getWindingRule() {
		return (this.windingRule == null) ? DEFAULT_WINDING_RULE : windingRuleProperty().get();
	}

	@Override
	public void setWindingRule(PathWindingRule rule) {
		assert rule != null : AssertMessages.positiveOrZeroParameter();
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
			this.isCurved = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_CURVED, false);
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
			this.isMultiparts = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_MULTIPARTS, false);
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
	public AlignedBox3dfx toBoundingBoxWithCtrlPoints() {
		AlignedBox3dfx bb = null;
		if (this.logicalBounds != null) {
			bb = controlPointBoundingBoxProperty().get();
		}
		if (bb == null) {
			return getGeomFactory().newBox();
		}
		return bb;
	}

	@Override
	public void toBoundingBoxWithCtrlPoints(AlignedBox3dfx box) {
		assert box != null : AssertMessages.positiveOrZeroParameter();
		if (this.logicalBounds != null) {
			box.set(controlPointBoundingBoxProperty().get());
		} else {
			box.clear();
		}
	}

	@Override
	public int[] toIntArray(Transform3D transform) {
		final int n = (this.coords != null) ? this.coords.size() : 0;
		final int[] clone = new int[n];
		if (n > 0) {
			final Iterator<Point3dfx> iterator = this.coords.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				final Point3dfx point = iterator.next();
				if (!(transform == null || transform.isIdentity())) {
					transform.transform(point);
				}
				clone[i++] = point.ix();
				clone[i++] = point.iy();
				clone[i++] = point.iz();
			}
		}
		return clone;
	}

	@Override
	public float[] toFloatArray(Transform3D transform) {
		final int n = (this.coords != null) ? this.coords.size() : 0;
		final float[] clone = new float[n];
		if (n > 0) {
			final Iterator<Point3dfx> iterator = this.coords.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				final Point3dfx point = iterator.next();
				if (!(transform == null || transform.isIdentity())) {
					transform.transform(point);
				}
				clone[i++] = (float) point.getX();
				clone[i++] = (float) point.getY();
				clone[i++] = (float) point.getZ();
			}
		}
		return clone;
	}

	@Override
	public double[] toDoubleArray(Transform3D transform) {
		final int n = (this.coords != null) ? this.coords.size() : 0;
		final double[] clone = new double[n];
		if (n > 0) {
			final Iterator<Point3dfx> iterator = this.coords.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				final Point3dfx point = iterator.next();
				if (!(transform == null || transform.isIdentity())) {
					transform.transform(point);
				}
				clone[i++] = point.getX();
				clone[i++] = point.getY();
				clone[i++] = point.getZ();
			}
		}
		return clone;
	}

	@Override
	public Point3dfx[] toPointArray(Transform3D transform) {
		final int n = (this.coords != null) ? this.coords.size() / 2 : 0;
		final Point3dfx[] clone = new Point3dfx[n];
		if (n > 0) {
			final Iterator<Point3dfx> iterator = this.coords.iterator();
			Point3dfx point = iterator.next();
			for (int i = 0; i < n; ++i) {
				if (!(transform == null || transform.isIdentity())) {
					transform.transform(point);
				}
				clone[i] = point;
				point = iterator.next();
			}
		}
		return clone;
	}

	@Override
	public Point3dfx getPointAt(int index) {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		return this.coords.get(index);
	}

	@Pure
	@Override
	public double getCurrentX() {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		final int baseIdx = this.coords.size() - 1;
		return this.coords.get(baseIdx).getX();
	}

	@Pure
	@Override
	public double getCurrentY() {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		final int baseIdx = this.coords.size() - 1;
		return this.coords.get(baseIdx).getY();
	}

	@Pure
	@Override
	public double getCurrentZ() {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		final int baseIdx = this.coords.size() - 1;
		return this.coords.get(baseIdx).getZ();
	}

	@Pure
	@Override
	public Point3dfx getCurrentPoint() {
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
			int coordIndex = coordSize;
			switch (this.types.get(lastIndex)) {
			case CLOSE:
				// no coord to remove
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
				//$CASES-OMITTED$
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
	public void moveTo(double x, double y, double z) {
		if (this.types != null && !this.types.isEmpty()
				&& this.types.get(this.types.size() - 1) == PathElementType.MOVE_TO) {
			assert this.coords != null && !this.coords.isEmpty();
			final int idx = this.coords.size() - 1;
			this.coords.set(idx, getGeomFactory().newPoint(x, y, z));
		} else {
			innerTypesProperty().add(PathElementType.MOVE_TO);
			final ReadOnlyListWrapper<Point3dfx> coords = innerCoordinatesProperty();
			coords.add(getGeomFactory().newPoint(x, y, z));
		}
	}

	@Override
	public void moveTo(Point3D<?, ?> position) {
		assert position != null : AssertMessages.notNullParameter();
		if (this.types != null && !this.types.isEmpty()
				&& this.types.get(this.types.size() - 1) == PathElementType.MOVE_TO) {
			assert this.coords != null && !this.coords.isEmpty();
			final int idx = this.coords.size() - 1;
			this.coords.set(idx, getGeomFactory().convertToPoint(position));
		} else {
			innerTypesProperty().add(PathElementType.MOVE_TO);
			final ReadOnlyListWrapper<Point3dfx> coords = innerCoordinatesProperty();
			coords.add(getGeomFactory().convertToPoint(position));
		}
	}

	private void ensureMoveTo() {
		if (this.types == null || this.types.isEmpty()) {
			throw new IllegalStateException(Locale.getString(Path3afp.class, "E1")); //$NON-NLS-1$
		}
	}

	@Override
	public void lineTo(double x, double y, double z) {
		ensureMoveTo();
		innerTypesProperty().add(PathElementType.LINE_TO);
		final ReadOnlyListWrapper<Point3dfx> coords = innerCoordinatesProperty();
		coords.add(getGeomFactory().newPoint(x, y, z));
	}

	@Override
	public void lineTo(Point3D<?, ?> to) {
		assert to != null : AssertMessages.notNullParameter();
		ensureMoveTo();
		innerTypesProperty().add(PathElementType.LINE_TO);
		final ReadOnlyListWrapper<Point3dfx> coords = innerCoordinatesProperty();
		coords.add(getGeomFactory().convertToPoint(to));
	}

	@Override
	public void quadTo(double x1, double y1, double z1, double x2, double y2, double z2) {
		ensureMoveTo();
		innerTypesProperty().add(PathElementType.QUAD_TO);
		final ReadOnlyListWrapper<Point3dfx> coords = innerCoordinatesProperty();
		coords.add(getGeomFactory().newPoint(x1, y1, z1));
		coords.add(getGeomFactory().newPoint(x2, y2, z2));
	}

	@Override
	public void quadTo(Point3D<?, ?> ctrl, Point3D<?, ?> to) {
		assert ctrl != null : AssertMessages.notNullParameter(0);
		assert to != null : AssertMessages.notNullParameter(1);
		ensureMoveTo();
		innerTypesProperty().add(PathElementType.QUAD_TO);
		final ReadOnlyListWrapper<Point3dfx> coords = innerCoordinatesProperty();
		coords.add(getGeomFactory().convertToPoint(ctrl));
		coords.add(getGeomFactory().convertToPoint(to));
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
	public void curveTo(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
		ensureMoveTo();
		innerTypesProperty().add(PathElementType.CURVE_TO);
		final ReadOnlyListWrapper<Point3dfx> coords = innerCoordinatesProperty();
		coords.add(getGeomFactory().newPoint(x1, y1, z1));
		coords.add(getGeomFactory().newPoint(x2, y2, z2));
		coords.add(getGeomFactory().newPoint(x3, y3, z3));
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
	public void curveTo(Point3D<?, ?> ctrl1, Point3D<?, ?> ctrl2, Point3D<?, ?> to) {
		assert ctrl1 != null : AssertMessages.notNullParameter(0);
		assert ctrl2 != null : AssertMessages.notNullParameter(1);
		assert to != null : AssertMessages.notNullParameter(2);
		ensureMoveTo();
		innerTypesProperty().add(PathElementType.CURVE_TO);
		final ReadOnlyListWrapper<Point3dfx> coords = innerCoordinatesProperty();
		coords.add(getGeomFactory().convertToPoint(ctrl1));
		coords.add(getGeomFactory().convertToPoint(ctrl2));
		coords.add(getGeomFactory().convertToPoint(to));
	}

	/** Replies the private coordinates property.
	 *
	 * @return the private coordinates property.
	 */
	protected ReadOnlyListWrapper<Point3dfx> innerCoordinatesProperty() {
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
	public ReadOnlyListProperty<Point3dfx> coordinatesProperty() {
		return innerCoordinatesProperty().getReadOnlyProperty();
	}

	@Override
	public double getCoordAt(int index) {
		if (this.coords == null) {
			throw new IndexOutOfBoundsException();
		}
		final Point3dfx point = this.coords.get(index / 3);
		return index % 3 == 0 ? point.getX() : index % 3 == 1 ? point.getY() : point.getZ();
	}

	@Override
	public void setLastPoint(double x, double y, double z) {
		if (this.coords != null && this.coords.size() >= 3) {
			final int idx = this.coords.size() - 1;
			final Point3dfx point = this.coords.get(idx);
			point.setX(x);
			point.setY(y);
			point.setZ(z);
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:booleanexpressioncomplexity", "checkstyle:magicnumber"})
	public boolean remove(double x, double y, double z) {
		if (this.types != null && !this.types.isEmpty() && this.coords != null && !this.coords.isEmpty()) {
			for (int i = 0, j = 0; i < this.coords.size() && j < this.types.size(); ++j) {
				final Point3dfx point = this.coords.get(i);
				switch (this.types.get(j)) {
				case MOVE_TO:
					//$FALL-THROUGH$
				case LINE_TO:
					if (x == point.getX() && y == point.getY() && z == point.getZ()) {
						this.coords.remove(i);
						this.types.remove(j);
						return true;
					}
					i++;
					break;
				case CURVE_TO:
					final Point3dfx p2 = this.coords.get(i + 1);
					final Point3dfx p3 = this.coords.get(i + 2);
					if ((x == point.getX() && y == point.getY() && z == point.getZ())
							|| (x == p2.getX() && y == p2.getY() && z == p2.getZ())
							|| (x == p3.getX() && y == p3.getY() && z == p3.getZ())) {
						this.coords.remove(i, i + 3);
						this.types.remove(j);
						return true;
					}
					i += 3;
					break;
				case QUAD_TO:
					final Point3dfx pt = this.coords.get(i + 1);
					if ((x == point.getX() && y == point.getY() && z == point.getZ())
							|| (x == pt.getX() && y == pt.getY() && z == pt.getZ())) {
						this.coords.remove(i, i + 2);
						this.types.remove(j);
						return true;
					}
					i += 2;
					break;
					//$CASES-OMITTED$
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
	 * @return {@code true} if the point was removed; {@code false} otherwise.
	 * @throws IllegalStateException if "arc-to" is found.
	 */
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:cyclomaticcomplexity"})
	public boolean remove(Point3D<?, ?> point) {
		if (this.types != null && !this.types.isEmpty() && this.coords != null && !this.coords.isEmpty()) {
			for (int i = 0, j = 0; i < this.coords.size() && j < this.types.size(); j++) {
				final Point3dfx currentPoint = this.coords.get(i);
				switch (this.types.get(j)) {
				case MOVE_TO:
					//$FALL-THROUGH$
				case LINE_TO:
					if (point.equals(currentPoint)) {
						this.coords.remove(i);
						this.types.remove(j);
						return true;
					}
					i++;
					break;
				case CURVE_TO:
					final Point3dfx p2 = this.coords.get(i + 1);
					final Point3dfx p3 = this.coords.get(i + 2);
					if ((point.equals(currentPoint))
							|| (point.equals(p2))
							|| (point.equals(p3))) {
						this.coords.remove(i, i + 3);
						this.types.remove(j);
						return true;
					}
					i += 3;
					break;
				case QUAD_TO:
					final Point3dfx pt = this.coords.get(i + 1);
					if ((point.equals(currentPoint))
							|| (point.equals(pt))) {
						this.coords.remove(i, i + 2);
						this.types.remove(j);
						return true;
					}
					i += 2;
					break;
				case ARC_TO:
					throw new IllegalStateException();
					//$CASES-OMITTED$
				default:
					break;
				}
			}
		}
		return false;
	}

	@Override
	public void set(Path3dfx path) {
		assert path != null : AssertMessages.positiveOrZeroParameter();
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
			this.length.bind(Bindings.createDoubleBinding(() ->
				Path3afp.computeLength(getPathIterator()),
				innerTypesProperty(), innerCoordinatesProperty()));
		}
		return this.length;
	}

	@Override
	public ObjectProperty<AlignedBox3dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new ReadOnlyObjectWrapper<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() -> {
				final AlignedBox3dfx bb = getGeomFactory().newBox();
				Path3afp.computeDrawableElementBoundingBox(
						getPathIterator(getGeomFactory().getSplineApproximationRatio()),
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
	public ObjectProperty<AlignedBox3dfx> controlPointBoundingBoxProperty() {
		if (this.logicalBounds == null) {
			this.logicalBounds = new ReadOnlyObjectWrapper<>(this, MathFXAttributeNames.CONTROL_POINT_BOUNDING_BOX);
			this.logicalBounds.bind(Bindings.createObjectBinding(() -> {
				final AlignedBox3dfx bb = getGeomFactory().newBox();
				Path3afp.computeControlPointBoundingBox(
						getPathIterator(),
						bb);
				return bb;
			},
					innerCoordinatesProperty()));
		}
		return this.logicalBounds;
	}

}
