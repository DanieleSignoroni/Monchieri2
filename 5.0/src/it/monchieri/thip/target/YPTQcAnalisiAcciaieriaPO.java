package it.monchieri.thip.target;

import java.math.BigDecimal;
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
	protected BigDecimal al;
	protected BigDecimal alsol;
	protected BigDecimal as_;
	protected BigDecimal b_;
	protected BigDecimal bi;
	protected BigDecimal c_;
	protected BigDecimal ca;
	protected BigDecimal cb;
	protected BigDecimal ce;
	protected BigDecimal co;
	protected BigDecimal cr;
	protected BigDecimal crEq;
	protected BigDecimal cu;
	protected BigDecimal h_;
	protected Integer hPpmOPerc;
	protected BigDecimal jf;
	protected BigDecimal mn;
	protected BigDecimal mo;
	protected BigDecimal n_;
	protected Integer nPpmOPerc;
	protected BigDecimal nb;
	protected BigDecimal ni;
	protected BigDecimal o_;
	protected Integer oPpmOPerc;
	protected BigDecimal p_;
	protected BigDecimal pb;
	protected BigDecimal pcm;
	protected BigDecimal pre;
	protected BigDecimal s_;
	protected BigDecimal sb;
	protected BigDecimal si;
	protected BigDecimal sn;
	protected BigDecimal ta;
	protected BigDecimal ti;
	protected BigDecimal v_;
	protected BigDecimal w_;
	protected BigDecimal xf;
	protected BigDecimal zr;
	protected BigDecimal cN;
	protected BigDecimal fe;
	protected BigDecimal nbTa;
	protected BigDecimal y_;
	protected String acFormula1;
	protected BigDecimal acFormula1Valore;
	protected String acFormula2;
	protected BigDecimal acFormula2Valore;
	protected String acFormula3;
	protected BigDecimal acFormula3Valore;
	protected String acFormula4;
	protected BigDecimal acFormula4Valore;
	protected String acFormula5;
	protected BigDecimal acFormula5Valore;
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

	public BigDecimal getAl() {
		return al;
	}

	public void setAl(BigDecimal al) {
		this.al = al;
		setDirty();
	}

	public BigDecimal getAlsol() {
		return alsol;
	}

	public void setAlsol(BigDecimal alsol) {
		this.alsol = alsol;
		setDirty();
	}

	public BigDecimal getAs_() {
		return as_;
	}

	public void setAs_(BigDecimal as_) {
		this.as_ = as_;
		setDirty();
	}

	public BigDecimal getB_() {
		return b_;
	}

	public void setB_(BigDecimal b_) {
		this.b_ = b_;
		setDirty();
	}

	public BigDecimal getBi() {
		return bi;
	}

	public void setBi(BigDecimal bi) {
		this.bi = bi;
		setDirty();
	}

	public BigDecimal getC_() {
		return c_;
	}

	public void setC_(BigDecimal c_) {
		this.c_ = c_;
		setDirty();
	}

	public BigDecimal getCa() {
		return ca;
	}

	public void setCa(BigDecimal ca) {
		this.ca = ca;
		setDirty();
	}

	public BigDecimal getCb() {
		return cb;
	}

	public void setCb(BigDecimal cb) {
		this.cb = cb;
		setDirty();
	}

	public BigDecimal getCe() {
		return ce;
	}

	public void setCe(BigDecimal ce) {
		this.ce = ce;
		setDirty();
	}

	public BigDecimal getCo() {
		return co;
	}

	public void setCo(BigDecimal co) {
		this.co = co;
		setDirty();
	}

	public BigDecimal getCr() {
		return cr;
	}

	public void setCr(BigDecimal cr) {
		this.cr = cr;
		setDirty();
	}

	public BigDecimal getCrEq() {
		return crEq;
	}

	public void setCrEq(BigDecimal crEq) {
		this.crEq = crEq;
		setDirty();
	}

	public BigDecimal getCu() {
		return cu;
	}

	public void setCu(BigDecimal cu) {
		this.cu = cu;
		setDirty();
	}

	public BigDecimal getH_() {
		return h_;
	}

	public void setH_(BigDecimal h_) {
		this.h_ = h_;
		setDirty();
	}

	public Integer getHPpmOPerc() {
		return hPpmOPerc;
	}

	public void setHPpmOPerc(Integer hPpmOPerc) {
		this.hPpmOPerc = hPpmOPerc;
		setDirty();
	}

	public BigDecimal getJf() {
		return jf;
	}

	public void setJf(BigDecimal jf) {
		this.jf = jf;
		setDirty();
	}

	public BigDecimal getMn() {
		return mn;
	}

	public void setMn(BigDecimal mn) {
		this.mn = mn;
		setDirty();
	}

	public BigDecimal getMo() {
		return mo;
	}

	public void setMo(BigDecimal mo) {
		this.mo = mo;
		setDirty();
	}

	public BigDecimal getN_() {
		return n_;
	}

	public void setN_(BigDecimal n_) {
		this.n_ = n_;
		setDirty();
	}

	public Integer getNPpmOPerc() {
		return nPpmOPerc;
	}

	public void setNPpmOPerc(Integer nPpmOPerc) {
		this.nPpmOPerc = nPpmOPerc;
		setDirty();
	}

	public BigDecimal getNb() {
		return nb;
	}

	public void setNb(BigDecimal nb) {
		this.nb = nb;
		setDirty();
	}

	public BigDecimal getNi() {
		return ni;
	}

	public void setNi(BigDecimal ni) {
		this.ni = ni;
		setDirty();
	}

	public BigDecimal getO_() {
		return o_;
	}

	public void setO_(BigDecimal o_) {
		this.o_ = o_;
		setDirty();
	}

	public Integer getOPpmOPerc() {
		return oPpmOPerc;
	}

	public void setOPpmOPerc(Integer oPpmOPerc) {
		this.oPpmOPerc = oPpmOPerc;
		setDirty();
	}

	public BigDecimal getP_() {
		return p_;
	}

	public void setP_(BigDecimal p_) {
		this.p_ = p_;
		setDirty();
	}

	public BigDecimal getPb() {
		return pb;
	}

	public void setPb(BigDecimal pb) {
		this.pb = pb;
		setDirty();
	}

	public BigDecimal getPcm() {
		return pcm;
	}

	public void setPcm(BigDecimal pcm) {
		this.pcm = pcm;
		setDirty();
	}

	public BigDecimal getPre() {
		return pre;
	}

	public void setPre(BigDecimal pre) {
		this.pre = pre;
		setDirty();
	}

	public BigDecimal getS_() {
		return s_;
	}

	public void setS_(BigDecimal s_) {
		this.s_ = s_;
		setDirty();
	}

	public BigDecimal getSb() {
		return sb;
	}

	public void setSb(BigDecimal sb) {
		this.sb = sb;
		setDirty();
	}

	public BigDecimal getSi() {
		return si;
	}

	public void setSi(BigDecimal si) {
		this.si = si;
		setDirty();
	}

	public BigDecimal getSn() {
		return sn;
	}

	public void setSn(BigDecimal sn) {
		this.sn = sn;
		setDirty();
	}

	public BigDecimal getTa() {
		return ta;
	}

	public void setTa(BigDecimal ta) {
		this.ta = ta;
		setDirty();
	}

	public BigDecimal getTi() {
		return ti;
	}

	public void setTi(BigDecimal ti) {
		this.ti = ti;
		setDirty();
	}

	public BigDecimal getV_() {
		return v_;
	}

	public void setV_(BigDecimal v_) {
		this.v_ = v_;
		setDirty();
	}

	public BigDecimal getW_() {
		return w_;
	}

	public void setW_(BigDecimal w_) {
		this.w_ = w_;
		setDirty();
	}

	public BigDecimal getXf() {
		return xf;
	}

	public void setXf(BigDecimal xf) {
		this.xf = xf;
		setDirty();
	}

	public BigDecimal getZr() {
		return zr;
	}

	public void setZr(BigDecimal zr) {
		this.zr = zr;
		setDirty();
	}

	public BigDecimal getC_N() {
		return cN;
	}

	public void setC_N(BigDecimal cN) {
		this.cN = cN;
		setDirty();
	}

	public BigDecimal getFe() {
		return fe;
	}

	public void setFe(BigDecimal fe) {
		this.fe = fe;
		setDirty();
	}

	public BigDecimal getNbTa() {
		return nbTa;
	}

	public void setNbTa(BigDecimal nbTa) {
		this.nbTa = nbTa;
		setDirty();
	}

	public BigDecimal getY_() {
		return y_;
	}

	public void setY_(BigDecimal y_) {
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

	public BigDecimal getAcFormula1Valore() {
		return acFormula1Valore;
	}

	public void setAcFormula1Valore(BigDecimal acFormula1Valore) {
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

	public BigDecimal getAcFormula2Valore() {
		return acFormula2Valore;
	}

	public void setAcFormula2Valore(BigDecimal acFormula2Valore) {
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

	public BigDecimal getAcFormula3Valore() {
		return acFormula3Valore;
	}

	public void setAcFormula3Valore(BigDecimal acFormula3Valore) {
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

	public BigDecimal getAcFormula4Valore() {
		return acFormula4Valore;
	}

	public void setAcFormula4Valore(BigDecimal acFormula4Valore) {
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

	public BigDecimal getAcFormula5Valore() {
		return acFormula5Valore;
	}

	public void setAcFormula5Valore(BigDecimal acFormula5Valore) {
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
