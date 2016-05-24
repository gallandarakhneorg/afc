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

package org.arakhne.afc.ui.android.zoom;



/** Abstract implementation of a DocumentWrapper
 * that is supporting only a single change listener.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public abstract class AbstractDocumentWrapper implements DocumentWrapper {
	
	private ChangeListener listener = null;
	
	/**
	 */
	public AbstractDocumentWrapper() {
		//
	}
	
	@Override
	public void addChangeListener(ChangeListener listener) {
		if (this.listener!=null)
			throw new IllegalStateException();
		this.listener = listener;
	}

	@Override
	public void removeChangeListener(ChangeListener listener) {
		if (this.listener!=listener)
			throw new IllegalStateException();
		this.listener = null;
	}
	
	/** Notifies the listener about a change in the geometry of the document.
	 */
	public void fireChange() {
		if (this.listener!=null) {
			this.listener.stateChanged(this);
		}
	}

}
