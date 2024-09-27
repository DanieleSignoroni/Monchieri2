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

public class YCicloCollaudoCaratt extends CicloCollaudoCaratteristica {

	protected String iYFormula;

	protected Proxy iCodificaCar = new Proxy(it.mame.thip.qualita.controllo.CodificaCaratteristicaCollaudo.class);

	public YCicloCollaudoCaratt() {
		setIdAzienda(Azienda.getAziendaCorrente());
	}

	public void setYFormula(String yFormula) {
		this.iYFormula = yFormula;
		setDirty();
	}

	public String getYFormula() {
		return iYFormula;
	}

	public void setCodificaCar(CodificaCaratteristicaCollaudo codificacar) {
		String oldObjectKey = getKey();
		this.iCodificaCar.setObject(codificacar);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public CodificaCaratteristicaCollaudo getCodificaCar() {
		return (CodificaCaratteristicaCollaudo)iCodificaCar.getObject();
	}

	public void setCodificaCarKey(String key) {
		String oldObjectKey = getKey();
		iCodificaCar.setKey(key);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getCodificaCarKey() {
		return iCodificaCar.getKey();
	}

	public void setIdAzienda(String idAzienda) {
		super.setIdAzienda(idAzienda);
		if(iCodificaCar != null) {
			String key = iCodificaCar.getKey();
			iCodificaCar.setKey(KeyHelper.replaceTokenObjectKey(key , 1, idAzienda));
		}

	}

	public void setIdCodificaCar(Short idCodificaCar) {
		String key = iCodificaCar.getKey();
		iCodificaCar.setKey(KeyHelper.replaceTokenObjectKey(key , 2, idCodificaCar));
		setDirty();
	}

	public Short getIdCodificaCar() {
		String key = iCodificaCar.getKey();
		String objIdCodificaCar = KeyHelper.getTokenObjectKey(key,2);
		return KeyHelper.stringToShortObj(objIdCodificaCar);
	}

	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
		YCicloCollaudoCaratt yCicloCollaudoCaratt = (YCicloCollaudoCaratt)obj;
		iCodificaCar.setEqual(yCicloCollaudoCaratt.iCodificaCar);
	}

}

