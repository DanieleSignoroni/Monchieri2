//Fix 5817 AB initial version
function codificaFaseOnKeyChange() {
	loadDatiCodificaFase();
}

function loadDatiCodificaFase() {
	var sequenzaFase = eval('document.forms[0].' + idFromName['SequenzaFase']).value;
	var descr = eval('document.forms[0].' + idFromName['Descrizione.Descrizione']).value;
	var descrRidotta = eval('document.forms[0].' + idFromName['Descrizione.DescrizioneRidotta']).value;
	var codiceCodificaMame = eval('document.forms[0].' + idFromName['IdCodificaFase']).value;
	var empty = isEm(codiceCodificaMame);
	//	disableEditGridButtons('AttributiFase', isEm(codiceCodificaMame));
	if (!empty) {
		var mode = document.forms[0].thMode.value;
		var params = "?ID_CODIFICA_FASE=" + codiceCodificaMame;
		var url = "/" + webAppPath + "/" + servletPath + "/" + "it.mame.thip.qualita.controllo.web.RecuperaDatiCodificaFaseMame" + params;
		//document.frames[errorsFrameName].location = url;
		setLocationOnWindow(document.getElementById(errorsFrameName).contentWindow, url);
	}
}

function writeValueInCampo(campoName, value) {
	var campo = eval('document.forms[0].' + idFromName[campoName]);

	if (campo && isEm(campo.value)) {
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

//redefinition Fix 5817
function getclassCDHdrName(gridClassCD) {
	if (gridClassCD == "Fasi")
		return "CicloCollaudoFase";
	//		return getReplacingClassHDRName('CicloCollaudoFase');
	//Fix AB 07416 begin
	//	if(gridClassCD=="Caratteristiche" ) 
	if (gridClassCD == "Caratteristiche" || gridClassCD == "CaratteristicheMame")
		//Fix AB 07416 end
		return "CicloCollaudoCaratt";
	//		return getReplacingClassHDRName('CicloCollaudoCaratt');

}

function getReplacingClassHDRName(originalClassName) {
	if (originalClassName == 'CicloCollaudoFase')
		return "YCicloCollaudoFase";
	if (originalClassName == 'CicloCollaudoCaratt')
		return "CicloCollaudoCarattMame";
}

//Fix AB 6244 Begin

function redefineFormOL() {
	var oldCicloCollaudoFaseOL = CicloCollaudoFaseOL;
	CicloCollaudoFaseOL = function fOL() {
		oldCicloCollaudoFaseOL();
		enableCodificaSegondoAttributiFase();
		gestioneAbilitaAZioneAttributiFase();
		redefineEditGridDeleteRow2();
		//fix 6424 begin
		var mode = document.forms[0].thMode.value;
		if (mode == 'NEW') {
			eval('document.forms[0].' + idFromName['Campionamento']).value = '2';
			visalizzaCasoX();
			eval('document.forms[0].' + idFromName['PercentualeDaControllare']).value = '100';
			disabiltaQtaDaControllare();
			eval('document.forms[0].' + idFromName['NumMaxDifettiAccettazione']).value = '0';
			eval('document.forms[0].' + idFromName['NumMinDifettiRifiuto']).value = '1';
			executeCampoOnBlur('PercentualeDaControllare');
		}
		//fix 6424 end
		hideTabCaratteristicheSTD();//Fix AB 07416
		redefineControllaCD();//Fix AB 07416
		redefineCostruisciChiaveNodeTree();//Fix AB 07416
	}
}
redefineFormOL();

function redefineEditGridDeleteRow2() {
	var oldEditGridDeleteRow = editGridDeleteRow;
	editGridDeleteRow = function EGDR(gridClassCD) {
		oldEditGridDeleteRow(gridClassCD);
		if (gridClassCD == 'AttributiFase') {
			var curGrid = eval(editGrid[gridClassCD]);
			enableSearchComponent('CodificaFase', isEmptyGrid(curGrid, curGrid.selectedRow + curGrid.firstRow), false);
		}
	}
}

function enableCodificaSegondoAttributiFase() {
	var mode = document.forms[0].thMode.value;
	if (mode != 'SHOW') {
		var curGrid = eval(editGrid['AttributiFase']);
		var size = curGrid.rows.length;
		enableSearchComponent('CodificaFase', (size == 0), false);
	}
}
function gestioneAbilitaAZioneAttributiFase() {
	var mode = document.forms[0].thMode.value;
	if (mode != 'SHOW') {
		var codiceCodificaMame = eval('document.forms[0].' + idFromName['IdCodificaFase']).value;
		disableEditGridButtons('AttributiFase', isEm(codiceCodificaMame));
	}
}
function isEmptyGrid(aGrid, curNum) {
	var size = aGrid.rows.length;
	if (size == 0)
		return true;
	else {
		for (var j = 0; j < size; j++) {
			if (curNum != j) {
				var aRow = aGrid.rows[j];
				if ((aRow.status != "DELETE") && (aRow.status != "DELETE_FROM_NEW"))
					return false;
			}
		}
	}
	return true;
}

function disableEditGridButtons(gridClassCD, flag) {
	var curGrid = eval(editGrid[gridClassCD]);
	eval("document.forms[0].NewRow_" + gridClassCD).disabled = flag;
	eval("document.forms[0].UpdateRow_" + gridClassCD).disabled = flag;
	eval("document.forms[0].ShowRow_" + gridClassCD).disabled = flag;
	eval("document.forms[0].CopyRow_" + gridClassCD).disabled = flag;
	eval("document.forms[0].DeleteRow_" + gridClassCD).disabled = flag;
	eval("document.forms[0].SelectColumns_" + gridClassCD).disabled = flag;
}

//Fix AB 6244 End

//Fix AB 07416
function redefineControllaCD(gridClassCD) {
	var oldControllaCD = controllaCD;
	controllaCD = function x(gridClassCD) {
		if (gridClassCD == "CaratteristicheMame")
			return true;
		return oldControllaCD(gridClassCD);
	}
}
//Fix AB 07416 
function hideTabCaratteristicheSTD() {
	mytabbedtabCaratteristiche_TAB.style.display = 'none';
}

//Fix AB 07416 
function redefineCostruisciChiaveNodeTree(gridClassCD, rowKey) {
	var oldCostruisciChiaveNodeTree = costruisciChiaveNodeTree;
	costruisciChiaveNodeTree = function x(gridClassCD, rowKey) {
		if (gridClassCD == "CaratteristicheMame")
			gridClassCD = "Caratteristiche";
		return oldCostruisciChiaveNodeTree(gridClassCD, rowKey);
		//sourceIdentifier+","+gridClassCD+";"+rowKey;
	}
}
//Fix AB 07416 
function getGridClassCDToUse(gridClassCD) {
	if (gridClassCD == "CaratteristicheMame")
		return "Caratteristiche";
	return gridClassCD;

}
//Fix AB 07416 
function redefineEditGridNewRow() {
	//...Ridefinisco l'azione "new" per chiamare quella dell'albero
	var oldEditGridNewRow = editGridNewRow;
	editGridNewRow = function EGNR(gridClassCD) {
		if (controllaCD(gridClassCD)) {
			var classCDHdrName = getclassCDHdrName(gridClassCD);
			gridClassCD = getGridClassCDToUse(gridClassCD);//Fix AB 07416
			var curNode = parent.frames[0].findNodeFromId(sourceIdentifier);
			curNode.select();
			parent.frames[0].doMenuAction(classCDHdrName, "NEW", "detail", gridClassCD);
		}
		else
			oldEditGridNewRow(gridClassCD);
	}
}

//Fix AB 07416 
function redefineEditGridShowRow() {
	//...Ridefinisco l'azione "show" per chiamare quella dell'albero
	var oldEditGridShowRow = editGridShowRow;
	editGridShowRow = function EGSR(gridClassCD) {
		var classCDHdrName = getclassCDHdrName(gridClassCD);
		if (controllaCD(gridClassCD)) {
			var curGrid = eval(editGrid[gridClassCD]);
			var numr = curGrid.selectedRow + curGrid.firstRow;
			gridClassCD = getGridClassCDToUse(gridClassCD);//Fix AB 07416
			var rowKey = getRowKey(gridClassCD, numr);
			var nodeKey = costruisciChiaveNodeTree(gridClassCD, rowKey);
			var curNode = parent.frames[0].findNodeFromId(nodeKey);
			curNode.select();
			parent.frames[0].doMenuAction(classCDHdrName, "SHOW", "detail");
		}
		else
			oldEditGridShowRow(gridClassCD);
	}
}

//Fix AB 07416 
function redefineEditGridDeleteRow() {
	//...Ridefinisco l'azione "delete" per chiamare quella dell'albero
	var oldEditGridDeleteRow = editGridDeleteRow;
	editGridDeleteRow = function EGDR(gridClassCD) {
		var curGrid = eval(editGrid[gridClassCD]);
		if (curGrid.isInEdit)
			return;

		if (curGrid.gridType == "IND") {
			if (confirm(deleteAsk)) {
				if (controllaCD(gridClassCD)) {
					var numr = curGrid.selectedRow + curGrid.firstRow;
					var rowKey = getRowKey(gridClassCD, numr);
					var nodeKey = costruisciChiaveNodeTree(gridClassCD, rowKey);
					var curNode = parent.frames[0].findNodeFromId(nodeKey);
					curNode.remove();
				}
				document.forms[0].target = gridClassCD + "IFrame";
				document.forms[0].thTarget.value = gridClassCD + "IFrame";
				var numr = curGrid.selectedRow + curGrid.firstRow;
				var key = getRowKey(gridClassCD, numr);
				gridClassCD = getGridClassCDToUse(gridClassCD);//Fix AB 07416
				var url = curGrid.rowUrl + "&ObjectKey=" + key + "&thAction=DELETE&thTarget=" + gridClassCD + "IFrame";
				var oa = document.forms[0].action;
				document.forms[0].action = url;
				document.forms[0].submit();
				document.forms[0].action = oa;
			}
		}
		else {
			selRow = curGrid.rows[curGrid.selectedRow + curGrid.firstRow];
			if (selRow.status == "DELETE" || selRow.status == "DELETE_FROM_NEW")
				return;

			if (selRow.status == "NEW" || selRow.status == "NEW_UPDATE")
				selRow.status = "DELETE_FROM_NEW";
			else
				selRow.status = "DELETE";

			showRowStatus(gridClassCD, curGrid.selectedRow, selRow.status);

			if (curGrid.gridType == "EDIT")
				editGridCloseEditComponent(gridClassCD, false, false);
		}
	}
}
//Fix 09875 RA Inizio
myOldcontrolloEsternoClick = controlloEsternoClick;
controlloEsternoClick = function() {
	myOldcontrolloEsternoClick();
	if (document.forms[0].thMode.value != "SHOW") {
		var elementTest = eval('document.forms[0].' + idFromName['ControlloEsterno']);
		if (elementTest.checked)
			enableSearchComponent('EnteIspet', true, false);
		else
			enableSearchComponent('EnteIspet', false, true);
	}
}
//Fix 09875 RA Fine
