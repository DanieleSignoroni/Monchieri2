<!-- WIZGEN Therm 2.0.0 as Form riga interna - multiBrowserGen = true -->
<% 
  if(false) 
  { 
%> 
<head><% 
  } 
%> 

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
  BODataCollector LottiPrenotazioneBODC = null; 
  WebFormForInternalRowForm LottiPrenotazioneForm =  
     new com.thera.thermfw.web.WebFormForInternalRowForm(request, response, "LottiPrenotazioneForm", "LottiPrenotazione", "Arial,10", "com.thera.thermfw.web.servlet.FormActionAdapter", false, false, true, true, true, true, null, 1); 
  int mode = LottiPrenotazioneForm.getMode(); 
  String key = LottiPrenotazioneForm.getKey(); 
  String errorMessage; 
  boolean requestIsValid = false; 
  boolean leftIsKey = false; 
  String leftClass = ""; 
  try 
  {
     se.initialize(request, response); 
     if(se.begin()) 
     { 
        LottiPrenotazioneForm.outTraceInfo(getClass().getName()); 
        ClassADCollection globCadc = LottiPrenotazioneForm.getClassADCollection(); 
        requestIsValid = true; 
        LottiPrenotazioneForm.write(out); 
        String collectorName = LottiPrenotazioneForm.findBODataCollectorName(); 
				 LottiPrenotazioneBODC = (BODataCollector)Factory.createObject(collectorName); 
        LottiPrenotazioneBODC.initialize("LottiPrenotazione", true, 1); 
        LottiPrenotazioneForm.setBODataCollector(LottiPrenotazioneBODC); 
        WebForm parentForm = (WebForm)request.getAttribute("parentForm"); 
        LottiPrenotazioneForm.setJSTypeList(parentForm.getOwnerForm().getJSTypeList()); 
        LottiPrenotazioneForm.setParent(parentForm); 
        LottiPrenotazioneForm.writeHeadElements(out); 
     }
  }
  catch(NamingException e) { 
    errorMessage = e.getMessage(); 
  } 
  catch(SQLException e) { 
     errorMessage = e.getMessage(); 
  } 
  finally 
  { 
     try 
     { 
        se.end(); 
     } 
     catch(IllegalArgumentException e) { 
        e.printStackTrace(); 
     } 
     catch(SQLException e) { 
        e.printStackTrace(); 
     } 
  } 
%> 

    <title></title>
    <% 
  WebScript script_0 =  
   new com.thera.thermfw.web.WebScript(); 
 script_0.setRequest(request); 
 script_0.setSrcAttribute("it/mame/thip/magazzino/generalemag/LottoPrenotazione.js"); 
 script_0.setLanguageAttribute("JavaScript1.2"); 
  script_0.write(out); 
%>
<!--<script src="it/mame/thip/magazzino/generalemag/LottoPrenotazione.js" language="JavaScript1.2" type="text/javascript"></script>-->
<% 
  if(false) 
  { 
%> 
</head><% 
  } 
%> 

<% 
  if(false) 
  { 
%> 
<body><% 
  } 
%> 
<%
   LottiPrenotazioneForm.writeBodyStartElements(out); 
%> 

<% 
  if(false) 
  { 
%> 
<form name="LottoPrenotazioneForm"><% 
  } 
%> 
<%
   LottiPrenotazioneForm.writeFormStartElements(out); 
%> 

   <table border="0" width="100%">	
		<tr>
			<td><% 
  WebTextInput LottiPrenotazioneIdAzienda =  
     new com.thera.thermfw.web.WebTextInput("LottiPrenotazione", "IdAzienda"); 
  LottiPrenotazioneIdAzienda.setParent(LottiPrenotazioneForm); 
%>
<input class="<%=LottiPrenotazioneIdAzienda.getClassType()%>" id="<%=LottiPrenotazioneIdAzienda.getId()%>" maxlength="<%=LottiPrenotazioneIdAzienda.getMaxLength()%>" name="<%=LottiPrenotazioneIdAzienda.getName()%>" size="<%=LottiPrenotazioneIdAzienda.getSize()%>" type="hidden"><% 
  LottiPrenotazioneIdAzienda.write(out); 
%>
</td>
		</tr>
		<tr>
			<td><% 
  WebTextInput LottiPrenotazioneIdArticolo =  
     new com.thera.thermfw.web.WebTextInput("LottiPrenotazione", "IdArticolo"); 
  LottiPrenotazioneIdArticolo.setParent(LottiPrenotazioneForm); 
%>
<input class="<%=LottiPrenotazioneIdArticolo.getClassType()%>" id="<%=LottiPrenotazioneIdArticolo.getId()%>" maxlength="<%=LottiPrenotazioneIdArticolo.getMaxLength()%>" name="<%=LottiPrenotazioneIdArticolo.getName()%>" size="<%=LottiPrenotazioneIdArticolo.getSize()%>" type="hidden"><% 
  LottiPrenotazioneIdArticolo.write(out); 
%>
</td>
		</tr>
		<tr>
			<td><% 
  WebTextInput LottiPrenotazioneIdLotto =  
     new com.thera.thermfw.web.WebTextInput("LottiPrenotazione", "IdLotto"); 
  LottiPrenotazioneIdLotto.setParent(LottiPrenotazioneForm); 
%>
<input class="<%=LottiPrenotazioneIdLotto.getClassType()%>" id="<%=LottiPrenotazioneIdLotto.getId()%>" maxlength="<%=LottiPrenotazioneIdLotto.getMaxLength()%>" name="<%=LottiPrenotazioneIdLotto.getName()%>" size="<%=LottiPrenotazioneIdLotto.getSize()%>" type="hidden"><% 
  LottiPrenotazioneIdLotto.write(out); 
