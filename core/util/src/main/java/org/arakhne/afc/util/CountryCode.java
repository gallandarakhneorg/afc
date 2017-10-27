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

package org.arakhne.afc.util;

import java.util.Locale;

import org.eclipse.xtext.xbase.lib.Pure;

/** This enumeration gives the official codes of the countries
 * given by the ISO 3166-1.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public enum CountryCode {

	/** AF.
	 */
	AFGHANISTAN("AF"), //$NON-NLS-1$
	/** AX.
	 */
	ALAND_ISLANDS("AX"), //$NON-NLS-1$
	/** AL.
	 */
	ALBANIA("AL"), //$NON-NLS-1$
	/** DZ.
	 */
	ALGERIA("DZ"), //$NON-NLS-1$
	/** AS.
	 */
	AMERICAN_SAMOA("AS"), //$NON-NLS-1$
	/** AD.
	 */
	ANDORRA("AD"), //$NON-NLS-1$
	/** AO.
	 */
	ANGOLA("AO"), //$NON-NLS-1$
	/** AI.
	 */
	ANGUILLA("AI"), //$NON-NLS-1$
	/** AQ.
	 */
	ANTARCTICA("AQ"), //$NON-NLS-1$
	/** AG.
	 */
	ANTIGUA_AND_BARBUDA("AG"), //$NON-NLS-1$
	/** AR.
	 */
	ARGENTINA("AR"), //$NON-NLS-1$
	/** AM.
	 */
	ARMENIA("AM"), //$NON-NLS-1$
	/** AW.
	 */
	ARUBA("AW"), //$NON-NLS-1$
	/** AU.
	 */
	AUSTRALIA("AU"), //$NON-NLS-1$
	/** AT.
	 */
	AUSTRIA("AT"), //$NON-NLS-1$
	/** AZ.
	 */
	AZERBAIJAN("AZ"), //$NON-NLS-1$

	/** BS.
	 */
	BAHAMAS("BS"), //$NON-NLS-1$
	/** BH.
	 */
	BAHRAIN("BH"), //$NON-NLS-1$
	/** BD.
	 */
	BANGLADESH("BD"), //$NON-NLS-1$
	/** BB.
	 */
	BARBADOS("BB"), //$NON-NLS-1$
	/** BY.
	 */
	BELARUS("BY"), //$NON-NLS-1$
	/** BE.
	 */
	BELGIUM("BE"), //$NON-NLS-1$
	/** BZ.
	 */
	BELIZE("BZ"), //$NON-NLS-1$
	/** BJ.
	 */
	BENIN("BJ"), //$NON-NLS-1$
	/** BM.
	 */
	BERMUDA("BM"), //$NON-NLS-1$
	/** BT.
	 */
	BHUTAN("BT"), //$NON-NLS-1$
	/** BO.
	 */
	BOLIVIA("BO"), //$NON-NLS-1$
	/** BQ.
	 */
	SAINT_EUSTATIUS_AND_SABA_BONAIRE("BQ"), //$NON-NLS-1$
	/** BA.
	 */
	BOSNIA_AND_HERZEGOVINA("BA"), //$NON-NLS-1$
	/** BW.
	 */
	BOTSWANA("BW"), //$NON-NLS-1$
	/** BV.
	 */
	BOUVET_ISLAND("BV"), //$NON-NLS-1$
	/** BR.
	 */
	BRAZIL("BR"), //$NON-NLS-1$
	/** IO.
	 */
	BRITISH_INDIAN_OCEAN_TERRITORY("IO"), //$NON-NLS-1$
	/** BN.
	 */
	BRUNEI_DARUSSALAM("BN"), //$NON-NLS-1$
	/** BG.
	 */
	BULGARIA("BG"), //$NON-NLS-1$
	/** BF.
	 */
	BURKINA_FASO("BF"), //$NON-NLS-1$
	/** BI.
	 */
	BURUNDI("BI"), //$NON-NLS-1$

	/** KH.
	 */
	CAMBODIA("KH"), //$NON-NLS-1$
	/** CM.
	 */
	CAMEROON("CM"), //$NON-NLS-1$
	/** CA.
	 */
	CANADA("CA"), //$NON-NLS-1$
	/** CV.
	 */
	CAPE_VERDE("CV"), //$NON-NLS-1$
	/** KY.
	 */
	CAYMAN_ISLANDS("KY"), //$NON-NLS-1$
	/** CF.
	 */
	CENTRAL_AFRICAN_REPUBLIC("CF"), //$NON-NLS-1$
	/** TD.
	 */
	CHAD("TD"), //$NON-NLS-1$
	/** CL.
	 */
	CHILE("CL"), //$NON-NLS-1$
	/** CN.
	 */
	CHINA("CN"), //$NON-NLS-1$
	/** CX.
	 */
	CHRISTMAS_ISLAND("CX"), //$NON-NLS-1$
	/** CC.
	 */
	COCOS_ISLANDS("CC"), //$NON-NLS-1$
	/** CO.
	 */
	COLOMBIA("CO"), //$NON-NLS-1$
	/** KM.
	 */
	COMOROS("KM"), //$NON-NLS-1$
	/** CG.
	 */
	CONGO("CG"), //$NON-NLS-1$
	/** CD.
	 */
	CONGO_DEMOCRATIC_REPUBLIC("CD"), //$NON-NLS-1$
	/** CK.
	 */
	COOK_ISLANDS("CK"), //$NON-NLS-1$
	/** CR.
	 */
	COSTA_RICA("CR"), //$NON-NLS-1$
	/** CI.
	 */
	COTE_D_IVOIRE("CI"), //$NON-NLS-1$
	/** HR.
	 */
	CROATIA("HR"), //$NON-NLS-1$
	/** CU.
	 */
	CUBA("CU"), //$NON-NLS-1$
	/** CW.
	 */
	CURACAO("CW"), //$NON-NLS-1$
	/** CY.
	 */
	CYPRUS("CY"), //$NON-NLS-1$
	/** CZ.
	 */
	CZECH_REPUBLIC("CZ"), //$NON-NLS-1$

	/** DK.
	 */
	DENMARK("DK"), //$NON-NLS-1$
	/** DJ.
	 */
	DJIBOUTI("DJ"), //$NON-NLS-1$
	/** DM.
	 */
	DOMINICA("DM"), //$NON-NLS-1$
	/** DO.
	 */
	DOMINICAN_REPUBLIC("DO"), //$NON-NLS-1$

	/** EC.
	 */
	ECUADOR("EC"), //$NON-NLS-1$
	/** EG.
	 */
	EGYPT("EG"), //$NON-NLS-1$
	/** SV.
	 */
	EL_SALVADOR("SV"), //$NON-NLS-1$
	/** GQ.
	 */
	EQUATORIAL_GUINEA("GQ"), //$NON-NLS-1$
	/** ER.
	 */
	ERITREA("ER"), //$NON-NLS-1$
	/** EE.
	 */
	ESTONIA("EE"), //$NON-NLS-1$
	/** ET.
	 */
	ETHIOPIA("ET"), //$NON-NLS-1$

	/** FK.
	 */
	FALKLAND_ISLANDS("FK"), //$NON-NLS-1$
	/** FO.
	 */
	FAROE_ISLANDS("FO"), //$NON-NLS-1$
	/** FJ.
	 */
	FIJI("FJ"), //$NON-NLS-1$
	/** FI.
	 */
	FINLAND("FI"), //$NON-NLS-1$
	/** FR.
	 */
	FRANCE("FR"), //$NON-NLS-1$
	/** GF.
	 */
	FRENCH_GUIANA("GF"), //$NON-NLS-1$
	/** PF.
	 */
	FRENCH_POLYNESIA("PF"), //$NON-NLS-1$
	/** TF.
	 */
	FRENCH_SOUTHERN_TERRITORIES("TF"), //$NON-NLS-1$

	/** GA.
	 */
	GABON("GA"), //$NON-NLS-1$
	/** GM.
	 */
	GAMBIA("GM"), //$NON-NLS-1$
	/** GE.
	 */
	GEORGIA("GE"), //$NON-NLS-1$
	/** DE.
	 */
	GERMANY("DE"), //$NON-NLS-1$
	/** GH.
	 */
	GHANA("GH"), //$NON-NLS-1$
	/** GI.
	 */
	GIBRALTAR("GI"), //$NON-NLS-1$
	/** GR.
	 */
	GREECE("GR"), //$NON-NLS-1$
	/** GL.
	 */
	GREENLAND("GL"), //$NON-NLS-1$
	/** GD.
	 */
	GRENADA("GD"), //$NON-NLS-1$
	/** GP.
	 */
	GUADELOUPE("GP"), //$NON-NLS-1$
	/** GU.
	 */
	GUAM("GU"), //$NON-NLS-1$
	/** GT.
	 */
	GUATEMALA("GT"), //$NON-NLS-1$
	/** GG.
	 */
	GUERNSEY("GG"), //$NON-NLS-1$
	/** GN.
	 */
	GUINEA("GN"), //$NON-NLS-1$
	/** GW.
	 */
	GUINEA_BISSAU("GW"), //$NON-NLS-1$
	/** GY.
	 */
	GUYANA("GY"), //$NON-NLS-1$

	/** HT.
	 */
	HAITI("HT"), //$NON-NLS-1$
	/** HM.
	 */
	HEARD_ISLAND_AND_MCDONALD_ISLANDS("HM"), //$NON-NLS-1$
	/** HN.
	 */
	HONDURAS("HN"), //$NON-NLS-1$
	/** HK.
	 */
	HONG_KONG("HK"), //$NON-NLS-1$
	/** HU.
	 */
	HUNGARY("HU"), //$NON-NLS-1$

	/** IS.
	 */
	ICELAND("IS"), //$NON-NLS-1$
	/** IN.
	 */
	INDIA("IN"), //$NON-NLS-1$
	/** ID.
	 */
	INDONESIA("ID"), //$NON-NLS-1$
	/** IR.
	 */
	IRAN("IR"), //$NON-NLS-1$
	/** IQ.
	 */
	IRAQ("IQ"), //$NON-NLS-1$
	/** IE.
	 */
	IRELAND("IE"), //$NON-NLS-1$
	/** IM.
	 */
	ISLE_OF_MAN("IM"), //$NON-NLS-1$
	/** IL.
	 */
	ISRAEL("IL"), //$NON-NLS-1$
	/** IT.
	 */
	ITALY("IT"), //$NON-NLS-1$

	/** JM.
	 */
	JAMAICA("JM"), //$NON-NLS-1$
	/** JP.
	 */
	JAPAN("JP"), //$NON-NLS-1$
	/** JE.
	 */
	JERSEY("JE"), //$NON-NLS-1$
	/** JO.
	 */
	JORDAN("JO"), //$NON-NLS-1$

	/** KZ.
	 */
	KAZAKHSTAN("KZ"), //$NON-NLS-1$
	/** KE.
	 */
	KENYA("KE"), //$NON-NLS-1$
	/** KI.
	 */
	KIRIBATI("KI"), //$NON-NLS-1$
	/** KP.
	 */
	KOREA_DEMOCRATIC_PEOPLE_REPUBLIC("KP"), //$NON-NLS-1$
	/** KR.
	 */
	KOREA_REPUBLIC("KR"), //$NON-NLS-1$
	/** KW.
	 */
	KUWAIT("KW"), //$NON-NLS-1$
	/** KG.
	 */
	KYRGYZSTAN("KG"), //$NON-NLS-1$

	/** LA.
	 */
	LAO("LA"), //$NON-NLS-1$
	/** LV.
	 */
	LATVIA("LV"), //$NON-NLS-1$
	/** LB.
	 */
	LEBANON("LB"), //$NON-NLS-1$
	/** LS.
	 */
	LESOTHO("LS"), //$NON-NLS-1$
	/** LR.
	 */
	LIBERIA("LR"), //$NON-NLS-1$
	/** LY.
	 */
	LIBYAN_ARAB_JAMAHIRIYA("LY"), //$NON-NLS-1$
	/** LI.
	 */
	LIECHTENSTEIN("LI"), //$NON-NLS-1$
	/** LT.
	 */
	LITHUANIA("LT"), //$NON-NLS-1$
	/** LU.
	 */
	LUXEMBOURG("LU"), //$NON-NLS-1$

	/** MO.
	 */
	MACAO("MO"), //$NON-NLS-1$
	/** MK.
	 */
	MACEDONIA("MK"), //$NON-NLS-1$
	/** MG.
	 */
	MADAGASCAR("MG"), //$NON-NLS-1$
	/** MW.
	 */
	MALAWI("MW"), //$NON-NLS-1$
	/** MY.
	 */
	MALAYSIA("MY"), //$NON-NLS-1$
	/** MV.
	 */
	MALDIVES("MV"), //$NON-NLS-1$
	/** ML.
	 */
	MALI("ML"), //$NON-NLS-1$
	/** MT.
	 */
	MALTA("MT"), //$NON-NLS-1$
	/** MH.
	 */
	MARSHALL_ISLANDS("MH"), //$NON-NLS-1$
	/** MQ.
	 */
	MARTINIQUE("MQ"), //$NON-NLS-1$
	/** MR.
	 */
	MAURITANIA("MR"), //$NON-NLS-1$
	/** MU.
	 */
	MAURITIUS("MU"), //$NON-NLS-1$
	/** YT.
	 */
	MAYOTTE("YT"), //$NON-NLS-1$
	/** MX.
	 */
	MEXICO("MX"), //$NON-NLS-1$
	/** FM.
	 */
	MICRONESIA("FM"), //$NON-NLS-1$
	/** MD.
	 */
	MOLDOVA("MD"), //$NON-NLS-1$
	/** MC.
	 */
	MONACO("MC"), //$NON-NLS-1$
	/** MN.
	 */
	MONGOLIA("MN"), //$NON-NLS-1$
	/** ME.
	 */
	MONTENEGRO("ME"), //$NON-NLS-1$
	/** MS.
	 */
	MONTSERRAT("MS"), //$NON-NLS-1$
	/** MA.
	 */
	MOROCCO("MA"), //$NON-NLS-1$
	/** MZ.
	 */
	MOZAMBIQUE("MZ"), //$NON-NLS-1$
	/** MM.
	 */
	MYANMAR("MM"), //$NON-NLS-1$

	/** NA.
	 */
	NAMIBIA("NA"), //$NON-NLS-1$
	/** NR.
	 */
	NAURU("NR"), //$NON-NLS-1$
	/** NP.
	 */
	NEPAL("NP"), //$NON-NLS-1$
	/** NL.
	 */
	NETHERLANDS("NL"), //$NON-NLS-1$
	/** NC.
	 */
	NEW_CALEDONIA("NC"), //$NON-NLS-1$
	/** NZ.
	 */
	NEW_ZEALAND("NZ"), //$NON-NLS-1$
	/** NI.
	 */
	NICARAGUA("NI"), //$NON-NLS-1$
	/** NE.
	 */
	NIGER("NE"), //$NON-NLS-1$
	/** NG.
	 */
	NIGERIA("NG"), //$NON-NLS-1$
	/** NU.
	 */
	NIUE("NU"), //$NON-NLS-1$
	/** NF.
	 */
	NORFOLK_ISLAND("NF"), //$NON-NLS-1$
	/** MP.
	 */
	NORTHERN_MARIANA_ISLANDS("MP"), //$NON-NLS-1$
	/** NO.
	 */
	NORWAY("NO"), //$NON-NLS-1$

	/** OM.
	 */
	OMAN("OM"), //$NON-NLS-1$

	/** PK.
	 */
	PAKISTAN("PK"), //$NON-NLS-1$
	/** PW.
	 */
	PALAU("PW"), //$NON-NLS-1$
	/** PS.
	 */
	PALESTINIAN_TERRITORY("PS"), //$NON-NLS-1$
	/** PA.
	 */
	PANAMA("PA"), //$NON-NLS-1$
	/** PG.
	 */
	PAPUA_NEW_GUINEA("PG"), //$NON-NLS-1$
	/** PY.
	 */
	PARAGUAY("PY"), //$NON-NLS-1$
	/** PE.
	 */
	PERU("PE"), //$NON-NLS-1$
	/** PH.
	 */
	PHILIPPINES("PH"), //$NON-NLS-1$
	/** PN.
	 */
	PITCAIRN("PN"), //$NON-NLS-1$
	/** PL.
	 */
	POLAND("PL"), //$NON-NLS-1$
	/** PT.
	 */
	PORTUGAL("PT"), //$NON-NLS-1$
	/** PR.
	 */
	PUERTO_RICO("PR"), //$NON-NLS-1$

	/** QA.
	 */
	QATAR("QA"), //$NON-NLS-1$

	/** RE.
	 */
	REUNION("RE"), //$NON-NLS-1$
	/** RO.
	 */
	ROMANIA("RO"), //$NON-NLS-1$
	/** RU.
	 */
	RUSSIAN_FEDERATION("RU"), //$NON-NLS-1$
	/** RW.
	 */
	RWANDA("RW"), //$NON-NLS-1$

	/** BL.
	 */
	SAINT_BARTHELEMY("BL"), //$NON-NLS-1$
	/** SH.
	 */
	SAINT_HELENA("SH"), //$NON-NLS-1$
	/** KN.
	 */
	SAINT_KITTS_AND_NEVIS("KN"), //$NON-NLS-1$
	/** LC.
	 */
	SAINT_LUCIA("LC"), //$NON-NLS-1$
	/** PM.
	 */
	SAINT_PIERRE_AND_MIQUELON("PM"), //$NON-NLS-1$
	/** VC.
	 */
	SAINT_VINCENT_AND_THE_GRENADINES("VC"), //$NON-NLS-1$
	/** WS.
	 */
	SAMOA("WS"), //$NON-NLS-1$
	/** SM.
	 */
	SAN_MARINO("SM"), //$NON-NLS-1$
	/** MF.
	 */
	SAINT_MARTIN_FRENCH_SIDE("MF"), //$NON-NLS-1$
	/** ST.
	 */
	SAO_TOME_AND_PRINCIPE("ST"), //$NON-NLS-1$
	/** SA.
	 */
	SAUDI_ARABIA("SA"), //$NON-NLS-1$
	/** SN.
	 */
	SENEGAL("SN"), //$NON-NLS-1$
	/** RS.
	 */
	SERBIA("RS"), //$NON-NLS-1$
	/** SC.
	 */
	SEYCHELLES("SC"), //$NON-NLS-1$
	/** SL.
	 */
	SIERRA_LEONE("SL"), //$NON-NLS-1$
	/** SG.
	 */
	SINGAPORE("SG"), //$NON-NLS-1$
	/** SX.
	 */
	SINT_MAARTEN_DUTCH_SIDE("SX"), //$NON-NLS-1$
	/** SK.
	 */
	SLOVAKIA("SK"), //$NON-NLS-1$
	/** SI.
	 */
	SLOVENIA("SI"), //$NON-NLS-1$
	/** SB.
	 */
	SOLOMON_ISLANDS("SB"), //$NON-NLS-1$
	/** SO.
	 */
	SOMALIA("SO"), //$NON-NLS-1$
	/** ZA.
	 */
	SOUTH_AFRICA("ZA"), //$NON-NLS-1$
	/** GS.
	 */
	SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS("GS"), //$NON-NLS-1$
	/** ES.
	 */
	SPAIN("ES"), //$NON-NLS-1$
	/** LK.
	 */
	SRI_LANKA("LK"), //$NON-NLS-1$
	/** SD.
	 */
	SUDAN("SD"), //$NON-NLS-1$
	/** SR.
	 */
	SURINAME("SR"), //$NON-NLS-1$
	/** SJ.
	 */
	SVALBARD_AND_JAN_MAYEN("SJ"), //$NON-NLS-1$
	/** SZ.
	 */
	SWAZILAND("SZ"), //$NON-NLS-1$
	/** SE.
	 */
	SWEDEN("SE"), //$NON-NLS-1$
	/** CH.
	 */
	SWITZERLAND("CH"), //$NON-NLS-1$
	/** SY.
	 */
	SYRIAN_ARAB_REPUBLIC("SY"), //$NON-NLS-1$

	/** TW.
	 */
	TAIWAN("TW"), //$NON-NLS-1$
	/** TJ.
	 */
	TAJIKISTAN("TJ"), //$NON-NLS-1$
	/** TZ.
	 */
	TANZANIA("TZ"), //$NON-NLS-1$
	/** TH.
	 */
	THAILAND("TH"), //$NON-NLS-1$
	/** TL.
	 */
	TIMOR_LESTE("TL"), //$NON-NLS-1$
	/** TG.
	 */
	TOGO("TG"), //$NON-NLS-1$
	/** TK.
	 */
	TOKELAU("TK"), //$NON-NLS-1$
	/** TO.
	 */
	TONGA("TO"), //$NON-NLS-1$
	/** TT.
	 */
	TRINIDAD_AND_TOBAGO("TT"), //$NON-NLS-1$
	/** TN.
	 */
	TUNISIA("TN"), //$NON-NLS-1$
	/** TR.
	 */
	TURKEY("TR"), //$NON-NLS-1$
	/** TM.
	 */
	TURKMENISTAN("TM"), //$NON-NLS-1$
	/** TC.
	 */
	TURKS_AND_CAICOS_ISLANDS("TC"), //$NON-NLS-1$
	/** TV.
	 */
	TUVALU("TV"), //$NON-NLS-1$

	/** UG.
	 */
	UGANDA("UG"), //$NON-NLS-1$
	/** UA.
	 */
	UKRAINE("UA"), //$NON-NLS-1$
	/** AE.
	 */
	UNITED_ARAB_EMIRATES("AE"), //$NON-NLS-1$
	/** GB.
	 */
	UNITED_KINGDOM("GB"), //$NON-NLS-1$
	/** US.
	 */
	UNITED_STATES("US"), //$NON-NLS-1$
	/** UM.
	 */
	UNITED_STATES_MINOR_OUTLYING_ISLANDS("UM"), //$NON-NLS-1$
	/** UY.
	 */
	URUGUAY("UY"), //$NON-NLS-1$
	/** UZ.
	 */
	UZBEKISTAN("UZ"), //$NON-NLS-1$

	/** VU.
	 */
	VANUATU("VU"), //$NON-NLS-1$
	/** VA.
	 */
	VATICAN_CITY_STATE("VA"), //$NON-NLS-1$
	/** VE.
	 */
	VENEZUELA("VE"), //$NON-NLS-1$
	/** VN.
	 */
	VIETNAM("VN"), //$NON-NLS-1$
	/** VG.
	 */
	VIRGIN_ISLANDS_BRITISH_SIDE("VG"), //$NON-NLS-1$
	/** VI.
	 */
	VIRGIN_ISLANDS_US_SIDE("VI"), //$NON-NLS-1$

	/** WF.
	 */
	WALLIS_AND_FUTUNA("WF"), //$NON-NLS-1$
	/** EH.
	 */
	WESTERN_SAHARA("EH"), //$NON-NLS-1$

	/** Y.
	 */
	YEMEN("Y"), //$NON-NLS-1$

	/** ZM.
	 */
	ZAMBIA("ZM"), //$NON-NLS-1$
	/** ZW.
	 */
	ZIMBABWE("ZW"); //$NON-NLS-1$

	private final String code;

	CountryCode(String isoCode) {
		this.code = isoCode.toLowerCase();
	}

	/** Replies the ISO 3166-1 code for the current country.
	 *
	 * @return the iso code for the country.
	 */
	@Pure
	public String getCode() {
		return this.code;
	}

	/** Replies the country code for the given locale.
	 *
	 * @param locale the locale.
	 * @return the country code
	 */
	@Pure
	public static CountryCode fromLocale(Locale locale) {
		final String c = locale.getCountry();
		for (final CountryCode cc : CountryCode.values()) {
			if (cc.code.equalsIgnoreCase(c)) {
				return cc;
			}
		}
		return null;
	}

	/** Replies the default locale for the country.
	 *
	 * @return the default locale for the country.
	 */
	@Pure
	public Locale getLocale() {
		return new Locale(this.code);
	}

	@Override
	@Pure
	public String toString() {
		return this.code;
	}

}
