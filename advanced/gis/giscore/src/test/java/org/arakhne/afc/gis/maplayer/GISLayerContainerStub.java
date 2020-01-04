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

package org.arakhne.afc.gis.maplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.primitive.ChangeListener;
import org.arakhne.afc.gis.primitive.GISElement;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.util.InformedArrayList;
import org.arakhne.afc.vmutil.json.JsonBuffer;

@SuppressWarnings("all")
class GISLayerContainerStub extends AbstractAttributeCollectionStub implements GISLayerContainer<MapLayer> {

	private static final long serialVersionUID = 5855772374121264878L;

	private final InformedArrayList<MapLayer> list = new InformedArrayList<>();

	private double zoomFactor, shiftX, shiftY;

	/**
	 */
	public GISLayerContainerStub() {
		Random rnd = new Random();
		this.zoomFactor = rnd.nextDouble()+1e-13; // avoid 0
		this.shiftX = rnd.nextDouble();
		this.shiftY = rnd.nextDouble();
	}

    @Override
	public int size() {
		return this.list.size();
	}

    @Override
	public void addLayerListener(MapLayerListener listener) {
		//
	}

    @Override
	public void removeLayerListener(MapLayerListener listener) {
		//
	}

    @Override
	public boolean addMapLayer(MapLayer layer) {
		if (this.list.add(layer)) {
			layer.setContainer(this);
		}
		return false;
	}

    @Override
	public void addMapLayer(int index, MapLayer layer) {
		this.list.add(index, layer);
		layer.setContainer(this);
	}

    @Override
	public void clear() {
		for (MapLayer layer : this.list) {
			layer.setContainer(null);
		}
		this.list.clear();
	}

    @Override
	public List<MapLayer> getAllMapLayers() {
		return new ArrayList<>(this.list);
	}

    @Override
	public MapLayer getMapLayerAt(int index) {
		return this.list.get(index);
	}

    @Override
	public int indexOf(MapLayer layer) {
		return this.list.indexOf(layer);
	}

    @Override
	public boolean moveLayerDown(MapLayer layer) {
		return moveLayerDown(indexOf(layer));
	}

    @Override
	public boolean moveLayerDown(int index) {
		if (index>1) {
			MapLayer l1 = this.list.get(index-1);
			MapLayer l2 = this.list.get(index);
			this.list.set(index-1, l2);
			this.list.set(index, l1);
			return true;
		}
		return false;
	}

    @Override
	public boolean moveLayerUp(MapLayer layer) {
		return moveLayerUp(indexOf(layer));
	}

    @Override
	public boolean moveLayerUp(int index) {
		if ((index>=0)&&(index<(getMapLayerCount()-1))) {
			MapLayer l1 = this.list.get(index+1);
			MapLayer l2 = this.list.get(index);
			this.list.set(index+1, l2);
			this.list.set(index, l1);
			return true;
		}
		return false;
	}

    @Override
	public boolean isBottomLayer(MapLayer layer) {
		if (this.list.isEmpty()) return false;
		return this.list.get(0)==layer;
	}

    @Override
	public boolean isTopLayer(MapLayer layer) {
		if (this.list.isEmpty()) return false;
		return this.list.get(this.list.size()-1)==layer;
	}

    @Override
	public boolean removeMapLayer(MapLayer layer) {
		return removeMapLayerAt(indexOf(layer)) != null;
	}

    @Override
	public MapLayer removeMapLayerAt(int index) {
		if ((index>=0)&&(index<getMapLayerCount())) {
			return this.list.remove(index);
		}
		return null;
	}

    @Override
	public int getMapLayerCount() {
		return this.list.size();
	}

    @Override
	public Rectangle2d getBoundingBox() {
    	Rectangle2d r = new Rectangle2d();
    	boolean first = true;
		for(MapLayer layer : this.list) {
			Rectangle2d r2 = layer.getBoundingBox();
			if (r2!=null) {
				if (first) {
					first = false;
					r.set(r2);
				} else {
					r.setUnion(r2);
				}
			}
		}
		return first ? null : r;
	}

    @Override
	public int getColor() {
		return 0xFFFFFF;
	}

	public int getSelectionColor() {
		return 0xFF0000;
	}

	public void setRandomZoomFactor() {
		this.zoomFactor = new Random().nextDouble()+1e-13; // avoid 0
	}

	public double getZoomFactor() {
		return this.zoomFactor;
	}

	public void setRandomShifts() {
		Random rnd = new Random();
		this.shiftX = rnd.nextDouble();
		this.shiftY = rnd.nextDouble();
	}