%>
</td>
		</tr>
		<tr>
			<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "LottiPrenotazione", "IdCommessa", null); 
   label.setParent(LottiPrenotazioneForm); 
%><label class="<%=label.getClassType()%>" for="Commessa"><%label.write(out);%></label><%}%></td>
			<td><% 
  WebMultiSearchForm LottiPrenotazioneCommessa =  
     new com.thera.thermfw.web.WebMultiSearchForm("LottiPrenotazione", "Commessa", false, false, true, 1, null, null); 
  LottiPrenotazioneCommessa.setParent(LottiPrenotazioneForm); 
  LottiPrenotazioneCommessa.setOnKeyChange("changeIdCommessa();"); 
  LottiPrenotazioneCommessa.write(out); 
%>
<!--<span class="multisearchform" id="Commessa"></span>--></td>
		</tr>
        <tr style="display:none;">
			<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "LottiPrenotazione", "DscCommessa", null); 
   label.setParent(LottiPrenotazioneForm); 
%><label class="<%=label.getClassType()%>" for="DscCommessa"><%label.write(out);%></label><%}%></td>
			<td><% 
  WebTextInput LottiPrenotazioneDscCommessa =  
     new com.thera.thermfw.web.WebTextInput("LottiPrenotazione", "DscCommessa"); 
  LottiPrenotazioneDscCommessa.setOnChange("changeDscCommessa();"); 
  LottiPrenotazioneDscCommessa.setParent(LottiPrenotazioneForm); 
%>
<input class="<%=LottiPrenotazioneDscCommessa.getClassType()%>" id="<%=LottiPrenotazioneDscCommessa.getId()%>" maxlength="<%=LottiPrenotazioneDscCommessa.getMaxLength()%>" name="<%=LottiPrenotazioneDscCommessa.getName()%>" size="<%=LottiPrenotazioneDscCommessa.getSize()%>"><% 
  LottiPrenotazioneDscCommessa.write(out); 
%>
</td>
		</tr>
		<tr>
			<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "LottiPrenotazione", "Quantita", null); 
   label.setParent(LottiPrenotazioneForm); 
%><label class="<%=label.getClassType()%>" for="Quantita"><%label.write(out);%></label><%}%></td>
			<td><% 
  WebTextInput LottiPrenotazioneQuantita =  
     new com.thera.thermfw.web.WebTextInput("LottiPrenotazione", "Quantita"); 
  LottiPrenotazioneQuantita.setParent(LottiPrenotazioneForm); 
%>
<input class="<%=LottiPrenotazioneQuantita.getClassType()%>" id="<%=LottiPrenotazioneQuantita.getId()%>" maxlength="<%=LottiPrenotazioneQuantita.getMaxLength()%>" name="<%=LottiPrenotazioneQuantita.getName()%>" size="<%=LottiPrenotazioneQuantita.getSize()%>"><% 
  LottiPrenotazioneQuantita.write(out); 
%>
</td>
		</tr>
		<tr>
			<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "LottiPrenotazione", "Data", null); 
   label.setParent(LottiPrenotazioneForm); 
%><label class="<%=label.getClassType()%>" for="Data"><%label.write(out);%></label><%}%></td>
			<td><% 
  WebTextInput LottiPrenotazioneData =  
     new com.thera.thermfw.web.WebTextInput("LottiPrenotazione", "Data"); 
  LottiPrenotazioneData.setShowCalendarBtn(true); 
  LottiPrenotazioneData.setParent(LottiPrenotazioneForm); 
%>
<input class="<%=LottiPrenotazioneData.getClassType()%>" id="<%=LottiPrenotazioneData.getId()%>" maxlength="<%=LottiPrenotazioneData.getMaxLength()%>" name="<%=LottiPrenotazioneData.getName()%>" size="<%=LottiPrenotazioneData.getSize()%>"><% 
  LottiPrenotazioneData.write(out); 
%>
</td>
		</tr>
		<tr>
			<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "LottiPrenotazione", "TipoPrenotazione", null); 
   label.setParent(LottiPrenotazioneForm); 
%><label class="<%=label.getClassType()%>" for="TipoPrenotazione"><%label.write(out);%></label><%}%></td>
			<td><% 
  WebComboBox LottiPrenotazioneTipoPrenotazione =  
     new com.thera.thermfw.web.WebComboBox("LottiPrenotazione", "TipoPrenotazione", null); 
  LottiPrenotazioneTipoPrenotazione.setParent(LottiPrenotazioneForm); 
%>
<select id="<%=LottiPrenotazioneTipoPrenotazione.getId()%>" name="<%=LottiPrenotazioneTipoPrenotazione.getName()%>"><% 
  LottiPrenotazioneTipoPrenotazione.write(out); 
%> 

				</select></td>
		</tr>
		<tr>
			<td colspan="2">
				<% 
  WebCheckBox LottiPrenotazioneEseguita =  
     new com.thera.thermfw.web.WebCheckBox("LottiPrenotazione", "Eseguita"); 
  LottiPrenotazioneEseguita.setParent(LottiPrenotazioneForm); 
%>
<input id="<%=LottiPrenotazioneEseguita.getId()%>" name="<%=LottiPrenotazioneEseguita.getName()%>" type="Checkbox" value="Y"><%
  LottiPrenotazioneEseguita.write(out); 
%>

			</td>
		</tr>
		
		
	</table>
  <%
  LottiPrenotazioneForm.writeFormEndElements(out); 
%>
<% 
  if(false) 
  { 
%> 
</form><% 
  } 
%> 

<%
   LottiPrenotazioneForm.writeBodyEndElements(out); 
%> 
<% 
  if(false) 
  { 
%> 
</body><% 
  } 
%> 

