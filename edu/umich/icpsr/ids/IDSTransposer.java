package edu.umich.icpsr.ids;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.supercsv.io.CsvListWriter;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

public class IDSTransposer {
	
	private List<Map<String,String>> dataList = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> entityMappingList = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> relationMappingList = new ArrayList<Map<String,String>>();
	
	private String dataFileName;
	private Map<String, File> dataFiles = new HashMap<String, File>();
	private String entityMappingFileName;
	private String relationMappingFileName;
	private String entityMappingFileRealFileName;
	private String relationMappingFileRealFileName;
	private String individualFileName;
	private String contextFileName;
	private String individualIndividualFileName;
	private String individualContextFileName;
	private String contextContextFileName;
	private String dataFileTableName;
	private String fileFormat;
	
	private ICsvListWriter individualListWriter = null;
	private ICsvListWriter contextListWriter = null;
	private ICsvListWriter individualIndividualListWriter = null;
	private ICsvListWriter individualContextListWriter = null;
	private ICsvListWriter contextContextListWriter = null;
	
	private int lastIndividualId = 0;
	private int lastContextId = 0;
	private int lastIndividualIndividualId = 0;
	private int lastIndividualContextId = 0;
	private int lastContextContextId = 0;
	
	//private Map<String, Map<String, List<String>>> idMap = new HashMap <String, Map<String,List<String>>>();
	private Map<String, Map<String, List<String>>> idMap = new TreeMap<String, Map<String,List<String>>>(String.CASE_INSENSITIVE_ORDER);
	
	private final String TABLE_NAME = "tableName";
	private final String ENTITY_TYPE = "entityType";
	private final String DATABASE_ID = "databaseId";
	private final String ENTITY_ID = "entityId";
	private final String SOURCE = "source";
	private final String VARIABLE_NAME = "variableName";
	private final String VALUE = "value";
	private final String TYPE = "type";
	private final String VALUE_ID_C = "Value_Id_C";
	private final String DATE_TYPE = "dateType";
	private final String DATE_ESTIMATION = "dateEstimationType";
	private final String DATE_MISSING_TYPE = "dateMissingType";
	private final String YEAR = "year";
	private final String MONTH = "month";
	private final String DAY = "day";
	private final String START_YEAR = "startYear";
	private final String START_MONTH = "startMonth";
	private final String START_DAY = "startDay";
	private final String END_YEAR = "endYear";
	private final String END_MONTH = "endMonth";
	private final String END_DAY = "endDay";
	private final String RELATIONSHIP_TYPE = "relationshipType";
	private final String FROM_ENTITY_ID = "fromEntityId";
	private final String TO_ENTITY_ID = "toEntityId";
	private final String RELATION = "relation";
	private final String RELATION_VARIABLE_NAME = "relationVariable";
	private final String[] individualHeader = {"Id","Id_D", "Id_I", "Source", "Type", "Value", "Value_Id_C", "Date_Type", "Estimation", "Missing", "Year", "Month", "Day", "Start_Year", "Start_Month", "Start_Day", "End_Year", "End_Month", "End_Day"};
	private final String[] contextHeader = {"Id","Id_D", "Id_C", "Source", "Type", "Value", "Date_Type", "Estimation", "Missing", "Year", "Month", "Day", "Start_Year", "Start_Month", "Start_Day", "End_Year", "End_Month", "End_Day"};
	private final String[] individualIndividualHeader = {"Id", "Id_D", "Id_I_1", "Id_I_2", "Source", "Relation", "Date_Type", "Estimation", "Missing", "Year", "Month", "Day", "Start_Year", "Start_Month", "Start_Day", "End_Year", "End_Month", "End_Day"};
	private final String[] individualContextHeader = {"Id", "Id_D", "Id_I", "Id_C", "Source", "Relation", "Date_Type", "Estimation", "Missing", "Year", "Month", "Day", "Start_Year", "Start_Month", "Start_Day", "End_Year", "End_Month", "End_Day"};
	private final String[] contextContextHeader = {"Id", "Id_D", "Id_C_1", "Id_C_2", "Source", "Relation", "Date_Type", "Estimation", "Missing", "Year", "Month", "Day", "Start_Year", "Start_Month", "Start_Day", "End_Year", "End_Month", "End_Day"};
	
