/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.gis.bus.network;

import java.io.Serializable;
import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Describes all the possible reasons of the invalidity
 * of a bus primitive.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class BusPrimitiveInvalidity implements Cloneable, Serializable {

	/** Generic invalidity.
	 */
	public static final BusPrimitiveInvalidity GENERIC_INVALIDITY =
			new BusPrimitiveInvalidity(BusPrimitiveInvalidityType.GENERIC_ERROR, null);

	private static final long serialVersionUID = -7704445937498885786L;

	private final BusPrimitiveInvalidityType type;

	private final String additionalInformation;

	/** Constructor.
	 * @param type is the type of the invalidity.
	 * @param additionalInformation describes some additional information on the invalidity.
	 */
	public BusPrimitiveInvalidity(BusPrimitiveInvalidityType type, String additionalInformation) {
		assert type != null;
		this.type = type;
		this.additionalInformation = additionalInformation;
	}

	@Override
	@Pure
	public BusPrimitiveInvalidity clone() {
		try {
			return (BusPrimitiveInvalidity) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	@Pure
	public boolean equals(Object obj) {
		if (obj instanceof BusPrimitiveInvalidityType) {
			return this.type == (BusPrimitiveInvalidityType) obj;
		} else if (obj instanceof BusPrimitiveInvalidity) {
			final BusPrimitiveInvalidity reason = (BusPrimitiveInvalidity) obj;
			return this.type == reason.type
					&& (this.additionalInformation == reason.additionalInformation
						|| (this.additionalInformation != null
						    && this.additionalInformation.equals(reason.additionalInformation)));
		}
		return super.equals(obj);
	}

	@Override
	@Pure
	public int hashCode() {
		return Objects.hash(this.type, this.additionalInformation);
	}

	/** Replies the type of the invalidity.
	 *
	 * @return the type of the validity.
	 */
	@Pure
	public BusPrimitiveInvalidityType getType() {
		return this.type;
	}

	/** Replies the localized message of the reason.
	 *
	 * @return the localized message of the reason.
	 */
	@Pure
	public String getLocalizedMessage() {
		return this.type.getLocalizedMessage(this.additionalInformation != null
				? this.additionalInformation.toString() : ""); //$NON-NLS-1$
	}

}
