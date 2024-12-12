package it.monchieri.thip.target;

import java.math.BigDecimal;
import java.sql.Date; // Using java.sql.Date as requested
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.thera.thermfw.base.Trace;
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

public abstract class YPTExpOrdEsecPO extends PersistentObject implements BusinessObject, Authorizable, Deletable, ConflictableWithKey {

	private static YPTExpOrdEsec cInstance;

	protected String docId;
	protected String annoDoc;
	protected BigDecimal numDoc;
	protected String serieDoc;
	protected Date dataDoc;
	protected String codCf;
	protected String noteInt;
	protected String noteStampa;
	protected String codCausDoc;
	protected String noteConsegna;
	protected String codDep;
	protected String codDep2;
	protected String codArt;
	protected String codVarMateriale;
	protected String codLot;
	protected String desProd;
	protected Float quantDaProd;
	protected String rifRigaOrdCli4b;
	protected String utenteIns;
	protected Date dataIns;
	protected String utenteMod;
	protected Date dataMod;
	protected BigDecimal pesgrez;
	protected BigDecimal pesmult;
	protected String chiodaia;
	protected String mandrino;
	protected String attrezz1;
	protected String attrezz2;
	protected String provnum;
	protected Date dataSped;
	protected Date dataTt;
	protected String identcli;
	protected BigDecimal nucleare;
	protected String codcli;
	protected String tipoOperazione;
	protected String eseguito;
	protected Integer numRiga;
	protected String docRigaId;
	protected String codArtComp;
	protected Float quantScaricata;
	protected Float quantMaterozza;
	protected String entrata;
	protected Date dataMovimento;

	SimpleDateFormat formatSQL = new SimpleDateFormat("yyyy-MM-dd");

