/* $Id$
 * 
 * Copyright (C) 2011-12 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.util.ref;

import java.util.Collection;
import java.util.Set;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class WeakTreeSetTest extends AbstractCollectionTestCase<String,Set<String>> {

	/**
	 */
	public WeakTreeSetTest() {
		super();
	}

	@Override
	protected String createContentInstance() {
		return randomString();
	}

	@Override
	protected Set<String> createCollection() {
		return new WeakTreeSet<String>();
	}

	@Override
	protected void initCollectionWith(Collection<String> toAdd) {
		this.collection.clear();
		this.collection.addAll(toAdd);
	}

	@Override
	protected void fillCollectionWith(Collection<String> toAdd) {
		this.collection.addAll(toAdd);
	}

}
