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

package org.arakhne.afc.math.geometry.d2;

import java.io.Serializable;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.PathElementType;

/** An element of the path.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PathElement2D extends Serializable, Cloneable {

	/** Replies the type of the element.
	 *
	 * @return {@code true} if the points are
	 *     the same; otherwise {@code false}.
	 */
	@Pure
	PathElementType getType();

	/** Replies if the element is empty, ie. the points are the same.
	 *
	 * @return {@code true} if the points are
	 *     the same; otherwise {@code false}.
	 */
	@Pure
	boolean isEmpty();

	/** Replies if the element is not empty and is drawable.
	 *
	 * <p>Only the path elements that may produce pixels on the screen
	 * must reply {@code true} in this function.
	 *
	 * @return {@code true} if the path element
	 *     is drawable; otherwise {@code false}.
	 */
	@Pure
	boolean isDrawable();

}
