package it.mame.thip.magazzino.generalemag;

import com.thera.thermfw.base.Trace;

import com.thera.thermfw.common.*;
import java.util.*;
import java.sql.*;
import com.thera.thermfw.persist.*;

import it.monchieri.thip.acquisti.ordineAC.YOrdineAcquisto;
import it.thera.thip.acquisti.documentoAC.*;
import it.thera.thip.acquisti.ordineAC.OrdineAcquistoRigaLottoPrm;
import it.thera.thip.acquisti.ordineAC.OrdineAcquistoRigaPrm;
import it.thera.thip.base.comuniVenAcq.*;

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

public class LottiPrenotazione extends LottiPrenotazionePO {

	final static public String SELECT_MAX_RIGA = " SELECT " +
			LottiPrenotazioneTM.ID_COMMESSA +
			" FROM " + LottiPrenotazioneTM.TABLE_NAME +
			" WHERE " + LottiPrenotazioneTM.ID_AZIENDA + " = ?" +
			" AND " + LottiPrenotazioneTM.ID_ARTICOLO + " = ?" +
			" AND " + LottiPrenotazioneTM.ID_LOTTO + " = ?" +
			" ORDER BY " + LottiPrenotazioneTM.ID_COMMESSA;
	public final static CachedStatement csMaxRiga = new CachedStatement(SELECT_MAX_RIGA);

	private static final char ACQUISTO = '1';
	/**
	 * checkDelete
	 * @return ErrorMessage
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 10/03/2009    Wizard     Codice generato da Wizard
	 *
	 */
	public ErrorMessage checkDelete ()
	{
		/**@todo*/
		return null;
	}

	public int save () throws SQLException
	{
		int rc;
		rc = beforeSave();
		if (rc >= ErrorCodes.OK) {
			rc = super.save();
		}

		return rc;
	}

	protected int beforeSave ()
	{
		int rc = ErrorCodes.OK;

		if (isCommessaDummyPrnt(getIdCommessa())) {
			if (!isOnDB()) {
				setIdCommessa(calcolaCommessaDummyPrnt());
			}
		} else {
			if (getIdCommessa() != null) {
				setDscCommessa(getIdCommessa());
			}
		}

		return rc;
	}

	protected boolean isCommessaDummyPrnt (String idCommessa)
	{
		boolean rc = false;

		if (idCommessa != null && idCommessa.length() > 8) {
			if (idCommessa.substring(0, 8).equals("PRNT_LOT")) {
				rc = true;
			}
		}

		return rc;
	}

	protected synchronized String calcolaCommessaDummyPrnt ()
	{
		String idCommessa = null;
		int maxRiga = 0;
		ResultSet rs = null;
		try {
			Database db = ConnectionManager.getCurrentDatabase();
			db.setString(csMaxRiga.getStatement(), 1, getIdAzienda());
			db.setString(csMaxRiga.getStatement(), 2, getIdArticolo());
			db.setString(csMaxRiga.getStatement(), 3, getIdLotto());
			rs = csMaxRiga.getStatement().executeQuery();
			while (rs.next()) {
				String id = rs.getString(LottiPrenotazioneTM.ID_COMMESSA).trim();
				if (id != null && id.length() > 8) {
					if (id.substring(0, 8).equals("PRNT_LOT")) {
						int n = Integer.parseInt(id.substring(8, id.length()));
						if (n > maxRiga + 1) {
							break;
						} else if (n > maxRiga) {
							maxRiga = n;
						}
					}
				}
			}
			maxRiga++;
			idCommessa = "PRNT_LOT" + Integer.toString(maxRiga);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				rs.close();
			}
			catch (SQLException sqlex) {
				sqlex.printStackTrace();
			}
		}

