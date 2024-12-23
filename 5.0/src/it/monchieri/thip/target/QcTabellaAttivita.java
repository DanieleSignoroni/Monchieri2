package it.monchieri.thip.target;

import java.sql.SQLException;
import java.util.List;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.common.ErrorMessage;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 23/12/2024
 * <br><br>
 * <b>71XXX    DSSOF3    23/12/2024</b>
 * <p></p>
 */

public class QcTabellaAttivita extends QcTabellaAttivitaPO {

	@Override
	public ErrorMessage checkDelete() {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<QcTabellaAttivita> listaAttivita() {
		String where = " "+QcTabellaAttivitaTM.ID_AZIENDA+" = '050' ";
		try {
			return retrieveList(where, "", false);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return null;
	}

}
