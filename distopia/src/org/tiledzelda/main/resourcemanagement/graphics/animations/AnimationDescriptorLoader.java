/* TiledZelda, a top-down 2d action-rpg game written in Java.
    Copyright (C) 2008  Facundo Manuel Quiroga <facundoq@gmail.com>
 	
 	This file is part of TiledZelda.

    TiledZelda is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TiledZelda is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TiledZelda.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.tiledzelda.main.resourcemanagement.graphics.animations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.tiledzelda.exceptions.GameLoadingError;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Given the directory of an animation, constructs a AnimationDescriptor object
 * 
 * @author Facundo Manuel Quiroga Feb 12, 2009
 */
public class AnimationDescriptorLoader {

	protected String animationsDirectoryPath;

	protected ArrayList<File> animationDirectories;

	/**
	 * @param animationDirectories
	 */
	public AnimationDescriptorLoader(ArrayList<File> animationDirectories, String animationsDirectoryPath) {
		this.animationDirectories = animationDirectories;
		this.animationsDirectoryPath = animationsDirectoryPath;
	}

	protected String getAnimationDirectoryPath() {
		return this.animationsDirectoryPath;
	}

	protected String getAnimationDescriptionFileName() {
		return "description.xml";
	}

	protected String getAnimationDescriptionSchemaFileName() {
		return "animation.xsd";
	}

	protected AnimationDescriptor getAnimationDescriptor() {
		/*
		 * Find the innermost animation description description file, the one nearest 
		 * to the actual animation folder that containts it's frames
		 * In this way, a bunch of animations can use the same
		* description file by merely putting it in an upper directory (but not the animations directory) 
		*/
		File animationDescriptionFile = this.getAnimationDescriptionFile(animationDirectories);
		// load the xml and generate the animation descriptor from it
		return this.generateAnimationDescriptorFromXML(animationDescriptionFile);

	}

	/**
	 * @param animationDescriptionFile
	 * @return
	 */
	private AnimationDescriptor generateAnimationDescriptorFromXML(File animationDescriptionFile) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);

		factory.setCoalescing(true);
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setExpandEntityReferences(true);
		//this.validateAnimationDescriptionFile(animationDescriptionFile);
		//factory.setValidating(true);

		try {
			this.addSchemaToFactory(factory);
			DocumentBuilder parser = factory.newDocumentBuilder();

			InputSource inputSource = new InputSource(new FileInputStream(animationDescriptionFile));
			//inputSource.setSystemId("animation.xsd");

			parser.setErrorHandler(new SAXAnimationErrorHandler());
			//parser.setEntityResolver(new AnimationEntityResolver(this.getAnimationDescriptionSchema()));
			//Document document = parser.parse(animationDescriptionFile);
			Document document = parser.parse(inputSource);
			//document.normalize(); // TODO remove this too if useless
			//document.normalizeDocument();
			return this.generateAnimationDescriptor(document);
			//return new AnimationDescriptor(animationFrameDescriptors,loops);
		} catch (ParserConfigurationException e) {
			throw new GameLoadingError(e);
		} catch (SAXException e) {
			throw new GameLoadingError(e);
		} catch (IOException e) {
			throw new GameLoadingError(e);
		}
	}

	/**
	 * @param factory
	 * @throws SAXException
	 * @throws FileNotFoundException
	 */
	private void addSchemaToFactory(DocumentBuilderFactory factory) throws SAXException, FileNotFoundException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		File animationDescriptionSchema = this.getAnimationDescriptionSchema();

		//XMLCleanerFilterInputStream xmCleanerFilterInputStream = new XMLCleanerFilterInputStream(new FileInputStream(animationDescriptionSchema));

		Source schemaSource = new StreamSource(animationDescriptionSchema);
		Schema schema = schemaFactory.newSchema(schemaSource);
		factory.setSchema(schema);
	}

	/**
	 * @param document
	 * @return
	 */
	protected AnimationDescriptor generateAnimationDescriptor(Document document) {
		NodeList loopsList = document.getElementsByTagName("loops");
		String loopsValue = loopsList.item(0).getTextContent().trim();
		if (!(loopsValue.equals("true") || loopsValue.equals("false"))) {
			throw new GameLoadingError("Invalid loop attribute value: " + loopsValue);
		}
		boolean loops = loopsValue.equals("true") ? true : false;

		ArrayList<AnimationFrameDescriptor> animationFrameDescriptors = new ArrayList<AnimationFrameDescriptor>();
		NodeList frameList = document.getElementsByTagName("frame");
		for (int i = 0; i < frameList.getLength(); i++) {
			Node frame = frameList.item(i);
			String imageName = null;
			String percentage = null;
			for (int j = 0; j < frame.getChildNodes().getLength(); j++) {
				// TODO PROPER VALIDATION -> XERCES IS BUGGY
				Node node = frame.getChildNodes().item(j);
				String nodeName = node.getLocalName();
				if (nodeName != null) {

					if (nodeName.equals("imageName")) {
						imageName = node.getTextContent();
					} else if (nodeName.equals("percentage")) {
						percentage = node.getTextContent();
					} else {
						throw new GameLoadingError("Unrecognized element " + nodeName);
					}
				}
				//System.out.print("Child "+j+": ");
				//System.out.println(frame.getChildNodes().item(j).getTextContent());
			}
			AnimationFrameDescriptor animationFrameDescriptor = new AnimationFrameDescriptor(imageName, new Integer(percentage));
			animationFrameDescriptors.add(animationFrameDescriptor);
		}
		return new AnimationDescriptor(animationFrameDescriptors, loops);
	}

	protected void validateAnimationDescriptionFile(File animationDescriptionFile) {
		try {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Source schemaSource = new StreamSource(this.getAnimationDescriptionSchema());
			Schema schema = schemaFactory.newSchema(schemaSource);
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(animationDescriptionFile));
		} catch (SAXException e) {
			throw new GameLoadingError(e);
		} catch (IOException e) {
			throw new GameLoadingError(e);
		}
	}

	protected File getAnimationDescriptionSchema() {
		String name = this.getAnimationDescriptionSchemaFileName();
		String path = this.getAnimationDirectoryPath();
		String fullPath = path + "/" + name;
		File file = new File(fullPath);
		if (!file.exists() || file.isDirectory()) {
			throw new GameLoadingError("Invalid animation description schema path or name, used: " + fullPath);
		}
		return file;
	}

	/**
	 * @param animationDirectories : list of directories that must be traveled to reach the animations directory
	 * @return the first description file found in the innermost directory.
	 */
	protected File getAnimationDescriptionFile(ArrayList<File> animationDirectories) {
		String animationDescriptionFileName = this.getAnimationDescriptionFileName();
		int numberOfDirectories = animationDirectories.size();
		for (int i = numberOfDirectories - 1; i >= 0; i--) {
			File animationDirectory = animationDirectories.get(i);
			String path = animationDirectory.getPath() + "/" + animationDescriptionFileName;
			File tentativeAnimationDescriptionFile = new File(path);
			//System.out.println(tentativeAnimationDescriptionFile);
			if (tentativeAnimationDescriptionFile.exists() && !tentativeAnimationDescriptionFile.isDirectory()) {
				return tentativeAnimationDescriptionFile;
			}
		}
		throw new GameLoadingError("No animation description file in directories: " + animationDirectories);

	}

}
