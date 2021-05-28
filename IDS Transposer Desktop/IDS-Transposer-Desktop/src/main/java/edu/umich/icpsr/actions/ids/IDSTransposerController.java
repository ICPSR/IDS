/*
Copyright 2021 George Alter

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package edu.umich.icpsr.actions.ids;


import java.io.File;
import java.util.*;


import org.apache.log4j.Logger;

import edu.umich.icpsr.ids.IDSTransposer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IDSTransposerController 
{
	private static Logger log = Logger.getLogger(IDSTransposerController.class);
	private File entityMappingFile;//The entity mapping file
	private File relationMappingFile;//The relation mapping file
	private String entityMappingFileFileName;
	private String relationMappingFileFileName;
	private Map<String, File> dataFiles = new HashMap();
	private String message="";
	private File targetFolder;
	private List<String> resultFileNames = new ArrayList<String>();
	
	public String execute() {
		log.info("entering IDSTransposer controller.");
		return "UPLOAD";
    }
	
	public String upload(String targetFolderPath, boolean isCommaSeparated, String entityFilePath, String relationFilePath, List<File> dataFilesList) {
		log.info("Entering IDSTransposer upload");
		
                log.info("Input parameters are:");
                log.info("\ttargetFolderPath = " + targetFolderPath);
                log.info("\tisCommaSeparated = " + isCommaSeparated);
                log.info("\tentityFilePath = " + entityFilePath);
                log.info("\trelationFilePath = " + relationFilePath);
                for (int i = 0; i < dataFilesList.size();i++){
                    log.info("\tdataFile = " + dataFilesList.get(i).getAbsolutePath());
                }
                
		try {
			targetFolder = new File(targetFolderPath);
			entityMappingFile = new File(entityFilePath);
			entityMappingFileFileName = entityMappingFile.getName();
			relationMappingFile = new File(relationFilePath);
			relationMappingFileFileName = relationMappingFile.getName();
			for (File file : dataFilesList) {
				dataFiles.put(file.getName(), file);
			}
		} catch (Exception e) {
			log.error("An Error has occured.");
			message = "An error has occured. One or more files were not selected.\n" + e.getMessage();
			return "DONE";
		}
		
		
		if ((entityMappingFile == null) ||  (relationMappingFile == null) || (dataFiles.isEmpty())) {
			message = "Please specify an entity mapping file, relationship mapping file, and at least one data file";
			return "DONE";
		}
		
		try {
			IDSTransposer idsTransposer= new IDSTransposer(dataFiles, entityMappingFile, entityMappingFileFileName, relationMappingFile, relationMappingFileFileName, isCommaSeparated);
			resultFileNames = idsTransposer.process();
			
			log.info("Total number of files produced: " + resultFileNames.size());
		} catch (Exception e) {
			log.error("An Error has occured.");
			message = "An error has occured. IDS Transposer failed to process the data.\n" + e.getMessage();
			return "DONE";
		}
		
		try {
			List<File> resultFiles = new ArrayList<>();
			for (String fileString : resultFileNames) {
				File file = new File(fileString);
				resultFiles.add(file);
			}
			
			String targetSubfolderPath = targetFolder.getPath() + "\\Results "
					+ LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + " "
					+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).replace(":", "");
			
			File targetSubfolder = new File(targetSubfolderPath);
			boolean folderCreated = targetSubfolder.mkdir();
			
			int fileMoves = 0;
			if (folderCreated) {
				for (File file : resultFiles) {
					String newFile = targetSubfolderPath + "\\" + file.getName();
					boolean moved = file.renameTo(new File(newFile));
					if (moved) {
						fileMoves++;
					}
				}
			}
			
			if (fileMoves == resultFiles.size()) {
				message = "Success! Your results are saved in " + targetSubfolder;
			
				resetFileVariables();
			} else {
				throw new Exception("Could not move files.");
			}
		} catch (Exception e) {
			log.error("An Error has occured.");
			message = "An error has occured while packaging the resulting files.\n" + e.getMessage();
			return "DONE";
		}
		log.info("Processing completed.");
		return "DONE";
	}
	
	//This method was originally deleteTempFiles(), which actually deleted all the files used. I changed it to keep the files but delete the variables referencing them.
	private void resetFileVariables() {
		targetFolder = null;
		entityMappingFile = null;
		entityMappingFileFileName = null;
		relationMappingFile = null;
		relationMappingFileFileName = null;
		dataFiles.clear();
		resultFileNames.clear();
	}
	
	public String getMessage() {
		return message;
	}
	
	public void runInCmd(String inputFile) {
		Scanner scanner = null;
        
		try {
			scanner = new Scanner(new BufferedReader(new FileReader(inputFile)));
		} catch (FileNotFoundException e) {
			log.error("An Error has occured.");
			message = "Error: File not found.\n" + e.getMessage();
			return;
		}

		String currentLine, targetFolderString = "", entityFileString = "", relationshipFileString = "";
		boolean isCommaSeparated = true;
		List<File> dataFileList = new ArrayList<>();

		while (scanner.hasNextLine()) {
			currentLine = scanner.nextLine();
			if (currentLine.startsWith("targetFolder=")) {
				targetFolderString = currentLine.trim().replace("targetFolder=","");
			}
			if (currentLine.startsWith("delimiterType=")) {
				if (currentLine.trim().endsWith("t")) {
					isCommaSeparated = false;
				}
			}
			if (currentLine.startsWith("entityFile=")) {
				entityFileString = currentLine.trim().replace("entityFile=", "");
			}
			if (currentLine.startsWith("relationshipFile=")) {
				relationshipFileString = currentLine.trim().replace("relationshipFile=", "");
			}
			if (currentLine.startsWith("dataFile=")) {
				String path = currentLine.trim().replace("dataFile=", "");
				try {
					File file = new File(path);
					dataFileList.add(file);
				} catch (Exception e) {
					log.error("An Error has occured.");
					message = "Error retrieving data files.\n" + e.getMessage();
				}
			}
		}
		
		scanner.close();
		
		upload(targetFolderString, isCommaSeparated, entityFileString, relationshipFileString, dataFileList);
	}
}

