package it.monchieri.thip.dtsx;

import java.sql.SQLException;

import com.thera.thermfw.base.SystemParam;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.TableManager;

import it.thera.thip.cs.DatiComuniEstesiTTM;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 27/11/2024
 * <br><br>
 * <b>71XXX	DSSOF3	27/11/2024</b>
 * <p>Prima stesura.<br>
 *  
 * </p>
 */

public class YTimestampElaborazDtsxTM extends TableManager {

	public static final String ID_AZIENDA = "ID_AZIENDA";

	public static final String STATO = "STATO";

	public static final String R_UTENTE_CRZ = "R_UTENTE_CRZ";

	public static final String TIMESTAMP_CRZ = "TIMESTAMP_CRZ";

	public static final String R_UTENTE_AGG = "R_UTENTE_AGG";

	public static final String TIMESTAMP_AGG = "TIMESTAMP_AGG";

	public static final String ID_FUNZIONE = "ID_FUNZIONE";

	public static final String TABLE_NAME = SystemParam.getSchema("THIPPERS") + "YDTSX_TIMESTAMP_ELAB_FUNCTION";

	private static TableManager cInstance;

	private static final String CLASS_NAME = it.monchieri.thip.dtsx.YTimestampElaborazDtsx.class.getName();

	public synchronized static TableManager getInstance() throws SQLException {
		if (cInstance == null) {
			cInstance = (TableManager) Factory.createObject(YTimestampElaborazDtsxTM.class);
		}
		return cInstance;
	}

	public YTimestampElaborazDtsxTM() throws SQLException {
		super();
	}

	protected void initialize() throws SQLException {
		setTableName(TABLE_NAME);
		setObjClassName(CLASS_NAME);
		init();
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();
		addAttribute("IdFunzione", ID_FUNZIONE);
		addAttribute("IdAzienda", ID_AZIENDA);

		addComponent("DatiComuniEstesi", DatiComuniEstesiTTM.class);
		setKeys(ID_AZIENDA + "," + ID_FUNZIONE);

		setTimestampColumn("TIMESTAMP_AGG");
		((it.thera.thip.cs.DatiComuniEstesiTTM) getTransientTableManager("DatiComuniEstesi")).setExcludedColums();
	}

	private void init() throws SQLException {
		configure(ID_FUNZIONE + ", " + ID_AZIENDA + ", " + STATO + ", " + R_UTENTE_CRZ + ", " + TIMESTAMP_CRZ + ", "
				+ R_UTENTE_AGG + ", " + TIMESTAMP_AGG);
	}

}
