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
  BODataCollector YStampaPrevCommBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm YStampaPrevCommForm =  
     new com.thera.thermfw.web.WebFormForBatchForm(request, response, "YStampaPrevCommForm", "YStampaPrevComm", "Arial,10", "com.thera.thermfw.batch.web.BatchFormActionAdapter", false, false, true, true, true, true, null, 1, true, "it/monchieri/thip/vendite/offerteCliente/StampaPreventivoCommessa.js"); 
  YStampaPrevCommForm.setServletEnvironment(se); 
  YStampaPrevCommForm.setJSTypeList(jsList); 
  YStampaPrevCommForm.setHeader("it.thera.thip.cs.Header.jsp"); 
  YStampaPrevCommForm.setFooter("it.thera.thip.cs.Footer.jsp"); 
  ((WebFormForBatchForm)  YStampaPrevCommForm).setGenerateThReportId(true); 
  YStampaPrevCommForm.setWebFormModifierClass("it.monchieri.thip.vendite.offerteCliente.web.StampaPreventivoCommessaModifier"); 
  YStampaPrevCommForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = YStampaPrevCommForm.getMode(); 
  String key = YStampaPrevCommForm.getKey(); 
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
        YStampaPrevCommForm.outTraceInfo(getClass().getName()); 
        String collectorName = YStampaPrevCommForm.findBODataCollectorName(); 
				 YStampaPrevCommBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (YStampaPrevCommBODC instanceof WebDataCollector) 
            ((WebDataCollector)YStampaPrevCommBODC).setServletEnvironment(se); 
        YStampaPrevCommBODC.initialize("YStampaPrevComm", true, 1); 
        int rcBODC; 
        if (YStampaPrevCommBODC.getBo() instanceof BatchRunnable) 
          rcBODC = YStampaPrevCommBODC.initSecurityServices("RUN", mode, true, false, true); 
        else 
          rcBODC = YStampaPrevCommBODC.initSecurityServices(mode, true, true, true); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           YStampaPrevCommForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = YStampaPrevCommBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              YStampaPrevCommForm.setBODataCollector(YStampaPrevCommBODC); 
              YStampaPrevCommForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 
<% 
  WebMenuBar menuBar = new com.thera.thermfw.web.WebMenuBar("HM_Array1", "150", "#000000","#000000","#A5B6CE","#E4EAEF","#FFFFFF","#000000"); 
  menuBar.setParent(YStampaPrevCommForm); 
   request.setAttribute("menuBar", menuBar); 
%> 
<jsp:include page="/it/thera/thip/cs/PrintRunnableMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="menuBar"/> 
</jsp:include> 
<% 
  menuBar.write(out); 
  menuBar.writeChildren(out); 
%> 
<% 
  WebToolBar myToolBarTB = new com.thera.thermfw.web.WebToolBar("myToolBar", "24", "24", "16", "16", "#f7fbfd","#C8D6E1"); 
  myToolBarTB.setParent(YStampaPrevCommForm); 
   request.setAttribute("toolBar", myToolBarTB); 
%> 
<jsp:include page="/it/thera/thip/cs/PrintRunnableMenu.jsp" flush="true"> 
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
 link_0.setHRefAttribute("com/thera/thermfw/common/form.css"); 
 link_0.setRelAttribute("STYLESHEET"); 
 link_0.setTypeAttribute("text/css"); 
  link_0.write(out); 
%>
<!--<link href="com/thera/thermfw/common/form.css" rel="STYLESHEET" type="text/css">-->
<% 
  WebLink link_1 =  
   new com.thera.thermfw.web.WebLink(); 
 link_1.setHttpServletRequest(request); 
 link_1.setHRefAttribute("thermweb/css/thermGrid.css"); 
 link_1.setRelAttribute("STYLESHEET"); 
 link_1.setTypeAttribute("text/css"); 
  link_1.write(out); 
%>
<!--<link href="thermweb/css/thermGrid.css" rel="STYLESHEET" type="text/css">-->
<body bottommargin="0" leftmargin="0" onbeforeunload="<%=YStampaPrevCommForm.getBodyOnBeforeUnload()%>" onload="<%=YStampaPrevCommForm.getBodyOnLoad()%>" onunload="<%=YStampaPrevCommForm.getBodyOnUnload()%>" rightmargin="0" topmargin="0"><%
   YStampaPrevCommForm.writeBodyStartElements(out); 
%> 


<table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = YStampaPrevCommForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", YStampaPrevCommBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=YStampaPrevCommForm.getServlet()%>" method="post" name="StampaPreventivoDiCommessa" style="height:100%"><%
  YStampaPrevCommForm.writeFormStartElements(out); 
%>

	<table cellpadding="2" cellspacing="0" height="100%" id="emptyborder" width="100%">
		<tr><td style="height:0"><% menuBar.writeElements(out); %> 
</td></tr>
		<tr><td style="height:0"><% myToolBarTB.writeChildren(out); %> 
</td></tr>
		<tr>
			<td height="100%">
				<!--<span class="tabbed" id="mytabbed">-->
<table width="100%" height="100%" cellpadding="0" cellspacing="0" style="padding-right:1px">
   <tr valign="top">
     <td><% 
  WebTabbed mytabbed = new com.thera.thermfw.web.WebTabbed("mytabbed", "100%", "100%"); 
  mytabbed.setParent(YStampaPrevCommForm); 
 mytabbed.addTab("tab1", "it.monchieri.thip.vendite.offerteCliente.resources.StampaPreventivoCommessa", "Generale", "YStampaPrevComm", null, null, null, null); 
  mytabbed.write(out); 
