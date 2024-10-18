<script>
	parent.document.getElementById('IdArticolo').readOnly = false;
	parent.document.getElementById('IdArticolo').style.backgroundColor  = 'rgb(255, 255, 153)';
	
	parent.document.getElementById('RArticolo$DescrizioneArticolo$Descrizione').readOnly = false;
	parent.document.getElementById('RArticolo$DescrizioneArticolo$Descrizione').style.backgroundColor  = 'rgb(255, 255, 153)';
	
	parent.document.getElementById('thRArticoloSearchBut').removeAttribute('disabled');
	parent.document.getElementById('thRArticoloArticoloBut').removeAttribute('disabled');
	
	parent.enableFormActions();
	parent.closeErrorList();
</script>