	private final Set<String> entityMappingHeader = new HashSet<String>(Arrays.asList("tableName", "entityType", "databaseId", "entityId", "source", "variableName", "value", "type", "Value_Id_C", "dateType", "dateEstimationType", "dateMissingType", "year", "month", "day", "startYear", "startMonth", "startDay", "endYear", "endMonth", "endDay"));
	private final Set<String> relationMappingHeader = new HashSet<String>(Arrays.asList("tableName", "relationshipType", "databaseId", "fromEntityId", "toEntityId", "source", "relation", "relationVariable", "dateType", "dateEstimationType", "dateMissingType", "year", "month", "day", "startYear", "startMonth", "startDay", "endYear", "endMonth", "endDay"));
	
	public  IDSTransposer(Map<String, File> dataFiles, File entityMappingFile, String entityMappingFileRealFileName, File relationMappingFile, String relationMappingFileRealFileName, String fileFormat) throws IDSException {
		
		this.dataFiles = dataFiles;
		this.entityMappingFileName = entityMappingFile.getAbsolutePath();
		this.relationMappingFileName = relationMappingFile.getAbsolutePath();
		this.entityMappingFileRealFileName = entityMappingFileRealFileName;
		this.relationMappingFileRealFileName = relationMappingFileRealFileName;
		this.fileFormat = fileFormat;
			
		String fileExtension = ".txt";
		if (isExcelFormat(fileFormat)){
			fileExtension = ".csv";			
		}
		try {
			this.individualFileName = File.createTempFile("individual-", fileExtension).getAbsolutePath();
			this.contextFileName = File.createTempFile("context-", fileExtension).getAbsolutePath();
			this.individualIndividualFileName = File.createTempFile("individual-individual-", fileExtension).getAbsolutePath();
			this.individualContextFileName = File.createTempFile("individual-Context-", fileExtension).getAbsolutePath();
			this.contextContextFileName = File.createTempFile("context-context-", fileExtension).getAbsolutePath();		
		}
		catch (Exception e) {
			System.out.println("Error when creating temp files" + e.getMessage());
			throw (new IDSException("Failed to create result files. \n " + e.getMessage()));
		}
		
				
	}
	
	public void readData() throws IDSException {
		
		try {
			dataList = readFile(dataFileName, true);
		}
		catch (IDSException e) {
			throw (new IDSException("Failed to read data file. \n " + e.getMessage()));
		}
	}
	
	public void readEntityMapping() throws IDSException {
		
		try {
			entityMappingList = readFile(entityMappingFileName, false);
			
			if (entityMappingList.size() == 0)
				throw (new IDSException("Entity mapping file is empty: " + entityMappingFileRealFileName + "."));
			else {
				Map<String, String> entry = entityMappingList.get(0);
				Set<String> keys = entry.keySet();
				for (String headerItem: entityMappingHeader) {
					if (!keys.contains(headerItem))
						throw (new IDSException("Missing column in entity mapping file: " + headerItem));
				}
			}
		}
		catch (IDSException e) {
			throw (new IDSException("Failed to read entity mapping file: " + entityMappingFileRealFileName + ".  " + e.getMessage()));
		}
	}
	
	public void readRelationshipMapping() throws Exception {
		
		try {
			relationMappingList = readFile(relationMappingFileName, false);			
			if (relationMappingList.size() == 0)
				throw (new IDSException("Relation mapping file is empty: " + relationMappingFileRealFileName + "."));
			else {
				Map<String, String> entry = relationMappingList.get(0);
				Set<String> keys = entry.keySet();
				for (String headerItem: relationMappingHeader) {
					if (!keys.contains(headerItem))
						throw (new IDSException("Missing column in relation mapping file: " + headerItem));
				}
			}
		}
		catch (IDSException e) {
			throw (new IDSException("Failed to read relation mapping file: " + relationMappingFileRealFileName + ".  " + e.getMessage()));
		}
	}
	
	private ICsvMapReader getReader(String fileName) throws FileNotFoundException, Exception {
		
		ICsvMapReader mapReader = null;		
		mapReader = new CsvMapReader(new FileReader(fileName), CSVPreference(fileFormat));
		
		return mapReader;
    }
	
	private ICsvMapReader getReader(File file) throws FileNotFoundException, Exception {
		
		ICsvMapReader mapReader = null;		
        mapReader = new CsvMapReader(new FileReader(file),  CSVPreference(fileFormat));
		
		return mapReader;
    }
	