	public double getShiftX() {
		return this.shiftX;
	}

	public double getShiftY() {
		return this.shiftY;
	}

	public Rectangle2d logical2pixel(Rectangle2d r_logic) {
		if (r_logic==null) return null;
		return new Rectangle2d(
				logical2pixel_x(r_logic.getMinX()),
				logical2pixel_y(r_logic.getMinY()),
				logical2pixel_size(r_logic.getWidth()),
				logical2pixel_size(r_logic.getHeight()));
	}

	/**
	 *
	 * @param geo_size
	 * @return a size
	 */
	public double logical2pixel_size(double geo_size) {
		return geo_size * this.zoomFactor;
	}

	/**
	 *
	 * @param geo_x
	 * @return x
	 */
	public double logical2pixel_x(double geo_x) {
		return geo_x * this.zoomFactor + this.shiftX;
	}

	/**
	 *
	 * @param geo_y
	 * @return y
	 */
	public double logical2pixel_y(double geo_y) {
		return geo_y * this.zoomFactor + this.shiftY;
	}

	/**
	 *
	 * @param r_screen
	 * @return a rectangle
	 */
	public Rectangle2d pixel2logical(Rectangle2d r_screen) {
		if (r_screen==null) return null;
		return new Rectangle2d(
				pixel2logical_x(r_screen.getMinX()),
				pixel2logical_y(r_screen.getMinY()),
				pixel2logical_size(r_screen.getWidth()),
				pixel2logical_size(r_screen.getHeight()));
	}

	/**
	 *
	 * @param pixel_size
	 * @return a size
	 */
	public double pixel2logical_size(double pixel_size) {
		return pixel_size / this.zoomFactor;
	}

	/**
	 *
	 * @param pixel_x
	 * @return x
	 */
	public double pixel2logical_x(double pixel_x) {
		return (pixel_x - this.shiftX) / this.zoomFactor;
	}

	/**
	 *
	 * @param pixel_y
	 * @return y
	 */
	public double pixel2logical_y(double pixel_y) {
		return (pixel_y - this.shiftY) / this.zoomFactor;
	}

    /** {@inheritDoc}
     */
    @Override
	public void resetBoundingBox() {
		//
	}

    /** {@inheritDoc}
     */
    @Override
	public Iterator<MapLayer> iterator() {
		return this.list.iterator();
	}

    /** {@inheritDoc}
     */
    @Override
	public Iterator<MapLayer> getTopDownIterator() {
		return this.list.iterator();
	}

    /** {@inheritDoc}
     */
    @Override
	public Iterator<MapLayer> reverseIterator() {
		ArrayList<MapLayer> r = new ArrayList<>(this.list);
		Collections.reverse(r);
		return r.iterator();
	}

    /** {@inheritDoc}
     */
    @Override
	public Iterator<MapLayer> getBottomUpIterator() {
		ArrayList<MapLayer> r = new ArrayList<>(this.list);
		Collections.reverse(r);
		return r.iterator();
	}

	/**
	 * @param container
	 * @return count of copied attributes
	 */
	public int copyAttributes(GISElement container) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return geo identifier
	 */
	public String getGeoId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return geo location
	 */
	public GeoLocation getGeoLocation() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return name
	 */
	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return uid
	 */
	public String getUid() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return hash key
	 */
	public String hashKey() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

    /** {@inheritDoc}
     */
    @Override
	public boolean renameAttribute(String oldname, String newname, boolean overwrite) {
		throw new UnsupportedOperationException();
	}


	/**
	 * @param container
	 */
	public void setContainer(GISLayerContainer<?> container) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Rectangle2d getVisibleBoundingBox() {
		return getBoundingBox();
	}

    /** {@inheritDoc}
     */
    @Override
	public void fireLayerAttributeChangedEvent(MapLayerAttributeChangeEvent event) {
		//
	}

    /** {@inheritDoc}
     */
    @Override
	public void fireLayerContentChangedEvent(MapLayerContentEvent event) {
		//
	}

    /** {@inheritDoc}
     */
    @Override
	public void fireLayerHierarchyChangedEvent(MapLayerHierarchyEvent event) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEventFirable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEventFirable(boolean isFirable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttributes(Map<String, Object> content) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttributes(AttributeProvider content) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addAttributes(Map<String, Object> content) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addAttributes(AttributeProvider content) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void toMap(Map<String, Object> mapToFill) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<? extends MapLayer> getElementType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void bindChangeListener(ChangeListener listener) {
		throw new UnsupportedOperationException();
	}

}
