/*
 * $Id$
 * 
 * Copyright (c) 2008-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.network;

/**
 * Type of a JaSIM network message.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum NetworkMessageType {

	/** "I'am a controller"
	 */
	IAMCONTROLLER(1),
	/** "I'am a viewer"
	 */
	IAMVIEWER(2),
	/** "I'am both"
	 */
	IAMBOTH(3),
	/** "Bye"
	 */
	BYE(4),
	/** "Kill the simulator"
	 */
	KILL_SIMULATOR(5),
	
	/** INIT.
	 */
	INIT(50),
	/** PLAY.
	 */
	PLAY(51),
	/** STEP.
	 */
	STEP(52),
	/** PAUSE.
	 */
	PAUSE(53),
	/** STOP.
	 */
	STOP(54),
	/** ADD_PROBE.
	 */
	ADD_PROBE(55),
	/** REMOVE_PROBE.
	 */
	REMOVE_PROBE(56),
	/** SET_SIMULATION_DELAY.
	 */
	SET_SIMULATION_DELAY(57),
	
	/** START.
	 */
	START(102),
	/** ADDITION.
	 */
	ADDITION(103),
	/** DELETION.
	 */
	DELETION(104),
	/** ACTION.
	 */
	ACTION(105),
	/** PROVE.
	 */
	PROBE(106),
	/** END.
	 */
	END(107),
	/** KILLED.
	 */
	KILLED(108),
	/** IDDLE.
	 */
	IDDLE(109);

	private final int jasimCode;	
	
	private NetworkMessageType(int code) {
		this.jasimCode = code;
	}
	
	/** Replies the message type which is corresponding to the given JaSIM code.
	 * 
	 * @param code
	 * @return the message type or <code>null</code>
	 */
	public static NetworkMessageType fromJaSIMCode(int code) {
		for(NetworkMessageType type : values()) {
			if (type.jasimCode==code)
				return type;
		}
		return null;
	}
	
	/** Replies the JaSIM code for this message type.
	 * 
	 * @return the JaSIM code
	 */
	public int toJaSIMCode() {
		return this.jasimCode;
	}

}

