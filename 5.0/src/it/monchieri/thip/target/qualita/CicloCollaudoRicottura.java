package it.monchieri.thip.target.qualita;

import java.math.BigDecimal;
import java.sql.SQLException;

import it.monchieri.thip.target.OrdEsecAtvSogCollaudo;
import it.monchieri.thip.target.QcTabellaAttivita;
import it.thera.thip.qualita.controllo.CicloCollaudoCaratteristica;
import it.thera.thip.qualita.controllo.CicloCollaudoFase;
import it.thera.thip.qualita.controllo.CicloCollaudoTestata;

public class CicloCollaudoRicottura extends CicloCollaudoImportatore {

	@SuppressWarnings("unchecked")
	@Override
	public CicloCollaudoTestata codificaCicloCollaudoPanthera(OrdEsecAtvSogCollaudo collaudo,
			QcTabellaAttivita attivita) throws SQLException {
		CicloCollaudoTestata ciclo = null;
		Integer idSequenzaFase = attivita.getIdSequenzaFase();
		Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

		ciclo = testataCicloCollaudo(collaudo);

		if(!ciclo.isOnDB()) {

			if(collaudo.getRiferimentoProeduraTtRicottura() != null) {
				ciclo.setRiferimentoProcedura(collaudo.getRiferimentoProeduraTtRicottura());
			}

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseCPezzoRicott = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "°C Pezzo in Ricottura", "°C Pezzo in Ric");

			ciclo.getFasi().add(faseCPezzoRicott);

			CicloCollaudoCaratteristica tempPezzo = caratteristicaCicloCollaudo(faseCPezzoRicott, (short) 1, "Temperatura Pezzo", "Temperatura P",collaudo);
			tempPezzo.setIdUnitaMisura("°C"); 

			faseCPezzoRicott.getCaratteristiche().add(tempPezzo);

			idSequenzaFase = idSequenzaFase + 1;
			idSequenzaVisualizz = idSequenzaVisualizz + 1;

			CicloCollaudoFase faseFornoRicott = faseCicloCollaudo(
					ciclo,
					idSequenzaFase.shortValue(),
					idSequenzaVisualizz.shortValue(),
					"Forno di Ricottura",
					"Forno di Ricott"
					);

			ciclo.getFasi().add(faseFornoRicott);

			// -- 1) Temperatura Iniziale --
			CicloCollaudoCaratteristica tempIniziale = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 1,
					"Temperatura Iniziale",
					"Temperatura ini",
					collaudo
					);
			tempIniziale.setIdUnitaMisura("°C");

