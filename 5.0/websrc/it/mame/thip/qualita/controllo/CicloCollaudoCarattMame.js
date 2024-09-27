function codificaCarattOnKeyChange() {
	loadDatiCodificaCaratt();
}

function loadDatiCodificaCaratt() {
	/*	var sequenzaCar = eval('document.forms[0].'+idFromName['SequenzaCaratteristica']).value;
		var descr = eval('document.forms[0].'+idFromName['Descrizione.Descrizione']).value; 
		var descrRidotta = eval('document.forms[0].'+idFromName['Descrizione.DescrizioneRidotta']).value; */
	var codiceCodificaCarattMame = eval('document.forms[0].' + idFromName['IdCodificaCar']).value;

	//	if(!isEm(codiceCodificaCarattMame ))
	//	{
	var mode = document.forms[0].thMode.value;
	if (mode == 'COPY' || mode == 'NEW') {
		//	var isSoloUpdate = (mode == 'UPDATE')?'Y':'N';
		var params = "?ID_CODIFICA_CAR=" + codiceCodificaCarattMame;//+ "&SOLO_UPDATE="+isSoloUpdate;
		var url = "/" + webAppPath + "/" + servletPath + "/" + "it.mame.thip.qualita.controllo.web.RecuperaDatiCodificaCarattMame" + params;
		//document.frames[errorsFrameName].location = url;
		setLocationOnWindow(document.getElementById(errorsFrameName).contentWindow, url);
	}
}

//function writeValueInCampo(campoName,value)
function writeValueInCampo(campoName, value, clearCurValue) {
	var campo = eval('document.forms[0].' + idFromName[campoName]);
	//	if(campo && isEm(campo.value))
	if (campo && isEm(campo.value) || clearCurValue) {
		campo.value = value;
	}
}
function executeCampoOnBlur(campoName) {
	var campo = eval('document.forms[0].' + idFromName[campoName]);
	if (campo && campo.onblur)
		campo.onblur();
}
function clearErrorDaCampo(campoName, mandatory) {
	var campo = eval('document.forms[0].' + idFromName[campoName]);
	clearError(campo, mandatory);
}

redefineFormOL();
function redefineFormOL() {
	var oldCicloCollaudoCarOL = CicloCollaudoCarattOL;
	CicloCollaudoCarattOL = function fOL() {
		oldCicloCollaudoCarOL();
		redefineUnitaMisuraOnKeyChange();
		redefineEditGridCloseEditComponent();//Fix AB 07087
		//		changeLivelloStrumenti();//Fix AB 07087
		hideStrumentiAndMaster();//Fix AB 07087
		redefineRunActionDirect();//fix AB 07416
		displayPercValore();//08360
		redefineVisalizzaCasoX();//08360
		displayCopiValori();//08361
	}
}
/**
 * Fix AB 07087
 */
function hideStrumentiAndMaster() {
	document.forms[0].Strumento1.style.display = 'none';
	document.forms[0].Strumento2.style.display = 'none';
	document.forms[0].Master.style.display = 'none';
}
//redefineUnitaMisuraOnKeyChange();
function redefineUnitaMisuraOnKeyChange() {
	var idUnitaMisura = eval('document.forms[0].' + idFromName['IdUnitaMisura']);
	var oldUnitaMisuraOC = idUnitaMisura.onchange;
	idUnitaMisura.onchange = function OMOC() {
		oldUnitaMisuraOC();
		unitaMisuraOnKeyChange();
	};
}

