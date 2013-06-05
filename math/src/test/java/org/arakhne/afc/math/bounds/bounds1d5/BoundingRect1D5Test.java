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
package org.arakhne.afc.math.bounds.bounds1d5;

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
import org.arakhne.afc.math.object.Point1D5;
import org.arakhne.afc.math.system.CoordinateSystem2D;
import org.arakhne.afc.math.system.CoordinateSystem3D;

/**
 * Unit test for BoundingRect1D5 class.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BoundingRect1D5Test extends AbstractCombinableBound1D5Test<GraphSegmentStub> {

	/** Reference instance.
	 */
	private BoundingRect1D5<GraphSegmentStub> testingBounds;
	
	/** Reference instance.
	 */
	private Tuple2f[] testingPoints;

	/** Reference lower point.
	 */
	private Tuple2f lowerPoint;

	/** Reference upper point.
	 */
	private Tuple2f upperPoint;

	/** Reference center point.
	 */
	private Tuple2f centerPoint;
	
	/** Jutting factor
	 */
	private float expectedJutting;

	/** Lateral size
	 */
	private float expectedSize;
	
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

		this.testingPoints = randomTuples2D();
		this.lowerPoint = new Point2f();
		this.upperPoint = new Point2f();
		this.centerPoint = new Point2f();
		computeLowerUpperCenter(this.testingPoints,this.lowerPoint,this.upperPoint,this.centerPoint);
		
		if (this.centerPoint.getX()<0) {
			this.lowerPoint.setX(this.lowerPoint.getX() - this.centerPoint.getX());
			this.upperPoint.setX(this.lowerPoint.getX() - this.centerPoint.getX());
			this.centerPoint.setX(0.f);
		}
		else if (this.centerPoint.getX()>this.segment.getLength()) {
			float delta = this.centerPoint.getX() - this.segment.getLength();
			this.lowerPoint.setX(this.lowerPoint.getX() - delta);
			this.upperPoint.setX(this.lowerPoint.getX() - delta);
			this.centerPoint.setX(this.segment.getLength());
		}
		
		this.expectedJutting = this.centerPoint.getY();
		this.expectedSize = this.upperPoint.getY() - this.lowerPoint.getY();
		this.testingBounds = createBounds(this.testingPoints);
		
		//
		assertNumber(this.lowerPoint.getX());
		assertNumber(this.lowerPoint.getY());
		assertNumber(this.upperPoint.getX());
		assertNumber(this.upperPoint.getY());
		assertNumber(this.centerPoint.getX());
		assertNumber(this.centerPoint.getY());
		assertNumber(this.expectedJutting);
		assertNumber(this.expectedSize);
		
		//
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getMinX());
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getMaxX());
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
		this.lowerPoint = null;
		this.upperPoint = null;
		this.centerPoint = null;
		this.entryPoint = null;
		this.exitPoint = null;
		this.segment = null;
		super.tearDown();
	}

	/**
	 * @param points
	 * @return bounds
	 */
	private BoundingRect1D5<GraphSegmentStub> createBounds(Tuple2f[] points) {
		return new BoundingRect1D5<GraphSegmentStub>(this.segment, points);
	}

	/** Compute the lower, upper and center points.
	 * @param segment
	 * @param points
	 * @param lower
	 * @param upper
	 * @param center
	 */
	private static void computeLowerUpperCenter(Tuple2f[] points, Tuple2f lower, Tuple2f upper, Tuple2f center) {
		float lx = Float.NaN;
		float ly = Float.NaN;
		float ux = Float.NaN;
		float uy = Float.NaN;
		
		for(Tuple2f tuple : points) {
			if (Float.isNaN(lx) || tuple.getX()<lx) lx = tuple.getX();
			if (Float.isNaN(ly) || tuple.getY()<ly) ly = tuple.getY();
			if (Float.isNaN(ux) || tuple.getX()>ux) ux = tuple.getX();
			if (Float.isNaN(uy) || tuple.getY()>uy) uy = tuple.getY();
		}
		
		if (lower!=null) lower.set(lx,ly);
		if (upper!=null) upper.set(ux,uy);
		if (center!=null) center.set((lx+ux)/2.f, (ly+uy)/2.f);
	}
	
	private void assertBoundsOnSegment(float lower, float upper, float jutting, float lateralSize) {
		float l = lower;
		float u=  upper;
		float c = (lower+upper)/2.f;
		if (c<0) {
			l -= c;
			u -= c;
		}
		float length = this.testingBounds.getSegment().getLength();
		if (c>length) {
			l -= (c-length);
			u -= (c-length);
		}
		assertEpsilonEquals("lower", l, this.testingBounds.getMinX()); //$NON-NLS-1$
		assertEpsilonEquals("upper", u, this.testingBounds.getMaxX()); //$NON-NLS-1$
		assertEpsilonEquals("jutting", jutting, this.testingBounds.getJutting()); //$NON-NLS-1$
		assertEpsilonEquals("lateral size", lateralSize, this.testingBounds.getLateralSize()); //$NON-NLS-1$
	}

	@Override
	public void testCombineFloatArray() {
		Tuple2f[] nPoints = randomPoints2D();
		Point2f l = new Point2f();
		Point2f u = new Point2f();
		Point2f c = new Point2f();
		computeLowerUpperCenter(nPoints, l, u, c);

		float min = Math.min(l.getX(), this.lowerPoint.getX());
		float max = Math.max(u.getX(), this.upperPoint.getX());
		
		float[] coords = new float[nPoints.length];
		for(int i=0; i<nPoints.length; ++i) {
			coords[i] = nPoints[i].getX();
		}
		
		this.testingBounds.combine(coords);
				
		assertBoundsOnSegment(
				min, max, 
				this.expectedJutting, 
				this.expectedSize);
	}

	@Override
	public void testGetCenterX() {
		float p = this.testingBounds.getCenterX();
		assertNumber(p);
		assertEpsilonEquals(this.centerPoint.getX(), p);
	}

	@Override
	public void testGetMaxX() {
		float p = this.testingBounds.getMaxX();
		assertNumber(p);
		assertEpsilonEquals(this.upperPoint.getX(), p);
	}

	@Override
	public void testGetMinX() {
		float p = this.testingBounds.getMinX();
		assertNumber(p);
		assertEpsilonEquals(this.lowerPoint.getX(), p);
	}

	@Override
	public void testGetCenterY() {
		float p = this.testingBounds.getCenterY();
		assertNumber(p);
		assertEpsilonEquals(this.expectedJutting, p);
	}

	@Override
	public void testGetJutting() {
		float p = this.testingBounds.getJutting();
		assertNumber(p);
		assertEpsilonEquals(this.expectedJutting, p);
	}

	@Override
	public void testGetMaxY() {
		float p = this.testingBounds.getMaxY();
		assertNumber(p);
		assertEpsilonEquals(this.upperPoint.getY(), p);
	}

	@Override
	public void testGetMinY() {
		float p = this.testingBounds.getMinY();
		assertNumber(p);
		assertEpsilonEquals(this.lowerPoint.getY(), p);
	}

	@Override
	public void testGetSegment() {
		assertSame(this.segment, this.testingBounds.getSegment());
		assertNotSame(this.otherSegment, this.testingBounds.getSegment());
	}

	@Override
	public void testGetSizeX() {
		float p = this.testingBounds.getSizeX();
		assertNumber(p);
		assertEpsilonEquals(this.upperPoint.getX()-this.lowerPoint.getX(), p);
	}

	@Override
	public void testGetSizeY() {
		float p = this.testingBounds.getSizeY();
		assertNumber(p);
		assertEpsilonEquals(this.upperPoint.getY()-this.lowerPoint.getY(), p);
	}

	@Override
	public void testGetLateralSize() {
		float p = this.testingBounds.getLateralSize();
		assertNumber(p);
		assertEpsilonEquals(this.upperPoint.getY()-this.lowerPoint.getY(), p);
	}

	@Override
	public void testSetBoxBounds1D5() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		float j = this.RANDOM.nextFloat() * 100.f;
		float l = this.RANDOM.nextFloat() * 200.f;
		float a, b;
		
		assertTrue(x1<=x2);
		
		a = 0.f;
		b = 0.f;
		
		BoundingRect1D5<GraphSegmentStub> box = new BoundingRect1D5<GraphSegmentStub>(this.segment);

		try {
			box.setBox(x1, x2, j, l);
			this.testingBounds.setBox(box);
			
			assertEpsilonEquals(x1, this.testingBounds.getMinX());
			assertEpsilonEquals(x2, this.testingBounds.getMaxX());
			assertEpsilonEquals(j, this.testingBounds.getJutting());
			assertEpsilonEquals(l, this.testingBounds.getLateralSize());
	
			a = -3.f*x2;
			b = x2;
			box.setBox(a, b, j, l);
			this.testingBounds.setBox(box);
	
			assertEpsilonEquals(a, this.testingBounds.getMinX());
			assertEpsilonEquals(b, this.testingBounds.getMaxX());
			assertEpsilonEquals(j, this.testingBounds.getJutting());
			assertEpsilonEquals(l, this.testingBounds.getLateralSize());
	
			a = x1;
			b = 2.f*this.segment.getLength()+x1;
			box.setBox(a, b, j, l);
			this.testingBounds.setBox(box);
	
			assertEpsilonEquals(a, this.testingBounds.getMinX());
			assertEpsilonEquals(b, this.testingBounds.getMaxX());
			assertEpsilonEquals(j, this.testingBounds.getJutting());
			assertEpsilonEquals(l, this.testingBounds.getLateralSize());
	
			a = -x1;
			b = 18.f*this.segment.getLength();
			box.setBox(a, b, j, l);
			this.testingBounds.setBox(box);
	
			assertEpsilonEquals(a, this.testingBounds.getMinX());
			assertEpsilonEquals(b, this.testingBounds.getMaxX());
			assertEpsilonEquals(j, this.testingBounds.getJutting());
			assertEpsilonEquals(l, this.testingBounds.getLateralSize());
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x1", x1, //$NON-NLS-1$
					"x2", x2, //$NON-NLS-1$
					"a", a, //$NON-NLS-1$
					"b", b, //$NON-NLS-1$
					"j", j, //$NON-NLS-1$
					"l", l); //$NON-NLS-1$
		}		
	}

	@Override
	public void testSetBoxFloatFloatFloatFloat() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		float j = this.RANDOM.nextFloat() * 100.f;
		float l = this.RANDOM.nextFloat() * 200.f;
		float a, b;
		
		assertTrue(x1<=x2);
		
		a = 0.f;
		b = 0.f;
		
		try {
			this.testingBounds.setBox(x1, x2, j, l);
			
			assertEpsilonEquals(x1, this.testingBounds.getMinX());
			assertEpsilonEquals(x2, this.testingBounds.getMaxX());
			assertEpsilonEquals(j, this.testingBounds.getJutting());
			assertEpsilonEquals(l, this.testingBounds.getLateralSize());
	
			a = -3.f*x2;
			b = x2;
			this.testingBounds.setBox(a, b, j, l);
	
			assertEpsilonEquals(a, this.testingBounds.getMinX());
			assertEpsilonEquals(b, this.testingBounds.getMaxX());
			assertEpsilonEquals(j, this.testingBounds.getJutting());
			assertEpsilonEquals(l, this.testingBounds.getLateralSize());
	
			a = x1;
			b = 2.f*this.segment.getLength()+x1;
			this.testingBounds.setBox(a, b, j, l);
	
			assertEpsilonEquals(a, this.testingBounds.getMinX());
			assertEpsilonEquals(b, this.testingBounds.getMaxX());
			assertEpsilonEquals(j, this.testingBounds.getJutting());
			assertEpsilonEquals(l, this.testingBounds.getLateralSize());
	
			a = -x1;
			b = 18.f*this.segment.getLength();
			this.testingBounds.setBox(a, b, j, l);
	
			assertEpsilonEquals(a, this.testingBounds.getMinX());
			assertEpsilonEquals(b, this.testingBounds.getMaxX());
			assertEpsilonEquals(j, this.testingBounds.getJutting());
			assertEpsilonEquals(l, this.testingBounds.getLateralSize());
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x1", x1, //$NON-NLS-1$
					"x2", x2, //$NON-NLS-1$
					"a", a, //$NON-NLS-1$
					"b", b, //$NON-NLS-1$
					"j", j, //$NON-NLS-1$
					"l", l); //$NON-NLS-1$
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
			BoundingRect1D5<GraphSegmentStub> newInterval;
			
			Tuple2f lower = new Point2f(this.lowerPoint);
			Tuple2f upper = new Point2f(this.upperPoint);
			
			newInterval = new BoundingRect1D5<GraphSegmentStub>(this.segment, this.lowerPoint, this.upperPoint);
			this.testingBounds.combine(newInterval);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(
					lower.getX(), upper.getX(), 
					(lower.getY()+upper.getY())/2.f,
					upper.getY()-lower.getY());

			newInterval = new BoundingRect1D5<GraphSegmentStub>(this.segment, 
					new Point2f(this.lowerPoint.getX()-10, this.lowerPoint.getY()), 
					this.upperPoint);
			this.testingBounds.combine(newInterval);
			assertSame(this.segment, this.testingBounds.getSegment());
			lower.setX(lower.getX() - 10);
			assertBoundsOnSegment(
					lower.getX(), upper.getX(), 
					(lower.getY()+upper.getY())/2.f,
					upper.getY()-lower.getY());
		
			float delta = Math.min(this.segment.getLength()+(this.upperPoint.getX()-this.lowerPoint.getX())/2.f, this.upperPoint.getX()+100) - this.upperPoint.getX();
			newInterval = new BoundingRect1D5<GraphSegmentStub>(this.segment, 
					this.lowerPoint, 
					new Point2f(this.lowerPoint.getX()+delta, this.upperPoint.getY()));
			this.testingBounds.combine(newInterval);
			assertSame(this.segment, this.testingBounds.getSegment());
			upper.setX(upper.getX() + delta);
			assertBoundsOnSegment(
					lower.getX(), upper.getX(), 
					(lower.getY()+upper.getY())/2.f,
					upper.getY()-lower.getY());

			newInterval = new BoundingRect1D5<GraphSegmentStub>(this.segment, this.lowerPoint, this.upperPoint);
			this.testingBounds.combine(newInterval);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(
					lower.getX(), upper.getX(), 
					(lower.getY()+upper.getY())/2.f,
					upper.getY()-lower.getY());

			newInterval = new BoundingRect1D5<GraphSegmentStub>(this.segment, 
					new Point2f(this.lowerPoint.getX(), this.lowerPoint.getY()+this.expectedSize), 
					this.upperPoint);
			this.testingBounds.combine(newInterval);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(
					lower.getX(), upper.getX(), 
					(lower.getY()+upper.getY())/2.f,
					upper.getY()-lower.getY());

			newInterval = new BoundingRect1D5<GraphSegmentStub>(this.segment, 
					this.lowerPoint, 
					new Point2f(this.upperPoint.getX(), this.upperPoint.getY()+100));
			this.testingBounds.combine(newInterval);
			assertSame(this.segment, this.testingBounds.getSegment());
			upper.setY(upper.getY() + 100);
			assertBoundsOnSegment(
					lower.getX(), upper.getX(), 
					(lower.getY()+upper.getY())/2.f,
					upper.getY()-lower.getY());
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength()); //$NON-NLS-1$
		}
	}

	@Override
	public void testCombineCollection() {
		try {
			List<BoundingRect1D5<GraphSegmentStub>> newIntervals =
				new ArrayList<BoundingRect1D5<GraphSegmentStub>>();
			
			Tuple2f lower = new Point2f(this.lowerPoint);
			Tuple2f upper = new Point2f(this.upperPoint);

			float delta = Math.min(this.segment.getLength()+(this.upperPoint.getX()-this.lowerPoint.getX())/2.f, this.upperPoint.getX()+100) - this.upperPoint.getX();
			
			newIntervals.add(new BoundingRect1D5<GraphSegmentStub>(this.segment, this.lowerPoint, this.upperPoint));
			newIntervals.add(new BoundingRect1D5<GraphSegmentStub>(this.segment, 
					new Point2f(this.lowerPoint.getX()-10, this.lowerPoint.getY()), 
					this.upperPoint));
			newIntervals.add(new BoundingRect1D5<GraphSegmentStub>(this.segment, 
					this.lowerPoint, 
					new Point2f(this.lowerPoint.getX()+delta, this.upperPoint.getY())));
			newIntervals.add(new BoundingRect1D5<GraphSegmentStub>(this.segment, this.lowerPoint, this.upperPoint));
			newIntervals.add(new BoundingRect1D5<GraphSegmentStub>(this.segment, 
					new Point2f(this.lowerPoint.getX(), this.lowerPoint.getY()+this.expectedSize), 
					this.upperPoint));
			newIntervals.add(new BoundingRect1D5<GraphSegmentStub>(this.segment, 
					this.lowerPoint, 
					new Point2f(this.upperPoint.getX(), this.upperPoint.getY()+100)));

			lower.setX(lower.getX() - 10);
			upper.setX(upper.getX() + delta);
			upper.setY(upper.getY() + 100);
			
			this.testingBounds.combine(newIntervals);
			
			assertBoundsOnSegment(
					lower.getX(), upper.getX(), 
					(lower.getY()+upper.getY())/2.f,
					upper.getY()-lower.getY());
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength()); //$NON-NLS-1$
		}
	}

	@Override
	public void testCombineP() {
		try {
			Point1D5 newPt;
			
			float lower = this.lowerPoint.getX();
			float upper = this.upperPoint.getX();
			
			newPt = new Point1D5(this.segment, this.lowerPoint);
			this.testingBounds.combine(newPt);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(
					lower,
					upper,
					this.expectedJutting,
					this.expectedSize);

			newPt = new Point1D5(this.segment, this.lowerPoint.getX()-10, this.lowerPoint.getY());
			this.testingBounds.combine(newPt);
			assertSame(this.segment, this.testingBounds.getSegment());
			lower -= 10;
			assertBoundsOnSegment(
					lower,
					upper,
					this.expectedJutting,
					this.expectedSize);
		
			newPt = new Point1D5(this.segment, this.upperPoint.getX()+100, this.lowerPoint.getY());
			this.testingBounds.combine(newPt);
			assertSame(this.segment, this.testingBounds.getSegment());
			upper += 100;
			assertBoundsOnSegment(
					lower,
					upper,
					this.expectedJutting,
					this.expectedSize);

			newPt = new Point1D5(this.segment, this.lowerPoint);
			this.testingBounds.combine(newPt);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(
					lower,
					upper,
					this.expectedJutting,
					this.expectedSize);

			newPt = new Point1D5(this.segment, this.lowerPoint.getX(), this.lowerPoint.getY()+this.expectedSize);
			this.testingBounds.combine(newPt);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(
					lower,
					upper,
					this.expectedJutting,
					this.expectedSize);

			newPt = new Point1D5(this.segment, this.lowerPoint.getX(), this.upperPoint.getY()+100);
			this.testingBounds.combine(newPt);
			assertSame(this.segment, this.testingBounds.getSegment());
			assertBoundsOnSegment(
					lower,
					upper,
					this.expectedJutting+50,
					this.expectedSize+100);
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength()); //$NON-NLS-1$
		}
	}

	@Override
	public void testIsInit() {
		BoundingRect1D5<GraphSegmentStub> b1 = new BoundingRect1D5<GraphSegmentStub>(this.segment);
		BoundingRect1D5<GraphSegmentStub> b2 = new BoundingRect1D5<GraphSegmentStub>(this.segment);
		b2.setBox(0, 0, 0, 0);
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
		float j = this.RANDOM.nextFloat() * 100.f;
		float l = this.RANDOM.nextFloat() * 200.f;
		float a, b, c;
		
		assertTrue(x1<=x2);
		
		a = 0.f;
		b = 0.f;
		c = 0.f;
		
		try {
			this.testingBounds.setSegment(this.otherSegment);
			assertSame(this.otherSegment, this.testingBounds.getSegment());
			
			BoundingRect1D5<GraphSegmentStub> box = new BoundingRect1D5<GraphSegmentStub>(this.segment);
			
			box.setBox(x1, x2, j, l);
			this.testingBounds.set(box);
			
			assertSame(this.segment, this.testingBounds.getSegment());
			assertEpsilonEquals(x1, this.testingBounds.getMinX());
			assertEpsilonEquals(x2, this.testingBounds.getMaxX());
			assertEpsilonEquals(j, this.testingBounds.getJutting());
			assertEpsilonEquals(l, this.testingBounds.getLateralSize());
	
			this.testingBounds.setSegment(this.otherSegment);
			assertSame(this.otherSegment, this.testingBounds.getSegment());
	
			a = -3.f*x2;
			b = x2;
			c = (a+b)/2.f;
			box.setBox(a, b, j, l);
			this.testingBounds.set(box);
	
			assertSame(this.segment, this.testingBounds.getSegment());
			assertEpsilonEquals(a-c, this.testingBounds.getMinX());
			assertEpsilonEquals(b-c, this.testingBounds.getMaxX());
			assertEpsilonEquals(j, this.testingBounds.getJutting());
			assertEpsilonEquals(l, this.testingBounds.getLateralSize());
	
			this.testingBounds.setSegment(this.otherSegment);
			assertSame(this.otherSegment, this.testingBounds.getSegment());
	
			a = x1;
			b = 2.f*this.segment.getLength()+x1;
			c = (a+b)/2.f;
			box.setBox(a, b, j, l);
			this.testingBounds.set(box);
	
			assertSame(this.segment, this.testingBounds.getSegment());
			assertEpsilonEquals(a-(c-this.segment.getLength()), this.testingBounds.getMinX());
			assertEpsilonEquals(b-(c-this.segment.getLength()), this.testingBounds.getMaxX());
			assertEpsilonEquals(j, this.testingBounds.getJutting());
			assertEpsilonEquals(l, this.testingBounds.getLateralSize());
	
			this.testingBounds.setSegment(this.otherSegment);
			assertSame(this.otherSegment, this.testingBounds.getSegment());
	
			a = -x1;
			b = 18.f*this.segment.getLength();
			c = (a+b)/2.f;
			box.setBox(a, b, j, l);
			this.testingBounds.set(box);
	
			assertSame(this.segment, this.testingBounds.getSegment());
			assertEpsilonEquals(a-(c-this.segment.getLength()), this.testingBounds.getMinX());
			assertEpsilonEquals(b-(c-this.segment.getLength()), this.testingBounds.getMaxX());
			assertEpsilonEquals(j, this.testingBounds.getJutting());
			assertEpsilonEquals(l, this.testingBounds.getLateralSize());
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x1", x1, //$NON-NLS-1$
					"x2", x2, //$NON-NLS-1$
					"a", a, //$NON-NLS-1$
					"b", b, //$NON-NLS-1$
					"c", c, //$NON-NLS-1$
					"j", j, //$NON-NLS-1$
					"l", l); //$NON-NLS-1$
		}
	}

	@Override
	public void testSetP() {
		float x = this.RANDOM.nextFloat() * this.segment.getLength();
		float j = 0;
		
		try {
			Point1D5 point = new Point1D5(this.segment, x, 0);
			this.testingBounds.set(point);
			
			assertSame(this.segment, this.testingBounds.getSegment());
			assertEpsilonEquals(x, this.testingBounds.getMinX());
			assertEpsilonEquals(x, this.testingBounds.getMaxX());
	
			point = new Point1D5(this.otherSegment, x, 0);
			this.testingBounds.set(point);

			assertSame(this.otherSegment, this.testingBounds.getSegment());
			assertEpsilonEquals(x, this.testingBounds.getMinX());
			assertEpsilonEquals(x, this.testingBounds.getMaxX());
			assertEpsilonEquals(j, this.testingBounds.getJutting());
			assertEpsilonEquals(0, this.testingBounds.getLateralSize());
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength(), //$NON-NLS-1$
					"x", x, //$NON-NLS-1$
					"j", j); //$NON-NLS-1$
		}
	}

	@Override
	public void testClone() {
		BoundingRect1D5<GraphSegmentStub> clone = this.testingBounds.clone();
		assertNotSame(this.testingBounds, clone);
		assertSame(this.testingBounds.getSegment(), clone.getSegment());
		assertEquals(this.testingBounds.getMinX(), clone.getMinX());
		assertEquals(this.testingBounds.getMaxX(), clone.getMaxX());
		assertEquals(this.testingBounds.getJutting(), clone.getJutting());
		assertEquals(this.testingBounds.getLateralSize(), clone.getLateralSize());
	}

	@Override
	public void testDistanceMaxP() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		
		Point1D5 p;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2,0,0);
			
			p = new Point1D5(this.segment, x1, 0);
			assertEpsilonEquals(x2-x1, this.testingBounds.distanceMax(p));

			p = new Point1D5(this.otherSegment, x1,0);
			assertNaN(this.testingBounds.distanceMax(p));
			
			p = new Point1D5(this.segment, x2, 0);
			assertEpsilonEquals(x2-x1, this.testingBounds.distanceMax(p));

			p = new Point1D5(this.otherSegment, x2, 0);
			assertNaN(this.testingBounds.distanceMax(p));

			p = new Point1D5(this.segment, (x1+x2)/2.f, 0);
			assertEpsilonEquals((x2-x1)/2.f, this.testingBounds.distanceMax(p));

			p = new Point1D5(this.otherSegment, (x1+x2)/2.f, 0);
			assertNaN(this.testingBounds.distanceMax(p));

			p = new Point1D5(this.segment, x1 - 10, 0);
			assertEpsilonEquals(x2-x1+10.f, this.testingBounds.distanceMax(p));

			p = new Point1D5(this.otherSegment, x1 - 10, 0);
			assertNaN(this.testingBounds.distanceMax(p));

			p = new Point1D5(this.segment, x2 + 20, 0);
			assertEpsilonEquals(x2-x1+20.f, this.testingBounds.distanceMax(p));

			p = new Point1D5(this.otherSegment, x2 + 20, 0);
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
		
		Point1D5 p;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2,0,0);
			
			p = new Point1D5(this.segment, x1, 0);
			assertEpsilonEquals((float) Math.pow(x2-x1,2.f), this.testingBounds.distanceMaxSquared(p));

			p = new Point1D5(this.otherSegment, x1,0);
			assertNaN(this.testingBounds.distanceMaxSquared(p));
			
			p = new Point1D5(this.segment, x2, 0);
			assertEpsilonEquals((float) Math.pow(x2-x1,2.), this.testingBounds.distanceMaxSquared(p));

			p = new Point1D5(this.otherSegment, x2, 0);
			assertNaN(this.testingBounds.distanceMaxSquared(p));

			p = new Point1D5(this.segment, (x1+x2)/2.f, 0);
			assertEpsilonEquals((float) Math.pow((x2-x1)/2.,2.), this.testingBounds.distanceMaxSquared(p));

			p = new Point1D5(this.otherSegment, (x1+x2)/2.f, 0);
			assertNaN(this.testingBounds.distanceMaxSquared(p));

			p = new Point1D5(this.segment, x1 - 10, 0);
			assertEpsilonEquals((float) Math.pow(x2-x1+10.f,2.), this.testingBounds.distanceMaxSquared(p));

			p = new Point1D5(this.otherSegment, x1 - 10, 0);
			assertNaN(this.testingBounds.distanceMaxSquared(p));

			p = new Point1D5(this.segment, x2 + 20, 0);
			assertEpsilonEquals((float) Math.pow(x2-x1+20.f,2.), this.testingBounds.distanceMaxSquared(p));

			p = new Point1D5(this.otherSegment, x2 + 20, 0);
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
	public void testDistanceMaxPoint1D() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		
		Point1D p;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2,0,0);
			
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
	public void testDistanceMaxSquaredPoint1D() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		
		Point1D p;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2,0,0);
			
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
			assertEpsilonEquals((float) Math.pow(x2-x1+10.f,2.), this.testingBounds.distanceMaxSquared(p));

			p = new Point1D(this.otherSegment, x1 - 10);
			assertNaN(this.testingBounds.distanceMaxSquared(p));

			p = new Point1D(this.segment, x2 + 20);
			assertEpsilonEquals((float) Math.pow(x2-x1+20.f,2.), this.testingBounds.distanceMaxSquared(p));

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
		
		Point1D5 p;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2,0,0);
			
			p = new Point1D5(this.segment, x1, 0);
			assertEpsilonEquals(0.f, this.testingBounds.distance(p));

			p = new Point1D5(this.otherSegment, x1, 0);
			assertNaN(this.testingBounds.distance(p));
			
			p = new Point1D5(this.segment, x2, 0);
			assertEpsilonEquals(0.f, this.testingBounds.distance(p));

			p = new Point1D5(this.otherSegment, x2, 0);
			assertNaN(this.testingBounds.distance(p));

			p = new Point1D5(this.segment, (x1+x2)/2.f, 0);
			assertEpsilonEquals(0.f, this.testingBounds.distance(p));

			p = new Point1D5(this.otherSegment, (x1+x2)/2.f, 0);
			assertNaN(this.testingBounds.distance(p));

			p = new Point1D5(this.segment, x1 - 10, 0);
			assertEpsilonEquals(10.f, this.testingBounds.distance(p));

			p = new Point1D5(this.otherSegment, x1 - 10, 0);
			assertNaN(this.testingBounds.distance(p));

			p = new Point1D5(this.segment, x2 + 20, 0);
			assertEpsilonEquals(20.f, this.testingBounds.distance(p));

			p = new Point1D5(this.otherSegment, x2 + 20, 0);
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
		
		Point1D5 p;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2,0,0);
			
			p = new Point1D5(this.segment, x1, 0);
			assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(p));

			p = new Point1D5(this.otherSegment, x1, 0);
			assertNaN(this.testingBounds.distanceSquared(p));
			
			p = new Point1D5(this.segment, x2, 0);
			assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(p));

			p = new Point1D5(this.otherSegment, x2, 0);
			assertNaN(this.testingBounds.distanceSquared(p));

			p = new Point1D5(this.segment, (x1+x2)/2.f, 0);
			assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(p));

			p = new Point1D5(this.otherSegment, (x1+x2)/2.f, 0);
			assertNaN(this.testingBounds.distanceSquared(p));

			p = new Point1D5(this.segment, x1 - 10, 0);
			assertEpsilonEquals(10.f*10.f, this.testingBounds.distanceSquared(p));

			p = new Point1D5(this.otherSegment, x1 - 10, 0);
			assertNaN(this.testingBounds.distanceSquared(p));

			p = new Point1D5(this.segment, x2 + 20, 0);
			assertEpsilonEquals(20.f*20.f, this.testingBounds.distanceSquared(p));

			p = new Point1D5(this.otherSegment, x2 + 20, 0);
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
	public void testDistancePoint1D() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		
		Point1D p;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2,0,0);
			
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
	public void testDistanceSquaredPoint1D() {
		float x1 = this.RANDOM.nextFloat() * this.segment.getLength();
		float x2 = this.RANDOM.nextFloat() * (this.segment.getLength()-x1) + x1;
		
		Point1D p;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2,0,0);
			
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
		Point1D5 p = this.testingBounds.getCenter();
		assertNumber(p.getCurvilineCoordinate());
		assertSame(this.segment, p.getSegment());
		assertEpsilonEquals(this.centerPoint.getX(), p.getCurvilineCoordinate());
		assertEpsilonEquals(this.centerPoint.getY(), p.getJuttingDistance());
	}

	@Override
	public void testGetLower() {
		Point1D5 p = this.testingBounds.getLower();
		assertNumber(p.getCurvilineCoordinate());
		assertSame(this.segment, p.getSegment());
		assertEpsilonEquals(this.lowerPoint.getX(), p.getCurvilineCoordinate());
		assertEpsilonEquals(this.lowerPoint.getY(), p.getJuttingDistance());
	}

	@Override
	public void testGetLowerUpper() {
		Point1D5 l = new Point1D5(this.segment);
		Point1D5 u = new Point1D5(this.segment);
		this.testingBounds.getLowerUpper(l, u);
		
		assertNumber(l.getCurvilineCoordinate());
		assertSame(this.segment, l.getSegment());
		assertEpsilonEquals(this.lowerPoint.getX(), l.getCurvilineCoordinate());
		assertEpsilonEquals(this.lowerPoint.getY(), l.getJuttingDistance());

		assertNumber(u.getCurvilineCoordinate());
		assertSame(this.segment, u.getSegment());
		assertEpsilonEquals(this.upperPoint.getX(), u.getCurvilineCoordinate());
		assertEpsilonEquals(this.upperPoint.getY(), u.getJuttingDistance());
	}

	@Override
	public void testGetPosition() {
		Point1D5 p = this.testingBounds.getPosition();
		assertNumber(p.getCurvilineCoordinate());
		assertSame(this.segment, p.getSegment());
		assertEpsilonEquals(this.centerPoint.getX(), p.getCurvilineCoordinate());
		assertEpsilonEquals(this.centerPoint.getY(), p.getJuttingDistance());
	}

	@Override
	public void testGetMathematicalDimension() {
		assertEquals(PseudoHamelDimension.DIMENSION_1D5, this.testingBounds.getMathematicalDimension());
	}

	@Override
	public void testGetUpper() {
		Point1D5 p = this.testingBounds.getUpper();
		assertNumber(p.getCurvilineCoordinate());
		assertSame(this.segment, p.getSegment());
		assertEpsilonEquals(this.upperPoint.getX(), p.getCurvilineCoordinate());
		assertEpsilonEquals(this.upperPoint.getY(), p.getJuttingDistance());
	}

	@Override
	public void testIsEmpty() {
		BoundingRect1D5<GraphSegmentStub> b1 = new BoundingRect1D5<GraphSegmentStub>(this.segment);
		BoundingRect1D5<GraphSegmentStub> b2 = new BoundingRect1D5<GraphSegmentStub>(this.segment);
		b2.setBox(0, 0, 0, 0);
		assertTrue(b1.isEmpty());
		assertTrue(b2.isEmpty());
		assertFalse(this.testingBounds.isEmpty());
	}

	@Override
	public void testClamp() {
		this.testingBounds.clamp();
		float c = this.testingBounds.getCenterX();
		assertPositive(c);
		assertTrue(c+"<="+this.segment.getLength(), c <= this.segment.getLength()+MathUtil.getEpsilon()); //$NON-NLS-1$
		
		float d = c - this.testingBounds.getMinX();
		
		assertEpsilonEquals(d, this.testingBounds.getMaxX() - c);

		assertEpsilonEquals(this.expectedJutting, this.testingBounds.getJutting());
		assertEpsilonEquals(this.expectedSize, this.testingBounds.getLateralSize());
	}

	@Override
	public void testCombineTuple2dArray() {
		try {
			Tuple2f[] nPoints = randomPoints2D();
			Point2f l = new Point2f();
			Point2f u = new Point2f();
			Point2f c = new Point2f();
			computeLowerUpperCenter(nPoints, l, u, c);

			float minx = Math.min(l.getX(), this.lowerPoint.getX());
			float maxx = Math.max(u.getX(), this.upperPoint.getX());
			float miny = Math.min(l.getY(), this.lowerPoint.getY());
			float maxy = Math.max(u.getY(), this.upperPoint.getY());
			
			this.testingBounds.combine(nPoints);
					
			assertBoundsOnSegment(
					minx, maxx, 
					(miny+maxy)/2.f, 
					maxy-miny);
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength()); //$NON-NLS-1$
		}
	}

	@Override
	public void testSetTuple2dArray() {
		try {
			Point2f p1, p2;
			
			p1 = new Point2f(this.lowerPoint.getX(), this.expectedJutting);
			p2 = new Point2f(this.lowerPoint.getX()-10.f, this.expectedJutting);
			this.testingBounds.set(p1, p2);			
			assertBoundsOnSegment(
					this.lowerPoint.getX()-10.f, 
					this.lowerPoint.getX(),
					this.expectedJutting, 
					0.f);
	
			p1 = new Point2f(this.upperPoint.getX()+100.f, this.expectedJutting);
			p2 = new Point2f(this.lowerPoint.getX(), this.expectedJutting);
			this.testingBounds.set(p1, p2);			
			assertBoundsOnSegment(
					this.lowerPoint.getX(),
					this.upperPoint.getX()+100.f,
					this.expectedJutting, 
					0.f);

			p1 = new Point2f(this.upperPoint.getX()+100.f, this.expectedJutting+500.);
			p2 = new Point2f(this.lowerPoint.getX(), this.expectedJutting-12.);
			this.testingBounds.set(p1, p2);			
			assertBoundsOnSegment(
					this.lowerPoint.getX(),
					this.upperPoint.getX()+100.f,
					this.expectedJutting+(500.f-12.f)/2.f,
					500.f+12.f);
		}
		catch(Throwable e) {
			forwardException(e, 
					"sLength", this.segment.getLength()); //$NON-NLS-1$
		}
	}

	@Override
	public void testSetJuttingFloat() {
		float j = this.RANDOM.nextFloat() * 1000.f;
		this.testingBounds.setJutting(j);
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getMinX());
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getMaxX());
		assertEpsilonEquals(j, this.testingBounds.getJutting());
		assertEpsilonEquals(this.expectedSize, this.testingBounds.getLateralSize());
	}

	@Override
	public void testSetLateralSizeFloat() {
		float l = this.RANDOM.nextFloat() * 1000.f;
		this.testingBounds.setLateralSize(l);
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getMinX());
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getMaxX());
		assertEpsilonEquals(this.expectedJutting, this.testingBounds.getJutting());
		assertEpsilonEquals(l, this.testingBounds.getLateralSize());
	}

	@Override
	public void testToBounds1D() {
		BoundingInterval<GraphSegmentStub> inter = this.testingBounds.toBounds1D();
		
		assertNotNull(inter);
		assertSame(this.segment, inter.getSegment());
		assertEpsilonEquals(this.lowerPoint.getX(), inter.getMinX());
		assertEpsilonEquals(this.upperPoint.getX(), inter.getMaxX());
	}

	@Override
	public void testToBounds2D() {
		Bounds2D b = this.testingBounds.toBounds2D();
		
		assertNotNull(b);
		assertTrue(b instanceof OrientedBoundingRectangle);
		OrientedBoundingRectangle obr = (OrientedBoundingRectangle)b;
		Point2f pos = new Point2f();
		Vector2f tangent = new Vector2f();
		this.segment.projectsOnPlane(this.testingBounds.getCenterX(), this.testingBounds.getCenterY(), pos, tangent, CoordinateSystem2D.getDefaultCoordinateSystem());
		assertEpsilonEquals(pos, obr.getCenter());
		assertEpsilonEquals(tangent, obr.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(this.testingBounds.getSizeX()/2.f, obr.getRExtent());
		assertEpsilonEquals(this.testingBounds.getSizeY()/2.f, obr.getSExtent());
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
			this.segment.projectsOnPlane(this.testingBounds.getCenterX(), this.testingBounds.getCenterY(), pos, tangent, cs);
			assertEpsilonEquals(pos, obr.getCenter());
			assertEpsilonEquals(tangent, obr.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(this.testingBounds.getSizeX()/2.f, obr.getRExtent());
			assertEpsilonEquals(this.testingBounds.getSizeY()/2.f, obr.getSExtent());
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
		this.segment.projectsOnPlane(this.testingBounds.getCenterX(), this.testingBounds.getCenterY(), pos, tangent, cs.toCoordinateSystem2D());
		Point3f p = cs.fromCoordinateSystem2D(pos);
		Vector3f v = cs.fromCoordinateSystem2D(tangent);
		assertEpsilonEquals(p, obr.getCenter());
		assertEpsilonEquals(v, obr.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(this.testingBounds.getSizeX()/2.f, obr.getRExtent());
		assertEpsilonEquals(this.testingBounds.getSizeY()/2.f, obr.getSExtent());
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
			this.segment.projectsOnPlane(this.testingBounds.getCenterX(), this.testingBounds.getCenterY(), pos, tangent, cs.toCoordinateSystem2D());
			Point3f p = cs.fromCoordinateSystem2D(pos);
			Vector3f v = cs.fromCoordinateSystem2D(tangent);
			assertEpsilonEquals(cs.name(), p, obr.getCenter());
			assertEpsilonEquals(cs.name(), v, obr.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(cs.name(), this.testingBounds.getSizeX()/2.f, obr.getRExtent());
			assertEpsilonEquals(cs.name(), this.testingBounds.getSizeY()/2.f, obr.getSExtent());
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
			this.segment.projectsOnPlane(this.testingBounds.getCenterX(), this.testingBounds.getCenterY(), pos, tangent, cs.toCoordinateSystem2D());
			Point3f p = cs.fromCoordinateSystem2D(pos, posZ);
			Vector3f v = cs.fromCoordinateSystem2D(tangent, 0.f);
			assertEpsilonEquals(cs.name(), p, obr.getCenter());
			assertEpsilonEquals(cs.name(), v, obr.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(cs.name(), this.testingBounds.getSizeX()/2.f, obr.getRExtent());
			assertEpsilonEquals(cs.name(), this.testingBounds.getSizeY()/2.f, obr.getSExtent());
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
		
		Point1D5 p, expected, actual;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2,0,0);
			
			p = new Point1D5(this.segment, x1, 0);
			expected = new Point1D5(this.segment, x2, 0.f);
			actual = this.testingBounds.farestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());
			assertEpsilonEquals(expected.getJuttingDistance(), actual.getJuttingDistance());

			p = new Point1D5(this.otherSegment, x1, 0);
			actual = this.testingBounds.farestPoint(p);
			assertNull(actual);
			
			p = new Point1D5(this.segment, x2, 0);
			expected = new Point1D5(this.segment, x1, 0.f);
			actual = this.testingBounds.farestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());
			assertEpsilonEquals(expected.getJuttingDistance(), actual.getJuttingDistance());

			p = new Point1D5(this.otherSegment, x2, 0);
			actual = this.testingBounds.farestPoint(p);
			assertNull(actual);

			p = new Point1D5(this.segment, (x1+x2)/2.f, 0);
			expected = new Point1D5(this.segment, x1, 0.f);
			actual = this.testingBounds.farestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());
			assertEpsilonEquals(expected.getJuttingDistance(), actual.getJuttingDistance());

			p = new Point1D5(this.otherSegment, (x1+x2)/2.f, 0);
			actual = this.testingBounds.farestPoint(p);
			assertNull(actual);

			p = new Point1D5(this.segment, x1 - 10, 0);
			expected = new Point1D5(this.segment, x2, 0.f);
			actual = this.testingBounds.farestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());
			assertEpsilonEquals(expected.getJuttingDistance(), actual.getJuttingDistance());

			p = new Point1D5(this.otherSegment, x1 - 10, 0);
			actual = this.testingBounds.farestPoint(p);
			assertNull(actual);

			p = new Point1D5(this.segment, x2 + 20, 0);
			expected = new Point1D5(this.segment, x1, 0.f);
			actual = this.testingBounds.farestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());
			assertEpsilonEquals(expected.getJuttingDistance(), actual.getJuttingDistance());

			p = new Point1D5(this.otherSegment, x2 + 20, 0);
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
		
		Point1D5 p, expected, actual;

		assertTrue(x1<=x2);
		
		try {
			this.testingBounds.setSegment(this.segment);
			this.testingBounds.setBox(x1,x2,0,0);
			
			p = new Point1D5(this.segment, x1, 0);
			expected = new Point1D5(this.segment, x1, 0.f);
			actual = this.testingBounds.nearestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());
			assertEpsilonEquals(expected.getJuttingDistance(), actual.getJuttingDistance());

			p = new Point1D5(this.otherSegment, x1, 0);
			actual = this.testingBounds.nearestPoint(p);
			assertNull(actual);
			
			p = new Point1D5(this.segment, x2, 0);
			expected = new Point1D5(this.segment, x2, 0.f);
			actual = this.testingBounds.nearestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());
			assertEpsilonEquals(expected.getJuttingDistance(), actual.getJuttingDistance());

			p = new Point1D5(this.otherSegment, x2, 0);
			actual = this.testingBounds.nearestPoint(p);
			assertNull(actual);

			p = new Point1D5(this.segment, (x1+x2)/2.f, 0);
			expected = new Point1D5(this.segment, (x1+x2)/2.f, 0.f);
			actual = this.testingBounds.nearestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());
			assertEpsilonEquals(expected.getJuttingDistance(), actual.getJuttingDistance());

			p = new Point1D5(this.otherSegment, (x1+x2)/2.f, 0);
			actual = this.testingBounds.nearestPoint(p);
			assertNull(actual);

			p = new Point1D5(this.segment, x1 - 10, 0);
			expected = new Point1D5(this.segment, x1, 0.f);
			actual = this.testingBounds.nearestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());
			assertEpsilonEquals(expected.getJuttingDistance(), actual.getJuttingDistance());

			p = new Point1D5(this.otherSegment, x1 - 10, 0);
			actual = this.testingBounds.nearestPoint(p);
			assertNull(actual);

			p = new Point1D5(this.segment, x2 + 20, 0);
			expected = new Point1D5(this.segment, x2, 0.f);
			actual = this.testingBounds.nearestPoint(p);
			assertNotNull(actual);
			assertEpsilonEquals(expected.getCurvilineCoordinate(), actual.getCurvilineCoordinate());
			assertEpsilonEquals(expected.getJuttingDistance(), actual.getJuttingDistance());

			p = new Point1D5(this.otherSegment, x2 + 20, 0);
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