	private ICsvListWriter getWriter(String fileName) throws IOException {
		
		ICsvListWriter listWriter = null;
		listWriter = new CsvListWriter(new FileWriter(fileName),  CSVPreference(fileFormat));
		
		return listWriter;
	}
	
	private List<Map<String,String>> readFile(String fileName, boolean validateHeader) throws IDSException {
		
		List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();		
		try {
			ICsvMapReader csvReader = getReader(fileName);
			String[] header = csvReader.getHeader(true);
			Map<String, String> csvMap;
			
			while ((csvMap = csvReader.read(header)) != null)
			{
				Map<String, String> tMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
				tMap.putAll(csvMap);
				mapList.add(tMap);
			}
			
			csvReader.close();
			if (validateHeader)
				validateHeader(header);
		}
		catch (IDSException e3) {
			throw e3;
		}
		catch (FileNotFoundException e) {
			throw (new IDSException("File not found - " + fileName + "\n  " + e.getMessage()));
		} 
		catch (IOException e1) {
			throw (new IDSException(e1.getMessage()));
		}
		catch (Exception e2) {
			throw (new IDSException("The file does not appear to be a valid csv file.  Please ensure that the first row contains the column names, that each column name is unique, and that fields are quoted correctly. " + e2.getMessage()));
		}
		
		return mapList;
	}
	
	
	private List<Map<String,String>> readFile(File file, boolean validateHeader) throws IDSException {
		
		List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
		
		try {			
			ICsvMapReader csvReader = getReader(file);
			String[] header = csvReader.getHeader(true);
			Map<String, String> csvMap;
			while ((csvMap = csvReader.read(header)) != null)
			{
				Map<String, String> tMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
				tMap.putAll(csvMap);
				mapList.add(tMap);
			}
			csvReader.close();
			if (validateHeader)
				validateHeader(header);
		}
		catch (IDSException e3) {
			throw e3;
		}
		catch (FileNotFoundException e) {
			throw (new IDSException("File not found - " + dataFileTableName + "\n  " + e.getMessage()));
		} 
		catch (IOException e1) {
			throw (new IDSException(e1.getMessage()));
		}		
		catch (Exception e2) {
			throw (new IDSException("The file does not appear to be a valid csv file.  Please ensure that the first row contains the column names, that each column name is unique, and that fields are quoted correctly. " + e2.getMessage()));
		}
		
		return mapList;
	}
	
