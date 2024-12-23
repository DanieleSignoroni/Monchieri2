package it.monchieri.thip.target;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.CachedStatement;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 23/12/2024
 * <br><br>
 * <b>71XXX    DSSOF3    23/12/2024</b>
 * <p></p>
 */

public class OrdEsecAtvSogCollaudo extends OrdEsecAtvSogCollaudoPO {

	protected String iNote_C03_TT_QUALITA;
	protected Map<Integer,String> descrizioniFase_C03_TT_QUALITA = null;
	
	protected String iNote_C03_TT_PRELIMINARE;
	protected Map<Integer,String> descrizioniFase_C03_TT_PRELIMINARE = null;
	
	protected String iNote_C03_TT_SUPPLEMENTARE = null;
	protected Map<Integer,String> descrizioniFase_C03_TT_SUPPLEMENTARE = null;
	
	protected Map<Integer,String> descrizioneFase_C06_CONTORLLO_INTERMEDIO = null;
	protected Map<Integer,String> descrizioneFase_C06_CONTORLLO_INTERMEDIO_1 = null;
	
	public Map<Integer, String> getDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1() {
		return descrizioneFase_C06_CONTORLLO_INTERMEDIO_1;
	}

	public void setDescrizioneFase_C06_CONTORLLO_INTERMEDIO_1(
			Map<Integer, String> descrizioneFase_C06_CONTORLLO_INTERMEDIO_1) {
		this.descrizioneFase_C06_CONTORLLO_INTERMEDIO_1 = descrizioneFase_C06_CONTORLLO_INTERMEDIO_1;
	}

	public Map<Integer, String> getDescrizioneFase_C06_CONTORLLO_INTERMEDIO() {
		return descrizioneFase_C06_CONTORLLO_INTERMEDIO;
	}

	public void setDescrizioneFase_C06_CONTORLLO_INTERMEDIO(Map<Integer, String> descrizioneFase_C03_CONTORLLO_INTERMEDIO) {
		this.descrizioneFase_C06_CONTORLLO_INTERMEDIO = descrizioneFase_C03_CONTORLLO_INTERMEDIO;
	}

	public String getNote_C03_TT_SUPPLEMENTARE() {
		return iNote_C03_TT_SUPPLEMENTARE;
	}

	public void setNote_C03_TT_SUPPLEMENTARE(String iNote_C03_TT_SUPPLEMENTARE) {
		this.iNote_C03_TT_SUPPLEMENTARE = iNote_C03_TT_SUPPLEMENTARE;
	}

	public Map<Integer, String> getDescrizioniFase_C03_TT_SUPPLEMENTARE() {
		return descrizioniFase_C03_TT_SUPPLEMENTARE;
	}

	public void setDescrizioniFase_C03_TT_SUPPLEMENTARE(Map<Integer, String> descrizioniFase_C03_TT_SUPPLEMENTARE) {
		this.descrizioniFase_C03_TT_SUPPLEMENTARE = descrizioniFase_C03_TT_SUPPLEMENTARE;
	}

	public Map<Integer, String> getDescrizioniFase_C03_TT_PRELIMINARE() {
		return descrizioniFase_C03_TT_PRELIMINARE;
	}

	public void setDescrizioniFase_C03_TT_PRELIMINARE(Map<Integer, String> descrizioniFase_C03_TT_PRELIMINARE) {
		this.descrizioniFase_C03_TT_PRELIMINARE = descrizioniFase_C03_TT_PRELIMINARE;
	}

	public String getNote_C03_TT_PRELIMINARE() {
		return iNote_C03_TT_PRELIMINARE;
	}

	public void setNote_C03_TT_PRELIMINARE(String iNote_C03_TT_PRELIMINARE) {
		this.iNote_C03_TT_PRELIMINARE = iNote_C03_TT_PRELIMINARE;
	}

	public Map<Integer, String> getDescrizioniFase_C03_TT_QUALITA() {
		return descrizioniFase_C03_TT_QUALITA;
	}

	public void setDescrizioniFase_C03_TT_QUALITA(Map<Integer, String> descrizioniFase_C03_TT_QUALITA) {
		this.descrizioniFase_C03_TT_QUALITA = descrizioniFase_C03_TT_QUALITA;
	}

	public String getNote_C03_TT_QUALITA() {
		return iNote_C03_TT_QUALITA;
	}

	public void setNote_C03_TT_QUALITA(String iNote_C03_TT_QUALITA) {
		this.iNote_C03_TT_QUALITA = iNote_C03_TT_QUALITA;
	}

