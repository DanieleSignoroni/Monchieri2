package it.mame.thip.magazzino.generalemag;

import com.thera.thermfw.persist.*;

import it.thera.thip.base.azienda.Azienda;

import java.sql.*;
import java.util.Vector;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.common.*;

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

public class CopiaLottiPrenotazione extends PersistentObject implements BusinessObject {

    protected Proxy iLottiPrenotazione = new Proxy(it.mame.thip.magazzino.generalemag.LottiPrenotazione.class);
    private String idAzienda;
    private LottiPrenotazione iLottiPrenotazioneCopiato = null;

    public CopiaLottiPrenotazione ()
    {
        setIdAzienda(Azienda.getAziendaCorrente());
    }

    public void setLottiPrenotazione (LottiPrenotazione lottoPren)
    {
        this.iLottiPrenotazione.setObject(lottoPren);
    }

    public LottiPrenotazione getLottiPrenotazione ()
    {
        return (LottiPrenotazione) iLottiPrenotazione.getObject();
    }

    public void setLottiPrenotazioneKey (String key)
    {
        iLottiPrenotazione.setKey(key);
    }

    public String getLottiPrenotazioneKey ()
    {
        return iLottiPrenotazione.getKey();
    }

    public void setIdLottiPrenotazione (Integer idLottoPren)
    {
        String key = iLottiPrenotazione.getKey();
        iLottiPrenotazione.setKey(KeyHelper.replaceTokenObjectKey(key, 3, idLottoPren));
    }

    public String getIdLottiPrenotazione ()
    {
        String key = iLottiPrenotazione.getKey();
        String objIdModello = KeyHelper.getTokenObjectKey(key, 3);
        return KeyHelper.objectToString(objIdModello);
    }

    public void setIdAzienda (String azienda)
    {
        this.idAzienda = azienda;
        String key = iLottiPrenotazione.getKey();
        iLottiPrenotazione.setKey(KeyHelper.replaceTokenObjectKey(key, 1, azienda));
    }

    public String getIdAzienda ()
    {
        return idAzienda;
    }

    public String getKey ()
    {
        return "";
    }

    public void setKey (String key)
    {

    }

    protected TableManager getTableManager () throws java.sql.SQLException
    {
        return null;
    }

    @SuppressWarnings("rawtypes")
	public Vector checkAll (BaseComponentsCollection components)
    {
        Vector errors = new Vector();
        components.runAllChecks(errors);
        return errors;
    }

    public boolean retrieve (int lockType) throws SQLException
    {
        return true;
    }

    public int save () throws SQLException
    {
        return 1;
    }

    public int createCopy (String key,String IdArticolo)
    {
    	LottiPrenotazione lottoPren = null;
        int ret = 0;
        try {
            lottoPren = (LottiPrenotazione) Factory.createObject(LottiPrenotazione.class);
            lottoPren.setKey(key);
            lottoPren.setDeepRetrieveEnabled(true);
            if (lottoPren.retrieve(true)) {
            	lottoPren.setOnDB(false);
            	lottoPren.setIdArticolo(IdArticolo);
            	lottoPren.setKey(KeyHelper.replaceTokenObjectKey(key, 2, IdArticolo));
                ret = lottoPren.save();
            }
            if (ret >= 0) {
                setLottiPrenotazioneCopiato(lottoPren);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace(Trace.excStream);
        }
        return ret;
    }

    public void setLottiPrenotazioneCopiato (LottiPrenotazione lottoPren)
    {
        try {
            this.iLottiPrenotazioneCopiato = (LottiPrenotazione) Factory.createObject(LottiPrenotazione.class);
            this.iLottiPrenotazioneCopiato.setEqual(lottoPren);
        }
        catch (CopyException ex) {
            ex.printStackTrace(Trace.excStream);
        }
    }

    public LottiPrenotazione getLottiPrenotazioneCopiato ()
    {
        return this.iLottiPrenotazioneCopiato;
    }

}
