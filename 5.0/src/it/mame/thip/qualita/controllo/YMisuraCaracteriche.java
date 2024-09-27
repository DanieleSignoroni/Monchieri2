package it.mame.thip.qualita.controllo;

import com.thera.thermfw.persist.*;
import it.thera.thip.qualita.controllo.*;
import it.thera.thip.base.azienda.Azienda;

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

public class YMisuraCaracteriche extends MisuraCaracteriche {

	protected String iYFormula;

	public YMisuraCaracteriche() {
		setIdAzienda(Azienda.getAziendaCorrente());
	}

	public void setYFormula(String yFormula) {
		this.iYFormula = yFormula;
		setDirty();
	}

	public String getYFormula() {
		return iYFormula;
	}

	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
	}

}

