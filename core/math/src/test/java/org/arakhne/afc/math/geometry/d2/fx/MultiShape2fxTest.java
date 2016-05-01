/* 
 * $Id$
 * 
 * Copyright (C) 2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.geometry.d2.fx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.arakhne.afc.math.geometry.d2.afp.AbstractMultiShape2afpTest;
import org.arakhne.afc.math.geometry.d2.fx.MultiShape2fx;
import org.arakhne.afc.math.geometry.d2.fx.Rectangle2fx;
import org.arakhne.afc.math.geometry.d2.fx.Shape2fx;
import org.junit.Test;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;

@SuppressWarnings("all")
public class MultiShape2fxTest extends AbstractMultiShape2afpTest<MultiShape2fx<Shape2fx<?>>, Shape2fx<?>, Rectangle2fx> {

	@Override
	protected TestShapeFactory2fx createFactory() {
		return TestShapeFactory2fx.SINGLETON;
	}

	@Test
	public void boundingBoxProperty() {
		ObjectProperty<Rectangle2fx> property = this.shape.boundingBoxProperty();
		assertNotNull(property);
		Rectangle2fx box = property.get();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
	}
	
	@Test
	public void elementsProperty() {
		ListProperty<Shape2fx<?>> property = this.shape.elementsProperty();
		assertNotNull(property);
		assertEquals(2, property.size());
		assertSame(firstObject, property.get(0));
		assertSame(secondObject, property.get(1));
	}

}