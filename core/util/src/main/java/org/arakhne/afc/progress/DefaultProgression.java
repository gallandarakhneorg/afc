/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.progress;

import org.arakhne.afc.util.ListenerCollection;

/**
 * An object that permits to indicates the progression of
 * a task. The progression of the value is always ascendent.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultProgression implements Progression {
	
	private int min, max;
	private double current; // use floating point value to allow finest subtask's progression.
	private boolean isIndeterminate;
	private boolean isAdjusting;
	private String comment;
	private SubProgressionModel child = null;
	
	/** Collection of listeners.
	 */
	protected final ListenerCollection<ProgressionListener> listeners = new ListenerCollection<>();
	
	/** Create a progress model with the specified <i>determinate</i> state.
	 * 
	 * @param value is the initial value of this model (between <var>min</var> and <var>max</var>).
	 * @param min is the minimal value
	 * @param max is the maximal value
	 * @param adjusting indicates if this model is under adjustement or not.
	 */
	public DefaultProgression(int value, int min, int max, boolean adjusting) {
		if (min>max) {
			this.min = max;
			this.max = min;
		}
		else {
			this.min = min;
			this.max = max;
		}
		this.current = (value<this.min) ? this.min : (value>this.max) ? this.max : value;
		this.isIndeterminate = false;
		this.isAdjusting = adjusting;
	}
	
	/** Create a progress model with the specified <i>determinate</i> state.
	 * The model is not adjusting.
	 * 
	 * @param value is the initial value of this model (between <var>min</var> and <var>max</var>).
	 * @param min is the minimal value
	 * @param max is the maximal value
	 */
	public DefaultProgression(int value, int min, int max) {
		this(value, min, max, false);
	}

	/** Create a progress model with the specified <i>indeterminate</i> state.
	 * The model is not adjusting.
	 * 
	 * @param min is the minimal value
	 * @param max is the maximal value
	 */
	public DefaultProgression(int min, int max) {
		if (min>max) {
			this.min = max;
			this.max = min;
		}
		else {
			this.min = min;
			this.max = max;
		}
		this.current = this.min;
		this.isIndeterminate = true;
		this.isAdjusting = false;
	}

	/** Create a progress model in a <i>indeterminate</i> state.
	 * The model is not adjusting.
	 */
	public DefaultProgression() {
		this.min = this.max = 0;
		this.current = this.min;
		this.isIndeterminate = true;
		this.isAdjusting = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void end() {
		int v = getValue();
		int m = getMaximum();
		String comment = getComment();
		if (v<m || comment!=null) {
			setValue(m, null);
		}
		fireStateChange();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addProgressionListener(ProgressionListener listener) {
		this.listeners.add(ProgressionListener.class, listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeProgressionListener(ProgressionListener listener) {
		this.listeners.remove(ProgressionListener.class, listener);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isRootModel() {
		return true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Progression getSuperTask() {
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getTaskDepth() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Progression getSubTask() {
		return this.child;
	}
	
	@Override
	public final void ensureNoSubTask() {
		if (this.child!=null) {
			disconnectSubTask(
					this.child,
					this.child.getMaxInParent(),
					false);
		}
	}

	/** Notify listeners about state change.
	 */
	protected void fireStateChange() {
		if (this.listeners!=null) {
			ProgressionEvent event = new ProgressionEvent(this, isRootModel());
			for(ProgressionListener listener : this.listeners.getListeners(ProgressionListener.class)) {
				listener.onProgressionStateChanged(event);
			}
		}
	}

	/** Notify listeners about value change.
	 */
	protected void fireValueChange() {
		if (this.listeners!=null) {
			ProgressionEvent event = new ProgressionEvent(this, isRootModel());
			for(ProgressionListener listener : this.listeners.getListeners(ProgressionListener.class)) {
				listener.onProgressionValueChanged(event);
			}
		}
	}

	@Override
	public int getMaximum() {
		return this.max;
	}

	@Override
	public int getMinimum() {
		return this.min;
	}

	@Override
	public double getPercent() {
		double extent = this.max - this.min;
		if (extent==0f) return 0f;
		return (this.current - this.min) * 100f / extent;
	}

	@Override
	public double getProgressionFactor() {
		double extent = this.max - this.min;
		if (extent==0f) return 0f;
		return (this.current - this.min) / extent;
	}

	@Override
	public int getValue() {
		return (int)this.current;
	}

	/** Replies the floating-point precision value.
	 * 
	 * @return the floating-point precision value.
	 */
	protected double getFloatValue() {
		return this.current;
	}

	@Override
	public boolean isAdjusting() {
		return this.isAdjusting;
	}

	@Override
	public boolean isIndeterminate() {
		return this.isIndeterminate;
	}

	@Override
	public void setIndeterminate(boolean newValue) {
		if (this.isIndeterminate!=newValue) {
			this.isIndeterminate = newValue;
			fireStateChange();
		}
	}

	@Override
	public void setMaximum(int newMaximum) {
		setProperties(this.current, this.min, newMaximum, this.isAdjusting, this.comment, false, false, true, null);
	}

	@Override
	public void setMinimum(int newMinimum) {
		setProperties(this.current, newMinimum, this.max, this.isAdjusting, this.comment, false, false, true, null);
	}

	@Override
	public void setProperties(int value, int min, int max, boolean adjusting, String comment) {
		setProperties(value, min, max, adjusting, comment, true, true, true, this.child);
	}

	@Override
	public void setProperties(int value, int min, int max, boolean adjusting) {
		setProperties(value, min, max, adjusting, this.comment, false, true, true, this.child);
	}

	@Override
	public void setValue(int newValue) {
		setProperties(newValue, this.min, this.max, this.isAdjusting, this.comment, false, true, false, null);
	}

	@Override
	public void setValue(int newValue, String comment) {
		setProperties(newValue, this.min, this.max, this.isAdjusting, comment, true, true, false, null);
	}

	@Override
	public void setAdjusting(boolean b) {
		setProperties(this.current, this.min, this.max, b, this.comment, false, false, false, null);
	}

	private void setProperties(double value, int min, int max, boolean adjusting, 
			String comment, boolean writeLocalComment,
			boolean forceDeterminate, boolean forceValue,
			SubProgressionModel subTask) {
		if (this.child!=null && this.child!=subTask) {
			this.child.disconnect();
		}

		String uptComment = comment;
		if (uptComment!=null && "".equals(uptComment)) //$NON-NLS-1$
			uptComment = null;

		int theMin = min;
		int theMax = max;
		if (theMin>max) {
			int tmp = theMin;
			theMin = theMax;
			theMax = tmp;
		}
		
		double theValue = value;
		if (theValue<theMin) theValue = theMin;
		if (theValue>theMax) theValue = theMax;
		
		boolean changed = 
			((forceValue)&&(theValue != this.current))
			|| ((!forceValue)&&(theValue > this.current))
			|| (theMin != this.min)
			|| (theMax != this.max)
			|| (adjusting != this.isAdjusting)
			|| (uptComment!=this.comment && (uptComment==null || !uptComment.equals(this.comment)));
		
		if (changed && !adjusting) {
			this.current = theValue;
			this.min = theMin;
			this.max = theMax;
			this.isAdjusting = adjusting;
			if (writeLocalComment) this.comment = uptComment;
			
			fireValueChange();
		}

		if (forceDeterminate) setIndeterminate(false);		
	}

	@Override
	public final Progression subTask(int extent, int minValue, int maxValue) {
		return subTask(extent, minValue, maxValue, false);
	}
	
	@Override
	public Progression subTask(int extent, int minValue, int maxValue, boolean overwriteComment) {
		if (this.child!=null) {
			this.child.disconnect();
		}
		this.child = new SubProgressionModel(
				this, 
				this.current, this.current+Math.abs(extent), 
				minValue, maxValue, 
				this.isIndeterminate, this.isAdjusting,
				overwriteComment);
		return this.child;
	}

	@Override
	public final Progression subTask(int extent) {
		return subTask(extent, false);
	}

	@Override
	public Progression subTask(int extent, boolean overwriteComment) {
		if (this.child!=null) {
			this.child.disconnect();
		}
		this.child = new SubProgressionModel(
				this, 
				this.current, this.current+Math.abs(extent), 
				this.isIndeterminate, this.isAdjusting,
				overwriteComment);
		return this.child;
	}

	/** Set the parent value and disconnect this subtask from its parent.
	 * This method is invoked from a subtask.
	 * 
	 * @param subTask
	 * @param value
	 * @param overwriteComment indicates if the comment of this parent model may
	 * be overwritten by the child's comment.
	 */
	void disconnectSubTask(SubProgressionModel subTask, double value, boolean overwriteComment) {
		if (this.child==subTask) {
			if (overwriteComment) {
				String cmt = subTask.getComment();
				if (cmt!=null)
					this.comment = cmt;
			}
			this.child = null;
			setProperties(value, this.min, this.max, this.isAdjusting, this.comment, overwriteComment, true, false, null);
		}
	}

	/** Set the value with a float-point precision. 
	 * This method is invoked from a subtask.
	 * 
	 * @param subTask
	 * @param newValue
	 * @param comment
	 */
	void setValue(SubProgressionModel subTask, double newValue, String comment) {
		setProperties(newValue, this.min, this.max, this.isAdjusting, 
				comment==null ? this.comment : comment, true, true, false,
				subTask);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getComment() {
		if (this.child!=null) {
			String childComment = this.child.getComment();
			if (childComment!=null) return childComment;
		}
		return this.comment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setComment(String comment) {
		setProperties(
				this.current, 
				this.min, this.max, 
				this.isAdjusting, comment, 
				true, true, false, null);
	}

	@Override
	public void increment(int amount) {
		setProperties((int)this.current+amount, this.min, this.max, this.isAdjusting, this.comment, false, true, false, null);
	}

	@Override
	public void increment(int amount, String comment) {
		setProperties((int)this.current+amount, this.min, this.max, this.isAdjusting, comment, true, true, false, null);
	}

	@Override
	public void increment() {
		setProperties((int)this.current+1, this.min, this.max, this.isAdjusting, this.comment, false, true, false, null);
	}

	@Override
	public void increment(String comment) {
		setProperties((int)this.current+1, this.min, this.max, this.isAdjusting, comment, true, true, false, null);
	}

}