function unitaMisuraOnKeyChange() {
	var idUnitaMisuraCompo = eval('document.forms[0].' + idFromName['IdUnitaMisura']);
	var idUnitaMisura = idUnitaMisuraCompo.value;
	if (!isEm(idUnitaMisura)) {
		var formattedValue = idUnitaMisuraCompo.typeNameJS.format(idUnitaMisura);
		var params = "?ID_UNITA_MISURA=" + formattedValue + "&ACTION=RECUPERA_UM";
		var url = "/" + webAppPath + "/" + servletPath + "/" + "it.mame.thip.qualita.controllo.web.RecuperaDatiPerCarattMame" + params;
		//document.frames[errorsFrameName].location = url;
		setLocationOnWindow(document.getElementById(errorsFrameName).contentWindow, url);
	}
}
//07087
function changeLivelloStrumenti() {
	var curGrid = eval(editGrid['CicliCarStrumenti']);
	var livelloMaster = eval("document.forms[0]." + curGrid.columns[getADColumn('CicliCarStrumenti', 'LivelloStrumentoRisorsa')].editComponents[0]);
	removeOption(livelloMaster, GRUPPO);
	removeOption(livelloMaster, FAMIGLIA);
	removeOption(livelloMaster, NON_SIGNIFICATIVO);
}
//07087
function getADColumn(gridClassCD, ad) {
	result = 0;
	for (var i = 0; i < eval(editGrid[gridClassCD]).columns.length; i++) {
		if (eval(editGrid[gridClassCD]).columns[i].classAD == ad)
			result = i;
	}
	return result;
}
//07087
function removeOption(field, value) {
	if (field && field.type == "select-one") {
		for (var idx = 0; idx < field.options.length; idx++) {
			if (field.options[idx].value == value) {
				field.options[idx] = null;
				break;
			}
		}
	}
}
/**
 * Fix AB 07087
 **/
function redefineEditGridCloseEditComponent() {
	var oldEditGridCloseEditComponent = editGridCloseEditComponent;
	editGridCloseEditComponent = function h(gridClassCD, closeForChangeRow, clearErrors) {
		if (gridClassCD == 'CicliCarStrumenti') {
			var curGrid = eval(editGrid['CicliCarStrumenti']);
			var curColumn = curGrid.columns[curGrid.selectedCol];
			if (curColumn && curColumn.classAD == 'IdStrumentoRisorsa')
				recuperaDescrRisorsa();
			oldEditGridCloseEditComponent(gridClassCD, closeForChangeRow, clearErrors);
		}
		else
			oldEditGridCloseEditComponent(gridClassCD, closeForChangeRow, clearErrors);
	}
}
/**
 * Fix AB 07087
 **/
function recuperaDescrRisorsa() {
	var curGrid = eval(editGrid['CicliCarStrumenti']);
	if (curGrid.selectedRow != -1) {
		workRow = curGrid.selectedRow;
		var tipoRisorsa = eval("document.forms[0]." + curGrid.columns[getADColumn('CicliCarStrumenti', 'TipoStrumentoRisorsa')].editComponents[0]).value;
		var livelloRisorsa = eval("document.forms[0]." + curGrid.columns[getADColumn('CicliCarStrumenti', 'LivelloStrumentoRisorsa')].editComponents[0]).value;
		var idRisorsa = eval("document.forms[0]." + curGrid.columns[getADColumn('CicliCarStrumenti', 'IdStrumentoRisorsa')].editComponents[0]).value;
		var params = "?ID_RISORSA=" + idRisorsa + "&TIPO_RISORSA=" + tipoRisorsa + "&LIVELLO_RISORSA=" + livelloRisorsa;
		var url = "/" + webAppPath + "/" + servletPath + "/" + "it.mame.thip.qualita.controllo.web.LoadDatiRisorsa" + params;
		//document.frames[errorsFrameName].location = url;
		setLocationOnWindow(document.getElementById(errorsFrameName).contentWindow, url);
	}
}
/**
 * Fix AB 07087
 **/
function valorizzaEDGField(gridClassCD, classADColName, value) {
	var curGrid = eval(editGrid[gridClassCD]);
	curGrid.rows[workRow].data[getADColumn(gridClassCD, classADColName)] = value;
	editGridLoadTable(gridClassCD);
	workRow = -1;
}
/**
 * Fix AB 07416 
 **/
