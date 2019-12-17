/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.progress;

import org.arakhne.afc.util.ListenerCollection;

/**
 * An object that permits to indicates the progression of
 * a task. The progression of the value is always ascendent.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultProgression implements Progression {

	/** Collection of listeners.
	 */
	protected final ListenerCollection<ProgressionListener> listeners = new ListenerCollection<>();

	private int min;

	private int max;

	/** Use floating point value to allow finest subtask's progression.
	 */
	private double current;

	private boolean isIndeterminate;

	private boolean isAdjusting;

	private String comment;

	private SubProgressionModel child;

	/** Create a progress model with the specified <i>determinate</i> state.
	 *
	 * @param value is the initial value of this model (between {@code min} and {@code max}).
	 * @param min is the minimal value
	 * @param max is the maximal value
	 * @param adjusting indicates if this model is under adjustement or not.
	 */
	public DefaultProgression(int value, int min, int max, boolean adjusting) {
		if (min > max) {
			this.min = max;
			this.max = min;
		} else {
			this.min = min;
			this.max = max;
		}
		this.current = (value < this.min) ? this.min : (value > this.max) ? this.max : value;
		this.isIndeterminate = false;
		this.isAdjusting = adjusting;
	}

	/** Create a progress model with the specified <i>determinate</i> state.
	 * The model is not adjusting.
	 *
	 * @param value is the initial value of this model (between {@code min} and {@code max}).
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
		if (min > max) {
			this.min = max;
			this.max = min;
		} else {
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
		this.min = 0;
		this.max = 0;
		this.current = this.min;
		this.isIndeterminate = true;
		this.isAdjusting = false;
	}

	@Override
	public void end() {
		final int v = getValue();
		final int m = getMaximum();
		final String comment = getComment();
		if (v < m || comment != null) {
			setValue(m, null);
		}
		fireStateChange();
	}

	@Override
	public void addProgressionListener(ProgressionListener listener) {
		this.listeners.add(ProgressionListener.class, listener);
	}

	@Override
	public void removeProgressionListener(ProgressionListener listener) {
		this.listeners.remove(ProgressionListener.class, listener);
	}

	@Override
	public boolean isRootModel() {
		return true;
	}

	@Override
	public Progression getSuperTask() {
		return null;
	}

	@Override
	public int getTaskDepth() {
		return 0;
	}

	@Override
	public final Progression getSubTask() {
		return this.child;
	}

	@Override
	public final void ensureNoSubTask() {
		if (this.child != null) {
			disconnectSubTask(
					this.child,
					this.child.getMaxInParent(),
					false);
		}
	}

	/** Notify listeners about state change.
	 */
	protected void fireStateChange() {
		if (this.listeners != null) {
			final ProgressionEvent event = new ProgressionEvent(this, isRootModel());
			for (final ProgressionListener listener : this.listeners.getListeners(ProgressionListener.class)) {
				listener.onProgressionStateChanged(event);
			}
		}
	}

	/** Notify listeners about value change.
	 */
	protected void fireValueChange() {
		if (this.listeners != null) {
			final ProgressionEvent event = new ProgressionEvent(this, isRootModel());
			for (final ProgressionListener listener : this.listeners.getListeners(ProgressionListener.class)) {
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
	@SuppressWarnings("checkstyle:magicnumber")
	public double getPercent() {
		final int extent = this.max - this.min;
		if (extent == 0.) {
			return 0.;
		}
		return (this.current - this.min) * 100. / extent;
	}

	@Override
	public double getProgressionFactor() {
		final int extent = this.max - this.min;
		if (extent == 0.) {
			return 0.;
		}
		return (this.current - this.min) / extent;
	}

	@Override
	public int getValue() {
		return (int) this.current;
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
		if (this.isIndeterminate != newValue) {
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

	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity",
			"checkstyle:parameternumber", "checkstyle:booleanexpressioncomplexity"})
	private void setProperties(double value, int min, int max, boolean adjusting,
			String comment, boolean writeLocalComment,
			boolean forceDeterminate, boolean forceValue,
			SubProgressionModel subTask) {
		if (this.child != null && this.child != subTask) {
			this.child.disconnect();
		}

		String uptComment = comment;
		if (uptComment != null && "".equals(uptComment)) { //$NON-NLS-1$
			uptComment = null;
		}

		int theMin = min;
		int theMax = max;
		if (theMin > max) {
			final int tmp = theMin;
			theMin = theMax;
			theMax = tmp;
		}

		double theValue = value;
		if (theValue < theMin) {
			theValue = theMin;
		}
		if (theValue > theMax) {
			theValue = theMax;
		}

		final boolean changed = (forceValue && (theValue != this.current))
				|| ((!forceValue) && (theValue > this.current))
				|| (theMin != this.min)
				|| (theMax != this.max)
				|| (adjusting != this.isAdjusting)
				|| (uptComment != this.comment && (uptComment == null || !uptComment.equals(this.comment)));

		if (changed && !adjusting) {
			this.current = theValue;
			this.min = theMin;
			this.max = theMax;
			this.isAdjusting = adjusting;
			if (writeLocalComment) {
				this.comment = uptComment;
			}

			fireValueChange();
		}

		if (forceDeterminate) {
			setIndeterminate(false);
		}
	}

	@Override
	public void setValue(int newValue) {
		setProperties(newValue, this.min, this.max, this.isAdjusting, this.comment, false, true, false, null);
	}

	@Override
	public void setValue(int newValue, String comment) {
		setProperties(newValue, this.min, this.max, this.isAdjusting, comment, true, true, false, null);
	}

	/** Set the value with a float-point precision.
	 * This method is invoked from a subtask.
	 *
	 * @param subTask the subtask.
	 * @param newValue the new value.
	 * @param comment the new comment.
	 */
	void setValue(SubProgressionModel subTask, double newValue, String comment) {
		setProperties(newValue, this.min, this.max, this.isAdjusting,
				comment == null ? this.comment : comment, true, true, false,
				subTask);
	}

	@Override
	public void setAdjusting(boolean adjusting) {
		setProperties(this.current, this.min, this.max, adjusting, this.comment, false, false, false, null);
	}

	@Override
	public final Progression subTask(int extent, int minValue, int maxValue) {
		return subTask(extent, minValue, maxValue, false);
	}

	@Override
	public Progression subTask(int extent, int minValue, int maxValue, boolean overwriteComment) {
		if (this.child != null) {
			this.child.disconnect();
		}
		this.child = new SubProgressionModel(
				this,
				this.current, this.current + Math.abs(extent),
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
		if (this.child != null) {
			this.child.disconnect();
		}
		this.child = new SubProgressionModel(
				this,
				this.current, this.current + Math.abs(extent),
				this.isIndeterminate, this.isAdjusting,
				overwriteComment);
		return this.child;
	}

	/** Set the parent value and disconnect this subtask from its parent.
	 * This method is invoked from a subtask.
	 *
	 * @param subTask the subtask to disconnect.
	 * @param value the value of the task.
	 * @param overwriteComment indicates if the comment of this parent model may
	 *     be overwritten by the child's comment.
	 */
	void disconnectSubTask(SubProgressionModel subTask, double value, boolean overwriteComment) {
		if (this.child == subTask) {
			if (overwriteComment) {
				final String cmt = subTask.getComment();
				if (cmt != null) {
					this.comment = cmt;
				}
			}
			this.child = null;
			setProperties(value, this.min, this.max, this.isAdjusting, this.comment, overwriteComment, true, false, null);
		}
	}

	@Override
	public String getComment() {
		if (this.child != null) {
			final String childComment = this.child.getComment();
			if (childComment != null) {
				return childComment;
			}
		}
		return this.comment;
	}

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
		setProperties((int) this.current + amount, this.min, this.max, this.isAdjusting, this.comment, false, true, false, null);
	}

	@Override
	public void increment(int amount, String comment) {
		setProperties((int) this.current + amount, this.min, this.max, this.isAdjusting, comment, true, true, false, null);
	}

	@Override
	public void increment() {
		setProperties((int) this.current + 1, this.min, this.max, this.isAdjusting, this.comment, false, true, false, null);
	}

	@Override
	public void increment(String comment) {
		setProperties((int) this.current + 1, this.min, this.max, this.isAdjusting, comment, true, true, false, null);
	}

}
