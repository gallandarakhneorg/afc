/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.ui.actionmode ;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.ui.ZoomableContext;
import org.arakhne.afc.ui.event.PointerEvent;

/** Implementation of a pointer event for action modes.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class ActionPointerEvent implements PointerEvent {

	private static final long serialVersionUID = -6024600428453074472L;
	
	private final PointerEvent originalEvent;
	private final ZoomableContext zoomContext;
	private float x = Float.NaN;
	private float y = Float.NaN;
	private float xPrecision = Float.NaN;
	private float yPrecision = Float.NaN;
	
	/**
	 * @param original
	 * @param zoomContext
	 */
	public ActionPointerEvent(PointerEvent original, ZoomableContext zoomContext) {
		this.originalEvent = original;
		this.zoomContext = zoomContext;
	}
	
	/** Replies the original event that is embedded in this action-mode event.
	 * 
	 * @return the original pointer event.
	 */
	public PointerEvent getOriginalPointerEvent() {
		return this.originalEvent;
	}
	
	/** Replies the zoomable context in which the event occurs.
	 * 
	 * @return the zoomable context, or <code>null</code> if none.
	 */
	public ZoomableContext getZoomableContext() {
		return this.zoomContext;
	}

	@Override
	public int getDeviceId() {
		return this.originalEvent.getDeviceId();
	}

	@Override
	public boolean isConsumed() {
		return this.originalEvent.isConsumed();
	}

	@Override
	public void consume() {
		this.originalEvent.consume();
	}

	@Override
	public boolean isShiftDown() {
		return this.originalEvent.isShiftDown();
	}

	@Override
	public boolean isControlDown() {
		return this.originalEvent.isControlDown();
	}

	@Override
	public boolean isMetaDown() {
		return this.originalEvent.isMetaDown();
	}

	@Override
	public boolean isAltDown() {
		return this.originalEvent.isAltDown();
	}

	@Override
	public boolean isAltGraphDown() {
		return this.originalEvent.isAltGraphDown();
	}

	@Override
	public boolean isContextualActionTriggered() {
		return this.originalEvent.isContextualActionTriggered();
	}

	@Override
	public long when() {
		return this.originalEvent.when();
	}

	@Override
	public float getX() {
		if (Float.isNaN(this.x)) {
			if (this.zoomContext==null)
				this.x = this.originalEvent.getX();
			else
				this.x = this.zoomContext.pixel2logical_x(this.originalEvent.getX());
		}
		return this.x;
	}
	
	/** Replies the position of the pointer.
	 * 
	 * @return the position of the pointer, never <code>null</code>.
	 */
	public Point2D getPosition() {
		return new Point2f(getX(), getY());
	}

	@Override
	public float getY() {
		if (Float.isNaN(this.y)) {
			if (this.zoomContext==null)
				this.y = this.originalEvent.getX();
			else
				this.y = this.zoomContext.pixel2logical_y(this.originalEvent.getY());
		}
		return this.y;
	}

	@Override
	public int getButton() {
		return this.originalEvent.getButton();
	}

	@Override
	public int getClickCount() {
		return this.originalEvent.getClickCount();
	}

	@Override
	public float getOrientation() {
		return this.originalEvent.getOrientation();
	}

	@Override
	public float getXPrecision() {
		if (Float.isNaN(this.xPrecision)) {
			if (this.zoomContext==null)
				this.xPrecision = this.originalEvent.getXPrecision();
			else
				this.xPrecision = this.zoomContext.pixel2logical_size(this.originalEvent.getXPrecision());
		}
		return this.xPrecision;
	}

	@Override
	public float getYPrecision() {
		if (Float.isNaN(this.yPrecision)) {
			if (this.zoomContext==null)
				this.yPrecision = this.originalEvent.getYPrecision();
			else
				this.yPrecision = this.zoomContext.pixel2logical_size(this.originalEvent.getYPrecision());
		}
		return this.yPrecision;
	}

	@Override
	public int getPointerCount() {
		return this.originalEvent.getPointerCount();
	}

	@Override
	public Shape2f getToolArea(int pointerIndex) {
		if (this.zoomContext==null)
			return this.originalEvent.getToolArea(pointerIndex);
		return this.zoomContext.pixel2logical(this.originalEvent.getToolArea(pointerIndex));
	}

	@Override
	public ToolType getToolType(int pointerIndex) {
		return this.originalEvent.getToolType(pointerIndex);
	}

	@Override
	public boolean isToolAreaSupported() {
		return this.originalEvent.isToolAreaSupported();
	}

	@Override
	public boolean intersects(Shape2f s) {
		for(int i=0; i<getPointerCount(); ++i) {
			if (s.intersects(getToolArea(i).getPathIterator())) {
				return true;
			}
		}
		return false;
	}

}
