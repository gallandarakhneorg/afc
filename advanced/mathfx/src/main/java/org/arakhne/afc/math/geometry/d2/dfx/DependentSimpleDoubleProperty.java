/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.dfx;

import java.lang.ref.WeakReference;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * A JavaFX double property that depends on another property.
 *
 * @param <T> type of the dependency
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
class DependentSimpleDoubleProperty<T extends ObservableValue<?>> extends SimpleDoubleProperty {

	@SuppressWarnings("rawtypes")
	private final Listener listener;

	private WeakReference<T> dependency;

	/** Construct a property.
	 *
	 * @param bean the owner of the property.
	 * @param name the name of the property.
	 * @param dependency the dependency.
	 */
	@SuppressWarnings("unchecked")
	DependentSimpleDoubleProperty(Object bean, String name, T dependency) {
		super(bean, name);
		assert dependency != null : AssertMessages.notNullParameter(2);
		this.dependency = new WeakReference<>(dependency);
		this.listener = new Listener<>();
		dependency.addListener(this.listener);
	}

	@Override
	protected final void invalidated() {
		invalidated(this.dependency.get());
	}

	/** This method be overridden to receive invalidation notifications.
     *
     * <p>The default implementation is empty.
     *
     * @param dependency the dependency.
     */
	protected void invalidated(T dependency) {
		//
	}

	/**
	 * A JavaFX double property that depends on another property.
	 *
	 * @param <DT> type of the data.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class Listener<DT> implements ChangeListener<DT> {

		/** Construct a listener.
		 */
		Listener() {
			//
		}

		@Override
		public void changed(ObservableValue<? extends DT> observable, DT oldValue, DT newValue) {
			invalidated();
		}

	}

}
