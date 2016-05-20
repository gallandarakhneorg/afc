/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.d2.dfx;

import java.lang.ref.WeakReference;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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
	public DependentSimpleDoubleProperty(Object bean, String name, T dependency) {
		super(bean, name);
		assert (dependency != null) : "Dependency must be not null"; //$NON-NLS-1$
		this.dependency = new WeakReference<>(dependency);
		this.listener = new Listener<>();
		dependency.addListener(this.listener);
	}
	
	
	
	@Override
	protected final void invalidated() {
		invalidated(this.dependency.get());
	}
	
	 /**
     * This method be overridden to receive invalidation notifications.
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
		public Listener() {
			//
		}

		@Override
		public void changed(ObservableValue<? extends DT> observable, DT oldValue, DT newValue) {
			invalidated();
		}
		
	}
	
}