	private void validateHeader(String[] header) throws IDSException {
		
		boolean valid = true;
		String missingHeader = "";
		List<String> headerList = Arrays.asList(header);
		for (Map<String,String> entityMapping:entityMappingList) {
			String tableName = getValue(entityMapping, TABLE_NAME);
			if (tableName.equalsIgnoreCase(dataFileTableName)) {
				String varName = getValue(entityMapping, VARIABLE_NAME);
				if (varName != null && !varName.trim().equals("")) {
					// check if the var name is in the header
					if (!headerList.contains(varName)) {
						valid = false;
						missingHeader = missingHeader + varName + " ";
					}
				}
			}
		}
		
		for (Map<String,String> relationMapping:relationMappingList) {
			String tableName = getValue(relationMapping,TABLE_NAME);
			if (tableName.equalsIgnoreCase(dataFileTableName)) {
				String fromID = getValue(relationMapping, FROM_ENTITY_ID);
				String toID = getValue(relationMapping, TO_ENTITY_ID);
				if (fromID != null && !fromID.trim().equals("") && toID != null && !toID.trim().equals("")) {
					// check if the IDs are in the header
					if (!headerList.contains(fromID)) {
						valid = false;
						missingHeader = missingHeader + fromID + " ";
					}
					if (!headerList.contains(toID)) {
						valid = false;
						missingHeader = missingHeader + toID + " ";
					}
				}
			}
		}
		
		
		if (!valid)
			throw (new IDSException("These headers are missing from the data file '" + dataFileTableName + "' - " + missingHeader +missingHeader + ".  Please check the data file header columns again."));
		
		return;
	}
	
	
	public List<String> process() throws Exception {
		
		System.out.println("Entering process");
		// initialize output files
		if (individualListWriter == null)
			 individualListWriter = getWriter(individualFileName);
		
		if (contextListWriter == null)
			 contextListWriter = getWriter(contextFileName);
		
		if (individualIndividualListWriter == null)
			individualIndividualListWriter = getWriter(individualIndividualFileName);
		
		if (individualContextListWriter == null)
			individualContextListWriter = getWriter(individualContextFileName);
		
		if (contextContextListWriter == null)
			contextContextListWriter = getWriter(contextContextFileName);
		
		individualListWriter.writeHeader(individualHeader);
		contextListWriter.writeHeader(contextHeader);
		individualIndividualListWriter.writeHeader(individualIndividualHeader);
		individualContextListWriter.writeHeader(individualContextHeader);
		contextContextListWriter.writeHeader(contextContextHeader);
		
		// read mapping files
		readEntityMapping();
		readRelationshipMapping();

		// iterate through file list
		Set<String> dataFileNames = dataFiles.keySet();	
		for (String dataFileName:dataFileNames) {
			System.out.println("Now processing " + dataFileName);
			File dataFile = (File) dataFiles.get(dataFileName);
			dataFileTableName = dataFileName.substring(0, dataFileName.lastIndexOf("."));	
			try {
				dataList = readFile(dataFile, true);
			}
			catch (IDSException e) {
				throw (new IDSException("Error when reading data File:  " + dataFileName + ".  " + e.getMessage()));
			}					
			
			// process file
			processEntities();
			processRelations();
		}		
						
		try {
			if (individualListWriter != null)
				individualListWriter.close();
			if (contextListWriter != null)
				contextListWriter.close();
			if (individualIndividualListWriter != null)
				individualIndividualListWriter.close();
			if (individualContextListWriter != null)
				individualContextListWriter.close();
			if (contextContextListWriter != null)
				contextContextListWriter.close();
		}
		catch(IOException e) {
			System.out.println("Error when closing writers - " + e.getMessage());
			throw (new IDSException("Error when closing writers - " + e.getMessage()));
		}
		
		List<String> resultFileNames = new ArrayList<String>();
		resultFileNames.add(individualFileName);
		resultFileNames.add(contextFileName);
		resultFileNames.add(individualIndividualFileName);
		resultFileNames.add(individualContextFileName);
		resultFileNames.add(contextContextFileName);
		
		return resultFileNames;		
	}
	
