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

package org.arakhne.afc.io.dbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.util.ArrayUtil;
import org.arakhne.afc.vmutil.Resources;

/** Unit test for DBaseFileFileWriter.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class DBaseFileWriterTest extends AbstractTestCase {
	
	private static final String TEST_FILENAME = "org/arakhne/afc/io/dbase/test.dbf"; //$NON-NLS-1$//"/home/sgalland/Desktop/Metro-B/donnees/Shape files/Bati/Bat_Trans.dbf";
	
	private List<AttributeProvider> list;
	
	private static InputStream openTestStream() throws IOException {
		InputStream is = Resources.getResourceAsStream(TEST_FILENAME);//new File(TEST_FILENAME).toURI().toURL();
		if (is==null) throw new IOException();
		return is;
	}
	
	private static List<AttributeProvider> readFile(InputStream inputStream) throws IOException {
		ArrayList<AttributeProvider> alist = new ArrayList<>();
		try (DBaseFileReader reader = new DBaseFileReader(inputStream)) {	
			for(AttributeProvider provider : reader) {
				alist.add(provider);
			}
		}
		return alist;
	}
	
	@Before
	public void setUp() throws Exception {
		this.list = readFile(openTestStream());
	}
	
	private static boolean sameProvider(AttributeProvider p1, AttributeProvider p2) {
		for(Attribute attr : p1.attributes()) {
			// Search for the provider
			if (!p2.hasAttribute(attr.getName())) return false;
		}
		for(Attribute attr : p2.attributes()) {
			// Search for the provider
			if (!p1.hasAttribute(attr.getName())) return false;
		}
		return true;
	}


	@After
	public void tearDown() throws Exception {
		this.list.clear();
		this.list = null;
	}
	
	private static void assertTestingInputStream(List<AttributeProvider> lst) {
		List<String> columns = new ArrayList<>();
		boolean first = true;

		for (AttributeProvider provider : lst) {
			List<String> myList = new ArrayList<>();
			
			for(Attribute attr : provider.attributes()) {
				assertNotNull(attr);
				String name = attr.getName();
				assertNotNull(name);
				assertFalse("".equals(name)); //$NON-NLS-1$
				assertTrue("for attribute '"+name+"' with type '"+ //$NON-NLS-1$ //$NON-NLS-2$
						attr.getType().toString()+"'", //$NON-NLS-1$
						(attr.isNullAllowed())||(attr.isAssigned()));
				if (first) columns.add(name);
				else myList.add(name);
			}
			
			for (String name : columns) {
				myList.remove(name);
			}
			
			assertTrue(myList.isEmpty());
		}
	}

	@Test
	public void testWrite() throws Exception {
		assertTestingInputStream(this.list);
		
		File tmpFile = File.createTempFile("dbfwriter", ".dbf");  //$NON-NLS-1$//$NON-NLS-2$
		tmpFile.deleteOnExit();
		
		try {
			// Write the file
			DBaseFileWriter writer = new DBaseFileWriter(tmpFile);
			writer.write(ArrayUtil.toArray(this.list, AttributeProvider.class));
			writer.close();
			
			// Read the content of the just written file
			List<AttributeProvider>  newContent = readFile(new FileInputStream(tmpFile));
			
			// Compare the two lists of attributes
			boolean found;
			for(AttributeProvider provider : this.list) {
				// Search for the provider
				found = false;
				for(AttributeProvider provider2 : newContent) {
					if (sameProvider(provider,provider2)) {
						newContent.remove(provider2);
						found = true;
						break;
					}
				}
				if (!found)
					fail("some data was not correctly written"); //$NON-NLS-1$
			}
			
			if (!newContent.isEmpty())
				fail("the written content has more data than the source"); //$NON-NLS-1$
		}
		finally {
			tmpFile.delete();
		}
	}



}
