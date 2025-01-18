package it.monchieri.thip.dtsx;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import com.thera.thermfw.persist.SQLServerJTDSNoUnicodeDatabase;
import com.thera.thermfw.security.Authorizable;

import it.monchieri.thip.base.articolo.YArticolo;
import it.monchieri.thip.produzione.ordese.YOrdineEsecutivo;
import it.monchieri.thip.target.YPTExpOrdEsec;
import it.monchieri.thip.target.YPTExpOrdEsecTM;
import it.monchieri.thip.vendite.ordineVE.YOrdineVenditaRigaPrm;
import it.thera.thip.base.articolo.Articolo;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.produzione.documento.DocumentoProduzione;
import it.thera.thip.produzione.documento.DocumentoProduzioneTM;
import it.thera.thip.produzione.ordese.AttivitaEsecLottiPrd;
import it.thera.thip.produzione.ordese.AttivitaEsecutiva;
import it.thera.thip.produzione.ordese.OrdineEsecutivo;
import it.thera.thip.produzione.ordese.OrdineEsecutivoTM;

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

public class YDtsxPtExpOrdEsecRunner extends BatchRunnable implements Authorizable{

	public static final String NOME_DB_EXT = "PantheraTarget_test";
	public static final String UTENTE_DB_EXT = "Panthera";
	public static final String PWD_DB_EXT = "panthera";
	public static final String SRV_DB_EXT = "SRVDB.fmonchieri.locale";
	public static final String PORTA_DB_EXT = "1433";

	public static final java.sql.Date MIN_SQL_DATE = TimeUtils.getDate(1900, 1, 1);
	public static final java.sql.Date MAX_SQL_DATE = TimeUtils.getDate(2100, 12, 31);

	public static final String SQL_ESTRAZ_ORD_ESEC = "SELECT "
			+ "DOCPRD.DATA_REG,	OE.* "
			+ "FROM  "
			+ "				THIP.ORD_ESEC OE "
			+ "INNER JOIN THIP.ORDESE_ATV_PRD PRD   "
			+ "			ON  "
			+ "				OE.ID_AZIENDA = PRD.ID_AZIENDA "
			+ "	AND OE.ID_ANNO_ORD = PRD.ID_ANNO_ORD "
			+ "	AND OE.ID_NUMERO_ORD = PRD.ID_NUMERO_ORD "
			+ "	AND PRD.TIPO_PRODOTTO = '0' "
			+ "	AND PRD.STATO_VERSAM < '2' "
			+ "INNER JOIN THIP.ARTICOLI ARTPRD   "
			+ "			ON  "
			+ "				ARTPRD.ID_AZIENDA = PRD.ID_AZIENDA "
			+ "	AND ARTPRD.ID_ARTICOLO = PRD.R_ARTICOLO "
			+ "	AND COALESCE(ARTPRD.R_LINEA_PROD, "
			+ "	'') = 'FOR' "
			+ "INNER JOIN THIP.ORD_ESEC_ATV ATV1  "
			+ "			    ON  "
			+ "				ATV1.ID_AZIENDA = OE.ID_AZIENDA "
			+ "	AND ATV1.ID_ANNO_ORD = OE.ID_ANNO_ORD "
			+ "	AND ATV1.ID_NUMERO_ORD = OE.ID_NUMERO_ORD "
			+ "	AND ATV1.ID_RIGA_ATTIVITA = ( "
			+ "	SELECT  "
			+ "					MIN(A.ID_RIGA_ATTIVITA) "
			+ "	FROM  "
			+ "					THIP.ORD_ESEC_ATV A "
			+ "	WHERE  "
			+ "					A.ID_AZIENDA = OE.ID_AZIENDA "
			+ "		AND A.ID_ANNO_ORD = OE.ID_ANNO_ORD "
			+ "		AND A.ID_NUMERO_ORD = OE.ID_NUMERO_ORD  "
			+ "			    ) "
			+ "INNER JOIN THIP.DOC_PRD DOCPRD   "
			+ "			ON  "
			+ "				DOCPRD.ID_AZIENDA = OE.ID_AZIENDA "
			+ "	AND DOCPRD.R_ANNO_ORDINE = OE.ID_ANNO_ORD "
			+ "	AND DOCPRD.R_NUMERO_ORD = OE.ID_NUMERO_ORD "
			+ "	AND DOCPRD.R_RIGA_ATTIVITA = ATV1.ID_RIGA_ATTIVITA "
			+ "	AND DOCPRD.STATO = 'V' "
			+ "INNER JOIN THIPPERS.MQT_ORD_ESEC_PARENTCHILD_ESEPRD moepe  "
			+ "ON "
			+ "	moepe.ID_AZIENDA_PRTN = OE.ID_AZIENDA "
			+ "	AND moepe.ID_ANNO_ORD_PRTN = OE.ID_ANNO_ORD "
			+ "	AND moepe.ID_NUMERO_ORD_PRTN = OE.ID_NUMERO_ORD "
			+ "INNER JOIN ( "
			+ "	SELECT "
			+ "		* "
			+ "	FROM "
			+ "		THIPPERS.MQT_ORD_ESEC_PARENTCHILD_ESEPRD q_moepe "
			+ "	WHERE "
			+ "		GENERATION = ( "
			+ "		SELECT "
			+ "			Min(mmoepe.GENERATION) "
			+ "		FROM "
			+ "			THIPPERS.MQT_ORD_ESEC_PARENTCHILD_ESEPRD mmoepe "
			+ "		WHERE "
			+ "			(mmoepe.R_CONFIGURAZIONE_MAT_CHLD IS NOT NULL "
			+ "				OR mmoepe.R_MAGAZZINO_PRL_MAT_CHLD IN('CL')) "
			+ "				AND mmoepe.ID_AZIENDA_PRTN = q_moepe.ID_AZIENDA_PRTN "
			+ "				AND mmoepe.ID_ANNO_ORD_PRTN = q_moepe.ID_ANNO_ORD_PRTN "
			+ "				AND mmoepe.ID_NUMERO_ORD_PRTN = q_moepe.ID_NUMERO_ORD_PRTN "
			+ "				AND mmoepe.ID_LOTTO_PRTN = q_moepe.ID_LOTTO_PRTN) "
			+ "	) moepe_mat "
			+ "ON "
			+ "	moepe_mat.ID_AZIENDA_PRTN = moepe.ID_AZIENDA_PRTN "
			+ "	AND moepe_mat.ID_ANNO_ORD_PRTN = moepe.ID_ANNO_ORD_PRTN "
			+ "	AND moepe_mat.ID_NUMERO_ORD_PRTN = moepe.ID_NUMERO_ORD_PRTN "
			+ "	AND moepe_mat.ID_LOTTO_PRTN = moepe.ID_LOTTO_PRTN "
			+ "WHERE OE.ID_AZIENDA = ? AND OE.TIMESTAMP_AGG > ? ";

