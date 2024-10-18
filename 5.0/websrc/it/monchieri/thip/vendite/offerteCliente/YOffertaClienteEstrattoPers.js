function stampaPreventivoDiCommessa(){
	document.getElementById('RigheSelezionate').value = parent.document.getElementById('righe').contentWindow.getSelectedKeys();
	//var target = window.parent.name;
	var res = runActionDirect("STAMPA_PREVENTIVO_COMMESSA", "action_submit", "OffertaCliente", "", "new", "no");
	return res;
}