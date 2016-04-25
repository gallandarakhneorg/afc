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

import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.fp.Vector2fp;

import javafx.beans.property.SimpleObjectProperty;

/**
 * A JavaFX property that is representing a unit vector.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class UnitVectorProperty extends SimpleObjectProperty<Vector2D> {

	/** Construct a property.
	 *
	 * @param bean the owner of the property.
	 * @param name the name of the property.
	 * @param initialValue the initial value.
	 */
	public UnitVectorProperty(Object bean, String name, Vector2D initialValue) {
		super(bean, name, initialValue.toUnitVector());
	}
	
	@Override
	public Vector2D get() {
		// Avoid external changes
		return super.get().toUnmodifiable();
	}
	
	@Override
	public void set(Vector2D newValue) {
		assert (newValue != null) : "Initial value must be not null"; //$NON-NLS-1$
		super.set(new Vector2fx(newValue));
	}
	
	@Override
	protected void invalidated() {
		super.get().normalize();
	}

	/** Change the coordinates of the vector.
	 *
	 * @param x x coordinate of the vector.
	 * @param y y coordinate of the vector.
	 */
	public void set(double x, double y) {
		set(new Vector2fp(x, y));
	}

	/** Replies the x coordinate of the vector.
	 *
	 * @return the x coordinate of the vector.
	 */
	public double getX() {
		return super.get().getX();
	}

	/** Replies the y coordinate of the vector.
	 *
	 * @return the y coordinate of the vector.
	 */
	public double getY() {
		return super.get().getY();
	}

}
