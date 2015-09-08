/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2.discrete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.continuous.Transform2D;
import org.arakhne.afc.math.geometry.d2.discrete.Circle2i;
import org.arakhne.afc.math.geometry.d2.discrete.Path2i;
import org.arakhne.afc.math.geometry.d2.discrete.PathElement2i;
import org.arakhne.afc.math.geometry.d2.discrete.PathIterator2i;
import org.arakhne.afc.math.geometry.d2.discrete.Point2i;
import org.arakhne.afc.math.geometry.d2.discrete.Rectangle2i;
import org.arakhne.afc.math.geometry.d2.discrete.Segment2i;
import org.junit.Test;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Path2iTest extends AbstractShape2iTestCase<Path2i> {
	
	@Override
	protected Path2i createShape() {
		Path2i p = new Path2i();
		p.moveTo(0, 0);
		p.lineTo(2, 2);
		p.quadTo(3, 0, 4, 3);
		p.curveTo(5, -1, 6, 5, 7, -5);
		p.closePath();
		return p;
	}
	
	@Test
	@Override
	public void isEmpty() {
		assertFalse(this.r.isEmpty());

		this.r.clear();
		assertTrue(this.r.isEmpty());
		
		this.r.moveTo(1, 2);
		assertTrue(this.r.isEmpty());
		this.r.moveTo(3, 4);
		assertTrue(this.r.isEmpty());
		this.r.lineTo(5, 6);
		assertFalse(this.r.isEmpty());
		this.r.closePath();
		assertFalse(this.r.isEmpty());

		this.r.clear();
		assertTrue(this.r.isEmpty());

		this.r.moveTo(1, 2);
		assertTrue(this.r.isEmpty());
		this.r.moveTo(3, 4);
		assertTrue(this.r.isEmpty());
		this.r.lineTo(3, 4);
		assertTrue(this.r.isEmpty());
		this.r.closePath();
		assertTrue(this.r.isEmpty());

		this.r.clear();
		assertTrue(this.r.isEmpty());

		this.r.moveTo(1, 2);
		assertTrue(this.r.isEmpty());
		this.r.moveTo(3, 4);
		assertTrue(this.r.isEmpty());
		this.r.lineTo(3, 4);
		assertTrue(this.r.isEmpty());
		this.r.lineTo(5, 6);
		assertFalse(this.r.isEmpty());
	}

	@Test
	@Override
	public void clear() {
		this.r.clear();
		assertEquals(0, this.r.size());
	}

	@Test
	@Override
	public void testClone() {
		Path2i b = this.r.clone();

		assertNotSame(b, this.r);
		PathElement2i pe1, pe2;
		PathIterator2i i1 = this.r.getPathIterator();
		PathIterator2i i2 = b.getPathIterator();
		while (i1.hasNext()) {
			assertTrue(i2.hasNext());
			pe1 = i1.next();
			pe2 = i2.next();
			assertEquals(pe1.type, pe2.type);
			assertEquals(pe1.fromX, pe2.fromX);
			assertEquals(pe1.fromY, pe2.fromY);
			assertEquals(pe1.ctrlX1, pe2.ctrlX1);
			assertEquals(pe1.ctrlY1, pe2.ctrlY1);
			assertEquals(pe1.ctrlX2, pe2.ctrlX2);
			assertEquals(pe1.ctrlY2, pe2.ctrlY2);
			assertEquals(pe1.toX, pe2.toX);
			assertEquals(pe1.toY, pe2.toY);
			
		}
		assertFalse(i2.hasNext());
		
		b.translate(1, 1);

		i1 = this.r.getPathIterator();
		i2 = b.getPathIterator();
		boolean first = true;
		while (i1.hasNext()) {
			assertTrue(i2.hasNext());
			pe1 = i1.next();
			pe2 = i2.next();
			assertEquals(pe1.type, pe2.type);
			if (pe1.type!=PathElementType.MOVE_TO || !first) {
				assertNotEquals(pe1.fromX, pe2.fromX);
				assertNotEquals(pe1.fromY, pe2.fromY);
			}
			
			if (pe1.type==PathElementType.CURVE_TO || pe1.type==PathElementType.QUAD_TO) {
				assertNotEquals(pe1.ctrlX1, pe2.ctrlX1);
				assertNotEquals(pe1.ctrlY1, pe2.ctrlY1);
				if (pe1.type==PathElementType.CURVE_TO) {
					assertNotEquals(pe1.ctrlX2, pe2.ctrlX2);
					assertNotEquals(pe1.ctrlY2, pe2.ctrlY2);
				}
			}
			
			assertNotEquals(pe1.toX, pe2.toX);
			assertNotEquals(pe1.toY, pe2.toY);
			first = false;
		}
		assertFalse(i2.hasNext());
	}

	/*private static int toX(float x) {
		return (int)(x * 100) + 20;
	}
	
	private static int toY(float y) {
		return (int)(y * 100) + 600;
	}

	private static int toS(float s) {
		return (int)(s * 100);
	}

	public void testFlattening() throws IOException {
		BufferedImage img = new BufferedImage(800, 1000, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)img.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 800, 1000);
		g.setColor(Color.LIGHT_GRAY);
		for(float y=-6; y<=4; y+=1) {
			if (y!=0) g.drawLine(toX(-10), toY(y), toX(10), toY(y));
		}
		for(float x=-1; x<=8; x+=1) {
			if (x!=0) g.drawLine(toX(x), toY(-10), toX(x), toY(10));
		}
		g.setColor(Color.BLACK);
		g.drawLine(toX(-10), toY(0), toX(10), toY(0));
		g.drawLine(toX(0), toY(-10), toX(0), toY(10));
		
		g.setColor(Color.ORANGE);
		Segment2i[] ells = new Segment2i[] {
				new Segment2i(0, 0, 1, 1),
				new Segment2i(4, 3, 1, 1),
				new Segment2i(2, 2, 1, 1),
				new Segment2i(2, 1, 1, 1),
				new Segment2i(3, 0, 1, 1),
				new Segment2i(-1, -1, 1, 1),
				new Segment2i(4, -3, 1, 1),
				new Segment2i(-3, 4, 1, 1),
				new Segment2i(6, -5, 1, 1),
				new Segment2i(4, 0, 1, 1),
				new Segment2i(5, 0, 1, 1),
				new Segment2i(.01, .01, 1, 1),
		};
		for(Segment2i ell : ells) {
			g.drawLine(
					toX(ell.getX1()), toY(ell.getY1()),
					toX(ell.getX2()), toY(ell.getY2()));
		}
		
		g.setColor(Color.GREEN);
		
		Segment2i[] segs = new Segment2i[] {
//				new Segment2i(-1, -5, 5, -4),
//				new Segment2i(-5, -1, -7, 3),
//				new Segment2i(10, -1, 11, -2),
//				new Segment2i(1, 6, 3, 5),
//				new Segment2i(6, -1, 5, .5),
//				new Segment2i(6, -1, 5, 2),
//				new Segment2i(6, .5, 5, 2),
//				new Segment2i(3, 0, .5, .5),
//				new Segment2i(3, 0, 0, 2),
//				new Segment2i(.5, -1, .5, 4),
//				new Segment2i(3, 0, 1, 3),
		};
		for(Segment2i seg : segs) {
			g.drawLine(toX(seg.getX1()), toY(seg.getY1()), toX(seg.getX2()), toY(seg.getY2()));
		}
		
		PathIterator2i pi = this.r.getPathIterator(.5);
		while (pi.hasNext()) {
			PathElement2f pe = pi.next();
			switch(pe.type) {
			case LINE_TO:
				g.drawLine(
						toX(pe.fromX), toY(pe.fromY),
						toX(pe.toX), toY(pe.toY));
				break;
			case QUAD_TO:
				g.draw( new QuadCurve2D.Float(
						toX(pe.fromX), toY(pe.fromY),
						toX(pe.ctrlX1), toY(pe.ctrlY1),
						toX(pe.toX), toY(pe.toY)));
				break;
			case CURVE_TO:
				g.draw( new CubicCurve2D.Float(
						toX(pe.fromX), toY(pe.fromY),
						toX(pe.ctrlX1), toY(pe.ctrlY1),
						toX(pe.ctrlX2), toY(pe.ctrlY2),
						toX(pe.toX), toY(pe.toY)));
				break;
			case CLOSE:
				g.drawLine(
						toX(pe.fromX), toY(pe.fromY),
						toX(pe.toX), toY(pe.toY));
				break;
			default:
			}
		}

		g.setColor(Color.RED);
		pi = this.r.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
		while (pi.hasNext()) {
			PathElement2f pe = pi.next();
			switch(pe.type) {
			case LINE_TO:
				g.drawLine(
						toX(pe.fromX), toY(pe.fromY),
						toX(pe.toX), toY(pe.toY));
				break;
			case CLOSE:
				g.drawLine(
						toX(pe.fromX), toY(pe.fromY),
						toX(pe.toX), toY(pe.toY));
				break;
			default:
			}
		}
		g.dispose();
		ImageIO.write(img, "png", new File("/home/sgalland/mytest.png"));
	}*/
	
	/**
	 */
	@Test
	public void getPointIterator() {
		Point2i p;
		Iterator<Point2i> it = this.r.getPointIterator();
		
		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 0, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 1, p.ix());
		assertEquals(p.toString(), 1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 2, p.ix());
		assertEquals(p.toString(), 2, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 3, p.ix());
		assertEquals(p.toString(), 1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 2, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 3, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 2, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 5, p.ix());
		assertEquals(p.toString(), 2, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 5, p.ix());
		assertEquals(p.toString(), 1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 6, p.ix());
		assertEquals(p.toString(), 1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 6, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 6, p.ix());
		assertEquals(p.toString(), -1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -2, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -3, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -4, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		assertFalse(it.hasNext());
	}
	
	/**
	 */
	@Test
	public void computeCrossingsFromPoint() {
		assertEquals(0, Path2i.computeCrossingsFromPoint(this.r.getPathIterator(), -2, 1));
		assertEquals(0, Path2i.computeCrossingsFromPoint(this.r.getPathIterator(), 0, -3));
		assertEquals(MathConstants.SHAPE_INTERSECTS,
				Path2i.computeCrossingsFromPoint(this.r.getPathIterator(), 4, 3));
		assertEquals(-2, Path2i.computeCrossingsFromPoint(this.r.getPathIterator(), 3, 0));
	}

	/**
	 */
	@Test
	public void computeCrossingsFromRect() {
		assertEquals(0, Path2i.computeCrossingsFromRect(
				this.r.getPathIterator(),
				-2, 1, -1, 2));
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2i.computeCrossingsFromRect(
				this.r.getPathIterator(),
				0, 1, 3, 6));
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2i.computeCrossingsFromRect(
				this.r.getPathIterator(),
				3, -1, 8, 0));
		assertEquals(-2, Path2i.computeCrossingsFromRect(
				this.r.getPathIterator(),
				3, -1, 4, 0));
		assertEquals(-2, Path2i.computeCrossingsFromRect(
				this.r.getPathIterator(),
				3, -1, 5, 0));
		assertEquals(0, Path2i.computeCrossingsFromRect(
				this.r.getPathIterator(),
				0, -4, 3, -3));
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2i.computeCrossingsFromRect(
				this.r.getPathIterator(),
				0, -4, 4, -3));
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2i.computeCrossingsFromRect(
				this.r.getPathIterator(),
				0, -4, 3, -2));
	}

	/**
	 */
	@Test
	public void computeCrossingsFromSegment() {
		assertEquals(0, Path2i.computeCrossingsFromSegment(
				this.r.getPathIterator(),
				-2, 1, -1, 2));
		assertEquals(0, Path2i.computeCrossingsFromSegment(
				this.r.getPathIterator(),
				0, 1, 3, 6));
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2i.computeCrossingsFromSegment(
				this.r.getPathIterator(),
				3, -1, 8, 0));
		assertEquals(-2, Path2i.computeCrossingsFromSegment(
				this.r.getPathIterator(),
				3, -1, 4, 0));
		assertEquals(-2, Path2i.computeCrossingsFromSegment(
				this.r.getPathIterator(),
				3, -1, 5, 0));
		assertEquals(0, Path2i.computeCrossingsFromSegment(
				this.r.getPathIterator(),
				0, -4, 3, -3));
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2i.computeCrossingsFromSegment(
				this.r.getPathIterator(),
				0, -4, 4, -3));
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2i.computeCrossingsFromSegment(
				this.r.getPathIterator(),
				0, -4, 3, -2));
	}
	
	/**
	 */
	@Test
	public void computeCrossingsFromCircle() {
		assertEquals(0, Path2i.computeCrossingsFromCircle(
				this.r.getPathIterator(),
				-2, 1, 1));
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2i.computeCrossingsFromCircle(
				this.r.getPathIterator(),
				-2, 1, 2));
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2i.computeCrossingsFromCircle(
				this.r.getPathIterator(),
				0, 1, 3));
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2i.computeCrossingsFromCircle(
				this.r.getPathIterator(),
				3, -1, 8));
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2i.computeCrossingsFromCircle(
				this.r.getPathIterator(),
				3, -1, 1));
		assertEquals(-2, Path2i.computeCrossingsFromCircle(
				this.r.getPathIterator(),
				4, -1, 0));
		assertEquals(0, Path2i.computeCrossingsFromCircle(
				this.r.getPathIterator(),
				20, 0, 2));
	}

	/**
	 */
	@Test
	public void containsPathIterator2iIntInt() {
		assertTrue(Path2i.contains(this.r.getPathIterator(), 0, 0));
		assertTrue(Path2i.contains(this.r.getPathIterator(), 4, 3));
		assertTrue(Path2i.contains(this.r.getPathIterator(), 2, 2));
		assertTrue(Path2i.contains(this.r.getPathIterator(), 2, 1));
		assertTrue(Path2i.contains(this.r.getPathIterator(), 4, 2));
		assertTrue(Path2i.contains(this.r.getPathIterator(), 4, 3));
		assertFalse(Path2i.contains(this.r.getPathIterator(), -1, -1));
		assertFalse(Path2i.contains(this.r.getPathIterator(), 6, 2));
		assertTrue(Path2i.contains(this.r.getPathIterator(), 3, -2));
		assertFalse(Path2i.contains(this.r.getPathIterator(), 2, -2));
	}

	/**
	 */
	@Test
	public void intersectsPathIterator2iIntIntIntInt() {
		assertTrue(Path2i.intersects(this.r.getPathIterator(), 0, 0, 1, 1));
		assertTrue(Path2i.intersects(this.r.getPathIterator(), 4, 3, 1, 1));
		assertTrue(Path2i.intersects(this.r.getPathIterator(), 2, 2, 1, 1));
		assertTrue(Path2i.intersects(this.r.getPathIterator(), 2, 1, 1, 1));
		assertTrue(Path2i.intersects(this.r.getPathIterator(), 3, 0, 1, 1));
		assertTrue(Path2i.intersects(this.r.getPathIterator(), -1, -1, 1, 1));
		assertTrue(Path2i.intersects(this.r.getPathIterator(), 4, -3, 1, 1));
		assertFalse(Path2i.intersects(this.r.getPathIterator(), -3, 4, 1, 1));
		assertTrue(Path2i.intersects(this.r.getPathIterator(), 6, -5, 1, 1));
		assertTrue(Path2i.intersects(this.r.getPathIterator(), 4, 0, 1, 1));
		assertTrue(Path2i.intersects(this.r.getPathIterator(), 5, 0, 1, 1));
		assertFalse(Path2i.intersects(this.r.getPathIterator(), 0, -3, 1, 1));
		assertFalse(Path2i.intersects(this.r.getPathIterator(), 0, -3, 2, 1));
	}
	
	/**
	 */
	@Test
	public void getClosestPointTo() {
		Point2D p;
		
		p = this.r.getClosestPointTo(new Point2i(0, 0));
		assertEquals(p.toString(), 0, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		p = this.r.getClosestPointTo(new Point2i(-1, -4)); // remeber: path is closed
		assertEquals(p.toString(), 1, p.ix());
		assertEquals(p.toString(), -1, p.iy());

		p = this.r.getClosestPointTo(new Point2i(4, 0));
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		p = this.r.getClosestPointTo(new Point2i(4, 2));
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 2, p.iy());

		p = this.r.getClosestPointTo(new Point2i(4, -1));
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), -1, p.iy());
	}
	
	/**
	 */
	@Test
	public void getFarthestPointTo() {
		Point2D p;
		
		p = this.r.getFarthestPointTo(new Point2i(0, 0));
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = this.r.getFarthestPointTo(new Point2i(-1, -4)); // remeber: path is closed
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -2, p.iy());

		p = this.r.getFarthestPointTo(new Point2i(4, 0));
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = this.r.getFarthestPointTo(new Point2i(4, 2));
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = this.r.getFarthestPointTo(new Point2i(4, -1));
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());
	}

	@Test
	@Override
	public void distancePoint2D() {
		assertEpsilonEquals(0f, this.r.distance(new Point2i(0, 0)));
		assertEpsilonEquals(0f, this.r.distance(new Point2i(1, 0)));
		assertEpsilonEquals(7.071067812f, this.r.distance(new Point2i(-5, -5)));
		assertEpsilonEquals(3f, this.r.distance(new Point2i(4, 6)));
		assertEpsilonEquals(1f, this.r.distance(new Point2i(7, 0)));
	}

	@Test
	@Override
	public void distanceSquaredPoint2D() {
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2i(0, 0)));
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2i(1, 0)));
		assertEpsilonEquals(50f, this.r.distanceSquared(new Point2i(-5, -5)));
		assertEpsilonEquals(9f, this.r.distanceSquared(new Point2i(4, 6)));
		assertEpsilonEquals(1f, this.r.distanceSquared(new Point2i(7, 0)));
	}

	@Test
	@Override
	public void distanceL1Point2D() {
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2i(0, 0)));
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2i(1, 0)));
		assertEpsilonEquals(10f, this.r.distanceL1(new Point2i(-5, -5)));
		assertEpsilonEquals(3f, this.r.distanceL1(new Point2i(4, 6)));
		assertEpsilonEquals(1f, this.r.distanceL1(new Point2i(7, 0)));
	}

	@Test
	@Override
	public void distanceLinfPoint2D() {
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2i(0, 0)));
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2i(1, 0)));
		assertEpsilonEquals(5f, this.r.distanceLinf(new Point2i(-5, -5)));
		assertEpsilonEquals(3f, this.r.distanceLinf(new Point2i(4, 6)));
		assertEpsilonEquals(1f, this.r.distanceLinf(new Point2i(7, 0)));
	}

	@Test
	@Override
	public void translateIntInt() {
		this.r.translate(3, 4);
		PathIterator2i pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	/**
	 */
	@Test
	public void setWindingRule() {
		assertEquals(PathWindingRule.NON_ZERO, this.r.getWindingRule());
		for(PathWindingRule rule : PathWindingRule.values()) {
			this.r.setWindingRule(rule);
			assertEquals(rule, this.r.getWindingRule());
		}
	}

	/**
	 */
	@Test
	public void addIterator() {
		Path2i p2 = new Path2i();
		p2.moveTo(3, 4);
		p2.lineTo(5, 6);
		
		this.r.add(p2.getPathIterator());
		
		PathIterator2i pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE);
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertNoElement(pi);
	}

	/** 
	 */
	@Test
	public void getPathIteratorVoid() {
		PathIterator2i pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	/** 
	 */
	@Test
	public void getPathIteratorTransform2D() {
		Transform2D tr;
		PathIterator2i pi;
		
		tr = new Transform2D();
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3, 4);
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	/** 
	 */
	@Test
	public void transformTransform2D() {
		Transform2D tr = new Transform2D();
		tr.makeTranslationMatrix(3, 4);
		Transform2D tr2 = new Transform2D();
		tr2.makeRotationMatrix(5.6f);
		
		Path2i clone = this.r.clone();
		clone.transform(tr);

		PathIterator2i pi = clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		clone = this.r.clone();
		clone.transform(tr2);

		pi = clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 2, -1, 4, -0);
		assertElement(pi, PathElementType.CURVE_TO, 3, -3, 7, 0, 2, -8);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		Transform2D tr3 = new Transform2D();
		tr3.mul(tr, tr2);
		clone = this.r.clone();
		clone.transform(tr3);

		pi = clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 4);
		assertElement(pi, PathElementType.QUAD_TO, 5, 2, 7, 3);
		assertElement(pi, PathElementType.CURVE_TO, 6, 0, 10, 4, 5, -4);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	/** 
	 */
	@Test
	public void createTransformedShape2D() {
		Transform2D tr = new Transform2D();
		tr.makeTranslationMatrix(3, 4);
		Path2i p2 = (Path2i)this.r.createTransformedShape(tr);

		PathIterator2i pi = p2.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	/** 
	 */
	@Test
	public void containsIntInt() {
		assertTrue(this.r.contains(0, 0));
		assertTrue(this.r.contains(4, 3));
		assertTrue(this.r.contains(2, 2));
		assertTrue(this.r.contains(2, 1));
		assertTrue(this.r.contains(4, 2));
		assertTrue(this.r.contains(4, 3));
		assertFalse(this.r.contains(-1, -1));
		assertFalse(this.r.contains(6, 2));
		assertTrue(this.r.contains(3, -2));
		assertFalse(this.r.contains(2, -2));
	}

	/** 
	 */
	@Test
	public void containsRectangle2i() {
		assertFalse(this.r.contains(new Rectangle2i(0, 0, 1, 1)));
		assertFalse(this.r.contains(new Rectangle2i(4, 3, 1, 1)));
		assertFalse(this.r.contains(new Rectangle2i(2, 2, 1, 1)));
		assertFalse(this.r.contains(new Rectangle2i(2, 1, 1, 1)));
		assertTrue(this.r.contains(new Rectangle2i(3, 0, 1, 1)));
		assertTrue(this.r.contains(new Rectangle2i(3, 0, 2, 1)));
		assertFalse(this.r.contains(new Rectangle2i(-1, -1, 1, 1)));
		assertTrue(this.r.contains(new Rectangle2i(4, -3, 1, 1)));
		assertFalse(this.r.contains(new Rectangle2i(-3, 4, 1, 1)));
		assertFalse(this.r.contains(new Rectangle2i(6, -5, 1, 1)));
		assertTrue(this.r.contains(new Rectangle2i(4, 0, 1, 1)));
		assertTrue(this.r.contains(new Rectangle2i(5, 0, 1, 1)));
		assertFalse(this.r.contains(new Rectangle2i(5, 0, 2, 1)));
		assertFalse(this.r.contains(new Rectangle2i(6, 0, 1, 1)));
	}

	/** 
	 */
	@Test
	public void intersectsRectangle2i() {
		assertTrue(this.r.intersects(new Rectangle2i(0, 0, 1, 1)));
		assertTrue(this.r.intersects(new Rectangle2i(4, 3, 1, 1)));
		assertTrue(this.r.intersects(new Rectangle2i(2, 2, 1, 1)));
		assertTrue(this.r.intersects(new Rectangle2i(2, 1, 1, 1)));
		assertTrue(this.r.intersects(new Rectangle2i(3, 0, 1, 1)));
		assertTrue(this.r.intersects(new Rectangle2i(-1, -1, 1, 1)));
		assertTrue(this.r.intersects(new Rectangle2i(4, -3, 1, 1)));
		assertFalse(this.r.intersects(new Rectangle2i(-3, 4, 1, 1)));
		assertTrue(this.r.intersects(new Rectangle2i(6, -5, 1, 1)));
		assertTrue(this.r.intersects(new Rectangle2i(4, 0, 1, 1)));
		assertTrue(this.r.intersects(new Rectangle2i(5, 0, 1, 1)));
		assertFalse(this.r.intersects(new Rectangle2i(0, -3, 1, 1)));
		assertFalse(this.r.intersects(new Rectangle2i(0, -3, 2, 1)));
	}

	/** 
	 */
	@Test
	public void intersectsCircle2i() {
		assertTrue(this.r.intersects(new Circle2i(0, 0, 1)));
		assertTrue(this.r.intersects(new Circle2i(4, 3, 1)));
		assertTrue(this.r.intersects(new Circle2i(2, 2, 1)));
		assertTrue(this.r.intersects(new Circle2i(2, 1, 1)));
		assertTrue(this.r.intersects(new Circle2i(3, 0, 1)));
		assertFalse(this.r.intersects(new Circle2i(-1, -1, 1)));
		assertTrue(this.r.intersects(new Circle2i(4, -3, 1)));
		assertFalse(this.r.intersects(new Circle2i(-3, 4, 1)));
		assertTrue(this.r.intersects(new Circle2i(6, -5, 1)));
		assertTrue(this.r.intersects(new Circle2i(4, 0, 1)));
		assertTrue(this.r.intersects(new Circle2i(5, 0, 1)));
		assertTrue(this.r.intersects(new Circle2i(6, 2, 1)));
		assertFalse(this.r.intersects(new Circle2i(-5, 0, 3)));
	}

	/** 
	 */
	@Test
	public void intersectsSegment2i() {
		assertTrue(this.r.intersects(new Segment2i(0, 0, 1, 1)));
		assertTrue(this.r.intersects(new Segment2i(4, 3, 1, 1)));
		assertTrue(this.r.intersects(new Segment2i(2, 2, 1, 1)));
		assertTrue(this.r.intersects(new Segment2i(2, 1, 1, 1)));
		assertTrue(this.r.intersects(new Segment2i(3, 0, 1, 1)));
		assertTrue(this.r.intersects(new Segment2i(-1, -1, 1, 1)));
		assertTrue(this.r.intersects(new Segment2i(4, -3, 1, 1)));
		assertTrue(this.r.intersects(new Segment2i(-3, 4, 1, 1)));
		assertTrue(this.r.intersects(new Segment2i(6, -5, 1, 1)));
		assertTrue(this.r.intersects(new Segment2i(4, 0, 1, 1)));
		assertTrue(this.r.intersects(new Segment2i(5, 0, 1, 1)));
		assertFalse(this.r.intersects(new Segment2i(-4, -4, -3, -3)));
		assertFalse(this.r.intersects(new Segment2i(-1, 0, 2, 3)));
		assertFalse(this.r.intersects(new Segment2i(7, 1, 18, 14)));
	}

	@Test
	@Override
	public void toBoundingBox() {
		Rectangle2i bb = this.r.toBoundingBox();
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(3, bb.getMaxY());
	}

	/**
	 */
	@Test
	public void toBoundingBoxWithCtrlPoints() {
		Rectangle2i bb = this.r.toBoundingBoxWithCtrlPoints();
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(5, bb.getMaxY());
	}

	/**
	 */
	@Test
	public void removeLast() {
		PathIterator2i pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
		
		this.r.removeLast();

		pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertNoElement(pi);

		this.r.removeLast();

		pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertNoElement(pi);

		this.r.removeLast();

		pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertNoElement(pi);

		this.r.removeLast();

		pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertNoElement(pi);

		this.r.removeLast();

		pi = this.r.getPathIterator();
		assertNoElement(pi);

		this.r.removeLast();

		pi = this.r.getPathIterator();
		assertNoElement(pi);
	}

	/**
	 */
	@Test
	public void setLastPointIntInt() {
		PathIterator2i pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
		
		this.r.setLastPoint(123, 789);

		pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 123, 789);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}
	
	/**
	 */
	@Test
	public void removeIntInt() {
		PathIterator2i pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
		
		this.r.remove(2, 2);

		pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		this.r.remove(4, 3);

		pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		this.r.remove(6, 5);

		pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		this.r.remove(6, 5);

		pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	/**
	 */
	@Test
	public void containsPointPoint2D() {
		PathIterator2i pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
		
		assertTrue(this.r.containsPoint(new Point2i(2, 2)));
		assertFalse(this.r.containsPoint(new Point2i(4, 4)));
		assertTrue(this.r.containsPoint(new Point2i(6, 5)));
		assertFalse(this.r.containsPoint(new Point2i(-1, 6)));
		assertFalse(this.r.containsPoint(new Point2i(1234, 5678)));
	}

}