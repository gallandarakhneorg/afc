/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.test.geometry;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import org.arakhne.afc.math.geometry.IntersectionType;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class IntersectionTypeTest {

	@Test
	public void invertIntersectionType() {
		assertSame(IntersectionType.INSIDE, IntersectionType.invert(IntersectionType.ENCLOSING));
		//assertSame(IntersectionType.OUTSIDE, IntersectionType.invert(IntersectionType.INSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.invert(IntersectionType.OUTSIDE));
		assertSame(IntersectionType.SAME, IntersectionType.invert(IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.invert(IntersectionType.SPANNING));
	}

	@Test
	public void invert() {
		assertSame(IntersectionType.INSIDE, IntersectionType.ENCLOSING.invert());
		//assertSame(IntersectionType.OUTSIDE, IntersectionType.INSIDE.invert());
		assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.invert());
		assertSame(IntersectionType.SAME, IntersectionType.SAME.invert());
		assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.invert());
	}

	@Test
	public void orIntersectionTypeIntersectionType() {
		assertSame(IntersectionType.ENCLOSING, IntersectionType.or(IntersectionType.ENCLOSING, IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.ENCLOSING, IntersectionType.INSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.ENCLOSING, IntersectionType.OUTSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.ENCLOSING, IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.ENCLOSING, IntersectionType.SPANNING));

		assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.INSIDE, IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.INSIDE, IntersectionType.INSIDE));
		assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.INSIDE, IntersectionType.OUTSIDE));
		assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.INSIDE, IntersectionType.SAME));
		assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.INSIDE, IntersectionType.SPANNING));

		assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.OUTSIDE, IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.OUTSIDE, IntersectionType.INSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.or(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.OUTSIDE, IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.OUTSIDE, IntersectionType.SPANNING));

		assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SAME, IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.SAME, IntersectionType.INSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SAME, IntersectionType.OUTSIDE));
		assertSame(IntersectionType.SAME, IntersectionType.or(IntersectionType.SAME, IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SAME, IntersectionType.SPANNING));

		assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SPANNING, IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.SPANNING, IntersectionType.INSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SPANNING, IntersectionType.OUTSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SPANNING, IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SPANNING, IntersectionType.SPANNING));
	}

	@Test
	public void orIntersectionType() {
		assertSame(IntersectionType.ENCLOSING, IntersectionType.ENCLOSING.or(IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.ENCLOSING.or(IntersectionType.INSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.ENCLOSING.or(IntersectionType.OUTSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.ENCLOSING.or(IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.ENCLOSING.or(IntersectionType.SPANNING));

		assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.or(IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.or(IntersectionType.INSIDE));
		assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.or(IntersectionType.OUTSIDE));
		assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.or(IntersectionType.SAME));
		assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.or(IntersectionType.SPANNING));

		assertSame(IntersectionType.SPANNING, IntersectionType.OUTSIDE.or(IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.OUTSIDE.or(IntersectionType.INSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.or(IntersectionType.OUTSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.OUTSIDE.or(IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.OUTSIDE.or(IntersectionType.SPANNING));

		assertSame(IntersectionType.SPANNING, IntersectionType.SAME.or(IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.SAME.or(IntersectionType.INSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.SAME.or(IntersectionType.OUTSIDE));
		assertSame(IntersectionType.SAME, IntersectionType.SAME.or(IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.SAME.or(IntersectionType.SPANNING));

		assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.or(IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.SPANNING.or(IntersectionType.INSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.or(IntersectionType.OUTSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.or(IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.or(IntersectionType.SPANNING));
	}

	@Test
	public void andIntersectionTypeIntersectionType() {
		assertSame(IntersectionType.ENCLOSING, IntersectionType.and(IntersectionType.ENCLOSING, IntersectionType.ENCLOSING));
		assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.ENCLOSING, IntersectionType.INSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.ENCLOSING, IntersectionType.OUTSIDE));
		assertSame(IntersectionType.ENCLOSING, IntersectionType.and(IntersectionType.ENCLOSING, IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.ENCLOSING, IntersectionType.SPANNING));

		assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.INSIDE, IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.and(IntersectionType.INSIDE, IntersectionType.INSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.INSIDE, IntersectionType.OUTSIDE));
		assertSame(IntersectionType.INSIDE, IntersectionType.and(IntersectionType.INSIDE, IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.INSIDE, IntersectionType.SPANNING));

		assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.OUTSIDE, IntersectionType.ENCLOSING));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.OUTSIDE, IntersectionType.INSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.OUTSIDE, IntersectionType.SAME));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.OUTSIDE, IntersectionType.SPANNING));

		assertSame(IntersectionType.ENCLOSING, IntersectionType.and(IntersectionType.SAME, IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.and(IntersectionType.SAME, IntersectionType.INSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.SAME, IntersectionType.OUTSIDE));
		assertSame(IntersectionType.SAME, IntersectionType.and(IntersectionType.SAME, IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.SAME, IntersectionType.SPANNING));

		assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.SPANNING, IntersectionType.ENCLOSING));
		assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.SPANNING, IntersectionType.INSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.SPANNING, IntersectionType.OUTSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.SPANNING, IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.SPANNING, IntersectionType.SPANNING));
	}

	@Test
	public void andIntersectionType() {
		assertSame(IntersectionType.ENCLOSING, IntersectionType.ENCLOSING.and(IntersectionType.ENCLOSING));
		assertSame(IntersectionType.SPANNING, IntersectionType.ENCLOSING.and(IntersectionType.INSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.ENCLOSING.and(IntersectionType.OUTSIDE));
		assertSame(IntersectionType.ENCLOSING, IntersectionType.ENCLOSING.and(IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.ENCLOSING.and(IntersectionType.SPANNING));

		assertSame(IntersectionType.SPANNING, IntersectionType.INSIDE.and(IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.and(IntersectionType.INSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.INSIDE.and(IntersectionType.OUTSIDE));
		assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.and(IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.INSIDE.and(IntersectionType.SPANNING));

		assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.and(IntersectionType.ENCLOSING));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.and(IntersectionType.INSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.and(IntersectionType.OUTSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.and(IntersectionType.SAME));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.and(IntersectionType.SPANNING));

		assertSame(IntersectionType.ENCLOSING, IntersectionType.SAME.and(IntersectionType.ENCLOSING));
		assertSame(IntersectionType.INSIDE, IntersectionType.SAME.and(IntersectionType.INSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.SAME.and(IntersectionType.OUTSIDE));
		assertSame(IntersectionType.SAME, IntersectionType.SAME.and(IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.SAME.and(IntersectionType.SPANNING));

		assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.and(IntersectionType.ENCLOSING));
		assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.and(IntersectionType.INSIDE));
		assertSame(IntersectionType.OUTSIDE, IntersectionType.SPANNING.and(IntersectionType.OUTSIDE));
		assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.and(IntersectionType.SAME));
		assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.and(IntersectionType.SPANNING));
	}

}