<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
                      "file:///K:/Thip/4.5.0/websrcsvil/dtd/xhtml1-transitional.dtd">
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
  BODataCollector SchedaLottoGUIBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm SchedaLottoGUIForm =  
     new com.thera.thermfw.web.WebForm(request, response, "SchedaLottoGUIForm", "SchedaLottoGUI", "Arial,10", "it.mame.thip.magazzino.generalemag.web.SchedaLottoFormActionAdapter", false, false, true, true, true, true, null, 1, true, "it/mame/thip/magazzino/generalemag/SchedaLotto.js"); 
  SchedaLottoGUIForm.setServletEnvironment(se); 
  SchedaLottoGUIForm.setJSTypeList(jsList); 
  SchedaLottoGUIForm.setHeader("it.thera.thip.cs.Header.jsp"); 
  SchedaLottoGUIForm.setFooter("it.thera.thip.cs.Footer.jsp"); 
  SchedaLottoGUIForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = SchedaLottoGUIForm.getMode(); 
  String key = SchedaLottoGUIForm.getKey(); 
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
        SchedaLottoGUIForm.outTraceInfo(getClass().getName()); 
        String collectorName = SchedaLottoGUIForm.findBODataCollectorName(); 
                SchedaLottoGUIBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (SchedaLottoGUIBODC instanceof WebDataCollector) 
            ((WebDataCollector)SchedaLottoGUIBODC).setServletEnvironment(se); 
        SchedaLottoGUIBODC.initialize("SchedaLottoGUI", true, 1); 
        SchedaLottoGUIForm.setBODataCollector(SchedaLottoGUIBODC); 
        int rcBODC = SchedaLottoGUIForm.initSecurityServices(); 
        mode = SchedaLottoGUIForm.getMode(); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           SchedaLottoGUIForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = SchedaLottoGUIBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              SchedaLottoGUIForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 

	<title>SchedaLotto</title>
<% 
  WebMenuBar menuBar = new com.thera.thermfw.web.WebMenuBar("HM_Array1", "150", "#000000","#000000","#A5B6CE","#E4EAEF","#FFFFFF","#000000"); 
  menuBar.setParent(SchedaLottoGUIForm); 
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
  myToolBarTB.setParent(SchedaLottoGUIForm); 
   request.setAttribute("toolBar", myToolBarTB); 
%> 
<jsp:include page="/it/thera/thip/cs/defObjMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="toolBar"/> 
</jsp:include> 
<% 
   myToolBarTB.write(out); 
%> 
</head>
<body bottommargin="0" leftmargin="0" onbeforeunload="<%=SchedaLottoGUIForm.getBodyOnBeforeUnload()%>" onload="<%=SchedaLottoGUIForm.getBodyOnLoad()%>" onunload="<%=SchedaLottoGUIForm.getBodyOnUnload()%>" rightmargin="0" topmargin="0"><%
   SchedaLottoGUIForm.writeBodyStartElements(out); 
%> 

	<table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = SchedaLottoGUIForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", SchedaLottoGUIBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=SchedaLottoGUIForm.getServlet()%>" method="post" name="SchedaLottoForm" style="height:100%"><%
  SchedaLottoGUIForm.writeFormStartElements(out); 
%>

		<table cellpadding="1" cellspacing="1" height="100%" width="100%">
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
  mytabbed.setParent(SchedaLottoGUIForm); 
 mytabbed.addTab("tab1", "it.mame.thip.magazzino.generalemag.resources.SchedaLotto", "LottoTab", "SchedaLottoGUI", null, null, null, null); 
 mytabbed.addTab("tab2", "it.mame.thip.magazzino.generalemag.resources.SchedaLotto", "PrenotazioneTab", "SchedaLottoGUI", null, null, null, null); 
 mytabbed.addTab("tab3", "it.mame.thip.magazzino.generalemag.resources.SchedaLotto", "AnalisiChimicaTab", "SchedaLottoGUI", null, null, null, null); 
  mytabbed.write(out); 
