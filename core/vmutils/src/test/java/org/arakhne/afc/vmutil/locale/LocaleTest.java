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

package org.arakhne.afc.vmutil.locale;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Locale")
@SuppressWarnings("all")
public class LocaleTest {

	private final LocaleStub tmp = new LocaleStub();
	private static final String RESOURCE1 = "org/arakhne/afc/vmutil/locale/LocaleTest";  //$NON-NLS-1$
	private static final String RESOURCE2 = "org/arakhne/afc/vmutil/locale/LocaleStub";  //$NON-NLS-1$
	private static final String NOKEY = "NOKEY";  //$NON-NLS-1$
	private static final String KEY1 = "ONE";  //$NON-NLS-1$
	private static final String KEY2 = "TWO";  //$NON-NLS-1$
	private static final String KEY3 = "THREE";  //$NON-NLS-1$
	private static final String DEFAULT = "DEFAULT";  //$NON-NLS-1$
	private static final String P1 = "P1";  //$NON-NLS-1$
	private static final String P2 = "P2";  //$NON-NLS-1$
	private static final String P3 = "P3";  //$NON-NLS-1$
	
	@DisplayName("getStringWithDefaultFrom")
	@Nested
	public class GetStringWithDefaultFrom {

		@DisplayName("(String,String,String,String...) #1")
		@Test
	    public void getStringWithDefaultFromStringStringStringStringArray_1() {
	    	assertEquals(
	    			DEFAULT,
	    			Locale.getStringWithDefaultFrom(RESOURCE1,NOKEY,DEFAULT,P1,P2,P3));
		}

		@DisplayName("(String,String,String,String...) #2")
		@Test
	    public void getStringWithDefaultFromStringStringStringStringArray_2() {
	    	assertEquals(
	    			"ABC P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefaultFrom(RESOURCE1,KEY1,DEFAULT,P1,P2,P3));
		}

		@DisplayName("(String,String,String,String...) #3")
		@Test
	    public void getStringWithDefaultFromStringStringStringStringArray_3() {
	    	assertEquals(
	    			"ABC P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefaultFrom(RESOURCE1,KEY1,DEFAULT,P3,P1,P2));
		}

		@DisplayName("(String,String,String,String...) #4")
		@Test
	    public void getStringWithDefaultFromStringStringStringStringArray_4() {
	    	assertEquals(
	    			DEFAULT,
	    			Locale.getStringWithDefaultFrom(RESOURCE2,NOKEY,DEFAULT,P1,P2,P3));
		}

		@DisplayName("(String,String,String,String...) #5")
		@Test
	    public void getStringWithDefaultFromStringStringStringStringArray_5() {
	    	assertEquals(
	    			"DEF P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefaultFrom(RESOURCE2,KEY1,DEFAULT,P1,P2,P3));
		}

		@DisplayName("(String,String,String,String...) #6")
		@Test
	    public void getStringWithDefaultFromStringStringStringStringArray_6() {
	    	assertEquals(
	    			"DEF P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefaultFrom(RESOURCE2,KEY1,DEFAULT,P3,P1,P2));
		}

		@DisplayName("(String,String,String,String...) #7")
		@Test
	    public void getStringWithDefaultFromStringStringStringStringArray_7() {
	    	assertInlineParameterUsage(Locale.class, "getStringWithDefaultFrom", //$NON-NLS-1$
	    			String.class, String.class, String.class, Object[].class);
	    }

		@Test
		@DisplayName("(ClassLoader,String,String,String,String...) #1")
	    public void getStringWithDefaultFromClassLoaderStringStringStringStringArray_1() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			DEFAULT,
	    			Locale.getStringWithDefaultFrom(l, RESOURCE1,NOKEY,DEFAULT,P1,P2,P3));
	    }

		@Test
		@DisplayName("(ClassLoader,String,String,String,String...) #2")
	    public void getStringWithDefaultFromClassLoaderStringStringStringStringArray_2() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"ABC P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefaultFrom(l, RESOURCE1,KEY1,DEFAULT,P1,P2,P3));
	    }

		@Test
		@DisplayName("(ClassLoader,String,String,String,String...) #3")
	    public void getStringWithDefaultFromClassLoaderStringStringStringStringArray_3() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"ABC P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefaultFrom(l, RESOURCE1,KEY1,DEFAULT,P3,P1,P2));
	    }

