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

import javafx.beans.property.ReadOnlyObjectPropertyBase;

/**
 * A convenient class to define read-only properties for unit vectors. It
 * creates two properties that are synchronized. One property is read-only
 * and can be passed to external users. The other property is read- and
 * writable and should be used internally only.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class ReadOnlyUnitVectorWrapper extends UnitVectorProperty {

	private ReadOnlyPropertyImpl readOnlyProperty;

	/** Construct a property.
	 *
	 * @param bean the owner of the property.
	 * @param name the name of the property.
	 * @param factory the factory used by this wrapper.
	 */
	public ReadOnlyUnitVectorWrapper(Object bean, String name, GeomFactory2dfx factory) {
		super(bean, name, factory);
	}

	/**
	 * Returns the read-only property, that is synchronized with this
	 * {@code ReadOnlyUnitVectorPropertyWrapper}.
	 *
	 * @return the read-only property.
	 */
	public ReadOnlyUnitVectorProperty getReadOnlyProperty() {
		if (this.readOnlyProperty == null) {
			this.readOnlyProperty = new ReadOnlyPropertyImpl();
		}
		return this.readOnlyProperty;
	}

	@Override
	protected void fireValueChangedEvent() {
		super.fireValueChangedEvent();
		if (this.readOnlyProperty != null) {
			this.readOnlyProperty.fireValueChangedEvent();
		}
	}

	/** Internal implementation.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class ReadOnlyPropertyImpl extends ReadOnlyObjectPropertyBase<Vector2dfx> implements ReadOnlyUnitVectorProperty {

		/** Constructor.
		 */
		ReadOnlyPropertyImpl() {
			//
		}

		@Override
		protected void fireValueChangedEvent() {
			super.fireValueChangedEvent();
		}

		@Override
		public Vector2dfx get() {
			return ReadOnlyUnitVectorWrapper.this.get();
		}

		@Override
		public Object getBean() {
			return ReadOnlyUnitVectorWrapper.this.getBean();
		}

		@Override
		public String getName() {
			return ReadOnlyUnitVectorWrapper.this.getName();
		}

		@Override
		public double getX() {
			return ReadOnlyUnitVectorWrapper.this.getX();
		}

		@Override
		public double getY() {
			return ReadOnlyUnitVectorWrapper.this.getY();
		}

	}

}