%>

     </td>
   </tr>
   <tr>
     <td height="100%"><div class="tabbed_pagine" id="tabbedPagine" style="position: relative; width: 100%; height: 100%;">
						<div class="tabbed_page" id="<%=mytabbed.getTabPageId("tab1")%>" style="width:100%;height:100%;overflow:auto;"><% mytabbed.startTab("tab1"); %>
							<table>
								<tr>
									<td>
										<table cellpadding="1" cellspacing="1">
											<tr>
												<td><% 
  WebTextInput SchedaLottoGUICodiceAzienda =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "CodiceAzienda"); 
  SchedaLottoGUICodiceAzienda.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUICodiceAzienda.getClassType()%>" id="<%=SchedaLottoGUICodiceAzienda.getId()%>" maxlength="<%=SchedaLottoGUICodiceAzienda.getMaxLength()%>" name="<%=SchedaLottoGUICodiceAzienda.getName()%>" size="<%=SchedaLottoGUICodiceAzienda.getSize()%>" type="hidden"><% 
  SchedaLottoGUICodiceAzienda.write(out); 
%>

												<% 
  WebTextInput SchedaLottoGUIMagazzino =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "Magazzino"); 
  SchedaLottoGUIMagazzino.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUIMagazzino.getClassType()%>" id="<%=SchedaLottoGUIMagazzino.getId()%>" maxlength="<%=SchedaLottoGUIMagazzino.getMaxLength()%>" name="<%=SchedaLottoGUIMagazzino.getName()%>" size="<%=SchedaLottoGUIMagazzino.getSize()%>" type="hidden"><% 
  SchedaLottoGUIMagazzino.write(out); 
%>

												<% 
  WebTextInput SchedaLottoGUIVersione =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "Versione"); 
  SchedaLottoGUIVersione.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUIVersione.getClassType()%>" id="<%=SchedaLottoGUIVersione.getId()%>" maxlength="<%=SchedaLottoGUIVersione.getMaxLength()%>" name="<%=SchedaLottoGUIVersione.getName()%>" size="<%=SchedaLottoGUIVersione.getSize()%>" type="hidden"><% 
  SchedaLottoGUIVersione.write(out); 
%>

												<% 
  WebTextInput SchedaLottoGUIConfigurazione =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "Configurazione"); 
  SchedaLottoGUIConfigurazione.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUIConfigurazione.getClassType()%>" id="<%=SchedaLottoGUIConfigurazione.getId()%>" maxlength="<%=SchedaLottoGUIConfigurazione.getMaxLength()%>" name="<%=SchedaLottoGUIConfigurazione.getName()%>" size="<%=SchedaLottoGUIConfigurazione.getSize()%>" type="hidden"><% 
  SchedaLottoGUIConfigurazione.write(out); 
%>
</td>
											</tr>
											<tr>
												<td nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "SchedaLottoGUI", "CodiceArticolo", null); 
   label.setParent(SchedaLottoGUIForm); 
%><label class="<%=label.getClassType()%>" for="CodiceArticolo"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebTextInput SchedaLottoGUICodiceArticolo =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "CodiceArticolo"); 
  SchedaLottoGUICodiceArticolo.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUICodiceArticolo.getClassType()%>" id="<%=SchedaLottoGUICodiceArticolo.getId()%>" maxlength="<%=SchedaLottoGUICodiceArticolo.getMaxLength()%>" name="<%=SchedaLottoGUICodiceArticolo.getName()%>" size="<%=SchedaLottoGUICodiceArticolo.getSize()%>" type="text"><% 
  SchedaLottoGUICodiceArticolo.write(out); 
%>

												</td>
											</tr>
											<!--tr >             <td nowrap="true">              <label for="Configurazione">Configurazione</label>             </td>             <td>              <input type="text" id="Configurazione" name="Configurazione"/>             </td>            </tr>            <tr>             <td nowrap="true">              <label for="Versione">Versione</label>             </td>             <td>              <input type="text" id="Versione" name="Versione"/>             </td>            </tr-->
											<tr>
												<td nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "SchedaLottoGUI", "CodiceLotto", null); 
   label.setParent(SchedaLottoGUIForm); 
%><label class="<%=label.getClassType()%>" for="CodiceLotto"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebTextInput SchedaLottoGUICodiceLotto =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "CodiceLotto"); 
  SchedaLottoGUICodiceLotto.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUICodiceLotto.getClassType()%>" id="<%=SchedaLottoGUICodiceLotto.getId()%>" maxlength="<%=SchedaLottoGUICodiceLotto.getMaxLength()%>" name="<%=SchedaLottoGUICodiceLotto.getName()%>" size="<%=SchedaLottoGUICodiceLotto.getSize()%>" type="text"><% 
  SchedaLottoGUICodiceLotto.write(out); 
