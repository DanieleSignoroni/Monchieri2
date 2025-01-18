package it.monchieri.thip.target;

import java.sql.SQLException;
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

public abstract class NoteAttivitaSogCollaudoPO extends PersistentObject implements BusinessObject, Authorizable, Deletable, ConflictableWithKey {

	private static NoteAttivitaSogCollaudo cInstance;

	protected String iIdAzienda;
	protected String iOrdCliRiga;
	protected String iNomeTabella;
	protected String iNumeroOrdFmt;
	protected String iDescription;
	protected String iNlsCommentText;
	protected String iIdCommentUse;
	protected boolean iReserved;
	protected char iTextType;
	protected Integer iCommentType1;
	protected Integer iCommentType2;
	protected String iIdAttivita;
	protected Integer idSequenzaFase;    
	protected Integer iSeqVisualizz;
	protected String iLanguage;
	
	@SuppressWarnings("rawtypes")
	public static Vector retrieveList(String where, String orderBy, boolean optimistic)
			throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (cInstance == null) {
			cInstance = (NoteAttivitaSogCollaudo) Factory.createObject(NoteAttivitaSogCollaudo.class);
		}
		return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
	}

	public static NoteAttivitaSogCollaudo elementWithKey(String key, int lockType) throws SQLException {
		return (NoteAttivitaSogCollaudo) PersistentObject.elementWithKey(NoteAttivitaSogCollaudo.class, key, lockType);
	}
	
	public String getLanguage() {
		return iLanguage;
	}

	public void setLanguage(String iLanguage) {
		this.iLanguage = iLanguage;
	}

	public String getIdAzienda() {
		return iIdAzienda;
	}
	public void setIdAzienda(String iIdAzienda) {
		this.iIdAzienda = iIdAzienda;
	}
	public String getOrdCliRiga() {
		return iOrdCliRiga;
	}
	public void setOrdCliRiga(String iOrdCliRiga) {
		this.iOrdCliRiga = iOrdCliRiga;
	}
	public String getNomeTabella() {
		return iNomeTabella;
	}
	public void setNomeTabella(String iNomeTabella) {
		this.iNomeTabella = iNomeTabella;
	}
	public String getNumeroOrdFmt() {
		return iNumeroOrdFmt;
	}
	public void setNumeroOrdFmt(String iNumeroOrdFmt) {
		this.iNumeroOrdFmt = iNumeroOrdFmt;
	}
	public String getDescription() {
		return iDescription;
	}
	public void setDescription(String iDescription) {
		this.iDescription = iDescription;
	}
	public String getNlsCommentText() {
		return iNlsCommentText;
	}
	public void setNlsCommentText(String iNlsCommentText) {
		this.iNlsCommentText = iNlsCommentText;
	}
	public String getIdCommentUse() {
		return iIdCommentUse;
	}
	public void setIdCommentUse(String iIdCommentUse) {
		this.iIdCommentUse = iIdCommentUse;
	}
	public boolean isReserved() {
		return iReserved;
	}
	public void setReserved(boolean iReserved) {
		this.iReserved = iReserved;
	}
	public char getTextType() {
		return iTextType;
	}
	public void setTextType(char iTextType) {
		this.iTextType = iTextType;
	}
	public Integer getCommentType1() {
		return iCommentType1;
	}
	public void setCommentType1(Integer iCommentType1) {
		this.iCommentType1 = iCommentType1;
	}
	public Integer getCommentType2() {
		return iCommentType2;
	}
	public void setCommentType2(Integer iCommentType2) {
		this.iCommentType2 = iCommentType2;
	}
	public String getIdAttivita() {
		return iIdAttivita;
	}
	public void setIdAttivita(String iIdAttivita) {
		this.iIdAttivita = iIdAttivita;
	}
	public Integer getIdSequenzaFase() {
		return idSequenzaFase;
	}
	public void setIdSequenzaFase(Integer idSequenzaFase) {
		this.idSequenzaFase = idSequenzaFase;
	}
	public Integer getSeqVisualizz() {
		return iSeqVisualizz;
	}
	public void setSeqVisualizz(Integer iSeqVisualizz) {
		this.iSeqVisualizz = iSeqVisualizz;
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
		//String[] keys = KeyHelper.unpackObjectKey(key);
		//		setNumDdtInt(keys[0]);
		//		setAnnoDoc(keys[1]);
		//		setNumRiga(Integer.valueOf(keys[2]));
		//		setTimestampAgg(Timestamp.valueOf(keys[3]));
	}

	public String getKey() {
		//Object[] keyParts = { getNumDdtInt(),getAnnoDoc(),getNumRiga(),getTimestampAgg() };
		//return KeyHelper.buildObjectKey(keyParts);
		return "";
	}

	public boolean isDeletable() {
		return checkDelete() == null;
	}

	public String toString() {
		return getClass().getName() + " [" + KeyHelper.formatKeyString(getKey()) + "]";
	}

	@Override
	protected TableManager getTableManager() throws SQLException {
		return NoteAttivitaSogCollaudoTM.getInstance();
	}

}