function redefineRunActionDirect() {
	var oldRunActionDirect = runActionDirect;
	runActionDirect = function rad(action, type, classhdr, key, target, toolbar) {
		if (action == 'SAVE' || action == 'SAVE_AND_CLOSE' || action == 'SAVE_AND_NEW' || action == 'CHECK_ALL') {
			var limInfTolleranza = eval('document.forms[0].' + idFromName['LimInfTolleranza']);
			var MAX_LIMIT_SUPERIORE = limInfTolleranza.typeNameJS.format('9999');
			var MIN_LIMIT_INFERIORE = limInfTolleranza.typeNameJS.format('-9999');

			var limInfTolleranza = eval('document.forms[0].' + idFromName['LimInfTolleranza']);
			var limSupTolleranza = eval('document.forms[0].' + idFromName['LimSupTolleranza']);
			if (limInfTolleranza.value != '' && limSupTolleranza.value == '')
				limSupTolleranza.value = MAX_LIMIT_SUPERIORE;
			else if (limInfTolleranza.value == '' && limSupTolleranza.value != '')
				limInfTolleranza.value = MIN_LIMIT_INFERIORE;

			var limInfDeroga = eval('document.forms[0].' + idFromName['LimInfDeroga']);
			var limSupDeroga = eval('document.forms[0].' + idFromName['LimSupDeroga']);
			if (limInfDeroga.value != '' && limSupDeroga.value == '')
				limSupDeroga.value = MAX_LIMIT_SUPERIORE;
			else if (limInfDeroga.value == '' && limSupDeroga.value != '')
				limInfDeroga.value = MIN_LIMIT_INFERIORE;

			var limInfAccettabilita = eval('document.forms[0].' + idFromName['LimInfAccettabilita']);
			var limSupAccettabilita = eval('document.forms[0].' + idFromName['LimSupAccettabilita']);
			if (limInfAccettabilita.value != '' && limSupAccettabilita.value == '')
				limSupAccettabilita.value = MAX_LIMIT_SUPERIORE;
			else if (limInfAccettabilita.value == '' && limSupAccettabilita.value != '')
				limInfAccettabilita.value = MIN_LIMIT_INFERIORE;
		}
		oldRunActionDirect(action, type, classhdr, key, target, toolbar);
	}


}

// Fix 08360 begin
function ricalcola() {
	if (eval('document.forms[0].' + idFromName['Percentuale']).value != '' ||
		eval('document.forms[0].' + idFromName['Valore']).value != '') {
		var decimalType = eval('document.forms[0].' + idFromName['LimInfDeroga']);
		var percentuale = eval('document.forms[0].' + idFromName['Percentuale']).typeNameJS.getDBValue(eval('document.forms[0].' + idFromName['Percentuale']).value);
		var valore = decimalType.typeNameJS.getDBValue(eval('document.forms[0].' + idFromName['Valore']).value);
		var limInfTol = decimalType.typeNameJS.getDBValue(eval('document.forms[0].' + idFromName['LimInfTolleranza']).value);
		var limSupTol = decimalType.typeNameJS.getDBValue(eval('document.forms[0].' + idFromName['LimSupTolleranza']).value);
		var params = "?PERCENTUALE=" + percentuale + "&VALORE=" + valore + "&LIM_INF_TOL=" + limInfTol + "&LIM_SUP_TOL=" + limSupTol;
		var url = "/" + webAppPath + "/" + servletPath + "/" + "it.mame.thip.qualita.controllo.web.RicalcolaValore" + params;
		//document.frames[errorsFrameName].location = url;
		setLocationOnWindow(document.getElementById(errorsFrameName).contentWindow, url);
	}
}

function displayPercValore() {
	/*
  PercValoreTABLE.style.display = blockCASO11.style.display ;
  if(document.forms[0].thMode.value =='SHOW')
		  PercValoreTABLE.style.display = "none";
		  */
}
// Fix 08360 end

// Fix 08361 begin
var initialAction;
function copiaValori() {
	initialAction = document.forms[0].action;
	document.forms[0].action = "/" + webAppPath + "/" + servletPath + "/" + "it.mame.thip.qualita.controllo.web.CopiaValori";
	runActionDirect('CHECK_ALL', 'action_submit', 'CicloCollaudoCarattMame', eval('document.forms[0].thKey').value, 'errorsFrame', 'no');
	document.forms[0].action = initialAction;
}

function displayCopiValori() {
	/*
	  document.forms[0].CopiaValori.style.display = blockCASO11.style.display ;
	  if(document.forms[0].thMode.value =='SHOW')
	   document.forms[0].CopiaValori.style.display = "none";
	*/
}

function redefineVisalizzaCasoX() {
	var oldVisalizzaCasoX = visalizzaCasoX;
	visalizzaCasoX = function f() {
		oldVisalizzaCasoX();
		displayCopiValori();
		displayPercValore();
	}
}

// Fix 08361 end