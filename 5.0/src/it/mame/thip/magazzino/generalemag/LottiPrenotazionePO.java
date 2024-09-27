package it.mame.thip.magazzino.generalemag;

import com.thera.thermfw.persist.*;
import java.sql.*;
import java.util.*;
import it.thera.thip.base.commessa.Commessa;
import it.thera.thip.base.azienda.Azienda;
import java.math.*;
import it.thera.thip.cs.*;
import com.thera.thermfw.common.*;
import com.thera.thermfw.security.*;
import com.thera.thermfw.base.TimeUtils;

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

public abstract class LottiPrenotazionePO extends PersistentObjectDC implements BusinessObject, Authorizable, Deletable, Child, ConflictableWithKey {

    private static LottiPrenotazione cInstance;

    // Attributo
    protected char iTipoPrenotazione = 'M';
    
    protected BigDecimal iQuantita;

    protected java.sql.Date iData = TimeUtils.getCurrentDate();

    //--- MAURO 90014 (29/04/2009) Begin
    protected boolean iEseguita = false;
    //--- MAURO 90014 (29/04/2009) End

    protected String iDscCommessa;

    // Proxy
    protected Proxy iSchedaLotto = new Proxy(it.mame.thip.magazzino.generalemag.SchedaLotto.class);
    protected Proxy iCommessa = new Proxy(it.thera.thip.base.commessa.Commessa.class);

