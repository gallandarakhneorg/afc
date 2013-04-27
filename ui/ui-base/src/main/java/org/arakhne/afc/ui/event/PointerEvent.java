/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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
package org.arakhne.afc.ui.event ;

import org.arakhne.afc.math.continous.object2d.Shape2f;


/** Describe the event related to the pointer (mouse or finger).
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PointerEvent extends InputEvent {

	/** Replies the minimal size (in pixels) of the touch area.
	 * The implementation classes must use this
	 * variable, when possible, to initiale the size
	 * of the touch areas.
	 */
	public static final float MINIMAL_TOOL_SIZE = 8;
	
	/** Replies the timestamp of when this event occurred.
	 * 
	 * @return the timestamp of when this event occurred.
	 */
	public long when();

	/** Replies the X position of the pointer.
	 * 
	 * @return the X position of the pointer.
	 */
	public float getX();
	
	/** Replies the Y position of the pointer.
	 * 
	 * @return the Y position of the pointer.
	 */
	public float getY();

	/**
     * Returns the states of the buttons or the number
     * of the button that is the cause of the event.
     * <p>
     * The semantic if the replied value depends on
     * the background platform. For Swing, it
     * is the number of the button. For Android,
     * it is the states of the buttons.
     *
     * @return the number of the button, or {@code 0} for none.
     */
    public int getButton();
    
    /** Replies the number of clicks.
     * 
     * @return the number of clicks.
     */
    public int getClickCount();
    
	/**
     * Returns the orientation of the pointer.
     * Returns the orientation of the touch area and 
     * tool area in radians clockwise from vertical 
     * for the pointer. An angle of 0 radians 
     * indicates that the major axis of contact is oriented 
     * upwards, is perfectly circular or is of unknown 
     * orientation. A positive angle indicates that the 
     * major axis of contact is oriented to the right. 
     * A negative angle indicates that the major axis 
     * of contact is oriented to the left. The full 
     * range is from -PI/2 radians (finger pointing fully left) 
     * to PI/2 radians (finger pointing fully right).
     *
     * @return the orientation (in radians) of the pointer.
     */
    public float getOrientation();
    
	/** Replies the precision of the X position of the pointer.
	 * <p>
	 * On several devices, the X position is approximate (on
	 * tactil devices for example). This precision permits to
	 * estimate the hardware X coordinate.
	 * 
	 * @return the precision X position of the pointer.
	 */
	public float getXPrecision();
	
	/** Replies the precision of the Y position of the pointer.
	 * <p>
	 * On several devices, the Y position is approximate (on
	 * tactil devices for example). This precision permits to
	 * estimate the hardware Y coordinate.
	 * 
	 * @return the Y position of the pointer.
	 */
	public float getYPrecision();
	
	/** Replies the number of pointers concerned by this event.
	 * 
	 * @return the number of pointers.
	 */
	public int getPointerCount();
	
	/** Returns a shape that describes the size of the approaching 
	 * tool for the given pointer index. The tool area represents 
	 * the estimated size of the finger or pen that is touching the 
	 * device independent of its actual touch area at the point of contact.
	 * 
	 * @param pointerIndex is the index of the pointer for which to retreive
	 * the tool area.
	 * @return the area covered by the tool.
	 * @see #isToolAreaSupported()
	 */
	public Shape2f getToolArea(int pointerIndex);

	/** Returns if the background API is able to reply the
	 * area covered by the pointer mean (finger, pen...).
	 * This function permits to determine if the value replied
	 * by {@link #getToolArea(int)} has a valid meaning or
	 * is only a work-around.
	 * 
	 * @return <code>true</code> if the background API could compute
	 * the area covered by the pointer, <code>false</code> otherwise.
	 */
	public boolean isToolAreaSupported();

	/** Returns the type of tool detected for the given pointer.
	 * 
	 * @param pointerIndex is the index of the pointer for which to retreive
	 * the tool type.
	 * @return the type of tool.
	 */
	public ToolType getToolType(int pointerIndex);

	/** Replies if the pointer tool area is intersecting
	 * the given shape.
	 * 
	 * @param s is the shape to test
	 * @return <code>true</code> if the pointer area intersect
	 * the given shape; <code>false</code> otherwise.
	 * @see #getToolArea(int)
	 * @see #inside(Shape2f)
	 */
	public boolean intersects(Shape2f s);
	
	/** Types of tools.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public enum ToolType {
		
		/** Unknown. */
		UNKNOW,
		/** Finger. */
		FINGER,
		/** Stylus. */
		STYLUS,
		/** Eraser. */
		ERASER,
		/** Mouse. */
		MOUSE;
		
	}
	
}
