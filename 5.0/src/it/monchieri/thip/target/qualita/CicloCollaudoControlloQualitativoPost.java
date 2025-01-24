package it.monchieri.thip.target.qualita;

import java.math.BigDecimal;
import java.sql.SQLException;

import it.monchieri.thip.target.OrdEsecAtvSogCollaudo;
import it.monchieri.thip.target.QcTabellaAttivita;
import it.thera.thip.qualita.controllo.CicloCollaudoCaratteristica;
import it.thera.thip.qualita.controllo.CicloCollaudoFase;
import it.thera.thip.qualita.controllo.CicloCollaudoTestata;

public class CicloCollaudoControlloQualitativoPost extends CicloCollaudoImportatore {

	@SuppressWarnings("unchecked")
	@Override
	public CicloCollaudoTestata codificaCicloCollaudoPanthera(OrdEsecAtvSogCollaudo collaudo,
			QcTabellaAttivita attivita) throws SQLException {
		CicloCollaudoTestata ciclo = null;
		Integer idSequenzaFase = attivita.getIdSequenzaFase();
		Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

		ciclo = testataCicloCollaudo(collaudo);

		if(!ciclo.isOnDB()) {

			if(collaudo.getRiferimentoProceduraControlloQualitativoPost() != null) {
				ciclo.setRiferimentoProcedura(collaudo.getRiferimentoProceduraControlloQualitativoPost());
			}

			CicloCollaudoFase faseControlloUT = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Controllo UT", "Ctrl UT");

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseControlloUT.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseControlloUT);

			CicloCollaudoCaratteristica posNeg = caratteristicaCicloCollaudo(faseControlloUT, (short) 1, "Positivo / Negativo", "Pos. / Neg.",collaudo);
			posNeg.setTipoCaratteristica('2');

			faseControlloUT.getCaratteristiche().add(posNeg);

			// Seconda caratteristica
			CicloCollaudoCaratteristica c2 = caratteristicaCicloCollaudo(faseControlloUT, (short) 2, "1 MDR", "1 MDR", collaudo);
			c2.setIdUnitaMisura("mm");
			c2.setLimInfTolleranza(new BigDecimal("-9999"));
			c2.setLimSupTolleranza(new BigDecimal("9999"));

			faseControlloUT.getCaratteristiche().add(c2);

			// Terza caratteristica
			CicloCollaudoCaratteristica c3 = caratteristicaCicloCollaudo(faseControlloUT, (short) 3, "1 Spessore", "1 Spessore", collaudo);
			c3.setIdUnitaMisura("mm");
			c3.setLimInfTolleranza(new BigDecimal("-9999"));
			c3.setLimSupTolleranza(new BigDecimal("9999"));

			faseControlloUT.getCaratteristiche().add(c3);

			// Quarta caratteristica
			CicloCollaudoCaratteristica c4 = caratteristicaCicloCollaudo(faseControlloUT, (short) 4, "2 MDR", "2 MDR", collaudo);
			c4.setIdUnitaMisura("mm");
			c4.setLimInfTolleranza(new BigDecimal("-9999"));
			c4.setLimSupTolleranza(new BigDecimal("9999"));

			faseControlloUT.getCaratteristiche().add(c4);

			// Quinta caratteristica
			CicloCollaudoCaratteristica c5 = caratteristicaCicloCollaudo(faseControlloUT, (short) 5, "2 Spessore", "2 Spessore", collaudo);
			c5.setLimInfTolleranza(new BigDecimal("-9999"));
			c5.setIdUnitaMisura("mm");
			c5.setLimSupTolleranza(new BigDecimal("9999"));

			faseControlloUT.getCaratteristiche().add(c5);

			// Sesta caratteristica
			CicloCollaudoCaratteristica c6 = caratteristicaCicloCollaudo(faseControlloUT, (short) 6, "3 MDR", "3 MDR", collaudo);
			c6.setIdUnitaMisura("mm");
			c6.setLimInfTolleranza(new BigDecimal("-9999"));
			c6.setLimSupTolleranza(new BigDecimal("9999"));

			faseControlloUT.getCaratteristiche().add(c6);

			// Settima caratteristica
			CicloCollaudoCaratteristica c7 = caratteristicaCicloCollaudo(faseControlloUT, (short) 7, "3 Spessore", "3 Spessore", collaudo);
			c7.setIdUnitaMisura("mm");
			c7.setLimInfTolleranza(new BigDecimal("-9999"));
			c7.setLimSupTolleranza(new BigDecimal("9999"));

			faseControlloUT.getCaratteristiche().add(c7);

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseControlloDimensionale = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Controllo Dimensionale", "Ctrl DIM");

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseControlloDimensionale.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseControlloDimensionale);

