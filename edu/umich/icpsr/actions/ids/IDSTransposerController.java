package edu.umich.icpsr.actions.ids;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


import org.apache.log4j.Logger;

import edu.umich.icpsr.ids.IDSTransposer;
import edu.umich.icpsr.util.Email;

public class IDSTransposerController 
{
	private static Logger log = Logger.getLogger(IDSTransposerController.class);
	private File entityMappingFile;//The entity mapping file
	private File relationMappingFile;//The relation mapping file
	private String entityMappingFileFileName;
	private String relationMappingFileFileName;
	private File dataFile1;//The data file
	private File dataFile2;//The data file
	private File dataFile3;//The data file
	private File dataFile4;//The data file
	private File dataFile5;//The data file
	private File dataFile6;//The data file
	private File dataFile7;//The data file
	private File dataFile8;//The data file
	private File dataFile9;//The data file
	private File dataFile10;//The data file
	private File dataFile11;//The data file
	private File dataFile12;//The data file
	private File dataFile13;//The data file
	private File dataFile14;//The data file
	private File dataFile15;//The data file
	private File dataFile16;//The data file
	private File dataFile17;//The data file
	private File dataFile18;//The data file
	private File dataFile19;//The data file
	private File dataFile20;//The data file
	private String message="";
	private String dataFile1FileName;
	private String dataFile2FileName;
	private String dataFile3FileName;
	private String dataFile4FileName;
	private String dataFile5FileName;
	private String dataFile6FileName;
	private String dataFile7FileName;
	private String dataFile8FileName;
	private String dataFile9FileName;
	private String dataFile10FileName;
	private String dataFile11FileName;
	private String dataFile12FileName;
	private String dataFile13FileName;
	private String dataFile14FileName;
	private String dataFile15FileName;
	private String dataFile16FileName;
	private String dataFile17FileName;
	private String dataFile18FileName;
	private String dataFile19FileName;
	private String dataFile20FileName;
	private String emailAddress;
	
	private String FROM_EMAIL_ADDRESS = "noreply@umich.edu";
	
	private List<String> resultFiles = new ArrayList<String>();
	String zipFileName;
	

	
	
	public String execute() 
    {
		log.info("entering IDSTransposer controller.");
		return "UPLOAD";
    }
	
	public String upload()
	{
		log.info("Entering IDSTransposer upload");
		

		if ((entityMappingFile == null) ||  (relationMappingFile == null) || (dataFile1 == null))
		{
			message = "Please specify an entity mapping file, relationship mapping file, and at least one data file";
			return "DONE";
		}
		
		Map <String, File> dataFiles = new HashMap<String, File>();
		
		if (dataFile1 != null)
		{
			dataFiles.put(dataFile1FileName, dataFile1);		
		}
		
		if (dataFile2 != null)
		{
			dataFiles.put(dataFile2FileName, dataFile2);			
		}
		
		if (dataFile3 != null)
		{
			dataFiles.put(dataFile3FileName, dataFile3);			
		}
		
		if (dataFile4 != null)
		{
			dataFiles.put(dataFile4FileName, dataFile4);			
		}
		
		if (dataFile5 != null)
		{
			dataFiles.put(dataFile5FileName, dataFile5);			
		}
		
		if (dataFile6 != null)
		{
			dataFiles.put(dataFile6FileName, dataFile6);			
		}
		
		if (dataFile7 != null)
		{
			dataFiles.put(dataFile7FileName, dataFile7);			
		}
		
		if (dataFile8 != null)
		{
			dataFiles.put(dataFile8FileName, dataFile8);			
		}
		
		if (dataFile9 != null)
		{
			dataFiles.put(dataFile9FileName, dataFile9);			
		}
		
		if (dataFile10 != null)
		{
			dataFiles.put(dataFile10FileName, dataFile10);		
		}
		if (dataFile11 != null)
		{
			dataFiles.put(dataFile11FileName, dataFile11);		
		}
		
		if (dataFile12 != null)
		{
			dataFiles.put(dataFile12FileName, dataFile12);			
		}
		
		if (dataFile13 != null)
		{
			dataFiles.put(dataFile13FileName, dataFile13);			
		}
		
		if (dataFile14 != null)
		{
			dataFiles.put(dataFile14FileName, dataFile14);			
		}
		
		if (dataFile15 != null)
		{
			dataFiles.put(dataFile15FileName, dataFile15);			
		}
		
		if (dataFile16 != null)
		{
			dataFiles.put(dataFile16FileName, dataFile16);			
		}
		
		if (dataFile17 != null)
		{
			dataFiles.put(dataFile17FileName, dataFile17);			
		}
		
		if (dataFile18 != null)
		{
			dataFiles.put(dataFile18FileName, dataFile18);			
		}
		
		if (dataFile19 != null)
		{
			dataFiles.put(dataFile19FileName, dataFile19);			
		}
		
		if (dataFile20 != null)
		{
			dataFiles.put(dataFile20FileName, dataFile20);		
		}
		
		try 
		{
			IDSTransposer idsTransposer= new IDSTransposer(dataFiles, entityMappingFile, entityMappingFileFileName, relationMappingFile, relationMappingFileFileName);
			resultFiles = idsTransposer.process();	
				
			log.info("Total number of files produced: " + resultFiles.size());
			zipFileName = zipFiles(resultFiles);
			
			emailResult();
			
			deleteTempFiles();
		}
		catch (Exception e)
		{
			log.error("An Error has occured.");
			message = "An error has occured.  IDS Transposer failed to process the data. \n " + e.getMessage();
			return "DONE";
		}
		log.info("Processing completed.");			
		
		message = "IDS Transposer process completed.  The results have been sent to your email address.";
		return "DONE";
	}
	
