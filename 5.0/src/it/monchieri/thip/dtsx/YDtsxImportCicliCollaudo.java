package it.monchieri.thip.dtsx;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.batch.BatchRunnable;
import com.thera.thermfw.cbs.Comment;
import com.thera.thermfw.cbs.CommentHandler;
import com.thera.thermfw.cbs.CommentHandlerLink;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionDescriptor;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.ErrorCodes;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.security.Authorizable;

import it.monchieri.thip.target.ImportazioneCicliControlloHelper;
import it.monchieri.thip.target.ImportazioneCicliControlloStatementHelper;
import it.monchieri.thip.target.NoteAttivitaSogCollaudo;
import it.monchieri.thip.target.OrdEsecAtvSogCollaudo;
import it.monchieri.thip.target.OrdEsecAtvSogCollaudoDatiCaratteristica;
import it.monchieri.thip.target.OrdEsecAtvSogCollaudoPO;
import it.monchieri.thip.target.QcTabellaAttivita;
import it.monchieri.thip.target.TargetCaratteristicheStatement;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.cs.DatiComuniEstesi;
import it.thera.thip.produzione.ordese.OrdineEsecutivo;
import it.thera.thip.produzione.ordese.OrdineEsecutivoTM;
import it.thera.thip.qualita.controllo.CicloCollaudoCaratteristica;
import it.thera.thip.qualita.controllo.CicloCollaudoFase;
import it.thera.thip.qualita.controllo.CicloCollaudoTestata;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 20/12/2024
 * <br><br>
 * <b>71XXX	DSSOF3	20/12/2024</b>
 * <p></p>
 */

public class YDtsxImportCicliCollaudo extends BatchRunnable implements Authorizable {

	public static final String NOME_DB_EXT = "PantheraTarget";
	public static final String UTENTE_DB_EXT = "Panthera";
	public static final String PWD_DB_EXT = "panthera";
	public static final String SRV_DB_EXT = "SRVDB.fmonchieri.locale";
	public static final String PORTA_DB_EXT = "1433";

	List<QcTabellaAttivita> attivitaTarget = null;

	@Override
	protected boolean run() {
		output.println(" ** Importazione cicli di controllo da "+NOME_DB_EXT);
		boolean isOk = true;
		try {
			List<OrdEsecAtvSogCollaudo> collaudi = leggiCollaudi();
			if(collaudi != null && attivitaTarget != null) {
				Map<String, List<OrdEsecAtvSogCollaudoPO>> collaudiRaggruppati = raggruppaCollaudiRigaOrdine(collaudi);
				importazioneCicliControllo(collaudiRaggruppati);
			}
		}catch (Exception e) {
			output.println(e.getMessage());
			isOk = false;
			e.printStackTrace(Trace.excStream);
		}
		output.println(" ** Termine importazione cicli di controllo da "+NOME_DB_EXT);
		return isOk;
	}