	private void processEntities()  throws Exception {
		
		for(Map<String,String> entityMapping:entityMappingList) {
			String tableName = getValue(entityMapping, TABLE_NAME);
			String entityType = getValue(entityMapping, ENTITY_TYPE);
			
			if (tableName.equals("") || entityType.equals(""))
				throw (new IDSException("Error found in entity mapping file.  TableName and EntityType should not be blank"));			
			
			// determine if the entry applies to this data file
			
			if (tableName.equalsIgnoreCase(dataFileTableName)) {
			
				if (entityType.equalsIgnoreCase("individual"))
					writeIndividual(entityMapping);
				else if (entityType.equalsIgnoreCase("context"))
					writeContext(entityMapping);
			}
		}		
	}
	
	
	private void processRelations() throws Exception {
				
		for(Map<String,String> relationMapping:relationMappingList) {
			String tableName = getValue(relationMapping, TABLE_NAME);
			
			if (tableName.equals(""))
				throw (new IDSException("Error found in relation mapping file.  TableName should not be blank"));
			
			// determine if the entry applies to this data file
						
			if (tableName.equalsIgnoreCase(dataFileTableName)) {
				String relationshipType = getValue(relationMapping, RELATIONSHIP_TYPE);
				
				if (relationshipType.equals(""))
					throw (new IDSException("Error found in relation mapping file.  relationshipType should not be blank"));
			
				if (relationshipType.trim().equalsIgnoreCase("indiv_indiv"))
					writeIndividualIndividual(relationMapping);
				else if (relationshipType.trim().equalsIgnoreCase("context_context"))
					writeContextContext(relationMapping);
				else if (relationshipType.trim().equalsIgnoreCase("indiv_context"))
					writeIndividualContext(relationMapping);
			}
		}
	}
	
	
	private void writeIndividual(Map<String, String> entityMapping)  throws IDSException {
		
		String databaseId = getValue(entityMapping, DATABASE_ID);
		String entityIdName = getValue(entityMapping, ENTITY_ID);
		String sourceName = getValue(entityMapping, SOURCE);
		String variableName = getValue(entityMapping, VARIABLE_NAME);
		String value = getValue(entityMapping, VALUE);
		String type = getValue(entityMapping, TYPE);
		String valueIdC = getValue(entityMapping, VALUE_ID_C);
		String dateType = getValue(entityMapping, DATE_TYPE);
		String dateEstimation = getValue(entityMapping, DATE_ESTIMATION);
		String yearName = getValue(entityMapping, YEAR);
		String monthName = getValue(entityMapping, MONTH);
		String dayName = getValue(entityMapping, DAY);
		String startYearName = getValue(entityMapping, START_YEAR);
		String startMonthName = getValue(entityMapping, START_MONTH);
		String startDayName = getValue(entityMapping, START_DAY);
		String endYearName = getValue(entityMapping, END_YEAR);
		String endMonthName = getValue(entityMapping, END_MONTH);
		String endDayName = getValue(entityMapping, END_DAY);	
		
		for (Map<String, String> data:dataList) {	
			
			// check if the individual exists in individual file already
			Map<String, List<String>> individualIdList = idMap.get(entityIdName);
			if (individualIdList == null) {

				individualIdList = new HashMap<String,List<String>>();
				idMap.put(entityIdName, individualIdList);
			}
			boolean found = false;
			String entityId = getValue(data, entityIdName);
			
			if (!isBlank(entityId)) {
				if (individualIdList.containsKey(entityId)) {
					List<String> typeList = (List<String>) individualIdList.get(entityId);
					for (String typeObject:typeList) {
						if (typeObject.trim().equals(type))
							found = true;
					}
				}
				
				if (found == false) {
					List<String> typeList;
					if (!individualIdList.containsKey(entityId))
						typeList = new ArrayList<String>();
					else
						typeList = (List<String>) individualIdList.get(entityId);
					typeList.add(type);
					individualIdList.put(entityId, typeList);					
	
					lastIndividualId ++;
	
					String source = getValue(data, sourceName);
					if (!isBlank(variableName)) {
						value = getValue(data, variableName);
					}
					String valueIdCValue = getValue(data, valueIdC);
					String dateEstimationValue = "";
					String dateMissingType = getValue(entityMapping, DATE_MISSING_TYPE);
					String year = getValue(data, yearName);
					String month = getValue(data, monthName);
					String day = getValue(data, dayName);
					String startYear = getValue(data, startYearName);
					String startMonth = getValue(data, startMonthName);
					String startDay = getValue(data, startDayName);
					String endYear = getValue(data, endYearName);
					String endMonth = getValue(data, endMonthName);
					String endDay = getValue(data, endDayName);
	
					dateEstimationValue = getDateEstimationType(data, dateEstimation, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay);
					dateMissingType = getDateMissingType(dateMissingType, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay);
										
					if ((!isBlank(value)) || ((!isBlank(year) || !isBlank(startYear) || !isBlank(endYear)) && (isBlank(variableName)))) {
							
						String[] individualArray = {Integer.toString(lastIndividualId), databaseId, entityId, source, type, value, valueIdCValue, dateType, dateEstimationValue, dateMissingType, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay };
						try {
							individualListWriter.write(Arrays.asList(individualArray));
						}
						catch (IOException e) {
							throw (new IDSException("Error when writing the line - " + individualArray.toString() + e.getMessage()));
						}
					}
				}
			}
		}		
	}
	
