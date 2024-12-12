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
  BODataCollector YDtsxRmOdaDDTRuBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm YDtsxRmOdaDDTRuForm =  
     new com.thera.thermfw.web.WebFormForBatchForm(request, response, "YDtsxRmOdaDDTRuForm", "YDtsxRmOdaDDTRu", "Arial,10", "com.thera.thermfw.batch.web.BatchFormActionAdapter", false, false, false, true, true, true, null, 0, false, null); 
  YDtsxRmOdaDDTRuForm.setServletEnvironment(se); 
  YDtsxRmOdaDDTRuForm.setJSTypeList(jsList); 
  YDtsxRmOdaDDTRuForm.setHeader("it.thera.thip.cs.PantheraHeader.jsp"); 
  YDtsxRmOdaDDTRuForm.setFooter("com.thera.thermfw.common.Footer.jsp"); 
  ((WebFormForBatchForm)  YDtsxRmOdaDDTRuForm).setGenerateThReportId(true); 
  ((WebFormForBatchForm)  YDtsxRmOdaDDTRuForm).setGenerateSSDEnabled(true); 
  YDtsxRmOdaDDTRuForm.setWebFormModifierClass("it.monchieri.thip.dtsx.web.YDtsxRmOdaDDTRunnerFormModifier"); 
  YDtsxRmOdaDDTRuForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = YDtsxRmOdaDDTRuForm.getMode(); 
  String key = YDtsxRmOdaDDTRuForm.getKey(); 
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
        YDtsxRmOdaDDTRuForm.outTraceInfo(getClass().getName()); 
        String collectorName = YDtsxRmOdaDDTRuForm.findBODataCollectorName(); 
				 YDtsxRmOdaDDTRuBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (YDtsxRmOdaDDTRuBODC instanceof WebDataCollector) 
            ((WebDataCollector)YDtsxRmOdaDDTRuBODC).setServletEnvironment(se); 
        YDtsxRmOdaDDTRuBODC.initialize("YDtsxRmOdaDDTRu", true, 0); 
        int rcBODC; 
        if (YDtsxRmOdaDDTRuBODC.getBo() instanceof BatchRunnable) 
          rcBODC = YDtsxRmOdaDDTRuBODC.initSecurityServices("RUN", mode, true, false, true); 
        else 
          rcBODC = YDtsxRmOdaDDTRuBODC.initSecurityServices(mode, true, true, true); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           YDtsxRmOdaDDTRuForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = YDtsxRmOdaDDTRuBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              YDtsxRmOdaDDTRuForm.setBODataCollector(YDtsxRmOdaDDTRuBODC); 
              YDtsxRmOdaDDTRuForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 

<title>Estrazione dati MQT</title>
<% 
  WebToolBar myToolBarTB = new com.thera.thermfw.web.WebToolBar("myToolBar", "24", "24", "16", "16", "#f7fbfd","#C8D6E1"); 
  myToolBarTB.setParent(YDtsxRmOdaDDTRuForm); 
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
<body bottommargin="0" leftmargin="0" onbeforeunload="<%=YDtsxRmOdaDDTRuForm.getBodyOnBeforeUnload()%>" onload="<%=YDtsxRmOdaDDTRuForm.getBodyOnLoad()%>" onunload="<%=YDtsxRmOdaDDTRuForm.getBodyOnUnload()%>" rightmargin="0" topmargin="0"><%
   YDtsxRmOdaDDTRuForm.writeBodyStartElements(out); 
%> 

	<table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = YDtsxRmOdaDDTRuForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", YDtsxRmOdaDDTRuBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=YDtsxRmOdaDDTRuForm.getServlet()%>" method="post" name="YDtsxRmOdaDDTRuForm" style="height:100%"><%
  YDtsxRmOdaDDTRuForm.writeFormStartElements(out); 
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
  mytabbed.setParent(YDtsxRmOdaDDTRuForm); 
 mytabbed.addTab("DatiGenerali", "it.monchieri.thip.dtsx.resources.YDtsxPtExpOrdEsecRunner", "DatiGenerali", "YDtsxRmOdaDDTRu", null, null, null, null); 
  mytabbed.write(out); 
