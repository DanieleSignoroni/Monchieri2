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

public abstract class OrdEsecAtvSogCollaudoPO extends PersistentObject  implements BusinessObject, Authorizable, Deletable, ConflictableWithKey {

	private static OrdEsecAtvSogCollaudo cInstance;

	protected String idAzienda;          // char(3)
	protected String dataOrdine;         // varchar(10)
	protected String ordCliRiga;         // varchar(23)
	protected String idOriginale;        // char(31)
	protected String idProgressivo;      // char(10)
	protected String articolo;           // char(25)
	protected String commessa;           // char(8)
	protected String cliente;            // char(8)
	protected String descrizione;        // char(35)
	protected String descrRidotta;       // char(15)
	protected String attivita;           // char(15)
	protected String rCentroLavoro;      // char(15)
	protected Integer qtaCtr;            // int
	protected Float percCtr;             // numeric(...) or float, based on DB
	protected String nomeTabella;        // varchar(255)
	protected String idAttivita;         // char(15)
	protected Integer idSequenzaFase;    // int
	protected Integer seqVisualizz;      // int
	protected String misureObbl;         // Possibly char(1) or int, adapt as needed
	protected String valNomProposto;     // Possibly nvarchar(1)
	protected String faseObbl;           // nchar(4000)
	protected String cmtrWhitness;       // nchar(16)
	protected String IdNumeroOrp;

	@SuppressWarnings("rawtypes")
	public static Vector retrieveList(String where, String orderBy, boolean optimistic)
			throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (cInstance == null) {
			cInstance = (OrdEsecAtvSogCollaudo) Factory.createObject(OrdEsecAtvSogCollaudo.class);
		}
		return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
	}

	public static OrdEsecAtvSogCollaudo elementWithKey(String key, int lockType) throws SQLException {
		return (OrdEsecAtvSogCollaudo) PersistentObject.elementWithKey(OrdEsecAtvSogCollaudo.class, key, lockType);
	}

	public String getIdAzienda() {
		return idAzienda;
	}
	public void setIdAzienda(String idAzienda) {
		this.idAzienda = idAzienda;
		setDirty();
	}

	public String getDataOrdine() {
		return dataOrdine;
	}
	public void setDataOrdine(String dataOrdine) {
		this.dataOrdine = dataOrdine;
		setDirty();
	}

	public String getOrdCliRiga() {
		return ordCliRiga;
	}
	public void setOrdCliRiga(String ordCliRiga) {
		this.ordCliRiga = ordCliRiga;
		setDirty();
	}

	public String getIdOriginale() {
		return idOriginale;
	}
	public void setIdOriginale(String idOriginale) {
		this.idOriginale = idOriginale;
		setDirty();
	}

	public String getIdProgressivo() {
		return idProgressivo;
	}
	public void setIdProgressivo(String idProgressivo) {
		this.idProgressivo = idProgressivo;
		setDirty();
	}

	public String getArticolo() {
		return articolo;
	}
	public void setArticolo(String articolo) {
		this.articolo = articolo;
		setDirty();
	}

	public String getCommessa() {
		return commessa;
	}
	public void setCommessa(String commessa) {
		this.commessa = commessa;
		setDirty();
	}

	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
		setDirty();
	}

	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		setDirty();
	}

	public String getDescrRidotta() {
		return descrRidotta;
	}
	public void setDescrRidotta(String descrRidotta) {
		this.descrRidotta = descrRidotta;
		setDirty();
	}

	public String getAttivita() {
		return attivita;
	}
	public void setAttivita(String attivita) {
		this.attivita = attivita;
		setDirty();
	}

	public String getRCentroLavoro() {
		return rCentroLavoro;
	}
	public void setRCentroLavoro(String rCentroLavoro) {
		this.rCentroLavoro = rCentroLavoro;
		setDirty();
	}

	public Integer getQtaCtr() {
		return qtaCtr;
	}
	public void setQtaCtr(Integer qtaCtr) {
		this.qtaCtr = qtaCtr;
		setDirty();
	}

	public Float getPercCtr() {
		return percCtr;
	}
	public void setPercCtr(Float percCtr) {
		this.percCtr = percCtr;
		setDirty();
	}

	public String getNomeTabella() {
		return nomeTabella;
	}
	public void setNomeTabella(String nomeTabella) {
		this.nomeTabella = nomeTabella;
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

	public String getCmtrWhitness() {
		return cmtrWhitness;
	}
	public void setCmtrWhitness(String cmtrWhitness) {
		this.cmtrWhitness = cmtrWhitness;
		setDirty();
	}

	@Override
	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
	}
	
	public String getIdNumeroOrp() {
		return IdNumeroOrp;
	}

	public void setIdNumeroOrp(String idNumeroOrp) {
		IdNumeroOrp = idNumeroOrp;
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
		return OrdEsecAtvSogCollaudoTM.getInstance();
	}
}
