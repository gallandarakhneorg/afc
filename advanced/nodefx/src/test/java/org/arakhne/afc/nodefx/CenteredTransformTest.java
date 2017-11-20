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

package org.arakhne.afc.nodefx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.testtools.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@SuppressWarnings("all")
public class CenteredTransformTest extends AbstractTestCase {

	private BooleanProperty trueProp;

	private BooleanProperty falseProp;

	private CenteringTransform[] ts;

	private Rectangle2d viz;

	private ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> vizProp;

	@Before
	public void setUp() {
		this.trueProp = new SimpleBooleanProperty(true);
		this.falseProp = new SimpleBooleanProperty(false);
		this.viz = new Rectangle2d(1, 2, 3, 4);
		this.vizProp = new SimpleObjectProperty<>(this.viz);
		this.ts = new CenteringTransform[] {
				new CenteringTransform(this.falseProp, this.falseProp, this.vizProp),
				new CenteringTransform(this.trueProp, this.falseProp, this.vizProp),
				new CenteringTransform(this.falseProp, this.trueProp, this.vizProp),
				new CenteringTransform(this.trueProp, this.trueProp, this.vizProp),
		};
	}

	@After
	public void tearDown() {
		this.trueProp = null;
		this.falseProp = null;
		this.vizProp = null;
		this.viz = null;
		this.ts = null;
	}
	
	@Test
	public void isInvertedAxisX_01() {
		assertFalse(this.ts[0].isInvertedAxisX());
		assertTrue(this.ts[1].isInvertedAxisX());
		assertFalse(this.ts[2].isInvertedAxisX());
		assertTrue(this.ts[3].isInvertedAxisX());
	}

	@Test
	public void isInvertedAxisX_02() {
		this.trueProp.set(false);
		assertFalse(this.ts[0].isInvertedAxisX());
		assertFalse(this.ts[1].isInvertedAxisX());
		assertFalse(this.ts[2].isInvertedAxisX());
		assertFalse(this.ts[3].isInvertedAxisX());
	}

	@Test
	public void isInvertedAxisY_01() {
		assertFalse(this.ts[0].isInvertedAxisY());
		assertFalse(this.ts[1].isInvertedAxisY());
		assertTrue(this.ts[2].isInvertedAxisY());
		assertTrue(this.ts[3].isInvertedAxisY());
	}

	@Test
	public void isInvertedAxisY_02() {
		this.trueProp.set(false);
		assertFalse(this.ts[0].isInvertedAxisY());
		assertFalse(this.ts[1].isInvertedAxisY());
		assertFalse(this.ts[2].isInvertedAxisY());
		assertFalse(this.ts[3].isInvertedAxisY());
	}

	@Test
	public void toCenterX() {
		assertEpsilonEquals(2.5,  this.ts[0].toCenterX(5));
		assertEpsilonEquals(-2.5,  this.ts[1].toCenterX(5));
		assertEpsilonEquals(2.5,  this.ts[2].toCenterX(5));
		assertEpsilonEquals(-2.5,  this.ts[3].toCenterX(5));
	}

	@Test
	public void toCenterY() {
		assertEpsilonEquals(1,  this.ts[0].toCenterY(5));
		assertEpsilonEquals(1,  this.ts[1].toCenterY(5));
		assertEpsilonEquals(-1,  this.ts[2].toCenterY(5));
		assertEpsilonEquals(-1,  this.ts[3].toCenterY(5));
	}

	@Test
	public void toGlobalX() {
		assertEpsilonEquals(-7.5,  this.ts[0].toGlobalX(5));
		assertEpsilonEquals(2.5,  this.ts[1].toGlobalX(5));
		assertEpsilonEquals(-7.5,  this.ts[2].toGlobalX(5));
		assertEpsilonEquals(2.5,  this.ts[3].toGlobalX(5));
	}

	@Test
	public void toGlobalY() {
		assertEpsilonEquals(-9,  this.ts[0].toGlobalY(5));
		assertEpsilonEquals(-9,  this.ts[1].toGlobalY(5));
		assertEpsilonEquals(1,  this.ts[2].toGlobalY(5));
		assertEpsilonEquals(1,  this.ts[3].toGlobalY(5));
	}

}
