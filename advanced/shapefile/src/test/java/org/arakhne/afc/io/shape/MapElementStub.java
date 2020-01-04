/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.io.shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.text.Encryption;

/** Stub for shape file elements.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
class MapElementStub implements Comparable<MapElementStub> {
	
	/**
	 */
	public final AttributeCollection attrs;

	/**
	 */
	public List<Point2d> points = new ArrayList<>();
	
	private ESRIBounds bounds = null;	
	private String id = null;
	
	/**
	 * @param attrs
	 */
	public MapElementStub(AttributeCollection attrs) {
		this.attrs = attrs;
	}
	
	/**
	 */
	public void random() {
		Random rnd = new Random();
		int count = rnd.nextInt(20);
		ESRIPoint p;
		
		this.bounds = null;
		this.points.clear();
		
		for(int i=0; i<count; ++i) {
			p = new ESRIPoint(
					rnd.nextDouble(), rnd.nextDouble(), 0, 0);
			this.points.add(new Point2d(p.getX(),p.getY()));
			if (this.bounds==null) this.bounds = new ESRIBounds(p);
			else this.bounds.add(p);
		}
	}

	/**
	 * @param x
	 * @param y
	 */
	public void addPoint(double x, double y) {
		this.points.add(new Point2d(x,y));
		this.bounds = null;
		this.id = null;
	}
	
	/**
	 * @return the id
	 */
	public String getID() {
		if (this.id==null) {
			StringBuilder buffer = new StringBuilder();
			for(Point2d p : this.points) {
				buffer.append(Integer.toHexString((int)p.getX()));
				buffer.append(';');
				buffer.append(Integer.toHexString((int)p.getY()));
				buffer.append('|');
			}
			this.id = Encryption.md5(buffer.toString());
		}
		return this.id;
	}

	/**
	 * @return the bounds
	 */
	public ESRIBounds getBounds() {
		if (this.bounds==null && !this.points.isEmpty()) {
			double minx, miny, maxx, maxy;
			minx = miny = Double.POSITIVE_INFINITY;
			maxx = maxy = Double.NEGATIVE_INFINITY;
			for(Point2d pts : this.points) {
				if (pts.getX()<minx) minx = pts.getX();
				if (pts.getX()>maxx) maxx = pts.getX();
				if (pts.getY()<miny) miny = pts.getY();
				if (pts.getY()>maxy) maxy = pts.getY();
			}
			this.bounds = new ESRIBounds(minx,maxx,miny,maxy);
		}
		return this.bounds;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MapElementStub) {
			return getID().equals(((MapElementStub)o).getID());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
        return getID().hashCode();
	}
	
	@Override
	public int compareTo(MapElementStub o) {
		return getID().compareTo(o.getID());
	}
	
}
