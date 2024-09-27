package it.mame.thip.magazzino.generalemag;

import java.sql.SQLException;
import com.thera.thermfw.base.SystemParam;

import it.thera.thip.magazzino.generalemag.LottoTM;

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

public class YLottoTM extends LottoTM {

	public static final String COD_LOTTO_CLI = "COD_LOTTO_CLI";

	public static final String NOTA = "ANNOTAZIONI"; 

	public static final String NUCLEARE = "NUCLEARE";

	public static final String YSTATO = "YSTATO";

	public static final String TABLE_NAME_EXT = SystemParam.getSchema("THIPPERS") + "LOTTI_X";

	public YLottoTM () throws SQLException{
	}

	protected void initializeRelation () throws SQLException {
		super.initializeRelation();
		linkTable(TABLE_NAME_EXT);
		addAttributeOnTable("CodiceLottoCliente", COD_LOTTO_CLI, TABLE_NAME_EXT);
		addAttributeOnTable("Nota", NOTA, TABLE_NAME_EXT); 
		addAttributeOnTable("Nucleare", NUCLEARE, TABLE_NAME_EXT);
		addAttributeOnTable("YStato", YSTATO, TABLE_NAME_EXT);
	}

}
