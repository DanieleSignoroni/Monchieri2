package it.monchieri.thip.target.qualita;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;

import com.thera.thermfw.cbs.CommentHandlerLink;

import it.monchieri.thip.target.NoteAttivitaSogCollaudo;
import it.monchieri.thip.target.OrdEsecAtvSogCollaudo;
import it.monchieri.thip.target.QcTabellaAttivita;
import it.thera.thip.qualita.controllo.CicloCollaudoCaratteristica;
import it.thera.thip.qualita.controllo.CicloCollaudoFase;
import it.thera.thip.qualita.controllo.CicloCollaudoTestata;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 24/01/2025
 * <br><br>
 * <b>71XXX    DSSOF3    24/01/2025</b>
 * <p></p>
 */

public class CicloCollaudoFucinatura extends CicloCollaudoImportatore {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public CicloCollaudoTestata codificaCicloCollaudoPanthera(OrdEsecAtvSogCollaudo collaudo,
			QcTabellaAttivita attivita) throws SQLException {
		CicloCollaudoTestata ciclo = null;
		Integer idSequenzaFase = attivita.getIdSequenzaFase();
		Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

		ciclo = testataCicloCollaudo(collaudo);

		if(!ciclo.isOnDB()) {

			CicloCollaudoFase faseRiscaldo = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Riscaldo", "Riscaldo");

			NoteAttivitaSogCollaudo commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
			if(commento != null)
				codificaCommento(faseRiscaldo.getCommenti(),commento);

			ciclo.getFasi().add(faseRiscaldo);

			// 1) Temperatura Iniziale
			CicloCollaudoCaratteristica tempInizio = caratteristicaCicloCollaudo(faseRiscaldo, (short) 1, "Temperatura Iniziale", "Temperatura Ini", collaudo);
			tempInizio.setIdUnitaMisura("°C");
			faseRiscaldo.getCaratteristiche().add(tempInizio);

			// 2) 1 Gradiente Termico
			CicloCollaudoCaratteristica gradTermico1 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter", collaudo);
			gradTermico1.setIdUnitaMisura("C/H");
			faseRiscaldo.getCaratteristiche().add(gradTermico1);

			// 3) 1 Temperatura Fine Rampa
			CicloCollaudoCaratteristica tempFineRampa1 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 4, "1 Temperatura Fine Rampa", "1 Temperatura F", collaudo);
			tempFineRampa1.setIdUnitaMisura("°C");
			faseRiscaldo.getCaratteristiche().add(tempFineRampa1);

			// 4) 1 Permanenza
			CicloCollaudoCaratteristica permanenza1 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 5, "1 Permanenza", "1 Permanenza", collaudo);
			permanenza1.setIdUnitaMisura("h");
			faseRiscaldo.getCaratteristiche().add(permanenza1);