%>

     </td>
   </tr>
   <tr>
     <td height="100%"><div class="tabbed_pagine" id="tabbedPagine" style="position: relative; width: 100%; height: 100%;"> <div class="tabbed_page" id="<%=mytabbed.getTabPageId("DatiGenerali")%>" style="width:100%;height:100%;overflow:auto;"><% mytabbed.startTab("DatiGenerali"); %>
							<table cellpadding="1" cellspacing="2">
								<tr>
									<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YDtsxRmOdaDDTRu", "DataEstrazioneAnalisiRm", null); 
   label.setParent(YDtsxRmOdaDDTRuForm); 
%><label class="<%=label.getClassType()%>" for="DataEstrazioneAnalisiRm"><%label.write(out);%></label><%}%></td>
									<td><% 
  WebTextInput YDtsxRmOdaDDTRuDataEstrazioneAnalisiRm =  
     new com.thera.thermfw.web.WebTextInput("YDtsxRmOdaDDTRu", "DataEstrazioneAnalisiRm"); 
  YDtsxRmOdaDDTRuDataEstrazioneAnalisiRm.setShowCalendarBtn(true); 
  YDtsxRmOdaDDTRuDataEstrazioneAnalisiRm.setParent(YDtsxRmOdaDDTRuForm); 
%>
<input class="<%=YDtsxRmOdaDDTRuDataEstrazioneAnalisiRm.getClassType()%>" id="<%=YDtsxRmOdaDDTRuDataEstrazioneAnalisiRm.getId()%>" maxlength="<%=YDtsxRmOdaDDTRuDataEstrazioneAnalisiRm.getMaxLength()%>" name="<%=YDtsxRmOdaDDTRuDataEstrazioneAnalisiRm.getName()%>" size="<%=YDtsxRmOdaDDTRuDataEstrazioneAnalisiRm.getSize()%>" type="text"><% 
  YDtsxRmOdaDDTRuDataEstrazioneAnalisiRm.write(out); 
%>
</td>
								</tr>
								<tr>
									<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YDtsxRmOdaDDTRu", "DataEstrazioneAnalisiAcc", null); 
   label.setParent(YDtsxRmOdaDDTRuForm); 
%><label class="<%=label.getClassType()%>" for="DataEstrazioneAnalisiAcc"><%label.write(out);%></label><%}%></td>
									<td><% 
  WebTextInput YDtsxRmOdaDDTRuDataEstrazioneAnalisiAcc =  
     new com.thera.thermfw.web.WebTextInput("YDtsxRmOdaDDTRu", "DataEstrazioneAnalisiAcc"); 
  YDtsxRmOdaDDTRuDataEstrazioneAnalisiAcc.setShowCalendarBtn(true); 
  YDtsxRmOdaDDTRuDataEstrazioneAnalisiAcc.setParent(YDtsxRmOdaDDTRuForm); 
%>
<input class="<%=YDtsxRmOdaDDTRuDataEstrazioneAnalisiAcc.getClassType()%>" id="<%=YDtsxRmOdaDDTRuDataEstrazioneAnalisiAcc.getId()%>" maxlength="<%=YDtsxRmOdaDDTRuDataEstrazioneAnalisiAcc.getMaxLength()%>" name="<%=YDtsxRmOdaDDTRuDataEstrazioneAnalisiAcc.getName()%>" size="<%=YDtsxRmOdaDDTRuDataEstrazioneAnalisiAcc.getSize()%>" type="text"><% 
  YDtsxRmOdaDDTRuDataEstrazioneAnalisiAcc.write(out); 
%>
</td>
								</tr>
								<tr>
									<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YDtsxRmOdaDDTRu", "DataEstrazioneOrdFor", null); 
   label.setParent(YDtsxRmOdaDDTRuForm); 
