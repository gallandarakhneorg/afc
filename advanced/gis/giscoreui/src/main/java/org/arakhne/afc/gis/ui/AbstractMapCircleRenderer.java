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

import javafx.scene.shape.Circle;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.MapCircle;
import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;

/** Abstract renderer of a map circle.
 *
 * @param <T> the type of the rendered elements.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class AbstractMapCircleRenderer<T extends MapCircle> extends AbstractGisElementRenderer<T> {

	/** Create the circle path.
	 *
	 * @param element the map element.
	 * @param bounds the gis area bounds.
	 * @return the JavaFX path.
	 */
	@Pure
	protected Circle createCircle(T element, RectangularShape2afp<?, ?, ?, ?, ?, ?> bounds) {
		final Circle circle = new Circle(
				gis2screenx(element.getX(), bounds),
				gis2screeny(element.getY(), bounds),
				element.getRadius());
		return circle;
	}

}
