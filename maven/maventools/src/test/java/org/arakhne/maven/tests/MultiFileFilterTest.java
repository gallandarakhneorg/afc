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
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;

import org.arakhne.maven.MultiFileFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MultiFileFilter")
@SuppressWarnings("all")
public class MultiFileFilterTest {

    private static final FileFilter PROPERTIES_FILTER = file ->
            file != null && file.getName().endsWith(".properties");

    private static final FileFilter TXT_FILTER = file ->
            file != null && file.getName().endsWith(".txt");

    @DisplayName("constructor()")
    @Nested
    public class ConstructorDefault {

        @DisplayName("without filters, accepts no regular file")
        @Test
        void constructor_default_acceptsNoRegularFile() {
            MultiFileFilter filter = new MultiFileFilter();
            assertFalse(filter.accept(new File("readme.txt")));
            assertFalse(filter.accept(new File("config.properties")));
        }
    }

    @DisplayName("constructor(Collection)")
    @Nested
    public class ConstructorCollection {

        @DisplayName("(null) -> no filter added")
        @Test
        void constructor_collection_null_noFilterAdded() {
            MultiFileFilter filter = new MultiFileFilter((java.util.Collection<? extends FileFilter>) null);
            assertFalse(filter.accept(new File("config.properties")));
        }

        @DisplayName("with one filter")
        @Test
        void constructor_collection_withFilter() {
            MultiFileFilter filter = new MultiFileFilter(Collections.singletonList(PROPERTIES_FILTER));
            assertTrue(filter.accept(new File("config.properties")));
            assertFalse(filter.accept(new File("readme.txt")));
        }
    }

    @DisplayName("constructor(varargs)")
    @Nested
    public class ConstructorVarargs {

        @DisplayName("with multiple filters")
        @Test
        void constructor_varargs_withMultipleFilters() {
            MultiFileFilter filter = new MultiFileFilter(PROPERTIES_FILTER, TXT_FILTER);
            assertTrue(filter.accept(new File("config.properties")));
            assertTrue(filter.accept(new File("readme.txt")));
            assertFalse(filter.accept(new File("image.png")));
        }
    }

    @DisplayName("addFileFilter(varargs)")
    @Nested
    public class AddFileFilterVarargs {

        @DisplayName("adds filters and affects matching")
        @Test
        void addFileFilter_varargs_addsFilters() {
            MultiFileFilter filter = new MultiFileFilter();
            assertFalse(filter.accept(new File("config.properties")));

            filter.addFileFilter(PROPERTIES_FILTER);
            assertTrue(filter.accept(new File("config.properties")));
            assertFalse(filter.accept(new File("readme.txt")));

            filter.addFileFilter(TXT_FILTER);
            assertTrue(filter.accept(new File("readme.txt")));
        }
    }

    @DisplayName("addFileFilter(Collection)")
    @Nested
    public class AddFileFilterCollection {

        @DisplayName("(null) does nothing")
        @Test
        void addFileFilter_collection_null_doesNothing() {
            MultiFileFilter filter = new MultiFileFilter();
            filter.addFileFilter((java.util.Collection<? extends FileFilter>) null);
            assertFalse(filter.accept(new File("config.properties")));
        }

        @DisplayName("adds all filters in collection")
        @Test
        void addFileFilter_collection_addsAll() {
            MultiFileFilter filter = new MultiFileFilter();
            filter.addFileFilter(Arrays.asList(PROPERTIES_FILTER, TXT_FILTER));

            assertTrue(filter.accept(new File("config.properties")));
            assertTrue(filter.accept(new File("readme.txt")));
            assertFalse(filter.accept(new File("archive.zip")));
        }
    }

    @DisplayName("removeFileFilter(varargs)")
    @Nested
    public class RemoveFileFilterVarargs {

        @DisplayName("removes provided filters")
        @Test
        void removeFileFilter_varargs_removesFilters() {
            MultiFileFilter filter = new MultiFileFilter(PROPERTIES_FILTER, TXT_FILTER);

            assertTrue(filter.accept(new File("config.properties")));
            assertTrue(filter.accept(new File("readme.txt")));

            filter.removeFileFilter(PROPERTIES_FILTER);

            assertFalse(filter.accept(new File("config.properties")));
            assertTrue(filter.accept(new File("readme.txt")));
        }
    }

    @DisplayName("removeFileFilter(Collection)")
    @Nested
    public class RemoveFileFilterCollection {

        @DisplayName("removes all filters from collection")
        @Test
        void removeFileFilter_collection_removesFilters() {
            MultiFileFilter filter = new MultiFileFilter(PROPERTIES_FILTER, TXT_FILTER);

            filter.removeFileFilter(Collections.singletonList(PROPERTIES_FILTER));

            assertFalse(filter.accept(new File("config.properties")));
            assertTrue(filter.accept(new File("readme.txt")));
        }
    }

    @DisplayName("clear")
    @Nested
    public class Clear {

        @DisplayName("removes every filter")
        @Test
        void clear_removesAllFilters() {
            MultiFileFilter filter = new MultiFileFilter(PROPERTIES_FILTER, TXT_FILTER);
            assertTrue(filter.accept(new File("config.properties")));
            assertTrue(filter.accept(new File("readme.txt")));

            filter.clear();

            assertFalse(filter.accept(new File("config.properties")));
            assertFalse(filter.accept(new File("readme.txt")));
        }
    }

    @DisplayName("accept")
    @Nested
    public class Accept {

        @DisplayName("(null)")
        @Test
        void accept_null_returnsFalse() {
            MultiFileFilter filter = new MultiFileFilter(PROPERTIES_FILTER);
            assertFalse(filter.accept(null));
        }

        @DisplayName("(folder)")
        @Test
        void accept_directory_returnsTrue() {
            MultiFileFilter filter = new MultiFileFilter();
            File directory = new File(System.getProperty("java.io.tmpdir"));
            assertTrue(directory.isDirectory(), "Precondition failed: expected a directory");
            assertTrue(filter.accept(directory));
        }

        @DisplayName("(matching file)")
        @Test
        void accept_matchingFile_returnsTrue() {
            MultiFileFilter filter = new MultiFileFilter(PROPERTIES_FILTER);
            assertTrue(filter.accept(new File("my.properties")));
        }

        @DisplayName("(non-matching file)")
        @Test
        void accept_nonMatchingFile_returnsFalse() {
            MultiFileFilter filter = new MultiFileFilter(PROPERTIES_FILTER);
            assertFalse(filter.accept(new File("my.xml")));
        }
    }

    @DisplayName("toString")
    @Nested
    public class ToString {

        @DisplayName("returns expected label")
        @Test
        void toString_returnsExpectedLabel() {
            MultiFileFilter filter = new MultiFileFilter();
            assertEquals("Multiple file", filter.toString());
        }
    }
}