	@Override
	public ErrorMessage checkDelete() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public static List<OrdEsecAtvSogCollaudo> listaCicliControlloDaImportare() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		List<OrdEsecAtvSogCollaudo> list = new ArrayList<OrdEsecAtvSogCollaudo>();
		String where = ""+OrdEsecAtvSogCollaudoTM.ID_PROGRESSIVO+" IS NULL ";
		list = retrieveList(where, "", false);
		return list;
	}
	
	public void recuperaNote_C03_TT_PRELIMINARE(){
		String stmt = "SELECT "
				+ "	CAST('it' AS nchar(2)) AS LANGUAGE, "
				+ "	CAST('CM_SGQ_CICLI_TES' AS nchar(18)) AS TABLE_NAME, "
				+ "	CAST('SGQ_CICLI_TES' AS nvarchar(35)) AS DESCRIPTION, "
				+ "COALESCE( "
				+ "CAST('Specifica:              ' + qc.SPECIFICA_1 + ' ' + qc.SPECIFICA_2 + CHAR(10) + 'Tipo Trattamento:       ' + qc.TIPO_TRAT + CHAR(10)  "
				+ "+ 'Materiale:              ' + qc.MATERIALE + CHAR(10) + 'Termocopia a contatto:  ' + "
				+ "	CASE COALESCE (qc.TT_TERMOC_CONT, "
				+ "	0) "
				+ "	WHEN 1 THEN 'SI' "
				+ "	ELSE 'NO' "
				+ "END + CHAR(10)  "
				+ "+ "
				+ "CASE "
				+ "	WHEN COALESCE (qc.NOTE, "
				+ "	'') <> '' THEN 'Note:                   ' + CHAR(10) + qc.NOTE + CHAR(10) "
				+ "	ELSE '' "
				+ "END AS nvarchar(1000)), "
				+ "	'.') "
				+ "AS NLS_COMMENT_TEXT, "
				+ "CAST('N' AS nchar(1)) AS RESERVED, "
				+ "CAST('0' AS nchar(1)) AS TEXT_TYPE, "
				+ "	NULL AS COMM_TYPE1_ID, "
				+ "	NULL AS COMM_TYPE2_ID, "
				+ "	CAST('SGQ_CICLI_TES' AS nchar(15)) AS R_COMMENT_USE "
				+ "	-- "
				+ "FROM PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO oea "
				+ "INNER JOIN [Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] qc "
				+ "ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_TT-PRELIMINARE' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				setNote_C03_TT_PRELIMINARE(rs.getString("NLS_COMMENT_TEXT"));
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(cs != null) {
					cs.free();
				}
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
	}

	public void recuperaNote_C03_TT_QUALITA_Target() {
		String stmt ="SELECT "
				+ "	COALESCE( "
				+ "CAST('Specifica:              ' + qc.SPECIFICA_1 + ' ' + qc.SPECIFICA_2 + CHAR(10) + 'Tipo Trattamento:       ' + qc.TIPO_TRAT + CHAR(10)  "
				+ "+ 'Materiale:              ' + qc.MATERIALE + CHAR(10) + 'Termocopia a contatto:  ' + "
				+ "	CASE COALESCE (qc.TT_TERMOC_CONT, "
				+ "	0) "
				+ "	WHEN 1 THEN 'SI' "
				+ "	ELSE 'NO' "
				+ "END + CHAR(10)  "
				+ "+ "
				+ "CASE "
				+ "	WHEN COALESCE (qc.NOTE, "
				+ "	'') <> '' THEN 'Note:                   ' + CHAR(10) + qc.NOTE + CHAR(10) "
				+ "	ELSE '' "
				+ "END AS nvarchar(1000)), "
				+ "	'.') "
				+ "AS NLS_COMMENT_TEXT, "
				+ "	CAST('N' AS nchar(1)) AS RESERVED, "
				+ "	CAST('0' AS nchar(1)) AS TEXT_TYPE, "
				+ "	NULL AS COMM_TYPE1_ID, "
				+ "	NULL AS COMM_TYPE2_ID, "
				+ "	CAST('SGQ_CICLI_TES' AS nchar(15)) AS R_COMMENT_USE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO oea "
				+ "INNER JOIN [Target].[dbo].[QC_TT_QUALITA_RIC_FM] qc "
				+ "ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA "
				+ "INNER JOIN target.dbo.ORD_CLI_RIGHE_USER ocru "
				+ "ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_TT-QUALITA' "
				+ "	AND ocru.FLAG_GENERATO_CERTIFICATO = 1 "
				+ "	AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga().trim()+"' "
				+ "	AND oea.R_COMMESSA = '"+getCommessa().trim()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				setNote_C03_TT_QUALITA(rs.getString("NLS_COMMENT_TEXT"));
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(cs != null) {
					cs.free();
				}
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
	}
	
	public void recuperaNote_C03_TT_QUALITA_FAS_Target() {
		descrizioniFase_C03_TT_QUALITA = new HashMap<Integer, String>();
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + qc.ID_SEQUENZA as ID_SEQUENZA_FASE , "
				+ "	CAST('SGQ_CICLI_FMO' AS nchar(20)) AS DATA_ORIGIN, "
				+ "	CAST('it' AS nchar(2)) AS LANGUAGE, "
				+ "	CAST('CM_SGQ_CICLI_FAS' AS nchar(18)) AS TABLE_NAME, "
				+ "	CAST('SGQ_CICLI_FAS' AS nvarchar(35))  "
				+ "AS DESCRIPTION, "
				+ "	CAST('Gradiente Salita C°/h:  ' + COALESCE (cast(qc.TT_GRADIENTE_SAL_GRAD_C AS varchar), "
				+ "	'') + CHAR(10)  "
				+ "+ 'Salita C°:              ' + COALESCE (cast(qc.TT_SALITA_GRADI_C AS varchar), "
				+ "	'') + CHAR(10) + 'Permanenza h:           ' + COALESCE (cast(qc.TT_PERM AS varchar), "
				+ "	'') + CHAR(10)  "
				+ "+ 'Gradiente Discesa C°/h: ' + COALESCE (cast(qc.TT_GRADIENTE_RAF_GRAD_C AS varchar), "
				+ "	'') + CHAR(10)  "
				+ "+ 'Raffreddamento:         ' + "
				+ "	CASE COALESCE (qc.TT_RAFFREDDAMENTO, "
				+ "	0) "
				+ "	WHEN 0 THEN 'Polimero' "
				+ "	WHEN 1 THEN 'Aria' "
				+ "	WHEN 2 THEN 'Acqua' "
				+ "	WHEN 3 THEN 'Forno' "
				+ "	WHEN 4 THEN 'Olio' "
				+ "	ELSE '' "
				+ "END AS nvarchar(1000)) AS NLS_COMMENT_TEXT, "
				+ "	CAST('N' AS nchar(1)) AS RESERVED, "
				+ "	CAST('0' AS nchar(1)) AS TEXT_TYPE, "
				+ "	NULL AS COMM_TYPE1_ID, "
				+ "	NULL AS COMM_TYPE2_ID, "
				+ "	CAST('SGQ_CICLI_FAS' AS nchar(15))  "
				+ "AS R_COMMENT_USE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO oea "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		1 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_NOME_1 AS TT_NOME, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_1 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_SALITA_GRADI_C_1 AS TT_SALITA_GRADI_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_PERM_1 AS TT_PERM, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_1 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_1 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		2 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_NOME_2 AS TT_NOME, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_2 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_SALITA_GRADI_C_2 AS TT_SALITA_GRADI_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_PERM_2 AS TT_PERM, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_2 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_2 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		3 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_NOME_3 AS TT_NOME, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_3 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_SALITA_GRADI_C_3 AS TT_SALITA_GRADI_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_PERM_3 AS TT_PERM, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_3 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_3 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		4 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_NOME_4 AS TT_NOME, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_4 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_SALITA_GRADI_C_4 AS TT_SALITA_GRADI_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_PERM_4 AS TT_PERM, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_4 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_4 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		5 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_NOME_5 AS TT_NOME, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_5 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_SALITA_GRADI_C_5 AS TT_SALITA_GRADI_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_PERM_5 AS TT_PERM, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_5 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_5 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		6 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_NOME_6 AS TT_NOME, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_6 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_SALITA_GRADI_C_6 AS TT_SALITA_GRADI_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_PERM_6 AS TT_PERM, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_6 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_6 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM]) qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_TT-QUALITA' "
				+ "	AND ocru.FLAG_GENERATO_CERTIFICATO = 1 "
				+ "	AND COALESCE (qc.TT_NOME, "
				+ "	'') <> '' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				descrizioniFase_C03_TT_QUALITA.put(rs.getInt("ID_SEQUENZA_FASE"), rs.getString("NLS_COMMENT_TEXT"));
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(cs != null) {
					cs.free();
				}
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
	}
	
	public void recuperaNote_C03_TT_PRELIMINARE_Map() {
		descrizioniFase_C03_TT_PRELIMINARE = new HashMap<Integer, String>();
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + qc.ID_SEQUENZA AS ID_SEQUENZA_FASE , "
				+ "	CAST('it' AS nchar(2)) AS LANGUAGE, "
				+ "	CAST('CM_SGQ_CICLI_FAS' AS nchar(18)) AS TABLE_NAME, "
				+ "	CAST('SGQ_CICLI_FAS' AS nvarchar(35)) AS DESCRIPTION, "
				+ "	-- "
				+ "CAST('Gradiente Salita C°/h:  ' + COALESCE(cast(qc.TT_GRADIENTE_SAL_GRAD_C AS varchar),'') + CHAR(10) + 'Salita C°:              ' + COALESCE(cast(qc.TT_SALITA_GRADI_C AS varchar),'') + CHAR(10)  "
				+ "	+ 'Permanenza h:           ' + COALESCE(cast(qc.TT_PERM AS varchar), "
				+ "	'') + CHAR(10) + 'Gradiente Discesa C°/h: ' + COALESCE(cast(qc.TT_GRADIENTE_RAF_GRAD_C AS varchar), "
				+ "	'') + CHAR(10)  "
				+ "	+ 'Raffreddamento:         ' "
				+ "	+ "
				+ "	CASE COALESCE (qc.TT_RAFFREDDAMENTO, "
				+ "	0) "
				+ "	WHEN 0 THEN 'Polimero' "
				+ "	WHEN 1 THEN 'Aria' "
				+ "	WHEN 2 THEN 'Acqua' "
				+ "	WHEN 3 THEN 'Forno' "
				+ "	WHEN 4 THEN 'Olio' "
				+ "	ELSE '' "
				+ "END AS nvarchar(1000) "
				+ ") AS NLS_COMMENT_TEXT, "
				+ "-- "
				+ "CAST('N' AS nchar(1)) AS RESERVED, "
				+ "CAST('0' AS nchar(1)) AS TEXT_TYPE, "
				+ "NULL AS COMM_TYPE1_ID, "
				+ "NULL AS COMM_TYPE2_ID, "
				+ "CAST('SGQ_CICLI_FAS' AS nchar(15)) AS R_COMMENT_USE "
				+ "-- "
				+ "FROM         PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO oea INNER JOIN "
				+ "                          ( "
				+ "SELECT "
				+ "	1 AS ID_SEQUENZA, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_NOME_1 AS TT_NOME, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_1 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_SALITA_GRADI_C_1 AS TT_SALITA_GRADI_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_PERM_1 AS TT_PERM, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_1 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_RAFFREDDAMENTO_1 AS TT_RAFFREDDAMENTO "
				+ "FROM "
				+ "	[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "UNION "
				+ "SELECT "
				+ "	2 AS ID_SEQUENZA, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_NOME_2 AS TT_NOME, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_2 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_SALITA_GRADI_C_2 AS TT_SALITA_GRADI_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_PERM_2 AS TT_PERM, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_2 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_RAFFREDDAMENTO_2 AS TT_RAFFREDDAMENTO "
				+ "FROM "
				+ "	[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "UNION "
				+ "SELECT "
				+ "	3 AS ID_SEQUENZA, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_NOME_3 AS TT_NOME, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_3 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_SALITA_GRADI_C_3 AS TT_SALITA_GRADI_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_PERM_3 AS TT_PERM, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_3 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_RAFFREDDAMENTO_3 AS TT_RAFFREDDAMENTO "
				+ "FROM "
				+ "	[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "UNION "
				+ "SELECT "
				+ "	4 AS ID_SEQUENZA, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_NOME_4 AS TT_NOME, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_4 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_SALITA_GRADI_C_4 AS TT_SALITA_GRADI_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_PERM_4 AS TT_PERM, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_4 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_RAFFREDDAMENTO_4 AS TT_RAFFREDDAMENTO "
				+ "FROM "
				+ "	[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "UNION "
				+ "SELECT "
				+ "	5 AS ID_SEQUENZA, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_NOME_5 AS TT_NOME, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_5 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_SALITA_GRADI_C_5 AS TT_SALITA_GRADI_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_PERM_5 AS TT_PERM, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_5 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_RAFFREDDAMENTO_5 AS TT_RAFFREDDAMENTO "
				+ "FROM "
				+ "	[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "UNION "
				+ "SELECT "
				+ "	6 AS ID_SEQUENZA, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_NOME_6 AS TT_NOME, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_6 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_SALITA_GRADI_C_6 AS TT_SALITA_GRADI_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_PERM_6 AS TT_PERM, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_6 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "	QC_TT_PRELIMINARE_RIC_FM.TT_RAFFREDDAMENTO_6 AS TT_RAFFREDDAMENTO "
				+ "FROM "
				+ "	[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "        ) qc ON "
				+ "qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA "
				+ "WHERE "
				+ "oea.NOME_TABELLA = 'C03_TT-PRELIMINARE' "
				+ "AND COALESCE (qc.TT_NOME, "
				+ "'') <> '' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				descrizioniFase_C03_TT_PRELIMINARE.put(rs.getInt("ID_SEQUENZA_FASE"), rs.getString("NLS_COMMENT_TEXT"));
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(cs != null) {
					cs.free();
				}
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
	}
	
	public void recuperaNote_C03_TT_SUPPLEMENTARE_Target() {
		String stmt ="SELECT "
				+ "	CAST('it' AS nchar(2)) AS LANGUAGE, "
				+ "	CAST('CM_SGQ_CICLI_TES' AS nchar(18)) AS TABLE_NAME, "
				+ "	CAST('SGQ_CICLI_TES' AS nvarchar(35)) AS DESCRIPTION, "
				+ "	CAST('Specifica:              ' + qc.SPECIFICA_1 + ' ' + qc.SPECIFICA_2 + CHAR(10) + 'Tipo Trattamento:       ' + qc.TIPO_TRAT + CHAR(10)  "
				+ "+ 'Materiale:              ' + qc.MATERIALE + CHAR(10) + 'Termocopia a contatto:  ' + "
				+ "	CASE COALESCE (qc.TT_TERMOC_CONT, "
				+ "	0) "
				+ "	WHEN 1 THEN 'SI' "
				+ "	ELSE 'NO' "
				+ "END + CHAR(10)  "
				+ "+ "
				+ "CASE "
				+ "	WHEN COALESCE (qc.NOTE, "
				+ "	'') <> '' THEN 'Note:                   ' + CHAR(10) + qc.NOTE + CHAR(10) "
				+ "	ELSE '' "
				+ "END AS nvarchar(1000)) AS NLS_COMMENT_TEXT, "
				+ "	CAST('N' AS nchar(1)) AS RESERVED, "
				+ "	CAST('0' AS nchar(1)) AS TEXT_TYPE, "
				+ "	NULL AS COMM_TYPE1_ID, "
				+ "	NULL AS COMM_TYPE2_ID, "
				+ "	CAST('SGQ_CICLI_TES' AS nchar(15))  "
				+ "AS R_COMMENT_USE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO oea "
				+ "INNER JOIN "
				+ "                      [Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_TT-SUPPLEMENTARE' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				setNote_C03_TT_SUPPLEMENTARE(rs.getString("NLS_COMMENT_TEXT"));
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(cs != null) {
					cs.free();
				}
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
	}
	
	public void recuperaNote_C03_TT_SUPPLEMENTARE_Map() {
		descrizioniFase_C03_TT_SUPPLEMENTARE = new HashMap<Integer, String>();
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + qc.ID_SEQUENZA AS ID_SEQUENZA_FASE, "
				+ "	CAST('it' AS nchar(2)) AS LANGUAGE, "
				+ "	CAST('CM_SGQ_CICLI_FAS' AS nchar(18)) AS TABLE_NAME, "
				+ "	CAST('SGQ_CICLI_FAS' AS nvarchar(35)) AS DESCRIPTION, "
				+ "	CAST('Gradiente Salita C°/h:  ' + COALESCE (cast(qc.TT_GRADIENTE_SAL_GRAD_C AS varchar), "
				+ "	'') + CHAR(10)  "
				+ "+ 'Salita C°:              ' + COALESCE (cast(qc.TT_SALITA_GRADI_C AS varchar), "
				+ "	'') + CHAR(10) + 'Permanenza h:           ' + COALESCE (cast(qc.TT_PERM AS varchar), "
				+ "	'') + CHAR(10)  "
				+ "+ 'Gradiente Discesa C°/h: ' + COALESCE (cast(qc.TT_GRADIENTE_RAF_GRAD_C AS varchar), "
				+ "	'') + CHAR(10)  "
				+ "+ 'Raffreddamento:         ' + "
				+ "	CASE COALESCE (qc.TT_RAFFREDDAMENTO, "
				+ "	0) "
				+ "	WHEN 0 THEN 'Polimero' "
				+ "	WHEN 1 THEN 'Aria' "
				+ "	WHEN 2 THEN 'Acqua' "
				+ "	WHEN 3 THEN 'Forno' "
				+ "	WHEN 4 THEN 'Olio' "
				+ "	ELSE '' "
				+ "END AS nvarchar(1000)) AS NLS_COMMENT_TEXT, "
				+ "	CAST('N' AS nchar(1)) AS RESERVED, "
				+ "	CAST('0' AS nchar(1)) AS TEXT_TYPE, "
				+ "	NULL AS COMM_TYPE1_ID, "
				+ "	NULL AS COMM_TYPE2_ID, "
				+ "	CAST('SGQ_CICLI_FAS' AS nchar(15))  "
				+ "AS R_COMMENT_USE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO oea "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		1 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_NOME_1 AS TT_NOME, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_1 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_SALITA_GRADI_C_1 AS TT_SALITA_GRADI_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_PERM_1 AS TT_PERM, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_1 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_RAFFREDDAMENTO_1 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		2 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_NOME_2 AS TT_NOME, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_2 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_SALITA_GRADI_C_2 AS TT_SALITA_GRADI_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_PERM_2 AS TT_PERM, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_2 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_RAFFREDDAMENTO_2 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		3 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_NOME_3 AS TT_NOME, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_3 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_SALITA_GRADI_C_3 AS TT_SALITA_GRADI_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_PERM_3 AS TT_PERM, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_3 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_RAFFREDDAMENTO_3 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		4 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_NOME_4 AS TT_NOME, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_4 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_SALITA_GRADI_C_4 AS TT_SALITA_GRADI_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_PERM_4 AS TT_PERM, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_4 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_RAFFREDDAMENTO_4 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		5 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_NOME_5 AS TT_NOME, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_5 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_SALITA_GRADI_C_5 AS TT_SALITA_GRADI_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_PERM_5 AS TT_PERM, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_5 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_RAFFREDDAMENTO_5 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		6 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_NOME_6 AS TT_NOME, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_GRADIENTE_SAL_GRAD_C_6 AS TT_GRADIENTE_SAL_GRAD_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_SALITA_GRADI_C_6 AS TT_SALITA_GRADI_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_PERM_6 AS TT_PERM, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_GRADIENTE_RAF_GRAD_C_6 AS TT_GRADIENTE_RAF_GRAD_C, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_RAFFREDDAMENTO_6 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM]) qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_TT-SUPPLEMENTARE' "
				+ "	AND COALESCE (qc.TT_NOME, "
				+ "	'') <> '' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				descrizioniFase_C03_TT_SUPPLEMENTARE.put(rs.getInt("ID_SEQUENZA_FASE"), rs.getString("NLS_COMMENT_TEXT"));
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(cs != null) {
					cs.free();
				}
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
	}
	
	public void recuperaNote_C06_CONTORLLO_INTERMEDIO_Map() {
		descrizioneFase_C06_CONTORLLO_INTERMEDIO = new HashMap<Integer, String>();
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + qc.ID_SEQUENZA AS ID_SEQUENZA_FASE , "
				+ "	CAST('it' AS nchar(2)) AS LANGUAGE, "
				+ "	CAST('CM_SGQ_CICLI_FAS' AS nchar(18)) AS TABLE_NAME, "
				+ "	CAST('SGQ_CICLI_FAS' AS nvarchar(35)) AS DESCRIPTION, "
				+ "	CAST "
				+ "	(CASE "
				+ "		WHEN COALESCE (qc.specifica_1, "
				+ "		'') = '' THEN '' "
				+ "		ELSE qc.specifica_1 + CHAR(10) "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN COALESCE (NDT_UT_CAL_AVG, "
				+ "		0)  "
				+ "<> 0 THEN 'Calibrazione: AVG' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN COALESCE (NDT_UT_BLOCCHI_CAL, "
				+ "		0) <> 0 THEN 'Calibrazione: Blocchi Calibrazione' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN COALESCE (NDT_UT_FBH, "
				+ "		0) <> 0 THEN 'Calibrazione: FBH - ' + COALESCE (CAST(NDT_UT_FBH_DIA AS nchar(6)), "
				+ "		'') + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN COALESCE (NDT_UT_CAL_ECO_RIF, "
				+ "		0) <> 0 THEN 'Calibrazione: Eco di riferimento' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END AS nvarchar(1000))  "
				+ "AS NLS_COMMENT_TEXT, "
				+ "	CAST('N' AS nchar(1)) AS RESERVED, "
				+ "	CAST('0' AS nchar(1)) AS TEXT_TYPE, "
				+ "	NULL AS COMM_TYPE1_ID, "
				+ "	NULL  "
				+ "AS COMM_TYPE2_ID, "
				+ "	CAST('SGQ_CICLI_FAS' AS nchar(15)) AS R_COMMENT_USE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO oea "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		2 AS ID_SEQUENZA, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM.ORD_CLI_RIGA, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.ORD_CLI_RIGA, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.ORD_CLI_RIGA, "
				+ "		QC_NDT_US_COMPL_F_1_RIC_FM.ORD_CLI_RIGA))) AS ORD_CLI_RIGA, "
				+ "		'Controllo UT Intermedio' AS DESCRIZIONE, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM.SPECIFICA_1, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.SPECIFICA_1, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.SPECIFICA_1, "
				+ "		QC_NDT_US_COMPL_F_1_RIC_FM.SPECIFICA_1))) AS SPECIFICA_1, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM.NDT_UT_CAL_AVG, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.NDT_UT_CAL_AVG, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.NDT_UT_CAL_AVG, "
				+ "		QC_NDT_US_COMPL_F_1_RIC_FM.NDT_UT_CAL_AVG))) AS NDT_UT_CAL_AVG, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM.NDT_UT_BLOCCHI_CAL, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.NDT_UT_BLOCCHI_CAL, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.NDT_UT_BLOCCHI_CAL, "
				+ "		QC_NDT_US_COMPL_F_1_RIC_FM.NDT_UT_BLOCCHI_CAL))) AS NDT_UT_BLOCCHI_CAL, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM.NDT_UT_FBH, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.NDT_UT_FBH, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.NDT_UT_FBH, "
				+ "		QC_NDT_US_COMPL_F_1_RIC_FM.NDT_UT_FBH))) AS NDT_UT_FBH, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM.NDT_UT_FBH_DIA, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.NDT_UT_FBH_DIA, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.NDT_UT_FBH_DIA, "
				+ "		QC_NDT_US_COMPL_F_1_RIC_FM.NDT_UT_FBH_DIA))) AS NDT_UT_FBH_DIA, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM.NDT_UT_CAL_ECO_RIF, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.NDT_UT_CAL_ECO_RIF, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.NDT_UT_CAL_ECO_RIF, "
				+ "		QC_NDT_US_COMPL_F_1_RIC_FM.NDT_UT_CAL_ECO_RIF))) AS NDT_UT_CAL_ECO_RIF, "
				+ "		NULL AS PM_DUR_MAC, "
				+ "		NULL AS PM_DUR_TIPO "
				+ "	FROM "
				+ "		Target.dbo.QC_CMTR QC_CMTR "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_STD_RIC_FM QC_NDT_US_STD_RIC_FM ON "
				+ "		QC_NDT_US_STD_RIC_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_COMPL_I_1_RIC_FM QC_NDT_US_COMPL_I_1_RIC_FM ON "
				+ "		QC_NDT_US_COMPL_I_1_RIC_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_STD_RIC_FM_2 QC_NDT_US_STD_RIC_FM_2 ON "
				+ "		QC_NDT_US_STD_RIC_FM_2.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_COMPL_F_1_RIC_FM QC_NDT_US_COMPL_F_1_RIC_FM ON "
				+ "		QC_NDT_US_COMPL_F_1_RIC_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA) qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C06_CONTROLLO_INTERMEDIO' "
				+ "	AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				descrizioneFase_C06_CONTORLLO_INTERMEDIO.put(rs.getInt("ID_SEQUENZA_FASE"), rs.getString("NLS_COMMENT_TEXT"));
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(cs != null) {
					cs.free();
				}
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
	}
	
	public void recuperaNote_C06_CONTORLLO_INTERMEDIO_1_Map() {
		descrizioneFase_C06_CONTORLLO_INTERMEDIO_1 = new HashMap<Integer, String>();
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + qc.ID_SEQUENZA AS ID_SEQUENZA_FASE , "
				+ "	CAST('it' AS nchar(2)) AS LANGUAGE, "
				+ "	CAST('CM_SGQ_CICLI_FAS' AS nchar(18)) AS TABLE_NAME, "
				+ "	CAST('SGQ_CICLI_FAS' AS nvarchar(35)) AS DESCRIPTION, "
				+ "	/*---------------------------------*/ "
				+ "	CAST "
				+ "	(CASE "
				+ "		WHEN COALESCE (qc.specifica_1, "
				+ "		'') = '' THEN '' "
				+ "		ELSE qc.specifica_1 + CHAR(10) "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN PM_DUR_MAC IS NOT NULL  "
				+ "THEN 'Metodo: ' + "
				+ "		CASE "
				+ "			PM_DUR_MAC WHEN 0 THEN 'ECOTIP' "
				+ "			WHEN 1 THEN 'ERNEST' "
				+ "			WHEN 2 THEN '3000Kg' "
				+ "			ELSE '' "
				+ "		END + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN COALESCE (PM_DUR_TIPO, "
				+ "		'') <> '' THEN 'Tipo: ' + PM_DUR_TIPO + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN PM_DUR_PZ_DA IS NOT NULL  "
				+ "THEN 'Valori Richiesti: Da ' + CAST(PM_DUR_PZ_DA AS varchar) + ' a ' + CAST(PM_DUR_PZ_A AS varchar) + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN COALESCE (PM_DUR_PZ_MIN, "
				+ "		0) <> 0 THEN 'Valori Richiesti: Minimo ' + CAST(PM_DUR_PZ_MIN AS varchar) + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN COALESCE (PM_DUR_PZ_MAX, "
				+ "		0) <> 0 THEN 'Valori Richiesti: Massimo ' + CAST(PM_DUR_PZ_MAX AS varchar) + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN COALESCE (PM_DUR_PZ_INF, "
				+ "		'') <> '' THEN 'Informativo: ' + PM_DUR_PZ_INF "
				+ "		ELSE '' "
				+ "	END AS nvarchar(1000)) AS NLS_COMMENT_TEXT, "
				+ "	/*-----------------------*/ "
				+ "	CAST('N' AS nchar(1)) AS RESERVED, "
				+ "	CAST('0' AS nchar(1)) AS TEXT_TYPE, "
				+ "	NULL AS COMM_TYPE1_ID, "
				+ "	NULL AS COMM_TYPE2_ID, "
				+ "	CAST('SGQ_CICLI_FAS' AS nchar(15)) AS R_COMMENT_USE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO oea "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		3 AS ID_SEQUENZA, "
				+ "		COALESCE (QC_PM_DUR_PZ_INT_FM.ORD_CLI_RIGA, "
				+ "		QC_PM_DUR_PZ_FM.ORD_CLI_RIGA) AS ORD_CLI_RIGA, "
				+ "		'Controllo Durezze' AS DESCRIZIONE, "
				+ "		COALESCE (QC_PM_DUR_PZ_INT_FM.SPECIFICA_1, "
				+ "		QC_PM_DUR_PZ_FM.SPECIFICA_1) AS SPECIFICA_1, "
				+ "		COALESCE (QC_PM_DUR_PZ_INT_FM.PM_DUR_MAC, "
				+ "		QC_PM_DUR_PZ_FM.PM_DUR_MAC) AS PM_DUR_MAC, "
				+ "		COALESCE (QC_PM_DUR_PZ_INT_FM.PM_DUR_TIPO, "
				+ "		QC_PM_DUR_PZ_FM.PM_DUR_TIPO) AS PM_DUR_TIPO, "
				+ "		COALESCE (QC_PM_DUR_PZ_INT_FM.PM_DUR_PZ_A, "
				+ "		QC_PM_DUR_PZ_FM.PM_DUR_PZ_A) AS PM_DUR_PZ_A, "
				+ "		COALESCE (QC_PM_DUR_PZ_INT_FM.PM_DUR_PZ_DA, "
				+ "		QC_PM_DUR_PZ_FM.PM_DUR_PZ_DA) AS PM_DUR_PZ_DA, "
				+ "		COALESCE (QC_PM_DUR_PZ_INT_FM.PM_DUR_PZ_MAX, "
				+ "		QC_PM_DUR_PZ_FM.PM_DUR_PZ_MAX) AS PM_DUR_PZ_MAX, "
				+ "		COALESCE (QC_PM_DUR_PZ_INT_FM.PM_DUR_PZ_MIN, "
				+ "		QC_PM_DUR_PZ_FM.PM_DUR_PZ_MIN) AS PM_DUR_PZ_MIN, "
				+ "		COALESCE (QC_PM_DUR_PZ_INT_FM.PM_DUR_PZ_INF, "
				+ "		QC_PM_DUR_PZ_FM.PM_DUR_PZ_INF) AS PM_DUR_PZ_INF "
				+ "	FROM "
				+ "		Target.dbo.QC_CMTR QC_CMTR "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_PM_DUR_PZ_INT_FM QC_PM_DUR_PZ_INT_FM ON "
				+ "		QC_PM_DUR_PZ_INT_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_PM_DUR_PZ_FM QC_PM_DUR_PZ_FM ON "
				+ "		QC_PM_DUR_PZ_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA) qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C06_CONTROLLO_INTERMEDIO' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				descrizioneFase_C06_CONTORLLO_INTERMEDIO_1.put(rs.getInt("ID_SEQUENZA_FASE"), rs.getString("NLS_COMMENT_TEXT"));
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(cs != null) {
					cs.free();
				}
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
	}

}
