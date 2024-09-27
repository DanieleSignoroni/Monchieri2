function changeIdCommessa() {
    if (eval("document.forms[0]." + idFromName['LottiPrenotazione.IdCommessa']).value != '' && eval("document.forms[0]." + idFromName['LottiPrenotazione.IdCommessa']).value != 'PRNT_LOTTO') {
        eval("document.forms[0]." + idFromName['LottiPrenotazione.DscCommessa']).value = eval("document.forms[0]." + idFromName['LottiPrenotazione.IdCommessa']).value;
    }
}

function changeDscCommessa() {
    if (eval("document.forms[0]." + idFromName['LottiPrenotazione.DscCommessa']).value != '') {
        eval("document.forms[0]." + idFromName['LottiPrenotazione.IdCommessa']).value = 'PRNT_LOTTO';
        eval("document.forms[0]." + idFromName['LottiPrenotazione.Commessa.Descrizione.Descrizione']).value = '';
    }
}