		return idCommessa;
	}

	public int creaPrenotazione (DocumentoTestata testata)
	{
		int rc = ErrorCodes.OK;
		rc = this.crea(testata, 'D');

		return rc;
	}

	public int creaPrenotazione (YOrdineAcquisto ordine)
	{
		int rc = ErrorCodes.OK;
		rc = this.crea(ordine, 'O');

		return rc;
	}

	@SuppressWarnings({ "rawtypes" })
	protected int crea (DocumentoTestata testata, char tipo)
	{
		int rc = ErrorCodes.OK;
		boolean aggiornato = false;
		if (testata instanceof DocumentoAcquisto && testata.getTipoDocumento() == ACQUISTO && testata.isCollegataAMagazzino()) {
			Iterator iRig = testata.getRighe().iterator();
			while (iRig.hasNext()) {
				DocumentoAcqRigaPrm docAcqRig = (DocumentoAcqRigaPrm) iRig.next();
				if (docAcqRig != null && docAcqRig.getIdCommessa() != null) {
					List lotti = docAcqRig.getRigheLotto();
					for (int j = 0; j < lotti.size(); j++) {
						DocumentoAcqRigaLottoPrm lotto = (DocumentoAcqRigaLottoPrm) lotti.get(j);
						//Verifico che il lotto di prodotto non abbia delle prenotazioni
						try {
							Vector prenLotti = LottiPrenotazione.retrieveList(LottiPrenotazioneTM.ID_AZIENDA + "='" + getIdAzienda() + "' AND " + LottiPrenotazioneTM.ID_ARTICOLO + "='" + lotto.getIdArticolo() +
									"' AND " + LottiPrenotazioneTM.ID_LOTTO + "='" + lotto.getIdLotto() + "' AND " + LottiPrenotazioneTM.ID_COMMESSA + "='" + docAcqRig.getIdCommessa() + "'", "", true);
							if (prenLotti.size() == 0) {
								// se non c'è la prenotazione la crea
								LottiPrenotazione prenotazione = (LottiPrenotazione) Factory.createObject(LottiPrenotazione.class);
								prenotazione.setIdAzienda(getIdAzienda());
								prenotazione.setIdArticolo(docAcqRig.getIdArticolo());
								prenotazione.setIdLotto(lotto.getIdLotto());
								prenotazione.setIdCommessa(docAcqRig.getIdCommessa());
								prenotazione.setQuantita(docAcqRig.getServizioQta().getQuantitaInUMSec());
								prenotazione.setEseguita(false);
								prenotazione.setTipoPrenotazione(tipo);
								if ( (rc = prenotazione.save()) < ErrorCodes.OK) {
									break;
								} else {
									aggiornato=true;
								}
							} else {
								// se l'ha trovata ed è la prenotazione fatta dall'ordine modifica la quantità mettendo quella effettiva
								for (Iterator iPren = prenLotti.iterator(); iPren.hasNext(); ) {
									LottiPrenotazione prenotazione = (LottiPrenotazione) iPren.next();
									if (prenotazione.getTipoPrenotazione() == 'O') {
										prenotazione.setQuantita(docAcqRig.getServizioQta().getQuantitaInUMSec());
										prenotazione.setTipoPrenotazione(tipo);
										rc = prenotazione.save();
										if ( (rc = prenotazione.save()) < ErrorCodes.OK) {
											break;
										} else {
											aggiornato=true;
										}
									}
								}
							}
						}
						catch (Exception ex) {
							ex.printStackTrace(Trace.excStream);
							rc = ErrorCodes.GENERIC_ERROR;
						}
					}
				}
			}
		}
		if (rc >= ErrorCodes.OK && aggiornato) {

			try {
				ConnectionManager.commit();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rc;
	}

	@SuppressWarnings("rawtypes")
	protected int crea (YOrdineAcquisto ordine, char tipo)
	{
		int rc = ErrorCodes.OK;
		boolean aggiornato = false;
		Iterator iRig = ordine.getRighe().iterator();
		while (iRig.hasNext()) {
			OrdineAcquistoRigaPrm ordAcqRig = (OrdineAcquistoRigaPrm) iRig.next();
			if (ordAcqRig != null && ordAcqRig.getIdCommessa() != null) {
				List lotti = ordAcqRig.getRigheLotto();
				for (int j = 0; j < lotti.size(); j++) {
					OrdineAcquistoRigaLottoPrm lotto = (OrdineAcquistoRigaLottoPrm) lotti.get(j);
					//Verifico che il lotto di prodotto non abbia delle prenotazioni
					Vector v;
					try {
						v = LottiPrenotazione.retrieveList(LottiPrenotazioneTM.ID_AZIENDA + "='" + getIdAzienda() + "' AND " + LottiPrenotazioneTM.ID_ARTICOLO + "='" + lotto.getIdArticolo() +
								"' AND " + LottiPrenotazioneTM.ID_LOTTO + "='" + lotto.getIdLotto() + "' AND " + LottiPrenotazioneTM.ID_COMMESSA + "='" + ordAcqRig.getIdCommessa() + "'", "", true);
						if (v.size() == 0) {
							LottiPrenotazione prenotazione = (LottiPrenotazione) Factory.createObject(LottiPrenotazione.class);
							prenotazione.setIdAzienda(getIdAzienda());
							prenotazione.setIdArticolo(ordAcqRig.getIdArticolo());
							prenotazione.setIdLotto(lotto.getIdLotto());
							prenotazione.setIdCommessa(ordAcqRig.getIdCommessa());
							prenotazione.setQuantita(ordAcqRig.getServizioQta().getQuantitaInUMSec());
							prenotazione.setEseguita(false);
							prenotazione.setTipoPrenotazione(tipo);
							if ( (rc = prenotazione.save()) < ErrorCodes.OK) {
								break;
							} else {
								aggiornato=true;
							}
						}
					}
					catch (Exception ex) {
						ex.printStackTrace();
						rc = ErrorCodes.GENERIC_ERROR;
					}
				}
			}
		}
		if (rc >= ErrorCodes.OK && aggiornato) {

			try {
				ConnectionManager.commit();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rc;
	}

}
