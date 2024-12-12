package it.monchieri.thip.target;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
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
 * @author Daniele Signoroni 11/12/2024
 * <br><br>
 * <b>71XXX    DSSOF3    11/12/2024</b>
 * <p></p>
 */

public abstract class YPTQcAnalisiAcciaieriaPO extends PersistentObject implements BusinessObject, Authorizable, Deletable, ConflictableWithKey {

	private static YPTQcAnalisiAcciaieria cInstance;
	
	protected String entrata;
	protected Date data;
	protected String colata;
	protected String acciaieria;
	protected String id;
	protected String acciaio;
	protected String alliasAcciaio;
	protected String specifica1;
	protected String elabAcciaio;
	protected String tipoTrat;
	protected String note;
	protected Timestamp timestampAgg;
	protected Float al;
	protected Float alsol;
	protected Float as_;
	protected Float b_;
	protected Float bi;
	protected Float c_;
	protected Float ca;
	protected Float cb;
	protected Float ce;
	protected Float co;
	protected Float cr;
	protected Float crEq;
	protected Float cu;
	protected Float h_;
	protected Integer hPpmOPerc;
	protected Float jf;
	protected Float mn;
	protected Float mo;
	protected Float n_;
	protected Integer nPpmOPerc;
	protected Float nb;
	protected Float ni;
	protected Float o_;
	protected Integer oPpmOPerc;
	protected Float p_;
	protected Float pb;
	protected Float pcm;
	protected Float pre;
	protected Float s_;
	protected Float sb;
	protected Float si;
	protected Float sn;
	protected Float ta;
	protected Float ti;
	protected Float v_;
	protected Float w_;
	protected Float xf;
	protected Float zr;
	protected Float cN;
	protected Float fe;
	protected Float nbTa;
	protected Float y_;
	protected String acFormula1;
	protected Float acFormula1Valore;
	protected String acFormula2;
	protected Float acFormula2Valore;
	protected String acFormula3;
	protected Float acFormula3Valore;
	protected String acFormula4;
	protected Float acFormula4Valore;
	protected String acFormula5;
	protected Float acFormula5Valore;
	protected String elaborato;
	
