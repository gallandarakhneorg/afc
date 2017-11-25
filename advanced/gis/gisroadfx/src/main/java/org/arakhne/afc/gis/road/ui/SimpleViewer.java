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
import java.text.MessageFormat;
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
import org.arakhne.afc.gis.mapelement.GISElementContainer;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.maplayer.MapElementLayer;
import org.arakhne.afc.gis.maplayer.TreeMapElementLayer;
import org.arakhne.afc.gis.primitive.FlagContainer;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.layer.RoadNetworkLayer;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.ui.GisPane;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.io.shape.ShapeFileFilter;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.text.TextUtil;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.arakhne.afc.vmutil.locale.Locale;

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

	private volatile MapElement selectedRoad;

	private static GISElementContainer<?> loadShapeFile(File file) {
		try {
			StandardRoadNetwork network = null;
			MapElementLayer<MapElement> layer = null;
			try (InputStream is = new FileInputStream(file)) {
				assert is != null;
				try (GISShapeFileReader reader = new GISShapeFileReader(is)) {
					final Rectangle2d worldRect = new Rectangle2d();
					final ESRIBounds esriBounds = reader.getBoundsFromHeader();
					worldRect.setFromCorners(
							esriBounds.getMinX(),
							esriBounds.getMinY(),
							esriBounds.getMaxX(),
							esriBounds.getMaxY());

					if (reader.getShapeElementType() == ShapeElementType.POLYLINE) {
						reader.setMapElementType(RoadPolyline.class);
					}

					MapElement element;

					while ((element = reader.read()) != null) {
						if (element instanceof RoadPolyline) {
							if (network == null) {
								network = new StandardRoadNetwork(worldRect);
							}
							final RoadPolyline sgmt = (RoadPolyline) element;
							try {
								network.addRoadSegment(sgmt);
							} catch (RoadNetworkException e) {
								throw new RuntimeException(e);
							}
						} else {
							if (layer == null) {
								layer = new TreeMapElementLayer<>(worldRect);
							}
							try {
								layer.addMapElement(element);
							} catch (RoadNetworkException e) {
								throw new RuntimeException(e);
							}
						}
					}
				}
			}
			if (network != null) {
				final RoadNetworkLayer networkLayer = new RoadNetworkLayer(network);
				return networkLayer;
			}
			return layer;
		} catch (IOException exception) {
			throw new IOError(exception);
		}
	}

	@Override
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:regexp", "rawtypes", "unchecked"})
	public void start(Stage primaryStage) {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Locale.getString(SimpleViewer.class, "OPEN_WINDOW_TITLE")); //$NON-NLS-1$
		fileChooser.getExtensionFilters().add(new ShapeFileFilter().toJavaFX());
		final File shapeFile = fileChooser.showOpenDialog(primaryStage);
		if (shapeFile != null) {
			final GISElementContainer loadedResource = loadShapeFile(shapeFile);

			final BorderPane root = new BorderPane();

			final Label messageBar = new Label(""); //$NON-NLS-1$
			messageBar.setTextAlignment(TextAlignment.CENTER);

			final GisPane<MapElement> scrollPane = new GisPane(loadedResource);

			final String mouseLocationPattern = Locale.getString(SimpleViewer.class, "MOUSE_POSITION"); //$NON-NLS-1$

			scrollPane.setOnMouseMoved(event -> {
				final Point2d mousePosition = scrollPane.toDocumentPosition(event.getX(), event.getY());
				messageBar.setText(MessageFormat.format(mouseLocationPattern,
						TextUtil.formatDouble(event.getX(), 1),
						TextUtil.formatDouble(event.getY(), 1),
						TextUtil.formatDouble(mousePosition.getX(), 4),
						TextUtil.formatDouble(mousePosition.getY(), 4)));
			});

			scrollPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
				final Point2d mousePosition = scrollPane.toDocumentPosition(event.getX(), event.getY());
				messageBar.setText(MessageFormat.format(mouseLocationPattern,
						TextUtil.formatDouble(event.getX(), 1),
						TextUtil.formatDouble(event.getY(), 1),
						TextUtil.formatDouble(mousePosition.getX(), 4),
						TextUtil.formatDouble(mousePosition.getY(), 4)));
			});

			scrollPane.setOnMouseClicked(event -> {
				final MapElement select1 = this.selectedRoad;
				this.selectedRoad = null;
				if (select1 != null) {
					select1.unsetFlag(FlagContainer.FLAG_SELECTED);
				}
				final Point2d mousePosition = scrollPane.toDocumentPosition(event.getX(), event.getY());
				final Rectangle2d selectionArea = scrollPane.toDocumentRect(event.getX() - 2, event.getY() - 2, 5, 5);
				final Iterator<? extends MapElement> iterator = scrollPane.getDocumentModel().iterator(selectionArea);
				double dist = Double.MAX_VALUE;
				MapElement select2 = null;
				while (iterator.hasNext()) {
					final MapElement road = iterator.next();
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

			primaryStage.setTitle(Locale.getString(SimpleViewer.class, "WINDOW_TITLE", shapeFile.getName())); //$NON-NLS-1$
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
