<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
                      "file:///K:/Thip/5.0.0/websrcsvil/dtd/xhtml1-transitional.dtd">
<html>
<!-- WIZGEN Therm 2.0.0 as Batch form - multiBrowserGen = true -->
<%=WebGenerator.writeRuntimeInfo()%>
<head>
<%@ page contentType="text/html; charset=Cp1252"%>
<%@ page import= " 
  java.sql.*, 
  java.util.*, 
  java.lang.reflect.*, 
  javax.naming.*, 
  com.thera.thermfw.common.*, 
  com.thera.thermfw.type.*, 
  com.thera.thermfw.web.*, 
  com.thera.thermfw.security.*, 
  com.thera.thermfw.base.*, 
  com.thera.thermfw.ad.*, 
  com.thera.thermfw.persist.*, 
  com.thera.thermfw.gui.cnr.*, 
  com.thera.thermfw.setting.*, 
  com.thera.thermfw.collector.*, 
  com.thera.thermfw.batch.web.*, 
  com.thera.thermfw.batch.*, 
  com.thera.thermfw.pref.* 
"%> 
<%
  ServletEnvironment se = (ServletEnvironment)Factory.createObject("com.thera.thermfw.web.ServletEnvironment"); 
  BODataCollector YDtsxImportCicliCollaudoBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm YDtsxImportCicliCollaudoForm =  
     new com.thera.thermfw.web.WebFormForBatchForm(request, response, "YDtsxImportCicliCollaudoForm", "YDtsxImportCicliCollaudo", "Arial,10", "com.thera.thermfw.batch.web.BatchFormActionAdapter", false, false, false, true, true, true, null, 0, false, null); 
  YDtsxImportCicliCollaudoForm.setServletEnvironment(se); 
  YDtsxImportCicliCollaudoForm.setJSTypeList(jsList); 
  YDtsxImportCicliCollaudoForm.setHeader("it.thera.thip.cs.PantheraHeader.jsp"); 
  YDtsxImportCicliCollaudoForm.setFooter("com.thera.thermfw.common.Footer.jsp"); 
  ((WebFormForBatchForm)  YDtsxImportCicliCollaudoForm).setGenerateThReportId(true); 
  ((WebFormForBatchForm)  YDtsxImportCicliCollaudoForm).setGenerateSSDEnabled(true); 
  YDtsxImportCicliCollaudoForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = YDtsxImportCicliCollaudoForm.getMode(); 
  String key = YDtsxImportCicliCollaudoForm.getKey(); 
  String errorMessage; 
  boolean requestIsValid = false; 
  boolean leftIsKey = false; 
  boolean conflitPresent = false; 
  String leftClass = ""; 
  try 
  {
     se.initialize(request, response); 
     if(se.begin()) 
     { 
        YDtsxImportCicliCollaudoForm.outTraceInfo(getClass().getName()); 
        String collectorName = YDtsxImportCicliCollaudoForm.findBODataCollectorName(); 
				 YDtsxImportCicliCollaudoBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (YDtsxImportCicliCollaudoBODC instanceof WebDataCollector) 
            ((WebDataCollector)YDtsxImportCicliCollaudoBODC).setServletEnvironment(se); 
        YDtsxImportCicliCollaudoBODC.initialize("YDtsxImportCicliCollaudo", true, 0); 
        int rcBODC; 
        if (YDtsxImportCicliCollaudoBODC.getBo() instanceof BatchRunnable) 
          rcBODC = YDtsxImportCicliCollaudoBODC.initSecurityServices("RUN", mode, true, false, true); 
        else 
          rcBODC = YDtsxImportCicliCollaudoBODC.initSecurityServices(mode, true, true, true); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           YDtsxImportCicliCollaudoForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = YDtsxImportCicliCollaudoBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              YDtsxImportCicliCollaudoForm.setBODataCollector(YDtsxImportCicliCollaudoBODC); 
              YDtsxImportCicliCollaudoForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 

<title></title>
<% 
  WebToolBar myToolBarTB = new com.thera.thermfw.web.WebToolBar("myToolBar", "24", "24", "16", "16", "#f7fbfd","#C8D6E1"); 
  myToolBarTB.setParent(YDtsxImportCicliCollaudoForm); 
   request.setAttribute("toolBar", myToolBarTB); 
%> 
<jsp:include page="/com/thera/thermfw/batch/BatchRunnableMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="toolBar"/> 
</jsp:include> 
<% 
   myToolBarTB.write(out); 
%> 
</head>
<% 
  WebLink link_0 =  
   new com.thera.thermfw.web.WebLink(); 
 link_0.setHttpServletRequest(request); 
 link_0.setHRefAttribute("thermweb/css/thermGrid.css"); 
 link_0.setRelAttribute("STYLESHEET"); 
 link_0.setTypeAttribute("text/css"); 
  link_0.write(out); 
%>
<!--<link href="thermweb/css/thermGrid.css" rel="STYLESHEET" type="text/css">-->
<body bottommargin="0" leftmargin="0" onbeforeunload="<%=YDtsxImportCicliCollaudoForm.getBodyOnBeforeUnload()%>" onload="<%=YDtsxImportCicliCollaudoForm.getBodyOnLoad()%>" onunload="<%=YDtsxImportCicliCollaudoForm.getBodyOnUnload()%>" rightmargin="0" topmargin="0"><%
   YDtsxImportCicliCollaudoForm.writeBodyStartElements(out); 
%> 

	<table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = YDtsxImportCicliCollaudoForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", YDtsxImportCicliCollaudoBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=YDtsxImportCicliCollaudoForm.getServlet()%>" method="post" name="YDtsxImportCicliCollaudoForm" style="height:100%"><%
  YDtsxImportCicliCollaudoForm.writeFormStartElements(out); 
%>

		<table cellpadding="2" cellspacing="2" height="100%" width="100%">
			<tr>
				<td style="height: 0"><% myToolBarTB.writeChildren(out); %> 
</td>
			</tr>
			<tr>
				<td height="100%"><!--<span class="tabbed" id="mytabbed">-->
<table width="100%" height="100%" cellpadding="0" cellspacing="0" style="padding-right:1px">
   <tr valign="top">
     <td><% 
  WebTabbed mytabbed = new com.thera.thermfw.web.WebTabbed("mytabbed", "100%", "100%"); 
  mytabbed.setParent(YDtsxImportCicliCollaudoForm); 
 mytabbed.addTab("DatiGenerali", "it.monchieri.thip.dtsx.resources.YDtsxPtExpOrdEsecRunner", "DatiGenerali", "YDtsxImportCicliCollaudo", null, null, null, null); 
  mytabbed.write(out); 
%>

     </td>
   </tr>
   <tr>
     <td height="100%"><div class="tabbed_pagine" id="tabbedPagine" style="position: relative; width: 100%; height: 100%;"> <div class="tabbed_page" id="<%=mytabbed.getTabPageId("DatiGenerali")%>" style="width:100%;height:100%;overflow:auto;"><% mytabbed.startTab("DatiGenerali"); %>
							<table cellpadding="1" cellspacing="2">
								<tr>
								</tr>
							</table>
					<% mytabbed.endTab(); %> 
</div>
				</div><% mytabbed.endTabbed();%> 

     </td>
   </tr>
</table><!--</span>--></td>
			</tr>
			<tr>
				<td style="height: 0"><% 
  WebErrorList errorList = new com.thera.thermfw.web.WebErrorList(); 
  errorList.setParent(YDtsxImportCicliCollaudoForm); 
  errorList.write(out); 
%>
<!--<span class="errorlist"></span>--></td>
			</tr>
		</table>
	<%
  YDtsxImportCicliCollaudoForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = YDtsxImportCicliCollaudoForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", YDtsxImportCicliCollaudoBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>


<%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              YDtsxImportCicliCollaudoForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, YDtsxImportCicliCollaudoBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, YDtsxImportCicliCollaudoBODC.getErrorList().getErrors()); 
           if(YDtsxImportCicliCollaudoBODC.getConflict() != null) 
                conflitPresent = true; 
     } 
     else 
        errors.add(new ErrorMessage("BAS0000010")); 
  } 
  catch(NamingException e) { 
     errorMessage = e.getMessage(); 
     errors.add(new ErrorMessage("CBS000025", errorMessage));  } 
  catch(SQLException e) {
     errorMessage = e.getMessage(); 
     errors.add(new ErrorMessage("BAS0000071", errorMessage));  } 
  catch(Throwable e) {
     e.printStackTrace(Trace.excStream);
  }
  finally 
  {
     if(YDtsxImportCicliCollaudoBODC != null && !YDtsxImportCicliCollaudoBODC.close(false)) 
        errors.addAll(0, YDtsxImportCicliCollaudoBODC.getErrorList().getErrors()); 
     try 
     { 
        se.end(); 
     }
     catch(IllegalArgumentException e) { 
        e.printStackTrace(Trace.excStream); 
     } 
     catch(SQLException e) { 
        e.printStackTrace(Trace.excStream); 
     } 
  } 
  if(!errors.isEmpty())
  { 
      if(!conflitPresent)
  { 
     request.setAttribute("ErrorMessages", errors); 
     String errorPage = YDtsxImportCicliCollaudoForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", YDtsxImportCicliCollaudoBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = YDtsxImportCicliCollaudoForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>
</html>
