package it.mame.thip.magazzino.generalemag;

import java.math.*;
import java.sql.*;
import java.util.*;
import com.thera.thermfw.persist.*;
import it.thera.thip.magazzino.saldi.SaldoMagLotto;
//--- MAURO 90013 (23/04/2009) Begin
import it.thera.thip.base.fornitore.*;
import it.thera.thip.base.azienda.Azienda;
import com.thera.thermfw.common.ErrorMessage;

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

public class SchedaLottoGUI extends SchedaLotto {

	public static final String separator = String.valueOf(PersistentObject.KEY_SEPARATOR);
	public static final String TIPOLOGIA = "TIPOLOGIA";
	public static final String SEZIONE = "SEZIONE";
	public static final String STATO_LINGOTTO = "STATO_LINGOTTO";

	//----------------  Proxy
	protected Proxy iSaldoLotto = new Proxy(SaldoMagLotto.class);

	//----------------  Proxy SaldoLotto inizio
	public void setSaldoLotto (SaldoMagLotto saldoLotto)
	{
		this.iSaldoLotto.setObject(saldoLotto);
	}

	public SaldoMagLotto getSaldoLotto ()
	{
		return (SaldoMagLotto) iSaldoLotto.getObject();
	}

	public void setSaldoLottoKey (String key)
	{
		iSaldoLotto.setKey(key);
	}

	public String getSaldoLottoKey ()
	{
		return iSaldoLotto.getKey();
	}

	public void setCodiceOperazione (String idOperazione)
	{
		String key = iSaldoLotto.getKey();
		iSaldoLotto.setKey(KeyHelper.replaceTokenObjectKey(key, 6, idOperazione));
	}

	public String getCodiceOperazione ()
	{
		String key = iSaldoLotto.getKey();
		String objIdOperazione = KeyHelper.getTokenObjectKey(key, 6);
		return objIdOperazione;
	}

	public String getIdMagazzino ()
	{
		String key = iSaldoLotto.getKey();
		String objIdMagazzino = KeyHelper.getTokenObjectKey(key, 2);
		return objIdMagazzino;
	}

	public void setIdMagazzino (String idMagazzino)
	{
		String key = iSaldoLotto.getKey();
		iSaldoLotto.setKey(KeyHelper.replaceTokenObjectKey(key, 2, idMagazzino));
	}

	public Integer getIdVersione ()
	{
		String key = iSaldoLotto.getKey();
		String objIdVersione = KeyHelper.getTokenObjectKey(key, 4);
		return KeyHelper.stringToIntegerObj(objIdVersione);
	}

	public void setIdVersione (Integer idVersione)
	{
		String key = iSaldoLotto.getKey();
		iSaldoLotto.setKey(KeyHelper.replaceTokenObjectKey(key, 4, idVersione));
	}

	public Integer getIdConfig ()
	{
		String key = iSaldoLotto.getKey();
		String objIdConfig = KeyHelper.getTokenObjectKey(key, 5);
		return KeyHelper.stringToIntegerObj(objIdConfig);
	}

	public String getStringaConfigurazione ()
	{
		if (iSaldoLotto != null && iSaldoLotto.getObject() != null && ( (SaldoMagLotto) iSaldoLotto.getObject()).getConfigurazione() != null) {
			return ( (SaldoMagLotto) iSaldoLotto.getObject()).getConfigurazione().getStringaConfig();
		}
		return null;
	}

	public void setIdConfig (Integer idConfig)
	{
		String key = iSaldoLotto.getKey();
		iSaldoLotto.setKey(KeyHelper.replaceTokenObjectKey(key, 5, idConfig));
	}

	public BigDecimal getGiacenza ()
	{
		BigDecimal giacenza = null;
		if (getSaldoLotto() != null && getSaldoLotto().getDatiSaldo() != null) {
			giacenza = getSaldoLotto().getDatiSaldo().getQtaGiacenzaUMPrim();
		}
		return giacenza;
	}

	public BigDecimal getGiacenzaSec ()
	{
		BigDecimal giacenza = null;
		if (getSaldoLotto() != null && getSaldoLotto().getDatiSaldo() != null) {
			giacenza = getSaldoLotto().getDatiSaldo().getQtaGiacenzaUMSec();
		}
		return giacenza;
	}