	private void writeContext(Map<String, String> entityMapping) throws IDSException {
		
		String databaseId = getValue(entityMapping, DATABASE_ID);
		String entityIdName = getValue(entityMapping, ENTITY_ID);
		String sourceName = getValue(entityMapping, SOURCE);
		String variableName = getValue(entityMapping, VARIABLE_NAME);
		String value = getValue(entityMapping, VALUE);
		String type = getValue(entityMapping, TYPE);
		String dateType = getValue(entityMapping, DATE_TYPE);
		String dateEstimation = getValue(entityMapping, DATE_ESTIMATION);
		String yearName = getValue(entityMapping, YEAR);
		String monthName = getValue(entityMapping, MONTH);
		String dayName = getValue(entityMapping, DAY);
		String startYearName = getValue(entityMapping, START_YEAR);
		String startMonthName = getValue(entityMapping, START_MONTH);
		String startDayName = getValue(entityMapping, START_DAY);
		String endYearName = getValue(entityMapping, END_YEAR);
		String endMonthName = getValue(entityMapping, END_MONTH);
		String endDayName = getValue(entityMapping, END_DAY);	
		

		
		for (Map<String, String> data:dataList) {
			lastContextId ++;
			String entityId = getValue(data, entityIdName);
			if ((entityId != null) && !(entityId.trim().equals(""))) {
				String source = getValue(data, sourceName);
				if (!isBlank(variableName))
					value = getValue(data, variableName);
				
				String dateEstimationValue = "";
				String dateMissingType = getValue(entityMapping, DATE_MISSING_TYPE);
				String year = getValue(data, yearName);
				String month = getValue(data, monthName);
				String day = getValue(data, dayName);
				String startYear = getValue(data, startYearName);
				String startMonth = getValue(data, startMonthName);
				String startDay = getValue(data, startDayName);
				String endYear = getValue(data, endYearName);
				String endMonth = getValue(data, endMonthName);
				String endDay = getValue(data, endDayName);
				
				dateEstimationValue = getDateEstimationType(data, dateEstimation, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay);
				dateMissingType = getDateMissingType(dateMissingType, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay);
				
				if ((!isBlank(value)) || ((!isBlank(year) || !isBlank(startYear) || !isBlank(endYear)) && (isBlank(variableName)))) {				
					String[] contextArray = {Integer.toString(lastContextId), databaseId, entityId, source, type, value, dateType, dateEstimationValue, dateMissingType, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay };
					try {
						contextListWriter.write(Arrays.asList(contextArray));
					}
					catch (IOException e) {
						System.out.println("Error when writing the line - " + contextArray.toString());
						throw (new IDSException("Error when writing the line - " + contextArray.toString() + e.getMessage()));
					}
				}
			}
		}				
	}
	
	private void writeIndividualIndividual(Map<String, String> relationMapping) throws IDSException {
		
		String relation = getValue(relationMapping, RELATION);
		String databaseId = getValue(relationMapping, DATABASE_ID);
		String fromEntityIdName = getValue(relationMapping, FROM_ENTITY_ID);
		String toEntityIdName = getValue(relationMapping, TO_ENTITY_ID);
		String sourceName = getValue(relationMapping, SOURCE);
		String dateType = getValue(relationMapping, DATE_TYPE);
		String dateEstimation = getValue(relationMapping, DATE_ESTIMATION);
		String yearName = getValue(relationMapping, YEAR);
		String monthName = getValue(relationMapping, MONTH);
		String dayName = getValue(relationMapping, DAY);
		String startYearName = getValue(relationMapping, START_YEAR);
		String startMonthName = getValue(relationMapping, START_MONTH);
		String startDayName = getValue(relationMapping, START_DAY);
		String endYearName = getValue(relationMapping, END_YEAR);
		String endMonthName = getValue(relationMapping, END_MONTH);
		String endDayName = getValue(relationMapping, END_DAY);	
		String relationVariableName = getValue(relationMapping, RELATION_VARIABLE_NAME)
;
		

		
		for (Map<String, String> data:dataList) {
			
			lastIndividualIndividualId ++;

			String fromEntityId = getValue(data, fromEntityIdName);
			String toEntityId = getValue(data, toEntityIdName);
			String source = getValue(data, sourceName);
			String relationValue = relation;
			
			String dateEstimationValue = "";
			String dateMissingType = getValue(relationMapping, DATE_MISSING_TYPE);
			String year = getValue(data, yearName);
			String month = getValue(data, monthName);
			String day = getValue(data, dayName);
			String startYear = getValue(data, startYearName);
			String startMonth = getValue(data, startMonthName);
			String startDay = getValue(data, startDayName);
			String endYear = getValue(data, endYearName);
			String endMonth = getValue(data, endMonthName);
			String endDay = getValue(data, endDayName);

			dateEstimationValue = getDateEstimationType(data, dateEstimation, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay);
			dateMissingType = getDateMissingType(dateMissingType, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay);
			
			if (isBlank(relation) && (!isBlank(relationVariableName)))
				relationValue = getValue(data, relationVariableName);
			
			if (!isBlank(fromEntityId) && !isBlank(toEntityId)) {			
				String[] individualIndividualArray = {Integer.toString(lastIndividualIndividualId), databaseId, fromEntityId, toEntityId, source, relationValue, dateType, dateEstimationValue, dateMissingType, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay };
				try {
					individualIndividualListWriter.write(Arrays.asList(individualIndividualArray));
				}
				catch (IOException e) {
					System.out.println("Error when writing the line - " + individualIndividualArray.toString());
					throw (new IDSException("Error when writing the line - " + individualIndividualArray.toString() + e.getMessage()));
				}
			}
		}		
	}
	
