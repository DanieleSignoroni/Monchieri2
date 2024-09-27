<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
                      "file:///C:\Documenti\_Panthera\Monchieri\Sorgenti\Panthera\websrc\dtd/xhtml1-transitional.dtd">
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
  BODataCollector CodificaCarCollaudoBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm CodificaCarCollaudoForm =  
     new com.thera.thermfw.web.WebForm(request, response, "CodificaCarCollaudoForm", "CodificaCarCollaudo", null, "com.thera.thermfw.web.servlet.FormActionAdapter", false, false, true, true, true, true, null, 1, false, null); 
  CodificaCarCollaudoForm.setServletEnvironment(se); 
  CodificaCarCollaudoForm.setJSTypeList(jsList); 
  CodificaCarCollaudoForm.setHeader("it.thera.thip.cs.Header.jsp"); 
  CodificaCarCollaudoForm.setFooter("it.thera.thip.cs.Footer.jsp"); 
  CodificaCarCollaudoForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = CodificaCarCollaudoForm.getMode(); 
  String key = CodificaCarCollaudoForm.getKey(); 
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
        CodificaCarCollaudoForm.outTraceInfo(getClass().getName()); 
        String collectorName = CodificaCarCollaudoForm.findBODataCollectorName(); 
                CodificaCarCollaudoBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (CodificaCarCollaudoBODC instanceof WebDataCollector) 
            ((WebDataCollector)CodificaCarCollaudoBODC).setServletEnvironment(se); 
        CodificaCarCollaudoBODC.initialize("CodificaCarCollaudo", true, 1); 
        CodificaCarCollaudoForm.setBODataCollector(CodificaCarCollaudoBODC); 
        int rcBODC = CodificaCarCollaudoForm.initSecurityServices(); 
        mode = CodificaCarCollaudoForm.getMode(); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           CodificaCarCollaudoForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = CodificaCarCollaudoBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              CodificaCarCollaudoForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 

<% 
  WebMenuBar menuBar = new com.thera.thermfw.web.WebMenuBar("HM_Array1", "150", "#000000","#000000","#A5B6CE","#E4EAEF","#FFFFFF","#000000"); 
  menuBar.setParent(CodificaCarCollaudoForm); 
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
  myToolBarTB.setParent(CodificaCarCollaudoForm); 
   request.setAttribute("toolBar", myToolBarTB); 
%> 
<jsp:include page="/it/thera/thip/cs/defObjMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="toolBar"/> 
</jsp:include> 
<% 
   myToolBarTB.write(out); 
%> 
</head>

<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0" onload="<%=CodificaCarCollaudoForm.getBodyOnLoad()%>" onunload="<%=CodificaCarCollaudoForm.getBodyOnUnload()%>" onbeforeunload="<%=CodificaCarCollaudoForm.getBodyOnBeforeUnload()%>"><%
   CodificaCarCollaudoForm.writeBodyStartElements(out); 
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
<% String hdr = CodificaCarCollaudoForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", CodificaCarCollaudoBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form name="form" method="post" style="height:100%" action="<%=CodificaCarCollaudoForm.getServlet()%>"><%
  CodificaCarCollaudoForm.writeFormStartElements(out); 
%>


<table height="100%" width="100%" cellspacing="0" cellpadding="2" id="emptyborder">
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
  mytabbed.setParent(CodificaCarCollaudoForm); 
 mytabbed.addTab("tab1", "it.thera.thip.cs.resources.Cs", "DatiGenerali", "CodificaCarCollaudo", null, null, null, null); 
 mytabbed.addTab("tab2", "it.thera.thip.cs.resources.Cs", "DescrizioniNLS", "CodificaCarCollaudo", null, null, null, null); 
  mytabbed.write(out); 
