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

package org.arakhne.afc.gis.road.ui.drawers;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.ui.drawers.AbstractMapPolylineDrawer;
import org.arakhne.afc.nodefx.ZoomableGraphicsContext;

/** Drawer of a map road polyline.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class RoadPolylineDrawer extends AbstractMapPolylineDrawer<RoadPolyline> {

	@Override
	@Pure
	public Class<? extends RoadPolyline> getElementType() {
		return RoadPolyline.class;
	}

	@Override
	public void draw(ZoomableGraphicsContext gc, RoadPolyline element) {
		definePath(gc, element);

		gc.setFill(null);
		gc.setLineCap(StrokeLineCap.ROUND);
		gc.setLineJoin(StrokeLineJoin.ROUND);

		switch (gc.getState()) {
		case RoadNetworkDrawerConstants.DRAWING_STATE_ROAD_BORDERS:
			setupRoadBorders(gc, element);
			break;
		case RoadNetworkDrawerConstants.DRAWING_STATE_ROAD_INTERIOR:
			setupRoadInterior(gc, element);
			break;
		case RoadNetworkDrawerConstants.DRAWING_STATE_ROAD_DETAILS:
			setupRoadDetails(gc, element);
			break;
		default:
		}

		gc.stroke();
	}

	/** Setup for drawing the road borders.
	 *
	 * @param gc the graphics context.
	 * @param element the element to draw.
	 */
	@SuppressWarnings("static-method")
	protected void setupRoadBorders(ZoomableGraphicsContext gc, RoadPolyline element) {
		final Color color = gc.rgb(element.getColor());
		gc.setStroke(color);
		final double width;
		if (element.isWidePolyline()) {
			width = 2 + gc.doc2fxSize(element.getWidth());
		} else {
			width = 3;
		}
		gc.setLineWidthInPixels(width);
	}

	/** Setup for drawing the road interior.
	 *
	 * @param gc the graphics context.
	 * @param element the element to draw.
	 */
	@SuppressWarnings("static-method")
	protected void setupRoadInterior(ZoomableGraphicsContext gc, RoadPolyline element) {
		final Color color = gc.rgb(element.getColor());
		gc.setStroke(color.invert());
		if (element.isWidePolyline()) {
			gc.setLineWidthInMeters(element.getWidth());
		} else {
			gc.setLineWidthInPixels(1);
		}
	}

	/** Setup for drawing the road details.
	 *
	 * @param gc the graphics context.
	 * @param element the element to draw.
	 */
	protected void setupRoadDetails(ZoomableGraphicsContext gc, RoadPolyline element) {
		//
	}

}
