package it.monchieri.thip.target.qualita;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;

import com.thera.thermfw.cbs.Comment;
import com.thera.thermfw.cbs.CommentHandlerLink;
import com.thera.thermfw.persist.Factory;

import it.monchieri.thip.target.NoteAttivitaSogCollaudo;
import it.monchieri.thip.target.OrdEsecAtvSogCollaudo;
import it.monchieri.thip.target.QcTabellaAttivita;
import it.thera.thip.qualita.controllo.CicloCollaudoCaratteristica;
import it.thera.thip.qualita.controllo.CicloCollaudoFase;
import it.thera.thip.qualita.controllo.CicloCollaudoTestata;

public class CicloCollaudoPreliminare extends CicloCollaudoImportatore {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public CicloCollaudoTestata codificaCicloCollaudoPanthera(OrdEsecAtvSogCollaudo collaudo,
			QcTabellaAttivita attivita) throws SQLException {
		CicloCollaudoTestata ciclo = null;
		Integer idSequenzaFase = attivita.getIdSequenzaFase();
		Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

		ciclo = testataCicloCollaudo(collaudo);

		String noteCiclo = collaudo.getNote_C03_TT_PRELIMINARE();

		if(!ciclo.isOnDB()) {

			if(noteCiclo != null) {
				NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
				note.setNlsCommentText(noteCiclo);
				note.setDescription("SGQ_CICLI_TES");
				note.setIdCommentUse("SGQ_CICLI_TES");
				codificaCommento(ciclo.getCommenti(), note);
			}

			CicloCollaudoFase faseTT1 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "1-Trattamento Termico", "TT_QUALITA");

			if(collaudo.getNoteFaseControlloTtPreliminare().containsKey(idSequenzaFase.toString())) {
				faseTT1.setNote(collaudo.getNoteFaseControlloTtPreliminare().get(idSequenzaFase.toString()));
			}

			NoteAttivitaSogCollaudo commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
			if(commento != null)
				codificaCommento(faseTT1.getCommenti(),commento);

			ciclo.getFasi().add(faseTT1);

			faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 1, "Temperatura Iniziale", "Temperatura ini",collaudo));
			faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter",collaudo));
			faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F",collaudo));
			faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 4, "1 Permanenza", "1 Permanenza",collaudo));
			faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter",collaudo));
			faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F",collaudo));
			faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 7, "2 Permanenza", "2 Permanenza",collaudo));
			faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter",collaudo));
			faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F",collaudo));
			faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 10, "3 Permanenza", "3 Permanenza",collaudo));
			faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter",collaudo));
			faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F",collaudo));
			faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 13, "4 Permanenza", "4 Permanenza",collaudo));

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseTT2 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "2-Trattamento Termico", "TT_QUALITA");

			if(collaudo.getNoteFaseControlloTtPreliminare().containsKey(idSequenzaFase.toString())) {
				faseTT2.setNote(collaudo.getNoteFaseControlloTtPreliminare().get(idSequenzaFase.toString()));
			}

			commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
			if(commento != null)
				codificaCommento(faseTT2.getCommenti(),commento);

			ciclo.getFasi().add(faseTT2);

			faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 1, "Temperatura Iniziale", "Temperatura ini",collaudo));
			faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter",collaudo));
			faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F",collaudo));
			faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 4, "1 Permanenza", "1 Permanenza",collaudo));
			faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter",collaudo));
			faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F",collaudo));
			faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 7, "2 Permanenza", "2 Permanenza",collaudo));
			faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter",collaudo));
			faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F",collaudo));
			faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 10, "3 Permanenza", "3 Permanenza",collaudo));
			faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter",collaudo));
			faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F",collaudo));
			faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 13, "4 Permanenza", "4 Permanenza",collaudo));

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseTT3 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "3-Trattamento Termico", "TT_QUALITA");

			if(collaudo.getNoteFaseControlloTtPreliminare().containsKey(idSequenzaFase.toString())) {
				faseTT3.setNote(collaudo.getNoteFaseControlloTtPreliminare().get(idSequenzaFase.toString()));
			}

			commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
			if(commento != null)
				codificaCommento(faseTT3.getCommenti(),commento);

			ciclo.getFasi().add(faseTT3);

			faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 1, "Temperatura Iniziale", "Temperatura ini",collaudo));
			faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter",collaudo));
			faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F",collaudo));
			faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 4, "1 Permanenza", "1 Permanenza",collaudo));
			faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter",collaudo));
			faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F",collaudo));
			faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 7, "2 Permanenza", "2 Permanenza",collaudo));
			faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter",collaudo));
			faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F",collaudo));
			faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 10, "3 Permanenza", "3 Permanenza",collaudo));
			faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter",collaudo));
			faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F",collaudo));
			faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 13, "4 Permanenza", "4 Permanenza",collaudo));

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseTT4 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "4-Trattamento Termico", "TT_QUALITA");

			if(collaudo.getNoteFaseControlloTtPreliminare().containsKey(idSequenzaFase.toString())) {
				faseTT4.setNote(collaudo.getNoteFaseControlloTtPreliminare().get(idSequenzaFase.toString()));
			}

			commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
			if(commento != null)
				codificaCommento(faseTT4.getCommenti(),commento);

			ciclo.getFasi().add(faseTT4);

			faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 1, "Temperatura Iniziale", "Temperatura ini",collaudo));
			faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter",collaudo));
			faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F",collaudo));
			faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 4, "1 Permanenza", "1 Permanenza",collaudo));
			faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter",collaudo));
			faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F",collaudo));
			faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 7, "2 Permanenza", "2 Permanenza",collaudo));
			faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter",collaudo));
			faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F",collaudo));
			faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 10, "3 Permanenza", "3 Permanenza",collaudo));
			faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter",collaudo));
			faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F",collaudo));
			faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 13, "4 Permanenza", "4 Permanenza",collaudo));

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Kg", "TT_QUALITA");

			if(collaudo.getNoteFaseControlloTtPreliminare().containsKey(idSequenzaFase.toString())) {
				faseDurezzaKg.setNote(collaudo.getNoteFaseControlloTtPreliminare().get(idSequenzaFase.toString()));
			}

			commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
			if(commento != null)
				codificaCommento(faseDurezzaKg.getCommenti(),commento);

			ciclo.getFasi().add(faseDurezzaKg);

			faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 1, "Durezza Kg", "Durezza Kg",collaudo));
			faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 2, "Durezza Kg", "Durezza Kg",collaudo));
			faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 3, "Durezza Kg", "Durezza Kg",collaudo));
			faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 4, "Durezza Kg", "Durezza Kg",collaudo));
			faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 5, "Durezza Kg", "Durezza Kg",collaudo));
			faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 6, "Durezza Kg", "Durezza Kg",collaudo));

		}else {
			if(noteCiclo != null) {
				//Gestione commenti testata
				Iterator iterCommenti = ciclo.getCommenti().getCommentHandlerLinks().iterator();
				while(iterCommenti.hasNext()) {
					CommentHandlerLink link = (CommentHandlerLink) iterCommenti.next();
					Comment commento = link.getComment();
					if(!commento.getText().equals(noteCiclo)){
						NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
						note.setNlsCommentText(noteCiclo);
						note.setDescription("SGQ_CICLI_TES");
						note.setIdCommentUse("SGQ_CICLI_TES");
						codificaCommento(ciclo.getCommenti(), note);
					}
				}

				//Gestione commenti fasi
				Iterator iterFasi = ciclo.getFasi().iterator();
				while(iterFasi.hasNext()) {
					CicloCollaudoFase fase = (CicloCollaudoFase) iterFasi.next();
					NoteAttivitaSogCollaudo commento = collaudo.commentoAttivita(Integer.valueOf(fase.getSequenzaFase()), Integer.valueOf(fase.getSequenzaVisualizzazione()));
					if(commento != null) {
						iterCommenti = fase.getCommenti().getCommentHandlerLinks().iterator();
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
		fase.setFaseDaStampare(true);
		fase.setQtaDaControllare(BigDecimal.ONE);
		fase.setControlloEsterno(false);
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
		caratt.setPercDaControllare(BigDecimal.ONE);
		return caratt;
	}

}
