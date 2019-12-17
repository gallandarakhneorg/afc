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

package org.arakhne.afc.nodefx;

/** Level of details for a {@code ZoomableCanvas}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public enum LevelOfDetails {

	/** Low level of details.
	 *
	 * <p>At this level, the size of a document metric unit is lower or equal to a couple of pixels.
	 */
	LOW,

	/** Normal level of details.
	 *
	 * <p>At this level, the size of a document 1/100 metric unit is lower or equal to a couple of pixels.
	 */
	NORMAL,

	/** High level of details.
	 *
	 * <p>At this level, the size of a document 1/100 metric unit could be easily seen when rendered.
	 */
	HIGH;

}
