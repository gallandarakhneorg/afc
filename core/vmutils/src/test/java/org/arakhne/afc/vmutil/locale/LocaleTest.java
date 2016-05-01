/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * Copyright (C) 2012 Stephane GALLAND, Olivier LAMOTTE.
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

package org.arakhne.afc.vmutil.locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;

import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("static-method")
public class LocaleTest {

	private final LocaleStub tmp = new LocaleStub();
	private static final String RESOURCE1 = "org/arakhne/afc/vmutil/locale/LocaleTest"; //$NON-NLS-1$
	private static final String RESOURCE2 = "org/arakhne/afc/vmutil/locale/LocaleStub"; //$NON-NLS-1$
	private static final String NOKEY = "NOKEY"; //$NON-NLS-1$
	private static final String KEY1 = "ONE"; //$NON-NLS-1$
	private static final String KEY2 = "TWO"; //$NON-NLS-1$
	private static final String KEY3 = "THREE"; //$NON-NLS-1$
	private static final String DEFAULT = "DEFAULT"; //$NON-NLS-1$
	private static final String P1 = "P1"; //$NON-NLS-1$
	private static final String P2 = "P2"; //$NON-NLS-1$
	private static final String P3 = "P3"; //$NON-NLS-1$
	
    /**
     */
	@Test
    public void testGetStringWithDefaultFromStringStringStringStringArray() {
    	assert(this.tmp!=null);
    	assertEquals(
    			DEFAULT,
    			Locale.getStringWithDefaultFrom(RESOURCE1,NOKEY,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"ABC P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefaultFrom(RESOURCE1,KEY1,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"ABC P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefaultFrom(RESOURCE1,KEY1,DEFAULT,P3,P1,P2));
    	
    	assertEquals(
    			DEFAULT,
    			Locale.getStringWithDefaultFrom(RESOURCE2,NOKEY,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"DEF P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefaultFrom(RESOURCE2,KEY1,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"DEF P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefaultFrom(RESOURCE2,KEY1,DEFAULT,P3,P1,P2));
    }

    /**
     */
	@Test
    public void testGetStringWithDefaultFromClassLoaderStringStringStringStringArray() {
    	ClassLoader l = LocaleTest.class.getClassLoader();
    	assertEquals(
    			DEFAULT,
    			Locale.getStringWithDefaultFrom(l, RESOURCE1,NOKEY,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"ABC P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefaultFrom(l, RESOURCE1,KEY1,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"ABC P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefaultFrom(l, RESOURCE1,KEY1,DEFAULT,P3,P1,P2));
    	
    	assertEquals(
    			DEFAULT,
    			Locale.getStringWithDefaultFrom(l, RESOURCE2,NOKEY,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"DEF P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefaultFrom(l, RESOURCE2,KEY1,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"DEF P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefaultFrom(l, RESOURCE2,KEY1,DEFAULT,P3,P1,P2));
    }

    /**
     */
	@Test
    public void testGetStringFromStringStringStringArray() {
    	assertEquals(NOKEY,
    			Locale.getStringFrom(RESOURCE1,NOKEY,P1,P2,P3));
    	assertEquals(
    			"ABC P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringFrom(RESOURCE1,KEY1,P1,P2,P3));
    	assertEquals(
    			"ABC P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringFrom(RESOURCE1,KEY1,P3,P1,P2));
    	
    	assertEquals(
    			NOKEY,
    			Locale.getStringFrom(RESOURCE2,NOKEY,P1,P2,P3));
    	assertEquals(
    			"DEF P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringFrom(RESOURCE2,KEY1,P1,P2,P3));
    	assertEquals(
    			"DEF P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringFrom(RESOURCE2,KEY1,P3,P1,P2));
    }
    
    /**
     */
	@Test
    public void testGetStringFromClassLoaderStringStringStringArray() {
    	ClassLoader l = LocaleTest.class.getClassLoader();
    	
    	assertEquals(NOKEY,
    			Locale.getStringFrom(l, RESOURCE1,NOKEY,P1,P2,P3));
    	assertEquals(
    			"ABC P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringFrom(l, RESOURCE1,KEY1,P1,P2,P3));
    	assertEquals(
    			"ABC P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringFrom(l, RESOURCE1,KEY1,P3,P1,P2));
    	
    	assertEquals(
    			NOKEY,
    			Locale.getStringFrom(l, RESOURCE2,NOKEY,P1,P2,P3));
    	assertEquals(
    			"DEF P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringFrom(l, RESOURCE2,KEY1,P1,P2,P3));
    	assertEquals(
    			"DEF P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringFrom(l, RESOURCE2,KEY1,P3,P1,P2));
    }

    /**
     */
	@Test
    public void testGetStringClassStringStringArray() {
    	assertEquals(NOKEY,
    			Locale.getString(LocaleTest.class,NOKEY,P1,P2,P3));
    	assertEquals(
    			"ABC P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getString(LocaleTest.class,KEY1,P1,P2,P3));
    	assertEquals(
    			"ABC P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getString(LocaleTest.class,KEY1,P3,P1,P2));
    	
    	assertEquals(
    			NOKEY,
    			Locale.getString(LocaleStub.class,NOKEY,P1,P2,P3));
    	assertEquals(
    			"DEF P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getString(LocaleStub.class,KEY1,P1,P2,P3));
    	assertEquals(
    			"DEF P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getString(LocaleStub.class,KEY1,P3,P1,P2));
    }
    
    /**
     */
	@Test
    public void testGetStringClassLoaderClassStringStringArray() {
    	ClassLoader l = LocaleTest.class.getClassLoader();
    	
    	assertEquals(NOKEY,
    			Locale.getString(l, LocaleTest.class,NOKEY,P1,P2,P3));
    	assertEquals(
    			"ABC P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getString(l, LocaleTest.class,KEY1,P1,P2,P3));
    	assertEquals(
    			"ABC P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getString(l, LocaleTest.class,KEY1,P3,P1,P2));
    	
    	assertEquals(
    			NOKEY,
    			Locale.getString(l, LocaleStub.class,NOKEY,P1,P2,P3));
    	assertEquals(
    			"DEF P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getString(l, LocaleStub.class,KEY1,P1,P2,P3));
    	assertEquals(
    			"DEF P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getString(l, LocaleStub.class,KEY1,P3,P1,P2));
    }

    /**
     */
	@Test
    public void testGetStringWithDefaultClassStringStringStringArray() {
    	assertEquals(
    			DEFAULT,
    			Locale.getStringWithDefault(LocaleTest.class,NOKEY,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"ABC P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefault(LocaleTest.class,KEY1,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"ABC P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefault(LocaleTest.class,KEY1,DEFAULT,P3,P1,P2));
    	
    	assertEquals(
    			DEFAULT,
    			Locale.getStringWithDefault(LocaleStub.class,NOKEY,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"DEF P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefault(LocaleStub.class,KEY1,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"DEF P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefault(LocaleStub.class,KEY1,DEFAULT,P3,P1,P2));
    }

    /**
     */
	@Test
    public void testGetStringWithDefaultClassLoaderClassStringStringStringArray() {
    	ClassLoader l = LocaleTest.class.getClassLoader();
    	
    	assertEquals(
    			DEFAULT,
    			Locale.getStringWithDefault(l, LocaleTest.class,NOKEY,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"ABC P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefault(l, LocaleTest.class,KEY1,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"ABC P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefault(l, LocaleTest.class,KEY1,DEFAULT,P3,P1,P2));
    	
    	assertEquals(
    			DEFAULT,
    			Locale.getStringWithDefault(l, LocaleStub.class,NOKEY,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"DEF P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefault(l, LocaleStub.class,KEY1,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"DEF P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefault(l, LocaleStub.class,KEY1,DEFAULT,P3,P1,P2));
    }

    /**
     */
	@Test
    public void testGetStringStringStringArray() {
    	assertEquals(
    			NOKEY,
    			Locale.getString(NOKEY,P1,P2,P3));
    	assertEquals(
    			"ABC P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getString(KEY1,P1,P2,P3));
    	assertEquals(
    			"ABC P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getString(KEY1,P3,P1,P2));
    	try {
    		Locale.getString(KEY2);
    		fail("expecting IllegalArgumentException"); //$NON-NLS-1$
    	}
    	catch(IllegalArgumentException exception) {
    		// expected exception
    	}
    	assertEquals(
    			"(d92b87b0-efe9-4dd9-903f-7c994b8e2a9f)", //$NON-NLS-1$
    			Locale.getString(KEY3));
    }
    
    /**
     */
	@Test
    public void testGetStringClassLoaderStringStringArray() {
    	ClassLoader l = LocaleTest.class.getClassLoader();
    	
    	assertEquals(
    			NOKEY,
    			Locale.getString(l, NOKEY,P1,P2,P3));
    	assertEquals(
    			"ABC P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getString(l, KEY1,P1,P2,P3));
    	assertEquals(
    			"ABC P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getString(l, KEY1,P3,P1,P2));
    	try {
    		Locale.getString(l, KEY2);
    		fail("expecting IllegalArgumentException"); //$NON-NLS-1$
    	}
    	catch(IllegalArgumentException exception) {
    		// expected exception
    	}
    	assertEquals(
    			"(d92b87b0-efe9-4dd9-903f-7c994b8e2a9f)", //$NON-NLS-1$
    			Locale.getString(l, KEY3));
    }

    /**
     */
	@Test
    public void testGetStringWithDefaultStringStringStringArray() {
    	assertEquals(
    			DEFAULT,
    			Locale.getStringWithDefault(NOKEY,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"ABC P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefault(KEY1,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"ABC P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefault(KEY1,DEFAULT,P3,P1,P2));
    	try {
    		Locale.getStringWithDefault(KEY2,DEFAULT);
    		fail("expecting IllegalArgumentException"); //$NON-NLS-1$
    	}
    	catch(IllegalArgumentException exception) {
    		//expected exception
    	}
    	assertEquals(
    			"(d92b87b0-efe9-4dd9-903f-7c994b8e2a9f)", //$NON-NLS-1$
    			Locale.getStringWithDefault(KEY3,DEFAULT));
    }

    /**
     */
	@Test
    public void testGetStringWithDefaultClassLoaderStringStringStringArray() {
    	ClassLoader l = LocaleTest.class.getClassLoader();
    	
    	assertEquals(
    			DEFAULT,
    			Locale.getStringWithDefault(l, NOKEY,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"ABC P1 'P2' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefault(l, KEY1,DEFAULT,P1,P2,P3));
    	assertEquals(
    			"ABC P3 'P1' {2}", //$NON-NLS-1$
    			Locale.getStringWithDefault(l, KEY1,DEFAULT,P3,P1,P2));
    	try {
    		Locale.getStringWithDefault(l, KEY2, DEFAULT);
    		fail("expecting IllegalArgumentException"); //$NON-NLS-1$
    	}
    	catch(IllegalArgumentException exception) {
    		// expected exception
    	}
    	assertEquals(
    			"(d92b87b0-efe9-4dd9-903f-7c994b8e2a9f)", //$NON-NLS-1$
    			Locale.getStringWithDefault(l, KEY3, DEFAULT));
    }
    
    /**
     */
	@Test
    public void testGetStringWithRawFormatStyle() {
    	Number data;
    	String raw, localized;
    	
    	data = Double.valueOf(123.456);
    	raw = Double.toString(123.456);
    	localized = MessageFormat.format("{0}", data); //$NON-NLS-1$
    	assertEquals(
    			localized+" "+raw, //$NON-NLS-1$
    			Locale.getString("FOUR", data)); //$NON-NLS-1$

    	data = Double.valueOf(-123.456);
    	raw = Double.toString(-123.456);
    	localized = MessageFormat.format("{0}", data); //$NON-NLS-1$
    	assertEquals(
    			localized+" "+raw, //$NON-NLS-1$
    			Locale.getString("FOUR", data)); //$NON-NLS-1$

    	data = Long.valueOf(123456);
    	raw = Long.toString(123456);
    	localized = MessageFormat.format("{0}", data); //$NON-NLS-1$
    	assertEquals(
    			localized+" "+raw, //$NON-NLS-1$
    			Locale.getString("FOUR", data)); //$NON-NLS-1$

    	data = Long.valueOf(-123456);
    	raw = Long.toString(-123456);
    	localized = MessageFormat.format("{0}", data); //$NON-NLS-1$
    	assertEquals(
    			localized+" "+raw, //$NON-NLS-1$
    			Locale.getString("FOUR", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)123);
    	raw = Short.toString((short)123);
    	localized = MessageFormat.format("{0}", data); //$NON-NLS-1$
    	assertEquals(
    			localized+" "+raw, //$NON-NLS-1$
    			Locale.getString("FOUR", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)-123);
    	raw = Short.toString((short)-123);
    	localized = MessageFormat.format("{0}", data); //$NON-NLS-1$
    	assertEquals(
    			localized+" "+raw, //$NON-NLS-1$
    			Locale.getString("FOUR", data)); //$NON-NLS-1$

    	raw = "123456789123456789123456789.123456789"; //$NON-NLS-1$
    	data = new BigDecimal(raw);
    	localized = MessageFormat.format("{0}", data); //$NON-NLS-1$
    	assertEquals(
    			localized+" "+raw, //$NON-NLS-1$
    			Locale.getString("FOUR", data)); //$NON-NLS-1$

    	raw = "-123456789123456789123456789.123456789"; //$NON-NLS-1$
    	data = new BigDecimal(raw);
    	localized = MessageFormat.format("{0}", data); //$NON-NLS-1$
    	assertEquals(
    			localized+" "+raw, //$NON-NLS-1$
    			Locale.getString("FOUR", data)); //$NON-NLS-1$

    	raw = "123456789123456789123456789123456789"; //$NON-NLS-1$
    	data = new BigInteger(raw);
    	localized = MessageFormat.format("{0}", data); //$NON-NLS-1$
    	assertEquals(
    			localized+" "+raw, //$NON-NLS-1$
    			Locale.getString("FOUR", data)); //$NON-NLS-1$

    	raw = "-123456789123456789123456789123456789"; //$NON-NLS-1$
    	data = new BigInteger(raw);
    	localized = MessageFormat.format("{0}", data); //$NON-NLS-1$
    	assertEquals(
    			localized+" "+raw, //$NON-NLS-1$
    			Locale.getString("FOUR", data)); //$NON-NLS-1$
    }
    
    /**
     */
	@Test
    public void testGetStringWithRawFormatStyleWithFormat1() {
    	Number data;
    	
    	data = Double.valueOf(123.456);
    	assertEquals(
    			"123", //$NON-NLS-1$
    			Locale.getString("FORMAT_1", data)); //$NON-NLS-1$

    	data = Double.valueOf(-123.456);
    	assertEquals(
    			"-123", //$NON-NLS-1$
    			Locale.getString("FORMAT_1", data)); //$NON-NLS-1$

    	data = Long.valueOf(123456);
    	assertEquals(
    			"123456", //$NON-NLS-1$
    			Locale.getString("FORMAT_1", data)); //$NON-NLS-1$

    	data = Long.valueOf(-123456);
    	assertEquals(
    			"-123456", //$NON-NLS-1$
    			Locale.getString("FORMAT_1", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)123);
    	assertEquals(
    			"123", //$NON-NLS-1$
    			Locale.getString("FORMAT_1", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)-123);
    	assertEquals(
    			"-123", //$NON-NLS-1$
    			Locale.getString("FORMAT_1", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_1", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456790", //$NON-NLS-1$
    			Locale.getString("FORMAT_1", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_1", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456790", //$NON-NLS-1$
    			Locale.getString("FORMAT_1", data)); //$NON-NLS-1$

    	data = new BigInteger("123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_1", data)); //$NON-NLS-1$

    	data = new BigInteger("-123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_1", data)); //$NON-NLS-1$
    }
 
    /**
     */
	@Test
    public void testGetStringWithRawFormatStyleWithFormat2() {
    	Number data;
    	
    	data = Double.valueOf(123.456);
    	assertEquals(
    			"123", //$NON-NLS-1$
    			Locale.getString("FORMAT_2", data)); //$NON-NLS-1$

    	data = Double.valueOf(-123.456);
    	assertEquals(
    			"-123", //$NON-NLS-1$
    			Locale.getString("FORMAT_2", data)); //$NON-NLS-1$

    	data = Long.valueOf(123456);
    	assertEquals(
    			"123456", //$NON-NLS-1$
    			Locale.getString("FORMAT_2", data)); //$NON-NLS-1$

    	data = Long.valueOf(-123456);
    	assertEquals(
    			"-123456", //$NON-NLS-1$
    			Locale.getString("FORMAT_2", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)123);
    	assertEquals(
    			"123", //$NON-NLS-1$
    			Locale.getString("FORMAT_2", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)-123);
    	assertEquals(
    			"-123", //$NON-NLS-1$
    			Locale.getString("FORMAT_2", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_2", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456790", //$NON-NLS-1$
    			Locale.getString("FORMAT_2", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_2", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456790", //$NON-NLS-1$
    			Locale.getString("FORMAT_2", data)); //$NON-NLS-1$

    	data = new BigInteger("123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_2", data)); //$NON-NLS-1$

    	data = new BigInteger("-123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_2", data)); //$NON-NLS-1$
    }
   
    /**
     */
	@Test
    public void testGetStringWithRawFormatStyleWithFormat3() {
    	Number data;
    	
    	data = Double.valueOf(123.456);
    	assertEquals(
    			"123.5", //$NON-NLS-1$
    			Locale.getString("FORMAT_3", data)); //$NON-NLS-1$

    	data = Double.valueOf(-123.456);
    	assertEquals(
    			"-123.5", //$NON-NLS-1$
    			Locale.getString("FORMAT_3", data)); //$NON-NLS-1$

    	data = Long.valueOf(123456);
    	assertEquals(
    			"123456", //$NON-NLS-1$
    			Locale.getString("FORMAT_3", data)); //$NON-NLS-1$

    	data = Long.valueOf(-123456);
    	assertEquals(
    			"-123456", //$NON-NLS-1$
    			Locale.getString("FORMAT_3", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)123);
    	assertEquals(
    			"123", //$NON-NLS-1$
    			Locale.getString("FORMAT_3", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)-123);
    	assertEquals(
    			"-123", //$NON-NLS-1$
    			Locale.getString("FORMAT_3", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789.1", //$NON-NLS-1$
    			Locale.getString("FORMAT_3", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456790.0", //$NON-NLS-1$
    			Locale.getString("FORMAT_3", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789.1", //$NON-NLS-1$
    			Locale.getString("FORMAT_3", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456790.0", //$NON-NLS-1$
    			Locale.getString("FORMAT_3", data)); //$NON-NLS-1$

    	data = new BigInteger("123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_3", data)); //$NON-NLS-1$

    	data = new BigInteger("-123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_3", data)); //$NON-NLS-1$
    }

    /**
     */
	@Test
    public void testGetStringWithRawFormatStyleWithFormat4() {
    	Number data;
    	
    	data = Double.valueOf(123.456);
    	assertEquals(
    			"123.46", //$NON-NLS-1$
    			Locale.getString("FORMAT_4", data)); //$NON-NLS-1$

    	data = Double.valueOf(-123.456);
    	assertEquals(
    			"-123.46", //$NON-NLS-1$
    			Locale.getString("FORMAT_4", data)); //$NON-NLS-1$

    	data = Long.valueOf(123456);
    	assertEquals(
    			"123456.0", //$NON-NLS-1$
    			Locale.getString("FORMAT_4", data)); //$NON-NLS-1$

    	data = Long.valueOf(-123456);
    	assertEquals(
    			"-123456.0", //$NON-NLS-1$
    			Locale.getString("FORMAT_4", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)123);
    	assertEquals(
    			"123.0", //$NON-NLS-1$
    			Locale.getString("FORMAT_4", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)-123);
    	assertEquals(
    			"-123.0", //$NON-NLS-1$
    			Locale.getString("FORMAT_4", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789.12", //$NON-NLS-1$
    			Locale.getString("FORMAT_4", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789.99", //$NON-NLS-1$
    			Locale.getString("FORMAT_4", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789.12", //$NON-NLS-1$
    			Locale.getString("FORMAT_4", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789.99", //$NON-NLS-1$
    			Locale.getString("FORMAT_4", data)); //$NON-NLS-1$

    	data = new BigInteger("123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789123456789.0", //$NON-NLS-1$
    			Locale.getString("FORMAT_4", data)); //$NON-NLS-1$

    	data = new BigInteger("-123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789123456789.0", //$NON-NLS-1$
    			Locale.getString("FORMAT_4", data)); //$NON-NLS-1$
    }
    
    /**
     */
	@Test
    public void testGetStringWithRawFormatStyleWithFormat5() {
    	Number data;
    	
    	data = Double.valueOf(123.456);
    	assertEquals(
    			"123.456", //$NON-NLS-1$
    			Locale.getString("FORMAT_5", data)); //$NON-NLS-1$

    	data = Double.valueOf(-123.456);
    	assertEquals(
    			"-123.456", //$NON-NLS-1$
    			Locale.getString("FORMAT_5", data)); //$NON-NLS-1$

    	data = Long.valueOf(123456);
    	assertEquals(
    			"123456", //$NON-NLS-1$
    			Locale.getString("FORMAT_5", data)); //$NON-NLS-1$

    	data = Long.valueOf(-123456);
    	assertEquals(
    			"-123456", //$NON-NLS-1$
    			Locale.getString("FORMAT_5", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)123);
    	assertEquals(
    			"123", //$NON-NLS-1$
    			Locale.getString("FORMAT_5", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)-123);
    	assertEquals(
    			"-123", //$NON-NLS-1$
    			Locale.getString("FORMAT_5", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789.123", //$NON-NLS-1$
    			Locale.getString("FORMAT_5", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789.988", //$NON-NLS-1$
    			Locale.getString("FORMAT_5", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789.123", //$NON-NLS-1$
    			Locale.getString("FORMAT_5", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789.988", //$NON-NLS-1$
    			Locale.getString("FORMAT_5", data)); //$NON-NLS-1$

    	data = new BigInteger("123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_5", data)); //$NON-NLS-1$

    	data = new BigInteger("-123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_5", data)); //$NON-NLS-1$
    }
    
    /**
     */
	@Test
    public void testGetStringWithRawFormatStyleWithFormat6() {
    	Number data;
    	
    	data = Double.valueOf(123.456);
    	assertEquals(
    			"123", //$NON-NLS-1$
    			Locale.getString("FORMAT_6", data)); //$NON-NLS-1$

    	data = Double.valueOf(-123.456);
    	assertEquals(
    			"-123", //$NON-NLS-1$
    			Locale.getString("FORMAT_6", data)); //$NON-NLS-1$

    	data = Long.valueOf(123456);
    	assertEquals(
    			"123456", //$NON-NLS-1$
    			Locale.getString("FORMAT_6", data)); //$NON-NLS-1$

    	data = Long.valueOf(-123456);
    	assertEquals(
    			"-123456", //$NON-NLS-1$
    			Locale.getString("FORMAT_6", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)123);
    	assertEquals(
    			"123", //$NON-NLS-1$
    			Locale.getString("FORMAT_6", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)-123);
    	assertEquals(
    			"-123", //$NON-NLS-1$
    			Locale.getString("FORMAT_6", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_6", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456790", //$NON-NLS-1$
    			Locale.getString("FORMAT_6", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_6", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456790", //$NON-NLS-1$
    			Locale.getString("FORMAT_6", data)); //$NON-NLS-1$

    	data = new BigInteger("123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_6", data)); //$NON-NLS-1$

    	data = new BigInteger("-123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_6", data)); //$NON-NLS-1$
    }
    
    /**
     */
	@Test
    public void testGetStringWithRawFormatStyleWithFormat7() {
    	Number data;
    	
    	data = Double.valueOf(123.456);
    	assertEquals(
    			"123", //$NON-NLS-1$
    			Locale.getString("FORMAT_7", data)); //$NON-NLS-1$

    	data = Double.valueOf(-123.456);
    	assertEquals(
    			"-123", //$NON-NLS-1$
    			Locale.getString("FORMAT_7", data)); //$NON-NLS-1$

    	data = Long.valueOf(123456);
    	assertEquals(
    			"123456", //$NON-NLS-1$
    			Locale.getString("FORMAT_7", data)); //$NON-NLS-1$

    	data = Long.valueOf(-123456);
    	assertEquals(
    			"-123456", //$NON-NLS-1$
    			Locale.getString("FORMAT_7", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)123);
    	assertEquals(
    			"123", //$NON-NLS-1$
    			Locale.getString("FORMAT_7", data)); //$NON-NLS-1$

    	data = Short.valueOf((short)-123);
    	assertEquals(
    			"-123", //$NON-NLS-1$
    			Locale.getString("FORMAT_7", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_7", data)); //$NON-NLS-1$

    	data = new BigDecimal("123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456790", //$NON-NLS-1$
    			Locale.getString("FORMAT_7", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_7", data)); //$NON-NLS-1$

    	data = new BigDecimal("-123456789123456789123456789.987654321"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456790", //$NON-NLS-1$
    			Locale.getString("FORMAT_7", data)); //$NON-NLS-1$

    	data = new BigInteger("123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"123456789123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_7", data)); //$NON-NLS-1$

    	data = new BigInteger("-123456789123456789123456789123456789"); //$NON-NLS-1$
    	assertEquals(
    			"-123456789123456789123456789123456789", //$NON-NLS-1$
    			Locale.getString("FORMAT_7", data)); //$NON-NLS-1$
    }

}