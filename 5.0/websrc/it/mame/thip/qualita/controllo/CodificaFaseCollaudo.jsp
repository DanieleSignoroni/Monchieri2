<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
                      "file:///K:/Thip/5.0.0/websrcsvil/dtd/xhtml1-transitional.dtd">
<html>
<!-- WIZGEN Therm 2.0.0 as Form - multiBrowserGen = true -->
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
  BODataCollector CodificaFaseCollaudoBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm CodificaFaseCollaudoForm =  
     new com.thera.thermfw.web.WebForm(request, response, "CodificaFaseCollaudoForm", "CodificaFaseCollaudo", null, "com.thera.thermfw.web.servlet.FormActionAdapter", false, false, true, true, true, true, null, 1, false, null); 
  CodificaFaseCollaudoForm.setServletEnvironment(se); 
  CodificaFaseCollaudoForm.setJSTypeList(jsList); 
  CodificaFaseCollaudoForm.setHeader("it.thera.thip.cs.Header.jsp"); 
  CodificaFaseCollaudoForm.setFooter("it.thera.thip.cs.Footer.jsp"); 
  CodificaFaseCollaudoForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = CodificaFaseCollaudoForm.getMode(); 
  String key = CodificaFaseCollaudoForm.getKey(); 
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
        CodificaFaseCollaudoForm.outTraceInfo(getClass().getName()); 
        String collectorName = CodificaFaseCollaudoForm.findBODataCollectorName(); 
                CodificaFaseCollaudoBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (CodificaFaseCollaudoBODC instanceof WebDataCollector) 
            ((WebDataCollector)CodificaFaseCollaudoBODC).setServletEnvironment(se); 
        CodificaFaseCollaudoBODC.initialize("CodificaFaseCollaudo", true, 1); 
        CodificaFaseCollaudoForm.setBODataCollector(CodificaFaseCollaudoBODC); 
        int rcBODC = CodificaFaseCollaudoForm.initSecurityServices(); 
        mode = CodificaFaseCollaudoForm.getMode(); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           CodificaFaseCollaudoForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = CodificaFaseCollaudoBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              CodificaFaseCollaudoForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 

<% 
  WebMenuBar menuBar = new com.thera.thermfw.web.WebMenuBar("HM_Array1", "150", "#000000","#000000","#A5B6CE","#E4EAEF","#FFFFFF","#000000"); 
  menuBar.setParent(CodificaFaseCollaudoForm); 
   request.setAttribute("menuBar", menuBar); 
%> 
<jsp:include page="/it/thera/thip/cs/defObjMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="menuBar"/> 
</jsp:include> 
<% 
  menuBar.write(out); 
  menuBar.writeChildren(out); 
%> 
<% 
  WebToolBar myToolBarTB = new com.thera.thermfw.web.WebToolBar("myToolBar", "24", "24", "16", "16", "#f7fbfd","#C8D6E1"); 
  myToolBarTB.setParent(CodificaFaseCollaudoForm); 
   request.setAttribute("toolBar", myToolBarTB); 
%> 
<jsp:include page="/it/thera/thip/cs/defObjMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="toolBar"/> 
</jsp:include> 
<% 
   myToolBarTB.write(out); 
%> 
</head>

<body bottommargin="0" leftmargin="0" onbeforeunload="<%=CodificaFaseCollaudoForm.getBodyOnBeforeUnload()%>" onload="<%=CodificaFaseCollaudoForm.getBodyOnLoad()%>" onunload="<%=CodificaFaseCollaudoForm.getBodyOnUnload()%>" rightmargin="0" topmargin="0"><%
   CodificaFaseCollaudoForm.writeBodyStartElements(out); 
%> 


<% 
  WebLink link_0 =  
   new com.thera.thermfw.web.WebLink(); 
 link_0.setHttpServletRequest(request); 
 link_0.setHRefAttribute("it/thera/thip/cs/form.css"); 
 link_0.setRelAttribute("STYLESHEET"); 
 link_0.setTypeAttribute("text/css"); 
  link_0.write(out); 
%>
<!--<LINK href="it/thera/thip/cs/form.css" rel="STYLESHEET" type="text/css">-->

<table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = CodificaFaseCollaudoForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", CodificaFaseCollaudoBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=CodificaFaseCollaudoForm.getServlet()%>" method="post" name="form" style="height:100%"><%
  CodificaFaseCollaudoForm.writeFormStartElements(out); 
%>


<table cellpadding="2" cellspacing="0" height="100%" id="emptyborder" width="100%">
  <tr>
    <td style="height:0">
      <% menuBar.writeElements(out); %> 

    </td>
  </tr>
  <tr>
    <td style="height:0">
      <% myToolBarTB.writeChildren(out); %> 

    </td>
  </tr>
  <tr>
    <td height="100%">
	    <!--<span class="tabbed" id="mytabbed">-->
