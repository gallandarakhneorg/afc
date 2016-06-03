/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.sizediterator;

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * A sized iterator is an Iterator that is able to
 * reply the size of the iterated collection and
 * the number of elements that may be encountered
 * in the next iterations.
 *
 * @param <M> is the type of element.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface SizedIterator<M> extends Iterator<M> {

	/** Replies the count of elements in the iterated collection.
	 *
	 * @return the count of elements in the iterated collection.
	 */
	@Pure
	int totalSize();

	/** Replies the count of elements which are not replied by the iterator.
	 *
	 * @return the count of elements which are not replied by the iterator.
	 */
	@Pure
	int rest();

	/** Replies the position of the last replied element in the iterated collection.
	 *
	 * @return the index of the last element replied by <code>next()</code>.
	 */
	@Pure
	int index();

}
