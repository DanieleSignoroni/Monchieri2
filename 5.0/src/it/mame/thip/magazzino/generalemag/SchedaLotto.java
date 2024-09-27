package it.mame.thip.magazzino.generalemag;

import java.sql.*;
import java.util.*;
import com.thera.thermfw.persist.*;
import it.thera.thip.base.fornitore.*;
import it.thera.thip.qualita.controllo.DocumentoCollaudoRiga;
import it.thera.thip.qualita.controllo.DocumentoCollaudoTestata;
import it.thera.thip.qualita.controllo.DocumentoCollaudoTestataTM;
import it.thera.thip.base.azienda.Azienda;
import it.mame.thip.qualita.controllo.*;
import it.monchieri.thip.qualita.controllo.YDocumentiCollaudoRilevazioneMisure;
import it.monchieri.thip.qualita.controllo.YMisuraFase;

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

public class SchedaLotto extends YLotto {

	protected OneToMany iLottiPrenotazione = new OneToMany(it.mame.thip.magazzino.generalemag.LottiPrenotazione.class, this, 7, false);

	@SuppressWarnings("rawtypes")
	protected List iAnalisiChimica = new ArrayList();

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

	public void setCodiceAzienda (String idAzienda)
	{
		super.setCodiceAzienda(idAzienda);
		if (iLottiPrenotazione != null) {
			iLottiPrenotazione.setFatherKeyChanged();
		}
	}

	public void setCodiceArticolo (String idArticolo)
	{
		super.setCodiceArticolo(idArticolo);
		if (iLottiPrenotazione != null) {
			iLottiPrenotazione.setFatherKeyChanged();
		}
	}

	public void setCodiceLotto (String idLotto)
	{
		super.setCodiceLotto(idLotto);
		if (iLottiPrenotazione != null) {
			iLottiPrenotazione.setFatherKeyChanged();
		}
	}

	public void setEqual (Copyable obj) throws CopyException
	{
		super.setEqual(obj);
		SchedaLotto schedaLotto = (SchedaLotto) obj;
		iLottiPrenotazione.setEqual(schedaLotto.iLottiPrenotazione);
	}

	protected OneToMany getLottiPrenotazioneInternal ()
	{
		if (iLottiPrenotazione.isNew()) {
			iLottiPrenotazione.retrieve();
		}
		return iLottiPrenotazione;
	}

	@SuppressWarnings("rawtypes")
	public List getLottiPrenotazione (){
		return getLottiPrenotazioneInternal();
	}

	public boolean initializeOwnedObjects (boolean result)
	{
		result = super.initializeOwnedObjects(result);
		if (iLottiPrenotazione != null) {
			result = iLottiPrenotazione.initialize(result);
		}

		caricaAnalisiChimica();

		return result;
	}

	public int saveOwnedObjects (int rc) throws SQLException
	{
		rc = super.saveOwnedObjects(rc);
		if (iLottiPrenotazione != null) {
			rc = iLottiPrenotazione.save(rc);
		}
		return rc;
	}

	public int deleteOwnedObjects () throws SQLException
	{
		int rc = super.deleteOwnedObjects();
		if (iLottiPrenotazione != null) {
			rc = iLottiPrenotazione.delete(rc);
		}
		return rc;
	}

	@SuppressWarnings("rawtypes")
	public void setAnalisiChinica (List ac){
		iAnalisiChimica = ac;
	}

