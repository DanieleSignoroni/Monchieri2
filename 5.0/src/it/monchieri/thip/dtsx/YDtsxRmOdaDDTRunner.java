package it.monchieri.thip.dtsx;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.batch.BatchRunnable;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionDescriptor;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Database;
import com.thera.thermfw.persist.ErrorCodes;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.security.Authorizable;

import it.mame.thip.qualita.controllo.YCicloCollaudoCaratt;
import it.mame.thip.qualita.controllo.YCicloCollaudoFase;
import it.mame.thip.qualita.controllo.YMisuraCaracteriche;
import it.mame.thip.qualita.controllo.YNormeQualita;
import it.mame.thip.qualita.controllo.YNormeQualitaTM;
import it.monchieri.thip.acquisti.ordineAC.YOrdineAcquisto;
import it.monchieri.thip.acquisti.ordineAC.YOrdineAcquistoRigaPrm;
import it.monchieri.thip.acquisti.ordineAC.YOrdineAcquistoRigaPrmTM;
import it.monchieri.thip.target.YPTDdtFor;
import it.monchieri.thip.target.YPTDdtForTM;
import it.monchieri.thip.target.YPTOrdFor;
import it.monchieri.thip.target.YPTOrdForTM;
import it.monchieri.thip.target.YPTQcAnalisiAcciaieria;
import it.monchieri.thip.target.YPTQcAnalisiAcciaieriaTM;
import it.monchieri.thip.target.YPTQcAnalisiRm;
import it.monchieri.thip.target.YPTQcAnalisiRmTM;
import it.thera.thip.acquisti.ordineAC.OrdineAcquistoRiga;
import it.thera.thip.acquisti.ordineAC.OrdineAcquistoRigaLottoPrm;
import it.thera.thip.acquisti.ordineAC.OrdineAcquistoRigaPrm;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.dipendente.Dipendente;
import it.thera.thip.magazzino.generalemag.Lotto;
import it.thera.thip.qualita.controllo.CicloCollaudoCaratteristica;
import it.thera.thip.qualita.controllo.CicloCollaudoTestata;
import it.thera.thip.qualita.controllo.DocumentiCollaudoRilevazioneMisure;
import it.thera.thip.qualita.controllo.DocumentoCollaudoRiga;
import it.thera.thip.qualita.controllo.DocumentoCollaudoTestata;
import it.thera.thip.qualita.controllo.DocumentoCollaudoTestataTM;
import it.thera.thip.qualita.controllo.MisuraFase;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 20/11/2024
 * <br><br>
 * <b>71XXX	DSSOF3	20/11/2024</b>
 * <p>Prima stesura.<br>
 *  
 * </p>
 */

public class YDtsxRmOdaDDTRunner extends BatchRunnable implements Authorizable {

	public static final String NOME_DB_EXT = "PantheraTarget_test";
	public static final String UTENTE_DB_EXT = "Panthera";
	public static final String PWD_DB_EXT = "panthera";
	public static final String SRV_DB_EXT = "SRVDB.fmonchieri.locale";
	public static final String PORTA_DB_EXT = "1433";

	protected static final String SQL_EXIST_DC = "SELECT * FROM " + DocumentoCollaudoTestataTM.TABLE_NAME +
			" WHERE " + DocumentoCollaudoTestataTM.ID_TIPO_DOCPRV + " LIKE ?" +
			" AND " + DocumentoCollaudoTestataTM.ID_AZIENDA + "= ?" +
			" AND " + DocumentoCollaudoTestataTM.STATO_COLL + " IN ("
			+ "'" + DocumentoCollaudoTestata.COLLAUDO_APPROVATO + "',"
			+ "'" + DocumentoCollaudoTestata.COLLAUDO_CHIUSO + "',"
			+ "'" + DocumentoCollaudoTestata.COLLAUDO_DICHIARATO + "')";

	protected static final String SQL_EXIST_DC_ORD_ACQ = SQL_EXIST_DC +
			" AND " + DocumentoCollaudoTestataTM.ANNO_ORD_ACQ + "= ?" +
			" AND " + DocumentoCollaudoTestataTM.NUMERO_ORD_ACQ + "= ?" +
			" AND " + DocumentoCollaudoTestataTM.RIGA_ORD_ACQ + "= ?" +
			" AND " + DocumentoCollaudoTestataTM.DET_RIGA_ORD_ACQ + "= ?";
	protected static CachedStatement existDCOrdAcqCachStmt = new CachedStatement(SQL_EXIST_DC_ORD_ACQ);

	public static final String SQL_EXTRACT_DOC_COLL_COLL_NORMA = "SELECT "
			+ "	* "
			+ "FROM "
			+ "	"+DocumentoCollaudoTestataTM.TABLE_NAME+" DCT "
			+ "INNER JOIN "+YOrdineAcquistoRigaPrmTM.TABLE_NAME_EXT+" AS ORD_ACQ_RIG_Y ON "
			+ "	ORD_ACQ_RIG_Y."+YOrdineAcquistoRigaPrmTM.ID_AZIENDA+" = DCT."+DocumentoCollaudoTestataTM.ID_AZIENDA+" "
			+ "	AND ORD_ACQ_RIG_Y."+YOrdineAcquistoRigaPrmTM.ID_ANNO_ORD+" = DCT."+DocumentoCollaudoTestataTM.ANNO_ORD_ACQ+" "
			+ "	AND ORD_ACQ_RIG_Y."+YOrdineAcquistoRigaPrmTM.ID_NUMERO_ORD+" = DCT."+DocumentoCollaudoTestataTM.NUMERO_ORD_ACQ+" "
			+ "	AND ORD_ACQ_RIG_Y."+YOrdineAcquistoRigaPrmTM.ID_RIGA_ORD+" = DCT."+DocumentoCollaudoTestataTM.RIGA_ORD_ACQ+" "
			+ "	AND ORD_ACQ_RIG_Y."+YOrdineAcquistoRigaPrmTM.ID_DET_RIGA_ORD+" = DCT."+DocumentoCollaudoTestataTM.DET_RIGA_ORD_ACQ+" "
			+ "INNER JOIN "+YNormeQualitaTM.TABLE_NAME+" AS YNORME_QLT ON "
			+ "	YNORME_QLT."+YNormeQualitaTM.ID_AZIENDA+" = ORD_ACQ_RIG_Y."+YOrdineAcquistoRigaPrmTM.ID_AZIENDA+" "
			+ "	AND YNORME_QLT."+YNormeQualitaTM.ID_NORMA+" = ORD_ACQ_RIG_Y."+YOrdineAcquistoRigaPrmTM.ID_NORMA_QLT+"  "
			+ "WHERE DCT."+DocumentoCollaudoTestataTM.ID_AZIENDA+" = ? AND DCT."+DocumentoCollaudoTestataTM.TIMESTAMP_AGG+" > ? ";
	public static CachedStatement cEstrazioneDocCollNorme = new CachedStatement(SQL_EXTRACT_DOC_COLL_COLL_NORMA);

	public static final String SQL_EXTRACT_ORD_FOR = "SELECT R.* FROM THIP.ORD_ACQ_TES T  "
			+ "INNER JOIN THIP.ORD_ACQ_RIG R "
			+ "ON T.ID_AZIENDA = R.ID_AZIENDA  "
			+ "AND T.ID_ANNO_ORDINE = R.ID_ANNO_ORD  "
			+ "AND T.ID_NUMERO_ORD = R.ID_NUMERO_ORD  "
			+ "WHERE T.R_CAU_ORD_ACQ = 'MP' "
			+ "AND T.STATO = 'V' "
			+ "AND T.ID_AZIENDA = ? AND R.TIMESTAMP_AGG > ?";

	public static final String SQL_EXTRACT_DDT_FOR = "SELECT "
			+ "    '000' + SUBSTRING(DOC_ACQ_TES.ID_NUMERO_DOC, 6, 5) AS NUM_DDT_INT, "
			+ "    DOC_ACQ_TES.ID_ANNO_DOC AS ANNO_DOC, "
			+ "    BBFORPT.FORCFEST AS COD_CF, "
			+ "    DOC_ACQ_TES.DATA_DOC AS DATA_DOC, "
			+ "    DOC_ACQ_TES.R_CAU_DOC_ACQ AS CODICE_CAUSALE, "
			+ "    CASE WHEN DOC_ACQ_RIG.R_MAGAZZINO IN ('MP', 'CDE') THEN '1.01' ELSE 'N/A' END AS COD_DEP, "
			+ "    CASE  "
			+ "      WHEN DOC_ACQ_RIG.R_MAG_LAV_ESN IS NOT NULL THEN DOC_ACQ_RIG.R_MAG_LAV_ESN "
			+ "      WHEN DOC_ACQ_TES.R_MAGAZZINO_TRA IS NOT NULL THEN DOC_ACQ_TES.R_MAGAZZINO_TRA "
			+ "      ELSE DOC_ACQ_TES.R_MAG_PRELIEVO "
			+ "    END AS COD_DEP_2, "
			+ "    ARTICOLI.R_CLASSE_FISCALE AS COD_ART, "
			+ "    DOC_ACQ_LOT.ID_ARTICOLO AS DES_RIGA, "
			+ "    DOC_ACQ_RIG.ID_RIGA_DOC AS NUM_RIGA, "
			+ "    DOC_ACQ_RIG.R_UM_ACQ AS UM_ACQ, "
			+ "    DOC_ACQ_RIG.QTA_RCV_UM_PRM / 1000 AS QTA_CONSEGNATA, "
			+ "    DOC_ACQ_RIG.PREZZO, "
			+ "    DOC_ACQ_LOT.ID_LOTTO AS LOTTO, "
			+ "    LEFT(DOC_ACQ_TES.NUM_RIF_FOR, 15) AS NUM_DDT_FORNITORE, "
			+ "    DOC_ACQ_TES.DATA_RIF_FOR AS DATA_DOC_FORNITORE, "
			+ "    CONFIGURAZIONI.DESCRIZIONE AS LINGOTTO, "
			+ "    DOC_ACQ_RIG.R_ANNO_ORD AS RIFERIMENTO_ANNO_ORD_FOR, "
			+ "    '000' + SUBSTRING(DOC_ACQ_RIG.R_NUMERO_ORD, 5, 5) AS RIFERIMENTO_NUM_ORD_FOR, "
			+ "    DOC_ACQ_RIG.R_RIGA_ORD AS RIFERIMENTO_RIGA_ORD_FOR, "
			+ "    MAX(v.MaxTime) AS TIMESTAMP_AGG "
			+ "FROM THIP.DOC_ACQ_TES DOC_ACQ_TES "
			+ "INNER JOIN THIP.DOC_ACQ_RIG DOC_ACQ_RIG ON "
			+ "    DOC_ACQ_TES.ID_AZIENDA = DOC_ACQ_RIG.ID_AZIENDA "
			+ "    AND DOC_ACQ_TES.ID_ANNO_DOC = DOC_ACQ_RIG.ID_ANNO_DOC "
			+ "    AND DOC_ACQ_TES.ID_NUMERO_DOC = DOC_ACQ_RIG.ID_NUMERO_DOC "
			+ "INNER JOIN THIP.FORNITORI_ACQ FORNITORI_ACQ ON "
			+ "    DOC_ACQ_TES.ID_AZIENDA = FORNITORI_ACQ.ID_AZIENDA "
			+ "    AND DOC_ACQ_TES.R_FORNITORE = FORNITORI_ACQ.ID_FORNITORE "
			+ "INNER JOIN FINANCE.BBFORPT BBFORPT ON "
			+ "    BBFORPT.T01CD = FORNITORI_ACQ.ID_AZIENDA "
			+ "    AND BBFORPT.FORCD = FORNITORI_ACQ.ID_FORNITORE "
			+ "INNER JOIN THIP.DOC_ACQ_LOT DOC_ACQ_LOT ON "
			+ "    DOC_ACQ_RIG.ID_AZIENDA = DOC_ACQ_LOT.ID_AZIENDA "
			+ "    AND DOC_ACQ_RIG.ID_ANNO_DOC = DOC_ACQ_LOT.ID_ANNO_DOC "
			+ "    AND DOC_ACQ_RIG.ID_NUMERO_DOC = DOC_ACQ_LOT.ID_NUMERO_DOC "
			+ "    AND DOC_ACQ_RIG.ID_RIGA_DOC = DOC_ACQ_LOT.ID_RIGA_DOC "
			+ "INNER JOIN THIP.ARTICOLI ARTICOLI ON "
			+ "    ARTICOLI.ID_AZIENDA = DOC_ACQ_LOT.ID_AZIENDA "
			+ "    AND ARTICOLI.ID_ARTICOLO = DOC_ACQ_LOT.ID_ARTICOLO "
			+ "INNER JOIN THIP.CONFIGURAZIONI CONFIGURAZIONI ON "
			+ "    CONFIGURAZIONI.ID_AZIENDA = DOC_ACQ_RIG.ID_AZIENDA "
			+ "    AND CONFIGURAZIONI.ID_CONFIG = DOC_ACQ_RIG.R_CONFIGURAZIONE "
			+ "CROSS APPLY (VALUES  "
			+ "    (DOC_ACQ_TES.TIMESTAMP_AGG), "
			+ "    (DOC_ACQ_RIG.TIMESTAMP_AGG), "
			+ "    (DOC_ACQ_LOT.TIMESTAMP_AGG), "
			+ "    (CONFIGURAZIONI.TIMESTAMP_AGG) "
			+ ") AS v(MaxTime) "
			+ "WHERE "
			+ "    DOC_ACQ_TES.ID_ANNO_DOC >= YEAR(CURRENT_TIMESTAMP) - 1 "
			+ "    AND DOC_ACQ_TES.R_CAU_DOC_ACQ IN ('MP', 'MPE') "
			+ "    AND DOC_ACQ_RIG.ID_DET_RIGA_DOC = 0 "
			+ "    AND DOC_ACQ_LOT.ID_DET_RIGA_DOC = 0 "
			+ "    AND DOC_ACQ_TES.ID_AZIENDA = ? AND DOC_ACQ_LOT.TIMESTAMP_AGG > ? "
			+ "GROUP BY "
			+ "    '000' + SUBSTRING(DOC_ACQ_TES.ID_NUMERO_DOC, 6, 5), "
			+ "    DOC_ACQ_TES.ID_ANNO_DOC, "
			+ "    BBFORPT.FORCFEST, "
			+ "    DOC_ACQ_TES.DATA_DOC, "
			+ "    DOC_ACQ_TES.R_CAU_DOC_ACQ, "
			+ "    CASE WHEN DOC_ACQ_RIG.R_MAGAZZINO IN ('MP', 'CDE') THEN '1.01' ELSE 'N/A' END, "
			+ "    CASE  "
			+ "      WHEN DOC_ACQ_RIG.R_MAG_LAV_ESN IS NOT NULL THEN DOC_ACQ_RIG.R_MAG_LAV_ESN "
			+ "      WHEN DOC_ACQ_TES.R_MAGAZZINO_TRA IS NOT NULL THEN DOC_ACQ_TES.R_MAGAZZINO_TRA "
			+ "      ELSE DOC_ACQ_TES.R_MAG_PRELIEVO "
			+ "    END, "
			+ "    ARTICOLI.R_CLASSE_FISCALE, "
			+ "    DOC_ACQ_LOT.ID_ARTICOLO, "
			+ "    DOC_ACQ_RIG.ID_RIGA_DOC, "
			+ "    DOC_ACQ_RIG.R_UM_ACQ, "
			+ "    DOC_ACQ_RIG.QTA_RCV_UM_PRM / 1000, "
			+ "    DOC_ACQ_RIG.PREZZO, "
			+ "    DOC_ACQ_LOT.ID_LOTTO, "
			+ "    LEFT(DOC_ACQ_TES.NUM_RIF_FOR, 15), "
			+ "    DOC_ACQ_TES.DATA_RIF_FOR, "
			+ "    CONFIGURAZIONI.DESCRIZIONE, "
			+ "    DOC_ACQ_RIG.R_ANNO_ORD, "
			+ "    '000' + SUBSTRING(DOC_ACQ_RIG.R_NUMERO_ORD, 5, 5), "
			+ "    DOC_ACQ_RIG.R_RIGA_ORD ";

