package it.monchieri.thip.target.qualita;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.cbs.Comment;
import com.thera.thermfw.cbs.CommentHandler;
import com.thera.thermfw.cbs.CommentHandlerLink;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;

import it.monchieri.thip.target.NoteAttivitaSogCollaudo;
import it.monchieri.thip.target.OrdEsecAtvSogCollaudo;
import it.monchieri.thip.target.OrdEsecAtvSogCollaudoDatiCaratteristica;
import it.monchieri.thip.target.QcTabellaAttivita;
import it.thera.thip.cs.DatiComuniEstesi;
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

public abstract class CicloCollaudoImportatore {
	
	public static final String CICLO_FUCINATURA = "C02_FUCINATURA";
	public static final String CICLO_CONTROLLO_GREZZO_POST_FORGIA = "C03_CONTROLLO_GREZZO_POST_FORGI";
	public static final String CICLO_CONTROLLO_HB_SU_FACCIA_T = "C03_CONTROLLO_HB_SU_FACCIA_DI_T";
	public static final String CICLO_CONTROLLO_QUALITATIVO_POST = "C03_CONTROLLO_QUALITATIVO_POST";
	public static final String CICLO_RICOTTURA = "C03_TT_DI_RICOTTURA";
	public static final String CICLO_PRELIMINARE = "C03_TT-PRELIMINARE";
	public static final String CICLO_QUALITA = "C03_TT-QUALITA";
	public static final String CICLO_SUPPLEMENTARE = "C03_TT-SUPPLEMENTARE";
	public static final String CICLO_UT_PRE = "C03_UT_PRE-TT";
	public static final String CICLO_CONTROLLO_LABORATORIO = "C05_CONTROLLO_LABORATORIO";
	public static final String CICLO_CONTROLLO_LABORATORIO_TR = "C05_CONTROLLO_LABORATORIO_TR";
	public static final String CICLO_CONTROLLO_NDT = "C06_CONTROLLI_NDT";
	public static final String CICLO_CONTROLLO_INTERMEDIO = "C06_CONTROLLO_INTERMEDIO";
	public static final String CICLO_SBLUMATURA = "C02_SBLUMATURA";
	public static final String CICLO_CONTROLLO_DUREZZE_DIMENSIONI = "C06_CONTROLLO_DUREZZE-DIMENSION";
	
	public abstract CicloCollaudoTestata codificaCicloCollaudoPanthera(OrdEsecAtvSogCollaudo collaudo, QcTabellaAttivita attivita) throws SQLException;

	protected CicloCollaudoTestata testataCicloCollaudo(OrdEsecAtvSogCollaudo collaudo) throws SQLException {
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

	protected CicloCollaudoFase faseCicloCollaudo(CicloCollaudoTestata testata,Short seqFase,Short seqVisualizz, String descrizione,String descrRidotta) {
		CicloCollaudoFase fase = (CicloCollaudoFase) Factory.createObject(CicloCollaudoFase.class);
		fase.setCicloCollaudoTestata(testata);
		fase.setSequenzaFase(seqFase);
		fase.setSequenzaVisualizzazione(seqVisualizz);
		fase.getDescrizione().setDescrizione(descrizione);
		fase.getDescrizione().setDescrizioneRidotta(descrRidotta);
		return fase;
	}

	protected CicloCollaudoCaratteristica caratteristicaCicloCollaudo(CicloCollaudoFase fase,Short seqCarat, String descrizione, String descrRidotta,
			OrdEsecAtvSogCollaudo atvTarget) {
		CicloCollaudoCaratteristica carat = (CicloCollaudoCaratteristica) Factory.createObject(CicloCollaudoCaratteristica.class);
		carat.setCicloCollaudoFase(fase);
		carat.setSequenzaCaratteristica(seqCarat);
		carat.getDescrizioneCicloNLS().setDescrizione(descrizione);
		carat.getDescrizioneCicloNLS().setDescrizioneRidotta(descrRidotta);
		carat.setRilavLimInfTol(false);
		carat.setRilavLimSupTol(false);
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
		carat.setQtaDaControllare(BigDecimal.ONE);
		return carat;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void codificaCommento(CommentHandler commentHandler,NoteAttivitaSogCollaudo commento) {
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
}