	//--- MAURO 90032 Begin
	@SuppressWarnings("rawtypes")
	public ErrorMessage checkGiacenzaSec ()
	{
		//Modificato il controllo sulla prenotazione prendendo come riferimento la giacenza e l'ordinato in UM sec. (FM 09-03-2016)
		BigDecimal totpr = new BigDecimal("0.00");
		BigDecimal giacenza = new BigDecimal("0.00");
		BigDecimal ordinato = new BigDecimal("0.00");
		if (getSaldoLotto() != null && getSaldoLotto().getDatiSaldo() != null) {
			giacenza = getSaldoLotto().getDatiSaldo().getQtaGiacenzaUMSec();
			ordinato = getSaldoLotto().getDatiSaldo().getQtaOrdFornitoreUMSec();
		}
		Iterator i = getLottiPrenotazione().iterator();
		while (i.hasNext()) {
			LottiPrenotazione lotpr = (LottiPrenotazione) i.next();
			if (!lotpr.isEseguita()) {
				totpr = totpr.add(lotpr.getQuantita());
			}
		}
		if (totpr.compareTo(giacenza.add(ordinato)) > 0) {
			return new ErrorMessage("BAS0000078","Prenotato superiore alla giacenza");
		}

		return null;
	}
	//--- MAURO 90032 End

	//----------------  Proxy SaldoLotto fine

	public void setKey (String key)
	{
		super.setKey(key);

		String objCodiceAzienda = KeyHelper.getTokenObjectKey(key, 1);
		String objCodiceArticolo = KeyHelper.getTokenObjectKey(key, 2);
		String objCodiceLotto = KeyHelper.getTokenObjectKey(key, 3);

		String objCodiceMagazzino = KeyHelper.getTokenObjectKey(key, 4);
		String objIdVersione = KeyHelper.getTokenObjectKey(key, 5);
		String objIdConfig = KeyHelper.getTokenObjectKey(key, 6);
		String objCodiceOperazione = KeyHelper.getTokenObjectKey(key, 7);

		if (objCodiceMagazzino != null && objIdVersione != null && objIdConfig != null && objCodiceOperazione != null) {
			String saldiLottoKey = KeyHelper.buildObjectKey(new String[] {objCodiceAzienda, objCodiceMagazzino, objCodiceArticolo, objIdVersione, objIdConfig, objCodiceOperazione, objCodiceLotto});
			setSaldoLottoKey(saldiLottoKey);
		}
	}

	public String getTipo ()
	{
		return getValore(TIPOLOGIA);
	}

	public String getSezione ()
	{
		return getValore(SEZIONE);
	}

	public String getAccaieria ()
	{
		return getValore(STATO_LINGOTTO);
	}

	@SuppressWarnings("rawtypes")
	public String getValore (String variabile)
	{
		String valore = "";
		if (getSaldoLotto() != null && getSaldoLotto().getConfigurazione() != null) {
			HashMap variabileValore = getVarValore(getSaldoLotto().getConfigurazione().getSintesiConfig());
			if (variabileValore != null && variabileValore.get(variabile) != null) {
				valore = (String) variabileValore.get(variabile);
			}
		}
		return valore;
	}

	public String getGruppo ()
	{
		return (getArticolo() == null) ? null : getArticolo().getIdClasseMateriale();
	}

	public String getColata ()
	{
		return getLottoAcquisto();
	}

	public String getIdFornitore ()
	{
		return getCodiceFornitore();
	}

	//--- MAURO 90013 (21/04/2009) Begin
	public String getDscFornitore ()
	{
		String dsc = null;
		if (getCodiceFornitore() != null) {
			Object[] keyParts = {Azienda.getAziendaCorrente(), getCodiceFornitore()};
			String key = KeyHelper.buildObjectKey(keyParts);
			Fornitore fornitore = (Fornitore) Factory.createObject(Fornitore.class);
			fornitore.setKey(key);
			fornitore.setDeepRetrieveEnabled(true);
			try {
				fornitore.retrieve();
				if (fornitore != null) {
					dsc = fornitore.getRagioneSociale();
				}
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return dsc;
	}
	//--- MAURO 90013 (21/04/2009) End

	public void setEqual (Copyable obj) throws CopyException
	{
		super.setEqual(obj);
		SchedaLottoGUI schedaLottoGUI = (SchedaLottoGUI) obj;
		iSaldoLotto.setEqual(schedaLottoGUI.iSaldoLotto);
	}

	public boolean initializeOwnedObjects (boolean result)
	{
		return super.initializeOwnedObjects(result);
	}

	public int saveOwnedObjects (int rc) throws SQLException
	{
		return super.saveOwnedObjects(rc);
	}

	public int deleteOwnedObjects () throws SQLException
	{
		return super.deleteOwnedObjects();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap getVarValore (String s)
	{
		HashMap variabileValore = new HashMap();
		if (s != null && s.length() != 0) {
			while (s.indexOf(separator) > 0) {
				String idVariabile = KeyHelper.getTokenObjectKey(s, 1);
				String idValore = KeyHelper.getTokenObjectKey(s, 2);
				variabileValore.put(idVariabile, idValore);
				s = s.substring(s.indexOf(separator) + 1);
				s = s.substring(s.indexOf(separator) + 1);
				if (s.indexOf(separator) > 0) {
					s = s.substring(s.indexOf(separator) + 1);
				}
			}
		}
		return variabileValore;
	}

}
