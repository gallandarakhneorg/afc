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
package org.arakhne.afc.math.geometry.d2.fpfx;

import static org.junit.Assert.*;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.fp.Vector2fp;
import org.junit.Before;
import org.junit.Test;

import javafx.beans.property.ReadOnlyDoubleProperty;

@SuppressWarnings("all")
public class UnitVectorPropertyTest extends AbstractMathTestCase {

	private static final double ox = 123.456;
	private static final double oy = 951.753;
	private static final double ux = 0.12864;
	private static final double uy = 0.99169;
	
	private UnitVectorProperty property;
	
	@Before
	public void setUp() {
		this.property = new UnitVectorProperty(this, "test");
		double length = Math.hypot(ox, oy);
		this.property.set(ox / length, oy / length);
	}
	
	@Test(expected = AssertionError.class)
	public void setDoubleDouble_notUnitVector() {
		this.property.set(ox, oy);
	}
	
	@Test
	public void setDoubleDouble_unitVector() {
		this.property.set(0.031598, -0.999501);
		assertEpsilonEquals(0.031598, this.property.getX());
		assertEpsilonEquals(-0.999501, this.property.getY());
	}

	@Test(expected = AssertionError.class)
	public void setVector2fx_notUnitVector() {
		this.property.set(new Vector2fx(ox, oy));
	}
	
	@Test
	public void setVector2fx_unitVector() {
		this.property.set(new Vector2fx(0.031598, -0.999501));
		assertEpsilonEquals(0.031598, this.property.getX());
		assertEpsilonEquals(-0.999501, this.property.getY());
	}

	@Test(expected = RuntimeException.class)
	public void setVector2D_onVector() {
		Vector2D v = this.property.get();
		v.set(new Vector2fx(0.031598, -0.999501));
	}

	@Test(expected = RuntimeException.class)
	public void setDoubleDouble_onVector() {
		Vector2D v = this.property.get();
		v.set(0.031598, -0.999501);
	}

	@Test
	public void get() {
		Vector2D v = this.property.get();
		assertNotNull(v);
		assertEpsilonEquals(ux, v.getX());
		assertEpsilonEquals(uy, v.getY());
	}
	
	@Test
	public void getX() {
		assertEpsilonEquals(ux, this.property.getX());
	}

	@Test
	public void getY() {
		assertEpsilonEquals(uy, this.property.getY());
	}

	@Test
	public void xProperty() {
		ReadOnlyDoubleProperty x = this.property.xProperty();
		assertNotNull(x);
		assertEpsilonEquals(ux, x.get());
	}

	@Test
	public void yProperty() {
		ReadOnlyDoubleProperty y = this.property.yProperty();
		assertNotNull(y);
		assertEpsilonEquals(uy, y.get());
	}

}
