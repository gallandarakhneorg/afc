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

package org.arakhne.afc.gis.bus.layer;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.bus.network.BusPrimitiveInvalidity;

/**
 * A bus layer is a container of bus primitives.
 * This interface provides all the functions common
 * to all the bus layers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface BusLayer {

	/** Replies if this layer and its content is marked as valid.
	 *
	 * <p>The validity of a bus primitive depends on the type of
	 * this primitive. Please refers to the documentation
	 * of that primitive.
	 *
	 * @return <code>true</code> if the layer is valid, otherwise <code>false</code>
	 */
	@Pure
	boolean isValidLayer();

	/** Replies the explanation of the invalidity
	 * of the primitive.
	 *
	 * @return the invalidity reason.
	 */
	@Pure
	BusPrimitiveInvalidity getInvalidityReason();

	/** Check if the validity of this layer is correctly
	 * set and change its values if necessary. This function
	 * revalidate also all the layers and primitives inside this layer.
	 */
	void revalidate();

}