%>

												</td>
											</tr>
											<!--tr>             <td nowrap="true">              <label for="Magazzino">Magazzino</label>             </td>             <td>              <input type="text" id="Magazzino" name="Magazzino"/>             </td>            </tr-->
											<tr>
												<td nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "SchedaLottoGUI", "Gruppo", null); 
   label.setParent(SchedaLottoGUIForm); 
%><label class="<%=label.getClassType()%>" for="Gruppo"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebTextInput SchedaLottoGUIGruppo =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "Gruppo"); 
  SchedaLottoGUIGruppo.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUIGruppo.getClassType()%>" id="<%=SchedaLottoGUIGruppo.getId()%>" maxlength="<%=SchedaLottoGUIGruppo.getMaxLength()%>" name="<%=SchedaLottoGUIGruppo.getName()%>" size="<%=SchedaLottoGUIGruppo.getSize()%>" type="text"><% 
  SchedaLottoGUIGruppo.write(out); 
%>

												</td>
											</tr>
											<tr>
												<td nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "SchedaLottoGUI", "Tipo", null); 
   label.setParent(SchedaLottoGUIForm); 
%><label class="<%=label.getClassType()%>" for="Tipo"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebTextInput SchedaLottoGUITipo =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "Tipo"); 
  SchedaLottoGUITipo.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUITipo.getClassType()%>" id="<%=SchedaLottoGUITipo.getId()%>" maxlength="<%=SchedaLottoGUITipo.getMaxLength()%>" name="<%=SchedaLottoGUITipo.getName()%>" size="<%=SchedaLottoGUITipo.getSize()%>" type="text"><% 
  SchedaLottoGUITipo.write(out); 
%>

												</td>
											</tr>
											<tr>
												<td nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "SchedaLottoGUI", "Sezione", null); 
   label.setParent(SchedaLottoGUIForm); 
%><label class="<%=label.getClassType()%>" for="Sezione"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebTextInput SchedaLottoGUISezione =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "Sezione"); 
  SchedaLottoGUISezione.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUISezione.getClassType()%>" id="<%=SchedaLottoGUISezione.getId()%>" maxlength="<%=SchedaLottoGUISezione.getMaxLength()%>" name="<%=SchedaLottoGUISezione.getName()%>" size="<%=SchedaLottoGUISezione.getSize()%>" type="text"><% 
  SchedaLottoGUISezione.write(out); 
%>

												</td>
											</tr>
											<tr>
												<td nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "SchedaLottoGUI", "Acciaieria", null); 
   label.setParent(SchedaLottoGUIForm); 
%><label class="<%=label.getClassType()%>" for="Acciaieria"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebTextInput SchedaLottoGUIAcciaieria =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "Acciaieria"); 
  SchedaLottoGUIAcciaieria.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUIAcciaieria.getClassType()%>" id="<%=SchedaLottoGUIAcciaieria.getId()%>" maxlength="<%=SchedaLottoGUIAcciaieria.getMaxLength()%>" name="<%=SchedaLottoGUIAcciaieria.getName()%>" size="<%=SchedaLottoGUIAcciaieria.getSize()%>" type="text"><% 
  SchedaLottoGUIAcciaieria.write(out); 
%>

												</td>
											</tr>
											<tr>
												<td nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "SchedaLottoGUI", "Colata", null); 
   label.setParent(SchedaLottoGUIForm); 
%><label class="<%=label.getClassType()%>" for="Colata"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebTextInput SchedaLottoGUIColata =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "Colata"); 
  SchedaLottoGUIColata.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUIColata.getClassType()%>" id="<%=SchedaLottoGUIColata.getId()%>" maxlength="<%=SchedaLottoGUIColata.getMaxLength()%>" name="<%=SchedaLottoGUIColata.getName()%>" size="<%=SchedaLottoGUIColata.getSize()%>" type="text"><% 
  SchedaLottoGUIColata.write(out); 
%>

												</td>
											</tr>
											<tr>
												<td nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "SchedaLottoGUI", "Giacenza", null); 
   label.setParent(SchedaLottoGUIForm); 