	private void writeIndividualContext(Map<String, String> relationMapping) throws IDSException {
		
		String relation = getValue(relationMapping, RELATION);
		String databaseId = getValue(relationMapping, DATABASE_ID);
		String fromEntityIdName = getValue(relationMapping, FROM_ENTITY_ID);
		String toEntityIdName = getValue(relationMapping, TO_ENTITY_ID);
		String sourceName = getValue(relationMapping, SOURCE);
		String dateType = getValue(relationMapping, DATE_TYPE);
		String dateEstimation = getValue(relationMapping, DATE_ESTIMATION);
		String yearName = getValue(relationMapping, YEAR);
		String monthName = getValue(relationMapping, MONTH);
		String dayName = getValue(relationMapping, DAY);
		String startYearName = getValue(relationMapping, START_YEAR);
		String startMonthName = getValue(relationMapping, START_MONTH);
		String startDayName = getValue(relationMapping, START_DAY);
		String endYearName = getValue(relationMapping, END_YEAR);
		String endMonthName = getValue(relationMapping, END_MONTH);
		String endDayName = getValue(relationMapping, END_DAY);
		String relationVariableName = getValue(relationMapping, RELATION_VARIABLE_NAME);
		
		for (Map<String, String> data:dataList) {
			lastIndividualContextId ++;
			String fromEntityId = getValue(data, fromEntityIdName);
			String toEntityId = getValue(data, toEntityIdName);
			String source = getValue(data, sourceName);
			String relationValue = relation;
			
			String dateEstimationValue = "";
			String dateMissingType = getValue(relationMapping, DATE_MISSING_TYPE);
			String year = getValue(data, yearName);
			String month = getValue(data, monthName);
			String day = getValue(data, dayName);
			String startYear = getValue(data, startYearName);
			String startMonth = getValue(data, startMonthName);
			String startDay = getValue(data, startDayName);
			String endYear = getValue(data, endYearName);
			String endMonth = getValue(data, endMonthName);
			String endDay = getValue(data, endDayName);
			
			dateEstimationValue = getDateEstimationType(data, dateEstimation, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay);
			dateMissingType = getDateMissingType(dateMissingType, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay);
			
			if (isBlank(relation) && (!isBlank(relationVariableName)))
				relationValue = getValue(data, relationVariableName);
			
			if (!isBlank(fromEntityId) && !isBlank(toEntityId)) {			
				String[] individualContextArray = {Integer.toString(lastIndividualContextId), databaseId, fromEntityId, toEntityId, source, relationValue, dateType, dateEstimationValue, dateMissingType, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay };
				try {
					individualContextListWriter.write(Arrays.asList(individualContextArray));
				}
				catch (IOException e) {
					System.out.println("Error when writing the line - " + individualContextArray.toString());
					throw (new IDSException("Error when writing the line - " + individualContextArray.toString() + e.getMessage()));
				}
			}
		}		
	}
	