%>

     </td>
   </tr>
   <tr>
     <td height="100%"><div class="tabbed_pagine" id="tabbedPagine" style="position: relative; width: 100%; height: 100%;">
  			<div class="tabbed_page" id="<%=mytabbed.getTabPageId("tab1")%>" style="width:100%;height:100%;overflow:auto;"><% mytabbed.startTab("tab1"); %>
				<table>
					<tr>
	            		<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "CodificaCarCollaudo", "Codice", null); 
   label.setParent(CodificaCarCollaudoForm); 
%><label for="Codice" class="<%=label.getClassType()%>"><%label.write(out);%></label><%}%></td>
						<td><% 
  WebTextInput CodificaCarCollaudoCodice =  
     new com.thera.thermfw.web.WebTextInput("CodificaCarCollaudo", "Codice"); 
  CodificaCarCollaudoCodice.setParent(CodificaCarCollaudoForm); 
%>
<input size="<%=CodificaCarCollaudoCodice.getSize()%>" id="<%=CodificaCarCollaudoCodice.getId()%>" name="<%=CodificaCarCollaudoCodice.getName()%>" maxlength="<%=CodificaCarCollaudoCodice.getMaxLength()%>" class="<%=CodificaCarCollaudoCodice.getClassType()%>"><% 
  CodificaCarCollaudoCodice.write(out); 
%>
</td>
					</tr>
					<tr>
	            		<td><% 
   request.setAttribute("parentForm", CodificaCarCollaudoForm); 
   String CDForDescrizione$it$thera$thip$cs$Descrizione$jsp = "Descrizione"; 
%>
<jsp:include page="/it/thera/thip/cs/Descrizione.jsp" flush="true"> 
<jsp:param name="CDName" value="<%=CDForDescrizione$it$thera$thip$cs$Descrizione$jsp%>"/> 
</jsp:include> 
<!--<span class="subform" id="Descrizione"></span>--></td>
					</tr>
					<tr>
	            		<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "CodificaCarCollaudo", "Formula", null); 
   label.setParent(CodificaCarCollaudoForm); 
%><label for="Formula" class="<%=label.getClassType()%>"><%label.write(out);%></label><%}%></td>
						<td><% 
  WebTextInput CodificaCarCollaudoFormula =  
     new com.thera.thermfw.web.WebTextInput("CodificaCarCollaudo", "Formula"); 
  CodificaCarCollaudoFormula.setParent(CodificaCarCollaudoForm); 
%>
<input size="<%=CodificaCarCollaudoFormula.getSize()%>" id="<%=CodificaCarCollaudoFormula.getId()%>" name="<%=CodificaCarCollaudoFormula.getName()%>" maxlength="<%=CodificaCarCollaudoFormula.getMaxLength()%>" class="<%=CodificaCarCollaudoFormula.getClassType()%>"><% 
  CodificaCarCollaudoFormula.write(out); 
%>
</td>
					</tr>
					<tr>
	            		<td><% 
   request.setAttribute("parentForm", CodificaCarCollaudoForm); 
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
				<table width="100%" height="100%">
				    <tr><td valign="top">
						<% 
   request.setAttribute("parentForm", CodificaCarCollaudoForm); 
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
  errorList.setParent(CodificaCarCollaudoForm); 
  errorList.write(out); 
%>
<!--<span class="errorlist"></span>--></td>
  </tr>
</table>

<%
  CodificaCarCollaudoForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = CodificaCarCollaudoForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", CodificaCarCollaudoBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>


<%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              CodificaCarCollaudoForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, CodificaCarCollaudoBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, CodificaCarCollaudoBODC.getErrorList().getErrors()); 
           if(CodificaCarCollaudoBODC.getConflict() != null) 
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
     if(CodificaCarCollaudoBODC != null && !CodificaCarCollaudoBODC.close(false)) 
        errors.addAll(0, CodificaCarCollaudoBODC.getErrorList().getErrors()); 
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
     String errorPage = CodificaCarCollaudoForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", CodificaCarCollaudoBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = CodificaCarCollaudoForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>

</html>
