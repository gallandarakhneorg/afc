/* 
 * $Id$
 * 
 * Copyright (c) 2008-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.bounds.bounds1d;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.bounds.GraphPointStub;
import org.arakhne.afc.math.bounds.GraphSegmentStub;
import org.arakhne.afc.math.bounds.bounds1f.BoundingInterval;
import org.arakhne.afc.math.bounds.bounds1f5.BoundingRect1D5;
import org.arakhne.afc.math.bounds.bounds2f.Bounds2D;
import org.arakhne.afc.math.bounds.bounds2f.OrientedBoundingRectangle;
import org.arakhne.afc.math.bounds.bounds3f.Bounds3D;
import org.arakhne.afc.math.bounds.bounds3f.OrientedBoundingBox;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.object.Point1D;
import org.arakhne.afc.math.system.CoordinateSystem2D;
import org.arakhne.afc.math.system.CoordinateSystem3D;

/**
 * Unit test for BoundingRect1D class.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BoundingInterval1DTest extends AbstractCombinableBound1DTest<GraphSegmentStub> {

	/** Reference instance.
	 */
	private BoundingInterval<GraphSegmentStub> testingBounds;
	
	/** Reference instance.
	 */
	private float[] testingPoints;

	/** Reference lower point.
	 */
	private float lowerPoint;

	/** Reference upper point.
	 */
	private float upperPoint;

	/** Reference center point.
	 */
	private float centerPoint;
		
	/**
	 * Segment
	 */
	private GraphSegmentStub segment;

	/**
	 * Segment
	 */
	private GraphSegmentStub otherSegment;

	/**
	 * Entry point
	 */
	private GraphPointStub entryPoint;

	/**
	 * Exit point
	 */
	private GraphPointStub exitPoint;

	/**
	 * Exit point
	 */
	private GraphPointStub exitPoint2;

	private float[] randomPoints() {
		int count = this.RANDOM.nextInt(100)+10;
		float[] tab = new float[count];
		for(int i=0; i<count; ++i) {
			tab[i] = this.RANDOM.nextFloat() * 500.f;
		}
		return tab;
	}
		
	/**
	 * {@inheritDoc}
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.entryPoint = new GraphPointStub();
		this.exitPoint = new GraphPointStub();
		this.segment = new GraphSegmentStub(this.entryPoint, this.exitPoint, this.RANDOM.nextFloat()*100.f);
		
		this.exitPoint2 = new GraphPointStub();
		this.otherSegment = new GraphSegmentStub(this.entryPoint, this.exitPoint2, this.segment.getLength()+this.RANDOM.nextFloat());

		this.testingPoints = randomPoints();
		Point2f p = new Point2f();
		computeLowerUpper(this.testingPoints,p);
		this.lowerPoint = p.getX();
		this.upperPoint = p.getY();
		this.centerPoint = (this.lowerPoint+this.upperPoint)/2.f;
		
		// Clamp the coordinates
		if (this.centerPoint<0) {
			this.lowerPoint -= this.centerPoint;
			this.upperPoint -= this.centerPoint;
			this.centerPoint = 0;
		}
		else if (this.centerPoint>this.segment.getLength()) {
			this.lowerPoint -= (this.centerPoint-this.segment.getLength());
			this.upperPoint -= (this.centerPoint-this.segment.getLength());
			this.centerPoint = this.segment.getLength();
		}
		
		this.testingBounds = createBounds(this.testingPoints);
		
		//
		assertNumber(this.lowerPoint);
		assertNumber(this.upperPoint);
		assertNumber(this.centerPoint);
		
		//
		assertEpsilonEquals(this.lowerPoint, this.testingBounds.getMinX());
		assertEpsilonEquals(this.upperPoint, this.testingBounds.getMaxX());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		this.testingBounds = null;
		this.testingPoints = null;
		this.entryPoint = null;
		this.exitPoint = null;
		this.segment = null;
		super.tearDown();
	}

	/**
	 * @param points
	 * @return bounds
	 */
	private BoundingInterval<GraphSegmentStub> createBounds(float[] points) {
		return new BoundingInterval<GraphSegmentStub>(this.segment, points);
	}

	/** Compute the lower, upper and center points.
	 * @param points
	 * @param lower
	 * @param upper
	 */
	private static void computeLowerUpper(float[] points, Tuple2f p) {
		float lx = Float.NaN;
		float ux = Float.NaN;
		
		for(float x : points) {
			if (Float.isNaN(lx) || x<lx) lx = x;
			if (Float.isNaN(ux) || x>ux) ux = x;
		}
		
		if (p!=null) p.set(lx,ux);
	}
	
	private void assertBoundsOnSegment(float lower, float upper) {
		/*float l = lower;
		float u = upper;
		float c = (lower+upper)/2.;
		if (c<0) {
			l -= c;
			u -= c;
		}
		float length = this.testingBounds.getSegment().getLength();
		if (c>length) {
			l -= (c-length);
			u -= (c-length);
		}*/
		warning("Not yet implemented"); //$NON-NLS-1$
		//assertEpsilonEquals("lower", l, this.testingBounds.lower); //$NON-NLS-1$
		//assertEpsilonEquals("upper", u, this.testingBounds.upper); //$NON-NLS-1$
	}

	@Override
	public void testClamp() {
		this.testingBounds.clamp();
		float c = this.testingBounds.getCenterX();
		assertPositive(c);
		assertTrue(c+"<="+this.segment.getLength(), c <= this.segment.getLength()+MathUtil.getEpsilon()); //$NON-NLS-1$
		
		float d = c - this.testingBounds.getMinX();
		
		assertEpsilonEquals(d, this.testingBounds.getMaxX() - c);
	}

	@Override
	public void testCombineFloatArray() {
		float[] nPoints = randomPoints();
		Point2f p = new Point2f();
		computeLowerUpper(nPoints, p);
		float min = p.getX();
		float max = p.getY();
		
		if (min>this.lowerPoint) min = this.lowerPoint;
		if (max<this.upperPoint) max = this.upperPoint;

		float center = (min+max) / 2.f;

		if (center<0) {
			min -= center;
			max -= center;
			center = 0;
		}
		if (center>this.segment.getLength()) {
			min -= (center-this.segment.getLength());
			max -= (center-this.segment.getLength());
			center = this.segment.getLength();
		}

		this.testingBounds.combine(nPoints);
				
		assertBoundsOnSegment(min, max);
	}

	@Override
	public void testGetCenterX() {
		float p = this.testingBounds.getCenterX();
		assertNumber(this.centerPoint);
		assertNumber(p);
		assertEpsilonEquals(this.centerPoint, p);
	}

	@Override
	public void testGetMaxX() {
		float p = this.testingBounds.getMaxX();
		assertNumber(this.upperPoint);
		assertNumber(p);
		assertEpsilonEquals(this.upperPoint, p);
	}

	@Override
	public void testGetMinX() {
		float p = this.testingBounds.getMinX();
		assertNumber(this.lowerPoint);
		assertNumber(p);
		assertEpsilonEquals(this.lowerPoint, p);
	}

	@Override
	public void testGetSegment() {
		assertSame(this.segment, this.testingBounds.getSegment());
		assertNotSame(this.otherSegment, this.testingBounds.getSegment());
	}

	@Override
	public void testGetSize() {
		float p = this.testingBounds.getSize();
		assertNumber(this.upperPoint);
		assertNumber(this.lowerPoint);
		assertNumber(p);
		assertEpsilonEquals(this.upperPoint-this.lowerPoint, p);
	}

	@Override
	public void testSetBoxBounds1D() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		float a, b;
		
		assertTrue(x1<=x2);
		
		a = 0.f;
		b = 0.f;
		
		BoundingInterval<GraphSegmentStub> box = new BoundingInterval<GraphSegmentStub>(this.segment);

		try {
			box.setBox(x1, x2);
			this.testingBounds.setBox(box);
			
			assertEpsilonEquals(x1, this.testingBounds.getLower().getCurvilineCoordinate());
			assertEpsilonEquals(x2, this.testingBounds.getUpper().getCurvilineCoordinate());
	
			a = -3.f*x2;
			b = x2;
			box.setBox(a, b);
			this.testingBounds.setBox(box);
	
			assertEpsilonEquals(a, this.testingBounds.getLower().getCurvilineCoordinate());
			assertEpsilonEquals(b, this.testingBounds.getUpper().getCurvilineCoordinate());
	
			a = x1;
			b = 2.f*this.segment.getLength()+x1;
			box.setBox(a, b);
			this.testingBounds.setBox(box);
	
			assertEpsilonEquals(a, this.testingBounds.getLower().getCurvilineCoordinate());
			assertEpsilonEquals(b, this.testingBounds.getUpper().getCurvilineCoordinate());
	
			a = -x1;
			b = 18.f*this.segment.getLength();
			box.setBox(a, b);
			this.testingBounds.setBox(box);
	
			assertEpsilonEquals(a, this.testingBounds.getLower().getCurvilineCoordinate());
			assertEpsilonEquals(b, this.testingBounds.getUpper().getCurvilineCoordinate());
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x1", x1, //$NON-NLS-1$
					"x2", x2, //$NON-NLS-1$
					"a", a, //$NON-NLS-1$
					"b", b); //$NON-NLS-1$
		}		
	}

	@Override
	public void testSetBoxFloatFloat() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		float a, b;
		
		assertTrue(x1<=x2);
		
		a = 0.f;
		b = 0.f;
		
		try {
			this.testingBounds.setBox(x1, x2);
			
			assertEpsilonEquals(x1, this.testingBounds.getMinX());
			assertEpsilonEquals(x2, this.testingBounds.getMaxX());
	
			a = -3.f*x2;
			b = x2;
			this.testingBounds.setBox(a, b);
	
			assertEpsilonEquals(a, this.testingBounds.getMinX());
			assertEpsilonEquals(b, this.testingBounds.getMaxX());
	
			a = x1;
			b = 2.f*this.segment.getLength()+x1;
			this.testingBounds.setBox(a, b);
	
			assertEpsilonEquals(a, this.testingBounds.getMinX());
			assertEpsilonEquals(b, this.testingBounds.getMaxX());
	
			a = -x1;
			b = 18.f*this.segment.getLength();
			this.testingBounds.setBox(a, b);
	
			assertEpsilonEquals(a, this.testingBounds.getMinX());
			assertEpsilonEquals(b, this.testingBounds.getMaxX());
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x1", x1, //$NON-NLS-1$
					"x2", x2, //$NON-NLS-1$
					"a", a, //$NON-NLS-1$
					"b", b); //$NON-NLS-1$
		}
	}

	@Override
	public void testSetFloatArray() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		float a, b, c;
		
		assertTrue(x1<=x2);
		
		a = 0.f;
		b = 0.f;
		c = 0.f;
		
		try {
			this.testingBounds.set(x1, x2);
			
			assertEpsilonEquals(x1, this.testingBounds.getMinX());
			assertEpsilonEquals(x2, this.testingBounds.getMaxX());
	
			a = -3.f*x2;
			b = x2;
			c = (a+b)/2.f;
			this.testingBounds.set(a, b);
	
			assertEpsilonEquals(a-c, this.testingBounds.getMinX());
			assertEpsilonEquals(b-c, this.testingBounds.getMaxX());
	
			a = x1;
			b = 2.f*this.segment.getLength()+x1;
			c = (a+b)/2.f;
			this.testingBounds.set(a, b);
	
			assertEpsilonEquals(a-(c-this.segment.getLength()), this.testingBounds.getMinX());
			assertEpsilonEquals(b-(c-this.segment.getLength()), this.testingBounds.getMaxX());
	
			a = -x1;
			b = 18.f*this.segment.getLength();
			c = (a+b)/2.f;
			this.testingBounds.set(a, b);
	
			assertEpsilonEquals(a-(c-this.segment.getLength()), this.testingBounds.getMinX());
			assertEpsilonEquals(b-(c-this.segment.getLength()), this.testingBounds.getMaxX());
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x1", x1, //$NON-NLS-1$
					"x2", x2, //$NON-NLS-1$
					"a", a, //$NON-NLS-1$
					"b", b, //$NON-NLS-1$
					"c", c); //$NON-NLS-1$
		}
	}

	@Override
	public void testSetSegmentS() {
		GraphSegmentStub newSegment = new GraphSegmentStub(this.entryPoint, this.exitPoint, 100.f);

		assertSame(this.segment, this.testingBounds.getSegment());
		assertNotSame(this.otherSegment, this.testingBounds.getSegment());
		assertNotSame(newSegment, this.testingBounds.getSegment());

		this.testingBounds.setSegment(this.segment);
		
		assertSame(this.segment, this.testingBounds.getSegment());
		assertNotSame(this.otherSegment, this.testingBounds.getSegment());
		assertNotSame(newSegment, this.testingBounds.getSegment());

		this.testingBounds.setSegment(newSegment);

		assertNotSame(this.segment, this.testingBounds.getSegment());
		assertNotSame(this.otherSegment, this.testingBounds.getSegment());
		assertSame(newSegment, this.testingBounds.getSegment());
	}

	@Override
	public void testCombineCB() {
		try {
			BoundingInterval<GraphSegmentStub> newInterval;

			float lower = this.lowerPoint;
			float upper = this.upperPoint;
			
			newInterval = new BoundingInterval<GraphSegmentStub>(this.segment, this.lowerPoint, this.upperPoint);
			this.testingBounds.combine(newInterval);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(lower, upper);

			newInterval = new BoundingInterval<GraphSegmentStub>(this.segment, this.lowerPoint-10, this.upperPoint);
			this.testingBounds.combine(newInterval);
			assertSame(this.segment, this.testingBounds.getSegment());
			lower -= 10;
			assertBoundsOnSegment(lower, upper);
		
			newInterval = new BoundingInterval<GraphSegmentStub>(this.segment, this.lowerPoint, this.upperPoint+100);
			this.testingBounds.combine(newInterval);
			assertSame(this.segment, this.testingBounds.getSegment());
			// newInterval box was upper clamped by 50
			assertEpsilonEquals(-50.f, newInterval.getMinX() - this.lowerPoint);
			assertEpsilonEquals(-50.f, newInterval.getMaxX() - this.upperPoint - 100f);
			lower -= 40.f; // 50-10 because lower==this.lowerPoint-10
			upper += 50.f;
			assertBoundsOnSegment(lower, upper);

			newInterval = new BoundingInterval<GraphSegmentStub>(this.segment, this.lowerPoint, this.upperPoint);
			this.testingBounds.combine(newInterval);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(lower, upper);
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength()); //$NON-NLS-1$
		}
	}

	@Override
	public void testCombineCollection() {
		try {
			List<BoundingInterval<GraphSegmentStub>> newIntervals =
				new ArrayList<BoundingInterval<GraphSegmentStub>>();
			
			newIntervals.add(new BoundingInterval<GraphSegmentStub>(this.segment, this.lowerPoint, this.upperPoint));
			newIntervals.add(new BoundingInterval<GraphSegmentStub>(this.segment, this.lowerPoint, this.upperPoint));
			newIntervals.add(new BoundingInterval<GraphSegmentStub>(this.segment, this.lowerPoint, this.upperPoint+100));
			newIntervals.add(new BoundingInterval<GraphSegmentStub>(this.segment, this.lowerPoint-10, this.upperPoint));
			
			float lower = this.lowerPoint - 50.f;
			float upper = this.upperPoint + 50.f;
			
			this.testingBounds.combine(newIntervals);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(lower, upper);
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength()); //$NON-NLS-1$
		}
	}
	
	@Override
	public void testCombineP() {
		try {
			Point1D newPt;
			
			newPt = new Point1D(this.segment, this.lowerPoint);
			this.testingBounds.combine(newPt);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(this.lowerPoint, this.upperPoint);

			newPt = new Point1D(this.segment, this.lowerPoint-10);
			this.testingBounds.combine(newPt);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(this.lowerPoint-10, this.upperPoint);
		
			newPt = new Point1D(this.segment, this.upperPoint+100);
			this.testingBounds.combine(newPt);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(this.lowerPoint-10, this.upperPoint+100);

			newPt = new Point1D(this.segment, this.lowerPoint);
			this.testingBounds.combine(newPt);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(this.lowerPoint-10, this.upperPoint+100);
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength()); //$NON-NLS-1$
		}
	}

	@Override
	public void testIsInit() {
		BoundingInterval<GraphSegmentStub> b1 = new BoundingInterval<GraphSegmentStub>(this.segment);
		BoundingInterval<GraphSegmentStub> b2 = new BoundingInterval<GraphSegmentStub>(this.segment);
		b2.setBox(0, 0);
		assertFalse(b1.isInit());
		assertTrue(b2.isInit());
		assertTrue(this.testingBounds.isInit());
	}

	@Override
	public void testReset() {
		assertTrue(this.testingBounds.isInit());
		this.testingBounds.reset();
		assertFalse(this.testingBounds.isInit());
	}

	@Override
	public void testSetCB() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		float a, b, c;
		
		assertTrue(x1<=x2);
		
		a = 0.f;
		b = 0.f;
		c = 0.f;
		
		try {
			this.testingBounds.setSegment(this.otherSegment);
			assertSame(this.otherSegment, this.testingBounds.getSegment());
			
			BoundingInterval<GraphSegmentStub> box = new BoundingInterval<GraphSegmentStub>(this.segment);
			
			box.setBox(x1, x2);
			this.testingBounds.set(box);
			
			assertSame(this.segment, this.testingBounds.getSegment());
			assertEpsilonEquals(x1, this.testingBounds.getMinX());
			assertEpsilonEquals(x2, this.testingBounds.getMaxX());
	
			this.testingBounds.setSegment(this.otherSegment);
			assertSame(this.otherSegment, this.testingBounds.getSegment());
	
			a = -3.f*x2;
			b = x2;
			c = (a+b)/2.f;
			box.setBox(a, b);
			this.testingBounds.set(box);
	
			assertSame(this.segment, this.testingBounds.getSegment());
			assertEpsilonEquals(a-c, this.testingBounds.getMinX());
			assertEpsilonEquals(b-c, this.testingBounds.getMaxX());
	
			this.testingBounds.setSegment(this.otherSegment);
			assertSame(this.otherSegment, this.testingBounds.getSegment());
	
			a = x1;
			b = 2.f*this.segment.getLength()+x1;
			c = (a+b)/2.f;
			box.setBox(a, b);
			this.testingBounds.set(box);
	
			assertSame(this.segment, this.testingBounds.getSegment());
			assertEpsilonEquals(a-(c-this.segment.getLength()), this.testingBounds.getMinX());
			assertEpsilonEquals(b-(c-this.segment.getLength()), this.testingBounds.getMaxX());
	
			this.testingBounds.setSegment(this.otherSegment);
			assertSame(this.otherSegment, this.testingBounds.getSegment());
	
			a = -x1;
			b = 18.f*this.segment.getLength();
			c = (a+b)/2.f;
			box.setBox(a, b);
			this.testingBounds.set(box);
	
			assertSame(this.segment, this.testingBounds.getSegment());
			assertEpsilonEquals(a-(c-this.segment.getLength()), this.testingBounds.getMinX());
			assertEpsilonEquals(b-(c-this.segment.getLength()), this.testingBounds.getMaxX());
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x1", x1, //$NON-NLS-1$
					"x2", x2, //$NON-NLS-1$
					"a", a, //$NON-NLS-1$
					"b", b, //$NON-NLS-1$
					"c", c); //$NON-NLS-1$
		}
	}

	@Override
	public void testSetP() {
		float x = this.RANDOM.nextFloat() * this.segment.getLength();
		float a, b, c;
		
		a = 0.f;
		b = 0.f;
		c = 0.f;
		
		try {
			Point1D point = new Point1D(this.segment, x);
			this.testingBounds.set(point);
			
			assertSame(this.segment, this.testingBounds.getSegment());
			assertEpsilonEquals(x, this.testingBounds.getMinX());
			assertEpsilonEquals(x, this.testingBounds.getMaxX());
	
			point = new Point1D(this.otherSegment, x);
			this.testingBounds.set(point);
			
			assertSame(this.otherSegment, this.testingBounds.getSegment());
			assertEpsilonEquals(x, this.testingBounds.getMinX());
			assertEpsilonEquals(x, this.testingBounds.getMaxX());
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x", x, //$NON-NLS-1$
					"a", a, //$NON-NLS-1$
					"b", b, //$NON-NLS-1$
					"c", c); //$NON-NLS-1$
		}
	}

	@Override
	public void testClone() {
		BoundingInterval<GraphSegmentStub> clone = this.testingBounds.clone();
		assertNotSame(this.testingBounds, clone);
		assertSame(this.testingBounds.getSegment(), clone.getSegment());
		assertEquals(this.testingBounds.getMinX(), clone.getMinX());
		assertEquals(this.testingBounds.getMaxX(), clone.getMaxX());
	}

	@Override
	public void testDistanceMaxP() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		
		Point1D p;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2);
			
			p = new Point1D(this.segment, x1);
			assertEpsilonEquals(x2-x1, this.testingBounds.distanceMax(p));

			p = new Point1D(this.otherSegment, x1);
			assertNaN(this.testingBounds.distanceMax(p));
			
			p = new Point1D(this.segment, x2);
			assertEpsilonEquals(x2-x1, this.testingBounds.distanceMax(p));

			p = new Point1D(this.otherSegment, x2);
			assertNaN(this.testingBounds.distanceMax(p));

			p = new Point1D(this.segment, (x1+x2)/2.f);
			assertEpsilonEquals((x2-x1)/2.f, this.testingBounds.distanceMax(p));

			p = new Point1D(this.otherSegment, (x1+x2)/2.f);
			assertNaN(this.testingBounds.distanceMax(p));

			p = new Point1D(this.segment, x1 - 10);
			assertEpsilonEquals(x2-x1+10.f, this.testingBounds.distanceMax(p));

			p = new Point1D(this.otherSegment, x1 - 10);
			assertNaN(this.testingBounds.distanceMax(p));

			p = new Point1D(this.segment, x2 + 20);
			assertEpsilonEquals(x2-x1+20.f, this.testingBounds.distanceMax(p));

			p = new Point1D(this.otherSegment, x2 + 20);
			assertNaN(this.testingBounds.distanceMax(p));

		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x1", x1, //$NON-NLS-1$
					"x2", x2); //$NON-NLS-1$
		}
	}


	@Override
	public void testDistanceMaxSquaredP() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		
		Point1D p;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2);
			
			p = new Point1D(this.segment, x1);
			assertEpsilonEquals((float) Math.pow(x2-x1,2.), this.testingBounds.distanceMaxSquared(p));

			p = new Point1D(this.otherSegment, x1);
			assertNaN(this.testingBounds.distanceMaxSquared(p));
			
			p = new Point1D(this.segment, x2);
			assertEpsilonEquals((float) Math.pow(x2-x1,2.), this.testingBounds.distanceMaxSquared(p));

			p = new Point1D(this.otherSegment, x2);
			assertNaN(this.testingBounds.distanceMaxSquared(p));

			p = new Point1D(this.segment, (x1+x2)/2.f);
			assertEpsilonEquals((float) Math.pow((x2-x1)/2.,2.), this.testingBounds.distanceMaxSquared(p));

			p = new Point1D(this.otherSegment, (x1+x2)/2.f);
			assertNaN(this.testingBounds.distanceMaxSquared(p));

			p = new Point1D(this.segment, x1 - 10);
			assertEpsilonEquals((float) Math.pow(x2-x1+10.,2.), this.testingBounds.distanceMaxSquared(p));

			p = new Point1D(this.otherSegment, x1 - 10);
			assertNaN(this.testingBounds.distanceMaxSquared(p));

			p = new Point1D(this.segment, x2 + 20);
			assertEpsilonEquals((float) Math.pow(x2-x1+20.,2.), this.testingBounds.distanceMaxSquared(p));

			p = new Point1D(this.otherSegment, x2 + 20);
			assertNaN(this.testingBounds.distanceMaxSquared(p));

		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x1", x1, //$NON-NLS-1$
					"x2", x2); //$NON-NLS-1$
		}
	}

	@Override
	public void testDistanceP() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		
		Point1D p;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2);
			
			p = new Point1D(this.segment, x1);
			assertEpsilonEquals(0.f, this.testingBounds.distance(p));

			p = new Point1D(this.otherSegment, x1);
			assertNaN(this.testingBounds.distance(p));
			
			p = new Point1D(this.segment, x2);
			assertEpsilonEquals(0.f, this.testingBounds.distance(p));

			p = new Point1D(this.otherSegment, x2);
			assertNaN(this.testingBounds.distance(p));

			p = new Point1D(this.segment, (x1+x2)/2.f);
			assertEpsilonEquals(0.f, this.testingBounds.distance(p));

			p = new Point1D(this.otherSegment, (x1+x2)/2.f);
			assertNaN(this.testingBounds.distance(p));

			p = new Point1D(this.segment, x1 - 10);
			assertEpsilonEquals(10.f, this.testingBounds.distance(p));

			p = new Point1D(this.otherSegment, x1 - 10);
			assertNaN(this.testingBounds.distance(p));

			p = new Point1D(this.segment, x2 + 20);
			assertEpsilonEquals(20.f, this.testingBounds.distance(p));

			p = new Point1D(this.otherSegment, x2 + 20);
			assertNaN(this.testingBounds.distance(p));

		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x1", x1, //$NON-NLS-1$
					"x2", x2); //$NON-NLS-1$
		}
	}


	@Override
	public void testDistanceSquaredP() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		
		Point1D p;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2);
			
			p = new Point1D(this.segment, x1);
			assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(p));

			p = new Point1D(this.otherSegment, x1);
			assertNaN(this.testingBounds.distanceSquared(p));
			
			p = new Point1D(this.segment, x2);
			assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(p));

			p = new Point1D(this.otherSegment, x2);
			assertNaN(this.testingBounds.distanceSquared(p));

			p = new Point1D(this.segment, (x1+x2)/2.f);
			assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(p));

			p = new Point1D(this.otherSegment, (x1+x2)/2.f);
			assertNaN(this.testingBounds.distanceSquared(p));

			p = new Point1D(this.segment, x1 - 10);
			assertEpsilonEquals(10.f*10.f, this.testingBounds.distanceSquared(p));

			p = new Point1D(this.otherSegment, x1 - 10);
			assertNaN(this.testingBounds.distanceSquared(p));

			p = new Point1D(this.segment, x2 + 20);
			assertEpsilonEquals(20.f*20.f, this.testingBounds.distanceSquared(p));

			p = new Point1D(this.otherSegment, x2 + 20);
			assertNaN(this.testingBounds.distanceSquared(p));

		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x1", x1, //$NON-NLS-1$
					"x2", x2); //$NON-NLS-1$
		}
	}

	@Override
	public void testGetCenter() {
		Point1D p = this.testingBounds.getCenter();
		assertNumber(this.centerPoint);
		assertNumber(p.getCurvilineCoordinate());
		assertSame(this.segment, p.getSegment());
		assertEpsilonEquals(this.centerPoint, p.getCurvilineCoordinate());
	}

	@Override
	public void testGetLower() {
		Point1D p = this.testingBounds.getLower();
		assertNumber(this.lowerPoint);
		assertNumber(p.getCurvilineCoordinate());
		assertSame(this.segment, p.getSegment());
		assertEpsilonEquals(this.lowerPoint, p.getCurvilineCoordinate());
	}

	@Override
	public void testGetLowerUpper() {
		Point1D l = new Point1D(this.segment);
		Point1D u = new Point1D(this.segment);
		this.testingBounds.getLowerUpper(l, u);
		
		assertNumber(this.lowerPoint);
		assertNumber(l.getCurvilineCoordinate());
		assertSame(this.segment, l.getSegment());
		assertEpsilonEquals(this.lowerPoint, l.getCurvilineCoordinate());

		assertNumber(this.upperPoint);
		assertNumber(u.getCurvilineCoordinate());
		assertSame(this.segment, u.getSegment());
		assertEpsilonEquals(this.upperPoint, u.getCurvilineCoordinate());
	}

	@Override
	public void testGetPosition() {
		Point1D p = this.testingBounds.getPosition();
		assertNumber(this.centerPoint);
		assertNumber(p.getCurvilineCoordinate());
		assertSame(this.segment, p.getSegment());
		assertEpsilonEquals(this.centerPoint, p.getCurvilineCoordinate());
	}

	@Override
	public void testGetMathematicalDimension() {
		assertEquals(PseudoHamelDimension.DIMENSION_1D, this.testingBounds.getMathematicalDimension());
	}

	@Override
	public void testGetUpper() {
		Point1D p = this.testingBounds.getUpper();
		assertNumber(this.upperPoint);
		assertNumber(p.getCurvilineCoordinate());
		assertSame(this.segment, p.getSegment());
		assertEpsilonEquals(this.upperPoint, p.getCurvilineCoordinate());
	}

	@Override
	public void testIsEmpty() {
		BoundingInterval<GraphSegmentStub> b1 = new BoundingInterval<GraphSegmentStub>(this.segment);
		BoundingInterval<GraphSegmentStub> b2 = new BoundingInterval<GraphSegmentStub>(this.segment);
		b2.setBox(0, 0);
		assertTrue(b1.isEmpty());
		assertTrue(b2.isEmpty());
		assertFalse(this.testingBounds.isEmpty());
	}
			
	@Override
	public void testToBounds1D5() {
		BoundingRect1D5<GraphSegmentStub> rect = this.testingBounds.toBounds1D5();
		
		assertNotNull(rect);
		assertSame(this.segment, rect.getSegment());
		assertEpsilonEquals(this.lowerPoint, rect.getMinX());
		assertEpsilonEquals(this.upperPoint, rect.getMaxX());
		assertEpsilonEquals(0.f, rect.getJutting());
		assertEpsilonEquals(0.f, rect.getLateralSize());
	}

	@Override
	public void testToBounds1D5FloatFloat() {
		BoundingRect1D5<GraphSegmentStub> rect = this.testingBounds.toBounds1D5(2345.f, 45674.f);
		
		assertNotNull(rect);
		assertSame(this.segment, rect.getSegment());
		assertEpsilonEquals(this.lowerPoint, rect.getMinX());
		assertEpsilonEquals(this.upperPoint, rect.getMaxX());
		assertEpsilonEquals(2345.f, rect.getJutting());
		assertEpsilonEquals(45674.f, rect.getLateralSize());

		rect = this.testingBounds.toBounds1D5(2345.f, -45674.f);
		
		assertNotNull(rect);
		assertSame(this.segment, rect.getSegment());
		assertEpsilonEquals(this.lowerPoint, rect.getMinX());
		assertEpsilonEquals(this.upperPoint, rect.getMaxX());
		assertEpsilonEquals(2345.f, rect.getJutting());
		assertEpsilonEquals(45674.f, rect.getLateralSize());

		rect = this.testingBounds.toBounds1D5(-2345.f, 45674.f);
		
		assertNotNull(rect);
		assertSame(this.segment, rect.getSegment());
		assertEpsilonEquals(this.lowerPoint, rect.getMinX());
		assertEpsilonEquals(this.upperPoint, rect.getMaxX());
		assertEpsilonEquals(-2345.f, rect.getJutting());
		assertEpsilonEquals(45674.f, rect.getLateralSize());

		rect = this.testingBounds.toBounds1D5(-2345.f, -45674.f);
		
		assertNotNull(rect);
		assertSame(this.segment, rect.getSegment());
		assertEpsilonEquals(this.lowerPoint, rect.getMinX());
		assertEpsilonEquals(this.upperPoint, rect.getMaxX());
		assertEpsilonEquals(-2345.f, rect.getJutting());
		assertEpsilonEquals(45674.f, rect.getLateralSize());
	}

	@Override
	public void testToBounds2D() {
		Bounds2D b = this.testingBounds.toBounds2D();
		
		assertNotNull(b);
		assertTrue(b instanceof OrientedBoundingRectangle);
		OrientedBoundingRectangle obr = (OrientedBoundingRectangle)b;
		Point2f pos = new Point2f();
		Vector2f tangent = new Vector2f();
		this.segment.projectsOnPlane(this.testingBounds.getCenterX(), pos, tangent, CoordinateSystem2D.getDefaultCoordinateSystem());
		assertEpsilonEquals(pos, obr.getCenter());
		assertEpsilonEquals(tangent, obr.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(this.testingBounds.getSize()/2.f, obr.getRExtent());
		assertEpsilonEquals(0.f, obr.getSExtent());
	}

	@Override
	public void testToBounds2DCoordinateSystem2D() {
		for(CoordinateSystem2D cs : CoordinateSystem2D.values()) {
			Bounds2D b = this.testingBounds.toBounds2D(cs);
			
			assertNotNull(b);
			assertTrue(b instanceof OrientedBoundingRectangle);
			OrientedBoundingRectangle obr = (OrientedBoundingRectangle)b;
			Point2f pos = new Point2f();
			Vector2f tangent = new Vector2f();
			this.segment.projectsOnPlane(this.testingBounds.getCenterX(), pos, tangent, cs);
			assertEpsilonEquals(cs.name(), pos, obr.getCenter());
			assertEpsilonEquals(cs.name(), tangent, obr.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(cs.name(), this.testingBounds.getSize()/2.f, obr.getRExtent());
			assertEpsilonEquals(cs.name(), 0.f, obr.getSExtent());
		}
	}

	@Override
	public void testToBounds2DFloatCoordinateSystem2D() {
		float lateralSize = this.RANDOM.nextFloat() * 10.f;
		for(CoordinateSystem2D cs : CoordinateSystem2D.values()) {
			Bounds2D b = this.testingBounds.toBounds2D(lateralSize, cs);
			
			assertNotNull(b);
			assertTrue(b instanceof OrientedBoundingRectangle);
			OrientedBoundingRectangle obr = (OrientedBoundingRectangle)b;
			Point2f pos = new Point2f();
			Vector2f tangent = new Vector2f();
			this.segment.projectsOnPlane(this.testingBounds.getCenterX(), pos, tangent, cs);
			assertEpsilonEquals(cs.name(), pos, obr.getCenter());
			assertEpsilonEquals(cs.name(), tangent, obr.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(cs.name(), this.testingBounds.getSize()/2.f, obr.getRExtent());
			assertEpsilonEquals(cs.name(), lateralSize/2.f, obr.getSExtent());
		}
	}

	@Override
	public void testToBounds3D() {
		Bounds3D b = this.testingBounds.toBounds3D();
		
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		assertNotNull(b);
		assertTrue(b instanceof OrientedBoundingBox);
		OrientedBoundingBox obr = (OrientedBoundingBox)b;
		Point2f pos = new Point2f();
		Vector2f tangent = new Vector2f();
		this.segment.projectsOnPlane(this.testingBounds.getCenterX(), pos, tangent, cs.toCoordinateSystem2D());
		Point3f p = cs.fromCoordinateSystem2D(pos);
		Vector3f v = cs.fromCoordinateSystem2D(tangent);
		assertEpsilonEquals(p, obr.getCenter());
		assertEpsilonEquals(v, obr.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(this.testingBounds.getSize()/2.f, obr.getRExtent());
		assertEpsilonEquals(0.f, obr.getSExtent());
		assertEpsilonEquals(0.f, obr.getTExtent());
	}

	@Override
	public void testToBounds3DCoordinateSystem3D() {
		for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			Bounds3D b = this.testingBounds.toBounds3D(cs);
			
			assertNotNull(b);
			assertTrue(b instanceof OrientedBoundingBox);
			OrientedBoundingBox obr = (OrientedBoundingBox)b;
			Point2f pos = new Point2f();
			Vector2f tangent = new Vector2f();
			this.segment.projectsOnPlane(this.testingBounds.getCenterX(), pos, tangent, cs.toCoordinateSystem2D());
			Point3f p = cs.fromCoordinateSystem2D(pos);
			Vector3f v = cs.fromCoordinateSystem2D(tangent);
			assertEpsilonEquals(cs.name(), p, obr.getCenter());
			assertEpsilonEquals(cs.name(), v, obr.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(cs.name(), this.testingBounds.getSize()/2.f, obr.getRExtent());
			assertEpsilonEquals(cs.name(), 0.f, obr.getSExtent());
			assertEpsilonEquals(cs.name(), 0.f, obr.getTExtent());
		}
	}

	@Override
	public void testToBounds3DFloatFloatCoordinateSystem3D() {
		float posZ = this.RANDOM.nextFloat() * 100.f;
		float sizeZ = this.RANDOM.nextFloat() * 10.f;
		for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			Bounds3D b = this.testingBounds.toBounds3D(posZ, sizeZ, cs);
			
			assertNotNull(b);
			assertTrue(b instanceof OrientedBoundingBox);
			OrientedBoundingBox obr = (OrientedBoundingBox)b;
			Point2f pos = new Point2f();
			Vector2f tangent = new Vector2f();
			this.segment.projectsOnPlane(this.testingBounds.getCenterX(), pos, tangent, cs.toCoordinateSystem2D());
			Point3f p = cs.fromCoordinateSystem2D(pos, posZ);
			Vector3f v = cs.fromCoordinateSystem2D(tangent, 0.f);
			assertEpsilonEquals(cs.name(), p, obr.getCenter());
			assertEpsilonEquals(cs.name(), v, obr.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(cs.name(), this.testingBounds.getSize()/2.f, obr.getRExtent());
			assertEpsilonEquals(cs.name(), 0.f, obr.getSExtent());
			assertEpsilonEquals(cs.name(), sizeZ/2.f, obr.getTExtent());
		}
	}

	@Override
	public void testToBounds3DFloatFloatFloatCoordinateSystem3D() {
		float lateralSize = this.RANDOM.nextFloat() * 10.f;
		float posZ = this.RANDOM.nextFloat() * 100.f;
		float sizeZ = this.RANDOM.nextFloat() * 10.f;
		for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			Bounds3D b = this.testingBounds.toBounds3D(lateralSize, posZ, sizeZ, cs);
			
			assertNotNull(b);
			assertTrue(b instanceof OrientedBoundingBox);
			OrientedBoundingBox obr = (OrientedBoundingBox)b;
			Point2f pos = new Point2f();
			Vector2f tangent = new Vector2f();
			this.segment.projectsOnPlane(this.testingBounds.getCenterX(), pos, tangent, cs.toCoordinateSystem2D());
			Point3f p = cs.fromCoordinateSystem2D(pos, posZ);
			Vector3f v = cs.fromCoordinateSystem2D(tangent, 0.f);
			assertEpsilonEquals(cs.name(), p, obr.getCenter());
			assertEpsilonEquals(cs.name(), v, obr.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(cs.name(), this.testingBounds.getSize()/2.f, obr.getRExtent());
			assertEpsilonEquals(cs.name(), lateralSize/2.f, obr.getSExtent());
			assertEpsilonEquals(cs.name(), sizeZ/2.f, obr.getTExtent());
		}
	}

	//------------------------------------------
	// IntersectionClassifier
	//------------------------------------------
	
	@Override
	public void testClassifiesIC() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	@Override
	public void testClassifiesP() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	@Override
	public void testClassifiesPP() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	@Override
	public void testIntersectsIC() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	@Override
	public void testIntersectsP() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	@Override
	public void testIntersectsPP() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	@Override
	public void testFarestPointP() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		
		Point1D p, expected, actual;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2);
			
			p = new Point1D(this.segment, x1);
			expected = new Point1D(this.segment, x2);
			actual = this.testingBounds.farestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());

			p = new Point1D(this.otherSegment, x1);
			actual = this.testingBounds.farestPoint(p);
			assertNull(actual);
			
			p = new Point1D(this.segment, x2);
			expected = new Point1D(this.segment, x1);
			actual = this.testingBounds.farestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());

			p = new Point1D(this.otherSegment, x2);
			actual = this.testingBounds.farestPoint(p);
			assertNull(actual);

			p = new Point1D(this.segment, (x1+x2)/2.f);
			expected = new Point1D(this.segment, x2);
			actual = this.testingBounds.farestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());

			p = new Point1D(this.otherSegment, (x1+x2)/2.f);
			actual = this.testingBounds.farestPoint(p);
			assertNull(actual);

			p = new Point1D(this.segment, x1 - 10);
			expected = new Point1D(this.segment, x2);
			actual = this.testingBounds.farestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());

			p = new Point1D(this.otherSegment, x1 - 10);
			actual = this.testingBounds.farestPoint(p);
			assertNull(actual);

			p = new Point1D(this.segment, x2 + 20);
			expected = new Point1D(this.segment, x1);
			actual = this.testingBounds.farestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());

			p = new Point1D(this.otherSegment, x2 + 20);
			actual = this.testingBounds.farestPoint(p);
			assertNull(actual);
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x1", x1, //$NON-NLS-1$
					"x2", x2); //$NON-NLS-1$
		}
	}

	@Override
	public void testNearestPointP() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		
		Point1D p, expected, actual;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2);
			
			p = new Point1D(this.segment, x1);
			expected = new Point1D(this.segment, x1);
			actual = this.testingBounds.nearestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());

			p = new Point1D(this.otherSegment, x1);
			actual = this.testingBounds.nearestPoint(p);
			assertNull(actual);
			
			p = new Point1D(this.segment, x2);
			expected = new Point1D(this.segment, x2);
			actual = this.testingBounds.nearestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());

			p = new Point1D(this.otherSegment, x2);
			actual = this.testingBounds.nearestPoint(p);
			assertNull(actual);

			p = new Point1D(this.segment, (x1+x2)/2.f);
			expected = new Point1D(this.segment, (x1+x2)/2.f);
			actual = this.testingBounds.nearestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());

			p = new Point1D(this.otherSegment, (x1+x2)/2.f);
			actual = this.testingBounds.nearestPoint(p);
			assertNull(actual);

			p = new Point1D(this.segment, x1 - 10);
			expected = new Point1D(this.segment, x1);
			actual = this.testingBounds.nearestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());

			p = new Point1D(this.otherSegment, x1 - 10);
			actual = this.testingBounds.nearestPoint(p);
			assertNull(actual);

			p = new Point1D(this.segment, x2 + 20);
			expected = new Point1D(this.segment, x2);
			actual = this.testingBounds.nearestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());

			p = new Point1D(this.otherSegment, x2 + 20);
			actual = this.testingBounds.nearestPoint(p);
			assertNull(actual);
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x1", x1, //$NON-NLS-1$
					"x2", x2); //$NON-NLS-1$
		}
	}

}
