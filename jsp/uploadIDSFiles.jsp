<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" id="ICPSR">
<head>
    <title>Upload IDS Files</title>
    <style type="text/css">
	.dropcontent{
	width: 300px;
	height: 140px;
	border: 1px solid black;
	background-color: #FFECC6;
	display:block;
	}
	</style>
</head>
<body>
<article>
	<div style="width:800px;">
	<s:actionerror />
	<s:fielderror />
	<h1>Intermediate Data Structure (IDS) Transposer</h1>
	<p>This tool can be used to transpose data files into the IDS format.  Provide your email address, mapping files, and
	source data files in the fields below.  The tool does not work well for large input files (anything over 1 MB.)  The 
	resulting IDS-formatted output files will be emailed to you once the transposition process has completed.</p>
	<s:form name="IDSFileUploadForm" action="idsTransposer!upload" enctype="multipart/form-data" method="post">
		<p>Your Email Address: <s:textfield name="emailAddress" label="Email Address" /></p>
		
		<h3>Mapping Files</h3>
		<p>The entity and relationship mapping files are required by the transposer.  The system accepts comma-separated
		value (CSV) text files with column names in the first row.  For more information about constructing the mapping files,
		refer to the mapping guide.</p>
		<p>Entity Mapping File: <s:file name="entityMappingFile" label="Entity Mapping File"/></p>
		<p>Relationship Mapping File: <s:file name="relationMappingFile" label="Relation Mapping File"/></p>
		
		<h3>Data Files</h3>
		<p>Upload your source data files in the fields below.  The transposer can accept up to 10 source data files,
		but only one file is required.  The system accepts comma-separated value (CSV) text files with column (variable) 
		names in the first row.  When you are finished, click the Submit button at the bottom of the page.</p>
		<p>Data File 1: <s:file name="dataFile1" label="Date File 1"/></p>
		<p>Data File 2: <s:file name="dataFile2" label="Date File 2"/></p>
		<p>Data File 3: <s:file name="dataFile3" label="Date File 3"/></p>
		<p>Data File 4: <s:file name="dataFile4" label="Date File 4"/></p>
		<p>Data File 5: <s:file name="dataFile5" label="Date File 5"/></p>
		<p>Data File 6: <s:file name="dataFile6" label="Date File 6"/></p>
		<p>Data File 7: <s:file name="dataFile7" label="Date File 7"/></p>
		<p>Data File 8: <s:file name="dataFile8" label="Date File 8"/></p>
		<p>Data File 9: <s:file name="dataFile9" label="Date File 9"/></p>
		<p>Data File 10: <s:file name="dataFile10" label="Date File 10"/></p>
		<p>Data File 11: <s:file name="dataFile11" label="Date File 11"/></p>
		<p>Data File 12: <s:file name="dataFile12" label="Date File 12"/></p>
		<p>Data File 13: <s:file name="dataFile13" label="Date File 13"/></p>
		<p>Data File 14: <s:file name="dataFile14" label="Date File 14"/></p>
		<p>Data File 15: <s:file name="dataFile15" label="Date File 15"/></p>
		<p>Data File 16: <s:file name="dataFile16" label="Date File 16"/></p>
		<p>Data File 17: <s:file name="dataFile17" label="Date File 17"/></p>
		<p>Data File 18: <s:file name="dataFile18" label="Date File 18"/></p>
		<p>Data File 19: <s:file name="dataFile19" label="Date File 19"/></p>
		<p>Data File 20: <s:file name="dataFile20" label="Date File 20"/></p>
		<br/><br/>
		
		<s:submit cssStyle="font-size:32px;"/>
	</s:form> 
	</div>
</article>
</body>
</html>