	private void deleteTempFiles()
	{
		entityMappingFile.delete();
		relationMappingFile.delete();
		if (dataFile1 != null)
		{
			dataFile1.delete();
		}
		if (dataFile2 != null)
		{
			dataFile2.delete();
		}
		if (dataFile3 != null)
		{
			dataFile3.delete();
		}
		if (dataFile4 != null)
		{
			dataFile4.delete();
		}
		if (dataFile5 != null)
		{
			dataFile5.delete();
		}
		if (dataFile6 != null)
		{
			dataFile6.delete();
		}
		if (dataFile7 != null)
		{
			dataFile7.delete();
		}
		if (dataFile8 != null)
		{
			dataFile8.delete();
		}
		if (dataFile9 != null)
		{
			dataFile9.delete();
		}
		if (dataFile10 != null)
		{
			dataFile10.delete();
		}
		if (dataFile11 != null)
		{
			dataFile11.delete();
		}
		if (dataFile12 != null)
		{
			dataFile12.delete();
		}
		if (dataFile13 != null)
		{
			dataFile13.delete();
		}
		if (dataFile14 != null)
		{
			dataFile14.delete();
		}
		if (dataFile15 != null)
		{
			dataFile15.delete();
		}
		if (dataFile16 != null)
		{
			dataFile16.delete();
		}
		if (dataFile17 != null)
		{
			dataFile17.delete();
		}
		if (dataFile18 != null)
		{
			dataFile18.delete();
		}
		if (dataFile19 != null)
		{
			dataFile19.delete();
		}
		if (dataFile20 != null)
		{
			dataFile20.delete();
		}
		for (String fn:resultFiles)
		{
			File resultFile = new File(fn);
			resultFile.delete();
		}
		
		File zipFile = new File(zipFileName);
		zipFile.delete();
	}
	
	private String zipFiles(List<String> fileNames)
	{
		String zipFileName = "";
		byte[] buffer = new byte[1024];
		 
    	try
    	{    	
    		File zipFile = File.createTempFile("Results", ".zip");
    		zipFileName = zipFile.getAbsolutePath();
    		FileOutputStream fos = new FileOutputStream(zipFile.getAbsolutePath());
    		ZipOutputStream zos = new ZipOutputStream(fos);
    		for (String fn:fileNames)
    		{
    			ZipEntry ze= new ZipEntry(new ZipEntry(fn.substring(fn.lastIndexOf('/')+1)));
    			zos.putNextEntry(ze);
    			FileInputStream in = new FileInputStream(fn);
 
    			int len;
    			while ((len = in.read(buffer)) > 0) 
    			{
    				zos.write(buffer, 0, len);
    			}
    			in.close();
    		}
    		zos.closeEntry();
    		zos.close();
 
    		log.info("created zip file " + zipFile.getAbsolutePath());
 
    	}
    	catch(IOException ex)
    	{
    		log.info("error when zipping files - " + ex.getMessage());
    	}
		
		return zipFileName;
	}
	
	private void emailResult()
	{
		Email email = new Email();
		email.setTo(emailAddress);
		email.setFrom(FROM_EMAIL_ADDRESS);
		email.setSubject("IDS Processing Results");
		List<String> attachment = new ArrayList<String>();
		attachment.add(zipFileName);
		email.setMultipartMessageContent("Your results are attached here.  Please let us know if there is any issue.  Thanks for using the service.  \n\nICPSR", attachment);
		try 
		{
			email.sendMessageWithAttachment();
		} catch (Exception e) 
		{
			log.error("Error when sending message. " + e.getMessage());
		}
	}
	
	public void setEntityMappingFile(File entityMappingFile)
	{
		this.entityMappingFile = entityMappingFile;
	}
	
	public void setRelationMappingFile(File relationMappingFile)
	{
		this.relationMappingFile = relationMappingFile;
	}
	
	public void setDataFile1(File dataFile1)
	{
		this.dataFile1 = dataFile1;
	}
	
