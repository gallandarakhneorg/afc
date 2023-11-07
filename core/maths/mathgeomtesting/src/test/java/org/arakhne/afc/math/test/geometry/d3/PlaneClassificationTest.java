/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.test.geometry.d3;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.math.geometry.d3.PlaneClassification;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PlaneClassificationTest {

	@Test
	public void invert() {
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneClassification.BEHIND.invert());
		assertSame(PlaneClassification.BEHIND, PlaneClassification.IN_FRONT_OF.invert());
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.invert());
	}

	@DisplayName("!c (xtext)")
	@Test
	public void operator_not() {
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneClassification.BEHIND.operator_not());
		assertSame(PlaneClassification.BEHIND, PlaneClassification.IN_FRONT_OF.operator_not());
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.operator_not());
	}

	@DisplayName("!c (scala)")
	@Test
	public void $bang() {
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneClassification.BEHIND.$bang());
		assertSame(PlaneClassification.BEHIND, PlaneClassification.IN_FRONT_OF.$bang());
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.$bang());
	}

	@DisplayName("or(PlanarClassification)")
	@Test
	public void or() {
		assertSame(PlaneClassification.BEHIND, PlaneClassification.BEHIND.or(PlaneClassification.BEHIND));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.BEHIND.or(PlaneClassification.COINCIDENT));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneClassification.BEHIND.or(PlaneClassification.IN_FRONT_OF));

		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.or(PlaneClassification.BEHIND));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.or(PlaneClassification.COINCIDENT));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.or(PlaneClassification.IN_FRONT_OF));

		assertSame(PlaneClassification.IN_FRONT_OF, PlaneClassification.IN_FRONT_OF.or(PlaneClassification.BEHIND));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.IN_FRONT_OF.or(PlaneClassification.COINCIDENT));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneClassification.IN_FRONT_OF.or(PlaneClassification.IN_FRONT_OF));
	}

	@DisplayName("and(PlanarClassification)")
	@Test
	public void and() {
		assertSame(PlaneClassification.BEHIND, PlaneClassification.BEHIND.and(PlaneClassification.BEHIND));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.BEHIND.and(PlaneClassification.COINCIDENT));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.BEHIND.and(PlaneClassification.IN_FRONT_OF));

		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.and(PlaneClassification.BEHIND));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.and(PlaneClassification.COINCIDENT));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.and(PlaneClassification.IN_FRONT_OF));

		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.IN_FRONT_OF.and(PlaneClassification.BEHIND));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.IN_FRONT_OF.and(PlaneClassification.COINCIDENT));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneClassification.IN_FRONT_OF.and(PlaneClassification.IN_FRONT_OF));
	}

	@DisplayName("c || PlanarClassification")
	@Test
	public void operator_or() {
		assertSame(PlaneClassification.BEHIND, PlaneClassification.BEHIND.operator_or(PlaneClassification.BEHIND));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.BEHIND.operator_or(PlaneClassification.COINCIDENT));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneClassification.BEHIND.operator_or(PlaneClassification.IN_FRONT_OF));

		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.operator_or(PlaneClassification.BEHIND));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.operator_or(PlaneClassification.COINCIDENT));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.operator_or(PlaneClassification.IN_FRONT_OF));

		assertSame(PlaneClassification.IN_FRONT_OF, PlaneClassification.IN_FRONT_OF.operator_or(PlaneClassification.BEHIND));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.IN_FRONT_OF.operator_or(PlaneClassification.COINCIDENT));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneClassification.IN_FRONT_OF.operator_or(PlaneClassification.IN_FRONT_OF));
	}

	@DisplayName("c && PlanarClassification")
	@Test
	public void operator_and() {
		assertSame(PlaneClassification.BEHIND, PlaneClassification.BEHIND.operator_and(PlaneClassification.BEHIND));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.BEHIND.operator_and(PlaneClassification.COINCIDENT));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.BEHIND.operator_and(PlaneClassification.IN_FRONT_OF));

		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.operator_and(PlaneClassification.BEHIND));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.operator_and(PlaneClassification.COINCIDENT));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.COINCIDENT.operator_and(PlaneClassification.IN_FRONT_OF));

		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.IN_FRONT_OF.operator_and(PlaneClassification.BEHIND));
		assertSame(PlaneClassification.COINCIDENT, PlaneClassification.IN_FRONT_OF.operator_and(PlaneClassification.COINCIDENT));
		assertSame(PlaneClassification.IN_FRONT_OF, PlaneClassification.IN_FRONT_OF.operator_and(PlaneClassification.IN_FRONT_OF));
	}

}