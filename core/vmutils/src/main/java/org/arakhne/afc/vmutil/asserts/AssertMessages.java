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

package org.arakhne.afc.vmutil.asserts;

import org.arakhne.afc.vmutil.locale.Locale;

/**
 * Provides standard error messages for assertions.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public final class AssertMessages {

	private AssertMessages() {
		//
	}

	private static String msg(String id, Object... parameters) {
		return Locale.getStringFrom(AssertMessages.class.getPackage().getName() + ".assert_messages", //$NON-NLS-1$
				id, parameters);
	}

	/** Positive or zero parameter.
	 *
	 * @param parameterIndex the index of the formal parameter.
	 * @return the error message.
	 */
	public static String positiveOrZeroParameter(int parameterIndex) {
		return msg("A1", parameterIndex); //$NON-NLS-1$
	}

	/** Positive or zero parameter.
	 *
	 * @return the error message.
	 */
	public static String positiveOrZeroParameter() {
		return msg("A1", 0); //$NON-NLS-1$
	}

	/** Unsupported primitive type.
	 *
	 * @return the error message.
	 */
	public static String unsupportedPrimitiveType() {
		return msg("A2", 0); //$NON-NLS-1$
	}

}
