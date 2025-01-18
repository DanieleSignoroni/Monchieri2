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

import it.monchieri.thip.produzione.ordese.YOrdineEsecutivo;
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
			long start = System.currentTimeMillis();
			long stop = System.currentTimeMillis();
			output.println("Estraggo collaudi da processare");
			List<OrdEsecAtvSogCollaudo> collaudi = leggiCollaudi();
			if(collaudi != null && attivitaTarget != null) {
				stop = System.currentTimeMillis();
				output.println("Tempo estrazione collaudi : "+((stop - start)/1000)+" secondi");
				output.println("Ho trovato "+collaudi.size()+" cicli da importare");
				start = stop;
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
			YOrdineEsecutivo ordineEsec = (YOrdineEsecutivo) ordineEsecutivoDaCommessa(collaudi.get(0).getCommessa());
			if(ordineEsec == null)
				continue;
			for (Iterator iterator = collaudi.iterator(); iterator.hasNext();) {
				OrdEsecAtvSogCollaudo collaudo = (OrdEsecAtvSogCollaudo) iterator.next();
				String tipoCiclo = collaudo.getNomeTabella();
				CicloCollaudoTestata ciclo = null;
				try {
					switch (tipoCiclo) {
					case "C02_FUCINATURA":
						QcTabellaAttivita attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							if(!ciclo.isOnDB()) {

								CicloCollaudoFase faseRiscaldo = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Riscaldo", "Riscaldo");

								NoteAttivitaSogCollaudo commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
								if(commento != null)
									codificaCommento(faseRiscaldo.getCommenti(),commento);

								ciclo.getFasi().add(faseRiscaldo);

								faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 1, "Temperatura Iniziale", "Temperatura Ini",collaudo));
								faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter",collaudo));
								faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 4, "1 Temperatura Fine Rampa", "1 Temperatura F",collaudo));
								faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 5, "1 Permanenza", "1 Permanenza",collaudo));
								faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 6, "2 Gradiente Termico", "2 Gradiente Ter",collaudo));
								faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 8, "2 Temperatura Fine Rampa", "2 Temperatura F",collaudo));
								faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 9, "2 Permanenza", "2 Permanenza",collaudo));
								faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 10, "3 Gradiente Termico", "3 Gradiente Ter",collaudo));
								faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 12, "3 Temperatura Fine Rampa", "3 Temperatura F",collaudo));
								faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 13, "3 Permanenza", "3 Permanenza",collaudo));
								faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 14, "4 Gradiente Termico", "4 Gradiente Ter",collaudo));
								faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 16, "4 Temperatura Fine Rampa", "4 Temperatura F",collaudo));
								faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 17, "4 Permanenza", "4 Permanenza",collaudo));

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

								CicloCollaudoFase faseTempForgia = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Temperatura di Forgia", "Forgia");

								commento = collaudo.commentoAttivita(idSequenzaFase, idSequenzaVisualizz);
								if(commento != null)
									codificaCommento(faseTempForgia.getCommenti(),commento);

								ciclo.getFasi().add(faseTempForgia);

								faseTempForgia.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTempForgia, (short) 1, "Numero Calde", "Numero Calde",collaudo));
								faseTempForgia.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTempForgia, (short) 2, "°C Minimi", "°C Minimi",collaudo));
								faseTempForgia.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTempForgia, (short) 4, "°C Massimi", "°C Massimi",collaudo));

							}else {
								//Su db quindi gestisco solo i commenti
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
						break;
					case "C03_CONTROLLO_GREZZO_POST_FORGI":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							if(!ciclo.isOnDB()) {

								if(collaudo.getRiferimentoProceduraControlloGrezzoPostForgia() != null) {
									ciclo.setRiferimentoProcedura(collaudo.getRiferimentoProceduraControlloGrezzoPostForgia());
								}

								CicloCollaudoFase faseDimensionale = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Dimensionale", "Dimensionale");

								ciclo.getFasi().add(faseDimensionale);

								faseDimensionale.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDimensionale, (short) 1, "Positivo / Negativo", "Pos. / Neg.",collaudo));

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

								CicloCollaudoFase faseDurezzaConSpina = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Con Spina Kg", "Durezza Con Spi");

								ciclo.getFasi().add(faseDurezzaConSpina);

								faseDurezzaConSpina.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 1, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaConSpina.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 2, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaConSpina.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 3, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaConSpina.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 4, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaConSpina.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 5, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaConSpina.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 6, "Durezza Kg", "Durezza Kg",collaudo));

							}

						}

						break;
					case "C03_CONTROLLO_HB_SU_FACCIA_DI_T": 
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							if(!ciclo.isOnDB()) {

								CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Kg", "Durezza Kg");

								ciclo.getFasi().add(faseDurezzaKg);

								faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 1, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 2, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 3, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 4, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 5, "Durezza Kg", "Durezza Kg",collaudo));
								faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 6, "Durezza Kg", "Durezza Kg",collaudo));

							}


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

						}
						break;
					case "C03_TT_DI_RICOTTURA": 
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							if(ciclo.isOnDB()) {

								if(collaudo.getRiferimentoProeduraTtRicottura() != null) {
									ciclo.setRiferimentoProcedura(collaudo.getRiferimentoProeduraTtRicottura());
								}

								CicloCollaudoFase faseVisivo = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Visivo", "Visivo");

								ciclo.getFasi().add(faseVisivo);

								faseVisivo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseVisivo, (short) 1, "Positivo / Negativo", "Pos. / Neg.",collaudo));

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

								CicloCollaudoFase faseCPezzoRicott = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "°C Pezzo in Ricottura", "°C Pezzo in Ric");

								ciclo.getFasi().add(faseCPezzoRicott);

								faseCPezzoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseCPezzoRicott, (short) 1, "Temperatura Pezzo", "Temperatura P",collaudo));

								idSequenzaFase = idSequenzaFase+1;
								idSequenzaVisualizz = idSequenzaVisualizz+1;

								CicloCollaudoFase faseFornoRicott = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Forno di Ricottura", "Forno di Ricott");

								ciclo.getFasi().add(faseFornoRicott);

								faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 1, "Temperatura Iniziale", "Temperatura ini",collaudo));
								faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter",collaudo));
								faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F",collaudo));
								faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 4, "1 Permanenza", "1 Permanenza",collaudo));
								faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter",collaudo));
								faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F",collaudo));
								faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 7, "2 Permanenza", "2 Permanenza",collaudo));
								faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter",collaudo));
								faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F",collaudo));
								faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 10, "3 Permanenza", "3 Permanenza",collaudo));
								faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter",collaudo));
								faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F",collaudo));
								faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 13, "4 Permanenza", "4 Permanenza",collaudo));

							}
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

	protected List<OrdEsecAtvSogCollaudo> leggiCollaudi() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		ImportazioneCicliControlloHelper importazioneHelper = ImportazioneCicliControlloHelper.getInstance();
		List<OrdEsecAtvSogCollaudo> collaudi = new ArrayList<OrdEsecAtvSogCollaudo>();
		ConnectionDescriptor cnd = YDtsxPtExpOrdEsecRunner.externalConnectionDescriptor(
				NOME_DB_EXT,
				UTENTE_DB_EXT,
				PWD_DB_EXT, 
				SRV_DB_EXT, 
				PORTA_DB_EXT);
		try {
			if(cnd != null) {
				ConnectionManager.pushConnection(cnd);
				attivitaTarget = QcTabellaAttivita.listaAttivita();
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C02_FUCINATURE));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_CONTROLLO_GREZZO_POST_FORGI));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_CONTROLLO_HB_SU_FACCIA_DI_T));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_CONTROLLO_POST_LM1));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_CONTROLLO_QUALITATIVO_POST));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_TT_DI_RICOTTURA));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_TT_PRELIMINARE));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_TT_QUALITA));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_TT_SUPPLEMENTARE));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C03_UT_PRE_TT));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C05_CONTROLLO_LABORATORIO));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C06_CONTROLLI_NDT));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SEELCT_C06_CONTROLLO_DUREZZE_DIMENSIONI));
				collaudi.addAll(importazioneHelper.leggiCicliControllo(ImportazioneCicliControlloStatementHelper.STMT_SELECT_C06_CONTROLLO_INTERMEDIO));
				for(OrdEsecAtvSogCollaudo collaudo : collaudi) {
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
						collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARAT_FUCINATURA_C02);
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
						collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARAT_CTRL_GREZZO_POST_FORG);
						break;
					case "C03_CONTROLLO_HB_SU_FACCIA_DI_T":
						collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARAT_CONTROLLO_HB_SFT);
						break;
					case "C03_CONTROLLO_QUALITATIVO_POST":
						collaudo.leggiRiferimentoProceduraControlloQualitativoPost();
						collaudo.leggiNoteFaseControlloQualitativoPost();
						collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARAT_CONTROLLO_QUALITATIVO_POST);
						break;
					case "C03_TT_DI_RICOTTURA":
						collaudo.leggiRiferimentoProceduraTtRicottura();
						collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARAT_TT_RICOTTURA);
						break;
					case "C03_UT_PRE-TT":
						collaudo.leggiRiferimentoProcedutaUtPreTT();
						collaudo.leggiCaratteristiche(TargetCaratteristicheStatement.STMT_SELECT_CARAT_UT_PRE_TT);
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException ex) {
			collaudi.clear();
			ex.printStackTrace(Trace.excStream);
		} finally {
			if (cnd != null) {
				try {
					cnd.closeConnection();
				} catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
				ConnectionManager.popConnection(cnd);
			}
		}
		return collaudi;
	}

	@Override
	protected String getClassAdCollectionName() {
		return "YDtsxImportCicliCollaudo";
	}

}