	public void setDataFile2(File dataFile2)
	{
		this.dataFile2 = dataFile2;
	}
	public void setDataFile3(File dataFile3)
	{
		this.dataFile3 = dataFile3;
	}
	public void setDataFile4(File dataFile4)
	{
		this.dataFile4 = dataFile4;
	}
	public void setDataFile5(File dataFile5)
	{
		this.dataFile5 = dataFile5;
	}
	public void setDataFile6(File dataFile6)
	{
		this.dataFile6 = dataFile6;
	}
	public void setDataFile7(File dataFile7)
	{
		this.dataFile7 = dataFile7;
	}
	public void setDataFile8(File dataFile8)
	{
		this.dataFile8 = dataFile8;
	}
	public void setDataFile9(File dataFile9)
	{
		this.dataFile9 = dataFile9;
	}
	public void setDataFile10(File dataFile10)
	{
		this.dataFile10 = dataFile10;
	}
	public void setDataFile11(File dataFile11)
	{
		this.dataFile11 = dataFile11;
	}
	
	public void setDataFile12(File dataFile12)
	{
		this.dataFile12 = dataFile12;
	}
	public void setDataFile13(File dataFile13)
	{
		this.dataFile13 = dataFile13;
	}
	public void setDataFile14(File dataFile14)
	{
		this.dataFile14 = dataFile14;
	}
	public void setDataFile15(File dataFile15)
	{
		this.dataFile15 = dataFile15;
	}
	public void setDataFile16(File dataFile16)
	{
		this.dataFile16 = dataFile16;
	}
	public void setDataFile17(File dataFile17)
	{
		this.dataFile17 = dataFile17;
	}
	public void setDataFile18(File dataFile18)
	{
		this.dataFile18 = dataFile18;
	}
	public void setDataFile19(File dataFile19)
	{
		this.dataFile19 = dataFile19;
	}
	public void setDataFile20(File dataFile20)
	{
		this.dataFile20 = dataFile20;
	}
	public String getMessage()
	{
		return message;
	}
	
	public void setDataFile1FileName(String dataFile1FileName)
	{
		this.dataFile1FileName = dataFile1FileName;
	}

	public void setDataFile2FileName(String dataFile2FileName)
	{
		this.dataFile2FileName = dataFile2FileName;
	}

	public void setDataFile3FileName(String dataFile3FileName)
	{
		this.dataFile3FileName = dataFile3FileName;
	}

	public void setDataFile4FileName(String dataFile4FileName)
	{
		this.dataFile4FileName = dataFile4FileName;
	}

	public void setDataFile5FileName(String dataFile5FileName)
	{
		this.dataFile5FileName = dataFile5FileName;
	}

	public void setDataFile6FileName(String dataFile6FileName)
	{
		this.dataFile6FileName = dataFile6FileName;
	}

	public void setDataFile7FileName(String dataFile7FileName)
	{
		this.dataFile7FileName = dataFile7FileName;
	}

	public void setDataFile8FileName(String dataFile8FileName)
	{
		this.dataFile8FileName = dataFile8FileName;
	}

	public void setDataFile9FileName(String dataFile9FileName)
	{
		this.dataFile9FileName = dataFile9FileName;
	}

	public void setDataFile10FileName(String dataFile10FileName)
	{
		this.dataFile10FileName = dataFile10FileName;
	}
	
	public void setDataFile11FileName(String dataFile11FileName)
	{
		this.dataFile11FileName = dataFile11FileName;
	}

	public void setDataFile12FileName(String dataFile12FileName)
	{
		this.dataFile12FileName = dataFile12FileName;
	}

	public void setDataFile13FileName(String dataFile13FileName)
	{
		this.dataFile13FileName = dataFile13FileName;
	}

	public void setDataFile14FileName(String dataFile14FileName)
	{
		this.dataFile14FileName = dataFile14FileName;
	}

	public void setDataFile15FileName(String dataFile15FileName)
	{
		this.dataFile15FileName = dataFile15FileName;
	}

	public void setDataFile16FileName(String dataFile16FileName)
	{
		this.dataFile16FileName = dataFile16FileName;
	}

	public void setDataFile17FileName(String dataFile17FileName)
	{
		this.dataFile17FileName = dataFile17FileName;
	}

	public void setDataFile18FileName(String dataFile18FileName)
	{
		this.dataFile18FileName = dataFile18FileName;
	}

	public void setDataFile19FileName(String dataFile19FileName)
	{
		this.dataFile19FileName = dataFile19FileName;
	}

	public void setDataFile20FileName(String dataFile20FileName)
	{
		this.dataFile20FileName = dataFile20FileName;
	}
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}
	
	public void setEntityMappingFileFileName(String entityMappingFileFileName)
	{
		this.entityMappingFileFileName = entityMappingFileFileName;
	}
	
	public void setRelationMappingFileFileName(String relationMappingFileFileName)
	{
		this.relationMappingFileFileName = relationMappingFileFileName;
	}
	

}