			// 5) 2 Gradiente Termico
			CicloCollaudoCaratteristica gradTermico2 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 6, "2 Gradiente Termico", "2 Gradiente Ter", collaudo);
			gradTermico2.setIdUnitaMisura("C/H");
			faseRiscaldo.getCaratteristiche().add(gradTermico2);

			// 6) 2 Temperatura Fine Rampa
			CicloCollaudoCaratteristica tempFineRampa2 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 8, "2 Temperatura Fine Rampa", "2 Temperatura F", collaudo);
			tempFineRampa2.setIdUnitaMisura("°C");
			faseRiscaldo.getCaratteristiche().add(tempFineRampa2);

			// 7) 2 Permanenza
			CicloCollaudoCaratteristica permanenza2 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 9, "2 Permanenza", "2 Permanenza", collaudo);
			permanenza2.setIdUnitaMisura("h");
			faseRiscaldo.getCaratteristiche().add(permanenza2);

			// 8) 3 Gradiente Termico
			CicloCollaudoCaratteristica gradTermico3 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 10, "3 Gradiente Termico", "3 Gradiente Ter", collaudo);
			gradTermico3.setIdUnitaMisura("C/H");
			faseRiscaldo.getCaratteristiche().add(gradTermico3);

			// 9) 3 Temperatura Fine Rampa
			CicloCollaudoCaratteristica tempFineRampa3 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 12, "3 Temperatura Fine Rampa", "3 Temperatura F", collaudo);
			tempFineRampa3.setIdUnitaMisura("°C");
			faseRiscaldo.getCaratteristiche().add(tempFineRampa3);

			// 10) 3 Permanenza
			CicloCollaudoCaratteristica permanenza3 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 13, "3 Permanenza", "3 Permanenza", collaudo);
			permanenza3.setIdUnitaMisura("h");
			faseRiscaldo.getCaratteristiche().add(permanenza3);

			// 11) 4 Gradiente Termico
			CicloCollaudoCaratteristica gradTermico4 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 14, "4 Gradiente Termico", "4 Gradiente Ter", collaudo);
			gradTermico4.setIdUnitaMisura("C/H");
			faseRiscaldo.getCaratteristiche().add(gradTermico4);

			// 12) 4 Temperatura Fine Rampa
			CicloCollaudoCaratteristica tempFineRampa4 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 16, "4 Temperatura Fine Rampa", "4 Temperatura F", collaudo);
			tempFineRampa4.setIdUnitaMisura("°C");
			faseRiscaldo.getCaratteristiche().add(tempFineRampa4);

			// 13) 4 Permanenza
			CicloCollaudoCaratteristica permanenza4 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 17, "4 Permanenza", "4 Permanenza", collaudo);
			permanenza4.setIdUnitaMisura("h");
			faseRiscaldo.getCaratteristiche().add(permanenza4);


			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseTempForgia = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Temperatura di Forgia", "Forgia");

			commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
			if(commento != null)
				codificaCommento(faseTempForgia.getCommenti(),commento);

			ciclo.getFasi().add(faseTempForgia);

			// 1) Numero Calde
			CicloCollaudoCaratteristica numeroCalde = caratteristicaCicloCollaudo(faseTempForgia, (short) 1, "Numero Calde", "Numero Calde", collaudo);
			numeroCalde.setIdUnitaMisura("nr"); 
			faseTempForgia.getCaratteristiche().add(numeroCalde);

			// 2) °C Minimi
			CicloCollaudoCaratteristica cMinimi = caratteristicaCicloCollaudo(faseTempForgia, (short) 2, "°C Minimi", "°C Minimi", collaudo);
			cMinimi.setIdUnitaMisura("°C");
			faseTempForgia.getCaratteristiche().add(cMinimi);

			// 3) °C Massimi
			CicloCollaudoCaratteristica cMassimi = caratteristicaCicloCollaudo(faseTempForgia, (short) 4, "°C Massimi", "°C Massimi", collaudo);
			cMassimi.setIdUnitaMisura("°C");

		}else {
			//Su db quindi gestisco solo i commenti
			Iterator iterFasi = ciclo.getFasi().iterator();
			while(iterFasi.hasNext()) {
				CicloCollaudoFase fase = (CicloCollaudoFase) iterFasi.next();
				NoteAttivitaSogCollaudo commento = collaudo.commentoAttivita(Integer.valueOf(fase.getSequenzaFase()), Integer.valueOf(fase.getSequenzaVisualizzazione()));
				if(commento != null) {
					Iterator iterCommenti = fase.getCommenti().getCommentHandlerLinks().iterator();
					while(iterCommenti.hasNext()) {
						CommentHandlerLink comLink = (CommentHandlerLink) iterCommenti.next();
						if(!comLink.getComment().getText().equals(commento.getNlsCommentText())) {
							int rc = comLink.delete();
							if(rc > 0) {
								codificaCommento(fase.getCommenti(),commento);
								ciclo.setDirty();
							}
						}
					}
				}
			}
		}
		return ciclo;
	}
	
	@Override
	protected CicloCollaudoFase faseCicloCollaudo(CicloCollaudoTestata testata, Short seqFase, Short seqVisualizz,
			String descrizione, String descrRidotta) {
		CicloCollaudoFase fase = super.faseCicloCollaudo(testata, seqFase, seqVisualizz, descrizione, descrRidotta);
		fase.setCampionamento(CicloCollaudoFase.NUMERICO);
		fase.setTipoFrequenzaControllo(CicloCollaudoFase.FREQ_CTR_PEZZI_ENT);	
		fase.setValNominaleObbligatorio(false);
		fase.setControlloEsterno(false);
		fase.setFaseDaStampare(true);
		fase.setLivelloCampionamento(CicloCollaudoFase.NUMERICO);
		fase.setLivelloIspezione(CicloCollaudoFase.LIVELLO_GEN_2);
		fase.setNormativaCartaCtr(CicloCollaudoFase.UNI);
		fase.setUtilizzoLimitiAttesi(false);
		fase.setRiferimentoCentroTolleran(false);
		fase.setVisualizzaIdentifPezzo(true);
		fase.setFaseObbligatoria(false);
		return fase;
	}

	@Override
	protected CicloCollaudoCaratteristica caratteristicaCicloCollaudo(CicloCollaudoFase fase, Short seqCarat,
			String descrizione, String descrRidotta, OrdEsecAtvSogCollaudo atvTarget) {
		CicloCollaudoCaratteristica caratteristica = super.caratteristicaCicloCollaudo(fase, seqCarat, descrizione, descrRidotta, atvTarget);
		caratteristica.setLimInfTolleranza(new BigDecimal("-9999"));
		caratteristica.setLimSupTolleranza(new BigDecimal("9999"));
		caratteristica.setMasterLivelloRis('3');
		caratteristica.setStrMisuraLivelloRis1('3');
		caratteristica.setStrMisuraLivelloRis2('3');
		return caratteristica;
	}

}
