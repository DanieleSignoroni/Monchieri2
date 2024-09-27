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

public class CodificaCaratteristicaCollaudo extends CodificaCaratteristicaCollaudoPO {

	public CodificaCaratteristicaCollaudo(){
		super();
		iAzienda.setKey(Azienda.getAziendaCorrente());
	}

	public ErrorMessage checkDelete() {
		return null;
	}

	public String toString() {
		return "(" + this.getClass().getName() + ":" + this.getKey() + ")";
	}

	public String getTableNLSName() {
		return SystemParam.getSchema("THIPPERS") + "SGQ_COD_CAR_COL_L";
	}

	@SuppressWarnings("rawtypes")
	public static CodificaCaratteristicaCollaudo recuperaCodificaCaratteristica(String descRidotta){
		if(descRidotta == null )
			return null;
		String where = CodificaCaratteristicaCollaudoTM.ID_AZIENDA + " ='" +  Azienda.getAziendaCorrente() + "' AND " +
				CodificaCaratteristicaCollaudoTM.DESCR_RIDOTTA + " ='" +  descRidotta + "'";
		String orderBy = CodificaCaratteristicaCollaudoTM.CODICE + " ASC ";
		try {
			List list = CodificaCaratteristicaCollaudo.retrieveList(where, orderBy, false);
			if(list == null || list.isEmpty())
				return null;
			return (CodificaCaratteristicaCollaudo)list.get(0);
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
