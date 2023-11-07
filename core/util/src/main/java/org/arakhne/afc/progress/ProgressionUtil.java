/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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
 * Utilities around task progression.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class ProgressionUtil {

	private ProgressionUtil() {
		//
	}

	/** Initialize the given task progression if not {@code null}.
	 *
	 * @param model is the task progression to initialize.
	 * @param value is the progression value.
	 * @param min is the minimum value.
	 * @param max is the maximum value.
	 * @param isAdjusting indicates if the value is still under changes.
	 * @param isInderterminate indicates if the progression value is inderterminate or not.
	 */
	public static void init(Progression model, int value, int min, int max, boolean isAdjusting, boolean isInderterminate) {
		if (model != null) {
			model.setProperties(value, min, max, isAdjusting);
			model.setIndeterminate(isInderterminate);
		}
	}

	/** Initialize the given task progression if not {@code null}.
	 *
	 * <p>The inderterminate state is not changed.
	 *
	 * @param model is the task progression to initialize.
	 * @param min is the minimum value.
	 * @param max is the maximum value.
	 * @param isAdjusting indicates if the value is still under changes.
	 */
	public static void init(Progression model, int min, int max, boolean isAdjusting) {
		if (model != null) {
			model.setProperties(min, min, max, isAdjusting);
		}
	}

	/** Initialize the given task progression if not {@code null}.
	 *
	 * <p>The inderterminate state is not changed.
	 * The adjusting state is set to {@code false}.
	 *
	 * @param model is the task progression to initialize.
	 * @param min is the minimum value.
	 * @param max is the maximum value.
	 */
	public static void init(Progression model, int min, int max) {
		if (model != null) {
			model.setProperties(min, min, max, false);
		}
	}

	/** Initialize the given task progression if not {@code null}.
	 *
	 * @param model is the task progression to initialize.
	 * @param value is the progression value.
	 * @param min is the minimum value.
	 * @param max is the maximum value.
	 * @param isAdjusting indicates if the value is still under changes.
	 * @param isInderterminate indicates if the progression value is inderterminate or not
	 * @param comment is the text associated with the progression.
	 */
	public static void init(Progression model, int value, int min, int max, boolean isAdjusting,
			boolean isInderterminate, String comment) {
		if (model != null) {
			model.setProperties(value, min, max, isAdjusting, comment);
			model.setIndeterminate(isInderterminate);
		}
	}

	/** Initialize the given task progression if not {@code null}.
	 *
	 * <p>The inderterminate state is not changed.
	 *
	 * @param model is the task progression to initialize.
	 * @param min is the minimum value.
	 * @param max is the maximum value.
	 * @param isAdjusting indicates if the value is still under changes.
	 * @param comment is the text associated with the progression.
	 */
	public static void init(Progression model, int min, int max, boolean isAdjusting, String comment) {
		if (model != null) {
			model.setProperties(min, min, max, isAdjusting, comment);
		}
	}

	/** Initialize the given task progression if not {@code null}.
	 *
	 * <p>The inderterminate state is not changed.
	 * The adjusting state is set to {@code false}.
	 *
	 * @param model is the task progression to initialize.
	 * @param min is the minimum value.
	 * @param max is the maximum value.
	 * @param comment is the text associated with the progression.
	 */
	public static void init(Progression model, int min, int max, String comment) {
		if (model != null) {
			model.setProperties(min, min, max, false, comment);
		}
	}

	/** Create and replies a task progression model that is for
	 * a subtask of the task associated to the given progression model.
	 *
	 * <p>The subtask progression model is not initialized.
	 * When the subtask progression model reaches its end,
	 * the value of the task progression is {@code s + extent}
	 * where {@code s} is the value of the task progression when
	 * this function is called.
	 *
	 * @param model is the model to derivate
	 * @param extent is the size of the subtask progression that is
	 *     covered in the task progression.
	 * @return the subtask progression model; or {@code null}.
	 */
	public static Progression sub(Progression model, int extent) {
		if (model != null) {
			return model.subTask(extent);
		}
		return null;
	}

	/** Create and replies a task progression model that is for
	 * a subtask of the task associated to the given progression model.
	 *
	 * <p>The subtask progression model is not initialized.
	 * When the subtask progression model reaches its end,
	 * the value of the task progression is {@code s + extent}
	 * where {@code s} is the value of the task progression when
	 * this function is called.
	 *
	 * @param model is the model to derivate
	 * @param extent is the size of the subtask progression that is
	 *     covered in the task progression.
	 * @param overwriteComment indicates if the comment of this task model may
	 *     be overwritten by the comment of the subtask when it is disconnected.
	 * @return the subtask progression model; or {@code null}.
	 */
	public static Progression sub(Progression model, int extent, boolean overwriteComment) {
		if (model != null) {
			return model.subTask(extent, overwriteComment);
		}
		return null;
	}

	/** Create and replies a task progression model that is for
	 * a subtask of the task associated to the given progression model.
	 *
	 * <p>The subtask progression model is not initialized.
	 * When the subtask progression model reaches its end,
	 * the value of the task progression is equal to the maximum.
	 *
	 * @param model is the model to derivate
	 * @return the subtask progression model; or {@code null}.
	 */
	public static Progression subToEnd(Progression model) {
		if (model != null) {
			return model.subTask(model.getMaximum() - model.getValue());
		}
		return null;
	}

	/** Create and replies a task progression model that is for
	 * a subtask of the task associated to the given progression model.
	 *
	 * <p>The subtask progression model is not initialized.
	 * When the subtask progression model reaches its end,
	 * the value of the task progression is equal to the maximum.
	 *
	 * @param model is the model to derivate
	 * @param overwriteComment indicates if the comment of this task model may
	 *     be overwritten by the comment of the subtask when it is disconnected.
	 * @return the subtask progression model; or {@code null}.
	 */
	public static Progression subToEnd(Progression model, boolean overwriteComment) {
		if (model != null) {
			return model.subTask(model.getMaximum() - model.getValue(), overwriteComment);
		}
		return null;
	}

	/** Increment the value of the given task progression,
	 * if not {@code null}, by the given amount.
	 *
	 * @param model is the progression to change
	 * @param value is the value to add to the progression value.
	 */
	public static void advance(Progression model, int value) {
		if (model != null && value > 0) {
			final SubProgressionModel sub = (SubProgressionModel) model.getSubTask();
			final double base;
			if (sub == null) {
				base = model.getValue();
			} else {
				base = sub.getMinInParent();
				model.ensureNoSubTask();
			}
			model.setValue((int) base + value);
		}
	}

	/** Increment the value of the given task progression,
	 * if not {@code null}, by {@code 1}.
	 *
	 * @param model is the progression to change
	 */
	public static void advance(Progression model) {
		advance(model, 1);
	}

	/** Increment the value of the given task progression,
	 * if not {@code null}, by the given amount.
	 *
	 * @param model is the progression to change
	 * @param value is the value to add to the progression value.
	 * @param comment is the comment associated to the progression.
	 */
	public static void advance(Progression model, int value, String comment) {
		if (model != null && value > 0) {
			model.setValue(model.getValue() + value, comment);
		}
	}

	/** Increment the value of the given task progression,
	 * if not {@code null}, by {@code 1}.
	 *
	 * @param model is the progression to change
	 * @param comment is the comment associated to the progression.
	 */
	public static void advance(Progression model, String comment) {
		advance(model, 1, comment);
	}

	/** Replies the value of the given task progression,
	 * if not {@code null}.
	 *
	 * @param model is the progression to change
	 * @return the value or {@code 0} if there is no progession model.
	 */
	public static int getValue(Progression model) {
		if (model != null) {
			return model.getValue();
		}
		return 0;
	}

	/** Replies the value from the current progression value
	 * to the maximal progression value of the given task progression,
	 * if not {@code null}.
	 *
	 * @param model is the progression to change
	 * @return the positive value to end or {@code 0} if there is no progession model.
	 */
	public static int getValueToEnd(Progression model) {
		if (model != null) {
			return model.getMaximum() - model.getValue();
		}
		return 0;
	}

	/** Replies the percent of progression for the given task progression,
	 * if not {@code null}.
	 *
	 * @param model is the progression to change
	 * @return the percent of progress or {@link Double#NaN}
	 *     if there is no progession model.
	 */
	public static double getPercent(Progression model) {
		if (model != null) {
			return model.getPercent();
		}
		return Double.NaN;
	}

	/** Set the value of the given task progression,
	 * if not {@code null}.
	 * The value must be greater than the current progression value.
	 *
	 * @param model is the progression to change
	 * @param value is the value to add to the progression value.
	 */
	public static void setValue(Progression model, int value) {
		if (model != null && value > model.getValue()) {
			model.setValue(value);
		}
	}

	/** Set the value of the given task progression,
	 * if not {@code null}.
	 * The value must be greater than the current progression value.
	 *
	 * @param model is the progression to change
	 * @param value is the value to add to the progression value.
	 * @param comment is the comment associated to the progression.
	 */
	public static void setValue(Progression model, int value, String comment) {
		if (model != null && value > model.getValue()) {
			model.setValue(value, comment);
		}
	}

	/** Force the given task progression, if not {@code null},
	 * to have its value equals to the maximal value.
	 *
	 * <p>The inderterminate and adjusting states are not changed.
	 * The comment is reset.
	 *
	 * @param model is the task progression to initialize.
	 */
	public static void end(Progression model) {
		if (model != null) {
			model.end();
			model.setComment(null);
		}
	}

	/** Ensure that there is no opened subtask.
	 * If a subtask is existing, it is ended().
	 *
	 * @param model is the task progression to initialize.
	 */
	public static void ensureNoSubTask(Progression model) {
		if (model != null) {
			model.ensureNoSubTask();
		}
	}

	/** Replies if the given progression indicators has its value
	 * equal to its min value.
	 *
	 * @param model is the task progression to initialize.
	 * @return {@code true} if the indicator is at its min value or
	 *     if {@code model} is {@code null}.
	 */
	public static boolean isMinValue(Progression model) {
		if (model != null) {
			return model.getValue() <= model.getMinimum();
		}
		return true;
	}

}
