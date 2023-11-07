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

package org.arakhne.afc.math.tree.builder;

/**
 * An exception during the tree building.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class TreeBuilderException extends Exception {

	private static final long serialVersionUID = -4301432338147841097L;

	/**
	 * This exception was thrown when the bounding box of the
	 * entities that must be insert inside a tree was empty.
	 *
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	public static class EmptyBoundingBoxException extends TreeBuilderException {

		private static final long serialVersionUID = 682140893184447598L;

	}

	/**
	 * This exception was thrown when the building function was
	 * called twice times on the same builder.
	 *
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	public static class BuilderAlreadyStartedException extends TreeBuilderException {

		private static final long serialVersionUID = -8430835745057032650L;

	}

}
