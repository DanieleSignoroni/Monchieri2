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

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 23/12/2024
 * <br><br>
 * <b>71XXX    DSSOF3    23/12/2024</b>
 * <p></p>
 */

public abstract class QcTabellaAttivitaPO extends PersistentObject implements BusinessObject, Authorizable, Deletable, ConflictableWithKey {
	
	private static QcTabellaAttivita cInstance;

	protected String nomeTabella;             
	protected String idAzienda;               
	protected String idAttivita;              
	protected Integer idSequenzaFase;         
	protected Integer seqVisualizz;           
	protected String misureObbl;              
	protected String valNomProposto;          
	protected String faseObbl;               
	protected String forzaSogCollaudo;        
	protected String flagGeneratoCertificato; 
	protected String cmtrWhitness;            
	protected Integer idSequenzaFaseTt;
	
	@SuppressWarnings("rawtypes")
	public static Vector retrieveList(String where, String orderBy, boolean optimistic)
			throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (cInstance == null) {
			cInstance = (QcTabellaAttivita) Factory.createObject(QcTabellaAttivita.class);
		}
		return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
	}

	public static QcTabellaAttivita elementWithKey(String key, int lockType) throws SQLException {
		return (QcTabellaAttivita) PersistentObject.elementWithKey(QcTabellaAttivita.class, key, lockType);
	}

	public String getNomeTabella() {
		return nomeTabella;
	}
	public void setNomeTabella(String nomeTabella) {
		this.nomeTabella = nomeTabella;
		setDirty();
	}

	public String getIdAzienda() {
		return idAzienda;
	}
	public void setIdAzienda(String idAzienda) {
		this.idAzienda = idAzienda;
		setDirty();
	}

	public String getIdAttivita() {
		return idAttivita;
	}
	public void setIdAttivita(String idAttivita) {
		this.idAttivita = idAttivita;
		setDirty();
	}

	public Integer getIdSequenzaFase() {
		return idSequenzaFase;
	}
	public void setIdSequenzaFase(Integer idSequenzaFase) {
		this.idSequenzaFase = idSequenzaFase;
		setDirty();
	}

	public Integer getSeqVisualizz() {
		return seqVisualizz;
	}
	public void setSeqVisualizz(Integer seqVisualizz) {
		this.seqVisualizz = seqVisualizz;
		setDirty();
	}

	public String getMisureObbl() {
		return misureObbl;
	}
	public void setMisureObbl(String misureObbl) {
		this.misureObbl = misureObbl;
		setDirty();
	}

	public String getValNomProposto() {
		return valNomProposto;
	}
	public void setValNomProposto(String valNomProposto) {
		this.valNomProposto = valNomProposto;
		setDirty();
	}

	public String getFaseObbl() {
		return faseObbl;
	}
	public void setFaseObbl(String faseObbl) {
		this.faseObbl = faseObbl;
		setDirty();
	}

	public String getForzaSogCollaudo() {
		return forzaSogCollaudo;
	}
	public void setForzaSogCollaudo(String forzaSogCollaudo) {
		this.forzaSogCollaudo = forzaSogCollaudo;
		setDirty();
	}

	public String getFlagGeneratoCertificato() {
		return flagGeneratoCertificato;
	}
	public void setFlagGeneratoCertificato(String flagGeneratoCertificato) {
		this.flagGeneratoCertificato = flagGeneratoCertificato;
		setDirty();
	}

	public String getCmtrWhitness() {
		return cmtrWhitness;
	}
	public void setCmtrWhitness(String cmtrWhitness) {
		this.cmtrWhitness = cmtrWhitness;
		setDirty();
	}

	public Integer getIdSequenzaFaseTt() {
		return idSequenzaFaseTt;
	}
	public void setIdSequenzaFaseTt(Integer idSequenzaFaseTt) {
		this.idSequenzaFaseTt = idSequenzaFaseTt;
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
		setNomeTabella(keys[0]);
		setIdAzienda(keys[1]);
		setIdAttivita(keys[2]);
	}

	public String getKey() {
		Object[] keyParts = { getNomeTabella(),getIdAzienda(),getIdAttivita()};
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
		return QcTabellaAttivitaTM.getInstance();
	}
}