	protected  Map<String, List<OrdEsecAtvSogCollaudoPO>> raggruppaCollaudiRigaOrdine(List<OrdEsecAtvSogCollaudo> collaudi) {
		Map<String, List<OrdEsecAtvSogCollaudoPO>> groupedByOrdCliRiga = collaudi.stream()
				.collect(Collectors.groupingBy(OrdEsecAtvSogCollaudoPO::getOrdCliRiga));
		return groupedByOrdCliRiga;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void importazioneCicliControllo(Map<String, List<OrdEsecAtvSogCollaudoPO>> collaudiRaggruppati) {
		for (Map.Entry<String, List<OrdEsecAtvSogCollaudoPO>> entry : collaudiRaggruppati.entrySet()) {
			List<OrdEsecAtvSogCollaudoPO> collaudi = entry.getValue();
			for (Iterator iterator = collaudi.iterator(); iterator.hasNext();) {
				OrdEsecAtvSogCollaudo collaudo = (OrdEsecAtvSogCollaudo) iterator.next();
				String tipoCiclo = collaudo.getNomeTabella();
				CicloCollaudoTestata ciclo = null;
				try {
					switch (tipoCiclo) {
					case "C02_FUCINATURA":
						QcTabellaAttivita attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							ciclo = gestioneCicloFucinatura(attivita,collaudo);
						}
						break;
					case "C03_CONTROLLO_GREZZO_POST_FORGI":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							ciclo = gestioneCicloControlloGrezzoPostForgia(attivita,collaudo);
						}

						break;
					case "C03_CONTROLLO_HB_SU_FACCIA_DI_T": 
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							ciclo = gestioneCicloControlloHbSuFacciaT(collaudo,attivita);
						}
						break;
					case "C03_CONTROLLO_POST-LM1":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();
							ciclo = testataCicloCollaudo(collaudo);

							if(!ciclo.isOnDB()) {

								CicloCollaudoFase faseDimensionale = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Dimensionale", "Dimensionale");

								ciclo.getFasi().add(faseDimensionale);

								faseDimensionale.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDimensionale, (short) 1, "Positivo / Negativo", "Pos. / Neg.",collaudo));

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

							}

						}
						break;
					case "C03_CONTROLLO_QUALITATIVO_POST":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							ciclo = gestioneCicloControlloQualitativoPost(collaudo,attivita);
						}
						break;
					case "C03_TT_DI_RICOTTURA": 
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							ciclo = gestioneCicloRicottura(collaudo,attivita);

						}
						break;
					case "C03_TT-PRELIMINARE": 
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
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
											int rc = fase.getCommenti().delete();
											if(rc > 0) {
												codificaCommento(fase.getCommenti(),commento);
												ciclo.setDirty();
											}
										}
									}
								}
							}

						}
						break;
					case "C03_TT-QUALITA":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);
							String noteCiclo = collaudo.getNote_C03_TT_QUALITA();
							if(!ciclo.isOnDB()) {

								if(collaudo.getRiferimentoProceduraTtQualita() != null) {
									ciclo.setRiferimentoProcedura(collaudo.getRiferimentoProceduraTtQualita());
								}

								if(noteCiclo != null) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(noteCiclo);
									note.setDescription("SGQ_CICLI_TES");
									note.setIdCommentUse("SGQ_CICLI_TES");
									codificaCommento(ciclo.getCommenti(), note);
								}

								CicloCollaudoFase faseTT1 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "1-Trattamento Termico", "TT_QUALITA");

								NoteAttivitaSogCollaudo commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
								if(commento != null)
									codificaCommento(faseTT1.getCommenti(),commento);

								if(collaudo.getNoteFaseTtQualita().containsKey(idSequenzaFase.toString())) {
									faseTT1.setNote(collaudo.getNoteFaseTtQualita().get(idSequenzaFase.toString()));
								}

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

								if(collaudo.getNoteFaseTtQualita().containsKey(idSequenzaFase.toString())) {
									faseTT2.setNote(collaudo.getNoteFaseTtQualita().get(idSequenzaFase.toString()));
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

								if(collaudo.getNoteFaseTtQualita().containsKey(idSequenzaFase.toString())) {
									faseTT3.setNote(collaudo.getNoteFaseTtQualita().get(idSequenzaFase.toString()));
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

								if(collaudo.getNoteFaseTtQualita().containsKey(idSequenzaFase.toString())) {
									faseTT4.setNote(collaudo.getNoteFaseTtQualita().get(idSequenzaFase.toString()));
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

								idSequenzaFase = idSequenzaFase+3;
								idSequenzaVisualizz = idSequenzaVisualizz+3;

								CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Kg", "TT_QUALITA");

								commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
								if(commento != null)
									codificaCommento(faseDurezzaKg.getCommenti(),commento);

								if(collaudo.getNoteFaseTtQualita().containsKey(idSequenzaFase.toString())) {
									faseDurezzaKg.setNote(collaudo.getNoteFaseTtQualita().get(idSequenzaFase.toString()));
								}

								ciclo.getFasi().add(faseDurezzaKg);

								faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 1, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 2, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 3, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 4, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 5, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 6, "Durezza Kg", "Durezza Kg",collaudo));

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

								CicloCollaudoFase faseDimensionalePostLM1 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Dimensionale post LM1", "TT_QUALITA");

								if(collaudo.getNoteFaseTtQualita().containsKey(idSequenzaFase.toString())) {
									faseDimensionalePostLM1.setNote(collaudo.getNoteFaseTtQualita().get(idSequenzaFase.toString()));
								}

								commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
								if(commento != null)
									codificaCommento(faseDimensionalePostLM1.getCommenti(),commento);

								ciclo.getFasi().add(faseDimensionalePostLM1);

								faseDimensionalePostLM1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDimensionalePostLM1, (short) 1, "Positivo / Negativo", "Pos. / Neg.",collaudo));

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
											int rc = fase.getCommenti().delete();
											if(rc > 0) {
												codificaCommento(fase.getCommenti(),commento);
												ciclo.setDirty();
											}
										}
									}
								}
							}

						}
						break;
					case "C03_TT-SUPPLEMENTARE":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							String noteCiclo = collaudo.getNote_C03_TT_SUPPLEMENTARE();

							if(!ciclo.isOnDB()) {

								if(collaudo.getRiferimentoProcedutaTtSupplementare() != null) {
									ciclo.setNote(collaudo.getRiferimentoProcedutaTtSupplementare());
								}

								if(noteCiclo != null) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(noteCiclo);
									note.setDescription("SGQ_CICLI_TES");
									note.setIdCommentUse("SGQ_CICLI_TES");
									codificaCommento(ciclo.getCommenti(), note);
								}

								CicloCollaudoFase faseTT1 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "1-Trattamento Termico", "TT_QUALITA");

								if(collaudo.getNoteFaseControlloTtSupplementare().containsKey(idSequenzaFase.toString())) {
									faseTT1.setNote(collaudo.getNoteFaseControlloTtSupplementare().get(idSequenzaFase.toString()));
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

								if(collaudo.getNoteFaseControlloTtSupplementare().containsKey(idSequenzaFase.toString())) {
									faseTT2.setNote(collaudo.getNoteFaseControlloTtSupplementare().get(idSequenzaFase.toString()));
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

								if(collaudo.getNoteFaseControlloTtSupplementare().containsKey(idSequenzaFase.toString())) {
									faseTT3.setNote(collaudo.getNoteFaseControlloTtSupplementare().get(idSequenzaFase.toString()));
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

								if(collaudo.getNoteFaseControlloTtSupplementare().containsKey(idSequenzaFase.toString())) {
									faseTT4.setNote(collaudo.getNoteFaseControlloTtSupplementare().get(idSequenzaFase.toString()));
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

								idSequenzaFase = idSequenzaFase+3;
								idSequenzaVisualizz = idSequenzaVisualizz+3;

								CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Kg", "TT_QUALITA");

								if(collaudo.getNoteFaseControlloTtSupplementare().containsKey(idSequenzaFase.toString())) {
									faseDurezzaKg.setNote(collaudo.getNoteFaseControlloTtSupplementare().get(idSequenzaFase.toString()));
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

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

								CicloCollaudoFase faseDurezzaHB = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza HB", "TT_QUALITA");

								if(collaudo.getNoteFaseControlloTtSupplementare().containsKey(idSequenzaFase.toString())) {
									faseDurezzaHB.setNote(collaudo.getNoteFaseControlloTtSupplementare().get(idSequenzaFase.toString()));
								}

								commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
								if(commento != null)
									codificaCommento(faseDurezzaHB.getCommenti(),commento);

								ciclo.getFasi().add(faseDurezzaHB);

								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 1, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 2, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 3, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 4, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 5, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 6, "Durezza HB", "Durezza HB",collaudo));

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
											int rc = fase.getCommenti().delete();
											if(rc > 0) {
												codificaCommento(fase.getCommenti(),commento);
												ciclo.setDirty();
											}
										}
									}
								}
							}

						}
						break;
					case "C03_UT_PRE-TT":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							if(collaudo.getRiferimentoProceduraUtPreTT() != null) {
								ciclo.setNote(collaudo.getRiferimentoProceduraUtPreTT());
							}

							CicloCollaudoFase faseControlloUT = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Controllo UT", "Ctrl UT");

							ciclo.getFasi().add(faseControlloUT);

							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 1, "Positivo / Negativo", "Pos. / Neg.",collaudo));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 2, "1 MDR", "1 MDR",collaudo));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 3, "1 Spessore", "1 Spessore",collaudo));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 4, "2 MDR", "2 MDR",collaudo));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 5, "2 Spessore", "2 Spessore",collaudo));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 6, "3 MDR", "3 MDR",collaudo));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 7, "3 Spessore", "3 Spessore",collaudo));
						}
						break;
					case "C05_CONTROLLO_LABORATORIO":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);
							CicloCollaudoFase faseAnalisiChimica = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Analisi Chimica", "Analisi Chimica");

							ciclo.getFasi().add(faseAnalisiChimica);

							faseAnalisiChimica.getCaratteristiche().add(caratteristicaCicloCollaudo(faseAnalisiChimica, (short) 1, "Analisi Chimica", "Analisi Chimica",collaudo));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseAnalisiProdotto = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Analisi Prodotto", "Analisi Prodott");

							ciclo.getFasi().add(faseAnalisiProdotto);

							faseAnalisiProdotto.getCaratteristiche().add(caratteristicaCicloCollaudo(faseAnalisiProdotto, (short) 1, "Analisi Chimica", "Analisi Chimica",collaudo));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseAnalisiTrazioneFornitura = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Trazione fornitura", "Trazione fornit");

							ciclo.getFasi().add(faseAnalisiTrazioneFornitura);

							faseAnalisiTrazioneFornitura.getCaratteristiche().add(caratteristicaCicloCollaudo(faseAnalisiTrazioneFornitura, (short) 1, "Trazione fornitura", "Trazione fornit",collaudo));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseAnalisiResilienzaFornitura = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Resilienza fornitura", "Resilienza forn");

							ciclo.getFasi().add(faseAnalisiResilienzaFornitura);

							faseAnalisiResilienzaFornitura.getCaratteristiche().add(caratteristicaCicloCollaudo(faseAnalisiResilienzaFornitura, (short) 1, "Resilienza fornitura", "Resilienza forn",collaudo));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase microGrano = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Micro Grano", "Micro Grano");

							ciclo.getFasi().add(microGrano);

							microGrano.getCaratteristiche().add(caratteristicaCicloCollaudo(microGrano, (short) 1, "Micro Grano", "Micro Grano",collaudo));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase creep = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Creep", "Creep");

							ciclo.getFasi().add(creep);

							creep.getCaratteristiche().add(caratteristicaCicloCollaudo(creep, (short) 1, "Creep", "Creep",collaudo));

						}
						break;
					case "C05_CONTROLLO_LABORATORIO_TR":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);
							CicloCollaudoFase faseTensioniResidue = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Tensioni Residue", "Tensioni Residue");

							ciclo.getFasi().add(faseTensioniResidue);

							faseTensioniResidue.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTensioniResidue, (short) 1, "Analisi Chimica", "Analisi Chimica",collaudo));
						}
						break;
					case "C06_CONTROLLI_NDT":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							if(!ciclo.isOnDB()) {

								CicloCollaudoFase faseControlloUT = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Ultrasuoni", "Ultrasuoni");

								if(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT() != null && collaudo.getDescrizioneFase_C06_CONTROLLI_NDT().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseControlloUT.getCommenti(), note);
								}

								if(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_1() != null && collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_1().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_1().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseControlloUT.getCommenti(), note);
								}

								if(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_2() != null && collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_2().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_2().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseControlloUT.getCommenti(), note);
								}

								if(collaudo.getNoteFaseControlloNDT().containsKey(idSequenzaFase.toString())) {
									faseControlloUT.setNote(collaudo.getNoteFaseControlloNDT().get(idSequenzaFase.toString()));
								}

								ciclo.getFasi().add(faseControlloUT);

								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 1, "Positivo / Negativo", "Pos. / Neg.",collaudo));
								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 3, "1 MDR", "1 MDR",collaudo));
								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 2, "1 Spessore", "1 Spessore",collaudo));
								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 5, "2 MDR", "2 MDR",collaudo));
								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 4, "2 Spessore", "2 Spessore",collaudo));
								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 7, "3 MDR", "3 MDR",collaudo));
								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 6, "3 Spessore", "3 Spessore",collaudo));

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

								CicloCollaudoFase faseMagnetico = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Magnetico", "Magnetico");

								if(collaudo.getNoteFaseControlloNDT().containsKey(idSequenzaFase.toString())) {
									faseMagnetico.setNote(collaudo.getNoteFaseControlloNDT().get(idSequenzaFase.toString()));
								}

								if(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT() != null && collaudo.getDescrizioneFase_C06_CONTROLLI_NDT().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseMagnetico.getCommenti(), note);
								}

								if(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_1() != null && collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_1().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_1().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseMagnetico.getCommenti(), note);
								}

								if(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_2() != null && collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_2().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_2().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseMagnetico.getCommenti(), note);
								}


								ciclo.getFasi().add(faseMagnetico);

								faseMagnetico.getCaratteristiche().add(caratteristicaCicloCollaudo(faseMagnetico, (short) 1, "Positivo / Negativo", "Pos./Neg.",collaudo));

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

								CicloCollaudoFase faseLiquidiPenetranti = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Liquidi Penetranti", "Liquidi Penetra");

								if(collaudo.getNoteFaseControlloNDT().containsKey(idSequenzaFase.toString())) {
									faseLiquidiPenetranti.setNote(collaudo.getNoteFaseControlloNDT().get(idSequenzaFase.toString()));
								}

								if(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT() != null && collaudo.getDescrizioneFase_C06_CONTROLLI_NDT().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseLiquidiPenetranti.getCommenti(), note);
								}

								if(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_1() != null && collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_1().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_1().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseLiquidiPenetranti.getCommenti(), note);
								}

								if(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_2() != null && collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_2().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_2().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseLiquidiPenetranti.getCommenti(), note);
								}

								ciclo.getFasi().add(faseLiquidiPenetranti);

								faseLiquidiPenetranti.getCaratteristiche().add(caratteristicaCicloCollaudo(faseLiquidiPenetranti, (short) 1, "Positivo / Negativo", "Pos./Neg.",collaudo));

							}else {
								//Gestione commenti fasi
								Iterator iterFasi = ciclo.getFasi().iterator();
								while(iterFasi.hasNext()) {
									CicloCollaudoFase fase = (CicloCollaudoFase) iterFasi.next();
									NoteAttivitaSogCollaudo commento = null;
									if(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT() != null && collaudo.getDescrizioneFase_C06_CONTROLLI_NDT().containsKey(idSequenzaFase)) {
										NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
										note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT().get(idSequenzaFase));
										note.setDescription("SGQ_CICLI_FAS");
										note.setIdCommentUse("SGQ_CICLI_FAS");
										commento = note;
									}

									if(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_1() != null && collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_1().containsKey(idSequenzaFase)) {
										NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
										note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_1().get(idSequenzaFase));
										note.setDescription("SGQ_CICLI_FAS");
										note.setIdCommentUse("SGQ_CICLI_FAS");
										commento = note;
									}

									if(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_2() != null && collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_2().containsKey(idSequenzaFase)) {
										NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
										note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT_2().get(idSequenzaFase));
										note.setDescription("SGQ_CICLI_FAS");
										note.setIdCommentUse("SGQ_CICLI_FAS");
										commento = note;
									}

									if(commento != null) {
										int rc = fase.getCommenti().delete();
										if(rc > 0) {
											codificaCommento(fase.getCommenti(),commento);
											ciclo.setDirty();
										}
									}
								}
							}
						}
						break;
					case "C06_CONTROLLO_INTERMEDIO":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							if(!ciclo.isOnDB()) {

								if(collaudo.getRiferimentoProceduraControlloIntermedio() != null) {
									ciclo.setRiferimentoProcedura(collaudo.getRiferimentoProceduraControlloIntermedio());
								}

								CicloCollaudoFase faseDimensionale = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Dimensionale", "Dimensionale");

								if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseDimensionale.getCommenti(), note);
								}

								if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseDimensionale.getCommenti(), note);
								}

								ciclo.getFasi().add(faseDimensionale);

								faseDimensionale.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDimensionale, (short) 1, "Positivo / Negativo", "Pos. / Neg.",collaudo));

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

								CicloCollaudoFase faseControlloUT = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Ultrasuoni", "Ultrasuoni");

								if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseControlloUT.getCommenti(), note);
								}

								if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseControlloUT.getCommenti(), note);
								}
								ciclo.getFasi().add(faseControlloUT);

								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 1, "Positivo / Negativo", "Pos. / Neg.",collaudo));
								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 3, "1 MDR", "1 MDR",collaudo));
								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 2, "1 Spessore", "1 Spessore",collaudo));
								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 5, "2 MDR", "2 MDR",collaudo));
								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 4, "2 Spessore", "2 Spessore",collaudo));
								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 7, "3 MDR", "3 MDR",collaudo));
								faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 6, "3 Spessore", "3 Spessore",collaudo));

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

								CicloCollaudoFase faseDurezzaHB = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza HB", "TT_QUALITA");

								if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseDurezzaHB.getCommenti(), note);
								}

								if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseDurezzaHB.getCommenti(), note);
								}
								ciclo.getFasi().add(faseDurezzaHB);

								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 1, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 2, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 3, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 4, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 5, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 6, "Durezza HB", "Durezza HB",collaudo));

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

								CicloCollaudoFase faseMagnetico = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Magnetico", "Magnetico");

								if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseMagnetico.getCommenti(), note);
								}

								if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseMagnetico.getCommenti(), note);
								}
								ciclo.getFasi().add(faseMagnetico);

								faseMagnetico.getCaratteristiche().add(caratteristicaCicloCollaudo(faseMagnetico, (short) 1, "Positivo / Negativo", "Pos./Neg.",collaudo));

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

								CicloCollaudoFase faseLiquidiPenetranti = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Liquidi Penetranti", "Liquidi Penetra");

								if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseLiquidiPenetranti.getCommenti(), note);
								}

								if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseLiquidiPenetranti.getCommenti(), note);
								}

								ciclo.getFasi().add(faseLiquidiPenetranti);

								faseLiquidiPenetranti.getCaratteristiche().add(caratteristicaCicloCollaudo(faseLiquidiPenetranti, (short) 1, "Positivo / Negativo", "Pos./Neg.",collaudo));

							}else {
								//Gestione commenti fasi
								Iterator iterFasi = ciclo.getFasi().iterator();
								while(iterFasi.hasNext()) {
									CicloCollaudoFase fase = (CicloCollaudoFase) iterFasi.next();
									NoteAttivitaSogCollaudo commento = null;
									if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().containsKey(idSequenzaFase)) {
										NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
										note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().get(idSequenzaFase));
										note.setDescription("SGQ_CICLI_FAS");
										note.setIdCommentUse("SGQ_CICLI_FAS");
										commento = note;
									}

									if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().containsKey(idSequenzaFase)) {
										NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
										note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().get(idSequenzaFase));
										note.setDescription("SGQ_CICLI_FAS");
										note.setIdCommentUse("SGQ_CICLI_FAS");
										commento = note;
									}

									if(commento != null) {
										int rc = fase.getCommenti().delete();
										if(rc > 0) {
											codificaCommento(fase.getCommenti(),commento);
											ciclo.setDirty();
										}
									}
								}
							}
						}
						break;
					case "C02_SBLUMATURA":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							CicloCollaudoFase faseTT1 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "1-Trattamento Termico", "TT_QUALITA");

							ciclo.getFasi().add(faseTT1);

							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 1, "Temperatura Iniziale", "Temperatura ini",collaudo));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter",collaudo));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 4, "1 Temperatura Fine Rampa", "1 Temperatura F",collaudo));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 5, "1 Permanenza", "1 Permanenza",collaudo));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 6, "2 Gradiente Termico", "2 Gradiente Ter",collaudo));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 8, "2 Temperatura Fine Rampa", "2 Temperatura F",collaudo));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 9, "2 Permanenza", "2 Permanenza",collaudo));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 10, "3 Gradiente Termico", "3 Gradiente Ter",collaudo));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 12, "3 Temperatura Fine Rampa", "3 Temperatura F",collaudo));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 13, "3 Permanenza", "3 Permanenza",collaudo));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 14, "4 Gradiente Termico", "4 Gradiente Ter",collaudo));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 16, "4 Temperatura Fine Rampa", "4 Temperatura F",collaudo));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 17, "4 Permanenza", "4 Permanenza",collaudo));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseTaempF = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Temperatura di Forgia", "Forgia");

							ciclo.getFasi().add(faseTaempF);

							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 1, "1 Fase  1 °C Minima", "Fase  1 °C Mini",collaudo));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 2, "1 Fase  1 °C Massima", "Fase  1 °C Mass",collaudo));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 3, "1 Fase  1 °C Minima", "Fase  1 °C Mini",collaudo));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 4, "1 Fase  1 °C Massima", "Fase  1 °C Mass",collaudo));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 5, "1 Fase  1 °C Minima", "Fase  1 °C Mini",collaudo));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 6, "1 Fase  1 °C Massima", "Fase  1 °C Mass",collaudo));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 7, "1 Fase  1 °C Minima", "Fase  1 °C Mini",collaudo));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 8, "1 Fase  1 °C Massima", "Fase  1 °C Mass",collaudo));

						}
						break;
					case "C06_CONTROLLO_DUREZZE-DIMENSION":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							if(!ciclo.isOnDB()) {

								if(collaudo.getRiferimentoProceduraControlloDurezzeDimensionali() != null) {
									ciclo.setNote(collaudo.getRiferimentoProceduraControlloDurezzeDimensionali());
								}

								CicloCollaudoFase faseDimensionale = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Dimensionale", "Dimensionale");

								if(collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION() != null && collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseDimensionale.getCommenti(), note);
								}

								if(collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1() != null && collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseDimensionale.getCommenti(), note);
								}

								ciclo.getFasi().add(faseDimensionale);

								faseDimensionale.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDimensionale, (short) 1, "Dimensione", "Dimensione",collaudo));

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

								CicloCollaudoFase faseDurezzaHB = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza HB", "TT_QUALITA");

								if(collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION() != null && collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseDurezzaHB.getCommenti(), note);
								}

								if(collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1() != null && collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1().containsKey(idSequenzaFase)) {
									NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
									note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1().get(idSequenzaFase));
									note.setDescription("SGQ_CICLI_FAS");
									note.setIdCommentUse("SGQ_CICLI_FAS");
									codificaCommento(faseDurezzaHB.getCommenti(), note);
								}

								ciclo.getFasi().add(faseDurezzaHB);

								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 1, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 2, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 3, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 4, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 5, "Durezza HB", "Durezza HB",collaudo));
								faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 6, "Durezza HB", "Durezza HB",collaudo));
							}else {
								//Gestione commenti fasi
								Iterator iterFasi = ciclo.getFasi().iterator();
								while(iterFasi.hasNext()) {
									CicloCollaudoFase fase = (CicloCollaudoFase) iterFasi.next();
									NoteAttivitaSogCollaudo commento = null;
									if(collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION() != null && collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION().containsKey(idSequenzaFase)) {
										NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
										note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLI_NDT().get(idSequenzaFase));
										note.setDescription("SGQ_CICLI_FAS");
										note.setIdCommentUse("SGQ_CICLI_FAS");
										commento = note;
									}

									if(collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1() != null && collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1().containsKey(idSequenzaFase)) {
										NoteAttivitaSogCollaudo note = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
										note.setNlsCommentText(collaudo.getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1().get(idSequenzaFase));
										note.setDescription("SGQ_CICLI_FAS");
										note.setIdCommentUse("SGQ_CICLI_FAS");
										commento = note;
									}

									if(commento != null) {
										int rc = fase.getCommenti().delete();
										if(rc > 0) {
											codificaCommento(fase.getCommenti(),commento);
											ciclo.setDirty();
										}
									}
								}
							}
						}
						break;
					default:
						break;
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
				if(ciclo != null) {
					try {
						if(ciclo.dirty) { //Se non e' sporco non salvo
							int rc = ciclo.save();
							if(rc > ErrorCodes.NO_ROWS_FOUND) {
								ConnectionManager.commit();
							}else {
								ConnectionManager.rollback();
							}
						}
					}catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	protected CicloCollaudoTestata gestioneCicloControlloQualitativoPost(OrdEsecAtvSogCollaudo collaudo,
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
			
			faseControlloUT.setTipoCaratteristiche('1');
			faseControlloUT.setRaccoltaDati('1');
			faseControlloUT.setCampionamento('2');
			faseControlloUT.setTipoFrequenzaControllo('6');
			faseControlloUT.setValNominaleObbligatorio(false);
			faseControlloUT.setControlloEsterno(false);
			faseControlloUT.setQtaDaControllare(BigDecimal.ONE);
			faseControlloUT.setNumMaxDifettiPerAccettaz(0);
			faseControlloUT.setNumMinDifettiPerRifiuto(1);
			faseControlloUT.setCampionamento('1');
			faseControlloUT.setLivelloCampionamento('2');
			faseControlloUT.setLivelloIspezione('6');
			faseControlloUT.setNormativaCartaCtr(DGT);

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseControlloUT.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseControlloUT);

			faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 1, "Positivo / Negativo", "Pos. / Neg.",collaudo));
			faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 2, "1 MDR", "1 MDR",collaudo));
			faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 3, "1 Spessore", "1 Spessore",collaudo));
			faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 4, "2 MDR", "2 MDR",collaudo));
			faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 5, "2 Spessore", "2 Spessore",collaudo));
			faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 6, "3 MDR", "3 MDR",collaudo));
			faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 7, "3 Spessore", "3 Spessore",collaudo));

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseControlloDimensionale = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Controllo Dimensionale", "Ctrl DIM");

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseControlloDimensionale.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseControlloDimensionale);

			faseControlloDimensionale.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloDimensionale, (short) 1, "Controllo Dimensionale", "Ctrl DIM",collaudo));

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Controllo Durezza Kg", "Ctrl HB Kg");

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseDurezzaKg.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseDurezzaKg);

			faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 1, "Durezza Kg", "Durezza Kg",collaudo));
			faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 2, "Durezza Kg", "Durezza Kg",collaudo));
			faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 3, "Durezza Kg", "Durezza Kg",collaudo));
			faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 4, "Durezza Kg", "Durezza Kg",collaudo));
			faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 5, "Durezza Kg", "Durezza Kg",collaudo));
			faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 6, "Durezza Kg", "Durezza Kg",collaudo));

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseMagnetico = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Magnetico", "Magnetico");

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseMagnetico.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseMagnetico);

			faseMagnetico.getCaratteristiche().add(caratteristicaCicloCollaudo(faseMagnetico, (short) 1, "Positivo / Negativo", "Pos./Neg.",collaudo));

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseLiquidiPenetranti = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Liquidi Penetranti", "Liquidi Penetra");

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseLiquidiPenetranti.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseLiquidiPenetranti);

			faseLiquidiPenetranti.getCaratteristiche().add(caratteristicaCicloCollaudo(faseLiquidiPenetranti, (short) 1, "Positivo / Negativo", "Pos./Neg.",collaudo));

		}
		return ciclo;
	}

	@SuppressWarnings("unchecked")
	protected CicloCollaudoTestata gestioneCicloControlloHbSuFacciaT(OrdEsecAtvSogCollaudo collaudo,
			QcTabellaAttivita attivita) throws SQLException {
		Integer idSequenzaFase = attivita.getIdSequenzaFase();
		Integer idSequenzaVisualizz = attivita.getSeqVisualizz();
		CicloCollaudoTestata ciclo = null;
		ciclo = testataCicloCollaudo(collaudo);

		if(!ciclo.isOnDB()) {

			CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Kg", "Durezza Kg");
			faseDurezzaKg.setTipoCaratteristiche('1');
			faseDurezzaKg.setRaccoltaDati('1');
			faseDurezzaKg.setCampionamento('2');
			faseDurezzaKg.setTipoFrequenzaControllo('P');
			faseDurezzaKg.setValNominaleObbligatorio(false);
			faseDurezzaKg.setControlloEsterno(false);
			faseDurezzaKg.setFaseObbligatoria(false);
			faseDurezzaKg.setFaseDaStampare(true);
			faseDurezzaKg.setPercentualeDaControlla(new BigDecimal(100));
			faseDurezzaKg.setNumMaxDifettiPerAccettaz(0);
			faseDurezzaKg.setNumMinDifettiPerRifiuto(1);
			faseDurezzaKg.setTipoCampionamento('1');
			faseDurezzaKg.setLivelloCampionamento('2');
			faseDurezzaKg.setLivelloIspezione('6');
			faseDurezzaKg.setNormativaCartaCtr('1');
			faseDurezzaKg.setUtilizzoLimitiAttesi(false);
			faseDurezzaKg.setRiferimentoCentroTolleran(false);

			ciclo.getFasi().add(faseDurezzaKg);

			CicloCollaudoCaratteristica carattDurezza1 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 1, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza1.setLimInfTolleranza(new BigDecimal("-9999"));
			carattDurezza1.setLimSupTolleranza(new BigDecimal("9999"));
			carattDurezza1.setIdUnitaMisura("kg");
			carattDurezza1.setValoreNominale(BigDecimal.ZERO);
			carattDurezza1.setMasterTipoRis('S');
			carattDurezza1.setMasterLivelloRis('3');
			carattDurezza1.setStrMisuraTipoRis1('S');
			carattDurezza1.setStrMisuraLivelloRis1('3');
			carattDurezza1.setStrMisuraTipoRis2('S');
			carattDurezza1.setStrMisuraLivelloRis2('4');
			carattDurezza1.setLivelloIspezione('6');
			carattDurezza1.setTipoCartaControllo('1');
			carattDurezza1.setDefaultMasterTipoRis('S');
			carattDurezza1.setDefaultMasterLivelloRis('4');
			carattDurezza1.setDefaultTipoRis1('S');
			carattDurezza1.setObbligatorieta(true);
			carattDurezza1.setDefaultTipoRis2('S');
			carattDurezza1.setDefaultLivelloRis1('4');
			carattDurezza1.setDefaultLivelloRis2('4');
			carattDurezza1.setRilavLimInfTol(true);
			carattDurezza1.setRilavLimSupTol(true);
			carattDurezza1.setFlagRilavorabilita(true);
			carattDurezza1.setTipoCaratteristica('1');
			carattDurezza1.setClasseImportanzaCar('1');
			carattDurezza1.setPercDaControllare(new BigDecimal(100));

			faseDurezzaKg.getCaratteristiche().add(carattDurezza1);

			// Durezza 2
			CicloCollaudoCaratteristica carattDurezza2 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 2, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza2.setLimInfTolleranza(new BigDecimal("-9999"));
			carattDurezza2.setLimSupTolleranza(new BigDecimal("9999"));
			carattDurezza2.setIdUnitaMisura("kg");
			carattDurezza2.setValoreNominale(BigDecimal.ZERO);
			carattDurezza2.setMasterTipoRis('S');
			carattDurezza2.setMasterLivelloRis('3');
			carattDurezza2.setStrMisuraTipoRis1('S');
			carattDurezza2.setStrMisuraLivelloRis1('3');
			carattDurezza2.setObbligatorieta(true);
			carattDurezza2.setStrMisuraTipoRis2('S');
			carattDurezza2.setStrMisuraLivelloRis2('4');
			carattDurezza2.setLivelloIspezione('6');
			carattDurezza2.setTipoCartaControllo('1');
			carattDurezza2.setDefaultMasterTipoRis('S');
			carattDurezza2.setDefaultMasterLivelloRis('4');
			carattDurezza2.setDefaultTipoRis1('S');
			carattDurezza2.setDefaultTipoRis2('S');
			carattDurezza2.setDefaultLivelloRis1('4');
			carattDurezza2.setDefaultLivelloRis2('4');
			carattDurezza2.setRilavLimInfTol(true);
			carattDurezza2.setRilavLimSupTol(true);
			carattDurezza2.setFlagRilavorabilita(true);
			carattDurezza2.setTipoCaratteristica('1');
			carattDurezza2.setClasseImportanzaCar('1');
			carattDurezza2.setPercDaControllare(new BigDecimal(100));

			faseDurezzaKg.getCaratteristiche().add(carattDurezza2);

			// Durezza 3
			CicloCollaudoCaratteristica carattDurezza3 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 3, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza3.setLimInfTolleranza(new BigDecimal("-9999"));
			carattDurezza3.setLimSupTolleranza(new BigDecimal("9999"));
			carattDurezza3.setIdUnitaMisura("kg");
			carattDurezza3.setValoreNominale(BigDecimal.ZERO);
			carattDurezza3.setMasterTipoRis('S');
			carattDurezza3.setMasterLivelloRis('3');
			carattDurezza3.setStrMisuraTipoRis1('S');
			carattDurezza3.setStrMisuraLivelloRis1('3');
			carattDurezza3.setStrMisuraTipoRis2('S');
			carattDurezza3.setStrMisuraLivelloRis2('4');
			carattDurezza3.setLivelloIspezione('6');
			carattDurezza3.setTipoCartaControllo('1');
			carattDurezza3.setDefaultMasterTipoRis('S');
			carattDurezza3.setObbligatorieta(true);
			carattDurezza3.setDefaultMasterLivelloRis('4');
			carattDurezza3.setDefaultTipoRis1('S');
			carattDurezza3.setDefaultTipoRis2('S');
			carattDurezza3.setDefaultLivelloRis1('4');
			carattDurezza3.setDefaultLivelloRis2('4');
			carattDurezza3.setRilavLimInfTol(true);
			carattDurezza3.setRilavLimSupTol(true);
			carattDurezza3.setFlagRilavorabilita(true);
			carattDurezza3.setTipoCaratteristica('1');
			carattDurezza3.setClasseImportanzaCar('1');
			carattDurezza3.setPercDaControllare(new BigDecimal(100));

			faseDurezzaKg.getCaratteristiche().add(carattDurezza3);

			// Durezza 4
			CicloCollaudoCaratteristica carattDurezza4 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 4, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza4.setLimInfTolleranza(new BigDecimal("-9999"));
			carattDurezza4.setLimSupTolleranza(new BigDecimal("9999"));
			carattDurezza4.setIdUnitaMisura("kg");
			carattDurezza4.setValoreNominale(BigDecimal.ZERO);
			carattDurezza4.setMasterTipoRis('S');
			carattDurezza4.setMasterLivelloRis('3');
			carattDurezza4.setStrMisuraTipoRis1('S');
			carattDurezza4.setStrMisuraLivelloRis1('3');
			carattDurezza4.setStrMisuraTipoRis2('S');
			carattDurezza4.setStrMisuraLivelloRis2('4');
			carattDurezza4.setLivelloIspezione('6');
			carattDurezza4.setTipoCartaControllo('1');
			carattDurezza4.setObbligatorieta(true);
			carattDurezza4.setDefaultMasterTipoRis('S');
			carattDurezza4.setDefaultMasterLivelloRis('4');
			carattDurezza4.setDefaultTipoRis1('S');
			carattDurezza4.setDefaultTipoRis2('S');
			carattDurezza4.setDefaultLivelloRis1('4');
			carattDurezza4.setDefaultLivelloRis2('4');
			carattDurezza4.setRilavLimInfTol(true);
			carattDurezza4.setRilavLimSupTol(true);
			carattDurezza4.setFlagRilavorabilita(true);
			carattDurezza4.setTipoCaratteristica('1');
			carattDurezza4.setClasseImportanzaCar('1');
			carattDurezza4.setPercDaControllare(new BigDecimal(100));

			faseDurezzaKg.getCaratteristiche().add(carattDurezza4);

			// Durezza 5
			CicloCollaudoCaratteristica carattDurezza5 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 5, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza5.setLimInfTolleranza(new BigDecimal("-9999"));
			carattDurezza5.setLimSupTolleranza(new BigDecimal("9999"));
			carattDurezza5.setIdUnitaMisura("kg");
			carattDurezza5.setValoreNominale(BigDecimal.ZERO);
			carattDurezza5.setMasterTipoRis('S');
			carattDurezza5.setMasterLivelloRis('3');
			carattDurezza5.setStrMisuraTipoRis1('S');
			carattDurezza5.setStrMisuraLivelloRis1('3');
			carattDurezza5.setStrMisuraTipoRis2('S');
			carattDurezza5.setStrMisuraLivelloRis2('4');
			carattDurezza5.setLivelloIspezione('6');
			carattDurezza5.setTipoCartaControllo('1');
			carattDurezza5.setDefaultMasterTipoRis('S');
			carattDurezza5.setDefaultMasterLivelloRis('4');
			carattDurezza5.setDefaultTipoRis1('S');
			carattDurezza5.setDefaultTipoRis2('S');
			carattDurezza5.setObbligatorieta(true);
			carattDurezza5.setDefaultLivelloRis1('4');
			carattDurezza5.setDefaultLivelloRis2('4');
			carattDurezza5.setRilavLimInfTol(true);
			carattDurezza5.setRilavLimSupTol(true);
			carattDurezza5.setFlagRilavorabilita(true);
			carattDurezza5.setTipoCaratteristica('1');
			carattDurezza5.setClasseImportanzaCar('1');
			carattDurezza5.setPercDaControllare(new BigDecimal(100));

			faseDurezzaKg.getCaratteristiche().add(carattDurezza5);

			// Durezza 6
			CicloCollaudoCaratteristica carattDurezza6 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 6, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza6.setLimInfTolleranza(new BigDecimal("-9999"));
			carattDurezza6.setLimSupTolleranza(new BigDecimal("9999"));
			carattDurezza6.setIdUnitaMisura("kg");
			carattDurezza6.setValoreNominale(BigDecimal.ZERO);
			carattDurezza6.setMasterTipoRis('S');
			carattDurezza6.setMasterLivelloRis('3');
			carattDurezza6.setStrMisuraTipoRis1('S');
			carattDurezza6.setStrMisuraLivelloRis1('3');
			carattDurezza6.setStrMisuraTipoRis2('S');
			carattDurezza6.setStrMisuraLivelloRis2('4');
			carattDurezza6.setLivelloIspezione('6');
			carattDurezza6.setTipoCartaControllo('1');
			carattDurezza6.setObbligatorieta(true);
			carattDurezza6.setDefaultMasterTipoRis('S');
			carattDurezza6.setDefaultMasterLivelloRis('4');
			carattDurezza6.setDefaultTipoRis1('S');
			carattDurezza6.setDefaultTipoRis2('S');
			carattDurezza6.setDefaultLivelloRis1('4');
			carattDurezza6.setDefaultLivelloRis2('4');
			carattDurezza6.setRilavLimInfTol(true);
			carattDurezza6.setRilavLimSupTol(true);
			carattDurezza6.setFlagRilavorabilita(true);
			carattDurezza6.setTipoCaratteristica('1');
			carattDurezza6.setClasseImportanzaCar('1');
			carattDurezza6.setPercDaControllare(new BigDecimal(100));

			faseDurezzaKg.getCaratteristiche().add(carattDurezza6);

		}else {

		}

		return ciclo;
	}

	@SuppressWarnings("unchecked")
	protected CicloCollaudoTestata gestioneCicloControlloGrezzoPostForgia(QcTabellaAttivita attivita,
			OrdEsecAtvSogCollaudo collaudo) throws SQLException {
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
			faseDimensionale.setTipoCaratteristiche('1');
			faseDimensionale.setRaccoltaDati('1');
			faseDimensionale.setCampionamento('2');
			faseDimensionale.setTipoFrequenzaControllo('P');
			faseDimensionale.setValNominaleObbligatorio(false);
			faseDimensionale.setControlloEsterno(false);
			faseDimensionale.setFaseObbligatoria(false);
			faseDimensionale.setFaseDaStampare(true);
			faseDimensionale.setPercentualeDaControlla(new BigDecimal(100));
			faseDimensionale.setNumMaxDifettiPerAccettaz(0);
			faseDimensionale.setNumMinDifettiPerRifiuto(1);
			faseDimensionale.setTipoCampionamento('1');
			faseDimensionale.setLivelloCampionamento('2');
			faseDimensionale.setLivelloIspezione('6');
			faseDimensionale.setNormativaCartaCtr('1');
			faseDimensionale.setUtilizzoLimitiAttesi(false);
			faseDimensionale.setRiferimentoCentroTolleran(false);

			ciclo.getFasi().add(faseDimensionale);

			CicloCollaudoCaratteristica posNeg = caratteristicaCicloCollaudo(faseDimensionale, (short) 1, "Positivo / Negativo", "Pos. / Neg.",collaudo);
			posNeg.setLimInfTolleranza(new BigDecimal("-9999"));
			posNeg.setLimSupTolleranza(new BigDecimal("9999"));
			posNeg.setIdUnitaMisura("kg"); 
			posNeg.setValoreNominale(BigDecimal.ZERO);
			posNeg.setMasterTipoRis('S');
			posNeg.setMasterLivelloRis('3');
			posNeg.setStrMisuraTipoRis1('S');
			posNeg.setStrMisuraLivelloRis1('3');
			posNeg.setStrMisuraTipoRis2('S');
			posNeg.setStrMisuraLivelloRis2('4');
			posNeg.setLivelloIspezione('6');
			posNeg.setTipoCartaControllo('1');
			posNeg.setDefaultMasterTipoRis('S');
			posNeg.setDefaultMasterLivelloRis('4');
			posNeg.setObbligatorieta(true);
			posNeg.setDefaultTipoRis1('S');
			posNeg.setDefaultTipoRis2('S');
			posNeg.setDefaultLivelloRis1('4');
			posNeg.setDefaultLivelloRis2('4');
			posNeg.setRilavLimInfTol(false);
			posNeg.setRilavLimSupTol(false);
			posNeg.setFlagRilavorabilita(true);
			posNeg.setTipoCaratteristica('2');
			posNeg.setClasseImportanzaCar('1');

			faseDimensionale.getCaratteristiche().add(posNeg);

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseDurezzaConSpina = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Con Spina Kg", "Durezza Con Spi");
			faseDurezzaConSpina.setTipoCaratteristiche('1');
			faseDurezzaConSpina.setRaccoltaDati('1');
			faseDurezzaConSpina.setCampionamento('2');
			faseDurezzaConSpina.setTipoFrequenzaControllo('P');
			faseDurezzaConSpina.setValNominaleObbligatorio(false);
			faseDurezzaConSpina.setControlloEsterno(false);
			faseDurezzaConSpina.setFaseObbligatoria(false);
			faseDurezzaConSpina.setFaseDaStampare(true);
			faseDurezzaConSpina.setPercentualeDaControlla(new BigDecimal(100));
			faseDurezzaConSpina.setNumMaxDifettiPerAccettaz(0);
			faseDurezzaConSpina.setNumMinDifettiPerRifiuto(1);
			faseDurezzaConSpina.setTipoCampionamento('1');
			faseDurezzaConSpina.setLivelloCampionamento('2');
			faseDurezzaConSpina.setLivelloIspezione('6');
			faseDurezzaConSpina.setNormativaCartaCtr('1');
			faseDurezzaConSpina.setUtilizzoLimitiAttesi(false);
			faseDurezzaConSpina.setRiferimentoCentroTolleran(false);

			ciclo.getFasi().add(faseDurezzaConSpina);

			// Durezza 1
			CicloCollaudoCaratteristica carattDurezza1 = caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 1, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza1.setLimInfTolleranza(new BigDecimal("-9999"));
			carattDurezza1.setLimSupTolleranza(new BigDecimal("9999"));
			carattDurezza1.setIdUnitaMisura("kg");
			carattDurezza1.setValoreNominale(BigDecimal.ZERO);
			carattDurezza1.setMasterTipoRis('S');
			carattDurezza1.setMasterLivelloRis('3');
			carattDurezza1.setStrMisuraTipoRis1('S');
			carattDurezza1.setStrMisuraLivelloRis1('3');
			carattDurezza1.setStrMisuraTipoRis2('S');
			carattDurezza1.setStrMisuraLivelloRis2('4');
			carattDurezza1.setLivelloIspezione('6');
			carattDurezza1.setTipoCartaControllo('1');
			carattDurezza1.setDefaultMasterTipoRis('S');
			carattDurezza1.setDefaultMasterLivelloRis('4');
			carattDurezza1.setDefaultTipoRis1('S');
			carattDurezza1.setDefaultTipoRis2('S');
			carattDurezza1.setDefaultLivelloRis1('4');
			carattDurezza1.setDefaultLivelloRis2('4');
			carattDurezza1.setRilavLimInfTol(true);
			carattDurezza1.setRilavLimSupTol(true);
			carattDurezza1.setFlagRilavorabilita(true);
			carattDurezza1.setTipoCaratteristica('1');
			carattDurezza1.setClasseImportanzaCar('1');

			faseDurezzaConSpina.getCaratteristiche().add(carattDurezza1);

			// Durezza 2
			CicloCollaudoCaratteristica carattDurezza2 = caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 2, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza2.setLimInfTolleranza(new BigDecimal("-9999"));
			carattDurezza2.setLimSupTolleranza(new BigDecimal("9999"));
			carattDurezza2.setIdUnitaMisura("kg");
			carattDurezza2.setValoreNominale(BigDecimal.ZERO);
			carattDurezza2.setMasterTipoRis('S');
			carattDurezza2.setMasterLivelloRis('3');
			carattDurezza2.setStrMisuraTipoRis1('S');
			carattDurezza2.setStrMisuraLivelloRis1('3');
			carattDurezza2.setStrMisuraTipoRis2('S');
			carattDurezza2.setStrMisuraLivelloRis2('4');
			carattDurezza2.setLivelloIspezione('6');
			carattDurezza2.setTipoCartaControllo('1');
			carattDurezza2.setDefaultMasterTipoRis('S');
			carattDurezza2.setDefaultMasterLivelloRis('4');
			carattDurezza2.setDefaultTipoRis1('S');
			carattDurezza2.setDefaultTipoRis2('S');
			carattDurezza2.setDefaultLivelloRis1('4');
			carattDurezza2.setDefaultLivelloRis2('4');
			carattDurezza2.setRilavLimInfTol(true);
			carattDurezza2.setRilavLimSupTol(true);
			carattDurezza2.setFlagRilavorabilita(true);
			carattDurezza2.setTipoCaratteristica('1');
			carattDurezza2.setClasseImportanzaCar('1');

			faseDurezzaConSpina.getCaratteristiche().add(carattDurezza2);

			// Durezza 3
			CicloCollaudoCaratteristica carattDurezza3 = caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 3, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza3.setLimInfTolleranza(new BigDecimal("-9999"));
			carattDurezza3.setLimSupTolleranza(new BigDecimal("9999"));
			carattDurezza3.setIdUnitaMisura("kg");
			carattDurezza3.setValoreNominale(BigDecimal.ZERO);
			carattDurezza3.setMasterTipoRis('S');
			carattDurezza3.setMasterLivelloRis('3');
			carattDurezza3.setStrMisuraTipoRis1('S');
			carattDurezza3.setStrMisuraLivelloRis1('3');
			carattDurezza3.setStrMisuraTipoRis2('S');
			carattDurezza3.setStrMisuraLivelloRis2('4');
			carattDurezza3.setLivelloIspezione('6');
			carattDurezza3.setTipoCartaControllo('1');
			carattDurezza3.setDefaultMasterTipoRis('S');
			carattDurezza3.setDefaultMasterLivelloRis('4');
			carattDurezza3.setDefaultTipoRis1('S');
			carattDurezza3.setDefaultTipoRis2('S');
			carattDurezza3.setDefaultLivelloRis1('4');
			carattDurezza3.setDefaultLivelloRis2('4');
			carattDurezza3.setRilavLimInfTol(true);
			carattDurezza3.setRilavLimSupTol(true);
			carattDurezza3.setFlagRilavorabilita(true);
			carattDurezza3.setTipoCaratteristica('1');
			carattDurezza3.setClasseImportanzaCar('1');

			faseDurezzaConSpina.getCaratteristiche().add(carattDurezza3);

			// Durezza 4
			CicloCollaudoCaratteristica carattDurezza4 = caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 4, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza4.setLimInfTolleranza(new BigDecimal("-9999"));
			carattDurezza4.setLimSupTolleranza(new BigDecimal("9999"));
			carattDurezza4.setIdUnitaMisura("kg");
			carattDurezza4.setValoreNominale(BigDecimal.ZERO);
			carattDurezza4.setMasterTipoRis('S');
			carattDurezza4.setMasterLivelloRis('3');
			carattDurezza4.setStrMisuraTipoRis1('S');
			carattDurezza4.setStrMisuraLivelloRis1('3');
			carattDurezza4.setStrMisuraTipoRis2('S');
			carattDurezza4.setStrMisuraLivelloRis2('4');
			carattDurezza4.setLivelloIspezione('6');
			carattDurezza4.setTipoCartaControllo('1');
			carattDurezza4.setDefaultMasterTipoRis('S');
			carattDurezza4.setDefaultMasterLivelloRis('4');
			carattDurezza4.setDefaultTipoRis1('S');
			carattDurezza4.setDefaultTipoRis2('S');
			carattDurezza4.setDefaultLivelloRis1('4');
			carattDurezza4.setDefaultLivelloRis2('4');
			carattDurezza4.setRilavLimInfTol(true);
			carattDurezza4.setRilavLimSupTol(true);
			carattDurezza4.setFlagRilavorabilita(true);
			carattDurezza4.setTipoCaratteristica('1');
			carattDurezza4.setClasseImportanzaCar('1');

			faseDurezzaConSpina.getCaratteristiche().add(carattDurezza4);

			// Durezza 5
			CicloCollaudoCaratteristica carattDurezza5 = caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 5, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza5.setLimInfTolleranza(new BigDecimal("-9999"));
			carattDurezza5.setLimSupTolleranza(new BigDecimal("9999"));
			carattDurezza5.setIdUnitaMisura("kg");
			carattDurezza5.setValoreNominale(BigDecimal.ZERO);
			carattDurezza5.setMasterTipoRis('S');
			carattDurezza5.setMasterLivelloRis('3');
			carattDurezza5.setStrMisuraTipoRis1('S');
			carattDurezza5.setStrMisuraLivelloRis1('3');
			carattDurezza5.setStrMisuraTipoRis2('S');
			carattDurezza5.setStrMisuraLivelloRis2('4');
			carattDurezza5.setLivelloIspezione('6');
			carattDurezza5.setTipoCartaControllo('1');
			carattDurezza5.setDefaultMasterTipoRis('S');
			carattDurezza5.setDefaultMasterLivelloRis('4');
			carattDurezza5.setDefaultTipoRis1('S');
			carattDurezza5.setDefaultTipoRis2('S');
			carattDurezza5.setDefaultLivelloRis1('4');
			carattDurezza5.setDefaultLivelloRis2('4');
			carattDurezza5.setRilavLimInfTol(true);
			carattDurezza5.setRilavLimSupTol(true);
			carattDurezza5.setFlagRilavorabilita(true);
			carattDurezza5.setTipoCaratteristica('1');
			carattDurezza5.setClasseImportanzaCar('1');

			faseDurezzaConSpina.getCaratteristiche().add(carattDurezza5);

			// Durezza 6
			CicloCollaudoCaratteristica carattDurezza6 = caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 6, "Durezza Kg", "Durezza Kg", collaudo);
			carattDurezza6.setLimInfTolleranza(new BigDecimal("-9999"));
			carattDurezza6.setLimSupTolleranza(new BigDecimal("9999"));
			carattDurezza6.setIdUnitaMisura("kg");
			carattDurezza6.setValoreNominale(BigDecimal.ZERO);
			carattDurezza6.setMasterTipoRis('S');
			carattDurezza6.setMasterLivelloRis('3');
			carattDurezza6.setStrMisuraTipoRis1('S');
			carattDurezza6.setStrMisuraLivelloRis1('3');
			carattDurezza6.setStrMisuraTipoRis2('S');
			carattDurezza6.setStrMisuraLivelloRis2('4');
			carattDurezza6.setLivelloIspezione('6');
			carattDurezza6.setTipoCartaControllo('1');
			carattDurezza6.setDefaultMasterTipoRis('S');
			carattDurezza6.setDefaultMasterLivelloRis('4');
			carattDurezza6.setDefaultTipoRis1('S');
			carattDurezza6.setDefaultTipoRis2('S');
			carattDurezza6.setDefaultLivelloRis1('4');
			carattDurezza6.setDefaultLivelloRis2('4');
			carattDurezza6.setRilavLimInfTol(true);
			carattDurezza6.setRilavLimSupTol(true);
			carattDurezza6.setFlagRilavorabilita(true);
			carattDurezza6.setTipoCaratteristica('1');
			carattDurezza6.setClasseImportanzaCar('1');

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

	@SuppressWarnings("unchecked")
	protected CicloCollaudoTestata gestioneCicloRicottura(OrdEsecAtvSogCollaudo collaudo, QcTabellaAttivita attivita) throws SQLException {
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

			faseCPezzoRicott.setTipoCaratteristiche('1');
			faseCPezzoRicott.setRaccoltaDati('1');
			faseCPezzoRicott.setCampionamento('2');
			faseCPezzoRicott.setTipoFrequenzaControllo('P');
			faseCPezzoRicott.setValNominaleObbligatorio(false);
			faseCPezzoRicott.setControlloEsterno(false);
			faseCPezzoRicott.setFaseDaStampare(true);
			faseCPezzoRicott.setNumMaxDifettiPerAccettaz(0);
			faseCPezzoRicott.setNumMinDifettiPerRifiuto(0);
			faseCPezzoRicott.setTipoCampionamento('1');
			faseCPezzoRicott.setLivelloCampionamento('2');
			faseCPezzoRicott.setLivelloIspezione('6');
			faseCPezzoRicott.setNormativaCartaCtr('1');
			faseCPezzoRicott.setUtilizzoLimitiAttesi(false);
			faseCPezzoRicott.setRiferimentoCentroTolleran(false);
			faseCPezzoRicott.setVisualizzaIdentifPezzo(true);

			ciclo.getFasi().add(faseCPezzoRicott);

			CicloCollaudoCaratteristica tempPezzo = caratteristicaCicloCollaudo(faseCPezzoRicott, (short) 1, "Temperatura Pezzo", "Temperatura P",collaudo);
			tempPezzo.setLimInfTolleranza(new BigDecimal("-9999"));
			tempPezzo.setLimSupTolleranza(new BigDecimal("9999"));
			tempPezzo.setIdUnitaMisura("°C"); 
			tempPezzo.setValoreNominale(BigDecimal.ZERO);
			tempPezzo.setMasterTipoRis('S');
			tempPezzo.setMasterLivelloRis('3');
			tempPezzo.setStrMisuraTipoRis1('S');
			tempPezzo.setStrMisuraLivelloRis1('3');
			tempPezzo.setStrMisuraTipoRis2('S');
			tempPezzo.setStrMisuraLivelloRis2('4');
			tempPezzo.setLivelloIspezione('6');
			tempPezzo.setTipoCartaControllo('1');
			tempPezzo.setDefaultMasterTipoRis('S');
			tempPezzo.setDefaultMasterLivelloRis('4');
			tempPezzo.setDefaultTipoRis1('S');
			tempPezzo.setDefaultTipoRis2('S');
			tempPezzo.setDefaultLivelloRis1('4');
			tempPezzo.setDefaultLivelloRis2('4');

			faseCPezzoRicott.getCaratteristiche().add(tempPezzo);

			// ----------------------------------------------------
			// FASE FORNO RICOTTURA
			// ----------------------------------------------------
			idSequenzaFase = idSequenzaFase + 1;
			idSequenzaVisualizz = idSequenzaVisualizz + 1;

			CicloCollaudoFase faseFornoRicott = faseCicloCollaudo(
					ciclo,
					idSequenzaFase.shortValue(),
					idSequenzaVisualizz.shortValue(),
					"Forno di Ricottura",
					"Forno di Ricott"
					);

			faseFornoRicott.setTipoCaratteristiche('1');
			faseFornoRicott.setRaccoltaDati('1');
			faseFornoRicott.setCampionamento('2');
			faseFornoRicott.setTipoFrequenzaControllo('P');
			faseFornoRicott.setValNominaleObbligatorio(false);
			faseFornoRicott.setControlloEsterno(false);
			faseFornoRicott.setFaseDaStampare(true);
			faseFornoRicott.setNumMaxDifettiPerAccettaz(0);
			faseFornoRicott.setNumMinDifettiPerRifiuto(0);
			faseFornoRicott.setTipoCampionamento('1');
			faseFornoRicott.setLivelloCampionamento('2');
			faseFornoRicott.setLivelloIspezione('6');
			faseFornoRicott.setNormativaCartaCtr('1');
			faseFornoRicott.setUtilizzoLimitiAttesi(false);
			faseFornoRicott.setRiferimentoCentroTolleran(false);
			faseFornoRicott.setVisualizzaIdentifPezzo(true);

			ciclo.getFasi().add(faseFornoRicott);

			// -- 1) Temperatura Iniziale --
			CicloCollaudoCaratteristica tempIniziale = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 1,
					"Temperatura Iniziale",
					"Temperatura ini",
					collaudo
					);
			tempIniziale.setLimInfTolleranza(new BigDecimal("-9999"));
			tempIniziale.setLimSupTolleranza(new BigDecimal("9999"));
			tempIniziale.setIdUnitaMisura("°C");
			tempIniziale.setValoreNominale(BigDecimal.ZERO);
			tempIniziale.setMasterTipoRis('S');
			tempIniziale.setMasterLivelloRis('3');
			tempIniziale.setStrMisuraTipoRis1('S');
			tempIniziale.setStrMisuraLivelloRis1('3');
			tempIniziale.setStrMisuraTipoRis2('S');
			tempIniziale.setStrMisuraLivelloRis2('4');
			tempIniziale.setLivelloIspezione('6');
			tempIniziale.setTipoCartaControllo('1');
			tempIniziale.setDefaultMasterTipoRis('S');
			tempIniziale.setDefaultMasterLivelloRis('4');
			tempIniziale.setDefaultTipoRis1('S');
			tempIniziale.setDefaultTipoRis2('S');
			tempIniziale.setDefaultLivelloRis1('4');
			tempIniziale.setDefaultLivelloRis2('4');

			faseFornoRicott.getCaratteristiche().add(tempIniziale);

			// -- 2) 1 Gradiente Termico --
			CicloCollaudoCaratteristica gradTerm1 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 2,
					"1 Gradiente Termico",
					"1 Gradiente Ter",
					collaudo
					);
			gradTerm1.setLimInfTolleranza(new BigDecimal("-9999"));
			gradTerm1.setLimSupTolleranza(new BigDecimal("9999"));
			gradTerm1.setIdUnitaMisura("C/H");
			gradTerm1.setValoreNominale(BigDecimal.ZERO);
			gradTerm1.setMasterTipoRis('S');
			gradTerm1.setMasterLivelloRis('3');
			gradTerm1.setStrMisuraTipoRis1('S');
			gradTerm1.setStrMisuraLivelloRis1('3');
			gradTerm1.setStrMisuraTipoRis2('S');
			gradTerm1.setStrMisuraLivelloRis2('4');
			gradTerm1.setLivelloIspezione('6');
			gradTerm1.setTipoCartaControllo('1');
			gradTerm1.setDefaultMasterTipoRis('S');
			gradTerm1.setDefaultMasterLivelloRis('4');
			gradTerm1.setDefaultTipoRis1('S');
			gradTerm1.setDefaultTipoRis2('S');
			gradTerm1.setDefaultLivelloRis1('4');
			gradTerm1.setDefaultLivelloRis2('4');

			faseFornoRicott.getCaratteristiche().add(gradTerm1);

			// -- 3) 1 Temperatura Fine Rampa --
			CicloCollaudoCaratteristica tempFineRampa1 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 3,
					"1 Temperatura Fine Rampa",
					"1 Temperatura F",
					collaudo
					);
			tempFineRampa1.setLimInfTolleranza(new BigDecimal("-9999"));
			tempFineRampa1.setLimSupTolleranza(new BigDecimal("9999"));
			tempFineRampa1.setIdUnitaMisura("°C");
			tempFineRampa1.setValoreNominale(BigDecimal.ZERO);
			tempFineRampa1.setMasterTipoRis('S');
			tempFineRampa1.setMasterLivelloRis('3');
			tempFineRampa1.setStrMisuraTipoRis1('S');
			tempFineRampa1.setStrMisuraLivelloRis1('3');
			tempFineRampa1.setStrMisuraTipoRis2('S');
			tempFineRampa1.setStrMisuraLivelloRis2('4');
			tempFineRampa1.setLivelloIspezione('6');
			tempFineRampa1.setTipoCartaControllo('1');
			tempFineRampa1.setDefaultMasterTipoRis('S');
			tempFineRampa1.setDefaultMasterLivelloRis('4');
			tempFineRampa1.setDefaultTipoRis1('S');
			tempFineRampa1.setDefaultTipoRis2('S');
			tempFineRampa1.setDefaultLivelloRis1('4');
			tempFineRampa1.setDefaultLivelloRis2('4');

			faseFornoRicott.getCaratteristiche().add(tempFineRampa1);

			// -- 4) 1 Permanenza --
			CicloCollaudoCaratteristica permanenza1 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 4,
					"1 Permanenza",
					"1 Permanenza",
					collaudo
					);
			permanenza1.setLimInfTolleranza(new BigDecimal("-9999"));
			permanenza1.setLimSupTolleranza(new BigDecimal("9999"));
			permanenza1.setIdUnitaMisura("h");
			permanenza1.setValoreNominale(BigDecimal.ZERO);
			permanenza1.setMasterTipoRis('S');
			permanenza1.setMasterLivelloRis('3');
			permanenza1.setStrMisuraTipoRis1('S');
			permanenza1.setStrMisuraLivelloRis1('3');
			permanenza1.setStrMisuraTipoRis2('S');
			permanenza1.setStrMisuraLivelloRis2('4');
			permanenza1.setLivelloIspezione('6');
			permanenza1.setTipoCartaControllo('1');
			permanenza1.setDefaultMasterTipoRis('S');
			permanenza1.setDefaultMasterLivelloRis('4');
			permanenza1.setDefaultTipoRis1('S');
			permanenza1.setDefaultTipoRis2('S');
			permanenza1.setDefaultLivelloRis1('4');
			permanenza1.setDefaultLivelloRis2('4');

			faseFornoRicott.getCaratteristiche().add(permanenza1);

			// -- 5) 2 Gradiente Termico --
			CicloCollaudoCaratteristica gradTerm2 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 5,
					"2 Gradiente Termico",
					"2 Gradiente Ter",
					collaudo
					);
			gradTerm2.setLimInfTolleranza(new BigDecimal("-9999"));
			gradTerm2.setLimSupTolleranza(new BigDecimal("9999"));
			gradTerm2.setIdUnitaMisura("C/H");
			gradTerm2.setValoreNominale(BigDecimal.ZERO);
			gradTerm2.setMasterTipoRis('S');
			gradTerm2.setMasterLivelloRis('3');
			gradTerm2.setStrMisuraTipoRis1('S');
			gradTerm2.setStrMisuraLivelloRis1('3');
			gradTerm2.setStrMisuraTipoRis2('S');
			gradTerm2.setStrMisuraLivelloRis2('4');
			gradTerm2.setLivelloIspezione('6');
			gradTerm2.setTipoCartaControllo('1');
			gradTerm2.setDefaultMasterTipoRis('S');
			gradTerm2.setDefaultMasterLivelloRis('4');
			gradTerm2.setDefaultTipoRis1('S');
			gradTerm2.setDefaultTipoRis2('S');
			gradTerm2.setDefaultLivelloRis1('4');
			gradTerm2.setDefaultLivelloRis2('4');

			faseFornoRicott.getCaratteristiche().add(gradTerm2);

			// -- 6) 2 Temperatura Fine Rampa --
			CicloCollaudoCaratteristica tempFineRampa2 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 6,
					"2 Temperatura Fine Rampa",
					"2 Temperatura F",
					collaudo
					);
			tempFineRampa2.setLimInfTolleranza(new BigDecimal("-9999"));
			tempFineRampa2.setLimSupTolleranza(new BigDecimal("9999"));
			tempFineRampa2.setIdUnitaMisura("°C");
			tempFineRampa2.setValoreNominale(BigDecimal.ZERO);
			tempFineRampa2.setMasterTipoRis('S');
			tempFineRampa2.setMasterLivelloRis('3');
			tempFineRampa2.setStrMisuraTipoRis1('S');
			tempFineRampa2.setStrMisuraLivelloRis1('3');
			tempFineRampa2.setStrMisuraTipoRis2('S');
			tempFineRampa2.setStrMisuraLivelloRis2('4');
			tempFineRampa2.setLivelloIspezione('6');
			tempFineRampa2.setTipoCartaControllo('1');
			tempFineRampa2.setDefaultMasterTipoRis('S');
			tempFineRampa2.setDefaultMasterLivelloRis('4');
			tempFineRampa2.setDefaultTipoRis1('S');
			tempFineRampa2.setDefaultTipoRis2('S');
			tempFineRampa2.setDefaultLivelloRis1('4');
			tempFineRampa2.setDefaultLivelloRis2('4');

			faseFornoRicott.getCaratteristiche().add(tempFineRampa2);

			// -- 7) 2 Permanenza --
			CicloCollaudoCaratteristica permanenza2 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 7,
					"2 Permanenza",
					"2 Permanenza",
					collaudo
					);
			permanenza2.setLimInfTolleranza(new BigDecimal("-9999"));
			permanenza2.setLimSupTolleranza(new BigDecimal("9999"));
			permanenza2.setIdUnitaMisura("h");
			permanenza2.setValoreNominale(BigDecimal.ZERO);
			permanenza2.setMasterTipoRis('S');
			permanenza2.setMasterLivelloRis('3');
			permanenza2.setStrMisuraTipoRis1('S');
			permanenza2.setStrMisuraLivelloRis1('3');
			permanenza2.setStrMisuraTipoRis2('S');
			permanenza2.setStrMisuraLivelloRis2('4');
			permanenza2.setLivelloIspezione('6');
			permanenza2.setTipoCartaControllo('1');
			permanenza2.setDefaultMasterTipoRis('S');
			permanenza2.setDefaultMasterLivelloRis('4');
			permanenza2.setDefaultTipoRis1('S');
			permanenza2.setDefaultTipoRis2('S');
			permanenza2.setDefaultLivelloRis1('4');
			permanenza2.setDefaultLivelloRis2('4');

			faseFornoRicott.getCaratteristiche().add(permanenza2);

			// -- 8) 3 Gradiente Termico --
			CicloCollaudoCaratteristica gradTerm3 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 8,
					"3 Gradiente Termico",
					"3 Gradiente Ter",
					collaudo
					);
			gradTerm3.setLimInfTolleranza(new BigDecimal("-9999"));
			gradTerm3.setLimSupTolleranza(new BigDecimal("9999"));
			gradTerm3.setIdUnitaMisura("C/H");
			gradTerm3.setValoreNominale(BigDecimal.ZERO);
			gradTerm3.setMasterTipoRis('S');
			gradTerm3.setMasterLivelloRis('3');
			gradTerm3.setStrMisuraTipoRis1('S');
			gradTerm3.setStrMisuraLivelloRis1('3');
			gradTerm3.setStrMisuraTipoRis2('S');
			gradTerm3.setStrMisuraLivelloRis2('4');
			gradTerm3.setLivelloIspezione('6');
			gradTerm3.setTipoCartaControllo('1');
			gradTerm3.setDefaultMasterTipoRis('S');
			gradTerm3.setDefaultMasterLivelloRis('4');
			gradTerm3.setDefaultTipoRis1('S');
			gradTerm3.setDefaultTipoRis2('S');
			gradTerm3.setDefaultLivelloRis1('4');
			gradTerm3.setDefaultLivelloRis2('4');

			faseFornoRicott.getCaratteristiche().add(gradTerm3);

			// -- 9) 3 Temperatura Fine Rampa --
			CicloCollaudoCaratteristica tempFineRampa3 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 9,
					"3 Temperatura Fine Rampa",
					"3 Temperatura F",
					collaudo
					);
			tempFineRampa3.setLimInfTolleranza(new BigDecimal("-9999"));
			tempFineRampa3.setLimSupTolleranza(new BigDecimal("9999"));
			tempFineRampa3.setIdUnitaMisura("°C");
			tempFineRampa3.setValoreNominale(BigDecimal.ZERO);
			tempFineRampa3.setMasterTipoRis('S');
			tempFineRampa3.setMasterLivelloRis('3');
			tempFineRampa3.setStrMisuraTipoRis1('S');
			tempFineRampa3.setStrMisuraLivelloRis1('3');
			tempFineRampa3.setStrMisuraTipoRis2('S');
			tempFineRampa3.setStrMisuraLivelloRis2('4');
			tempFineRampa3.setLivelloIspezione('6');
			tempFineRampa3.setTipoCartaControllo('1');
			tempFineRampa3.setDefaultMasterTipoRis('S');
			tempFineRampa3.setDefaultMasterLivelloRis('4');
			tempFineRampa3.setDefaultTipoRis1('S');
			tempFineRampa3.setDefaultTipoRis2('S');
			tempFineRampa3.setDefaultLivelloRis1('4');
			tempFineRampa3.setDefaultLivelloRis2('4');

			faseFornoRicott.getCaratteristiche().add(tempFineRampa3);

			// -- 10) 3 Permanenza --
			CicloCollaudoCaratteristica permanenza3 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 10,
					"3 Permanenza",
					"3 Permanenza",
					collaudo
					);
			permanenza3.setLimInfTolleranza(new BigDecimal("-9999"));
			permanenza3.setLimSupTolleranza(new BigDecimal("9999"));
			permanenza3.setIdUnitaMisura("h");
			permanenza3.setValoreNominale(BigDecimal.ZERO);
			permanenza3.setMasterTipoRis('S');
			permanenza3.setMasterLivelloRis('3');
			permanenza3.setStrMisuraTipoRis1('S');
			permanenza3.setStrMisuraLivelloRis1('3');
			permanenza3.setStrMisuraTipoRis2('S');
			permanenza3.setStrMisuraLivelloRis2('4');
			permanenza3.setLivelloIspezione('6');
			permanenza3.setTipoCartaControllo('1');
			permanenza3.setDefaultMasterTipoRis('S');
			permanenza3.setDefaultMasterLivelloRis('4');
			permanenza3.setDefaultTipoRis1('S');
			permanenza3.setDefaultTipoRis2('S');
			permanenza3.setDefaultLivelloRis1('4');
			permanenza3.setDefaultLivelloRis2('4');

			faseFornoRicott.getCaratteristiche().add(permanenza3);

			// -- 11) 4 Gradiente Termico --
			CicloCollaudoCaratteristica gradTerm4 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 11,
					"4 Gradiente Termico",
					"4 Gradiente Ter",
					collaudo
					);
			gradTerm4.setLimInfTolleranza(new BigDecimal("-9999"));
			gradTerm4.setLimSupTolleranza(new BigDecimal("9999"));
			gradTerm4.setIdUnitaMisura("C/H");
			gradTerm4.setValoreNominale(BigDecimal.ZERO);
			gradTerm4.setMasterTipoRis('S');
			gradTerm4.setMasterLivelloRis('3');
			gradTerm4.setStrMisuraTipoRis1('S');
			gradTerm4.setStrMisuraLivelloRis1('3');
			gradTerm4.setStrMisuraTipoRis2('S');
			gradTerm4.setStrMisuraLivelloRis2('4');
			gradTerm4.setLivelloIspezione('6');
			gradTerm4.setTipoCartaControllo('1');
			gradTerm4.setDefaultMasterTipoRis('S');
			gradTerm4.setDefaultMasterLivelloRis('4');
			gradTerm4.setDefaultTipoRis1('S');
			gradTerm4.setDefaultTipoRis2('S');
			gradTerm4.setDefaultLivelloRis1('4');
			gradTerm4.setDefaultLivelloRis2('4');

			faseFornoRicott.getCaratteristiche().add(gradTerm4);

			// -- 12) 4 Temperatura Fine Rampa --
			CicloCollaudoCaratteristica tempFineRampa4 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 12,
					"4 Temperatura Fine Rampa",
					"4 Temperatura F",
					collaudo
					);
			tempFineRampa4.setLimInfTolleranza(new BigDecimal("-9999"));
			tempFineRampa4.setLimSupTolleranza(new BigDecimal("9999"));
			tempFineRampa4.setIdUnitaMisura("°C");
			tempFineRampa4.setValoreNominale(BigDecimal.ZERO);
			tempFineRampa4.setMasterTipoRis('S');
			tempFineRampa4.setMasterLivelloRis('3');
			tempFineRampa4.setStrMisuraTipoRis1('S');
			tempFineRampa4.setStrMisuraLivelloRis1('3');
			tempFineRampa4.setStrMisuraTipoRis2('S');
			tempFineRampa4.setStrMisuraLivelloRis2('4');
			tempFineRampa4.setLivelloIspezione('6');
			tempFineRampa4.setTipoCartaControllo('1');
			tempFineRampa4.setDefaultMasterTipoRis('S');
			tempFineRampa4.setDefaultMasterLivelloRis('4');
			tempFineRampa4.setDefaultTipoRis1('S');
			tempFineRampa4.setDefaultTipoRis2('S');
			tempFineRampa4.setDefaultLivelloRis1('4');
			tempFineRampa4.setDefaultLivelloRis2('4');

			faseFornoRicott.getCaratteristiche().add(tempFineRampa4);

			// -- 13) 4 Permanenza --
			CicloCollaudoCaratteristica permanenza4 = caratteristicaCicloCollaudo(
					faseFornoRicott,
					(short) 13,
					"4 Permanenza",
					"4 Permanenza",
					collaudo
					);
			permanenza4.setLimInfTolleranza(new BigDecimal("-9999"));
			permanenza4.setLimSupTolleranza(new BigDecimal("9999"));
			permanenza4.setIdUnitaMisura("h");
			permanenza4.setValoreNominale(BigDecimal.ZERO);
			permanenza4.setMasterTipoRis('S');
			permanenza4.setMasterLivelloRis('3');
			permanenza4.setStrMisuraTipoRis1('S');
			permanenza4.setStrMisuraLivelloRis1('3');
			permanenza4.setStrMisuraTipoRis2('S');
			permanenza4.setStrMisuraLivelloRis2('4');
			permanenza4.setLivelloIspezione('6');
			permanenza4.setTipoCartaControllo('1');
			permanenza4.setDefaultMasterTipoRis('S');
			permanenza4.setDefaultMasterLivelloRis('4');
			permanenza4.setDefaultTipoRis1('S');
			permanenza4.setDefaultTipoRis2('S');
			permanenza4.setDefaultLivelloRis1('4');
			permanenza4.setDefaultLivelloRis2('4');

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected CicloCollaudoTestata gestioneCicloFucinatura(QcTabellaAttivita attivita, OrdEsecAtvSogCollaudo collaudo) throws SQLException {
		CicloCollaudoTestata ciclo = null;
		Integer idSequenzaFase = attivita.getIdSequenzaFase();
		Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

		ciclo = testataCicloCollaudo(collaudo);

		if(!ciclo.isOnDB()) {

			CicloCollaudoFase faseRiscaldo = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Riscaldo", "Riscaldo");

			faseRiscaldo.setCampionamento(CicloCollaudoFase.NUMERICO);
			faseRiscaldo.setTipoFrequenzaControllo(CicloCollaudoFase.FREQ_CTR_PEZZI_ENT);	
			faseRiscaldo.setValNominaleObbligatorio(false);
			faseRiscaldo.setControlloEsterno(false);
			faseRiscaldo.setFaseDaStampare(true);
			faseRiscaldo.setLivelloCampionamento(CicloCollaudoFase.NUMERICO);
			faseRiscaldo.setLivelloIspezione('6');
			faseRiscaldo.setNormativaCartaCtr('1');
			faseRiscaldo.setUtilizzoLimitiAttesi(false);
			faseRiscaldo.setRiferimentoCentroTolleran(false);
			faseRiscaldo.setVisualizzaIdentifPezzo(true);

			NoteAttivitaSogCollaudo commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
			if(commento != null)
				codificaCommento(faseRiscaldo.getCommenti(),commento);

			ciclo.getFasi().add(faseRiscaldo);

			// 1) Temperatura Iniziale
			CicloCollaudoCaratteristica tempInizio = caratteristicaCicloCollaudo(faseRiscaldo, (short) 1, "Temperatura Iniziale", "Temperatura Ini", collaudo);
			tempInizio.setLimInfTolleranza(new BigDecimal("-9999"));
			tempInizio.setLimSupTolleranza(new BigDecimal("9999"));
			tempInizio.setIdUnitaMisura("°C");
			faseRiscaldo.getCaratteristiche().add(tempInizio);

			// 2) 1 Gradiente Termico
			CicloCollaudoCaratteristica gradTermico1 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter", collaudo);
			gradTermico1.setLimInfTolleranza(new BigDecimal("-9999"));
			gradTermico1.setLimSupTolleranza(new BigDecimal("9999"));
			gradTermico1.setIdUnitaMisura("C/H");
			faseRiscaldo.getCaratteristiche().add(gradTermico1);

			// 3) 1 Temperatura Fine Rampa
			CicloCollaudoCaratteristica tempFineRampa1 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 4, "1 Temperatura Fine Rampa", "1 Temperatura F", collaudo);
			tempFineRampa1.setLimInfTolleranza(new BigDecimal("-9999"));
			tempFineRampa1.setLimSupTolleranza(new BigDecimal("9999"));
			tempFineRampa1.setIdUnitaMisura("°C");
			faseRiscaldo.getCaratteristiche().add(tempFineRampa1);

			// 4) 1 Permanenza
			CicloCollaudoCaratteristica permanenza1 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 5, "1 Permanenza", "1 Permanenza", collaudo);
			permanenza1.setLimInfTolleranza(new BigDecimal("-9999"));
			permanenza1.setLimSupTolleranza(new BigDecimal("9999"));
			permanenza1.setIdUnitaMisura("h");
			faseRiscaldo.getCaratteristiche().add(permanenza1);

			// 5) 2 Gradiente Termico
			CicloCollaudoCaratteristica gradTermico2 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 6, "2 Gradiente Termico", "2 Gradiente Ter", collaudo);
			gradTermico2.setLimInfTolleranza(new BigDecimal("-9999"));
			gradTermico2.setLimSupTolleranza(new BigDecimal("9999"));
			gradTermico2.setIdUnitaMisura("C/H");
			faseRiscaldo.getCaratteristiche().add(gradTermico2);

			// 6) 2 Temperatura Fine Rampa
			CicloCollaudoCaratteristica tempFineRampa2 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 8, "2 Temperatura Fine Rampa", "2 Temperatura F", collaudo);
			tempFineRampa2.setLimInfTolleranza(new BigDecimal("-9999"));
			tempFineRampa2.setLimSupTolleranza(new BigDecimal("9999"));
			tempFineRampa2.setIdUnitaMisura("°C");
			faseRiscaldo.getCaratteristiche().add(tempFineRampa2);

			// 7) 2 Permanenza
			CicloCollaudoCaratteristica permanenza2 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 9, "2 Permanenza", "2 Permanenza", collaudo);
			permanenza2.setLimInfTolleranza(new BigDecimal("-9999"));
			permanenza2.setLimSupTolleranza(new BigDecimal("9999"));
			permanenza2.setIdUnitaMisura("h");
			faseRiscaldo.getCaratteristiche().add(permanenza2);

			// 8) 3 Gradiente Termico
			CicloCollaudoCaratteristica gradTermico3 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 10, "3 Gradiente Termico", "3 Gradiente Ter", collaudo);
			gradTermico3.setLimInfTolleranza(new BigDecimal("-9999"));
			gradTermico3.setLimSupTolleranza(new BigDecimal("9999"));
			gradTermico3.setIdUnitaMisura("C/H");
			faseRiscaldo.getCaratteristiche().add(gradTermico3);

			// 9) 3 Temperatura Fine Rampa
			CicloCollaudoCaratteristica tempFineRampa3 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 12, "3 Temperatura Fine Rampa", "3 Temperatura F", collaudo);
			tempFineRampa3.setLimInfTolleranza(new BigDecimal("-9999"));
			tempFineRampa3.setLimSupTolleranza(new BigDecimal("9999"));
			tempFineRampa3.setIdUnitaMisura("°C");
			faseRiscaldo.getCaratteristiche().add(tempFineRampa3);

			// 10) 3 Permanenza
			CicloCollaudoCaratteristica permanenza3 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 13, "3 Permanenza", "3 Permanenza", collaudo);
			permanenza3.setLimInfTolleranza(new BigDecimal("-9999"));
			permanenza3.setLimSupTolleranza(new BigDecimal("9999"));
			permanenza3.setIdUnitaMisura("h");
			faseRiscaldo.getCaratteristiche().add(permanenza3);

			// 11) 4 Gradiente Termico
			CicloCollaudoCaratteristica gradTermico4 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 14, "4 Gradiente Termico", "4 Gradiente Ter", collaudo);
			gradTermico4.setLimInfTolleranza(new BigDecimal("-9999"));
			gradTermico4.setLimSupTolleranza(new BigDecimal("9999"));
			gradTermico4.setIdUnitaMisura("C/H");
			faseRiscaldo.getCaratteristiche().add(gradTermico4);

			// 12) 4 Temperatura Fine Rampa
			CicloCollaudoCaratteristica tempFineRampa4 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 16, "4 Temperatura Fine Rampa", "4 Temperatura F", collaudo);
			tempFineRampa4.setLimInfTolleranza(new BigDecimal("-9999"));
			tempFineRampa4.setLimSupTolleranza(new BigDecimal("9999"));
			tempFineRampa4.setIdUnitaMisura("°C");
			faseRiscaldo.getCaratteristiche().add(tempFineRampa4);

			// 13) 4 Permanenza
			CicloCollaudoCaratteristica permanenza4 = caratteristicaCicloCollaudo(faseRiscaldo, (short) 17, "4 Permanenza", "4 Permanenza", collaudo);
			permanenza4.setLimInfTolleranza(new BigDecimal("-9999"));
			permanenza4.setLimSupTolleranza(new BigDecimal("9999"));
			permanenza4.setIdUnitaMisura("h");
			faseRiscaldo.getCaratteristiche().add(permanenza4);


			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseTempForgia = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Temperatura di Forgia", "Forgia");

			faseTempForgia.setCampionamento(CicloCollaudoFase.NUMERICO);
			faseTempForgia.setTipoFrequenzaControllo(CicloCollaudoFase.FREQ_CTR_PEZZI_ENT);	
			faseTempForgia.setValNominaleObbligatorio(false);
			faseTempForgia.setControlloEsterno(false);
			faseTempForgia.setFaseDaStampare(true);
			faseTempForgia.setLivelloCampionamento(CicloCollaudoFase.NUMERICO);
			faseTempForgia.setLivelloIspezione('6');
			faseTempForgia.setNormativaCartaCtr('1');
			faseTempForgia.setUtilizzoLimitiAttesi(false);
			faseTempForgia.setRiferimentoCentroTolleran(false);
			faseTempForgia.setVisualizzaIdentifPezzo(true);

			commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
			if(commento != null)
				codificaCommento(faseTempForgia.getCommenti(),commento);

			ciclo.getFasi().add(faseTempForgia);

			// 1) Numero Calde
			CicloCollaudoCaratteristica numeroCalde = caratteristicaCicloCollaudo(faseTempForgia, (short) 1, "Numero Calde", "Numero Calde", collaudo);
			numeroCalde.setLimInfTolleranza(new BigDecimal("-9999"));
			numeroCalde.setLimSupTolleranza(new BigDecimal("9999"));
			numeroCalde.setIdUnitaMisura("nr"); 
			faseTempForgia.getCaratteristiche().add(numeroCalde);

			// 2) °C Minimi
			CicloCollaudoCaratteristica cMinimi = caratteristicaCicloCollaudo(faseTempForgia, (short) 2, "°C Minimi", "°C Minimi", collaudo);
			cMinimi.setLimInfTolleranza(new BigDecimal("-9999"));
			cMinimi.setLimSupTolleranza(new BigDecimal("9999"));
			cMinimi.setIdUnitaMisura("°C");
			faseTempForgia.getCaratteristiche().add(cMinimi);

			// 3) °C Massimi
			CicloCollaudoCaratteristica cMassimi = caratteristicaCicloCollaudo(faseTempForgia, (short) 4, "°C Massimi", "°C Massimi", collaudo);
			cMassimi.setLimInfTolleranza(new BigDecimal("-9999"));
			cMassimi.setLimSupTolleranza(new BigDecimal("9999"));
			cMassimi.setIdUnitaMisura("°C");
			faseTempForgia.getCaratteristiche().add(cMassimi);

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void codificaCommento(CommentHandler commentHandler,NoteAttivitaSogCollaudo commento) {
		Comment commentoOrdTesta = (Comment)Factory.createObject(Comment.class);
		commentoOrdTesta.setDescription(commento.getDescription());
		commentoOrdTesta.setText(commento.getNlsCommentText());
		String[] commentoArr = {commento.getNlsCommentText(),""};
		commentoOrdTesta.getTextNLSHandler().setTextsForLanguage(commentoArr, "it");
		try {
			commentoOrdTesta.save();
		}
		catch(Exception eSComm) {
			eSComm.printStackTrace(Trace.excStream);
		}
		CommentHandlerLink ordTestaCHL = (CommentHandlerLink)Factory.createObject(CommentHandlerLink.class);
		Vector<String> commentUseVector = new Vector<>(Arrays.asList(commento.getIdCommentUse()));
		ordTestaCHL.setChoiceCommentUseKeys(commentUseVector);
		ordTestaCHL.setComment(commentoOrdTesta);
		List commentHLList = commentHandler.getCommentHandlerLinks();
		commentHLList.add(ordTestaCHL);
		try {
			commentHandler.save();
		}
		catch(Exception eCUL) {
			eCUL.printStackTrace(Trace.excStream);
		}
	}

	public CicloCollaudoTestata testataCicloCollaudo(OrdEsecAtvSogCollaudo collaudo) throws SQLException {
		CicloCollaudoTestata testata = (CicloCollaudoTestata) Factory.createObject(CicloCollaudoTestata.class);
		if(collaudo.getIdProgressivo() != null) {
			testata.setProgressivo(collaudo.getIdProgressivo());
			testata.setDeepRetrieveEnabled(true);
			testata.retrieve();
		}else {
			testata.setDominio(CicloCollaudoTestata.ATT_LAV_ACCETTAZIONE);
			testata.setIdArticolo(collaudo.getArticolo());
			testata.setIdCommessa(collaudo.getCommessa());
			testata.setIdAttivitaLavorativa(collaudo.getAttivita());
			testata.getDescrizione().setDescrizione(collaudo.getDescrizione());
			testata.getDescrizione().setDescrizioneRidotta(collaudo.getDescrRidotta());
			testata.setValoreNominaleProposto(collaudo.getValNomProposto().equals("Y") ? true : false);
			testata.setIdLqa("STD");
			testata.setDataInizioValidita(TimeUtils.getCurrentDate());
			testata.getDatiComuniEstesi().setStato(DatiComuniEstesi.VALIDO);
		}
		return testata;
	}

	public CicloCollaudoFase faseCicloCollaudo(CicloCollaudoTestata testata,Short seqFase,Short seqVisualizz, String descrizione,String descrRidotta) {
		CicloCollaudoFase fase = (CicloCollaudoFase) Factory.createObject(CicloCollaudoFase.class);
		fase.setCicloCollaudoTestata(testata);
		fase.setSequenzaFase(seqFase);
		fase.setSequenzaVisualizzazione(seqVisualizz);
		fase.getDescrizione().setDescrizione(descrizione);
		fase.getDescrizione().setDescrizioneRidotta(descrRidotta);
		fase.setPercentualeDaControlla(new BigDecimal("100"));
		return fase;
	}

	public CicloCollaudoCaratteristica caratteristicaCicloCollaudo(CicloCollaudoFase fase,Short seqCarat, String descrizione, String descrRidotta,
			OrdEsecAtvSogCollaudo atvTarget) {
		CicloCollaudoCaratteristica carat = (CicloCollaudoCaratteristica) Factory.createObject(CicloCollaudoCaratteristica.class);
		carat.setCicloCollaudoFase(fase);
		carat.setSequenzaCaratteristica(seqCarat);
		carat.getDescrizioneCicloNLS().setDescrizione(descrizione);
		carat.getDescrizioneCicloNLS().setDescrizioneRidotta(descrRidotta);
		carat.setPercDaControllare(new BigDecimal("100"));
		//carat.setLimInfTolleranza(new BigDecimal("-9999"));
		//carat.setLimSupTolleranza(new BigDecimal("9999"));
		carat.setRilavLimInfTol(true);
		carat.setRilavLimSupTol(true);
		carat.setNumMaxDifettiAccettazione(0);
		carat.setNumMinDifettiRifiuto(1);

		String key = KeyHelper.buildObjectKey(new String[]{fase.getSequenzaFase().toString(),seqCarat.toString()});
		if(atvTarget.getCaratteristiche().containsKey(key)) {
			OrdEsecAtvSogCollaudoDatiCaratteristica dati = atvTarget.getCaratteristiche().get(key);
			carat.setValoreNominale(dati.getValoreNominale());
			carat.setIdUnitaMisura(dati.getIdUnitaMisura());
			carat.setRilavLimInfTol(dati.isRilevaLimiteInferiore());
			carat.setRilavLimSupTol(dati.isRilevaLimiteSuperiore());
			carat.setPercDaControllare(dati.getPercentualeControllare());
			carat.setLimSupTolleranza(new BigDecimal(dati.getLimiteSuperioreToll()));
			carat.setLimInfTolleranza(new BigDecimal(dati.getLimiteInferioreToll()));
		}
		return carat;
	}

	public QcTabellaAttivita ricercaAttivita(String nomeTabella, String idAttivita) {
		return attivitaTarget.stream()
				.filter(attivita -> nomeTabella.equals(attivita.getNomeTabella()) && idAttivita.equals(attivita.getIdAttivita()))
				.findFirst()
				.orElse(null);
	}

	public String generateOrpEffDocId(String idNumeroOrd) {
		String prefix = "";
		String trimmedId = idNumeroOrd.trim();

		// Determine the prefix based on the first two characters
		if (trimmedId.startsWith("P1")) {
			prefix = "-ORP-";
		} else if (trimmedId.startsWith("P2")) {
			prefix = "-ORPFM-";
		} else if (trimmedId.startsWith("P3")) {
			prefix = "-ORPSS-";
		}

		// Extract the last 7 characters and pad with zeros on the left
		String rightPart = trimmedId.length() > 7
				? trimmedId.substring(trimmedId.length() - 7)
						: trimmedId;
		String paddedRightPart = String.format("%07d", Integer.parseInt(rightPart));

		// Combine the prefix and the right part
		return prefix + paddedRightPart;
	}

	public OrdineEsecutivo ordineEsecutivoDaCommessa(String idCommessa) {
		OrdineEsecutivo ord = null;
		String stmt = " SELECT "+OrdineEsecutivoTM.ID_AZIENDA+","+OrdineEsecutivoTM.ID_ANNO_ORD+","+OrdineEsecutivoTM.ID_NUMERO_ORD+" ";
		stmt += "FROM "+OrdineEsecutivoTM.TABLE_NAME+" ";
		stmt += "WHERE "+OrdineEsecutivoTM.ID_AZIENDA+" = '"+Azienda.getAziendaCorrente()+"' AND "+OrdineEsecutivoTM.R_COMMESSA+" = '"+idCommessa+"' ";
		ResultSet rs = null;
		CachedStatement cs = null;
		try {
			cs = new CachedStatement(stmt);
			rs = cs.executeQuery();
			if(rs.next()) {
				ord = (OrdineEsecutivo) OrdineEsecutivo.elementWithKey(OrdineEsecutivo.class, KeyHelper.buildObjectKey(new String[] {
						rs.getString(OrdineEsecutivoTM.ID_AZIENDA),
						rs.getString(OrdineEsecutivoTM.ID_ANNO_ORD),
						rs.getString(OrdineEsecutivoTM.ID_NUMERO_ORD)
				}), PersistentObject.NO_LOCK);
			}
		}catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			if(rs != null) {
				try {
					rs.close();
					cs.free();
				} catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
			}
		}
		return ord;
	}

	@SuppressWarnings("rawtypes")
	protected List<OrdEsecAtvSogCollaudo> leggiCollaudi() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		long start = System.currentTimeMillis();
		long stop = System.currentTimeMillis();
		output.println(" Estraggo da Target collaudi da processare");
		ImportazioneCicliControlloHelper importazioneHelper = ImportazioneCicliControlloHelper.getInstance();
		List<OrdEsecAtvSogCollaudo> collaudi = new ArrayList<OrdEsecAtvSogCollaudo>();
		ConnectionDescriptor cnd = YDtsxPtExpOrdEsecRunner.externalConnectionDescriptor(
				NOME_DB_EXT,
				UTENTE_DB_EXT,
				PWD_DB_EXT, 
				SRV_DB_EXT, 
				PORTA_DB_EXT);
		int count_da_importare = 0;
		int count_da_aggiornare = 0;
		try {
			if(cnd != null) {
				ConnectionManager.pushConnection(cnd);
				attivitaTarget = QcTabellaAttivita.listaAttivita();
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C02_FUCINATURE));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_CONTROLLO_GREZZO_POST_FORGI));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_CONTROLLO_HB_SU_FACCIA_DI_T));
				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_CONTROLLO_POST_LM1));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_CONTROLLO_QUALITATIVO_POST));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_TT_DI_RICOTTURA));
				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_TT_PRELIMINARE));
				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_TT_QUALITA));
				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_TT_SUPPLEMENTARE));
				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_UT_PRE_TT));
				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C05_CONTROLLO_LABORATORIO));
				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C06_CONTROLLI_NDT));
				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SEELCT_C06_CONTROLLO_DUREZZE_DIMENSIONI));
				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C06_CONTROLLO_INTERMEDIO));
				Iterator iterCollaudi = collaudi.iterator();
				while(iterCollaudi.hasNext()){
					OrdEsecAtvSogCollaudo collaudo = (OrdEsecAtvSogCollaudo) iterCollaudi.next();
					if(collaudo.getIdProgressivo() != null) {
						count_da_aggiornare++;
					}else {
						count_da_importare++;
					}
					try {
						String nomeTabella = collaudo.getNomeTabella();
						switch (nomeTabella) {
						case "C03_TT-QUALITA":
							collaudo.recuperaNote_C03_TT_QUALITA_Target();
							//collaudo.recuperaNote_C03_TT_QUALITA_FAS_Target();
							importazioneHelper.leggiNoteCicloControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_NOTE_C03_TT_QUALITA, collaudo);
							collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARAT_TT_QUALITA_C03);
							collaudo.leggiRiferimentoProceduraTtQualita();
							collaudo.leggiNoteFaseTtQualita();
							break;
						case "C03_TT-PRELIMINARE":
							collaudo.recuperaNote_C03_TT_PRELIMINARE();
							//collaudo.recuperaNote_C03_TT_PRELIMINARE_Map();
							//collaudo.leggiNoteFaseControlloTtPreliminare();
							importazioneHelper.leggiNoteCicloControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_NOTE_C03_TT_PRELIMINARE, collaudo);
							collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARAT_TT_PRELIMINARE);
							break;
						case "C03_TT-SUPPLEMENTARE":
							collaudo.recuperaNote_C03_TT_SUPPLEMENTARE_Target();
							//collaudo.recuperaNote_C03_TT_SUPPLEMENTARE_Map();
							collaudo.leggiRiferimentoProcedutaTtSupplementare();
							collaudo.leggiNoteFaseControlloTtSupplementare();
							importazioneHelper.leggiNoteCicloControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_NOTE_C03_TT_SUPPLEMENTARE, collaudo);
							collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARAT_TT_SUPPLEMENTARE);
							break;
						case "C06_CONTROLLO_INTERMEDIO":
							collaudo.recuperaNote_C06_CONTORLLO_INTERMEDIO_Map();
							collaudo.recuperaNote_C06_CONTORLLO_INTERMEDIO_1_Map();
							collaudo.leggiRiferimentoProcedutaControlloIntermedio();
							collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARAT_CONTROLLO_INTERMEDIO);
							break;
						case "C02_FUCINATURA":
							importazioneHelper.leggiNoteCicloControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_NOTE_C02_FUCINATURE, collaudo);
							break;
						case "C06_CONTROLLI_NDT":
							collaudo.leggiNote_C06_CONTROLLI_NDT();
							collaudo.leggiNote_C06_CONTROLLI_NDT_1();
							collaudo.leggiNote_C06_CONTROLLI_NDT_2();
							collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARATT_CONTROLLI_NDT);
							collaudo.leggiNoteFaseControlloNDT();
							break;
						case "C06_CONTROLLO_DUREZZE-DIMENSION":
							collaudo.leggiNote_C06_CONTROLLO_DUREZZE_DIMENSION();
							collaudo.leggiNote_C06_CONTROLLO_DUREZZE_DIMENSION_1();
							collaudo.leggiRiferimentoProcedutaControlloDurezzeDimensionali();
							collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARATT_CONTROLLO_DUREZZ_DIMENSION);
							break;
						case "C03_CONTROLLO_GREZZO_POST_FORGI":
							collaudo.leggiRiferimentoProceduraControlloGrezzoPostForgia();
							break;
						case "C03_CONTROLLO_QUALITATIVO_POST":
							collaudo.leggiRiferimentoProceduraControlloQualitativoPost();
							collaudo.leggiNoteFaseControlloQualitativoPost();
							collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARAT_CONTROLLO_QUALITATIVO_POST);
							break;
						case "C03_TT_DI_RICOTTURA":
							collaudo.leggiRiferimentoProceduraTtRicottura();
							break;
						case "C03_UT_PRE-TT":
							collaudo.leggiRiferimentoProcedutaUtPreTT();
							collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARAT_UT_PRE_TT);
							break;
						default:
							break;
						}
					}catch (Exception e) {
						collaudi.remove(collaudo);
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (cnd != null) {
				try {
					cnd.closeConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				ConnectionManager.popConnection(cnd);
			}
		}
		stop = System.currentTimeMillis();
		output.println(" Tempo estrazione collaudi : "+((stop - start)/1000)+" secondi");
		output.println(" Ho trovato "+count_da_importare+" cicli da importare");
		output.println(" Ho trovato "+count_da_aggiornare+" cicli da aggiornare");
		start = stop;
		return collaudi;
	}

	@Override
	protected String getClassAdCollectionName() {
		return "YDtsxImportCicliCollaudo";
	}

}