    @SuppressWarnings("rawtypes")
	public static Vector retrieveList (String where, String orderBy, boolean optimistic) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        if (cInstance == null) {
            cInstance = (LottiPrenotazione) Factory.createObject(LottiPrenotazione.class);
        }
        return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
    }

    public static LottiPrenotazione elementWithKey (String key, int lockType) throws SQLException
    {
        return (LottiPrenotazione) PersistentObject.elementWithKey(LottiPrenotazione.class, key, lockType);
    }

    public LottiPrenotazionePO ()
    {
        super();
        setIdAzienda(Azienda.getAziendaCorrente());
    }

    public void setTipoPrenotazione (char tipoPrenotazione)
    {
        this.iTipoPrenotazione = tipoPrenotazione;
        setDirty();
    }

    public char getTipoPrenotazione ()
    {
        return iTipoPrenotazione;
    }

    // Manage Attribut Quantita
    public void setQuantita (BigDecimal quantita)
    {
        this.iQuantita = quantita;
        setDirty();
    }

    public BigDecimal getQuantita ()
    {
        return iQuantita;
    }

    // Manage Attribut Data
    public void setData (java.sql.Date data)
    {
        this.iData = data;
        setDirty();
    }

    public java.sql.Date getData ()
    {
        return iData;
    }

    //--- MAURO 90014 (29/04/2009) Begin
    // Manage Attribut Eseguita
    public void setEseguita (boolean eseguita)
    {
        this.iEseguita = eseguita;
        setDirty();
    }

    public boolean isEseguita ()
    {
        return iEseguita;
    }
    //--- MAURO 90014 (29/04/2009) End

    public void setDscCommessa (String dsc)
    {
        this.iDscCommessa = dsc;
        setDirty();
    }

    public String getDscCommessa ()
    {
        return iDscCommessa;
    }

    // Manage Proxy  SchedaLotto
    public void setSchedaLotto (SchedaLotto schedaLotto)
    {
        String idAzienda = null;
        if (schedaLotto != null) {
            idAzienda = KeyHelper.getTokenObjectKey(schedaLotto.getKey(), 1);
        }
        setIdAziendaInternal(idAzienda);
        this.iSchedaLotto.setObject(schedaLotto);
        setDirty();
        setOnDB(false);
    }

    public SchedaLotto getSchedaLotto ()
    {
        return (SchedaLotto) iSchedaLotto.getObject();
    }

    public void setSchedaLottoKey (String key)
    {
        iSchedaLotto.setKey(key);
        String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
        setIdAziendaInternal(idAzienda);
        setDirty();
        setOnDB(false);
    }

    public String getSchedaLottoKey ()
    {
        return iSchedaLotto.getKey();
    }

    public void setIdLotto (String idLotto)
    {
        String key = iSchedaLotto.getKey();
        iSchedaLotto.setKey(KeyHelper.replaceTokenObjectKey(key, 3, idLotto));
        setDirty();
        setOnDB(false);
    }

    public String getIdLotto ()
    {
        String key = iSchedaLotto.getKey();
        String objIdLotto = KeyHelper.getTokenObjectKey(key, 3);
        return objIdLotto;
    }

    public void setIdArticolo (String idArticolo)
    {
        String key = iSchedaLotto.getKey();
        iSchedaLotto.setKey(KeyHelper.replaceTokenObjectKey(key, 2, idArticolo));
        setDirty();
        setOnDB(false);
    }

    public String getIdArticolo ()
    {
        String key = iSchedaLotto.getKey();
        String objIdArticolo = KeyHelper.getTokenObjectKey(key, 2);
        return objIdArticolo;
    }

    // Manage Proxy  Commessa
    public void setCommessa (Commessa commessa)
    {
        String idAzienda = null;
        if (commessa != null) {
            idAzienda = KeyHelper.getTokenObjectKey(commessa.getKey(), 1);
        }
        setIdAziendaInternal(idAzienda);
        this.iCommessa.setObject(commessa);
        setDirty();
        setOnDB(false);
    }

    public Commessa getCommessa ()
    {
        return (Commessa) iCommessa.getObject();
    }

    public void setCommessaKey (String key)
    {
        iCommessa.setKey(key);
        String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
        setIdAziendaInternal(idAzienda);
        setDirty();
        setOnDB(false);
    }

    public String getCommessaKey ()
    {
        return iCommessa.getKey();
    }

    public void setIdCommessa (String idCommessa)
    {
        String key = iCommessa.getKey();
        iCommessa.setKey(KeyHelper.replaceTokenObjectKey(key, 2, idCommessa));
        setDirty();
        setOnDB(false);
    }

    public String getIdCommessa ()
    {
        String key = iCommessa.getKey();
        String objIdCommessa = KeyHelper.getTokenObjectKey(key, 2);
        return objIdCommessa;
    }

    public void setIdAzienda (String idAzienda)
    {
        setIdAziendaInternal(idAzienda);
        setDirty();
        setOnDB(false);
    }

    public String getIdAzienda ()
    {
        String key = iSchedaLotto.getKey();
        String objIdAzienda = KeyHelper.getTokenObjectKey(key, 1);
        return objIdAzienda;

    }

    public void setEqual (Copyable obj) throws CopyException
    {
        super.setEqual(obj);
        LottiPrenotazionePO lottiPrenotazionePO = (LottiPrenotazionePO) obj;
        if (lottiPrenotazionePO.iData != null) {
            iData = (java.sql.Date) lottiPrenotazionePO.iData.clone();
        }
        iSchedaLotto.setEqual(lottiPrenotazionePO.iSchedaLotto);
        iCommessa.setEqual(lottiPrenotazionePO.iCommessa);
    }

    @SuppressWarnings("rawtypes")
	public Vector checkAll (BaseComponentsCollection components)
    {
        Vector errors = new Vector();
        components.runAllChecks(errors);
        return errors;
    }

    public void setKey (String key)
    {
        setIdAzienda(KeyHelper.getTokenObjectKey(key, 1));
        setIdArticolo(KeyHelper.getTokenObjectKey(key, 2));
        setIdLotto(KeyHelper.getTokenObjectKey(key, 3));
        setIdCommessa(KeyHelper.getTokenObjectKey(key, 4));
    }

    public String getKey ()
    {
        String idAzienda = getIdAzienda();
        String idArticolo = getIdArticolo();
        String idLotto = getIdLotto();
        String idCommessa = getIdCommessa();
        Object[] keyParts = {idAzienda, idArticolo, idLotto, idCommessa};
        return KeyHelper.buildObjectKey(keyParts);
    }

    public boolean isDeletable ()
    {
        return checkDelete() == null;
    }

    public String toString ()
    {
        return getClass().getName() + " [" + KeyHelper.formatKeyString(getKey()) + "]";
    }

    protected TableManager getTableManager () throws SQLException
    {
        return LottiPrenotazioneTM.getInstance();
    }

    protected void setIdAziendaInternal (String idAzienda)
    {
        iCommessa.setKey(KeyHelper.replaceTokenObjectKey(iCommessa.getKey(), 1, idAzienda));
        iSchedaLotto.setKey(KeyHelper.replaceTokenObjectKey(iSchedaLotto.getKey(), 1, idAzienda));
    }

    public String getFatherKey ()
    {
        return getSchedaLottoKey();
    }

    public void setFatherKey (String key)
    {
        setSchedaLottoKey(key);
    }

    public void setFather (PersistentObject father)
    {
        iSchedaLotto.setObject(father);
    }

    public String getOrderByClause ()
    {
        return "";
    }

}