%><label class="<%=label.getClassType()%>" for="Giacenza"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebTextInput SchedaLottoGUIGiacenza =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "Giacenza"); 
  SchedaLottoGUIGiacenza.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUIGiacenza.getClassType()%>" id="<%=SchedaLottoGUIGiacenza.getId()%>" maxlength="<%=SchedaLottoGUIGiacenza.getMaxLength()%>" name="<%=SchedaLottoGUIGiacenza.getName()%>" size="<%=SchedaLottoGUIGiacenza.getSize()%>" type="text"><% 
  SchedaLottoGUIGiacenza.write(out); 
%>

													<input id="MagVersConf" name="MagVersConf" size="32" type="text">
												</td>
											</tr>
											<tr>
												<td nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "SchedaLottoGUI", "GiacenzaSec", null); 
   label.setParent(SchedaLottoGUIForm); 
%><label class="<%=label.getClassType()%>" for="GiacenzaSec"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebTextInput SchedaLottoGUIGiacenzaSec =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "GiacenzaSec"); 
  SchedaLottoGUIGiacenzaSec.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUIGiacenzaSec.getClassType()%>" id="<%=SchedaLottoGUIGiacenzaSec.getId()%>" maxlength="<%=SchedaLottoGUIGiacenzaSec.getMaxLength()%>" name="<%=SchedaLottoGUIGiacenzaSec.getName()%>" size="<%=SchedaLottoGUIGiacenzaSec.getSize()%>" type="text"><% 
  SchedaLottoGUIGiacenzaSec.write(out); 
%>

												</td>
											</tr>
											<tr>
												<td nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "SchedaLottoGUI", "Fornitore", null); 
   label.setParent(SchedaLottoGUIForm); 
%><label class="<%=label.getClassType()%>" for="Fornitore"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebTextInput SchedaLottoGUIFornitore =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "Fornitore"); 
  SchedaLottoGUIFornitore.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUIFornitore.getClassType()%>" id="<%=SchedaLottoGUIFornitore.getId()%>" maxlength="<%=SchedaLottoGUIFornitore.getMaxLength()%>" name="<%=SchedaLottoGUIFornitore.getName()%>" size="<%=SchedaLottoGUIFornitore.getSize()%>" type="text"><% 
  SchedaLottoGUIFornitore.write(out); 
%>
&nbsp;<% 
  WebTextInput SchedaLottoGUIDscFornitore =  
     new com.thera.thermfw.web.WebTextInput("SchedaLottoGUI", "DscFornitore"); 
  SchedaLottoGUIDscFornitore.setParent(SchedaLottoGUIForm); 
%>
<input class="<%=SchedaLottoGUIDscFornitore.getClassType()%>" id="<%=SchedaLottoGUIDscFornitore.getId()%>" maxlength="<%=SchedaLottoGUIDscFornitore.getMaxLength()%>" name="<%=SchedaLottoGUIDscFornitore.getName()%>" size="<%=SchedaLottoGUIDscFornitore.getSize()%>" type="text"><% 
  SchedaLottoGUIDscFornitore.write(out); 
%>

												</td>
											</tr>
                                    		<tr>
                                    			<td colspan="2">
                                    				<% 
  WebCheckBox SchedaLottoGUINucleare =  
     new com.thera.thermfw.web.WebCheckBox("SchedaLottoGUI", "Nucleare"); 
  SchedaLottoGUINucleare.setParent(SchedaLottoGUIForm); 
%>
<input id="<%=SchedaLottoGUINucleare.getId()%>" name="<%=SchedaLottoGUINucleare.getName()%>" type="Checkbox" value="Y"><%
  SchedaLottoGUINucleare.write(out); 
%>

                                    			</td>
                                    		</tr>
											<tr>
												<td nowrap>
													<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "SchedaLottoGUI", "Nota", null); 
   label.setParent(SchedaLottoGUIForm); 
%><label class="<%=label.getClassType()%>" for="Nota"><%label.write(out);%></label><%}%>
												</td>
												<td>
													<% 
  WebTextInput SchedaLottoGUINota =  
     new com.thera.thermfw.web.WebTextArea("SchedaLottoGUI", "Nota"); 
  SchedaLottoGUINota.setParent(SchedaLottoGUIForm); 
%>
<textarea class="<%=SchedaLottoGUINota.getClassType()%>" cols="70" id="<%=SchedaLottoGUINota.getId()%>" maxlength="<%=SchedaLottoGUINota.getMaxLength()%>" name="<%=SchedaLottoGUINota.getName()%>" rows="3" size="<%=SchedaLottoGUINota.getSize()%>"></textarea><% 
  SchedaLottoGUINota.write(out); 