		@Test
		@DisplayName("(ClassLoader,String,String,String,String...) #4")
	    public void getStringWithDefaultFromClassLoaderStringStringStringStringArray_4() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			DEFAULT,
	    			Locale.getStringWithDefaultFrom(l, RESOURCE2,NOKEY,DEFAULT,P1,P2,P3));
	    }

		@Test
		@DisplayName("(ClassLoader,String,String,String,String...) #5")
	    public void getStringWithDefaultFromClassLoaderStringStringStringStringArray_5() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"DEF P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefaultFrom(l, RESOURCE2,KEY1,DEFAULT,P1,P2,P3));
	    }

		@Test
		@DisplayName("(ClassLoader,String,String,String,String...) #6")
	    public void getStringWithDefaultFromClassLoaderStringStringStringStringArray_6() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"DEF P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefaultFrom(l, RESOURCE2,KEY1,DEFAULT,P3,P1,P2));
	    }
	}

	@DisplayName("getStringFrom")
	@Nested
	public class GetStringFrom {

		@Test
		@DisplayName("(String, String, Object...) #1")
	    public void getStringFromStringStringObjectArray_1() {
	    	assertEquals(NOKEY,
	    			Locale.getStringFrom(RESOURCE1,NOKEY,P1,P2,P3));
		}

		@Test
		@DisplayName("(String, String, Object...) #2")
	    public void getStringFromStringStringObjectArray_2() {
	    	assertEquals(
	    			"ABC P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringFrom(RESOURCE1,KEY1,P1,P2,P3));
		}

		@Test
		@DisplayName("(String, String, Object...) #3")
	    public void getStringFromStringStringObjectArray_3() {
	    	assertEquals(
	    			"ABC P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringFrom(RESOURCE1,KEY1,P3,P1,P2));
		}

		@Test
		@DisplayName("(String, String, Object...) #4")
	    public void getStringFromStringStringObjectArray_4() {
	    	assertEquals(
	    			NOKEY,
	    			Locale.getStringFrom(RESOURCE2,NOKEY,P1,P2,P3));
		}

		@Test
		@DisplayName("(String, String, Object...) #5")
	    public void getStringFromStringStringObjectArray_5() {
	    	assertEquals(
	    			"DEF P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringFrom(RESOURCE2,KEY1,P1,P2,P3));
		}

		@Test
		@DisplayName("(String, String, Object...) #6")
	    public void getStringFromStringStringObjectArray_6() {
	    	assertEquals(
	    			"DEF P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringFrom(RESOURCE2,KEY1,P3,P1,P2));
		}

		@Test
		@DisplayName("(String, String, Object...) #7")
	    public void getStringFromStringStringObjectArray_7() {
	    	assertInlineParameterUsage(Locale.class, "getStringFrom", String.class, String.class, Object[].class); //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(ClassLoader, String, String, Object...) #1")
	    public void getStringFromClassLoaderStringStringObjectArray_1() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(NOKEY,
	    			Locale.getStringFrom(l, RESOURCE1,NOKEY,P1,P2,P3));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, String, String, Object...) #2")
	    public void getStringFromClassLoaderStringStringObjectArray_2() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"ABC P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringFrom(l, RESOURCE1,KEY1,P1,P2,P3));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, String, String, Object...) #3")
	    public void getStringFromClassLoaderStringStringObjectArray_3() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"ABC P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringFrom(l, RESOURCE1,KEY1,P3,P1,P2));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, String, String, Object...) #4")
	    public void getStringFromClassLoaderStringStringObjectArray_4() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			NOKEY,
	    			Locale.getStringFrom(l, RESOURCE2,NOKEY,P1,P2,P3));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, String, String, Object...) #5")
	    public void getStringFromClassLoaderStringStringObjectArray_5() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"DEF P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringFrom(l, RESOURCE2,KEY1,P1,P2,P3));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, String, String, Object...) #6")
	    public void getStringFromClassLoaderStringStringObjectArray_6() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"DEF P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringFrom(l, RESOURCE2,KEY1,P3,P1,P2));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, String, String, Object...) #7")
	    public void getStringFromClassLoaderStringStringObjectArray_7() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertInlineParameterUsage(Locale.class, "getStringFrom", ClassLoader.class, String.class, String.class, Object[].class); //$NON-NLS-1$
		}
	}

	@DisplayName("getString")
	@Nested
	public class GetString {

		@Test
		@DisplayName("(Class, String, Object...) #1")
	    public void getStringClassStringObjectArray_1() {
	    	assertEquals(NOKEY,
	    			Locale.getString(LocaleTest.class,NOKEY,P1,P2,P3));
		}

		@Test
		@DisplayName("(Class, String, Object...) #2")
	    public void getStringClassStringObjectArray_2() {
	    	assertEquals(
	    			"ABC P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getString(LocaleTest.class,KEY1,P1,P2,P3));
		}

		@Test
		@DisplayName("(Class, String, Object...) #3")
	    public void getStringClassStringObjectArray_3() {
	    	assertEquals(
	    			"ABC P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getString(LocaleTest.class,KEY1,P3,P1,P2));
		}

		@Test
		@DisplayName("(Class, String, Object...) #4")
	    public void getStringClassStringObjectArray_4() {
	    	assertEquals(
	    			NOKEY,
	    			Locale.getString(LocaleStub.class,NOKEY,P1,P2,P3));
		}

		@Test
		@DisplayName("(Class, String, Object...) #5")
	    public void getStringClassStringObjectArray_5() {
	    	assertEquals(
	    			"DEF P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getString(LocaleStub.class,KEY1,P1,P2,P3));
		}

		@Test
		@DisplayName("(Class, String, Object...) #6")
	    public void getStringClassStringObjectArray_6() {
	    	assertEquals(
	    			"DEF P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getString(LocaleStub.class,KEY1,P3,P1,P2));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, Class, String, Object...) #1")
	    public void getStringClassLoaderClassStringObjectArray_1() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(NOKEY,
	    			Locale.getString(l, LocaleTest.class,NOKEY,P1,P2,P3));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, Class, String, Object...) #2")
	    public void getStringClassLoaderClassStringObjectArray_2() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"ABC P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getString(l, LocaleTest.class,KEY1,P1,P2,P3));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, Class, String, Object...) #3")
	    public void getStringClassLoaderClassStringObjectArray_3() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"ABC P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getString(l, LocaleTest.class,KEY1,P3,P1,P2));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, Class, String, Object...) #4")
	    public void getStringClassLoaderClassStringObjectArray_4() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			NOKEY,
	    			Locale.getString(l, LocaleStub.class,NOKEY,P1,P2,P3));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, Class, String, Object...) #5")
	    public void getStringClassLoaderClassStringObjectArray_5() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"DEF P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getString(l, LocaleStub.class,KEY1,P1,P2,P3));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, Class, String, Object...) #6")
	    public void getStringClassLoaderClassStringObjectArray_6() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"DEF P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getString(l, LocaleStub.class,KEY1,P3,P1,P2));
	    }
		
		@Test
		@DisplayName("(String, Object...) #1")
	    public void getStringStringObjectArray_1() {
	    	assertEquals(
	    			NOKEY,
	    			Locale.getString(NOKEY,P1,P2,P3));
	    }
		
		@Test
		@DisplayName("(String, Object...) #2")
	    public void getStringStringObjectArray_2() {
	    	assertEquals(
	    			"ABC P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getString(KEY1,P1,P2,P3));
	    }
		
		@Test
		@DisplayName("(String, Object...) #3")
	    public void getStringStringObjectArray_3() {
	    	assertEquals(
	    			"ABC P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getString(KEY1,P3,P1,P2));
	    }
		
		@Test
		@DisplayName("(String, Object...) #4")
	    public void getStringStringObjectArray_4() {
	    	try {
	    		Locale.getString(KEY2);
	    		fail("expecting IllegalArgumentException");  //$NON-NLS-1$
	    	}
	    	catch(IllegalArgumentException exception) {
	    		// expected exception
	    	}
	    }
		
		@Test
		@DisplayName("(String, Object...) #5")
	    public void getStringStringObjectArray_5() {
	    	assertEquals(
	    			"(d92b87b0-efe9-4dd9-903f-7c994b8e2a9f)",  //$NON-NLS-1$
	    			Locale.getString(KEY3));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, String, Object...) #1")
	    public void getStringClassLoaderStringObjectArray_1() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			NOKEY,
	    			Locale.getString(l, NOKEY,P1,P2,P3));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, String, Object...) #2")
	    public void getStringClassLoaderStringObjectArray_2() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"ABC P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getString(l, KEY1,P1,P2,P3));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, String, Object...) #3")
	    public void getStringClassLoaderStringObjectArray_3() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"ABC P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getString(l, KEY1,P3,P1,P2));
	    }
	    
		@Test
		@DisplayName("(ClassLoader, String, Object...) #4")
	    public void getStringClassLoaderStringObjectArray_4() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	try {
	    		Locale.getString(l, KEY2);
	    		fail("expecting IllegalArgumentException");  //$NON-NLS-1$
	    	}
	    	catch(IllegalArgumentException exception) {
	    		// expected exception
	    	}
	    }
	    
		@Test
		@DisplayName("(ClassLoader, String, Object...) #5")
	    public void getStringClassLoaderStringObjectArray_5() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"(d92b87b0-efe9-4dd9-903f-7c994b8e2a9f)",  //$NON-NLS-1$
	    			Locale.getString(l, KEY3));
	    }

		@Test
		@DisplayName("(\"{0} {0,number,raw}\", Object...) #1")
	    public void getStringWithRawFormatStyle_1() {
	    	var data = Double.valueOf(123.456);
	    	var raw = Double.toString(123.456);
	    	var localized = MessageFormat.format("{0}", data);  //$NON-NLS-1$
	    	assertEquals(
	    			localized+" "+raw,  //$NON-NLS-1$
	    			Locale.getString("FOUR", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0} {0,number,raw}\", Object...) #2")
	    public void getStringWithRawFormatStyle_2() {
	    	var data = Double.valueOf(-123.456);
	    	var raw = Double.toString(-123.456);
	    	var localized = MessageFormat.format("{0}", data);  //$NON-NLS-1$
	    	assertEquals(
	    			localized+" "+raw,  //$NON-NLS-1$
	    			Locale.getString("FOUR", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0} {0,number,raw}\", Object...) #3")
	    public void getStringWithRawFormatStyle_3() {
	    	var data = Long.valueOf(123456);
	    	var raw = Long.toString(123456);
	    	var localized = MessageFormat.format("{0}", data);  //$NON-NLS-1$
	    	assertEquals(
	    			localized+" "+raw,  //$NON-NLS-1$
	    			Locale.getString("FOUR", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0} {0,number,raw}\", Object...) #4")
	    public void getStringWithRawFormatStyle_4() {
	    	var data = Long.valueOf(-123456);
	    	var raw = Long.toString(-123456);
	    	var localized = MessageFormat.format("{0}", data);  //$NON-NLS-1$
	    	assertEquals(
	    			localized+" "+raw,  //$NON-NLS-1$
	    			Locale.getString("FOUR", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0} {0,number,raw}\", Object...) #5")
	    public void getStringWithRawFormatStyle_5() {
	    	var data = Short.valueOf((short)123);
	    	var raw = Short.toString((short)123);
	    	var localized = MessageFormat.format("{0}", data);  //$NON-NLS-1$
	    	assertEquals(
	    			localized+" "+raw,  //$NON-NLS-1$
	    			Locale.getString("FOUR", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0} {0,number,raw}\", Object...) #6")
	    public void getStringWithRawFormatStyle_6() {
	    	var data = Short.valueOf((short)-123);
	    	var raw = Short.toString((short)-123);
	    	var localized = MessageFormat.format("{0}", data);  //$NON-NLS-1$
	    	assertEquals(
	    			localized+" "+raw,  //$NON-NLS-1$
	    			Locale.getString("FOUR", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0} {0,number,raw}\", Object...) #7")
	    public void getStringWithRawFormatStyle_7() {
	    	var raw = "123456789123456789123456789.123456789";  //$NON-NLS-1$
	    	var data = new BigDecimal(raw);
	    	var localized = MessageFormat.format("{0}", data);  //$NON-NLS-1$
	    	assertEquals(
	    			localized+" "+raw,  //$NON-NLS-1$
	    			Locale.getString("FOUR", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0} {0,number,raw}\", Object...) #8")
	    public void getStringWithRawFormatStyle_8() {
	    	var raw = "-123456789123456789123456789.123456789";  //$NON-NLS-1$
	    	var data = new BigDecimal(raw);
	    	var localized = MessageFormat.format("{0}", data);  //$NON-NLS-1$
	    	assertEquals(
	    			localized+" "+raw,  //$NON-NLS-1$
	    			Locale.getString("FOUR", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0} {0,number,raw}\", Object...) #9")
	    public void getStringWithRawFormatStyle_9() {
	    	var raw = "123456789123456789123456789123456789";  //$NON-NLS-1$
	    	var data = new BigInteger(raw);
	    	var localized = MessageFormat.format("{0}", data);  //$NON-NLS-1$
	    	assertEquals(
	    			localized+" "+raw,  //$NON-NLS-1$
	    			Locale.getString("FOUR", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0} {0,number,raw}\", Object...) #10")
	    public void getStringWithRawFormatStyle_10() {
	    	var raw = "-123456789123456789123456789123456789";  //$NON-NLS-1$
	    	var data = new BigInteger(raw);
	    	var localized = MessageFormat.format("{0}", data);  //$NON-NLS-1$
	    	assertEquals(
	    			localized+" "+raw,  //$NON-NLS-1$
	    			Locale.getString("FOUR", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#}\", Object...) #1")
	    public void getStringWithRawFormatStyleWithFormat1_1() {
	    	var data = Double.valueOf(123.456);
	    	assertEquals(
	    			"123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_1", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#}\", Object...) #2")
	    public void getStringWithRawFormatStyleWithFormat1_2() {
	    	var data = Double.valueOf(-123.456);
	    	assertEquals(
	    			"-123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_1", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#}\", Object...) #3")
	    public void getStringWithRawFormatStyleWithFormat1_3() {
	    	var data = Long.valueOf(123456);
	    	assertEquals(
	    			"123456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_1", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#}\", Object...) #4")
	    public void getStringWithRawFormatStyleWithFormat1_4() {
	    	var data = Long.valueOf(-123456);
	    	assertEquals(
	    			"-123456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_1", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#}\", Object...) #5")
	    public void getStringWithRawFormatStyleWithFormat1_5() {
	    	var data = Short.valueOf((short)123);
	    	assertEquals(
	    			"123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_1", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#}\", Object...) #6")
	    public void getStringWithRawFormatStyleWithFormat1_6() {
	    	var data = Short.valueOf((short)-123);
	    	assertEquals(
	    			"-123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_1", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#}\", Object...) #7")
	    public void getStringWithRawFormatStyleWithFormat1_7() {
	    	var data = new BigDecimal("123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_1", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#}\", Object...) #8")
	    public void getStringWithRawFormatStyleWithFormat1_8() {
	    	var data = new BigDecimal("123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456790",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_1", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#}\", Object...) #9")
	    public void getStringWithRawFormatStyleWithFormat1_9() {
	    	var data = new BigDecimal("-123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_1", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#}\", Object...) #10")
	    public void getStringWithRawFormatStyleWithFormat1_10() {
	    	var data = new BigDecimal("-123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456790",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_1", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#}\", Object...) #11")
	    public void getStringWithRawFormatStyleWithFormat1_11() {
	    	var data = new BigInteger("123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_1", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#}\", Object...) #12")
	    public void getStringWithRawFormatStyleWithFormat1_12() {
	    	var data = new BigInteger("-123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_1", data));  //$NON-NLS-1$
	    }
	 
		@Test
		@DisplayName("(\"{0,number,raw,#.}\", Object...) #1")
	    public void getStringWithRawFormatStyleWithFormat2_1() {
	    	var data = Double.valueOf(123.456);
	    	assertEquals(
	    			"123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_2", data));  //$NON-NLS-1$
	    }
		 
		@Test
		@DisplayName("(\"{0,number,raw,#.}\", Object...) #2")
	    public void getStringWithRawFormatStyleWithFormat2_2() {
	    	var data = Double.valueOf(-123.456);
	    	assertEquals(
	    			"-123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_2", data));  //$NON-NLS-1$
	    }
		 
		@Test
		@DisplayName("(\"{0,number,raw,#.}\", Object...) #3")
	    public void getStringWithRawFormatStyleWithFormat2_3() {
	    	var data = Long.valueOf(123456);
	    	assertEquals(
	    			"123456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_2", data));  //$NON-NLS-1$
	    }
		 
		@Test
		@DisplayName("(\"{0,number,raw,#.}\", Object...) #4")
	    public void getStringWithRawFormatStyleWithFormat2_4() {
	    	var data = Long.valueOf(-123456);
	    	assertEquals(
	    			"-123456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_2", data));  //$NON-NLS-1$
	    }
		 
		@Test
		@DisplayName("(\"{0,number,raw,#.}\", Object...) #5")
	    public void getStringWithRawFormatStyleWithFormat2_5() {
	    	var data = Short.valueOf((short)123);
	    	assertEquals(
	    			"123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_2", data));  //$NON-NLS-1$
	    }
		 
		@Test
		@DisplayName("(\"{0,number,raw,#.}\", Object...) #6")
	    public void getStringWithRawFormatStyleWithFormat2_6() {
	    	var data = Short.valueOf((short)-123);
	    	assertEquals(
	    			"-123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_2", data));  //$NON-NLS-1$
	    }
		 
		@Test
		@DisplayName("(\"{0,number,raw,#.}\", Object...) #7")
	    public void getStringWithRawFormatStyleWithFormat2_7() {
	    	var data = new BigDecimal("123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_2", data));  //$NON-NLS-1$
	    }
		 
		@Test
		@DisplayName("(\"{0,number,raw,#.}\", Object...) #8")
	    public void getStringWithRawFormatStyleWithFormat2_8() {
	    	var data = new BigDecimal("123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456790",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_2", data));  //$NON-NLS-1$
	    }
		 
		@Test
		@DisplayName("(\"{0,number,raw,#.}\", Object...) #9")
	    public void getStringWithRawFormatStyleWithFormat2_9() {
	    	var data = new BigDecimal("-123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_2", data));  //$NON-NLS-1$
	    }
		 
		@Test
		@DisplayName("(\"{0,number,raw,#.}\", Object...) #10")
	    public void getStringWithRawFormatStyleWithFormat2_10() {
	    	var data = new BigDecimal("-123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456790",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_2", data));  //$NON-NLS-1$
	    }
		 
		@Test
		@DisplayName("(\"{0,number,raw,#.}\", Object...) #11")
	    public void getStringWithRawFormatStyleWithFormat2_11() {
	    	var data = new BigInteger("123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_2", data));  //$NON-NLS-1$
	    }
		 
		@Test
		@DisplayName("(\"{0,number,raw,#.}\", Object...) #12")
	    public void getStringWithRawFormatStyleWithFormat2_12() {
	    	var data = new BigInteger("-123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_2", data));  //$NON-NLS-1$
	    }
	   
		@Test
		@DisplayName("(\"{0,number,raw,#.#}\", Object...) #1")
	    public void getStringWithRawFormatStyleWithFormat3_1() {
	    	var data = Double.valueOf(123.456);
	    	assertEquals(
	    			"123.5",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_3", data));  //$NON-NLS-1$
	    }
		   
		@Test
		@DisplayName("(\"{0,number,raw,#.#}\", Object...) #2")
	    public void getStringWithRawFormatStyleWithFormat3_2() {
	    	var data = Double.valueOf(-123.456);
	    	assertEquals(
	    			"-123.5",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_3", data));  //$NON-NLS-1$
	    }
		   
		@Test
		@DisplayName("(\"{0,number,raw,#.#}\", Object...) #3")
	    public void getStringWithRawFormatStyleWithFormat3_3() {
	    	var data = Long.valueOf(123456);
	    	assertEquals(
	    			"123456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_3", data));  //$NON-NLS-1$
	    }
		   
		@Test
		@DisplayName("(\"{0,number,raw,#.#}\", Object...) #4")
	    public void getStringWithRawFormatStyleWithFormat3_4() {
	    	var data = Long.valueOf(-123456);
	    	assertEquals(
	    			"-123456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_3", data));  //$NON-NLS-1$
	    }
		   
		@Test
		@DisplayName("(\"{0,number,raw,#.#}\", Object...) #5")
	    public void getStringWithRawFormatStyleWithFormat3_5() {
	    	var data = Short.valueOf((short)123);
	    	assertEquals(
	    			"123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_3", data));  //$NON-NLS-1$
	    }
		   
		@Test
		@DisplayName("(\"{0,number,raw,#.#}\", Object...) #6")
	    public void getStringWithRawFormatStyleWithFormat3_6() {
	    	var data = Short.valueOf((short)-123);
	    	assertEquals(
	    			"-123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_3", data));  //$NON-NLS-1$
	    }
		   
		@Test
		@DisplayName("(\"{0,number,raw,#.#}\", Object...) #7")
	    public void getStringWithRawFormatStyleWithFormat3_7() {
	    	var data = new BigDecimal("123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789.1",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_3", data));  //$NON-NLS-1$
	    }
		   
		@Test
		@DisplayName("(\"{0,number,raw,#.#}\", Object...) #8")
	    public void getStringWithRawFormatStyleWithFormat3_8() {
	    	var data = new BigDecimal("123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456790.0",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_3", data));  //$NON-NLS-1$
	    }
		   
		@Test
		@DisplayName("(\"{0,number,raw,#.#}\", Object...) #9")
	    public void getStringWithRawFormatStyleWithFormat3_9() {
	    	var data = new BigDecimal("-123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789.1",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_3", data));  //$NON-NLS-1$
	    }
		   
		@Test
		@DisplayName("(\"{0,number,raw,#.#}\", Object...) #10")
	    public void getStringWithRawFormatStyleWithFormat3_10() {
	    	var data = new BigDecimal("-123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456790.0",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_3", data));  //$NON-NLS-1$
	    }
		   
		@Test
		@DisplayName("(\"{0,number,raw,#.#}\", Object...) #11")
	    public void getStringWithRawFormatStyleWithFormat3_11() {
	    	var data = new BigInteger("123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_3", data));  //$NON-NLS-1$
	    }
		   
		@Test
		@DisplayName("(\"{0,number,raw,#.#}\", Object...) #12")
	    public void getStringWithRawFormatStyleWithFormat3_12() {
	    	var data = new BigInteger("-123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_3", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0,number,raw,0.0#}\", Object...) #1")
	    public void getStringWithRawFormatStyleWithFormat4_1() {
	    	var data = Double.valueOf(123.456);
	    	assertEquals(
	    			"123.46",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_4", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0,number,raw,0.0#}\", Object...) #2")
	    public void getStringWithRawFormatStyleWithFormat4_2() {
	    	var data = Double.valueOf(-123.456);
	    	assertEquals(
	    			"-123.46",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_4", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0,number,raw,0.0#}\", Object...) #3")
	    public void getStringWithRawFormatStyleWithFormat4_3() {
	    	var data = Long.valueOf(123456);
	    	assertEquals(
	    			"123456.0",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_4", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0,number,raw,0.0#}\", Object...) #4")
	    public void getStringWithRawFormatStyleWithFormat4_4() {
	    	var data = Long.valueOf(-123456);
	    	assertEquals(
	    			"-123456.0",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_4", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0,number,raw,0.0#}\", Object...) #5")
	    public void getStringWithRawFormatStyleWithFormat4_5() {
	    	var data = Short.valueOf((short)123);
	    	assertEquals(
	    			"123.0",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_4", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0,number,raw,0.0#}\", Object...) #6")
	    public void getStringWithRawFormatStyleWithFormat4_6() {
	    	var data = Short.valueOf((short)-123);
	    	assertEquals(
	    			"-123.0",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_4", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0,number,raw,0.0#}\", Object...) #7")
	    public void getStringWithRawFormatStyleWithFormat4_7() {
	    	var data = new BigDecimal("123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789.12",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_4", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0,number,raw,0.0#}\", Object...) #8")
	    public void getStringWithRawFormatStyleWithFormat4_8() {
	    	var data = new BigDecimal("123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789.99",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_4", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0,number,raw,0.0#}\", Object...) #9")
	    public void getStringWithRawFormatStyleWithFormat4_9() {
	    	var data = new BigDecimal("-123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789.12",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_4", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0,number,raw,0.0#}\", Object...) #10")
	    public void getStringWithRawFormatStyleWithFormat4_10() {
	    	var data = new BigDecimal("-123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789.99",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_4", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0,number,raw,0.0#}\", Object...) #11")
	    public void getStringWithRawFormatStyleWithFormat4_11() {
	    	var data = new BigInteger("123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789123456789.0",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_4", data));  //$NON-NLS-1$
	    }

		@Test
		@DisplayName("(\"{0,number,raw,0.0#}\", Object...) #12")
	    public void getStringWithRawFormatStyleWithFormat4_12() {
	    	var data = new BigInteger("-123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789123456789.0",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_4", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#.###}\", Object...) #1")
	    public void getStringWithRawFormatStyleWithFormat5_1() {
	    	var data = Double.valueOf(123.456);
	    	assertEquals(
	    			"123.456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_5", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#.###}\", Object...) #2")
	    public void getStringWithRawFormatStyleWithFormat5_2() {
	    	var data = Double.valueOf(-123.456);
	    	assertEquals(
	    			"-123.456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_5", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#.###}\", Object...) #3")
	    public void getStringWithRawFormatStyleWithFormat5_3() {
	    	var data = Long.valueOf(123456);
	    	assertEquals(
	    			"123456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_5", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#.###}\", Object...) #4")
	    public void getStringWithRawFormatStyleWithFormat5_4() {
	    	var data = Long.valueOf(-123456);
	    	assertEquals(
	    			"-123456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_5", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#.###}\", Object...) #5")
	    public void getStringWithRawFormatStyleWithFormat5_5() {
	    	var data = Short.valueOf((short)123);
	    	assertEquals(
	    			"123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_5", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#.###}\", Object...) #6")
	    public void getStringWithRawFormatStyleWithFormat5_6() {
	    	var data = Short.valueOf((short)-123);
	    	assertEquals(
	    			"-123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_5", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#.###}\", Object...) #7")
	    public void getStringWithRawFormatStyleWithFormat5_7() {
	    	var data = new BigDecimal("123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789.123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_5", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#.###}\", Object...) #8")
	    public void getStringWithRawFormatStyleWithFormat5_8() {
	    	var data = new BigDecimal("123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789.988",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_5", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#.###}\", Object...) #9")
	    public void getStringWithRawFormatStyleWithFormat5_9() {
	    	var data = new BigDecimal("-123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789.123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_5", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#.###}\", Object...) #10")
	    public void getStringWithRawFormatStyleWithFormat5_10() {
	    	var data = new BigDecimal("-123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789.988",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_5", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#.###}\", Object...) #11")
	    public void getStringWithRawFormatStyleWithFormat5_11() {
	    	var data = new BigInteger("123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_5", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#.###}\", Object...) #12")
	    public void getStringWithRawFormatStyleWithFormat5_12() {
	    	var data = new BigInteger("-123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_5", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0.}\", Object...) #1")
	    public void getStringWithRawFormatStyleWithFormat6_1() {
	    	var data = Double.valueOf(123.456);
	    	assertEquals(
	    			"123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_6", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0.}\", Object...) #2")
	    public void getStringWithRawFormatStyleWithFormat6_2() {
	    	var data = Double.valueOf(-123.456);
	    	assertEquals(
	    			"-123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_6", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0.}\", Object...) #3")
	    public void getStringWithRawFormatStyleWithFormat6_3() {
	    	var data = Long.valueOf(123456);
	    	assertEquals(
	    			"123456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_6", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0.}\", Object...) #4")
	    public void getStringWithRawFormatStyleWithFormat6_4() {
	    	var data = Long.valueOf(-123456);
	    	assertEquals(
	    			"-123456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_6", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0.}\", Object...) #5")
	    public void getStringWithRawFormatStyleWithFormat6_5() {
	    	var data = Short.valueOf((short)123);
	    	assertEquals(
	    			"123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_6", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0.}\", Object...) #6")
	    public void getStringWithRawFormatStyleWithFormat6_6() {
	    	var data = Short.valueOf((short)-123);
	    	assertEquals(
	    			"-123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_6", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0.}\", Object...) #7")
	    public void getStringWithRawFormatStyleWithFormat6_7() {
	    	var data = new BigDecimal("123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_6", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0.}\", Object...) #8")
	    public void getStringWithRawFormatStyleWithFormat6_8() {
	    	var data = new BigDecimal("123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456790",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_6", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0.}\", Object...) #9")
	    public void getStringWithRawFormatStyleWithFormat6_9() {
	    	var data = new BigDecimal("-123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_6", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0.}\", Object...) #10")
	    public void getStringWithRawFormatStyleWithFormat6_10() {
	    	var data = new BigDecimal("-123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456790",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_6", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0.}\", Object...) #11")
	    public void getStringWithRawFormatStyleWithFormat6_11() {
	    	var data = new BigInteger("123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_6", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0.}\", Object...) #12")
	    public void getStringWithRawFormatStyleWithFormat6_12() {
	    	var data = new BigInteger("-123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_6", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0}\", Object...) #1")
	    public void getStringWithRawFormatStyleWithFormat7_1() {
	    	var data = Double.valueOf(123.456);
	    	assertEquals(
	    			"123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_7", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0}\", Object...) #2")
	    public void getStringWithRawFormatStyleWithFormat7_2() {
	    	var data = Double.valueOf(-123.456);
	    	assertEquals(
	    			"-123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_7", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0}\", Object...) #3")
	    public void getStringWithRawFormatStyleWithFormat7_3() {
	    	var data = Long.valueOf(123456);
	    	assertEquals(
	    			"123456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_7", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0}\", Object...) #4")
	    public void getStringWithRawFormatStyleWithFormat7_4() {
	    	var data = Long.valueOf(-123456);
	    	assertEquals(
	    			"-123456",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_7", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0}\", Object...) #5")
	    public void getStringWithRawFormatStyleWithFormat7_5() {
	    	var data = Short.valueOf((short)123);
	    	assertEquals(
	    			"123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_7", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0}\", Object...) #6")
	    public void getStringWithRawFormatStyleWithFormat7_6() {
	    	var data = Short.valueOf((short)-123);
	    	assertEquals(
	    			"-123",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_7", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0}\", Object...) #7")
	    public void getStringWithRawFormatStyleWithFormat7_7() {
	    	var data = new BigDecimal("123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_7", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0}\", Object...) #8")
	    public void getStringWithRawFormatStyleWithFormat7_8() {
	    	var data = new BigDecimal("123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456790",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_7", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0}\", Object...) #9")
	    public void getStringWithRawFormatStyleWithFormat7_9() {
	    	var data = new BigDecimal("-123456789123456789123456789.123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_7", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0}\", Object...) #10")
	    public void getStringWithRawFormatStyleWithFormat7_10() {
	    	var data = new BigDecimal("-123456789123456789123456789.987654321");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456790",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_7", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0}\", Object...) #11")
	    public void getStringWithRawFormatStyleWithFormat7_11() {
	    	var data = new BigInteger("123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"123456789123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_7", data));  //$NON-NLS-1$
	    }
	    
		@Test
		@DisplayName("(\"{0,number,raw,#0}\", Object...) #12")
	    public void getStringWithRawFormatStyleWithFormat7_12() {
	    	var data = new BigInteger("-123456789123456789123456789123456789");  //$NON-NLS-1$
	    	assertEquals(
	    			"-123456789123456789123456789123456789",  //$NON-NLS-1$
	    			Locale.getString("FORMAT_7", data));  //$NON-NLS-1$
	    }
	}

	@DisplayName("getStringWithDefault")
	@Nested
	public class GetStringWithDefault {

		@Test
		@DisplayName("(Class, String, String, Object...) #1")
	    public void getStringWithDefaultClassStringStringObjectArray_1() {
	    	assertEquals(
	    			DEFAULT,
	    			Locale.getStringWithDefault(LocaleTest.class,NOKEY,DEFAULT,P1,P2,P3));
		}

		@Test
		@DisplayName("(Class, String, String, Object...) #2")
	    public void getStringWithDefaultClassStringStringObjectArray_2() {
	    	assertEquals(
	    			"ABC P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(LocaleTest.class,KEY1,DEFAULT,P1,P2,P3));
		}

		@Test
		@DisplayName("(Class, String, String, Object...) #3")
	    public void getStringWithDefaultClassStringStringObjectArray_3() {
	    	assertEquals(
	    			"ABC P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(LocaleTest.class,KEY1,DEFAULT,P3,P1,P2));
		}

		@Test
		@DisplayName("(Class, String, String, Object...) #4")
	    public void getStringWithDefaultClassStringStringObjectArray_4() {
	    	assertEquals(
	    			DEFAULT,
	    			Locale.getStringWithDefault(LocaleStub.class,NOKEY,DEFAULT,P1,P2,P3));
		}

		@Test
		@DisplayName("(Class, String, String, Object...) #5")
	    public void getStringWithDefaultClassStringStringObjectArray_5() {
	    	assertEquals(
	    			"DEF P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(LocaleStub.class,KEY1,DEFAULT,P1,P2,P3));
		}

		@Test
		@DisplayName("(Class, String, String, Object...) #6")
	    public void getStringWithDefaultClassStringStringObjectArray_6() {
	    	assertEquals(
	    			"DEF P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(LocaleStub.class,KEY1,DEFAULT,P3,P1,P2));
	    }
	
		@Test
		@DisplayName("(ClassLoader, Class, String, String, Object...) #1")
	    public void getStringWithDefaultClassLoaderClassStringStringObjectArray_1() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			DEFAULT,
	    			Locale.getStringWithDefault(l, LocaleTest.class,NOKEY,DEFAULT,P1,P2,P3));
	    }
		
		@Test
		@DisplayName("(ClassLoader, Class, String, String, Object...) #2")
	    public void getStringWithDefaultClassLoaderClassStringStringObjectArray_2() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"ABC P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(l, LocaleTest.class,KEY1,DEFAULT,P1,P2,P3));
	    }
		
		@Test
		@DisplayName("(ClassLoader, Class, String, String, Object...) #3")
	    public void getStringWithDefaultClassLoaderClassStringStringObjectArray_34() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"ABC P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(l, LocaleTest.class,KEY1,DEFAULT,P3,P1,P2));
	    }
		
		@Test
		@DisplayName("(ClassLoader, Class, String, String, Object...) #4")
	    public void getStringWithDefaultClassLoaderClassStringStringObjectArray_4() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			DEFAULT,
	    			Locale.getStringWithDefault(l, LocaleStub.class,NOKEY,DEFAULT,P1,P2,P3));
	    }
		
		@Test
		@DisplayName("(ClassLoader, Class, String, String, Object...) #5")
	    public void getStringWithDefaultClassLoaderClassStringStringObjectArray_5() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"DEF P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(l, LocaleStub.class,KEY1,DEFAULT,P1,P2,P3));
	    }
		
		@Test
		@DisplayName("(ClassLoader, Class, String, String, Object...) #6")
	    public void getStringWithDefaultClassLoaderClassStringStringObjectArray_6() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"DEF P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(l, LocaleStub.class,KEY1,DEFAULT,P3,P1,P2));
	    }

		@Test
		@DisplayName("(String, String, Object...) #1")
	    public void getStringWithDefaultStringStringObjectArray_1() {
	    	assertEquals(
	    			DEFAULT,
	    			Locale.getStringWithDefault(NOKEY,DEFAULT,P1,P2,P3));
	    }

		@Test
		@DisplayName("(String, String, Object...) #2")
	    public void getStringWithDefaultStringStringObjectArray_2() {
	    	assertEquals(
	    			"ABC P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(KEY1,DEFAULT,P1,P2,P3));
	    }

		@Test
		@DisplayName("(String, String, Object...) #3")
	    public void getStringWithDefaultStringStringObjectArray_3() {
	    	assertEquals(
	    			"ABC P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(KEY1,DEFAULT,P3,P1,P2));
	    }

		@Test
		@DisplayName("(String, String, Object...) #4")
	    public void getStringWithDefaultStringStringObjectArray_4() {
	    	try {
	    		Locale.getStringWithDefault(KEY2,DEFAULT);
	    		fail("expecting IllegalArgumentException");  //$NON-NLS-1$
	    	}
	    	catch(IllegalArgumentException exception) {
	    		//expected exception
	    	}
	    }

		@Test
		@DisplayName("(String, String, Object...) #5")
	    public void getStringWithDefaultStringStringObjectArray_5() {
	    	assertEquals(
	    			"(d92b87b0-efe9-4dd9-903f-7c994b8e2a9f)",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(KEY3,DEFAULT));
	    }

		@Test
		@DisplayName("(ClassLoader, String, String, Object...) #1")
	    public void getStringWithDefaultClassLoaderStringStringObjectArray_1() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			DEFAULT,
	    			Locale.getStringWithDefault(l, NOKEY,DEFAULT,P1,P2,P3));
	    }

		@Test
		@DisplayName("(ClassLoader, String, String, Object...) #2")
	    public void getStringWithDefaultClassLoaderStringStringObjectArray_2() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"ABC P1 'P2' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(l, KEY1,DEFAULT,P1,P2,P3));
	    }

		@Test
		@DisplayName("(ClassLoader, String, String, Object...) #3")
	    public void getStringWithDefaultClassLoaderStringStringObjectArray_3() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"ABC P3 'P1' {2}",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(l, KEY1,DEFAULT,P3,P1,P2));
	    }

		@Test
		@DisplayName("(ClassLoader, String, String, Object...) #4")
	    public void getStringWithDefaultClassLoaderStringStringObjectArray_4() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	try {
	    		Locale.getStringWithDefault(l, KEY2, DEFAULT);
	    		fail("expecting IllegalArgumentException");  //$NON-NLS-1$
	    	}
	    	catch(IllegalArgumentException exception) {
	    		// expected exception
	    	}
	    }

		@Test
		@DisplayName("(ClassLoader, String, String, Object...) #5")
	    public void getStringWithDefaultClassLoaderStringStringObjectArray_5() {
	    	ClassLoader l = LocaleTest.class.getClassLoader();
	    	assertEquals(
	    			"(d92b87b0-efe9-4dd9-903f-7c994b8e2a9f)",  //$NON-NLS-1$
	    			Locale.getStringWithDefault(l, KEY3, DEFAULT));
	    }
	}
    
	@DisplayName("decodeString")
	@Nested
	public class DecodeString {

		@Test
		@DisplayName("(byte[])")
		public void decodeStringByteArray() {
			byte[] bytes = new byte[] {80, 104, 111, 116, 111, 103, 114, 97, 109, 109, -126, 116, 114,
					105, 101, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32};
			String str = Locale.decodeString(bytes);
			assertEquals("Photogrammétrie", str); //$NON-NLS-1$
		}
	}
}
