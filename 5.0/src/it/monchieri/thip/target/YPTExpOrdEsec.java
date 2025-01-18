package it.monchieri.thip.target;

import java.sql.SQLException;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.PersistentObject;

import it.monchieri.thip.produzione.ordese.YOrdineEsecutivo;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 12/12/2024
 * <br><br>
 * <b>71XXX    DSSOF3    12/12/2024</b>
 * <p></p>
 */

public class YPTExpOrdEsec extends YPTExpOrdEsecPO {
	
	public String iChiaveOrdineEsecutivoPanthera = null;
	
	protected YOrdineEsecutivo ordineEsecutivoPanthera = null;
	
	public YOrdineEsecutivo getOrdineEsecutivoPanthera() {
		if(ordineEsecutivoPanthera == null && iChiaveOrdineEsecutivoPanthera != null) {
			loadOrdineEsecutivoPanthera();
		}
		return ordineEsecutivoPanthera;
	}

	public void setOrdineEsecutivoPanthera(YOrdineEsecutivo ordineEsecutivoPanthera) {
		this.ordineEsecutivoPanthera = ordineEsecutivoPanthera;
	}

	@Override
	public ErrorMessage checkDelete() {
		return null;
	}
	
	private void loadOrdineEsecutivoPanthera() {
		try {
			setOrdineEsecutivoPanthera((YOrdineEsecutivo) YOrdineEsecutivo.elementWithKey(YOrdineEsecutivo.class, iChiaveOrdineEsecutivoPanthera, PersistentObject.NO_LOCK));
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		
	}

}
