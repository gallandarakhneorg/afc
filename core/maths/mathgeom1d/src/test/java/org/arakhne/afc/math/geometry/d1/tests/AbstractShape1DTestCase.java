/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d1.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.arakhne.afc.math.geometry.base.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.afp.Shape1afp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Test abstract shape.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
@SuppressWarnings("all")
public abstract class AbstractShape1DTestCase<
		SH extends Shape1afp<?, ?, ?, ?, ? super SG, ?>,
		SG extends Segment1D<?, ?>>
		extends AbstractMathTestCase {

	private SG segment;
	
	private SH shape;

	protected abstract SG createSegment();

	protected abstract SH createShape();

	protected SG getSG() {
		return this.segment;
	}

	protected SH getSH() {
		return this.shape;
	}
	
	@BeforeEach
	public void setUp() {
		this.segment = createSegment();
		this.shape = createShape();
	}

	@Test
	@DisplayName("getSegment")
	public void getSegment() {
		assertSame(getSG(), getSH().getSegment());
	}

	@Test
	@DisplayName("setSegment")
	public void setSegment() {
		var sg = createSegment();
		getSH().setSegment(sg);
		assertSame(sg, getSH().getSegment());
	}

	@Test
	@DisplayName("getGeomFactory")
	public void getGeomFactory() {
		assertNotNull(getSH().getGeomFactory());
	}

}