%>

     </td>
   </tr>
   <tr>
     <td height="100%"><div class="tabbed_pagine" id="tabbedPagine" style="position: relative; width: 100%; height: 100%;">
					<div class="tabbed_page" id="<%=mytabbed.getTabPageId("tab1")%>" style="width:100%;height:100%;overflow:auto;"><% mytabbed.startTab("tab1"); %>
						<table height="100%" width="100%">
						<tr>
						<% 
  WebTextInput YStampaPrevCommChiaveOfferta =  
     new com.thera.thermfw.web.WebTextInput("YStampaPrevComm", "ChiaveOfferta"); 
  YStampaPrevCommChiaveOfferta.setParent(YStampaPrevCommForm); 
%>
<input class="<%=YStampaPrevCommChiaveOfferta.getClassType()%>" id="<%=YStampaPrevCommChiaveOfferta.getId()%>" maxlength="<%=YStampaPrevCommChiaveOfferta.getMaxLength()%>" name="<%=YStampaPrevCommChiaveOfferta.getName()%>" size="<%=YStampaPrevCommChiaveOfferta.getSize()%>" type="hidden"><% 
  YStampaPrevCommChiaveOfferta.write(out); 
%>

						<% 
  WebTextInput YStampaPrevCommChiaviSelezionati =  
     new com.thera.thermfw.web.WebTextInput("YStampaPrevComm", "ChiaviSelezionati"); 
  YStampaPrevCommChiaviSelezionati.setParent(YStampaPrevCommForm); 
%>
<input class="<%=YStampaPrevCommChiaviSelezionati.getClassType()%>" id="<%=YStampaPrevCommChiaviSelezionati.getId()%>" maxlength="<%=YStampaPrevCommChiaviSelezionati.getMaxLength()%>" name="<%=YStampaPrevCommChiaviSelezionati.getName()%>" size="<%=YStampaPrevCommChiaviSelezionati.getSize()%>" type="hidden"><% 
  YStampaPrevCommChiaviSelezionati.write(out); 
%>

						<td>
							<% 
  WebCheckBox YStampaPrevCommStampaInLingua =  
     new com.thera.thermfw.web.WebCheckBox("YStampaPrevComm", "StampaInLingua"); 
  YStampaPrevCommStampaInLingua.setParent(YStampaPrevCommForm); 
%>
<input id="<%=YStampaPrevCommStampaInLingua.getId()%>" name="<%=YStampaPrevCommStampaInLingua.getName()%>" type="checkbox" value="Y"><%
  YStampaPrevCommStampaInLingua.write(out); 
%>

						</td>
					</tr>
							<tr>
			<td valign="top">
				<table width="100%">
					<tr id="ListaTR" style="display:'none'">
						<td id="ListaTD"></td>
					</tr>
				</table>
			</td>
		</tr>
						</table>
					<% mytabbed.endTab(); %> 
</div>
				</div><% mytabbed.endTabbed();%> 

     </td>
   </tr>
</table><!--</span>-->
			</td>
		</tr>
		<tr><td style="height:0"><% 
  WebErrorList errorList = new com.thera.thermfw.web.WebErrorList(); 
  errorList.setParent(YStampaPrevCommForm); 
  errorList.write(out); 
%>
<!--<span class="errorlist"></span>--></td></tr>
	</table>
	<div style="display: none;">
	<% 
  WebCheckBox YStampaPrevCommSSDEnabled =  
     new com.thera.thermfw.web.WebCheckBox("YStampaPrevComm", "SSDEnabled"); 
  YStampaPrevCommSSDEnabled.setParent(YStampaPrevCommForm); 
%>
<input id="<%=YStampaPrevCommSSDEnabled.getId()%>" name="<%=YStampaPrevCommSSDEnabled.getName()%>" type="checkbox" value="Y"><%
  YStampaPrevCommSSDEnabled.write(out); 
%>

	<% 
  WebCheckBox YStampaPrevCommDocDgtEnabled =  
     new com.thera.thermfw.web.WebCheckBox("YStampaPrevComm", "DocDgtEnabled"); 
  YStampaPrevCommDocDgtEnabled.setParent(YStampaPrevCommForm); 
%>
<input id="<%=YStampaPrevCommDocDgtEnabled.getId()%>" name="<%=YStampaPrevCommDocDgtEnabled.getName()%>" type="checkbox" value="Y"><%
  YStampaPrevCommDocDgtEnabled.write(out); 
%>
	
	</div>	
<%
  YStampaPrevCommForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = YStampaPrevCommForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", YStampaPrevCommBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>



<%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              YStampaPrevCommForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, YStampaPrevCommBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, YStampaPrevCommBODC.getErrorList().getErrors()); 
           if(YStampaPrevCommBODC.getConflict() != null) 
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
     if(YStampaPrevCommBODC != null && !YStampaPrevCommBODC.close(false)) 
        errors.addAll(0, YStampaPrevCommBODC.getErrorList().getErrors()); 
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
     String errorPage = YStampaPrevCommForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", YStampaPrevCommBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = YStampaPrevCommForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>
<% 
  WebScript script_0 =  
   new com.thera.thermfw.web.WebScript(); 
 script_0.setRequest(request); 
 script_0.setSrcAttribute("com/thera/thermfw/batch/PrintBatchRunnable.js"); 
 script_0.setLanguageAttribute("JavaScript1.2"); 
  script_0.write(out); 
%>
<!--<script language="JavaScript1.2" src="com/thera/thermfw/batch/PrintBatchRunnable.js" type="text/javascript"></script>-->
</html>