	protected Date iDataEstrazioneAnalisiAcc;

	protected Date iDataEstrazioneAnalisiRm;

	protected Date iDataEstrazioneOrdFor;

	protected Date iDataEstrazioneDdtFor;

	public Date getDataEstrazioneAnalisiAcc() {
		return iDataEstrazioneAnalisiAcc;
	}

	public void setDataEstrazioneAnalisiAcc(Date iDataEstrazioneAnalisiAcc) {
		this.iDataEstrazioneAnalisiAcc = iDataEstrazioneAnalisiAcc;
	}

	public Date getDataEstrazioneAnalisiRm() {
		return iDataEstrazioneAnalisiRm;
	}

	public void setDataEstrazioneAnalisiRm(Date iDataEstrazioneAnalisiRm) {
		this.iDataEstrazioneAnalisiRm = iDataEstrazioneAnalisiRm;
	}

	public Date getDataEstrazioneOrdFor() {
		return iDataEstrazioneOrdFor;
	}

	public void setDataEstrazioneOrdFor(Date iDataEstrazioneOrdFor) {
		this.iDataEstrazioneOrdFor = iDataEstrazioneOrdFor;
	}

	public Date getDataEstrazioneDdtFor() {
		return iDataEstrazioneDdtFor;
	}

	public void setDataEstrazioneDdtFor(Date iDataEstrazioneDdtFor) {
		this.iDataEstrazioneDdtFor = iDataEstrazioneDdtFor;
	}

