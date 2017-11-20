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
public class ContDrawer1 implements DocumentDrawer<String, MyDoc<String>> {

	@Override
	public void draw(ZoomableGraphicsContext gc, MyDoc<String> element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<? extends MyDoc<String>> getElementType() {
		return (Class<? extends MyDoc<String>>) MyDoc.class;
	}

	@Override
	public Drawer<? super String> getElementDrawer() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<? extends String> getContainedElementType() {
		return String.class;
	}

	@Override
	public boolean isDrawable(ZoomableGraphicsContext gc, String mapelement) {
		throw new UnsupportedOperationException();
	}

}