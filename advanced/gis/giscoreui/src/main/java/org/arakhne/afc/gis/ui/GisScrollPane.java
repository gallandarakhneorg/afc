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

package org.arakhne.afc.gis.ui;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.GISElementContainer;
import org.arakhne.afc.gis.mapelement.MapPolyline;

/**
 * Panel that is displaying the GIS primitives and supporting zooming.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class GisScrollPane extends ScrollPane {

	private static final double DEFAULT_SCALE_DELTA = 0.1;

	private static final double MINIMUM_SCALE_VALUE = 0.1;

	private static final double MAXIMUM_SCALE_VALUE = 10.0;

	private Group zoomGroup;

	private Scale scaleTransform;

	private double scaleValue = 1.0;

	private double delta = DEFAULT_SCALE_DELTA;

	private double minScaleValue = MINIMUM_SCALE_VALUE;

	private double maxScaleValue = MAXIMUM_SCALE_VALUE;

	/** Constructor.
	 *
	 * @param content the content of the panel.
	 */
	public GisScrollPane(Node content) {
		initializePane(content);
	}

	/** Constructor.
	 *
	 * @param model the source of the elements.
	 */
	public GisScrollPane(GISElementContainer<? extends MapPolyline> model) {
		final Group canvas = new Group();
		final GisPane<?> gisLayer = new GisPane<>(model);
		canvas.getChildren().add(gisLayer);
		initializePane(canvas);
	}

	/** Initialize the pane content.
	 *
	 * @param content the content.
	 */
	protected void initializePane(Node content) {
		final Group contentGroup = new Group();
		this.zoomGroup = new Group();
		contentGroup.getChildren().add(this.zoomGroup);
		this.zoomGroup.getChildren().add(content);
		setContent(contentGroup);
		this.scaleTransform = new Scale(this.scaleValue, this.scaleValue, 0, 0);
		this.zoomGroup.getTransforms().add(this.scaleTransform);
		this.zoomGroup.setOnScroll(new ZoomHandler());
		setContent(contentGroup);
        setFitToWidth(true);
        setFitToHeight(true);
	}

	/** Replies the scale value.
	 *
	 * <p>A scale value of {@code 1} is neither zoom in nor zoom out.
	 * A value greater than 1 is a zoom in. A value lower than 1 is a zoom out.
	 *
	 * @return the scale value.
	 */
	@Pure
	public double getScaleValue() {
		return this.scaleValue;
	}

	/** Replies the minimum scale value.
	 *
	 * @return the minimum scale value.
	 */
	@Pure
	public double getMinimumScaleValue() {
		return this.minScaleValue;
	}

	/** Replies the maximum scale value.
	 *
	 * @return the maximum scale value.
	 */
	@Pure
	public double getMaximumScaleValue() {
		return this.maxScaleValue;
	}

	/** Change the minimum scale value. This function changes the scale value
	 * if it is lower than the given minimum value.
	 *
	 * @param minValue the minimum scale value.
	 * @param maxValue the maximum scale value.
	 */
	public void setScaleValueBounds(double minValue, double maxValue) {
		if (minValue < maxValue) {
			this.minScaleValue = minValue;
			this.maxScaleValue = maxValue;
		} else {
			this.minScaleValue = maxValue;
			this.maxScaleValue = minValue;
		}
		final double val = getScaleValue();
		if (val < this.minScaleValue) {
			zoomTo(this.minScaleValue);
		} else if (val > this.maxScaleValue) {
			zoomTo(this.maxScaleValue);
		}
	}

	/** Replies the delta value that is applied when zooming in or out.
	 *
	 * @return the delta value.
	 */
	@Pure
	public double getScaleDelta() {
		return this.delta;
	}

	/** Change the delta value that is applied when zooming in or out.
	 *
	 * @param delta the delta positive value. Only positive values are accepted.
	 */
	public void setScaleDelta(double delta) {
		if (delta > 0) {
			this.delta = delta;
		}
	}

	/** Reset the scale value to avoid any zoom out or in.
	 */
	public void resetZoom() {
		zoomTo(1.0);
	}

	/** Change the zoom factor.
	 *
	 * <p>A scale value of {@code 1} is neither zoom in nor zoom out.
	 * A value greater than 1 is a zoom in. A value lower than 1 is a zoom out.
	 *
	 * @param scaleValue the scale value.
	 */
	public void zoomTo(double scaleValue) {
		this.scaleValue = scaleValue;
		this.scaleTransform.setX(scaleValue);
		this.scaleTransform.setY(scaleValue);

	}

	/** Zoom out.
	 */
	public void zoomOut() {
		double newValue = getScaleValue() - getScaleDelta();
		final double min = getMinimumScaleValue();
		if (newValue < min) {
			newValue = min;
		}
		zoomTo(newValue);
	}

	/** Zoom in.
	 */
	public void zoomIn() {
		double newValue = getScaleValue() + getScaleDelta();
		final double max = getMaximumScaleValue();
		if (newValue > max) {
			newValue = max;
		}
		zoomTo(newValue);
	}

	/** Zoom to fit the content.
	 *
	 * @param minimizeOnly If the content fits already into the viewport, then we don't
	 *     zoom if this parameter is true.
	 */
	public void zoomToFit(boolean minimizeOnly) {
		final Bounds viewport = getViewportBounds();
		final Bounds content = getContent().getBoundsInLocal();

		final double contentWidth = content.getWidth();
		final double contentHeight = content.getHeight();

		double scaleX = contentWidth == 0. ? 1. : viewport.getWidth() / contentWidth;
		double scaleY = contentHeight == 0. ? 1. : viewport.getHeight() / contentHeight;

		// consider current scale (in content calculation)
		final double scaleValue = getScaleValue();
		scaleX *= scaleValue;
		scaleY *= scaleValue;

		// distorted zoom: we don't want it => we search the minimum scale
		// factor and apply it
		double scale = Math.min(scaleX, scaleY);

		// check precondition
		if (minimizeOnly) {
			// check if zoom factor would be an enlargement and if so, just set
			// it to 1
			if (scale > 1.) {
				scale = 1;
			}
		}

		// apply zoom
		zoomTo(scale);
	}

	/** Zoom handler.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class ZoomHandler implements EventHandler<ScrollEvent> {

		ZoomHandler() {
			//
		}

		@Override
		public void handle(ScrollEvent scrollEvent) {
			scrollEvent.consume();
			if (scrollEvent.getDeltaY() < 0) {
				zoomOut();
			} else {
				zoomIn();
			}
		}

	}

}
