/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class ShapeFileIndexRecordTest extends AbstractIoShapeTest {

	private int offset, length;
	private ShapeFileIndexRecord record;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.offset = 10;
		this.length = 16;
		this.record = new ShapeFileIndexRecord(this.offset, this.length, false, -1);
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.record = null;
	}

	@Test
	public void testRecordContentLength() {
		assertEquals(
				((this.length%2)!=0 ? this.length+1 : this.length) - 8,
				this.record.getRecordContentLength());
	}

	@Test
	public void testEntireRecordLength() {
		assertEquals(
				((this.length%2)!=0 ? this.length+1 : this.length),
				this.record.getEntireRecordLength());
	}

	@Test
	public void testGetOffsetInFile() {
		assertEquals(
				((this.offset%2)!=0 ? this.offset+1 : this.offset),
				this.record.getOffsetInFile());
	}

	@Test
	public void testGetOffsetInContent() {
		assertEquals(
				((this.offset%2)!=0 ? this.offset+1 : this.offset) - 100,
				this.record.getOffsetInContent());
	}

	@Test
	public void testEqualsObject() {
		assertFalse(this.record.equals(null));
		assertFalse(this.record.equals(new Object()));
		assertTrue(this.record.equals(this.record));
		
		assertTrue(this.record.equals(new ShapeFileIndexRecord(this.offset, this.length, false, -1)));
		assertFalse(this.record.equals(new ShapeFileIndexRecord(this.offset, this.length+2, false, -1)));
		assertFalse(this.record.equals(new ShapeFileIndexRecord(this.offset, this.length-2, false, -1)));
		
		assertFalse(this.record.equals(new ShapeFileIndexRecord(this.offset+2, this.length, false, -1)));
		assertFalse(this.record.equals(new ShapeFileIndexRecord(this.offset+2, this.length+2, false, -1)));
		assertFalse(this.record.equals(new ShapeFileIndexRecord(this.offset+2, this.length-2, false, -1)));

		assertFalse(this.record.equals(new ShapeFileIndexRecord(this.offset-2, this.length, false, -1)));		
		assertFalse(this.record.equals(new ShapeFileIndexRecord(this.offset-2, this.length+2, false, -1)));
		assertFalse(this.record.equals(new ShapeFileIndexRecord(this.offset-2, this.length-2, false, -1)));
	}

	@Test
	public void testCompareTo() {
		assertStrictlyNegative(this.record.compareTo(null));
		assertZero(this.record.compareTo(this.record));
		
		assertZero(this.record.compareTo(new ShapeFileIndexRecord(this.offset, this.length, false, -1)));
		assertStrictlyNegative(this.record.compareTo(new ShapeFileIndexRecord(this.offset, this.length+2, false, -1)));
		assertStrictlyPositive(this.record.compareTo(new ShapeFileIndexRecord(this.offset, this.length-2, false, -1)));
		
		assertStrictlyNegative(this.record.compareTo(new ShapeFileIndexRecord(this.offset+2, this.length, false, -1)));
		assertStrictlyNegative(this.record.compareTo(new ShapeFileIndexRecord(this.offset+2, this.length+2, false, -1)));
		assertStrictlyNegative(this.record.compareTo(new ShapeFileIndexRecord(this.offset+2, this.length-2, false, -1)));

		assertStrictlyPositive(this.record.compareTo(new ShapeFileIndexRecord(this.offset-2, this.length, false, -1)));		
		assertStrictlyPositive(this.record.compareTo(new ShapeFileIndexRecord(this.offset-2, this.length+2, false, -1)));
		assertStrictlyPositive(this.record.compareTo(new ShapeFileIndexRecord(this.offset-2, this.length-2, false, -1)));
	}
	
}
