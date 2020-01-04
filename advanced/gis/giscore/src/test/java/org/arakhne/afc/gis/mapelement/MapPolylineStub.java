/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.gis.mapelement;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;

/** Stub for MapPolyline.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class MapPolylineStub extends MapPolyline {

	private static final long serialVersionUID = 4782201307823311706L;

	private final Shape2d<?> theoriticalBounds;

	/**
	 */
	public MapPolylineStub() {
		super(new HeapAttributeCollection());

		int pointCount = (int)(Math.random() * 200.)+2;

		double minX=0, maxX=0, minY=0, maxY=0;

		for(int i=0; i<pointCount; ++i) {
			double x = Math.random() * 1000. - 500.;
			double y = Math.random() * 1000. - 500.;
			addPoint(new Point2d(x,y));
			if ((i==0)||(x<minX)) minX = x;
			if ((i==0)||(x>maxX)) maxX = x;
			if ((i==0)||(y<minY)) minY = y;
			if ((i==0)||(y>maxY)) maxY = y;
		}

		this.theoriticalBounds = new Rectangle2d(minX, minY, maxX - minX, maxY - minY);
	}

	/**
	 * @return the original bounds.
	 */
	public Shape2d<?> getOriginalBounds() {
		return this.theoriticalBounds;
	}

}
