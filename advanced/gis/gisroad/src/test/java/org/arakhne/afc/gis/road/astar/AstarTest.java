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

package org.arakhne.afc.gis.road.astar;

import java.lang.ref.SoftReference;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.gis.io.shape.GISShapeFileReader;
import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.road.AbstractGisTest;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.path.RoadPath;
import org.arakhne.afc.gis.road.path.astar.RoadAStar;
import org.arakhne.afc.gis.road.path.astar.RoadAStarDistanceHeuristic;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.graph.astar.AStarHeuristic;
import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.Resources;

/** Unit test dedicated to A* search algorithm on road network.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class AstarTest extends AbstractGisTest {

	private final Object[][] DATA_SET = new Object[][] {
		new Object[] {
				//start
				"27.891204338983396;0.0/97ea0d3cbf58f3879260f466f9171845#940850;2304409;941406;2304554", //$NON-NLS-1$
				//end
				"66.1357978505595;0.0/884cc9213dd9b94fa281b1b054397cb5#939075;2304480;939195;2304536", //$NON-NLS-1$
				//inverted path: this data set is found as inverted from the case study (Carpooling)
				"884cc9213dd9b94fa281b1b054397cb5#939075;2304480;939195;2304536, 474fd08971ec9a2b84943cce94498ce9#939075;2304451;939103;2304481, 5d194e79a2a66194fc4e1fd30e104219#939102;2304384;939132;2304452, 05439f01253b4ee67857d3fdb49f66a6#939131;2304342;939142;2304385, 13dd428b94c4804eb622880d23dc6c42#939141;2304297;939179;2304343, 7c547560c1090fe90ba2ab9fc88da8f5#939178;2304281;939195;2304298, b8ca65cbb0b6b9ea8d6a04ce93c89f58#939194;2304281;939336;2304323, 6f70e09cfc9591c03004008551281def#939335;2304304;939347;2304323, bf3a7f6906f6d1067b9552958d245458#939346;2304297;939349;2304304, be9d81ea56a8f0dce9396bb1a7a9c09f#939348;2304297;939359;2304301, fbe9bd5b19eed77f17c39f3d0de7f4a7#939358;2304301;939387;2304312, 2674662f3da70bb838197f8c04a51b83#939386;2304311;939396;2304315, c2d8c2bbd87fb8f3bbccb2c43957b8de#939395;2304314;939424;2304327, 8deeed62a7e5967a80f6b7d85733225d#939423;2304326;939579;2304389, c076d25075aae26d050825af4c5c3f58#939578;2304388;939623;2304407, c8da47ac97bb09409c064a428aefabee#939623;2304406;939670;2304425, e29c3270fe84f7ac5f66a03e708e8477#939669;2304425;939721;2304446, 9a30f0f7e14780959f9183b758541464#939720;2304445;939820;2304489, 2b2d1b0f44a67b0117f3c6f7ae17b148#939819;2304489;939822;2304513, 39f6aca26e2b8957b6a4f4abf8ab02e0#939821;2304512;939899;2304515, a6a05c930d50f53e82cc519153125f18#939898;2304515;939958;2304533, f4e289bf6bb27fab17666cfee3a2f047#939957;2304443;939993;2304533, 0cc562e508f33da26b06c51e68337a01#939992;2304408;940034;2304444, d418aa1cf06fad62ae2d83e54318437a#940033;2304391;940055;2304409, 9fd9fd08f12d36f6bd3d5a306e02474c#940054;2304328;940244;2304392, 3c8eabd4084b5d8a242186be6822f738#940243;2304342;940284;2304361, bcb8ec13810d597e6f82817de37da3ae#940283;2304361;940371;2304378, d3565838ba0e88541ae68d1b57276695#940370;2304343;940415;2304377, b464ace3901e39809c73d7622f6375e7#940414;2304343;940842;2304491, 6c740d5628e0d08d358ee25ddb0dac08#940841;2304459;940850;2304464, 97ea0d3cbf58f3879260f466f9171845#940850;2304409;941406;2304554", //$NON-NLS-1$
				//not inverted path: this data set is found as not inverted from the case study (Carpooling)
				null,
		},
		new Object[] {
				//start
				"33.89356484460727;0.0/633a8786745bb1971d13fb27b671c503#938914;2297484;938929;2297553", //$NON-NLS-1$
				//end
				"65.43934173734891;0.0/ce3a41e908432bf2add2cea827f882ac#937736;2299448;937890;2299634", //$NON-NLS-1$
				//inverted path: this data set is found as inverted from the case study (Carpooling)
				"ce3a41e908432bf2add2cea827f882ac#937736;2299448;937890;2299634, 6cfc457ba5391b7771d5e56c2c7ba349#937706;2299447;937737;2299448, 97063f6f42bd557cf7b3314d1cee9dba#937610;2299448;937706;2299550, b031ef9575b1a46331b756278da4c28c#937516;2299539;937611;2299550, 02bdca6b2229b8fc16bb47248281f2a1#937505;2299443;937525;2299540, eaeb09a9e0a4302ea295e6b769c1eb4d#937489;2299389;937506;2299443, b1908ff1bec189114a143574a6f1b7f4#937477;2299354;937491;2299390, 7adbce12cc2ab140c274f45b6ae27310#937459;2299301;937478;2299355, 10b6c3b23a67f2e2397aa6d9fb5234b7#937424;2299301;937460;2299314, 326227b408ac901ba1df67f95b54b742#937416;2299288;937425;2299314, 9a7b2608b046e5fd162b2f339fe04d01#937410;2299188;937420;2299288, 0da6abb4795ec614cf4032c87c69eb09#937407;2299158;937411;2299189, 6743c177c915bd20ea6c00714ed30f08#937334;2299013;937407;2299158, 9bc049001eb86f6e245ed8d9c9104629#937188;2298825;937335;2299014, e7c45081a7ff2809550a61b06387a357#937142;2298779;937188;2298826, 26d7ea42d7d370f8dd1d7fab3dd7c9ce#937142;2298681;937181;2298780, cdf70b141e3226e5f6713e81aa6c3071#937155;2298566;937181;2298682, 479ccef393a8d5b926d9ec6cb32124af#937159;2298551;937160;2298566, f055ea64bb24a90a789d453cd7710451#937149;2298479;937160;2298552, 021e6c0a8ce8ae038933e433a030f2c6#937149;2298325;937542;2298479, d59c774a600c0ccd8d839e61448017ed#937535;2298308;937542;2298326, 2dced596f1c5fc629c4e145ab576ea48#937512;2298263;937536;2298308, 80d9a38d8fe71aff5ffb75f149b7a983#937506;2298251;937513;2298264, eecc716aab8558ade815992af677f1ff#937506;2298250;937942;2298297, 9d4a3fb665bc1c1f69213aae107d69f5#937941;2298289;938059;2298346, 1a7bce6aaf6ad2e1234939f93e0e0ba0#938058;2298345;938183;2298404, 132d8bcf3b53e7991fe75482d18c7b97#938182;2298403;938237;2298414, 17584986d515ffba5780d30fbae92d8c#938236;2298377;938473;2298422, 61eea60adb101440a874c3b8708058ad#938472;2298340;938525;2298378, 955c68524c03a46699a38ff560bf4d1c#938524;2298131;938640;2298341, cb46eead1e475cd95f47ef3e2ab3bfb9#938639;2298131;938682;2298140, 56358ff50b7a50c8103e4e16f2800a25#938681;2297783;938940;2298140, 21e0b13786224d821d50ab251afc507b#938903;2297755;938928;2297783, 414d2d67c2aa9237606b25e20648d028#938834;2297660;938903;2297755, 2b994e1b5fcd95561edb71ee7b4232bb#938702;2297506;938835;2297661, 038f7e34de1a033734b2717e6eec994c#938697;2297495;938703;2297506, fb32fcf957e2b839f898d389fa04dde9#938697;2297495;938717;2297504, 0ed5a7c93a7c105afdd5cfc30cca61f2#938716;2297503;938793;2297533, dba4253953ac76ebb66d0f3058f82eb0#938792;2297533;938881;2297547, 5db5fc68d7adee1944f249e0bb1068e5#938880;2297546;938915;2297553, 633a8786745bb1971d13fb27b671c503#938914;2297484;938929;2297553", //$NON-NLS-1$
				//not inverted path: this data set is found as not inverted from the case study (Carpooling)
				null,
		},
		new Object[] {
				//start
				"112.89690046599802;0.0/d99885b81112518c8f755d89ca75ef4e#942750;2304944;942902;2305180", //$NON-NLS-1$
				//end
				"251.89968923374454;0.0/782b7d53c10d6c57fbaa6f7edcc1737b#941027;2303396;941334;2303530", //$NON-NLS-1$
				//inverted path: this data set is found as inverted from the case study (Carpooling)
				"782b7d53c10d6c57fbaa6f7edcc1737b#941027;2303396;941334;2303530, f806369c856a9ca8d9d0e74cc722004a#941333;2303475;941374;2303530, 3690f1b45d1cd4956095731a8f083e2e#941373;2303475;941390;2303479, e690806b534b0fe103ab0ea474e13762#941389;2303463;941399;2303478, 0fbe9a7a02b5ecf05ac5cc64fc9e9856#941394;2303451;941399;2303464, a843a03dd1cc40d9f89f28b87b442524#941394;2303361;941496;2303452, b90341c0e9d360f1084e41c01d1c3a18#941495;2303321;941522;2303364, ec2326d5c917b721e3f4cd2bfdf7e462#941521;2303285;941553;2303321, 065df768ac7b6676f162e3c2ce7e5560#941552;2303285;941699;2303450, d0537d06abc5b2cee34b671c9e59283d#941698;2303405;941736;2303450, 9921671f1ae98fb68392d14ddcc572cd#941735;2303103;942084;2303406, 4585c6ae05e6fdedd5691dbc5601fd2d#942083;2303086;942218;2303103, de7e73503f1a9c53236dabde9899c59c#942212;2303086;942219;2303157, 25f0bef21dac973a567a7d7a1ec842c7#942209;2303156;942222;2303230, d5b3395fd076a67ef164f8ee9ca68d05#942221;2303230;942256;2303305, a398f70f2fe8457a4f160ec0ec3025b9#942255;2303305;942267;2303332, 65edea28d1604846f7e547a66d335100#942266;2303331;942342;2303507, 232e0a3162843eea57431dbd67a5053b#942341;2303506;942408;2303591, 9c5401243f1f69f7c8292f4448a3e2cf#942407;2303591;942532;2303678, e30fdd699f3d194b7ef934c259dfa074#942531;2303677;942549;2303766, 608c54f5132ff25c742d71cab6bb5c6b#942547;2303765;942549;2303780, 847a39c3cb8ac45671f6ec662104b73e#942522;2303779;942548;2303847, d16ef1b3621dc7e1404672af7f2ce5a1#942520;2303846;942536;2303967, d4948b1d3ed90b9e1f611ca60044983f#942534;2303966;942732;2304463, 82934cbaca211a290f62dbc6f4ba50da#942731;2304463;942737;2304514, 56e1628820ccc20d4a793753c505e746#942640;2304513;942733;2304570, 7a20f861fedb0f2176443f61142e3c77#942613;2304570;942641;2304589, 5848e0ebbd518aa1ab52fdd3ff1c1887#942599;2304589;942614;2304649, e5813f48510f9260d444a6ddce8b76fa#942574;2304648;942600;2304690, 58ff04ad9bd262921fc92d3618aadc1a#942536;2304689;942575;2304743, 81e6537771cf3dfdb9f3aa79c2a84061#942513;2304743;942537;2304776, b0da9351244cff50823d4e7cc7665876#942467;2304775;942514;2304844, ee164f83c96fed3b282d7fda2e6fc078#942467;2304844;942522;2304878, 1d42992d66aad9ef846a89f8aa6ddd40#942521;2304877;942530;2304886, a188c4650aa8b1777f0cdff810e29e07#942529;2304885;942626;2305065, f1a182bc020864dcae29f6c0158ac7c4#942625;2305060;942752;2305136, 987817f6fc23c287e735cc00a20e6c80#942747;2305135;942752;2305151, 154271590a28c642c9b1f607535a58bb#942747;2305151;942867;2305173, c5097c54e325a10d523a34a5624cf798#942866;2305172;942898;2305180, d99885b81112518c8f755d89ca75ef4e#942750;2304944;942902;2305180", //$NON-NLS-1$
				//not inverted path: this data set is found as not inverted from the case study (Carpooling)
				null,
		},
		new Object[] {
				//start
				"93.89719798319005;0.0/96aa0c8f455dd152093aeb0e711fe925#939852;2296646;939923;2296730", //$NON-NLS-1$
				//end
				"500.055347547909;0.0/8dccf3182963f3370ecbec9eae094db5#941509;2295438;942272;2295702", //$NON-NLS-1$
				//inverted path: this data set is found as inverted from the case study (Carpooling)
				"8dccf3182963f3370ecbec9eae094db5#941509;2295438;942272;2295702, b48575e3f7f885032427f618e909579d#941471;2295489;941865;2295954, af2a068e9b42ce35afbc59b8bff08555#941858;2295953;941870;2295999, 31a86bf1a2c4ba364a91c4450325f06f#941806;2295998;941859;2296016, 71e5ebfae8afefbe1a5298e8cdfcc646#941780;2296016;941807;2296051, 1041300ca5779011aa7681958493934f#941780;2296051;941792;2296057, 2b8129da211be7ea0468a33e3342e148#941790;2296057;941795;2296275, ac7287b5313d404bdab0cb9c12d41fe7#941778;2296274;941794;2296411, f175751daa8ba2fd08ed1e3bae27194c#941746;2296410;941781;2296416, 05fe942969c070be892ac547ab0b6be0#941746;2296414;941793;2296595, 0e76adf92aa683e53c6ff866b75ce530#941773;2296595;941804;2296751, 6073d949ada00f0a4622c4d9cd83c27d#941803;2296750;941856;2296863, 359bb0592cec402a5f0793b391b1c4b1#941855;2296863;941949;2297034, 55c90e32dfdfbd13e6461a38117a16b9#941830;2297034;941935;2297156, 79fb85956d34319824a00b42850f7edb#941836;2297155;941946;2297202, f575e846104b1f46f672497ca96ae09e#941674;2297200;941946;2297262, ee09b79d609a6bc734fdca59c75e4b59#941618;2297200;941675;2297285, b2ec7a245d6d7343153c847ae6196070#941610;2297284;941619;2297331, fe8f4832932f43d83c103d499804fcc4#941576;2297316;941611;2297331, f9f954608a21de24d5edb3b7a35797fd#941458;2297279;941576;2297317, 4150b2335b9173be93dc093f0363244c#941455;2297279;941459;2297280, 223eec5bd19852cb7251c656659b2032#941436;2297257;941456;2297280, ae4f2abbacf4551ce518b7b1d93c1c69#941355;2297114;941437;2297258, 83ea9b83d5feb897657f79811c2482f4#941345;2297098;941356;2297115, 450a5847663fc75c19920aae4b0b0083#941320;2297071;941346;2297099, c48f0f1846bf363a4b5e6d71e6020610#941298;2297052;941321;2297072, 59c11641e10d44364b6444ba77732f49#941203;2296935;941299;2297053, 6e853bda0a35fe705342e295f1439510#941201;2296920;941204;2296936, c19ac3d00968b399a78a3fbd551d219d#941200;2296886;941202;2296921, b79b44a0ed4de34bbf06ef6c3e949b61#941127;2296858;941202;2296886, b7cba434bee300a93c6ef9a58bf9ec0d#941062;2296841;941128;2296859, 93711e810cbc50aeac5b7815bfffab61#940617;2296813;941063;2296842, 08c522cba3b1e6e3c5f2999cb429bae7#940607;2296836;940618;2296839, bd6e955967a67d1026153fc5da2191e3#940425;2296838;940608;2296896, 951555fa1bd9ebe325861e2797be6d8a#940415;2296895;940426;2296899, b68ef56933ed5f76938dcd6d5067e8b0#940323;2296899;940416;2296933, e3b5989172a2cf08a8cf07b231c265ff#940268;2296931;940324;2296948, c681a8398d316ce6e793eeb306d9f007#940199;2296947;940269;2296975, a5ac86e42dfd29b09234f1176dd148bc#940184;2296974;940199;2296980, 52be8ee2e364bb7fbfa42b88a8806d44#940102;2296978;940185;2296987, f7c2ee2cab657c42d5960d44afd7e157#940069;2296960;940103;2296978, 5f00349b042636ec82f8ff9e1899e950#940004;2296904;940070;2296961, e56ca91f8e4e7efa7bb5f935dd9cedab#939935;2296877;940005;2296904, 2b81ed01a20b55944182b803d8252d80#939928;2296877;939936;2296886, 91043274ca6a795d3a86d9106d5752bf#939914;2296882;939929;2296886, 45ee64cc8d7a8e66e3d0cf282c9d55df#939911;2296875;939915;2296883, 4e86112f902e90b4de79179654cce09e#939871;2296748;939912;2296876, 8568d6c3fdd6b359eb95fdf40d43051b#939851;2296743;939872;2296748, b1de683103481f2acba250f23d5c2776#939843;2296741;939852;2296744, b95b5d369382d013f7e1cb269bc13f97#939843;2296729;939854;2296742, 96aa0c8f455dd152093aeb0e711fe925#939852;2296646;939923;2296730", //$NON-NLS-1$
				//not inverted path: this data set is found as not inverted from the case study (Carpooling)
				null,
		},
		new Object[] {
				//start
				"530.5349311668696;0.0/166f54332bfffbafec99bc0ed5ba0fa7#936047;2297925;936487;2298151", //$NON-NLS-1$
				//end
				"35.585710429397196;0.0/90ff0612780901c27134d1fdc65220e7#936531;2299837;936677;2300553", //$NON-NLS-1$
				//inverted path: this data set is found as inverted from the case study (Carpooling)
				null,
				//not inverted path: this data set is found as not inverted from the case study (Carpooling)
				"166f54332bfffbafec99bc0ed5ba0fa7#936047;2297925;936487;2298151, ce0872fc7d045d399f0453e9e0f44a45#935953;2297925;936048;2298023, 8d27359bf04675754b28b62923c3edd1#935949;2298022;935953;2298031, e001acf2e21662270123411b193426f6#935936;2298030;935950;2298049, e95a704df350401f2705b1f4889bcb62#935916;2298048;935937;2298106, bff27e325d51e68dff9b98edb8067049#935916;2298105;935979;2298138, 86b3a86430e97852f480c84dee1c6bdb#935979;2298137;936051;2298184, 4aa7e62e35b98b980e8b6a608cbe0cf5#936045;2298183;936059;2298301, 7953d0be3633c89d0127b0c80f9b8e14#936036;2298300;936046;2298341, 82b6edf01f3141fb711d7a4c0705e54c#936026;2298340;936037;2298392, 212cc77a6a9a21dcf5b4507912a035c3#936026;2298392;936039;2298398, e5ddf596588153e5ce360aceca23434f#936038;2298397;936048;2298421, 87b43810bbcbedbc3c61bfc0e3dca065#936047;2298420;936193;2298458, cb8261ca824cf8d710b4b621dd367d37#936192;2298457;936375;2298527, eb11aa8020adc2dc1680ddcc23e41750#936374;2298527;936412;2298545, eda88af9c598136eab308f3aa4129729#936411;2298544;936454;2298556, 05bca72306ebd78e527cd21768405e48#936453;2298553;936481;2298571, ebd35e1b9fe8da02dc10593213442bed#936481;2298570;936511;2298597, 7ac30018e60987999ad8a4897a606cfe#936510;2298596;936534;2298610, efc9dd79b9bde80f572df507dcad1595#936534;2298610;936586;2298638, 96f54c06f6d9ccd650272942754ace4c#936585;2298638;936800;2298744, 65bba304533b594517e31bd69249b813#936798;2298743;936803;2298752, d9d60f662d2f5b17d062082c32fe714f#936803;2298751;936809;2298753, 92e8a36d2cb8b525ffbacb67c94b1645#936808;2298752;936940;2298795, b2f63c20d415dda2fc08611ad997ed64#936939;2298794;936996;2298808, f2944f68f48fcc43bc36194f69b8eddf#936995;2298807;937073;2298884, 9f5c651b5a82a1a609fccba1f2676556#937072;2298883;937087;2298905, e2472d90f4cfc1ea316bf604bcfab9d2#937086;2298904;937131;2298984, 786b64c196dff67e86eda533443c59c0#937130;2298983;937135;2298988, a217be463fbe048fa4fa6c06f98ba324#937134;2298988;937155;2299009, c74e6bc0cd176e61405fe41607af02f7#937154;2299009;937216;2299056, 42ae52ca732022c1f4330204fc525942#937208;2299055;937216;2299171, 2b10842f2e0662bddc60d571b156cad7#937170;2299170;937214;2299282, 6f050ea461ebfd0d86638ca6d8237502#937128;2299281;937171;2299314, 12af86f09fa0f076467d884f4cd0fb80#936990;2299313;937129;2299376, c0b7532cfa52544ecd0ba2a8c78f0af2#936774;2299376;936991;2299590, 21ad8dd0a07ba2092eda10a28584b391#936667;2299589;936775;2299750, 6abe3997d3bf3a5b4fa271595362be2c#936654;2299749;936668;2299838, 90ff0612780901c27134d1fdc65220e7#936531;2299837;936677;2300553", //$NON-NLS-1$
		}
	};
	
	@Before
	public void setUp() {
		GeoLocationUtil.setGISCoordinateSystemAsDefault();
	}

	private SoftReference<RoadNetwork> ROAD_NETWORK = null;

	private RoadNetwork getRoadNetwork() {
		RoadNetwork rn;
		synchronized(AstarTest.class) {
			rn = (this.ROAD_NETWORK==null) ? null : this.ROAD_NETWORK.get();
			if (rn==null) {
				try {
					URL file = Resources.getResource(AstarTest.class, "Belfort.shp"); //$NON-NLS-1$
					System.out.print("Reading "+FileSystem.largeBasename(file)+"..."); //$NON-NLS-1$ //$NON-NLS-2$
					try (GISShapeFileReader reader = new GISShapeFileReader(file, RoadPolyline.class)) {
						ESRIBounds eBounds = reader.getBoundsFromHeader();
						Rectangle2d mapBounds = new Rectangle2d();
						mapBounds.setFromCorners(
								eBounds.getMinX(), eBounds.getMinY(),
								eBounds.getMaxX(), eBounds.getMaxY());
						rn = new StandardRoadNetwork(mapBounds);
						RoadPolyline e;
						while ((e=(RoadPolyline)reader.read())!=null) {
							rn.addRoadSegment(e);
						}
						this.ROAD_NETWORK = new SoftReference<>(rn);
						System.out.println("done"); //$NON-NLS-1$
					}
				}
				catch(RuntimeException e) {
					throw e;
				}
				catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return rn;
	}

	private Point1d parsePoint(String s) {
		int i1 = s.indexOf('/');
		assert(i1>0) : "'/' not found on: "+s; //$NON-NLS-1$
		RoadSegment segment = parseSegment(s.substring(i1+1));
		String pos = s.substring(0, i1);
		i1 = pos.indexOf(';');
		assert(i1>0) : "';' not found on: "+pos; //$NON-NLS-1$
		double x = Double.parseDouble(pos.substring(0, i1));
		double y = Double.parseDouble(pos.substring(i1+1));
		return new Point1d(segment, x, y);
	}

	private RoadSegment parseSegment(String s) {
		GeoId geoid = GeoId.valueOf(s);
		assertNotNull(geoid);
		RoadSegment segment = getRoadNetwork().getRoadSegment(geoid);
		assertNotNull("road segment "+s+ "not found", segment); //$NON-NLS-1$ //$NON-NLS-2$
		return segment;
	}

	private RoadSegment[] parsePath(String s) {
		String[] pointStrings = s.split(", "); //$NON-NLS-1$
		RoadSegment[] segments = new RoadSegment[pointStrings.length];
		for(int i=0; i<pointStrings.length; ++i) {
			segments[i] = parseSegment(pointStrings[i]);
			assertNotNull(segments[i]);
		}
		return segments;
	}

	@Test
	public void testDataset() {
		Point1d start, end;
		Point2d start2d, end2d;
		RoadSegment[] rawInvertedPath;
		RoadSegment[] rawPath;
		long index = 0;
		for(Object[] o : this.DATA_SET) {
			start = parsePoint((String)o[0]);
			assertNotNull("On data #"+index, start); //$NON-NLS-1$

			start2d = toPoint2D(start);
			assertNotNull("On data #"+index, start2d); //$NON-NLS-1$

			end = parsePoint((String)o[1]);
			assertNotNull("On data #"+index, end); //$NON-NLS-1$

			end2d = toPoint2D(end);
			assertNotNull("On data #"+index, end2d); //$NON-NLS-1$

			if (o[2]!=null) {
				rawInvertedPath = parsePath((String)o[2]);
				assertNotNull("On data #"+index, rawInvertedPath); //$NON-NLS-1$

				assertEquals(start.getSegment(), rawInvertedPath[rawInvertedPath.length-1]);
				assertEquals(end.getSegment(), rawInvertedPath[0]);
			}

			if (o[3]!=null) {
				rawPath = parsePath((String)o[3]);
				assertNotNull("On data #"+index, rawPath); //$NON-NLS-1$

				assertEquals(start.getSegment(), rawPath[0]);
				assertEquals(end.getSegment(), rawPath[rawPath.length-1]);
			}

			++index;
		}
	}
	
	private Point2d toPoint2D(Point1d pts) {
		Point2d pos = new Point2d();
		pts.getSegment().projectsOnPlane(
				pts.getCurvilineCoordinate(),
				pos, null, CoordinateSystem2D.getDefaultCoordinateSystem());
		return pos;
	}

	/**
	 */
	private void runAstarTextWithHeuristic(AStarHeuristic<RoadConnection> heuristic) {
		Point1d start, end;
		Point2d start2d, end2d;
		RoadSegment[] rawInvertedPath;
		RoadSegment[] rawPath;
		long index = 0;
		for(Object[] o : this.DATA_SET) {
			start = parsePoint((String)o[0]);
			start2d = toPoint2D(start);
			end = parsePoint((String)o[1]);
			end2d = toPoint2D(end);

			RoadAStar astar = new RoadAStar(heuristic);
			RoadPath path = astar.solve(start2d, end2d, getRoadNetwork());
			assertNotNull("On data #"+index+": no path found",path);  //$NON-NLS-1$//$NON-NLS-2$

			if (o[2]!=null) {
				rawInvertedPath = parsePath((String)o[2]);
				for(int i=0, j=rawInvertedPath.length-1; j>0; ++i, --j) {
					assertEquals("On data #"+index+": not same path element at position "+i,  //$NON-NLS-1$//$NON-NLS-2$
							rawInvertedPath[j],
							path.get(i));
				}
			}

			if (o[3]!=null) {
				rawPath = parsePath((String)o[3]);
				for(int i=0; i<rawPath.length; ++i) {
					assertEquals("On data #"+index+": not same path element at position "+i,  //$NON-NLS-1$//$NON-NLS-2$
							rawPath[i],
							path.get(i));
				}
			}

			++index;
		}
	}

	@Test
	public void testAstarDefaultHeuristic() {
		runAstarTextWithHeuristic(null);
	}

	@Test
	public void testAstarDistanceHeuristic() {
		RoadAStarDistanceHeuristic heuristic = new RoadAStarDistanceHeuristic();
		runAstarTextWithHeuristic(heuristic);
	}

}