	@SuppressWarnings("rawtypes")
	public static Vector retrieveList(String where, String orderBy, boolean optimistic)
			throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (cInstance == null) {
			cInstance = (YPTQcAnalisiAcciaieria) Factory.createObject(YPTQcAnalisiAcciaieria.class);
		}
		return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
	}

	public static YPTQcAnalisiAcciaieria elementWithKey(String key, int lockType) throws SQLException {
		return (YPTQcAnalisiAcciaieria) PersistentObject.elementWithKey(YPTQcAnalisiAcciaieria.class, key, lockType);
	}

	public String getEntrata() {
		return entrata;
	}

	public void setEntrata(String entrata) {
		this.entrata = entrata;
		setDirty();
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
		setDirty();
	}

	public String getColata() {
		return colata;
	}

	public void setColata(String colata) {
		this.colata = colata;
		setDirty();
	}

	public String getAcciaieria() {
		return acciaieria;
	}

	public void setAcciaieria(String acciaieria) {
		this.acciaieria = acciaieria;
		setDirty();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		setDirty();
	}

	public String getAcciaio() {
		return acciaio;
	}

	public void setAcciaio(String acciaio) {
		this.acciaio = acciaio;
		setDirty();
	}

	public String getAlliasAcciaio() {
		return alliasAcciaio;
	}

	public void setAlliasAcciaio(String alliasAcciaio) {
		this.alliasAcciaio = alliasAcciaio;
		setDirty();
	}

	public String getSpecifica1() {
		return specifica1;
	}

	public void setSpecifica1(String specifica1) {
		this.specifica1 = specifica1;
		setDirty();
	}

	public String getElabAcciaio() {
		return elabAcciaio;
	}

	public void setElabAcciaio(String elabAcciaio) {
		this.elabAcciaio = elabAcciaio;
		setDirty();
	}

	public String getTipoTrat() {
		return tipoTrat;
	}

	public void setTipoTrat(String tipoTrat) {
		this.tipoTrat = tipoTrat;
		setDirty();
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
		setDirty();
	}

	public Timestamp getTimestampAgg() {
		return timestampAgg;
	}

	public void setTimestampAgg(Timestamp timestampAgg) {
		this.timestampAgg = timestampAgg;
		setDirty();
	}

	public Float getAl() {
		return al;
	}

	public void setAl(Float al) {
		this.al = al;
		setDirty();
	}

	public Float getAlsol() {
		return alsol;
	}

	public void setAlsol(Float alsol) {
		this.alsol = alsol;
		setDirty();
	}

	public Float getAs_() {
		return as_;
	}

	public void setAs_(Float as_) {
		this.as_ = as_;
		setDirty();
	}

	public Float getB_() {
		return b_;
	}

	public void setB_(Float b_) {
		this.b_ = b_;
		setDirty();
	}

	public Float getBi() {
		return bi;
	}

	public void setBi(Float bi) {
		this.bi = bi;
		setDirty();
	}

	public Float getC_() {
		return c_;
	}

	public void setC_(Float c_) {
		this.c_ = c_;
		setDirty();
	}

	public Float getCa() {
		return ca;
	}

	public void setCa(Float ca) {
		this.ca = ca;
		setDirty();
	}

	public Float getCb() {
		return cb;
	}

	public void setCb(Float cb) {
		this.cb = cb;
		setDirty();
	}

	public Float getCe() {
		return ce;
	}

	public void setCe(Float ce) {
		this.ce = ce;
		setDirty();
	}

	public Float getCo() {
		return co;
	}

	public void setCo(Float co) {
		this.co = co;
		setDirty();
	}

	public Float getCr() {
		return cr;
	}

	public void setCr(Float cr) {
		this.cr = cr;
		setDirty();
	}

	public Float getCrEq() {
		return crEq;
	}

	public void setCrEq(Float crEq) {
		this.crEq = crEq;
		setDirty();
	}

	public Float getCu() {
		return cu;
	}

	public void setCu(Float cu) {
		this.cu = cu;
		setDirty();
	}

	public Float getH_() {
		return h_;
	}

	public void setH_(Float h_) {
		this.h_ = h_;
		setDirty();
	}

	public Integer gethPpmOPerc() {
		return hPpmOPerc;
	}

	public void sethPpmOPerc(Integer hPpmOPerc) {
		this.hPpmOPerc = hPpmOPerc;
		setDirty();
	}

	public Float getJf() {
		return jf;
	}

	public void setJf(Float jf) {
		this.jf = jf;
		setDirty();
	}

	public Float getMn() {
		return mn;
	}

	public void setMn(Float mn) {
		this.mn = mn;
		setDirty();
	}

	public Float getMo() {
		return mo;
	}

	public void setMo(Float mo) {
		this.mo = mo;
		setDirty();
	}

	public Float getN_() {
		return n_;
	}

	public void setN_(Float n_) {
		this.n_ = n_;
		setDirty();
	}

	public Integer getnPpmOPerc() {
		return nPpmOPerc;
	}

	public void setnPpmOPerc(Integer nPpmOPerc) {
		this.nPpmOPerc = nPpmOPerc;
		setDirty();
	}

	public Float getNb() {
		return nb;
	}

	public void setNb(Float nb) {
		this.nb = nb;
		setDirty();
	}

	public Float getNi() {
		return ni;
	}

	public void setNi(Float ni) {
		this.ni = ni;
		setDirty();
	}

	public Float getO_() {
		return o_;
	}

	public void setO_(Float o_) {
		this.o_ = o_;
		setDirty();
	}

	public Integer getoPpmOPerc() {
		return oPpmOPerc;
	}

	public void setoPpmOPerc(Integer oPpmOPerc) {
		this.oPpmOPerc = oPpmOPerc;
		setDirty();
	}

	public Float getP_() {
		return p_;
	}

	public void setP_(Float p_) {
		this.p_ = p_;
		setDirty();
	}

	public Float getPb() {
		return pb;
	}

	public void setPb(Float pb) {
		this.pb = pb;
		setDirty();
	}

	public Float getPcm() {
		return pcm;
	}

	public void setPcm(Float pcm) {
		this.pcm = pcm;
		setDirty();
	}

	public Float getPre() {
		return pre;
	}

	public void setPre(Float pre) {
		this.pre = pre;
		setDirty();
	}

	public Float getS_() {
		return s_;
	}

	public void setS_(Float s_) {
		this.s_ = s_;
		setDirty();
	}

	public Float getSb() {
		return sb;
	}

	public void setSb(Float sb) {
		this.sb = sb;
		setDirty();
	}

	public Float getSi() {
		return si;
	}

	public void setSi(Float si) {
		this.si = si;
		setDirty();
	}

	public Float getSn() {
		return sn;
	}

	public void setSn(Float sn) {
		this.sn = sn;
		setDirty();
	}

	public Float getTa() {
		return ta;
	}

	public void setTa(Float ta) {
		this.ta = ta;
		setDirty();
	}

	public Float getTi() {
		return ti;
	}

	public void setTi(Float ti) {
		this.ti = ti;
		setDirty();
	}

	public Float getV_() {
		return v_;
	}

	public void setV_(Float v_) {
		this.v_ = v_;
		setDirty();
	}

	public Float getW_() {
		return w_;
	}

	public void setW_(Float w_) {
		this.w_ = w_;
		setDirty();
	}

	public Float getXf() {
		return xf;
	}

	public void setXf(Float xf) {
		this.xf = xf;
		setDirty();
	}

	public Float getZr() {
		return zr;
	}

	public void setZr(Float zr) {
		this.zr = zr;
		setDirty();
	}

	public Float getcN() {
		return cN;
	}

	public void setcN(Float cN) {
		this.cN = cN;
		setDirty();
	}

	public Float getFe() {
		return fe;
	}

	public void setFe(Float fe) {
		this.fe = fe;
		setDirty();
	}

	public Float getNbTa() {
		return nbTa;
	}

	public void setNbTa(Float nbTa) {
		this.nbTa = nbTa;
		setDirty();
	}

	public Float getY_() {
		return y_;
	}

	public void setY_(Float y_) {
		this.y_ = y_;
		setDirty();
	}

	public String getAcFormula1() {
		return acFormula1;
	}

	public void setAcFormula1(String acFormula1) {
		this.acFormula1 = acFormula1;
		setDirty();
	}

	public Float getAcFormula1Valore() {
		return acFormula1Valore;
	}

	public void setAcFormula1Valore(Float acFormula1Valore) {
		this.acFormula1Valore = acFormula1Valore;
		setDirty();
	}

	public String getAcFormula2() {
		return acFormula2;
	}

	public void setAcFormula2(String acFormula2) {
		this.acFormula2 = acFormula2;
		setDirty();
	}

	public Float getAcFormula2Valore() {
		return acFormula2Valore;
	}

	public void setAcFormula2Valore(Float acFormula2Valore) {
		this.acFormula2Valore = acFormula2Valore;
		setDirty();
	}

	public String getAcFormula3() {
		return acFormula3;
	}

	public void setAcFormula3(String acFormula3) {
		this.acFormula3 = acFormula3;
		setDirty();
	}

	public Float getAcFormula3Valore() {
		return acFormula3Valore;
	}

	public void setAcFormula3Valore(Float acFormula3Valore) {
		this.acFormula3Valore = acFormula3Valore;
		setDirty();
	}

	public String getAcFormula4() {
		return acFormula4;
	}

	public void setAcFormula4(String acFormula4) {
		this.acFormula4 = acFormula4;
		setDirty();
	}

	public Float getAcFormula4Valore() {
		return acFormula4Valore;
	}

	public void setAcFormula4Valore(Float acFormula4Valore) {
		this.acFormula4Valore = acFormula4Valore;
		setDirty();
	}

	public String getAcFormula5() {
		return acFormula5;
	}

	public void setAcFormula5(String acFormula5) {
		this.acFormula5 = acFormula5;
		setDirty();
	}

	public Float getAcFormula5Valore() {
		return acFormula5Valore;
	}

	public void setAcFormula5Valore(Float acFormula5Valore) {
		this.acFormula5Valore = acFormula5Valore;
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
		setId(keys[0]);
		setTimestampAgg(Timestamp.valueOf(keys[1]));
	}

	public String getKey() {
		Object[] keyParts = { getId(),getTimestampAgg() };
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
		return YPTQcAnalisiAcciaieriaTM.getInstance();
	}
}
