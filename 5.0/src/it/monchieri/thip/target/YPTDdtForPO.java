package it.monchieri.thip.target;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.Vector;

import com.thera.thermfw.common.BaseComponentsCollection;
import com.thera.thermfw.common.BusinessObject;
import com.thera.thermfw.common.Deletable;
import com.thera.thermfw.persist.CopyException;
import com.thera.thermfw.persist.Copyable;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.persist.TableManager;
import com.thera.thermfw.security.Authorizable;
import com.thera.thermfw.security.ConflictableWithKey;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 12/12/2024
 * <br><br>
 * <b>71XXX    DSSOF3    12/12/2024</b>
 * <p></p>
 */

public abstract class YPTDdtForPO extends PersistentObject implements BusinessObject, Authorizable, Deletable, ConflictableWithKey {
	
	private static YPTDdtFor cInstance;

	protected String numDdtInt;
	protected String annoDoc;
	protected String codCf;
	protected Date dataDoc;
	protected String codiceCausale;
	protected String codDep;
	protected String codDep2;
	protected String codArt;
	protected String desRiga;
	protected Integer numRiga;
	protected String umAcq;
	protected BigDecimal qtaConsegnata;
	protected BigDecimal prezzo;
	protected String lotto;
	protected String numDdtFornitore;
	protected Date dataDocFornitore;
	protected String lingotto;
	protected String riferimentoAnnoOrdFor;
	protected String riferimentoNumOrdFor;
	protected Integer riferimentoRigaOrdFor;
	protected Timestamp timestampAgg;
	protected String elaborato;
	
	@SuppressWarnings("rawtypes")
	public static Vector retrieveList(String where, String orderBy, boolean optimistic)
			throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (cInstance == null) {
			cInstance = (YPTDdtFor) Factory.createObject(YPTDdtFor.class);
		}
		return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
	}

	public static YPTDdtFor elementWithKey(String key, int lockType) throws SQLException {
		return (YPTDdtFor) PersistentObject.elementWithKey(YPTDdtFor.class, key, lockType);
	}

	public String getNumDdtInt() {
		return numDdtInt;
	}

	public void setNumDdtInt(String numDdtInt) {
		this.numDdtInt = numDdtInt;
		setDirty();
	}

	public String getAnnoDoc() {
		return annoDoc;
	}

	public void setAnnoDoc(String annoDoc) {
		this.annoDoc = annoDoc;
		setDirty();
	}

	public String getCodCf() {
		return codCf;
	}

	public void setCodCf(String codCf) {
		this.codCf = codCf;
		setDirty();
	}

	public Date getDataDoc() {
		return dataDoc;
	}

	public void setDataDoc(Date dataDoc) {
		this.dataDoc = dataDoc;
		setDirty();
	}

	public String getCodiceCausale() {
		return codiceCausale;
	}

	public void setCodiceCausale(String codiceCausale) {
		this.codiceCausale = codiceCausale;
		setDirty();
	}

	public String getCodDep() {
		return codDep;
	}

	public void setCodDep(String codDep) {
		this.codDep = codDep;
		setDirty();
	}

	public String getCodDep2() {
		return codDep2;
	}

	public void setCodDep2(String codDep2) {
		this.codDep2 = codDep2;
		setDirty();
	}

	public String getCodArt() {
		return codArt;
	}

	public void setCodArt(String codArt) {
		this.codArt = codArt;
		setDirty();
	}

	public String getDesRiga() {
		return desRiga;
	}

	public void setDesRiga(String desRiga) {
		this.desRiga = desRiga;
		setDirty();
	}

	public Integer getNumRiga() {
		return numRiga;
	}

	public void setNumRiga(Integer numRiga) {
		this.numRiga = numRiga;
		setDirty();
	}

	public String getUmAcq() {
		return umAcq;
	}

	public void setUmAcq(String umAcq) {
		this.umAcq = umAcq;
		setDirty();
	}

	public BigDecimal getQtaConsegnata() {
		return qtaConsegnata;
	}

	public void setQtaConsegnata(BigDecimal qtaConsegnata) {
		this.qtaConsegnata = qtaConsegnata;
		setDirty();
	}

	public BigDecimal getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
		setDirty();
	}

	public String getLotto() {
		return lotto;
	}

	public void setLotto(String lotto) {
		this.lotto = lotto;
		setDirty();
	}

	public String getNumDdtFornitore() {
		return numDdtFornitore;
	}

	public void setNumDdtFornitore(String numDdtFornitore) {
		this.numDdtFornitore = numDdtFornitore;
		setDirty();
	}

	public Date getDataDocFornitore() {
		return dataDocFornitore;
	}

	public void setDataDocFornitore(Date dataDocFornitore) {
		this.dataDocFornitore = dataDocFornitore;
		setDirty();
	}

	public String getLingotto() {
		return lingotto;
	}

	public void setLingotto(String lingotto) {
		this.lingotto = lingotto;
		setDirty();
	}

	public String getRiferimentoAnnoOrdFor() {
		return riferimentoAnnoOrdFor;
	}

	public void setRiferimentoAnnoOrdFor(String riferimentoAnnoOrdFor) {
		this.riferimentoAnnoOrdFor = riferimentoAnnoOrdFor;
		setDirty();
	}

	public String getRiferimentoNumOrdFor() {
		return riferimentoNumOrdFor;
	}

	public void setRiferimentoNumOrdFor(String riferimentoNumOrdFor) {
		this.riferimentoNumOrdFor = riferimentoNumOrdFor;
		setDirty();
	}

	public Integer getRiferimentoRigaOrdFor() {
		return riferimentoRigaOrdFor;
	}

	public void setRiferimentoRigaOrdFor(Integer riferimentoRigaOrdFor) {
		this.riferimentoRigaOrdFor = riferimentoRigaOrdFor;
		setDirty();
	}

	public Timestamp getTimestampAgg() {
		return timestampAgg;
	}

	public void setTimestampAgg(Timestamp timestampAgg) {
		this.timestampAgg = timestampAgg;
		setDirty();
	}

	public String getElaborato() {
		return elaborato;
	}

	public void setElaborato(String elaborato) {
		this.elaborato = elaborato;
		setDirty();
	}
	
	@Override
	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
	}

	@SuppressWarnings("rawtypes")
	public Vector checkAll(BaseComponentsCollection components) {
		Vector errors = new Vector();
		components.runAllChecks(errors);
		return errors;
	}

	public void setKey(String key) {
		String[] keys = KeyHelper.unpackObjectKey(key);
		setNumDdtInt(keys[0]);
		setAnnoDoc(keys[1]);
		setNumRiga(Integer.valueOf(keys[2]));
		setTimestampAgg(Timestamp.valueOf(keys[3]));
	}

	public String getKey() {
		Object[] keyParts = { getNumDdtInt(),getAnnoDoc(),getNumRiga(),getTimestampAgg() };
		return KeyHelper.buildObjectKey(keyParts);
	}

	public boolean isDeletable() {
		return checkDelete() == null;
	}

	public String toString() {
		return getClass().getName() + " [" + KeyHelper.formatKeyString(getKey()) + "]";
	}

	@Override
	protected TableManager getTableManager() throws SQLException {
		return YPTDdtForTM.getInstance();
	}
}
