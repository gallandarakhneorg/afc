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

package org.arakhne.afc.gis.road.primitive;

import static org.arakhne.afc.util.CountryCode.ANGUILLA;
import static org.arakhne.afc.util.CountryCode.ANTIGUA_AND_BARBUDA;
import static org.arakhne.afc.util.CountryCode.AUSTRALIA;
import static org.arakhne.afc.util.CountryCode.BAHAMAS;
import static org.arakhne.afc.util.CountryCode.BANGLADESH;
import static org.arakhne.afc.util.CountryCode.BARBADOS;
import static org.arakhne.afc.util.CountryCode.BERMUDA;
import static org.arakhne.afc.util.CountryCode.BHUTAN;
import static org.arakhne.afc.util.CountryCode.BOTSWANA;
import static org.arakhne.afc.util.CountryCode.BRUNEI_DARUSSALAM;
import static org.arakhne.afc.util.CountryCode.CAYMAN_ISLANDS;
import static org.arakhne.afc.util.CountryCode.CHRISTMAS_ISLAND;
import static org.arakhne.afc.util.CountryCode.COCOS_ISLANDS;
import static org.arakhne.afc.util.CountryCode.COOK_ISLANDS;
import static org.arakhne.afc.util.CountryCode.CYPRUS;
import static org.arakhne.afc.util.CountryCode.DOMINICA;
import static org.arakhne.afc.util.CountryCode.FALKLAND_ISLANDS;
import static org.arakhne.afc.util.CountryCode.FIJI;
import static org.arakhne.afc.util.CountryCode.GRENADA;
import static org.arakhne.afc.util.CountryCode.GUERNSEY;
import static org.arakhne.afc.util.CountryCode.GUYANA;
import static org.arakhne.afc.util.CountryCode.HONG_KONG;
import static org.arakhne.afc.util.CountryCode.INDIA;
import static org.arakhne.afc.util.CountryCode.INDONESIA;
import static org.arakhne.afc.util.CountryCode.IRELAND;
import static org.arakhne.afc.util.CountryCode.ISLE_OF_MAN;
import static org.arakhne.afc.util.CountryCode.JAMAICA;
import static org.arakhne.afc.util.CountryCode.JAPAN;
import static org.arakhne.afc.util.CountryCode.JERSEY;
import static org.arakhne.afc.util.CountryCode.KENYA;
import static org.arakhne.afc.util.CountryCode.KIRIBATI;
import static org.arakhne.afc.util.CountryCode.LESOTHO;
import static org.arakhne.afc.util.CountryCode.MACAO;
import static org.arakhne.afc.util.CountryCode.MALAWI;
import static org.arakhne.afc.util.CountryCode.MALAYSIA;
import static org.arakhne.afc.util.CountryCode.MALDIVES;
import static org.arakhne.afc.util.CountryCode.MALTA;
import static org.arakhne.afc.util.CountryCode.MAURITIUS;
import static org.arakhne.afc.util.CountryCode.MONTSERRAT;
import static org.arakhne.afc.util.CountryCode.MOZAMBIQUE;
import static org.arakhne.afc.util.CountryCode.NAMIBIA;
import static org.arakhne.afc.util.CountryCode.NAURU;
import static org.arakhne.afc.util.CountryCode.NEPAL;
import static org.arakhne.afc.util.CountryCode.NEW_ZEALAND;
import static org.arakhne.afc.util.CountryCode.NIUE;
import static org.arakhne.afc.util.CountryCode.NORFOLK_ISLAND;
import static org.arakhne.afc.util.CountryCode.PAKISTAN;
import static org.arakhne.afc.util.CountryCode.PAPUA_NEW_GUINEA;
import static org.arakhne.afc.util.CountryCode.PITCAIRN;
import static org.arakhne.afc.util.CountryCode.SAINT_HELENA;
import static org.arakhne.afc.util.CountryCode.SAINT_KITTS_AND_NEVIS;
import static org.arakhne.afc.util.CountryCode.SAINT_LUCIA;
import static org.arakhne.afc.util.CountryCode.SAINT_VINCENT_AND_THE_GRENADINES;
import static org.arakhne.afc.util.CountryCode.SAMOA;
import static org.arakhne.afc.util.CountryCode.SEYCHELLES;
import static org.arakhne.afc.util.CountryCode.SINGAPORE;
import static org.arakhne.afc.util.CountryCode.SOLOMON_ISLANDS;
import static org.arakhne.afc.util.CountryCode.SOUTH_AFRICA;
import static org.arakhne.afc.util.CountryCode.SRI_LANKA;
import static org.arakhne.afc.util.CountryCode.SURINAME;
import static org.arakhne.afc.util.CountryCode.SWAZILAND;
import static org.arakhne.afc.util.CountryCode.TANZANIA;
import static org.arakhne.afc.util.CountryCode.THAILAND;
import static org.arakhne.afc.util.CountryCode.TIMOR_LESTE;
import static org.arakhne.afc.util.CountryCode.TOKELAU;
import static org.arakhne.afc.util.CountryCode.TONGA;
import static org.arakhne.afc.util.CountryCode.TRINIDAD_AND_TOBAGO;
import static org.arakhne.afc.util.CountryCode.TURKS_AND_CAICOS_ISLANDS;
import static org.arakhne.afc.util.CountryCode.TUVALU;
import static org.arakhne.afc.util.CountryCode.UGANDA;
import static org.arakhne.afc.util.CountryCode.UNITED_KINGDOM;
import static org.arakhne.afc.util.CountryCode.VIRGIN_ISLANDS_BRITISH_SIDE;
import static org.arakhne.afc.util.CountryCode.VIRGIN_ISLANDS_US_SIDE;
import static org.arakhne.afc.util.CountryCode.ZAMBIA;
import static org.arakhne.afc.util.CountryCode.ZIMBABWE;

