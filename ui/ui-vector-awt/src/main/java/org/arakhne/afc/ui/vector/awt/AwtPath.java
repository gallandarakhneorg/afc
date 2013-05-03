/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012-13 Stephane GALLAND.
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
package org.arakhne.afc.ui.vector.awt;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.arakhne.afc.math.continous.object2d.Path2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.matrix.Transform2D;
import org.arakhne.afc.ui.CenteringTransform;
import org.arakhne.afc.ui.ZoomableContextUtil;
import org.arakhne.afc.ui.awt.VirtualizableShape;
import org.arakhne.afc.ui.awt.ZoomableAwtContextUtil;

/** Public implementation of a Path2D.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AwtPath implements VirtualizableShape, NativeWrapper {

	private final Path2f path;
	
	/**
	 * @param path
	 */
	public AwtPath(Path2f path) {
		super();
		this.path = path;
	}

	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this);
	}

	@Override
	public Rectangle getBounds() {
		Rectangle2f bb = this.path.toBoundingBox();
		int ix = (int)bb.getMinX();
		int iy = (int)bb.getMinY();
		int mx = (int)Math.ceil(bb.getMaxX());
		int my = (int)Math.ceil(bb.getMaxY());
		return new Rectangle(ix, iy, mx-ix, my-iy);
	}

	@Override
	public Rectangle2D getBounds2D() {
		Rectangle2f bb = this.path.toBoundingBox();
		return new Rectangle2D.Float(
				bb.getMinX(), bb.getMinY(),
				bb.getWidth(), bb.getHeight());
	}

	@Override
	public boolean contains(double x, double y) {
		return this.path.contains((float)x, (float)y);
	}

	@Override
	public boolean contains(Point2D p) {
		return this.path.contains((float)p.getX(), (float)p.getY());
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return Path2f.intersects(this.path.getPathIterator(),
				(float)x, (float)y, (float)w, (float)h);
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		return Path2f.intersects(this.path.getPathIterator(),
				(float)r.getMinX(), (float)r.getMinY(),
				(float)r.getWidth(), (float)r.getHeight());
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		return Path2f.contains(this.path.getPathIterator(),
				(float)x, (float)y, (float)w, (float)h);
	}

	@Override
	public boolean contains(Rectangle2D r) {
		return Path2f.contains(this.path.getPathIterator(),
				(float)r.getMinX(), (float)r.getMinY(),
				(float)r.getWidth(), (float)r.getHeight());
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		if (at==null || at.isIdentity())
			return new AwtPathIterator(this.path.getPathIterator());
		Transform2D tr = new Transform2D(
				(float)at.getScaleX(),
				(float)at.getShearX(),
				(float)at.getTranslateX(),
				(float)at.getShearY(),
				(float)at.getScaleY(),
				(float)at.getTranslateY());
		return new AwtPathIterator(this.path.getPathIterator(tr));
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		if (at==null || at.isIdentity())
			return new AwtPathIterator(this.path.getPathIterator((float)flatness));
		Transform2D tr = new Transform2D(
				(float)at.getScaleX(),
				(float)at.getShearX(),
				(float)at.getTranslateX(),
				(float)at.getShearY(),
				(float)at.getScaleY(),
				(float)at.getTranslateY());
		return new AwtPathIterator(this.path.getPathIterator(tr, (float)flatness));
	}

	@Override
	public VirtualizableShape toScreen(CenteringTransform centeringTransform, float zoom) {
		return new ScreenPath(centeringTransform, zoom);
	}

	@Override
	public VirtualizableShape fromScreen(CenteringTransform centeringTransform, float zoom) {
		return this;
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class ScreenPath implements VirtualizableShape, NativeWrapper {

		final CenteringTransform centeringTransform;
		float zoom;
		
		/**
		 * @param centeringTransform is the transformation that permits to
		 * center the objects on the view. 
		 * @param zoom is the current zooming factor of the view.
		 */
		public ScreenPath(
				CenteringTransform centeringTransform,
				float zoom) {
			this.centeringTransform = centeringTransform;
			this.zoom = zoom;
		}

		@Override
		public Rectangle getBounds() {
			Rectangle r = (Rectangle)AwtPath.this.getBounds().clone();
			ZoomableAwtContextUtil.logical2pixel(
					r,
					this.centeringTransform,
					this.zoom);
			return r;
		}

		@Override
		public Rectangle2D getBounds2D() {
			Rectangle2D r = (Rectangle2D)AwtPath.this.getBounds2D().clone();
			ZoomableAwtContextUtil.logical2pixel(
					r,
					this.centeringTransform,
					this.zoom);
			return r;
		}

		@Override
		public boolean contains(double x, double y) {
			float sx = ZoomableContextUtil.pixel2logical_x(
					(float)x,
					this.centeringTransform, this.zoom);
			float sy = ZoomableContextUtil.pixel2logical_y(
					(float)y,
					this.centeringTransform,
					this.zoom);
			return AwtPath.this.contains(sx, sy);
		}

		@Override
		public boolean contains(Point2D p) {
			return contains(p.getX(), p.getY());
		}

		@Override
		public boolean intersects(double x, double y, double w, double h) {
			Rectangle2D r = new Rectangle2D.Float((float)x, (float)y, (float)w, (float)h);
			ZoomableAwtContextUtil.pixel2logical(
					r,
					this.centeringTransform,
					this.zoom);
			return AwtPath.this.intersects(r);
		}

		@Override
		public boolean intersects(Rectangle2D r) {
			Rectangle2D rr = (Rectangle2D)r.clone();
			ZoomableAwtContextUtil.pixel2logical(
					rr,
					this.centeringTransform,
					this.zoom);
			return AwtPath.this.intersects(rr);
		}

		@Override
		public boolean contains(double x, double y, double w, double h) {
			Rectangle2D r = new Rectangle2D.Float((float)x, (float)y, (float)w, (float)h);
			ZoomableAwtContextUtil.pixel2logical(
					r,
					this.centeringTransform,
					this.zoom);
			return AwtPath.this.contains(r);
		}

		@Override
		public boolean contains(Rectangle2D r) {
			Rectangle2D rr = (Rectangle2D)r.clone();
			ZoomableAwtContextUtil.pixel2logical(
					rr,
					this.centeringTransform,
					this.zoom);
			return AwtPath.this.contains(rr);
		}

		@Override
		public PathIterator getPathIterator(AffineTransform at) {
			return new ScreenPathIterator(at, AwtPath.this.getPathIterator(null));
		}

		@Override
		public PathIterator getPathIterator(AffineTransform at, double flatness) {
			float vFlatness = ZoomableContextUtil.pixel2logical_size((float)flatness, this.zoom);
			return new ScreenPathIterator(at, AwtPath.this.getPathIterator(null,vFlatness));
		}

		@Override
		public <T> T getNativeObject(Class<T> type) {
			return type.cast(this);
		}

		@Override
		public VirtualizableShape toScreen(CenteringTransform centeringTransform, float zoom) {
			return this;
		}

		@Override
		public VirtualizableShape fromScreen(CenteringTransform centeringTransform, float zoom) {
			return AwtPath.this;
		}
		
		/**
		 * @author $Author: galland$
		 * @version $FullVersion$
		 * @mavengroupid $GroupId$
		 * @mavenartifactid $ArtifactId$
		 */
		private class ScreenPathIterator implements PathIterator {

			private final AffineTransform tr;
			private final PathIterator pi;
			
			public ScreenPathIterator(AffineTransform tr, PathIterator pi) {
				this.pi = pi;
				this.tr = tr;
			}

			@Override
			public int getWindingRule() {
				return this.pi.getWindingRule();
			}

			@Override
			public boolean isDone() {
				return this.pi.isDone();
			}

			@Override
			public void next() {
				this.pi.next();
			}

			@Override
			public int currentSegment(float[] coords) {
				int t = this.pi.currentSegment(coords);
				int numPts = 0;
				switch(t) {
				case PathIterator.SEG_CUBICTO:
					++numPts;
					coords[4] = ZoomableContextUtil.logical2pixel_x(
							coords[4],
							ScreenPath.this.centeringTransform,
							ScreenPath.this.zoom);
					coords[5] = ZoomableContextUtil.logical2pixel_y(
							coords[5],
							ScreenPath.this.centeringTransform,
							ScreenPath.this.zoom);
					//$FALL-THROUGH$
				case PathIterator.SEG_QUADTO:
					++numPts;
					coords[2] = ZoomableContextUtil.logical2pixel_x(
							coords[2],
							ScreenPath.this.centeringTransform,
							ScreenPath.this.zoom);
					coords[3] = ZoomableContextUtil.logical2pixel_y(
							coords[3],
							ScreenPath.this.centeringTransform,
							ScreenPath.this.zoom);
					//$FALL-THROUGH$
				case PathIterator.SEG_MOVETO:
				case PathIterator.SEG_LINETO:
					++numPts;
					coords[0] = ZoomableContextUtil.logical2pixel_x(
							coords[0],
							ScreenPath.this.centeringTransform,
							ScreenPath.this.zoom);
					coords[1] = ZoomableContextUtil.logical2pixel_y(
							coords[1],
							ScreenPath.this.centeringTransform,
							ScreenPath.this.zoom);
					if (this.tr!=null) {
						this.tr.transform(coords, 0, coords, 0, numPts);
					}
					break;
				case SEG_CLOSE:
				default:
					break;
				}
				return t;
			}

			@Override
			public int currentSegment(double[] coords) {
				int t = this.pi.currentSegment(coords);
				int numPts = 0;
				switch(t) {
				case PathIterator.SEG_CUBICTO:
					++numPts;
					coords[4] = ZoomableContextUtil.logical2pixel_x(
							(float)coords[4],
							ScreenPath.this.centeringTransform,
							ScreenPath.this.zoom);
					coords[5] = ZoomableContextUtil.logical2pixel_y(
							(float)coords[5],
							ScreenPath.this.centeringTransform,
							ScreenPath.this.zoom);
					//$FALL-THROUGH$
				case PathIterator.SEG_QUADTO:
					++numPts;
					coords[2] = ZoomableContextUtil.logical2pixel_x(
							(float)coords[2],
							ScreenPath.this.centeringTransform,
							ScreenPath.this.zoom);
					coords[3] = ZoomableContextUtil.logical2pixel_y(
							(float)coords[3],
							ScreenPath.this.centeringTransform,
							ScreenPath.this.zoom);
					//$FALL-THROUGH$
				case PathIterator.SEG_MOVETO:
				case PathIterator.SEG_LINETO:
					++numPts;
					coords[0] = ZoomableContextUtil.logical2pixel_x(
							(float)coords[0],
							ScreenPath.this.centeringTransform,
							ScreenPath.this.zoom);
					coords[1] = ZoomableContextUtil.logical2pixel_y(
							(float)coords[1],
							ScreenPath.this.centeringTransform,
							ScreenPath.this.zoom);
					if (this.tr!=null) {
						this.tr.transform(coords, 0, coords, 0, numPts);
					}
					break;
				case SEG_CLOSE:
				default:
					break;
				}
				return t;
			}
			
		}
		
	}
	
}