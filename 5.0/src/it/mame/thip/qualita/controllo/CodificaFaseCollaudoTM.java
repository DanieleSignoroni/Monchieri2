package it.mame.thip.qualita.controllo;

import com.thera.thermfw.persist.*;
import java.sql.*;
import com.thera.thermfw.base.*;
import it.thera.thip.cs.DescrizioneTTM;
import it.thera.thip.cs.DatiComuniEstesiTTM;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 26/09/2024
 * <br><br>
 * <b>71XXX	DSSOF3	26/09/2024</b>
 * <p>Prima stesura.<br>
 *  
 * </p>
 */

public class CodificaFaseCollaudoTM extends TableManager {

	public static final String ID_AZIENDA = "ID_AZIENDA";

	public static final String CODICE = "CODICE";

	public static final String DESCRIZIONE = DescrizioneTTM.DESCRIZIONE;

	public static final String DESCR_RIDOTTA = DescrizioneTTM.DESCR_RIDOTTA;

	public static final String R_UTENTE_CRZ = "R_UTENTE_CRZ";

	public static final String R_UTENTE_AGG = "R_UTENTE_AGG";

	public static final String TIMESTAMP_CRZ = "TIMESTAMP_CRZ";

	public static final String TIMESTAMP_AGG = "TIMESTAMP_AGG";

	public static final String STATO = "STATO";

	public static final String TABLE_NAME = SystemParam.getSchema("THIPPERS") + "SGQ_COD_FASE_COL";

	private static TableManager cInstance;

	private static final String CLASS_NAME = it.mame.thip.qualita.controllo.CodificaFaseCollaudo.class.getName();

	public synchronized static TableManager getInstance() throws SQLException {
		if (cInstance == null) {
			cInstance = (TableManager) Factory.createObject(CodificaFaseCollaudoTM.class);
		}
		return cInstance;
	}

	public CodificaFaseCollaudoTM() throws SQLException {
		super();
	}

	protected void initialize() throws SQLException {
		setTableName(TABLE_NAME);
		setObjClassName(CLASS_NAME);
		init();
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();
		addAttribute("IdAzienda" , ID_AZIENDA);
		addAttribute("Codice", CODICE);
		addComponent("Descrizione", DescrizioneTTM.class);
		addComponent("DatiComuniEstesi", DatiComuniEstesiTTM.class);
		addTimestampAttribute("Timestamp",TIMESTAMP_AGG);
		((DatiComuniEstesiTTM)getTransientTableManager("DatiComuniEstesi")).setExcludedColums();
		setKeys(ID_AZIENDA + " , " + CODICE);
	}

	private void init() throws SQLException {
		configure();
	}

}
