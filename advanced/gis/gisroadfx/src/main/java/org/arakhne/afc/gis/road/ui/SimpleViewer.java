/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.arakhne.afc.gis.io.shape.GISShapeFileReader;
import org.arakhne.afc.gis.mapelement.GISElementContainer;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.maplayer.GISLayerContainer;
import org.arakhne.afc.gis.maplayer.MapElementLayer;
import org.arakhne.afc.gis.maplayer.MultiMapLayer;
import org.arakhne.afc.gis.maplayer.TreeMapElementLayer;
import org.arakhne.afc.gis.primitive.FlagContainer;
import org.arakhne.afc.gis.primitive.GISContainer;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.layer.RoadNetworkLayer;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.ui.GisPane;
import org.arakhne.afc.inputoutput.filefilter.FileFilter;
import org.arakhne.afc.io.dbase.DBaseFileFilter;
import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.io.shape.ShapeFileFilter;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.text.TextUtil;
import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.arakhne.afc.vmutil.locale.Locale;
import org.eclipse.xtext.xbase.lib.Pure;

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

	private volatile boolean dragging;

	private volatile MapElement selectedRoad;

	private static MapElementLayer<?> loadShapeFile(File file) {
		try {
			StandardRoadNetwork network = null;
			MapElementLayer<MapElement> layer = null;

			final var dbfFile = FileSystem.replaceExtension(file, DBaseFileFilter.EXTENSION_DBASE_FILE);
			final URL dbfUrl;
			if (dbfFile.canRead()) {
				dbfUrl = dbfFile.toURI().toURL();
			} else {
				dbfUrl = null;
			}

			try (var is = new FileInputStream(file)) {
				assert is != null;
				try (var reader = new GISShapeFileReader(is, null, dbfUrl)) {
					final var worldRect = new Rectangle2d();
					final var esriBounds = reader.getBoundsFromHeader();
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
						if (element instanceof RoadPolyline sgmt) {
							if (network == null) {
								network = new StandardRoadNetwork(worldRect);
							}
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
				final var networkLayer = new RoadNetworkLayer(network);
				return networkLayer;
			}
			return layer;
		} catch (IOException exception) {
			throw new IOError(exception);
		}
	}

	/** Convert the given standard file filter to its equivalent JavaFX file filter.
	 *
	 * @param filter the standard file filter to convert.
	 * @return the JavaFX file filter.
	 */
	@Pure
	public static ExtensionFilter toJavaFX(FileFilter filter) {
		return new ExtensionFilter(filter.getDescription(), filter.getExtensions());
	}

	@Override
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:regexp", "checkstyle:npathcomplexity",
		"checkstyle:nestedifdepth", "rawtypes", "unchecked"})
	public void start(Stage primaryStage) {
		final var fileChooser = new FileChooser();
		fileChooser.setTitle(Locale.getString(SimpleViewer.class, "OPEN_WINDOW_TITLE")); //$NON-NLS-1$
		fileChooser.getExtensionFilters().add(
				toJavaFX(new ShapeFileFilter()));
		final var shapeFiles = fileChooser.showOpenMultipleDialog(primaryStage);
		if (shapeFiles != null && !shapeFiles.isEmpty()) {
			final var containers = new ArrayList<MapElementLayer>();
			final var filename = new StringBuilder();
			for (final var shapeFile : shapeFiles) {
				final var loadedResource = loadShapeFile(shapeFile);
				if (loadedResource != null) {
					containers.add(loadedResource);
					if (filename.length() > 0) {
						filename.append(", "); //$NON-NLS-1$
					}
					filename.append(shapeFile.getName());
				}
			}

			if (containers.isEmpty()) {
				System.exit(0);
			}

			final GISContainer container;
			if (containers.size() == 1) {
				container = containers.get(0);
			} else {
				final var layer = new MultiMapLayer<>();
				for (final var child : containers) {
					layer.addMapLayer(child);
				}
				container = layer;
			}

			final var root = new BorderPane();

			final var messageBar = new Label(""); //$NON-NLS-1$
			messageBar.setTextAlignment(TextAlignment.CENTER);

			final var scrollPane = new GisPane(container);

			final var mouseLocationPattern = Locale.getString(SimpleViewer.class, "MOUSE_POSITION"); //$NON-NLS-1$

			scrollPane.setOnMouseMoved(event -> {
				final var mousePosition = scrollPane.toDocumentPosition(event.getX(), event.getY());
				messageBar.setText(MessageFormat.format(mouseLocationPattern,
						TextUtil.formatDouble(event.getX(), 1),
						TextUtil.formatDouble(event.getY(), 1),
						TextUtil.formatDouble(mousePosition.getX(), 4),
						TextUtil.formatDouble(mousePosition.getY(), 4)));
			});

			scrollPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
				this.dragging = true;
				final var mousePosition = scrollPane.toDocumentPosition(event.getX(), event.getY());
				messageBar.setText(MessageFormat.format(mouseLocationPattern,
						TextUtil.formatDouble(event.getX(), 1),
						TextUtil.formatDouble(event.getY(), 1),
						TextUtil.formatDouble(mousePosition.getX(), 4),
						TextUtil.formatDouble(mousePosition.getY(), 4)));
			});

			scrollPane.setOnMouseReleased(event -> {
				if (!this.dragging) {
					final var select1 = this.selectedRoad;
					this.selectedRoad = null;
					if (select1 != null) {
						select1.unsetFlag(FlagContainer.FLAG_SELECTED);
					}
					final var select2 = getElementUnderMouse(scrollPane, event.getX(), event.getY());
					if (select2 != select1) {
						if (select2 != null) {
							select2.setFlag(FlagContainer.FLAG_SELECTED);
							this.selectedRoad = select2;
							if (event.isControlDown()) {
								// Force the loading of all the attributes.
								select2.getAllAttributeNames();
							}
							System.out.println(JsonBuffer.toString(select2));
						}
						scrollPane.drawContent();
						event.consume();
					}
				}
				this.dragging = false;
			});

			root.setCenter(scrollPane);
			root.setBottom(messageBar);

			final var scene = new Scene(root, 1024, 768);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); //$NON-NLS-1$

			primaryStage.setTitle(Locale.getString(SimpleViewer.class, "WINDOW_TITLE", filename.toString())); //$NON-NLS-1$
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			primaryStage.close();
			System.exit(0);
		}
	}

	/** Replies the element at the given mouse position.
	 *
	 * @param pane the element pane.
	 * @param x the x position of the mouse.
	 * @param y the y position of the mouse.
	 * @return the element.
	 * @since 15.0
	 */
	public MapElement getElementUnderMouse(GisPane<?> pane, double x, double y) {
		final var model = pane.getDocumentModel();
		final var mousePosition = pane.toDocumentPosition(x, y);
		final var selectionArea = pane.toDocumentRect(x - 2, y - 2, 5, 5);
		return getElementUnderMouse(model, mousePosition, selectionArea);
	}

	private MapElement getElementUnderMouse(Object model, Point2d mousePosition, Rectangle2d selectionArea) {
		if (model instanceof GISElementContainer elt) {
			return getElementUnderMouse(elt, mousePosition, selectionArea);
		}
		if (model instanceof GISLayerContainer elt) {
			return getElementUnderMouse(elt, mousePosition, selectionArea);
		}
		return null;
	}

	@SuppressWarnings({ "static-method" })
	private MapElement getElementUnderMouse(GISElementContainer<?> model, Point2d mousePosition, Rectangle2d selectionArea) {
		final var iterator = model.iterator(selectionArea);
		var dist = Double.MAX_VALUE;
		MapElement select = null;
		while (iterator.hasNext()) {
			final var road = iterator.next();
			final var distance = Math.abs(road.getDistance(mousePosition));
			if (distance < dist) {
				dist = distance;
				select = road;
			}
		}
		return select;
	}

	private MapElement getElementUnderMouse(GISLayerContainer<?> model, Point2d mousePosition, Rectangle2d selectionArea) {
		final var iterator = model.iterator();
		while (iterator.hasNext()) {
			final var layer = iterator.next();
			if (layer.isVisible() && layer.isClickable()) {
				final var selected = getElementUnderMouse(layer, mousePosition, selectionArea);
				if (selected != null) {
					return selected;
				}
			}
		}
		return null;
	}

	/** Main program.
	 *
	 * @param args the command line arguments.
	 */
	public static void main(String[] args) {
		launch();
	}

}
