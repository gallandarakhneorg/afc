/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.attrs.attr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Point3D;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Colors;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.VectorToolkit;

/**
 * Test of Attribute.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@Ignore//TODO: fixcode
public class AttributeTest extends AbstractTestCase {

	/**
	 * @param attr
	 * @param type
	 */
	protected static void assertAllGetFailed(AttributeValue attr, AttributeType type) {
		try {
			attr.getValue();
			fail("getValue: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getBoolean();
			fail("getBoolean: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getColor();
			fail("getColor: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getDate();
			fail("getDate: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getImage();
			fail("getImage: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getInteger();
			fail("getInteger: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getJavaObject();
			if (type.isBaseType())
				fail("getJavaObject: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeNotInitializedException exception) {
			// expected case
		}
		catch(InvalidAttributeTypeException exception) {
			if (attr.isObjectValue())
				fail("getJavaObject: unexpected exception InvalidAttributeTypeException for "+type); 
		}

		try {
			attr.getPoint();
			fail("getPoint: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getPoint3D();
			fail("getPoint3D: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getPoint3D();
			fail("getPoint3D: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getPolyline3D();
			fail("getPolyline3D: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getReal();
			fail("getReal: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getString();
			fail("getString: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeNotInitializedException exception) {
			// expected case
		}
		catch(InvalidAttributeTypeException exception) {
			if (!attr.isObjectValue())
				fail("getString: unexpected exception InvalidAttributeTypeException for "+type); 
		}

		try {
			attr.getTimestamp();
			fail("getTimestamp: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getURI();
			fail("getURI: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getURL();
			fail("getURL: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
			// expected case
		}

		try {
			attr.getUUID();
			fail("getUUID: the exception AttributeNotInitializedException was not thrown for "+type); 
		}
		catch(AttributeException exception) {
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
			fail("the exception AttributeException was not thrown"); 
		}
		catch(InvocationTargetException e) {
			Throwable ex = e.getTargetException();
			if (ex instanceof AttributeException) {
				//
			}
			else {
				fail("the exception AttributeException was not thrown"); 
			}
		}
	}

	
	@Test
	public void attributeImpl() {
		Attribute attr = new AttributeImpl();
		
		assertFalse(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(AttributeType.OBJECT, attr.getType());
		
		try {
			attr.getValue();
			fail("the exception AttributeNotInitializedException was not thrown"); 
		}
		catch(AttributeNotInitializedException exception) {
			// expected case
		}
		catch(InvalidAttributeTypeException exception) {
			fail("unexpected exception InvalidAttributeTypeException"); 
		}

		try {
			attr.getBoolean();
			fail("the exception AttributeNotInitializedException was not thrown"); 
		}
		catch(AttributeException exception) {
			// expected case
		}
	}

	@Test
	public void attributeImplAttributeType() throws Exception {
		AttributeType[] values = AttributeType.values();
		//AttributeType[] values = new AttributeType[] {AttributeType.OBJECT};
		for (AttributeType type : values) {
			Attribute attr = new AttributeImpl(type);
			
			assertEquals(type, attr.getType());

			assertFalse(attr.isAssigned());
			assertEquals(type.isBaseType(),attr.isBaseType());
			assertEquals("on type "+type, 
					!type.isBaseType(),
					attr.isObjectValue());
			
			if (type.isNullAllowed()) {
				assertAttributeException(attr, "getBoolean"); 
				assertAttributeException(attr, "getColor"); 
				assertAttributeException(attr, "getDate"); 
				assertAttributeException(attr, "getImage"); 
				assertAttributeException(attr, "getInteger"); 
				assertNull(attr.getJavaObject());
				assertAttributeException(attr, "getPoint"); 
				assertAttributeException(attr, "getPoint3D"); 
				assertAttributeException(attr, "getPolyline"); 
				assertAttributeException(attr, "getPolyline3D"); 
				assertAttributeException(attr, "getReal"); 
				assertAttributeException(attr, "getString"); 
				assertAttributeException(attr, "getTimestamp"); 
				assertAttributeException(attr, "getURI"); 
				assertAttributeException(attr, "getURL"); 
				assertAttributeException(attr, "getUUID"); 
				assertAttributeException(attr, "getValue"); 
			}
			else {
				assertAllGetFailed(attr, type);
			}
		}
	}
	
	@Test
	public void attributeImplBoolean() throws Exception {
		Attribute attr = new AttributeImpl(randomString(),false);
		
		assertEquals(AttributeType.BOOLEAN, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertFalse((Boolean)attr.getValue());
		assertFalse(attr.getBoolean());
		assertAttributeException(attr,"getColor"); 
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertEquals(0, attr.getInteger());
		assertEquals(0., attr.getReal());
		assertEquals(0, attr.getTimestamp());
		assertEquals(Boolean.toString(false),attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertAttributeException(attr,"getPoint"); 
		assertAttributeException(attr,"getPoint3D"); 
		assertAttributeException(attr,"getPolyline"); 
		assertAttributeException(attr,"getPolyline3D"); 
	}

	@Test
	public void attributeImplColor() throws Exception {
		String txt = "255;0;0;255"; 
		Attribute attr = new AttributeImpl(randomString(),Colors.RED);
		
		assertEquals(AttributeType.COLOR, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(Colors.RED,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertEquals(Colors.RED,attr.getColor());
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertEquals(Colors.RED.getRGB(), attr.getInteger());
		assertEquals((double)Colors.RED.getRGB(), attr.getReal());
		assertEquals(Colors.RED.getRGB(), attr.getTimestamp());
		assertEquals(txt,attr.getString());
		assertEquals(VectorToolkit.color(255,0,0), attr.getJavaObject());
		assertEquals(new Point2f(255,0),attr.getPoint());
		assertEquals(new Point3f(255,0,0),attr.getPoint3D());
		assertAttributeException(attr,"getPolyline"); 
		assertAttributeException(attr,"getPolyline3D"); 
	}

	@Test
	public void attributeImplDate() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); 
		String txt = fmt.format(currentDate);
		Attribute attr = new AttributeImpl(randomString(),currentDate);
		
		assertEquals(AttributeType.DATE, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(currentDate,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertEquals(VectorToolkit.color((int)currentDate.getTime()), attr.getColor());
		assertEquals(currentDate,attr.getDate());
		assertAttributeException(attr,"getImage"); 
		assertEquals(currentDate.getTime(),attr.getInteger());
		assertEquals((double)currentDate.getTime(),attr.getReal());
		assertEquals(currentDate.getTime(),attr.getTimestamp());
		assertEquals(txt,attr.getString());
		assertEquals(currentDate, attr.getJavaObject());
		assertEquals(new Point2f(currentDate.getTime(), 0), attr.getPoint());
		assertEquals(new Point3f(currentDate.getTime(), 0, 0), attr.getPoint3D());
		assertAttributeException(attr,"getPolyline"); 
		assertAttributeException(attr,"getPolyline3D"); 
	}

	@Test
	public void attributeImplFloat() throws Exception {
		float nb = (float)Math.random();
		String txt = Double.toString(nb);
		Attribute attr = new AttributeImpl(randomString(),nb);
		
		assertEquals(AttributeType.REAL, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(nb,((Number)attr.getValue()).floatValue());
		assertEquals(nb!=0f, attr.getBoolean());
		assertEquals(VectorToolkit.color((int)nb), attr.getColor());
		assertEquals(new Date((long)nb),attr.getDate());
		assertAttributeException(attr,"getImage"); 
		assertEquals((long)nb,attr.getInteger());
		assertEquals((double)nb,attr.getReal());
		assertEquals((long)nb,attr.getTimestamp());
		assertEquals(txt,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertEquals(new Point2f(nb,0),attr.getPoint());
		assertEquals(new Point3f(nb,0,0),attr.getPoint3D());
		assertAttributeException(attr,"getPolyline"); 
		assertAttributeException(attr,"getPolyline3D"); 
	}

	@Test
	public void attributeImplDouble() throws Exception {
		double nb = Math.random();
		String txt = Double.toString(nb);
		Attribute attr = new AttributeImpl(randomString(),nb);
		
		assertEquals(AttributeType.REAL, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(nb,((Number)attr.getValue()).doubleValue());
		assertEquals(nb!=0., attr.getBoolean());
		assertEquals(VectorToolkit.color((int)nb), attr.getColor());
		assertEquals(new Date((long)nb),attr.getDate());
		assertAttributeException(attr,"getImage"); 
		assertEquals((long)nb,attr.getInteger());
		assertEquals(nb,attr.getReal());
		assertEquals((long)nb,attr.getTimestamp());
		assertEquals(txt,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertEquals(new Point2f(nb,0),attr.getPoint());
		assertEquals(new Point3f(nb,0,0),attr.getPoint3D());
		assertAttributeException(attr,"getPolyline"); 
		assertAttributeException(attr,"getPolyline3D"); 
	}

	@Test
	public void attributeImplIcon() throws Exception {
		Image ic = VectorToolkit.image(1,1,false);
		Attribute attr = new AttributeImpl(randomString(),ic);
		
		assertEquals(AttributeType.IMAGE, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(ic,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertAttributeException(attr,"getColor"); 
		assertAttributeException(attr,"getDate"); 
		assertEquals(ic,attr.getImage());
		assertAttributeException(attr,"getInteger"); 
		assertAttributeException(attr,"getReal"); 
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(ic.toString(), attr.getString());
		assertEquals(ic,attr.getJavaObject());
		assertAttributeException(attr,"getPoint"); 
		assertAttributeException(attr,"getPoint3D"); 
		assertAttributeException(attr,"getPolyline"); 
		assertAttributeException(attr,"getPolyline3D"); 
	}

	@Test
	public void attributeImplInt() throws Exception {
		int nb = new Random().nextInt();
		String txt = Long.toString(nb);
		Attribute attr = new AttributeImpl(randomString(),nb);
		
		assertEquals(AttributeType.INTEGER, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(nb,((Number)attr.getValue()).intValue());
		assertEquals(nb!=0, attr.getBoolean());
		assertEquals(VectorToolkit.color(nb), attr.getColor());
		assertEquals(new Date(nb),attr.getDate());
		assertAttributeException(attr,"getImage"); 
		assertEquals(nb,attr.getInteger());
		assertEquals(nb,(int)attr.getReal());
		assertEquals(nb,attr.getTimestamp());
		assertEquals(txt,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertEquals(new Point2f(nb,0),attr.getPoint());
		assertEquals(new Point3f(nb,0,0),attr.getPoint3D());
		assertAttributeException(attr,"getPolyline"); 
		assertAttributeException(attr,"getPolyline3D"); 
	}

	@Test
	public void attributeImplLong() throws Exception {
		long nb = new Random().nextLong();
		String txt = Long.toString(nb);
		Attribute attr = new AttributeImpl(randomString(),nb);
		
		assertEquals(AttributeType.INTEGER, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(nb,((Number)attr.getValue()).longValue());
		assertEquals(nb!=0, attr.getBoolean());
		assertEquals(VectorToolkit.color((int)nb), attr.getColor());
		assertEquals(new Date(nb),attr.getDate());
		assertAttributeException(attr,"getImage"); 
		assertEquals(nb,attr.getInteger());
		assertEquals((double)nb,attr.getReal());
		assertEquals(nb,attr.getTimestamp());
		assertEquals(txt,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertEquals(new Point2f(nb,0),attr.getPoint());
		assertEquals(new Point3f(nb,0,0),attr.getPoint3D());
		assertAttributeException(attr,"getPolyline"); 
		assertAttributeException(attr,"getPolyline3D"); 
	}

	@Test
	public void attributeImplPoint2d() throws Exception {
		Point2D pt = new Point2f(Math.random(),Math.random());
		Point3D pt3d = new Point3f(pt.getX(),pt.getY(),0.);
		String str = pt.getX()+";"+pt.getY(); 
		Attribute attr = new AttributeImpl(randomString(),pt);
		
		assertEquals(AttributeType.POINT, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(pt,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertEquals(VectorToolkit.color(pt.getX(),pt.getY(),0f),attr.getColor());
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertAttributeException(attr,"getReal"); 
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertEquals(pt, attr.getJavaObject());
		assertEquals(pt,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}
	
	@Test
	public void attributeImplDoubleDouble() throws Exception {
		double x = Math.random();
		double y = Math.random();
		Point2D pt = new Point2f(x,y);
		Point3D pt3d = new Point3f(x,y,0.);
		String str = ((float)x)+";"+((float)y); 
		Attribute attr = new AttributeImpl(randomString(),x,y);
		
		assertEquals(AttributeType.POINT, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(pt,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertEquals(VectorToolkit.color(pt.getX(),pt.getY(),0f),attr.getColor());
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertAttributeException(attr,"getReal"); 
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertEquals(pt, attr.getJavaObject());
		assertEquals(pt,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}
	
	@Test
	public void attributeImplPoint3d() throws Exception {
		double x = Math.random();
		double y = Math.random();
		double z = Math.random();
		Point3D pt = new Point3f(x,y,z);
		Point2D pt2d = new Point2f(x,y);
		String str = ((float)x)+";"+((float)y)+";"+((float)z);  
		Attribute attr = new AttributeImpl(randomString(),pt);
		
		assertEquals(AttributeType.POINT3D, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(pt,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertEquals(VectorToolkit.color(pt.getX(),pt.getY(),pt.getZ()),attr.getColor());
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertAttributeException(attr,"getReal"); 
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertEquals(pt, attr.getJavaObject());
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt},attr.getPolyline3D());
	}

	@Test
	public void attributeImplDoubleDoubleDouble() throws Exception {
		double x = Math.random();
		double y = Math.random();
		double z = Math.random();
		Point3D pt = new Point3f(x,y,z);
		Point2D pt2d = new Point2f(x,y);
		String str = ((float)x)+";"+((float)y)+";"+((float)z);  
		Attribute attr = new AttributeImpl(randomString(),x,y,z);
		
		assertEquals(AttributeType.POINT3D, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(pt,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertEquals(VectorToolkit.color(pt.getX(),pt.getY(),pt.getZ()),attr.getColor());
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertAttributeException(attr,"getReal"); 
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertEquals(pt, attr.getJavaObject());
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt},attr.getPolyline3D());
	}

	@Test
	public void attributeImplString_random() throws Exception {
		double x = Math.random();
		Point2D pt2d = new Point2f(x,0);
		Point3D pt3d = new Point3f(x,0,0);
		String str = Double.toHexString(x);
		Attribute attr = new AttributeImpl(randomString(),str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertAttributeException(attr,"getColor"); 
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertEquals(x,attr.getReal());
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertAttributeException(attr,"getJavaObject"); 
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	@Test
	public void attributeImplString_boolean() throws Exception {
		String str = Boolean.toString(true);
		Attribute attr = new AttributeImpl(randomString(),str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertTrue(attr.getBoolean());
		assertAttributeException(attr,"getColor"); 
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertAttributeException(attr,"getReal"); 
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertAttributeException(attr,"getPoint"); 
		assertAttributeException(attr,"getPoint3D"); 
		assertAttributeException(attr,"getPolyline"); 
		assertAttributeException(attr,"getPolyline3D"); 
	}

	@Test
	public void attributeImplString_Color() throws Exception {
		Color c = Colors.RED;
		Point2D pt2d = new Point2f(c.getRed(),c.getGreen());
		Point2D pt2d2 = new Point2f(c.getBlue(),0);
		Point3D pt3d = new Point3f(c.getRed(),c.getGreen(),c.getBlue());
		String str = c.getRed()+";"+c.getGreen()+";"+c.getBlue();  
		Attribute attr = new AttributeImpl(randomString(),str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertEquals(c,attr.getColor());
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertAttributeException(attr,"getReal"); 
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d,pt2d2},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	@Test
	public void attributeImplString_Date() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String str = format.format(currentDate); 
		Attribute attr = new AttributeImpl(randomString(),str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertAttributeException(attr,"getColor"); 
		assertEpsilonEquals(currentDate,attr.getDate());
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertAttributeException(attr,"getReal"); 
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertAttributeException(attr,"getPoint"); 
		assertAttributeException(attr,"getPoint3D"); 
		assertAttributeException(attr,"getPolyline"); 
		assertAttributeException(attr,"getPolyline3D"); 
	}

	@Test
	public void attributeImplString_JDate() throws Exception {
		Date currentDate = new Date();
		String str = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL).format(currentDate); 
		Attribute attr = new AttributeImpl(randomString(),str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertAttributeException(attr,"getColor"); 
		assertEpsilonEquals(currentDate,attr.getDate());
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertAttributeException(attr,"getReal"); 
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertAttributeException(attr,"getPoint"); 
		assertAttributeException(attr,"getPoint3D"); 
		assertAttributeException(attr,"getPolyline"); 
		assertAttributeException(attr,"getPolyline3D"); 
	}

	@Test
	public void attributeImplString_Integer() throws Exception {
		int nb = new Random().nextInt(20000)+256;
		Point2D pt2d = new Point2f(nb,0);
		Point3D pt3d = new Point3f(nb,0,0);
		String str = Integer.toString(nb);
		Attribute attr = new AttributeImpl(randomString(),str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertAttributeException(attr,"getColor"); 
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertEquals(nb,attr.getInteger());
		assertEquals((double)nb,attr.getReal());
		assertEquals(nb,attr.getTimestamp());
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	@Test
	public void attributeImplString_Long() throws Exception {
		long nb = new Random().nextInt(20000)+256;
		Point2D pt2d = new Point2f(nb,0);
		Point3D pt3d = new Point3f(nb,0,0);
		String str = Long.toString(nb);
		Attribute attr = new AttributeImpl(randomString(),str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertAttributeException(attr,"getColor"); 
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertEquals(nb,attr.getInteger());
		assertEquals((double)nb,attr.getReal());
		assertEquals(nb,attr.getTimestamp());
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	@Test
	public void attributeImplString_Double() throws Exception {
		double nb = Math.random()+256;
		Point2D pt2d = new Point2f(nb,0);
		Point3D pt3d = new Point3f(nb,0,0);
		String str = Double.toString(nb);
		Attribute attr = new AttributeImpl(randomString(),str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertAttributeException(attr,"getColor"); 
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertEquals(nb,attr.getReal());
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	@Test
	public void attributeImplString_Point2D() throws Exception {
		double x = Math.random()+256;
		double y = Math.random()+256;
		Point2D pt2d = new Point2f(x,y);
		Point3D pt3d = new Point3f(x,y,0);
		String str = x+";"+y; 
		Attribute attr = new AttributeImpl(randomString(),str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertAttributeException(attr,"getColor"); 
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertAttributeException(attr,"getReal"); 
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	@Test
	public void attributeImplString_Point3D() throws Exception {
		double x = Math.random()+256;
		double y = Math.random()+256;
		double z = Math.random()+256;
		Point2D pt2d = new Point2f(x,y);
		Point2D pt2d2 = new Point2f(z,0);
		Point3D pt3d = new Point3f(x,y,z);
		String str = x+";"+y+";"+z;  
		Attribute attr = new AttributeImpl(randomString(),str);
		
		assertEquals(AttributeType.STRING, attr.getType());

		assertTrue(attr.isAssigned());
		assertTrue(attr.isBaseType());
		assertFalse(attr.isObjectValue());
		
		assertEquals(str,attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertAttributeException(attr,"getColor"); 
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertAttributeException(attr,"getReal"); 
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertAttributeException(attr,"getJavaObject"); 
		assertEquals(pt2d,attr.getPoint());
		assertEquals(pt3d,attr.getPoint3D());
		assertEquals(new Point2D[]{pt2d,pt2d2},attr.getPolyline());
		assertEquals(new Point3D[]{pt3d},attr.getPolyline3D());
	}

	@Test
	public void attributeImplPoint2DArray() throws Exception {
		double x1 = Math.random();
		double y1 = Math.random();
		double x2 = Math.random();
		double y2 = Math.random();

		Point2D pt1 = new Point2f(x1,y1);
		Point2D pt2 = new Point2f(x2,y2);
		
		Point2D[] list = new Point2D[]{ pt1, pt2 };
		Point3D[] list2 = new Point3D[]{ new Point3f(x1,y1,0), new Point3f(x2,y2,0) };

		String str = ((float)x1)+";"+((float)y1)+";"+((float)x2)+";"+((float)y2);   

		Attribute attr = new AttributeImpl(randomString(),list);
		
		assertEquals(AttributeType.POLYLINE, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(list,(Point2D[])attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertAttributeException(attr,"getColor"); 
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertAttributeException(attr,"getReal"); 
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertEquals(list, attr.getJavaObject());
		assertAttributeException(attr,"getPoint"); 
		assertAttributeException(attr,"getPoint3D"); 
		assertEquals(list,attr.getPolyline());
		assertEquals(list2,attr.getPolyline3D());
	}

	@Test
	public void attributeImplPoint3DArray() throws Exception {
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

		String str = ((float)x1)+";"+((float)y1)+";"+((float)z1)+";"+((float)x2)+";"+((float)y2)+";"+((float)z2);     

		Attribute attr = new AttributeImpl(randomString(),list);
		
		assertEquals(AttributeType.POLYLINE3D, attr.getType());

		assertTrue(attr.isAssigned());
		assertFalse(attr.isBaseType());
		assertTrue(attr.isObjectValue());
		
		assertEquals(list,(Point3D[])attr.getValue());
		assertAttributeException(attr,"getBoolean"); 
		assertAttributeException(attr,"getColor"); 
		assertAttributeException(attr,"getDate"); 
		assertAttributeException(attr,"getImage"); 
		assertAttributeException(attr,"getInteger"); 
		assertAttributeException(attr,"getReal"); 
		assertAttributeException(attr,"getTimestamp"); 
		assertEquals(str,attr.getString());
		assertEquals(list, attr.getJavaObject());
		assertAttributeException(attr,"getPoint"); 
		assertAttributeException(attr,"getPoint3D"); 
		assertEquals(list2,attr.getPolyline());
		assertEquals(list,attr.getPolyline3D());
	}

	@Test
	public void testEquals() {
		AttributeImpl attr = new AttributeImpl("A1"); 
		
		attr.setBoolean(true);
		assertTrue(attr.equals(new AttributeImpl("A1", true))); 
		assertFalse(attr.equals(new AttributeImpl("A1", false))); 
		assertFalse(attr.equals(new AttributeImpl("A2", true))); 
		assertFalse(attr.equals(new AttributeImpl("A2", false))); 
		assertTrue(attr.equals(new AttributeValueImpl(true)));
		assertFalse(attr.equals(new AttributeValueImpl(false)));
		assertTrue(attr.equals(true));
		assertFalse(attr.equals(false));
		assertTrue(attr.equals(new AttributeValueImpl("true"))); 
		assertFalse(attr.equals(new AttributeValueImpl("false"))); 
		assertTrue(attr.equals("true")); 
		assertFalse(attr.equals("false")); 
		assertFalse(attr.equals(new AttributeValueImpl(1.)));
		assertFalse(attr.equals(new AttributeValueImpl("1."))); 
		assertFalse(attr.equals(1.));
		assertFalse(attr.equals("toto")); 

		attr.setBoolean(false);
		assertFalse(attr.equals(new AttributeImpl("A1", true))); 
		assertTrue(attr.equals(new AttributeImpl("A1", false))); 
		assertFalse(attr.equals(new AttributeImpl("A2", true))); 
		assertFalse(attr.equals(new AttributeImpl("A2", false))); 
		assertFalse(attr.equals(new AttributeValueImpl(true)));
		assertTrue(attr.equals(new AttributeValueImpl(false)));
		assertFalse(attr.equals(true));
		assertTrue(attr.equals(false));
		assertFalse(attr.equals(new AttributeValueImpl("true"))); 
		assertTrue(attr.equals(new AttributeValueImpl("false"))); 
		assertFalse(attr.equals("true")); 
		assertTrue(attr.equals("false")); 
		assertFalse(attr.equals(new AttributeValueImpl(1.)));
		assertFalse(attr.equals(new AttributeValueImpl("1."))); 
		assertFalse(attr.equals(1.));
		assertFalse(attr.equals("toto")); 
	}
	
}
