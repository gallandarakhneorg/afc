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

package org.arakhne.afc.vmutil;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.locale.Locale;

/**
 * Utilities for retrieving color values from a color name.
 *
 * <p>The supported color names are listing is the table below. The given colors are defined within the JavaFX standards.
 * <table border="1">
 * <thead>
 * <tr><th>Color Name</th><th>RGB Color</th><th>R, G, B, A Components</th><th>Example</th></tr>
 * </thead>
 * <tbody>
 * <tr><td>aliceblue</td><td>0xF0F8FF</td><td>rgba(240, 496, 255, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#F0F8FF"><!-- F0F8FF--></td></tr>
 * <tr><td>antiquewhite</td><td>0xFAEBD7</td><td>rgba(250, 470, 215, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FAEBD7"><!-- FAEBD7--></td></tr>
 * <tr><td>aqua</td><td>0x00FFFF</td><td>rgba(0, 510, 255, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#00FFFF"><!-- 00FFFF--></td></tr>
 * <tr><td>aquamarine</td><td>0x7FFFD4</td><td>rgba(127, 510, 212, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#7FFFD4"><!-- 7FFFD4--></td></tr>
 * <tr><td>azure</td><td>0xF0FFFF</td><td>rgba(240, 510, 255, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#F0FFFF"><!-- F0FFFF--></td></tr>
 * <tr><td>barkblue</td><td>0x00008B</td><td>rgba(0, 0, 139, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#00008B"><!-- 00008B--></td></tr>
 * <tr><td>beige</td><td>0xF5F5DC</td><td>rgba(245, 490, 220, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#F5F5DC"><!-- F5F5DC--></td></tr>
 * <tr><td>bisque</td><td>0xFFE4C4</td><td>rgba(255, 456, 196, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFE4C4"><!-- FFE4C4--></td></tr>
 * <tr><td>black</td><td>0x000000</td><td>rgba(0, 0, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#000000"><!-- 000000--></td></tr>
 * <tr><td>blancheddalmond</td><td>0xFFEBCD</td><td>rgba(255, 470, 205, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFEBCD"><!-- FFEBCD--></td></tr>
 * <tr><td>blue</td><td>0x0000FF</td><td>rgba(0, 0, 255, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#0000FF"><!-- 0000FF--></td></tr>
 * <tr><td>blueviolet</td><td>0x8A2BE2</td><td>rgba(138, 86, 226, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#8A2BE2"><!-- 8A2BE2--></td></tr>
 * <tr><td>brown</td><td>0xA52A2A</td><td>rgba(165, 84, 42, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#A52A2A"><!-- A52A2A--></td></tr>
 * <tr><td>burlywood</td><td>0xDEB887</td><td>rgba(222, 368, 135, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#DEB887"><!-- DEB887--></td></tr>
 * <tr><td>cadetblue</td><td>0x5F9EA0</td><td>rgba(95, 316, 160, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#5F9EA0"><!-- 5F9EA0--></td></tr>
 * <tr><td>chartreuse</td><td>0x7FFF00</td><td>rgba(127, 510, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#7FFF00"><!-- 7FFF00--></td></tr>
 * <tr><td>chocolate</td><td>0xD2691E</td><td>rgba(210, 210, 30, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#D2691E"><!-- D2691E--></td></tr>
 * <tr><td>coral</td><td>0xFF7F50</td><td>rgba(255, 254, 80, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FF7F50"><!-- FF7F50--></td></tr>
 * <tr><td>cornflowerblue</td><td>0x6495ED</td><td>rgba(100, 298, 237, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#6495ED"><!-- 6495ED--></td></tr>
 * <tr><td>cornsilk</td><td>0xFFF8DC</td><td>rgba(255, 496, 220, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFF8DC"><!-- FFF8DC--></td></tr>
 * <tr><td>crimson</td><td>0xDC143C</td><td>rgba(220, 40, 60, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#DC143C"><!-- DC143C--></td></tr>
 * <tr><td>cyan</td><td>0x00FFFF</td><td>rgba(0, 510, 255, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#00FFFF"><!-- 00FFFF--></td></tr>
 * <tr><td>dargray</td><td>0xA9A9A9</td><td>rgba(169, 338, 169, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#A9A9A9"><!-- A9A9A9--></td></tr>
 * <tr><td>darkcyan</td><td>0x008B8B</td><td>rgba(0, 278, 139, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#008B8B"><!-- 008B8B--></td></tr>
 * <tr><td>darkgoldenrod</td><td>0xB8860B</td><td>rgba(184, 268, 11, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#B8860B"><!-- B8860B--></td></tr>
 * <tr><td>darkgreen</td><td>0x006400</td><td>rgba(0, 200, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#006400"><!-- 006400--></td></tr>
 * <tr><td>darkgrey</td><td>0xA9A9A9</td><td>rgba(169, 338, 169, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#A9A9A9"><!-- A9A9A9--></td></tr>
 * <tr><td>darkkhaki</td><td>0xBDB76B</td><td>rgba(189, 366, 107, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#BDB76B"><!-- BDB76B--></td></tr>
 * <tr><td>darkmagenta</td><td>0x8B008B</td><td>rgba(139, 0, 139, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#8B008B"><!-- 8B008B--></td></tr>
 * <tr><td>darkolivegreen</td><td>0x556B2F</td><td>rgba(85, 214, 47, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#556B2F"><!-- 556B2F--></td></tr>
 * <tr><td>darkorange</td><td>0xFF8C00</td><td>rgba(255, 280, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FF8C00"><!-- FF8C00--></td></tr>
 * <tr><td>darkorchid</td><td>0x9932CC</td><td>rgba(153, 100, 204, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#9932CC"><!-- 9932CC--></td></tr>
 * <tr><td>darkred</td><td>0x8B0000</td><td>rgba(139, 0, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#8B0000"><!-- 8B0000--></td></tr>
 * <tr><td>darksalmon</td><td>0xE9967A</td><td>rgba(233, 300, 122, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#E9967A"><!-- E9967A--></td></tr>
 * <tr><td>darkseagreen</td><td>0x8FBC8F</td><td>rgba(143, 376, 143, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#8FBC8F"><!-- 8FBC8F--></td></tr>
 * <tr><td>darkskyblue</td><td>0x00BFFF</td><td>rgba(0, 382, 255, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#00BFFF"><!-- 00BFFF--></td></tr>
 * <tr><td>darkslateblue</td><td>0x483D8B</td><td>rgba(72, 122, 139, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#483D8B"><!-- 483D8B--></td></tr>
 * <tr><td>darkslategray</td><td>0x2F4F4F</td><td>rgba(47, 158, 79, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#2F4F4F"><!-- 2F4F4F--></td></tr>
 * <tr><td>darkslategrey</td><td>0x2F4F4F</td><td>rgba(47, 158, 79, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#2F4F4F"><!-- 2F4F4F--></td></tr>
 * <tr><td>darkturquoise</td><td>0x00CED1</td><td>rgba(0, 412, 209, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#00CED1"><!-- 00CED1--></td></tr>
 * <tr><td>darkviolet</td><td>0x9400D3</td><td>rgba(148, 0, 211, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#9400D3"><!-- 9400D3--></td></tr>
 * <tr><td>deeppink</td><td>0xFF1493</td><td>rgba(255, 40, 147, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FF1493"><!-- FF1493--></td></tr>
 * <tr><td>dimgray</td><td>0x696969</td><td>rgba(105, 210, 105, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#696969"><!-- 696969--></td></tr>
 * <tr><td>dimgrey</td><td>0x696969</td><td>rgba(105, 210, 105, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#696969"><!-- 696969--></td></tr>
 * <tr><td>dodgerblue</td><td>0x1E90FF</td><td>rgba(30, 288, 255, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#1E90FF"><!-- 1E90FF--></td></tr>
 * <tr><td>firebrick</td><td>0xB22222</td><td>rgba(178, 68, 34, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#B22222"><!-- B22222--></td></tr>
 * <tr><td>floralwhite</td><td>0xFFFAF0</td><td>rgba(255, 500, 240, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFFAF0"><!-- FFFAF0--></td></tr>
 * <tr><td>forestgreen</td><td>0x228B22</td><td>rgba(34, 278, 34, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#228B22"><!-- 228B22--></td></tr>
 * <tr><td>fuchsia</td><td>0xFF00FF</td><td>rgba(255, 0, 255, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FF00FF"><!-- FF00FF--></td></tr>
 * <tr><td>gainsboro</td><td>0xDCDCDC</td><td>rgba(220, 440, 220, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#DCDCDC"><!-- DCDCDC--></td></tr>
 * <tr><td>ghostwhite</td><td>0xF8F8FF</td><td>rgba(248, 496, 255, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#F8F8FF"><!-- F8F8FF--></td></tr>
 * <tr><td>gold</td><td>0xFFD700</td><td>rgba(255, 430, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFD700"><!-- FFD700--></td></tr>
 * <tr><td>goldenrod</td><td>0xDAA520</td><td>rgba(218, 330, 32, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#DAA520"><!-- DAA520--></td></tr>
 * <tr><td>gray</td><td>0x808080</td><td>rgba(128, 256, 128, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#808080"><!-- 808080--></td></tr>
 * <tr><td>green</td><td>0x007D00</td><td>rgba(0, 250, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#007D00"><!-- 007D00--></td></tr>
 * <tr><td>greenyellow</td><td>0xADFF2F</td><td>rgba(173, 510, 47, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#ADFF2F"><!-- ADFF2F--></td></tr>
 * <tr><td>grey</td><td>0x808080</td><td>rgba(128, 256, 128, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#808080"><!-- 808080--></td></tr>
 * <tr><td>honeydew</td><td>0xF0FFF0</td><td>rgba(240, 510, 240, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#F0FFF0"><!-- F0FFF0--></td></tr>
 * <tr><td>hotpink</td><td>0xFF69B4</td><td>rgba(255, 210, 180, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FF69B4"><!-- FF69B4--></td></tr>
 * <tr><td>indianred</td><td>0xCD5C5C</td><td>rgba(205, 184, 92, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#CD5C5C"><!-- CD5C5C--></td></tr>
 * <tr><td>indigo</td><td>0x4B0082</td><td>rgba(75, 0, 130, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#4B0082"><!-- 4B0082--></td></tr>
 * <tr><td>ivory</td><td>0xFFFFF0</td><td>rgba(255, 510, 240, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFFFF0"><!-- FFFFF0--></td></tr>
 * <tr><td>khaki</td><td>0xF0E68C</td><td>rgba(240, 460, 140, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#F0E68C"><!-- F0E68C--></td></tr>
 * <tr><td>lavender</td><td>0xE6E6FA</td><td>rgba(230, 460, 250, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#E6E6FA"><!-- E6E6FA--></td></tr>
 * <tr><td>lavenderblush</td><td>0xFFF0F5</td><td>rgba(255, 480, 245, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFF0F5"><!-- FFF0F5--></td></tr>
 * <tr><td>lawngreen</td><td>0x7CFC00</td><td>rgba(124, 504, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#7CFC00"><!-- 7CFC00--></td></tr>
 * <tr><td>lemonchiffon</td><td>0xFFFACD</td><td>rgba(255, 500, 205, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFFACD"><!-- FFFACD--></td></tr>
 * <tr><td>lightblue</td><td>0xADD8E6</td><td>rgba(173, 432, 230, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#ADD8E6"><!-- ADD8E6--></td></tr>
 * <tr><td>lightcoral</td><td>0xF08080</td><td>rgba(240, 256, 128, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#F08080"><!-- F08080--></td></tr>
 * <tr><td>lightcyan</td><td>0xE0FFFF</td><td>rgba(224, 510, 255, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#E0FFFF"><!-- E0FFFF--></td></tr>
 * <tr><td>lightgoldenrodyyellow</td><td>0xFAFAD2</td><td>rgba(250, 500, 210, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FAFAD2"><!-- FAFAD2--></td></tr>
 * <tr><td>lightgray</td><td>0xD3D3D3</td><td>rgba(211, 422, 211, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#D3D3D3"><!-- D3D3D3--></td></tr>
 * <tr><td>lightgreen</td><td>0x90EE90</td><td>rgba(144, 476, 144, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#90EE90"><!-- 90EE90--></td></tr>
 * <tr><td>lightgrey</td><td>0xD3D3D3</td><td>rgba(211, 422, 211, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#D3D3D3"><!-- D3D3D3--></td></tr>
 * <tr><td>lightpink</td><td>0xFFB6C1</td><td>rgba(255, 364, 193, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFB6C1"><!-- FFB6C1--></td></tr>
 * <tr><td>lightsalmon</td><td>0xFFA07A</td><td>rgba(255, 320, 122, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFA07A"><!-- FFA07A--></td></tr>
 * <tr><td>lightseagreen</td><td>0x20B2AA</td><td>rgba(32, 356, 170, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#20B2AA"><!-- 20B2AA--></td></tr>
 * <tr><td>lightskyblue</td><td>0x87CEFA</td><td>rgba(135, 412, 250, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#87CEFA"><!-- 87CEFA--></td></tr>
 * <tr><td>lightslategray</td><td>0x778899</td><td>rgba(119, 272, 153, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#778899"><!-- 778899--></td></tr>
 * <tr><td>lightslategrey</td><td>0x778899</td><td>rgba(119, 272, 153, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#778899"><!-- 778899--></td></tr>
 * <tr><td>lightsteelblue</td><td>0xB0C4DE</td><td>rgba(176, 392, 222, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#B0C4DE"><!-- B0C4DE--></td></tr>
 * <tr><td>lightyellow</td><td>0xFFFFE0</td><td>rgba(255, 510, 224, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFFFE0"><!-- FFFFE0--></td></tr>
 * <tr><td>lime</td><td>0x00FF00</td><td>rgba(0, 510, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#00FF00"><!-- 00FF00--></td></tr>
 * <tr><td>limegreen</td><td>0x32CD32</td><td>rgba(50, 410, 50, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#32CD32"><!-- 32CD32--></td></tr>
 * <tr><td>linen</td><td>0xFAF0E6</td><td>rgba(250, 480, 230, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FAF0E6"><!-- FAF0E6--></td></tr>
 * <tr><td>magenta</td><td>0xFF00FF</td><td>rgba(255, 0, 255, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FF00FF"><!-- FF00FF--></td></tr>
 * <tr><td>maroon</td><td>0x800000</td><td>rgba(128, 0, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#800000"><!-- 800000--></td></tr>
 * <tr><td>mediumaquamarine</td><td>0x66CDAA</td><td>rgba(102, 410, 170, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#66CDAA"><!-- 66CDAA--></td></tr>
 * <tr><td>mediumblue</td><td>0x0000CD</td><td>rgba(0, 0, 205, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#0000CD"><!-- 0000CD--></td></tr>
 * <tr><td>mediumorchid</td><td>0xBA55D3</td><td>rgba(186, 170, 211, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#BA55D3"><!-- BA55D3--></td></tr>
 * <tr><td>mediumpurple</td><td>0x9370DB</td><td>rgba(147, 224, 219, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#9370DB"><!-- 9370DB--></td></tr>
 * <tr><td>mediumseagreen</td><td>0x3CB371</td><td>rgba(60, 358, 113, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#3CB371"><!-- 3CB371--></td></tr>
 * <tr><td>mediumslateblue</td><td>0x7B68EE</td><td>rgba(123, 208, 238, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#7B68EE"><!-- 7B68EE--></td></tr>
 * <tr><td>mediumspringgreen</td><td>0x00FA9A</td><td>rgba(0, 500, 154, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#00FA9A"><!-- 00FA9A--></td></tr>
 * <tr><td>mediumturquoise</td><td>0x30D1CC</td><td>rgba(48, 418, 204, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#30D1CC"><!-- 30D1CC--></td></tr>
 * <tr><td>mediumvioletred</td><td>0x191970</td><td>rgba(25, 50, 112, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#191970"><!-- 191970--></td></tr>
 * <tr><td>mintcream</td><td>0xF5FFFA</td><td>rgba(245, 510, 250, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#F5FFFA"><!-- F5FFFA--></td></tr>
 * <tr><td>mistyrose</td><td>0xFFE4E1</td><td>rgba(255, 456, 225, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFE4E1"><!-- FFE4E1--></td></tr>
 * <tr><td>moccasin</td><td>0xFFE4B5</td><td>rgba(255, 456, 181, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFE4B5"><!-- FFE4B5--></td></tr>
 * <tr><td>navajowhite</td><td>0xFFDEAD</td><td>rgba(255, 444, 173, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFDEAD"><!-- FFDEAD--></td></tr>
 * <tr><td>navy</td><td>0x000080</td><td>rgba(0, 0, 128, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#000080"><!-- 000080--></td></tr>
 * <tr><td>oldlace</td><td>0xFDF5E6</td><td>rgba(253, 490, 230, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FDF5E6"><!-- FDF5E6--></td></tr>
 * <tr><td>olive</td><td>0x808000</td><td>rgba(128, 256, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#808000"><!-- 808000--></td></tr>
 * <tr><td>olivedrab</td><td>0x6B8E23</td><td>rgba(107, 284, 35, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#6B8E23"><!-- 6B8E23--></td></tr>
 * <tr><td>orange</td><td>0xFFA500</td><td>rgba(255, 330, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFA500"><!-- FFA500--></td></tr>
 * <tr><td>orangered</td><td>0xFF4500</td><td>rgba(255, 138, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FF4500"><!-- FF4500--></td></tr>
 * <tr><td>orchid</td><td>0xDA70D6</td><td>rgba(218, 224, 214, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#DA70D6"><!-- DA70D6--></td></tr>
 * <tr><td>palegoldenrod</td><td>0xEEE8AA</td><td>rgba(238, 464, 170, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#EEE8AA"><!-- EEE8AA--></td></tr>
 * <tr><td>palegreen</td><td>0x98FB98</td><td>rgba(152, 502, 152, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#98FB98"><!-- 98FB98--></td></tr>
 * <tr><td>paleturquoise</td><td>0xAFEEEE</td><td>rgba(175, 476, 238, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#AFEEEE"><!-- AFEEEE--></td></tr>
 * <tr><td>palevioletred</td><td>0xDB7093</td><td>rgba(219, 224, 147, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#DB7093"><!-- DB7093--></td></tr>
 * <tr><td>papayawhip</td><td>0xFFEFD5</td><td>rgba(255, 478, 213, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFEFD5"><!-- FFEFD5--></td></tr>
 * <tr><td>peachpuff</td><td>0xFFDAB9</td><td>rgba(255, 436, 185, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFDAB9"><!-- FFDAB9--></td></tr>
 * <tr><td>peru</td><td>0xCD853F</td><td>rgba(205, 266, 63, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#CD853F"><!-- CD853F--></td></tr>
 * <tr><td>pink</td><td>0xFFC0CB</td><td>rgba(255, 384, 203, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFC0CB"><!-- FFC0CB--></td></tr>
 * <tr><td>plum</td><td>0xDDA0DD</td><td>rgba(221, 320, 221, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#DDA0DD"><!-- DDA0DD--></td></tr>
 * <tr><td>powderblue</td><td>0xB0E0E6</td><td>rgba(176, 448, 230, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#B0E0E6"><!-- B0E0E6--></td></tr>
 * <tr><td>purple</td><td>0x800080</td><td>rgba(128, 0, 128, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#800080"><!-- 800080--></td></tr>
 * <tr><td>red</td><td>0xFF0000</td><td>rgba(255, 0, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FF0000"><!-- FF0000--></td></tr>
 * <tr><td>rosybrown</td><td>0xBC8F8F</td><td>rgba(188, 286, 143, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#BC8F8F"><!-- BC8F8F--></td></tr>
 * <tr><td>royalblue</td><td>0x4169E1</td><td>rgba(65, 210, 225, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#4169E1"><!-- 4169E1--></td></tr>
 * <tr><td>saddlebrown</td><td>0x8B4513</td><td>rgba(139, 138, 19, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#8B4513"><!-- 8B4513--></td></tr>
 * <tr><td>salmon</td><td>0xFA8072</td><td>rgba(250, 256, 114, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FA8072"><!-- FA8072--></td></tr>
 * <tr><td>sandybrown</td><td>0xF4A460</td><td>rgba(244, 328, 96, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#F4A460"><!-- F4A460--></td></tr>
 * <tr><td>seagreen</td><td>0x2E8B57</td><td>rgba(46, 278, 87, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#2E8B57"><!-- 2E8B57--></td></tr>
 * <tr><td>seashell</td><td>0xFFF5EE</td><td>rgba(255, 490, 238, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFF5EE"><!-- FFF5EE--></td></tr>
 * <tr><td>sienna</td><td>0xA0522D</td><td>rgba(160, 164, 45, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#A0522D"><!-- A0522D--></td></tr>
 * <tr><td>silver</td><td>0xC0C0C0</td><td>rgba(192, 384, 192, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#C0C0C0"><!-- C0C0C0--></td></tr>
 * <tr><td>skyblue</td><td>0x87CEEB</td><td>rgba(135, 412, 235, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#87CEEB"><!-- 87CEEB--></td></tr>
 * <tr><td>slateblue</td><td>0x6A5AFA</td><td>rgba(106, 180, 250, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#6A5AFA"><!-- 6A5AFA--></td></tr>
 * <tr><td>slategray</td><td>0x708090</td><td>rgba(112, 256, 144, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#708090"><!-- 708090--></td></tr>
 * <tr><td>slategrey</td><td>0x708090</td><td>rgba(112, 256, 144, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#708090"><!-- 708090--></td></tr>
 * <tr><td>snow</td><td>0xFFFAFA</td><td>rgba(255, 500, 250, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFFAFA"><!-- FFFAFA--></td></tr>
 * <tr><td>springgreen</td><td>0x00FF7F</td><td>rgba(0, 510, 127, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#00FF7F"><!-- 00FF7F--></td></tr>
 * <tr><td>steelblue</td><td>0x4682B4</td><td>rgba(70, 260, 180, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#4682B4"><!-- 4682B4--></td></tr>
 * <tr><td>tan</td><td>0xD2B48C</td><td>rgba(210, 360, 140, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#D2B48C"><!-- D2B48C--></td></tr>
 * <tr><td>teal</td><td>0x008080</td><td>rgba(0, 256, 128, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#008080"><!-- 008080--></td></tr>
 * <tr><td>thistle</td><td>0xD8BFD8</td><td>rgba(216, 382, 216, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#D8BFD8"><!-- D8BFD8--></td></tr>
 * <tr><td>tomato</td><td>0xFF6347</td><td>rgba(255, 198, 71, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FF6347"><!-- FF6347--></td></tr>
 * <tr><td>transparent</td><td>0x000000</td><td>rgba(0, 0, 0, 0)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#000000"><!-- 000000--></td></tr>
 * <tr><td>turquoise</td><td>0x40E0D0</td><td>rgba(64, 448, 208, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#40E0D0"><!-- 40E0D0--></td></tr>
 * <tr><td>violet</td><td>0xEE82EE</td><td>rgba(238, 260, 238, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#EE82EE"><!-- EE82EE--></td></tr>
 * <tr><td>wheat</td><td>0xF5DEB3</td><td>rgba(245, 444, 179, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#F5DEB3"><!-- F5DEB3--></td></tr>
 * <tr><td>white</td><td>0xFFFFFF</td><td>rgba(255, 510, 255, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFFFFF"><!-- FFFFFF--></td></tr>
 * <tr><td>whitesmoke</td><td>0xF5F5F5</td><td>rgba(245, 490, 245, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#F5F5F5"><!-- F5F5F5--></td></tr>
 * <tr><td>yellow</td><td>0xFFFF00</td><td>rgba(255, 510, 0, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#FFFF00"><!-- FFFF00--></td></tr>
 * <tr><td>yellowgreen</td><td>0x9ACD32</td><td>rgba(154, 410, 50, 255)</td>
 *     <td style="width:40px; height:20px; border-color:black; background-color:#9ACD32"><!-- 9ACD32--></td></tr>
 * </tbody>
 * </table>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public final class ColorNames {

	/** List colors and there corresponding Java colors.
	 */
	private static final Map<String, Integer> COLOR_MATCHES;

	private static final String POSITIVE_INTEGER_NUMBER_PATTERN = "[0-9]+"; //$NON-NLS-1$

	private static final String COLOR_DEFINITION_PATTERN = "^\\s*([^:\\s*]+)\\s*:\\s*(" //$NON-NLS-1$
			+ POSITIVE_INTEGER_NUMBER_PATTERN + ")\\s*,\\s*(" //$NON-NLS-1$
			+ POSITIVE_INTEGER_NUMBER_PATTERN + ")\\s*,\\s*(" //$NON-NLS-1$
			+ POSITIVE_INTEGER_NUMBER_PATTERN + ")\\s*,\\s*(" //$NON-NLS-1$
			+ POSITIVE_INTEGER_NUMBER_PATTERN + ")\\s*$"; //$NON-NLS-1$

	private static final String EOL_PATTERN = "[ \t\n\r]*;[ \\t\\n\\r]*"; //$NON-NLS-1$

	private ColorNames() {
		//
	}

	static {
		COLOR_MATCHES = initializeColors();
	}

	@SuppressWarnings("checkstyle:magicnumber")
	private static Map<String, Integer> initializeColors() {
		final Map<String, Integer> map = new TreeMap<>();
		final String colors = Locale.getString(ColorNames.class, "COLOR_MATCHES"); //$NON-NLS-1$
		if (colors != null) {
			final Pattern pattern = Pattern.compile(COLOR_DEFINITION_PATTERN);
			for (final String definition : colors.split(EOL_PATTERN)) {
				final Matcher matcher = pattern.matcher(definition);
				if (matcher.matches()) {
					map.put(matcher.group(1).toLowerCase(),
							encodeRgbaColor(
									Integer.parseInt(matcher.group(2)),
									Integer.parseInt(matcher.group(3)),
									Integer.parseInt(matcher.group(4)),
									Integer.parseInt(matcher.group(5))));
				}
			}
		}
		return map;
	}

	@SuppressWarnings("checkstyle:magicnumber")
	private static int encodeRgbaColor(int red, int green, int blue, int alpha) {
		int col = (alpha & 0xFF) << 24;
		col |= (red & 0xFF) << 16;
		col |= (green & 0xFF) << 8;
		col |= blue & 0xFF;
		return Integer.valueOf(col);
	}

	/** Replies the names of the known colors.
	 *
	 * @return the names of the colors.
	 */
	@Pure
	public static Set<String> getColorNames() {
		return Collections.unmodifiableSet(COLOR_MATCHES.keySet());
	}

	/** Replies the definitions of the known colors.
	 *
	 * @return the definitions of the colors.
	 */
	@Pure
	public static Map<String, Integer> getColorDefinitions() {
		return Collections.unmodifiableMap(COLOR_MATCHES);
	}

	/** Replies the color value for the given color name.
	 *
	 * <p>See the documentation of the {@link #ColorNames} type for obtaining a list of the colors.
	 *
	 * @param colorName the color name.
	 * @param defaultValue if the given name does not corresponds to a known color, this value is replied.
	 * @return the color value.
	 */
	@Pure
	public static int getColorFromName(String colorName, int defaultValue) {
		final Integer value = COLOR_MATCHES.get(Strings.nullToEmpty(colorName).toLowerCase());
		if (value != null) {
			return value.intValue();
		}
		return defaultValue;
	}

	/** Replies the color value for the given color name.
	 *
	 * <p>See the documentation of the {@link #ColorNames} type for obtaining a list of the colors.
	 *
	 * @param colorName the color name.
	 * @return the color value, or {@code null} if the name does not correspond to an known color.
	 */
	@Pure
	public static Integer getColorFromName(String colorName) {
		return COLOR_MATCHES.get(colorName);
	}

	/** Replies the color name for the given color value.
	 *
	 * <p>See the documentation of the {@link #ColorNames} type for obtaining a list of the colors.
	 *
	 * @param colorValue the color value.
	 * @return the color name, or {@code null} if the value does not correspond to an known color.
	 */
	@Pure
	public static String getColorNameFromValue(int colorValue) {
		for (final Entry<String, Integer> entry : COLOR_MATCHES.entrySet()) {
			final int knownValue = entry.getValue().intValue();
			if (colorValue == knownValue) {
				return entry.getKey();
			}
		}
		return null;
	}

}
