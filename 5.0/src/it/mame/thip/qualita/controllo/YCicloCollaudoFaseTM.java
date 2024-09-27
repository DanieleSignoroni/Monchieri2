package it.mame.thip.qualita.controllo;

import java.sql.*;
import it.thera.thip.qualita.controllo.*;
import com.thera.thermfw.base.*;

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

public class YCicloCollaudoFaseTM extends CicloCollaudoFaseTM {

	public static final String R_FAS_MAME = "R_FAS_MAME";

	public static final String TABLE_NAME_EXT = SystemParam.getSchema("THIPPERS") + "YSGQ_CICLI_FAS";

	private static final String CLASS_NAME = it.mame.thip.qualita.controllo.YCicloCollaudoFase.class.getName();

	public YCicloCollaudoFaseTM() throws SQLException {
		super();
	}

	protected void initialize() throws SQLException {
		super.initialize();
		setObjClassName(CLASS_NAME);
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();
		linkTable(TABLE_NAME_EXT);
		addAttributeOnTable("IdCodificaFase", R_FAS_MAME, "getShortObject", TABLE_NAME_EXT);

	}

}

