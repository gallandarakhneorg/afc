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

package org.arakhne.afc.io.shape;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("ShapeMultiPatchType")
@SuppressWarnings("all")
public class ShapeMultiPatchTypeTest extends AbstractIoShapeTestCase {

	public static Stream<Arguments> generateValidCases() {
		final var arguments = new ArrayList<Arguments>();
		for (final var expected : ShapeMultiPatchType.values()) {
			arguments.add(Arguments.of(expected.partType, expected));
		}
		return arguments.stream();
	}
	
	public static Stream<Arguments> generateInvalidCases() {
		final var arguments = new ArrayList<Arguments>();
		for (var i = -5; i < 0; ++i) {
			arguments.add(Arguments.of(i));
		}
		int max = -1;
		for (final var expected : ShapeMultiPatchType.values()) {
			if (max < expected.partType) {
				max = expected.partType;
			}
		}
		for (var i = max + 1; i < 50; ++i) {
			arguments.add(Arguments.of(i));
		}
		return arguments.stream();
	}

	@DisplayName("fromESRIInteger - valid")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("generateValidCases")
	public void testFromESRIInteger_valid(Integer i, ShapeMultiPatchType expected) throws Exception {
		assertEquals(expected, ShapeMultiPatchType.fromESRIInteger(i));
	}

	@DisplayName("fromESRIInteger - invalid")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("generateInvalidCases")
	public void testFromESRIInteger_invalid(Integer i) throws Exception {
		assertThrows(ShapeFileException.class, () -> ShapeMultiPatchType.fromESRIInteger(i));
	}

}
