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

package org.arakhne.afc.gis.primitive;

import org.eclipse.xtext.xbase.lib.Pure;

/** Describe any object that is container a set of flags.
 * Flags are application-dependent, except for the
 * "selected" and "read-only" flags, which are directly
 * provided by this interface.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface FlagContainer {

	/** Selection flag.
	 *
	 * @see #getFlags()
	 */
	int FLAG_SELECTED = 0x1;

	/** Read-only flag.
	 *
	 * @see #getFlags()
	 */
	int FLAG_READONLY = 0x2;

	/** Replies the flags associated to this element.
	 *
	 * <p>The flag could be predefined (eg. {@link #FLAG_SELECTED}, or
	 * user defined.
	 *
	 * @return the flags
	 * @see #FLAG_SELECTED
	 * @see #hasFlag(int)
	 * @see #setFlag(int)
	 * @see #unsetFlag(int)
	 */
	@Pure
	int getFlags();

	/** Replies if the specified flag is set for this element.
	 *
	 * <p>The flag could be predefined (eg. {@link #FLAG_SELECTED}, or
	 * user defined.
	 *
	 * @param flagIndex is the index of the flag
	 * @return {@code true} if the flag was set, otherwise {@code false}
	 * @see #FLAG_SELECTED
	 * @see #getFlags()
	 * @see #setFlag(int)
	 * @see #unsetFlag(int)
	 */
	@Pure
	boolean hasFlag(int flagIndex);

	/** Switch the value of the specified flag on this element.
	 *
	 * <p>The flag could be predefined (eg. {@link #FLAG_SELECTED}, or
	 * user defined.
	 *
	 * @param flagIndex is the index of the flag
	 * @see #FLAG_SELECTED
	 * @see #getFlags()
	 * @see #setFlag(int)
	 * @see #unsetFlag(int)
	 */
	void switchFlag(int flagIndex);

	/** Set the flag.
	 *
	 * <p>The flag could be predefined (eg. {@link #FLAG_SELECTED}, or
	 * user defined.
	 *
	 * @param flag is the index of the flag
	 * @see #FLAG_SELECTED
	 * @see #getFlags()
	 * @see #hasFlag(int)
	 * @see #unsetFlag(int)
	 */
	void setFlag(int flag);

	/** Unset the flag.
	 *
	 * <p>The flag could be predefined (eg. {@link #FLAG_SELECTED}, or
	 * user defined.
	 *
	 * @param flagIndex is the index of the flag
	 * @see #FLAG_SELECTED
	 * @see #getFlags()
	 * @see #hasFlag(int)
	 * @see #setFlag(int)
	 */
	void unsetFlag(int flagIndex);

}
