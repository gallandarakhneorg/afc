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

package org.arakhne.afc.bootique.printconfig.commands;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import com.google.inject.Injector;
import com.google.inject.Provider;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.log.BootLogger;
import io.bootique.meta.application.CommandMetadata;
import io.bootique.meta.config.ConfigMetadataNode;
import io.bootique.meta.module.ModulesMetadata;

import org.arakhne.afc.bootique.printconfig.configs.Configs;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * Command for showing configuration.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class PrintConfigCommand extends CommandWithMetadata {

	private static final String CLI_NAME = "printconfig"; //$NON-NLS-1$

	private static final char CLI_SHORTNAME = 'C';

	private final Provider<BootLogger> bootLogger;

	private final Provider<ModulesMetadata> modulesMetadata;

	private final Injector injector;

	/** Constructor.
	 *
	 * @param bootLogger the boot logger.
	 * @param modulesMetadata the metadata of the bootique modules.
	 * @param injector the injector.
	 */
	public PrintConfigCommand(Provider<BootLogger> bootLogger, Provider<ModulesMetadata> modulesMetadata, Injector injector) {
		super(CommandMetadata
	            .builder(PrintConfigCommand.class)
	            .description(Locale.getString("COMMAND_DESCRIPTION")) //$NON-NLS-1$
	            .name(CLI_NAME).shortName(CLI_SHORTNAME));
		this.bootLogger = bootLogger;
		this.modulesMetadata = modulesMetadata;
		this.injector = injector;
	}

	@Override
	public CommandOutcome run(Cli cli) {
		final Map<String, Object> values = new TreeMap<>();
		extractConfigValues(values, Configs.extractConfigs(this.modulesMetadata.get()));
		// Generate the output
		final String content;
		try {
				content = generateYaml(values);
		} catch (JsonProcessingException exception) {
			return CommandOutcome.failed(ERROR_CODE, exception.getLocalizedMessage(), exception);
		}
		this.bootLogger.get().stdout(content);
		return CommandOutcome.succeeded();
	}

	/** Extract the definition from the given configurations.
	 *
	 * @param yaml the to fill out.
	 * @param configs the configurations.
	 */
	protected void extractConfigValues(Map<String, Object> yaml, List<ConfigMetadataNode> configs) {
		for (final ConfigMetadataNode config : configs) {
			Configs.defineConfig(yaml, config, this.injector);
		}
	}

	/** Generate the Yaml representation of the given map.
	 *
	 * @param map the map to print out.
	 * @return the Yaml representation.
	 * @throws JsonProcessingException when the Json cannot be processed.
	 */
	@SuppressWarnings("static-method")
	protected String generateYaml(Map<String, Object> map) throws JsonProcessingException {
		final YAMLFactory yamlFactory = new YAMLFactory();
		yamlFactory.configure(Feature.WRITE_DOC_START_MARKER, false);
		final ObjectMapper mapper = new ObjectMapper(yamlFactory);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
	}

	}

}
