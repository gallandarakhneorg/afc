/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
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
package org.arakhne.afc.ui.android.event ;

import java.util.EventObject;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.ui.event.PointerEvent;

import android.view.KeyEvent;
import android.view.MotionEvent;

/** Android implementation of a pointer event.
 *  
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class PointerEventAndroid extends EventObject implements PointerEvent {

	private static final long serialVersionUID = -1281311602355739859L;

	private final MotionEvent motionEvent;
	private long when;
	private float x;
	private float y;
	private boolean consumed = false;
	
	/**
	 * @param source
	 * @param x
	 * @param y
	 * @param me
	 */
	public PointerEventAndroid(Object source, float x, float y, MotionEvent me) {
		super(source);
		this.motionEvent = me;
		this.when = me.getEventTime();
		this.x = x;
		this.y = y;
	}

	@Override
	public int getDeviceId() {
		return this.motionEvent.getDeviceId();
	}
	
	@Override
	public long when() {
		return this.when;
	}
	
	/** Change the timestamp of the event.
	 * 
	 * @param when is the new timestamp.
	 */
	public void setWhen(long when) {
		this.when = when;
	}

	@Override
	public boolean isConsumed() {
		return this.consumed;
	}

	@Override
	public void consume() {
		this.consumed = true;		
	}
	
	/** Mark this event as not consumed.
	 */
	public void unconsume() {
		this.consumed = false;
	}

	@Override
	public boolean isShiftDown() {
		return (this.motionEvent.getMetaState()&KeyEvent.META_SHIFT_ON)!=0;
	}

	@Override
	public boolean isControlDown() {
		return (this.motionEvent.getMetaState()&KeyEvent.META_CTRL_ON)!=0;
	}

	@Override
	public boolean isMetaDown() {
		return (this.motionEvent.getMetaState()&KeyEvent.META_META_ON)!=0;
	}

	@Override
	public boolean isAltDown() {
		return (this.motionEvent.getMetaState()&KeyEvent.META_ALT_ON)!=0;
	}

	@Override
	public boolean isAltGraphDown() {
		return false;
	}

	@Override
	public boolean isContextualActionTriggered() {
		return false;
	}
	
	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return this.y;
	}

	@Override
	public int getButton() {
		return this.motionEvent.getButtonState();
	}

	@Override
	public int getClickCount() {
		return 1;
	}

	@Override
	public float getOrientation() {
		return this.motionEvent.getOrientation();
	}
	
	/** Set the position of the event.
	 * 
	 * @param x
	 * @param y
	 */
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "device="+getDeviceId()+ //$NON-NLS-1$
				"\nx="+getX()+ //$NON-NLS-1$
				"\ny="+getY()+ //$NON-NLS-1$
				"\nbutton="+getButton()+ //$NON-NLS-1$
				"\nkeys=0x"+Integer.toHexString(this.motionEvent.getMetaState())+ //$NON-NLS-1$
				"\norientation="+getOrientation()+ //$NON-NLS-1$
				"\nconsumed="+isConsumed(); //$NON-NLS-1$
	}

	@Override
	public float getXPrecision() {
		return this.motionEvent.getXPrecision();
	}

	@Override
	public float getYPrecision() {
		return this.motionEvent.getYPrecision();
	}

	@Override
	public int getPointerCount() {
		return this.motionEvent.getPointerCount();
	}

	@Override
	public Shape2f getToolArea(int pointerIndex) {
		float x = this.motionEvent.getX(pointerIndex);
		float y = this.motionEvent.getY(pointerIndex);
		float major = this.motionEvent.getToolMajor(pointerIndex);
		float minor = this.motionEvent.getToolMinor(pointerIndex);
		// The circle has a minimal radius of 1 pixel.
		return new Circle2f(x, y,
				Math.max(
						Math.max(major, minor),
						MINIMAL_TOOL_SIZE));
	}

	@Override
	public ToolType getToolType(int pointerIndex) {
		switch(this.motionEvent.getToolType(pointerIndex)) {
		case MotionEvent.TOOL_TYPE_FINGER:
			return ToolType.FINGER;
		case MotionEvent.TOOL_TYPE_STYLUS:
			return ToolType.STYLUS;
		case MotionEvent.TOOL_TYPE_MOUSE:
			return ToolType.MOUSE;
		case MotionEvent.TOOL_TYPE_ERASER:
			return ToolType.ERASER;
		default:
		}
		return ToolType.UNKNOW;
	}

	@Override
	public boolean isToolAreaSupported() {
		return true;
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
