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

import javax.vecmath.Vector2d;

import org.arakhne.afc.util.OutputParameter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.utbm.set.jasim.JasimConstants;
import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody;
import fr.utbm.set.jasim.environment.interfaces.body.factory.CircularFrustumDescription;
import fr.utbm.set.jasim.environment.interfaces.body.factory.FrustumDescription;
import fr.utbm.set.jasim.environment.interfaces.body.factory.FrustumDescription.FrustumType;
import fr.utbm.set.jasim.environment.interfaces.body.factory.PedestrianFrustumDescription;
import fr.utbm.set.jasim.environment.interfaces.body.factory.RectangularFrustumDescription;
import fr.utbm.set.jasim.environment.interfaces.body.factory.TriangularFrustumDescription;
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
 * Parse an XML file to extract 3D simulation parameters.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class Parser3D extends AbstractParser {

	/**
	 * @param type is the current type of the file to parse.
	 */
	Parser3D(SFGFileType type) {
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
			if (dimension!=3f) throw new IOException("invalid environment dimension: "+dimension);  //$NON-NLS-1$
			
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

			URL prebuild = getOptURL(e,XML_ATTR_PREBUILDRESOURCE,null);
			if (prebuild!=null)
				env.setGroundPrebuiltResource(prebuild);

			GroundType type; 
			String stype = getMandatoryString(e, XML_ATTR_TYPE);
			if (isXMLVersion(8, 0)) {
				// 8.0
				if (XML_VALUE_ALIGNEDINDOOR.equalsIgnoreCase(stype)) {
					type = GroundType.ALIGNED_CONSTANT;
				}
				else if (XML_VALUE_ORIENTEDINDOOR.equalsIgnoreCase(stype)) {
					type = GroundType.ORIENTED_CONSTANT;
				}
				else if (XML_VALUE_REPULSIONHEIGHMAP.equalsIgnoreCase(stype)) {
					type = GroundType.REPULSION_HEIGHTMAP;
				}
				else if (XML_VALUE_HEIGHTMAP.equalsIgnoreCase(stype)) {
					type = GroundType.STANDARD_HEIGHTMAP;
				}
				else if (XML_VALUE_NONE.equalsIgnoreCase(stype)) {
					type = GroundType.NONE;
				}
				else if (XML_VALUE_ANY.equalsIgnoreCase(stype)) {
					// Try to detect the type of ground according to inner xml nodes
					if (getFirstXMLChild(e, XML_NODE_HEIGHTMAP)!=null) {
						type = GroundType.STANDARD_HEIGHTMAP;
					}
					else {
						Element indoorElement = getFirstXMLChild(e, XML_NODE_INDOORGROUND);
						if (indoorElement!=null) {
							indoorElement = getFirstXMLChild(indoorElement, XML_NODE_ORIENTEDAREA);
							if (indoorElement!=null)
								type = GroundType.ORIENTED_CONSTANT;
							else
								type = GroundType.ALIGNED_CONSTANT;
						}
						else {
							type = GroundType.NONE;
						}
					}
				}
				else {
					throw new IOException("Ground environment type '" //$NON-NLS-1$
							+stype
							+"' is not supported"); //$NON-NLS-1$
				}
			}
			else {
				// 7.0 and lower
				if (XML_VALUE_CONSTANT.equalsIgnoreCase(stype)) {
					type = GroundType.ALIGNED_CONSTANT;
				}
				else if (XML_VALUE_REPULSION.equalsIgnoreCase(stype)) {
					type = GroundType.REPULSION_HEIGHTMAP;
				}
				else if (XML_VALUE_SIMPLE.equalsIgnoreCase(stype)) {
					type = GroundType.STANDARD_HEIGHTMAP;
				}
				else if (XML_VALUE_NONE.equalsIgnoreCase(stype)) {
					type = GroundType.NONE;
				}
				else if (XML_VALUE_ANY.equalsIgnoreCase(stype)) {
					// Try to detect the type of ground according to inner xml nodes
					if (getFirstXMLChild(e, XML_NODE_HEIGHTMAP)!=null) {
						type = GroundType.STANDARD_HEIGHTMAP;
					}	
					else if (getFirstXMLChild(e, XML_NODE_INDOORGROUND)!=null) {
						type = GroundType.ALIGNED_CONSTANT;
					}
					else {
						type = GroundType.NONE;
					}
				}
				else {
					throw new IOException("Ground environment type '" //$NON-NLS-1$
							+stype
							+"' is not supported"); //$NON-NLS-1$
				}
			}

			env.setGroundType(type);

			switch(type) {
			case NONE:
				break;
			case STANDARD_HEIGHTMAP:
			case REPULSION_HEIGHTMAP:
				xmlHeightmap(env, e);
				break;
			case ALIGNED_CONSTANT:
			case ORIENTED_CONSTANT:
				xmlIndoorGround(env, e);
				break;
			default:
			}
		}
	}
	
	/** Extract the heighmap.
	 *
	 * @param env describes the environment.
	 * @param parentNode is the parent node of the heightmap node to parse.
	 * @throws IOException 
	 */
	protected void xmlIndoorGround(EnvironmentSimulationParameterSet env, Element parentNode) throws IOException {
		Element indoorGround = getFirstXMLChild(parentNode, XML_NODE_INDOORGROUND);
		if (indoorGround!=null) {
			
			double z = getMandatoryDouble(indoorGround, XML_ATTR_Z);
			env.setGroundZero(z-Double.MIN_VALUE*2.);

			if (isXMLVersion(8, 0)) {
				Element child = getFirstXMLChild(indoorGround, XML_NODE_ALIGNEDAREA);
				if (child!=null) {
					double minX = getMandatoryDouble(child, XML_ATTR_MINX);
					double minY = getMandatoryDouble(child, XML_ATTR_MINY);
					double maxX = getMandatoryDouble(child, XML_ATTR_MAXX);
					double maxY = getMandatoryDouble(child, XML_ATTR_MAXY);
					env.setGroundMinPoint(minX, minY, z);
					env.setGroundMaxPoint(maxX, maxY, z);
				}
				else {
					child = getFirstXMLChild(indoorGround, XML_NODE_ORIENTEDAREA);
					assert(child!=null);
					double centerX = getMandatoryDouble(child, XML_ATTR_CENTERX);
					double centerY = getMandatoryDouble(child, XML_ATTR_CENTERY);
					double Rx = getMandatoryDouble(child, XML_ATTR_RX);
					double Ry = getMandatoryDouble(child, XML_ATTR_RY);
					double Sx = getMandatoryDouble(child, XML_ATTR_SX);
					double Sy = getMandatoryDouble(child, XML_ATTR_SY);
					env.setGroundCenterPoint(centerX, centerY, z);
					env.setGroundAxisVectors(Rx, Ry, Sx, Sy);
				}
			}
			else {
				double minX = getMandatoryDouble(indoorGround, XML_ATTR_MINX);
				double minY = getMandatoryDouble(indoorGround, XML_ATTR_MINY);
				double maxX = getMandatoryDouble(indoorGround, XML_ATTR_MAXX);
				double maxY = getMandatoryDouble(indoorGround, XML_ATTR_MAXY);
				env.setGroundMinPoint(minX, minY, z);
				env.setGroundMaxPoint(maxX, maxY, z);
			}
									
			URL semantic = getOptURL(indoorGround,XML_ATTR_SEMANTIC,null);
			if (semantic!=null) env.setGroundSemanticResource(semantic);
		}
		else {
			throw new IOException("tag "+XML_NODE_INDOORGROUND+" required"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/** Extract the heighmap.
	 *
	 * @param env describes the environment.
	 * @param parentNode is the parent node of the heightmap node to parse.
	 * @throws IOException 
	 */
	protected void xmlHeightmap(EnvironmentSimulationParameterSet env, Element parentNode) throws IOException {
		Element heightmap = getFirstXMLChild(parentNode, XML_NODE_HEIGHTMAP);
		if (heightmap!=null) {
			env.setGroundHeightResource(getMandatoryURL(heightmap, XML_ATTR_URL));
			double minX = getMandatoryDouble(heightmap, XML_ATTR_MINX);
			double minY = getMandatoryDouble(heightmap, XML_ATTR_MINY);
			double minZ = getMandatoryDouble(heightmap, XML_ATTR_MINZ);
			double maxX = getMandatoryDouble(heightmap, XML_ATTR_MAXX);
			double maxY = getMandatoryDouble(heightmap, XML_ATTR_MAXY);
			double maxZ = getMandatoryDouble(heightmap, XML_ATTR_MAXZ);
			env.setGroundMinPoint(minX, minY, minZ);
			env.setGroundMaxPoint(maxX, maxY, maxZ);
			byte rawGroundZero = getMandatoryByte(heightmap, XML_ATTR_RAWGROUNDZERO);
			double groundZero = getOptDouble(heightmap, XML_ATTR_GROUNDZERO, Double.NaN);
			if (Double.isNaN(groundZero))
				env.setRawGroundZero(rawGroundZero);
			else
				env.setGroundZero(groundZero);
			
			URL semantic = getOptURL(heightmap,XML_ATTR_SEMANTIC,null);
			if (semantic!=null) env.setGroundSemanticResource(semantic);
		}
		else {
			throw new IOException("tag "+XML_NODE_HEIGHTMAP+" required"); //$NON-NLS-1$ //$NON-NLS-2$
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
				Class<?> clazz = getMandatoryClass(e, XML_ATTR_CLASS);
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
			OutputParameter<Boolean> onLeft = new OutputParameter<Boolean>();
			xmlPortalSource(theset, node, outStr, sourcePosition1, sourcePosition2, onLeft);
			
			UUID sourceId = outStr.get();
			EnvironmentSimulationParameterSet sourceCfg = theset.getEnvironmentParameters(sourceId, false);
			if (sourceCfg==null) throw new IOException("undefined place "+sourceId+" in <portalSource>"); //$NON-NLS-1$ //$NON-NLS-2$
			
			OutputParameter<PositionParameter> targetPosition = new OutputParameter<PositionParameter>();
			OutputParameter<Vector2d> direction = new OutputParameter<Vector2d>();
			xmlPortalTarget(theset, node, sourceId, outStr, targetPosition, direction);
			UUID targetId = outStr.get();
			EnvironmentSimulationParameterSet targetCfg = theset.getEnvironmentParameters(targetId, false);
			if (targetCfg==null) throw new IOException("undefined place "+targetId+" in <portalSource>"); //$NON-NLS-1$ //$NON-NLS-2$
			
			PortalSimulationParameterSet portal = new PortalSimulationParameterSet(
					theset.getEnvironmentDimension(),
					sourceId,
					sourcePosition1.get(),
					sourcePosition2.get(),
					onLeft.get(),
					targetId,
					targetPosition.get(),
					direction.get().x,direction.get().y);
	
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
	 * @param onLeft
	 * @throws IOException 
	 */
	@SuppressWarnings("static-method")
	protected void xmlPortalSource(SimulationParameterSet theset, Element node,
			OutputParameter<UUID> sourceId,
			OutputParameter<PositionParameter> pos1,
			OutputParameter<PositionParameter> pos2,
			OutputParameter<Boolean> onLeft) throws IOException {
		Element source = getFirstXMLChild(node, XML_NODE_PORTALSOURCE);
		if (source==null) throw new IOException("no <portalSource> in a <portal>"); //$NON-NLS-1$
		
		
		sourceId.set(getMandatoryUUID(source, XML_ATTR_PLACE));
		
		onLeft.set(getOptBoolean(source, XML_ATTR_ONLEFT, true));
		PositionParameter sourcePosition1, sourcePosition2;

		double x1 = getMandatoryDouble(source, XML_ATTR_X1);
		double y1 = getMandatoryDouble(source, XML_ATTR_Y1);
		double x2 = getMandatoryDouble(source, XML_ATTR_X2);
		double y2 = getMandatoryDouble(source, XML_ATTR_Y2);
		sourcePosition1 = new PositionParameter(x1, y1);
		sourcePosition1.setPlace(sourceId.get());
		sourcePosition2 = new PositionParameter(x2, y2);
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
			OutputParameter<Vector2d> direction) throws IOException {
		Element target = getFirstXMLChild(node, XML_NODE_PORTALTARGET);
		if (target==null) throw new IOException("no <portalTarget> in a <portal>"); //$NON-NLS-1$

		targetId.set(getMandatoryUUID(target, XML_ATTR_PLACE));
		if (targetId.get()!=null && targetId.get().equals(sourceId)) throw new IOException("a portal must not loop on the same place: "+targetId.get()); //$NON-NLS-1$

		double x = getMandatoryDouble(target, XML_ATTR_X);
		double y = getMandatoryDouble(target, XML_ATTR_Y);
		direction.set(new Vector2d(
				getMandatoryDouble(target, XML_ATTR_DX),
				getMandatoryDouble(target, XML_ATTR_DY)));
		targetPosition.set(new PositionParameter(x, y));
		targetPosition.get().setPlace(targetId.get());
	}

	/** Extract the spawners.
	 * 
	 * @param theset is the variable to fill with the content of the DOM tree.
	 * @param node is the node to explore
	 * @throws IOException 
	 */
	protected void xmlSpawners(SimulationParameterSet theset, Node node) throws IOException {
		Element e = getFirstXMLChild(node, XML_NODE_SPAWNERS);
		if (e!=null && isActivatedNode(e)) {
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
				double height = getOptDouble(node, XML_ATTR_HEIGHT, 0);
				spawner.setDimension(width,height);
				
				double startAngle = getMandatoryDouble(node, XML_ATTR_STARTANGLE);
				double endAngle = getMandatoryDouble(node, XML_ATTR_ENDANGLE);
				spawner.setAngles(startAngle, endAngle);
				
				for(Element entityNode : getXMLChildren(node, XML_NODE_ENTITY)) {
					xmlEntity(spawner, entityNode);
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
				
				OutputParameter<Double> bodyHeight = new OutputParameter<Double>(JasimConstants.DEFAULT_PEDESTRIAN_HEIGHT);
				OutputParameter<Double> bodyRadius = new OutputParameter<Double>(JasimConstants.DEFAULT_PEDESTRIAN_SIZE);
				OutputParameter<Class<? extends AgentBody<INF,PER>>> bodyType = new OutputParameter<Class<? extends AgentBody<INF,PER>>>();
				Element e;
							
				xmlBody(getFirstXMLChild(node, XML_NODE_BODY), bodyHeight, bodyRadius, bodyType);
				
				EntitySimulationParameterSet entity = spawner.createEntity(agent, bodyType.get());
				
				entity.setPedestrianBody(bodyHeight.get(), bodyRadius.get());
				
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
				if (e!=null && isActivatedNode(node)) {
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
	 * @param bodyHeight
	 * @param bodyRadius
	 * @param bodyType
	 * @throws IOException 
	 */
	@SuppressWarnings({ "unchecked", "static-method" })
	protected <ADR, INF extends Influence, PER extends Perception> void xmlBody(Element node,
			OutputParameter<Double> bodyHeight,
			OutputParameter<Double> bodyRadius,
			OutputParameter<Class<? extends AgentBody<INF,PER>>> bodyType) throws IOException {
		bodyType.set(null);
		Class<?> rawBodyType = getOptClass(node,XML_ATTR_TYPE);
		if (node!=null) {
			if (rawBodyType==null || AgentBody.class.isAssignableFrom(rawBodyType)) {
				bodyType.set((Class<? extends AgentBody<INF,PER>>)rawBodyType);
			}
			bodyHeight.set(getOptDouble(node, XML_ATTR_HEIGHT, JasimConstants.DEFAULT_PEDESTRIAN_HEIGHT));
			bodyRadius.set(getOptDouble(node, XML_ATTR_RADIUS, JasimConstants.DEFAULT_PEDESTRIAN_SIZE));
		}
		else {
			bodyHeight.set(JasimConstants.DEFAULT_PEDESTRIAN_HEIGHT);
			bodyRadius.set(JasimConstants.DEFAULT_PEDESTRIAN_SIZE);
		}
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
		
		double eyePosition = getOptDouble(node, XML_ATTR_EYEPOSITION, JasimConstants.DEFAULT_PEDESTRIAN_EYE_POSITION);
		
		switch(type) {
		case RECTANGLE:
			{
				double farDistance = getMandatoryDouble(node, XML_ATTR_FARDISTANCE);				
				double lrDistance, buDistance;
				lrDistance = getMandatoryDouble(node, XML_ATTR_SIDESIZE);
				buDistance = getMandatoryDouble(node, XML_ATTR_VERTICALSIZE);
				description = new RectangularFrustumDescription(
					eyePosition, farDistance, lrDistance, buDistance);
			}
			break;
		case SPHERE:
			{
				double farDistance = getMandatoryDouble(node, XML_ATTR_FARDISTANCE);
				description = new CircularFrustumDescription(eyePosition, farDistance);
			}
			break;
		case PYRAMID:
			{
				double nearDistance = getMandatoryDouble(node, XML_ATTR_NEARDISTANCE);
				double farDistance = getMandatoryDouble(node, XML_ATTR_FARDISTANCE);
				double hAngle, vAngle;
				hAngle = getMandatoryDouble(node, XML_ATTR_HORIZONTALFOVANGLE);
				vAngle = getMandatoryDouble(node, XML_ATTR_VERTICALFOVANGLE);
				description = new TriangularFrustumDescription(
						eyePosition, nearDistance, farDistance, hAngle, vAngle);
			}
			break;
		case PEDESTRIAN:
			{
				double nearDistance = getMandatoryDouble(node, XML_ATTR_NEARDISTANCE); 
				double farDistance = getMandatoryDouble(node, XML_ATTR_FARDISTANCE);
				double hAngle = getMandatoryDouble(node, XML_ATTR_HORIZONTALFOVANGLE);
				double vAngle = getMandatoryDouble(node, XML_ATTR_VERTICALFOVANGLE);
				description = new PedestrianFrustumDescription(
						eyePosition, nearDistance, farDistance, hAngle, vAngle);
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
					
			double tangentX, tangentY, tangentZ;
			tangentX = getOptDouble(node, XML_ATTR_TANGENTX, Double.NaN);
			tangentY = getOptDouble(node, XML_ATTR_TANGENTY, Double.NaN);
			tangentZ = getOptDouble(node, XML_ATTR_TANGENTZ, Double.NaN);
			entity.addWaypoint(name, position,tangentX,tangentY,tangentZ,velocity,time);
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
	
			double tangentX = getOptDouble(node, XML_ATTR_TANGENTX, Double.NaN);
			double tangentY = getOptDouble(node, XML_ATTR_TANGENTY, Double.NaN);
			double tangentZ = getOptDouble(node, XML_ATTR_TANGENTZ, Double.NaN);
			entity.addGoal(name,position,tangentX,tangentY,tangentZ,time);
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
