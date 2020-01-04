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

package org.arakhne.afc.nodefx.drawers;

import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.nodefx.Drawer;
import org.arakhne.afc.nodefx.ZoomableGraphicsContext;

/** Drawer of a 2D rounded rectangle.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@SuppressWarnings("rawtypes")
public class RoundRectangle2afpDrawer implements Drawer<RoundRectangle2afp> {

	@Override
	public Class<? extends RoundRectangle2afp> getPrimitiveType() {
		return RoundRectangle2afp.class;
	}

	@Override
	public void draw(ZoomableGraphicsContext gc, RoundRectangle2afp element) {
		gc.fillRoundRect(element.getMinX(), element.getMinY(), element.getWidth(), element.getHeight(),
				element.getArcWidth(), element.getArcHeight());
		gc.strokeRoundRect(element.getMinX(), element.getMinY(), element.getWidth(), element.getHeight(),
				element.getArcWidth(), element.getArcHeight());
	}

}
