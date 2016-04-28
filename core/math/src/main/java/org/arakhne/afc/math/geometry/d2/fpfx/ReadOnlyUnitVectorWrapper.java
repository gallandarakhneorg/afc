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
package org.arakhne.afc.math.geometry.d2.fpfx;

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
	 */
	public ReadOnlyUnitVectorWrapper(Object bean, String name) {
		super(bean, name);
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent();
        }
    }

    private class ReadOnlyPropertyImpl extends ReadOnlyObjectPropertyBase<Vector2fx> implements ReadOnlyUnitVectorProperty {

    	public ReadOnlyPropertyImpl() {
    		//
		}
    	
    	@Override
    	protected void fireValueChangedEvent() {
    		super.fireValueChangedEvent();
    	}
    	
        @Override
        public Vector2fx get() {
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
