package it.monchieri.thip.dtsx;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.security.Authorizable;

import it.monchieri.thip.produzione.ordese.YOrdineEsecutivo;
import it.monchieri.thip.target.OrdEsecAtvSogCollaudo;
import it.monchieri.thip.target.OrdEsecAtvSogCollaudoPO;
import it.monchieri.thip.target.QcTabellaAttivita;
import it.thera.thip.base.azienda.Azienda;
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

	public static final String NOME_DB_EXT = "PantheraTarget_test";
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
			//String key = entry.getKey();
			List<OrdEsecAtvSogCollaudoPO> collaudi = entry.getValue();
			for (Iterator iterator = collaudi.iterator(); iterator.hasNext();) {
				OrdEsecAtvSogCollaudo collaudo = (OrdEsecAtvSogCollaudo) iterator.next();
				String tipoCiclo = collaudo.getNomeTabella();
				YOrdineEsecutivo ordineEsec = (YOrdineEsecutivo) ordineEsecutivoDaCommessa(collaudo.getCommessa());
				if(ordineEsec != null) {
					String idOrp = generateOrpEffDocId(ordineEsec.getIdNumeroOrdine());
					CicloCollaudoTestata ciclo = null;
					switch (tipoCiclo) {
					case "C02_FUCINATURA":

						QcTabellaAttivita attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							CicloCollaudoFase faseRiscaldo = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Riscaldo", "Riscaldo");

							ciclo.getFasi().add(faseRiscaldo);

							faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 1, "Temperatura Iniziale", "Temperatura Ini"));
							faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 4, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 5, "1 Permanenza", "1 Permanenza"));
							faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 6, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 8, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 9, "2 Permanenza", "2 Permanenza"));
							faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 10, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 12, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 13, "3 Permanenza", "3 Permanenza"));
							faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 14, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 16, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseRiscaldo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseRiscaldo, (short) 17, "4 Permanenza", "4 Permanenza"));

							//alla fase di riscaldo vanno aggiunti i commenti

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseTempForgia = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Temperatura di Forgia", "Forgia");

							ciclo.getFasi().add(faseTempForgia);

							faseTempForgia.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTempForgia, (short) 1, "Numero Calde", "Numero Calde"));
							faseTempForgia.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTempForgia, (short) 2, "°C Minimi", "°C Minimi"));
							faseTempForgia.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTempForgia, (short) 4, "°C Massimi", "°C Massimi"));
						}

						break;
					case "C03_CONTROLLO_GREZZO_POST_FORGI":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							CicloCollaudoFase faseDimensionale = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Dimensionale", "Dimensionale");

							ciclo.getFasi().add(faseDimensionale);

							faseDimensionale.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDimensionale, (short) 1, "Positivo / Negativo", "Pos. / Neg."));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseDurezzaConSpina = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Con Spina Kg", "Durezza Con Spi");

							ciclo.getFasi().add(faseDurezzaConSpina);

							faseDurezzaConSpina.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 1, "Durezza Kg", "Durezza Kg"));
							faseDurezzaConSpina.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 2, "Durezza Kg", "Durezza Kg"));
							faseDurezzaConSpina.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 3, "Durezza Kg", "Durezza Kg"));
							faseDurezzaConSpina.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 4, "Durezza Kg", "Durezza Kg"));
							faseDurezzaConSpina.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 5, "Durezza Kg", "Durezza Kg"));
							faseDurezzaConSpina.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaConSpina, (short) 6, "Durezza Kg", "Durezza Kg"));

						}

						break;
					case "C03_CONTROLLO_HB_SU_FACCIA_DI_T": 
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Kg", "Durezza Kg");

							ciclo.getFasi().add(faseDurezzaKg);

							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 1, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 2, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 3, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 4, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 5, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 6, "Durezza Kg", "Durezza Kg"));



						}
						break;
					case "C03_CONTROLLO_POST-LM1":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							CicloCollaudoFase faseDimensionale = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Dimensionale", "Dimensionale");

							ciclo.getFasi().add(faseDimensionale);

							faseDimensionale.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDimensionale, (short) 1, "Positivo / Negativo", "Pos. / Neg."));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

						}
						break;
					case "C03_CONTROLLO_QUALITATIVO_POST":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							CicloCollaudoFase faseControlloUT = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Controllo UT", "Ctrl UT");

							ciclo.getFasi().add(faseControlloUT);

							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 1, "Positivo / Negativo", "Pos. / Neg."));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 2, "1 MDR", "1 MDR"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 3, "1 Spessore", "1 Spessore"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 4, "2 MDR", "2 MDR"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 5, "2 Spessore", "2 Spessore"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 6, "3 MDR", "3 MDR"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 7, "3 Spessore", "3 Spessore"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseControlloDimensionale = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Controllo Dimensionale", "Ctrl DIM");

							ciclo.getFasi().add(faseControlloDimensionale);

							faseControlloDimensionale.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloDimensionale, (short) 1, "Controllo Dimensionale", "Ctrl DIM"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Controllo Durezza Kg", "Ctrl HB Kg");

							ciclo.getFasi().add(faseDurezzaKg);

							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 1, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 2, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 3, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 4, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 5, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 6, "Durezza Kg", "Durezza Kg"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseMagnetico = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Magnetico", "Magnetico");

							ciclo.getFasi().add(faseMagnetico);

							faseMagnetico.getCaratteristiche().add(caratteristicaCicloCollaudo(faseMagnetico, (short) 1, "Positivo / Negativo", "Pos./Neg."));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseLiquidiPenetranti = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Liquidi Penetranti", "Liquidi Penetra");

							ciclo.getFasi().add(faseLiquidiPenetranti);

							faseLiquidiPenetranti.getCaratteristiche().add(caratteristicaCicloCollaudo(faseLiquidiPenetranti, (short) 1, "Positivo / Negativo", "Pos./Neg."));

						}
						break;
					case "C03_TT_DI_RICOTTURA": 
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							CicloCollaudoFase faseVisivo = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Visivo", "Visivo");

							ciclo.getFasi().add(faseVisivo);

							faseVisivo.getCaratteristiche().add(caratteristicaCicloCollaudo(faseVisivo, (short) 1, "Positivo / Negativo", "Pos. / Neg."));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseCPezzoRicott = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "°C Pezzo in Ricottura", "°C Pezzo in Ric");

							ciclo.getFasi().add(faseCPezzoRicott);

							faseCPezzoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseCPezzoRicott, (short) 1, "Temperatura Pezzo", "Temperatura P"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseFornoRicott = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Forno di Ricottura", "Forno di Ricottura");

							ciclo.getFasi().add(faseFornoRicott);

							faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 4, "1 Permanenza", "1 Permanenza"));
							faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 7, "2 Permanenza", "2 Permanenza"));
							faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 10, "3 Permanenza", "3 Permanenza"));
							faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseFornoRicott.getCaratteristiche().add(caratteristicaCicloCollaudo(faseFornoRicott, (short) 13, "4 Permanenza", "4 Permanenza"));


						}
						break;
					case "C03_TT-PRELIMINARE": 
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);
							
							if(collaudo.getNote_C03_TT_PRELIMINARE() != null) {
								codificaCommento(ciclo.getCommenti(), "it", "SGQ_CICLI_TES", "SGQ_CICLI_TES", collaudo.getNote_C03_TT_PRELIMINARE());
							}

							CicloCollaudoFase faseTT1 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "1-Trattamento Termico", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_PRELIMINARE() != null && collaudo.getDescrizioniFase_C03_TT_PRELIMINARE().containsKey(idSequenzaFase)) {
								codificaCommento(faseTT1.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_PRELIMINARE().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseTT1);

							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 4, "1 Permanenza", "1 Permanenza"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 7, "2 Permanenza", "2 Permanenza"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 10, "3 Permanenza", "3 Permanenza"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 13, "4 Permanenza", "4 Permanenza"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseTT2 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "2-Trattamento Termico", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_PRELIMINARE() != null && collaudo.getDescrizioniFase_C03_TT_PRELIMINARE().containsKey(idSequenzaFase)) {
								codificaCommento(faseTT2.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_PRELIMINARE().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseTT2);

							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 4, "1 Permanenza", "1 Permanenza"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 7, "2 Permanenza", "2 Permanenza"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 10, "3 Permanenza", "3 Permanenza"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 13, "4 Permanenza", "4 Permanenza"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseTT3 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "3-Trattamento Termico", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_PRELIMINARE() != null && collaudo.getDescrizioniFase_C03_TT_PRELIMINARE().containsKey(idSequenzaFase)) {
								codificaCommento(faseTT3.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_PRELIMINARE().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseTT3);

							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 4, "1 Permanenza", "1 Permanenza"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 7, "2 Permanenza", "2 Permanenza"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 10, "3 Permanenza", "3 Permanenza"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 13, "4 Permanenza", "4 Permanenza"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseTT4 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "4-Trattamento Termico", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_PRELIMINARE() != null && collaudo.getDescrizioniFase_C03_TT_PRELIMINARE().containsKey(idSequenzaFase)) {
								codificaCommento(faseTT4.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_PRELIMINARE().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseTT4);

							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 4, "1 Permanenza", "1 Permanenza"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 7, "2 Permanenza", "2 Permanenza"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 10, "3 Permanenza", "3 Permanenza"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 13, "4 Permanenza", "4 Permanenza"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Kg", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_PRELIMINARE() != null && collaudo.getDescrizioniFase_C03_TT_PRELIMINARE().containsKey(idSequenzaFase)) {
								codificaCommento(faseDurezzaKg.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_PRELIMINARE().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseDurezzaKg);

							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 1, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 2, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 3, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 4, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 5, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 6, "Durezza Kg", "Durezza Kg"));

						}
						break;
					case "C03_TT-QUALITA":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);
							
							if(collaudo.getNote_C03_TT_QUALITA() != null) {
								codificaCommento(ciclo.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getNote_C03_TT_QUALITA());
							}

							CicloCollaudoFase faseTT1 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "1-Trattamento Termico", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_QUALITA() != null && collaudo.getDescrizioniFase_C03_TT_QUALITA().containsKey(idSequenzaFase)) {
								codificaCommento(faseTT1.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_QUALITA().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseTT1);

							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 4, "1 Permanenza", "1 Permanenza"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 7, "2 Permanenza", "2 Permanenza"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 10, "3 Permanenza", "3 Permanenza"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 13, "4 Permanenza", "4 Permanenza"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseTT2 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "2-Trattamento Termico", "TT_QUALITA");

							if(collaudo.getDescrizioniFase_C03_TT_QUALITA() != null && collaudo.getDescrizioniFase_C03_TT_QUALITA().containsKey(idSequenzaFase)) {
								codificaCommento(faseTT2.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_QUALITA().get(idSequenzaFase));
							}
							
							ciclo.getFasi().add(faseTT2);

							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 4, "1 Permanenza", "1 Permanenza"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 7, "2 Permanenza", "2 Permanenza"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 10, "3 Permanenza", "3 Permanenza"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 13, "4 Permanenza", "4 Permanenza"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseTT3 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "3-Trattamento Termico", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_QUALITA() != null && collaudo.getDescrizioniFase_C03_TT_QUALITA().containsKey(idSequenzaFase)) {
								codificaCommento(faseTT3.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_QUALITA().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseTT3);

							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 4, "1 Permanenza", "1 Permanenza"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 7, "2 Permanenza", "2 Permanenza"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 10, "3 Permanenza", "3 Permanenza"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 13, "4 Permanenza", "4 Permanenza"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseTT4 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "4-Trattamento Termico", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_QUALITA() != null && collaudo.getDescrizioniFase_C03_TT_QUALITA().containsKey(idSequenzaFase)) {
								codificaCommento(faseTT4.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_QUALITA().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseTT4);

							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 4, "1 Permanenza", "1 Permanenza"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 7, "2 Permanenza", "2 Permanenza"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 10, "3 Permanenza", "3 Permanenza"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 13, "4 Permanenza", "4 Permanenza"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Kg", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_QUALITA() != null && collaudo.getDescrizioniFase_C03_TT_QUALITA().containsKey(idSequenzaFase)) {
								codificaCommento(faseDurezzaKg.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_QUALITA().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseDurezzaKg);

							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 1, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 2, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 3, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 4, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 5, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 6, "Durezza Kg", "Durezza Kg"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseDimensionalePostLM1 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Dimensionale post LM1", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_QUALITA() != null && collaudo.getDescrizioniFase_C03_TT_QUALITA().containsKey(idSequenzaFase)) {
								codificaCommento(faseDimensionalePostLM1.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_QUALITA().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseDimensionalePostLM1);

							faseDimensionalePostLM1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDimensionalePostLM1, (short) 1, "Positivo / Negativo", "Pos. / Neg."));

						}
						break;
					case "C03_TT-SUPPLEMENTARE":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);
							
							if(collaudo.getNote_C03_TT_SUPPLEMENTARE() != null) {
								codificaCommento(ciclo.getCommenti(), "it", "SGQ_CICLI_TES", "SGQ_CICLI_TES", collaudo.getNote_C03_TT_SUPPLEMENTARE());
							}

							CicloCollaudoFase faseTT1 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "1-Trattamento Termico", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE() != null && collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE().containsKey(idSequenzaFase)) {
								codificaCommento(faseTT1.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseTT1);

							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 4, "1 Permanenza", "1 Permanenza"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 7, "2 Permanenza", "2 Permanenza"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 10, "3 Permanenza", "3 Permanenza"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 13, "4 Permanenza", "4 Permanenza"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseTT2 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "2-Trattamento Termico", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE() != null && collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE().containsKey(idSequenzaFase)) {
								codificaCommento(faseTT2.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseTT2);

							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 4, "1 Permanenza", "1 Permanenza"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 7, "2 Permanenza", "2 Permanenza"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 10, "3 Permanenza", "3 Permanenza"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseTT2.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT2, (short) 13, "4 Permanenza", "4 Permanenza"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseTT3 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "3-Trattamento Termico", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE() != null && collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE().containsKey(idSequenzaFase)) {
								codificaCommento(faseTT3.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseTT3);

							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 4, "1 Permanenza", "1 Permanenza"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 7, "2 Permanenza", "2 Permanenza"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 10, "3 Permanenza", "3 Permanenza"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseTT3.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT3, (short) 13, "4 Permanenza", "4 Permanenza"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseTT4 = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "4-Trattamento Termico", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE() != null && collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE().containsKey(idSequenzaFase)) {
								codificaCommento(faseTT4.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseTT4);

							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 3, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 4, "1 Permanenza", "1 Permanenza"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 5, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 6, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 7, "2 Permanenza", "2 Permanenza"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 8, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 9, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 10, "3 Permanenza", "3 Permanenza"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 11, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 12, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseTT4.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT4, (short) 13, "4 Permanenza", "4 Permanenza"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseDurezzaKg = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza Kg", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE() != null && collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE().containsKey(idSequenzaFase)) {
								codificaCommento(faseDurezzaKg.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseDurezzaKg);

							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 1, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 2, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 3, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 4, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 5, "Durezza Kg", "Durezza Kg"));
							faseDurezzaKg.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaKg, (short) 6, "Durezza Kg", "Durezza Kg"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseDurezzaHB = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza HB", "TT_QUALITA");
							
							if(collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE() != null && collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE().containsKey(idSequenzaFase)) {
								codificaCommento(faseDurezzaHB.getCommenti(), "it", "SGQ_CICLI_FMO", "SGQ_CICLI_TES", collaudo.getDescrizioniFase_C03_TT_SUPPLEMENTARE().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseDurezzaHB);

							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 1, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 2, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 3, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 4, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 5, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 6, "Durezza HB", "Durezza HB"));

						}
						break;
					case "C03_UT_PRE-TT":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							CicloCollaudoFase faseControlloUT = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Controllo UT", "Ctrl UT");

							ciclo.getFasi().add(faseControlloUT);

							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 1, "Positivo / Negativo", "Pos. / Neg."));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 2, "1 MDR", "1 MDR"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 3, "1 Spessore", "1 Spessore"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 4, "2 MDR", "2 MDR"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 5, "2 Spessore", "2 Spessore"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 6, "3 MDR", "3 MDR"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 7, "3 Spessore", "3 Spessore"));
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

							faseAnalisiChimica.getCaratteristiche().add(caratteristicaCicloCollaudo(faseAnalisiChimica, (short) 1, "Analisi Chimica", "Analisi Chimica"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseAnalisiProdotto = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Analisi Prodotto", "Analisi Prodott");

							ciclo.getFasi().add(faseAnalisiProdotto);

							faseAnalisiProdotto.getCaratteristiche().add(caratteristicaCicloCollaudo(faseAnalisiProdotto, (short) 1, "Analisi Chimica", "Analisi Chimica"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseAnalisiTrazioneFornitura = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Trazione fornitura", "Trazione fornit");

							ciclo.getFasi().add(faseAnalisiTrazioneFornitura);

							faseAnalisiTrazioneFornitura.getCaratteristiche().add(caratteristicaCicloCollaudo(faseAnalisiTrazioneFornitura, (short) 1, "Trazione fornitura", "Trazione fornit"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseAnalisiResilienzaFornitura = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Resilienza fornitura", "Resilienza forn");

							ciclo.getFasi().add(faseAnalisiResilienzaFornitura);

							faseAnalisiResilienzaFornitura.getCaratteristiche().add(caratteristicaCicloCollaudo(faseAnalisiResilienzaFornitura, (short) 1, "Resilienza fornitura", "Resilienza forn"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase microGrano = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Micro Grano", "Micro Grano");

							ciclo.getFasi().add(microGrano);

							microGrano.getCaratteristiche().add(caratteristicaCicloCollaudo(microGrano, (short) 1, "Micro Grano", "Micro Grano"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase creep = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Creep", "Creep");

							ciclo.getFasi().add(creep);

							creep.getCaratteristiche().add(caratteristicaCicloCollaudo(creep, (short) 1, "Creep", "Creep"));

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

							faseTensioniResidue.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTensioniResidue, (short) 1, "Analisi Chimica", "Analisi Chimica"));
						}
						break;
					case "C06_CONTROLLI_NDT":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							CicloCollaudoFase faseControlloUT = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Ultrasuoni", "Ultrasuoni");

							ciclo.getFasi().add(faseControlloUT);

							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 1, "Positivo / Negativo", "Pos. / Neg."));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 3, "1 MDR", "1 MDR"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 2, "1 Spessore", "1 Spessore"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 5, "2 MDR", "2 MDR"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 4, "2 Spessore", "2 Spessore"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 7, "3 MDR", "3 MDR"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 6, "3 Spessore", "3 Spessore"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseMagnetico = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Magnetico", "Magnetico");

							ciclo.getFasi().add(faseMagnetico);

							faseMagnetico.getCaratteristiche().add(caratteristicaCicloCollaudo(faseMagnetico, (short) 1, "Positivo / Negativo", "Pos./Neg."));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseLiquidiPenetranti = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Liquidi Penetranti", "Liquidi Penetra");

							ciclo.getFasi().add(faseLiquidiPenetranti);

							faseLiquidiPenetranti.getCaratteristiche().add(caratteristicaCicloCollaudo(faseLiquidiPenetranti, (short) 1, "Positivo / Negativo", "Pos./Neg."));
						}
						break;
					case "C06_CONTROLLO_INTERMEDIO":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							CicloCollaudoFase faseDimensionale = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Dimensionale", "Dimensionale");
							
							if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().containsKey(idSequenzaFase)) {
								codificaCommento(faseDimensionale.getCommenti(), "it", "SGQ_CICLI_FAS", "SGQ_CICLI_FAS", collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseDimensionale);

							faseDimensionale.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDimensionale, (short) 1, "Positivo / Negativo", "Pos. / Neg."));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseControlloUT = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Ultrasuoni", "Ultrasuoni");
							
							if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().containsKey(idSequenzaFase)) {
								codificaCommento(faseControlloUT.getCommenti(), "it", "SGQ_CICLI_FAS", "SGQ_CICLI_FAS", collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().get(idSequenzaFase));
							}
							
							if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().containsKey(idSequenzaFase)) {
								codificaCommento(faseControlloUT.getCommenti(), "it", "SGQ_CICLI_FAS", "SGQ_CICLI_FAS", collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseControlloUT);

							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 1, "Positivo / Negativo", "Pos. / Neg."));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 3, "1 MDR", "1 MDR"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 2, "1 Spessore", "1 Spessore"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 5, "2 MDR", "2 MDR"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 4, "2 Spessore", "2 Spessore"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 7, "3 MDR", "3 MDR"));
							faseControlloUT.getCaratteristiche().add(caratteristicaCicloCollaudo(faseControlloUT, (short) 6, "3 Spessore", "3 Spessore"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseDurezzaHB = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza HB", "TT_QUALITA");
							
							if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().containsKey(idSequenzaFase)) {
								codificaCommento(faseDurezzaHB.getCommenti(), "it", "SGQ_CICLI_FAS", "SGQ_CICLI_FAS", collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().get(idSequenzaFase));
							}
							
							if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().containsKey(idSequenzaFase)) {
								codificaCommento(faseDurezzaHB.getCommenti(), "it", "SGQ_CICLI_FAS", "SGQ_CICLI_FAS", collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseDurezzaHB);

							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 1, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 2, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 3, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 4, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 5, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 6, "Durezza HB", "Durezza HB"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseMagnetico = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Magnetico", "Magnetico");
							
							if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().containsKey(idSequenzaFase)) {
								codificaCommento(faseMagnetico.getCommenti(), "it", "SGQ_CICLI_FAS", "SGQ_CICLI_FAS", collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().get(idSequenzaFase));
							}
							
							if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().containsKey(idSequenzaFase)) {
								codificaCommento(faseMagnetico.getCommenti(), "it", "SGQ_CICLI_FAS", "SGQ_CICLI_FAS", collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseMagnetico);

							faseMagnetico.getCaratteristiche().add(caratteristicaCicloCollaudo(faseMagnetico, (short) 1, "Positivo / Negativo", "Pos./Neg."));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseLiquidiPenetranti = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Liquidi Penetranti", "Liquidi Penetra");
							
							if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().containsKey(idSequenzaFase)) {
								codificaCommento(faseLiquidiPenetranti.getCommenti(), "it", "SGQ_CICLI_FAS", "SGQ_CICLI_FAS", collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO().get(idSequenzaFase));
							}
							
							if(collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1() != null && collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().containsKey(idSequenzaFase)) {
								codificaCommento(faseLiquidiPenetranti.getCommenti(), "it", "SGQ_CICLI_FAS", "SGQ_CICLI_FAS", collaudo.getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1().get(idSequenzaFase));
							}

							ciclo.getFasi().add(faseLiquidiPenetranti);

							faseLiquidiPenetranti.getCaratteristiche().add(caratteristicaCicloCollaudo(faseLiquidiPenetranti, (short) 1, "Positivo / Negativo", "Pos./Neg."));
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

							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 1, "Temperatura Iniziale", "Temperatura ini"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 2, "1 Gradiente Termico", "1 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 4, "1 Temperatura Fine Rampa", "1 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 5, "1 Permanenza", "1 Permanenza"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 6, "2 Gradiente Termico", "2 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 8, "2 Temperatura Fine Rampa", "2 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 9, "2 Permanenza", "2 Permanenza"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 10, "3 Gradiente Termico", "3 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 12, "3 Temperatura Fine Rampa", "3 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 13, "3 Permanenza", "3 Permanenza"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 14, "4 Gradiente Termico", "4 Gradiente Ter"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 16, "4 Temperatura Fine Rampa", "4 Temperatura F"));
							faseTT1.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTT1, (short) 17, "4 Permanenza", "4 Permanenza"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseTaempF = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Temperatura di Forgia", "Forgia");

							ciclo.getFasi().add(faseTaempF);

							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 1, "1 Fase  1 °C Minima", "Fase  1 °C Mini"));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 2, "1 Fase  1 °C Massima", "Fase  1 °C Mass"));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 3, "1 Fase  1 °C Minima", "Fase  1 °C Mini"));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 4, "1 Fase  1 °C Massima", "Fase  1 °C Mass"));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 5, "1 Fase  1 °C Minima", "Fase  1 °C Mini"));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 6, "1 Fase  1 °C Massima", "Fase  1 °C Mass"));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 7, "1 Fase  1 °C Minima", "Fase  1 °C Mini"));
							faseTaempF.getCaratteristiche().add(caratteristicaCicloCollaudo(faseTaempF, (short) 8, "1 Fase  1 °C Massima", "Fase  1 °C Mass"));

						}
						break;
					case "C06_CONTROLLO_DUREZZE-DIMENSION":
						attivita = ricercaAttivita(tipoCiclo, collaudo.getIdAttivita());
						if(attivita != null) {
							Integer idSequenzaFase = attivita.getIdSequenzaFase();
							Integer idSequenzaVisualizz = attivita.getSeqVisualizz();

							ciclo = testataCicloCollaudo(collaudo);

							CicloCollaudoFase faseDimensionale = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Dimensionale", "Dimensionale");

							ciclo.getFasi().add(faseDimensionale);

							faseDimensionale.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDimensionale, (short) 1, "Dimensione", "Dimensione"));

							idSequenzaFase = idSequenzaFase+1;
							idSequenzaVisualizz = idSequenzaVisualizz+1;

							CicloCollaudoFase faseDurezzaHB = faseCicloCollaudo(ciclo, idSequenzaFase.shortValue(),idSequenzaVisualizz.shortValue(), "Durezza HB", "TT_QUALITA");

							ciclo.getFasi().add(faseDurezzaHB);

							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 1, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 2, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 3, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 4, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 5, "Durezza HB", "Durezza HB"));
							faseDurezzaHB.getCaratteristiche().add(caratteristicaCicloCollaudo(faseDurezzaHB, (short) 6, "Durezza HB", "Durezza HB"));
						}
						break;
					default:
						break;
					}
				}
			}

		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void codificaCommento(CommentHandler commentHandler,String language,String commentUse,String description,String text) {
		Comment commentoOrdTesta = (Comment)Factory.createObject(Comment.class);
		commentoOrdTesta.setDescription(description);
		commentoOrdTesta.setText(text);
		String[] commentoArr = {text,""};
		commentoOrdTesta.getTextNLSHandler().setTextsForLanguage(commentoArr, "it");
		try {
			commentoOrdTesta.save();
		}
		catch(Exception eSComm) {
			eSComm.printStackTrace(Trace.excStream);
		}
		CommentHandlerLink ordTestaCHL = (CommentHandlerLink)Factory.createObject(CommentHandlerLink.class);
		ordTestaCHL.setChoiceCommentUseKeys((Vector) Arrays.asList(commentUse));
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

	public CicloCollaudoTestata testataCicloCollaudo(OrdEsecAtvSogCollaudo collaudo) {
		CicloCollaudoTestata testata = (CicloCollaudoTestata) Factory.createObject(CicloCollaudoTestata.class);
		testata.setDominio(CicloCollaudoTestata.ATT_LAV_ACCETTAZIONE);
		testata.setIdArticolo(collaudo.getArticolo());
		testata.setIdCommessa(collaudo.getCommessa());
		testata.setIdAttivitaLavorativa(collaudo.getAttivita());
		testata.getDescrizione().setDescrizione(collaudo.getDescrizione());
		testata.getDescrizione().setDescrizioneRidotta(collaudo.getDescrRidotta());
		testata.setValoreNominaleProposto(collaudo.getValNomProposto().equals("Y") ? true : false);
		testata.setIdLqa("STD");
		testata.setDataInizioValidita(TimeUtils.getCurrentDate());
		return testata;
	}

	public CicloCollaudoFase faseCicloCollaudo(CicloCollaudoTestata testata,Short seqFase,Short seqVisualizz, String descrizione,String descrRidotta) {
		CicloCollaudoFase fase = (CicloCollaudoFase) Factory.createObject(CicloCollaudoFase.class);
		fase.setCicloCollaudoTestata(testata);
		fase.setSequenzaFase(seqFase);
		fase.setSequenzaVisualizzazione(seqVisualizz);
		fase.getDescrizione().setDescrizione(descrizione);
		fase.getDescrizione().setDescrizioneRidotta(descrizione);
		fase.setPercentualeDaControlla(new BigDecimal("100"));
		return fase;
	}

	public CicloCollaudoCaratteristica caratteristicaCicloCollaudo(CicloCollaudoFase fase,Short seqCarat, String descrizione, String descrRidotta) {
		CicloCollaudoCaratteristica carat = (CicloCollaudoCaratteristica) Factory.createObject(CicloCollaudoCaratteristica.class);
		carat.setCicloCollaudoFase(fase);
		carat.setSequenzaCaratteristica(seqCarat);
		carat.getDescrizione().setDescrizione(descrizione);
		carat.getDescrizione().setDescrizioneRidotta(descrizione);
		carat.setPercDaControllare(new BigDecimal("100"));
		carat.setLimInfTolleranza(new BigDecimal("-9999"));
		carat.setLimSupTolleranza(new BigDecimal("9999"));
		carat.setRilavLimInfTol(true);
		carat.setRilavLimSupTol(true);
		carat.setNumMaxDifettiAccettazione(0);
		carat.setNumMinDifettiRifiuto(1);
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
				List<OrdEsecAtvSogCollaudo> collaudi = OrdEsecAtvSogCollaudo.listaCicliControlloDaImportare();
				for(OrdEsecAtvSogCollaudo collaudo : collaudi) {
					String nomeTabella = collaudo.getNomeTabella();
					switch (nomeTabella) {
					case "C03_TT-QUALITA":
						collaudo.recuperaNote_C03_TT_QUALITA_Target();
						collaudo.recuperaNote_C03_TT_QUALITA_FAS_Target();
						break;
					case "C03_TT-PRELIMINARE":
						collaudo.recuperaNote_C03_TT_PRELIMINARE();
						collaudo.recuperaNote_C03_TT_PRELIMINARE_Map();
						break;
					case "C03_TT-SUPPLEMENTARE":
						collaudo.recuperaNote_C03_TT_SUPPLEMENTARE_Target();
						break;
					case "C06_CONTROLLO_INTERMEDIO":
						collaudo.recuperaNote_C06_CONTORLLO_INTERMEDIO_Map();
						break;
					default:
						break;
					}
				}
			}
		} catch (SQLException ex) {
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
		return null;
	}

	@Override
	protected String getClassAdCollectionName() {
		return "YDtsxImportCicliCollaudo";
	}

}
