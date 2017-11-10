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

package org.arakhne.afc.gis.road.ui;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.arakhne.afc.gis.io.shape.GISShapeFileReader;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.layer.RoadNetworkLayer;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.ui.GisScrollPane;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.vmutil.Resources;

/**
 * Application for viewing GIS primitives.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class SimpleViewer extends Application {

	private static final String SHP_RESOURCE = "org/arakhne/afc/gis/road/ui/Belfort.shp"; //$NON-NLS-1$

	private static StandardRoadNetwork loadNetwork() {
		try {
			final StandardRoadNetwork network;
			try (InputStream is = Resources.getResourceAsStream(SHP_RESOURCE)) {
				assert is != null;
				try (GISShapeFileReader reader = new GISShapeFileReader(is, RoadPolyline.class)) {
					final Rectangle2d worldRect = new Rectangle2d();
					final ESRIBounds esriBounds = reader.getBoundsFromHeader();
					worldRect.setFromCorners(
							esriBounds.getMinX(),
							esriBounds.getMinY(),
							esriBounds.getMaxX(),
							esriBounds.getMaxY());

					network = new StandardRoadNetwork(worldRect);
					MapElement element;

					while ((element = reader.read()) != null) {
						if (element instanceof RoadPolyline) {
							final RoadPolyline sgmt = (RoadPolyline) element;
							try {
								network.addRoadSegment(sgmt);
							} catch (RoadNetworkException e) {
								throw new RuntimeException(e);
							}
						}
					}
				}
			}
			return network;
		} catch (IOException exception) {
			throw new IOError(exception);
		}
	}

	@Override
	public void start(Stage primaryStage) {
		final StandardRoadNetwork network = loadNetwork();
		final RoadNetworkLayer networkLayer = new RoadNetworkLayer(network);

		final BorderPane root = new BorderPane();

		final GisScrollPane scrollPane = new GisScrollPane(networkLayer);

		root.setCenter(scrollPane);

		final Scene scene = new Scene(root, 1024, 768);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); //$NON-NLS-1$

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/** Main program.
	 *
	 * @param args the command line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