	@Override
	protected boolean run() {
		if(getBatchJob().getFromScheduledJob()) {
			setDataEstrazioneAnalisiAcc(null);
			setDataEstrazioneAnalisiRm(null);
			setDataEstrazioneDdtFor(null);
			setDataEstrazioneOrdFor(null);
		}
		try {
			int rc = esportazioneAnalisiChimicheTarget();
			rc += esportazioneOrdFor();
			rc += esportazioneOrdDdt();
			rc += esportazioneAnalisiAcciaieriaTarget();
			if(rc < ErrorCodes.OK) {
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected int esportazioneOrdDdt() throws Exception {
		output.println();
		output.println(" ----------------- Estrazione "+YPTDdtForTM.TABLE_NAME+" -------------------- ");
		Timestamp timestamp = null;
		YTimestampElaborazDtsx elabAnalisiQcRm = (YTimestampElaborazDtsx) Factory.createObject(YTimestampElaborazDtsx.class);
		elabAnalisiQcRm.setKey(KeyHelper.buildObjectKey(new String[] {Azienda.getAziendaCorrente(),"YPT_DDT_FOR"}));
		elabAnalisiQcRm.retrieve();
		if(getDataEstrazioneAnalisiAcc() != null)
			timestamp = TimeUtils.getTimestamp(getDataEstrazioneDdtFor());
		else 
			timestamp = elabAnalisiQcRm.getDatiComuniEstesi().getTimestampAgg();
		output.println("  Estraggo dati DDT_FOR con TIMESTAMP_AGG > "+timestamp.toString());
		List ordFors = estrazioneDdtFor(timestamp);
		output.println("  Estratte "+ordFors.size()+" righe ");
		output.println("  Esportazione "+YPTDdtForTM.TABLE_NAME+" verso Target ");
		int rc = esportaOggettiVersoTarget(ordFors);
		if(rc >= ErrorCodes.OK) {
			elabAnalisiQcRm.setDirty(true);
			if(elabAnalisiQcRm.save() > 0) {
				ConnectionManager.commit();
			}

			//qui eseguo sql 
			ConnectionDescriptor cnd = YDtsxPtExpOrdEsecRunner.externalConnectionDescriptor(
					NOME_DB_EXT,
					UTENTE_DB_EXT,
					PWD_DB_EXT, 
					SRV_DB_EXT, 
					PORTA_DB_EXT);
			try {
				if(cnd != null) {
					ConnectionManager.pushConnection(cnd);
					riattiva_DDT_FOR(cnd);
					riattiva_DDT_FOR_1(cnd);
					YPT_DDT_FOR_set_Elaborato_To_X(cnd);
				}
			} catch (Throwable ex) {
				ex.printStackTrace(Trace.excStream);
			} finally {
				if (cnd != null) {
					cnd.closeConnection();
					ConnectionManager.popConnection(cnd);
				}
			}

		}
		output.println("  Esportazione verso "+YPTDdtForTM.TABLE_NAME+" avvenuta con "+(rc > 0 ? "successo" : "errori"));
		output.println(" ----------------- Fine estrazione "+YPTDdtForTM.TABLE_NAME+" -------------------- ");
		output.println();
		return rc;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected int esportazioneOrdFor() throws Exception {
		output.println();
		output.println(" ----------------- Estrazione "+YPTOrdForTM.TABLE_NAME+" -------------------- ");
		Timestamp timestamp = null;
		YTimestampElaborazDtsx elabAnalisiQcRm = (YTimestampElaborazDtsx) Factory.createObject(YTimestampElaborazDtsx.class);
		elabAnalisiQcRm.setKey(KeyHelper.buildObjectKey(new String[] {Azienda.getAziendaCorrente(),"YPT_ORD_FOR"}));
		elabAnalisiQcRm.retrieve();
		if(getDataEstrazioneOrdFor() != null)
			timestamp = TimeUtils.getTimestamp(getDataEstrazioneAnalisiAcc());
		else 
			timestamp = elabAnalisiQcRm.getDatiComuniEstesi().getTimestampAgg();
		output.println("  Estraggo dati ORD_FOR con TIMESTAMP_AGG > "+timestamp.toString());
		List ordFors = estrazioneOrdFor(timestamp);
		output.println("  Estratte "+ordFors.size()+" righe ");
		output.println("  Esportazione "+YPTOrdForTM.TABLE_NAME+" verso Target ");
		int rc = esportaOggettiVersoTarget(ordFors);
		if(rc >= ErrorCodes.OK) {
			elabAnalisiQcRm.setDirty(true);
			if(elabAnalisiQcRm.save() > 0) {
				ConnectionManager.commit();
			}

			ConnectionDescriptor cnd = YDtsxPtExpOrdEsecRunner.externalConnectionDescriptor(
					NOME_DB_EXT,
					UTENTE_DB_EXT,
					PWD_DB_EXT, 
					SRV_DB_EXT, 
					PORTA_DB_EXT);
			try {
				if(cnd != null) {
					ConnectionManager.pushConnection(cnd);
					riattiva_ORD_FOR(cnd);
					riattiva_ORD_FOR_1(cnd);
					YPT_ORD_FOR_set_Elaborato_To_X(cnd);
				}
			} catch (Throwable ex) {
				ex.printStackTrace(Trace.excStream);
			} finally {
				if (cnd != null) {
					cnd.closeConnection();
					ConnectionManager.popConnection(cnd);
				}
			}

		}
		output.println("  Esportazione verso "+YPTOrdForTM.TABLE_NAME+" avvenuta con "+(rc > 0 ? "successo" : "errori"));
		output.println(" ----------------- Fine estrazione "+YPTOrdForTM.TABLE_NAME+" -------------------- ");
		output.println();
		return rc;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected int esportazioneAnalisiAcciaieriaTarget() throws Exception {
		output.println();
		output.println(" ----------------- Estrazione "+YPTQcAnalisiAcciaieriaTM.TABLE_NAME+" -------------------- ");
		Timestamp timestamp = null;
		YTimestampElaborazDtsx elabAnalisiQcRm = (YTimestampElaborazDtsx) Factory.createObject(YTimestampElaborazDtsx.class);
		elabAnalisiQcRm.setKey(KeyHelper.buildObjectKey(new String[] {Azienda.getAziendaCorrente(),"QC_ANALISI_ACCIAIERIA"}));
		elabAnalisiQcRm.retrieve();
		if(getDataEstrazioneAnalisiAcc() != null)
			timestamp = TimeUtils.getTimestamp(getDataEstrazioneAnalisiAcc());
		else 
			timestamp = elabAnalisiQcRm.getDatiComuniEstesi().getTimestampAgg();

		output.println("  Estraggo dati con TIMESTAMP_AGG > "+timestamp.toString());
		List stmtInsertNorme = estrazioneAnalisiAcciaieria(timestamp);
		output.println("  Estratte "+stmtInsertNorme.size()+" righe ");
		output.println("  Esportazione "+YPTQcAnalisiAcciaieriaTM.TABLE_NAME+" verso Target ");
		int rc = esportaOggettiVersoTarget(stmtInsertNorme);
		if(rc == ErrorCodes.OK) {
			elabAnalisiQcRm.setDirty(true);
			if(elabAnalisiQcRm.save() > 0) {
				ConnectionManager.commit();
			}

			ConnectionDescriptor cnd = YDtsxPtExpOrdEsecRunner.externalConnectionDescriptor(
					NOME_DB_EXT,
					UTENTE_DB_EXT,
					PWD_DB_EXT, 
					SRV_DB_EXT, 
					PORTA_DB_EXT);
			try {
				if(cnd != null) {
					ConnectionManager.pushConnection(cnd);
					riattiva_QC_ANALISI_ACCIAIERIA(cnd);
					riattiva_QC_ANALISI_ACCIAIERIA_1(cnd);
					YPT_QC_ANALISI_ACCIAIERIA_set_Elaborato_To_X(cnd);
					riattiva_DDT_FOR_1_1(cnd);
					YPT_DDT_FOR_set_Elaborato_To_X(cnd);
				}
			} catch (Throwable ex) {
				ex.printStackTrace(Trace.excStream);
			} finally {
				if (cnd != null) {
					cnd.closeConnection();
					ConnectionManager.popConnection(cnd);
				}
			}

		}
		output.println("  Esportazione verso "+YPTQcAnalisiAcciaieriaTM.TABLE_NAME+" avvenuta con "+(rc > 0 ? "successo" : "errori"));
		output.println(" ----------------- Fine estrazione "+YPTQcAnalisiAcciaieriaTM.TABLE_NAME+" -------------------- ");
		output.println();
		return rc;
	}

	/**
	 * Esporto le 'Analisi Chimiche' verso il database di Target.<br>
	 * @author Daniele Signoroni 27/11/2024
	 * <p>
	 * Prima stesura.<br>
	 *
	 * </p>
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected int esportazioneAnalisiChimicheTarget() throws Exception {
		output.println();
		output.println(" ----------------- Estrazione "+YPTQcAnalisiRmTM.TABLE_NAME+" -------------------- ");
		Timestamp timestamp = null;
		YTimestampElaborazDtsx elabAnalisiQcRm = (YTimestampElaborazDtsx) Factory.createObject(YTimestampElaborazDtsx.class);
		elabAnalisiQcRm.setKey(KeyHelper.buildObjectKey(new String[] {Azienda.getAziendaCorrente(),"QC_ANALISI_RM"}));
		elabAnalisiQcRm.retrieve();
		if(getDataEstrazioneAnalisiAcc() != null)
			timestamp = TimeUtils.getTimestamp(getDataEstrazioneAnalisiRm());
		else 
			timestamp = elabAnalisiQcRm.getDatiComuniEstesi().getTimestampAgg();
		output.println("  Estraggo "+YNormeQualitaTM.TABLE_NAME+" con TIMESTAMP_AGG > "+timestamp.toString());
		List analisiChimiche = estrazioneAnalisiQcRm(timestamp);
		output.println("  Estratte "+analisiChimiche.size()+" "+YNormeQualitaTM.TABLE_NAME+" ");
		output.println("  Esportazione "+YPTQcAnalisiRmTM.TABLE_NAME+" verso Target ");
		int rc = esportaOggettiVersoTarget(analisiChimiche);
		if(rc == ErrorCodes.OK) {
			elabAnalisiQcRm.setDirty(true);
			if(elabAnalisiQcRm.save() > 0) {
				ConnectionManager.commit();
			}

			ConnectionDescriptor cnd = YDtsxPtExpOrdEsecRunner.externalConnectionDescriptor(
					NOME_DB_EXT,
					UTENTE_DB_EXT,
					PWD_DB_EXT, 
					SRV_DB_EXT, 
					PORTA_DB_EXT);
			try {
				if(cnd != null) {
					ConnectionManager.pushConnection(cnd);
					riattiva_QC_ANALISI_RM(cnd);
					riattiva_QC_ANALISI_RM_2(cnd);
					YPT_QC_ANALISI_RM_set_Elaborato_To_X(cnd);
				}
			} catch (Throwable ex) {
				ex.printStackTrace(Trace.excStream);
			} finally {
				if (cnd != null) {
					cnd.closeConnection();
					ConnectionManager.popConnection(cnd);
				}
			}

		}
		output.println("  Esportazione verso "+YPTQcAnalisiRmTM.TABLE_NAME+" avvenuta con "+(rc > 0 ? "successo" : "errori"));
		output.println(" ----------------- Fine estrazione "+YPTQcAnalisiRmTM.TABLE_NAME+" -------------------- ");
		output.println();
		return rc;
	}

	protected static int esportaOggettiVersoTarget(List<PersistentObject> pos) {
		int result = ErrorCodes.OK;
		if(pos.size() == 0)
			return result;
		ConnectionDescriptor cnd = YDtsxPtExpOrdEsecRunner.externalConnectionDescriptor(
				NOME_DB_EXT,
				UTENTE_DB_EXT,
				PWD_DB_EXT, 
				SRV_DB_EXT, 
				PORTA_DB_EXT);
		try {
			if(cnd != null) {
				ConnectionManager.pushConnection(cnd);
				for(PersistentObject analisi : pos) {
					result += analisi.save();
				}
				cnd.commit();
			}
		} catch (Throwable ex) {
			ex.printStackTrace(Trace.excStream);
			result = ErrorCodes.GENERIC_ERROR;
		} finally {
			if (cnd != null) {
				if(result == ErrorCodes.GENERIC_ERROR) {
					try {
						cnd.rollback();
					} catch (SQLException e) {
						e.printStackTrace(Trace.excStream);
					}
				}
				try {
					cnd.closeConnection();
				} catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
				ConnectionManager.popConnection(cnd);
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	protected List estrazioneDdtFor(Timestamp timestamp) {
		List<YPTDdtFor> ddts = new ArrayList<YPTDdtFor>();
		PreparedStatement ps = null;
		try {
			ResultSet rs = null;
			ps = ConnectionManager.getCurrentConnection().prepareStatement(SQL_EXTRACT_DDT_FOR);
			Database db = ConnectionManager.getCurrentDatabase();
			db.setString(ps, 1, Azienda.getAziendaCorrente());
			db.setString(ps, 2, getFormattedTimestampForQuery(timestamp));
			rs = ps.executeQuery();
			while(rs.next()) {
				YPTDdtFor ddt = (YPTDdtFor) Factory.createObject(YPTDdtFor.class);
				ddt.setNumDdtInt(rs.getString("NUM_DDT_INT"));
				ddt.setAnnoDoc(rs.getString("ANNO_DOC"));
				ddt.setCodCf(rs.getString("COD_CF"));
				ddt.setDataDoc(rs.getDate("DATA_DOC"));
				ddt.setCodiceCausale(rs.getString("CODICE_CAUSALE"));
				ddt.setCodDep(rs.getString("COD_DEP"));
				ddt.setCodDep(rs.getString("COD_DEP_2"));
				ddt.setCodArt(rs.getString("COD_ART"));
				ddt.setDesRiga(rs.getString("DES_RIGA"));
				ddt.setNumRiga(rs.getInt("NUM_RIGA"));
				ddt.setUmAcq(rs.getString("UM_ACQ"));
				ddt.setQtaConsegnata(rs.getBigDecimal("QTA_CONSEGNATA"));
				ddt.setPrezzo(rs.getBigDecimal("PREZZO"));
				ddt.setLotto(rs.getString("LOTTO"));
				ddt.setNumDdtFornitore(rs.getString("NUM_DDT_FORNITORE"));
				ddt.setDataDocFornitore(rs.getDate("DATA_DOC_FORNITORE"));
				ddt.setLingotto(rs.getString("LINGOTTO"));
				ddt.setRiferimentoAnnoOrdFor(rs.getString("RIFERIMENTO_ANNO_ORD_FOR"));
				ddt.setRiferimentoNumOrdFor(rs.getString("RIFERIMENTO_NUM_ORD_FOR"));
				ddt.setRiferimentoRigaOrdFor(rs.getInt("RIFERIMENTO_RIGA_ORD_FOR"));
				ddt.setTimestampAgg(rs.getTimestamp("TIMESTAMP_AGG"));
				ddt.setElaborato("0");

				ddts.add(ddt);
			}
		}catch (SQLException ex) {
			ex.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(ps != null) {
					ps.close();
				}
			}catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		return ddts;
	}

	@SuppressWarnings("rawtypes")
	protected List estrazioneOrdFor(Timestamp timestamp) {
		List<YPTOrdFor> ordFors = new ArrayList<YPTOrdFor>();
		List<YOrdineAcquistoRigaPrm> righe = getRigheAcquistoPerEsportazioneOrdFor(timestamp);
		for(YOrdineAcquistoRigaPrm riga : righe) {
			Timestamp timestampAgg = null;
			YOrdineAcquisto testata = (YOrdineAcquisto) riga.getTestata();

			timestampAgg = testata.getDatiComuni().getTimestampAgg();

			if(riga.getDatiComuni().getTimestampAgg().compareTo(timestamp) > 0) {
				timestampAgg = riga.getDatiComuni().getTimestampAgg();
			}

			YPTOrdFor ordFor = (YPTOrdFor) Factory.createObject(YPTOrdFor.class);
			ordFor.setAnnoDoc(testata.getAnnoDocumento());
			String numDoc = "000"+testata.getNumeroDocumento().substring(4, 9);
			ordFor.setNumDoc(numDoc);
			ordFor.setNumRiga(riga.getNumeroRigaDocumento());
			ordFor.setDataDoc(testata.getDataDocumento());
			ordFor.setCodArt(riga.getArticolo().getIdClasseFiscale());

			ordFor.setCodCf(testata.getFornitore().getCodSistemaInfEsterno());

			if(riga.getRigheLotto().size() > 0) {
				OrdineAcquistoRigaLottoPrm rigaLotto = (OrdineAcquistoRigaLottoPrm) riga.getRigheLotto().get(0);
				ordFor.setDesRiga(rigaLotto.getIdArticolo());
				ordFor.setQuantUmBase(rigaLotto.getQuantitaOrdinata().getQuantitaInUMRif());

				if(rigaLotto.getDatiComuni().getTimestampAgg().compareTo(timestamp) > 0) {
					timestampAgg = riga.getDatiComuni().getTimestampAgg();
				}
			}else {
				continue;
			}

			ordFor.setUmBase(riga.getIdUMRif());
			ordFor.setPrezzo(riga.getPrezzo());
			ordFor.setNoteInt(riga.getNota());
			ordFor.setDataConsRiga(riga.getDataConsegnaConfermata());
			ordFor.setId(riga.getIdNormaQlt());

			ordFor.setTimestampAgg(timestampAgg);

			ordFor.setElaborato("0");

			ordFors.add(ordFor);

		}
		return ordFors;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<YPTQcAnalisiRm> estrazioneAnalisiQcRm(Timestamp timestampElaborazione) {
		List list = new ArrayList();

		Vector<YNormeQualita> norme = norme(timestampElaborazione);
		if(norme != null) {

			for (Iterator<YNormeQualita> iterator = norme.iterator(); iterator.hasNext();) {
				YNormeQualita norma = (YNormeQualita) iterator.next();

				try {
					String ALIAS_ACCIAIO = norma.getAliasAcciaio() != null ? norma.getAliasAcciaio() : "";
					String ACCIAO = norma.getDscAcciaio();
					Float TP_FUS_ACC = 0.f;
					String DOC_REV_APPR	= "";
					String DOC_REV_PREP = "";
					if(norma.getIdDipendAprv() != null) {
						Dipendente dipendenteApprovazione = getDipendente(norma.getIdDipendAprv());
						DOC_REV_APPR = dipendenteApprovazione.getCognome() + " " + dipendenteApprovazione.getNome();
					}
					if(norma.getIdDipendCrz() != null) {
						Dipendente dipendentePreparazione = getDipendente(norma.getIdDipendCrz());
						DOC_REV_PREP = dipendentePreparazione.getCognome() + " " + dipendentePreparazione.getNome();
					}
					String ID = norma.getIdNorma(); //first key part
					String NOTE = norma.getNote1();
					String NOTE_2 = norma.getNote2();
					String NOTE_3 = norma.getNote3();
					Integer FLAG_PR_SPE = norma.getProcessoSpec() ? 1 : 0;
					String DOC_REV = norma.getRevisione();
					String STD_ACC = norma.getSpecRifCod1();
					String STD_ACC_2 = norma.getSpecRifCod2();
					String STD_ACC_3 = norma.getSpecRifCod3();
					String STD_ACC_4 = norma.getSpecRifCod4();
					String STD_ACC_5 = norma.getSpecRifCod5();
					String STD_ACC_6 = norma.getSpecRifCod6();
					String STD_ACC_DES = norma.getSpecRifDsc1();
					String STD_ACC_DES_2 = norma.getSpecRifDsc2();
					String STD_ACC_DES_3 = norma.getSpecRifDsc3();
					String STD_ACC_DES_4 = norma.getSpecRifDsc4();
					String STD_ACC_DES_5 = norma.getSpecRifDsc5();
					String STD_ACC_DES_6 = norma.getSpecRifDsc6();
					String TAS_INCL_A = norma.getTasInclA();
					String TAS_INCL_B = norma.getTasInclB();
					String TAS_INCL_C = norma.getTasInclC();
					String TAS_INCL_D = norma.getTasInclD();
					String TAS_INCL_A_G = norma.getTasInclGa();
					String TAS_INCL_B_G = norma.getTasInclGb();
					String TAS_INCL_C_G = norma.getTasInclGc();
					String TAS_INCL_D_G = norma.getTasInclGd();
					String TAS_INCL_NOTE_1 = norma.getTasInclNote1();
					String TAS_INCL_NOTE_2 = norma.getTasInclNote2();
					String TAS_INCL_NOTE_3 = norma.getTasInclNote3();
					Float DOC_RICH = 0.f;
					Integer TP_AFF_ACC = norma.getTypAffAcc01() ? 1 : 0;
					Integer TP_AFF_ACC_2 = norma.getTypAffAcc02() ? 1 : 0;
					Integer TP_AFF_ACC_3 = norma.getTypAffAcc03() ? 1 : 0;
					Integer TP_AFF_ACC_4 = norma.getTypAffAcc04() ? 1 : 0;
					Integer TP_AFF_ACC_5 = norma.getTypAffAcc05() ? 1 : 0;
					Integer TP_AFF_ACC_6 = norma.getTypAffAcc06() ? 1 : 0;
					Integer TP_AFF_GR_1 = norma.getTypAffGra01() ? 1 : 0;
					String RM_NUM_REV = norma.getDescrizione();
					String RM_OR_N = norma.getRMOrNum();
					String DES_VALORE_PRODOTTO = norma.getDescrValProdotto();
					String DOC_DESC_REV = norma.getDescrizione2();
					Timestamp DOC_DATA_REV = norma.getDatiComuni().getTimestampCrz();
					String TAS_INCL_ACT = norma.getTasInclDsc();
					String TAS_INCL_ACT_G = norma.getTasInclgDsc();
					String TP_AFF_GR = "";
					Integer FLAG_RMC = norma.getTipologia() == '2' ? 1 : 0;
					Timestamp TIMESTAMP_AGG = norma.getDatiComuni().getTimestampAgg(); //second key part
					Integer AL_FLAG_INF = 0;
					Integer ALSOL_FLAG_INF = 0;
					Integer ALSOL_INF = 0;
					Integer AS_FLAG_INF = 0;
					Integer B_FLAG_INF = 0;
					Integer BI_FLAG_INF = 0;
					Integer C_FLAG_INF = 0;
					Integer C_N_FLAG_INF = 0;
					Integer CA_FLAG_INF = 0;
					Integer CB_FLAG_INF = 0;
					Integer CE_FLAG_INF = 0;
					Integer CO_FLAG_INF = 0;
					Integer CR_FLAG_INF = 0;
					Integer CR_EQ_FLAG_INF = 0;
					Integer CU_FLAG_INF = 0;
					Integer FE_FLAG_INF = 0;
					Integer H_FLAG_INF = 0;
					Integer JF_FLAG_INF = 0;
					Integer MN_FLAG_INF = 0;
					Integer MO_FLAG_INF = 0;
					Integer N_FLAG_INF = 0;
					Integer NB_FLAG_INF = 0;
					Integer NB_TA_FLAG_INF = 0;
					Integer NI_FLAG_INF = 0;
					Integer O_FLAG_INF = 0;
					Integer P_FLAG_INF = 0;
					Integer PB_FLAG_INF = 0;
					Integer PCM_FLAG_INF = 0;
					Integer PRE_FLAG_INF = 0;
					Integer S_FLAG_INF = 0;
					Integer SB_FLAG_INF = 0;
					Integer SI_FLAG_INF = 0;
					Integer SN_FLAG_INF = 0;
					Integer TA_FLAG_INF = 0;
					Integer TI_FLAG_INF = 0;
					Integer V_FLAG_INF = 0;
					Integer W_FLAG_INF = 0;
					Integer XF_FLAG_INF = 0;
					Integer Y_FLAG_INF = 0;
					Integer ZR_FLAG_INF = 0;

					BigDecimal AL_MIN = BigDecimal.ZERO;
					BigDecimal AL_MAX = BigDecimal.ZERO;
					BigDecimal ALSOL_MIN = BigDecimal.ZERO;
					BigDecimal ALSOL_MAX = BigDecimal.ZERO;
					BigDecimal AS_MIN = BigDecimal.ZERO;
					BigDecimal AS_MAX = BigDecimal.ZERO;
					BigDecimal B_MIN = BigDecimal.ZERO;
					BigDecimal B_MAX = BigDecimal.ZERO;
					BigDecimal BI_MIN = BigDecimal.ZERO;
					BigDecimal BI_MAX = BigDecimal.ZERO;
					BigDecimal C_MIN = BigDecimal.ZERO;
					BigDecimal C_MAX = BigDecimal.ZERO;
					BigDecimal C_N_MIN = BigDecimal.ZERO;
					BigDecimal C_N_MAX = BigDecimal.ZERO;
					BigDecimal CA_MIN = BigDecimal.ZERO;
					BigDecimal CA_MAX = BigDecimal.ZERO;
					BigDecimal CB_MIN = BigDecimal.ZERO;
					BigDecimal CB_MAX = BigDecimal.ZERO;
					BigDecimal CE_MIN = BigDecimal.ZERO;
					BigDecimal CE_MAX = BigDecimal.ZERO;
					BigDecimal CO_MIN = BigDecimal.ZERO;
					BigDecimal CO_MAX = BigDecimal.ZERO;
					BigDecimal CR_MIN = BigDecimal.ZERO;
					BigDecimal CR_MAX = BigDecimal.ZERO;
					BigDecimal CR_EQ_MIN = BigDecimal.ZERO;
					BigDecimal CR_EQ_MAX = BigDecimal.ZERO;
					BigDecimal CU_MIN = BigDecimal.ZERO;
					BigDecimal CU_MAX = BigDecimal.ZERO;
					BigDecimal FE_MIN = BigDecimal.ZERO;
					BigDecimal FE_MAX = BigDecimal.ZERO;
					BigDecimal H_MIN = BigDecimal.ZERO;
					BigDecimal H_MAX = BigDecimal.ZERO;
					BigDecimal H_PPM_O_PERC = BigDecimal.ZERO;
					BigDecimal JF_MIN = BigDecimal.ZERO;
					BigDecimal JF_MAX = BigDecimal.ZERO;
					BigDecimal MN_MIN = BigDecimal.ZERO;
					BigDecimal MN_MAX = BigDecimal.ZERO;
					BigDecimal MO_MIN = BigDecimal.ZERO;
					BigDecimal MO_MAX = BigDecimal.ZERO;
					BigDecimal N_MIN = BigDecimal.ZERO;
					BigDecimal N_MAX = BigDecimal.ZERO;
					BigDecimal N_PPM_O_PERC = BigDecimal.ZERO;
					BigDecimal NB_MIN = BigDecimal.ZERO;
					BigDecimal NB_MAX = BigDecimal.ZERO;
					BigDecimal NB_TA_MIN = BigDecimal.ZERO;
					BigDecimal NB_TA_MAX = BigDecimal.ZERO;
					BigDecimal NI_MIN = BigDecimal.ZERO;
					BigDecimal NI_MAX = BigDecimal.ZERO;
					BigDecimal O_MIN = BigDecimal.ZERO;
					BigDecimal O_MAX = BigDecimal.ZERO;
					BigDecimal O_PPM_O_PERC = BigDecimal.ZERO;
					BigDecimal P_MIN = BigDecimal.ZERO;
					BigDecimal P_MAX = BigDecimal.ZERO;
					BigDecimal PB_MIN = BigDecimal.ZERO;
					BigDecimal PB_MAX = BigDecimal.ZERO;
					BigDecimal PCM_MIN = BigDecimal.ZERO;
					BigDecimal PCM_MAX = BigDecimal.ZERO;
					BigDecimal PRE_MIN = BigDecimal.ZERO;
					BigDecimal PRE_MAX = BigDecimal.ZERO;
					BigDecimal S_MIN = BigDecimal.ZERO;
					BigDecimal S_MAX = BigDecimal.ZERO;
					BigDecimal SB_MIN = BigDecimal.ZERO;
					BigDecimal SB_MAX = BigDecimal.ZERO;
					BigDecimal SI_MIN = BigDecimal.ZERO;
					BigDecimal SI_MAX = BigDecimal.ZERO;
					BigDecimal SN_MIN = BigDecimal.ZERO;
					BigDecimal SN_MAX = BigDecimal.ZERO;
					BigDecimal TA_MIN = BigDecimal.ZERO;
					BigDecimal TA_MAX = BigDecimal.ZERO;
					BigDecimal TI_MIN = BigDecimal.ZERO;
					BigDecimal TI_MAX = BigDecimal.ZERO;
					BigDecimal V_MIN = BigDecimal.ZERO;
					BigDecimal V_MAX = BigDecimal.ZERO;
					BigDecimal W_MIN = BigDecimal.ZERO;
					BigDecimal W_MAX = BigDecimal.ZERO;
					BigDecimal XF_MIN = BigDecimal.ZERO;
					BigDecimal XF_MAX = BigDecimal.ZERO;
					BigDecimal Y_MIN = BigDecimal.ZERO;
					BigDecimal Y_MAX = BigDecimal.ZERO;
					BigDecimal ZR_MIN = BigDecimal.ZERO;
					BigDecimal ZR_MAX = BigDecimal.ZERO;
					String AC_FORMULA_1 = null;
					String AC_FORMULA_2 = null;
					String AC_FORMULA_3 = null;
					String AC_FORMULA_4 = null;
					String AC_FORMULA_5 = null;
					BigDecimal AC_FORMULA_1_MAX = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_1_MIN = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_2_MAX = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_2_MIN = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_3_MAX = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_3_MIN = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_4_MAX = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_4_MIN = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_5_MAX = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_5_MIN = BigDecimal.ZERO;

					CicloCollaudoTestata ciclo = norma.getAnalisiChimica();
					if(ciclo != null) {
						if(ciclo.getFasi().size() > 0) {
							YCicloCollaudoFase faseAnalisiChimica = (YCicloCollaudoFase) ciclo.getFasi().get(0);
							List caratteristiche = faseAnalisiChimica.getCaratteristiche();
							Iterator iterCarat = caratteristiche.iterator();

							while(iterCarat.hasNext()) {
								YCicloCollaudoCaratt caratteristica = (YCicloCollaudoCaratt) iterCarat.next();
								String descrizioneRidotta = caratteristica.getDescrizioneCicloNLS().getDescrizioneRidotta();
								Integer INF = caratteristica.getYInformativo() ? 1 : 0;
								BigDecimal LIM_SUP_TOL = BigDecimal.ZERO;
								BigDecimal LIM_INF_TOL = BigDecimal.ZERO;

								if (caratteristica.getLimSupTolleranza() != null
										&& (caratteristica.getLimSupTolleranza().compareTo(BigDecimal.valueOf(-9999)) == 0
										|| caratteristica.getLimSupTolleranza().compareTo(BigDecimal.valueOf(9999)) == 0)) {
									LIM_SUP_TOL = null;
								} else {
									LIM_SUP_TOL = caratteristica.getLimSupTolleranza();
								}

								if (caratteristica.getLimInfTolleranza() != null
										&& (caratteristica.getLimInfTolleranza().compareTo(BigDecimal.valueOf(-9999)) == 0
										|| caratteristica.getLimInfTolleranza().compareTo(BigDecimal.valueOf(9999)) == 0)) {
									LIM_INF_TOL = null;
								} else {
									LIM_INF_TOL = caratteristica.getLimInfTolleranza();
								}

								switch (descrizioneRidotta) {
								case "Al":
									AL_FLAG_INF = INF;
									AL_MIN = LIM_INF_TOL;
									AL_MAX = LIM_SUP_TOL;
									break;
								case "Al Sol":
									ALSOL_FLAG_INF = INF;
									ALSOL_MIN = LIM_INF_TOL;
									ALSOL_MAX = LIM_SUP_TOL;
									ALSOL_INF = INF;
									break;
								case "As":
									AS_FLAG_INF = INF;
									AS_MIN = LIM_INF_TOL;
									AS_MAX = LIM_SUP_TOL;
									break;
								case "B":
									B_FLAG_INF = INF;
									B_MIN = LIM_INF_TOL;
									B_MAX = LIM_SUP_TOL;
									break;
								case "Bi":
									BI_FLAG_INF = INF;
									BI_MIN = LIM_INF_TOL;
									BI_MAX = LIM_SUP_TOL;
									break;
								case "C":
									C_FLAG_INF = INF;
									C_MIN = LIM_INF_TOL;
									C_MAX = LIM_SUP_TOL;
									break;
								case "Ca":
									CA_FLAG_INF = INF;
									CA_MIN = LIM_INF_TOL;
									CA_MAX = LIM_SUP_TOL;
									break;
								case "Cb":
									CB_FLAG_INF = INF;
									CB_MIN = LIM_INF_TOL;
									CB_MAX = LIM_SUP_TOL;
									break;
								case "Ceq":
									CE_FLAG_INF = INF;
									CE_MIN = LIM_INF_TOL;
									CE_MAX = LIM_SUP_TOL;
									break;
								case "Co":
									CO_FLAG_INF = INF;
									CO_MIN = LIM_INF_TOL;
									CO_MAX = LIM_SUP_TOL;
									break;
								case "Cr":
									CR_FLAG_INF = INF;
									CR_MIN = LIM_INF_TOL;
									CR_MAX = LIM_SUP_TOL;
									break;
								case "Creq":
									CR_EQ_FLAG_INF = INF;
									CR_EQ_MIN = LIM_INF_TOL;
									CR_EQ_MAX = LIM_SUP_TOL;
									break;
								case "Cu":
									CU_FLAG_INF = INF;
									CU_MIN = LIM_INF_TOL;
									CU_MAX = LIM_SUP_TOL;
									break;
								case "H":
									H_FLAG_INF = INF;
									H_MIN = LIM_INF_TOL;
									H_MAX = LIM_SUP_TOL;
									if(caratteristica.getIdUnitaMisura() != null && 
											(caratteristica.getIdUnitaMisura().equals("%") || caratteristica.getIdUnitaMisura().equals("P%"))) {
										H_PPM_O_PERC = BigDecimal.ONE;
									}
									break;
								case "J Fact":
									JF_FLAG_INF = INF;
									JF_MIN = LIM_INF_TOL;
									JF_MAX = LIM_SUP_TOL;
									break;
								case "Mn":
									MN_FLAG_INF = INF;
									MN_MIN = LIM_INF_TOL;
									MN_MAX = LIM_SUP_TOL;
									break;
								case "Mo":
									MO_FLAG_INF = INF;
									MO_MIN = LIM_INF_TOL;
									MO_MAX = LIM_SUP_TOL;
									break;
								case "N":
									N_FLAG_INF = INF;
									N_MIN = LIM_INF_TOL;
									N_MAX = LIM_SUP_TOL;
									if(caratteristica.getIdUnitaMisura() != null && 
											(caratteristica.getIdUnitaMisura().equals("%") || caratteristica.getIdUnitaMisura().equals("P%"))) {
										N_PPM_O_PERC = BigDecimal.ONE;
									}
									break;
								case "Nb":
									NB_FLAG_INF = INF;
									NB_MIN = LIM_INF_TOL;
									NB_MAX = LIM_SUP_TOL;
									break;
								case "Ni":
									NI_FLAG_INF = INF;
									NI_MIN = LIM_INF_TOL;
									NI_MAX = LIM_SUP_TOL;
									break;
								case "O":
									O_FLAG_INF = INF;
									O_MIN = LIM_INF_TOL;
									O_MAX = LIM_SUP_TOL;
									if(caratteristica.getIdUnitaMisura() != null && 
											(caratteristica.getIdUnitaMisura().equals("%") || caratteristica.getIdUnitaMisura().equals("P%"))) {
										O_PPM_O_PERC = BigDecimal.ONE;
									}
									break;
								case "P":
									P_FLAG_INF = INF;
									P_MIN = LIM_INF_TOL;
									P_MAX = LIM_SUP_TOL;
									break;
								case "Pb":
									PB_FLAG_INF = INF;
									PB_MIN = LIM_INF_TOL;
									PB_MAX = LIM_SUP_TOL;
									break;
								case "PCM":
									PCM_FLAG_INF = INF;
									PCM_MIN = LIM_INF_TOL;
									PCM_MAX = LIM_SUP_TOL;
									break;
								case "Pre":
									PRE_FLAG_INF = INF;
									PRE_MIN = LIM_INF_TOL;
									PRE_MAX = LIM_SUP_TOL;
									break;
								case "S":
									S_FLAG_INF = INF;
									S_MIN = LIM_INF_TOL;
									S_MAX = LIM_SUP_TOL;
									break;
								case "Sb":
									SB_FLAG_INF = INF;
									SB_MIN = LIM_INF_TOL;
									SB_MAX = LIM_SUP_TOL;
									break;
								case "Si":
									SI_FLAG_INF = INF;
									SI_MIN = LIM_INF_TOL;
									SI_MAX = LIM_SUP_TOL;
									break;
								case "Sn":
									SN_FLAG_INF = INF;
									SN_MIN = LIM_INF_TOL;
									SN_MAX = LIM_SUP_TOL;
									break;
								case "Ta":
									TA_FLAG_INF = INF;
									TA_MIN = LIM_INF_TOL;
									TA_MAX = LIM_SUP_TOL;
									break;
								case "Ti":
									TI_FLAG_INF = INF;
									TI_MIN = LIM_INF_TOL;
									TI_MAX = LIM_SUP_TOL;
									break;
								case "V":
									V_FLAG_INF = INF;
									V_MIN = LIM_INF_TOL;
									V_MAX = LIM_SUP_TOL;
									break;
								case "W":
									W_FLAG_INF = INF;
									W_MIN = LIM_INF_TOL;
									W_MAX = LIM_SUP_TOL;
									break;
								case "X Fact":
									XF_FLAG_INF = INF;
									XF_MIN = LIM_INF_TOL;
									XF_MAX = LIM_SUP_TOL;
									break;
								case "Zr":
									ZR_FLAG_INF = INF;
									ZR_MIN = LIM_INF_TOL;
									ZR_MAX = LIM_SUP_TOL;
									break;
								case "C + N":
									C_N_FLAG_INF = INF;
									C_N_MIN = LIM_INF_TOL;
									C_N_MAX = LIM_SUP_TOL;
									break;
								case "Fe":
									FE_FLAG_INF = INF;
									FE_MIN = LIM_INF_TOL;
									FE_MAX = LIM_SUP_TOL;
									break;
								case "Nb + Ta":
									NB_TA_FLAG_INF = INF;
									NB_TA_MIN = LIM_INF_TOL;
									NB_TA_MAX = LIM_SUP_TOL;
									break;
								case "Y":
									Y_FLAG_INF = INF;
									Y_MIN = LIM_INF_TOL;
									Y_MAX = LIM_SUP_TOL;
									break;
								default:
									if(caratteristica.getYFormula() != null &&  !caratteristica.getYFormula().isEmpty()) {
										if(AC_FORMULA_1 == null) {
											AC_FORMULA_1 = caratteristica.getYFormula();
											if(AC_FORMULA_1 != null  && AC_FORMULA_1.length() > 15) {
												AC_FORMULA_1 = AC_FORMULA_1.substring(0,15);
											}
											AC_FORMULA_1_MIN = LIM_INF_TOL;
											AC_FORMULA_1_MAX = LIM_SUP_TOL;
										}
										if(AC_FORMULA_2 == null) {
											AC_FORMULA_2 = caratteristica.getYFormula();
											if(AC_FORMULA_2 != null  && AC_FORMULA_2.length() > 15) {
												AC_FORMULA_2 = AC_FORMULA_2.substring(0,15);
											}
											AC_FORMULA_2_MIN = LIM_INF_TOL;
											AC_FORMULA_2_MAX = LIM_SUP_TOL;
										}
										if(AC_FORMULA_3 == null) {
											AC_FORMULA_3 = caratteristica.getYFormula();
											if(AC_FORMULA_3 != null  && AC_FORMULA_3.length() > 15) {
												AC_FORMULA_3 = AC_FORMULA_3.substring(0,15);
											}
											AC_FORMULA_3_MIN = LIM_INF_TOL;
											AC_FORMULA_3_MAX = LIM_SUP_TOL;
										}
										if(AC_FORMULA_4 == null) {
											AC_FORMULA_4 = caratteristica.getYFormula();
											if(AC_FORMULA_4 != null  && AC_FORMULA_4.length() > 15) {
												AC_FORMULA_4 = AC_FORMULA_4.substring(0,15);
											}
											AC_FORMULA_4_MIN = LIM_INF_TOL;
											AC_FORMULA_4_MAX = LIM_SUP_TOL;
										}
										if(AC_FORMULA_5 == null) {
											AC_FORMULA_5 = caratteristica.getYFormula();
											if(AC_FORMULA_5 != null  && AC_FORMULA_5.length() > 15) {
												AC_FORMULA_5 = AC_FORMULA_5.substring(0,15);
											}
											AC_FORMULA_5_MIN = LIM_INF_TOL;
											AC_FORMULA_5_MAX = LIM_SUP_TOL;
										}

									}
									break;
								}
							}
						}
					}

					YPTQcAnalisiRm analisiChimica = (YPTQcAnalisiRm) Factory.createObject(YPTQcAnalisiRm.class);

					analisiChimica.setAlliasAcciaio(ALIAS_ACCIAIO);
					analisiChimica.setAcciaio(ACCIAO);
					analisiChimica.setTpFusAcc(TP_FUS_ACC);
					analisiChimica.setDocRevAppr(DOC_REV_APPR);
					analisiChimica.setDocRevPrep(DOC_REV_PREP);
					analisiChimica.setId(ID);
					analisiChimica.setNote(NOTE);
					analisiChimica.setNote2(NOTE_2);
					analisiChimica.setNote3(NOTE_3);
					analisiChimica.setFlagPrSpe(FLAG_PR_SPE);
					analisiChimica.setDocRev(DOC_REV);
					analisiChimica.setRmNum(norma.getNumeroRM());
					analisiChimica.setStdAcc(STD_ACC);
					analisiChimica.setStdAcc2(STD_ACC_2);
					analisiChimica.setStdAcc3(STD_ACC_3);
					analisiChimica.setStdAcc4(STD_ACC_4);
					analisiChimica.setStdAcc5(STD_ACC_5);
					analisiChimica.setStdAcc6(STD_ACC_6);
					analisiChimica.setStdAccDes(STD_ACC_DES);
					analisiChimica.setStdAccDes2(STD_ACC_DES_2);
					analisiChimica.setStdAccDes3(STD_ACC_DES_3);
					analisiChimica.setStdAccDes4(STD_ACC_DES_4);
					analisiChimica.setStdAccDes5(STD_ACC_DES_5);
					analisiChimica.setStdAccDes6(STD_ACC_DES_6);
					analisiChimica.setTasInclA(TAS_INCL_A);
					analisiChimica.setTasInclB(TAS_INCL_B);
					analisiChimica.setTasInclC(TAS_INCL_C);
					analisiChimica.setTasInclD(TAS_INCL_D);
					analisiChimica.setTasInclAG(TAS_INCL_A_G);
					analisiChimica.setTasInclBG(TAS_INCL_B_G);
					analisiChimica.setTasInclCG(TAS_INCL_C_G);
					analisiChimica.setTasInclDG(TAS_INCL_D_G);
					analisiChimica.setTasInclNote1(TAS_INCL_NOTE_1);
					analisiChimica.setTasInclNote2(TAS_INCL_NOTE_2);
					analisiChimica.setTasInclNote3(TAS_INCL_NOTE_3);
					analisiChimica.setDocRich(DOC_RICH);
					analisiChimica.setTpAffAcc(TP_AFF_ACC);
					analisiChimica.setTpAffAcc2(TP_AFF_ACC_2);
					analisiChimica.setTpAffAcc3(TP_AFF_ACC_3);
					analisiChimica.setTpAffAcc4(TP_AFF_ACC_4);
					analisiChimica.setTpAffAcc5(TP_AFF_ACC_5);
					analisiChimica.setTpAffAcc6(TP_AFF_ACC_6);
					analisiChimica.setTpAffGr1(TP_AFF_GR_1);
					analisiChimica.setRmNumRev(RM_NUM_REV);
					analisiChimica.setRmOrN(RM_OR_N);
					analisiChimica.setDesValoreProdotto(DES_VALORE_PRODOTTO);
					analisiChimica.setDocDescRev(DOC_DESC_REV);
					analisiChimica.setDocDataRev(TimeUtils.getDate(DOC_DATA_REV));
					analisiChimica.setTasInclAct(TAS_INCL_ACT);
					analisiChimica.setTasInclActG(TAS_INCL_ACT_G);
					analisiChimica.setTpAffGr(TP_AFF_GR);
					analisiChimica.setFlagRmc(FLAG_RMC);
					analisiChimica.setTimestampAgg(TIMESTAMP_AGG);

					analisiChimica.setAlFlagInf(AL_FLAG_INF);
					analisiChimica.setAlsolFlagInf(ALSOL_FLAG_INF);
					analisiChimica.setAlMin(AL_MIN);
					analisiChimica.setAlMax(AL_MAX);
					analisiChimica.setAlsolInf(ALSOL_INF);
					analisiChimica.setAlsolMin(ALSOL_MIN);
					analisiChimica.setAlsolMax(ALSOL_MAX);
					analisiChimica.setAsFlagInf(AS_FLAG_INF);
					analisiChimica.setAsMin(AS_MIN);
					analisiChimica.setAsMax(AS_MAX);
					analisiChimica.setBFlagInf(B_FLAG_INF);
					analisiChimica.setBMin(B_MIN);
					analisiChimica.setBMax(B_MAX);
					analisiChimica.setBiFlagInf(BI_FLAG_INF);
					analisiChimica.setBiMin(BI_MIN);
					analisiChimica.setBiMax(BI_MAX);
					analisiChimica.setCFlagInf(C_FLAG_INF);
					analisiChimica.setCMin(C_MIN);
					analisiChimica.setCMax(C_MAX);
					analisiChimica.setCNFlagInf(C_N_FLAG_INF);
					analisiChimica.setCNMin(C_N_MIN);
					analisiChimica.setCNMax(C_N_MAX);
					analisiChimica.setCaFlagInf(CA_FLAG_INF);
					analisiChimica.setCaMin(CA_MIN);
					analisiChimica.setCaMax(CA_MAX);
					analisiChimica.setCbFlagInf(CB_FLAG_INF);
					analisiChimica.setCbMin(CB_MIN);
					analisiChimica.setCbMax(CB_MAX);
					analisiChimica.setCeFlagInf(CE_FLAG_INF);
					analisiChimica.setCeMin(CE_MIN);
					analisiChimica.setCeMax(CE_MAX);
					analisiChimica.setCoFlagInf(CO_FLAG_INF);
					analisiChimica.setCoMin(CO_MIN);
					analisiChimica.setCoMax(CO_MAX);
					analisiChimica.setCrFlagInf(CR_FLAG_INF);
					analisiChimica.setCrMin(CR_MIN);
					analisiChimica.setCrMax(CR_MAX);
					analisiChimica.setCrEqFlagInf(CR_EQ_FLAG_INF);
					analisiChimica.setCrEqMin(CR_EQ_MIN);
					analisiChimica.setCrEqMax(CR_EQ_MAX);
					analisiChimica.setCuFlagInf(CU_FLAG_INF);
					analisiChimica.setCuMin(CU_MIN);
					analisiChimica.setCuMax(CU_MAX);
					analisiChimica.setFeFlagInf(FE_FLAG_INF);
					analisiChimica.setFeMin(FE_MIN);
					analisiChimica.setFeMax(FE_MAX);
					analisiChimica.setHFlagInf(H_FLAG_INF);
					analisiChimica.setHMin(H_MIN);
					analisiChimica.setHMax(H_MAX);

					if (H_PPM_O_PERC != null) {
						analisiChimica.setHPpmOPerc(H_PPM_O_PERC.intValue());
					}

					analisiChimica.setJfFlagInf(JF_FLAG_INF);
					analisiChimica.setJfMin(JF_MIN);
					analisiChimica.setJfMax(JF_MAX);
					analisiChimica.setMnFlagInf(MN_FLAG_INF);
					analisiChimica.setMnMin(MN_MIN);
					analisiChimica.setMnMax(MN_MAX);
					analisiChimica.setMoFlagInf(MO_FLAG_INF);
					analisiChimica.setMoMin(MO_MIN);
					analisiChimica.setMoMax(MO_MAX);
					analisiChimica.setNFlagInf(N_FLAG_INF);
					analisiChimica.setNMin(N_MIN);
					analisiChimica.setNMax(N_MAX);

					analisiChimica.setNPpmOPerc(N_PPM_O_PERC.intValue());

					analisiChimica.setNbFlagInf(NB_FLAG_INF);
					analisiChimica.setNbMin(NB_MIN);
					analisiChimica.setNbMax(NB_MAX);
					analisiChimica.setNbTaFlagInf(NB_TA_FLAG_INF);
					analisiChimica.setNbTaMin(NB_TA_MIN);
					analisiChimica.setNbTaMax(NB_TA_MAX);
					analisiChimica.setNiFlagInf(NI_FLAG_INF);
					analisiChimica.setNiMin(NI_MIN);
					analisiChimica.setNiMax(NI_MAX);
					analisiChimica.setOFlagInf(O_FLAG_INF);
					analisiChimica.setOMin(O_MIN);
					analisiChimica.setOMax(O_MAX);

					analisiChimica.setOPpmOPerc(O_PPM_O_PERC.intValue());

					analisiChimica.setPFlagInf(P_FLAG_INF);
					analisiChimica.setPMin(P_MIN);
					analisiChimica.setPMax(P_MAX);
					analisiChimica.setPbFlagInf(PB_FLAG_INF);
					analisiChimica.setPbMin(PB_MIN);
					analisiChimica.setPbMax(PB_MAX);
					analisiChimica.setPcmFlagInf(PCM_FLAG_INF);
					analisiChimica.setPcmMin(PCM_MIN);
					analisiChimica.setPcmMax(PCM_MAX);
					analisiChimica.setPreFlagInf(PRE_FLAG_INF);
					analisiChimica.setPreMin(PRE_MIN);
					analisiChimica.setPreMax(PRE_MAX);
					analisiChimica.setSFlagInf(S_FLAG_INF);
					analisiChimica.setSMin(S_MIN);
					analisiChimica.setSMax(S_MAX);
					analisiChimica.setSbFlagInf(SB_FLAG_INF);
					analisiChimica.setSbMin(SB_MIN);
					analisiChimica.setSbMax(SB_MAX);
					analisiChimica.setSiFlagInf(SI_FLAG_INF);
					analisiChimica.setSiMin(SI_MIN);
					analisiChimica.setSiMax(SI_MAX);
					analisiChimica.setSnFlagInf(SN_FLAG_INF);
					analisiChimica.setSnMin(SN_MIN);
					analisiChimica.setSnMax(SN_MAX);
					analisiChimica.setTaFlagInf(TA_FLAG_INF);
					analisiChimica.setTaMin(TA_MIN);
					analisiChimica.setTaMax(TA_MAX);
					analisiChimica.setTiFlagInf(TI_FLAG_INF);
					analisiChimica.setTiMin(TI_MIN);
					analisiChimica.setTiMax(TI_MAX);
					analisiChimica.setVFlagInf(V_FLAG_INF);
					analisiChimica.setVMin(V_MIN);
					analisiChimica.setVMax(V_MAX);
					analisiChimica.setWFlagInf(W_FLAG_INF);
					analisiChimica.setWMin(W_MIN);
					analisiChimica.setWMax(W_MAX);
					analisiChimica.setXfFlagInf(XF_FLAG_INF);
					analisiChimica.setXfMin(XF_MIN);
					analisiChimica.setXfMax(XF_MAX);
					analisiChimica.setYFlagInf(Y_FLAG_INF);
					analisiChimica.setYMin(Y_MIN);
					analisiChimica.setYMax(Y_MAX);
					analisiChimica.setZrFlagInf(ZR_FLAG_INF);
					analisiChimica.setZrMin(ZR_MIN);
					analisiChimica.setZrMax(ZR_MAX);
					analisiChimica.setAcFormula1(AC_FORMULA_1);
					analisiChimica.setAcFormula1Max(AC_FORMULA_1_MAX);
					analisiChimica.setAcFormula1Min(AC_FORMULA_1_MIN);
					analisiChimica.setAcFormula2(AC_FORMULA_2);
					analisiChimica.setAcFormula2Max(AC_FORMULA_2_MAX);
					analisiChimica.setAcFormula2Min(AC_FORMULA_2_MIN);
					analisiChimica.setAcFormula3(AC_FORMULA_3);
					analisiChimica.setAcFormula3Max(AC_FORMULA_3_MAX);
					analisiChimica.setAcFormula3Min(AC_FORMULA_3_MIN);
					analisiChimica.setAcFormula4(AC_FORMULA_4);
					analisiChimica.setAcFormula4Max(AC_FORMULA_4_MAX);
					analisiChimica.setAcFormula4Min(AC_FORMULA_4_MIN);
					analisiChimica.setAcFormula5(AC_FORMULA_5);
					analisiChimica.setAcFormula5Max(AC_FORMULA_5_MAX);
					analisiChimica.setAcFormula5Min(AC_FORMULA_5_MIN);

					analisiChimica.setTimestampAgg(norma.getDatiComuni().getTimestampAgg());

					analisiChimica.setElaborato("0");

					list.add(analisiChimica);


				}catch (Exception e) {
					output.println(" Norma : {"+norma.getKey()+"} exc : "+e.getMessage());
					e.printStackTrace(Trace.excStream);
				}
			}
		}

		return list;
	}

	@SuppressWarnings("rawtypes")
	protected List<YPTQcAnalisiAcciaieria> estrazioneAnalisiAcciaieria(Timestamp timestamp) {
		List<YPTQcAnalisiAcciaieria> list = new ArrayList<YPTQcAnalisiAcciaieria>();
		List<DocumentoCollaudoTestata> collaudi = recuperaDocumentiCollaudoDaEsportare(timestamp);
		if(collaudi != null) {

			for (DocumentoCollaudoTestata collaudo : collaudi) {
				try {
					String keyRigaOrdAcq = KeyHelper.buildObjectKey(new String[] {
							collaudo.getIdAzienda(),collaudo.getIdAnnoOrdine(),collaudo.getIdNumeroOrd(),collaudo.getNumeroRigaOrdineAcq().toString()
					});
					YOrdineAcquistoRigaPrm rigaOrdAcq = (YOrdineAcquistoRigaPrm) YOrdineAcquistoRigaPrm.elementWithKey(YOrdineAcquistoRigaPrm.class, keyRigaOrdAcq, PersistentObject.NO_LOCK);
					if(rigaOrdAcq != null) {
						YNormeQualita norma = rigaOrdAcq.getNormaqualita();
						if(collaudo != null) {
							CicloCollaudoTestata ciclo = collaudo.riperimentoCicloDeep(collaudo.getIdArticolo(),CicloCollaudoTestata.getDominio(collaudo.getIdTipoDocumentoCollaudo()),collaudo.getDataDocumento(),collaudo.getIdAttivitaLavorativa(),collaudo.getIdCommessa(),collaudo.getDocumentoDaOrdine(),collaudo.getOrdineAcquisto());
							Lotto lotto = collaudo.getLotto();
							if(lotto != null && ciclo != null) {
								String ENTRATA = lotto.getCodiceLotto();
								Date DATA = lotto.getDataDocAcq();
								String COLATA = lotto.getLottoAcquisto();
								String ACCIAIERIA = "";
								if(lotto.getFornitore() != null) {
									ACCIAIERIA = lotto.getFornitore().getRagioneSociale();
								}
								String ID = adaptProgressivo(ciclo.getProgressivo());
								String ACCIAIO = norma.getDscAcciaio();
								String ALLIAS_ACCIAIO = norma.getAliasAcciaio() != null ? norma.getAliasAcciaio() : "";
								String SPECIFICA_1 = "";
								String ELAB_ACCIAIO = "";
								String TIPO_TRAT = "";
								String NOTE = "";
								Timestamp TIMESTAMP_AGG = collaudo.getDatiComuni().getTimestampAgg();
								Float AL = 0.f;
								Float ALSOI= 0.f;
								Float AS_= 0.f;
								Float B= 0.f;
								Float BI= 0.f;
								Float CA= 0.f;
								Float CB= 0.f;
								Float CE= 0.f;
								Float CO= 0.f;
								Float CR= 0.f;
								Float CR_EQ= 0.f;
								Float CU= 0.f;
								Integer H_PPM_O_PERC= 0;
								Float JF= 0.f;
								Float MN= 0.f;
								Float MO= 0.f;
								Integer N_PPM_O_PERC= 0;
								Float NB= 0.f;
								Float NI= 0.f;
								Integer O_PPM_O_PERC= 0;
								Float P= 0.f;
								Float PB= 0.f;
								Float PCM= 0.f;
								Float PRE= 0.f;
								Float S= 0.f;
								Float SB= 0.f;
								Float SI= 0.f;
								Float SN= 0.f;
								Float TA= 0.f;
								Float TI= 0.f;
								Float V= 0.f;
								Float C= 0.f;
								Float W= 0.f;
								Float XF= 0.f;
								Float ZR= 0.f;
								Float C_N= 0.f;
								Float FE= 0.f;
								Float NB_TA= 0.f;
								Float Y= 0.f;
								Float H= 0.f;
								Float N= 0.f;
								Float O= 0.f;
								String AC_FORMULA_1 = null;
								Float AC_FORMULA_1_VALORE= 0.f;
								String AC_FORMULA_2= null;
								Float AC_FORMULA_2_VALORE= 0.f;
								String AC_FORMULA_3= null;
								Float AC_FORMULA_3_VALORE= 0.f;
								String AC_FORMULA_4= null;
								Float AC_FORMULA_4_VALORE= 0.f;
								String AC_FORMULA_5= null;
								Float AC_FORMULA_5_VALORE= 0.f;

								if(collaudo.getRighe().size() > 0) {
									DocumentoCollaudoRiga riga = (DocumentoCollaudoRiga) collaudo.getRighe().get(0);
									MisuraFase fase0 = (MisuraFase) riga.getMisureFasi().get(0); //questa e' la fase 'Analisi Chimica'
									List caratteristiche = fase0.getMisuraCaracteriche(); //queste sono le sue caratteristiche 
									Iterator iterCarat = caratteristiche.iterator();

									while(iterCarat.hasNext()) {
										YMisuraCaracteriche misuraCaratterisitca = (YMisuraCaracteriche) iterCarat.next(); //la caratteristica, tipo carbonio, manganese
										String carattKey = KeyHelper.buildObjectKey(new String[] {KeyHelper.getTokenObjectKey(getKey(), 1), ciclo.getProgressivo(), KeyHelper.getTokenObjectKey(getKey(), 7), KeyHelper.getTokenObjectKey(getKey(), 8)});

										//Questa e' a parita di sequenza la corrispondente caratteristica dell'anagrafica ciclo, mi serve per prendere la descrizione ridotta
										CicloCollaudoCaratteristica cicloCarratteristica = CicloCollaudoCaratteristica.elementWithKey(carattKey, PersistentObject.NO_LOCK);
										if(cicloCarratteristica != null) {
											String descrizioneRidotta = cicloCarratteristica.getDescrizioneCicloNLS().getDescrizioneRidotta();
											DocumentiCollaudoRilevazioneMisure rilevazione = (DocumentiCollaudoRilevazioneMisure) misuraCaratterisitca.getMisura().get(0);
											switch (descrizioneRidotta) {
											case "Al":
												AL = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Al Sol":
												ALSOI = rilevazione.getValoreRilevato().floatValue();
												break;
											case "As":
												AS_ = rilevazione.getValoreRilevato().floatValue();
												break;
											case "B":
												B = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Bi":
												BI = rilevazione.getValoreRilevato().floatValue();
												break;
											case "C":
												C = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Ca":
												CA = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Cb":
												CB = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Ceq":
												CE = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Co":
												CO = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Cr":
												CR = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Creq":
												CR_EQ = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Cu":
												CU = rilevazione.getValoreRilevato().floatValue();
												break;
											case "H":
												H = rilevazione.getValoreRilevato().floatValue();
												if(misuraCaratterisitca.getIdUnitaMisura().equals("P%")) {
													H_PPM_O_PERC = 1;
												}else {
													H_PPM_O_PERC = 0;
												}
												break;
											case "J Fact":
												JF = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Mn":
												MN = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Mo":
												MO = rilevazione.getValoreRilevato().floatValue();
												break;
											case "N":
												N = rilevazione.getValoreRilevato().floatValue();
												if(misuraCaratterisitca.getIdUnitaMisura().equals("P%")) {
													N_PPM_O_PERC = 1;
												}else {
													N_PPM_O_PERC = 0;
												}
												break;
											case "Nb":
												NB = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Ni":
												NI = rilevazione.getValoreRilevato().floatValue();
												break;
											case "O":
												O = rilevazione.getValoreRilevato().floatValue();
												break;
											case "P":
												P = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Pb":
												PB = rilevazione.getValoreRilevato().floatValue();
												break;
											case "PCM":
												PCM = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Pre":
												PRE = rilevazione.getValoreRilevato().floatValue();
												break;
											case "S":
												S = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Sb":
												SB = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Si":
												SI = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Sn":
												SN = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Ta":
												TA = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Ti":
												TI = rilevazione.getValoreRilevato().floatValue();
												break;
											case "V":
												V = rilevazione.getValoreRilevato().floatValue();
												break;
											case "W":
												W = rilevazione.getValoreRilevato().floatValue();
												break;
											case "X Fact":
												XF = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Zr":
												ZR = rilevazione.getValoreRilevato().floatValue();
												break;
											case "C + N":
												C_N = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Fe":
												FE = rilevazione.getValoreRilevato().floatValue();
												break;
											case "Nb + Ta":
												NB_TA= rilevazione.getValoreRilevato().floatValue();
												break;
											case "Y":
												Y = rilevazione.getValoreRilevato().floatValue();
												break;
											default:
												if(misuraCaratterisitca.getYFormula() != null &&  !misuraCaratterisitca.getYFormula().isEmpty()) {
													if(AC_FORMULA_1 == null) {
														AC_FORMULA_1 = misuraCaratterisitca.getYFormula();
														AC_FORMULA_1_VALORE = rilevazione.getValoreRilevato().floatValue();
													}
													if(AC_FORMULA_2 == null) {
														AC_FORMULA_2 = misuraCaratterisitca.getYFormula();
														AC_FORMULA_2_VALORE = rilevazione.getValoreRilevato().floatValue();
													}
													if(AC_FORMULA_3 == null) {
														AC_FORMULA_3 = misuraCaratterisitca.getYFormula();
														AC_FORMULA_3_VALORE = rilevazione.getValoreRilevato().floatValue();
													}
													if(AC_FORMULA_4 == null) {
														AC_FORMULA_4 = misuraCaratterisitca.getYFormula();
														AC_FORMULA_4_VALORE = rilevazione.getValoreRilevato().floatValue();
													}
													if(AC_FORMULA_5 == null) {
														AC_FORMULA_5 = misuraCaratterisitca.getYFormula();
														AC_FORMULA_5_VALORE = rilevazione.getValoreRilevato().floatValue();
													}

												}
												break;
											}
										}
									}
								}

								YPTQcAnalisiAcciaieria analisi = (YPTQcAnalisiAcciaieria) Factory.createObject(YPTQcAnalisiAcciaieria.class);

								analisi.setEntrata(ENTRATA);
								analisi.setData(DATA);
								analisi.setColata(COLATA);
								analisi.setAcciaieria(ACCIAIERIA);
								analisi.setId(ID);
								analisi.setAcciaio(ACCIAIO);
								analisi.setAlliasAcciaio(ALLIAS_ACCIAIO);
								analisi.setSpecifica1(SPECIFICA_1);
								analisi.setElabAcciaio(ELAB_ACCIAIO);
								analisi.setTipoTrat(TIPO_TRAT);
								analisi.setNote(NOTE);
								analisi.setTimestampAgg(TIMESTAMP_AGG);

								analisi.setAl(AL);
								analisi.setAlsol(ALSOI);
								analisi.setAs_(AS_);
								analisi.setB_(B);
								analisi.setBi(BI);
								analisi.setC_(C);
								analisi.setCa(CA);
								analisi.setCb(CB);
								analisi.setCe(CE);
								analisi.setCo(CO);
								analisi.setCr(CR);
								analisi.setCrEq(CR_EQ);
								analisi.setCu(CU);
								analisi.setH_(H);
								analisi.sethPpmOPerc(H_PPM_O_PERC);
								analisi.setJf(JF);
								analisi.setMn(MN);
								analisi.setMo(MO);
								analisi.setN_(N);
								analisi.setnPpmOPerc(N_PPM_O_PERC);
								analisi.setNb(NB);
								analisi.setNi(NI);
								analisi.setO_(O);
								analisi.setoPpmOPerc(O_PPM_O_PERC);
								analisi.setP_(P);
								analisi.setPb(PB);
								analisi.setPcm(PCM);
								analisi.setPre(PRE);
								analisi.setS_(S);
								analisi.setSb(SB);
								analisi.setSi(SI);
								analisi.setSn(SN);
								analisi.setTa(TA);
								analisi.setTi(TI);
								analisi.setV_(V);
								analisi.setW_(W);
								analisi.setXf(XF);
								analisi.setZr(ZR);
								analisi.setcN(C_N);
								analisi.setFe(FE);
								analisi.setNbTa(NB_TA);
								analisi.setY_(Y);

								analisi.setAcFormula1(AC_FORMULA_1);
								analisi.setAcFormula1Valore(AC_FORMULA_1_VALORE);
								analisi.setAcFormula2(AC_FORMULA_2);
								analisi.setAcFormula2Valore(AC_FORMULA_2_VALORE);
								analisi.setAcFormula3(AC_FORMULA_3);
								analisi.setAcFormula3Valore(AC_FORMULA_3_VALORE);
								analisi.setAcFormula4(AC_FORMULA_4);
								analisi.setAcFormula4Valore(AC_FORMULA_4_VALORE);
								analisi.setAcFormula5(AC_FORMULA_5);
								analisi.setAcFormula5Valore(AC_FORMULA_5_VALORE);

								analisi.setElaborato("0");

								list.add(analisi);
							}
						}
					}else {
						//non e' da processare
					}
				}catch (Exception e) {
					e.printStackTrace(Trace.excStream);
				}

			}
		}
		return list;
	}

	public static String adaptProgressivo(String idProgressivo) {
		int adjustedValue = Integer.parseInt(idProgressivo.trim()) + 6000;
		String formattedValue = String.format("%06d", adjustedValue);
		return formattedValue;
	}

	public List<DocumentoCollaudoTestata> recuperaDocumentiCollaudoDaEsportare(Timestamp timestamp) {
		List<DocumentoCollaudoTestata> lista = new ArrayList<DocumentoCollaudoTestata>();
		PreparedStatement ps = null;
		List<String> keys = new ArrayList<String>();
		try {
			ResultSet rs = null;
			ps = ConnectionManager.getCurrentConnection().prepareStatement(cEstrazioneDocCollNorme.getStmtString());
			Database db = ConnectionManager.getCurrentDatabase();
			db.setString(ps, 1, Azienda.getAziendaCorrente());
			db.setString(ps, 2, getFormattedTimestampForQuery(timestamp));
			rs = ps.executeQuery();
			while(rs.next()) {
				keys.add(KeyHelper.buildObjectKey(new String[] {
						rs.getString(DocumentoCollaudoTestataTM.ID_AZIENDA),
						rs.getString(DocumentoCollaudoTestataTM.ID_TIPO_DOCPRV),
						rs.getString(DocumentoCollaudoTestataTM.ID_ANNO_DOC),
						rs.getString(DocumentoCollaudoTestataTM.ID_NUMERO_DOC),
						rs.getString(DocumentoCollaudoTestataTM.ID_RIGA)
				}));
			}
		}catch (SQLException ex) {
			ex.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(ps != null) {
					ps.close();
				}
			}catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		for(String key : keys) {
			try {
				lista.add((DocumentoCollaudoTestata) DocumentoCollaudoTestata.elementWithKey(DocumentoCollaudoTestata.class, key, PersistentObject.NO_LOCK));
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		return lista;
	}

	public static DocumentoCollaudoTestata getDocumentoCollaudoDaRigaOrdAcq(String tipoDoc, OrdineAcquistoRiga ordineAcquistoRiga) {
		DocumentoCollaudoTestata doc = null;
		PreparedStatement ps = null;
		try {
			ResultSet rs = null;
			ps = existDCOrdAcqCachStmt.getStatement();
			Database db = ConnectionManager.getCurrentDatabase();
			db.setString(ps, 1, tipoDoc);
			db.setString(ps, 2, ordineAcquistoRiga.getIdAzienda());
			db.setString(ps, 3, ordineAcquistoRiga.getAnnoDocumento());
			db.setString(ps, 4, ordineAcquistoRiga.getNumeroDocumento());
			ps.setInt(5, ordineAcquistoRiga.getNumeroRigaDocumento().intValue());
			ps.setInt(6, ordineAcquistoRiga.getDettaglioRigaDocumento().intValue());
			rs = ps.executeQuery();
			if (rs.next())
				return (DocumentoCollaudoTestata) DocumentoCollaudoTestata.elementWithKey(DocumentoCollaudoTestata.class, KeyHelper.buildObjectKey(new String[] {
						rs.getString(DocumentoCollaudoTestataTM.ID_AZIENDA),
						rs.getString(DocumentoCollaudoTestataTM.ID_TIPO_DOCPRV),
						rs.getString(DocumentoCollaudoTestataTM.ID_ANNO_DOC),
						rs.getString(DocumentoCollaudoTestataTM.ID_NUMERO_DOC),
						rs.getString(DocumentoCollaudoTestataTM.ID_RIGA)
				}), PersistentObject.NO_LOCK);
		}
		catch (SQLException ex) {
			ex.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(ps != null) {
					ps.close();
				}
			}catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		return doc;
	}

	public OrdineAcquistoRigaPrm getRigaOrdineAcquistoDaNorma(String idNormaQlt) {
		OrdineAcquistoRigaPrm riga = null;
		String stmt = " SELECT "+YOrdineAcquistoRigaPrmTM.ID_AZIENDA+","+YOrdineAcquistoRigaPrmTM.ID_ANNO_ORD+","+YOrdineAcquistoRigaPrmTM.ID_NUMERO_ORD+","+YOrdineAcquistoRigaPrmTM.ID_RIGA_ORD+" ";
		stmt += "FROM "+YOrdineAcquistoRigaPrmTM.TABLE_NAME_EXT+" ";
		stmt += "WHERE "+YOrdineAcquistoRigaPrmTM.ID_AZIENDA+" = '"+Azienda.getAziendaCorrente()+"' AND "+YOrdineAcquistoRigaPrmTM.ID_NORMA_QLT+" = '"+idNormaQlt+"' ";
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = ConnectionManager.getCurrentConnection().prepareStatement(stmt);
			rs = ps.executeQuery();
			if(rs.next()) {
				riga = (OrdineAcquistoRigaPrm) OrdineAcquistoRigaPrm.elementWithKey(OrdineAcquistoRigaPrm.class, KeyHelper.buildObjectKey(new String[] {
						rs.getString(YOrdineAcquistoRigaPrmTM.ID_AZIENDA),
						rs.getString(YOrdineAcquistoRigaPrmTM.ID_ANNO_ORD),
						rs.getString(YOrdineAcquistoRigaPrmTM.ID_NUMERO_ORD),
						rs.getString(YOrdineAcquistoRigaPrmTM.ID_RIGA_ORD),
				}), PersistentObject.NO_LOCK);
			}
		}catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
			}
		}
		return riga;
	}

	public Dipendente getDipendente(String idDipdenente) {
		try {
			return (Dipendente) Dipendente.elementWithKey(Dipendente.class, KeyHelper.buildObjectKey(new String[] {
					Azienda.getAziendaCorrente(),idDipdenente
			}) ,PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected Vector<YNormeQualita> norme(Timestamp timestampElaborazione) {

		try {
			String where = " "+YNormeQualitaTM.ID_AZIENDA+" = '"+Azienda.getAziendaCorrente()+"'"
					+ " AND "+YNormeQualitaTM.TIMESTAMP_AGG+" > '"+getFormattedTimestampForQuery(timestampElaborazione)+"' ";
			return (YNormeQualita.retrieveList(YNormeQualita.class,where, "", false));
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return null;
	}

	public static String getFormattedTimestampForQuery(Timestamp timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		return sdf.format(timestamp);
	}

	public List<YOrdineAcquistoRigaPrm> getRigheAcquistoPerEsportazioneOrdFor(Timestamp timestamp){
		List<YOrdineAcquistoRigaPrm> lista = new ArrayList<YOrdineAcquistoRigaPrm>();
		PreparedStatement ps = null;
		List<String> keys = new ArrayList<String>();
		try {
			ResultSet rs = null;
			ps = ConnectionManager.getCurrentConnection().prepareStatement(SQL_EXTRACT_ORD_FOR);
			Database db = ConnectionManager.getCurrentDatabase();
			db.setString(ps, 1, Azienda.getAziendaCorrente());
			db.setString(ps, 2, getFormattedTimestampForQuery(timestamp));
			rs = ps.executeQuery();
			while(rs.next()) {
				keys.add(KeyHelper.buildObjectKey(new String[] {
						rs.getString(YOrdineAcquistoRigaPrmTM.ID_AZIENDA),
						rs.getString(YOrdineAcquistoRigaPrmTM.ID_ANNO_ORD),
						rs.getString(YOrdineAcquistoRigaPrmTM.ID_NUMERO_ORD),
						rs.getString(YOrdineAcquistoRigaPrmTM.ID_RIGA_ORD),
				}));
			}
		}catch (SQLException ex) {
			ex.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(ps != null) {
					ps.close();
				}
			}catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		for(String key : keys) {
			try {
				lista.add((YOrdineAcquistoRigaPrm) YOrdineAcquistoRigaPrm.elementWithKey(YOrdineAcquistoRigaPrm.class, key, PersistentObject.NO_LOCK));
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		return lista;

	}

	protected void riattiva_QC_ANALISI_RM(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE [PantheraTarget].[dbo].[YPT_QC_ANALISI_RM] "
					+ "SET ELABORATO = '0'   "
					+ "WHERE NOT EXISTS (SELECT * FROM Target.dbo.QC_ANALISI_RM WHERE QC_ANALISI_RM.ID = YPT_QC_ANALISI_RM.ID) "
					+ "AND ELABORATO <> '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println("  Dtsx-block : riattiva_QC_ANALISI_RM "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void riattiva_QC_ANALISI_RM_2(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	[PantheraTarget].[dbo].[YPT_QC_ANALISI_RM] "
					+ "SET "
					+ "	YPT_QC_ANALISI_RM.ELABORATO = '0' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT "
					+ "		* "
					+ "	from "
					+ "		[PantheraTarget].[dbo].[YPT_ORD_FOR] YPT_ORD_FOR "
					+ "	inner JOIN [PantheraTarget].[dbo].[YPT_DDT_FOR] YPT_DDT_FOR ON "
					+ "		YPT_DDT_FOR.RIFERIMENTO_ANNO_ORD_FOR = YPT_ORD_FOR.ANNO_DOC "
					+ "		and YPT_DDT_FOR.RIFERIMENTO_NUM_ORD_FOR = YPT_ORD_FOR.NUM_DOC "
					+ "		and YPT_DDT_FOR.RIFERIMENTO_RIGA_ORD_FOR = YPT_ORD_FOR.NUM_RIGA "
					+ "	WHERE "
					+ "		NOT EXISTS ( "
					+ "		SELECT "
					+ "			* "
					+ "		FROM "
					+ "			Target.DBO.QC_ANALISI_ACCIAIERIA "
					+ "		WHERE "
					+ "			QC_ANALISI_ACCIAIERIA.ENTRATA = YPT_DDT_FOR.LOTTO) "
					+ "		AND YPT_QC_ANALISI_RM.ID = YPT_ORD_FOR.ID)";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println("  Dtsx-block : riattiva_QC_ANALISI_RM_2 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void YPT_QC_ANALISI_RM_set_Elaborato_To_X(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	PantheraTarget.dbo.YPT_QC_ANALISI_RM "
					+ "SET "
					+ "	ELABORATO = 'X' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "		SELECT QRY_YPT_QC_ANALISI_RM.ID, "
					+ "		QRY_YPT_QC_ANALISI_RM.TIMESTAMP_AGG "
					+ "	FROM "
					+ "		PantheraTarget.dbo.YPT_QC_ANALISI_RM QRY_YPT_QC_ANALISI_RM "
					+ "	INNER JOIN ( "
					+ "			SELECT ID , "
					+ "			MAX(TIMESTAMP_AGG) MAX_TIMESTAMP_AGG "
					+ "		FROM "
					+ "			PantheraTarget.dbo.YPT_QC_ANALISI_RM YPT_QC_ANALISI_RM "
					+ "		GROUP BY "
					+ "			ID) MAX_YPT_QC_ANALISI_RM ON "
					+ "		MAX_YPT_QC_ANALISI_RM.ID = QRY_YPT_QC_ANALISI_RM.ID "
					+ "		AND MAX_YPT_QC_ANALISI_RM.MAX_TIMESTAMP_AGG > QRY_YPT_QC_ANALISI_RM.TIMESTAMP_AGG "
					+ "	WHERE "
					+ "		YPT_QC_ANALISI_RM.ID = QRY_YPT_QC_ANALISI_RM.ID "
					+ "		AND YPT_QC_ANALISI_RM.TIMESTAMP_AGG = QRY_YPT_QC_ANALISI_RM.TIMESTAMP_AGG) "
					+ "	AND YPT_QC_ANALISI_RM.ELABORATO = '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println("  Dtsx-block : YPT_QC_ANALISI_RM_set_Elaborato_To_X "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void riattiva_ORD_FOR(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE [PantheraTarget].[dbo].[YPT_ORD_FOR] "
					+ "SET ELABORATO = '0' "
					+ "WHERE NOT EXISTS (SELECT * FROM Target.dbo.ORD_FOR WHERE ORD_FOR.ANNO_DOC = YPT_ORD_FOR.ANNO_DOC AND ORD_FOR.NUM_DOC = YPT_ORD_FOR.NUM_DOC) "
					+ "AND ELABORATO <> '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println("  Dtsx-block : riattiva_ORD_FOR "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void riattiva_ORD_FOR_1(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	[PantheraTarget].[dbo].[YPT_ORD_FOR] "
					+ "SET "
					+ "	ELABORATO = '0' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT "
					+ "		* "
					+ "	from "
					+ "		[PantheraTarget].[dbo].[YPT_DDT_FOR] YPT_DDT_FOR "
					+ "	WHERE "
					+ "		YPT_DDT_FOR.RIFERIMENTO_ANNO_ORD_FOR = YPT_ORD_FOR.ANNO_DOC "
					+ "		and YPT_DDT_FOR.RIFERIMENTO_NUM_ORD_FOR = YPT_ORD_FOR.NUM_DOC "
					+ "		and YPT_DDT_FOR.RIFERIMENTO_RIGA_ORD_FOR = YPT_ORD_FOR.NUM_RIGA "
					+ "		and NOT EXISTS ( "
					+ "		SELECT "
					+ "			* "
					+ "		FROM "
					+ "			Target.DBO.QC_ANALISI_ACCIAIERIA "
					+ "		WHERE "
					+ "			QC_ANALISI_ACCIAIERIA.ENTRATA = YPT_DDT_FOR.LOTTO))";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println("  Dtsx-block : riattiva_ORD_FOR_1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void YPT_ORD_FOR_set_Elaborato_To_X(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	PantheraTarget.dbo.YPT_ORD_FOR "
					+ "SET "
					+ "	ELABORATO = 'X' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT "
					+ "		QRY_YPT_ORD_FOR.NUM_DOC, "
					+ "		QRY_YPT_ORD_FOR.ANNO_DOC, "
					+ "		QRY_YPT_ORD_FOR.NUM_RIGA, "
					+ "		QRY_YPT_ORD_FOR.TIMESTAMP_AGG "
					+ "	FROM "
					+ "		PantheraTarget.dbo.YPT_ORD_FOR QRY_YPT_ORD_FOR "
					+ "	INNER JOIN ( "
					+ "		SELECT "
					+ "			NUM_DOC , "
					+ "			ANNO_DOC, "
					+ "			NUM_RIGA, "
					+ "			MAX(TIMESTAMP_AGG) MAX_TIMESTAMP_AGG "
					+ "		FROM "
					+ "			PantheraTarget.dbo.YPT_ORD_FOR YPT_ORD_FOR "
					+ "		GROUP BY "
					+ "			NUM_DOC , "
					+ "			ANNO_DOC, "
					+ "			NUM_RIGA) MAX_YPT_ORD_FOR ON "
					+ "		MAX_YPT_ORD_FOR.NUM_DOC = QRY_YPT_ORD_FOR.NUM_DOC "
					+ "		AND MAX_YPT_ORD_FOR.ANNO_DOC = QRY_YPT_ORD_FOR.ANNO_DOC "
					+ "		AND MAX_YPT_ORD_FOR.NUM_RIGA = QRY_YPT_ORD_FOR.NUM_RIGA "
					+ "		AND MAX_YPT_ORD_FOR.MAX_TIMESTAMP_AGG > QRY_YPT_ORD_FOR.TIMESTAMP_AGG "
					+ "	WHERE "
					+ "		YPT_ORD_FOR.NUM_DOC = QRY_YPT_ORD_FOR.NUM_DOC "
					+ "		AND YPT_ORD_FOR.ANNO_DOC = QRY_YPT_ORD_FOR.ANNO_DOC "
					+ "		AND YPT_ORD_FOR.NUM_RIGA = QRY_YPT_ORD_FOR.NUM_RIGA "
					+ "		AND YPT_ORD_FOR.TIMESTAMP_AGG = QRY_YPT_ORD_FOR.TIMESTAMP_AGG) "
					+ "	AND YPT_ORD_FOR.ELABORATO = '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println("  Dtsx-block : YPT_ORD_FOR_set_Elaborato_To_X "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void riattiva_DDT_FOR(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE [PantheraTarget].[dbo].[YPT_DDT_FOR] "
					+ "SET ELABORATO = '0' "
					+ "WHERE NOT EXISTS (SELECT * FROM Target.dbo.DDT_FOR WHERE DDT_FOR.ANNO_DOC = YPT_DDT_FOR.ANNO_DOC AND DDT_FOR.NUM_DOC = YPT_DDT_FOR.NUM_DDT_INT) "
					+ "AND ELABORATO <> '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println("  Dtsx-block : riattiva_DDT_FOR "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void riattiva_DDT_FOR_1(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	[PantheraTarget].[dbo].[YPT_DDT_FOR] "
					+ "SET "
					+ "	ELABORATO = '0' "
					+ "WHERE "
					+ "	NOT EXISTS ( "
					+ "	SELECT "
					+ "		* "
					+ "	FROM "
					+ "		Target.DBO.QC_ANALISI_ACCIAIERIA "
					+ "	WHERE "
					+ "		QC_ANALISI_ACCIAIERIA.ENTRATA = YPT_DDT_FOR.LOTTO) "
					+ "";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println("  Dtsx-block : riattiva_DDT_FOR_1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void YPT_DDT_FOR_set_Elaborato_To_X(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	PantheraTarget.dbo.YPT_DDT_FOR "
					+ "SET "
					+ "	ELABORATO = 'X' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT "
					+ "		QRY_YPT_DDT_FOR.NUM_DDT_INT, "
					+ "		QRY_YPT_DDT_FOR.ANNO_DOC, "
					+ "		QRY_YPT_DDT_FOR.NUM_RIGA, "
					+ "		QRY_YPT_DDT_FOR.TIMESTAMP_AGG "
					+ "	FROM "
					+ "		PantheraTarget.dbo.YPT_DDT_FOR QRY_YPT_DDT_FOR "
					+ "	INNER JOIN ( "
					+ "		SELECT "
					+ "			NUM_DDT_INT , "
					+ "			ANNO_DOC, "
					+ "			NUM_RIGA, "
					+ "			MAX(TIMESTAMP_AGG) MAX_TIMESTAMP_AGG "
					+ "		FROM "
					+ "			PantheraTarget.dbo.YPT_DDT_FOR YPT_DDT_FOR "
					+ "		GROUP BY "
					+ "			NUM_DDT_INT , "
					+ "			ANNO_DOC, "
					+ "			NUM_RIGA) MAX_YPT_DDT_FOR ON "
					+ "		MAX_YPT_DDT_FOR.NUM_DDT_INT = QRY_YPT_DDT_FOR.NUM_DDT_INT "
					+ "		AND MAX_YPT_DDT_FOR.ANNO_DOC = QRY_YPT_DDT_FOR.ANNO_DOC "
					+ "		AND MAX_YPT_DDT_FOR.NUM_RIGA = QRY_YPT_DDT_FOR.NUM_RIGA "
					+ "		AND MAX_YPT_DDT_FOR.MAX_TIMESTAMP_AGG > QRY_YPT_DDT_FOR.TIMESTAMP_AGG "
					+ "	WHERE "
					+ "		YPT_DDT_FOR.NUM_DDT_INT = QRY_YPT_DDT_FOR.NUM_DDT_INT "
					+ "		AND YPT_DDT_FOR.ANNO_DOC = QRY_YPT_DDT_FOR.ANNO_DOC "
					+ "		AND YPT_DDT_FOR.NUM_RIGA = QRY_YPT_DDT_FOR.NUM_RIGA "
					+ "		AND YPT_DDT_FOR.TIMESTAMP_AGG = QRY_YPT_DDT_FOR.TIMESTAMP_AGG) "
					+ "	AND YPT_DDT_FOR.ELABORATO = '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println("  Dtsx-block : YPT_DDT_FOR_set_Elaborato_To_X "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void riattiva_QC_ANALISI_ACCIAIERIA(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE [PantheraTarget].[dbo].[YPT_QC_ANALISI_ACCIAIERIA] "
					+ "SET ELABORATO = '0' "
					+ "WHERE NOT EXISTS (SELECT * FROM Target.DBO.QC_ANALISI_ACCIAIERIA WHERE QC_ANALISI_ACCIAIERIA.ENTRATA = YPT_QC_ANALISI_ACCIAIERIA.ENTRATA) "
					+ "AND ELABORATO <> '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println("  Dtsx-block : riattiva_QC_ANALISI_ACCIAIERIA "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void riattiva_QC_ANALISI_ACCIAIERIA_1(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	[PantheraTarget].[dbo].[YPT_QC_ANALISI_ACCIAIERIA] "
					+ "SET "
					+ "	ELABORATO = '0' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT "
					+ "		* "
					+ "	FROM "
					+ "		[PantheraTarget].[dbo].[YPT_DDT_FOR] YPT_DDT_FOR "
					+ "	WHERE "
					+ "		NOT EXISTS ( "
					+ "		SELECT "
					+ "			* "
					+ "		FROM "
					+ "			Target.DBO.QC_ANALISI_ACCIAIERIA "
					+ "		WHERE "
					+ "			QC_ANALISI_ACCIAIERIA.ENTRATA = YPT_DDT_FOR.LOTTO) "
					+ "		and YPT_DDT_FOR.LOTTO = YPT_QC_ANALISI_ACCIAIERIA.ENTRATA)";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println("  Dtsx-block : riattiva_QC_ANALISI_ACCIAIERIA_1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void YPT_QC_ANALISI_ACCIAIERIA_set_Elaborato_To_X(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	PantheraTarget.dbo.YPT_QC_ANALISI_ACCIAIERIA "
					+ "SET "
					+ "	ELABORATO = 'X' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT "
					+ "		QRY_YPT_QC_ANALISI_ACCIAIERIA.ENTRATA, "
					+ "		QRY_YPT_QC_ANALISI_ACCIAIERIA.TIMESTAMP_AGG "
					+ "	FROM "
					+ "		PantheraTarget.dbo.YPT_QC_ANALISI_ACCIAIERIA QRY_YPT_QC_ANALISI_ACCIAIERIA "
					+ "	INNER JOIN ( "
					+ "		SELECT "
					+ "			ENTRATA , "
					+ "			MAX(TIMESTAMP_AGG) MAX_TIMESTAMP_AGG "
					+ "		FROM "
					+ "			PantheraTarget.dbo.YPT_QC_ANALISI_ACCIAIERIA YPT_QC_ANALISI_ACCIAIERIA "
					+ "		GROUP BY "
					+ "			ENTRATA) MAX_YPT_QC_ANALISI_ACCIAIERIA ON "
					+ "		MAX_YPT_QC_ANALISI_ACCIAIERIA.ENTRATA = QRY_YPT_QC_ANALISI_ACCIAIERIA.ENTRATA "
					+ "		AND MAX_YPT_QC_ANALISI_ACCIAIERIA.MAX_TIMESTAMP_AGG > QRY_YPT_QC_ANALISI_ACCIAIERIA.TIMESTAMP_AGG "
					+ "	WHERE "
					+ "		YPT_QC_ANALISI_ACCIAIERIA.ENTRATA = QRY_YPT_QC_ANALISI_ACCIAIERIA.ENTRATA "
					+ "		AND YPT_QC_ANALISI_ACCIAIERIA.TIMESTAMP_AGG = QRY_YPT_QC_ANALISI_ACCIAIERIA.TIMESTAMP_AGG) "
					+ "	AND YPT_QC_ANALISI_ACCIAIERIA.ELABORATO = '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println("  Dtsx-block : YPT_QC_ANALISI_ACCIAIERIA_set_Elaborato_To_X "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void riattiva_DDT_FOR_1_1(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	[PantheraTarget].[dbo].[YPT_DDT_FOR] "
					+ "SET "
					+ "	ELABORATO = '0' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT * "
					+ "  FROM [PantheraTarget].[dbo].[YPT_QC_ANALISI_ACCIAIERIA] "
					+ "	WHERE "
					+ "		[YPT_QC_ANALISI_ACCIAIERIA].ENTRATA = YPT_DDT_FOR.LOTTO "
					+ "		AND ELABORATO = '0')";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println("  Dtsx-block : riattiva_DDT_FOR_1_1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	@Override
	protected String getClassAdCollectionName() {
		return "YDtsxRmOdaDDTRu";
	}

}
