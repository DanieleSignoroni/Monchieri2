package it.mame.thip.magazzino.generalemag;

import java.sql.*;

import com.thera.thermfw.base.*;
import com.thera.thermfw.persist.*;
import it.thera.thip.cs.*;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 27/09/2024
 * <br><br>
 * <b>71XXX	DSSOF3	27/09/2024</b>
 * <p>Prima stesura.<br>
 *  
 * </p>
 */

public class LottiPrenotazioneTM extends TableManager {

	public static final String ID_AZIENDA = "ID_AZIENDA";

	public static final String ID_ARTICOLO = "ID_ARTICOLO";

	public static final String ID_LOTTO = "ID_LOTTO";

	public static final String ID_COMMESSA = "ID_COMMESSA";

	public static final String DSC_COMMESSA = "DSC_COMMESSA";

	public static final String QUANTITA = "QUANTITA";

	public static final String DATA = "DATA";

	public static final String ESEGUITA = "ESEGUITA";

	public static final String R_UTENTE_CRZ = "R_UTENTE_CRZ";

	public static final String R_UTENTE_AGG = "R_UTENTE_AGG";

	public static final String TIMESTAMP_CRZ = "TIMESTAMP_CRZ";

	public static final String TIMESTAMP_AGG = "TIMESTAMP_AGG";

	public static final String TIPO_PRENOTAZIONE = "TIPO_PRENOTAZIONE";

	public static final String TABLE_NAME = SystemParam.getSchema("THIPPERS") + "LOTTI_PRNT";

	private static TableManager cInstance;

	private static final String CLASS_NAME = it.mame.thip.magazzino.generalemag.LottiPrenotazione.class.getName();

	public synchronized static TableManager getInstance() throws SQLException {
		if (cInstance == null) {
			cInstance = (TableManager)Factory.createObject(LottiPrenotazioneTM.class);
		}
		return cInstance;
	}

	public LottiPrenotazioneTM() throws SQLException {
		super();
	}

	protected void initialize() throws SQLException {
		setTableName(TABLE_NAME);
		setObjClassName(CLASS_NAME);
		init();
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();
		addAttribute("IdLotto" , ID_LOTTO);
		addAttribute("IdCommessa" , ID_COMMESSA);
		addAttribute("Quantita" , QUANTITA);
		addAttribute("Data" , DATA);
		addAttribute("Eseguita", ESEGUITA);
		addAttribute("IdAzienda" , ID_AZIENDA);
		addAttribute("IdArticolo" , ID_ARTICOLO);
		addAttribute("DscCommessa", DSC_COMMESSA);
		addAttribute("TipoPrenotazione",TIPO_PRENOTAZIONE);
		addComponent("DatiComuni", DatiComuniTTM.class);
		setTimestampColumn(TIMESTAMP_AGG);
		((DatiComuniTTM)getTransientTableManager("DatiComuni")).setExcludedColums();

		setKeys(ID_AZIENDA + "," + ID_ARTICOLO + "," + ID_LOTTO + "," + ID_COMMESSA);
	}

	private void init() throws SQLException {
		configure();
	}

}

