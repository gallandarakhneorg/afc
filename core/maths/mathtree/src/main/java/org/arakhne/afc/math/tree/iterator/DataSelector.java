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

package org.arakhne.afc.math.tree.iterator;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This interface is used to select the data to reply
 * by a Data*TreeIterator.
 *
 * @param <D> is the type of the data inside the tree
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @see PostfixDataDepthFirstTreeIterator
 * @see DataBroadFirstTreeIterator
 */
@FunctionalInterface
public interface DataSelector<D> {

	/** Replies if the specified data could be replied by the iterator.
	 *
	 * @param data is the data to test.
	 * @return {@code true} if the data could be replied by the iterator,
	 *     otherwhise {@code false}
	 */
	@Pure
	boolean dataCouldBeRepliedByIterator(D data);

}
