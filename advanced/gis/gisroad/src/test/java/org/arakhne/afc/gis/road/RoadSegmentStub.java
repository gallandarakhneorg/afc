/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.gis.road;

import java.util.ArrayList;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** Stub for RoadSegment.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class RoadSegmentStub extends RoadPolyline {

	private static final long serialVersionUID = 2694567030815887510L;

	private final String label;
	private Rectangle2d theoriticalBounds;
	private double theoriticalLength;
	private final ArrayList<Point2d> points = new ArrayList<>();
	private int[] groupIdx;

	/**
	 * @param label
	 */
	public RoadSegmentStub(String label) {
		this(label, false);
	}

	/**
	 * @param label
	 * @param allowGroups
	 */
	public RoadSegmentStub(String label, boolean allowGroups) {
		super(new HeapAttributeCollection());

		this.label = label;

		int groupCount = allowGroups ? (int)(Math.random() * 10.)+1 : 1;
		this.groupIdx = new int[groupCount];

		double length=0;
		double minX=0, maxX=0, minY=0, maxY=0;

		for(int idxGrp=0; idxGrp<groupCount; ++idxGrp) {
			int pointCount = (int)(Math.random() * 50.)+2;

			this.groupIdx[idxGrp] = this.points.size();
			double ox=0,oy=0;

			for(int i=0; i<pointCount; ++i) {
				double x = Math.random() * 1000. - 500.;
				double y = Math.random() * 1000. - 500.;
				Point2d pts = new Point2d(x,y);
				if (i==0)
					addGroup(pts);
				else
					addPoint(pts);
				this.points.add(pts);
				if ((i==0)||(x<minX)) minX = x;
				if ((i==0)||(x>maxX)) maxX = x;
				if ((i==0)||(y<minY)) minY = y;
				if ((i==0)||(y>maxY)) maxY = y;
				if (i>0) {
					length += Point2D.getDistancePointPoint(ox, oy, x, y);
				}
				ox = x;
				oy = y;
			}
		}

		this.theoriticalBounds = new Rectangle2d(minX, minY, maxX-minX, maxY-minY);
		this.theoriticalLength = length;
	}

	/**
	 * @param label
	 * @param startX
	 * @param startY
	 */
	public RoadSegmentStub(String label, double startX, double startY) {
		super(new HeapAttributeCollection());

		this.label = label;

		this.groupIdx = new int[] { 0 };
		Point2d pts = new Point2d(startX,startY);
		addPoint(pts);
		this.points.add(pts);
		pts = new Point2d(0,0);
		addPoint(pts);
		this.points.add(pts);

		this.theoriticalBounds = new Rectangle2d(0,0,0,0);
		this.theoriticalBounds.setFromCorners(startX,startY,startX,startY);
		this.theoriticalLength = 0;
	}

	/**
	 * @param label
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	public RoadSegmentStub(String label, double startX, double startY, double endX, double endY) {
		super(new HeapAttributeCollection());

		this.label = label;

		this.groupIdx = new int[] { 0 };
		Point2d pts = new Point2d(startX,startY);
		addPoint(pts);
		this.points.add(pts);
		pts = new Point2d(endX,endY);
		addPoint(pts);
		this.points.add(pts);

		this.theoriticalBounds = new Rectangle2d();
		this.theoriticalBounds.setFromCorners(startX,startY,endX,endY);
		this.theoriticalLength = Point2D.getDistancePointPoint(startX, startY, endX, endY);
	}

	/**
	 * @param label
	 * @param points
	 */
	public RoadSegmentStub(String label, double[] points) {
		super(new HeapAttributeCollection());

		assert(points.length%2==0);

		this.label = label;

		double minX=0, maxX=0, minY=0, maxY=0;
		double length = 0;
		double x=0,y=0;

		this.groupIdx = new int[] { 0 };

		for(int i=0; i<points.length; i+=2) {
			Point2d pts = new Point2d(points[i],points[i+1]);
			addPoint(pts);
			this.points.add(pts);
			if ((i==0)||(points[i]<minX)) minX = points[i];
			if ((i==0)||(points[i]>maxX)) maxX = points[i];
			if ((i==0)||(points[i+1]<minY)) minY = points[i+1];
			if ((i==0)||(points[i+1]>maxY)) maxY = points[i+1];
			if (i>0) {
				length += Point2D.getDistancePointPoint(x,y,points[i],points[i+1]);
			}
			x = points[i];
			y=  points[i+1];
		}

		this.theoriticalBounds = new Rectangle2d(minX,minY,maxX-minX,maxY-minY);
		this.theoriticalLength = length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void setStartPoint(StandardRoadConnection point) {
		super.setStartPoint(point);
		if (point!=null) {
			this.points.set(0, point.getPoint());
			computeLengthBounds();
		}
	}

	@Override
	void setEndPoint(StandardRoadConnection point) {
		super.setEndPoint(point);
		if (point!=null) {
			this.points.set(this.points.size()-1, point.getPoint());
			computeLengthBounds();
		}
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		buffer.add("label", this.label); //$NON-NLS-1$
	}

	/**
	 * @return the original bounds
	 */
	public Rectangle2d getOriginalBounds() {
		return this.theoriticalBounds;
	}

	/**
	 * @return the original length
	 */
	public double getOriginalLength() {
		return this.theoriticalLength;
	}

	/**
	 * @return the original antepenulvian point
	 */
	public Point2d getOriginalAntepenulvian() {
		return this.points.get(this.points.size()-2);
	}

	/**
	 * @param ratio
	 * @return the original start distance
	 */
	public double getOriginalStartDistance(double ratio) {
		assert(ratio>=0. && ratio<=1.);
		return ratio*this.theoriticalLength;
	}

	/**
	 * @param ratio
	 * @return the original end distance
	 */
	public double getOriginalEndDistance(double ratio) {
		assert(ratio>=0. && ratio<=1.);
		return this.theoriticalLength - ratio*this.theoriticalLength;
	}

	/**
	 * @return the original count of points
	 */
	public int getOriginalPointCount() {
		return this.points.size();
	}

	/**
	 * @param index
	 * @return the original point
	 */
	public Point2d getOriginalPointAt(int index) {
		return this.points.get(index);
	}

	/**
	 * @param ratio
	 * @return the original point
	 */
	public Point2d getOriginalPointFor(double ratio) {
		double length = getOriginalLength();
		double desiredLength = length * ratio;
		double currentLength = 0;

		for(int idx=0; idx<this.groupIdx.length; ++idx) {
			int startIdx = this.groupIdx[idx];
			int finalIdx = (idx<this.groupIdx.length-1) ? this.groupIdx[idx+1] : this.points.size();

			for(int i=startIdx; i<finalIdx-1; ++i) {
				Point2d pt1 = this.points.get(i+1);
				Point2d pt2 = this.points.get(i);
				double distance = pt2.getDistance(pt1);
				if (currentLength+distance>=desiredLength) {
					desiredLength -= currentLength;
					Vector2d v = new Vector2d(pt1.getX()-pt2.getX(),pt1.getY()-pt2.getY());
					v.scale(desiredLength / distance);
					return new Point2d(
							pt2.getX()+v.getX(),
							pt2.getY()+v.getY());
				}
				currentLength += distance;
			}

		}

		return null;
	}

	private void computeLengthBounds() {
		double length = 0;
		double xmin, xmax, ymin, ymax;

		xmin = xmax = ymin = ymax = 0;

		for(int idx=0; idx<this.groupIdx.length; ++idx) {
			int startIdx = this.groupIdx[idx];
			int finalIdx = (idx<this.groupIdx.length-1) ? this.groupIdx[idx+1] : this.points.size();

			for(int i=startIdx; i<finalIdx-1; ++i) {

				Point2d pt1 = this.points.get(i+1);
				Point2d pt2 = this.points.get(i);

				if ((idx==0)&&(i==startIdx)) {
					if (pt2.getX()<pt1.getX()) {
						xmin = pt2.getX();
						xmax = pt1.getX();
					}
					else {
						xmin = pt1.getX();
						xmax = pt2.getX();
					}
					if (pt2.getY()<pt1.getY()) {
						ymin = pt2.getY();
						ymax = pt1.getY();
					}
					else {
						ymin = pt1.getY();
						ymax = pt2.getY();
					}
				}
				else if (i==startIdx) {
					if (pt2.getX()<xmin) xmin = pt2.getX();
					if (pt2.getX()>xmax) xmax = pt2.getX();
					if (pt2.getY()<ymin) ymin = pt2.getY();
					if (pt2.getY()>ymax) ymax = pt2.getY();
				}

				if (pt1.getX()<xmin) xmin = pt1.getX();
				if (pt1.getX()>xmax) xmax = pt1.getX();
				if (pt1.getY()<ymin) ymin = pt1.getY();
				if (pt1.getY()>ymax) ymax = pt1.getY();

				double distance = pt2.getDistance(pt1);
				length += distance;
			}

		}

		this.theoriticalLength = length;
		this.theoriticalBounds = new Rectangle2d(xmin,ymin,xmax-xmin,ymax-ymin);

	}

	/**
	 * @param indexPoint
	 * @return the original vector
	 */
	public Vector2d getOriginalVectorAt(int indexPoint) {
		Point2d p0 = this.points.get(indexPoint);
		Point2d p1 = this.points.get(indexPoint+1);
		return new Vector2d(
				p1.getX() - p0.getX(),
				p1.getY() - p0.getY());
	}

	/**
	 * @param ratio
	 * @return the original vector
	 */
	public Vector2d getOriginalVectorFor(double ratio) {
		double length = getOriginalLength();
		double desiredLength = length * ratio;
		double currentLength = 0;

		for(int idx=0; idx<this.groupIdx.length; ++idx) {
			int startIdx = this.groupIdx[idx];
			int finalIdx = (idx<this.groupIdx.length-1) ? this.groupIdx[idx+1] : this.points.size();

			for(int i=startIdx; i<finalIdx-1; ++i) {
				Point2d pt1 = this.points.get(i+1);
				Point2d pt2 = this.points.get(i);
				double distance = pt2.getDistance(pt1);
				if (currentLength+distance>=desiredLength) {
					final Vector2d v = new Vector2d(pt1.getX()-pt2.getX(),pt1.getY()-pt2.getY());
					v.normalize();
					return v;
				}
				currentLength += distance;
			}

		}

		return null;
	}

}
