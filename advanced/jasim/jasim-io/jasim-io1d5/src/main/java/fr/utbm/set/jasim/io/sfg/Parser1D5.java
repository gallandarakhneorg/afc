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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.arakhne.afc.util.OutputParameter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.jasim.JasimConstants;
import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody;
import fr.utbm.set.jasim.environment.interfaces.body.factory.FrustumDescription;
import fr.utbm.set.jasim.environment.interfaces.body.factory.FrustumDescription.FrustumType;
import fr.utbm.set.jasim.environment.interfaces.body.factory.RectangularFrustumDescription;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGeneratorType;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.io.sfg.EnvironmentSimulationParameterSet.GroundType;
import fr.utbm.set.jasim.io.sfg.SpawnerSimulationParameterSet.SpawnerParameterType;
import fr.utbm.set.jasim.io.sfg.TimeSimulationParameterSet.TimeParameterType;
import fr.utbm.set.jasim.spawn.SpawningLaw;
import fr.utbm.set.math.stochastic.StochasticLaw;

/**
 * Parse an XML file to extract 1.5D simulation parameters.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class Parser1D5 extends AbstractParser {

	/**
	 * @param type is the current type of the file to parse.
	 */
	Parser1D5(SFGFileType type) {
		super(type);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimulationParameterSet parse(Document document)
			throws IOException {
		if (!isXMLVersion(6, 0))
			throw new IOException("Unsupported DTD version. Must by >=6.0"); //$NON-NLS-1$
		return xmlSimulation(document);
	}
	
	/** Extract the parameter set from the given node.
	 * The parameter node is assumed to be a child of the given node.
	 * 
	 * @param node is the node to explore
	 * @return the parameter set or <code>null</code>
	 * @throws IOException 
	 */
	protected SimulationParameterSet xmlSimulation(Node node) throws IOException {
		SimulationParameterSet theset = null;
		Element e = getFirstXMLChild(node, XML_NODE_SIMULATION);
		if (e!=null) {
			if (isXMLVersion(7, 0)) {
				theset = new SimulationParameterSet(getMandatoryUUID(e, XML_ATTR_ID));
				theset.setSimulationName(e.getAttribute(XML_ATTR_NAME));
			}
			else {
				theset = new SimulationParameterSet(getMandatoryUUID(e, XML_ATTR_NAME));
			}
			theset = new SimulationParameterSet(getMandatoryUUID(e, XML_ATTR_NAME));
			theset.setSimulationDescription(e.getAttribute(XML_ATTR_DESCRIPTION));
			theset.setSimulationVersion(e.getAttribute(XML_ATTR_VERSION));
			theset.setSimulationAuthors(e.getAttribute(XML_ATTR_AUTHORS));
			theset.setSimulationDate(getOptDate(e,XML_ATTR_DATE,null));
			xmlEnvironment(theset, e);
			xmlTime(theset, e);
			xmlSpawners(theset, e);
		}
		return theset;
	}
	
	/** Extract the time.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	@SuppressWarnings("static-method")
	protected void xmlTime(SimulationParameterSet theset, Node node) throws IOException {
		Element e = getFirstXMLChild(node, XML_NODE_TIME);
		if (e!=null) {
			TimeParameterType type = TimeParameterType.REALTIME;
			String str = e.getAttribute(XML_ATTR_TYPE);
			if (XML_VALUE_STEP.equalsIgnoreCase(str)) {
				type = TimeParameterType.STEP_BY_STEP;
			}
			str = e.getAttribute(XML_ATTR_UNIT);
			TimeUnit unit = TimeUnit.SECONDS;
			if (XML_VALUE_MILLISECOND.equalsIgnoreCase(str)) {
				unit = TimeUnit.MILLISECONDS;
			}
			else if (XML_VALUE_MINUTE.equalsIgnoreCase(str)) {
				unit = TimeUnit.MINUTES;
			}
			else if (XML_VALUE_HOUR.equalsIgnoreCase(str)) {
				unit = TimeUnit.HOURS;
			}
			double timeStep = getOptDouble(e, XML_ATTR_TIMESTEP, 1.);
			
			theset.setTimeManager(new TimeSimulationParameterSet(
					theset.getEnvironmentDimension(), type, unit, timeStep));
		}
	}
	
	/** Extract the enviromnent.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	protected void xmlEnvironment(SimulationParameterSet theset, Node node) throws IOException {
		Element e = getFirstXMLChild(node, XML_NODE_ENVIRONMENT);
		if (e!=null) {
			// Get space dimension
			float dimension = (float)getMandatoryDouble(e, XML_ATTR_DIMENSION);
			if (dimension!=1.5f) throw new IOException("invalid environment dimension: "+dimension);  //$NON-NLS-1$
			
			theset.setEnvironmentDimension(dimension);
			
			Node placesNode = getFirstXMLChild(e, XML_NODE_PLACES);
			if (placesNode!=null) {
				for(Element placeNode : getXMLChildren(placesNode, XML_NODE_PLACE)) {
					xmlPlace(theset, placeNode);
				}
			}
			
			Element portalsNode = getFirstXMLChild(e, XML_NODE_PORTALS);
			if (portalsNode!=null && isActivatedNode(portalsNode)) {
				for(Element portalNode : getXMLChildren(portalsNode, XML_NODE_PORTAL)) {
					xmlPortal(theset, portalNode);
				}
			}
		}
	}
	
	/** Extract the place.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	protected void xmlPlace(SimulationParameterSet theset, Element node) throws IOException {
		if (isActivatedNode(node)) {
			UUID placeId;
			if (isXMLVersion(7, 0)) {
				placeId = getMandatoryUUID(node, XML_ATTR_ID);
				EnvironmentSimulationParameterSet env = theset.getEnvironmentParameters(placeId);
				if (env!=null) {
					env.setPlaceName(getOptString(node, XML_ATTR_NAME, null));
					String perceptionAlgorithm = getOptString(node, XML_ATTR_PERCEPTIONALGO, null);
					String perceptionTraversal = getOptString(node, XML_ATTR_PERCEPTIONTRAVERSAL, null);
					PerceptionGeneratorType algorithmType = PerceptionGeneratorType.getSystemDefault();
					if (perceptionAlgorithm!=null) {
						if (XML_VALUE_PARALLEL.equalsIgnoreCase(perceptionAlgorithm)) {
							if (XML_VALUE_BOTTOMUP.equalsIgnoreCase(perceptionTraversal)) {
								algorithmType = PerceptionGeneratorType.LOCAL_THREADED_BOTTOMUP;
							}
							else {
								algorithmType = PerceptionGeneratorType.LOCAL_THREADED_TOPDOWN;
							}
						}
						else {
							if (XML_VALUE_BOTTOMUP.equalsIgnoreCase(perceptionTraversal)) {
								algorithmType = PerceptionGeneratorType.LOCAL_SEQUENTIAL_BOTTOMUP;
							}
							else {
								algorithmType = PerceptionGeneratorType.LOCAL_SEQUENTIAL_TOPDOWN;
							}
						}
					}
					env.setPerceptionGeneratorType(algorithmType);
				}
			}
			else {
				placeId = getMandatoryUUID(node, XML_ATTR_NAME);
			}
			xmlGroundEnviromnent(theset, placeId, node);
			xmlStaticEnviromnent(theset, placeId, node);
			xmlDynamicEnviromnent(theset, placeId, node);
			xmlEnviromnentProbes(theset, placeId, node);
		}
	}

	/** Extract the ground.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param placeName is the name of the place.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	protected void xmlGroundEnviromnent(SimulationParameterSet theset, UUID placeName, Node node) throws IOException {
		Element e = getFirstXMLChild(node, XML_NODE_GROUNDENVIRONMENT);
		if (e!=null) {
			EnvironmentSimulationParameterSet env = theset.getEnvironmentParameters(placeName);

			UUID id = getMandatoryUUID(e,XML_ATTR_ID);
			env.setGroundIdentifier(id);

			URL shapeFile = getMandatoryURL(e,XML_ATTR_SHAPEFILE);
			URL dbaseFile = getOptURL(e,XML_ATTR_DBASEFILE,null);

			env.setGroundRoadShapeResource(shapeFile);
			env.setGroundRoadAttributeResource(dbaseFile);
			env.setGroundType(GroundType.NONE);
		}
	}
	
	/** Extract the static environment.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param placeName is the name of the place.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	protected void xmlStaticEnviromnent(SimulationParameterSet theset, UUID placeName, Node node) throws IOException {
		Element e = getFirstXMLChild(node, XML_NODE_STATICENVIRONMENT);
		if (e!=null) {
			try {
				Class<?> clazz = Class.forName(e.getAttribute(XML_ATTR_CLASS));
				URL prebuild = getOptURL(e,XML_ATTR_PREBUILDRESOURCE,null);
				
				EnvironmentSimulationParameterSet env = theset.getEnvironmentParameters(placeName);
				
				env.setStaticEnvironmentType(clazz);
				env.setStaticEnvironmentResources(prebuild);
			}
			catch (Exception e1) {
				throw new IOException(e1);
			}
		}
	}

	/** Extract the dynamic environment.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param placeName is the name of the place.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	protected void xmlDynamicEnviromnent(SimulationParameterSet theset, UUID placeName, Node node) throws IOException {
		Element e = getFirstXMLChild(node, XML_NODE_DYNAMICENVIRONMENT);
		if (e!=null) {
			try {
				Class<?> clazz = getOptClass(e,XML_ATTR_CLASS);
				URL prebuild = getOptURL(e,XML_ATTR_PREBUILDRESOURCE,null);
				Class<?> defaultAgentType = getOptClass(e,XML_ATTR_AGENTTYPE);
				
				EnvironmentSimulationParameterSet env = theset.getEnvironmentParameters(placeName);
				
				if (defaultAgentType!=null)
					env.addDefaultDynamicEnvironmentAgentMapping((Class<? extends SituatedAgent<?,?,?>>)defaultAgentType);

				env.setDynamicEnvironmentType(clazz);
				env.setDynamicEnvironmentResources(prebuild);
				
				for(Element elt : getXMLChildren(e, XML_NODE_AGENT)) {
					xmlAgent(env, elt);
				}
			}
			catch(IOException e1) {
				throw e1;
			}
			catch (Exception e1) {
				throw new IOException(e1);
			}
		}
	}
	
	/** Extract the agent.
	 * 
	 * @param env is the environment description to fill.
	 * @param agentNode is the node for an agent
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	protected void xmlAgent(EnvironmentSimulationParameterSet env, Element agentNode) throws IOException {
		if (isActivatedNode(agentNode)) {
			Class<?> agentClass = getMandatoryClass(agentNode, XML_ATTR_CLASS);
			Class<?> bodyType = getMandatoryClass(agentNode, XML_ATTR_TYPE);
			env.addDynamicEnvironmentAgentMapping(
					(Class<? extends AgentBody<?,?>>)bodyType,
					(Class<? extends SituatedAgent<?,?,?>>)agentClass);
			xmlPreBuiltAgentSemantic(env, agentNode, agentClass);
		}
	}

	/** Extract the agent semantic.
	 * 
	 * @param env is the environment description to fill.
	 * @param agentNode is the node for an agent.
	 * @param agentClass is the Java class for the agents.
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "static-method" })
	protected void xmlPreBuiltAgentSemantic(EnvironmentSimulationParameterSet env, Element agentNode, Class<?> agentClass) throws IOException {
		String textContent;
		Class<? extends Semantic> agentSemantic;
		for(Element semanticNode : getXMLChildren(agentNode, XML_NODE_AGENTSEMANTIC)) {
			textContent = semanticNode.getTextContent();
			if ((textContent!=null)&&(!"".equals(textContent))) { //$NON-NLS-1$
				try {
					agentSemantic = (Class<? extends Semantic>)Class.forName(textContent);
				}
				catch(Exception ex) {
					throw new IOException("not a semantic class: "+textContent, ex); //$NON-NLS-1$
				}
				env.addDynamicEnvironmentAgentSemantic(
						(Class<? extends SituatedAgent<?,?,?>>)agentClass,
						agentSemantic);
			}
			throw new IOException("not a semantic class: "+textContent); //$NON-NLS-1$
		}
	}

	/** Extract the environment probes.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param placeName is the name of the place.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	protected void xmlEnviromnentProbes(SimulationParameterSet theset, UUID placeName, Node node) throws IOException {
		Element e = getFirstXMLChild(node, XML_NODE_ENVIRONMENTPROBES);
		if (e!=null && isActivatedNode(e)) {
			EnvironmentSimulationParameterSet env = theset.getEnvironmentParameters(placeName);

			for(Element probeNode : getXMLChildren(e, XML_NODE_ENVIRONMENTPROBE)) {
				xmlEnviromnentProbe(theset, env, placeName, probeNode);
			}
		}
	}

	/** Extract the environment probe.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param env is the variable to fill with the content of the DOM tree.
	 * @param placeName is the name of the place.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	@SuppressWarnings("static-method")
	protected void xmlEnviromnentProbe(SimulationParameterSet theset, EnvironmentSimulationParameterSet env, UUID placeName, Element node) throws IOException {
		if (isActivatedNode(node)) {
			String name;
			PositionParameter position;
	
			name = node.getAttribute(XML_ATTR_NAME);
			position = getMandatoryPosition(theset, node, placeName);
		
			env.addProbe(name,position);
		}
	}

	/** Extract the portal.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	protected void xmlPortal(SimulationParameterSet theset, Element node) throws IOException {
		if (isActivatedNode(node)) {
			OutputParameter<UUID> outStr = new OutputParameter<UUID>();
			OutputParameter<PositionParameter> sourcePosition1 = new OutputParameter<PositionParameter>();
			OutputParameter<PositionParameter> sourcePosition2 = new OutputParameter<PositionParameter>();
			OutputParameter<Direction1D> inDirection = new OutputParameter<Direction1D>();
			xmlPortalSource(theset, node, outStr, sourcePosition1, sourcePosition2, inDirection);
			
			UUID sourceId = outStr.get();
			EnvironmentSimulationParameterSet sourceCfg = theset.getEnvironmentParameters(sourceId, false);
			if (sourceCfg==null) throw new IOException("undefined place "+sourceId+" in <portalSource>"); //$NON-NLS-1$ //$NON-NLS-2$
			
			OutputParameter<PositionParameter> targetPosition = new OutputParameter<PositionParameter>();
			OutputParameter<Direction1D> outDirection = new OutputParameter<Direction1D>();
			xmlPortalTarget(theset, node, sourceId, outStr, targetPosition, outDirection);
			UUID targetId = outStr.get();
			EnvironmentSimulationParameterSet targetCfg = theset.getEnvironmentParameters(targetId, false);
			if (targetCfg==null) throw new IOException("undefined place "+targetId+" in <portalSource>"); //$NON-NLS-1$ //$NON-NLS-2$
			
			PortalSimulationParameterSet portal = new PortalSimulationParameterSet(
					theset.getEnvironmentDimension(),
					sourceId,
					sourcePosition1.get(),
					sourcePosition2.get(),
					inDirection.get(),
					targetId,
					targetPosition.get(),
					outDirection.get());
	
			sourceCfg.addPortal(portal);
		}
	}
	
	/** Extract the portal source.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @param sourceId
	 * @param pos1
	 * @param pos2
	 * @param direction
	 * @throws IOException 
	 */
	@SuppressWarnings("static-method")
	protected void xmlPortalSource(SimulationParameterSet theset, Element node,
			OutputParameter<UUID> sourceId,
			OutputParameter<PositionParameter> pos1,
			OutputParameter<PositionParameter> pos2,
			OutputParameter<Direction1D> direction) throws IOException {
		Element source = getFirstXMLChild(node, XML_NODE_PORTALSOURCE);
		if (source==null) throw new IOException("no <portalSource> in a <portal>"); //$NON-NLS-1$
		
		
		sourceId.set(getMandatoryUUID(source, XML_ATTR_PLACE));
		
		direction.set(getOptDirection1D(source, XML_ATTR_DIRECTION));
		PositionParameter sourcePosition1, sourcePosition2;

		String road = getMandatoryString(source, XML_ATTR_ROAD);
		double x = getMandatoryDouble(source, XML_ATTR_X);
		double y1 = getMandatoryDouble(source, XML_ATTR_Y1);
		double y2 = getMandatoryDouble(source, XML_ATTR_Y2);
		sourcePosition1 = new PositionParameter(road, x, y1);
		sourcePosition1.setPlace(sourceId.get());
		sourcePosition2 = new PositionParameter(road, x, y2);
		sourcePosition2.setPlace(sourceId.get());
	}

	/** Extract the portal target.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @param sourceId is the identifier of the source place for the portal
	 * @param targetId
	 * @param targetPosition
	 * @param direction
	 * @throws IOException 
	 */
	@SuppressWarnings("static-method")
	protected void xmlPortalTarget(SimulationParameterSet theset, Element node, UUID sourceId,
			OutputParameter<UUID> targetId,
			OutputParameter<PositionParameter> targetPosition,
			OutputParameter<Direction1D> direction) throws IOException {
		Element target = getFirstXMLChild(node, XML_NODE_PORTALTARGET);
		if (target==null) throw new IOException("no <portalTarget> in a <portal>"); //$NON-NLS-1$

		targetId.set(getMandatoryUUID(target, XML_ATTR_PLACE));
		if (targetId.get()!=null && targetId.get().equals(sourceId)) throw new IOException("a portal must not loop on the same place: "+targetId.get()); //$NON-NLS-1$

		String road = getMandatoryString(target, XML_ATTR_ROAD);
		double x = getMandatoryDouble(target, XML_ATTR_X);
		double y = getMandatoryDouble(target, XML_ATTR_Y);
		direction.set(getMandatoryDirection1D(target, XML_ATTR_DIRECTION));
		targetPosition.set(new PositionParameter(road, x, y));
		targetPosition.get().setPlace(targetId.get());
	}

	/** Extract the spawners.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	protected void xmlSpawners(SimulationParameterSet theset, Element node) throws IOException {
		Element e = getFirstXMLChild(node, XML_NODE_SPAWNERS);
		if (e!=null && isActivatedNode(node)) {
			for(Element spawnerNode : getXMLChildren(e, XML_NODE_SPAWNER)) {
				xmlSpawner(theset, spawnerNode);
			}
		}
	}

	/** Extract a spawner.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	protected void xmlSpawner(SimulationParameterSet theset, Element node) throws IOException {
		if (isActivatedNode(node)) {
			try {
				UUID spawnerId;
				
				if (isXMLVersion(7,0)) {
					spawnerId = getMandatoryUUID(node, XML_ATTR_ID);
				}
				else {
					spawnerId = getMandatoryUUID(node, XML_ATTR_NAME);
				}
	
				SpawnerSimulationParameterSet spawner = theset.getSpawnerParameters(spawnerId);
				
				if (isXMLVersion(7,0)) {
					spawner.setName(getOptString(node, XML_ATTR_NAME, null));
				}

				PositionParameter position = getMandatoryPosition(spawner, node);
				if (position.getPlace()==null || "".equals(position.getPlace())) //$NON-NLS-1$
					throw new IOException("no attribute \"place\" in tag <spawner>"); //$NON-NLS-1$
				spawner.setPosition(position);
				
				double startDate = getOptDouble(node, XML_ATTR_STARTDATE, 0);
				double endDate = getOptDouble(node, XML_ATTR_ENDDATE, Double.POSITIVE_INFINITY);
				spawner.setLifeTime(startDate, endDate);
	
				SpawnerParameterType type;
				String strType = getMandatoryString(node,XML_ATTR_TYPE);
				if (XML_VALUE_POINT.equalsIgnoreCase(strType)) {
					type = SpawnerParameterType.POINT;
				}
				else if (XML_VALUE_AREA.equalsIgnoreCase(strType)) {
					type = SpawnerParameterType.AREA;
				}
				else {
					throw new IOException("invalid attribute value '"+strType+"'"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				spawner.setType(type);
	
				double width = getOptDouble(node, XML_ATTR_WIDTH, 0);
				spawner.setDimension(width);
				
				Direction1D direction = getMandatoryDirection1D(node, XML_ATTR_DIRECTION);
				spawner.setDirection1D(direction);
				
				for(Element entityNode : getXMLChildren(node, XML_NODE_ENTITY)) {
					if (entityNode!=null) xmlEntity(spawner, entityNode);
				}
			}
			catch(IOException e1) {
				throw e1;
			}
			catch(Exception e1) {
				throw new IOException(e1);
			}
		}
	}
	
	/** Extract an entity.
	 *
	 * @param <ADR> is the type of the agent's address. 
	 * @param <INF> is the type of influence this situated agent may receive and forward to the collector
	 * @param <PER> is the type of perception this situated agent may receive
	 * @param spawner is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	protected <ADR, INF extends Influence, PER extends Perception> void xmlEntity(SpawnerSimulationParameterSet spawner, Element node) throws IOException {
		if (isActivatedNode(node)) {
			try {
				Class<? extends SituatedAgent<ADR,INF,PER>> agent = getMandatoryClass(node, XML_ATTR_AGENTTYPE);
				Long budget = getOptLong(node, XML_ATTR_BUDGET, null);
				
				OutputParameter<Double> bodyLength = new OutputParameter<Double>(JasimConstants.DEFAULT_VEHICLE_LENGTH);
				OutputParameter<Double> bodyLateralSize = new OutputParameter<Double>(JasimConstants.DEFAULT_VEHICLE_WIDTH);
				OutputParameter<Double> bodyVerticalSize = new OutputParameter<Double>(JasimConstants.DEFAULT_VEHICLE_HEIGHT);
				OutputParameter<Class<? extends AgentBody<INF,PER>>> bodyType = new OutputParameter<Class<? extends AgentBody<INF,PER>>>();
				Element e;
						
				Element bodyNode = getFirstXMLChild(node, XML_NODE_BODY);
				if (bodyNode!=null) xmlBody(bodyNode, bodyLength, bodyLateralSize, bodyVerticalSize, bodyType);
				
				EntitySimulationParameterSet entity = spawner.createEntity(agent, bodyType.get());
				
				entity.setVehicleBody(
						bodyLength.get(),
						bodyLateralSize.get(),
						bodyVerticalSize.get());
				
				if (budget==null) {
					entity.setInfiniteBudget();
				}
				else {
					entity.setBudget(budget);
				}
				
				e = getFirstXMLChild(node, XML_NODE_FRUSTUMS);
				if (e!=null) {
					for(Element frustumNode : getXMLChildren(e, XML_NODE_FRUSTUM)) {
						xmlFrustum(entity, frustumNode);
					}
				}
				
				e = getFirstXMLChild(node, XML_NODE_AGENTSEMANTICS);
				if (e!=null) {
					xmlSpawnedAgentSemantics(entity, e);
				}			
	
				e = getFirstXMLChild(node, XML_NODE_WAYPOINTS);
				if (e!=null && isActivatedNode(e)) {
					for(Element waypointNode : getXMLChildren(e, XML_NODE_WAYPOINT)) {
						xmlWaypoint(entity, waypointNode);
					}
				}
	
				e = getFirstXMLChild(node, XML_NODE_GOALS);
				if (e!=null && isActivatedNode(e)) {
					for(Element goalNode : getXMLChildren(e, XML_NODE_GOAL)) {
						xmlGoal(entity, goalNode);
					}
				}
	
				e = getFirstXMLChild(node, XML_NODE_ATTRIBUTES);
				if (e!=null && isActivatedNode(e)) {
					for(Element attrNode : getXMLChildren(e, XML_NODE_ATTRIBUTE)) {
						xmlAttr(entity, attrNode);
					}
				}
	
				xmlGenerationLaw(entity,node);
				xmlProbes(entity, node);
			}
			catch(IOException e1) {
				throw e1;
			}
			catch(Throwable e1) {
				throw new IOException(e1);
			}
		}
	}
	
	/** Extract a body.
	 * 
	 * @param <ADR> is the type of the agent's address. 
	 * @param <INF> is the type of influence this situated agent may receive and forward to the collector
	 * @param <PER> is the type of perception this situated agent may receive
	 * @param node is the node to explore
	 * @param bodyLength
	 * @param bodyLateralSize
	 * @param bodyVerticalSize
	 * @param bodyType
	 * @throws IOException 
	 */
	@SuppressWarnings({ "unchecked", "static-method" })
	protected <ADR, INF extends Influence, PER extends Perception> void xmlBody(Element node,
			OutputParameter<Double> bodyLength,
			OutputParameter<Double> bodyLateralSize,
			OutputParameter<Double> bodyVerticalSize,
			OutputParameter<Class<? extends AgentBody<INF,PER>>> bodyType) throws IOException {
		bodyType.set(null);
		if (node!=null) {
			Class<?> rawBodyType = getOptClass(node,XML_ATTR_TYPE);
			if (rawBodyType==null || AgentBody.class.isAssignableFrom(rawBodyType)) {
				bodyType.set((Class<? extends AgentBody<INF,PER>>)rawBodyType);
			}
			bodyLength.set(getOptDouble(node, XML_ATTR_CURVILINESIZE, JasimConstants.DEFAULT_VEHICLE_LENGTH));
			bodyLateralSize.set(getOptDouble(node, XML_ATTR_LATERALSIZE, JasimConstants.DEFAULT_VEHICLE_WIDTH));
		}
		else {
			bodyLength.set(JasimConstants.DEFAULT_VEHICLE_LENGTH);
			bodyLateralSize.set(JasimConstants.DEFAULT_VEHICLE_WIDTH);
		}
		bodyVerticalSize.set(JasimConstants.DEFAULT_VEHICLE_HEIGHT);
	}

	/** Extract the agent semantic.
	 * 
	 * @param entity is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException
	 */
	protected void xmlSpawnedAgentSemantics(EntitySimulationParameterSet entity, Element node) throws IOException {
		for(Element semanticNode : getXMLChildren(node, XML_NODE_AGENTSEMANTIC)) {
			xmlSpawnedAgentSemantic(entity, semanticNode);
		}
	}

	/** Extract the agent semantic.
	 * 
	 * @param entity is the variable to fill with the content of the DOM tree.
	 * @param semanticNode is the node to explore
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "static-method" })
	protected void xmlSpawnedAgentSemantic(EntitySimulationParameterSet entity, Element semanticNode) throws IOException {
		Class<? extends Semantic> agentSemantic;
		String textContent = semanticNode.getTextContent();
		if ((textContent!=null)&&(!"".equals(textContent))) { //$NON-NLS-1$
			try {
				agentSemantic = (Class<? extends Semantic>)Class.forName(textContent);
			}
			catch(Exception ex) {
				throw new IOException("not a semantic class: "+textContent, ex); //$NON-NLS-1$
			}
			
			entity.addSemantic(agentSemantic);
		}
		throw new IOException("not a semantic class: "+textContent); //$NON-NLS-1$
	}

	/** Extract a frustum.
	 * 
	 * @param entity is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	@SuppressWarnings("static-method")
	protected void xmlFrustum(EntitySimulationParameterSet entity, Element node) throws IOException {
		FrustumType type = FrustumType.parse(node.getAttribute(XML_ATTR_TYPE));

		FrustumDescription description;
		
		switch(type) {
		case RECTANGLE:
			{
				double farDistance = getMandatoryDouble(node, XML_ATTR_FARDISTANCE);				
				double lrDistance = getMandatoryDouble(node, XML_ATTR_SIDESIZE);
				description = new RectangularFrustumDescription(
					0., farDistance, lrDistance, 0.);
			}
			break;
		default:
			throw new IOException("invalid frustum type: "+type); //$NON-NLS-1$
		}
		
		entity.addFrustum(description);
	}

	/** Extract a waypoint.
	 * 
	 * @param entity is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	protected void xmlWaypoint(EntitySimulationParameterSet entity, Element node) throws IOException {
		if (isActivatedNode(node)) {
			String name = null;
			if (isXMLVersion(7,0)) {
				name = getOptString(node, XML_ATTR_NAME, null);
			}

			double time = getOptDouble(node, XML_ATTR_TIME, Double.NaN);
			double velocity = getOptDouble(node, XML_ATTR_VELOCITY, Double.NaN);
			PositionParameter position = getMandatoryPosition(entity,node);
					
			Direction1D direction = getOptDirection1D(node, XML_ATTR_DIRECTION);
			entity.addWaypoint(name,position,direction,velocity,time);
		}
	}

	/** Extract a goal.
	 * 
	 * @param entity is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	protected void xmlGoal(EntitySimulationParameterSet entity, Element node) throws IOException {
		if (isActivatedNode(node)) {
			String name = null;
			if (isXMLVersion(7,0)) {
				name = getOptString(node, XML_ATTR_NAME, null);
			}

			PositionParameter position = getMandatoryPosition(entity, node);
			double time = getOptDouble(node, XML_ATTR_TIME, Double.NaN);
	
			Direction1D direction = getOptDirection1D(node, XML_ATTR_DIRECTION);
			entity.addGoal(name,position,direction,time);
		}
	}

	/** Extract a generation law.
	 * 
	 * @param entity is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	protected void xmlGenerationLaw(EntitySimulationParameterSet entity, Element node) throws IOException {
		Element e = getFirstXMLChild(node, XML_NODE_GENERATIONLAW);
		if (e==null) throw new IOException();
		
		try {
			Class<?> type = getMandatoryClass(e, XML_ATTR_CLASS);
			
			Map<String,String> parameters = new HashMap<String,String>();
			
			for(Element lawNode : getXMLChildren(e, XML_NODE_LAWPARAM)) {
				xmlLawParam(lawNode, parameters);
			}
			
			if (StochasticLaw.class.isAssignableFrom(type)) 
				entity.setStochasticGenerationLaw((Class<? extends StochasticLaw>)type,parameters);
			else if (SpawningLaw.class.isAssignableFrom(type)) 
				entity.setSpawningGenerationLaw((Class<? extends SpawningLaw>)type,parameters);
			else
				throw new IOException("unsupported spawing law type: "+type.getCanonicalName()); //$NON-NLS-1$
		}
		catch(IOException e1) {
			throw e1;
		}
		catch(Exception e1) {
			throw new IOException(e1);
		}
	}

	/** Extract a generation law parameter.
	 * 
	 * @param node is the node to explore
	 * @param parameters is the structure to fill.
	 * @throws IOException 
	 */
	@SuppressWarnings("static-method")
	protected void xmlLawParam(Element node, Map<String,String> parameters) throws IOException {
		String name = node.getAttribute(XML_ATTR_NAME);
		String value = node.getAttribute(XML_ATTR_VALUE);
		parameters.put(name,value);
	}

	/** Extract an attribute.
	 * 
	 * @param entity is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	@SuppressWarnings("static-method")
	protected void xmlAttr(EntitySimulationParameterSet entity, Element node) throws IOException {
		if (isActivatedNode(node)) {
			String name = node.getAttribute(XML_ATTR_NAME);
			String value = node.getAttribute(XML_ATTR_VALUE);
			entity.setAttribute(name,value);
		}
	}

	/** Extract the entity probes.
	 * 
	 * @param entity is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	@SuppressWarnings("static-method")
	protected void xmlProbes(EntitySimulationParameterSet entity, Node node) throws IOException {
		Element e = getFirstXMLChild(node, XML_NODE_PROBES);
		if (e!=null && isActivatedNode(e)) {
			try {
				String name;
	
				for(Element probeNode : getXMLChildren(e, XML_NODE_PROBE)) {
					if (isActivatedNode(probeNode)) {
						name = probeNode.getAttribute(XML_ATTR_NAME);
						entity.addProbe(name);
					}
				}
			}
			catch(Exception e1) {
				throw new IOException(e1);
			}
		}
	}
	
}
