/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

open module org.arakhne.afc.gis.ui.gisroadfx {
	requires org.eclipse.xtext.xbase.lib;
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires org.arakhne.afc.core.text;
	requires org.arakhne.afc.core.vmutils;
	requires org.arakhne.afc.core.mathgraph;
	requires org.arakhne.afc.advanced.shapefile;
	requires transitive org.arakhne.afc.gis.giscore;
	requires transitive org.arakhne.afc.gis.gisroad;
	requires transitive org.arakhne.afc.gis.ui.giscorefx;
	requires org.arakhne.afc.gis.gisinputoutput;

	exports org.arakhne.afc.gis.road.ui;
	exports org.arakhne.afc.gis.road.ui.drawers;

	provides org.arakhne.afc.nodefx.Drawer
		with org.arakhne.afc.gis.road.ui.drawers.RoadPolylineDrawer,
		org.arakhne.afc.gis.road.ui.drawers.RoadNetworkDrawer;
}