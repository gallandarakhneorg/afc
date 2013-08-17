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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import org.arakhne.afc.attrs.AbstractAttrTestCase;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeNotInitializedException;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.attr.InvalidAttributeTypeException;
import org.arakhne.afc.attrs.attr.Timestamp;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Point3D;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Colors;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.VectorToolkit;

/**
 * Test of AttributeValue.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AttributeValueTest extends AbstractAttrTestCase {

	/**
	 * @param attr
	 * @param type
	 */
	protected static void assertAllGetFailed(AttributeValue attr, AttributeType type) {
		try {
			attr.getValue();
			fail("getValue: the exception AttributeNotInitializedException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getBoolean();
			fail("getBoolean: the exception InvalidAttributeTypeException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getColor();
			fail("getColor: the exception InvalidAttributeTypeException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getDate();
			fail("getDate: the exception InvalidAttributeTypeException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getImage();
			fail("getImage: the exception AttributeNotInitializedException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getInteger();
			fail("getInteger: the exception InvalidAttributeTypeException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getJavaObject();
			if (type.isBaseType())
				fail("getJavaObject: the exception AttributeNotInitializedException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getPoint();
			fail("getPoint: the exception InvalidAttributeTypeException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getPoint3D();
			fail("getPoint3D: the exception InvalidAttributeTypeException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getPolyline();
			fail("getPolyline: the exception InvalidAttributeTypeException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getPolyline3D();
			fail("getPolyline3D: the exception InvalidAttributeTypeException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getReal();
			fail("getReal: the exception InvalidAttributeTypeException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getString();
			fail("getString: the exception AttributeNotInitializedException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeNotInitializedException _) {
			//
		}
		catch(InvalidAttributeTypeException _) {
			if (!attr.isObjectValue())
				fail("unexpected exception InvalidAttributeTypeException for "+type); //$NON-NLS-1$
		}

		try {
			attr.getTimestamp();
			fail("getTimestamp: the exception InvalidAttributeTypeException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getUUID();
			fail("getUUID: the exception InvalidAttributeTypeException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getURI();
			fail("getURI: the exception InvalidAttributeTypeException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}

		try {
			attr.getURL();
			fail("getURL: the exception InvalidAttributeTypeException was not thrown for "+type); //$NON-NLS-1$
		}
		catch(AttributeException _) {
			// expected case
		}
	}

	/**
	 * @param attr
	 * @param methodName
	 * @throws Exception
	 */
	protected static void assertAttributeException(AttributeValue attr, String methodName) throws Exception {
		try {
			Class<? extends AttributeValue> clazz = attr.getClass();
			Method method = clazz.getMethod(methodName);
			method.invoke(attr);
			fail("the exception AttributeException was not thrown"); //$NON-NLS-1$
		}
		catch(InvocationTargetException e) {
			Throwable ex = e.getTargetException();
			if (ex instanceof AssertionError) {
				throw (AssertionError)ex;
			}
			if (ex instanceof AttributeException) {
				//
			}
			else {
				fail("the exception AttributeException was not thrown"); //$NON-NLS-1$
			}
		}
	}

	/**
	 */
	public static void testAttributeValueImpl() {
		AttributeValue attr = new AttributeValueImpl();
		
		assertFalse(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(AttributeType.OBJECT, attr.getType());
		
		try {
			attr.getValue();
			fail("the exception AttributeNotInitializedException was not thrown"); //$NON-NLS-1$
		}
		catch(AttributeNotInitializedException _) {
			// expected case
		}
		catch(InvalidAttributeTypeException _) {
			fail("unexpected exception InvalidAttributeTypeException"); //$NON-NLS-1$
		}

		try {
			attr.getBoolean();
			fail("the exception AttributeNotInitializedException was not thrown"); //$NON-NLS-1$
		}
		catch(AttributeNotInitializedException _) {
			// expected case
		}
		catch(InvalidAttributeTypeException _) {
			fail("unexpected exception InvalidAttributeTypeException"); //$NON-NLS-1$
		}
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplAttributeType() throws Exception {
		AttributeType[] values = AttributeType.values();
		//AttributeType[] values = new AttributeType[] {AttributeType.OBJECT};
		for (AttributeType type : values) {
			AttributeValue attr = new AttributeValueImpl(type);
			
			assertEquals(type, attr.getType());

			assertFalse(attr.isAssigned());
			assertEquals(type.isBaseType(),attr.isBaseType());
			assertEquals("on type "+type, //$NON-NLS-1$
					!type.isBaseType(),
					attr.isObjectValue());
			
			if (type.isNullAllowed()) {
				assertAttributeException(attr, "getBoolean"); //$NON-NLS-1$
				assertAttributeException(attr, "getColor"); //$NON-NLS-1$
				assertAttributeException(attr, "getDate"); //$NON-NLS-1$
				assertAttributeException(attr, "getImage"); //$NON-NLS-1$
				assertAttributeException(attr, "getInteger"); //$NON-NLS-1$
				assertNull(attr.getJavaObject());
				assertAttributeException(attr, "getPoint"); //$NON-NLS-1$
				assertAttributeException(attr, "getPoint3D"); //$NON-NLS-1$
				assertAttributeException(attr, "getPolyline"); //$NON-NLS-1$
				assertAttributeException(attr, "getPolyline3D"); //$NON-NLS-1$
				assertAttributeException(attr, "getReal"); //$NON-NLS-1$
				assertAttributeException(attr, "getString"); //$NON-NLS-1$
				assertAttributeException(attr, "getTimestamp"); //$NON-NLS-1$
				assertAttributeException(attr, "getURI"); //$NON-NLS-1$
				assertAttributeException(attr, "getURL"); //$NON-NLS-1$
				assertAttributeException(attr, "getUUID"); //$NON-NLS-1$
				assertAttributeException(attr, "getValue"); //$NON-NLS-1$
			}
			else {
				assertAllGetFailed(attr, type);
			}
		}
	}
	
	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplBoolean() throws Exception {
		AttributeValue attr = new AttributeValueImpl(false);
		
		assertEquals(AttributeType.BOOLEAN, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertFalse((Boolean)attr.getValue());
		assertFalse(attr.getBoolean());
		assertAttributeException(attr,"getColor"); //$NON-NLS-1$
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertEquals(0, attr.getInteger());
		assertEquals(0., attr.getReal());
		assertEquals(0, attr.getTimestamp());
		assertEquals(Boolean.toString(false),attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertAttributeException(attr,"getPoint"); //$NON-NLS-1$
		assertAttributeException(attr,"getPoint3D"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline3D"); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplColor() throws Exception {
		String txt = "255;0;0;255"; //$NON-NLS-1$
		AttributeValue attr = new AttributeValueImpl(Colors.RED);
		
		assertEquals(AttributeType.COLOR, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(Colors.RED,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertEquals(Colors.RED,attr.getColor());
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertEquals(Colors.RED.getRGB(), attr.getInteger());
		assertEquals((double)Colors.RED.getRGB(), attr.getReal());
		assertEquals(Colors.RED.getRGB(), attr.getTimestamp());
		assertEquals(txt,attr.getString());
		assertEquals(Colors.RED, attr.getJavaObject());
		assertEquals(new Point2f(255,0),attr.getPoint());
		assertEquals(new Point3f(255,0,0),attr.getPoint3D());
		assertAttributeException(attr,"getPolyline"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline3D"); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplDate() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
		String txt = fmt.format(currentDate);
		AttributeValue attr = new AttributeValueImpl(currentDate);
		
		assertEquals(AttributeType.DATE, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(currentDate,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertEquals(VectorToolkit.color((int)currentDate.getTime()), attr.getColor());
		assertEquals(currentDate,attr.getDate());
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertEquals(currentDate.getTime(),attr.getInteger());
		assertEquals((double)currentDate.getTime(),attr.getReal());
		assertEquals(currentDate.getTime(),attr.getTimestamp());
		assertEquals(txt,attr.getString());
		assertEquals(currentDate, attr.getJavaObject());
		assertEquals(new Point2f(currentDate.getTime(), 0), attr.getPoint());
		assertEquals(new Point3f(currentDate.getTime(), 0, 0), attr.getPoint3D());
		assertAttributeException(attr,"getPolyline"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline3D"); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplFloat() throws Exception {
		float nb = (float)Math.random();
		String txt = Double.toString(nb);
		AttributeValue attr = new AttributeValueImpl(nb);
		
		assertEquals(AttributeType.REAL, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(nb,((Number)attr.getValue()).floatValue());
		assertEquals(nb!=0f, attr.getBoolean());
		assertEquals(VectorToolkit.color((int)nb), attr.getColor());
		assertEquals(new Date((long)nb),attr.getDate());
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertEquals((long)nb,attr.getInteger());
		assertEquals((double)nb,attr.getReal());
		assertEquals((long)nb,attr.getTimestamp());
		assertEquals(txt,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertEquals(new Point2f(nb,0),attr.getPoint());
		assertEquals(new Point3f(nb,0,0),attr.getPoint3D());
		assertAttributeException(attr,"getPolyline"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline3D"); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplDouble() throws Exception {
		double nb = Math.random();
		String txt = Double.toString(nb);
		AttributeValue attr = new AttributeValueImpl(nb);
		
		assertEquals(AttributeType.REAL, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(nb,((Number)attr.getValue()).doubleValue());
		assertEquals(nb!=0., attr.getBoolean());
		assertEquals(VectorToolkit.color((int)nb), attr.getColor());
		assertEquals(new Date((long)nb),attr.getDate());
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertEquals((long)nb,attr.getInteger());
		assertEquals(nb,attr.getReal());
		assertEquals((long)nb,attr.getTimestamp());
		assertEquals(txt,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertEquals(new Point2f(nb,0),attr.getPoint());
		assertEquals(new Point3f(nb,0,0),attr.getPoint3D());
		assertAttributeException(attr,"getPolyline"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline3D"); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplIcon() throws Exception {
		Image ic = VectorToolkit.image(1,1,false);
		AttributeValue attr = new AttributeValueImpl(ic);
		
		assertEquals(AttributeType.IMAGE, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(ic,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertAttributeException(attr,"getColor"); //$NON-NLS-1$
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertEquals(ic,attr.getImage());
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertAttributeException(attr,"getReal"); //$NON-NLS-1$
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(ic.toString(), attr.getString());
		assertEquals(ic,attr.getJavaObject());
		assertAttributeException(attr,"getPoint"); //$NON-NLS-1$
		assertAttributeException(attr,"getPoint3D"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline3D"); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplInt() throws Exception {
		int nb = new Random().nextInt();
		String txt = Long.toString(nb);
		AttributeValue attr = new AttributeValueImpl(nb);
		
		assertEquals(AttributeType.INTEGER, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(nb,((Number)attr.getValue()).intValue());
		assertEquals(nb!=0, attr.getBoolean());
		assertEquals(VectorToolkit.color(nb), attr.getColor());
		assertEquals(new Date(nb),attr.getDate());
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertEquals(nb,attr.getInteger());
		assertEquals(nb,(int)attr.getReal());
		assertEquals(nb,attr.getTimestamp());
		assertEquals(txt,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertEquals(new Point2f(nb,0),attr.getPoint());
		assertEquals(new Point3f(nb,0,0),attr.getPoint3D());
		assertAttributeException(attr,"getPolyline"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline3D"); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplLong() throws Exception {
		long nb = new Random().nextLong();
		String txt = Long.toString(nb);
		AttributeValue attr = new AttributeValueImpl(nb);
		
		assertEquals(AttributeType.INTEGER, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(nb,((Number)attr.getValue()).longValue());
		assertEquals(nb!=0, attr.getBoolean());
		assertEquals(VectorToolkit.color((int)nb), attr.getColor());
		assertEquals(new Date(nb),attr.getDate());
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertEquals(nb,attr.getInteger());
		assertEquals((double)nb,attr.getReal());
		assertEquals(nb,attr.getTimestamp());
		assertEquals(txt,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertEquals(new Point2f(nb,0),attr.getPoint());
		assertEquals(new Point3f(nb,0,0),attr.getPoint3D());
		assertAttributeException(attr,"getPolyline"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline3D"); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplPoint2d() throws Exception {
		Point2f pt = new Point2f((float)Math.random(),(float)Math.random());
		Point3f pt3d = new Point3f(pt.getX(),pt.getY(),0);
		String str = pt.getX()+";"+pt.getY(); //$NON-NLS-1$
		AttributeValue attr = new AttributeValueImpl(pt);
		
		assertEquals(AttributeType.POINT, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(pt,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertEquals(VectorToolkit.color(pt.getX(),pt.getY(),0f),attr.getColor());
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertAttributeException(attr,"getReal"); //$NON-NLS-1$
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertEquals(pt, attr.getJavaObject());
		assertEquals(pt,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}
	
	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplDoubleDouble() throws Exception {
		float x = (float)Math.random();
		float y = (float)Math.random();
		Point2f pt = new Point2f(x,y);
		Point3f pt3d = new Point3f(x,y,0);
		String str = x+";"+y; //$NON-NLS-1$
		AttributeValue attr = new AttributeValueImpl(x,y);
		
		assertEquals(AttributeType.POINT, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(pt,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertEquals(VectorToolkit.color(pt.getX(),pt.getY(),0f),attr.getColor());
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertAttributeException(attr,"getReal"); //$NON-NLS-1$
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertEquals(pt, attr.getJavaObject());
		assertEquals(pt,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}
	
	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplPoint3d() throws Exception {
		float x = (float)Math.random();
		float y = (float)Math.random();
		float z = (float)Math.random();
		Point3f pt = new Point3f(x,y,z);
		Point2f pt2d = new Point2f(x,y);
		String str = x+";"+y+";"+z; //$NON-NLS-1$ //$NON-NLS-2$
		AttributeValue attr = new AttributeValueImpl(pt);
		
		assertEquals(AttributeType.POINT3D, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(pt,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertEquals(VectorToolkit.color(pt.getX(),pt.getY(),pt.getZ()),attr.getColor());
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertAttributeException(attr,"getReal"); //$NON-NLS-1$
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertEquals(pt, attr.getJavaObject());
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt},attr.getPolyline3D());
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplDoubleDoubleDouble() throws Exception {
		double x = Math.random();
		double y = Math.random();
		double z = Math.random();
		Point3f pt = new Point3f(x,y,z);
		Point2f pt2d = new Point2f(x,y);
		String str = ((float)x)+";"+((float)y)+";"+((float)z); //$NON-NLS-1$ //$NON-NLS-2$
		AttributeValue attr = new AttributeValueImpl(x,y,z);
		
		assertEquals(AttributeType.POINT3D, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(pt,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertEquals(VectorToolkit.color(pt.getX(),pt.getY(),pt.getZ()),attr.getColor());
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertAttributeException(attr,"getReal"); //$NON-NLS-1$
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertEquals(pt, attr.getJavaObject());
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt},attr.getPolyline3D());
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplString_random() throws Exception {
		double x = Math.random();
		Point2f pt2d = new Point2f(x,0);
		Point3f pt3d = new Point3f(x,0,0);
		String str = Double.toHexString(x);
		AttributeValue attr = new AttributeValueImpl(str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertAttributeException(attr,"getColor"); //$NON-NLS-1$
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertEquals(x,attr.getReal());
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplString_boolean() throws Exception {
		String str = Boolean.toString(true);
		AttributeValue attr = new AttributeValueImpl(str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertTrue(attr.getBoolean());
		assertAttributeException(attr,"getColor"); //$NON-NLS-1$
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertAttributeException(attr,"getReal"); //$NON-NLS-1$
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertAttributeException(attr,"getPoint"); //$NON-NLS-1$
		assertAttributeException(attr,"getPoint3D"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline3D"); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplString_Color() throws Exception {
		Color c = Colors.RED;
		Point2D pt2d = new Point2f(c.getRed(),c.getGreen());
		Point2D pt2d2 = new Point2f(c.getBlue(),0);
		Point3D pt3d = new Point3f(c.getRed(),c.getGreen(),c.getBlue());
		String str = c.getRed()+";"+c.getGreen()+";"+c.getBlue();  //$NON-NLS-1$//$NON-NLS-2$
		AttributeValue attr = new AttributeValueImpl(str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertEquals(c,attr.getColor());
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertAttributeException(attr,"getReal"); //$NON-NLS-1$
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d,pt2d2},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplString_Date() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
		String str = format.format(currentDate); 
		AttributeValue attr = new AttributeValueImpl(str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertAttributeException(attr,"getColor"); //$NON-NLS-1$
		assertEpsilonEquals(currentDate,attr.getDate());
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertAttributeException(attr,"getReal"); //$NON-NLS-1$
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertAttributeException(attr,"getPoint"); //$NON-NLS-1$
		assertAttributeException(attr,"getPoint3D"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline3D"); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplString_JDate() throws Exception {
		Date currentDate = new Date();
		String str = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL).format(currentDate); 
		AttributeValue attr = new AttributeValueImpl(str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertAttributeException(attr,"getColor"); //$NON-NLS-1$
		assertEpsilonEquals(currentDate,attr.getDate());
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertAttributeException(attr,"getReal"); //$NON-NLS-1$
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertAttributeException(attr,"getPoint"); //$NON-NLS-1$
		assertAttributeException(attr,"getPoint3D"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline"); //$NON-NLS-1$
		assertAttributeException(attr,"getPolyline3D"); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplString_Integer() throws Exception {
		int nb = new Random().nextInt(20000)+256;
		Point2D pt2d = new Point2f(nb,0);
		Point3D pt3d = new Point3f(nb,0,0);
		String str = Integer.toString(nb);
		AttributeValue attr = new AttributeValueImpl(str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertAttributeException(attr,"getColor"); //$NON-NLS-1$
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertEquals(nb,attr.getInteger());
		assertEquals((double)nb,attr.getReal());
		assertEquals(nb,attr.getTimestamp());
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplString_Long() throws Exception {
		long nb = new Random().nextInt(20000)+256;
		Point2D pt2d = new Point2f(nb,0);
		Point3D pt3d = new Point3f(nb,0,0);
		String str = Long.toString(nb);
		AttributeValue attr = new AttributeValueImpl(str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertAttributeException(attr,"getColor"); //$NON-NLS-1$
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertEquals(nb,attr.getInteger());
		assertEquals((double)nb,attr.getReal());
		assertEquals(nb,attr.getTimestamp());
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplString_Double() throws Exception {
		double nb = Math.random()+256;
		Point2D pt2d = new Point2f(nb,0);
		Point3D pt3d = new Point3f(nb,0,0);
		String str = Double.toString(nb);
		AttributeValue attr = new AttributeValueImpl(str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertAttributeException(attr,"getColor"); //$NON-NLS-1$
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertEquals(nb,attr.getReal());
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplString_Point2D() throws Exception {
		double x = Math.random()+256;
		double y = Math.random()+256;
		Point2D pt2d = new Point2f(x,y);
		Point3D pt3d = new Point3f(x,y,0);
		String str = x+";"+y; //$NON-NLS-1$
		AttributeValue attr = new AttributeValueImpl(str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertAttributeException(attr,"getColor"); //$NON-NLS-1$
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertAttributeException(attr,"getReal"); //$NON-NLS-1$
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplString_Point3D() throws Exception {
		double x = Math.random()+256;
		double y = Math.random()+256;
		double z = Math.random()+256;
		Point2D pt2d = new Point2f(x,y);
		Point2D pt2d2 = new Point2f(z,0);
		Point3D pt3d = new Point3f(x,y,z);
		String str = x+";"+y+";"+z; //$NON-NLS-1$ //$NON-NLS-2$
		AttributeValue attr = new AttributeValueImpl(str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertAttributeException(attr,"getColor"); //$NON-NLS-1$
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertAttributeException(attr,"getReal"); //$NON-NLS-1$
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); //$NON-NLS-1$
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d,pt2d2},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplPoint2DArray() throws Exception {
		double x1 = Math.random();
		double y1 = Math.random();
		double x2 = Math.random();
		double y2 = Math.random();

		Point2D pt1 = new Point2f(x1,y1);
		Point2D pt2 = new Point2f(x2,y2);
		
		Point2D[] list = new Point2D[]{ pt1, pt2 };
		Point3D[] list2 = new Point3D[]{ new Point3f(x1,y1,0), new Point3f(x2,y2,0) };

		String str = ((float)x1)+";"+((float)y1)+";"+((float)x2)+";"+((float)y2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		AttributeValue attr = new AttributeValueImpl(list);
		
		assertEquals(AttributeType.POLYLINE, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(list,(Point2D[])attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertAttributeException(attr,"getColor"); //$NON-NLS-1$
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertAttributeException(attr,"getReal"); //$NON-NLS-1$
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertTrue(Arrays.equals(list, (Point2D[])attr.getJavaObject()));
		assertAttributeException(attr,"getPoint"); //$NON-NLS-1$
		assertAttributeException(attr,"getPoint3D"); //$NON-NLS-1$
		assertEquals(list,attr.getPolyline());
		assertEquals(list2,attr.getPolyline3D());
	}

	/**
	 * @throws Exception
	 */
	public static void testAttributeValueImplPoint3DArray() throws Exception {
		double x1 = Math.random();
		double y1 = Math.random();
		double z1 = Math.random();
		double x2 = Math.random();
		double y2 = Math.random();
		double z2 = Math.random();

		Point3D pt1 = new Point3f(x1,y1,z1);
		Point3D pt2 = new Point3f(x2,y2,z2);
		
		Point3D[] list = new Point3D[]{ pt1, pt2 };
		Point2D[] list2 = new Point2D[]{ new Point2f(x1,y1), new Point2f(x2,y2) };

		String str = ((float)x1)+";"+((float)y1)+";"+((float)z1)+";"+((float)x2)+";"+((float)y2)+";"+((float)z2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		AttributeValue attr = new AttributeValueImpl(list);
		
		assertEquals(AttributeType.POLYLINE3D, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(list,(Point3D[])attr.getValue());
		assertAttributeException(attr,"getBoolean"); //$NON-NLS-1$
		assertAttributeException(attr,"getColor"); //$NON-NLS-1$
		assertAttributeException(attr,"getDate"); //$NON-NLS-1$
		assertAttributeException(attr,"getImage"); //$NON-NLS-1$
		assertAttributeException(attr,"getInteger"); //$NON-NLS-1$
		assertAttributeException(attr,"getReal"); //$NON-NLS-1$
		assertAttributeException(attr,"getTimestamp"); //$NON-NLS-1$
		assertEquals(str,attr.getString());
		assertTrue(Arrays.equals(list, (Point3D[])attr.getJavaObject()));
		assertAttributeException(attr,"getPoint"); //$NON-NLS-1$
		assertAttributeException(attr,"getPoint3D"); //$NON-NLS-1$
		assertEquals(list2,attr.getPolyline());
		assertEquals(list,attr.getPolyline3D());
	}

	/**
	 * @throws AttributeException
	 */
	public static void testCast() throws AttributeException {
		AttributeValue attr1, attr2;
		String msg, str;
		AttributeType source, target;
		long time;
		Color col;
		Date dt;
		DateFormat format;
		Point2D pt2d;
		Point3D pt3d;
		
		//
		// SOURCE: BOOLEAN
		//
		
		source = AttributeType.BOOLEAN;
		target = AttributeType.BOOLEAN;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getBoolean());

		source = AttributeType.BOOLEAN;
		target = AttributeType.COLOR;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getColor());

		source = AttributeType.BOOLEAN;
		target = AttributeType.DATE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,((Date)attr2.getValue()).getTime()<=time);
		assertTrue(msg,attr2.getDate().getTime()<=time);

		source = AttributeType.BOOLEAN;
		target = AttributeType.IMAGE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertException(msg, AttributeNotInitializedException.class, attr2, "getImage"); //$NON-NLS-1$

		source = AttributeType.BOOLEAN;
		target = AttributeType.INTEGER;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getInteger());

		source = AttributeType.BOOLEAN;
		target = AttributeType.OBJECT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getJavaObject());

		source = AttributeType.BOOLEAN;
		target = AttributeType.POINT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint());

		source = AttributeType.BOOLEAN;
		target = AttributeType.POINT3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint3D());

		source = AttributeType.BOOLEAN;
		target = AttributeType.POLYLINE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), attr2.getPolyline());

		source = AttributeType.BOOLEAN;
		target = AttributeType.POLYLINE3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), attr2.getPolyline3D());

		source = AttributeType.BOOLEAN;
		target = AttributeType.REAL;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getReal());

		source = AttributeType.BOOLEAN;
		target = AttributeType.STRING;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,Boolean.toString((Boolean)AttributeType.BOOLEAN.getDefaultValue()), attr2.getValue());
		assertEquals(msg,Boolean.toString((Boolean)AttributeType.BOOLEAN.getDefaultValue()), attr2.getString());

		source = AttributeType.BOOLEAN;
		target = AttributeType.TIMESTAMP;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,attr2.getValue() instanceof Timestamp);
		time = System.currentTimeMillis();
		assertTrue(msg,((Number)attr2.getValue()).longValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

		//
		// SOURCE: COLOR
		//
		
		source = AttributeType.COLOR;
		target = AttributeType.BOOLEAN;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getBoolean());

		source = AttributeType.COLOR;
		target = AttributeType.COLOR;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getColor());

		source = AttributeType.COLOR;
		target = AttributeType.DATE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,((Date)attr2.getValue()).getTime()<=time);
		assertTrue(msg,attr2.getDate().getTime()<=time);

		source = AttributeType.COLOR;
		target = AttributeType.IMAGE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertException(msg, AttributeNotInitializedException.class, attr2, "getImage"); //$NON-NLS-1$

		source = AttributeType.COLOR;
		target = AttributeType.INTEGER;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,((Color)source.getDefaultValue()).getRGB(), ((Long)attr2.getValue()).intValue());
		assertEquals(msg,((Color)source.getDefaultValue()).getRGB(), (int)attr2.getInteger());

		source = AttributeType.COLOR;
		target = AttributeType.OBJECT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,VectorToolkit.color(0,0,0), attr2.getValue());
		assertEquals(msg,VectorToolkit.color(0,0,0), attr2.getJavaObject());

		source = AttributeType.COLOR;
		target = AttributeType.POINT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint());

		source = AttributeType.COLOR;
		target = AttributeType.POINT3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint3D());

		source = AttributeType.COLOR;
		target = AttributeType.POLYLINE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), attr2.getPolyline());

		source = AttributeType.COLOR;
		target = AttributeType.POLYLINE3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), attr2.getPolyline3D());

		source = AttributeType.COLOR;
		target = AttributeType.REAL;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,((Color)source.getDefaultValue()).getRGB(), ((Number)attr2.getValue()).intValue());
		assertEquals(msg,((Color)source.getDefaultValue()).getRGB(), (int)attr2.getReal());

		source = AttributeType.COLOR;
		target = AttributeType.STRING;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		col = (Color)source.getDefaultValue();
		str = col.getRed()+";"+col.getGreen()+";"+col.getBlue()+";"+col.getAlpha(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals(msg,str, attr2.getValue());
		assertEquals(msg,str, attr2.getString());

		source = AttributeType.COLOR;
		target = AttributeType.TIMESTAMP;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,attr2.getValue() instanceof Timestamp);
		time = System.currentTimeMillis();
		assertTrue(msg,((Number)attr2.getValue()).longValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

		//
		// SOURCE: DATE
		//
		
		source = AttributeType.DATE;
		target = AttributeType.BOOLEAN;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getBoolean());

		source = AttributeType.DATE;
		target = AttributeType.DATE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,((Date)attr2.getValue()).getTime()<=time);
		assertTrue(msg,attr2.getDate().getTime()<=time);

		source = AttributeType.DATE;
		target = AttributeType.IMAGE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertException(msg, AttributeNotInitializedException.class, attr2, "getImage"); //$NON-NLS-1$

		source = AttributeType.DATE;
		target = AttributeType.INTEGER;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		dt = attr2.getDate();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,dt.getTime(), attr2.getValue());
		assertEquals(msg,dt.getTime(), attr2.getInteger());

		source = AttributeType.DATE;
		target = AttributeType.OBJECT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertNotNull(msg, attr2.getValue());
		assertNotNull(msg,attr2.getJavaObject());

		source = AttributeType.DATE;
		target = AttributeType.POINT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		Point2D pt = attr2.getPoint();
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,pt, attr2.getValue());
		assertEquals(msg,pt, attr2.getPoint());

		source = AttributeType.DATE;
		target = AttributeType.POINT3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		Point3D pt3 = attr2.getPoint3D();
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,pt3, attr2.getValue());
		assertEquals(msg,pt3, attr2.getPoint3D());

		source = AttributeType.DATE;
		target = AttributeType.POLYLINE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), attr2.getPolyline());

		source = AttributeType.DATE;
		target = AttributeType.POLYLINE3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), attr2.getPolyline3D());

		source = AttributeType.DATE;
		target = AttributeType.REAL;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		dt = attr2.getDate();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,dt.getTime(), ((Double)attr2.getValue()).longValue());
		assertEquals(msg,dt.getTime(), (long)attr2.getReal());

		source = AttributeType.DATE;
		target = AttributeType.STRING;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		dt = (Date)source.getDefaultValue();
		format = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
		str = format.format(dt);
		assertEquals(msg,str, attr2.getValue());
		assertEquals(msg,str, attr2.getString());

		source = AttributeType.DATE;
		target = AttributeType.TIMESTAMP;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,attr2.getValue() instanceof Timestamp);
		time = System.currentTimeMillis();
		assertTrue(msg,((Number)attr2.getValue()).longValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

		//
		// SOURCE: ICON
		//
		
		source = AttributeType.IMAGE;
		target = AttributeType.BOOLEAN;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getBoolean());

		source = AttributeType.IMAGE;
		target = AttributeType.COLOR;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getColor());

		source = AttributeType.IMAGE;
		target = AttributeType.DATE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,((Date)attr2.getValue()).getTime()<=time);
		assertTrue(msg,attr2.getDate().getTime()<=time);

		source = AttributeType.IMAGE;
		target = AttributeType.IMAGE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertException(msg, AttributeNotInitializedException.class, attr2, "getImage"); //$NON-NLS-1$

		source = AttributeType.IMAGE;
		target = AttributeType.INTEGER;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(),attr2.getValue());
		assertEquals(msg,target.getDefaultValue(),attr2.getInteger());

		source = AttributeType.IMAGE;
		target = AttributeType.OBJECT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertNull(msg,attr2.getValue());
		assertNull(msg,attr2.getJavaObject());

		source = AttributeType.IMAGE;
		target = AttributeType.POINT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint());

		source = AttributeType.IMAGE;
		target = AttributeType.POINT3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint3D());

		source = AttributeType.IMAGE;
		target = AttributeType.POLYLINE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), attr2.getPolyline());

		source = AttributeType.IMAGE;
		target = AttributeType.POLYLINE3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), attr2.getPolyline3D());

		source = AttributeType.IMAGE;
		target = AttributeType.REAL;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getReal());

		source = AttributeType.IMAGE;
		target = AttributeType.STRING;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue().toString(), attr2.getString());

		source = AttributeType.IMAGE;
		target = AttributeType.TIMESTAMP;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,attr2.getValue() instanceof Timestamp);
		time = System.currentTimeMillis();
		assertTrue(msg,((Number)attr2.getValue()).longValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

		//
		// SOURCE: INTEGER
		//
		
		source = AttributeType.INTEGER;
		target = AttributeType.BOOLEAN;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getBoolean());

		source = AttributeType.INTEGER;
		target = AttributeType.COLOR;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getColor());

		source = AttributeType.INTEGER;
		target = AttributeType.DATE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,((Date)attr2.getValue()).getTime()<=time);
		assertTrue(msg,attr2.getDate().getTime()<=time);

		source = AttributeType.INTEGER;
		target = AttributeType.IMAGE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertException(msg, AttributeNotInitializedException.class, attr2, "getImage"); //$NON-NLS-1$

		source = AttributeType.INTEGER;
		target = AttributeType.INTEGER;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(),attr2.getValue());
		assertEquals(msg,target.getDefaultValue(),attr2.getInteger());

		source = AttributeType.INTEGER;
		target = AttributeType.OBJECT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertNull(msg,attr2.getValue());
		assertNull(msg,attr2.getJavaObject());

		source = AttributeType.INTEGER;
		target = AttributeType.POINT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint());

		source = AttributeType.INTEGER;
		target = AttributeType.POINT3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint3D());

		source = AttributeType.INTEGER;
		target = AttributeType.POLYLINE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), attr2.getPolyline());

		source = AttributeType.INTEGER;
		target = AttributeType.POLYLINE3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), attr2.getPolyline3D());

		source = AttributeType.INTEGER;
		target = AttributeType.REAL;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getReal());

		source = AttributeType.INTEGER;
		target = AttributeType.STRING;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,"0", attr2.getValue()); //$NON-NLS-1$
		assertEquals(msg,"0", attr2.getString()); //$NON-NLS-1$

		source = AttributeType.INTEGER;
		target = AttributeType.TIMESTAMP;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,attr2.getValue() instanceof Timestamp);
		time = System.currentTimeMillis();
		assertTrue(msg,((Number)attr2.getValue()).longValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

		//
		// SOURCE: OBJECT
		//
		
		source = AttributeType.OBJECT;
		target = AttributeType.BOOLEAN;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getBoolean());

		source = AttributeType.OBJECT;
		target = AttributeType.COLOR;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getColor());

		source = AttributeType.OBJECT;
		target = AttributeType.DATE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,((Date)attr2.getValue()).getTime()<=time);
		assertTrue(msg,attr2.getDate().getTime()<=time);

		source = AttributeType.OBJECT;
		target = AttributeType.IMAGE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertException(msg, AttributeNotInitializedException.class, attr2, "getImage"); //$NON-NLS-1$

		source = AttributeType.OBJECT;
		target = AttributeType.INTEGER;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(),attr2.getValue());
		assertException(msg,InvalidAttributeTypeException.class, attr2,"getImage"); //$NON-NLS-1$

		source = AttributeType.OBJECT;
		target = AttributeType.OBJECT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertNull(msg,attr2.getValue());
		assertNull(msg,attr2.getJavaObject());

		source = AttributeType.OBJECT;
		target = AttributeType.POINT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint());

		source = AttributeType.OBJECT;
		target = AttributeType.POINT3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint3D());

		source = AttributeType.OBJECT;
		target = AttributeType.POLYLINE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), attr2.getPolyline());

		source = AttributeType.OBJECT;
		target = AttributeType.POLYLINE3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), attr2.getPolyline3D());

		source = AttributeType.OBJECT;
		target = AttributeType.REAL;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getReal());

		source = AttributeType.OBJECT;
		target = AttributeType.STRING;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getString());

		source = AttributeType.OBJECT;
		target = AttributeType.TIMESTAMP;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,attr2.getValue() instanceof Timestamp);
		time = System.currentTimeMillis();
		assertTrue(msg,((Number)attr2.getValue()).longValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

		//
		// SOURCE: POINT
		//
		
		source = AttributeType.POINT;
		target = AttributeType.BOOLEAN;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getBoolean());

		source = AttributeType.POINT;
		target = AttributeType.COLOR;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getColor());

		source = AttributeType.POINT;
		target = AttributeType.DATE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,((Date)attr2.getValue()).getTime()<=time);
		assertTrue(msg,attr2.getDate().getTime()<=time);

		source = AttributeType.POINT;
		target = AttributeType.IMAGE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertException(msg, AttributeNotInitializedException.class, attr2, "getImage"); //$NON-NLS-1$

		source = AttributeType.POINT;
		target = AttributeType.INTEGER;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(),attr2.getValue());
		assertEquals(msg,target.getDefaultValue(),attr2.getInteger());

		source = AttributeType.POINT;
		target = AttributeType.OBJECT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,new Point2f(), attr2.getValue());
		assertEquals(msg,new Point2f(), attr2.getJavaObject());

		source = AttributeType.POINT;
		target = AttributeType.POINT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint());

		source = AttributeType.POINT;
		target = AttributeType.POINT3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint3D());

		source = AttributeType.POINT;
		target = AttributeType.POLYLINE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,new Point2D[] {(Point2D)source.getDefaultValue()}, (Point2D[])attr2.getValue());
		assertEquals(msg,new Point2D[] {(Point2D)source.getDefaultValue()}, attr2.getPolyline());

		source = AttributeType.POINT;
		target = AttributeType.POLYLINE3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,new Point3D[] {(Point3D)AttributeType.POINT3D.getDefaultValue()}, (Point3D[])attr2.getValue());
		assertEquals(msg,new Point3D[] {(Point3D)AttributeType.POINT3D.getDefaultValue()}, attr2.getPolyline3D());

		source = AttributeType.POINT;
		target = AttributeType.REAL;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getReal());

		source = AttributeType.POINT;
		target = AttributeType.STRING;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		pt2d = (Point2D)source.getDefaultValue();
		str = pt2d.getX()+";"+pt2d.getY(); //$NON-NLS-1$
		assertEquals(msg,str, attr2.getValue());
		assertEquals(msg,str, attr2.getString());

		source = AttributeType.POINT;
		target = AttributeType.TIMESTAMP;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,attr2.getValue() instanceof Timestamp);
		time = System.currentTimeMillis();
		assertTrue(msg,((Number)attr2.getValue()).longValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

		//
		// SOURCE: POINT3D
		//
		
		source = AttributeType.POINT3D;
		target = AttributeType.BOOLEAN;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getBoolean());

		source = AttributeType.POINT3D;
		target = AttributeType.COLOR;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getColor());

		source = AttributeType.POINT3D;
		target = AttributeType.DATE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,((Date)attr2.getValue()).getTime()<=time);
		assertTrue(msg,attr2.getDate().getTime()<=time);

		source = AttributeType.POINT3D;
		target = AttributeType.IMAGE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertException(msg, AttributeNotInitializedException.class, attr2, "getImage"); //$NON-NLS-1$

		source = AttributeType.POINT3D;
		target = AttributeType.INTEGER;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(),attr2.getValue());
		assertEquals(msg,target.getDefaultValue(),attr2.getInteger());

		source = AttributeType.POINT3D;
		target = AttributeType.OBJECT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,new Point3f(), attr2.getValue());
		assertEquals(msg,new Point3f(), attr2.getJavaObject());

		source = AttributeType.POINT3D;
		target = AttributeType.POINT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint());

		source = AttributeType.POINT3D;
		target = AttributeType.POINT3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint3D());

		source = AttributeType.POINT3D;
		target = AttributeType.POLYLINE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,new Point2D[] {(Point2D)AttributeType.POINT.getDefaultValue()}, (Point2D[])attr2.getValue());
		assertEquals(msg,new Point2D[] {(Point2D)AttributeType.POINT.getDefaultValue()}, attr2.getPolyline());

		source = AttributeType.POINT3D;
		target = AttributeType.POLYLINE3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,new Point3D[] {(Point3D)AttributeType.POINT3D.getDefaultValue()}, (Point3D[])attr2.getValue());
		assertEquals(msg,new Point3D[] {(Point3D)AttributeType.POINT3D.getDefaultValue()}, attr2.getPolyline3D());

		source = AttributeType.POINT3D;
		target = AttributeType.REAL;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getReal());

		source = AttributeType.POINT3D;
		target = AttributeType.STRING;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		pt3d = (Point3D)source.getDefaultValue();
		str = pt2d.getX()+";"+pt2d.getY()+";"+pt3d.getZ(); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(msg,str, attr2.getValue());
		assertEquals(msg,str, attr2.getString());

		source = AttributeType.POINT3D;
		target = AttributeType.TIMESTAMP;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,attr2.getValue() instanceof Timestamp);
		time = System.currentTimeMillis();
		assertTrue(msg,((Number)attr2.getValue()).longValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

		//
		// SOURCE: POLYLINE
		//
		
		source = AttributeType.POLYLINE;
		target = AttributeType.BOOLEAN;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getBoolean());

		source = AttributeType.POLYLINE;
		target = AttributeType.COLOR;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getColor());

		source = AttributeType.POLYLINE;
		target = AttributeType.DATE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,((Date)attr2.getValue()).getTime()<=time);
		assertTrue(msg,attr2.getDate().getTime()<=time);

		source = AttributeType.POLYLINE;
		target = AttributeType.IMAGE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertException(msg, AttributeNotInitializedException.class, attr2, "getImage"); //$NON-NLS-1$

		source = AttributeType.POLYLINE;
		target = AttributeType.INTEGER;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(),attr2.getValue());
		assertEquals(msg,target.getDefaultValue(),attr2.getInteger());

		source = AttributeType.POLYLINE;
		target = AttributeType.OBJECT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,Arrays.equals(new Point2f[0], (Point2D[])attr2.getValue()));
		assertTrue(msg,Arrays.equals(new Point2f[0], (Point2D[])attr2.getJavaObject()));

		source = AttributeType.POLYLINE;
		target = AttributeType.POINT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint());

		source = AttributeType.POLYLINE;
		target = AttributeType.POINT3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint3D());

		source = AttributeType.POLYLINE;
		target = AttributeType.POLYLINE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point2D[])source.getDefaultValue(), (Point2D[])attr2.getValue());
		assertEquals(msg,(Point2D[])source.getDefaultValue(), attr2.getPolyline());
		
		source = AttributeType.POLYLINE;
		target = AttributeType.POLYLINE3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point3D[])AttributeType.POLYLINE3D.getDefaultValue(), (Point3D[])attr2.getValue());
		assertEquals(msg,(Point3D[])AttributeType.POLYLINE3D.getDefaultValue(), attr2.getPolyline3D());

		source = AttributeType.POLYLINE;
		target = AttributeType.REAL;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getReal());

		source = AttributeType.POLYLINE;
		target = AttributeType.STRING;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		str = ""; //$NON-NLS-1$
		assertEquals(msg,str, attr2.getValue());
		assertEquals(msg,str, attr2.getString());

		source = AttributeType.POLYLINE;
		target = AttributeType.TIMESTAMP;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,attr2.getValue() instanceof Timestamp);
		time = System.currentTimeMillis();
		assertTrue(msg,((Number)attr2.getValue()).longValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

		//
		// SOURCE: POLYLINE3D
		//
		
		source = AttributeType.POLYLINE3D;
		target = AttributeType.BOOLEAN;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getBoolean());

		source = AttributeType.POLYLINE3D;
		target = AttributeType.COLOR;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getColor());

		source = AttributeType.POLYLINE3D;
		target = AttributeType.DATE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,((Date)attr2.getValue()).getTime()<=time);
		assertTrue(msg,attr2.getDate().getTime()<=time);

		source = AttributeType.POLYLINE3D;
		target = AttributeType.IMAGE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertException(msg, AttributeNotInitializedException.class, attr2, "getImage"); //$NON-NLS-1$

		source = AttributeType.POLYLINE3D;
		target = AttributeType.INTEGER;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(),attr2.getValue());
		assertEquals(msg,target.getDefaultValue(),attr2.getInteger());

		source = AttributeType.POLYLINE3D;
		target = AttributeType.OBJECT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,Arrays.equals(new Point3f[0], (Point3D[])attr2.getValue()));
		assertTrue(msg,Arrays.equals(new Point3f[0], (Point3D[])attr2.getJavaObject()));

		source = AttributeType.POLYLINE3D;
		target = AttributeType.POINT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint());

		source = AttributeType.POLYLINE3D;
		target = AttributeType.POINT3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint3D());

		source = AttributeType.POLYLINE3D;
		target = AttributeType.POLYLINE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point2D[])AttributeType.POLYLINE.getDefaultValue(), (Point2D[])attr2.getValue());
		assertEquals(msg,(Point2D[])AttributeType.POLYLINE.getDefaultValue(), attr2.getPolyline());
		
		source = AttributeType.POLYLINE3D;
		target = AttributeType.POLYLINE3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point3D[])AttributeType.POLYLINE3D.getDefaultValue(), (Point3D[])attr2.getValue());
		assertEquals(msg,(Point3D[])AttributeType.POLYLINE3D.getDefaultValue(), attr2.getPolyline3D());

		source = AttributeType.POLYLINE3D;
		target = AttributeType.REAL;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getReal());

		source = AttributeType.POLYLINE3D;
		target = AttributeType.STRING;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		str = ""; //$NON-NLS-1$
		assertEquals(msg,str, attr2.getValue());
		assertEquals(msg,str, attr2.getString());

		source = AttributeType.POLYLINE3D;
		target = AttributeType.TIMESTAMP;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,attr2.getValue() instanceof Timestamp);
		time = System.currentTimeMillis();
		assertTrue(msg,((Number)attr2.getValue()).longValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

		//
		// SOURCE: REAL
		//
		
		source = AttributeType.REAL;
		target = AttributeType.BOOLEAN;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getBoolean());

		source = AttributeType.REAL;
		target = AttributeType.COLOR;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getColor());

		source = AttributeType.REAL;
		target = AttributeType.DATE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,((Date)attr2.getValue()).getTime()<=time);
		assertTrue(msg,attr2.getDate().getTime()<=time);

		source = AttributeType.REAL;
		target = AttributeType.IMAGE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertException(msg, AttributeNotInitializedException.class, attr2, "getImage"); //$NON-NLS-1$

		source = AttributeType.REAL;
		target = AttributeType.INTEGER;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(),attr2.getValue());
		assertEquals(msg,target.getDefaultValue(),attr2.getInteger());

		source = AttributeType.REAL;
		target = AttributeType.OBJECT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertNull(msg,attr2.getValue());
		assertNull(msg,attr2.getJavaObject());

		source = AttributeType.REAL;
		target = AttributeType.POINT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint());

		source = AttributeType.REAL;
		target = AttributeType.POINT3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint3D());

		source = AttributeType.REAL;
		target = AttributeType.POLYLINE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), attr2.getPolyline());

		source = AttributeType.REAL;
		target = AttributeType.POLYLINE3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), attr2.getPolyline3D());

		source = AttributeType.REAL;
		target = AttributeType.REAL;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getReal());

		source = AttributeType.REAL;
		target = AttributeType.STRING;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,Double.toString(0), attr2.getValue());
		assertEquals(msg,Double.toString(0), attr2.getString());

		source = AttributeType.REAL;
		target = AttributeType.TIMESTAMP;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,attr2.getValue() instanceof Timestamp);
		time = System.currentTimeMillis();
		assertTrue(msg,((Number)attr2.getValue()).longValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

		//
		// SOURCE: STRING
		//
		
		source = AttributeType.STRING;
		target = AttributeType.BOOLEAN;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getBoolean());

		source = AttributeType.STRING;
		target = AttributeType.COLOR;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getColor());

		source = AttributeType.STRING;
		target = AttributeType.DATE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,((Date)attr2.getValue()).getTime()<=time);
		assertTrue(msg,attr2.getDate().getTime()<=time);

		source = AttributeType.STRING;
		target = AttributeType.IMAGE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertException(msg, AttributeNotInitializedException.class, attr2, "getImage"); //$NON-NLS-1$

		source = AttributeType.STRING;
		target = AttributeType.INTEGER;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(),attr2.getValue());
		assertEquals(msg,target.getDefaultValue(),attr2.getInteger());

		source = AttributeType.STRING;
		target = AttributeType.OBJECT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertNull(msg,attr2.getValue());
		assertNull(msg,attr2.getJavaObject());

		source = AttributeType.STRING;
		target = AttributeType.POINT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint());

		source = AttributeType.STRING;
		target = AttributeType.POINT3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getPoint3D());

		source = AttributeType.STRING;
		target = AttributeType.POLYLINE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), attr2.getPolyline());

		source = AttributeType.STRING;
		target = AttributeType.POLYLINE3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), attr2.getPolyline3D());

		source = AttributeType.STRING;
		target = AttributeType.REAL;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertEquals(msg,target.getDefaultValue(), attr2.getReal());

		source = AttributeType.STRING;
		target = AttributeType.STRING;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		str = ""; //$NON-NLS-1$
		assertEquals(msg,str, attr2.getValue());
		assertEquals(msg,str, attr2.getString());

		source = AttributeType.STRING;
		target = AttributeType.TIMESTAMP;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,attr2.getValue() instanceof Timestamp);
		time = System.currentTimeMillis();
		assertTrue(msg,((Number)attr2.getValue()).longValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

		//
		// SOURCE: TIMESTAMP
		//
		
		source = AttributeType.TIMESTAMP;
		target = AttributeType.BOOLEAN;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,true, attr2.getValue());
		assertEquals(msg,true, attr2.getBoolean());

		source = AttributeType.TIMESTAMP;
		target = AttributeType.DATE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,((Date)attr2.getValue()).getTime()<=time);
		assertTrue(msg,attr2.getDate().getTime()<=time);

		source = AttributeType.TIMESTAMP;
		target = AttributeType.IMAGE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,target.getDefaultValue(), attr2.getValue());
		assertException(msg, AttributeNotInitializedException.class, attr2, "getImage"); //$NON-NLS-1$

		source = AttributeType.TIMESTAMP;
		target = AttributeType.INTEGER;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		time = System.currentTimeMillis();
		assertTrue(msg,(Long)attr2.getValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

		source = AttributeType.TIMESTAMP;
		target = AttributeType.OBJECT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertNull(msg,attr2.getValue());
		assertNull(msg,attr2.getJavaObject());

		source = AttributeType.TIMESTAMP;
		target = AttributeType.POINT;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		time = attr2.getTimestamp();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,new Point2f(time,0), attr2.getValue());
		assertEquals(msg,new Point2f(time,0), attr2.getPoint());

		source = AttributeType.TIMESTAMP;
		target = AttributeType.POINT3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		time = attr2.getTimestamp();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,new Point3f(time,0,0), attr2.getValue());
		assertEquals(msg,new Point3f(time,0,0), attr2.getPoint3D());

		source = AttributeType.TIMESTAMP;
		target = AttributeType.POLYLINE;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), (Point2D[])attr2.getValue());
		assertEquals(msg,(Point2D[])target.getDefaultValue(), attr2.getPolyline());

		source = AttributeType.TIMESTAMP;
		target = AttributeType.POLYLINE3D;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), (Point3D[])attr2.getValue());
		assertEquals(msg,(Point3D[])target.getDefaultValue(), attr2.getPolyline3D());

		source = AttributeType.TIMESTAMP;
		target = AttributeType.REAL;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		time = attr2.getTimestamp();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertEquals(msg,Double.valueOf(time), attr2.getValue());
		assertEquals(msg,Long.valueOf(time).doubleValue(), attr2.getReal());

		source = AttributeType.TIMESTAMP;
		target = AttributeType.STRING;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();
		time = attr2.getTimestamp();
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		str = format.format(new Date(time));
		assertEquals(msg,str, attr2.getValue());
		assertEquals(msg,str, attr2.getString());

		source = AttributeType.TIMESTAMP;
		target = AttributeType.TIMESTAMP;
		msg = "from '"+source.toString()+"' to '"+target.toString()+"'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attr1 = new AttributeValueImpl(source);
		attr2 = new AttributeValueImpl(source);
		attr2.setToDefault();		
		attr1.cast(target);
		assertFalse(msg,attr1.isAssigned());
		attr2.cast(target);
		assertTrue(msg,attr2.isAssigned());
		assertTrue(msg,attr2.getValue() instanceof Timestamp);
		time = System.currentTimeMillis();
		assertTrue(msg,((Number)attr2.getValue()).longValue()<=time);
		assertTrue(msg,attr2.getTimestamp()<=time);

	}
	
	/**
	 */
	public static void testEquals() {
		AttributeValueImpl attr = new AttributeValueImpl();
		
		attr.setBoolean(true);
		assertTrue(attr.equals(new AttributeValueImpl(true)));
		assertFalse(attr.equals(new AttributeValueImpl(false)));
		assertTrue(attr.equals(true));
		assertFalse(attr.equals(false));
		assertTrue(attr.equals(new AttributeValueImpl("true"))); //$NON-NLS-1$
		assertFalse(attr.equals(new AttributeValueImpl("false"))); //$NON-NLS-1$
		assertTrue(attr.equals("true")); //$NON-NLS-1$
		assertFalse(attr.equals("false")); //$NON-NLS-1$
		assertFalse(attr.equals(new AttributeValueImpl(1.)));
		assertFalse(attr.equals(new AttributeValueImpl("1."))); //$NON-NLS-1$
		assertFalse(attr.equals(1.));
		assertFalse(attr.equals("toto")); //$NON-NLS-1$

		attr.setBoolean(false);
		assertFalse(attr.equals(new AttributeValueImpl(true)));
		assertTrue(attr.equals(new AttributeValueImpl(false)));
		assertFalse(attr.equals(true));
		assertTrue(attr.equals(false));
		assertFalse(attr.equals(new AttributeValueImpl("true"))); //$NON-NLS-1$
		assertTrue(attr.equals(new AttributeValueImpl("false"))); //$NON-NLS-1$
		assertFalse(attr.equals("true")); //$NON-NLS-1$
		assertTrue(attr.equals("false")); //$NON-NLS-1$
		assertFalse(attr.equals(new AttributeValueImpl(1.)));
		assertFalse(attr.equals(new AttributeValueImpl("1."))); //$NON-NLS-1$
		assertFalse(attr.equals(1.));
		assertFalse(attr.equals("toto")); //$NON-NLS-1$
	}
	
	/** 
	 */
	public static void testParse() {
		AttributeValueImpl v;
		
		v = AttributeValueImpl.parse("127.0.0.1"); //$NON-NLS-1$
		assertSame(AttributeType.INET_ADDRESS, v.getType());

		v = AttributeValueImpl.parse("localhost"); //$NON-NLS-1$
		assertSame(AttributeType.INET_ADDRESS, v.getType());

		v = AttributeValueImpl.parse("java.lang.String"); //$NON-NLS-1$
		assertSame(AttributeType.TYPE, v.getType());

		v = AttributeValueImpl.parse(AttributeType.class.getName()+"."+AttributeType.ENUMERATION.toString()); //$NON-NLS-1$
		assertSame(AttributeType.ENUMERATION, v.getType());

		v = AttributeValueImpl.parse("3eade434-b267-4ffa-a574-2e2cbff0151a"); //$NON-NLS-1$
		assertSame(AttributeType.UUID, v.getType());

		v = AttributeValueImpl.parse("134"); //$NON-NLS-1$
		assertSame(AttributeType.INTEGER, v.getType());

		v = AttributeValueImpl.parse("-134"); //$NON-NLS-1$
		assertSame(AttributeType.INTEGER, v.getType());

		v = AttributeValueImpl.parse("134e34"); //$NON-NLS-1$
		assertSame(AttributeType.REAL, v.getType());

		v = AttributeValueImpl.parse("-134.5"); //$NON-NLS-1$
		assertSame(AttributeType.REAL, v.getType());

		v = AttributeValueImpl.parse("2012-11-30 18:22:34"); //$NON-NLS-1$
		assertSame(AttributeType.DATE, v.getType());

		v = AttributeValueImpl.parse("Fri, 30 Nov 2012 18:22:42 +0100"); //$NON-NLS-1$
		assertSame(AttributeType.DATE, v.getType());

		v = AttributeValueImpl.parse("True"); //$NON-NLS-1$
		assertSame(AttributeType.BOOLEAN, v.getType());
		
		v = AttributeValueImpl.parse("False"); //$NON-NLS-1$
		assertSame(AttributeType.BOOLEAN, v.getType());

		v = AttributeValueImpl.parse("TrUe"); //$NON-NLS-1$
		assertSame(AttributeType.BOOLEAN, v.getType());

		v = AttributeValueImpl.parse("http://www.multiagent.fr"); //$NON-NLS-1$
		assertSame(AttributeType.URL, v.getType());

		v = AttributeValueImpl.parse("mailto:stephane.galland@utbm.fr"); //$NON-NLS-1$
		assertSame(AttributeType.URL, v.getType());

		v = AttributeValueImpl.parse("urn:isbn:096139210x"); //$NON-NLS-1$
		assertSame(AttributeType.URI, v.getType());

		v = AttributeValueImpl.parse("1;2;3;4;5;6;7;8;9"); //$NON-NLS-1$
		assertSame(AttributeType.POLYLINE3D, v.getType());

		v = AttributeValueImpl.parse("1;2;3;4;5;6;7;8"); //$NON-NLS-1$
		assertSame(AttributeType.POLYLINE, v.getType());

		v = AttributeValueImpl.parse("1;2;3;4"); //$NON-NLS-1$
		assertSame(AttributeType.COLOR, v.getType());

		v = AttributeValueImpl.parse("1;2;3"); //$NON-NLS-1$
		assertSame(AttributeType.COLOR, v.getType());

		v = AttributeValueImpl.parse("1;2;300"); //$NON-NLS-1$
		assertSame(AttributeType.POINT3D, v.getType());

		v = AttributeValueImpl.parse("1;2"); //$NON-NLS-1$
		assertSame(AttributeType.POINT, v.getType());

		v = AttributeValueImpl.parse("blablabla"); //$NON-NLS-1$
		assertSame(AttributeType.STRING, v.getType());
	}

}