	@SuppressWarnings("rawtypes")
	public List getAnalisiChinica (){
		return iAnalisiChimica;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void caricaAnalisiChimica ()
	{
		DocumentoCollaudoTestata doccol = null;
		Vector v = null;

		try {
			v = DocumentoCollaudoTestata.retrieveList(DocumentoCollaudoTestataTM.ID_AZIENDA + "='" + getCodiceAzienda() + "'" +
					" AND " + DocumentoCollaudoTestataTM.ID_TIPO_DOCPRV + " IN ('A01', 'A02')" +
					//                " AND " + DocumentoCollaudoTestataTM.R_ARTICOLO + "='" + getCodiceArticolo() + "'" +
					" AND " + DocumentoCollaudoTestataTM.R_LOTTO + "='" + getCodiceLotto() + "'" +
					" AND " + DocumentoCollaudoTestataTM.STATO + "='V'" +
					" AND " + DocumentoCollaudoTestataTM.STATO_COLL + " >= '" + DocumentoCollaudoTestata.COLLAUDO_DICHIARATO + "'",
					DocumentoCollaudoTestataTM.TIMESTAMP_AGG + " DESC", true);
			if (!v.isEmpty()) {
				doccol = (DocumentoCollaudoTestata) v.get(0);
				Iterator iRig = doccol.getRighe().iterator();
				if (iRig.hasNext()) {
					DocumentoCollaudoRiga doccolrig = (DocumentoCollaudoRiga) iRig.next();
					Iterator iMisFas = doccolrig.getMisureFasi().iterator();
					while (iMisFas.hasNext()) {
						YMisuraFase fase = (YMisuraFase) iMisFas.next();
						if (fase.getIdCodificaFaseMame().intValue() == 10) {
							List vCarat = fase.getMisuraCaracteriche();
							Iterator iCarat = vCarat.iterator();
							while (iCarat.hasNext()) {
								YMisuraCaracteriche carat = (YMisuraCaracteriche) iCarat.next();
								List vMisure = carat.getMisura();
								if (!vMisure.isEmpty()) {
									YDocumentiCollaudoRilevazioneMisure misuraRilevati = (YDocumentiCollaudoRilevazioneMisure) vMisure.get(0);
									LottoAnalisiChimica lottoAC = (LottoAnalisiChimica) Factory.createObject(LottoAnalisiChimica.class);
									lottoAC.setSchedaLotto(this);
									lottoAC.setCaratteristica(carat.getDescrizioneCaratteristica());
									lottoAC.setLimiteInfTol(misuraRilevati.getLimInfTolleranza());
									lottoAC.setLimiteSupTol(misuraRilevati.getLimSuoTolleranza());
									lottoAC.setValNominale(misuraRilevati.getValoreNominale());
									lottoAC.setValore01(misuraRilevati.getValoreRilevato());
									//lottoAC.setValore02(misuraRilevati.getValoreRilevatoMame());
									lottoAC.setEsito01(misuraRilevati.getIndicazioneEsito());
									// lottoAC.setEsito02(misuraRilevati.getIndicazioneEsitoMame());
									getAnalisiChinica().add(lottoAC);
								}
							}
							break;
						}
					}
				}
			} else {
				caricaAnalisiChimicaDaScheda();
			}
		}
		catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void caricaAnalisiChimicaDaScheda ()
	{
		YLottoAnalisiChimica daticol = null;
		Vector v = null;

		try {
			v = YLottoAnalisiChimica.retrieveList(YLottoAnalisiChimicaTM.ID_AZIENDA + "='" + getCodiceAzienda() + "'" +
					//                                                  " AND " + YLottoAnalisiChimicaTM.ID_ARTICOLO + "='" + getCodiceArticolo() + "'" +
					" AND " + YLottoAnalisiChimicaTM.ID_LOTTO + "='" + getCodiceLotto() + "'",
					YLottoAnalisiChimicaTM.ID_FASE_SGQ + ", " + YLottoAnalisiChimicaTM.ID_CARAT_SGQ + ", " +
							YLottoAnalisiChimicaTM.ID_PROGR_PEZZO, true);
			Iterator iRig = v.iterator();
			while (iRig.hasNext()) {
				daticol = (YLottoAnalisiChimica) iRig.next();
				if (daticol.getIdFaseSgq() == 10) {
					LottoAnalisiChimica lottoAC = (LottoAnalisiChimica) Factory.createObject(LottoAnalisiChimica.class);
					lottoAC.setSchedaLotto(this);
					lottoAC.setCaratteristica(daticol.getDscCaratSgq());
					lottoAC.setLimiteInfTol(daticol.getLimInfTol());
					lottoAC.setLimiteSupTol(daticol.getLimSupTol());
					lottoAC.setValNominale(daticol.getValoreNominale());
					lottoAC.setValore01(daticol.getValRilevato1());
					lottoAC.setValore02(daticol.getValRilevato2());
					lottoAC.setEsito01(daticol.getEsito1());
					lottoAC.setEsito02(daticol.getEsito2());
					getAnalisiChinica().add(lottoAC);
				}
			}
		}
		catch (Throwable ex) {
			ex.printStackTrace();
		}
	}
}
