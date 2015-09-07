/*
 * $Id$
 *
 * Jaak environment model is an open-source multiagent library.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014 Stéphane GALLAND.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sarl.jaak.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.discrete.object2d.Point2i;

/** This class provides 2D Bresenham algorithms.
 * <p>
 * The label "Bresenham" is used today for a whole family of algorithms extending
 * or modifying Bresenham's original algorithm. See further functions below.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * FIXME: Move this class into the Arakhne Foundation Classes.
 */
public final class Bresenham {

	private static final int SIDES = 4;

	private Bresenham() {
		//
	}

	/** The Bresenham line algorithm is an algorithm which determines which points in
	 * an n-dimensional raster should be plotted in order to form a close
	 * approximation to a straight line between two given points. It is
	 * commonly used to draw lines on a computer screen, as it uses only
	 * integer addition, subtraction and bit shifting, all of which are
	 * very cheap operations in standard computer architectures. It is one of the
	 * earliest algorithms developed in the field of computer graphics. A minor extension
	 * to the original algorithm also deals with drawing circles.
	 * <p>
	 * While algorithms such as Wu's algorithm are also frequently used in modern
	 * computer graphics because they can support antialiasing, the speed and
	 * simplicity of Bresenham's line algorithm mean that it is still important.
	 * The algorithm is used in hardware such as plotters and in the graphics
	 * chips of modern graphics cards. It can also be found in many software
	 * graphics libraries. Because the algorithm is very simple, it is often
	 * implemented in either the firmware or the hardware of modern graphics cards.
	 *
	 * @param x0 is the x-coordinate of the first point of the Bresenham line.
	 * @param y0 is the y-coordinate of the first point of the Bresenham line.
	 * @param x1 is the x-coordinate of the last point of the Bresenham line.
	 * @param y1 is the y-coordinate of the last point of the Bresenham line.
	 * @return an iterator on the points along the Bresenham line.
	 */
	public static Iterator<Point2i> line(int x0, int y0, int x1, int y1) {
		return new LineIterator(x0, y0, x1, y1);
	}

	/** Replies the points on a rectangle.
	 *
	 * @param x0 is the x-coordinate of the lowest point of the rectangle.
	 * @param y0 is the y-coordinate of the lowest point of the rectangle.
	 * @param width is the width of the rectangle.
	 * @param height is the height of the rectangle.
	 * @param firstSide indicates the first rectangle side to reply: <code>0</code>
	 * for top, <code>1</code> for right, <code>2</code> for bottom, or
	 * <code>3</code> for left.
	 * @return an iterator on the points along the rectangle.
	 */
	public static Iterator<Point2i> rectangle(int x0, int y0, int width, int height, int firstSide) {
		return new RectangleIterator(x0, y0, width, height, firstSide);
	}

	/** The Bresenham line algorithm is an algorithm which determines which points in
	 * an n-dimensional raster should be plotted in order to form a close
	 * approximation to a straight line between two given points. It is
	 * commonly used to draw lines on a computer screen, as it uses only
	 * integer addition, subtraction and bit shifting, all of which are
	 * very cheap operations in standard computer architectures. It is one of the
	 * earliest algorithms developed in the field of computer graphics. A minor extension
	 * to the original algorithm also deals with drawing circles.
	 * <p>
	 * While algorithms such as Wu's algorithm are also frequently used in modern
	 * computer graphics because they can support antialiasing, the speed and
	 * simplicity of Bresenham's line algorithm mean that it is still important.
	 * The algorithm is used in hardware such as plotters and in the graphics
	 * chips of modern graphics cards. It can also be found in many software
	 * graphics libraries. Because the algorithm is very simple, it is often
	 * implemented in either the firmware or the hardware of modern graphics cards.
	 *
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $FullVersion$
	 * @mavengroupid org.janus-project.jaak
	 * @mavenartifactid util
	 */
	private static class LineIterator implements Iterator<Point2i> {

		private final boolean steep;
		private final int ystep;
		private final int xstep;
		private final int deltax;
		private final int deltay;
		private final int x1;
		private int y;
		private int x;
		private int error;

