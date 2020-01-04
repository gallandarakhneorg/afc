/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.io.dbase;

import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * Exception throws by a Shape file reader
 * when a type of field is unsupported.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class InvalidDBaseFieldTypeException extends DBaseFileException {

	private static final long serialVersionUID = -6808202714059751254L;

	/** Constructor.
	 *
	 * @param type is the invalid type.
	 */
	public InvalidDBaseFieldTypeException(byte type) {
		super(Locale.getString("MESSAGE", "0x" + Integer.toHexString(type)));  //$NON-NLS-1$//$NON-NLS-2$
	}

	/** Constructor.
	 *
	 * @param type is the invalid type
	 */
	public InvalidDBaseFieldTypeException(AttributeType type) {
		super(Locale.getString("MESSAGE", type.getLocalizedName())); //$NON-NLS-1$
	}

}
