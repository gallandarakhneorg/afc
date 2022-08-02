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

package org.arakhne.afc.io.dbase;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Exception throws when the size of a record specified inside
 * the header does not corresponds to the computed one
 * after reading the header.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class InvalidRecordSizeException extends DBaseFileException {

	private static final long serialVersionUID = -765723839371892324L;

	private final int desiredSize;

	private final int computedSize;

	/** Constructor.
	 *
	 * @param desiredSize the desired size.
	 * @param computedSize the computed size.
	 */
	public InvalidRecordSizeException(int desiredSize, int computedSize) {
		this.desiredSize = desiredSize;
		this.computedSize = computedSize;
	}

	/** Replies the attempted size of the header.
	 *
	 * @return the attempted size of the header.
	 */
	@Pure
	public int getHeaderSize() {
		return this.desiredSize;
	}

	/** Replies the computed size of the header.
	 *
	 * @return the computed size of the header.
	 */
	@Pure
	public int getComputedSize() {
		return this.computedSize;
	}

}
