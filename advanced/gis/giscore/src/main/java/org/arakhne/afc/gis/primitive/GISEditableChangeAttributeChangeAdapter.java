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

package org.arakhne.afc.gis.primitive;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeChangeEvent;
import org.arakhne.afc.attrs.collection.AttributeChangeListener;

/** Translate any {@link AttributeChangeEvent} to be
 * received by a specified {@link GISEditableChangeListener}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class GISEditableChangeAttributeChangeAdapter implements AttributeChangeListener {

	private final GISEditableChangeListener listener;

	private final GISEditable wrapper;

	/** Constructor.
	 * @param listener is the listener to notify about any attribute change.
	 */
	public GISEditableChangeAttributeChangeAdapter(GISEditableChangeListener listener) {
		this(listener, null);
	}

	/** Constructor.
	 * @param listener is the listener to notify about any attribute change.
	 * @param wrapper is the GISEditable to give to the listener when an attribute has changed,
	 *     if <code>null</code> only the attribute change events with a GISEditable source will
	 *     be forwarded.
	 */
	public GISEditableChangeAttributeChangeAdapter(GISEditableChangeListener listener, GISEditable wrapper) {
		this.listener = listener;
		this.wrapper = wrapper;
	}

	@Override
	public void onAttributeChangeEvent(AttributeChangeEvent event) {
		if (this.listener != null) {
			GISEditable editable = this.wrapper;
			if (editable == null && event.getSource() instanceof GISEditable) {
				editable = (GISEditable) event.getSource();
			}
			if (editable != null) {
				this.listener.editableGISElementHasChanged(editable);
			}
		}
	}

	@Override
	@Pure
	public boolean equals(Object obj) {
		if (obj instanceof GISEditableChangeListener) {
			return this.listener == obj;
		}
		if (obj instanceof GISEditableChangeAttributeChangeAdapter) {
			return this.listener == ((GISEditableChangeAttributeChangeAdapter) obj).listener;
		}
		return false;
	}

	@Override
	@Pure
	public int hashCode() {
		return this.listener == null ? 0 : this.listener.hashCode();
	}

}
