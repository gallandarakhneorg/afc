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

package org.arakhne.afc.nodefx.tests;

import org.arakhne.afc.nodefx.DocumentDrawer;
import org.arakhne.afc.nodefx.Drawer;
import org.arakhne.afc.nodefx.ZoomableGraphicsContext;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@SuppressWarnings("all")
public class ContDrawer2 implements DocumentDrawer<Number, MyDoc<Number>> {

	@Override
	public void draw(ZoomableGraphicsContext gc, MyDoc<Number> element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<? extends MyDoc<Number>> getElementType() {
		return (Class<? extends MyDoc<Number>>) MyDoc.class;
	}

	@Override
	public Drawer<? super Number> getElementDrawer() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<? extends Number> getContainedElementType() {
		return Integer.class;
	}

	@Override
	public boolean isDrawable(ZoomableGraphicsContext gc, Number mapelement) {
		throw new UnsupportedOperationException();
	}

}