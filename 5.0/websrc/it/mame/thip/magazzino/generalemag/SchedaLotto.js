function SchedaLottoGUIOL(){
	fillReadOnly();
	fillMagVersConfig();
}

function fillMagVersConfig(){

	var magazzino = eval("document.forms[0]." + idFromName['Magazzino']).value;
	var versione = eval("document.forms[0]." + idFromName['Versione']).value;
	var configurazione = eval("document.forms[0]." + idFromName['Configurazione']).value;
	if(magazzino != ""){
		document.forms[0].MagVersConf.value = 'Mag. ' + magazzino + ' Vers. ' + versione + ' Conf. ' + configurazione ;
		document.forms[0].MagVersConf.readOnly = true;
		document.forms[0].MagVersConf.style.background = eval("document.forms[0]").style.background;
	}else{
		document.forms[0].MagVersConf.style.display = "none";
	}
}

function fillReadOnly(){
	/*eval("document.forms[0]." + idFromName['Configurazione']).readOnly = true;
	eval("document.forms[0]." + idFromName['Configurazione']).style.background = eval("document.forms[0]").style.background;
	eval("document.forms[0]." + idFromName['Versione']).readOnly = true;
	eval("document.forms[0]." + idFromName['Versione']).style.background = eval("document.forms[0]").style.background;
	eval("document.forms[0]." + idFromName['Magazzino']).readOnly = true;
	eval("document.forms[0]." + idFromName['Magazzino']).style.background = eval("document.forms[0]").style.background;*/
	eval("document.forms[0]." + idFromName['Gruppo']).readOnly = true;
	eval("document.forms[0]." + idFromName['Gruppo']).style.background = eval("document.forms[0]").style.background;
	eval("document.forms[0]." + idFromName['Tipo']).readOnly = true;
	eval("document.forms[0]." + idFromName['Tipo']).style.background = eval("document.forms[0]").style.background;
	eval("document.forms[0]." + idFromName['Sezione']).readOnly = true;
	eval("document.forms[0]." + idFromName['Sezione']).style.background = eval("document.forms[0]").style.background;
	eval("document.forms[0]." + idFromName['Acciaieria']).readOnly = true;
	eval("document.forms[0]." + idFromName['Acciaieria']).style.background = eval("document.forms[0]").style.background;
	eval("document.forms[0]." + idFromName['Colata']).readOnly = true;
	eval("document.forms[0]." + idFromName['Colata']).style.background = eval("document.forms[0]").style.background;
	eval("document.forms[0]." + idFromName['Giacenza']).readOnly = true;
	eval("document.forms[0]." + idFromName['Giacenza']).style.background = eval("document.forms[0]").style.background;
	eval("document.forms[0]." + idFromName['GiacenzaSec']).readOnly = true;
	eval("document.forms[0]." + idFromName['GiacenzaSec']).style.background = eval("document.forms[0]").style.background;
	eval("document.forms[0]." + idFromName['Fornitore']).readOnly = true;
	eval("document.forms[0]." + idFromName['Fornitore']).style.background = eval("document.forms[0]").style.background;
	eval("document.forms[0]." + idFromName['DscFornitore']).readOnly = true;
	eval("document.forms[0]." + idFromName['DscFornitore']).style.background = eval("document.forms[0]").style.background;
}