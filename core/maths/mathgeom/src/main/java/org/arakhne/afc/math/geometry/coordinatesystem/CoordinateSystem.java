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

package org.arakhne.afc.math.geometry.coordinatesystem;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Represents a space referencial and provides the convertion utilities.
 *
 * @author $Author: cbohrhauer$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface CoordinateSystem {

	/** Replies the count of dimension in this space referential.
	 *
	 * @return the count of dimensions.
	 */
	@Pure
	int getDimensions();

	/** Replies if this coordinate system is a right-hand coordinate system.
	 *
	 * @return <code>true</code> if this coordinate system is right-handed, otherwise <code>false</code>
	 */
	@Pure
	boolean isRightHanded();

	/** Replies if this coordinate system is a left-hand coordinate system.
	 *
	 * @return <code>true</code> if this coordinate system is left-handed, otherwise <code>false</code>
	 */
	@Pure
	boolean isLeftHanded();

	/** Replies the name of the coordinate system.
	 *
	 * @return the name of the coordinate system.
	 */
	@Pure
	String name();

	/** Replies the index of this coordinate in the enumeration of the available
	 * coordinate systems of the same type.
	 *
	 * @return the index of this coordinate system in the enumeration of the
	 *     available coordinate systems of the same type.
	 */
	@Pure
	int ordinal();

}