	@SuppressWarnings("rawtypes")
	public static Vector retrieveList(String where, String orderBy, boolean optimistic)
			throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (cInstance == null) {
			cInstance = (YPTExpOrdEsec) Factory.createObject(YPTExpOrdEsec.class);
		}
		return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
	}

	public static YPTExpOrdEsec elementWithKey(String key, int lockType) throws SQLException {
		return (YPTExpOrdEsec) PersistentObject.elementWithKey(YPTExpOrdEsec.class, key, lockType);
	}

	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
		setDirty();
	}

	public String getAnnoDoc() {
		return annoDoc;
	}
	public void setAnnoDoc(String annoDoc) {
		this.annoDoc = annoDoc;
		setDirty();
	}

	public BigDecimal getNumDoc() {
		return numDoc;
	}
	public void setNumDoc(BigDecimal numDoc) {
		this.numDoc = numDoc;
		setDirty();
	}

	public String getSerieDoc() {
		return serieDoc;
	}
	public void setSerieDoc(String serieDoc) {
		this.serieDoc = serieDoc;
		setDirty();
	}

	public Date getDataDoc() {
		return dataDoc;
	}
	public void setDataDoc(Date dataDoc) {
		this.dataDoc = dataDoc;
		setDirty();
	}

	public String getCodCf() {
		return codCf;
	}
	public void setCodCf(String codCf) {
		this.codCf = codCf;
		setDirty();
	}

	public String getNoteInt() {
		return noteInt;
	}
	public void setNoteInt(String noteInt) {
		this.noteInt = noteInt;
		setDirty();
	}

	public String getNoteStampa() {
		return noteStampa;
	}
	public void setNoteStampa(String noteStampa) {
		this.noteStampa = noteStampa;
		setDirty();
	}

	public String getCodCausDoc() {
		return codCausDoc;
	}
	public void setCodCausDoc(String codCausDoc) {
		this.codCausDoc = codCausDoc;
		setDirty();
	}

	public String getNoteConsegna() {
		return noteConsegna;
	}
	public void setNoteConsegna(String noteConsegna) {
		this.noteConsegna = noteConsegna;
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

	public String getCodVarMateriale() {
		return codVarMateriale;
	}
	public void setCodVarMateriale(String codVarMateriale) {
		this.codVarMateriale = codVarMateriale;
		setDirty();
	}

	public String getCodLot() {
		return codLot;
	}
	public void setCodLot(String codLot) {
		this.codLot = codLot;
		setDirty();
	}

	public String getDesProd() {
		return desProd;
	}
	public void setDesProd(String desProd) {
		this.desProd = desProd;
		setDirty();
	}

	public Float getQuantDaProd() {
		return quantDaProd;
	}
	public void setQuantDaProd(Float quantDaProd) {
		this.quantDaProd = quantDaProd;
		setDirty();
	}

	public String getRifRigaOrdCli4b() {
		return rifRigaOrdCli4b;
	}
	public void setRifRigaOrdCli4b(String rifRigaOrdCli4b) {
		this.rifRigaOrdCli4b = rifRigaOrdCli4b;
		setDirty();
	}

	public String getUtenteIns() {
		return utenteIns;
	}
	public void setUtenteIns(String utenteIns) {
		this.utenteIns = utenteIns;
		setDirty();
	}

	public Date getDataIns() {
		return dataIns;
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
		setDirty();
	}

	public String getUtenteMod() {
		return utenteMod;
	}
	public void setUtenteMod(String utenteMod) {
		this.utenteMod = utenteMod;
		setDirty();
	}

	public Date getDataMod() {
		return dataMod;
	}
	public void setDataMod(Date dataMod) {
		this.dataMod = dataMod;
		setDirty();
	}

	public BigDecimal getPesgrez() {
		return pesgrez;
	}
	public void setPesgrez(BigDecimal pesgrez) {
		this.pesgrez = pesgrez;
		setDirty();
	}

	public BigDecimal getPesmult() {
		return pesmult;
	}
	public void setPesmult(BigDecimal pesmult) {
		this.pesmult = pesmult;
		setDirty();
	}

	public String getChiodaia() {
		return chiodaia;
	}
	public void setChiodaia(String chiodaia) {
		this.chiodaia = chiodaia;
		setDirty();
	}

	public String getMandrino() {
		return mandrino;
	}
	public void setMandrino(String mandrino) {
		this.mandrino = mandrino;
		setDirty();
	}

	public String getAttrezz1() {
		return attrezz1;
	}
	public void setAttrezz1(String attrezz1) {
		this.attrezz1 = attrezz1;
		setDirty();
	}

	public String getAttrezz2() {
		return attrezz2;
	}
	public void setAttrezz2(String attrezz2) {
		this.attrezz2 = attrezz2;
		setDirty();
	}

	public String getProvnum() {
		return provnum;
	}
	public void setProvnum(String provnum) {
		this.provnum = provnum;
		setDirty();
	}

	public Date getDataSped() {
		return dataSped;
	}
	public void setDataSped(Date dataSped) {
		this.dataSped = dataSped;
		setDirty();
	}

	public Date getDataTt() {
		return dataTt;
	}
	public void setDataTt(Date dataTt) {
		this.dataTt = dataTt;
		setDirty();
	}

	public String getIdentcli() {
		return identcli;
	}
	public void setIdentcli(String identcli) {
		this.identcli = identcli;
		setDirty();
	}

	public BigDecimal getNucleare() {
		return nucleare;
	}
	public void setNucleare(BigDecimal nucleare) {
		this.nucleare = nucleare;
		setDirty();
	}

	public String getCodcli() {
		return codcli;
	}
	public void setCodcli(String codcli) {
		this.codcli = codcli;
		setDirty();
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
		setDirty();
	}

	public String getEseguito() {
		return eseguito;
	}
	public void setEseguito(String eseguito) {
		this.eseguito = eseguito;
		setDirty();
	}

	public Integer getNumRiga() {
		return numRiga;
	}
	public void setNumRiga(Integer numRiga) {
		this.numRiga = numRiga;
		setDirty();
	}

	public String getDocRigaId() {
		return docRigaId;
	}
	public void setDocRigaId(String docRigaId) {
		this.docRigaId = docRigaId;
		setDirty();
	}

	public String getCodArtComp() {
		return codArtComp;
	}
	public void setCodArtComp(String codArtComp) {
		this.codArtComp = codArtComp;
		setDirty();
	}

	public Float getQuantScaricata() {
		return quantScaricata;
	}
	public void setQuantScaricata(Float quantScaricata) {
		this.quantScaricata = quantScaricata;
		setDirty();
	}

	public Float getQuantMaterozza() {
		return quantMaterozza;
	}
	public void setQuantMaterozza(Float quantMaterozza) {
		this.quantMaterozza = quantMaterozza;
		setDirty();
	}

	public String getEntrata() {
		return entrata;
	}
	public void setEntrata(String entrata) {
		this.entrata = entrata;
		setDirty();
	}

	public Date getDataMovimento() {
		return dataMovimento;
	}
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
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
		setDocId(keys[0]);
		java.util.Date utilDate;
		try {
			utilDate = formatSQL.parse(keys[1]);
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			setDataDoc(sqlDate);
		} catch (ParseException e) {
			e.printStackTrace(Trace.excStream);
		}

	}

	public String getKey() {
		Object[] keyParts = { getDocId(),getDataDoc() };
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
		return YPTExpOrdEsecTM.getInstance();
	}
}
