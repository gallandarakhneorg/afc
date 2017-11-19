/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.nodefx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.afp.BoundedElement2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.util.InformedIterable;

/** View of document elements that provides a basic UI to show them.
 *
 * <p>The document elements are displayed within a {@link ZoomableCanvas} by the {@link Drawer drawers}
 * that are declared as services.
 *
 * <p>The {@code ZoomableViewer} provides advanced UI components (scroll bars, etc.)
 * and interaction means (mouse support, etc.) for scrolling and zooming in and out.
 *
 * @param <T> the type of the document elements.
 * @param <DT> the type of the document.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public interface ZoomableViewer<T, DT extends InformedIterable<? super T> & BoundedElement2afp<?>> {

	/** Name of the property that contains the zooming/scale value.
	 * A value of 1 means that there is no scaling. A value greater than
	 * 1 means that the document is zooming in. A value below 1 means that
	 * the document is zoomed out.
	 */
	String SCALE_VALUE_PROPERTY = "scaleValue"; //$NON-NLS-1$

	/** Name of the property for the minimal scale value.
	 */
	String MIN_SCALE_VALUE_PROPERTY = "minScaleValue"; //$NON-NLS-1$

	/** Name of the property for the maximal scale value.
	 */
	String MAX_SCALE_VALUE_PROPERTY = "maxScaleValue"; //$NON-NLS-1$

	/** Name of the property for the bounds of the document in document coordinates.
	 */
	String DOCUMENT_BOUNDS_PROPERTY = "documentBounds"; //$NON-NLS-1$

	/** Name of the property for the bounds of the viewport in document coordinates.
	 */
	String VIEWPORT_BOUNDS_PROPERTY = "viewportBounds"; //$NON-NLS-1$

	/** Name of the property for the x coordinate of the viewport center in document coordinates.
	 */
	String VIEWPORT_CENTER_X_PROPERTY = "viewportCenterX"; //$NON-NLS-1$

	/** Name of the property for the y coordinate of the viewport center in document coordinates.
	 */
	String VIEWPORT_CENTER_Y_PROPERTY = "viewportCenterY"; //$NON-NLS-1$

	/** Name of the property that indicates if the X axis is inverted.
	 */
	String INVERTED_AXIS_X_PROPERTY = "invertedAxisX"; //$NON-NLS-1$

	/** Name of the property that indicates if the Y axis is inverted.
	 */
	String INVERTED_AXIS_Y_PROPERTY = "invertedAxisY"; //$NON-NLS-1$

	/** Name of the property for the document model.
	 */
	String DOCUMENT_MODEL_PROPERTY = "documentModel"; //$NON-NLS-1$

	/** Name of the property for the document drawer.
	 */
	String DOCUMENT_DRAWER_PROPERTY = "documentDrawer"; //$NON-NLS-1$

	/** Name of the property for the factor to apply when zooming in or out.
	 */
	String SCALE_CHANGE_PROPERTY = "scaleChange"; //$NON-NLS-1$

	/** Name of the property for the drawable element budget.
	 */
	String DRAWABLE_ELEMENT_BUDGET_PROPERTY = "drawableElementBudget"; //$NON-NLS-1$

	/** Default budget for drawable elements.
	 */
	int DEFAULT_DRAWABLE_ELEMENT_BUDGET = 20000;

	/** Default budget for drawable elements.
	 */
	int MIN_DRAWABLE_ELEMENT_BUDGET = 100;

	/** Default scaling delta.
	 */
	double DEFAULT_SCALE_CHANGE = 1.05;

	/** Minimum scaling delta.
	 */
	double MIN_SCALE_CHANGE = 1.000000001;

	/** Minimal scale value.
	 */
	double MINIMUM_SCALE_VALUE = 0.001;

	/** Maximal scale value.
	 */
	double MAXIMUM_SCALE_VALUE = 100.0;

	/** Replies the property that contains the document model.
	 *
	 * @return the property.
	 */
	@Pure
	ObjectProperty<DT> documentModelProperty();

	/** Replies the model of the document.
	 *
	 * @return the model of the document.
	 */
	@Pure
	DT getDocumentModel();

	/** Change the model of the document.
	 *
	 * @param model the model of the document.
	 */
	void setDocumentModel(DT model);

	/** Replies the property that contains the document drawer.
	 *
	 * @return the property.
	 */
	@Pure
	ObjectProperty<DocumentDrawer<T, DT>> documentDrawerProperty();

	/** Replies the drawer of the document.
	 *
	 * @return the drawer of the document.
	 */
	@Pure
	DocumentDrawer<T, DT> getDocumentDrawer();

	/** Change the drawer of the document.
	 *
	 * @param drawer the drawer of the document.
	 */
	void setDocumentDrawer(DocumentDrawer<T, DT> drawer);

	/** Replies the property that contains the document bounds.
	 *
	 * @return the property.
	 */
	@Pure
	ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> documentBoundsProperty();

	/** Replies the bounds of the document.
	 *
	 * @return the bounds of the document.
	 */
	@Pure
	Rectangle2afp<?, ?, ?, ?, ?, ?> getDocumentBounds();

	/** Replies the property for the scale value.
	 *
	 * <p>A scale value of {@code 1} is neither zoom in nor zoom out.
	 * A value greater than 1 is a zoom in. A value lower than 1 is a zoom out.
	 *
	 * @return the property.
	 */
	@Pure
	DoubleProperty scaleValueProperty();

	/** Replies the scale value.
	 *
	 * <p>A scale value of {@code 1} is neither zoom in nor zoom out.
	 * A value greater than 1 is a zoom in. A value lower than 1 is a zoom out.
	 *
	 * @return the scale value.
	 */
	@Pure
	double getScaleValue();

	/** Change the scale value.
	 *
	 * <p>A scale value of {@code 1} is neither zoom in nor zoom out.
	 * A value greater than 1 is a zoom in. A value lower than 1 is a zoom out.
	 *
	 * @param value the scale value.
	 */
	void setScaleValue(double value);

	/** Change the zoom factor and center the view point at the given given position.
	 *
	 * <p>A scale value of {@code 1} is neither zoom in nor zoom out.
	 * A value greater than 1 is a zoom in. A value lower than 1 is a zoom out.
	 *
	 * @param scaleValue the scale value.
	 * @param centerX the position along the X axis of the viewport center. Value in [0;1].
	 * @param centerY the position along the Y axis of the viewport center. Value in [0;1].
	 */
	void setScaleValue(double scaleValue, double centerX, double centerY);

	/** Replies the property for the minimum scale value.
	 *
	 * <p>The min scale value has always precedence on the max scale value. It means that
	 * the min value becomes greater to the max value, or the max value becomes lower than
	 * the min value, the min value will be the valid reference and the max value will
	 * be changed accordingly.
	 *
	 * @return the property.
	 */
	@Pure
	DoubleProperty minScaleValueProperty();

	/** Replies the minimum scale value.
	 *
	 * @return the minimum scale value.
	 */
	@Pure
	double getMinScaleValue();

	/** Change the minimum scale value.
	 *
	 * @param value the minimum scale value.
	 */
	void setMinScaleValue(double value);

	/** Replies the property for the maximum scale value.
	 *
	 * <p>The min scale value has always precedence on the max scale value. It means that
	 * the min value becomes greater to the max value, or the max value becomes lower than
	 * the min value, the min value will be the valid reference and the max value will
	 * be changed accordingly.
	 *
	 * @return the property.
	 */
	@Pure
	DoubleProperty maxScaleValueProperty();

	/** Replies the maximum scale value.
	 *
	 * @return the maximum scale value.
	 */
	@Pure
	double getMaxScaleValue();

	/** Change the maximum scale value.
	 *
	 * @param value the maximum scale value.
	 */
	void setMaxScaleValue(double value);

	/** Replies the property for the x coordinate of the viewport center in document coordinates.
	 *
	 * @return the property.
	 */
	@Pure
	DoubleProperty viewportCenterXProperty();

	/** Replies the x coordinate of the viewport center in document coordinates.
	 *
	 * @return the x coordinate of the viewport center in document coordinates.
	 */
	@Pure
	double getViewportCenterX();

	/** Change the x coordinate of the viewport center in document coordinates.
	 *
	 * @param x the x coordinate of the viewport center in document coordinates.
	 */
	void setViewportCenterX(double x);

	/** Replies the property for the y coordinate of the viewport center in document coordinates.
	 *
	 * @return the property.
	 */
	@Pure
	DoubleProperty viewportCenterYProperty();

	/** Replies the y coordinate of the viewport center in document coordinates.
	 *
	 * @return the y coordinate of the viewport center in document coordinates.
	 */
	@Pure
	double getViewportCenterY();

	/** Change the y coordinate of the viewport center in document coordinates.
	 *
	 * @param y the y coordinate of the viewport center in document coordinates.
	 */
	void setViewportCenterY(double y);

	/** Change the coordinates of the viewport center in document coordinates.
	 *
	 * @param x the x coordinate of the viewport center in document coordinates.
	 * @param y the y coordinate of the viewport center in document coordinates.
	 */
	void setViewportCenter(double x, double y);

	/** Replies the property that contains the viewport bounds in the document coordinates.
	 *
	 * @return the property.
	 */
	@Pure
	ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> viewportBoundsProperty();

	/** Replies the bounds of the viewport in document coordinates.
	 *
	 * @return the bounds of the viewport.
	 */
	@Pure
	Rectangle2afp<?, ?, ?, ?, ?, ?> getViewportBounds();

	/** Replies the scale value to fit the content to the viewport.
	 *
	 * @return the scale value to apply for fitting the content to the viewport.
	 */
	@Pure
	default double getScaleValueToFit() {
		return getScaleValueToFit(false);
	}

	/** Replies the scale value to fit the content to the viewport.
	 *
	 * @param minimizeOnly If the content fits already into the viewport, then we don't
	 *     zoom if this parameter is true.
	 * @return the scale value to apply for fitting the content to the viewport.
	 */
	@Pure
	double getScaleValueToFit(boolean minimizeOnly);

	/** Zoom to fit the content.
	 *
	 * @param minimizeOnly If the content fits already into the viewport, then we don't
	 *     zoom if this parameter is true.
	 */
	default void setScaleValueToFit(boolean minimizeOnly) {
		final double scale = getScaleValueToFit(minimizeOnly);
		final Rectangle2afp<?, ?, ?, ?, ?, ?> document = getDocumentBounds();
		setScaleValue(scale, document.getCenterX(), document.getCenterY());
	}

	/** Reset the scale value to avoid any zoom out or in.
	 */
	default void resetScale() {
		setScaleValue(1.0);
	}

	/** Zoom out.
	 */
	default void zoomOut() {
		zoomOut(getViewportCenterX(), getViewportCenterY());
	}

	/** Zoom out from a specific location, and center at the view point to the given location.
	 *
	 * @param centerX the x coordinate on the point to zoom in.
	 * @param centerY the y coordinate on the point to zoom in.
	 */
	default void zoomOut(double centerX, double centerY) {
		double newValue = getScaleValue() / getScaleChange();
		final double min = getMinScaleValue();
		if (newValue < min) {
			newValue = min;
		}
		setScaleValue(newValue, centerX, centerY);
	}

	/** Zoom in.
	 */
	default void zoomIn() {
		zoomIn(getViewportCenterX(), getViewportCenterY());
	}

	/** Zoom in from a specific location, and center at the view point to the given location.
	 *
	 * @param centerX the x coordinate on the point to zoom in.
	 * @param centerY the y coordinate on the point to zoom in.
	 */
	default void zoomIn(double centerX, double centerY) {
		double newValue = getScaleValue() * getScaleChange();
		final double max = getMaxScaleValue();
		if (newValue > max) {
			newValue = max;
		}
		setScaleValue(newValue, centerX, centerY);
	}

	/** Replies the property for the delta value that is applied when zooming in or out.
	 * The value is strictly greater than 1.
	 *
	 * @return the property.
	 */
	@Pure
	DoubleProperty scaleChangeProperty();


	/** Replies the delta value that is applied when zooming in or out.
	 * The value is strictly greater than 1.
	 *
	 * @return the change positive value. The value is greater than 1.
	 */
	@Pure
	double getScaleChange();

	/** Change the change factor that is applied when zooming in or out.
	 * The value is strictly greater than 1.
	 *
	 * @param change the change positive value. Only positive values greater than 1 are accepted.
	 */
	void setScaleChange(double change);

	/** Replies the property that indicates if the X axis of the displayed data is inverted regarding to the
	 * standard JavaFX coordinate system.
	 *
	 * @return the property.
	 */
	@Pure
	BooleanProperty invertedAxisXProperty();

	/** Replies if the x axis of the displayed data is inverted regarding to the
	 * standard JavaFX coordinate system.
	 *
	 * @return the x coordinate of the viewport center in document coordinates.
	 */
	@Pure
	boolean isInvertedAxisX();

	/** Change the flag if the x axis of the displayed data is inverted regarding to the
	 * standard JavaFX coordinate system.
	 *
	 * @param inverted the x coordinate of the viewport center in document coordinates.
	 */
	void setInvertedAxisX(boolean inverted);

	/** Replies the property that indicates if the Y axis of the displayed data is inverted regarding to the
	 * standard JavaFX coordinate system.
	 *
	 * @return the property.
	 */
	@Pure
	BooleanProperty invertedAxisYProperty();

	/** Replies if the Y axis of the displayed data is inverted regarding to the
	 * standard JavaFX coordinate system.
	 *
	 * @return the Y coordinate of the viewport center in document coordinates.
	 */
	@Pure
	boolean isInvertedAxisY();

	/** Change the flag if the Y axis of the displayed data is inverted regarding to the
	 * standard JavaFX coordinate system.
	 *
	 * @param inverted the Y coordinate of the viewport center in document coordinates.
	 */
	void setInvertedAxisY(boolean inverted);

	/** Replies the property that defines the maximal number of drawable elements to be render.
	 *
	 * <p>The value of the budget is a strictly positive number greater than 100.
	 *
	 * @return the property.
	 */
	@Pure
	IntegerProperty drawableElementBudgetProperty();

	/** Replies the maximal number of drawable elements to be render.
	 *
	 * <p>The value of the budget is a strictly positive number greater than 100.
	 *
	 * @return the budget.
	 */
	@Pure
	int getDrawableElementBudget();

	/** Change the maximal number of drawable elements to be render.
	 *
	 * <p>The value of the budget is a strictly positive number greater than 100.
	 *
	 * @param budget the maximal number of elements to draw.
	 */
	void setDrawableElementBudget(int budget);

}
