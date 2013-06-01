/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.arakhne.afc.math.AbstractRepeatedTestCase;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.object.Point1D5;
import org.arakhne.afc.math.object.Segment1DStub;

/**
 * Unit test for Transform1D5. 
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Transform1D5Test extends AbstractRepeatedTestCase {

	private Segment1DStub segment1;
	private Segment1DStub segment2;
	private Segment1DStub segment3;
	private Segment1DStub segment4;
	private List<Segment1DStub> defaultPath;
	
	/**
	 */
	public Transform1D5Test() {
		super(20);
	}
	
	/**
	 * @throws Exception
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.segment1 = new Segment1DStub("segment1", randomPoint2D(), randomPoint2D()); //$NON-NLS-1$
		this.segment2 = new Segment1DStub("segment2", randomPoint2D(), this.segment1.getLastPoint()); //$NON-NLS-1$
		this.segment3 = new Segment1DStub("segment3", this.segment2.getFirstPoint(), randomPoint2D()); //$NON-NLS-1$
		this.segment4 = new Segment1DStub("segment4", randomPoint2D(), randomPoint2D()); //$NON-NLS-1$
		this.segment1.connectWith(this.segment2);
		this.segment2.connectWith(this.segment3);
		this.defaultPath = new ArrayList<Segment1DStub>();
		this.defaultPath.add(this.segment1);
		this.defaultPath.add(this.segment2);
		this.defaultPath.add(this.segment3);
	}
	
	/**
	 * @throws Exception
	 */
	@Override
	public void tearDown() throws Exception {
		this.defaultPath.clear();
		this.defaultPath = null;
		this.segment1 = this.segment2 = this.segment3 = this.segment4 = null;
		super.tearDown();
	}

	/**
	 */
	public void testTranform1D5Void() {
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>();
		
		assertEquals(0.f,trans.getCurvilineTransformation());
		assertEquals(0.f,trans.getJuttingTransformation());
		assertEquals(new Vector2f(), trans.getTranslationVector());

		assertNotNull(trans.getPath());
		assertTrue(trans.getPath().isEmpty());
	}

	/**
	 */
	public void testTranform1D5FloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(expectedCurviline, expectedJutting);
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());
		assertEquals(expectedJutting,trans.getJuttingTransformation());
		assertEquals(new Vector2f(expectedCurviline, expectedJutting), trans.getTranslationVector());

		assertNotNull(trans.getPath());
		assertTrue(trans.getPath().isEmpty());
	}
	
	/**
	 */
	public void testTranform1D5ListFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline, expectedJutting);
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());
		assertEquals(expectedJutting,trans.getJuttingTransformation());
		assertEquals(new Vector2f(expectedCurviline, expectedJutting), trans.getTranslationVector());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/**
	 */
	public void testTranform1D5List() {
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath));
		
		assertEquals(0.,trans.getCurvilineTransformation());
		assertEquals(0.,trans.getJuttingTransformation());
		assertEquals(new Vector2f(), trans.getTranslationVector());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/**
	 */
	public void testTranform1D5Tranform1D5() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		Transform1D5<Segment1DStub> expectedTrans = new Transform1D5<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline, expectedJutting);

		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(expectedTrans);
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());
		assertEquals(expectedJutting,trans.getJuttingTransformation());
		assertEquals(new Vector2f(expectedCurviline, expectedJutting), trans.getTranslationVector());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testHasPath_Transform1D5Void() {
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>();
		assertFalse(trans.hasPath());
	}

	/** 
	 */
	public void testHasPath_Transform1D5FloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(expectedCurviline, expectedJutting);
		assertFalse(trans.hasPath());
	}

	/** 
	 */
	public void testHasPath_Transform1D5List() {
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath));
		assertTrue(trans.hasPath());
	}

	/** 
	 */
	public void testHasPath_Transform1D5ListFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline, expectedJutting);
		assertTrue(trans.hasPath());
	}

	/** 
	 */
	public void testGetPath_Transform1D5Void() {
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>();
		assertNotNull(trans.getPath());
		assertTrue(trans.getPath().isEmpty());
	}

	/** 
	 */
	public void testGetPath_Transform1D5FloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(expectedCurviline, expectedJutting);
		assertNotNull(trans.getPath());
		assertTrue(trans.getPath().isEmpty());
	}

	/** 
	 */
	public void testGetPath_Transform1D5List() {
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath));
		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testGetPath_Transform1D5ListFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline, expectedJutting);
		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testSetPathList() {
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>();
		assertNotNull(trans.getPath());
		assertTrue(trans.getPath().isEmpty());
		
		trans.setPath(null);
		assertNotNull(trans.getPath());
		assertTrue(trans.getPath().isEmpty());
		
		trans.setPath(new ArrayList<Segment1DStub>());
		assertNotNull(trans.getPath());
		assertTrue(trans.getPath().isEmpty());

		trans.setPath(new ArrayList<Segment1DStub>(this.defaultPath));
		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());

		ArrayList<Segment1DStub> newPath = new ArrayList<Segment1DStub>();
		newPath.add(this.segment2);
		trans.setPath(newPath);
		assertEquals(newPath.size(), trans.getPath().size());
		assertEquals(newPath, trans.getPath());
	}

	/** 
	 */
	public void testSetIdentity() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline, expectedJutting);
		
		trans.setIdentity();

		assertEquals(0.,trans.getCurvilineTransformation());
		assertEquals(0.,trans.getJuttingTransformation());
		assertEquals(new Vector2f(), trans.getTranslationVector());

		assertNotNull(trans.getPath());
		assertTrue(trans.getPath().isEmpty());
	}

	/** 
	 */
	public void testSetTranslationFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath);

		trans.setTranslation(expectedCurviline, expectedJutting);
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());
		assertEquals(expectedJutting,trans.getJuttingTransformation());
		assertEquals(new Vector2f(expectedCurviline, expectedJutting), trans.getTranslationVector());

		assertNotNull(trans.getPath());
		assertEquals(testPath.size(), trans.getPath().size());
		assertEquals(testPath, trans.getPath());
	}
	
	/** 
	 */
	public void testSetTranslationListFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath);

		trans.setTranslation(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline, expectedJutting);
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());
		assertEquals(expectedJutting,trans.getJuttingTransformation());
		assertEquals(new Vector2f(expectedCurviline, expectedJutting), trans.getTranslationVector());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testSetTranslationTuple2d() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath);

		trans.setTranslation(new Point2f(expectedCurviline, expectedJutting));
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());
		assertEquals(expectedJutting,trans.getJuttingTransformation());
		assertEquals(new Vector2f(expectedCurviline, expectedJutting), trans.getTranslationVector());

		assertNotNull(trans.getPath());
		assertEquals(testPath.size(), trans.getPath().size());
		assertEquals(testPath, trans.getPath());
	}

	/** 
	 */
	public void testSetTranslationListTuple2d() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath);

		trans.setTranslation(new ArrayList<Segment1DStub>(this.defaultPath), new Point2f(expectedCurviline, expectedJutting));
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());
		assertEquals(expectedJutting,trans.getJuttingTransformation());
		assertEquals(new Vector2f(expectedCurviline, expectedJutting), trans.getTranslationVector());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testSetTranslationTuple2f() {
		float expectedCurviline = (float)((this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f);
		float expectedJutting = (float)((this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f);
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath);

		trans.setTranslation(new Point2f(expectedCurviline, expectedJutting));
		
		assertEquals(expectedCurviline,(float)trans.getCurvilineTransformation());
		assertEquals(expectedJutting,(float)trans.getJuttingTransformation());
		assertEquals(new Vector2f(expectedCurviline, expectedJutting), trans.getTranslationVector());

		assertNotNull(trans.getPath());
		assertEquals(testPath.size(), trans.getPath().size());
		assertEquals(testPath, trans.getPath());
	}

	/** 
	 */
	public void testSetTranslationListTupled2f() {
		float expectedCurviline = (float)((this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f);
		float expectedJutting = (float)((this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f);
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath);

		trans.setTranslation(new ArrayList<Segment1DStub>(this.defaultPath), new Point2f(expectedCurviline, expectedJutting));
		
		assertEquals(expectedCurviline,(float)trans.getCurvilineTransformation());
		assertEquals(expectedJutting,(float)trans.getJuttingTransformation());
		assertEquals(new Vector2f(expectedCurviline, expectedJutting), trans.getTranslationVector());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testSetTranslationPoint1D5() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath);

		trans.setTranslation(new Point1D5(this.segment2, expectedCurviline, expectedJutting));
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());
		assertEquals(expectedJutting,trans.getJuttingTransformation());
		assertEquals(new Vector2f(expectedCurviline, expectedJutting), trans.getTranslationVector());

		assertNotNull(trans.getPath());
		assertEquals(testPath.size(), trans.getPath().size());
		assertEquals(testPath, trans.getPath());
	}

	/** 
	 */
	public void testSetTranslationListPoint1D5() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath);

		trans.setTranslation(new ArrayList<Segment1DStub>(this.defaultPath), new Point1D5(this.segment1, expectedCurviline, expectedJutting));
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());
		assertEquals(expectedJutting,trans.getJuttingTransformation());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testTranslateFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurviline, expectedJutting);

		float expectedCurviline2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;		
		
		trans.translate(expectedCurviline2, expectedJutting2);
		
		assertEquals(expectedCurviline+expectedCurviline2,trans.getCurvilineTransformation());
		assertEquals(expectedJutting+expectedJutting2,trans.getJuttingTransformation());

		assertNotNull(trans.getPath());
		assertEquals(testPath.size(), trans.getPath().size());
		assertEquals(testPath, trans.getPath());
	}

	/** 
	 */
	public void testTranslateListFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurviline, expectedJutting);

		float expectedCurviline2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;		
		
		trans.translate(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline2, expectedJutting2);
		
		assertEquals(expectedCurviline+expectedCurviline2,trans.getCurvilineTransformation());
		assertEquals(expectedJutting+expectedJutting2,trans.getJuttingTransformation());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testTranslateTuple2d() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurviline, expectedJutting);

		float expectedCurviline2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;		
		
		trans.translate(new Point2f(expectedCurviline2, expectedJutting2));
		
		assertEquals(expectedCurviline+expectedCurviline2,trans.getCurvilineTransformation());
		assertEquals(expectedJutting+expectedJutting2,trans.getJuttingTransformation());

		assertNotNull(trans.getPath());
		assertEquals(testPath.size(), trans.getPath().size());
		assertEquals(testPath, trans.getPath());
	}

	/** 
	 */
	public void testTranslateListTuple2d() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurviline, expectedJutting);

		float expectedCurviline2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;		
		
		trans.translate(new ArrayList<Segment1DStub>(this.defaultPath), new Point2f(expectedCurviline2, expectedJutting2));
		
		assertEquals(expectedCurviline+expectedCurviline2,trans.getCurvilineTransformation());
		assertEquals(expectedJutting+expectedJutting2,trans.getJuttingTransformation());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testTranslateTuple2f() {
		float expectedCurviline = (float)((this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f);
		float expectedJutting = (float)((this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f);
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurviline, expectedJutting);

		float expectedCurviline2 = (float)((this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f);
		float expectedJutting2 = (float)((this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f);		
		
		trans.translate(new Point2f(expectedCurviline2, expectedJutting2));
		
		assertEquals(expectedCurviline+expectedCurviline2,(float)trans.getCurvilineTransformation());
		assertEquals(expectedJutting+expectedJutting2,(float)trans.getJuttingTransformation());

		assertNotNull(trans.getPath());
		assertEquals(testPath.size(), trans.getPath().size());
		assertEquals(testPath, trans.getPath());
	}

	/** 
	 */
	public void testTranslateListTuple2f() {
		float expectedCurviline = (float)((this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f);
		float expectedJutting = (float)((this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f);
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurviline, expectedJutting);

		float expectedCurviline2 = (float)((this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f);
		float expectedJutting2 = (float)((this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f);		
		
		trans.translate(new ArrayList<Segment1DStub>(this.defaultPath), new Point2f(expectedCurviline2, expectedJutting2));
		
		assertEquals(expectedCurviline+expectedCurviline2,(float)trans.getCurvilineTransformation());
		assertEquals(expectedJutting+expectedJutting2,(float)trans.getJuttingTransformation());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testTranslatePoint1D5() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurviline, expectedJutting);

		float expectedCurviline2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;		
		
		trans.translate(new Point1D5(this.segment3, expectedCurviline2, expectedJutting2));
		
		assertEquals(expectedCurviline+expectedCurviline2,trans.getCurvilineTransformation());
		assertEquals(expectedJutting+expectedJutting2,trans.getJuttingTransformation());

		assertNotNull(trans.getPath());
		assertEquals(testPath.size(), trans.getPath().size());
		assertEquals(testPath, trans.getPath());
	}

	/** 
	 */
	public void testTranslateListPoint1D5() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurviline, expectedJutting);

		float expectedCurviline2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float expectedJutting2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;		
		
		trans.translate(new ArrayList<Segment1DStub>(this.defaultPath), new Point1D5(this.segment3, expectedCurviline2, expectedJutting2));
		
		assertEquals(expectedCurviline+expectedCurviline2,trans.getCurvilineTransformation());
		assertEquals(expectedJutting+expectedJutting2,trans.getJuttingTransformation());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testTransformPoint1D5_NotOnPath() {
		float deltaCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		float deltaJutting = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, deltaCurviline, deltaJutting);
		
		Point1D5 point = new Point1D5(this.segment4, this.RANDOM.nextFloat()*100.f, (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f);
		point.clamp();
		float originalCurviline = point.getCurvilineCoordinate();
		float originalJutting = point.getJuttingDistance();
		
		// One segment means "in the same direction as the segment".
		// So the jutting transformation's coordinate system is the
		// same as the segment's coordinate system.
		float expectedJutting = originalJutting + deltaJutting;
		
		int segmentIndex = trans.transform(point);
		
		assertEquals(-1, segmentIndex);
		assertEquals(this.segment4, point.getSegment());
		assertEquals(originalCurviline+deltaCurviline, point.getCurvilineCoordinate());
		assertEquals(expectedJutting, point.getJuttingDistance());
	}

	/** 
	 */
	public void testTransformPoint1D5_OnOneSegmentPath() {
		float expectedCurvilineTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * this.segment1.getLength();
		float expectedJuttingTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>();
		assertTrue(testPath.add(this.segment1));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurvilineTrans, expectedJuttingTrans);
		
		Segment1DStub expectedSegment = this.segment1;
		Point1D5 point = new Point1D5(expectedSegment, this.RANDOM.nextFloat()*expectedSegment.getLength(), (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f);
		point.clamp();
		float originalCurviline = point.getCurvilineCoordinate();
		float originalJutting = point.getJuttingDistance();
		
		if (expectedCurvilineTrans<0) {
			int segmentIndex = trans.transform(point);
			
			assertEquals(-1, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEquals(originalCurviline+expectedCurvilineTrans, point.getCurvilineCoordinate());
			assertEquals(originalJutting+expectedJuttingTrans, point.getJuttingDistance());
		}
		else {
			float expectedCurviline = originalCurviline + expectedCurvilineTrans;
			int expectedSegmentIndex = 0;
			
			int segmentIndex = trans.transform(point);

			assertEquals(expectedSegmentIndex, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEpsilonEquals(expectedCurviline, point.getCurvilineCoordinate());
			assertEpsilonEquals(originalJutting+expectedJuttingTrans, point.getJuttingDistance());
		}
	}

	/** 
	 */
	public void testTransformPoint1D5_OnOneSegmentPath_Reverted() {
		float expectedCurvilineTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * this.segment2.getLength();
		float expectedJuttingTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>();
		assertTrue(testPath.add(this.segment2));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurvilineTrans, expectedJuttingTrans);
		
		Segment1DStub expectedSegment = this.segment2;
		Point1D5 point = new Point1D5(expectedSegment, this.RANDOM.nextFloat()*expectedSegment.getLength(), (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f);
		point.clamp();
		float originalCurviline = point.getCurvilineCoordinate();
		float originalJutting = point.getJuttingDistance();
		
		if (expectedCurvilineTrans<0) {
			int segmentIndex = trans.transform(point);
			
			assertEquals(-1, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEquals(originalCurviline+expectedCurvilineTrans, point.getCurvilineCoordinate());
			assertEquals(originalJutting+expectedJuttingTrans, point.getJuttingDistance());
		}
		else {
			float expectedCurviline = originalCurviline + expectedCurvilineTrans;
			int expectedSegmentIndex = 0;
			
			int segmentIndex = trans.transform(point);

			assertEquals(expectedSegmentIndex, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEpsilonEquals(expectedCurviline, point.getCurvilineCoordinate());
			assertEpsilonEquals(originalJutting+expectedJuttingTrans, point.getJuttingDistance());
		}
	}

	/** 
	 */
	public void testTransformPoint1D5_OnTwoSegmentPath() {
		float expectedCurvilineTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * (this.segment1.getLength()+this.segment2.getLength());
		float expectedJuttingTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>();
		assertTrue(testPath.add(this.segment1));
		assertTrue(testPath.add(this.segment2));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurvilineTrans, expectedJuttingTrans);
		
		Segment1DStub expectedSegment = this.segment1;
		Point1D5 point = new Point1D5(expectedSegment, this.RANDOM.nextFloat()*expectedSegment.getLength(), (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f);
		point.clamp();
		float originalCurviline = point.getCurvilineCoordinate();
		float originalJutting = point.getJuttingDistance();
		
		if (expectedCurvilineTrans<0) {
			int segmentIndex = trans.transform(point);
			
			assertEquals(-1, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEquals(originalCurviline+expectedCurvilineTrans, point.getCurvilineCoordinate());
			assertEquals(originalJutting+expectedJuttingTrans, point.getJuttingDistance());
		}
		else {
			float expectedCurviline = originalCurviline + expectedCurvilineTrans;
			int expectedSegmentIndex = 0;
			float juttingSign = 1f;
			
			if (expectedCurviline>this.segment1.getLength()) {
				expectedCurviline -= this.segment1.getLength();
				expectedSegmentIndex = 1;
				expectedSegment = this.segment2;
				
				// Because segment2 is reverted
				juttingSign = -1f;
				expectedCurviline = this.segment2.getLength() - expectedCurviline;
			}
			
			int segmentIndex = trans.transform(point);

			assertEquals(expectedSegmentIndex, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEpsilonEquals(expectedCurviline, point.getCurvilineCoordinate());
			assertEpsilonEquals(juttingSign*(originalJutting+expectedJuttingTrans), point.getJuttingDistance());
		}
	}

	/** 
	 */
	public void testTransformPoint1D5_OnThreeSegmentPath() {
		float expectedCurvilineTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * (this.segment1.getLength()+this.segment2.getLength()+this.segment3.getLength());
		float expectedJuttingTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>();
		assertTrue(testPath.add(this.segment1));
		assertTrue(testPath.add(this.segment2));
		assertTrue(testPath.add(this.segment3));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurvilineTrans, expectedJuttingTrans);
		
		Segment1DStub expectedSegment = this.segment1;
		Point1D5 point = new Point1D5(expectedSegment, this.RANDOM.nextFloat()*expectedSegment.getLength(), (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f);
		point.clamp();
		float originalCurviline = point.getCurvilineCoordinate();
		float originalJutting = point.getJuttingDistance();
		
		if (expectedCurvilineTrans<0) {
			int segmentIndex = trans.transform(point);
			
			assertEquals(-1, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEquals(originalCurviline+expectedCurvilineTrans, point.getCurvilineCoordinate());
			assertEquals(originalJutting+expectedJuttingTrans, point.getJuttingDistance());
		}
		else {
			float expectedCurviline = originalCurviline + expectedCurvilineTrans;
			int expectedSegmentIndex = 0;
			float juttingSign = 1f;
			
			if (expectedCurviline>this.segment1.getLength()) {
				expectedCurviline -= this.segment1.getLength();
				expectedSegmentIndex = 1;
				expectedSegment = this.segment2;
				
				if (expectedCurviline>this.segment2.getLength()) {
					expectedCurviline -= this.segment2.getLength();
					expectedSegmentIndex = 2;
					expectedSegment = this.segment3;					
				}
				else {
					// Because segment2 is reverted
					juttingSign = -1f;
					expectedCurviline = this.segment2.getLength() - expectedCurviline;
				}
			}
			
			int segmentIndex = trans.transform(point);

			assertEquals(expectedSegmentIndex, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEpsilonEquals(expectedCurviline, point.getCurvilineCoordinate());
			assertEpsilonEquals(juttingSign*(originalJutting+expectedJuttingTrans), point.getJuttingDistance());
		}
	}

	/** 
	 */
	public void testTransformPoint1D5_OnFourSegmentPath() {
		float expectedCurvilineTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * (this.segment1.getLength()+this.segment2.getLength()+this.segment3.getLength());
		float expectedJuttingTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>();
		assertTrue(testPath.add(this.segment1));
		assertTrue(testPath.add(this.segment2));
		assertTrue(testPath.add(this.segment3));
		assertTrue(testPath.add(this.segment4));
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurvilineTrans, expectedJuttingTrans);
		
		Segment1DStub expectedSegment = this.segment1;
		Point1D5 point = new Point1D5(expectedSegment, this.RANDOM.nextFloat()*expectedSegment.getLength(), (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 50.f);
		point.clamp();
		float originalCurviline = point.getCurvilineCoordinate();
		float originalJutting = point.getJuttingDistance();
		
		if (expectedCurvilineTrans<0) {
			int segmentIndex = trans.transform(point);
			
			assertEquals(-1, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEquals(originalCurviline+expectedCurvilineTrans, point.getCurvilineCoordinate());
			assertEquals(originalJutting+expectedJuttingTrans, point.getJuttingDistance());
		}
		else {
			float expectedCurviline = originalCurviline + expectedCurvilineTrans;
			int expectedSegmentIndex = 0;
			float juttingSign = 1f;
			
			if (expectedCurviline>this.segment1.getLength()) {
				expectedCurviline -= this.segment1.getLength();
				expectedSegmentIndex = 1;
				expectedSegment = this.segment2;
				
				if (expectedCurviline>this.segment2.getLength()) {
					expectedCurviline -= this.segment2.getLength();
					expectedSegmentIndex = 2;
					expectedSegment = this.segment3;					
				}
				else {
					// Because segment2 is reverted
					juttingSign = -1f;
					expectedCurviline = this.segment2.getLength() - expectedCurviline;
				}
			}
			
			int segmentIndex = trans.transform(point);
			
			//try {
				assertTrue(segmentIndex>=0 && segmentIndex<3);
			/*}
			catch(AssertionFailedError e) {
				System.err.println(this.segment1.toString());
				System.err.println(this.segment1.getFirstPoint().toString());
				System.err.println(this.segment1.getLastPoint().toString());
				System.err.println(this.segment2.toString());
				System.err.println(this.segment2.getFirstPoint().toString());
				System.err.println(this.segment2.getLastPoint().toString());
				System.err.println(this.segment3.toString());
				System.err.println(this.segment3.getFirstPoint().toString());
				System.err.println(this.segment3.getLastPoint().toString());
				System.err.println(this.segment4.toString());
				System.err.println(this.segment4.getFirstPoint().toString());
				System.err.println(this.segment4.getLastPoint().toString());
				System.err.println("originalCurviline="+originalCurviline);
				System.err.println("originalJutting="+originalJutting);
				System.err.println("curvilineTrans="+expectedCurvilineTrans);
				System.err.println("juttingTrans="+expectedJuttingTrans);
				throw e;
			}*/

			assertSame(expectedSegment, point.getSegment());
			assertEpsilonEquals(expectedCurviline, point.getCurvilineCoordinate());
			assertEpsilonEquals(juttingSign*(originalJutting+expectedJuttingTrans), point.getJuttingDistance());
			assertEquals(expectedSegmentIndex, segmentIndex);
		}
	}
	
	/**
	 */
	/*public void testMyTest() {
		Segment1DStub s1 = new Segment1DStub("segment1", //$NON-NLS-1$
				new Point2f(139.98607238927286, 478.9914497017532),
				new Point2f(826.6959794994866, 101.67713971204984));
		Segment1DStub s2 = new Segment1DStub("segment2", //$NON-NLS-1$
				new Point2f(558.0796918012238, 72.18127382468398),
				new Point2f(826.6959794994866, 101.67713971204984));
		Segment1DStub s3 = new Segment1DStub("segment3", //$NON-NLS-1$
				new Point2f(558.0796918012238, 72.18127382468398),
				new Point2f(724.124625761833, 852.1844084136753));
		Segment1DStub s4 = new Segment1DStub("segment4", //$NON-NLS-1$
				new Point2f(729.4427176272255, 86.10368966622394),
				new Point2f(288.61092443959103, 673.724665387028));
		
		s1.connectWith(s2);
		s2.connectWith(s3);

		float originalCurviline = 505.5820047317796;
		float originalJutting = -17.910904024100983;
		float expectedCurvilineTrans = 1538.3455384048543;
		float expectedJuttingTrans = 3.2283989458269993;

		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>();
		assertTrue(testPath.add(s1));
		assertTrue(testPath.add(s2));
		assertTrue(testPath.add(s3));
		assertTrue(testPath.add(s4));
		
		Transform1D5<Segment1DStub> trans = new Transform1D5<Segment1DStub>(testPath, expectedCurvilineTrans, expectedJuttingTrans);
		
		Segment1DStub expectedSegment = s1;
		Point1D5 point = new Point1D5(expectedSegment, originalCurviline, originalJutting);
		

		float expectedCurviline = originalCurviline + expectedCurvilineTrans;
		int expectedSegmentIndex = 0;
		float juttingSign = 1f;
		
		if (expectedCurviline>s1.getLength()) {
			expectedCurviline -= s1.getLength();
			expectedSegmentIndex = 1;
			expectedSegment = s2;
			
			if (expectedCurviline>s2.getLength()) {
				expectedCurviline -= s2.getLength();
				expectedSegmentIndex = 2;
				expectedSegment = s3;					
			}
			else {
				// Because segment2 is reverted
				juttingSign = -1f;
				expectedCurviline = s2.getLength() - expectedCurviline;
			}
		}
		
		int segmentIndex = trans.transform(point);
		
		assertTrue(segmentIndex>=0 && segmentIndex<3);

		assertSame(expectedSegment, point.getSegment());
		assertEpsilonEquals(expectedCurviline, point.getCurvilineCoordinate());
		assertEpsilonEquals(juttingSign*(originalJutting+expectedJuttingTrans), point.getJuttingDistance());
		assertEquals(expectedSegmentIndex, segmentIndex);
	}*/

}