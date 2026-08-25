/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.maven.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.arakhne.maven.HtmlFileFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HtmlFileFilter")
@SuppressWarnings("all")
public class HtmlFileFilterTest {

    private final HtmlFileFilter filter = new HtmlFileFilter();

    @DisplayName("accept")
    @Nested
    public class Accept {

        @DisplayName("(null)")
        @Test
        void accept_null_returnsFalse() {
            assertFalse(filter.accept(null));
        }

        @DisplayName("(folder)")
        @Test
        void accept_directory_returnsTrue() {
            File directory = new File(System.getProperty("java.io.tmpdir"));
            assertTrue(directory.isDirectory(), "Precondition failed: expected a directory");
            assertTrue(filter.accept(directory));
        }

        @DisplayName("(.html file)")
        @Test
        void accept_htmlFile_returnsTrue() {
            assertTrue(filter.accept(new File("index.html")));
        }

        @DisplayName("(.htm file)")
        @Test
        void accept_htmFile_returnsTrue() {
            assertTrue(filter.accept(new File("index.htm")));
        }

        @DisplayName("(.ht file)")
        @Test
        void accept_htFile_returnsTrue() {
            assertTrue(filter.accept(new File("index.ht")));
        }

        @DisplayName("(.phtml file)")
        @Test
        void accept_phtmlFile_returnsTrue() {
            assertTrue(filter.accept(new File("index.phtml")));
        }

        @DisplayName("(uppercase extension)")
        @Test
        void accept_uppercaseExtension_returnsTrue() {
            assertTrue(filter.accept(new File("INDEX.HTML")));
            assertTrue(filter.accept(new File("INDEX.HTM")));
            assertTrue(filter.accept(new File("INDEX.HT")));
            assertTrue(filter.accept(new File("INDEX.PHTML")));
        }

        @DisplayName("(mixed-case extension)")
        @Test
        void accept_mixedCaseExtension_returnsTrue() {
            assertTrue(filter.accept(new File("index.HtMl")));
        }

        @DisplayName("(other extension)")
        @Test
        void accept_otherExtension_returnsFalse() {
            assertFalse(filter.accept(new File("index.txt")));
        }

        @DisplayName("(no extension)")
        @Test
        void accept_noExtension_returnsFalse() {
            assertFalse(filter.accept(new File("index")));
        }

        @DisplayName("(empty filename)")
        @Test
        void accept_emptyFilename_returnsFalse() {
            assertTrue(filter.accept(new File("")));
        }
    }

    @DisplayName("toString")
    @Nested
    public class ToString {

        @DisplayName("returns expected label")
        @Test
        void toString_returnsExpectedLabel() {
            assertEquals("Hypertext (.html)", filter.toString());
        }
    }
}