	private void writeContextContext(Map<String, String> relationMapping) throws IDSException {
		
		String relation = getValue(relationMapping, RELATION);
		String databaseId = getValue(relationMapping, DATABASE_ID);
		String fromEntityIdName = getValue(relationMapping, FROM_ENTITY_ID);
		String toEntityIdName = getValue(relationMapping, TO_ENTITY_ID);
		String sourceName = getValue(relationMapping, SOURCE);
		String dateType = getValue(relationMapping, DATE_TYPE);
		String dateEstimation = getValue(relationMapping, DATE_ESTIMATION);
		String yearName = getValue(relationMapping, YEAR);
		String monthName = getValue(relationMapping, MONTH);
		String dayName = getValue(relationMapping, DAY);
		String startYearName = getValue(relationMapping, START_YEAR);
		String startMonthName = getValue(relationMapping, START_MONTH);
		String startDayName = getValue(relationMapping, START_DAY);
		String endYearName = getValue(relationMapping, END_YEAR);
		String endMonthName = getValue(relationMapping, END_MONTH);
		String endDayName = getValue(relationMapping, END_DAY);
		String relationVariableName = getValue(relationMapping, RELATION_VARIABLE_NAME);
				
		for (Map<String, String> data:dataList) {
			lastContextContextId ++;
			String fromEntityId = getValue(data, fromEntityIdName);
			String toEntityId = getValue(data, toEntityIdName);
			String source = getValue(data, sourceName);
			
			String dateEstimationValue = "";
			String dateMissingType = getValue(relationMapping, DATE_MISSING_TYPE);
			String year = getValue(data, yearName);
			String month = getValue(data, monthName);
			String day = getValue(data, dayName);
			String startYear = getValue(data, startYearName);
			String startMonth = getValue(data, startMonthName);
			String startDay = getValue(data, startDayName);
			String endYear = getValue(data, endYearName);
			String endMonth = getValue(data, endMonthName);
			String endDay = getValue(data, endDayName);

			dateEstimationValue = getDateEstimationType(data, dateEstimation, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay);
			dateMissingType = getDateMissingType(dateMissingType, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay);
			
			 
			if  (!isBlank(relationVariableName)) { 
				relation = getValue(data, relationVariableName);
			}
 
			if (!isBlank(fromEntityId) && !isBlank(toEntityId)) {
				String[] contextContextArray = {Integer.toString(lastContextContextId), databaseId, fromEntityId, toEntityId, source, relation, dateType, dateEstimationValue, dateMissingType, year, month, day, startYear, startMonth, startDay, endYear, endMonth, endDay };
				try {
					contextContextListWriter.write(Arrays.asList(contextContextArray));
				}
				catch (IOException e) {
					System.out.println("Error when writing the line - " + contextContextArray.toString());
					throw (new IDSException("Error when writing the line - " + contextContextArray.toString() + e.getMessage()));
				}
			}
		}		
	}
	
	private String getDateEstimationType(Map<String, String> data, String dateEstimationVariable, String year, String month, String day, String startYear, String startMonth, String startDay, String endYear, String endMonth, String endDay) throws IDSException {
		
		String dateEstimationType = "";
		
		if (!isBlank(dateEstimationVariable)) {
			dateEstimationType = getValue(data, dateEstimationVariable);
		}
		
		if (isBlank(dateEstimationType)) {
			if (!isBlank(year))
				dateEstimationType = getDateEstimationTypeByYear(year, month, day);
			else if (!isBlank(startYear))
				dateEstimationType = getDateEstimationTypeByYear(startYear, startMonth, startDay);
			else if (!isBlank(endYear))
				dateEstimationType = getDateEstimationTypeByYear(endYear, endMonth, endDay);
		}
		return dateEstimationType;
			
	}
	
	private String getDateEstimationTypeByYear(String year, String month, String day) {
		
		String returnValue = "";
		if (!isBlank(year) && !isBlank(month) && !isBlank(day))
			returnValue = "Exact";
		else if (!isBlank(year) && !isBlank(month) && isBlank(day))
			returnValue = "Month_Year";
		else if (!isBlank(year) && isBlank(month) && isBlank(day))
			returnValue = "Year";

		return returnValue;
	}
	
	private String getDateMissingType(String dateMissingType, String year, String month, String day, String startYear, String startMonth, String startDay, String endYear, String endMonth, String endDay) {
			
		String returnValue = dateMissingType; 
		if (isBlank(returnValue))
			if (isBlank(year) && isBlank(month) && isBlank(day) && isBlank(startYear) && isBlank(startMonth) && isBlank(startDay) && isBlank(endYear) && isBlank(endMonth) && isBlank(endDay))
				returnValue = "Unavailable";
		
		return returnValue;
	}
	
	private boolean isBlank(String theString) {
		
		if (theString == null)
			return true;
		else if (theString.trim().equals(""))
			return true;
		else return false;
	}
	
	private String getValue (Map<String, String> map, String key) throws IDSException {
		
		if ((key != null) && (!key.trim().equals("")) && (!map.containsKey(key)))
			throw (new IDSException("The field does not exist: " + key + ". "));
		
		String value = (String) map.get(key);
		if (value != null)
			return value.trim();
		else
			return "";
	}
	
	private static boolean isExcelFormat(String format){
		if (format.equals("c")){
			return true;
		}
		return false;
		
	}
	
	private static boolean isCSVFormat(String format){
		return !isExcelFormat(format);
		
	}
	
	private static CsvPreference  CSVPreference(String fileFormat){
		if (isExcelFormat(fileFormat)){
			return CsvPreference.STANDARD_PREFERENCE;
		}
		
		return CsvPreference.TAB_PREFERENCE;
	}

}