			CicloCollaudoCaratteristica carattCntrldiM = caratteristicaCicloCollaudo(faseControlloDimensionale, (short) 1, "Controllo Dimensionale", "Ctrl DIM",collaudo);
			carattCntrldiM.setTipoCaratteristica('2');
			carattCntrldiM.setPercDaControllare(new BigDecimal(100));
			
			faseControlloDimensionale.getCaratteristiche().add(carattCntrldiM);

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Controllo Durezza Kg", "Ctrl HB Kg");

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseDurezzaKg.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseDurezzaKg);

			// Caratteristica 1
			CicloCollaudoCaratteristica durezza1 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 1, "Durezza Kg", "Durezza Kg", collaudo);
			durezza1.setRilavLimInfTol(true);
			durezza1.setRilavLimSupTol(true);
			
			faseDurezzaKg.getCaratteristiche().add(durezza1);

			// Caratteristica 2
			CicloCollaudoCaratteristica durezza2 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 2, "Durezza Kg", "Durezza Kg", collaudo);
			durezza2.setRilavLimInfTol(true);
			durezza2.setRilavLimSupTol(true);
			
			faseDurezzaKg.getCaratteristiche().add(durezza2);

			// Caratteristica 3
			CicloCollaudoCaratteristica durezza3 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 3, "Durezza Kg", "Durezza Kg", collaudo);
			durezza3.setRilavLimInfTol(true);
			durezza3.setRilavLimSupTol(true);
			
			faseDurezzaKg.getCaratteristiche().add(durezza3);

			// Caratteristica 4
			CicloCollaudoCaratteristica durezza4 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 4, "Durezza Kg", "Durezza Kg", collaudo);
			durezza4.setRilavLimInfTol(true);
			durezza4.setRilavLimSupTol(true);
			
			faseDurezzaKg.getCaratteristiche().add(durezza4);

			// Caratteristica 5
			CicloCollaudoCaratteristica durezza5 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 5, "Durezza Kg", "Durezza Kg", collaudo);
			durezza5.setRilavLimInfTol(true);
			durezza5.setRilavLimSupTol(true);
			
			faseDurezzaKg.getCaratteristiche().add(durezza5);

			// Caratteristica 6
			CicloCollaudoCaratteristica durezza6 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 6, "Durezza Kg", "Durezza Kg", collaudo);
			durezza6.setRilavLimInfTol(true);
			durezza6.setRilavLimSupTol(true);
			
			faseDurezzaKg.getCaratteristiche().add(durezza6);


			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseMagnetico = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Magnetico", "Magnetico");

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseMagnetico.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseMagnetico);

			CicloCollaudoCaratteristica posNeg6403 = caratteristicaCicloCollaudo(faseMagnetico, (short) 1, "Positivo / Negativo", "Pos./Neg.", collaudo);

			faseMagnetico.getCaratteristiche().add(posNeg6403);


			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseLiquidiPenetranti = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Liquidi Penetranti", "Liquidi Penetra");

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseLiquidiPenetranti.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseLiquidiPenetranti);

			CicloCollaudoCaratteristica posNeg6404 = caratteristicaCicloCollaudo(faseLiquidiPenetranti, (short) 1, "Positivo / Negativo", "Pos./Neg.", collaudo);

			faseLiquidiPenetranti.getCaratteristiche().add(posNeg6404);

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
		fase.setTipoFrequenzaControllo('6');
		fase.setValNominaleObbligatorio(false);
		fase.setControlloEsterno(false);
		fase.setQtaDaControllare(BigDecimal.ONE);
		fase.setNumMaxDifettiPerAccettaz(0);
		fase.setNumMinDifettiPerRifiuto(1);
		fase.setCampionamento('1');
		fase.setLivelloCampionamento('2');
		fase.setLivelloIspezione('6');
		fase.setNormativaCartaCtr(CicloCollaudoFase.UNI);
		return fase;
	}
	
	@Override
	protected CicloCollaudoCaratteristica caratteristicaCicloCollaudo(CicloCollaudoFase fase, Short seqCarat,
			String descrizione, String descrRidotta, OrdEsecAtvSogCollaudo atvTarget) {
		CicloCollaudoCaratteristica caratt = super.caratteristicaCicloCollaudo(fase, seqCarat, descrizione, descrRidotta, atvTarget);
		caratt.setTipoCaratteristica('1');
		return caratt;
	}

}