%><label class="<%=label.getClassType()%>" for="DataEstrazioneOrdFor"><%label.write(out);%></label><%}%></td>
									<td><% 
  WebTextInput YDtsxRmOdaDDTRuDataEstrazioneOrdFor =  
     new com.thera.thermfw.web.WebTextInput("YDtsxRmOdaDDTRu", "DataEstrazioneOrdFor"); 
  YDtsxRmOdaDDTRuDataEstrazioneOrdFor.setShowCalendarBtn(true); 
  YDtsxRmOdaDDTRuDataEstrazioneOrdFor.setParent(YDtsxRmOdaDDTRuForm); 
%>
<input class="<%=YDtsxRmOdaDDTRuDataEstrazioneOrdFor.getClassType()%>" id="<%=YDtsxRmOdaDDTRuDataEstrazioneOrdFor.getId()%>" maxlength="<%=YDtsxRmOdaDDTRuDataEstrazioneOrdFor.getMaxLength()%>" name="<%=YDtsxRmOdaDDTRuDataEstrazioneOrdFor.getName()%>" size="<%=YDtsxRmOdaDDTRuDataEstrazioneOrdFor.getSize()%>" type="text"><% 
  YDtsxRmOdaDDTRuDataEstrazioneOrdFor.write(out); 
%>
</td>
								</tr>
								<tr>
									<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YDtsxRmOdaDDTRu", "DataEstrazioneDdtFor", null); 
   label.setParent(YDtsxRmOdaDDTRuForm); 
%><label class="<%=label.getClassType()%>" for="DataEstrazioneDdtFor"><%label.write(out);%></label><%}%></td>
									<td><% 
  WebTextInput YDtsxRmOdaDDTRuDataEstrazioneDdtFor =  
     new com.thera.thermfw.web.WebTextInput("YDtsxRmOdaDDTRu", "DataEstrazioneDdtFor"); 
  YDtsxRmOdaDDTRuDataEstrazioneDdtFor.setShowCalendarBtn(true); 
  YDtsxRmOdaDDTRuDataEstrazioneDdtFor.setParent(YDtsxRmOdaDDTRuForm); 
%>
<input class="<%=YDtsxRmOdaDDTRuDataEstrazioneDdtFor.getClassType()%>" id="<%=YDtsxRmOdaDDTRuDataEstrazioneDdtFor.getId()%>" maxlength="<%=YDtsxRmOdaDDTRuDataEstrazioneDdtFor.getMaxLength()%>" name="<%=YDtsxRmOdaDDTRuDataEstrazioneDdtFor.getName()%>" size="<%=YDtsxRmOdaDDTRuDataEstrazioneDdtFor.getSize()%>" type="text"><% 
  YDtsxRmOdaDDTRuDataEstrazioneDdtFor.write(out); 
%>
</td>
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
  errorList.setParent(YDtsxRmOdaDDTRuForm); 
  errorList.write(out); 
%>
<!--<span class="errorlist"></span>--></td>
			</tr>
		</table>
	<%
  YDtsxRmOdaDDTRuForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = YDtsxRmOdaDDTRuForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", YDtsxRmOdaDDTRuBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>


<%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              YDtsxRmOdaDDTRuForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, YDtsxRmOdaDDTRuBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, YDtsxRmOdaDDTRuBODC.getErrorList().getErrors()); 
           if(YDtsxRmOdaDDTRuBODC.getConflict() != null) 
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
     if(YDtsxRmOdaDDTRuBODC != null && !YDtsxRmOdaDDTRuBODC.close(false)) 
        errors.addAll(0, YDtsxRmOdaDDTRuBODC.getErrorList().getErrors()); 
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
     String errorPage = YDtsxRmOdaDDTRuForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", YDtsxRmOdaDDTRuBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = YDtsxRmOdaDDTRuForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>
</html>
