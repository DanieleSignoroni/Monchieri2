package it.mame.thip.magazzino.generalemag;

import com.thera.thermfw.persist.*;
import it.thera.thip.base.azienda.*;
import java.sql.SQLException;
import java.util.Vector;
import com.thera.thermfw.common.BaseComponentsCollection;
import com.thera.thermfw.common.BusinessObjectAdapter;
import java.math.*;

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

public class LottoAnalisiChimica extends BusinessObjectAdapter implements Child {
	
    protected Proxy iSchedaLotto = new Proxy(SchedaLotto.class);
    protected String iCaratteristica;
    protected BigDecimal iLimiteInfTol;
    protected BigDecimal iLimiteSupTol;
    protected BigDecimal iValNominale;
    protected BigDecimal iValore01;
    protected BigDecimal iValore02;
    protected char iEsito01;
    protected char iEsito02;


    public LottoAnalisiChimica ()
    {
        setIdAzienda(Azienda.getAziendaCorrente());
    }

    public void setCaratteristica (String caratteristica)
    {
        iCaratteristica = caratteristica;
    }

    public String getCaratteristica ()
    {
        return iCaratteristica;
    }

    public void setLimiteInfTol (BigDecimal n)
    {
        iLimiteInfTol = n;
    }

    public BigDecimal getLimiteInfTol ()
    {
        return iLimiteInfTol;
    }

    public void setLimiteSupTol (BigDecimal n)
    {
        iLimiteSupTol = n;
    }

    public BigDecimal getLimiteSupTol ()
    {
        return iLimiteSupTol;
    }

    public void setValNominale (BigDecimal n)
    {
        iValNominale = n;
    }

    public BigDecimal getValNominale ()
    {
        return iValNominale;
    }

    public void setValore01 (BigDecimal v)
    {
        iValore01 = v;
    }

    public BigDecimal getValore01 ()
    {
        return iValore01;
    }

    public void setValore02 (BigDecimal v)
    {
        iValore02 = v;
    }

    public BigDecimal getValore02 ()
    {
        return iValore02;
    }

    public void setEsito01 (char esito)
    {
        iEsito01 = esito;
    }

    public char getEsito01 ()
    {
        return iEsito01;
    }

    public void setEsito02 (char esito)
    {
        iEsito02 = esito;
    }

    public char getEsito02 ()
    {
        return iEsito02;
    }

    public void setIdAzienda (String idAzienda)
    {
        setIdAziendaInternal(idAzienda);
    }

    public String getIdAzienda ()
    {
        String key = iSchedaLotto.getKey();
        String objIdAzienda = KeyHelper.getTokenObjectKey(key, 1);
        return objIdAzienda;
    }

    protected void setIdAziendaInternal (String idAzienda)
    {
        String key1 = iSchedaLotto.getKey();
        iSchedaLotto.setKey(KeyHelper.replaceTokenObjectKey(key1, 1, idAzienda));
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
    }

    public String getSchedaLottoKey ()
    {
        return iSchedaLotto.getKey();
    }

    public void setIdLotto (String idLotto)
    {
        String key = iSchedaLotto.getKey();
        iSchedaLotto.setKey(KeyHelper.replaceTokenObjectKey(key, 3, idLotto));
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
    }

    public String getIdArticolo ()
    {
        String key = iSchedaLotto.getKey();
        String objIdArticolo = KeyHelper.getTokenObjectKey(key, 2);
        return objIdArticolo;
    }

    //******** Manage Father
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

    public void setKey (String key)
    {
        setIdAzienda(KeyHelper.getTokenObjectKey(key, 1));
        setIdArticolo(KeyHelper.getTokenObjectKey(key, 2));
        setIdLotto(KeyHelper.getTokenObjectKey(key, 3));
        setCaratteristica(KeyHelper.getTokenObjectKey(key, 4));
    }

    public String getKey ()
    {
        String idAzienda = getIdAzienda();
        String idArticolo = getIdArticolo();
        String idLotto = getIdLotto();
        String caratteristica = getCaratteristica();
        Object[] keyParts = {idAzienda, idArticolo, idLotto, caratteristica};
        return KeyHelper.buildObjectKey(keyParts);
    }

    public void setEqual (Copyable obj) throws CopyException
    {
        super.setEqual(obj);
        LottoAnalisiChimica lottoAC = (LottoAnalisiChimica) obj;
        iSchedaLotto.setEqual(lottoAC.iSchedaLotto);
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
        return false;
    }
}
