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

public class YCicloCollaudoCarattTM extends CicloCollaudoCaratteristicaTM {

	public static final String YFORMULA = "YFORMULA";

	public static final String R_CAR_MAME = "R_CAR_MAME";

	public static final String TABLE_NAME_EXT = SystemParam.getSchema("THIPPERS") + "YSGQ_CICLI_CAR";

	private static final String CLASS_NAME = it.mame.thip.qualita.controllo.YCicloCollaudoCaratt.class.getName();

	public YCicloCollaudoCarattTM() throws SQLException {
		super();
	}

	protected void initialize() throws SQLException {
		super.initialize();
		setObjClassName(CLASS_NAME);
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();
		linkTable(TABLE_NAME_EXT);
		addAttributeOnTable("YFormula", YFORMULA, TABLE_NAME_EXT);
		addAttributeOnTable("IdCodificaCar", R_CAR_MAME, "getShortObject", TABLE_NAME_EXT);

	}

}