			faseFornoRicott.getCaratteristiche().add(tempIniziale);
			// -- 2) 1 Gradiente Termico --
			CicloCollaudoCaratteristica gradTerm1 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 2,
					"1 Gradiente Termico",
					"1 Gradiente Ter",
					collaudo
					);
			gradTerm1.setIdUnitaMisura("C/H");

			faseFornoRicott.getCaratteristiche().add(gradTerm1);

			// -- 3) 1 Temperatura Fine Rampa --
			CicloCollaudoCaratteristica tempFineRampa1 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 3,
					"1 Temperatura Fine Rampa",
					"1 Temperatura F",
					collaudo
					);
			tempFineRampa1.setIdUnitaMisura("°C");

			faseFornoRicott.getCaratteristiche().add(tempFineRampa1);

			// -- 4) 1 Permanenza --
			CicloCollaudoCaratteristica permanenza1 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 4,
					"1 Permanenza",
					"1 Permanenza",
					collaudo
					);
			permanenza1.setIdUnitaMisura("h");

			faseFornoRicott.getCaratteristiche().add(permanenza1);

			// -- 5) 2 Gradiente Termico --
			CicloCollaudoCaratteristica gradTerm2 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 5,
					"2 Gradiente Termico",
					"2 Gradiente Ter",
					collaudo
					);
			gradTerm2.setIdUnitaMisura("C/H");

			faseFornoRicott.getCaratteristiche().add(gradTerm2);

			// -- 6) 2 Temperatura Fine Rampa --
			CicloCollaudoCaratteristica tempFineRampa2 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 6,
					"2 Temperatura Fine Rampa",
					"2 Temperatura F",
					collaudo
					);
			tempFineRampa2.setIdUnitaMisura("°C");

			faseFornoRicott.getCaratteristiche().add(tempFineRampa2);

			// -- 7) 2 Permanenza --
			CicloCollaudoCaratteristica permanenza2 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 7,
					"2 Permanenza",
					"2 Permanenza",
					collaudo
					);
			permanenza2.setIdUnitaMisura("h");

			faseFornoRicott.getCaratteristiche().add(permanenza2);

			// -- 8) 3 Gradiente Termico --
			CicloCollaudoCaratteristica gradTerm3 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 8,
					"3 Gradiente Termico",
					"3 Gradiente Ter",
					collaudo
					);
			gradTerm3.setIdUnitaMisura("C/H");

			faseFornoRicott.getCaratteristiche().add(gradTerm3);

			// -- 9) 3 Temperatura Fine Rampa --
			CicloCollaudoCaratteristica tempFineRampa3 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 9,
					"3 Temperatura Fine Rampa",
					"3 Temperatura F",
					collaudo
					);
			tempFineRampa3.setIdUnitaMisura("°C");

			faseFornoRicott.getCaratteristiche().add(tempFineRampa3);

			// -- 10) 3 Permanenza --
			CicloCollaudoCaratteristica permanenza3 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 10,
					"3 Permanenza",
					"3 Permanenza",
					collaudo
					);
			permanenza3.setIdUnitaMisura("h");

			faseFornoRicott.getCaratteristiche().add(permanenza3);

			// -- 11) 4 Gradiente Termico --
			CicloCollaudoCaratteristica gradTerm4 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 11,
					"4 Gradiente Termico",
					"4 Gradiente Ter",
					collaudo
					);
			gradTerm4.setIdUnitaMisura("C/H");

			faseFornoRicott.getCaratteristiche().add(gradTerm4);

			// -- 12) 4 Temperatura Fine Rampa --
			CicloCollaudoCaratteristica tempFineRampa4 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 12,
					"4 Temperatura Fine Rampa",
					"4 Temperatura F",
					collaudo
					);
			tempFineRampa4.setIdUnitaMisura("°C");

			faseFornoRicott.getCaratteristiche().add(tempFineRampa4);

			// -- 13) 4 Permanenza --
			CicloCollaudoCaratteristica permanenza4 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 13,
					"4 Permanenza",
					"4 Permanenza",
					collaudo
					);
			permanenza4.setIdUnitaMisura("h");

			faseFornoRicott.getCaratteristiche().add(permanenza4);

		}else {
			String currentProcedura = ciclo.getRiferimentoProcedura() != null ? ciclo.getRiferimentoProcedura() : "";
			if(collaudo.getRiferimentoProeduraTtRicottura() != null && !currentProcedura.equals(collaudo.getRiferimentoProeduraTtRicottura())) {
				ciclo.setRiferimentoProcedura(collaudo.getRiferimentoProeduraTtRicottura());
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
		fase.setFaseDaStampare(true);
		fase.setNumMaxDifettiPerAccettaz(0);
		fase.setNumMinDifettiPerRifiuto(0);
		fase.setTipoCampionamento('1');
		fase.setLivelloCampionamento('2');
		fase.setLivelloIspezione('6');
		fase.setNormativaCartaCtr('1');
		fase.setUtilizzoLimitiAttesi(false);
		fase.setRiferimentoCentroTolleran(false);
		fase.setVisualizzaIdentifPezzo(true);
		return fase;
	}

	@Override
	protected CicloCollaudoCaratteristica caratteristicaCicloCollaudo(CicloCollaudoFase fase, Short seqCarat,
			String descrizione, String descrRidotta, OrdEsecAtvSogCollaudo atvTarget) {
		CicloCollaudoCaratteristica caratt = super.caratteristicaCicloCollaudo(fase, seqCarat, descrizione, descrRidotta, atvTarget);
		caratt.setLimInfTolleranza(new BigDecimal("-9999"));
		caratt.setLimSupTolleranza(new BigDecimal("9999"));
		caratt.setIdUnitaMisura("°C"); 
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
		return caratt;
	}

}
