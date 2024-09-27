package it.mame.thip.magazzino.generalemag;

import com.thera.thermfw.persist.*;
import java.sql.*;
import java.util.*;
import java.math.*;
import com.thera.thermfw.common.*;
import com.thera.thermfw.security.*;

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

public abstract class YLottoAnalisiChimicaPO extends PersistentObject implements BusinessObject, Authorizable, Deletable, Child, ConflictableWithKey {

    private static YLottoAnalisiChimica cInstance;

    protected short iIdFaseSgq;

    protected short iIdCaratSgq;

    protected int iIdProgrPezzo;

    protected String iDscFaseSgq;

    protected String iDscCaratSgq;

    protected String iDscCaratSgq2;

    protected String iIdUnitaMisura;

    protected BigDecimal iLimInfTol;

    protected BigDecimal iLimSupTol;

    protected BigDecimal iValoreNominale;

    protected BigDecimal iValRilevato1;

    protected char iEsito1;

    protected BigDecimal iValRilevato2;

    protected char iEsito2;

    protected String iAnnotazioni;

    protected Proxy iLotto = new Proxy(it.mame.thip.magazzino.generalemag.SchedaLotto.class);

    @SuppressWarnings("rawtypes")
	public static Vector retrieveList (String where, String orderBy, boolean optimistic) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        if (cInstance == null) {
            cInstance = (YLottoAnalisiChimica) Factory.createObject(YLottoAnalisiChimica.class);
        }
        return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
    }

    public static YLottoAnalisiChimica elementWithKey (String key, int lockType) throws SQLException
    {
        return (YLottoAnalisiChimica) PersistentObject.elementWithKey(YLottoAnalisiChimica.class, key, lockType);
    }

    /**
     * Valorizza l'attributo.
     * @param idFaseSgq
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setIdFaseSgq (short idFaseSgq)
    {
        this.iIdFaseSgq = idFaseSgq;
        setDirty();
        setOnDB(false);
    }

    /**
     * Restituisce l'attributo.
     * @return short
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public short getIdFaseSgq ()
    {
        return iIdFaseSgq;
    }

    /**
     * Valorizza l'attributo.
     * @param idCaratSgq
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setIdCaratSgq (short idCaratSgq)
    {
        this.iIdCaratSgq = idCaratSgq;
        setDirty();
        setOnDB(false);
    }

    /**
     * Restituisce l'attributo.
     * @return short
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public short getIdCaratSgq ()
    {
        return iIdCaratSgq;
    }

    /**
     * Valorizza l'attributo.
     * @param idProgrPezzo
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setIdProgrPezzo (int idProgrPezzo)
    {
        this.iIdProgrPezzo = idProgrPezzo;
        setDirty();
        setOnDB(false);
    }

    /**
     * Restituisce l'attributo.
     * @return int
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public int getIdProgrPezzo ()
    {
        return iIdProgrPezzo;
    }

    /**
     * Valorizza l'attributo.
     * @param dscFaseSgq
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setDscFaseSgq (String dscFaseSgq)
    {
        this.iDscFaseSgq = dscFaseSgq;
        setDirty();
    }

    /**
     * Restituisce l'attributo.
     * @return String
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public String getDscFaseSgq ()
    {
        return iDscFaseSgq;
    }

    /**
     * Valorizza l'attributo.
     * @param dscCaratSgq
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setDscCaratSgq (String dscCaratSgq)
    {
        this.iDscCaratSgq = dscCaratSgq;
        setDirty();
    }

    /**
     * Restituisce l'attributo.
     * @return String
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public String getDscCaratSgq ()
    {
        return iDscCaratSgq;
    }

    /**
     * Valorizza l'attributo.
     * @param dscCaratSgq2
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setDscCaratSgq2 (String dscCaratSgq2)
    {
        this.iDscCaratSgq2 = dscCaratSgq2;
        setDirty();
    }

    /**
     * Restituisce l'attributo.
     * @return String
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public String getDscCaratSgq2 ()
    {
        return iDscCaratSgq2;
    }

    /**
     * Valorizza l'attributo.
     * @param idUnitaMisura
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setIdUnitaMisura (String idUnitaMisura)
    {
        this.iIdUnitaMisura = idUnitaMisura;
        setDirty();
    }

    /**
     * Restituisce l'attributo.
     * @return String
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public String getIdUnitaMisura ()
    {
        return iIdUnitaMisura;
    }

    /**
     * Valorizza l'attributo.
     * @param limInfTol
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setLimInfTol (BigDecimal limInfTol)
    {
        this.iLimInfTol = limInfTol;
        setDirty();
    }

    /**
     * Restituisce l'attributo.
     * @return BigDecimal
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public BigDecimal getLimInfTol ()
    {
        return iLimInfTol;
    }

    /**
     * Valorizza l'attributo.
     * @param limSupTol
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setLimSupTol (BigDecimal limSupTol)
    {
        this.iLimSupTol = limSupTol;
        setDirty();
    }

    /**
     * Restituisce l'attributo.
     * @return BigDecimal
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public BigDecimal getLimSupTol ()
    {
        return iLimSupTol;
    }

    /**
     * Valorizza l'attributo.
     * @param valoreNominale
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setValoreNominale (BigDecimal valoreNominale)
    {
        this.iValoreNominale = valoreNominale;
        setDirty();
    }

    /**
     * Restituisce l'attributo.
     * @return BigDecimal
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public BigDecimal getValoreNominale ()
    {
        return iValoreNominale;
    }

    /**
     * Valorizza l'attributo.
     * @param valRilevato1
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setValRilevato1 (BigDecimal valRilevato1)
    {
        this.iValRilevato1 = valRilevato1;
        setDirty();
    }

    /**
     * Restituisce l'attributo.
     * @return BigDecimal
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public BigDecimal getValRilevato1 ()
    {
        return iValRilevato1;
    }

    /**
     * Valorizza l'attributo.
     * @param esito1
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setEsito1 (char esito1)
    {
        this.iEsito1 = esito1;
        setDirty();
    }

    /**
     * Restituisce l'attributo.
     * @return char
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public char getEsito1 ()
    {
        return iEsito1;
    }

    /**
     * Valorizza l'attributo.
     * @param valRilevato2
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setValRilevato2 (BigDecimal valRilevato2)
    {
        this.iValRilevato2 = valRilevato2;
        setDirty();
    }

    /**
     * Restituisce l'attributo.
     * @return BigDecimal
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public BigDecimal getValRilevato2 ()
    {
        return iValRilevato2;
    }

    /**
     * Valorizza l'attributo.
     * @param esito2
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setEsito2 (char esito2)
    {
        this.iEsito2 = esito2;
        setDirty();
    }

    /**
     * Restituisce l'attributo.
     * @return char
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public char getEsito2 ()
    {
        return iEsito2;
    }

    /**
     * Valorizza l'attributo.
     * @param annotazioni
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setAnnotazioni (String annotazioni)
    {
        this.iAnnotazioni = annotazioni;
        setDirty();
    }

    /**
     * Restituisce l'attributo.
     * @return String
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public String getAnnotazioni ()
    {
        return iAnnotazioni;
    }

    /**
     * Valorizza l'attributo.
     * @param lotto
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setLotto (SchedaLotto lotto)
    {
        this.iLotto.setObject(lotto);
        setDirty();
        setOnDB(false);
    }

    /**
     * Restituisce l'attributo.
     * @return SchedaLotto
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public SchedaLotto getLotto ()
    {
        return (SchedaLotto) iLotto.getObject();
    }

    /**
     * setLottoKey
     * @param key
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setLottoKey (String key)
    {
        iLotto.setKey(key);
        setDirty();
        setOnDB(false);
    }

    /**
     * getLottoKey
     * @return String
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public String getLottoKey ()
    {
        return iLotto.getKey();
    }

    /**
     * Valorizza l'attributo.
     * @param idAzienda
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setIdAzienda (String idAzienda)
    {
        String key = iLotto.getKey();
        iLotto.setKey(KeyHelper.replaceTokenObjectKey(key, 1, idAzienda));
        setDirty();
        setOnDB(false);
    }

    /**
     * Restituisce l'attributo.
     * @return String
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public String getIdAzienda ()
    {
        String key = iLotto.getKey();
        String objIdAzienda = KeyHelper.getTokenObjectKey(key, 1);
        return objIdAzienda;

    }

    /**
     * Valorizza l'attributo.
     * @param idArticolo
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setIdArticolo (String idArticolo)
    {
        String key = iLotto.getKey();
        iLotto.setKey(KeyHelper.replaceTokenObjectKey(key, 2, idArticolo));
        setDirty();
        setOnDB(false);
    }

    /**
     * Restituisce l'attributo.
     * @return String
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public String getIdArticolo ()
    {
        String key = iLotto.getKey();
        String objIdArticolo = KeyHelper.getTokenObjectKey(key, 2);
        return objIdArticolo;

    }

    /**
     * Valorizza l'attributo.
     * @param idLotto
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setIdLotto (String idLotto)
    {
        String key = iLotto.getKey();
        iLotto.setKey(KeyHelper.replaceTokenObjectKey(key, 3, idLotto));
        setDirty();
        setOnDB(false);
    }

    /**
     * Restituisce l'attributo.
     * @return String
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public String getIdLotto ()
    {
        String key = iLotto.getKey();
        String objIdLotto = KeyHelper.getTokenObjectKey(key, 3);
        return objIdLotto;
    }

    /**
     * setEqual
     * @param obj
     * @throws CopyException
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setEqual (Copyable obj) throws CopyException
    {
        super.setEqual(obj);
        YLottoAnalisiChimicaPO yLottoAnalisiChimicaPO = (YLottoAnalisiChimicaPO) obj;
        iLotto.setEqual(yLottoAnalisiChimicaPO.iLotto);
    }

    /**
     * checkAll
     * @param components
     * @return Vector
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    @SuppressWarnings("rawtypes")
	public Vector checkAll (BaseComponentsCollection components)
    {
        Vector errors = new Vector();
        components.runAllChecks(errors);
        return errors;
    }

    /**
     *  setKey
     * @param key
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setKey (String key)
    {
        String[] keys = KeyHelper.unpackObjectKey(key);
        setIdAzienda(keys[0]);
        setIdArticolo(keys[1]);
        setIdLotto(keys[2]);
        setIdFaseSgq(KeyHelper.stringToShort(keys[3]));
        setIdCaratSgq(KeyHelper.stringToShort(keys[4]));
        setIdProgrPezzo(KeyHelper.stringToInt(keys[5]));
    }

    /**
     *  getKey
     * @return String
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public String getKey ()
    {
        String idAzienda = getIdAzienda();
        String idArticolo = getIdArticolo();
        String idLotto = getIdLotto();
        Short idFaseSgq = new Short(getIdFaseSgq());
        Short idCaratSgq = new Short(getIdCaratSgq());
        Integer idProgrPezzo = new Integer(getIdProgrPezzo());
        Object[] keyParts = {idAzienda, idArticolo, idLotto, idFaseSgq, idCaratSgq, idProgrPezzo};
        return KeyHelper.buildObjectKey(keyParts);
    }

    /**
     * isDeletable
     * @return boolean
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public boolean isDeletable ()
    {
        return checkDelete() == null;
    }

    /**
     * getFatherKey
     * @return String
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public String getFatherKey ()
    {
        return getLottoKey();
    }

    /**
     * setFatherKey
     * @param key
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setFatherKey (String key)
    {
        setLottoKey(key);
    }

    /**
     * setFather
     * @param father
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public void setFather (PersistentObject father)
    {
        iLotto.setObject(father);
    }

    /**
     * getOrderByClause
     * @return String
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public String getOrderByClause ()
    {
        return "";
    }

    /**
     * toString
     * @return String
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    Wizard     Codice generato da Wizard
     *
     */
    public String toString ()
    {
        return getClass().getName() + " [" + KeyHelper.formatKeyString(getKey()) + "]";
    }

    /**
     *  getTableManager
     * @return TableManager
     * @throws SQLException
     */
    /*
     * Revisions:
     * Date          Owner      Description
     * 26/05/2016    CodeGen     Codice generato da CodeGenerator
     *
     */
    protected TableManager getTableManager () throws SQLException
    {
        return YLottoAnalisiChimicaTM.getInstance();
    }

}
