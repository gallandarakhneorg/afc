/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2013 Stephane GALLAND.
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
package org.arakhne.afc.attrs.attr;

import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.arakhne.afc.attrs.AbstractAttrTestCase;
import org.arakhne.afc.attrs.attr.AttributeConstants;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.NullAttribute;
import org.arakhne.afc.attrs.attr.Timestamp;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.discrete.object2d.Point2i;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Point3D;
import org.arakhne.afc.math.generic.Tuple2D;
import org.arakhne.afc.math.generic.Tuple3D;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Colors;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.VectorToolkit;

/**
 * Test of AttributeType.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AttributeTypeTest extends AbstractAttrTestCase {

	/** 
	 * @param type
	 * @param value
	 */
	protected static void assertCastException(AttributeType type, Object value) {
		assertException(ClassCastException.class, type, "cast", new Class<?>[] {Object.class}, new Object[] {value}); //$NON-NLS-1$
	}

	/**
	 * @param type
	 * @param value
	 */
	protected static void assertNullException(AttributeType type, Object value) {
		assertException(NullPointerException.class, type, "cast", new Class<?>[] {Object.class}, new Object[] {value}); //$NON-NLS-1$
	}

	/**
	 */
	public static void testGetName() {
		for(AttributeType type : AttributeType.values()) {
			String name = type.getName();
			assertNotNull(name);
			assertNotSame("", name); //$NON-NLS-1$
		}
	}

	/**
	 */
	public static void testIsNumberType() {
		for(AttributeType t : AttributeType.values()) {
			assertEquals(t==AttributeType.INTEGER || t==AttributeType.REAL || t==AttributeType.TIMESTAMP,
					t.isNumberType());
			if (t.isNumberType()) assertTrue(t.isBaseType());
		}
	}

	/**
	 */
	public static void testFromInteger() {
		AttributeType[] types = AttributeType.values();
		assertNotNull(types);
		assertFalse(0==types.length);
		for(int i=-1; i<types.length+10; ++i) {
			AttributeType type = AttributeType.fromInteger(i);
			assertNotNull(type);
			if ((i<0)||(i>=types.length)) {
				assertEquals(AttributeType.OBJECT, type);
			}
			else {
				assertEquals(i,type.ordinal());
				assertEquals(types[i],type);
			}
		}
	}

	/**
	 * @throws Exception
	 */
	public void testFromValue() throws Exception {
		assertEquals(AttributeType.OBJECT, AttributeType.fromValue(null));
		assertEquals(AttributeType.BOOLEAN, AttributeType.fromValue(true));
		assertEquals(AttributeType.STRING, AttributeType.fromValue('c'));
		assertEquals(AttributeType.INTEGER, AttributeType.fromValue((byte)1));
		assertEquals(AttributeType.INTEGER, AttributeType.fromValue((short)1));
		assertEquals(AttributeType.INTEGER, AttributeType.fromValue(1));
		assertEquals(AttributeType.INTEGER, AttributeType.fromValue(1l));
		assertEquals(AttributeType.REAL, AttributeType.fromValue(1f));
		assertEquals(AttributeType.REAL, AttributeType.fromValue(1.));
		assertEquals(AttributeType.STRING, AttributeType.fromValue("Hello")); //$NON-NLS-1$
		assertEquals(AttributeType.STRING, AttributeType.fromValue(new StringBuffer()));
		assertEquals(AttributeType.STRING, AttributeType.fromValue(new StringBuilder()));
		assertEquals(AttributeType.DATE, AttributeType.fromValue(Calendar.getInstance()));
		assertEquals(AttributeType.DATE, AttributeType.fromValue(new Date()));
		assertEquals(AttributeType.COLOR, AttributeType.fromValue(Colors.RED));
		assertEquals(AttributeType.UUID, AttributeType.fromValue(UUID.randomUUID()));
		assertEquals(AttributeType.POINT, AttributeType.fromValue(new Point2f(0,0)));
		assertEquals(AttributeType.POINT, AttributeType.fromValue(new Point2i(0,0)));
		assertEquals(AttributeType.POINT3D, AttributeType.fromValue(new Point3f(0,0,0)));
		assertEquals(AttributeType.POLYLINE, AttributeType.fromValue(new Point2D[0]));
		assertEquals(AttributeType.POLYLINE3D, AttributeType.fromValue(new Point3D[0]));
		assertEquals(AttributeType.IMAGE, AttributeType.fromValue(VectorToolkit.image(1,1,false)));
		assertEquals(AttributeType.UUID, AttributeType.fromValue(UUID.randomUUID()));
		assertEquals(AttributeType.URL, AttributeType.fromValue(new URL("http://set.utbm.fr"))); //$NON-NLS-1$
		assertEquals(AttributeType.URI, AttributeType.fromValue(new URI("http://set.utbm.fr"))); //$NON-NLS-1$
		assertEquals(AttributeType.INET_ADDRESS, AttributeType.fromValue(InetAddress.getLocalHost()));
		AttributeType randomType = randomEnum(AttributeType.class);
		assertEquals(AttributeType.ENUMERATION, AttributeType.fromValue(randomType));
		assertEquals(AttributeType.OBJECT, AttributeType.fromValue(new int[0]));
		assertEquals(AttributeType.OBJECT, AttributeType.fromValue(Locale.getDefault()));
		assertEquals(AttributeType.TYPE, AttributeType.fromValue(AttributeTypeTest.class));
		assertEquals(AttributeType.TYPE, AttributeType.fromValue(double.class));
	}

	/**
	 * @throws Exception
	 */
	public static void testFromClass() throws Exception {
		assertEquals(AttributeType.OBJECT, AttributeType.fromClass(null));
		assertEquals(AttributeType.BOOLEAN, AttributeType.fromClass(boolean.class));
		assertEquals(AttributeType.BOOLEAN, AttributeType.fromClass(Boolean.class));
		assertEquals(AttributeType.STRING, AttributeType.fromClass(char.class));
		assertEquals(AttributeType.STRING, AttributeType.fromClass(Character.class));
		assertEquals(AttributeType.INTEGER, AttributeType.fromClass(byte.class));
		assertEquals(AttributeType.INTEGER, AttributeType.fromClass(Byte.class));
		assertEquals(AttributeType.INTEGER, AttributeType.fromClass(short.class));
		assertEquals(AttributeType.INTEGER, AttributeType.fromClass(Short.class));
		assertEquals(AttributeType.INTEGER, AttributeType.fromClass(int.class));
		assertEquals(AttributeType.INTEGER, AttributeType.fromClass(Integer.class));
		assertEquals(AttributeType.INTEGER, AttributeType.fromClass(long.class));
		assertEquals(AttributeType.INTEGER, AttributeType.fromClass(Long.class));
		assertEquals(AttributeType.REAL, AttributeType.fromClass(float.class));
		assertEquals(AttributeType.REAL, AttributeType.fromClass(Float.class));
		assertEquals(AttributeType.REAL, AttributeType.fromClass(double.class));
		assertEquals(AttributeType.REAL, AttributeType.fromClass(Double.class));
		assertEquals(AttributeType.STRING, AttributeType.fromClass(String.class));
		assertEquals(AttributeType.STRING, AttributeType.fromClass(StringBuffer.class));
		assertEquals(AttributeType.STRING, AttributeType.fromClass(StringBuilder.class));
		assertEquals(AttributeType.DATE, AttributeType.fromClass(Calendar.class));
		assertEquals(AttributeType.DATE, AttributeType.fromClass(Date.class));
		assertEquals(AttributeType.COLOR, AttributeType.fromClass(Color.class));
		assertEquals(AttributeType.UUID, AttributeType.fromClass(UUID.class));
		assertEquals(AttributeType.POINT, AttributeType.fromClass(Point2D.class));
		assertEquals(AttributeType.POINT, AttributeType.fromClass(Point2D.class));
		assertEquals(AttributeType.POINT3D, AttributeType.fromClass(Point3D.class));
		assertEquals(AttributeType.OBJECT, AttributeType.fromClass(Tuple2D[].class));
		assertEquals(AttributeType.POLYLINE, AttributeType.fromClass(Point2D[].class));
		assertEquals(AttributeType.OBJECT, AttributeType.fromClass(Tuple3D[].class));
		assertEquals(AttributeType.POLYLINE3D, AttributeType.fromClass(Point3D[].class));
		assertEquals(AttributeType.IMAGE, AttributeType.fromClass(Image.class));
		assertEquals(AttributeType.UUID, AttributeType.fromClass(UUID.class));
		assertEquals(AttributeType.URL, AttributeType.fromClass(URL.class));
		assertEquals(AttributeType.URI, AttributeType.fromClass(URI.class));
		assertEquals(AttributeType.INET_ADDRESS, AttributeType.fromClass(InetAddress.class));
		assertEquals(AttributeType.ENUMERATION, AttributeType.fromClass(AttributeType.class));
		assertEquals(AttributeType.OBJECT, AttributeType.fromClass(int[].class));
		assertEquals(AttributeType.OBJECT, AttributeType.fromClass(Locale.class));
		assertEquals(AttributeType.TYPE, AttributeType.fromClass(Class.class));
	}

	/**
	 */
	public static void testIsBaseType() {
		assertTrue(AttributeType.BOOLEAN.isBaseType());
		assertTrue(AttributeType.INTEGER.isBaseType());
		assertTrue(AttributeType.REAL.isBaseType());
		assertTrue(AttributeType.STRING.isBaseType());
		assertTrue(AttributeType.TIMESTAMP.isBaseType());
		
		assertFalse(AttributeType.COLOR.isBaseType());
		assertFalse(AttributeType.DATE.isBaseType());
		assertFalse(AttributeType.IMAGE.isBaseType());
		assertFalse(AttributeType.OBJECT.isBaseType());
		assertFalse(AttributeType.POINT.isBaseType());
		assertFalse(AttributeType.POINT3D.isBaseType());
		assertFalse(AttributeType.POLYLINE.isBaseType());
		assertFalse(AttributeType.POLYLINE3D.isBaseType());
		assertFalse(AttributeType.URL.isBaseType());
		assertFalse(AttributeType.URI.isBaseType());
		assertFalse(AttributeType.UUID.isBaseType());
		assertFalse(AttributeType.INET_ADDRESS.isBaseType());
		assertFalse(AttributeType.ENUMERATION.isBaseType());
		assertFalse(AttributeType.TYPE.isBaseType());
	}
	
	/**
	 */
	public static void testIsNullAllowed() {
		assertFalse(AttributeType.BOOLEAN.isNullAllowed());
		assertFalse(AttributeType.INTEGER.isNullAllowed());
		assertFalse(AttributeType.REAL.isNullAllowed());
		assertFalse(AttributeType.STRING.isNullAllowed());
		assertFalse(AttributeType.TIMESTAMP.isNullAllowed());
		
		assertFalse(AttributeType.COLOR.isNullAllowed());
		assertFalse(AttributeType.DATE.isNullAllowed());
		assertTrue(AttributeType.IMAGE.isNullAllowed());
		assertTrue(AttributeType.OBJECT.isNullAllowed());
		assertFalse(AttributeType.POINT.isNullAllowed());
		assertFalse(AttributeType.POINT3D.isNullAllowed());
		assertFalse(AttributeType.POLYLINE.isNullAllowed());
		assertFalse(AttributeType.POLYLINE3D.isNullAllowed());
		assertTrue(AttributeType.URL.isNullAllowed());
		assertTrue(AttributeType.URI.isNullAllowed());
		assertFalse(AttributeType.UUID.isNullAllowed());
		assertTrue(AttributeType.INET_ADDRESS.isNullAllowed());
		assertTrue(AttributeType.ENUMERATION.isNullAllowed());
		assertFalse(AttributeType.TYPE.isNullAllowed());
	}

	/**
	 * @throws Exception
	 */
	public static void testGetDefaultValue() throws Exception {
		assertEquals(Boolean.FALSE, AttributeType.BOOLEAN.getDefaultValue());
		assertEquals(new Long(0), AttributeType.INTEGER.getDefaultValue());
		assertEquals(new Double(0.), AttributeType.REAL.getDefaultValue());
		assertEquals(new String(), AttributeType.STRING.getDefaultValue());
		assertNotNull(AttributeType.TIMESTAMP.getDefaultValue());
		
		assertEquals(VectorToolkit.color(0,0,0), AttributeType.COLOR.getDefaultValue());
		assertNotNull(AttributeType.DATE.getDefaultValue());
		assertNull(AttributeType.IMAGE.getDefaultValue());
		assertNull(AttributeType.OBJECT.getDefaultValue());
		assertEquals(new Point2f(), AttributeType.POINT.getDefaultValue());
		assertEquals(new Point3f(), AttributeType.POINT3D.getDefaultValue());
		assertTrue(Arrays.equals(new Point2D[0], (Point2D[])AttributeType.POLYLINE.getDefaultValue()));
		assertTrue(Arrays.equals(new Point3D[0], (Point3D[])AttributeType.POLYLINE3D.getDefaultValue()));
		assertNull(AttributeType.URL.getDefaultValue());
		assertNull(AttributeType.URI.getDefaultValue());
		assertEquals(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"), AttributeType.UUID.getDefaultValue()); //$NON-NLS-1$
		assertEquals(InetAddress.getLocalHost(), AttributeType.INET_ADDRESS.getDefaultValue());
		assertNull(AttributeType.ENUMERATION.getDefaultValue());
		assertEquals(Object.class, AttributeType.TYPE.getDefaultValue());
	}

	/**
	 * @throws Exception
	 */
	public void testCast() throws Exception {
		AttributeType type;
		char vChar = 'c';
		byte vByte = 1;
		short vShort = 1;
		int vInt = 1;
		long vLong = 1;
		float vFloat = 1f;
		double vDouble = 1.;
		String vStr = "Hello"; //$NON-NLS-1$
		StringBuilder vStrB = new StringBuilder("World"); //$NON-NLS-1$
		Calendar cal = Calendar.getInstance();
		Date dt = new Date();
		Point2f pt2d1 = new Point2f(0,0);
		Image img = VectorToolkit.image(1,1,false);
		Color col = Colors.RED;
		UUID uuid = UUID.nameUUIDFromBytes("abcd".getBytes()); //$NON-NLS-1$
		Point3D pt3d = new Point3f(0,0,0);
		Point2D[] tabpt2d1 = new Point2D[0];
		Point3D[] tabpt3d = new Point3D[0];
		int[] tabint = new int[0];
		Object obj = Locale.getDefault();
		URL url = new URL("http://set.utbm.fr"); //$NON-NLS-1$
		URI uri = new URI("http://set.utbm.fr"); //$NON-NLS-1$
		InetAddress ipAddress = InetAddress.getLocalHost();
		String vIpStr = ipAddress.toString();
		AttributeType enumeration = randomEnum(AttributeType.class);
		String vEnumStr = AttributeType.class.getCanonicalName()+"."+AttributeType.values()[1].name(); //$NON-NLS-1$
		
		// BOOLEAN
		type = AttributeType.BOOLEAN;
		assertNullException(type,null);
		assertEquals(Boolean.TRUE, type.cast(true));
		assertEquals(Boolean.FALSE, type.cast(false));
		assertCastException(type,vChar);
		assertCastException(type,vByte);
		assertCastException(type,vShort);
		assertCastException(type,vInt);
		assertCastException(type,vLong);
		assertCastException(type,vFloat);
		assertCastException(type,vDouble);
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertCastException(type,url);
		assertCastException(type,uri);
		assertCastException(type,ipAddress);
		assertCastException(type,enumeration);

		// COLOR
		type = AttributeType.COLOR;
		assertNullException(type,null);
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertEquals(VectorToolkit.color(vByte), type.cast(vByte));
		assertEquals(VectorToolkit.color(vShort), type.cast(vShort));
		assertEquals(VectorToolkit.color(vInt), type.cast(vInt));
		assertEquals(VectorToolkit.color((int)vLong), type.cast(vLong));
		assertEquals(VectorToolkit.color((int)vFloat), type.cast(vFloat));
		assertEquals(VectorToolkit.color((int)vDouble), type.cast(vDouble));
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertSame(col,type.cast(col));
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertCastException(type,url);
		assertCastException(type,uri);
		assertCastException(type,ipAddress);
		assertCastException(type,enumeration);

		// UUID
		type = AttributeType.UUID;
		assertNullException(type,null);
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertCastException(type,vByte);
		assertCastException(type,vShort);
		assertCastException(type,vInt);
		assertCastException(type,vLong);
		assertCastException(type,vFloat);
		assertCastException(type,vDouble);
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertCastException(type,col);
		assertSame(uuid,type.cast(uuid));
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertCastException(type,url);
		assertCastException(type,uri);
		assertCastException(type,ipAddress);
		assertCastException(type,enumeration);

		// DATE
		type = AttributeType.DATE;
		assertNullException(type,null);
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertEquals(new Date(vByte),type.cast(vByte));
		assertEquals(new Date(vShort),type.cast(vShort));
		assertEquals(new Date(vInt),type.cast(vInt));
		assertEquals(new Date(vLong),type.cast(vLong));
		assertEquals(new Date((long)vFloat),type.cast(vFloat));
		assertEquals(new Date((long)vDouble),type.cast(vDouble));
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertEquals(cal.getTime(),type.cast(cal));
		assertSame(dt,type.cast(dt));
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertCastException(type,url);
		assertCastException(type,uri);
		assertCastException(type,ipAddress);
		assertCastException(type,enumeration);

		// ICON
		type = AttributeType.IMAGE;
		assertEquals(new NullAttribute(type),type.cast(null));
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertCastException(type,vByte);
		assertCastException(type,vShort);
		assertCastException(type,vInt);
		assertCastException(type,vLong);
		assertCastException(type,vFloat);
		assertCastException(type,vDouble);
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertNotNull(type.cast(img));
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertCastException(type,url);
		assertCastException(type,uri);
		assertCastException(type,ipAddress);
		assertCastException(type,enumeration);

		// INTEGER
		type = AttributeType.INTEGER;
		assertNullException(type,null);
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertEquals((long)vByte,type.cast(vByte));
		assertEquals((long)vShort,type.cast(vShort));
		assertEquals((long)vInt,type.cast(vInt));
		assertEquals(vLong,type.cast(vLong));
		assertEquals((long)vFloat,type.cast(vFloat));
		assertEquals((long)vDouble,type.cast(vDouble));
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertCastException(type,url);
		assertCastException(type,uri);
		assertCastException(type,ipAddress);
		assertEquals((long)enumeration.ordinal(), type.cast(enumeration));

		// OBJECT
		type = AttributeType.OBJECT;
		assertEquals(new NullAttribute(type),type.cast(null));
		assertEquals(true,type.cast(true));
		assertEquals(false,type.cast(false));
		assertEquals(vChar,type.cast(vChar));
		assertEquals(vByte,type.cast(vByte));
		assertEquals(vShort,type.cast(vShort));
		assertEquals(vInt,type.cast(vInt));
		assertEquals(vLong,type.cast(vLong));
		assertEquals(vFloat, type.cast(vFloat));
		assertEquals(vDouble,type.cast(vDouble));
		assertEquals(vStr,type.cast(vStr));
		assertEquals(vStrB,type.cast(vStrB));
		assertSame(cal,type.cast(cal));
		assertSame(dt,type.cast(dt));
		assertSame(col,type.cast(col));
		assertSame(uuid,type.cast(uuid));
		assertSame(pt2d1,type.cast(pt2d1));
		assertSame(pt3d,type.cast(pt3d));
		assertSame(tabpt2d1,type.cast(tabpt2d1));
		assertSame(tabpt3d,type.cast(tabpt3d));
		assertSame(img,type.cast(img));
		assertSame(tabint,type.cast(tabint));
		assertSame(obj,type.cast(obj));
		assertSame(url,type.cast(url));
		assertSame(uri,type.cast(uri));
		assertSame(ipAddress, type.cast(ipAddress));
		assertSame(enumeration,type.cast(enumeration));

		// POINT
		type = AttributeType.POINT;
		assertNullException(type,null);
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertCastException(type,vByte);
		assertCastException(type,vShort);
		assertCastException(type,vInt);
		assertCastException(type,vLong);
		assertCastException(type,vFloat);
		assertCastException(type,vDouble);
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertSame(pt2d1,type.cast(pt2d1));
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertCastException(type,url);
		assertCastException(type,uri);
		assertCastException(type,ipAddress);
		assertCastException(type,enumeration);

		// POINT3D
		type = AttributeType.POINT3D;
		assertNullException(type,null);
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertCastException(type,vByte);
		assertCastException(type,vShort);
		assertCastException(type,vInt);
		assertCastException(type,vLong);
		assertCastException(type,vFloat);
		assertCastException(type,vDouble);
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertSame(pt3d,type.cast(pt3d));
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertCastException(type,url);
		assertCastException(type,uri);
		assertCastException(type,ipAddress);
		assertCastException(type,enumeration);

		// POLYLINE
		type = AttributeType.POLYLINE;
		assertNullException(type,null);
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertCastException(type,vByte);
		assertCastException(type,vShort);
		assertCastException(type,vInt);
		assertCastException(type,vLong);
		assertCastException(type,vFloat);
		assertCastException(type,vDouble);
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertSame(tabpt2d1,type.cast(tabpt2d1));
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertCastException(type,url);
		assertCastException(type,uri);
		assertCastException(type,ipAddress);
		assertCastException(type,enumeration);

		// POLYINE3D
		type = AttributeType.POLYLINE3D;
		assertNullException(type,null);
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertCastException(type,vByte);
		assertCastException(type,vShort);
		assertCastException(type,vInt);
		assertCastException(type,vLong);
		assertCastException(type,vFloat);
		assertCastException(type,vDouble);
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertSame(tabpt3d,type.cast(tabpt3d));
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertCastException(type,url);
		assertCastException(type,uri);
		assertCastException(type,ipAddress);
		assertCastException(type,enumeration);

		// REAL
		type = AttributeType.REAL;
		assertNullException(type,null);
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertEquals((double)vByte,type.cast(vByte));
		assertEquals((double)vShort,type.cast(vShort));
		assertEquals((double)vInt,type.cast(vInt));
		assertEquals((double)vLong,type.cast(vLong));
		assertEquals((double)vFloat,type.cast(vFloat));
		assertEquals(vDouble,type.cast(vDouble));
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertCastException(type,url);
		assertCastException(type,uri);
		assertCastException(type,ipAddress);
		assertEquals((double)enumeration.ordinal(), type.cast(enumeration));

		// STRING
		type = AttributeType.STRING;
		assertEquals(type.getDefaultValue(),type.cast(null));
		assertEquals(Boolean.toString(true),type.cast(true));
		assertEquals(Boolean.toString(false),type.cast(false));
		assertEquals(Character.toString(vChar),type.cast(vChar));
		assertEquals(Byte.toString(vByte),type.cast(vByte));
		assertEquals(Short.toString(vShort),type.cast(vShort));
		assertEquals(Integer.toString(vInt),type.cast(vInt));
		assertEquals(Long.toString(vLong),type.cast(vLong));
		assertEquals(Float.toString(vFloat),type.cast(vFloat));
		assertEquals(Double.toString(vDouble),type.cast(vDouble));
		assertEquals(vStr,type.cast(vStr));
		assertEquals(vStrB.toString(),type.cast(vStrB));
		assertEquals(cal.toString(),type.cast(cal));
		assertEquals(dt.toString(),type.cast(dt));
		assertEquals(col.toString(),type.cast(col));
		assertEquals(uuid.toString(),type.cast(uuid));
		assertEquals(pt2d1.toString(),type.cast(pt2d1));
		assertEquals(pt3d.toString(),type.cast(pt3d));
		assertEquals(tabpt2d1.toString(),type.cast(tabpt2d1));
		assertEquals(tabpt3d.toString(),type.cast(tabpt3d));
		assertEquals(img.toString(),type.cast(img));
		assertEquals(tabint.toString(),type.cast(tabint));
		assertEquals(obj.toString(),type.cast(obj));
		assertEquals(url.toString(), type.cast(url));
		assertEquals(uri.toString(), type.cast(uri));
		assertEquals(ipAddress.toString(), type.cast(ipAddress));
		assertEquals(enumeration.getClass().getCanonicalName()+"."+enumeration.name(), type.cast(enumeration)); //$NON-NLS-1$

		// TIMESTAMP
		type = AttributeType.TIMESTAMP;
		assertNullException(type,null);
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertEquals(new Timestamp(vByte),type.cast(vByte));
		assertEquals(new Timestamp(vShort),type.cast(vShort));
		assertEquals(new Timestamp(vInt),type.cast(vInt));
		assertEquals(new Timestamp(vLong),type.cast(vLong));
		assertEquals(new Timestamp((int)vFloat),type.cast(vFloat));
		assertEquals(new Timestamp((int)vDouble),type.cast(vDouble));
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertEquals(cal.getTimeInMillis(),type.cast(cal));
		assertEquals(dt.getTime(),type.cast(dt));
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertCastException(type,url);
		assertCastException(type,uri);
		assertCastException(type,ipAddress);
		assertCastException(type,enumeration);

		// URI
		type = AttributeType.URI;
		assertEquals(new NullAttribute(type),type.cast(null));
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertCastException(type,vByte);
		assertCastException(type,vShort);
		assertCastException(type,vInt);
		assertCastException(type,vLong);
		assertCastException(type,vFloat);
		assertCastException(type,vDouble);
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertEquals(url.toURI(), type.cast(url));
		assertSame(uri, type.cast(uri));
		assertEquals(new URI(AttributeConstants.DEFAULT_SCHEME.name(), ipAddress.getHostAddress(), ""), type.cast(ipAddress)); //$NON-NLS-1$
		assertCastException(type,enumeration);

		// URL
		type = AttributeType.URL;
		assertEquals(new NullAttribute(type),type.cast(null));
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertCastException(type,vByte);
		assertCastException(type,vShort);
		assertCastException(type,vInt);
		assertCastException(type,vLong);
		assertCastException(type,vFloat);
		assertCastException(type,vDouble);
		assertCastException(type,vStr);
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertSame(url, type.cast(url));
		assertEquals(uri.toURL(), type.cast(uri));
		assertEquals(new URL(AttributeConstants.DEFAULT_SCHEME.name(), ipAddress.getHostAddress(), ""), type.cast(ipAddress)); //$NON-NLS-1$
		assertCastException(type,enumeration);

		// INET_ADDRESS
		type = AttributeType.INET_ADDRESS;
		assertEquals(new NullAttribute(type),type.cast(null));
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertCastException(type,vByte);
		assertCastException(type,vShort);
		assertCastException(type,vInt);
		assertCastException(type,vLong);
		assertCastException(type,vFloat);
		assertCastException(type,vDouble);
		assertCastException(type,vStr);
		assertEquals(ipAddress,type.cast(vIpStr));
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertEquals(InetAddress.getByName("set.utbm.fr"), type.cast(url)); //$NON-NLS-1$
		assertEquals(InetAddress.getByName("set.utbm.fr"), type.cast(uri)); //$NON-NLS-1$
		assertSame(ipAddress, type.cast(ipAddress));
		assertCastException(type,enumeration);

		// ENUMERATION
		type = AttributeType.ENUMERATION;
		assertEquals(new NullAttribute(type),type.cast(null));
		assertCastException(type,true);
		assertCastException(type,false);
		assertCastException(type,vChar);
		assertCastException(type,vByte);
		assertCastException(type,vShort);
		assertCastException(type,vInt);
		assertCastException(type,vLong);
		assertCastException(type,vFloat);
		assertCastException(type,vDouble);
		assertCastException(type,vStr);
		assertSame(AttributeType.values()[1],type.cast(vEnumStr));
		assertCastException(type,vStrB);
		assertCastException(type,cal);
		assertCastException(type,dt);
		assertCastException(type,col);
		assertCastException(type,uuid);
		assertCastException(type,pt2d1);
		assertCastException(type,pt3d);
		assertCastException(type,tabpt2d1);
		assertCastException(type,tabpt3d);
		assertCastException(type,img);
		assertCastException(type,tabint);
		assertCastException(type,obj);
		assertCastException(type,url);
		assertCastException(type,uri);
		assertCastException(type,ipAddress);
		assertSame(enumeration,type.cast(enumeration));
	}

	/**
	 */
	public static void testIsAssignableFrom() {
		assertTrue(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.BOOLEAN));
		assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.COLOR));
		assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.IMAGE));
		assertTrue(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.OBJECT));
		assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.POINT));
		assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.POINT3D));
		assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.POLYLINE));
		assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.POLYLINE3D));
		assertTrue(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.STRING));
		assertTrue(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.TIMESTAMP));
		assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.URI));
		assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.URL));
		assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.UUID));
		assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertFalse(AttributeType.BOOLEAN.isAssignableFrom(AttributeType.ENUMERATION));

		assertFalse(AttributeType.COLOR.isAssignableFrom(AttributeType.BOOLEAN));
		assertTrue(AttributeType.COLOR.isAssignableFrom(AttributeType.COLOR));
		assertTrue(AttributeType.COLOR.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.COLOR.isAssignableFrom(AttributeType.IMAGE));
		assertTrue(AttributeType.COLOR.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.COLOR.isAssignableFrom(AttributeType.OBJECT));
		assertTrue(AttributeType.COLOR.isAssignableFrom(AttributeType.POINT));
		assertTrue(AttributeType.COLOR.isAssignableFrom(AttributeType.POINT3D));
		assertFalse(AttributeType.COLOR.isAssignableFrom(AttributeType.POLYLINE));
		assertFalse(AttributeType.COLOR.isAssignableFrom(AttributeType.POLYLINE3D));
		assertTrue(AttributeType.COLOR.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.COLOR.isAssignableFrom(AttributeType.STRING));
		assertTrue(AttributeType.COLOR.isAssignableFrom(AttributeType.TIMESTAMP));
		assertFalse(AttributeType.COLOR.isAssignableFrom(AttributeType.URI));
		assertFalse(AttributeType.COLOR.isAssignableFrom(AttributeType.URL));
		assertFalse(AttributeType.COLOR.isAssignableFrom(AttributeType.UUID));
		assertFalse(AttributeType.COLOR.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertFalse(AttributeType.COLOR.isAssignableFrom(AttributeType.ENUMERATION));

		assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.BOOLEAN));
		assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.COLOR));
		assertTrue(AttributeType.DATE.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.IMAGE));
		assertTrue(AttributeType.DATE.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.DATE.isAssignableFrom(AttributeType.OBJECT));
		assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.POINT));
		assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.POINT3D));
		assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.POLYLINE));
		assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.POLYLINE3D));
		assertTrue(AttributeType.DATE.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.DATE.isAssignableFrom(AttributeType.STRING));
		assertTrue(AttributeType.DATE.isAssignableFrom(AttributeType.TIMESTAMP));
		assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.URI));
		assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.URL));
		assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.UUID));
		assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertFalse(AttributeType.DATE.isAssignableFrom(AttributeType.ENUMERATION));

		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.BOOLEAN));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.COLOR));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.DATE));
		assertTrue(AttributeType.IMAGE.isAssignableFrom(AttributeType.IMAGE));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.IMAGE.isAssignableFrom(AttributeType.OBJECT));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.POINT));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.POINT3D));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.POLYLINE));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.POLYLINE3D));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.REAL));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.STRING));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.TIMESTAMP));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.URI));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.URL));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.UUID));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertFalse(AttributeType.IMAGE.isAssignableFrom(AttributeType.ENUMERATION));

		assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.COLOR));
		assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.IMAGE));
		assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.OBJECT));
		assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.POINT));
		assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.POINT3D));
		assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.POLYLINE));
		assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.POLYLINE3D));
		assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.STRING));
		assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.TIMESTAMP));
		assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.URI));
		assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.URL));
		assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.UUID));
		assertFalse(AttributeType.INTEGER.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertTrue(AttributeType.INTEGER.isAssignableFrom(AttributeType.ENUMERATION));

		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.BOOLEAN));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.COLOR));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.DATE));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.IMAGE));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.OBJECT));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.POINT));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.POINT3D));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.POLYLINE));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.POLYLINE3D));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.STRING));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.TIMESTAMP));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.URI));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.URL));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.UUID));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertTrue(AttributeType.OBJECT.isAssignableFrom(AttributeType.ENUMERATION));

		assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.BOOLEAN));
		assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.COLOR));
		assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.IMAGE));
		assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.OBJECT));
		assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.POINT));
		assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.POINT3D));
		assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.POLYLINE));
		assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.POLYLINE3D));
		assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.STRING));
		assertTrue(AttributeType.POINT.isAssignableFrom(AttributeType.TIMESTAMP));
		assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.URI));
		assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.URL));
		assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.UUID));
		assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertFalse(AttributeType.POINT.isAssignableFrom(AttributeType.ENUMERATION));

		assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.BOOLEAN));
		assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.COLOR));
		assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.IMAGE));
		assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.OBJECT));
		assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.POINT));
		assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.POINT3D));
		assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.POLYLINE));
		assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.POLYLINE3D));
		assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.STRING));
		assertTrue(AttributeType.POINT3D.isAssignableFrom(AttributeType.TIMESTAMP));
		assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.URI));
		assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.URL));
		assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.UUID));
		assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertFalse(AttributeType.POINT3D.isAssignableFrom(AttributeType.ENUMERATION));

		assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.BOOLEAN));
		assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.COLOR));
		assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.IMAGE));
		assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.POLYLINE.isAssignableFrom(AttributeType.OBJECT));
		assertTrue(AttributeType.POLYLINE.isAssignableFrom(AttributeType.POINT));
		assertTrue(AttributeType.POLYLINE.isAssignableFrom(AttributeType.POINT3D));
		assertTrue(AttributeType.POLYLINE.isAssignableFrom(AttributeType.POLYLINE));
		assertTrue(AttributeType.POLYLINE.isAssignableFrom(AttributeType.POLYLINE3D));
		assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.POLYLINE.isAssignableFrom(AttributeType.STRING));
		assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.TIMESTAMP));
		assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.URI));
		assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.URL));
		assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.UUID));
		assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertFalse(AttributeType.POLYLINE.isAssignableFrom(AttributeType.ENUMERATION));

		assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.BOOLEAN));
		assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.COLOR));
		assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.IMAGE));
		assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.OBJECT));
		assertTrue(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.POINT));
		assertTrue(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.POINT3D));
		assertTrue(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.POLYLINE));
		assertTrue(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.POLYLINE3D));
		assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.STRING));
		assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.TIMESTAMP));
		assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.URI));
		assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.URL));
		assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.UUID));
		assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertFalse(AttributeType.POLYLINE3D.isAssignableFrom(AttributeType.ENUMERATION));

		assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.BOOLEAN));
		assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.COLOR));
		assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.IMAGE));
		assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.OBJECT));
		assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.POINT));
		assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.POINT3D));
		assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.POLYLINE));
		assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.POLYLINE3D));
		assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.STRING));
		assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.TIMESTAMP));
		assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.URI));
		assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.URL));
		assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.UUID));
		assertFalse(AttributeType.REAL.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertTrue(AttributeType.REAL.isAssignableFrom(AttributeType.ENUMERATION));

		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.BOOLEAN));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.COLOR));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.DATE));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.IMAGE));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.OBJECT));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.POINT));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.POINT3D));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.POLYLINE));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.POLYLINE3D));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.STRING));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.TIMESTAMP));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.URI));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.URL));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.UUID));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertTrue(AttributeType.STRING.isAssignableFrom(AttributeType.ENUMERATION));

		assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.BOOLEAN));
		assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.COLOR));
		assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.IMAGE));
		assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.OBJECT));
		assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.POINT));
		assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.POINT3D));
		assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.POLYLINE));
		assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.POLYLINE3D));
		assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.STRING));
		assertTrue(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.TIMESTAMP));
		assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.URI));
		assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.URL));
		assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.UUID));
		assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertFalse(AttributeType.TIMESTAMP.isAssignableFrom(AttributeType.ENUMERATION));

		assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.BOOLEAN));
		assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.COLOR));
		assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.IMAGE));
		assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.URI.isAssignableFrom(AttributeType.OBJECT));
		assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.POINT));
		assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.POINT3D));
		assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.POLYLINE));
		assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.POLYLINE3D));
		assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.URI.isAssignableFrom(AttributeType.STRING));
		assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.TIMESTAMP));
		assertTrue(AttributeType.URI.isAssignableFrom(AttributeType.URI));
		assertTrue(AttributeType.URI.isAssignableFrom(AttributeType.URL));
		assertTrue(AttributeType.URI.isAssignableFrom(AttributeType.UUID));
		assertTrue(AttributeType.URI.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertFalse(AttributeType.URI.isAssignableFrom(AttributeType.ENUMERATION));

		assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.BOOLEAN));
		assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.COLOR));
		assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.IMAGE));
		assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.URL.isAssignableFrom(AttributeType.OBJECT));
		assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.POINT));
		assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.POINT3D));
		assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.POLYLINE));
		assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.POLYLINE3D));
		assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.URL.isAssignableFrom(AttributeType.STRING));
		assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.TIMESTAMP));
		assertTrue(AttributeType.URL.isAssignableFrom(AttributeType.URI));
		assertTrue(AttributeType.URL.isAssignableFrom(AttributeType.URL));
		assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.UUID));
		assertTrue(AttributeType.URL.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertFalse(AttributeType.URL.isAssignableFrom(AttributeType.ENUMERATION));

		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.BOOLEAN));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.COLOR));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.DATE));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.IMAGE));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.OBJECT));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.POINT));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.POINT3D));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.POLYLINE));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.POLYLINE3D));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.STRING));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.TIMESTAMP));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.URI));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.URL));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.UUID));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertTrue(AttributeType.UUID.isAssignableFrom(AttributeType.ENUMERATION));

		assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.BOOLEAN));
		assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.COLOR));
		assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.IMAGE));
		assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.OBJECT));
		assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.POINT));
		assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.POINT3D));
		assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.POLYLINE));
		assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.POLYLINE3D));
		assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.STRING));
		assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.TIMESTAMP));
		assertTrue(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.URI));
		assertTrue(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.URL));
		assertTrue(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertFalse(AttributeType.INET_ADDRESS.isAssignableFrom(AttributeType.ENUMERATION));

		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.BOOLEAN));
		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.COLOR));
		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.DATE));
		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.IMAGE));
		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.INTEGER));
		assertTrue(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.OBJECT));
		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.POINT));
		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.POINT3D));
		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.POLYLINE));
		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.POLYLINE3D));
		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.REAL));
		assertTrue(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.STRING));
		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.TIMESTAMP));
		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.URI));
		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.URL));
		assertFalse(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.INET_ADDRESS));
		assertTrue(AttributeType.ENUMERATION.isAssignableFrom(AttributeType.ENUMERATION));
	}

}
