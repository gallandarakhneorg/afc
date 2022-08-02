/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.gis.maplayer;

import java.util.Random;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;

/** Stub for MapLayer.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class MapLayerStub extends MapLayer {

	private static final long serialVersionUID = -7994252834059008501L;

	private final Rectangle2d bounds;

	/**
	 */
	public MapLayerStub() {
		super(null, new HeapAttributeCollection());
		Random rnd = new Random();
		this.bounds = new Rectangle2d();
		this.bounds.setFromCorners(
				rnd.nextDouble()*1000.,
				rnd.nextDouble()*1000.,
				rnd.nextDouble()*1000.,
				rnd.nextDouble()*1000.);
	}

	/**
	 * @return the original bounds.
	 */
	public Rectangle2d getOriginalBounds() {
		return this.bounds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Rectangle2d calcBounds() {
		return this.bounds.clone();
	}

	@Override
	public Shape2d<?> getShape() {
		return getBoundingBox();
	}

}
