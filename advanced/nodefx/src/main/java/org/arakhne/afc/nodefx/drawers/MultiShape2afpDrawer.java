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

package org.arakhne.afc.nodefx.drawers;

import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.Shape2afp;
import org.arakhne.afc.nodefx.Drawer;
import org.arakhne.afc.nodefx.Drawers;
import org.arakhne.afc.nodefx.ZoomableGraphicsContext;

/** Drawer of a 2D multishape.
*
* @author $Author: sgalland$
* @version $FullVersion$
* @mavengroupid $GroupId$
* @mavenartifactid $ArtifactId$
* @since 15.0
*/
public class MultiShape2afpDrawer implements Drawer<MultiShape2afp<?, ?, ?, ?, ?, ?, ?>> {

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends MultiShape2afp<?, ?, ?, ?, ?, ?, ?>> getPrimitiveType() {
		return (Class<? extends MultiShape2afp<?, ?, ?, ?, ?, ?, ?>>) MultiShape2afp.class;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void draw(ZoomableGraphicsContext gc, MultiShape2afp<?, ?, ?, ?, ?, ?, ?> element) {
		for (final Shape2afp<?, ?, ?, ?, ?, ?> shape : element) {
			final Drawer drawer = Drawers.getDrawerFor(shape);
			if (drawer != null) {
				drawer.draw(gc, shape);
			}
		}
	}

}
