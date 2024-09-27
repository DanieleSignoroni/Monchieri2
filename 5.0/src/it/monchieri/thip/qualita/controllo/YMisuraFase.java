package it.monchieri.thip.qualita.controllo;

import com.thera.thermfw.persist.*;
import it.thera.thip.qualita.controllo.*;
import it.thera.thip.base.azienda.Azienda;

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

public class YMisuraFase extends MisuraFase {

	protected Short iIdCodificaFaseMame;

	public YMisuraFase() {
		setIdAzienda(Azienda.getAziendaCorrente());
	}

	public void setIdCodificaFaseMame(Short idCodificaFaseMame) {
		this.iIdCodificaFaseMame = idCodificaFaseMame;
		setDirty();
	}

	public Short getIdCodificaFaseMame() {
		return iIdCodificaFaseMame;
	}

	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
	}

}

