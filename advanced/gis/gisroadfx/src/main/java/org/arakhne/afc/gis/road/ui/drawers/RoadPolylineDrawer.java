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
import org.arakhne.afc.vmutil.ColorNames;

/** Drawer of a map road polyline.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class RoadPolylineDrawer extends AbstractMapPolylineDrawer<RoadPolyline> {

	/** Default color for roads' interior.
	 */
	public static final int ROAD_COLOR = ColorNames.getColorFromName("white"); //$NON-NLS-1$

	/** Default color for selected roads' interior.
	 */
	public static final int SELECTED_ROAD_COLOR = ColorNames.getColorFromName("antiquewhite"); //$NON-NLS-1$

	@Override
	@Pure
	public Class<? extends RoadPolyline> getPrimitiveType() {
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
			gc.stroke();
			break;
		case RoadNetworkDrawerConstants.DRAWING_STATE_ROAD_INTERIOR:
			setupRoadInterior(gc, element);
			gc.stroke();
			break;
		case RoadNetworkDrawerConstants.DRAWING_STATE_ROAD_DETAILS:
			setupRoadDetails(gc, element);
			gc.stroke();
			break;
		default:
		}
	}

	/** Setup for drawing the road borders.
	 *
	 * @param gc the graphics context.
	 * @param element the element to draw.
	 */
	protected void setupRoadBorders(ZoomableGraphicsContext gc, RoadPolyline element) {
		final Color color = gc.rgb(getDrawingColor(element));
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
	protected void setupRoadInterior(ZoomableGraphicsContext gc, RoadPolyline element) {
		final Color color;
		if (isSelected(element)) {
			color = gc.rgb(SELECTED_ROAD_COLOR);
		} else {
			color = gc.rgb(ROAD_COLOR);
		}
		gc.setStroke(color);
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
