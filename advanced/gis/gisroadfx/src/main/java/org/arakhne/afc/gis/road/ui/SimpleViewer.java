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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.arakhne.afc.gis.io.shape.GISShapeFileReader;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.primitive.FlagContainer;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.layer.RoadNetworkLayer;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.ui.GisPane;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ShapeFileFilter;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.text.TextUtil;
import org.arakhne.afc.vmutil.json.JsonBuffer;

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

	private volatile RoadPolyline selectedRoad;

	private static StandardRoadNetwork loadNetwork(File file) {
		try {
			final StandardRoadNetwork network;
			try (InputStream is = new FileInputStream(file)) {
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
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:regexp"})
	public void start(Stage primaryStage) {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Shape File"); //$NON-NLS-1$
		fileChooser.setSelectedExtensionFilter(new ShapeFileFilter().toJavaFX());
		final File shapeFile = fileChooser.showOpenDialog(primaryStage);
		if (shapeFile != null) {
			final StandardRoadNetwork network = loadNetwork(shapeFile);
			final RoadNetworkLayer networkLayer = new RoadNetworkLayer(network);

			final BorderPane root = new BorderPane();

			final Label messageBar = new Label(""); //$NON-NLS-1$
			messageBar.setTextAlignment(TextAlignment.CENTER);

			final GisPane<RoadPolyline> scrollPane = new GisPane<>(networkLayer);

			scrollPane.setOnMouseMoved(event -> {
				final Point2d mousePosition = scrollPane.toDocumentPosition(event.getX(), event.getY());
				messageBar.setText("(" + TextUtil.formatDouble(event.getX(), 4) //$NON-NLS-1$
					+ "; " + TextUtil.formatDouble(event.getY(), 4) //$NON-NLS-1$
					+ ") / (" + TextUtil.formatDouble(mousePosition.getX(), 4) //$NON-NLS-1$
					+ "; " + TextUtil.formatDouble(mousePosition.getY(), 4) //$NON-NLS-1$
					+ ")"); //$NON-NLS-1$
			});

			scrollPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
				final Point2d mousePosition = scrollPane.toDocumentPosition(event.getX(), event.getY());
				messageBar.setText("(" + TextUtil.formatDouble(event.getX(), 4) //$NON-NLS-1$
					+ "; " + TextUtil.formatDouble(event.getY(), 4) //$NON-NLS-1$
					+ ") / (" + TextUtil.formatDouble(mousePosition.getX(), 4) //$NON-NLS-1$
					+ "; " + TextUtil.formatDouble(mousePosition.getY(), 4) //$NON-NLS-1$
					+ ")"); //$NON-NLS-1$
			});

			scrollPane.setOnMouseClicked(event -> {
				final RoadPolyline select1 = this.selectedRoad;
				this.selectedRoad = null;
				if (select1 != null) {
					select1.unsetFlag(FlagContainer.FLAG_SELECTED);
				}
				final Point2d mousePosition = scrollPane.toDocumentPosition(event.getX(), event.getY());
				final Rectangle2d selectionArea = scrollPane.toDocumentRect(event.getX() - 2, event.getY() - 2, 5, 5);
				final Iterator<RoadPolyline> iterator = scrollPane.getDocumentModel().iterator(selectionArea);
				double dist = Double.MAX_VALUE;
				RoadPolyline select2 = null;
				while (iterator.hasNext()) {
					final RoadPolyline road = iterator.next();
					final double distance = Math.abs(road.getDistance(mousePosition));
					if (distance < dist) {
						dist = distance;
						select2 = road;
					}
				}
				if (select2 != select1) {
					if (select2 != null) {
						select2.setFlag(FlagContainer.FLAG_SELECTED);
						this.selectedRoad = select2;
						System.out.println(JsonBuffer.toString(select2));
					}
					scrollPane.drawContent();
					event.consume();
				}
			});

			root.setCenter(scrollPane);
			root.setBottom(messageBar);

			final Scene scene = new Scene(root, 1024, 768);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); //$NON-NLS-1$

			primaryStage.setTitle("Road Network Viewer"); //$NON-NLS-1$
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			primaryStage.close();
			System.exit(0);
		}
	}

	/** Main program.
	 *
	 * @param args the command line arguments.
	 */
	public static void main(String[] args) {
		launch();
	}

}
