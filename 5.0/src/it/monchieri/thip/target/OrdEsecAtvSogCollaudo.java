package it.monchieri.thip.target;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.KeyHelper;

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

	protected String iNoteC02_FUCINATURA;
	protected Map<Integer,String> descrizioneFase_C02_FUCINATURA = null;

	protected Map<Integer,String> descrizioneFase_C06_CONTROLLI_NDT = null;
	protected Map<Integer,String> descrizioneFase_C06_CONTROLLI_NDT_1 = null;
	protected Map<Integer,String> descrizioneFase_C06_CONTROLLI_NDT_2 = null;

	protected Map<Integer,String> descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION = null;
	protected Map<Integer,String> descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1 = null;
	public Map<Integer, String> getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION() {
		return descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION;
	}

	public void setDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION(
			Map<Integer, String> descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION) {
		this.descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION = descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION;
	}

	public Map<Integer, String> getDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1() {
		return descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1;
	}

	public void setDescrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1(
			Map<Integer, String> descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1) {
		this.descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1 = descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1;
	}

	public Map<Integer, String> getDescrizioneFase_C06_CONTROLLI_NDT_2() {
		return descrizioneFase_C06_CONTROLLI_NDT_2;
	}

	public void setDescrizioneFase_C06_CONTROLLI_NDT_2(Map<Integer, String> descrizioneFase_C06_CONTROLLI_NDT_2) {
		this.descrizioneFase_C06_CONTROLLI_NDT_2 = descrizioneFase_C06_CONTROLLI_NDT_2;
	}

	public Map<Integer, String> getDescrizioneFase_C06_CONTROLLI_NDT_1() {
		return descrizioneFase_C06_CONTROLLI_NDT_1;
	}

	public void setDescrizioneFase_C06_CONTROLLI_NDT_1(Map<Integer, String> descrizioneFase_C06_CONTROLLI_NDT_1) {
		this.descrizioneFase_C06_CONTROLLI_NDT_1 = descrizioneFase_C06_CONTROLLI_NDT_1;
	}

	public Map<Integer, String> getDescrizioneFase_C06_CONTROLLI_NDT() {
		return descrizioneFase_C06_CONTROLLI_NDT;
	}

	public void setDescrizioneFase_C06_CONTROLLI_NDT(Map<Integer, String> descrizioneFase_C06_CONTROLLI_NDT) {
		this.descrizioneFase_C06_CONTROLLI_NDT = descrizioneFase_C06_CONTROLLI_NDT;
	}

	public Map<Integer, String> getDescrizioneFase_C02_FUCINATURA() {
		return descrizioneFase_C02_FUCINATURA;
	}

	public void setDescrizioneFase_C02_FUCINATURA(Map<Integer, String> descrizioneFase_C02_FUCINATURA) {
		this.descrizioneFase_C02_FUCINATURA = descrizioneFase_C02_FUCINATURA;
	}

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

	protected Map<String, OrdEsecAtvSogCollaudoDatiCaratteristica> caratteristiche = new HashMap<String, OrdEsecAtvSogCollaudoDatiCaratteristica>();
	
	public Map<String, OrdEsecAtvSogCollaudoDatiCaratteristica> getCaratteristiche() {
		return caratteristiche;
	}

	public void setCaratteristiche(Map<String, OrdEsecAtvSogCollaudoDatiCaratteristica> caratteristiche) {
		this.caratteristiche = caratteristiche;
	}
	
	protected String iRiferimentoProceduraTtQualita = null;
	public String getRiferimentoProceduraTtQualita() {
		return iRiferimentoProceduraTtQualita;
	}
	public void setRiferimentoProceduraTtQualita(String iRiferimentoProceduraTtQualita) {
		this.iRiferimentoProceduraTtQualita = iRiferimentoProceduraTtQualita;
	}
	
	protected Map<String, String> noteFaseTtQualita = new HashMap<String, String>();
	public Map<String, String> getNoteFaseTtQualita() {
		return noteFaseTtQualita;
	}

	public void setNoteFaseTtQualita(Map<String, String> noteFaseTtQualita) {
		this.noteFaseTtQualita = noteFaseTtQualita;
	}
	
	protected String iRiferimentoProceduraControlloGrezzoPostForgia = null;
	public String getRiferimentoProceduraControlloGrezzoPostForgia() {
		return iRiferimentoProceduraControlloGrezzoPostForgia;
	}

	public void setRiferimentoProceduraControlloGrezzoPostForgia(String iRiferimentoProceduraControlloGrezzoPostForgia) {
		this.iRiferimentoProceduraControlloGrezzoPostForgia = iRiferimentoProceduraControlloGrezzoPostForgia;
	}
	
	protected String iRiferimentoProceduraControlloQualitativoPost = null;
	protected Map<String, String> noteFaseControlloQualitativoPost = new HashMap<String, String>();
	public String getRiferimentoProceduraControlloQualitativoPost() {
		return iRiferimentoProceduraControlloQualitativoPost;
	}

	public void setRiferimentoProceduraControlloQualitativoPost(String iRiferimentoProceduraControlloQualitativoPost) {
		this.iRiferimentoProceduraControlloQualitativoPost = iRiferimentoProceduraControlloQualitativoPost;
	}
	public Map<String, String> getNoteFaseControlloQualitativoPost() {
		return noteFaseControlloQualitativoPost;
	}

	public void setNoteFaseControlloQualitativoPost(Map<String, String> noteFaseControlloQualitativoPost) {
		this.noteFaseControlloQualitativoPost = noteFaseControlloQualitativoPost;
	}
	
	protected String iRiferimentoProeduraTtRicottura = null;
	public String getRiferimentoProeduraTtRicottura() {
		return iRiferimentoProeduraTtRicottura;
	}

	public void setRiferimentoProeduraTtRicottura(String iRiferimentoProeduraTtRicottura) {
		this.iRiferimentoProeduraTtRicottura = iRiferimentoProeduraTtRicottura;
	}
	
	protected Map<String, String> noteFaseControlloTtPreliminare = new HashMap<String, String>();
	public Map<String, String> getNoteFaseControlloTtPreliminare() {
		return noteFaseControlloTtPreliminare;
	}

	public void setNoteFaseControlloTtPreliminare(Map<String, String> noteFaseControlloTtPreliminare) {
		this.noteFaseControlloTtPreliminare = noteFaseControlloTtPreliminare;
	}
	
	protected String riferimentoProcedutaTtSupplementare = null;
	protected Map<String, String> noteFaseControlloTtSupplementare = new HashMap<String, String>();
	public String getRiferimentoProcedutaTtSupplementare() {
		return riferimentoProcedutaTtSupplementare;
	}

	public void setRiferimentoProcedutaTtSupplementare(String riferimentoProcedutaTtSupplementare) {
		this.riferimentoProcedutaTtSupplementare = riferimentoProcedutaTtSupplementare;
	}
	public Map<String, String> getNoteFaseControlloTtSupplementare() {
		return noteFaseControlloTtSupplementare;
	}

	public void setNoteFaseControlloTtSupplementare(Map<String, String> noteFaseControlloTtSupplementare) {
		this.noteFaseControlloTtSupplementare = noteFaseControlloTtSupplementare;
	}
	
	protected String riferimentoProceduraUtPreTT = null;
	public String getRiferimentoProceduraUtPreTT() {
		return riferimentoProceduraUtPreTT;
	}

	public void setRiferimentoProceduraUtPreTT(String riferimentoProceduraUtPreTT) {
		this.riferimentoProceduraUtPreTT = riferimentoProceduraUtPreTT;
	}

	protected Map<String, String> noteFaseControlloNDT = new HashMap<String, String>();
	public Map<String, String> getNoteFaseControlloNDT() {
		return noteFaseControlloNDT;
	}

	public void setNoteFaseControlloNDT(Map<String, String> noteFaseControlloNDT) {
		this.noteFaseControlloNDT = noteFaseControlloNDT;
	}
	
	protected String iRiferimentoProceduraControlloDurezzeDimensionali = null;
	public String getRiferimentoProceduraControlloDurezzeDimensionali() {
		return iRiferimentoProceduraControlloDurezzeDimensionali;
	}

	public void setRiferimentoProceduraControlloDurezzeDimensionali(
			String iRiferimentoProceduraControlloDurezzeDimensionali) {
		this.iRiferimentoProceduraControlloDurezzeDimensionali = iRiferimentoProceduraControlloDurezzeDimensionali;
	}
	
	protected String iRiferimentoProceduraControlloIntermedio = null;
	public String getRiferimentoProceduraControlloIntermedio() {
		return iRiferimentoProceduraControlloIntermedio;
	}

	public void setRiferimentoProceduraControlloIntermedio(String iRiferimentoProceduraControlloIntermedio) {
		this.iRiferimentoProceduraControlloIntermedio = iRiferimentoProceduraControlloIntermedio;
	}

	@Override
	public ErrorMessage checkDelete() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public static List<OrdEsecAtvSogCollaudo> listaCicliControlloDaImportare() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		List<OrdEsecAtvSogCollaudo> list = new ArrayList<OrdEsecAtvSogCollaudo>();
		String where = ""+OrdEsecAtvSogCollaudoTM.ID_PROGRESSIVO+" IS NULL AND R_COMMESSA = 'A9071001' ";
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
				+ "FROM PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN [Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] qc "
				+ "ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
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
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN [Target].[dbo].[QC_TT_QUALITA_RIC_FM] qc "
				+ "ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
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
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
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
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
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
				+ "FROM         PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea INNER JOIN "
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
				+ "qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
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
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                      [Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
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
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
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
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_TT-SUPPLEMENTARE' "
				+ "	AND COALESCE (qc.TT_NOME, "
				+ "	'') <> '' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			while(rs.next()) {
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
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
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
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
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
			while(rs.next()) {
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
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
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
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C06_CONTROLLO_INTERMEDIO' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			while(rs.next()) {
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

	public void leggiNote_C02_FUCINATURA() {
		descrizioneFase_C02_FUCINATURA = new HashMap<Integer, String>();
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + qc.ID_SEQUENZA as ID_SEQUENZA_FASE, "
				+ "	CAST('it' AS nchar(2)) AS LANGUAGE, "
				+ "	CAST('CM_SGQ_CICLI_FAS' AS nchar(18)) AS TABLE_NAME, "
				+ "	CAST(oea.ORP_EFF_DOC_ID AS nvarchar(35)) AS DESCRIPTION, "
				+ "	CAST(qc.NC_NOTE_FUCINATURA AS nvarchar(1000)) AS NLS_COMMENT_TEXT, "
				+ "	CAST('N' AS nchar(1)) AS RESERVED, "
				+ "	CAST('0' AS nchar(1)) AS TEXT_TYPE, "
				+ "	NULL AS COMM_TYPE1_ID, "
				+ "	NULL AS COMM_TYPE2_ID, "
				+ "	CAST('SGQ_CICLI_FAS' AS nchar(15)) AS R_COMMENT_USE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_ORP_20 oea "
				+ "INNER JOIN ( "
				+ "	SELECT "
				+ "		1 AS ID_SEQUENZA, "
				+ "			QC_FUC_RIC_RIL.ORD_CLI_RIGA, "
				+ "			QC_FUC_RIC_RIL.ORP_EFF_DOC_ID, "
				+ "			QC_FUC_RIC_RIL.NC_NOTE_FUCINATURA "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_FUC_RIC_RIL] "
				+ "	WHERE "
				+ "		COALESCE(QC_FUC_RIC_RIL.NC_NOTE_FUCINATURA, "
				+ "		'') <> '') qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "	AND qc.ORP_EFF_DOC_ID = oea.ORP_EFF_DOC_ID COLLATE Latin1_General_CS_AS "
				+ " WHERE "
				+ "	oea.NOME_TABELLA = 'C02_FUCINATURA' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			while(rs.next()) {
				descrizioneFase_C02_FUCINATURA.put(rs.getInt("ID_SEQUENZA_FASE"), rs.getString("NLS_COMMENT_TEXT"));
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

	public void leggiNote_C06_CONTROLLI_NDT() {
		descrizioneFase_C06_CONTROLLI_NDT = new HashMap<Integer, String>();
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + qc.ID_SEQUENZA AS ID_SEQUENZA_FASE, "
				+ "	CAST('it' AS nchar(2)) AS LANGUAGE, "
				+ "	CAST('CM_SGQ_CICLI_FAS' AS nchar(18)) AS TABLE_NAME, "
				+ "	CAST('SGQ_CICLI_FAS' AS nvarchar(35))  "
				+ "AS DESCRIPTION, "
				+ "	/*---------------------------------*/ "
				+ "	CAST "
				+ "	(CASE "
				+ "		WHEN COALESCE (qc.specifica_1, "
				+ "		'') = '' THEN '' "
				+ "		ELSE qc.specifica_1 + CHAR(10) "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN COALESCE (NDT_UT_CAL_AVG, "
				+ "		0) <> 0 THEN 'Calibrazione: AVG' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN COALESCE (NDT_UT_BLOCCHI_CAL, "
				+ "		0)  "
				+ "<> 0 THEN 'Calibrazione: Blocchi Calibrazione' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN COALESCE (NDT_UT_FBH, "
				+ "		0)  "
				+ "<> 0 THEN 'Calibrazione: FBH - ' + COALESCE (CAST(NDT_UT_FBH_DIA AS nchar(6)), "
				+ "		'') + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN COALESCE (NDT_UT_CAL_ECO_RIF, "
				+ "		0)  "
				+ "<> 0 THEN 'Calibrazione: Eco di riferimento' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END AS nvarchar(1000)) AS NLS_COMMENT_TEXT, "
				+ "	/*-----------------------*/ "
				+ "	CAST('N' AS nchar(1)) AS RESERVED, "
				+ "	CAST('0' AS nchar(1)) AS TEXT_TYPE, "
				+ "	NULL AS COMM_TYPE1_ID, "
				+ "	NULL AS COMM_TYPE2_ID, "
				+ "	CAST('SGQ_CICLI_FAS' AS nchar(15)) AS R_COMMENT_USE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		1 AS ID_SEQUENZA, "
				+ "		COALESCE (QC_NDT_US_COMPL_F_1_RIC_FM.ORD_CLI_RIGA, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.ORD_CLI_RIGA, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_NDT_US_STD_RIC_FM.ORD_CLI_RIGA))) AS ORD_CLI_RIGA, "
				+ "		'Controlli NDT - Ultrasuoni' AS DESCRIZIONE, "
				+ "		COALESCE (QC_NDT_US_COMPL_F_1_RIC_FM.SPECIFICA_1, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.SPECIFICA_1, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.SPECIFICA_1, "
				+ "		QC_NDT_US_STD_RIC_FM.SPECIFICA_1))) AS SPECIFICA_1, "
				+ "		COALESCE (QC_NDT_US_COMPL_F_1_RIC_FM.NDT_UT_CAL_AVG, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.NDT_UT_CAL_AVG, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.NDT_UT_CAL_AVG, "
				+ "		QC_NDT_US_STD_RIC_FM.NDT_UT_CAL_AVG))) AS NDT_UT_CAL_AVG, "
				+ "		COALESCE (QC_NDT_US_COMPL_F_1_RIC_FM.NDT_UT_BLOCCHI_CAL, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.NDT_UT_BLOCCHI_CAL, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.NDT_UT_BLOCCHI_CAL, "
				+ "		QC_NDT_US_STD_RIC_FM.NDT_UT_BLOCCHI_CAL))) AS NDT_UT_BLOCCHI_CAL, "
				+ "		COALESCE (QC_NDT_US_COMPL_F_1_RIC_FM.NDT_UT_FBH, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.NDT_UT_FBH, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.NDT_UT_FBH, "
				+ "		QC_NDT_US_STD_RIC_FM.NDT_UT_FBH))) AS NDT_UT_FBH, "
				+ "		COALESCE (QC_NDT_US_COMPL_F_1_RIC_FM.NDT_UT_FBH_DIA, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.NDT_UT_FBH_DIA, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.NDT_UT_FBH_DIA, "
				+ "		QC_NDT_US_STD_RIC_FM.NDT_UT_FBH_DIA))) AS NDT_UT_FBH_DIA, "
				+ "		COALESCE (QC_NDT_US_COMPL_F_1_RIC_FM.NDT_UT_CAL_ECO_RIF, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.NDT_UT_CAL_ECO_RIF, "
				+ "		COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.NDT_UT_CAL_ECO_RIF, "
				+ "		QC_NDT_US_STD_RIC_FM.NDT_UT_CAL_ECO_RIF))) AS NDT_UT_CAL_ECO_RIF, "
				+ "		NULL AS PM_DUR_MAC, "
				+ "		NULL AS PM_DUR_TIPO "
				+ "	FROM "
				+ "		Target.dbo.QC_CMTR QC_CMTR "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_COMPL_F_1_RIC_FM QC_NDT_US_COMPL_F_1_RIC_FM ON "
				+ "		QC_NDT_US_COMPL_F_1_RIC_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_STD_RIC_FM_2 QC_NDT_US_STD_RIC_FM_2 ON "
				+ "		QC_NDT_US_STD_RIC_FM_2.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_COMPL_I_1_RIC_FM QC_NDT_US_COMPL_I_1_RIC_FM ON "
				+ "		QC_NDT_US_COMPL_I_1_RIC_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_STD_RIC_FM QC_NDT_US_STD_RIC_FM ON "
				+ "		QC_NDT_US_STD_RIC_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA) qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C06_CONTROLLI_NDT' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			while(rs.next()) {
				descrizioneFase_C06_CONTROLLI_NDT.put(rs.getInt("ID_SEQUENZA_FASE"), rs.getString("NLS_COMMENT_TEXT"));
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

	public void leggiNote_C06_CONTROLLI_NDT_1() {
		descrizioneFase_C06_CONTROLLI_NDT_1 = new HashMap<Integer, String>();
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + qc.ID_SEQUENZA AS ID_SEQUENZA_FASE, "
				+ "	CAST('it' AS nchar(2)) AS LANGUAGE, "
				+ "	CAST('CM_SGQ_CICLI_FAS' AS nchar(18)) AS TABLE_NAME, "
				+ "	CAST('SGQ_CICLI_FAS' AS nvarchar(35))  "
				+ "AS DESCRIPTION, "
				+ "	/*---------------------------------*/ "
				+ "	CAST "
				+ "	(CASE "
				+ "		WHEN COALESCE (qc.specifica_1, "
				+ "		'') = '' THEN '' "
				+ "		ELSE qc.specifica_1 + CHAR(10) "
				+ "	END + 'Metodo: ' + "
				+ "	CASE "
				+ "		WHEN NDT_MT_YOKE = 1 THEN 'Yoke' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN NDT_MT_PUNT = 1 THEN 'Puntali' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN NDT_MT_COND = 1 THEN 'Conduttore Centrale' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + 'Strumenti: ' + "
				+ "	CASE "
				+ "		WHEN NDT_MT_LAMP_BIA = 1 THEN 'Luce Bianca' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN NDT_MT_LAMP_NERA = 1 THEN 'Luce Nera' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END AS nvarchar(1000)) AS NLS_COMMENT_TEXT, "
				+ "	/*-----------------------*/ "
				+ "	CAST('N' AS nchar(1)) AS RESERVED, "
				+ "	CAST('0' AS nchar(1)) AS TEXT_TYPE, "
				+ "	NULL AS COMM_TYPE1_ID, "
				+ "	NULL AS COMM_TYPE2_ID, "
				+ "	CAST('SGQ_CICLI_FAS' AS nchar(15)) AS R_COMMENT_USE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		2 AS ID_SEQUENZA, "
				+ "		QC_NDT_MT_RIC_FM.ORD_CLI_RIGA AS ORD_CLI_RIGA, "
				+ "		'Controlli NDT - Magnetico' AS DESCRIZIONE, "
				+ "		QC_NDT_MT_RIC_FM.SPECIFICA_1 AS SPECIFICA_1, "
				+ "		COALESCE (QC_NDT_MT_RIC_FM.NDT_MT_YOKE, "
				+ "		0) AS NDT_MT_YOKE, "
				+ "		COALESCE (QC_NDT_MT_RIC_FM.NDT_MT_PUNT, "
				+ "		0) AS NDT_MT_PUNT, "
				+ "		COALESCE (QC_NDT_MT_RIC_FM.NDT_MT_COND, "
				+ "		0) AS NDT_MT_COND, "
				+ "		COALESCE (QC_NDT_MT_RIC_FM.NDT_MT_LAMP_BIA, "
				+ "		0) AS NDT_MT_LAMP_BIA, "
				+ "		COALESCE (QC_NDT_MT_RIC_FM.NDT_MT_LAMP_NERA, "
				+ "		0)  "
				+ "                                                   AS NDT_MT_LAMP_NERA "
				+ "	FROM "
				+ "		Target.dbo.QC_CMTR QC_CMTR "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_MT_RIC_FM QC_NDT_MT_RIC_FM ON "
				+ "		QC_NDT_MT_RIC_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA) qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C06_CONTROLLI_NDT' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			while(rs.next()) {
				descrizioneFase_C06_CONTROLLI_NDT_1.put(rs.getInt("ID_SEQUENZA_FASE"), rs.getString("NLS_COMMENT_TEXT"));
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

	public void leggiNote_C06_CONTROLLI_NDT_2() {
		descrizioneFase_C06_CONTROLLI_NDT_2 = new HashMap<Integer, String>();
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + qc.ID_SEQUENZA AS ID_SEQUENZA_FASE, "
				+ "	CAST('it' AS nchar(2)) AS LANGUAGE, "
				+ "	CAST('CM_SGQ_CICLI_FAS' AS nchar(18)) AS TABLE_NAME, "
				+ "	CAST('SGQ_CICLI_FAS' AS nvarchar(35))  "
				+ "AS DESCRIPTION, "
				+ "	/*---------------------------------*/ "
				+ "	CAST "
				+ "	(CASE "
				+ "		WHEN COALESCE (qc.specifica_1, "
				+ "		'') = '' THEN '' "
				+ "		ELSE qc.specifica_1 + CHAR(10) "
				+ "	END + 'Metodo: ' + "
				+ "	CASE "
				+ "		WHEN NDT_LP_SOL_WATER = 1 THEN 'Solubile in acqua' "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN NDT_LP_SOL_WATER_VIS = 1 THEN ' Visibile' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN NDT_LP_SOL_WATER_FLUOR = 1 THEN ' Fluorescente' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN NDT_LP_SOL_SOLV = 1 THEN 'Solubile Solvente' "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN NDT_LP_SOL_SOLV_VIS = 1 THEN ' Visibile' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END + "
				+ "	CASE "
				+ "		WHEN NDT_LP_SOL_SOLV_FLUOR = 1 THEN ' Fluorescente' + CHAR(10) "
				+ "		ELSE '' "
				+ "	END AS nvarchar(1000)) AS NLS_COMMENT_TEXT, "
				+ "	/*-----------------------*/ "
				+ "	CAST('N' AS nchar(1)) AS RESERVED, "
				+ "	CAST('0' AS nchar(1)) AS TEXT_TYPE, "
				+ "	NULL AS COMM_TYPE1_ID, "
				+ "	NULL AS COMM_TYPE2_ID, "
				+ "	CAST('SGQ_CICLI_FAS' AS nchar(15)) AS R_COMMENT_USE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		3 AS ID_SEQUENZA, "
				+ "		QC_NDT_LP_RIC_FM.ORD_CLI_RIGA AS ORD_CLI_RIGA, "
				+ "		'Controlli NDT - Magnetico' AS DESCRIZIONE, "
				+ "		QC_NDT_LP_RIC_FM.SPECIFICA_1 AS SPECIFICA_1, "
				+ "		COALESCE (QC_NDT_LP_RIC_FM.NDT_LP_SOL_WATER, "
				+ "		0) AS NDT_LP_SOL_WATER, "
				+ "		COALESCE (QC_NDT_LP_RIC_FM.NDT_LP_SOL_WATER_VIS, "
				+ "		0) AS NDT_LP_SOL_WATER_VIS, "
				+ "		COALESCE (QC_NDT_LP_RIC_FM.NDT_LP_SOL_WATER_FLUOR, "
				+ "		0) AS NDT_LP_SOL_WATER_FLUOR, "
				+ "		COALESCE (QC_NDT_LP_RIC_FM.NDT_LP_SOL_SOLV, "
				+ "		0) AS NDT_LP_SOL_SOLV, "
				+ "		COALESCE (QC_NDT_LP_RIC_FM.NDT_LP_SOL_SOLV_VIS, "
				+ "		0)  "
				+ "                                                   AS NDT_LP_SOL_SOLV_VIS, "
				+ "		COALESCE (QC_NDT_LP_RIC_FM.NDT_LP_SOL_SOLV_FLUOR, "
				+ "		0) AS NDT_LP_SOL_SOLV_FLUOR "
				+ "	FROM "
				+ "		Target.dbo.QC_CMTR QC_CMTR "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_LP_RIC_FM QC_NDT_LP_RIC_FM ON "
				+ "		QC_NDT_LP_RIC_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA) qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C06_CONTROLLI_NDT' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			while(rs.next()) {
				descrizioneFase_C06_CONTROLLI_NDT_2.put(rs.getInt("ID_SEQUENZA_FASE"), rs.getString("NLS_COMMENT_TEXT"));
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

	public void leggiNote_C06_CONTROLLO_DUREZZE_DIMENSION() {
		descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION = new HashMap<Integer, String>();
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + qc.ID_SEQUENZA AS ID_SEQUENZA_FASE, "
				+ "	CAST('it' AS nchar(2)) AS LANGUAGE, "
				+ "	CAST('CM_SGQ_CICLI_FAS' AS nchar(18)) AS TABLE_NAME, "
				+ "	CAST('SGQ_CICLI_FAS' AS nvarchar(35)) AS DESCRIPTION, "
				+ "	/*---------------------------------*/ "
				+ "	CAST(qc.specifica_1 + CHAR(10) AS nvarchar(1000)) AS NLS_COMMENT_TEXT, "
				+ "	/*-----------------------*/ "
				+ "	CAST('N' AS nchar(1)) AS RESERVED, "
				+ "	CAST('0' AS nchar(1)) AS TEXT_TYPE, "
				+ "	NULL AS COMM_TYPE1_ID, "
				+ "	NULL AS COMM_TYPE2_ID, "
				+ "	CAST('SGQ_CICLI_FAS' AS nchar(15)) AS R_COMMENT_USE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		1 AS ID_SEQUENZA, "
				+ "		QC_NDT_DIM_RIC_FM.ORD_CLI_RIGA AS ORD_CLI_RIGA, "
				+ "		'Controllo Dimensionale' AS DESCRIZIONE, "
				+ "		'Specifica: ' + QC_NDT_DIM_RIC_FM.SPECIFICA_1 AS SPECIFICA_1 "
				+ "	FROM "
				+ "		Target.dbo.QC_NDT_DIM_RIC_FM) qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C06_CONTROLLO_DUREZZE-DIMENSION' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			while(rs.next()) {
				descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION.put(rs.getInt("ID_SEQUENZA_FASE"), rs.getString("NLS_COMMENT_TEXT"));
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

	public void leggiNote_C06_CONTROLLO_DUREZZE_DIMENSION_1() {
		descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1 = new HashMap<Integer, String>();
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + qc.ID_SEQUENZA AS ID_SEQUENZA_FASE, "
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
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		2 AS ID_SEQUENZA, "
				+ "		QC_PM_DUR_PZ_RIC.ORD_CLI_RIGA AS ORD_CLI_RIGA, "
				+ "		'Controllo Durezze' AS DESCRIZIONE, "
				+ "		QC_PM_DUR_PZ_RIC.SPECIFICA_1 AS SPECIFICA_1, "
				+ "		QC_PM_DUR_PZ_RIC.PM_DUR_MAC AS PM_DUR_MAC, "
				+ "		QC_PM_DUR_PZ_RIC.PM_DUR_TIPO AS PM_DUR_TIPO, "
				+ "		QC_PM_DUR_PZ_RIC.PM_DUR_PZ_A AS PM_DUR_PZ_A, "
				+ "		QC_PM_DUR_PZ_RIC.PM_DUR_PZ_DA AS PM_DUR_PZ_DA, "
				+ "		QC_PM_DUR_PZ_RIC.PM_DUR_PZ_MAX AS PM_DUR_PZ_MAX, "
				+ "		QC_PM_DUR_PZ_RIC.PM_DUR_PZ_MIN AS PM_DUR_PZ_MIN, "
				+ "		QC_PM_DUR_PZ_RIC.PM_DUR_PZ_INF AS PM_DUR_PZ_INF "
				+ "	FROM "
				+ "		Target.dbo.QC_PM_DUR_PZ_RIC QC_PM_DUR_PZ_RIC) qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C06_CONTROLLO_DUREZZE-DIMENSION' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			while(rs.next()) {
				descrizioneFase_C06_CONTROLLO_DUREZZE_DIMENSION_1.put(rs.getInt("ID_SEQUENZA_FASE"), rs.getString("NLS_COMMENT_TEXT"));
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

	public void leggiCaratteristiche(String query) throws SQLException{
		OrdEsecAtvSogCollaudoDatiCaratteristicaRsIterator rsIterator = null;
		ResultSet rs = null;
		try {
			query += " AND oea.R_COMMESSA = '"+getCommessa().trim()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga().trim()+"' ";
			PreparedStatement ps = ConnectionManager.getCurrentConnection().prepareStatement(query);
			rs = ps.executeQuery();
			rsIterator = new OrdEsecAtvSogCollaudoDatiCaratteristicaRsIterator(rs);
			while(rsIterator.hasNext()) {
				OrdEsecAtvSogCollaudoDatiCaratteristica carat = (OrdEsecAtvSogCollaudoDatiCaratteristica) rsIterator.next();
				String key = KeyHelper.buildObjectKey(new String[] {carat.getIdSequenzaFase().toString(),carat.getIdSequenzaCar().toString()});
				caratteristiche.put(key, carat);
			}
		}finally {
			if(rsIterator != null) {
				try {
					rsIterator.closeCursor();
				} catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
			}
		}
	}
	
	public void leggiRiferimentoProceduraTtQualita() {
		String stmt = "SELECT "
				+ "	CAST(qc.SPECIFICA_1 AS nchar(35)) AS RIF_PROCEDURA, "
				+ "	CAST(qc.NOTE AS nvarchar(250)) AS NOTE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                      [Target].[dbo].[QC_TT_QUALITA_RIC_FM] qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CI_AS "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_TT-QUALITA' "
				+ "	AND ocru.FLAG_GENERATO_CERTIFICATO = 1 AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				setRiferimentoProceduraTtQualita(rs.getString("RIF_PROCEDURA"));
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
	
	public void leggiRiferimentoProceduraControlloQualitativoPost() {
		String stmt = "SELECT "
				+ "	CAST(qc.SPECIFICA_1 AS nchar(35)) AS RIF_PROCEDURA, "
				+ "	CAST(qc.NOTE AS nvarchar(250)) AS NOTE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                      [Target].[dbo].[QC_TT_QUALITA_RIC] qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_CONTROLLO_QUALITATIVO_POST' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				setRiferimentoProceduraControlloQualitativoPost(rs.getString("RIF_PROCEDURA"));
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
	
	public void leggiRiferimentoProceduraControlloGrezzoPostForgia() {
		String stmt = "SELECT "
				+ "	CAST(qc.CMTR_SPECIFICA AS nchar(35)) AS RIF_PROCEDURA, "
				+ "	CAST(NULL AS nvarchar(250)) AS NOTE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                      [Target].[dbo].QC_CMTR qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CI_AS "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_CONTROLLO_GREZZO_POST_FORGI' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				setRiferimentoProceduraControlloGrezzoPostForgia(rs.getString("RIF_PROCEDURA"));
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
	
	public void leggiRiferimentoProceduraTtRicottura() {
		String stmt = "SELECT "
				+ "	CAST(qc.CMTR_SPECIFICA AS nchar(35)) AS RIF_PROCEDURA, "
				+ "	CAST(NULL AS nvarchar(250)) AS NOTE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                      [Target].[dbo].QC_CMTR qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_TT_DI_RICOTTURA' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				setRiferimentoProeduraTtRicottura(rs.getString("RIF_PROCEDURA"));
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
	
	public void leggiRiferimentoProcedutaTtSupplementare() {
		String stmt = "SELECT "
				+ "	CAST(qc.SPECIFICA_1 AS nchar(35)) AS RIF_PROCEDURA, "
				+ "	CAST(qc.NOTE AS nvarchar(250)) AS NOTE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                      [Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_TT-SUPPLEMENTARE' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				setRiferimentoProcedutaTtSupplementare(rs.getString("RIF_PROCEDURA"));
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
	
	public void leggiRiferimentoProcedutaUtPreTT() {
		String stmt = "SELECT "
				+ "	CAST(qc.CMTR_SPECIFICA AS nchar(35)) AS RIF_PROCEDURA, "
				+ "	CAST(qc.NOTE AS nvarchar(250)) AS NOTE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                      [Target].[dbo].[QC_CMTR] qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_UT_PRE-TT' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				setRiferimentoProceduraUtPreTT(rs.getString("RIF_PROCEDURA"));
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
	
	public void leggiRiferimentoProcedutaControlloIntermedio() {
		String stmt = "SELECT "
				+ "	CAST(RIF_PROCEDURA AS nchar(35)) AS RIF_PROCEDURA, "
				+ "	CAST(qc.NOTE AS nvarchar(250)) AS NOTE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		QC_CMTR.ORD_CLI_RIGA AS ORD_CLI_RIGA, "
				+ "		QC_CMTR.DATA AS DATA, "
				+ "		QC_CMTR.NOTE AS NOTE, "
				+ "		QC_CMTR.CMTR_SPECIFICA AS RIF_PROCEDURA "
				+ "	FROM "
				+ "		target.dbo.QC_CMTR) qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
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
				setRiferimentoProceduraControlloIntermedio(rs.getString("RIF_PROCEDURA"));
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
	
	public void leggiRiferimentoProcedutaControlloDurezzeDimensionali() {
		String stmt = "SELECT "
				+ "	CAST(RIF_PROCEDURA AS nchar(35)) AS RIF_PROCEDURA, "
				+ "	CAST(qc.NOTE AS nvarchar(250)) AS NOTE "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		QC_CMTR.ORD_CLI_RIGA AS ORD_CLI_RIGA, "
				+ "		QC_CMTR.DATA AS DATA, "
				+ "		QC_CMTR.NOTE AS NOTE, "
				+ "		QC_CMTR.CMTR_SPECIFICA AS RIF_PROCEDURA "
				+ "	FROM "
				+ "		target.dbo.QC_CMTR) qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C06_CONTROLLO_DUREZZE-DIMENSION' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			if(rs.next()) {
				setRiferimentoProceduraControlloDurezzeDimensionali(rs.getString("RIF_PROCEDURA"));
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
	
	public void leggiNoteFaseTtQualita() {
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + c.ID_SEQUENZA AS ID_SEQUENZA_FASE, "
				+ "	CAST(c.TT_NOME AS nvarchar(35)) AS DESCRIZIONE, "
				+ "	CAST('TT_QUALITA' AS nchar(15)) AS DESCR_RIDOTTA, "
				+ "	(oea.SEQ_VISUALIZZ - 1) + c.ID_SEQUENZA AS SEQ_VISUALIZZ, "
				+ "	CAST(oea.R_ARTICOLO AS nchar(25)) AS R_ARTICOLO, "
				+ "	CAST(oea.R_ATTIVITA AS nchar(15)) AS R_ATTIVITA_LAV, "
				+ "	CAST('1' AS nchar(1)) AS CARATTERISTICHE, "
				+ "	CAST('1' AS nchar(1)) AS RACCOLTA_DATI, "
				+ "	NULL  "
				+ "AS DATA_INZ_VALID, "
				+ "	NULL AS DATA_FIN_VALID, "
				+ "	NULL AS R_VER_INZ_VALID, "
				+ "	NULL AS R_VER_FIN_VALID, "
				+ "	NULL AS R_REGOLA, "
				+ "	NULL AS TEMPO_CTR, "
				+ "	CAST('0' AS nchar(1))  "
				+ "AS TIPO_CRIT_VALID, "
				+ "	NULL AS R_UM_TEMPO_CTR, "
				+ "	NULL AS TEMPO_ATR_CTR, "
				+ "	NULL AS R_UM_TEMPO_ATR_CTR, "
				+ "	NULL AS COSTO_CTR, "
				+ "	NULL AS R_VALUTA, "
				+ "	CAST('2' AS nchar(1)) AS CAMPIONAMENTO, "
				+ "	NULL AS FREQUENZA_CTR, "
				+ "	CAST('P' AS nchar(1)) AS TIPO_FREQUENZA_CTR, "
				+ "	CAST('N' AS nchar(1)) AS VAL_NOM_OBBL, "
				+ "	CAST('N' AS nchar(1)) AS CTR_ESTERNO, "
				+ "	NULL AS R_FORNITORE, "
				+ "	substring(oea.FASE_OBBL, ROW_NUMBER() OVER (PARTITION BY COALESCE (oea.ID_PROGRESSIVO,  "
				+ "oea.ID_ORIGINALE) "
				+ "ORDER BY c.ID_SEQUENZA), 1) AS FASE_OBBL, "
				+ "	CAST('Y' AS nchar(1)) AS STAMPA_FASE, "
				+ "	CAST('Raffreddamento: ' + "
				+ "	CASE "
				+ "		c.TT_RAFFREDDAMENTO WHEN 0 THEN 'Polimero' "
				+ "		WHEN 1 THEN 'Aria' "
				+ "		WHEN 2 THEN 'Acqua' "
				+ "		WHEN 3 THEN 'Forno' "
				+ "		WHEN 4 THEN 'Olio' "
				+ "		ELSE "
				+ " '' "
				+ "	END AS nchar(50)) AS NOTE, "
				+ "	/*SOSTITUIRE STRINCA CON CAMPO TARGET*/ "
				+ "	oea.QTA_CTR AS QTA_CTR, "
				+ "	oea.PERC_CTR AS PERC_CTR, "
				+ "	0 AS NUM_DIF_ACC, "
				+ "	1 AS NUM_DIF_RIF, "
				+ "	CAST('1' AS nchar(1)) AS TIPO_CAMPIONAMENTO, "
				+ "	CAST('2' AS nchar(1)) AS LIV_CAMPIONAMENTO, "
				+ "	NULL AS COEF_LQA, "
				+ "	CAST('6' AS nchar(1))  "
				+ "AS LIV_ISPEZIONE, "
				+ "	NULL AS R_GES_COMMENTI, "
				+ "	NULL AS R_DOCUMENTO_MM, "
				+ "	CAST('1' AS nchar(1)) AS NORM_CARTA_CTR, "
				+ "	NULL AS NUM_CAMPIONE, "
				+ "	NULL  "
				+ "AS FREQ_MINUTI, "
				+ "	NULL AS FREQ_PEZZI, "
				+ "	CAST('N' AS nchar(1)) AS LIMITI_ATTESI, "
				+ "	CAST('N' AS nchar(1)) AS RIF_CENTRO_TOLL, "
				+ "	NULL AS R_P_REAZIONE, "
				+ "	CAST('V' AS nchar(1))  "
				+ "AS STATO, "
				+ "	CAST('' AS nchar(20)) AS R_UTENTE_CRZ, "
				+ "	CAST('' AS nchar(20)) AS R_UTENTE_AGG, "
				+ "	CURRENT_TIMESTAMP AS TIMESTAMP_CRZ, "
				+ "	CURRENT_TIMESTAMP AS TIMESTAMP_AGG, "
				+ "	NULL AS R_CAT_DOC_DGT, "
				+ "	NULL AS R_TP_DOC_DGT, "
				+ "	NULL AS R_DOC_DGT, "
				+ "	NULL AS R_VERS_DOC_DGT, "
				+ "	CAST('Y' AS nchar(1)) "
				+ " AS VISUAL_IDENT "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                      [Target].[dbo].[QC_TT_QUALITA_RIC_FM] qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CI_AS "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA COLLATE Latin1_General_CI_AS "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		1 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		'1-Trattamento Termico' AS TT_NOME, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_1 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		2 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		'2-Trattamento Termico' AS TT_NOME, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_2 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		3 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		'3-Trattamento Termico' AS TT_NOME, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_3 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		4 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		'4-Trattamento Termico' AS TT_NOME, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_4 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		5 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_NOME_5 AS TT_NOME, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_5 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		6 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_NOME_6 AS TT_NOME, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_6 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		6 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_NOME_6 AS TT_NOME, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_6 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		7 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		'Durezza Kg' AS TT_NOME, "
				+ "		NULL AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		8 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		'Dimensionale post LM1' AS DESCRIZIONE, "
				+ "		NULL AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM]) c ON "
				+ "	qc.ORD_CLI_RIGA = c.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_TT-QUALITA' "
				+ "	AND ocru.FLAG_GENERATO_CERTIFICATO = 1 "
				+ "	AND COALESCE (c.TT_NOME, "
				+ "	'') <> '' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			while(rs.next()) {
				noteFaseTtQualita.put(String.valueOf(rs.getInt("ID_SEQUENZA_FASE")), rs.getString("NOTE").trim());
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
	
	public void leggiNoteFaseControlloQualitativoPost() {
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + c.ID_SEQUENZA AS ID_SEQUENZA_FASE, "
				+ "	CAST(c.TT_NOME AS nvarchar(35)) AS DESCRIZIONE, "
				+ "	CAST(c.DESCR_RIDOTTA AS nchar(15)) AS DESCR_RIDOTTA, "
				+ "	(oea.SEQ_VISUALIZZ - 1) + c.ID_SEQUENZA AS SEQ_VISUALIZZ, "
				+ "	CAST(oea.R_ARTICOLO AS nchar(25))  "
				+ "AS R_ARTICOLO, "
				+ "	CAST(oea.R_ATTIVITA AS nchar(15)) AS R_ATTIVITA_LAV, "
				+ "	CAST('1' AS nchar(1)) AS CARATTERISTICHE, "
				+ "	CAST('1' AS nchar(1)) AS RACCOLTA_DATI, "
				+ "	NULL  "
				+ "AS DATA_INZ_VALID, "
				+ "	NULL AS DATA_FIN_VALID, "
				+ "	NULL AS R_VER_INZ_VALID, "
				+ "	NULL AS R_VER_FIN_VALID, "
				+ "	NULL AS R_REGOLA, "
				+ "	NULL AS TEMPO_CTR, "
				+ "	CAST('0' AS nchar(1))  "
				+ "AS TIPO_CRIT_VALID, "
				+ "	NULL AS R_UM_TEMPO_CTR, "
				+ "	NULL AS TEMPO_ATR_CTR, "
				+ "	NULL AS R_UM_TEMPO_ATR_CTR, "
				+ "	NULL AS COSTO_CTR, "
				+ "	NULL AS R_VALUTA, "
				+ "	CAST('2' AS nchar(1)) AS CAMPIONAMENTO, "
				+ "	NULL AS FREQUENZA_CTR, "
				+ "	CAST('P' AS nchar(1)) AS TIPO_FREQUENZA_CTR, "
				+ "	CAST('N' AS nchar(1)) AS VAL_NOM_OBBL, "
				+ "	CAST('N' AS nchar(1)) AS CTR_ESTERNO, "
				+ "	NULL AS R_FORNITORE, "
				+ "	substring(oea.FASE_OBBL, ROW_NUMBER() OVER (PARTITION BY COALESCE (oea.ID_PROGRESSIVO,  "
				+ "oea.ID_ORIGINALE) "
				+ "ORDER BY c.ID_SEQUENZA), 1) AS FASE_OBBL, "
				+ "	CAST(c.STAMPA_FASE AS nchar(1)) AS STAMPA_FASE, "
				+ "	CAST('Raffreddamento: ' + "
				+ "	CASE "
				+ "		c.TT_RAFFREDDAMENTO WHEN 0 THEN 'Polimero' "
				+ "		WHEN 1 THEN 'Aria' "
				+ "		WHEN 2 THEN 'Acqua' "
				+ "		WHEN 3 THEN 'Forno' "
				+ "		WHEN 4 THEN 'Olio' "
				+ "		ELSE "
				+ " '' "
				+ "	END AS nchar(50)) AS NOTE, "
				+ "	/*SOSTITUIRE STRINCA CON CAMPO TARGET*/ "
				+ "	oea.QTA_CTR AS QTA_CTR, "
				+ "	oea.PERC_CTR AS PERC_CTR, "
				+ "	0 AS NUM_DIF_ACC, "
				+ "	1 AS NUM_DIF_RIF, "
				+ "	CAST('1' AS nchar(1)) AS TIPO_CAMPIONAMENTO, "
				+ "	CAST('2' AS nchar(1)) AS LIV_CAMPIONAMENTO, "
				+ "	NULL AS COEF_LQA, "
				+ "	CAST('6' AS nchar(1))  "
				+ "AS LIV_ISPEZIONE, "
				+ "	NULL AS R_GES_COMMENTI, "
				+ "	NULL AS R_DOCUMENTO_MM, "
				+ "	CAST('1' AS nchar(1)) AS NORM_CARTA_CTR, "
				+ "	NULL AS NUM_CAMPIONE, "
				+ "	NULL  "
				+ "AS FREQ_MINUTI, "
				+ "	NULL AS FREQ_PEZZI, "
				+ "	CAST('N' AS nchar(1)) AS LIMITI_ATTESI, "
				+ "	CAST('N' AS nchar(1)) AS RIF_CENTRO_TOLL, "
				+ "	NULL AS R_P_REAZIONE, "
				+ "	CAST('V' AS nchar(1))  "
				+ "AS STATO, "
				+ "	CAST('' AS nchar(20)) AS R_UTENTE_CRZ, "
				+ "	CAST('' AS nchar(20)) AS R_UTENTE_AGG, "
				+ "	CURRENT_TIMESTAMP AS TIMESTAMP_CRZ, "
				+ "	CURRENT_TIMESTAMP AS TIMESTAMP_AGG, "
				+ "	NULL AS R_CAT_DOC_DGT, "
				+ "	NULL AS R_TP_DOC_DGT, "
				+ "	NULL AS R_DOC_DGT, "
				+ "	NULL AS R_VERS_DOC_DGT, "
				+ "	CAST('Y' AS nchar(1)) "
				+ " AS VISUAL_IDENT "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                      [Target].[dbo].[QC_TT_QUALITA_RIC_FM] qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		1 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		'Controllo UT' AS TT_NOME, "
				+ "		'Ctrl UT' AS DESCR_RIDOTTA, "
				+ "		'Y' AS STAMPA_FASE, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_1 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		2 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		'Controllo Dimensionale' AS TT_NOME, "
				+ "		'Ctrl DIM' AS DESCR_RIDOTTA, "
				+ "		'Y' AS STAMPA_FASE, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_2 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		3 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		'Controllo Durezza Kg' AS TT_NOME, "
				+ "		'Ctrl HB Kg' AS DESCR_RIDOTTA, "
				+ "		'Y' AS STAMPA_FASE, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_3 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		4 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		'Magnetico' AS TT_NOME, "
				+ "		'Magnetico' AS DESCR_RIDOTTA, "
				+ "		'N' AS STAMPA_FASE, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_3 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		5 AS ID_SEQUENZA, "
				+ "		QC_TT_QUALITA_RIC_FM.ORD_CLI_RIGA, "
				+ "		'Liquidi Penetranti' AS TT_NOME, "
				+ "		'Liquidi Penetranti' AS DESCR_RIDOTTA, "
				+ "		'N' AS STAMPA_FASE, "
				+ "		QC_TT_QUALITA_RIC_FM.TT_RAFFREDDAMENTO_3 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_QUALITA_RIC_FM]) c ON "
				+ "	qc.ORD_CLI_RIGA = c.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_CONTROLLO_QUALITATIVO_POST' "
				+ "	AND COALESCE (c.TT_NOME, "
				+ "	'') <> '' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			while(rs.next()) {
				noteFaseControlloQualitativoPost.put(String.valueOf(rs.getInt("ID_SEQUENZA_FASE")), rs.getString("NOTE").trim());
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
	
	public void leggiNoteFaseControlloTtPreliminare() {
		String stmt = "SELECT "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + c.ID_SEQUENZA AS ID_SEQUENZA_FASE, "
				+ "	CAST(c.TT_NOME AS nvarchar(35)) AS DESCRIZIONE, "
				+ "	CAST('TT_QUALITA' AS nchar(15)) AS DESCR_RIDOTTA, "
				+ "	(oea.SEQ_VISUALIZZ - 1) + c.ID_SEQUENZA AS SEQ_VISUALIZZ, "
				+ "	CAST(oea.R_ARTICOLO AS nchar(25)) AS R_ARTICOLO, "
				+ "	CAST(oea.R_ATTIVITA AS nchar(15)) AS R_ATTIVITA_LAV, "
				+ "	CAST('1' AS nchar(1)) AS CARATTERISTICHE, "
				+ "	CAST('1' AS nchar(1)) AS RACCOLTA_DATI, "
				+ "	NULL  "
				+ "AS DATA_INZ_VALID, "
				+ "	NULL AS DATA_FIN_VALID, "
				+ "	NULL AS R_VER_INZ_VALID, "
				+ "	NULL AS R_VER_FIN_VALID, "
				+ "	NULL AS R_REGOLA, "
				+ "	NULL AS TEMPO_CTR, "
				+ "	CAST('0' AS nchar(1))  "
				+ "AS TIPO_CRIT_VALID, "
				+ "	NULL AS R_UM_TEMPO_CTR, "
				+ "	NULL AS TEMPO_ATR_CTR, "
				+ "	NULL AS R_UM_TEMPO_ATR_CTR, "
				+ "	NULL AS COSTO_CTR, "
				+ "	NULL AS R_VALUTA, "
				+ "	CAST('2' AS nchar(1)) AS CAMPIONAMENTO, "
				+ "	NULL AS FREQUENZA_CTR, "
				+ "	CAST('P' AS nchar(1)) AS TIPO_FREQUENZA_CTR, "
				+ "	CAST('N' AS nchar(1)) AS VAL_NOM_OBBL, "
				+ "	CAST('N' AS nchar(1)) AS CTR_ESTERNO, "
				+ "	NULL AS R_FORNITORE, "
				+ "	substring(oea.FASE_OBBL, ROW_NUMBER() OVER (PARTITION BY COALESCE (oea.ID_PROGRESSIVO,  "
				+ "oea.ID_ORIGINALE) "
				+ "ORDER BY c.ID_SEQUENZA), 1) AS FASE_OBBL, "
				+ "	CAST('Y' AS nchar(1)) AS STAMPA_FASE, "
				+ "	CAST('Raffreddamento: ' + "
				+ "	CASE "
				+ "		c.TT_RAFFREDDAMENTO WHEN 0 THEN 'Polimero' "
				+ "		WHEN 1 THEN 'Aria' "
				+ "		WHEN 2 THEN 'Acqua' "
				+ "		WHEN 3 THEN 'Forno' "
				+ "		WHEN 4 THEN 'Olio' "
				+ "		ELSE "
				+ " '' "
				+ "	END AS nchar(50)) AS NOTE, "
				+ "	/*SOSTITUIRE STRINCA CON CAMPO TARGET*/ "
				+ "	oea.QTA_CTR AS QTA_CTR, "
				+ "	oea.PERC_CTR AS PERC_CTR, "
				+ "	0 AS NUM_DIF_ACC, "
				+ "	1 AS NUM_DIF_RIF, "
				+ "	CAST('1' AS nchar(1)) AS TIPO_CAMPIONAMENTO, "
				+ "	CAST('2' AS nchar(1)) AS LIV_CAMPIONAMENTO, "
				+ "	NULL AS COEF_LQA, "
				+ "	CAST('6' AS nchar(1))  "
				+ "AS LIV_ISPEZIONE, "
				+ "	NULL AS R_GES_COMMENTI, "
				+ "	NULL AS R_DOCUMENTO_MM, "
				+ "	CAST('1' AS nchar(1)) AS NORM_CARTA_CTR, "
				+ "	NULL AS NUM_CAMPIONE, "
				+ "	NULL  "
				+ "AS FREQ_MINUTI, "
				+ "	NULL AS FREQ_PEZZI, "
				+ "	CAST('N' AS nchar(1)) AS LIMITI_ATTESI, "
				+ "	CAST('N' AS nchar(1)) AS RIF_CENTRO_TOLL, "
				+ "	NULL AS R_P_REAZIONE, "
				+ "	CAST('V' AS nchar(1))  "
				+ "AS STATO, "
				+ "	CAST('' AS nchar(20)) AS R_UTENTE_CRZ, "
				+ "	CAST('' AS nchar(20)) AS R_UTENTE_AGG, "
				+ "	CURRENT_TIMESTAMP AS TIMESTAMP_CRZ, "
				+ "	CURRENT_TIMESTAMP AS TIMESTAMP_AGG, "
				+ "	NULL AS R_CAT_DOC_DGT, "
				+ "	NULL AS R_TP_DOC_DGT, "
				+ "	NULL AS R_DOC_DGT, "
				+ "	NULL AS R_VERS_DOC_DGT, "
				+ "	CAST('Y' AS nchar(1)) "
				+ " AS VISUAL_IDENT "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                      [Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		1 AS ID_SEQUENZA, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		'1-Trattamento Termico' AS TT_NOME, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.TT_RAFFREDDAMENTO_1 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		2 AS ID_SEQUENZA, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		'2-Trattamento Termico' AS TT_NOME, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.TT_RAFFREDDAMENTO_2 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		3 AS ID_SEQUENZA, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		'3-Trattamento Termico' AS TT_NOME, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.TT_RAFFREDDAMENTO_3 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		4 AS ID_SEQUENZA, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		'4-Trattamento Termico' AS TT_NOME, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.TT_RAFFREDDAMENTO_4 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		5 AS ID_SEQUENZA, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.TT_NOME_5 AS TT_NOME, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.TT_RAFFREDDAMENTO_5 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		6 AS ID_SEQUENZA, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.TT_NOME_6 AS TT_NOME, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.TT_RAFFREDDAMENTO_6 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		6 AS ID_SEQUENZA, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.TT_NOME_6 AS TT_NOME, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.TT_RAFFREDDAMENTO_6 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		7 AS ID_SEQUENZA, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		'Durezza Kg' AS TT_NOME, "
				+ "		NULL AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		8 AS ID_SEQUENZA, "
				+ "		QC_TT_PRELIMINARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		'Controllo Dimensionale' AS TT_NOME, "
				+ "		NULL AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_PRELIMINARE_RIC_FM]) c ON "
				+ "	qc.ORD_CLI_RIGA = c.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_TT-PRELIMINARE' "
				+ "	AND COALESCE (c.TT_NOME, "
				+ "	'') <> '' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			while(rs.next()) {
				noteFaseControlloTtPreliminare.put(String.valueOf(rs.getInt("ID_SEQUENZA_FASE")), rs.getString("NOTE").trim());
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
	
	public void leggiNoteFaseControlloTtSupplementare() {
		String stmt = "SELECT "
				+ "	CAST('SGQ_CICLI_FMO' AS nchar(20)) AS DATA_ORIGIN, "
				+ "	0 AS RUN_ID, "
				+ "	( "
				+ "	SELECT "
				+ "		* "
				+ "	FROM "
				+ "		OPENQUERY(PANTH01, "
				+ "		'select COALESCE(max(ROW_ID),0) from thip.CM_SGQ_CICLI_FAS WHERE DATA_ORIGIN = ''SGQ_CICLI_FMO'' AND RUN_ID = 0')) + ROW_NUMBER()  "
				+ "                      OVER ( "
				+ "	ORDER BY oea.ID_PROGRESSIVO) AS ROW_ID, "
				+ "	CAST "
				+ "	(CASE WHEN oea.ID_PROGRESSIVO IS NULL THEN 0 "
				+ "	ELSE 1 "
				+ "END AS nchar(1)) AS RUN_ACTION, "
				+ "	/*0=insert/update 1 = update/insert*/ "
				+ "	CAST('0' AS nchar(1)) AS TRASF_STATUS, "
				+ "	CAST('050' AS nchar(3)) AS ID_AZIENDA, "
				+ "	CAST(COALESCE (ID_PROGRESSIVO, "
				+ "	ID_ORIGINALE)  "
				+ "AS nchar(10)) AS ID_PROGRESSIVO, "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + c.ID_SEQUENZA AS ID_SEQUENZA_FASE, "
				+ "	CAST(c.TT_NOME AS nvarchar(35)) AS DESCRIZIONE, "
				+ "	CAST('TT_QUALITA' AS nchar(15)) AS DESCR_RIDOTTA, "
				+ "	(oea.SEQ_VISUALIZZ - 1) + c.ID_SEQUENZA AS SEQ_VISUALIZZ, "
				+ "	CAST(oea.R_ARTICOLO AS nchar(25)) AS R_ARTICOLO, "
				+ "	CAST(oea.R_ATTIVITA AS nchar(15)) AS R_ATTIVITA_LAV, "
				+ "	CAST('1' AS nchar(1)) AS CARATTERISTICHE, "
				+ "	CAST('1' AS nchar(1)) AS RACCOLTA_DATI, "
				+ "	NULL  "
				+ "AS DATA_INZ_VALID, "
				+ "	NULL AS DATA_FIN_VALID, "
				+ "	NULL AS R_VER_INZ_VALID, "
				+ "	NULL AS R_VER_FIN_VALID, "
				+ "	NULL AS R_REGOLA, "
				+ "	NULL AS TEMPO_CTR, "
				+ "	CAST('0' AS nchar(1))  "
				+ "AS TIPO_CRIT_VALID, "
				+ "	NULL AS R_UM_TEMPO_CTR, "
				+ "	NULL AS TEMPO_ATR_CTR, "
				+ "	NULL AS R_UM_TEMPO_ATR_CTR, "
				+ "	NULL AS COSTO_CTR, "
				+ "	NULL AS R_VALUTA, "
				+ "	CAST('2' AS nchar(1)) AS CAMPIONAMENTO, "
				+ "	NULL AS FREQUENZA_CTR, "
				+ "	CAST('P' AS nchar(1)) AS TIPO_FREQUENZA_CTR, "
				+ "	CAST('N' AS nchar(1)) AS VAL_NOM_OBBL, "
				+ "	CAST('N' AS nchar(1)) AS CTR_ESTERNO, "
				+ "	NULL AS R_FORNITORE, "
				+ "	substring(oea.FASE_OBBL, ROW_NUMBER() OVER (PARTITION BY COALESCE (oea.ID_PROGRESSIVO,  "
				+ "oea.ID_ORIGINALE) "
				+ "ORDER BY c.ID_SEQUENZA), 1) AS FASE_OBBL, "
				+ "	CAST('Y' AS nchar(1)) AS STAMPA_FASE, "
				+ "	CAST('Raffreddamento: ' + "
				+ "	CASE "
				+ "		c.TT_RAFFREDDAMENTO WHEN 0 THEN 'Polimero' "
				+ "		WHEN 1 THEN 'Aria' "
				+ "		WHEN 2 THEN 'Acqua' "
				+ "		WHEN 3 THEN 'Forno' "
				+ "		WHEN 4 THEN 'Olio' "
				+ "		ELSE "
				+ " '' "
				+ "	END AS nchar(50)) AS NOTE, "
				+ "	/*SOSTITUIRE STRINCA CON CAMPO TARGET*/ "
				+ "	oea.QTA_CTR AS QTA_CTR, "
				+ "	oea.PERC_CTR AS PERC_CTR, "
				+ "	0 AS NUM_DIF_ACC, "
				+ "	1 AS NUM_DIF_RIF, "
				+ "	CAST('1' AS nchar(1)) AS TIPO_CAMPIONAMENTO, "
				+ "	CAST('2' AS nchar(1)) AS LIV_CAMPIONAMENTO, "
				+ "	NULL AS COEF_LQA, "
				+ "	CAST('6' AS nchar(1))  "
				+ "AS LIV_ISPEZIONE, "
				+ "	NULL AS R_GES_COMMENTI, "
				+ "	NULL AS R_DOCUMENTO_MM, "
				+ "	CAST('1' AS nchar(1)) AS NORM_CARTA_CTR, "
				+ "	NULL AS NUM_CAMPIONE, "
				+ "	NULL  "
				+ "AS FREQ_MINUTI, "
				+ "	NULL AS FREQ_PEZZI, "
				+ "	CAST('N' AS nchar(1)) AS LIMITI_ATTESI, "
				+ "	CAST('N' AS nchar(1)) AS RIF_CENTRO_TOLL, "
				+ "	NULL AS R_P_REAZIONE, "
				+ "	CAST('V' AS nchar(1))  "
				+ "AS STATO, "
				+ "	CAST('' AS nchar(20)) AS R_UTENTE_CRZ, "
				+ "	CAST('' AS nchar(20)) AS R_UTENTE_AGG, "
				+ "	CURRENT_TIMESTAMP AS TIMESTAMP_CRZ, "
				+ "	CURRENT_TIMESTAMP AS TIMESTAMP_AGG, "
				+ "	NULL AS R_CAT_DOC_DGT, "
				+ "	NULL AS R_TP_DOC_DGT, "
				+ "	NULL AS R_DOC_DGT, "
				+ "	NULL AS R_VERS_DOC_DGT, "
				+ "	CAST('Y' AS nchar(1)) "
				+ " AS VISUAL_IDENT "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                      [Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA COLLATE Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		1 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		'1-Trattamento Termico' AS TT_NOME, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_RAFFREDDAMENTO_1 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		2 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		'2-Trattamento Termico' AS TT_NOME, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_RAFFREDDAMENTO_2 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		3 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		'3-Trattamento Termico' AS TT_NOME, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_RAFFREDDAMENTO_3 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		4 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		'4-Trattamento Termico' AS TT_NOME, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_RAFFREDDAMENTO_4 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		5 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_NOME_5 AS TT_NOME, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_RAFFREDDAMENTO_5 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		6 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_NOME_6 AS TT_NOME, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_RAFFREDDAMENTO_6 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		6 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_NOME_6 AS TT_NOME, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.TT_RAFFREDDAMENTO_6 AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		7 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		'Durezza Kg' AS TT_NOME, "
				+ "		NULL AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM] "
				+ "UNION "
				+ "	SELECT "
				+ "		8 AS ID_SEQUENZA, "
				+ "		QC_TT_SUPPLEMENTARE_RIC_FM.ORD_CLI_RIGA, "
				+ "		'Durezza HB' AS TT_NOME, "
				+ "		NULL AS TT_RAFFREDDAMENTO "
				+ "	FROM "
				+ "		[Target].[dbo].[QC_TT_SUPPLEMENTARE_RIC_FM]) c ON "
				+ "	qc.ORD_CLI_RIGA = c.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C03_TT-SUPPLEMENTARE' "
				+ "	AND COALESCE (c.TT_NOME, "
				+ "	'') <> '' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			while(rs.next()) {
				noteFaseControlloTtSupplementare.put(String.valueOf(rs.getInt("ID_SEQUENZA_FASE")), rs.getString("NOTE").trim());
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

	public void leggiNoteFaseControlloNDT() {
		String stmt = "SELECT "
				+ "	CAST('SGQ_CICLI_FMO' AS nchar(20)) AS DATA_ORIGIN, "
				+ "	0 AS RUN_ID, "
				+ "	( "
				+ "	SELECT "
				+ "		* "
				+ "	FROM "
				+ "		OPENQUERY(PANTH01, "
				+ "		'select COALESCE(max(ROW_ID),0) from thip.CM_SGQ_CICLI_FAS WHERE DATA_ORIGIN = ''SGQ_CICLI_FMO'' AND RUN_ID = 0')) + ROW_NUMBER()  "
				+ "                      OVER ( "
				+ "ORDER BY "
				+ "	oea.ID_PROGRESSIVO) AS ROW_ID, "
				+ "	CAST "
				+ "	(CASE "
				+ "		WHEN oea.ID_PROGRESSIVO IS NULL THEN 0 "
				+ "		ELSE 1 "
				+ "	END AS nchar(1)) AS RUN_ACTION, "
				+ "	/*0=insert/update 1 = update/insert*/ "
				+ "	CAST('0' AS nchar(1)) AS TRASF_STATUS, "
				+ "	CAST('050' AS nchar(3)) AS ID_AZIENDA, "
				+ "	CAST(COALESCE (ID_PROGRESSIVO, "
				+ "	ID_ORIGINALE)  "
				+ "AS nchar(10)) AS ID_PROGRESSIVO, "
				+ "	(oea.ID_SEQUENZA_FASE - 1) + c.ID_SEQUENZA AS ID_SEQUENZA_FASE, "
				+ "	CAST(c.DESCRIZIONE AS nvarchar(35)) AS DESCRIZIONE, "
				+ "	CAST(c.DESCR_RIDOTTA AS nchar(15)) AS DESCR_RIDOTTA, "
				+ "	(oea.SEQ_VISUALIZZ - 1) + c.ID_SEQUENZA AS SEQ_VISUALIZZ, "
				+ "	CAST(oea.R_ARTICOLO AS nchar(25))  "
				+ "AS R_ARTICOLO, "
				+ "	CAST(oea.R_ATTIVITA AS nchar(15)) AS R_ATTIVITA_LAV, "
				+ "	CAST('1' AS nchar(1)) AS CARATTERISTICHE, "
				+ "	CAST('1' AS nchar(1)) AS RACCOLTA_DATI, "
				+ "	NULL  "
				+ "AS DATA_INZ_VALID, "
				+ "	NULL AS DATA_FIN_VALID, "
				+ "	NULL AS R_VER_INZ_VALID, "
				+ "	NULL AS R_VER_FIN_VALID, "
				+ "	NULL AS R_REGOLA, "
				+ "	NULL AS TEMPO_CTR, "
				+ "	CAST('0' AS nchar(1))  "
				+ "AS TIPO_CRIT_VALID, "
				+ "	NULL AS R_UM_TEMPO_CTR, "
				+ "	NULL AS TEMPO_ATR_CTR, "
				+ "	NULL AS R_UM_TEMPO_ATR_CTR, "
				+ "	NULL AS COSTO_CTR, "
				+ "	NULL AS R_VALUTA, "
				+ "	CAST('2' AS nchar(1)) AS CAMPIONAMENTO, "
				+ "	NULL AS FREQUENZA_CTR, "
				+ "	CAST('P' AS nchar(1)) AS TIPO_FREQUENZA_CTR, "
				+ "	CAST('N' AS nchar(1)) AS VAL_NOM_OBBL, "
				+ "	CAST('N' AS nchar(1)) AS CTR_ESTERNO, "
				+ "	NULL AS R_FORNITORE, "
				+ "	substring(oea.FASE_OBBL, ROW_NUMBER() OVER (PARTITION BY COALESCE (oea.ID_PROGRESSIVO,  "
				+ "oea.ID_ORIGINALE) "
				+ "ORDER BY c.ID_SEQUENZA), 1) AS FASE_OBBL, "
				+ "	CAST(c.STAMPA_FASE AS nchar(1)) AS STAMPA_FASE, "
				+ "	CAST(c.NOTE AS nchar(50)) AS NOTE, "
				+ "	/*SOSTITUIRE STRINCA CON CAMPO TARGET*/ "
				+ "	oea.QTA_CTR AS QTA_CTR, "
				+ "	oea.PERC_CTR AS PERC_CTR, "
				+ "	0 AS NUM_DIF_ACC, "
				+ "	1 AS NUM_DIF_RIF, "
				+ "	CAST('1' AS nchar(1))  "
				+ "AS TIPO_CAMPIONAMENTO, "
				+ "	CAST('2' AS nchar(1)) AS LIV_CAMPIONAMENTO, "
				+ "	NULL AS COEF_LQA, "
				+ "	CAST('6' AS nchar(1)) AS LIV_ISPEZIONE, "
				+ "	NULL AS R_GES_COMMENTI, "
				+ "	NULL  "
				+ "AS R_DOCUMENTO_MM, "
				+ "	CAST('1' AS nchar(1)) AS NORM_CARTA_CTR, "
				+ "	NULL AS NUM_CAMPIONE, "
				+ "	NULL AS FREQ_MINUTI, "
				+ "	NULL AS FREQ_PEZZI, "
				+ "	CAST('N' AS nchar(1))  "
				+ "AS LIMITI_ATTESI, "
				+ "	CAST('N' AS nchar(1)) AS RIF_CENTRO_TOLL, "
				+ "	NULL AS R_P_REAZIONE, "
				+ "	CAST('V' AS nchar(1)) AS STATO, "
				+ "	CAST('' AS nchar(20)) AS R_UTENTE_CRZ, "
				+ "	CAST('' AS nchar(20)) AS R_UTENTE_AGG, "
				+ "	CURRENT_TIMESTAMP AS TIMESTAMP_CRZ, "
				+ "	CURRENT_TIMESTAMP AS TIMESTAMP_AGG, "
				+ "	NULL AS R_CAT_DOC_DGT, "
				+ "	NULL  "
				+ "AS R_TP_DOC_DGT, "
				+ "	NULL AS R_DOC_DGT, "
				+ "	NULL AS R_VERS_DOC_DGT, "
				+ "	CAST('Y' AS nchar(1)) AS VISUAL_IDENT "
				+ "FROM "
				+ "	PantheraTarget.THIPPERS.ORD_ESEC_ATV_SOG_COLLAUDO_20 oea "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.ORD_CLI_RIGA, "
				+ "		COALESCE (QC_NDT_US_COMPL_F_1_RIC_FM.ORD_CLI_RIGA, "
				+ "		COALESCE (QC_NDT_MT_RIC_FM.ORD_CLI_RIGA, "
				+ "		COALESCE (QC_NDT_LP_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_CMTR.ORD_CLI_RIGA)))) AS ORD_CLI_RIGA, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.DATA, "
				+ "		COALESCE (QC_NDT_US_COMPL_F_1_RIC_FM.DATA, "
				+ "		COALESCE (QC_NDT_MT_RIC_FM.DATA, "
				+ "		COALESCE (QC_NDT_LP_RIC_FM.DATA, "
				+ "		QC_CMTR.DATA)))) AS DATA, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.NOTE, "
				+ "		COALESCE (QC_NDT_US_COMPL_F_1_RIC_FM.NOTE, "
				+ "		COALESCE (QC_NDT_MT_RIC_FM.NOTE, "
				+ "		QC_NDT_LP_RIC_FM.NOTE))) AS NOTE "
				+ "	FROM "
				+ "		Target.dbo.QC_CMTR QC_CMTR "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_STD_RIC_FM_2 QC_NDT_US_STD_RIC_FM_2 ON "
				+ "		QC_NDT_US_STD_RIC_FM_2.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_COMPL_F_1_RIC_FM QC_NDT_US_COMPL_F_1_RIC_FM ON "
				+ "		QC_NDT_US_COMPL_F_1_RIC_FM.ORD_CLI_RIGA = QC_NDT_US_STD_RIC_FM_2.ORD_CLI_RIGA "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_MT_RIC_FM QC_NDT_MT_RIC_FM ON "
				+ "		QC_NDT_MT_RIC_FM.ORD_CLI_RIGA = QC_NDT_US_COMPL_F_1_RIC_FM.ORD_CLI_RIGA "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_LP_RIC_FM QC_NDT_LP_RIC_FM ON "
				+ "		QC_NDT_LP_RIC_FM.ORD_CLI_RIGA = QC_NDT_MT_RIC_FM.ORD_CLI_RIGA) qc ON "
				+ "	qc.ORD_CLI_RIGA = oea.ORD_CLI_RIGA collate Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                      target.dbo.ORD_CLI_RIGHE_USER ocru ON "
				+ "	ocru.DOC_RIGA_ID = qc.ORD_CLI_RIGA collate Latin1_General_CS_AS "
				+ "INNER JOIN "
				+ "                          ( "
				+ "	SELECT "
				+ "		1 AS ID_SEQUENZA, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.ORD_CLI_RIGA, "
				+ "		COALESCE (QC_NDT_US_COMPL_F_1_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_CMTR.ORD_CLI_RIGA)) AS ORD_CLI_RIGA, "
				+ "		'Ultrasuoni' AS DESCRIZIONE, "
				+ "		'Ultrasuoni' AS DESCR_RIDOTTA, "
				+ "		COALESCE (QC_NDT_US_STD_RIC_FM_2.SPECIFICA_1, "
				+ "		QC_NDT_US_COMPL_F_1_RIC_FM.SPECIFICA_1) AS NOTE, "
				+ "		CASE "
				+ "			WHEN COALESCE (QC_NDT_US_COMPL_F_1_RIC_FM.ID, "
				+ "			COALESCE (QC_NDT_US_STD_RIC_FM_2.ID, "
				+ "			COALESCE (QC_NDT_US_COMPL_I_1_RIC_FM.ID, "
				+ "			QC_NDT_US_STD_RIC_FM.ID))) IS NULL THEN 'N' "
				+ "			ELSE 'Y' "
				+ "		END AS STAMPA_FASE "
				+ "	FROM "
				+ "		Target.dbo.QC_CMTR QC_CMTR "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_COMPL_F_1_RIC_FM QC_NDT_US_COMPL_F_1_RIC_FM ON "
				+ "		QC_NDT_US_COMPL_F_1_RIC_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_STD_RIC_FM_2 QC_NDT_US_STD_RIC_FM_2 ON "
				+ "		QC_NDT_US_STD_RIC_FM_2.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_COMPL_I_1_RIC_FM QC_NDT_US_COMPL_I_1_RIC_FM ON "
				+ "		QC_NDT_US_COMPL_I_1_RIC_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA "
				+ "	LEFT JOIN "
				+ "                                                   Target.dbo.QC_NDT_US_STD_RIC_FM QC_NDT_US_STD_RIC_FM ON "
				+ "		QC_NDT_US_STD_RIC_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA "
				+ "UNION ALL "
				+ "	SELECT "
				+ "		2 AS ID_SEQUENZA, "
				+ "		COALESCE (QC_NDT_MT_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_CMTR.ORD_CLI_RIGA) AS ORD_CLI_RIGA, "
				+ "		'Magnetico' AS DESCRIZIONE, "
				+ "		'Magnetico' AS DESCR_RIDOTTA, "
				+ "		QC_NDT_MT_RIC_FM.SPECIFICA_1 AS NOTE, "
				+ "		CASE "
				+ "			WHEN QC_NDT_MT_RIC_FM.ID IS NULL  "
				+ "                                                  THEN 'N' "
				+ "			ELSE 'Y' "
				+ "		END AS STAMPA_FASE "
				+ "	FROM "
				+ "		Target.dbo.QC_CMTR QC_CMTR "
				+ "	LEFT JOIN "
				+ "                                                  Target.dbo.QC_NDT_MT_RIC_FM QC_NDT_MT_RIC_FM ON "
				+ "		QC_NDT_MT_RIC_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA "
				+ "UNION ALL "
				+ "	SELECT "
				+ "		3 AS ID_SEQUENZA, "
				+ "		COALESCE (QC_NDT_LP_RIC_FM.ORD_CLI_RIGA, "
				+ "		QC_CMTR.ORD_CLI_RIGA) AS ORD_CLI_RIGA, "
				+ "		'Penetranti' AS DESCRIZIONE, "
				+ "		'Penetranti' AS DESCR_RIDOTTA, "
				+ "		QC_NDT_LP_RIC_FM.SPECIFICA_1 AS NOTE, "
				+ "		CASE "
				+ "			WHEN QC_NDT_LP_RIC_FM.ID IS NULL  "
				+ "                                                  THEN 'N' "
				+ "			ELSE 'Y' "
				+ "		END AS STAMPA_FASE "
				+ "	FROM "
				+ "		Target.dbo.QC_CMTR QC_CMTR "
				+ "	LEFT JOIN "
				+ "                                                  Target.dbo.QC_NDT_LP_RIC_FM QC_NDT_LP_RIC_FM ON "
				+ "		QC_NDT_LP_RIC_FM.ORD_CLI_RIGA = QC_CMTR.ORD_CLI_RIGA) c ON "
				+ "	qc.ORD_CLI_RIGA = c.ORD_CLI_RIGA "
				+ "WHERE "
				+ "	oea.NOME_TABELLA = 'C06_CONTROLLI_NDT' AND oea.R_COMMESSA = '"+getCommessa()+"' AND oea.ORD_CLI_RIGA = '"+getOrdCliRiga()+"' ";
		ResultSet rs = null;
		CachedStatement cs = new CachedStatement(stmt);
		try {
			rs = cs.executeQuery();
			while(rs.next()) {
				noteFaseControlloNDT.put(String.valueOf(rs.getInt("ID_SEQUENZA_FASE")), (rs.getString("NOTE") != null ? rs.getString("NOTE").trim() : ""));
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
