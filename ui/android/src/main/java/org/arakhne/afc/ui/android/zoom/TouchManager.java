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
package org.arakhne.afc.ui.android.zoom;

import java.lang.ref.WeakReference;

import org.arakhne.afc.ui.android.event.PointerEventAndroid;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

/** This class provides a general behavior to support touch events.
 * <p>
 * This manager tries to detected scaling, move and click gestures and
 * avoid to generate unecessary events.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
class TouchManager {

	/** Constant that is the identifier representing of an invalid pointer
	 * on the droid device.
	 */
	public static final int INVALID_POINTER_ID = -1;

	/** Scale gesture detector.
	 */
	private final ScaleGestureDetector scaleGestureDetector;

	/** Manager of the scaling events.
	 */
	private final ScaleGestureManager scaleGestureManager;

	/** Move gesture detector.
	 */
	private final MoveGestureManager moveGestureDetector;

	/** Listener on clicks.
	 */
	private final ClickListener clickListener;
	
	/** View managed by this touch manager.
	 */
	private final WeakReference<ZoomableView> view;
	
	/** Indicates if a special gesture was activated.
	 */
	private boolean isSpecialGestureActivated = false;

	/**
	 * @param view is the view associated to this manager.
	 */
	public TouchManager(ZoomableView view) {
		assert(view!=null);
		this.view = new WeakReference<>(view);
		this.scaleGestureManager = new ScaleGestureManager();
		this.scaleGestureDetector = new ScaleGestureDetector(view.getContext(), this.scaleGestureManager);
		this.moveGestureDetector = new MoveGestureManager();
		this.clickListener = new ClickListener();
	}
	
	/** Initialize the manager.
	 * <p>
	 * This function must be invoked just after the construction and
	 * before any Android event.
	 */
	public void init() {
		ZoomableView view = getView();
		view.setClickable(true);
		view.setLongClickable(true);
		view.setOnLongClickListener(this.clickListener);
		view.setOnClickListener(this.clickListener);
	}
	
	/** Invoked to treat a touch event.
	 * 
	 * @param ev
	 * @return a flag with {@code 0x1} if the event is consumed,
	 * {@code 0x2} if the View's touchEvent function must be invoked. 
	 */
	public byte onTouchEvent(MotionEvent ev) {
		ZoomableView view = getView();
		byte consumed = 0;
		// A disabled view that is clickable still consumes the touch
		// events, it just doesn't respond to them.
		if (!view.isEnabled()) {
			this.isSpecialGestureActivated = false;
			if (view.isClickable() || view.isLongClickable()) {
				consumed |= 1;
			}
		}
		else {
			int action = ev.getAction() & MotionEvent.ACTION_MASK;
			// Reset the special gesture flag when touch down
			if (action==MotionEvent.ACTION_DOWN) {
				this.isSpecialGestureActivated = false;
			}
			
			// Let the ScaleGestureDetector inspect all events.
			if (this.scaleGestureDetector.onTouchEvent(ev)) {
				consumed |= 1;
			}
	
			// Let the move gesture detector inspect all events
			if (this.moveGestureDetector.onTouchEvent(ev,
					!(this.isSpecialGestureActivated || this.scaleGestureDetector.isInProgress()),
					this.scaleGestureDetector.isInProgress(),
					this.scaleGestureDetector.getFocusX(),
					this.scaleGestureDetector.getFocusY())) {
				consumed |= 1;
			}
			
			//logTouchEvent(ev.getAction());
			
			// Let the inherited function inspect all events for click and long click events.
			if (!this.scaleGestureDetector.isInProgress()
				&& !this.moveGestureDetector.isInProgress()) {
				if (action==MotionEvent.ACTION_POINTER_UP &&
					!this.isSpecialGestureActivated) {
					consumed |= 2;
				}
			}
			else {
				this.isSpecialGestureActivated = true;
			}
		}
				
		return consumed;
	}
	
	/*private void logTouchEvent(int action) {
		String n;
		switch(action) {
		case MotionEvent.ACTION_CANCEL:
			n = "CANCEL"; break;
		case MotionEvent.ACTION_DOWN:
			n = "DOWN"; break;
		case MotionEvent.ACTION_MOVE:
			n = "MOVE"; break;
		case MotionEvent.ACTION_POINTER_DOWN:
			n = "PDOWN"; break;
		case MotionEvent.ACTION_POINTER_UP:
			n = "PUP"; break;
		case MotionEvent.ACTION_SCROLL:
			n = "SCROLL"; break;
		case MotionEvent.ACTION_UP:
			n = "UP"; break;
		default:
			n = "???"; break;
		}
		Log.d("TM", "type="+n+"; isSpecialTouch="+this.isSpecialGestureActivated
				+"; isScale="+this.scaleGestureDetector.isInProgress()
				+"; isMove="+this.moveGestureDetector.isInProgress());
	}*/

	/** Replies the view associated to this manager.
	 * 
	 * @return the view.
	 */
	protected ZoomableView getView() {
		return this.view.get();
	}
	
	/** Change the listener on long click.
	 *
	 * @param l is the new listener
	 * @return the listener that must be used by the view.
	 */
	public final OnLongClickListener setOnLongClickListener(OnLongClickListener l) {
		if (l!=this.clickListener) this.clickListener.onLongClickListener = l;
		return this.clickListener;
	}

	/** Change the listener on click.
	 *
	 * @param l is the new listener
	 * @return the listener that must be used by the view.
	 */
	public final OnClickListener setOnClickListener(OnClickListener l) {
		if (l!=this.clickListener) this.clickListener.onClickListener = l;
		return this.clickListener;
	}

	/** This class provides listening functions on the View click and
	 * long click events.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class ClickListener implements OnLongClickListener, OnClickListener {

		public OnLongClickListener onLongClickListener = null;
		public OnClickListener onClickListener = null;
		private boolean isClickEnabled = true;

		/**
		 */
		public ClickListener() {
			//
		}

		/** Reset the internal flags of this click listener.
		 */
		public void reset() {
			this.isClickEnabled = true;
		}

		@SuppressWarnings("synthetic-access")
		@Override
		public boolean onLongClick(View v) {
			boolean b = false;
			this.isClickEnabled = false;
			if (getView().isLongClickable()
					&& !TouchManager.this.scaleGestureDetector.isInProgress()
					&& !TouchManager.this.moveGestureDetector.isInProgress()) {
				if (this.onLongClickListener!=null) {
					b = this.onLongClickListener.onLongClick(v);
				}
				if (!b) {
					PointerEventAndroid e = TouchManager.this.moveGestureDetector.lastPointerEvent;
					if (e!=null) {
						e.unconsume();
						e.setWhen(System.currentTimeMillis());
						getView().onLongClick(e);
						b = e.isConsumed();
					}
				}
			}
			return b;
		}

		@SuppressWarnings("synthetic-access")
		@Override
		public void onClick(View v) {
			if (getView().isClickable()
					&& !TouchManager.this.scaleGestureDetector.isInProgress()
					&& !TouchManager.this.moveGestureDetector.isInProgress()) {
				if (this.isClickEnabled) {
					if (this.onClickListener!=null) {
						this.onClickListener.onClick(v);
					}
					PointerEventAndroid e = TouchManager.this.moveGestureDetector.lastPointerEvent;
					if (e!=null) {
						e.unconsume();
						e.setWhen(System.currentTimeMillis());
						getView().onClick(e);
					}
				}
			}
			this.isClickEnabled = true;
		}

	} // class ClickListener

	/** This class detects scale gesting.
	 * It is based on the standard Android scaling gesture detector.
	 * <p>
	 * It invoked {@link TouchManager#onScale(float, float, float)} when
	 * a scaling gesture is detected.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @see ScaleGestureDetector
	 */
	private class ScaleGestureManager extends ScaleGestureDetector.SimpleOnScaleGestureListener {

		/**
		 */
		public ScaleGestureManager() {
			//
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			ZoomableView v = getView();
			if (v.onScale(
					detector.getFocusX(), detector.getFocusY(),
					detector.getScaleFactor())) {
				v.repaint();
			}
			return true;
		}

	} // class ScaleGestureManager

	/** This class detects move gestures.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class MoveGestureManager {

		/** Coordinate of the last touch.
		 */
		private float lastTouchX = Float.NaN;

		/** Coordinate of the last touch.
		 */
		private float lastTouchY = Float.NaN;

		/** Indicates if the move gesture is activated (by a touch down for example).
		 */
		private boolean isActivated = false;

		/** Indicates if the move gesture is in progress, ie.
		 * touch down and move events were found.
		 */
		private boolean inProgress = false;

		/** Identifier of the current active pointer.
		 */
		private int activePointerId = INVALID_POINTER_ID;

		public PointerEventAndroid lastPointerEvent = null;

		/**
		 */
		public MoveGestureManager() {
			//
		}

		/** Replies if a move gesture is in progress.
		 * 
		 * @return <code>true</code> if a move gesture is in progress;
		 * otherwise <code>false</code>.
		 */
		public boolean isInProgress() {
			return this.inProgress;
		}

		/** Invoked for detecting touch gestures.
		 * 
		 * @param ev
		 * @param canInvokeViewCallbacks indicates if this manager is able to invoke the view callbacks.
		 * @param isScaleGestureInProgression indicates if the scale gesture is under progress.
		 * @param scaleGestureX is the X coordinate of the scale gesture, if under progress; otherwise the value is indetermined.
		 * @param scaleGestureY is the Y coordinate of the scale gesture, if under progress; otherwise the value is indetermined.
		 * @return <code>true</code> if the event is consumed;
		 * <code>false</code> otherwise.
		 */
		@SuppressWarnings("synthetic-access")
		public boolean onTouchEvent(MotionEvent ev,
				boolean canInvokeViewCallbacks,
				boolean isScaleGestureInProgression,
				float scaleGestureX,
				float scaleGestureY) {
			int pointerIndex, pointerId;
			float x, y;
			int action = ev.getActionMasked();
			
			ZoomableView view = getView();

			if (isScaleGestureInProgression) {
				x = scaleGestureX;
				y = scaleGestureY;
			}
			else {
				x = ev.getX();
				y = ev.getY();
			}

			this.lastPointerEvent = new PointerEventAndroid(
					TouchManager.this,
					x,
					y,
					ev);

			boolean consumed = false;

			switch (action) {
			case MotionEvent.ACTION_DOWN:
				// The screen was touched. The touch-down event permits
				// to initialize the attributes.
				this.activePointerId = INVALID_POINTER_ID;
				this.lastPointerEvent.unconsume();
				if (canInvokeViewCallbacks) {
					view.onPointerPressed(this.lastPointerEvent);
				}
				consumed = this.lastPointerEvent.isConsumed();
				if (!consumed) {
					this.isActivated = true;
					this.inProgress = false;
					this.activePointerId = ev.getPointerId(0);
					this.lastTouchX = ev.getX(0);
					this.lastTouchY = ev.getY(0);
					consumed = true;
					this.lastPointerEvent.setXY(this.lastTouchX, this.lastTouchY);
				}
				TouchManager.this.clickListener.reset();
				break;

			case MotionEvent.ACTION_MOVE:
				if (this.isActivated) {
					this.inProgress = true;

					pointerIndex = ev.findPointerIndex(this.activePointerId);
					x = ev.getX(pointerIndex);
					y = ev.getY(pointerIndex);

					this.lastPointerEvent.setXY(x, y);
					float dx = view.pixel2logical_size(x - this.lastTouchX);
					float dy = view.pixel2logical_size(y - this.lastTouchY);

					this.lastTouchX = x;
					this.lastTouchY = y;

					if (view.isMoveDirectionInverted()) {
						view.translateFocusPoint(dx, dy);
					}
					else {
						view.translateFocusPoint(-dx, -dy);
					}

					// Always consume the event on moves
					consumed = true;
				}
				else if (canInvokeViewCallbacks) {
					this.lastPointerEvent.unconsume();
					view.onPointerDragged(this.lastPointerEvent);
					consumed = this.lastPointerEvent.isConsumed();
				}
				break;

			case MotionEvent.ACTION_UP:
				this.lastTouchX = ev.getX();
				this.lastTouchY = ev.getY();
				if (this.isActivated) {
					this.isActivated = false;
					this.inProgress = false;
					this.activePointerId = INVALID_POINTER_ID;
					view.repaint();
					consumed = true;
				}
				else {
					this.lastPointerEvent.unconsume();
					if (canInvokeViewCallbacks) {
						view.onPointerReleased(this.lastPointerEvent);
					}
					consumed = this.lastPointerEvent.isConsumed();
				}
				break;

			case MotionEvent.ACTION_CANCEL:
				this.lastTouchX = ev.getX();
				this.lastTouchY = ev.getY();
				if (this.isActivated) {
					this.isActivated = false;
					this.inProgress = false;
					this.activePointerId = INVALID_POINTER_ID;
					view.repaint();
					consumed = true;
				}
				else {
					this.lastPointerEvent.unconsume();
					if (canInvokeViewCallbacks) {
						view.onPointerReleased(this.lastPointerEvent);
					}
					consumed = this.lastPointerEvent.isConsumed();
				}
				break;

			case MotionEvent.ACTION_POINTER_UP:
				if (this.isActivated) {
					pointerIndex = ((ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
					pointerId = ev.getPointerId(pointerIndex);
					if (pointerId == this.activePointerId) {
						// This was our active pointer going up. Choose a new
						// active pointer and adjust accordingly.
						int newPointerIndex = pointerIndex == 0 ? 1 : 0;
						this.lastTouchX = ev.getX(newPointerIndex);
						this.lastTouchY = ev.getY(newPointerIndex);
						this.activePointerId = ev.getPointerId(newPointerIndex);
						view.repaint();
						consumed = true;
					}
					else {
						this.lastTouchX = ev.getX();
						this.lastTouchY = ev.getY();
					}
				}
				else {
					this.lastTouchX = ev.getX();
					this.lastTouchY = ev.getY();
				}
				break;

			default:
				// The other events are ignored.
			}

			return consumed;
		}

	} // class MoveGestureDetector

}