		/**
		 * @param x0 is the x-coordinate of the first point of the Bresenham line.
		 * @param y0 is the y-coordinate of the first point of the Bresenham line.
		 * @param x1 is the x-coordinate of the last point of the Bresenham line.
		 * @param y1 is the y-coordinate of the last point of the Bresenham line.
		 */
		public LineIterator(int x0, int y0, int x1, int y1) {
			int _x0 = x0;
			int _y0 = y0;
			int _x1 = x1;
			int _y1 = y1;

			this.steep = Math.abs(_y1 - _y0) > Math.abs(_x1 - _x0);

			int swapv;
			if (this.steep) {
				//swap(x0, y0);
				swapv = _x0;
				_x0 = _y0;
				_y0 = swapv;
				//swap(x1, y1);
				swapv = _x1;
				_x1 = _y1;
				_y1 = swapv;
			}
			/*if (_x0 > _x1) {
				//swap(x0, x1);
				swapv = _x0;
				_x0 = _x1;
				_x1 = swapv;
				//swap(y0, y1);
				swapv = _y0;
				_y0 = _y1;
				_y1 = swapv;
			}*/

			this.deltax = Math.abs(_x1 - _x0);
			this.deltay = Math.abs(_y1 - _y0);
			this.error = this.deltax / 2;
			this.y = _y0;

			if (_x0 < _x1) {
				this.xstep = 1;
			} else {
				this.xstep = -1;
			}

			if (_y0 < _y1) {
				this.ystep = 1;
			} else {
				this.ystep = -1;
			}

			this.x1 = _x1;
			this.x = _x0;
		}

		@Override
		public boolean hasNext() {
			return ((this.xstep > 0) && (this.x <= this.x1))
					|| ((this.xstep < 0) && (this.x1 <= this.x));
		}

		@Override
		public Point2i next() {
			Point2i p = new Point2i();

			if (this.steep) {
				p.set(this.y, this.x);
			} else {
				p.set(this.x, this.y);
			}

			this.error = this.error - this.deltay;

			if (this.error < 0) {
				this.y = this.y + this.ystep;
				this.error = this.error + this.deltax;
			}

			this.x += this.xstep;

			return p;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	} // class LineIterator

	/** Iterates on points on a rectangle.
	 *
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $FullVersion$
	 * @mavengroupid org.janus-project.jaak
	 * @mavenartifactid util
	 */
	private static class RectangleIterator implements Iterator<Point2i> {

		private final int x0;
		private final int y0;
		private final int x1;
		private final int y1;
		private final int firstSide;

		private int currentSide;
		private int i;

		/**
		 * @param x
		 * @param y
		 * @param width
		 * @param height
		 * @param firstSide
		 */
		public RectangleIterator(int x, int y, int width, int height, int firstSide) {
			assert (firstSide >= 0 && firstSide < SIDES);
			this.firstSide = firstSide;
			this.x0 = x;
			this.y0 = y;
			this.x1 = x + width - 1;
			this.y1 = y + height - 1;

			this.currentSide = (width > 0 && height > 0) ? this.firstSide : -1;
			this.i = 0;
		}

		@Override
		public boolean hasNext() {
			return this.currentSide != -1;
		}

		private void computeCoordinates(Point2i p) {
			switch(this.currentSide) {
			// top
			case 0:
				p.set(
						this.x0 + this.i,
						this.y0);
				break;
			// right
			case 1:
				p.set(
						this.x1,
						this.y0 + this.i);
				break;
			// bottom
			case 2:
				p.set(
						this.x1 - this.i,
						this.y1);
				break;
			// left
			case 3:
				p.set(
						this.x0,
						this.y1 - this.i);
				break;
			default:
				throw new NoSuchElementException();
			}
		}

		private void changeSideIfNecessary(Point2i p) {
			int newSide = -1;

			switch(this.currentSide) {
			// top
			case 0:
				if (p.x() >= this.x1) {
					newSide = 1;
					this.i = 0;
				}
				break;
			// right
			case 1:
				if (p.y() >= this.y1) {
					newSide = 2;
					this.i = 0;
				}
				break;
			// bottom
			case 2:
				if (p.x() <= this.x0) {
					newSide = 3;
					this.i = 0;
				}
				break;
			// left
			case 3:
				if (p.y() <= this.y0) {
					newSide = 0;
					this.i = 0;
				}
				break;
			default:
				throw new NoSuchElementException();
			}

			this.currentSide = filterSide(newSide);
		}

		private int filterSide(int s) {
			if (s != -1) {
				if (this.firstSide == s) {
					return -1;
				}
				return s;
			}
			return this.currentSide;
		}

		@Override
		public Point2i next() {
			Point2i p = new Point2i();

			computeCoordinates(p);
			++this.i;
			changeSideIfNecessary(p);

			return p;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	} // class RectangleIterator

}
