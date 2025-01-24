package it.monchieri.thip.target.qualita;

import java.math.BigDecimal;
import java.sql.SQLException;

import it.monchieri.thip.target.OrdEsecAtvSogCollaudo;
import it.monchieri.thip.target.QcTabellaAttivita;
import it.thera.thip.qualita.controllo.CicloCollaudoCaratteristica;
import it.thera.thip.qualita.controllo.CicloCollaudoFase;
import it.thera.thip.qualita.controllo.CicloCollaudoTestata;

public class CicloCollaudoGrezzoPostForgia extends CicloCollaudoImportatore {

	@SuppressWarnings("unchecked")
	@Override
	public CicloCollaudoTestata codificaCicloCollaudoPanthera(OrdEsecAtvSogCollaudo collaudo,
			QcTabellaAttivita attivita) throws SQLException {
		CicloCollaudoTestata ciclo = null;
		Integer idSequenzaFase = attivita.getIdSequenzaFase();
		Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

		ciclo = testataCicloCollaudo(collaudo);

		if(!ciclo.isOnDB()) {
			ciclo.setAssociaCiclo(true);
			if(collaudo.getRiferimentoProceduraControlloGrezzoPostForgia() != null) {
				ciclo.setRiferimentoProcedura(collaudo.getRiferimentoProceduraControlloGrezzoPostForgia());
			}

			CicloCollaudoFase faseDimensionale = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Dimensionale", "Dimensionale");

			ciclo.getFasi().add(faseDimensionale);

			CicloCollaudoCaratteristica posNeg = caratteristicaCicloCollaudo(faseDimensionale, (short) 1, "Positivo / Negativo", "Pos. / Neg.",collaudo);
			posNeg.setIdUnitaMisura("kg"); 

			faseDimensionale.getCaratteristiche().add(posNeg);

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseDurezzaConSpina = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Con Spina Kg", "Durezza Con Spi");

			ciclo.getFasi().add(faseDurezzaConSpina);

			// Durezza 1
			CicloCollaudoCaratteristica carattDurezza1 = caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 1, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza1.setIdUnitaMisura("kg");
			carattDurezza1.setRilavLimInfTol(true);
			carattDurezza1.setRilavLimSupTol(true);

			faseDurezzaConSpina.getCaratteristiche().add(carattDurezza1);

			// Durezza 2
			CicloCollaudoCaratteristica carattDurezza2 = caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 2, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza2.setIdUnitaMisura("kg");
			carattDurezza2.setRilavLimInfTol(true);
			carattDurezza2.setRilavLimSupTol(true);

			faseDurezzaConSpina.getCaratteristiche().add(carattDurezza2);

			// Durezza 3
			CicloCollaudoCaratteristica carattDurezza3 = caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 3, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza3.setIdUnitaMisura("kg");
			carattDurezza3.setRilavLimInfTol(true);
			carattDurezza3.setRilavLimSupTol(true);

			faseDurezzaConSpina.getCaratteristiche().add(carattDurezza3);

			// Durezza 4
			CicloCollaudoCaratteristica carattDurezza4 = caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 4, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza4.setIdUnitaMisura("kg");
			carattDurezza4.setRilavLimInfTol(true);
			carattDurezza4.setRilavLimSupTol(true);

			faseDurezzaConSpina.getCaratteristiche().add(carattDurezza4);

			// Durezza 5
			CicloCollaudoCaratteristica carattDurezza5 = caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 5, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza5.setIdUnitaMisura("kg");
			carattDurezza5.setRilavLimInfTol(true);
			carattDurezza5.setRilavLimSupTol(true);

			faseDurezzaConSpina.getCaratteristiche().add(carattDurezza5);

			// Durezza 6
			CicloCollaudoCaratteristica carattDurezza6 = caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 6, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza6.setIdUnitaMisura("kg");
			carattDurezza6.setRilavLimInfTol(true);
			carattDurezza6.setRilavLimSupTol(true);

			faseDurezzaConSpina.getCaratteristiche().add(carattDurezza6);
		}else {
			String riferimentoProcedura = ciclo.getRiferimentoProcedura() != null ? ciclo.getRiferimentoProcedura() : "";
			if(collaudo.getRiferimentoProceduraControlloGrezzoPostForgia() != null && !collaudo.getRiferimentoProceduraControlloGrezzoPostForgia().equals(riferimentoProcedura)) {
				ciclo.setRiferimentoProcedura(collaudo.getRiferimentoProceduraControlloGrezzoPostForgia());
				ciclo.setDirty();
			}
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
		caratt.setObbligatorieta(true);
		caratt.setDefaultTipoRis1('S');
		caratt.setDefaultTipoRis2('S');
		caratt.setDefaultLivelloRis1('4');
		caratt.setDefaultLivelloRis2('4');
		caratt.setRilavLimInfTol(false);
		caratt.setRilavLimSupTol(false);
		caratt.setFlagRilavorabilita(true);
		caratt.setTipoCaratteristica('2');
		caratt.setClasseImportanzaCar('1');
		return caratt;
	}

}
