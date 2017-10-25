/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

/**
 * An object that permits to indicates the progression of
 * a task. The progression of the value is always ascendent.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Progression {

	/** Add a listener on this model.
	 *
	 * @param listener is the listener to add.
	 */
	void addProgressionListener(ProgressionListener listener);

	/** Remove a listener on this model.
	 *
	 * @param listener is the listener to remove.
	 */
	void removeProgressionListener(ProgressionListener listener);

	/** Returns the minimum acceptable value.
	 *
	 * @return the value of the minimum property
	 * @see #setMinimum
	 */
	int getMinimum();


	/** Sets the model's minimum to <I>newMinimum</I>. The
	 * other properties may be changed as well, to ensure
	 * that:
	 * <pre>
	 * minimum &lt;= value &lt;= maximum
	 * </pre>
	 *
	 * @param newMinimum the model's new minimum
	 * @see #getMinimum
	 */
	void setMinimum(int newMinimum);


	/**
	 * Returns the model's maximum.
	 *
	 * @return the value of the maximum property.
	 * @see #setMaximum
	 */
	int getMaximum();


	/**
	 * Sets the model's maximum to <I>newMaximum</I>. The other
	 * properties may be changed as well, to ensure that
	 * <pre>
	 * minimum &lt;= value &lt;= maximum
	 * </pre>
	 *
	 * @param newMaximum the model's new maximum
	 * @see #getMaximum
	 */
	void setMaximum(int newMaximum);


	/**
	 * Returns the model's current value.  Note that the upper
	 * limit on the model's value is <code>maximum</code>
	 * and the lower limit is <code>minimum</code>.
	 *
	 * @return  the model's value
	 * @see     #setValue
	 */
	int getValue();

	/**
	 * Returns the model's current value in percent of pregression.
	 *
	 * @return  the model's value between 0 and 100
	 * @see     #getValue()
	 * @see     #getProgressionFactor()
	 */
	double getPercent();

	/**
	 * Returns the model's current value in percent of pregression.
	 *
	 * @return  the model's value between 0 and 1
	 * @see     #getValue()
	 * @see     #getPercent()
	 */
	double getProgressionFactor();

	/**
	 * Sets the model's current value to <code>newValue</code> if <code>newValue</code>
	 * satisfies the model's constraints. Those constraints are:
	 * <pre>
	 * minimum &lt;= value &lt;= maximum
	 * </pre>
	 * Otherwise, if <code>newValue</code> is less than <code>minimum</code>
	 * it's set to <code>minimum</code>, if its greater than
	 * <code>maximum</code> then it's set to <code>maximum</code>.
	 *
	 * @param newValue the model's new value
	 * @see #getValue
	 */
	void setValue(int newValue);

	/**
	 * Sets the model's current value to <code>newValue</code> if <code>newValue</code>
	 * satisfies the model's constraints. Those constraints are:
	 * <pre>
	 * minimum &lt;= value &lt;= maximum
	 * </pre>
	 * Otherwise, if <code>newValue</code> is less than <code>minimum</code>
	 * it's set to <code>minimum</code>, if its greater than
	 * <code>maximum</code> then it's set to <code>maximum</code>.
	 *
	 * @param newValue the model's new value
	 * @param comment is the comment to display.
	 * @see #getValue
	 */
	void setValue(int newValue, String comment);

	/**
	 * This attribute indicates that any upcoming changes to the value
	 * of the model should be considered a single event. This attribute
	 * will be set to true at the start of a series of changes to the value,
	 * and will be set to false when the value has finished changing.  Normally
	 * this allows a listener to only take action when the final value change in
	 * committed, instead of having to do updates for all intermediate values.
	 *
	 * @param adjusting <code>true</code> if the upcoming changes to the value property are part of a series
	 * @see #subTask(int)
	 */
	void setAdjusting(boolean adjusting);


	/**
	 * Returns true if the current changes to the value property are part
	 * of a series of changes.
	 *
	 * @return the valueIsAdjustingProperty.
	 * @see #setAdjusting(boolean)
	 */
	boolean isAdjusting();

	/**
	 * This method sets all of the model's data with a single method call.
	 * The method results in a single change event being generated. This is
	 * convenient when you need to adjust all the model data simultaneously and
	 * do not want individual change events to occur.
	 *
	 * @param value  an int giving the current value
	 * @param min    an int giving the minimum value
	 * @param max    an int giving the maximum value
	 * @param adjusting a boolean, true if a series of changes are in
	 *                    progress
	 * @param comment is the comment associated to the current task, or <code>null</code>
	 *
	 * @see #setValue
	 * @see #setMinimum
	 * @see #setMaximum
	 * @see #setComment(String)
	 * @see #setAdjusting(boolean)
	 */
	void setProperties(int value, int min, int max, boolean adjusting, String comment);

	/**
	 * This method sets all of the model's data with a single method call.
	 * The method results in a single change event being generated. This is
	 * convenient when you need to adjust all the model data simultaneously and
	 * do not want individual change events to occur.
	 *
	 * @param value  an int giving the current value
	 * @param min    an int giving the minimum value
	 * @param max    an int giving the maximum value
	 * @param adjusting a boolean, true if a series of changes are in
	 *     progress
	 *
	 * @see #setValue
	 * @see #setMinimum
	 * @see #setMaximum
	 * @see #setComment(String)
	 * @see #setAdjusting(boolean)
	 */
	void setProperties(int value, int min, int max, boolean adjusting);

	/**
	 * Sets the <code>indeterminate</code> property of the task progression,
	 * which determines whether the progression is in determinate
	 * or indeterminate mode.
	 * For example an indeterminate progress bar continuously displays animation
	 * indicating that an operation of unknown length is occurring.
	 *
	 * @param newValue	<code>true</code> if the progress bar
	 *     should change to indeterminate mode;
	 * 	   <code>false</code> if it should revert to normal.
	 * @see #isIndeterminate
	 */
	void setIndeterminate(boolean newValue);

	/**
	 * Returns the value of the <code>indeterminate</code> property.
	 *
	 * @return the value of the <code>indeterminate</code> property
	 * @see    #setIndeterminate
	 */
	boolean isIndeterminate();

	/** Set the comment associated to the currect task.
	 *
	 * <p>If the given string is <code>null</code> or empty,
	 * the current task is assumed to not have a comment.
	 *
	 * @param comment is the comment for the current task.
	 */
	void setComment(String comment);

	/** Replies the comment associated to the currect task.
	 *
	 * @return the comment for the current task or <code>null</code>
	 *     if the current task has not any comment.
	 */
	String getComment();

	/** Enter into a subtask.
	 * When a sub task is created, it will progress as a sub part of
	 * the main task.
	 *
	 * <p>The minimum value of the subtask in its parent
	 * will correspond to the
	 * current value of the main task. The maximum value of the
	 * sub task in its parent
	 * will correspond to the current value of the main task
	 * plus the given {@code extend}.
	 *
	 * <p>If the {@code extend} plus the current value is exceeding
	 * the maximum value, it will be bounded to the maximum.
	 *
	 * @param extent is the size of the sub task inside the main task.
	 * @param min is the minimum value inside the subtask.
	 * @param max is the maximum value inside the subtask.
	 * @return the subtask progression
	 */
	Progression subTask(int extent, int min, int max);

	/** Enter into a subtask.
	 * When a sub task is created, it will progress as a sub part of
	 * the main task.
	 *
	 * <p>The minimum value of the subtask in its parent
	 * will correspond to the
	 * current value of the main task. The maximum value of the
	 * sub task in its parent
	 * will correspond to the current value of the main task
	 * plus the given {@code extend}.
	 *
	 * <p>If the {@code extend} plus the current value is exceeding
	 * the maximum value, it will be bounded to the maximum.
	 *
	 * <p>The minimum and maximum values of the subtask should be manually
	 * set the the caller.
	 *
	 * @param extent is the size of the sub task inside the main task.
	 * @return the subtask progression
	 */
	Progression subTask(int extent);

	/** Enter into a subtask.
	 * When a sub task is created, it will progress as a sub part of
	 * the main task.
	 *
	 * <p>The minimum value of the subtask in its parent
	 * will correspond to the
	 * current value of the main task. The maximum value of the
	 * sub task in its parent
	 * will correspond to the current value of the main task
	 * plus the given {@code extend}.
	 *
	 * <p>If the {@code extend} plus the current value is exceeding
	 * the maximum value, it will be bounded to the maximum.
	 *
	 * @param extent is the size of the sub task inside the main task.
	 * @param min is the minimum value inside the subtask.
	 * @param max is the maximum value inside the subtask.
	 * @param overwriteComment indicates if the comment of this task model may
	 *     be overwritten by the comment of the subtask when it is disconnected.
	 * @return the subtask progression
	 */
	Progression subTask(int extent, int min, int max, boolean overwriteComment);

	/** Enter into a subtask.
	 * When a sub task is created, it will progress as a sub part of
	 * the main task.
	 *
	 * <p>The minimum value of the subtask in its parent
	 * will correspond to the
	 * current value of the main task. The maximum value of the
	 * sub task in its parent
	 * will correspond to the current value of the main task
	 * plus the given {@code extend}.
	 *
	 * <p>If the {@code extend} plus the current value is exceeding
	 * the maximum value, it will be bounded to the maximum.
	 *
	 * <p>The minimum and maximum values of the subtask should be manually
	 * set the the caller.
	 *
	 * @param extent is the size of the sub task inside the main task.
	 * @param overwriteComment indicates if the comment of this task model may
	 *     be overwritten by the comment of the subtask when it is disconnected.
	 * @return the subtask progression
	 */
	Progression subTask(int extent, boolean overwriteComment);

	/** Replies the current subtask of this task model.
	 *
	 * @return the current subtask or <code>null</code> this task was
	 *     not decomposed.
	 */
	Progression getSubTask();

	/**
	 * Ensure that there is no opened subtask.
	 * If a subtask is existing, it is ended().
	 */
	void ensureNoSubTask();

	/** Replies the super task of this task model.
	 *
	 * @return the super task or <code>null</code> this task was
	 *     not a decomposition of a super task.
	 */
	Progression getSuperTask();

	/** Force this progression task to end its indicator.
	 */
	void end();

	/** Replies if is task model is a root model.
	 *
	 * <p>{@code isRootMode() == (getTaskDepth()==0)}
	 *
	 * @return <code>true</code> if this model is a root model,
	 *     otherwise <code>false</code>
	 */
	boolean isRootModel();

	/** Replies the depth level of this task.
	 * The root task (ie. a task without parent task)
	 * has always a depth of {@code 0}. A subtask of the
	 * root task has a depth of {@code 1}. A subtask
	 * of this subtask has a depth of {@code 2}, etc.
	 *
	 * <p>{@code isRootMode() == (getTaskDepth()==0)}
	 *
	 * @return the depth of the task.
	 */
	int getTaskDepth();

	/** Increment the current value by the given amount.
	 *
	 * <p>This function is equivalent to:
	 * <pre><code>
	 * this.setValue(this.getValue()+amount);
	 * </code></pre>
	 *
	 * @param amount is the amount to add to the current value.
	 * @since 11.0
	 */
	void increment(int amount);

	/** Increment the current value by the given amount.
	 *
	 * <p>This function is equivalent to:
	 * <pre><code>
	 * this.setValue(this.getValue()+amount, comment);
	 * </code></pre>
	 *
	 * @param amount is the amount to add to the current value.
	 * @param comment is the comment to display.
	 * @since 11.0
	 */
	void increment(int amount, String comment);

	/** Increment the current value by 1.
	 *
	 * <p>This function is equivalent to:
	 * <pre><code>
	 * this.setValue(this.getValue()+1);
	 * </code></pre>
	 *
	 * @since 11.0
	 */
	void increment();

	/** Increment the current value by 1.
	 *
	 * <p>This function is equivalent to:
	 * <pre><code>
	 * this.setValue(this.getValue()+1, comment);
	 * </code></pre>
	 *
	 * @param comment is the comment to display.
	 * @since 11.0
	 */
	void increment(String comment);

}
