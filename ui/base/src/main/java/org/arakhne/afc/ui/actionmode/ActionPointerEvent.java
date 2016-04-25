/* 
 * $Id$
 * 
 * Copyright (C) 2002 Stephane GALLAND, Madhi HANNOUN, Marc BAUMGARTNER.
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