	protected Date iDataEstrazioneOrdEsec;

	public Date getDataEstrazioneOrdEsec() {
		return iDataEstrazioneOrdEsec;
	}

	public void setDataEstrazioneOrdEsec(Date iDataEstrazioneOrdEsec) {
		this.iDataEstrazioneOrdEsec = iDataEstrazioneOrdEsec;
	}

	@Override
	protected boolean run() {
		if(getBatchJob().getFromScheduledJob()) {
			setDataEstrazioneOrdEsec(null);
		}
		try {
			int rc = esportazioneOrdiniEsecutivi();
			if(rc < ErrorCodes.OK) {
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected int esportazioneOrdiniEsecutivi() throws SQLException {
		output.println();
		output.println(" ----------------- Estrazione "+YPTExpOrdEsecTM.TABLE_NAME+" -------------------- ");
		Timestamp timestamp = null;
		YTimestampElaborazDtsx elabAnalisiQcRm = (YTimestampElaborazDtsx) Factory.createObject(YTimestampElaborazDtsx.class);
		elabAnalisiQcRm.setKey(KeyHelper.buildObjectKey(new String[] {Azienda.getAziendaCorrente(),"YPT_EXP_ORD_ESEC"}));
		elabAnalisiQcRm.retrieve();
		if(getDataEstrazioneOrdEsec() != null)
			timestamp = TimeUtils.getTimestamp(getDataEstrazioneOrdEsec());
		else 
			timestamp = elabAnalisiQcRm.getDatiComuniEstesi().getTimestampAgg();
		output.println("  Estraggo "+OrdineEsecutivoTM.TABLE_NAME+" con TIMESTAMP_AGG > "+timestamp.toString());
		List lista = listaOrdiniEsecutiviTarget(timestamp);
		output.println("  Estratte "+lista.size()+" "+OrdineEsecutivoTM.TABLE_NAME+" ");
		int rc = 0;
		if(lista.size() > 0) {
			rc = YDtsxRmOdaDDTRunner.esportaOggettiVersoTarget(lista);
			output.println("  Esportazione "+YPTExpOrdEsecTM.TABLE_NAME+" verso Target ");
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
						Update_ESEGUITA(cnd);
						//Update_ORP_EFF_USER_PROVNUM(cnd);
						//Update_ORP_EFF_USER_IDENTCLI(cnd);
						//Update_ORP_EFF_USER_CODCLI(cnd);
						Update_ORP_EFF_COD_CF(cnd);
						UPDATE_FM(cnd);
						Update_SC(cnd);
						Aggiorna_ORP_EFF_COD_CF(cnd);
						Update_YPT_EXP_ORD_ESEC_COD_CF(cnd);
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
		}
		output.println("  Esportazione verso "+YPTExpOrdEsecTM.TABLE_NAME+" avvenuta con "+(rc > 0 ? "successo" : "errori"));
		output.println(" ----------------- Fine estrazione "+YPTExpOrdEsecTM.TABLE_NAME+" -------------------- ");
		output.println();
		return rc;
	}

	@SuppressWarnings("rawtypes")
	public List listaOrdiniEsecutiviTarget(Timestamp timestamp){
		List<YPTExpOrdEsec> ordini = new ArrayList<YPTExpOrdEsec>();

		List<YPTExpOrdEsec> ordiniEsecutivi = recuperaOrdiniEsecutiviDaEsportare(timestamp);
		for(YPTExpOrdEsec ordine : ordiniEsecutivi) {
			YOrdineEsecutivo ordEsec = ordine.getOrdineEsecutivoPanthera();
			if(ordEsec != null) {
				ordine.setDocId(ordEsec.getNumeroOrdFmt());
				ordine.setAnnoDoc(ordEsec.getIdAnnoOrdine().trim());
				String lastSix = ordEsec.getIdNumeroOrdine().substring(ordEsec.getIdNumeroOrdine().length() - 6);
				int numDoc = Integer.parseInt(lastSix);
				ordine.setNumDoc(BigDecimal.valueOf(numDoc).setScale(0,RoundingMode.DOWN));
				ordine.setSerieDoc(ordEsec.getIdNumeroOrdine().substring(0,2));
				ordine.setDataDoc(ordEsec.getDataOrdine());
				if(ordEsec.getCliente() != null) {
					ordine.setCodCf(ordEsec.getCliente().getCodSistemaInfEsterno());
				}

				ordine.setNoteConsegna("");
				ordine.setNoteInt("");
				ordine.setNoteStampa("");

				ordine.setCodCausDoc("OP");

				String codDep = ordEsec.getIdMagazzinoPrl();
				if(ordEsec.getIdMagazzinoPrl().equals("MP") || ordEsec.getIdMagazzinoPrl().equals("SB") || ordEsec.getIdMagazzinoPrl().equals("MR")) {
					codDep = "1.01";
				}
				ordine.setCodDep(codDep);

				Articolo artPrd = null;

				if(ordEsec.getOrdineVenditaRiga() != null) {
					ordine.setCodArt(ordEsec.getOrdineVenditaRiga().getArticolo().getVecchioArticolo()); //cod target

					try {
						artPrd = (Articolo) Articolo.elementWithKey(Articolo.class, 
								KeyHelper.buildObjectKey(new String[] {
										ordEsec.getIdAzienda(),ordine.getCodArt()
								}), PersistentObject.NO_LOCK);
					} catch (SQLException e) {
						e.printStackTrace(Trace.excStream);
					}
				}

				if(artPrd != null) {
					ordine.setDesProd(artPrd.getDescrizioneArticoloNLS().getDescrizione());
				}

				ordine.setQuantDaProd(ordEsec.getQtaOrdinataUMPrm().floatValue());

				String codLot = "";
				if(ordEsec.getRigaProdottoPrimario() != null && ordEsec.getRigaProdottoPrimario().getLottiProdotti().size() > 0) {
					AttivitaEsecLottiPrd lotto = (AttivitaEsecLottiPrd) ordEsec.getRigaProdottoPrimario().getLottiProdotti().get(0);
					codLot = lotto.getIdLotto();
				}
				ordine.setCodLot(codLot);

				Articolo artTecnico = null;
				Object[] dati = trovaMaterialeMinGenerationMqt(ordEsec);
				if(dati != null && dati.length == 4) {
					String idArticoloMatChld = dati[0] != null ? (String) dati[0] : "";
					String idLottoMatChld = dati[1] != null ? (String) dati[1] : "";
					BigDecimal qtaMatPrlChld = dati[2] != null ? (BigDecimal) dati[2] : BigDecimal.ZERO;
					//Double coefRisorsa = dati[3] != null ? (Double) dati[3] : 0;

					try {
						artTecnico = (Articolo) Articolo.elementWithKey(Articolo.class, 
								KeyHelper.buildObjectKey(new String[] {
										ordEsec.getIdAzienda(),idArticoloMatChld
								}), PersistentObject.NO_LOCK);
					} catch (SQLException e) {
						e.printStackTrace(Trace.excStream);
					}

					ordine.setEntrata(idLottoMatChld);
					ordine.setQuantScaricata(qtaMatPrlChld.floatValue());
				}

				if(artTecnico != null) {
					ordine.setCodVarMateriale(artTecnico.getIdArticoloTecnico());
				}

				String RIF_RIGA_ORD_CLI_4B = "";

				if(ordEsec.getOrdineVenditaRiga() != null) {
					RIF_RIGA_ORD_CLI_4B = ordEsec.getAnnoOrdineCliente() + "-ORDC-000" + ordEsec.getNumeroOrdineCliente().substring(6,10);
					RIF_RIGA_ORD_CLI_4B += "-"+String.format("%03d", ordEsec.getRigaOrdineCliente());
				}

				ordine.setRifRigaOrdCli4b(RIF_RIGA_ORD_CLI_4B);

				ordine.setUtenteIns(ordEsec.getDatiComuniEstesi().getIdUtenteCrz());
				ordine.setDataIns(ordEsec.getDatiComuniEstesi().getDataCrz());
				ordine.setUtenteMod(ordEsec.getDatiComuniEstesi().getIdUtenteAgg());

				Timestamp dataMod = null;
				if(ordEsec.getOrdineVenditaRiga() != null && (
						ordEsec.getDatiComuniEstesi().getTimestampAgg().compareTo(ordEsec.getOrdineVenditaRiga().getDatiComuniEstesi().getTimestampCrz()) < 0)) {
					dataMod = (ordEsec.getOrdineVenditaRiga().getDatiComuniEstesi().getTimestampAgg());

				}else {
					dataMod = (ordEsec.getDatiComuniEstesi().getTimestampAgg());
				}
				
				dataMod = TimeUtils.getCurrentTimestamp();

				ordine.setDataMod(dataMod);

				if(ordEsec.getOrdineVenditaRiga() != null) {
					ordine.setPesgrez(((YOrdineVenditaRigaPrm)ordEsec.getOrdineVenditaRiga()).getPesoGrezzo());
				}

				ordine.setPesmult(null);
				ordine.setChiodaia(null);
				ordine.setMandrino(null);
				ordine.setAttrezz1(null);
				ordine.setAttrezz2(null);

				ordine.setProvnum(ordEsec.getNumeroProva());
				ordine.setDataSped(null);
				ordine.setDataTt(null);

				if(ordEsec.getYmatricolaCli() != null) {
					ordine.setIdentcli(ordEsec.getYmatricolaCli());
				}else if(ordEsec.getOrdineVenditaRiga() != null) {
					ordine.setIdentcli(((YOrdineVenditaRigaPrm)ordEsec.getOrdineVenditaRiga()).getMatricolaCliente());
				}

				if(ordEsec.getOrdineVenditaRiga() != null) {
					boolean isNucleare = ((YArticolo)ordEsec.getOrdineVenditaRiga().getArticolo()).isNucleare();
					ordine.setNucleare(isNucleare ? BigDecimal.ONE : BigDecimal.ZERO);
				}

				if(ordEsec.getPartNumber() != null) {
					ordine.setCodcli(ordEsec.getPartNumber());
				}else if(ordEsec.getOrdineVenditaRiga() != null) {
					ordine.setCodcli(((YOrdineVenditaRigaPrm)ordEsec.getOrdineVenditaRiga()).getPartNumber());
				}

				ordine.setTipoOperazione("0");
				ordine.setEseguito("0");
				ordine.setNumRiga(1);

				ordine.setDocRigaId(ordEsec.getNumeroOrdFmt()+"/0001");

				String codArtComp = "";

				if(artTecnico != null) {
					if(artTecnico.getIdClasseFiscale() != null) {
						codArtComp = artTecnico.getIdClasseFiscale();
					}else{
						codArtComp = "000.0";
					}
				}

				ordine.setCodArtComp(codArtComp);

				ordine.setQuantMaterozza(0.f);

				//Date dataMovimento = ordEsec.getDateProgrammate().getStartDate();
				//ordine.setDataMovimento(dataMovimento);

				ordini.add(ordine);
			}
		}
		return ordini;
	}

	public Object[] trovaMaterialeMinGenerationMqt(OrdineEsecutivo ordEsec) {
		Object[] ret = new Object[4];
		ResultSet rs = null;
		String stmt = "SELECT "
				+ "	moepe_mat.MAT_QTA_PRL_UM_PRM_CHLD * moepe_mat.COEF_RISORSA AS MAT_QTA_PRL_UM_PRM_CHLD, "
				+ "	COALESCE(moepe_mat.ID_LOTTO_MAT_CHLD, "
				+ "	'' ) AS ID_LOTTO_MAT_CHLD, "
				+ "	moepe_mat.R_ARTICOLO_MAT_CHLD , "
				+ "	moepe_mat.COEF_RISORSA  "
				+ "FROM "
				+ "	THIPPERS.MQT_ORD_ESEC_PARENTCHILD_ESEPRD moepe "
				+ "INNER JOIN ( "
				+ "	SELECT "
				+ "		* "
				+ "	FROM "
				+ "		THIPPERS.MQT_ORD_ESEC_PARENTCHILD_ESEPRD q_moepe "
				+ "	WHERE "
				+ "		GENERATION = ( "
				+ "		SELECT "
				+ "			Min(mmoepe.GENERATION) "
				+ "		FROM "
				+ "			THIPPERS.MQT_ORD_ESEC_PARENTCHILD_ESEPRD mmoepe "
				+ "		WHERE "
				+ "			(mmoepe.R_CONFIGURAZIONE_MAT_CHLD IS NOT NULL "
				+ "				OR mmoepe.R_MAGAZZINO_PRL_MAT_CHLD IN('CL')) "
				+ "				AND mmoepe.ID_AZIENDA_PRTN = q_moepe.ID_AZIENDA_PRTN "
				+ "				AND mmoepe.ID_ANNO_ORD_PRTN = q_moepe.ID_ANNO_ORD_PRTN "
				+ "				AND mmoepe.ID_NUMERO_ORD_PRTN = q_moepe.ID_NUMERO_ORD_PRTN "
				+ "				AND mmoepe.ID_LOTTO_PRTN = q_moepe.ID_LOTTO_PRTN) "
				+ "	) moepe_mat "
				+ "ON "
				+ "	moepe_mat.ID_AZIENDA_PRTN = moepe.ID_AZIENDA_PRTN "
				+ "	AND moepe_mat.ID_ANNO_ORD_PRTN = moepe.ID_ANNO_ORD_PRTN "
				+ "	AND moepe_mat.ID_NUMERO_ORD_PRTN = moepe.ID_NUMERO_ORD_PRTN "
				+ "	AND moepe_mat.ID_LOTTO_PRTN = moepe.ID_LOTTO_PRTN "
				+ "WHERE moepe.ID_AZIENDA_PRTN = '"+ordEsec.getIdAzienda()+"' "
				+ "AND moepe.ID_ANNO_ORD_PRTN = '"+ordEsec.getIdAnnoOrdine()+"' "
				+ "AND moepe.ID_NUMERO_ORD_PRTN = '"+ordEsec.getIdNumeroOrdine()+"' ";
		CachedStatement cs = null;
		try {
			cs = new CachedStatement(stmt);
			rs = cs.executeQuery();
			if(rs.next()) {
				ret[0] = rs.getString("R_ARTICOLO_MAT_CHLD");
				ret[1] = rs.getString("ID_LOTTO_MAT_CHLD");
				ret[2] = rs.getBigDecimal("MAT_QTA_PRL_UM_PRM_CHLD");
				ret[3] = rs.getDouble("COEF_RISORSA");
			}
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(cs != null) {
					cs.free();
				}
			}catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		return ret;
	}

	protected DocumentoProduzione trovaDocumentoProduzioneDaAttivita(AttivitaEsecutiva atv) {
		DocumentoProduzione docPrd = null;
		String stmt = "SELECT * FROM THIP.DOC_PRD ";
		stmt += " ID_AZIENDA = '"+atv.getIdAzienda()+"' AND R_ANNO_ORDINE = '"+atv.getIdAnnoOrdine()+"' AND R_NUMERO_ORD = '"+atv.getIdNumeroOrdine()+"' AND R_RIGA_ATTIVITA = '"+atv.getIdRigaAttivita()+"' ";
		ResultSet rs = null;
		CachedStatement cs = null;
		try {
			cs = new CachedStatement(stmt);
			rs = cs.executeQuery();
			if(rs.next()) {
				docPrd = (DocumentoProduzione) DocumentoProduzione.elementWithKey(DocumentoProduzione.class, KeyHelper.buildObjectKey(new String[] {
						rs.getString(DocumentoProduzioneTM.ID_AZIENDA),
						rs.getString(DocumentoProduzioneTM.ID_ANNO_DOC),
						rs.getString(DocumentoProduzioneTM.ID_NUMERO_DOC)
				}), PersistentObject.NO_LOCK);
			}
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(cs != null) {
					cs.free();
				}
			}catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		return docPrd;
	}

	public List<YPTExpOrdEsec> recuperaOrdiniEsecutiviDaEsportare(Timestamp timestamp){
		List<YPTExpOrdEsec> ordini = new ArrayList<YPTExpOrdEsec>();
		PreparedStatement ps = null;
		try {
			ResultSet rs = null;
			ps = ConnectionManager.getCurrentConnection().prepareStatement(SQL_ESTRAZ_ORD_ESEC);
			Database db = ConnectionManager.getCurrentDatabase();
			db.setString(ps, 1, Azienda.getAziendaCorrente());
			db.setString(ps, 2, YDtsxRmOdaDDTRunner.getFormattedTimestampForQuery(timestamp));
			rs = ps.executeQuery();
			while(rs.next()) {
				YPTExpOrdEsec ordine = (YPTExpOrdEsec) Factory.createObject(YPTExpOrdEsec.class);
				ordine.iChiaveOrdineEsecutivoPanthera = (KeyHelper.buildObjectKey(new String[] {
						rs.getString(OrdineEsecutivoTM.ID_AZIENDA),
						rs.getString(OrdineEsecutivoTM.ID_ANNO_ORD),
						rs.getString(OrdineEsecutivoTM.ID_NUMERO_ORD)
				}));
				ordine.setDataMovimento(rs.getDate(DocumentoProduzioneTM.DATA_REG));
				ordini.add(ordine);
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
		return ordini;
	}

	protected void Update_ESEGUITA(ConnectionDescriptor cnd) {
		try {
			String Update_ESEGUITA = "UPDATE [MAME].[YPT_EXP_ORD_ESEC] "
					+ "SET YPT_EXP_ORD_ESEC.eseguito = 'X' "
					+ "WHERE EXISTS (SELECT 1 as DATA_MOD_MAX "
					+ "  FROM [MAME].[YPT_EXP_ORD_ESEC] as YPT_EXP_ORD_ESEC_SQL "
					+ "  where YPT_EXP_ORD_ESEC.doc_id = YPT_EXP_ORD_ESEC_SQL.DOC_ID "
					+ "  and YPT_EXP_ORD_ESEC.eseguito = '0' "
					+ "  group by DOC_ID "
					+ "  having COUNT(doc_id) > 1 "
					+ "  and YPT_EXP_ORD_ESEC.data_mod < MAX(DATA_MOD)) ";
			int rc = eseguiUpdateSuDbExt(Update_ESEGUITA,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Update-ESEGUITA "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void Update_ORP_EFF_USER_PROVNUM(ConnectionDescriptor cnd) {
		try {
			String Update_ORP_EFF_USER_PROVNUM = "UPDATE Target.dbo.ORP_EFF_USER  "
					+ "SET ORP_EFF_USER.PROVNUM = ( "
					+ "SELECT ORD_ESEC_MAME.NUMERO_PROVA "
					+ "FROM openquery(panth01,'SELECT * FROM MAME.ORD_ESEC_MAME WHERE ID_AZIENDA=''050''') ORD_ESEC_MAME  "
					+ "WHERE RTRIM( ORD_ESEC_MAME.ID_ANNO_ORD )+ CASE LEFT(ORD_ESEC_MAME.ID_NUMERO_ORD, 2) "
					+ "		WHEN 'P1' THEN '-ORP-' "
					+ "		WHEN 'P2' THEN '-ORPFM-' "
					+ "		WHEN 'P3' THEN '-ORPSS-' "
					+ "		WHEN 'P4' THEN '' "
					+ "		WHEN 'P5' THEN '' "
					+ "	END + RIGHT('0000000' + RIGHT(RTRIM( ORD_ESEC_MAME.ID_NUMERO_ORD ),	6),	7)= ORP_EFF_USER.DOC_ID "
					+ "AND	ORP_EFF_USER.PROVNUM <> ORD_ESEC_MAME.NUMERO_PROVA) "
					+ "WHERE EXISTS ( "
					+ "SELECT ORD_ESEC_MAME.NUMERO_PROVA "
					+ "FROM openquery(panth01,'SELECT * FROM MAME.ORD_ESEC_MAME WHERE ID_AZIENDA=''050''') ORD_ESEC_MAME  "
					+ "WHERE RTRIM( ORD_ESEC_MAME.ID_ANNO_ORD )+ CASE LEFT(ORD_ESEC_MAME.ID_NUMERO_ORD, 2) "
					+ "		WHEN 'P1' THEN '-ORP-' "
					+ "		WHEN 'P2' THEN '-ORPFM-' "
					+ "		WHEN 'P3' THEN '-ORPSS-' "
					+ "		WHEN 'P4' THEN '' "
					+ "		WHEN 'P5' THEN '' "
					+ "	END + RIGHT('0000000' + RIGHT(RTRIM( ORD_ESEC_MAME.ID_NUMERO_ORD ),	6),	7)= ORP_EFF_USER.DOC_ID "
					+ "AND	ORP_EFF_USER.PROVNUM <> ORD_ESEC_MAME.NUMERO_PROVA)";
			int rc = eseguiUpdateSuDbExt(Update_ORP_EFF_USER_PROVNUM,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Update_ORP_EFF_USER_PROVNUM "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void Update_ORP_EFF_USER_IDENTCLI(ConnectionDescriptor cnd) {
		try {
			String Update_ORP_EFF_USER_IDENTCLI = "UPDATE Target.dbo.ORP_EFF_USER  "
					+ "SET ORP_EFF_USER.IDENTCLI = ( "
					+ "SELECT ORD_ESEC_MAME.YMATRICOLA_CLI "
					+ "FROM openquery(panth01,'SELECT * FROM MAME.ORD_ESEC_MAME WHERE ID_AZIENDA=''050''') ORD_ESEC_MAME  "
					+ "WHERE RTRIM( ORD_ESEC_MAME.ID_ANNO_ORD )+ CASE LEFT(ORD_ESEC_MAME.ID_NUMERO_ORD, 2) "
					+ "		WHEN 'P1' THEN '-ORP-' "
					+ "		WHEN 'P2' THEN '-ORPFM-' "
					+ "		WHEN 'P3' THEN '-ORPSS-' "
					+ "		WHEN 'P4' THEN '' "
					+ "		WHEN 'P5' THEN '' "
					+ "	END + RIGHT('0000000' + RIGHT(RTRIM( ORD_ESEC_MAME.ID_NUMERO_ORD ),	6),	7)= ORP_EFF_USER.DOC_ID "
					+ "AND	ORP_EFF_USER.IDENTCLI <> ORD_ESEC_MAME.YMATRICOLA_CLI) "
					+ "WHERE EXISTS ( "
					+ "SELECT ORD_ESEC_MAME.YMATRICOLA_CLI "
					+ "FROM openquery(panth01,'SELECT * FROM MAME.ORD_ESEC_MAME WHERE ID_AZIENDA=''050''') ORD_ESEC_MAME  "
					+ "WHERE RTRIM( ORD_ESEC_MAME.ID_ANNO_ORD )+ CASE LEFT(ORD_ESEC_MAME.ID_NUMERO_ORD, 2) "
					+ "		WHEN 'P1' THEN '-ORP-' "
					+ "		WHEN 'P2' THEN '-ORPFM-' "
					+ "		WHEN 'P3' THEN '-ORPSS-' "
					+ "		WHEN 'P4' THEN '' "
					+ "		WHEN 'P5' THEN '' "
					+ "	END + RIGHT('0000000' + RIGHT(RTRIM( ORD_ESEC_MAME.ID_NUMERO_ORD ),	6),	7)= ORP_EFF_USER.DOC_ID "
					+ "AND	ORP_EFF_USER.IDENTCLI <> ORD_ESEC_MAME.YMATRICOLA_CLI) ";
			int rc = eseguiUpdateSuDbExt(Update_ORP_EFF_USER_IDENTCLI,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Update_ORP_EFF_USER_IDENTCLI "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void Update_ORP_EFF_USER_CODCLI(ConnectionDescriptor cnd) {
		try {
			String Update_ORP_EFF_USER_CODCLI = "UPDATE Target.dbo.ORP_EFF_USER  "
					+ "SET ORP_EFF_USER.CODCLI = ( "
					+ "SELECT LEFT(ORD_ESEC_MAME.PART_NUMBER,40) AS PART_NUMBER "
					+ "FROM openquery(panth01,'SELECT * FROM MAME.ORD_ESEC_MAME WHERE ID_AZIENDA=''050''') ORD_ESEC_MAME  "
					+ "WHERE RTRIM( ORD_ESEC_MAME.ID_ANNO_ORD )+ CASE LEFT(ORD_ESEC_MAME.ID_NUMERO_ORD, 2) "
					+ "		WHEN 'P1' THEN '-ORP-' "
					+ "		WHEN 'P2' THEN '-ORPFM-' "
					+ "		WHEN 'P3' THEN '-ORPSS-' "
					+ "		WHEN 'P4' THEN '' "
					+ "		WHEN 'P5' THEN '' "
					+ "	END + RIGHT('0000000' + RIGHT(RTRIM( ORD_ESEC_MAME.ID_NUMERO_ORD ),	6),	7)= ORP_EFF_USER.DOC_ID "
					+ "AND	ORP_EFF_USER.CODCLI <> ORD_ESEC_MAME.PART_NUMBER) "
					+ "WHERE EXISTS ( "
					+ "SELECT ORD_ESEC_MAME.PART_NUMBER "
					+ "FROM openquery(panth01,'SELECT * FROM MAME.ORD_ESEC_MAME WHERE ID_AZIENDA=''050''') ORD_ESEC_MAME  "
					+ "WHERE RTRIM( ORD_ESEC_MAME.ID_ANNO_ORD )+ CASE LEFT(ORD_ESEC_MAME.ID_NUMERO_ORD, 2) "
					+ "		WHEN 'P1' THEN '-ORP-' "
					+ "		WHEN 'P2' THEN '-ORPFM-' "
					+ "		WHEN 'P3' THEN '-ORPSS-' "
					+ "		WHEN 'P4' THEN '' "
					+ "		WHEN 'P5' THEN '' "
					+ "	END + RIGHT('0000000' + RIGHT(RTRIM( ORD_ESEC_MAME.ID_NUMERO_ORD ),	6),	7)= ORP_EFF_USER.DOC_ID "
					+ "AND	ORP_EFF_USER.CODCLI <> ORD_ESEC_MAME.PART_NUMBER) ";
			int rc = eseguiUpdateSuDbExt(Update_ORP_EFF_USER_CODCLI,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Update_ORP_EFF_USER_CODCLI "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void Update_ORP_EFF_COD_CF(ConnectionDescriptor cnd) {
		try {
			String Update_ORP_EFF_COD_CF = "UPDATE Target.dbo.ORP_EFF "
					+ "SET ORP_EFF.COD_CF = (SELECT ORD_ESEC.R_CLIENTE "
					+ "FROM openquery(panth01,'SELECT "
					+ "	ORD_ESEC.ID_ANNO_ORD, "
					+ "	ORD_ESEC.ID_NUMERO_ORD, "
					+ "	BBCLIPT.CLICFEST AS R_CLIENTE "
					+ "FROM "
					+ "	THIP.ORD_ESEC ORD_ESEC "
					+ "INNER JOIN FINANCE.BBCLIPT BBCLIPT ON "
					+ "	BBCLIPT.T01CD = ORD_ESEC.ID_AZIENDA "
					+ "	AND BBCLIPT.CLICD = ORD_ESEC.R_CLIENTE "
					+ "WHERE "
					+ "	ORD_ESEC.ID_AZIENDA = ''001'' "
					+ "	AND BBCLIPT.CLICFEST IS NOT NULL') ORD_ESEC  "
					+ "WHERE RTRIM(ORD_ESEC.ID_ANNO_ORD )+ CASE LEFT(ORD_ESEC.ID_NUMERO_ORD, 2) "
					+ "		WHEN 'P1' THEN '-ORP-' "
					+ "		WHEN 'P2' THEN '' "
					+ "		WHEN 'P3' THEN '-ORPSS-' "
					+ "		WHEN 'P4' THEN '' "
					+ "		WHEN 'P5' THEN '' "
					+ "	END + RIGHT('0000000' + RIGHT(RTRIM( ORD_ESEC.ID_NUMERO_ORD ),	6),	7)= ORP_EFF.DOC_ID "
					+ "AND	ORP_EFF.COD_CF <> ORD_ESEC.R_CLIENTE "
					+ "AND ANNO_DOC > 2017) "
					+ "WHERE EXISTS (SELECT ORD_ESEC.R_CLIENTE "
					+ "FROM openquery(panth01,'SELECT "
					+ "	ORD_ESEC.ID_ANNO_ORD, "
					+ "	ORD_ESEC.ID_NUMERO_ORD, "
					+ "	BBCLIPT.CLICFEST AS R_CLIENTE "
					+ "FROM "
					+ "	THIP.ORD_ESEC ORD_ESEC "
					+ "INNER JOIN FINANCE.BBCLIPT BBCLIPT ON "
					+ "	BBCLIPT.T01CD = ORD_ESEC.ID_AZIENDA "
					+ "	AND BBCLIPT.CLICD = ORD_ESEC.R_CLIENTE "
					+ "WHERE "
					+ "	ORD_ESEC.ID_AZIENDA = ''050'' "
					+ "	AND BBCLIPT.CLICFEST IS NOT NULL') ORD_ESEC  "
					+ "WHERE RTRIM(ORD_ESEC.ID_ANNO_ORD )+ CASE LEFT(ORD_ESEC.ID_NUMERO_ORD, 2) "
					+ "		WHEN 'P1' THEN '-ORP-' "
					+ "		WHEN 'P2' THEN '' "
					+ "		WHEN 'P3' THEN '-ORPSS-' "
					+ "		WHEN 'P4' THEN '' "
					+ "		WHEN 'P5' THEN '' "
					+ "	END + RIGHT('0000000' + RIGHT(RTRIM( ORD_ESEC.ID_NUMERO_ORD ),	6),	7)= ORP_EFF.DOC_ID "
					+ "AND	ORP_EFF.COD_CF <> ORD_ESEC.R_CLIENTE "
					+ "AND ANNO_DOC > 2017) ";
			int rc = eseguiUpdateSuDbExt(Update_ORP_EFF_COD_CF,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Update_ORP_EFF_COD_CF "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void Update_SC(ConnectionDescriptor cnd) {
		try {
			String Update_SC = "UPDATE "
					+ "	PANTHERATARGET.MAME.YPT_EXP_ORD_ESEC   "
					+ "	SET "
					+ "		YPT_EXP_ORD_ESEC.ESEGUITO = '0', "
					+ "		YPT_EXP_ORD_ESEC.COD_LOT = ( "
					+ "		SELECT "
					+ "			left(YPT_EXP_ORD_ESEC_QRY.COD_LOT, "
					+ "			CHARINDEX('[', "
					+ "			YPT_EXP_ORD_ESEC_QRY.COD_LOT)-1) "
					+ "		FROM "
					+ "			PANTHERATARGET.MAME.YPT_EXP_ORD_ESEC YPT_EXP_ORD_ESEC_QRY "
					+ "		INNER JOIN ( "
					+ "			SELECT "
					+ "				DOC_ID, "
					+ "				MAX(DATA_MOD) AS DATA_MOD "
					+ "			FROM "
					+ "				PANTHERATARGET.MAME.YPT_EXP_ORD_ESEC "
					+ "			GROUP BY "
					+ "				DOC_ID) YPT_EXP_ORD_ESEC_MAX ON "
					+ "			YPT_EXP_ORD_ESEC_QRY.DOC_ID = YPT_EXP_ORD_ESEC_MAX.DOC_ID "
					+ "			AND YPT_EXP_ORD_ESEC_QRY.DATA_MOD = YPT_EXP_ORD_ESEC_MAX.DATA_MOD "
					+ "		WHERE "
					+ "			YPT_EXP_ORD_ESEC_QRY.COD_LOT LIKE '%[[]%]' "
					+ "			AND YPT_EXP_ORD_ESEC_QRY.DOC_ID = YPT_EXP_ORD_ESEC.DOC_ID "
					+ "			AND YPT_EXP_ORD_ESEC_QRY.DATA_MOD = YPT_EXP_ORD_ESEC.DATA_MOD) "
					+ "	WHERE "
					+ "		EXISTS( "
					+ "		SELECT "
					+ "			YPT_EXP_ORD_ESEC_QRY.DOC_ID, "
					+ "			YPT_EXP_ORD_ESEC_QRY.DATA_MOD "
					+ "		FROM "
					+ "			PANTHERATARGET.MAME.YPT_EXP_ORD_ESEC YPT_EXP_ORD_ESEC_QRY "
					+ "		INNER JOIN ( "
					+ "			SELECT "
					+ "				DOC_ID, "
					+ "				MAX(DATA_MOD) AS DATA_MOD "
					+ "			FROM "
					+ "				PANTHERATARGET.MAME.YPT_EXP_ORD_ESEC "
					+ "			GROUP BY "
					+ "				DOC_ID) YPT_EXP_ORD_ESEC_MAX ON "
					+ "			YPT_EXP_ORD_ESEC_QRY.DOC_ID = YPT_EXP_ORD_ESEC_MAX.DOC_ID "
					+ "			AND YPT_EXP_ORD_ESEC_QRY.DATA_MOD = YPT_EXP_ORD_ESEC_MAX.DATA_MOD "
					+ "		WHERE "
					+ "			YPT_EXP_ORD_ESEC_QRY.COD_LOT LIKE '%[[]%]' "
					+ "			AND YPT_EXP_ORD_ESEC_QRY.DOC_ID = YPT_EXP_ORD_ESEC.DOC_ID "
					+ "			AND YPT_EXP_ORD_ESEC_QRY.DATA_MOD = YPT_EXP_ORD_ESEC.DATA_MOD)";
			int rc = eseguiUpdateSuDbExt(Update_SC,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Update_SC "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void Aggiorna_ORP_EFF_COD_CF(ConnectionDescriptor cnd) {
		try {
			String Aggiorna_ORP_EFF_COD_CF = "update dbo.ORP_EFF  "
					+ "set COD_CF = (select oc.COD_CF  "
					+ "from dbo.ORD_CLI_RIGHE ocr "
					+ "inner join dbo.ORD_CLI oc "
					+ "on oc.DOC_ID = ocr.DOC_ID "
					+ "WHERE ORP_EFF.RIF_RIGA_ORD_CLI_4B = ocr.DOC_RIGA_ID "
					+ "and ORP_EFF.COD_CF <> oc.COD_CF) "
					+ "where exists(select oc.COD_CF  "
					+ "from dbo.ORD_CLI_RIGHE ocr "
					+ "inner join dbo.ORD_CLI oc "
					+ "on oc.DOC_ID = ocr.DOC_ID "
					+ "WHERE ORP_EFF.RIF_RIGA_ORD_CLI_4B = ocr.DOC_RIGA_ID "
					+ "and ORP_EFF.COD_CF <> oc.COD_CF) "
					+ "AND ORP_EFF.COD_CF in ('00007797')";
			int rc = eseguiUpdateSuDbExt(Aggiorna_ORP_EFF_COD_CF,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Aggiorna_ORP_EFF_COD_CF "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void UPDATE_FM(ConnectionDescriptor cnd) {
		try {
			String Aggiorna_ORP_EFF_COD_CF = "UPDATE [MAME].[YPT_EXP_ORD_ESEC] "
					+ "SET COD_ART = 'DIPICO', "
					+ "RIF_RIGA_ORD_CLI_4B = '2018-ORDC-0000000-001' "
					+ "WHERE COD_ART is null "
					+ "and RIF_RIGA_ORD_CLI_4B is null "
					+ "and COD_LOT LIKE '%FM%' ";
			int rc = eseguiUpdateSuDbExt(Aggiorna_ORP_EFF_COD_CF,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : UPDATE_FM "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void Update_YPT_EXP_ORD_ESEC_COD_CF(ConnectionDescriptor cnd) {
		try {
			String Update_YPT_EXP_ORD_ESEC_COD_CF = "UPDATE [MAME].[YPT_EXP_ORD_ESEC] "
					+ "SET COD_CF=(select oc.COD_CF  "
					+ "from dbo.ORD_CLI_RIGHE ocr "
					+ "inner join dbo.ORD_CLI oc "
					+ "on oc.DOC_ID = ocr.DOC_ID "
					+ "WHERE [MAME].[YPT_EXP_ORD_ESEC].RIF_RIGA_ORD_CLI_4B = ocr.DOC_RIGA_ID "
					+ "and [MAME].[YPT_EXP_ORD_ESEC].COD_CF <> oc.COD_CF) "
					+ "where exists(select oc.COD_CF  "
					+ "from dbo.ORD_CLI_RIGHE ocr "
					+ "inner join dbo.ORD_CLI oc "
					+ "on oc.DOC_ID = ocr.DOC_ID "
					+ "WHERE [MAME].[YPT_EXP_ORD_ESEC].RIF_RIGA_ORD_CLI_4B = ocr.DOC_RIGA_ID "
					+ "and [MAME].[YPT_EXP_ORD_ESEC].COD_CF <> oc.COD_CF) "
					+ "AND [MAME].[YPT_EXP_ORD_ESEC].COD_CF in ('00007797') "
					+ "and ESEGUITO = '0'";
			int rc = eseguiUpdateSuDbExt(Update_YPT_EXP_ORD_ESEC_COD_CF,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Update_YPT_EXP_ORD_ESEC_COD_CF "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected static int eseguiUpdateSuDbExt(String stmt, ConnectionDescriptor cnd) throws SQLException {
		int res = 0;
		PreparedStatement ps = cnd.getConnection().prepareStatement(stmt);
		res = ps.executeUpdate();
		return res;
	}

	protected static ConnectionDescriptor externalConnectionDescriptor(String nomedb,String utentedb,String pwddb,String serverdb,String portadb) {
		ConnectionDescriptor cnd = null;
		cnd = new ConnectionDescriptor(nomedb,utentedb, pwddb,new SQLServerJTDSNoUnicodeDatabase(serverdb,portadb));
		return cnd;
	}

	@Override
	protected String getClassAdCollectionName() {
		return "YDtsxPtExpOrdEs";
	}

}
