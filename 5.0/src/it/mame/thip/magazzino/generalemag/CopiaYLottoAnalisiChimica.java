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

public class CopiaYLottoAnalisiChimica extends PersistentObject implements BusinessObject {

    protected Proxy iYLottoAnalisiChimica = new Proxy(it.mame.thip.magazzino.generalemag.YLottoAnalisiChimica.class);
    private String idAzienda;
    private YLottoAnalisiChimica iYLottoAnalisiChimicaCopiato = null;

    public CopiaYLottoAnalisiChimica ()
    {
        setIdAzienda(Azienda.getAziendaCorrente());
    }

    public void setYLottoAnalisiChimica (YLottoAnalisiChimica lottoAnalisi)
    {
        this.iYLottoAnalisiChimica.setObject(lottoAnalisi);
    }

    public YLottoAnalisiChimica getYLottoAnalisiChimica ()
    {
        return (YLottoAnalisiChimica) iYLottoAnalisiChimica.getObject();
    }

    public void setYLottoAnalisiChimicaKey (String key)
    {
        iYLottoAnalisiChimica.setKey(key);
    }

    public String getYLottoAnalisiChimicaKey ()
    {
        return iYLottoAnalisiChimica.getKey();
    }

    public void setIdYLottoAnalisiChimica (Integer idLottoAnalisi)
    {
        String key = iYLottoAnalisiChimica.getKey();
        iYLottoAnalisiChimica.setKey(KeyHelper.replaceTokenObjectKey(key, 3, idLottoAnalisi));
    }

    public String getIdYLottoAnalisiChimica ()
    {
        String key = iYLottoAnalisiChimica.getKey();
        String objIdModello = KeyHelper.getTokenObjectKey(key, 3);
        return KeyHelper.objectToString(objIdModello);
    }

    public void setIdAzienda (String azienda)
    {
        this.idAzienda = azienda;
        String key = iYLottoAnalisiChimica.getKey();
        iYLottoAnalisiChimica.setKey(KeyHelper.replaceTokenObjectKey(key, 1, azienda));
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
    	YLottoAnalisiChimica lottoAnalisi = null;
        int ret = 0;
        try {
            lottoAnalisi = (YLottoAnalisiChimica) Factory.createObject(YLottoAnalisiChimica.class);
            lottoAnalisi.setKey(key);
            lottoAnalisi.setDeepRetrieveEnabled(true);
            if (lottoAnalisi.retrieve(true)) {
                lottoAnalisi.setOnDB(false);
                lottoAnalisi.setIdArticolo(IdArticolo);
                lottoAnalisi.setKey(KeyHelper.replaceTokenObjectKey(key, 2, IdArticolo));
                ret = lottoAnalisi.save();
            }
            if (ret >= 0) {
                setYLottoAnalisiChimicaCopiato(lottoAnalisi);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace(Trace.excStream);
        }
        return ret;
    }

    public void setYLottoAnalisiChimicaCopiato (YLottoAnalisiChimica lottoAnalisi)
    {
        try {
            this.iYLottoAnalisiChimicaCopiato = (YLottoAnalisiChimica) Factory.createObject(YLottoAnalisiChimica.class);
            this.iYLottoAnalisiChimicaCopiato.setEqual(lottoAnalisi);
        }
        catch (CopyException ex) {
            ex.printStackTrace(Trace.excStream);
        }
    }

    public YLottoAnalisiChimica getYLottoAnalisiChimicaCopiato ()
    {
        return this.iYLottoAnalisiChimicaCopiato;
    }

}
