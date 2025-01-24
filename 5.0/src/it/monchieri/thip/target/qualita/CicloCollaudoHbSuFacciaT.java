package it.monchieri.thip.target.qualita;

import java.math.BigDecimal;
import java.sql.SQLException;

import it.monchieri.thip.target.OrdEsecAtvSogCollaudo;
import it.monchieri.thip.target.QcTabellaAttivita;
import it.thera.thip.qualita.controllo.CicloCollaudoCaratteristica;
import it.thera.thip.qualita.controllo.CicloCollaudoFase;
import it.thera.thip.qualita.controllo.CicloCollaudoTestata;

public class CicloCollaudoHbSuFacciaT extends CicloCollaudoImportatore {

	@SuppressWarnings("unchecked")
	@Override
	public CicloCollaudoTestata codificaCicloCollaudoPanthera(OrdEsecAtvSogCollaudo collaudo,
			QcTabellaAttivita attivita) throws SQLException {
		Integer idSequenzaFase = attivita.getIdSequenzaFase();
		Integer idSequenzaVisualizz = attivita.getSeqVisualizz();
		CicloCollaudoTestata ciclo = null;
		ciclo = testataCicloCollaudo(collaudo);

		if(!ciclo.isOnDB()) {

			CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Kg", "Durezza Kg");

			ciclo.getFasi().add(faseDurezzaKg);

			CicloCollaudoCaratteristica carattDurezza1 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 1, "Durezza Kg", "Durezza Kg", collaudo);

			faseDurezzaKg.getCaratteristiche().add(carattDurezza1);

			// Durezza 2
			CicloCollaudoCaratteristica carattDurezza2 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 2, "Durezza Kg", "Durezza Kg", collaudo);

			faseDurezzaKg.getCaratteristiche().add(carattDurezza2);

			// Durezza 3
			CicloCollaudoCaratteristica carattDurezza3 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 3, "Durezza Kg", "Durezza Kg", collaudo);

			faseDurezzaKg.getCaratteristiche().add(carattDurezza3);

			// Durezza 4
			CicloCollaudoCaratteristica carattDurezza4 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 4, "Durezza Kg", "Durezza Kg", collaudo);

			faseDurezzaKg.getCaratteristiche().add(carattDurezza4);

			// Durezza 5
			CicloCollaudoCaratteristica carattDurezza5 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 5, "Durezza Kg", "Durezza Kg", collaudo);

			faseDurezzaKg.getCaratteristiche().add(carattDurezza5);

			// Durezza 6
			CicloCollaudoCaratteristica carattDurezza6 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 6, "Durezza Kg", "Durezza Kg", collaudo);

			faseDurezzaKg.getCaratteristiche().add(carattDurezza6);

		}else {

		}

		return ciclo;
	}
	
	@Override
	protected CicloCollaudoFase faseCicloCollaudo(CicloCollaudoTestata testata, Short seqFase, Short seqVisualizz,
			String descrizione, String descrRidotta) {
		CicloCollaudoFase fase = super.faseCicloCollaudo(testata, seqFase, seqVisualizz, descrizione, descrRidotta);
		fase.setTipoCaratteristiche('1');
		fase.setRaccoltaDati('1');
		fase.setCampionamento('2');
		fase.setTipoFrequenzaControllo('P');
		fase.setValNominaleObbligatorio(false);
		fase.setControlloEsterno(false);
		fase.setFaseObbligatoria(false);
		fase.setFaseDaStampare(true);
		fase.setPercentualeDaControlla(new BigDecimal(100));
		fase.setNumMaxDifettiPerAccettaz(0);
		fase.setNumMinDifettiPerRifiuto(1);
		fase.setTipoCampionamento('1');
		fase.setLivelloCampionamento('2');
		fase.setLivelloIspezione('6');
		fase.setNormativaCartaCtr('1');
		fase.setUtilizzoLimitiAttesi(false);
		fase.setRiferimentoCentroTolleran(false); 
		return fase;
	}
	
	@Override
	protected CicloCollaudoCaratteristica caratteristicaCicloCollaudo(CicloCollaudoFase fase, Short seqCarat,
			String descrizione, String descrRidotta, OrdEsecAtvSogCollaudo atvTarget) {
		CicloCollaudoCaratteristica caratt = super.caratteristicaCicloCollaudo(fase, seqCarat, descrizione, descrRidotta, atvTarget);
		caratt.setLimInfTolleranza(new BigDecimal("-9999"));
		caratt.setLimSupTolleranza(new BigDecimal("9999"));
		caratt.setIdUnitaMisura("kg");
		caratt.setValoreNominale(BigDecimal.ZERO);
		caratt.setMasterTipoRis('S');
		caratt.setMasterLivelloRis('3');
		caratt.setStrMisuraTipoRis1('S');
		caratt.setStrMisuraLivelloRis1('3');
		caratt.setStrMisuraTipoRis2('S');
		caratt.setStrMisuraLivelloRis2('4');
		caratt.setLivelloIspezione('6');
		caratt.setTipoCartaControllo('1');
		caratt.setDefaultMasterTipoRis('S');
		caratt.setDefaultMasterLivelloRis('4');
		caratt.setDefaultTipoRis1('S');
		caratt.setDefaultTipoRis2('S');
		caratt.setDefaultLivelloRis1('4');
		caratt.setDefaultLivelloRis2('4');
		caratt.setRilavLimInfTol(true);
		caratt.setRilavLimSupTol(true);
		caratt.setFlagRilavorabilita(true);
		caratt.setTipoCaratteristica('1');
		caratt.setClasseImportanzaCar('1');
		caratt.setPercDaControllare(new BigDecimal(100));
		return caratt;
	}

}
