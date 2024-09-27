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

public class YCicloCollaudoFase extends CicloCollaudoFase {

	protected Proxy iCodificaFase = new Proxy(it.mame.thip.qualita.controllo.CodificaFaseCollaudo.class);

	public YCicloCollaudoFase() {
		setIdAzienda(Azienda.getAziendaCorrente());
	}

	public void setCodificaFase(CodificaFaseCollaudo codificafase) {
		String oldObjectKey = getKey();
		this.iCodificaFase.setObject(codificafase);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public CodificaFaseCollaudo getCodificaFase() {
		return (CodificaFaseCollaudo)iCodificaFase.getObject();
	}

	public void setCodificaFaseKey(String key) {
		String oldObjectKey = getKey();
		iCodificaFase.setKey(key);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getCodificaFaseKey() {
		return iCodificaFase.getKey();
	}

	public void setIdAzienda(String idAzienda) {
		super.setIdAzienda(idAzienda);
		if(iCodificaFase != null) {
			String key = iCodificaFase.getKey();
			iCodificaFase.setKey(KeyHelper.replaceTokenObjectKey(key , 1, idAzienda));
		}

	}

	public void setIdCodificaFase(Short idCodificaFase) {
		String key = iCodificaFase.getKey();
		iCodificaFase.setKey(KeyHelper.replaceTokenObjectKey(key , 2, idCodificaFase));
		setDirty();
	}

	public Short getIdCodificaFase() {
		String key = iCodificaFase.getKey();
		String objIdCodificaFase = KeyHelper.getTokenObjectKey(key,2);
		return KeyHelper.stringToShortObj(objIdCodificaFase);
	}

	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
		YCicloCollaudoFase yCicloCollaudoFase = (YCicloCollaudoFase)obj;
		iCodificaFase.setEqual(yCicloCollaudoFase.iCodificaFase);
	}

}

