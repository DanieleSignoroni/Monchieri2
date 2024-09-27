package it.mame.thip.qualita.controllo;

import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.base.SystemParam;
import com.thera.thermfw.base.Trace;

import it.thera.thip.base.azienda.Azienda;
import java.sql.SQLException;
import java.util.List;

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

public class CodificaFaseCollaudo extends CodificaFaseCollaudoPO {

	public CodificaFaseCollaudo(){
		super();
		setAziendaKey(Azienda.getAziendaCorrente());
	}

	public ErrorMessage checkDelete() {
		return null;
	}

	public String toString(){
		return "(" + this.getClass().getName() + ":" + this.getKey() + ")";
	}

	public  String getTableNLSName(){
		return SystemParam.getSchema("THIPPERS") + "SGQ_COD_FASE_COL_L";
	}

	@SuppressWarnings("rawtypes")
	public static CodificaFaseCollaudo recuperaCodificaFase(String descRidotta){
		if(descRidotta == null )
			return null;
		String where = CodificaFaseCollaudoTM.ID_AZIENDA + " ='" +  Azienda.getAziendaCorrente() + "' AND " +
				CodificaFaseCollaudoTM.DESCR_RIDOTTA + " ='" +  descRidotta + "'";
		String orderBy = CodificaFaseCollaudoTM.CODICE + " ASC ";
		try {
			List list = CodificaFaseCollaudo.retrieveList(where, orderBy, false);
			if(list == null || list.isEmpty())
				return null;
			return (CodificaFaseCollaudo)list.get(0);
		}
		catch (IllegalAccessException ex) {
			ex.printStackTrace(Trace.excStream);
		}
		catch (InstantiationException ex) {
			ex.printStackTrace(Trace.excStream);
		}
		catch (ClassNotFoundException ex) {
			ex.printStackTrace(Trace.excStream);
		}
		catch (SQLException ex) {
			ex.printStackTrace(Trace.excStream);
		}
		return null;
	}

}