<table width="100%" height="100%" cellpadding="0" cellspacing="0" style="padding-right:1px">
   <tr valign="top">
     <td><% 
  WebTabbed mytabbed = new com.thera.thermfw.web.WebTabbed("mytabbed", "100%", "100%"); 
  mytabbed.setParent(CodificaFaseCollaudoForm); 
 mytabbed.addTab("tab1", "it.thera.thip.cs.resources.Cs", "DatiGenerali", "CodificaFaseCollaudo", null, null, null, null); 
 mytabbed.addTab("tab2", "it.thera.thip.cs.resources.Cs", "DescrizioniNLS", "CodificaFaseCollaudo", null, null, null, null); 
  mytabbed.write(out); 
%>

     </td>
   </tr>
   <tr>
     <td height="100%"><div class="tabbed_pagine" id="tabbedPagine" style="position: relative; width: 100%; height: 100%;">
  			<div class="tabbed_page" id="<%=mytabbed.getTabPageId("tab1")%>" style="width:100%;height:100%;overflow:auto;"><% mytabbed.startTab("tab1"); %>
				<table>
					<tr>
	            		<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "CodificaFaseCollaudo", "Codice", null); 
   label.setParent(CodificaFaseCollaudoForm); 
%><label class="<%=label.getClassType()%>" for="Codice"><%label.write(out);%></label><%}%></td>
						<td><% 
  WebTextInput CodificaFaseCollaudoCodice =  
     new com.thera.thermfw.web.WebTextInput("CodificaFaseCollaudo", "Codice"); 
  CodificaFaseCollaudoCodice.setParent(CodificaFaseCollaudoForm); 
%>
<input class="<%=CodificaFaseCollaudoCodice.getClassType()%>" id="<%=CodificaFaseCollaudoCodice.getId()%>" maxlength="<%=CodificaFaseCollaudoCodice.getMaxLength()%>" name="<%=CodificaFaseCollaudoCodice.getName()%>" size="<%=CodificaFaseCollaudoCodice.getSize()%>"><% 
  CodificaFaseCollaudoCodice.write(out); 
%>
</td>
					</tr>
					<tr>
	            		<td><% 
   request.setAttribute("parentForm", CodificaFaseCollaudoForm); 
   String CDForDescrizione$it$thera$thip$cs$Descrizione$jsp = "Descrizione"; 
%>
<jsp:include page="/it/thera/thip/cs/Descrizione.jsp" flush="true"> 
<jsp:param name="CDName" value="<%=CDForDescrizione$it$thera$thip$cs$Descrizione$jsp%>"/> 
</jsp:include> 
<!--<span class="subform" id="Descrizione"></span>--></td>
					</tr>
					<tr>
	            		<td><% 
   request.setAttribute("parentForm", CodificaFaseCollaudoForm); 
   String CDForDatiComuniEstesi$it$thera$thip$cs$DatiComuniEstesi$jsp = "DatiComuniEstesi"; 
%>
<jsp:include page="/it/thera/thip/cs/DatiComuniEstesi.jsp" flush="true"> 
<jsp:param name="CDName" value="<%=CDForDatiComuniEstesi$it$thera$thip$cs$DatiComuniEstesi$jsp%>"/> 
</jsp:include> 
<!--<span class="subform" id="DatiComuniEstesi"></span>--></td>
					</tr>
				</table>								
			<% mytabbed.endTab(); %> 
</div>		
	  		<div class="tabbed_page" id="<%=mytabbed.getTabPageId("tab2")%>" style="width:100%;height:100%;overflow:auto;"><% mytabbed.startTab("tab2"); %>
				<table height="100%" width="100%">
				    <tr><td valign="top">
						<% 
   request.setAttribute("parentForm", CodificaFaseCollaudoForm); 
   String CDForDescrizione$it$thera$thip$cs$DescrizioneInLingua$jsp = "Descrizione"; 
%>
<jsp:include page="/it/thera/thip/cs/DescrizioneInLingua.jsp" flush="true"> 
<jsp:param name="CDName" value="<%=CDForDescrizione$it$thera$thip$cs$DescrizioneInLingua$jsp%>"/> 
</jsp:include> 
<!--<span class="subform" id="DescrizioneNLS"></span>-->
					</td></tr>
				</table>
		    <% mytabbed.endTab(); %> 
</div>

	    </div><% mytabbed.endTabbed();%> 

     </td>
   </tr>
</table><!--</span>-->											
    </td>
  </tr>
  <tr>
    <td style="height:0"><% 
  WebErrorList errorList = new com.thera.thermfw.web.WebErrorList(); 
  errorList.setParent(CodificaFaseCollaudoForm); 
  errorList.write(out); 
%>
<!--<span class="errorlist"></span>--></td>
  </tr>
</table>

<%
  CodificaFaseCollaudoForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = CodificaFaseCollaudoForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", CodificaFaseCollaudoBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>


<%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              CodificaFaseCollaudoForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, CodificaFaseCollaudoBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, CodificaFaseCollaudoBODC.getErrorList().getErrors()); 
           if(CodificaFaseCollaudoBODC.getConflict() != null) 
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
     if(CodificaFaseCollaudoBODC != null && !CodificaFaseCollaudoBODC.close(false)) 
        errors.addAll(0, CodificaFaseCollaudoBODC.getErrorList().getErrors()); 
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
     String errorPage = CodificaFaseCollaudoForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", CodificaFaseCollaudoBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = CodificaFaseCollaudoForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>

</html>
