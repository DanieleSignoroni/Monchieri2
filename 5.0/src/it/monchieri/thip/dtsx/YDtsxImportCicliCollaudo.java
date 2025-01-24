package it.monchieri.thip.dtsx;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import com.thera.thermfw.base.IniFile;
import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.batch.BatchRunnable;
import com.thera.thermfw.cbs.Comment;
import com.thera.thermfw.cbs.CommentHandler;
import com.thera.thermfw.cbs.CommentHandlerLink;
import com.thera.thermfw.persist.ConnectionDescriptor;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.ErrorCodes;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.security.Authorizable;

import it.monchieri.thip.target.ImportazioneCicliControlloHelper;
import it.monchieri.thip.target.ImportazioneCicliControlloStatementHelper;
import it.monchieri.thip.target.InterfacciaTarget;
import it.monchieri.thip.target.NoteAttivitaSogCollaudo;
import it.monchieri.thip.target.OrdEsecAtvSogCollaudo;
import it.monchieri.thip.target.OrdEsecAtvSogCollaudoDatiCaratteristica;
import it.monchieri.thip.target.OrdEsecAtvSogCollaudoPO;
import it.monchieri.thip.target.QcTabellaAttivita;
import it.monchieri.thip.target.TargetCaratteristicheStatement;
import it.monchieri.thip.target.qualita.CicloCollaudoControlloQualitativoPost;
import it.monchieri.thip.target.qualita.CicloCollaudoFucinatura;
import it.monchieri.thip.target.qualita.CicloCollaudoGrezzoPostForgia;
import it.monchieri.thip.target.qualita.CicloCollaudoHbSuFacciaT;
import it.monchieri.thip.target.qualita.CicloCollaudoImportatore;
import it.monchieri.thip.target.qualita.CicloCollaudoPreliminare;
import it.monchieri.thip.target.qualita.CicloCollaudoQualita;
import it.monchieri.thip.target.qualita.CicloCollaudoRicottura;
import it.monchieri.thip.target.qualita.CicloCollaudoSupplementare;
import it.thera.thip.cs.DatiComuniEstesi;
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

	List<QcTabellaAttivita> attivitaTarget = null;

	private InterfacciaTarget interfacciaTarget = null;

	@Override
	protected boolean run() {
		String dbName = IniFile.getValue("thermfw.ini","Web", "Database");
		dbName = dbName.substring(0,dbName.indexOf(","));
		output.println(" ** Importazione cicli di controllo da Target");
		boolean isOk = true;
		if(dbName.equals("PANTH01")) {
			interfacciaTarget = InterfacciaTarget.getInterfacciaTarget();
			if(interfacciaTarget != null) {
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
			}else {
				isOk = false;
				output.println("Impossibile avviare il lavoro, va prima definita la pers. dati di Target .. ");
			}
		}
		output.println(" ** Termine importazione cicli di controllo da Target");
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
					case CicloCollaudoImportatore.CICLO_FUCINATURA:
						QcTabellaAttivita attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							CicloCollaudoFucinatura importatore = (CicloCollaudoFucinatura) Factory.createObject(CicloCollaudoFucinatura.class);
							ciclo = importatore.codificaCicloCollaudoPanthera(collaudo, attivita);
						}
						break;
					case CicloCollaudoImportatore.CICLO_CONTROLLO_GREZZO_POST_FORGIA:
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							CicloCollaudoGrezzoPostForgia importatore = (CicloCollaudoGrezzoPostForgia) Factory.createObject(CicloCollaudoGrezzoPostForgia.class);
							ciclo = importatore.codificaCicloCollaudoPanthera(collaudo, attivita);
						}
						break;
					case CicloCollaudoImportatore.CICLO_CONTROLLO_HB_SU_FACCIA_T: 
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							CicloCollaudoHbSuFacciaT importatore = (CicloCollaudoHbSuFacciaT) Factory.createObject(CicloCollaudoHbSuFacciaT.class);
							ciclo = importatore.codificaCicloCollaudoPanthera(collaudo, attivita);
						}
						break;
					case CicloCollaudoImportatore.CICLO_CONTROLLO_QUALITATIVO_POST:
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							CicloCollaudoControlloQualitativoPost importatore = (CicloCollaudoControlloQualitativoPost) Factory.createObject(CicloCollaudoControlloQualitativoPost.class);						
							ciclo = importatore.codificaCicloCollaudoPanthera(collaudo, attivita);
						}
						break;
					case CicloCollaudoImportatore.CICLO_RICOTTURA: 
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							CicloCollaudoRicottura importatore = (CicloCollaudoRicottura) Factory.createObject(CicloCollaudoRicottura.class);
							ciclo = importatore.codificaCicloCollaudoPanthera(collaudo, attivita);
						}
						break;
					case CicloCollaudoImportatore.CICLO_PRELIMINARE: 
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							CicloCollaudoPreliminare importatore = (CicloCollaudoPreliminare) Factory.createObject(CicloCollaudoPreliminare.class);					
							ciclo = importatore.codificaCicloCollaudoPanthera(collaudo, attivita);
						}
						break;
					case CicloCollaudoImportatore.CICLO_QUALITA:
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							CicloCollaudoQualita importatore = (CicloCollaudoQualita) Factory.createObject(CicloCollaudoQualita.class);
							ciclo = importatore.codificaCicloCollaudoPanthera(collaudo, attivita);
						}
						break;
					case CicloCollaudoImportatore.CICLO_SUPPLEMENTARE:
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							CicloCollaudoSupplementare importatore = (CicloCollaudoSupplementare) Factory.createObject(CicloCollaudoSupplementare.class);
							ciclo = importatore.codificaCicloCollaudoPanthera(collaudo, attivita);
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
							ciclo.setMisureObbligatorie(false);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected CicloCollaudoTestata gestioneCicloTTPreliminare(QcTabellaAttivita attivita,
			OrdEsecAtvSogCollaudo collaudo) throws SQLException {
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

	@SuppressWarnings("unchecked")
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

			CicloCollaudoCaratteristica posNeg = caratteristicaCicloCollaudo(faseControlloUT, (short) 1, "Positivo / Negativo", "Pos. / Neg.",collaudo);
			posNeg.setTipoCaratteristica('2');
			posNeg.setPercDaControllare(new BigDecimal(100));
			posNeg.setNumMaxDifettiAccettazione(0);
			posNeg.setNumMinDifettiRifiuto(1);
			posNeg.setRilavLimInfTol(false);
			posNeg.setRilavLimSupTol(false);

			faseControlloUT.getCaratteristiche().add(posNeg);

			// Seconda caratteristica
			CicloCollaudoCaratteristica c2 = caratteristicaCicloCollaudo(faseControlloUT, (short) 2, "1 MDR", "1 MDR", collaudo);
			c2.setTipoCaratteristica('1');
			c2.setIdUnitaMisura("mm");
			c2.setLimInfTolleranza(new BigDecimal("-9999"));
			c2.setLimSupTolleranza(new BigDecimal("9999"));
			c2.setPercDaControllare(new BigDecimal(100));
			c2.setNumMaxDifettiAccettazione(0);
			c2.setNumMinDifettiRifiuto(1);
			c2.setRilavLimInfTol(false);
			c2.setRilavLimSupTol(false);

			faseControlloUT.getCaratteristiche().add(c2);

			// Terza caratteristica
			CicloCollaudoCaratteristica c3 = caratteristicaCicloCollaudo(faseControlloUT, (short) 3, "1 Spessore", "1 Spessore", collaudo);
			c3.setTipoCaratteristica('1');
			c3.setIdUnitaMisura("mm");
			c3.setLimInfTolleranza(new BigDecimal("-9999"));
			c3.setLimSupTolleranza(new BigDecimal("9999"));
			c3.setPercDaControllare(new BigDecimal(100));
			c3.setNumMaxDifettiAccettazione(0);
			c3.setNumMinDifettiRifiuto(1);
			c3.setRilavLimInfTol(false);
			c3.setRilavLimSupTol(false);

			faseControlloUT.getCaratteristiche().add(c3);

			// Quarta caratteristica
			CicloCollaudoCaratteristica c4 = caratteristicaCicloCollaudo(faseControlloUT, (short) 4, "2 MDR", "2 MDR", collaudo);
			c4.setTipoCaratteristica('1');
			c4.setIdUnitaMisura("mm");
			c4.setLimInfTolleranza(new BigDecimal("-9999"));
			c4.setLimSupTolleranza(new BigDecimal("9999"));
			c4.setPercDaControllare(new BigDecimal(100));
			c4.setNumMaxDifettiAccettazione(0);
			c4.setNumMinDifettiRifiuto(1);
			c4.setRilavLimInfTol(false);
			c4.setRilavLimSupTol(false);

			faseControlloUT.getCaratteristiche().add(c4);

			// Quinta caratteristica
			CicloCollaudoCaratteristica c5 = caratteristicaCicloCollaudo(faseControlloUT, (short) 5, "2 Spessore", "2 Spessore", collaudo);
			c5.setTipoCaratteristica('1');
			c5.setLimInfTolleranza(new BigDecimal("-9999"));
			c5.setIdUnitaMisura("mm");
			c5.setLimSupTolleranza(new BigDecimal("9999"));
			c5.setPercDaControllare(new BigDecimal(100));
			c5.setNumMaxDifettiAccettazione(0);
			c5.setNumMinDifettiRifiuto(1);
			c5.setRilavLimInfTol(false);
			c5.setRilavLimSupTol(false);

			faseControlloUT.getCaratteristiche().add(c5);

			// Sesta caratteristica
			CicloCollaudoCaratteristica c6 = caratteristicaCicloCollaudo(faseControlloUT, (short) 6, "3 MDR", "3 MDR", collaudo);
			c6.setTipoCaratteristica('1');
			c6.setIdUnitaMisura("mm");
			c6.setLimInfTolleranza(new BigDecimal("-9999"));
			c6.setLimSupTolleranza(new BigDecimal("9999"));
			c6.setPercDaControllare(new BigDecimal(100));
			c6.setNumMaxDifettiAccettazione(0);
			c6.setNumMinDifettiRifiuto(1);
			c6.setRilavLimInfTol(false);
			c6.setRilavLimSupTol(false);

			faseControlloUT.getCaratteristiche().add(c6);

			// Settima caratteristica
			CicloCollaudoCaratteristica c7 = caratteristicaCicloCollaudo(faseControlloUT, (short) 7, "3 Spessore", "3 Spessore", collaudo);
			c7.setTipoCaratteristica('1');
			c7.setIdUnitaMisura("mm");
			c7.setLimInfTolleranza(new BigDecimal("-9999"));
			c7.setLimSupTolleranza(new BigDecimal("9999"));
			c7.setPercDaControllare(new BigDecimal(100));
			c7.setNumMaxDifettiAccettazione(0);
			c7.setNumMinDifettiRifiuto(1);
			c7.setRilavLimInfTol(false);
			c7.setRilavLimSupTol(false);

			faseControlloUT.getCaratteristiche().add(c7);

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseControlloDimensionale = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Controllo Dimensionale", "Ctrl DIM");

			faseControlloDimensionale.setTipoCaratteristiche('1');
			faseControlloDimensionale.setRaccoltaDati('1');
			faseControlloDimensionale.setCampionamento('2');
			faseControlloDimensionale.setTipoFrequenzaControllo('6');
			faseControlloDimensionale.setValNominaleObbligatorio(false);
			faseControlloDimensionale.setControlloEsterno(false);
			faseControlloDimensionale.setQtaDaControllare(BigDecimal.ONE);
			faseControlloDimensionale.setNumMaxDifettiPerAccettaz(0);
			faseControlloDimensionale.setNumMinDifettiPerRifiuto(1);
			faseControlloDimensionale.setCampionamento('1');
			faseControlloDimensionale.setLivelloCampionamento('2');
			faseControlloDimensionale.setLivelloIspezione('6');
			faseControlloDimensionale.setNormativaCartaCtr(DGT);

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseControlloDimensionale.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseControlloDimensionale);

			CicloCollaudoCaratteristica carattCntrldiM = caratteristicaCicloCollaudo(faseControlloDimensionale, (short) 1, "Controllo Dimensionale", "Ctrl DIM",collaudo);
			carattCntrldiM.setTipoCaratteristica('2');
			carattCntrldiM.setPercDaControllare(new BigDecimal(100));
			carattCntrldiM.setNumMaxDifettiAccettazione(0);
			carattCntrldiM.setNumMinDifettiRifiuto(1);
			carattCntrldiM.setRilavLimInfTol(false);
			carattCntrldiM.setRilavLimSupTol(false);
			faseControlloDimensionale.getCaratteristiche().add(carattCntrldiM);

			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Controllo Durezza Kg", "Ctrl HB Kg");

			faseDurezzaKg.setTipoCaratteristiche('1');
			faseDurezzaKg.setRaccoltaDati('1');
			faseDurezzaKg.setCampionamento('2');
			faseDurezzaKg.setTipoFrequenzaControllo('6');
			faseDurezzaKg.setValNominaleObbligatorio(false);
			faseDurezzaKg.setControlloEsterno(false);
			faseDurezzaKg.setQtaDaControllare(BigDecimal.ONE);
			faseDurezzaKg.setNumMaxDifettiPerAccettaz(0);
			faseDurezzaKg.setNumMinDifettiPerRifiuto(1);
			faseDurezzaKg.setCampionamento('1');
			faseDurezzaKg.setLivelloCampionamento('2');
			faseDurezzaKg.setLivelloIspezione('6');
			faseDurezzaKg.setNormativaCartaCtr(DGT);

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseDurezzaKg.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseDurezzaKg);

			// Caratteristica 1
			CicloCollaudoCaratteristica durezza1 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 1, "Durezza Kg", "Durezza Kg", collaudo);
			durezza1.setTipoCaratteristica('1');
			durezza1.setPercDaControllare(new BigDecimal(100));
			durezza1.setNumMaxDifettiAccettazione(0);
			durezza1.setNumMinDifettiRifiuto(1);
			durezza1.setRilavLimInfTol(true);
			durezza1.setRilavLimSupTol(true);
			faseDurezzaKg.getCaratteristiche().add(durezza1);

			// Caratteristica 2
			CicloCollaudoCaratteristica durezza2 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 2, "Durezza Kg", "Durezza Kg", collaudo);
			durezza2.setTipoCaratteristica('1');
			durezza2.setPercDaControllare(new BigDecimal(100));
			durezza2.setNumMaxDifettiAccettazione(0);
			durezza2.setNumMinDifettiRifiuto(1);
			durezza2.setRilavLimInfTol(true);
			durezza2.setRilavLimSupTol(true);
			faseDurezzaKg.getCaratteristiche().add(durezza2);

			// Caratteristica 3
			CicloCollaudoCaratteristica durezza3 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 3, "Durezza Kg", "Durezza Kg", collaudo);
			durezza3.setTipoCaratteristica('1');
			durezza3.setPercDaControllare(new BigDecimal(100));
			durezza3.setNumMaxDifettiAccettazione(0);
			durezza3.setNumMinDifettiRifiuto(1);
			durezza3.setRilavLimInfTol(true);
			durezza3.setRilavLimSupTol(true);
			faseDurezzaKg.getCaratteristiche().add(durezza3);

			// Caratteristica 4
			CicloCollaudoCaratteristica durezza4 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 4, "Durezza Kg", "Durezza Kg", collaudo);
			durezza4.setTipoCaratteristica('1');
			durezza4.setPercDaControllare(new BigDecimal(100));
			durezza4.setNumMaxDifettiAccettazione(0);
			durezza4.setNumMinDifettiRifiuto(1);
			durezza4.setRilavLimInfTol(true);
			durezza4.setRilavLimSupTol(true);
			faseDurezzaKg.getCaratteristiche().add(durezza4);

			// Caratteristica 5
			CicloCollaudoCaratteristica durezza5 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 5, "Durezza Kg", "Durezza Kg", collaudo);
			durezza5.setTipoCaratteristica('1');
			durezza5.setPercDaControllare(new BigDecimal(100));
			durezza5.setNumMaxDifettiAccettazione(0);
			durezza5.setNumMinDifettiRifiuto(1);
			durezza5.setRilavLimInfTol(true);
			durezza5.setRilavLimSupTol(true);
			faseDurezzaKg.getCaratteristiche().add(durezza5);

			// Caratteristica 6
			CicloCollaudoCaratteristica durezza6 = caratteristicaCicloCollaudo(faseDurezzaKg, (short) 6, "Durezza Kg", "Durezza Kg", collaudo);
			durezza6.setTipoCaratteristica('1');
			durezza6.setPercDaControllare(new BigDecimal(100));
			durezza6.setNumMaxDifettiAccettazione(0);
			durezza6.setNumMinDifettiRifiuto(1);
			durezza6.setRilavLimInfTol(true);
			durezza6.setRilavLimSupTol(true);
			faseDurezzaKg.getCaratteristiche().add(durezza6);


			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseMagnetico = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Magnetico", "Magnetico");

			faseMagnetico.setTipoCaratteristiche('1');
			faseMagnetico.setRaccoltaDati('1');
			faseMagnetico.setCampionamento('2');
			faseMagnetico.setTipoFrequenzaControllo('6');
			faseMagnetico.setValNominaleObbligatorio(false);
			faseMagnetico.setControlloEsterno(false);
			faseMagnetico.setQtaDaControllare(BigDecimal.ONE);
			faseMagnetico.setNumMaxDifettiPerAccettaz(0);
			faseMagnetico.setNumMinDifettiPerRifiuto(1);
			faseMagnetico.setCampionamento('1');
			faseMagnetico.setLivelloCampionamento('2');
			faseMagnetico.setLivelloIspezione('6');
			faseMagnetico.setNormativaCartaCtr(DGT);

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseMagnetico.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseMagnetico);

			CicloCollaudoCaratteristica posNeg6403 = caratteristicaCicloCollaudo(
					faseMagnetico, (short) 1, "Positivo / Negativo", "Pos./Neg.", collaudo
					);

			posNeg6403.setTipoCaratteristica('1');
			posNeg6403.setPercDaControllare(new BigDecimal(100));
			posNeg6403.setNumMaxDifettiAccettazione(0);
			posNeg6403.setNumMinDifettiRifiuto(1);
			posNeg6403.setRilavLimInfTol(false);
			posNeg6403.setRilavLimSupTol(false);

			faseMagnetico.getCaratteristiche().add(posNeg6403);


			idSequenzaFase = idSequenzaFase+1;
			idSequenzaVisualizz = idSequenzaVisualizz+1;

			CicloCollaudoFase faseLiquidiPenetranti = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Liquidi Penetranti", "Liquidi Penetra");

			faseLiquidiPenetranti.setTipoCaratteristiche('1');
			faseLiquidiPenetranti.setRaccoltaDati('1');
			faseLiquidiPenetranti.setCampionamento('2');
			faseLiquidiPenetranti.setTipoFrequenzaControllo('6');
			faseLiquidiPenetranti.setValNominaleObbligatorio(false);
			faseLiquidiPenetranti.setControlloEsterno(false);
			faseLiquidiPenetranti.setQtaDaControllare(BigDecimal.ONE);
			faseLiquidiPenetranti.setNumMaxDifettiPerAccettaz(0);
			faseLiquidiPenetranti.setNumMinDifettiPerRifiuto(1);
			faseLiquidiPenetranti.setCampionamento('1');
			faseLiquidiPenetranti.setLivelloCampionamento('2');
			faseLiquidiPenetranti.setLivelloIspezione('6');
			faseLiquidiPenetranti.setNormativaCartaCtr(DGT);

			if(collaudo.getNoteFaseControlloQualitativoPost().containsKey(idSequenzaFase.toString())) {
				faseLiquidiPenetranti.setNote(collaudo.getNoteFaseControlloQualitativoPost().get(idSequenzaFase.toString()));
			}

			ciclo.getFasi().add(faseLiquidiPenetranti);

			CicloCollaudoCaratteristica posNeg6404 = caratteristicaCicloCollaudo(
					faseLiquidiPenetranti, (short) 1, "Positivo / Negativo", "Pos./Neg.", collaudo
					);

			posNeg6404.setTipoCaratteristica('1');
			posNeg6404.setPercDaControllare(new BigDecimal(100));
			posNeg6404.setNumMaxDifettiAccettazione(0);
			posNeg6404.setNumMinDifettiRifiuto(1);
			posNeg6404.setRilavLimInfTol(false);
			posNeg6404.setRilavLimSupTol(false);

			faseLiquidiPenetranti.getCaratteristiche().add(posNeg6404);

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


	public QcTabellaAttivita ricercaAttivita(String nomeTabella, String idAttivita) {
		return attivitaTarget.stream()
				.filter(attivita -> nomeTabella.equals(attivita.getNomeTabella()) && idAttivita.equals(attivita.getIdAttivita()))
				.findFirst()
				.orElse(null);
	}

	@SuppressWarnings("rawtypes")
	protected List<OrdEsecAtvSogCollaudo> leggiCollaudi() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		long start = System.currentTimeMillis();
		long stop = System.currentTimeMillis();
		output.println(" Estraggo da Target collaudi da processare");
		ImportazioneCicliControlloHelper importazioneHelper = ImportazioneCicliControlloHelper.getInstance();
		List<OrdEsecAtvSogCollaudo> collaudi = new ArrayList<OrdEsecAtvSogCollaudo>();
		ConnectionDescriptor cnd = interfacciaTarget.getConnectionDescriptor();
		int count_da_importare = 0;
		int count_da_aggiornare = 0;
		try {
			if(cnd != null) {
				ConnectionManager.pushConnection(cnd);
				attivitaTarget = QcTabellaAttivita.listaAttivita();
				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C02_FUCINATURE));
				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_CONTROLLO_GREZZO_POST_FORGI));
				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_CONTROLLO_HB_SU_FACCIA_DI_T));

				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_CONTROLLO_POST_LM1));

				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_CONTROLLO_QUALITATIVO_POST));
				//				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_TT_DI_RICOTTURA));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_TT_PRELIMINARE));

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
