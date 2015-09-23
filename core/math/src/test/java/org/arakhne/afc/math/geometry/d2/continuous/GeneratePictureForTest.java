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
package org.arakhne.afc.math.geometry.d2.continuous;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.d2.continuous.Circle2f;
import org.arakhne.afc.math.geometry.d2.continuous.Ellipse2f;
import org.arakhne.afc.math.geometry.d2.continuous.Path2f;
import org.arakhne.afc.math.geometry.d2.continuous.AbstractPathElement2F;
import org.arakhne.afc.math.geometry.d2.continuous.PathIterator2f;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.arakhne.afc.math.geometry.d2.continuous.Rectangle2f;
import org.arakhne.afc.math.geometry.d2.continuous.Segment2f;
import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
import org.arakhne.afc.vmutil.FileSystem;
import org.junit.Before;
import org.junit.Test;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GeneratePictureForTest {

	private static final boolean GENERATE_TESTING_PICTURE = true;
	
	private static final double CELL_SIZE = 100;

	private Path2f[] paths;
	private Point2f[] points;
	private Segment2f[] lines;
	private Segment2f[] segments;
	private Circle2f[] circles;
	private Rectangle2f[] rectangles;
	private Ellipse2f[] ellipses;

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		//
		// PATHS
		Path2f r0 = new Path2f();
		r0.moveTo(1f, 1f);
		r0.lineTo(2f, 2f);
		r0.quadTo(3f, 0f, 4f, 3f);
		r0.curveTo(5f, -1f, 6f, 5f, 7f, -5f);

		Path2f r1 = new Path2f();
		r1.moveTo(-2f, -2f);
		r1.lineTo(-2f, 2f);
		r1.lineTo(2f, 2f);
		r1.lineTo(2f, -2f);

		Path2f r2 = new Path2f();
		r2.moveTo(-2f, -2f);
		r2.lineTo(0f, 0f);
		r2.lineTo(-2f, 2f);

		Path2f r3 = new Path2f();
		r3.moveTo(-2f, -2f);
		r3.lineTo(2f, 2f);
		r3.lineTo(-2f, 2f);

		Path2f r4 = new Path2f();
		r4.moveTo(-2f, -2f);
		r4.lineTo(-2f, 2f);
		r4.lineTo(2f, -2f);

		Path2f r5 = new Path2f();
		r5.moveTo(-2f, 2f);
		r5.lineTo(1f, 0f);
		r5.lineTo(2f, 1f);

		Path2f r6 = new Path2f();
		r6.moveTo(-2f, 2f);
		r6.lineTo(2f, 1f);
		r6.lineTo(1f, 0f);

		this.paths = new Path2f[] {
//				r0,
//				r1,
//				r2,
//				r3,
//				r4,
//				r5,
//				r6,
		};

		//
		// POINTS
		this.points = new Point2f[] {
				//				new Point2f(0f, 0f),
				//				new Point2f(4f, 3f),
				//				new Point2f(2f, 2f),
				//				new Point2f(2f, 1f),
				//				new Point2f(5f, 0f),
				//				new Point2f(-1f, -1f),
				//				new Point2f(5f, 2f),
				//				new Point2f(3.5f, -2.5f),
				//				new Point2f(7f, -4f),
				//				new Point2f(2.5f, 1.5f),
		};

		//
		// LINES
		this.lines = new Segment2f[] {
				//new Segment2f(6.7773438f, -3.0272121f, 6.7890625f, -3.1188917f),
				//new Segment2f(6.7890625f, -3.1188917f, 6.8007812f, -3.2118688f),
				//new Segment2f(6.8007812f, -3.2118688f, 6.8125f, -3.3061523f),
				//new Segment2f(6.9414062f, -4.4321795f, 6.953125f, -4.5428696f),
				//new Segment2f(.5f, -1f, .5f, 2f),
				//new Segment2f(4f, -3f, 1f, 1f),
		};

		//
		// SEGMENTS
		this.segments = new Segment2f[] {
				new Segment2f(4f, -3f, 1f, 1f),
				new Segment2f(7f, -5f, 1f, 1f),
				//				new Segment2f(0f, 0f, 1f, 1f),
				//				new Segment2f(4f, 3f, 1f, 1f),
				//				new Segment2f(2f, 2f, 1f, 1f),
				//				new Segment2f(2f, 1f, 1f, 1f),
				//				new Segment2f(3f, 0f, 1f, 1f),
				//				new Segment2f(3f, 0f, 1.5f, 1f),
				//				new Segment2f(-1f, -1f, 1f, 1f),
				//				new Segment2f(4f, -3f, 1f, 1f),
				//				new Segment2f(4f, -3f, 1f, 0f),
				//				new Segment2f(-3f, 4f, 1f, 1f),
				//				new Segment2f(6f, -5f, 1f, 1f),
				//				new Segment2f(6f, -5f, 1f, 0f),
				//				new Segment2f(4f, 0f, 1f, 1f),
				//				new Segment2f(5f, 0f, 1f, 1f),
				//				new Segment2f(.01f, .01f, 1f, 1f),
		};

		//
		// CIRCLES
		this.circles = new Circle2f[] {
				//new Circle2f(0f, 0f, 1f),
				//				new Circle2f(4f, 3f, 1f),
				//				new Circle2f(2f, 2f, 1f),
				//				new Circle2f(2f, 1f, 1f),
				//				new Circle2f(3f, 0f, 1f),
				//				new Circle2f(-1f, -1f, 1f),
				//				new Circle2f(4f, -3f, 1f),
				//				new Circle2f(-3f, 4f, 1f),
				//				new Circle2f(6f, -5f, 1f),
				//				new Circle2f(6f, -5f, .95f),
				//				new Circle2f(4f, 0f, 1f),
				//				new Circle2f(5f, 0f, 1f),
				//				new Circle2f(.01f, .01f, 1f),
				//				new Circle2f(6f, 2f, .8f),
		};

		//
		// RECTANGLES
		this.rectangles = new Rectangle2f[] {
				//				new Rectangle2f(0f, 0f, 1f, 1f),
				//				new Rectangle2f(4f, 3f, 1f, 1f),
				//				new Rectangle2f(2f, 2f, 1f, 1f),
				//				new Rectangle2f(2f, 1f, 1f, 1f),
				//				new Rectangle2f(3f, 0f, 1f, 1f),
				//				new Rectangle2f(-1f, -1f, 1f, 1f),
				//				new Rectangle2f(4f, -3f, 1f, 1f),
				//				new Rectangle2f(-3f, 4f, 1f, 1f),
				//				new Rectangle2f(6f, -5f, 1f, 1f),
				//				new Rectangle2f(4f, 0f, 1f, 1f),
				//				new Rectangle2f(5f, 0f, 1f, 1f),
				//				new Rectangle2f(.01f, .01f, 1f, 1f),
		};

		//
		// ELLIPSES
		this.ellipses = new Ellipse2f[] {
				//				new Ellipse2f(0f, 0f, 1.1f, 1f),
				//				new Ellipse2f(0f, 0f, 1f, 2f),
				//				new Ellipse2f(4f, 3f, 1f, 2f),
				//				new Ellipse2f(2f, 2f, 1f, 2f),
				//				new Ellipse2f(2f, 1f, 1f, 2f),
				//				new Ellipse2f(3f, 0f, 1f, 2f),
				//				new Ellipse2f(-1f, -1f, 1f, 2f),
				//				new Ellipse2f(4f, -3f, 1f, 2f),
				//				new Ellipse2f(-3f, 4f, 1f, 2f),
				//				new Ellipse2f(6f, -5f, 1f, 2f),
				//				new Ellipse2f(6f, -5f, .8f, 2f),
				//				new Ellipse2f(4f, 0f, 1f, 2f),
				//				new Ellipse2f(6f, 0f, 1f, 2f),
		};

	}
	
	private static int toX(double x, Rectangle2f doc) {
		return (int)((x - doc.getMinX()) * CELL_SIZE);
	}

	private static int toY(double y, Rectangle2f doc) {
		return (int)((doc.getMaxY() - y) * CELL_SIZE);
	}

	private static int toS(double s) {
		return (int)(s * CELL_SIZE);
	}

	/**
	 * @throws IOException
	 */
	@Test
	public void imageGeneration() throws IOException {
		PathIterator2f pi;

		// Compute the bounds
		Rectangle2f docBounds = new Rectangle2f();
		boolean isset = false;
		for(Point2f obj : this.points) {
			if (isset)
				docBounds.add(obj.getX(), obj.getY());
			else {
				docBounds.set(obj.getX(), obj.getY(), 0, 0);
				isset = true;
			}
		}
		for(Path2f obj : this.paths) {
			Rectangle2f bb = obj.toBoundingBox();
			if (isset) {
				docBounds.setUnion(bb);
			}
			else {
				docBounds.set(bb);
				isset = true;
			}
		}
		for(Segment2f obj : this.lines) {
			double xx1 = obj.getX1();
			double yy1 = obj.getY1();
			double xx2 = obj.getX2();
			double yy2 = obj.getY2();
			if (isset) {
				Rectangle2f r = new Rectangle2f();
				r.setFromCorners(xx1, yy1, xx2, yy2);
				docBounds.setUnion(r);
			}
			else {
				docBounds.setFromCorners(xx1, yy1, xx2, yy2);
				isset = true;
			}
		}
		for(Segment2f obj : this.segments) {
			double xx1 = obj.getX1();
			double yy1 = obj.getY1();
			double xx2 = obj.getX2();
			double yy2 = obj.getY2();
			if (isset) {
				Rectangle2f r = new Rectangle2f();
				r.setFromCorners(xx1, yy1, xx2, yy2);
				docBounds.setUnion(r);
			}
			else {
				docBounds.setFromCorners(xx1, yy1, xx2, yy2);
				isset = true;
			}
		}
		for(Circle2f obj : this.circles) {
			double xx = obj.getX();
			double yy = obj.getY();
			double r = obj.getRadius();
			if (isset) {
				Rectangle2f rect = new Rectangle2f();
				rect.setFromCorners(xx-r, yy-r, xx+r, yy+r);
				docBounds.setUnion(rect);
			}
			else {
				docBounds.setFromCorners(xx-r, yy-r, xx+r, yy+r);
				isset = true;
			}
		}
		for(Ellipse2f obj : this.ellipses) {
			double x1 = obj.getMinX();
			double y1 = obj.getMinY();
			double x2 = obj.getMaxX();
			double y2 = obj.getMaxY();
			if (isset) {
				Rectangle2f rect = new Rectangle2f();
				rect.setFromCorners(x1, y1, x2, y2);
				docBounds.setUnion(rect);
			}
			else {
				docBounds.setFromCorners(x1, y1, x2, y2);
				isset = true;
			}
		}
		for(Rectangle2f obj : this.rectangles) {
			double x1 = obj.getMinX();
			double y1 = obj.getMinY();
			double x2 = obj.getMaxX();
			double y2 = obj.getMaxY();
			if (isset) {
				Rectangle2f rect = new Rectangle2f();
				rect.setFromCorners(x1, y1, x2, y2);
				docBounds.setUnion(rect);
			}
			else {
				docBounds.setFromCorners(x1, y1, x2, y2);
				isset = true;
			}
		}
		docBounds.inflate(2, 2, 2, 2);
		
		// Create the image
		BufferedImage img = new BufferedImage(
				toS(docBounds.getWidth()), 
				toS(docBounds.getHeight()),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)img.getGraphics();
		//Background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());

		// Elements
		g.setColor(Color.ORANGE);
		for(Point2f obj : this.points) {
			g.fillRect(
					toX(obj.getX(), docBounds) - 4, toY(obj.getY(), docBounds) - 4,
					9, 9);
		}
		Stroke oldStroke = g.getStroke();
		for(Segment2f obj : this.lines) {
			int xx1 = toX(obj.getX1(), docBounds);
			int yy1 = toY(obj.getY1(), docBounds);
			int xx2 = toX(obj.getX2(), docBounds);
			int yy2 = toY(obj.getY2(), docBounds);

			g.setStroke(new BasicStroke(2));
			Vector2f v = new Vector2f(xx2 - xx1, yy2 - yy1);
			v.normalize();
			v.scale(1000);
			int px1 = (int)(xx1 + v.x);
			int py1 = (int)(yy1 + v.y);
			int px2 = (int)(xx1 - v.x);
			int py2 = (int)(yy1 - v.y);
			g.drawLine(px1, py1, px2, py2);

			g.setStroke(new BasicStroke(4));
			g.drawLine(xx1, yy1, xx2, yy2);
		}
		g.setStroke(new BasicStroke(3));
		for(Segment2f obj : this.segments) {
			g.drawLine(
					toX(obj.getX1(), docBounds), toY(obj.getY1(), docBounds),
					toX(obj.getX2(), docBounds), toY(obj.getY2(), docBounds));
		}
		g.setStroke(oldStroke);
		for(Circle2f obj : this.circles) {
			int r = toS(obj.getRadius());
			g.drawOval(
					toX(obj.getX(), docBounds)-r, toY(obj.getY(), docBounds)-r,
					r*2, r*2);
		}
		for(Ellipse2f obj : this.ellipses) {
			int x1 = toX(obj.getMinX(), docBounds);
			int y1 = toY(obj.getMinY(), docBounds);
			int x2 = toX(obj.getMaxX(), docBounds);
			int y2 = toY(obj.getMaxY(), docBounds);
			g.drawOval(
					Math.min(x1, x2), Math.min(y1, y2),
					toS(obj.getWidth()), toS(obj.getHeight()));
		}
		for(Rectangle2f obj : this.rectangles) {
			int x1 = toX(obj.getMinX(), docBounds);
			int y1 = toY(obj.getMinY(), docBounds);
			int x2 = toX(obj.getMaxX(), docBounds);
			int y2 = toY(obj.getMaxY(), docBounds);
			g.fillRect(
					Math.min(x1, x2), Math.min(y1, y2),
					toS(obj.getWidth()), toS(obj.getHeight()));
		}

		// Cells
		g.setColor(Color.LIGHT_GRAY);
		int minXpixel = toX(docBounds.getMinX(), docBounds);
		int minYpixel = toY(docBounds.getMinY(), docBounds);
		int maxXpixel = toX(docBounds.getMaxX(), docBounds);
		int maxYpixel = toY(docBounds.getMaxY(), docBounds);
		if (minXpixel>maxXpixel) {
			int t = minXpixel;
			minXpixel = maxXpixel;
			maxXpixel = t;
		}
		if (minYpixel>maxYpixel) {
			int t = minYpixel;
			minYpixel = maxYpixel;
			maxYpixel = t;
		}
		for(int y=minYpixel; y<=maxYpixel; y+=CELL_SIZE) {
			g.drawLine(minXpixel, y, maxXpixel, y);
		}
		for(int x=minXpixel; x<=maxXpixel; x+=CELL_SIZE) {
			g.drawLine(x, minYpixel, x, maxYpixel);
		}
		g.setColor(Color.BLACK);
		int xzero = toX(0, docBounds);
		int yzero = toY(0, docBounds);
		g.drawLine(minXpixel, yzero, maxXpixel, yzero);
		g.drawLine(xzero, minYpixel, xzero, maxYpixel);
		g.fillOval(xzero-3, yzero-3, 7, 7);
		g.fillOval(xzero-3, toY(1, docBounds)-3, 7, 7);
		g.fillOval(toX(1, docBounds)-3, yzero-3, 7, 7);

		// Path
		Color[] colors = new Color[] {
				Color.RED, Color.BLUE, Color.CYAN, Color.GREEN
		};
		int colorIndex = 0;
		for(Path2f path : this.paths) {
			g.setColor(colors[colorIndex]);
			colorIndex = (colorIndex+1)%colors.length;
			pi = path.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
			boolean closed = false;
			int mx, my, cx, cy;
			mx = my = cx = cy = 0;
			while (pi.hasNext()) {
				AbstractPathElement2F pe = pi.next();
				switch(pe.type) {
				case MOVE_TO:
					cx = mx = toX(pe.toX, docBounds);
					cy = my = toY(pe.toY, docBounds);
					break;
				case LINE_TO:
					cx = toX(pe.toX, docBounds);
					cy = toY(pe.toY, docBounds);
					g.drawLine(
							toX(pe.fromX, docBounds), toY(pe.fromY, docBounds),
							cx, cy);
					break;
				case CLOSE:
					cx = toX(pe.toX, docBounds);
					cy = toY(pe.toY, docBounds);
					g.drawLine(
							toX(pe.fromX, docBounds), toY(pe.fromY, docBounds),
							cx, cy);
					closed = true;
					break;
				default:
				}
			}
			if (!closed) {
				g.setStroke(new BasicStroke(
						1,
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND,
						2,
						new float[]{5,5},
						0f));
				g.drawLine(
						mx, my,
						cx, cy);
				g.setStroke(oldStroke);
			}
		}
		g.dispose();
		if (GENERATE_TESTING_PICTURE)
			ImageIO.write(img, "png", new File(FileSystem.getUserHomeDirectory(), "mytest.png"));  //$NON-NLS-1$//$NON-NLS-2$
	}

}