package it.monchieri.thip.qualita.controllo;

import java.sql.*;
import it.thera.thip.qualita.controllo.*;
import com.thera.thermfw.base.*;

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

public class YMisuraFaseTM extends MisuraFaseTM {

	public static final String R_FASE_MAME = "R_FASE_MAME";

	public static final String TABLE_NAME_EXT = SystemParam.getSchema("THIPPERS") + "YSGQ_MIS_FAS";

	private static final String CLASS_NAME = it.monchieri.thip.qualita.controllo.YMisuraFase.class.getName();

	public YMisuraFaseTM() throws SQLException {
		super();
	}

	protected void initialize() throws SQLException {
		super.initialize();
		setObjClassName(CLASS_NAME);
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();
		linkTable(TABLE_NAME_EXT);
		addAttributeOnTable("IdCodificaFaseMame", R_FASE_MAME, "getShortObject", TABLE_NAME_EXT);

	}

}

