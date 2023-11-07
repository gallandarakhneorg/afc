/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.gis.grid;

import java.util.Iterator;

import org.arakhne.afc.gis.primitive.GISPrimitive;

/**
 * Iterator that is replying the cells around a specified position.
 *
 * @param <P> is the type of the user data inside the node.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
interface AroundCellIterator<P extends GISPrimitive> extends Iterator<GridCell<P>> {

	/** Replies the index of the cocentric circle on which the last element
	 * replied by {@link #next()} is.
	 * @return {@code 0} if the cell is the center, {@code 1} if the cell is on
	 *     the first circle around, {@code 2} if the cell is on the second circle around,
	 *     etc.
	 */
	int getLevel();

}
