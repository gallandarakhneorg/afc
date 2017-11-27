/*
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.io.sfg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Types of SFG file.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
enum SFGFileType {

	/** SFG 3D DTD 8.0.
	 */
	V8_3D(	"-//set.utbm.fr//DTD JaSimConfigurationFile3d v8.0//EN",  //$NON-NLS-1$
			"/fr/utbm/set/jasim/controller/config/jasim-config-3d-8.0.dtd",  //$NON-NLS-1$
			8,
			0,
			true),

	/** SFG 3D DTD 7.0.
	 */
	V7_3D(	"-//set.utbm.fr//DTD JaSimConfigurationFile3d v7.0//EN",  //$NON-NLS-1$
			"/fr/utbm/set/jasim/controller/config/jasim-config-3d-7.0.dtd",  //$NON-NLS-1$
			7,
			0,
			true),

	/** SFG 1.5D DTD 7.0.
	 */
	V7_1D5(	"-//set.utbm.fr//DTD JaSimConfigurationFile1d5 v7.0//EN",  //$NON-NLS-1$
			"/fr/utbm/set/jasim/controller/config/jasim-config-1d5-7.0.dtd",  //$NON-NLS-1$
			7,
			0,
			false),

	/** SFG 3D DTD 6.0.
	 */
	V6_3D(	"-//set.utbm.fr//DTD JaSimConfigurationFile3d v6.0//EN",  //$NON-NLS-1$
			"/fr/utbm/set/jasim/controller/config/jasim-config-3d-6.0.dtd",  //$NON-NLS-1$
			6,
			0,
			true),

	/** SFG 1.5D DTD 6.0.
	 */
	V6_1D5(	"-//set.utbm.fr//DTD JaSimConfigurationFile1d5 v6.0//EN",  //$NON-NLS-1$
			"/fr/utbm/set/jasim/controller/config/jasim-config-1d5-6.0.dtd",  //$NON-NLS-1$
			6,
			0,
			false);

	/** Replies the file type which is corresponding to the given system identifier.
	 * 
	 * @param systemId
	 * @return the file type or <code>null</code>.
	 */
	public static SFGFileType fromSystemId(String systemId) {
		for(SFGFileType type : SFGFileType.values()) {
			if (type.isSystemIdentifier(systemId)) {
				return type;
			}
		}
		return null;
	}

	private final String publicId;
	private final String systemId;
	private final int majorVersion;
	private final int minorVersion;
	private final boolean is3d;
	
	private SFGFileType(String publicId, String systemId, int majorVersion, int minorVersion, boolean is3d) {
		this.publicId = publicId;
		this.systemId = systemId;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.is3d = is3d;
	}
	
	/** Replies if this SFG file type may be parsed by 3D parser.
	 * 
	 * @return <code>true</code> if the 3d parser may be used, otherwise <code>false</code>.
	 */
	public boolean is3D() {
		return this.is3d;
	}
	
	/** Replies if this SFG file type may be parsed by 2D parser.
	 * 
	 * @return <code>true</code> if the 2d parser may be used, otherwise <code>false</code>.
	 */
	@SuppressWarnings("static-method")
	public boolean is2D() {
		return false;
	}

	/** Replies if this SFG file type may be parsed by 1.5D parser.
	 * 
	 * @return <code>true</code> if the 1.5d parser may be used, otherwise <code>false</code>.
	 */
	public boolean is1D5() {
		return !this.is3d;
	}

	/** Replies the public identifier.
	 * 
	 * @return the public identifier.
	 */
	public String getPublicIdentifier() {
		return this.publicId;
	}

	/** Replies the system identifier.
	 * 
	 * @return the system identifier.
	 */
	public String getSystemIdentifier() {
		return this.systemId;
	}
	
	/** Replies the supported major version.
	 * 
	 * @return the supported major version.
	 */
	public int getSupportedMajorVersion() {
		return this.majorVersion;
	}
	
	/** Replies the supported minor version.
	 * 
	 * @return the supported minor version.
	 */
	public int getSupportedMinorVersion() {
		return this.minorVersion;
	}

	/** Replies if the given system identifier is corresponding to
	 * the systme identifier of this type.
	 * 
	 * @param id
	 * @return <code>true</code> if the given identifier corresponds to
	 * the system identifier of this type, otherwise <code>false</code>.
	 */
	public boolean isSystemIdentifier(String id) {
		//SystemId: /fr/utbm/set/jasim/controller/config/jasim-config-3d-8.0.dtd
		//SystemId: file:/fr/utbm/set/jasim/controller/config/jasim-config-3d-8.0.dtd
		//SystemId: file://fr/utbm/set/jasim/controller/config/jasim-config-3d-8.0.dtd
		//SystemId: file:///fr/utbm/set/jasim/controller/config/jasim-config-3d-8.0.dtd
		if (id==null) return false;
		Pattern pattern = Pattern.compile("^file[:][/]+", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$
		Matcher matcher = pattern.matcher(id);
		String filteredId = matcher.replaceFirst("/"); //$NON-NLS-1$
		return this.systemId.equalsIgnoreCase(filteredId);
	}

}
