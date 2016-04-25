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

@SuppressWarnings("all")
public class UnitVectorPropertyTest extends AbstractMathTestCase {

	private static final double ox = 123.456;
	private static final double oy = 951.753;
	private static final double ux = 0.12864;
	private static final double uy = 0.99169;
	
	private UnitVectorProperty property;
	
	@Before
	public void setUp() {
		this.property = new UnitVectorProperty(this, "test", new Vector2fp(ox, oy));
	}
	
	@Test
	public void get() {
		Vector2D v = this.property.get();
		assertNotNull(v);
		assertEpsilonEquals(ux, v.getX());
		assertEpsilonEquals(uy, v.getY());
	}
	
	@Test
	public void setVector2D() {
		Vector2D o = new Vector2fp(3456, 47521);
		this.property.set(o);
		Vector2D v = this.property.get();
		assertNotNull(v);
		assertNotSame(o, v);
		assertEpsilonEquals(.072534, v.getX());
		assertEpsilonEquals(.997366, v.getY());
	}
	
	@Test
	public void setDoubleDouble() {
		this.property.set(3456, 47521);
		Vector2D v = this.property.get();
		assertNotNull(v);
		assertEpsilonEquals(.072534, v.getX());
		assertEpsilonEquals(.997366, v.getY());
	}

	@Test
	public void getX() {
		assertEpsilonEquals(ux, this.property.getX());
	}

	@Test
	public void getY() {
		assertEpsilonEquals(uy, this.property.getY());
	}

}
