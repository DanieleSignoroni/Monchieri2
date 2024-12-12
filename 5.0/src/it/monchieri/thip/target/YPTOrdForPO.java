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

public abstract class YPTOrdForPO extends PersistentObject implements BusinessObject, Authorizable, Deletable, ConflictableWithKey {
	
	private static YPTOrdFor cInstance;
	
	protected String numDoc;
	protected String annoDoc;
	protected String codCf;
	protected Date dataDoc;
	protected String codArt;
	protected String desRiga;
	protected Integer numRiga;
	protected String umBase;
	protected BigDecimal quantUmBase;
	protected BigDecimal prezzo;
	protected String noteInt;
	protected Date dataConsRiga;
	protected String id;
	protected Timestamp timestampAgg;
	protected String elaborato;
	
	@SuppressWarnings("rawtypes")
	public static Vector retrieveList(String where, String orderBy, boolean optimistic)
			throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (cInstance == null) {
			cInstance = (YPTOrdFor) Factory.createObject(YPTOrdFor.class);
		}
		return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
	}

	public static YPTOrdFor elementWithKey(String key, int lockType) throws SQLException {
		return (YPTOrdFor) PersistentObject.elementWithKey(YPTOrdFor.class, key, lockType);
	}
	
	public String getNumDoc() {
		return numDoc;
	}
	public void setNumDoc(String numDoc) {
		this.numDoc = numDoc;
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
	public String getUmBase() {
		return umBase;
	}
	public void setUmBase(String umBase) {
		this.umBase = umBase;
		setDirty();
	}
	public BigDecimal getQuantUmBase() {
		return quantUmBase;
	}
	public void setQuantUmBase(BigDecimal quantUmBase) {
		this.quantUmBase = quantUmBase;
		setDirty();
	}
	public BigDecimal getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
		setDirty();
	}
	public String getNoteInt() {
		return noteInt;
	}
	public void setNoteInt(String noteInt) {
		this.noteInt = noteInt;
		setDirty();
	}
	public Date getDataConsRiga() {
		return dataConsRiga;
	}
	public void setDataConsRiga(Date dataConsRiga) {
		this.dataConsRiga = dataConsRiga;
		setDirty();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
		setNumDoc(keys[0]);
		setAnnoDoc(keys[1]);
		setNumRiga(Integer.valueOf(keys[2]));
		setTimestampAgg(Timestamp.valueOf(keys[3]));
	}

	public String getKey() {
		Object[] keyParts = { getNumDoc(),getAnnoDoc(),getNumRiga(),getTimestampAgg() };
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
		return YPTOrdForTM.getInstance();
	}
}
