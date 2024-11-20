package it.monchieri.thip.dtsx;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.batch.BatchRunnable;
import com.thera.thermfw.persist.ConnectionDescriptor;
import com.thera.thermfw.persist.ConnectionManager;

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

public class YDtsxRmOdaDDTRunner extends BatchRunnable {

	@Override
	protected boolean run() {
		ConnectionDescriptor cnd = YDtsxPtExpOrdEsecRunner.externalConnectionDescriptor(YDtsxPtExpOrdEsecRunner.NOME_DB_EXT, YDtsxPtExpOrdEsecRunner.UTENTE_DB_EXT, YDtsxPtExpOrdEsecRunner.PWD_DB_EXT, YDtsxPtExpOrdEsecRunner.SRV_DB_EXT, YDtsxPtExpOrdEsecRunner.PORTA_DB_EXT);
		try {
			if(cnd != null) {
				ConnectionManager.pushConnection(cnd);

			}else {
				output.print("** Impossibile creare il descrittore di connesione per Target **");
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		} finally {
			if (cnd != null) {
				ConnectionManager.popConnection(cnd);
			}
		}
		return false;
	}
	
	protected void passo0(ConnectionDescriptor cnd) {
		try {
			String insert = "INSERT INTO [PantheraTarget].[dbo].[YPT_QC_ANALISI_RM] ( "
					+ "    ALLIAS_ACCIAIO, "
					+ "    ACCIAIO, "
					+ "    TP_FUS_ACC, "
					+ "    DOC_REV_APPR, "
					+ "    DOC_REV_PREP, "
					+ "    ID, "
					+ "    NOTE, "
					+ "    NOTE_2, "
					+ "    NOTE_3, "
					+ "    FLAG_PR_SPE, "
					+ "    DOC_REV, "
					+ "    RM_NUM, "
					+ "    STD_ACC, "
					+ "    STD_ACC_2, "
					+ "    STD_ACC_3, "
					+ "    STD_ACC_4, "
					+ "    STD_ACC_5, "
					+ "    STD_ACC_6, "
					+ "    STD_ACC_DES, "
					+ "    STD_ACC_DES_2, "
					+ "    STD_ACC_DES_3, "
					+ "    STD_ACC_DES_4, "
					+ "    STD_ACC_DES_5, "
					+ "    STD_ACC_DES_6, "
					+ "    TAS_INCL_A, "
					+ "    TAS_INCL_B, "
					+ "    TAS_INCL_C, "
					+ "    TAS_INCL_D, "
					+ "    TAS_INCL_A_G, "
					+ "    TAS_INCL_B_G, "
					+ "    TAS_INCL_C_G, "
					+ "    TAS_INCL_D_G, "
					+ "    TAS_INCL_NOTE_1, "
					+ "    TAS_INCL_NOTE_2, "
					+ "    TAS_INCL_NOTE_3, "
					+ "    DOC_RICH, "
					+ "    TP_AFF_ACC, "
					+ "    TP_AFF_ACC_2, "
					+ "    TP_AFF_ACC_3, "
					+ "    TP_AFF_ACC_4, "
					+ "    TP_AFF_ACC_5, "
					+ "    TP_AFF_ACC_6, "
					+ "    TP_AFF_GR_1, "
					+ "    RM_NUM_REV, "
					+ "    RM_OR_N, "
					+ "    DES_VALORE_PRODOTTO, "
					+ "    DOC_DESC_REV, "
					+ "    DOC_DATA_REV, "
					+ "    TAS_INCL_ACT, "
					+ "    TAS_INCL_ACT_G, "
					+ "    TP_AFF_GR, "
					+ "    FLAG_RMC, "
					+ "    TIMESTAMP_AGG, "
					+ "    AL_FLAG_INF, "
					+ "    ALSOL_FLAG_INF, "
					+ "    AL_MIN, "
					+ "    AL_MAX, "
					+ "    ALSOL_INF, "
					+ "    ALSOL_MIN, "
					+ "    ALSOL_MAX, "
					+ "    AS_FLAG_INF, "
					+ "    AS_MIN, "
					+ "    AS_MAX, "
					+ "    B_FLAG_INF, "
					+ "    B_MIN, "
					+ "    B_MAX, "
					+ "    BI_FLAG_INF, "
					+ "    BI_MIN, "
					+ "    BI_MAX, "
					+ "    C_FLAG_INF, "
					+ "    C_MIN, "
					+ "    C_MAX, "
					+ "    C_N_FLAG_INF, "
					+ "    C_N_MIN, "
					+ "    C_N_MAX, "
					+ "    CA_FLAG_INF, "
					+ "    CA_MIN, "
					+ "    CA_MAX, "
					+ "    CB_FLAG_INF, "
					+ "    CB_MIN, "
					+ "    CB_MAX, "
					+ "    CE_FLAG_INF, "
					+ "    CE_MIN, "
					+ "    CE_MAX, "
					+ "    CO_FLAG_INF, "
					+ "    CO_MIN, "
					+ "    CO_MAX, "
					+ "    CR_FLAG_INF, "
					+ "    CR_MIN, "
					+ "    CR_MAX, "
					+ "    CR_EQ_FLAG_INF, "
					+ "    CR_EQ_MIN, "
					+ "    CR_EQ_MAX, "
					+ "    CU_FLAG_INF, "
					+ "    CU_MIN, "
					+ "    CU_MAX, "
					+ "    FE_FLAG_INF, "
					+ "    FE_MIN, "
					+ "    FE_MAX, "
					+ "    H_FLAG_INF, "
					+ "    H_MIN, "
					+ "    H_MAX, "
					+ "    H_PPM_O_PERC, "
					+ "    JF_FLAG_INF, "
					+ "    JF_MIN, "
					+ "    JF_MAX, "
					+ "    MN_FLAG_INF, "
					+ "    MN_MIN, "
					+ "    MN_MAX, "
					+ "    MO_FLAG_INF, "
					+ "    MO_MIN, "
					+ "    MO_MAX, "
					+ "    N_FLAG_INF, "
					+ "    N_MIN, "
					+ "    N_MAX, "
					+ "    N_PPM_O_PERC, "
					+ "    NB_FLAG_INF, "
					+ "    NB_MIN, "
					+ "    NB_MAX, "
					+ "    NB_TA_FLAG_INF, "
					+ "    NB_TA_MIN, "
					+ "    NB_TA_MAX, "
					+ "    NI_FLAG_INF, "
					+ "    NI_MIN, "
					+ "    NI_MAX, "
					+ "    O_FLAG_INF, "
					+ "    O_MIN, "
					+ "    O_MAX, "
					+ "    O_PPM_O_PERC, "
					+ "    P_FLAG_INF, "
					+ "    P_MIN, "
					+ "    P_MAX, "
					+ "    PB_FLAG_INF, "
					+ "    PB_MIN, "
					+ "    PB_MAX, "
					+ "    PCM_FLAG_INF, "
					+ "    PCM_MIN, "
					+ "    PCM_MAX, "
					+ "    PRE_FLAG_INF, "
					+ "    PRE_MIN, "
					+ "    PRE_MAX, "
					+ "    S_FLAG_INF, "
					+ "    S_MIN, "
					+ "    S_MAX, "
					+ "    SB_FLAG_INF, "
					+ "    SB_MIN, "
					+ "    SB_MAX, "
					+ "    SI_FLAG_INF, "
					+ "    SI_MIN, "
					+ "    SI_MAX, "
					+ "    SN_FLAG_INF, "
					+ "    SN_MIN, "
					+ "    SN_MAX, "
					+ "    TA_FLAG_INF, "
					+ "    TA_MIN, "
					+ "    TA_MAX, "
					+ "    TI_FLAG_INF, "
					+ "    TI_MIN, "
					+ "    TI_MAX, "
					+ "    V_FLAG_INF, "
					+ "    V_MIN, "
					+ "    V_MAX, "
					+ "    W_FLAG_INF, "
					+ "    W_MIN, "
					+ "    W_MAX, "
					+ "    XF_FLAG_INF, "
					+ "    XF_MIN, "
					+ "    XF_MAX, "
					+ "    Y_FLAG_INF, "
					+ "    Y_MIN, "
					+ "    Y_MAX, "
					+ "    ZR_FLAG_INF, "
					+ "    ZR_MIN, "
					+ "    ZR_MAX, "
					+ "    AC_FORMULA_1, "
					+ "    AC_FORMULA_1_MAX, "
					+ "    AC_FORMULA_1_MIN, "
					+ "    AC_FORMULA_2, "
					+ "    AC_FORMULA_2_MAX, "
					+ "    AC_FORMULA_2_MIN, "
					+ "    AC_FORMULA_3, "
					+ "    AC_FORMULA_3_MAX, "
					+ "    AC_FORMULA_3_MIN, "
					+ "    AC_FORMULA_4, "
					+ "    AC_FORMULA_4_MAX, "
					+ "    AC_FORMULA_4_MIN, "
					+ "    AC_FORMULA_5, "
					+ "    AC_FORMULA_5_MAX, "
					+ "    AC_FORMULA_5_MIN "
					+ ") "
					+ "SELECT  "
					+ "    ALLIAS_ACCIAIO, "
					+ "    ACCIAIO, "
					+ "    TP_FUS_ACC, "
					+ "    DOC_REV_APPR, "
					+ "    DOC_REV_PREP, "
					+ "    ID, "
					+ "    NOTE, "
					+ "    NOTE_2, "
					+ "    NOTE_3, "
					+ "    FLAG_PR_SPE, "
					+ "    DOC_REV, "
					+ "    RM_NUM, "
					+ "    STD_ACC, "
					+ "    STD_ACC_2, "
					+ "    STD_ACC_3, "
					+ "    STD_ACC_4, "
					+ "    STD_ACC_5, "
					+ "    STD_ACC_6, "
					+ "    STD_ACC_DES, "
					+ "    STD_ACC_DES_2, "
					+ "    STD_ACC_DES_3, "
					+ "    STD_ACC_DES_4, "
					+ "    STD_ACC_DES_5, "
					+ "    STD_ACC_DES_6, "
					+ "    TAS_INCL_A, "
					+ "    TAS_INCL_B, "
					+ "    TAS_INCL_C, "
					+ "    TAS_INCL_D, "
					+ "    TAS_INCL_A_G, "
					+ "    TAS_INCL_B_G, "
					+ "    TAS_INCL_C_G, "
					+ "    TAS_INCL_D_G, "
					+ "    TAS_INCL_NOTE_1, "
					+ "    TAS_INCL_NOTE_2, "
					+ "    TAS_INCL_NOTE_3, "
					+ "    DOC_RICH, "
					+ "    TP_AFF_ACC, "
					+ "    TP_AFF_ACC_2, "
					+ "    TP_AFF_ACC_3, "
					+ "    TP_AFF_ACC_4, "
					+ "    TP_AFF_ACC_5, "
					+ "    TP_AFF_ACC_6, "
					+ "    TP_AFF_GR_1, "
					+ "    RM_NUM_REV, "
					+ "    RM_OR_N, "
					+ "    DES_VALORE_PRODOTTO, "
					+ "    DOC_DESC_REV, "
					+ "    DOC_DATA_REV, "
					+ "    TAS_INCL_ACT, "
					+ "    TAS_INCL_ACT_G, "
					+ "    TP_AFF_GR, "
					+ "    FLAG_RMC, "
					+ "    TIMESTAMP_AGG, "
					+ "    AL_FLAG_INF, "
					+ "    ALSOL_FLAG_INF, "
					+ "    AL_MIN, "
					+ "    AL_MAX, "
					+ "    ALSOL_INF, "
					+ "    ALSOL_MIN, "
					+ "    ALSOL_MAX, "
					+ "    AS_FLAG_INF, "
					+ "    AS_MIN, "
					+ "    AS_MAX, "
					+ "    B_FLAG_INF, "
					+ "    B_MIN, "
					+ "    B_MAX, "
					+ "    BI_FLAG_INF, "
					+ "    BI_MIN, "
					+ "    BI_MAX, "
					+ "    C_FLAG_INF, "
					+ "    C_MIN, "
					+ "    C_MAX, "
					+ "    C_N_FLAG_INF, "
					+ "    C_N_MIN, "
					+ "    C_N_MAX, "
					+ "    CA_FLAG_INF, "
					+ "    CA_MIN, "
					+ "    CA_MAX, "
					+ "    CB_FLAG_INF, "
					+ "    CB_MIN, "
					+ "    CB_MAX, "
					+ "    CE_FLAG_INF, "
					+ "    CE_MIN, "
					+ "    CE_MAX, "
					+ "    CO_FLAG_INF, "
					+ "    CO_MIN, "
					+ "    CO_MAX, "
					+ "    CR_FLAG_INF, "
					+ "    CR_MIN, "
					+ "    CR_MAX, "
					+ "    CR_EQ_FLAG_INF, "
					+ "    CR_EQ_MIN, "
					+ "    CR_EQ_MAX, "
					+ "    CU_FLAG_INF, "
					+ "    CU_MIN, "
					+ "    CU_MAX, "
					+ "    FE_FLAG_INF, "
					+ "    FE_MIN, "
					+ "    FE_MAX, "
					+ "    H_FLAG_INF, "
					+ "    H_MIN, "
					+ "    H_MAX, "
					+ "    H_PPM_O_PERC, "
					+ "    JF_FLAG_INF, "
					+ "    JF_MIN, "
					+ "    JF_MAX, "
					+ "    MN_FLAG_INF, "
					+ "    MN_MIN, "
					+ "    MN_MAX, "
					+ "    MO_FLAG_INF, "
					+ "    MO_MIN, "
					+ "    MO_MAX, "
					+ "    N_FLAG_INF, "
					+ "    N_MIN, "
					+ "    N_MAX, "
					+ "    N_PPM_O_PERC, "
					+ "    NB_FLAG_INF, "
					+ "    NB_MIN, "
					+ "    NB_MAX, "
					+ "    NB_TA_FLAG_INF, "
					+ "    NB_TA_MIN, "
					+ "    NB_TA_MAX, "
					+ "    NI_FLAG_INF, "
					+ "    NI_MIN, "
					+ "    NI_MAX, "
					+ "    O_FLAG_INF, "
					+ "    O_MIN, "
					+ "    O_MAX, "
					+ "    O_PPM_O_PERC, "
					+ "    P_FLAG_INF, "
					+ "    P_MIN, "
					+ "    P_MAX, "
					+ "    PB_FLAG_INF, "
					+ "    PB_MIN, "
					+ "    PB_MAX, "
					+ "    PCM_FLAG_INF, "
					+ "    PCM_MIN, "
					+ "    PCM_MAX, "
					+ "    PRE_FLAG_INF, "
					+ "    PRE_MIN, "
					+ "    PRE_MAX, "
					+ "    S_FLAG_INF, "
					+ "    S_MIN, "
					+ "    S_MAX, "
					+ "    SB_FLAG_INF, "
					+ "    SB_MIN, "
					+ "    SB_MAX, "
					+ "    SI_FLAG_INF, "
					+ "    SI_MIN, "
					+ "    SI_MAX, "
					+ "    SN_FLAG_INF, "
					+ "    SN_MIN, "
					+ "    SN_MAX, "
					+ "    TA_FLAG_INF, "
					+ "    TA_MIN, "
					+ "    TA_MAX, "
					+ "    TI_FLAG_INF, "
					+ "    TI_MIN, "
					+ "    TI_MAX, "
					+ "    V_FLAG_INF, "
					+ "    V_MIN, "
					+ "    V_MAX, "
					+ "    W_FLAG_INF, "
					+ "    W_MIN, "
					+ "    W_MAX, "
					+ "    XF_FLAG_INF, "
					+ "    XF_MIN, "
					+ "    XF_MAX, "
					+ "    Y_FLAG_INF, "
					+ "    Y_MIN, "
					+ "    Y_MAX, "
					+ "    ZR_FLAG_INF, "
					+ "    ZR_MIN, "
					+ "    ZR_MAX, "
					+ "    AC_FORMULA_1, "
					+ "    AC_FORMULA_1_MAX, "
					+ "    AC_FORMULA_1_MIN, "
					+ "    AC_FORMULA_2, "
					+ "    AC_FORMULA_2_MAX, "
					+ "    AC_FORMULA_2_MIN, "
					+ "    AC_FORMULA_3, "
					+ "    AC_FORMULA_3_MAX, "
					+ "    AC_FORMULA_3_MIN, "
					+ "    AC_FORMULA_4, "
					+ "    AC_FORMULA_4_MAX, "
					+ "    AC_FORMULA_4_MIN, "
					+ "    AC_FORMULA_5, "
					+ "    AC_FORMULA_5_MAX, "
					+ "    AC_FORMULA_5_MIN "
					+ "FROM MAME.VW_QC_ANALISI_RM; "
					+ "";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(insert,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_QC_ANALISI_RM "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo2(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ " PantheraTarget.dbo.YPT_QC_ANALISI_RM "
					+ "SET "
					+ " ELABORATO = '0' "
					+ "WHERE "
					+ " ELABORATO is null";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_QC_ANALISI_RM Set Elaborato to 0 if Null "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo3(ConnectionDescriptor cnd) {
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
			output.println(" Dtsx-block : Riattiva QC_ANALISI_RM "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo4(ConnectionDescriptor cnd) {
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
			output.println(" Dtsx-block : Riattiva QC_ANALISI_RM 2 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo5(ConnectionDescriptor cnd) {
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
			output.println(" Dtsx-block : YPT_QC_ANALISI_RM Set Elaborato to X "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo6(ConnectionDescriptor cnd) {
		try {
			String stmt = "INSERT INTO [PantheraTarget].[dbo].[YPT_ORD_FOR] ( "
					+ "    NUM_DOC, "
					+ "    ANNO_DOC, "
					+ "    COD_CF, "
					+ "    DATA_DOC, "
					+ "    COD_ART, "
					+ "    DES_RIGA, "
					+ "    NUM_RIGA, "
					+ "    UM_BASE, "
					+ "    QUANT_UM_BASE, "
					+ "    PREZZO, "
					+ "    NOTE_INT, "
					+ "    DATA_CONS_RIGA, "
					+ "    ID, "
					+ "    TIMESTAMP_AGG "
					+ ") "
					+ "SELECT  "
					+ "    NUM_DOC, "
					+ "    ANNO_DOC, "
					+ "    COD_CF, "
					+ "    DATA_DOC, "
					+ "    COD_ART, "
					+ "    DES_RIGA, "
					+ "    NUM_RIGA, "
					+ "    UM_BASE, "
					+ "    QUANT_UM_BASE, "
					+ "    PREZZO, "
					+ "    NOTE_INT, "
					+ "    DATA_CONS_RIGA, "
					+ "    ID, "
					+ "    TIMESTAMP_AGG "
					+ "FROM MAME.VW_ORD_FOR; "
					+ "";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : INSERT INTO [PantheraTarget].[dbo].[YPT_ORD_FOR] "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo8(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ " PantheraTarget.dbo.YPT_ORD_FOR "
					+ "SET "
					+ " ELABORATO = '0' "
					+ "WHERE "
					+ " ELABORATO is null";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_ORD_FOR Set Elaborato to 0 if Null "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo9(ConnectionDescriptor cnd) {
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
			output.println(" Dtsx-block : Riattiva ORD_FOR "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo10(ConnectionDescriptor cnd) {
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
			output.println(" Dtsx-block : Riattiva ORD_FOR 1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo11(ConnectionDescriptor cnd) {
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
			output.println(" Dtsx-block : YPT_QC_ANALISI_RM "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo12(ConnectionDescriptor cnd) {
		try {
			String stmt = "INSERT INTO [PantheraTarget].[dbo].[YPT_DDT_FOR] ( "
					+ "    NUM_DDT_INT, "
					+ "    ANNO_DOC, "
					+ "    COD_CF, "
					+ "    DATA_DOC, "
					+ "    CODICE_CAUSALE, "
					+ "    COD_DEP, "
					+ "    COD_DEP_2, "
					+ "    COD_ART, "
					+ "    DES_RIGA, "
					+ "    NUM_RIGA, "
					+ "    UM_ACQ, "
					+ "    QTA_CONSEGNATA, "
					+ "    PREZZO, "
					+ "    LOTTO, "
					+ "    NUM_DDT_FORNITORE, "
					+ "    DATA_DOC_FORNITORE, "
					+ "    LINGOTTO, "
					+ "    RIFERIMENTO_ANNO_ORD_FOR, "
					+ "    RIFERIMENTO_NUM_ORD_FOR, "
					+ "    RIFERIMENTO_RIGA_ORD_FOR, "
					+ "    TIMESTAMP_AGG "
					+ ") "
					+ "SELECT  "
					+ "    NUM_DDT_INT, "
					+ "    ANNO_DOC, "
					+ "    COD_CF, "
					+ "    DATA_DOC, "
					+ "    CODICE_CAUSALE, "
					+ "    COD_DEP, "
					+ "    COD_DEP_2, "
					+ "    COD_ART, "
					+ "    DES_RIGA, "
					+ "    NUM_RIGA, "
					+ "    UM_ACQ, "
					+ "    QTA_CONSEGNATA, "
					+ "    PREZZO, "
					+ "    LOTTO, "
					+ "    NUM_DDT_FORNITORE, "
					+ "    DATA_DOC_FORNITORE, "
					+ "    LINGOTTO, "
					+ "    RIFERIMENTO_ANNO_ORD_FOR, "
					+ "    RIFERIMENTO_NUM_ORD_FOR, "
					+ "    RIFERIMENTO_RIGA_ORD_FOR, "
					+ "    TIMESTAMP_AGG "
					+ "FROM MAME.VW_DDT_FOR; "
					+ "";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_QC_ANALISI_RM "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo13(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ " PantheraTarget.dbo.YPT_DDT_FOR "
					+ "SET "
					+ " ELABORATO = '0' "
					+ "WHERE "
					+ " ELABORATO is null";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_DDT_FOR Set Elaborato to 0 if Null "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo1(ConnectionDescriptor cnd) {
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
			output.println(" Dtsx-block : YPT_QC_ANALISI_RM "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo14(ConnectionDescriptor cnd) {
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
			output.println(" Dtsx-block : Riattiva DDT_FOR 1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo15(ConnectionDescriptor cnd) {
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
			output.println(" Dtsx-block : YPT_DDT_FOR Set Elaborato to X "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo16(ConnectionDescriptor cnd) {
		try {
			String stmt = "INSERT INTO [PantheraTarget].[dbo].[YPT_QC_ANALISI_ACCIAIERIA] ( "
					+ "    ENTRATA, "
					+ "    \"DATA\", "
					+ "    COLATA, "
					+ "    ACCIAIERIA, "
					+ "    ID, "
					+ "    ACCIAIO, "
					+ "    ALLIAS_ACCIAIO, "
					+ "    SPECIFICA_1, "
					+ "    ELAB_ACCIAIO, "
					+ "    TIPO_TRAT, "
					+ "    NOTE, "
					+ "    TIMESTAMP_AGG, "
					+ "    AL, "
					+ "    ALSOL, "
					+ "    AS_, "
					+ "    B, "
					+ "    BI, "
					+ "    C, "
					+ "    CA, "
					+ "    CB, "
					+ "    CE, "
					+ "    CO, "
					+ "    CR, "
					+ "    CR_EQ, "
					+ "    CU, "
					+ "    H, "
					+ "    H_PPM_O_PERC, "
					+ "    JF, "
					+ "    MN, "
					+ "    MO, "
					+ "    N, "
					+ "    N_PPM_O_PERC, "
					+ "    NB, "
					+ "    NI, "
					+ "    O, "
					+ "    O_PPM_O_PERC, "
					+ "    P, "
					+ "    PB, "
					+ "    PCM, "
					+ "    PRE, "
					+ "    S, "
					+ "    SB, "
					+ "    SI, "
					+ "    SN, "
					+ "    TA, "
					+ "    TI, "
					+ "    V, "
					+ "    W, "
					+ "    XF, "
					+ "    ZR, "
					+ "    C_N, "
					+ "    FE, "
					+ "    NB_TA, "
					+ "    Y, "
					+ "    AC_FORMULA_1, "
					+ "    AC_FORMULA_1_VALORE, "
					+ "    AC_FORMULA_2, "
					+ "    AC_FORMULA_2_VALORE, "
					+ "    AC_FORMULA_3, "
					+ "    AC_FORMULA_3_VALORE, "
					+ "    AC_FORMULA_4, "
					+ "    AC_FORMULA_4_VALORE, "
					+ "    AC_FORMULA_5, "
					+ "    AC_FORMULA_5_VALORE "
					+ ") "
					+ "SELECT  "
					+ "    ENTRATA, "
					+ "    \"DATA\", "
					+ "    COLATA, "
					+ "    ACCIAIERIA, "
					+ "    ID, "
					+ "    ACCIAIO, "
					+ "    ALLIAS_ACCIAIO, "
					+ "    SPECIFICA_1, "
					+ "    ELAB_ACCIAIO, "
					+ "    TIPO_TRAT, "
					+ "    NOTE, "
					+ "    TIMESTAMP_AGG, "
					+ "    AL, "
					+ "    ALSOL, "
					+ "    AS_, "
					+ "    B, "
					+ "    BI, "
					+ "    C, "
					+ "    CA, "
					+ "    CB, "
					+ "    CE, "
					+ "    CO, "
					+ "    CR, "
					+ "    CR_EQ, "
					+ "    CU, "
					+ "    H, "
					+ "    H_PPM_O_PERC, "
					+ "    JF, "
					+ "    MN, "
					+ "    MO, "
					+ "    N, "
					+ "    N_PPM_O_PERC, "
					+ "    NB, "
					+ "    NI, "
					+ "    O, "
					+ "    O_PPM_O_PERC, "
					+ "    P, "
					+ "    PB, "
					+ "    PCM, "
					+ "    PRE, "
					+ "    S, "
					+ "    SB, "
					+ "    SI, "
					+ "    SN, "
					+ "    TA, "
					+ "    TI, "
					+ "    V, "
					+ "    W, "
					+ "    XF, "
					+ "    ZR, "
					+ "    C_N, "
					+ "    FE, "
					+ "    NB_TA, "
					+ "    Y, "
					+ "    AC_FORMULA_1, "
					+ "    AC_FORMULA_1_VALORE, "
					+ "    AC_FORMULA_2, "
					+ "    AC_FORMULA_2_VALORE, "
					+ "    AC_FORMULA_3, "
					+ "    AC_FORMULA_3_VALORE, "
					+ "    AC_FORMULA_4, "
					+ "    AC_FORMULA_4_VALORE, "
					+ "    AC_FORMULA_5, "
					+ "    AC_FORMULA_5_VALORE "
					+ "FROM MAME.WV_QC_ANALISI_ACCIAIERIA; "
					+ "";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : INSERT INTO [PantheraTarget].[dbo].[YPT_QC_ANALISI_ACCIAIERIA] "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo18(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ " PantheraTarget.dbo.YPT_QC_ANALISI_ACCIAIERIA "
					+ "SET "
					+ " ELABORATO = '0' "
					+ "WHERE "
					+ " ELABORATO is null";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_QC_ANALISI_ACCIAIERIA  Set Elaborato to 0 if Null "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo19(ConnectionDescriptor cnd) {
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
			output.println(" Dtsx-block : Riattiva QC_ANALISI_ACCIAIERIA "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo20(ConnectionDescriptor cnd) {
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
			output.println(" Dtsx-block : Riattiva QC_ANALISI_ACCIAIERIA 1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo21(ConnectionDescriptor cnd) {
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
			output.println(" Dtsx-block : YPT_QC_ANALISI_ACCIAIERIA Set Elaborato to X "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	
	protected void passo22(ConnectionDescriptor cnd) {
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
			output.println(" Dtsx-block : Riattiva DDT_FOR 1 1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo23(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ " PantheraTarget.dbo.YPT_DDT_FOR "
					+ "SET "
					+ " ELABORATO = 'X' "
					+ "WHERE "
					+ " EXISTS ( "
					+ " SELECT "
					+ "  QRY_YPT_DDT_FOR.NUM_DDT_INT, "
					+ "  QRY_YPT_DDT_FOR.ANNO_DOC, "
					+ "  QRY_YPT_DDT_FOR.NUM_RIGA, "
					+ "  QRY_YPT_DDT_FOR.TIMESTAMP_AGG "
					+ " FROM "
					+ "  PantheraTarget.dbo.YPT_DDT_FOR QRY_YPT_DDT_FOR "
					+ " INNER JOIN ( "
					+ "  SELECT "
					+ "   NUM_DDT_INT , "
					+ "   ANNO_DOC, "
					+ "   NUM_RIGA, "
					+ "   MAX(TIMESTAMP_AGG) MAX_TIMESTAMP_AGG "
					+ "  FROM "
					+ "   PantheraTarget.dbo.YPT_DDT_FOR YPT_DDT_FOR "
					+ "  GROUP BY "
					+ "   NUM_DDT_INT , "
					+ "   ANNO_DOC, "
					+ "   NUM_RIGA) MAX_YPT_DDT_FOR ON "
					+ "  MAX_YPT_DDT_FOR.NUM_DDT_INT = QRY_YPT_DDT_FOR.NUM_DDT_INT "
					+ "  AND MAX_YPT_DDT_FOR.ANNO_DOC = QRY_YPT_DDT_FOR.ANNO_DOC "
					+ "  AND MAX_YPT_DDT_FOR.NUM_RIGA = QRY_YPT_DDT_FOR.NUM_RIGA "
					+ "  AND MAX_YPT_DDT_FOR.MAX_TIMESTAMP_AGG > QRY_YPT_DDT_FOR.TIMESTAMP_AGG "
					+ " WHERE "
					+ "  YPT_DDT_FOR.NUM_DDT_INT = QRY_YPT_DDT_FOR.NUM_DDT_INT "
					+ "  AND YPT_DDT_FOR.ANNO_DOC = QRY_YPT_DDT_FOR.ANNO_DOC "
					+ "  AND YPT_DDT_FOR.NUM_RIGA = QRY_YPT_DDT_FOR.NUM_RIGA "
					+ "  AND YPT_DDT_FOR.TIMESTAMP_AGG = QRY_YPT_DDT_FOR.TIMESTAMP_AGG) "
					+ " AND YPT_DDT_FOR.ELABORATO = '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_DDT_FOR Set Elaborato to X 1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	@Override
	protected String getClassAdCollectionName() {
		return "YDtsxRmOdaDDTRunner";
	}

}