%>

												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						<% mytabbed.endTab(); %> 
</div>

						<div class="tabbed_page" id="<%=mytabbed.getTabPageId("tab2")%>" style="width:100%;height:100%;overflow:auto;"><% mytabbed.startTab("tab2"); %>
							<table cellpadding="1" cellspacing="1">
								<tr>
									<td>
										<!--<span class="editgrid" id="PrenotazioneGrid">--><% 
  WebEditGrid SchedaLottoGUILottiPrenotazione =  
     new com.thera.thermfw.web.WebEditGrid("SchedaLottoGUI", "LottiPrenotazione", 7, new String[]{"IdCommessa", "Quantita", "Data", "Eseguita", "TipoPrenotazione", "DatiComuni.IdUtenteAgg"}, 1, null, null,false,"com.thera.thermfw.web.servlet.GridActionAdapterForIndependentRow"); 
 SchedaLottoGUILottiPrenotazione.setParent(SchedaLottoGUIForm); 
 SchedaLottoGUILottiPrenotazione.setNoControlRowKeys(true); 
 SchedaLottoGUILottiPrenotazione.write(out); 
%>
<BR><% 
   request.setAttribute("parentForm", SchedaLottoGUIForm); 
   String CDForLottiPrenotazione = "LottiPrenotazione"; 
%>
<jsp:include page="/it/mame/thip/magazzino/generalemag/LottoPrenotazione.jsp" flush="true"> 
<jsp:param name="EditGridCDName" value="<%=CDForLottiPrenotazione%>"/> 
<jsp:param name="Mode" value="NEW"/> 
</jsp:include> 
<!--</span>-->
									</td>
								</tr>
							</table>
						<% mytabbed.endTab(); %> 
</div>

						<div class="tabbed_page" id="<%=mytabbed.getTabPageId("tab3")%>" style="width:100%;height:100%;overflow:auto;"><% mytabbed.startTab("tab3"); %>
							<table cellpadding="1" cellspacing="1">
								<tr>
									<td>
										<!--<span class="editgrid" id="AnalisiChimicaGrid">--><% 
  WebEditGrid SchedaLottoGUIAnalisiChimica =  
     new com.thera.thermfw.web.WebEditGrid("SchedaLottoGUI", "AnalisiChimica", 15, new String[]{"Caratteristica", "LimInfTolleranza", "LimSupTolleranza", "ValNominale", "Valore01", "Esito01", "Valore02", "Esito02"}, 0, null, null,false,"com.thera.thermfw.web.servlet.GridActionAdapterForIndependentRow"); 
 SchedaLottoGUIAnalisiChimica.setParent(SchedaLottoGUIForm); 
 SchedaLottoGUIAnalisiChimica.setNoControlRowKeys(true); 
 SchedaLottoGUIAnalisiChimica.excludeAction("UpdateRow"); 
 SchedaLottoGUIAnalisiChimica.excludeAction("CopyRow"); 
 SchedaLottoGUIAnalisiChimica.excludeAction("NewRow"); 
 SchedaLottoGUIAnalisiChimica.excludeAction("ShowRow"); 
 SchedaLottoGUIAnalisiChimica.excludeAction("DeleteRow"); 
 SchedaLottoGUIAnalisiChimica.write(out); 
%>
<!--</span>-->
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
			<tr valign="top">
				<td style="height:0">
					<% 
  WebErrorList errorList = new com.thera.thermfw.web.WebErrorList(); 
  errorList.setParent(SchedaLottoGUIForm); 
  errorList.write(out); 
%>
<!--<span class="errorlist"></span>-->
				</td>
			</tr>
		</table>
	<%
  SchedaLottoGUIForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = SchedaLottoGUIForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", SchedaLottoGUIBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>


<%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              SchedaLottoGUIForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, SchedaLottoGUIBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, SchedaLottoGUIBODC.getErrorList().getErrors()); 
           if(SchedaLottoGUIBODC.getConflict() != null) 
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
     if(SchedaLottoGUIBODC != null && !SchedaLottoGUIBODC.close(false)) 
        errors.addAll(0, SchedaLottoGUIBODC.getErrorList().getErrors()); 
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
     String errorPage = SchedaLottoGUIForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", SchedaLottoGUIBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = SchedaLottoGUIForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>
</html>
