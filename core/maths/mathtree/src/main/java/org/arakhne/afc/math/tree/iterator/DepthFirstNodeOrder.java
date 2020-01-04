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

package org.arakhne.afc.math.tree.iterator;

/** Indicates when a node will be treated
 * in comparison to its child nodes.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public enum DepthFirstNodeOrder {

	/** The parent node will be treated before
	 * all its children.
	 */
	PREFIX,

	/** The parent node will be treated after
	 * all its children.
	 */
	POSTFIX,

	/** The parent node will be treated at the same
	 * time of its children.
	 * The "at the same time" notion is ambigous.
	 * The default behavior of the iterator assumes
	 * that the parent node will be treated in
	 * the middle of its child set.
	 */
	INFIX;

}
