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
import org.arakhne.afc.math.object.Point1D;
import org.arakhne.afc.math.object.Segment1DStub;

/**
 * Unit test for Transform1D5. 
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Transform1DTest extends AbstractRepeatedTestCase {

	private Segment1DStub segment1;
	private Segment1DStub segment2;
	private Segment1DStub segment3;
	private Segment1DStub segment4;
	private List<Segment1DStub> defaultPath;
	
	/**
	 */
	public Transform1DTest() {
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
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>();
		
		assertEquals(0.,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertTrue(trans.getPath().isEmpty());
	}

	/**
	 */
	public void testTranform1D5FloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(expectedCurviline);
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertTrue(trans.getPath().isEmpty());
	}
	
	/**
	 */
	public void testTranform1D5ListFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline);
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/**
	 */
	public void testTranform1D5List() {
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath));
		
		assertEquals(0.,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/**
	 */
	public void testTranform1D5Tranform1D5() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		Transform1D<Segment1DStub> expectedTrans = new Transform1D<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline);

		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(expectedTrans);
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testHasPath_Transform1D5Void() {
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>();
		assertFalse(trans.hasPath());
	}

	/** 
	 */
	public void testHasPath_Transform1D5FloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(expectedCurviline);
		assertFalse(trans.hasPath());
	}

	/** 
	 */
	public void testHasPath_Transform1D5List() {
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath));
		assertTrue(trans.hasPath());
	}

	/** 
	 */
	public void testHasPath_Transform1D5ListFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline);
		assertTrue(trans.hasPath());
	}

	/** 
	 */
	public void testGetPath_Transform1DVoid() {
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>();
		assertNotNull(trans.getPath());
		assertTrue(trans.getPath().isEmpty());
	}

	/** 
	 */
	public void testGetPath_Transform1DFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(expectedCurviline);
		assertNotNull(trans.getPath());
		assertTrue(trans.getPath().isEmpty());
	}

	/** 
	 */
	public void testGetPath_Transform1DList() {
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath));
		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testGetPath_Transform1DListFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline);
		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testSetPathList() {
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>();
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
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline);
		
		trans.setIdentity();

		assertEquals(0.,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertTrue(trans.getPath().isEmpty());
	}

	/** 
	 */
	public void testSetTranslationFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath);

		trans.setTranslation(expectedCurviline);
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertEquals(testPath.size(), trans.getPath().size());
		assertEquals(testPath, trans.getPath());
	}
	
	/** 
	 */
	public void testSetTranslationListFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath);

		trans.setTranslation(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline);
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testSetTranslationPoint1D() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath);

		trans.setTranslation(new Point1D(this.segment2, expectedCurviline));
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertEquals(testPath.size(), trans.getPath().size());
		assertEquals(testPath, trans.getPath());
	}

	/** 
	 */
	public void testSetTranslationListPoint1D5() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath);

		trans.setTranslation(new ArrayList<Segment1DStub>(this.defaultPath), new Point1D(this.segment1, expectedCurviline));
		
		assertEquals(expectedCurviline,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testTranslateFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath, expectedCurviline);

		float expectedCurviline2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		
		trans.translate(expectedCurviline2);
		
		assertEquals(expectedCurviline+expectedCurviline2,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertEquals(testPath.size(), trans.getPath().size());
		assertEquals(testPath, trans.getPath());
	}

	/** 
	 */
	public void testTranslateListFloatFloat() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath, expectedCurviline);

		float expectedCurviline2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		
		trans.translate(new ArrayList<Segment1DStub>(this.defaultPath), expectedCurviline2);
		
		assertEquals(expectedCurviline+expectedCurviline2,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testTranslatePoint1D() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath, expectedCurviline);

		float expectedCurviline2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		
		trans.translate(new Point1D(this.segment3, expectedCurviline2));
		
		assertEquals(expectedCurviline+expectedCurviline2,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertEquals(testPath.size(), trans.getPath().size());
		assertEquals(testPath, trans.getPath());
	}

	/** 
	 */
	public void testTranslateListPoint1D() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath, expectedCurviline);

		float expectedCurviline2 = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		
		trans.translate(new ArrayList<Segment1DStub>(this.defaultPath), new Point1D(this.segment3, expectedCurviline2));
		
		assertEquals(expectedCurviline+expectedCurviline2,trans.getCurvilineTransformation());

		assertNotNull(trans.getPath());
		assertEquals(this.defaultPath.size(), trans.getPath().size());
		assertEquals(this.defaultPath, trans.getPath());
	}

	/** 
	 */
	public void testTransformPoint1D_NotOnPath() {
		float expectedCurviline = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * 100.f;
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>(Collections.singleton(this.segment1));
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath, expectedCurviline);
		
		Point1D point = new Point1D(this.segment4, this.RANDOM.nextFloat()*100.f);
		point.clamp();
		float originalCurviline = point.getCurvilineCoordinate();
		
		int segmentIndex = trans.transform(point);
		
		assertEquals(-1, segmentIndex);
		assertEquals(this.segment4, point.getSegment());
		assertEquals(originalCurviline+expectedCurviline, point.getCurvilineCoordinate());
	}

	/** 
	 */
	public void testTransformPoint1D_OnOneSegmentPath() {
		float expectedCurvilineTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * this.segment1.getLength();
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>();
		testPath.add(this.segment1);
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath, expectedCurvilineTrans);
		
		Segment1DStub expectedSegment = this.segment1;
		Point1D point = new Point1D(expectedSegment, this.RANDOM.nextFloat()*expectedSegment.getLength());
		point.clamp();
		float originalCurviline = point.getCurvilineCoordinate();
		
		if (expectedCurvilineTrans<0) {
			int segmentIndex = trans.transform(point);
			
			assertEquals(-1, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEquals(originalCurviline+expectedCurvilineTrans, point.getCurvilineCoordinate());
		}
		else {
			float expectedCurviline = originalCurviline + expectedCurvilineTrans;
			int expectedSegmentIndex = 0;
			
			int segmentIndex = trans.transform(point);

			assertEquals(expectedSegmentIndex, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEpsilonEquals(expectedCurviline, point.getCurvilineCoordinate());
		}
	}

	/** 
	 */
	public void testTransformPoint1D_OnOneSegmentPath_Reverted() {
		float expectedCurvilineTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * this.segment2.getLength();
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>();
		testPath.add(this.segment2);
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath, expectedCurvilineTrans);
		
		Segment1DStub expectedSegment = this.segment2;
		Point1D point = new Point1D(expectedSegment, this.RANDOM.nextFloat()*expectedSegment.getLength());
		point.clamp();
		float originalCurviline = point.getCurvilineCoordinate();
		
		if (expectedCurvilineTrans<0) {
			int segmentIndex = trans.transform(point);
			
			assertEquals(-1, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEquals(originalCurviline+expectedCurvilineTrans, point.getCurvilineCoordinate());
		}
		else {
			float expectedCurviline = originalCurviline + expectedCurvilineTrans;
			int expectedSegmentIndex = 0;
			
			int segmentIndex = trans.transform(point);

			assertEquals(expectedSegmentIndex, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEpsilonEquals(expectedCurviline, point.getCurvilineCoordinate());
		}
	}

	/** 
	 */
	public void testTransformPoint1D5_OnTwoSegmentPath() {
		float expectedCurvilineTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * (this.segment1.getLength()+this.segment2.getLength());
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>();
		testPath.add(this.segment1);
		testPath.add(this.segment2);
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath, expectedCurvilineTrans);
		
		Segment1DStub expectedSegment = this.segment1;
		Point1D point = new Point1D(expectedSegment, this.RANDOM.nextFloat()*expectedSegment.getLength());
		point.clamp();
		float originalCurviline = point.getCurvilineCoordinate();
		
		if (expectedCurvilineTrans<0) {
			int segmentIndex = trans.transform(point);
			
			assertEquals(-1, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEquals(originalCurviline+expectedCurvilineTrans, point.getCurvilineCoordinate());
		}
		else {
			float expectedCurviline = originalCurviline + expectedCurvilineTrans;
			int expectedSegmentIndex = 0;
			
			if (expectedCurviline>this.segment1.getLength()) {
				expectedCurviline -= this.segment1.getLength();
				expectedSegmentIndex = 1;
				expectedSegment = this.segment2;
				
				// Because segment2 is reverted
				expectedCurviline = this.segment2.getLength() - expectedCurviline;
			}
			
			int segmentIndex = trans.transform(point);

			assertEquals(expectedSegmentIndex, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEpsilonEquals(expectedCurviline, point.getCurvilineCoordinate());
		}
	}

	/** 
	 */
	public void testTransformPoint1D5_OnThreeSegmentPath() {
		float expectedCurvilineTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * (this.segment1.getLength()+this.segment2.getLength()+this.segment3.getLength());
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>();
		testPath.add(this.segment1);
		testPath.add(this.segment2);
		testPath.add(this.segment3);
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath, expectedCurvilineTrans);
		
		Segment1DStub expectedSegment = this.segment1;
		Point1D point = new Point1D(expectedSegment, this.RANDOM.nextFloat()*expectedSegment.getLength());
		point.clamp();
		float originalCurviline = point.getCurvilineCoordinate();
		
		if (expectedCurvilineTrans<0) {
			int segmentIndex = trans.transform(point);
			
			assertEquals(-1, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEquals(originalCurviline+expectedCurvilineTrans, point.getCurvilineCoordinate());
		}
		else {
			float expectedCurviline = originalCurviline + expectedCurvilineTrans;
			int expectedSegmentIndex = 0;
			
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
					expectedCurviline = this.segment2.getLength() - expectedCurviline;
				}
			}
			
			int segmentIndex = trans.transform(point);

			assertEquals(expectedSegmentIndex, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEpsilonEquals(expectedCurviline, point.getCurvilineCoordinate());
		}
	}

	/** 
	 */
	public void testTransformPoint1D5_OnFourSegmentPath() {
		float expectedCurvilineTrans = (this.RANDOM.nextFloat()-this.RANDOM.nextFloat()) * (this.segment1.getLength()+this.segment2.getLength()+this.segment3.getLength());
		List<Segment1DStub> testPath = new ArrayList<Segment1DStub>();
		testPath.add(this.segment1);
		testPath.add(this.segment2);
		testPath.add(this.segment3);
		testPath.add(this.segment4);
		Transform1D<Segment1DStub> trans = new Transform1D<Segment1DStub>(testPath, expectedCurvilineTrans);
		
		Segment1DStub expectedSegment = this.segment1;
		Point1D point = new Point1D(expectedSegment, this.RANDOM.nextFloat()*expectedSegment.getLength());
		point.clamp();
		float originalCurviline = point.getCurvilineCoordinate();
		
		if (expectedCurvilineTrans<0) {
			int segmentIndex = trans.transform(point);
			
			assertEquals(-1, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEquals(originalCurviline+expectedCurvilineTrans, point.getCurvilineCoordinate());
		}
		else {
			float expectedCurviline = originalCurviline + expectedCurvilineTrans;
			int expectedSegmentIndex = 0;
			
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
					expectedCurviline = this.segment2.getLength() - expectedCurviline;
				}
			}
			
			int segmentIndex = trans.transform(point);

			assertEquals(expectedSegmentIndex, segmentIndex);
			assertSame(expectedSegment, point.getSegment());
			assertEpsilonEquals(expectedCurviline, point.getCurvilineCoordinate());
		}
	}

}