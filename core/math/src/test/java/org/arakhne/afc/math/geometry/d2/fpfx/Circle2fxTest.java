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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.AbstractCircle2afpTest;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.afp.TestShapeFactory;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.Test;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;

@SuppressWarnings("all")
public class Circle2fxTest extends AbstractCircle2afpTest<Circle2fx, Rectangle2fx> {

	@Override
	protected TestShapeFactory<Point2fx, Rectangle2fx> createFactory() {
		return TestShapeFactory2fx.SINGLETON;
	}

	@Test
	public void xProperty() {
		assertEpsilonEquals(5, this.shape.getX());
		
		DoubleProperty property = this.shape.xProperty();
		assertNotNull(property);
		assertEpsilonEquals(5, property.get());
		
		this.shape.setX(456.159);
		assertEpsilonEquals(456.159, property.get());
	}

	@Test
	public void yProperty() {
		assertEpsilonEquals(8, this.shape.getY());
		
		DoubleProperty property = this.shape.yProperty();
		assertNotNull(property);
		assertEpsilonEquals(8, property.get());
		
		this.shape.setY(456.159);
		assertEpsilonEquals(456.159, property.get());
	}

	@Test
	public void radiusProperty() {
		assertEpsilonEquals(5, this.shape.getRadius());
		
		DoubleProperty property = this.shape.radiusProperty();
		assertNotNull(property);
		assertEpsilonEquals(5, property.get());
		
		this.shape.setRadius(456.159);
		assertEpsilonEquals(456.159, property.get());
	}

}