import java.util.Comparator;
import java.util.Locale;

import org.arakhne.afc.util.ArrayUtil;
import org.arakhne.afc.util.CountryCode;

/**
 * Legal side of the traffic of the vehicles on the road segments.
 *
 * <p>When left-side traffic direction rule is used, it is supposed that all
 * vehicles are going on the left side of the roads. For example,
 * this rule is used in UK.
 *
 * <p><img src="./doc-files/traffic_side_map.png" alt="World map of the traffic sides" width="100%">
 * <br><small>Red: drives on right; Blue: drive on left.</small>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public enum LegalTrafficSide {

	/** Vehicles are moving on left side of the roads.
	 */
	LEFT,

	/** Vehicles are moving on right side of the roads.
	 */
	RIGHT;

	private static Object[] leftSide = {
		ANGUILLA,
		ANTIGUA_AND_BARBUDA,
		AUSTRALIA,
		BAHAMAS,
		BANGLADESH,
		BARBADOS,
		BERMUDA,
		BHUTAN,
		BOTSWANA,
		BRUNEI_DARUSSALAM,
		CAYMAN_ISLANDS,
		CHRISTMAS_ISLAND,
		COOK_ISLANDS,
		CYPRUS,
		DOMINICA,
		TIMOR_LESTE,
		FALKLAND_ISLANDS,
		FIJI,
		GRENADA,
		GUERNSEY,
		GUYANA,
		HONG_KONG,
		INDIA,
		INDONESIA,
		IRELAND,
		ISLE_OF_MAN,
		JAMAICA,
		JAPAN,
		JERSEY,
		KENYA,
		KIRIBATI,
		COCOS_ISLANDS,
		LESOTHO,
		MACAO,
		MALAWI,
		MALAYSIA,
		MALDIVES,
		MALTA,
		MAURITIUS,
		MONTSERRAT,
		MOZAMBIQUE,
		NAMIBIA,
		NAURU,
		NEPAL,
		NEW_ZEALAND,
		NIUE,
		NORFOLK_ISLAND,
		PAKISTAN,
		PAPUA_NEW_GUINEA,
		PITCAIRN,
		SAINT_HELENA,
		SAINT_KITTS_AND_NEVIS,
		SAINT_LUCIA,
		SAINT_VINCENT_AND_THE_GRENADINES,
		SAMOA,
		SEYCHELLES,
		SINGAPORE,
		SOLOMON_ISLANDS,
		SOUTH_AFRICA,
		SRI_LANKA,
		SURINAME,
		SWAZILAND,
		TANZANIA,
		THAILAND,
		TOKELAU,
		TONGA,
		TRINIDAD_AND_TOBAGO,
		TURKS_AND_CAICOS_ISLANDS,
		TUVALU,
		UGANDA,
		UNITED_KINGDOM,
		VIRGIN_ISLANDS_BRITISH_SIDE,
		VIRGIN_ISLANDS_US_SIDE,
		ZAMBIA,
		ZIMBABWE,
	};

	private static final Comparator<Object> COMPARATOR = (Object o1, Object o2) -> {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		final String n1 = o1.toString();
		final String n2 = o2.toString();
		return n1.compareToIgnoreCase(n2);
	};

	/** Replies the legal traffic side according to your current locale.
	 *
	 * @return the legal traffic side according to your current locale.
	 */
	public static LegalTrafficSide getCurrent() {
		return get(Locale.getDefault());
	}

	/** Replies the legal traffic side according to the given locale.
	 *
	 * @param locale the locale.
	 * @return the legal traffic side according to the given locale.
	 */
	public static LegalTrafficSide get(Locale locale) {
		final String country = locale.getCountry();
		if (ArrayUtil.contains(COMPARATOR, country, leftSide)) {
			return LEFT;
		}
		return RIGHT;
	}

	/** Replies the legal traffic side according to the given country code.
	 *
	 * @param country the country code.
	 * @return the legal traffic side according to the given country code.
	 */
	public static LegalTrafficSide get(CountryCode country) {
		if (ArrayUtil.contains(COMPARATOR, country, leftSide)) {
			return LEFT;
		}
		return RIGHT;
	}

}
