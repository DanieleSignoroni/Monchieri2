package it.monchieri.thip.dtsx;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.batch.BatchRunnable;
import com.thera.thermfw.persist.ConnectionDescriptor;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.SQLServerJTDSNoUnicodeDatabase;

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

public class YDtsxPtExpOrdEsecRunner extends BatchRunnable {

	public static final String NOME_DB_EXT = "PantheraTarget";
	public static final String UTENTE_DB_EXT = "Panthera";
	public static final String PWD_DB_EXT = "panthera";
	public static final String SRV_DB_EXT = "SRVDB.fmonchieri.locale";
	public static final String PORTA_DB_EXT = "1433";

	public static final java.sql.Date MIN_SQL_DATE = TimeUtils.getDate(1900, 1, 1);
	public static final java.sql.Date MAX_SQL_DATE = TimeUtils.getDate(2100, 12, 31);

	@Override
	protected boolean run() {
		ConnectionDescriptor cnd = externalConnectionDescriptor(NOME_DB_EXT, UTENTE_DB_EXT, PWD_DB_EXT, SRV_DB_EXT, PORTA_DB_EXT);
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
			String insert = "INSERT	INTO [MAME].[YPT_EXP_ORD_ESEC] "
					+ "SELECT DISTINCT "
					+ "			cast(VW_EXP_ORD_ESEC.[DOC_ID] as char(20)) , "
					+ "			cast(VW_EXP_ORD_ESEC.[ANNO_DOC]  as char(4)) , "
					+ "			VW_EXP_ORD_ESEC.[NUM_DOC] , "
					+ "			cast(VW_EXP_ORD_ESEC.[SERIE_DOC]  as char(5)) , "
					+ "			VW_EXP_ORD_ESEC.[DATA_DOC] , "
					+ "			cast(VW_EXP_ORD_ESEC.[COD_CF]  as char(12)), "
					+ "			cast(VW_EXP_ORD_ESEC.[NOTE_INT]  as char(255)), "
					+ "			cast(VW_EXP_ORD_ESEC.[NOTE_STAMPA]  as char(255)), "
					+ "			cast(VW_EXP_ORD_ESEC.[COD_CAUS_DOC]  as char(12)), "
					+ "			cast(VW_EXP_ORD_ESEC.[NOTE_CONSEGNA]  as char(255)), "
					+ "			cast(VW_EXP_ORD_ESEC.[COD_DEP]  as char(20)), "
					+ "			cast(VW_EXP_ORD_ESEC.[COD_ART]  as char(30)), "
					+ "			cast(VW_EXP_ORD_ESEC.[COD_VAR_MATERIALE]  as char(30)), "
					+ "			cast(REPLACE(VW_EXP_ORD_ESEC.[COD_LOT], '/', '\\') as char(30)) AS [COD_LOT]  , "
					+ "			cast(VW_EXP_ORD_ESEC.[DES_PROD]  as char(255)), "
					+ "			VW_EXP_ORD_ESEC.[QUANT_DA_PROD] , "
					+ "			cast(VW_EXP_ORD_ESEC.[RIF_RIGA_ORD_CLI_4B]  as char(30)), "
					+ "			cast(VW_EXP_ORD_ESEC.[UTENTE_INS]  as char(20)), "
					+ "			VW_EXP_ORD_ESEC.[DATA_INS] , "
					+ "			cast(VW_EXP_ORD_ESEC.[UTENTE_MOD]  as char(20)), "
					+ "			VW_EXP_ORD_ESEC.[DATA_MOD] , "
					+ "			VW_EXP_ORD_ESEC.[PESGREZ] , "
					+ "			VW_EXP_ORD_ESEC.[PESMULT] , "
					+ "			cast(VW_EXP_ORD_ESEC.[CHIODAIA]  as char(20)), "
					+ "			cast(VW_EXP_ORD_ESEC.[MANDRINO]  as char(20)), "
					+ "			cast(VW_EXP_ORD_ESEC.[ATTREZZ1]  as char(40)), "
					+ "			cast(VW_EXP_ORD_ESEC.[ATTREZZ2]  as char(40)), "
					+ "			cast(VW_EXP_ORD_ESEC.[PROVNUM]  as char(10)), "
					+ "			VW_EXP_ORD_ESEC.[DATA_SPED] , "
					+ "			VW_EXP_ORD_ESEC.[DATA_TT] , "
					+ "			cast(VW_EXP_ORD_ESEC.[IDENTCLI]  as char(40)), "
					+ "			VW_EXP_ORD_ESEC.[NUCLEARE] , "
					+ "			cast(VW_EXP_ORD_ESEC.[CODCLI]  as char(40)), "
					+ "			cast(CASE WHEN YPT_EXP_ORD_ESEC_max.DOC_ID IS NULL THEN 0	ELSE 1 END as varchar(1)) AS [TIPO_OPERAZIONE] , "
					+ "			cast(VW_EXP_ORD_ESEC.[ESEGUITO]  as varchar(1)), "
					+ "			VW_EXP_ORD_ESEC.[NUM_RIGA] , "
					+ "			cast(VW_EXP_ORD_ESEC.[DOC_RIGA_ID] as char(30)), "
					+ "			cast(VW_EXP_ORD_ESEC.[COD_ART_COMP]  as char(30)), "
					+ "			coalesce(VW_EXP_ORD_ESEC.[QUANT_SCARICATA],1) as  QUANT_SCARICATA, "
					+ "			coalesce(VW_EXP_ORD_ESEC.[QUANT_MATEROZZA],0) as UANT_MATEROZZA, "
					+ "			cast(VW_EXP_ORD_ESEC.[ENTRATA] as char(20)), "
					+ "			VW_EXP_ORD_ESEC.[DATA_MOVIMENTO] "
					+ "FROM "
					+ "			openquery(panth01,'select * from MAME.VW_EXP_ORD_ESEC') VW_EXP_ORD_ESEC "
					+ "LEFT JOIN (SELECT DOC_ID, MAX(DATA_MOD) AS DATA_MOD	FROM MAME.YPT_EXP_ORD_ESEC GROUP BY DOC_ID) YPT_EXP_ORD_ESEC_MAX ON "
					+ "		VW_EXP_ORD_ESEC.DOC_ID = YPT_EXP_ORD_ESEC_max.DOC_ID "
					+ "WHERE CAST(VW_EXP_ORD_ESEC.DATA_MOD AS datetime) > isnull(YPT_EXP_ORD_ESEC_MAX.DATA_MOD,0) "
					+ "	AND VW_EXP_ORD_ESEC.[TIPO_OPERAZIONE] IS NOT NULL "
					+ "	AND VW_EXP_ORD_ESEC.[ESEGUITO] IS NOT NULL "
					+ "	AND VW_EXP_ORD_ESEC.[NUM_RIGA] IS NOT NULL "
					+ "	AND VW_EXP_ORD_ESEC.[DOC_RIGA_ID] IS NOT NULL "
					+ "	--AND VW_EXP_ORD_ESEC.[QUANT_SCARICATA] IS NOT NULL "
					+ "	--AND VW_EXP_ORD_ESEC.[QUANT_MATEROZZA] IS NOT NULL "
					+ "	AND VW_EXP_ORD_ESEC.[ENTRATA] IS NOT NULL "
					+ "	--AND VW_EXP_ORD_ESEC.[DATA_MOVIMENTO] IS NOT NULL "
					+ "	AND coalesce(VW_EXP_ORD_ESEC.[DATA_MOVIMENTO],'0001-01-01') <> '0001-01-01' "
					+ "	AND VW_EXP_ORD_ESEC.[COD_ART_COMP] IS NOT NULL "
					+ "	AND (SUBSTRING(VW_EXP_ORD_ESEC.COD_LOT, 3, 1) = '/'	OR LEFT(VW_EXP_ORD_ESEC.COD_LOT,2) = 'SN') "
					+ "	ORDER BY VW_EXP_ORD_ESEC.[DATA_MOVIMENTO] ";
			int rc = eseguiUpdateSuDbExt(insert,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : insert "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo1(ConnectionDescriptor cnd) {
		try {
			String Update_ESEGUITA = "UPDATE [PantheraTarget].[MAME].[YPT_EXP_ORD_ESEC] "
					+ "SET YPT_EXP_ORD_ESEC.eseguito = 'X' "
					+ "WHERE EXISTS (SELECT 1 as DATA_MOD_MAX "
					+ "  FROM [PantheraTarget].[MAME].[YPT_EXP_ORD_ESEC] as YPT_EXP_ORD_ESEC_SQL "
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

	protected void passo2(ConnectionDescriptor cnd) {
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

	protected void passo3(ConnectionDescriptor cnd) {
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

	protected void passo4(ConnectionDescriptor cnd) {
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

	protected void passo5(ConnectionDescriptor cnd) {
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

	protected void passo6(ConnectionDescriptor cnd) {
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

	protected void passo7(ConnectionDescriptor cnd) {
		try {
			String Update_ORP_EFF_COD_CF = "update dbo.ORP_EFF  "
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

	protected void passo8(ConnectionDescriptor cnd) {
		try {
			String Update_YPT_EXP_ORD_ESEC_COD_CF = "UPDATE [PantheraTarget].[MAME].[YPT_EXP_ORD_ESEC] "
					+ "SET COD_CF=(select oc.COD_CF  "
					+ "from dbo.ORD_CLI_RIGHE ocr "
					+ "inner join dbo.ORD_CLI oc "
					+ "on oc.DOC_ID = ocr.DOC_ID "
					+ "WHERE [PantheraTarget].[MAME].[YPT_EXP_ORD_ESEC].RIF_RIGA_ORD_CLI_4B = ocr.DOC_RIGA_ID "
					+ "and [PantheraTarget].[MAME].[YPT_EXP_ORD_ESEC].COD_CF <> oc.COD_CF) "
					+ "where exists(select oc.COD_CF  "
					+ "from dbo.ORD_CLI_RIGHE ocr "
					+ "inner join dbo.ORD_CLI oc "
					+ "on oc.DOC_ID = ocr.DOC_ID "
					+ "WHERE [PantheraTarget].[MAME].[YPT_EXP_ORD_ESEC].RIF_RIGA_ORD_CLI_4B = ocr.DOC_RIGA_ID "
					+ "and [PantheraTarget].[MAME].[YPT_EXP_ORD_ESEC].COD_CF <> oc.COD_CF) "
					+ "AND [PantheraTarget].[MAME].[YPT_EXP_ORD_ESEC].COD_CF in ('00007797') "
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
		return "YDtsxPtExpOrdEsecRunner";
	}

}
