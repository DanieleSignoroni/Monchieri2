package it.monchieri.thip.target;

import java.math.BigDecimal;
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

public abstract class YPTQcAnalisiRmPO extends PersistentObject implements BusinessObject, Authorizable, Deletable, ConflictableWithKey {

	private static YPTQcAnalisiRm cInstance;
	
	protected String alliasAcciaio;
	protected String acciaio;
	protected Float tpFusAcc;
	protected String docRevAppr;
	protected String docRevPrep;
	protected String id;
	protected String note;
	protected String note2;
	protected String note3;
	protected Integer flagPrSpe;
	protected String docRev;
	protected Integer rmNum;
	protected String stdAcc;
	protected String stdAcc2;
	protected String stdAcc3;
	protected String stdAcc4;
	protected String stdAcc5;
	protected String stdAcc6;
	protected String stdAccDes;
	protected String stdAccDes2;
	protected String stdAccDes3;
	protected String stdAccDes4;
	protected String stdAccDes5;
	protected String stdAccDes6;
	protected String tasInclA;
	protected String tasInclB;
	protected String tasInclC;
	protected String tasInclD;
	protected String tasInclAG;
	protected String tasInclBG;
	protected String tasInclCG;
	protected String tasInclDG;
	protected String tasInclNote1;
	protected String tasInclNote2;
	protected String tasInclNote3;
	protected Float docRich;
	protected Integer tpAffAcc;
	protected Integer tpAffAcc2;
	protected Integer tpAffAcc3;
	protected Integer tpAffAcc4;
	protected Integer tpAffAcc5;
	protected Integer tpAffAcc6;
	protected Integer tpAffGr1;
	protected String rmNumRev;
	protected String rmOrN;
	protected String desValoreProdotto;
	protected String docDescRev;
	protected java.sql.Date docDataRev;
	protected String tasInclAct;
	protected String tasInclActG;
	protected String tpAffGr;
	protected Integer flagRmc;
	protected java.sql.Timestamp timestampAgg;
	protected Integer alFlagInf;
	protected Integer alsolFlagInf;
	protected BigDecimal alMin;
	protected BigDecimal alMax;
	protected Integer alsolInf;
	protected BigDecimal alsolMin;
	protected BigDecimal alsolMax;
	protected Integer asFlagInf;
	protected BigDecimal asMin;
	protected BigDecimal asMax;
	protected Integer bFlagInf;
	protected BigDecimal bMin;
	protected BigDecimal bMax;
	protected Integer biFlagInf;
	protected BigDecimal biMin;
	protected BigDecimal biMax;
	protected Integer cFlagInf;
	protected BigDecimal cMin;
	protected BigDecimal cMax;
	protected Integer cNFlagInf;
	protected BigDecimal cNMin;
	protected BigDecimal cNMax;
	protected Integer caFlagInf;
	protected BigDecimal caMin;
	protected BigDecimal caMax;
	protected Integer cbFlagInf;
	protected BigDecimal cbMin;
	protected BigDecimal cbMax;
	protected Integer ceFlagInf;
	protected BigDecimal ceMin;
	protected BigDecimal ceMax;
	protected Integer coFlagInf;
	protected BigDecimal coMin;
	protected BigDecimal coMax;
	protected Integer crFlagInf;
	protected BigDecimal crMin;
	protected BigDecimal crMax;
	protected Integer crEqFlagInf;
	protected BigDecimal crEqMin;
	protected BigDecimal crEqMax;
	protected Integer cuFlagInf;
	protected BigDecimal cuMin;
	protected BigDecimal cuMax;
	protected Integer feFlagInf;
	protected BigDecimal feMin;
	protected BigDecimal feMax;
	protected Integer hFlagInf;
	protected BigDecimal hMin;
	protected BigDecimal hMax;
	protected Integer hPpmOPerc;
	protected Integer jfFlagInf;
	protected BigDecimal jfMin;
	protected BigDecimal jfMax;
	protected Integer mnFlagInf;
	protected BigDecimal mnMin;
	protected BigDecimal mnMax;
	protected Integer moFlagInf;
	protected BigDecimal moMin;
	protected BigDecimal moMax;
	protected Integer nFlagInf;
	protected BigDecimal nMin;
	protected BigDecimal nMax;
	protected Integer nPpmOPerc;
	protected Integer nbFlagInf;
	protected BigDecimal nbMin;
	protected BigDecimal nbMax;
	protected Integer nbTaFlagInf;
	protected BigDecimal nbTaMin;
	protected BigDecimal nbTaMax;
	protected Integer niFlagInf;
	protected BigDecimal niMin;
	protected BigDecimal niMax;
	protected Integer oFlagInf;
	protected BigDecimal oMin;
	protected BigDecimal oMax;
	protected Integer oPpmOPerc;
	protected Integer pFlagInf;
	protected BigDecimal pMin;
	protected BigDecimal pMax;
	protected Integer pbFlagInf;
	protected BigDecimal pbMin;
	protected BigDecimal pbMax;
	protected Integer pcmFlagInf;
	protected BigDecimal pcmMin;
	protected BigDecimal pcmMax;
	protected Integer preFlagInf;
	protected BigDecimal preMin;
	protected BigDecimal preMax;
	protected Integer sFlagInf;
	protected BigDecimal sMin;
	protected BigDecimal sMax;
	protected Integer sbFlagInf;
	protected BigDecimal sbMin;
	protected BigDecimal sbMax;
	protected Integer siFlagInf;
	protected BigDecimal siMin;
	protected BigDecimal siMax;
	protected Integer snFlagInf;
	protected BigDecimal snMin;
	protected BigDecimal snMax;
	protected Integer taFlagInf;
	protected BigDecimal taMin;
	protected BigDecimal taMax;
	protected Integer tiFlagInf;
	protected BigDecimal tiMin;
	protected BigDecimal tiMax;
	protected Integer vFlagInf;
	protected BigDecimal vMin;
	protected BigDecimal vMax;
	protected Integer wFlagInf;
	protected BigDecimal wMin;
	protected BigDecimal wMax;
	protected Integer xfFlagInf;
	protected BigDecimal xfMin;
	protected BigDecimal xfMax;
	protected Integer yFlagInf;
	protected BigDecimal yMin;
	protected BigDecimal yMax;
	protected Integer zrFlagInf;
	protected BigDecimal zrMin;
	protected BigDecimal zrMax;
	protected String acFormula1;
	protected BigDecimal acFormula1Max;
	protected BigDecimal acFormula1Min;
	protected String acFormula2;
	protected BigDecimal acFormula2Max;
	protected BigDecimal acFormula2Min;
	protected String acFormula3;
	protected BigDecimal acFormula3Max;
	protected BigDecimal acFormula3Min;
	protected String acFormula4;
	protected BigDecimal acFormula4Max;
	protected BigDecimal acFormula4Min;
	protected String acFormula5;
	protected BigDecimal acFormula5Max;
	protected BigDecimal acFormula5Min;
	protected String elaborato;
	
	@SuppressWarnings("rawtypes")
	public static Vector retrieveList(String where, String orderBy, boolean optimistic)
			throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (cInstance == null) {
			cInstance = (YPTQcAnalisiRm) Factory.createObject(YPTQcAnalisiRm.class);
		}
		return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
	}

	public static YPTQcAnalisiRm elementWithKey(String key, int lockType) throws SQLException {
		return (YPTQcAnalisiRm) PersistentObject.elementWithKey(YPTQcAnalisiRm.class, key, lockType);
	}
	
	public String getAlliasAcciaio() {
	    return alliasAcciaio;
	}
	public void setAlliasAcciaio(String alliasAcciaio) {
	    this.alliasAcciaio = alliasAcciaio;
	    setDirty();
	}

	public String getAcciaio() {
	    return acciaio;
	}
	public void setAcciaio(String acciaio) {
	    this.acciaio = acciaio;
	    setDirty();
	}

	public Float getTpFusAcc() {
	    return tpFusAcc;
	}
	public void setTpFusAcc(Float tpFusAcc) {
	    this.tpFusAcc = tpFusAcc;
	    setDirty();
	}

	public String getDocRevAppr() {
	    return docRevAppr;
	}
	public void setDocRevAppr(String docRevAppr) {
	    this.docRevAppr = docRevAppr;
	    setDirty();
	}

	public String getDocRevPrep() {
	    return docRevPrep;
	}
	public void setDocRevPrep(String docRevPrep) {
	    this.docRevPrep = docRevPrep;
	    setDirty();
	}

	public String getId() {
	    return id;
	}
	public void setId(String id) {
	    this.id = id;
	    setDirty();
	}

	public String getNote() {
	    return note;
	}
	public void setNote(String note) {
	    this.note = note;
	    setDirty();
	}

	public String getNote2() {
	    return note2;
	}
	public void setNote2(String note2) {
	    this.note2 = note2;
	    setDirty();
	}

	public String getNote3() {
	    return note3;
	}
	public void setNote3(String note3) {
	    this.note3 = note3;
	    setDirty();
	}

	public Integer getFlagPrSpe() {
	    return flagPrSpe;
	}
	public void setFlagPrSpe(Integer flagPrSpe) {
	    this.flagPrSpe = flagPrSpe;
	    setDirty();
	}

	public String getDocRev() {
	    return docRev;
	}
	public void setDocRev(String docRev) {
	    this.docRev = docRev;
	    setDirty();
	}

	public Integer getRmNum() {
	    return rmNum;
	}
	public void setRmNum(Integer rmNum) {
	    this.rmNum = rmNum;
	    setDirty();
	}

	public String getStdAcc() {
	    return stdAcc;
	}
	public void setStdAcc(String stdAcc) {
	    this.stdAcc = stdAcc;
	    setDirty();
	}

	public String getStdAcc2() {
	    return stdAcc2;
	}
	public void setStdAcc2(String stdAcc2) {
	    this.stdAcc2 = stdAcc2;
	    setDirty();
	}

	public String getStdAcc3() {
	    return stdAcc3;
	}
	public void setStdAcc3(String stdAcc3) {
	    this.stdAcc3 = stdAcc3;
	    setDirty();
	}

	public String getStdAcc4() {
	    return stdAcc4;
	}
	public void setStdAcc4(String stdAcc4) {
	    this.stdAcc4 = stdAcc4;
	    setDirty();
	}

	public String getStdAcc5() {
	    return stdAcc5;
	}
	public void setStdAcc5(String stdAcc5) {
	    this.stdAcc5 = stdAcc5;
	    setDirty();
	}

	public String getStdAcc6() {
	    return stdAcc6;
	}
	public void setStdAcc6(String stdAcc6) {
	    this.stdAcc6 = stdAcc6;
	    setDirty();
	}

	public String getStdAccDes() {
	    return stdAccDes;
	}
	public void setStdAccDes(String stdAccDes) {
	    this.stdAccDes = stdAccDes;
	    setDirty();
	}

	public String getStdAccDes2() {
	    return stdAccDes2;
	}
	public void setStdAccDes2(String stdAccDes2) {
	    this.stdAccDes2 = stdAccDes2;
	    setDirty();
	}

	public String getStdAccDes3() {
	    return stdAccDes3;
	}
	public void setStdAccDes3(String stdAccDes3) {
	    this.stdAccDes3 = stdAccDes3;
	    setDirty();
	}

	public String getStdAccDes4() {
	    return stdAccDes4;
	}
	public void setStdAccDes4(String stdAccDes4) {
	    this.stdAccDes4 = stdAccDes4;
	    setDirty();
	}

	public String getStdAccDes5() {
	    return stdAccDes5;
	}
	public void setStdAccDes5(String stdAccDes5) {
	    this.stdAccDes5 = stdAccDes5;
	    setDirty();
	}

	public String getStdAccDes6() {
	    return stdAccDes6;
	}
	public void setStdAccDes6(String stdAccDes6) {
	    this.stdAccDes6 = stdAccDes6;
	    setDirty();
	}

	public String getTasInclA() {
	    return tasInclA;
	}
	public void setTasInclA(String tasInclA) {
	    this.tasInclA = tasInclA;
	    setDirty();
	}

	public String getTasInclB() {
	    return tasInclB;
	}
	public void setTasInclB(String tasInclB) {
	    this.tasInclB = tasInclB;
	    setDirty();
	}

	public String getTasInclC() {
	    return tasInclC;
	}
	public void setTasInclC(String tasInclC) {
	    this.tasInclC = tasInclC;
	    setDirty();
	}

	public String getTasInclD() {
	    return tasInclD;
	}
	public void setTasInclD(String tasInclD) {
	    this.tasInclD = tasInclD;
	    setDirty();
	}

	public String getTasInclAG() {
	    return tasInclAG;
	}
	public void setTasInclAG(String tasInclAG) {
	    this.tasInclAG = tasInclAG;
	    setDirty();
	}

	public String getTasInclBG() {
	    return tasInclBG;
	}
	public void setTasInclBG(String tasInclBG) {
	    this.tasInclBG = tasInclBG;
	    setDirty();
	}

	public String getTasInclCG() {
	    return tasInclCG;
	}
	public void setTasInclCG(String tasInclCG) {
	    this.tasInclCG = tasInclCG;
	    setDirty();
	}

	public String getTasInclDG() {
	    return tasInclDG;
	}
	public void setTasInclDG(String tasInclDG) {
	    this.tasInclDG = tasInclDG;
	    setDirty();
	}

	public String getTasInclNote1() {
	    return tasInclNote1;
	}
	public void setTasInclNote1(String tasInclNote1) {
	    this.tasInclNote1 = tasInclNote1;
	    setDirty();
	}

	public String getTasInclNote2() {
	    return tasInclNote2;
	}
	public void setTasInclNote2(String tasInclNote2) {
	    this.tasInclNote2 = tasInclNote2;
	    setDirty();
	}

	public String getTasInclNote3() {
	    return tasInclNote3;
	}
	public void setTasInclNote3(String tasInclNote3) {
	    this.tasInclNote3 = tasInclNote3;
	    setDirty();
	}

	public Float getDocRich() {
	    return docRich;
	}
	public void setDocRich(Float docRich) {
	    this.docRich = docRich;
	    setDirty();
	}

	public Integer getTpAffAcc() {
	    return tpAffAcc;
	}
	public void setTpAffAcc(Integer tpAffAcc) {
	    this.tpAffAcc = tpAffAcc;
	    setDirty();
	}

	public Integer getTpAffAcc2() {
	    return tpAffAcc2;
	}
	public void setTpAffAcc2(Integer tpAffAcc2) {
	    this.tpAffAcc2 = tpAffAcc2;
	    setDirty();
	}

	public Integer getTpAffAcc3() {
	    return tpAffAcc3;
	}
	public void setTpAffAcc3(Integer tpAffAcc3) {
	    this.tpAffAcc3 = tpAffAcc3;
	    setDirty();
	}

	public Integer getTpAffAcc4() {
	    return tpAffAcc4;
	}
	public void setTpAffAcc4(Integer tpAffAcc4) {
	    this.tpAffAcc4 = tpAffAcc4;
	    setDirty();
	}

	public Integer getTpAffAcc5() {
	    return tpAffAcc5;
	}
	public void setTpAffAcc5(Integer tpAffAcc5) {
	    this.tpAffAcc5 = tpAffAcc5;
	    setDirty();
	}

	public Integer getTpAffAcc6() {
	    return tpAffAcc6;
	}
	public void setTpAffAcc6(Integer tpAffAcc6) {
	    this.tpAffAcc6 = tpAffAcc6;
	    setDirty();
	}

	public Integer getTpAffGr1() {
	    return tpAffGr1;
	}
	public void setTpAffGr1(Integer tpAffGr1) {
	    this.tpAffGr1 = tpAffGr1;
	    setDirty();
	}

	public String getRmNumRev() {
	    return rmNumRev;
	}
	public void setRmNumRev(String rmNumRev) {
	    this.rmNumRev = rmNumRev;
	    setDirty();
	}

	public String getRmOrN() {
	    return rmOrN;
	}
	public void setRmOrN(String rmOrN) {
	    this.rmOrN = rmOrN;
	    setDirty();
	}

	public String getDesValoreProdotto() {
	    return desValoreProdotto;
	}
	public void setDesValoreProdotto(String desValoreProdotto) {
	    this.desValoreProdotto = desValoreProdotto;
	    setDirty();
	}

	public String getDocDescRev() {
	    return docDescRev;
	}
	public void setDocDescRev(String docDescRev) {
	    this.docDescRev = docDescRev;
	    setDirty();
	}

	public java.sql.Date getDocDataRev() {
	    return docDataRev;
	}
	public void setDocDataRev(java.sql.Date docDataRev) {
	    this.docDataRev = docDataRev;
	    setDirty();
	}

	public String getTasInclAct() {
	    return tasInclAct;
	}
	public void setTasInclAct(String tasInclAct) {
	    this.tasInclAct = tasInclAct;
	    setDirty();
	}

	public String getTasInclActG() {
	    return tasInclActG;
	}
	public void setTasInclActG(String tasInclActG) {
	    this.tasInclActG = tasInclActG;
	    setDirty();
	}

	public String getTpAffGr() {
	    return tpAffGr;
	}
	public void setTpAffGr(String tpAffGr) {
	    this.tpAffGr = tpAffGr;
	    setDirty();
	}

	public Integer getFlagRmc() {
	    return flagRmc;
	}
	public void setFlagRmc(Integer flagRmc) {
	    this.flagRmc = flagRmc;
	    setDirty();
	}

	public java.sql.Timestamp getTimestampAgg() {
	    return timestampAgg;
	}
	public void setTimestampAgg(java.sql.Timestamp timestampAgg) {
	    this.timestampAgg = timestampAgg;
	    setDirty();
	}

	// For elements that start with a lowercase letter after get/set, we now uppercase them:

	public Integer getHFlagInf() {
	    return hFlagInf;
	}
	public void setHFlagInf(Integer hFlagInf) {
	    this.hFlagInf = hFlagInf;
	    setDirty();
	}

	public BigDecimal getHMin() {
	    return hMin;
	}
	public void setHMin(BigDecimal hMin) {
	    this.hMin = hMin;
	    setDirty();
	}

	public BigDecimal getHMax() {
	    return hMax;
	}
	public void setHMax(BigDecimal hMax) {
	    this.hMax = hMax;
	    setDirty();
	}

	public Integer getHPpmOPerc() {
	    return hPpmOPerc;
	}
	public void setHPpmOPerc(Integer hPpmOPerc) {
	    this.hPpmOPerc = hPpmOPerc;
	    setDirty();
	}

	public Integer getAlFlagInf() {
	    return alFlagInf;
	}
	public void setAlFlagInf(Integer alFlagInf) {
	    this.alFlagInf = alFlagInf;
	    setDirty();
	}

	public Integer getAlsolFlagInf() {
	    return alsolFlagInf;
	}
	public void setAlsolFlagInf(Integer alsolFlagInf) {
	    this.alsolFlagInf = alsolFlagInf;
	    setDirty();
	}

	public BigDecimal getAlMin() {
	    return alMin;
	}
	public void setAlMin(BigDecimal alMin) {
	    this.alMin = alMin;
	    setDirty();
	}

	public BigDecimal getAlMax() {
	    return alMax;
	}
	public void setAlMax(BigDecimal alMax) {
	    this.alMax = alMax;
	    setDirty();
	}

	public Integer getAlsolInf() {
	    return alsolInf;
	}
	public void setAlsolInf(Integer alsolInf) {
	    this.alsolInf = alsolInf;
	    setDirty();
	}

	public BigDecimal getAlsolMin() {
	    return alsolMin;
	}
	public void setAlsolMin(BigDecimal alsolMin) {
	    this.alsolMin = alsolMin;
	    setDirty();
	}

	public BigDecimal getAlsolMax() {
	    return alsolMax;
	}
	public void setAlsolMax(BigDecimal alsolMax) {
	    this.alsolMax = alsolMax;
	    setDirty();
	}

	public Integer getAsFlagInf() {
	    return asFlagInf;
	}
	public void setAsFlagInf(Integer asFlagInf) {
	    this.asFlagInf = asFlagInf;
	    setDirty();
	}

	public BigDecimal getAsMin() {
	    return asMin;
	}
	public void setAsMin(BigDecimal asMin) {
	    this.asMin = asMin;
	    setDirty();
	}

	public BigDecimal getAsMax() {
	    return asMax;
	}
	public void setAsMax(BigDecimal asMax) {
	    this.asMax = asMax;
	    setDirty();
	}

	public Integer getBFlagInf() {
	    return bFlagInf;
	}
	public void setBFlagInf(Integer bFlagInf) {
	    this.bFlagInf = bFlagInf;
	    setDirty();
	}

	public BigDecimal getBMin() {
	    return bMin;
	}
	public void setBMin(BigDecimal bMin) {
	    this.bMin = bMin;
	    setDirty();
	}

	public BigDecimal getBMax() {
	    return bMax;
	}
	public void setBMax(BigDecimal bMax) {
	    this.bMax = bMax;
	    setDirty();
	}

	public Integer getBiFlagInf() {
	    return biFlagInf;
	}
	public void setBiFlagInf(Integer biFlagInf) {
	    this.biFlagInf = biFlagInf;
	    setDirty();
	}

	public BigDecimal getBiMin() {
	    return biMin;
	}
	public void setBiMin(BigDecimal biMin) {
	    this.biMin = biMin;
	    setDirty();
	}

	public BigDecimal getBiMax() {
	    return biMax;
	}
	public void setBiMax(BigDecimal biMax) {
	    this.biMax = biMax;
	    setDirty();
	}

	public Integer getCFlagInf() {
	    return cFlagInf;
	}
	public void setCFlagInf(Integer cFlagInf) {
	    this.cFlagInf = cFlagInf;
	    setDirty();
	}

	public BigDecimal getCMin() {
	    return cMin;
	}
	public void setCMin(BigDecimal cMin) {
	    this.cMin = cMin;
	    setDirty();
	}

	public BigDecimal getCMax() {
	    return cMax;
	}
	public void setCMax(BigDecimal cMax) {
	    this.cMax = cMax;
	    setDirty();
	}

	public Integer getCNFlagInf() {
	    return cNFlagInf;
	}
	public void setCNFlagInf(Integer cNFlagInf) {
	    this.cNFlagInf = cNFlagInf;
	    setDirty();
	}

	public BigDecimal getCNMin() {
	    return cNMin;
	}
	public void setCNMin(BigDecimal cNMin) {
	    this.cNMin = cNMin;
	    setDirty();
	}

	public BigDecimal getCNMax() {
	    return cNMax;
	}
	public void setCNMax(BigDecimal cNMax) {
	    this.cNMax = cNMax;
	    setDirty();
	}

	public Integer getCaFlagInf() {
	    return caFlagInf;
	}
	public void setCaFlagInf(Integer caFlagInf) {
	    this.caFlagInf = caFlagInf;
	    setDirty();
	}

	public BigDecimal getCaMin() {
	    return caMin;
	}
	public void setCaMin(BigDecimal caMin) {
	    this.caMin = caMin;
	    setDirty();
	}

	public BigDecimal getCaMax() {
	    return caMax;
	}
	public void setCaMax(BigDecimal caMax) {
	    this.caMax = caMax;
	    setDirty();
	}

	public Integer getCbFlagInf() {
	    return cbFlagInf;
	}
	public void setCbFlagInf(Integer cbFlagInf) {
	    this.cbFlagInf = cbFlagInf;
	    setDirty();
	}

	public BigDecimal getCbMin() {
	    return cbMin;
	}
	public void setCbMin(BigDecimal cbMin) {
	    this.cbMin = cbMin;
	    setDirty();
	}

	public BigDecimal getCbMax() {
	    return cbMax;
	}
	public void setCbMax(BigDecimal cbMax) {
	    this.cbMax = cbMax;
	    setDirty();
	}

	public Integer getCeFlagInf() {
	    return ceFlagInf;
	}
	public void setCeFlagInf(Integer ceFlagInf) {
	    this.ceFlagInf = ceFlagInf;
	    setDirty();
	}

	public BigDecimal getCeMin() {
	    return ceMin;
	}
	public void setCeMin(BigDecimal ceMin) {
	    this.ceMin = ceMin;
	    setDirty();
	}

	public BigDecimal getCeMax() {
	    return ceMax;
	}
	public void setCeMax(BigDecimal ceMax) {
	    this.ceMax = ceMax;
	    setDirty();
	}

	public Integer getCoFlagInf() {
	    return coFlagInf;
	}
	public void setCoFlagInf(Integer coFlagInf) {
	    this.coFlagInf = coFlagInf;
	    setDirty();
	}

	public BigDecimal getCoMin() {
	    return coMin;
	}
	public void setCoMin(BigDecimal coMin) {
	    this.coMin = coMin;
	    setDirty();
	}

	public BigDecimal getCoMax() {
	    return coMax;
	}
	public void setCoMax(BigDecimal coMax) {
	    this.coMax = coMax;
	    setDirty();
	}

	public Integer getCrFlagInf() {
	    return crFlagInf;
	}
	public void setCrFlagInf(Integer crFlagInf) {
	    this.crFlagInf = crFlagInf;
	    setDirty();
	}

	public BigDecimal getCrMin() {
	    return crMin;
	}
	public void setCrMin(BigDecimal crMin) {
	    this.crMin = crMin;
	    setDirty();
	}

	public BigDecimal getCrMax() {
	    return crMax;
	}
	public void setCrMax(BigDecimal crMax) {
	    this.crMax = crMax;
	    setDirty();
	}

	public Integer getCrEqFlagInf() {
	    return crEqFlagInf;
	}
	public void setCrEqFlagInf(Integer crEqFlagInf) {
	    this.crEqFlagInf = crEqFlagInf;
	    setDirty();
	}

	public BigDecimal getCrEqMin() {
	    return crEqMin;
	}
	public void setCrEqMin(BigDecimal crEqMin) {
	    this.crEqMin = crEqMin;
	    setDirty();
	}

	public BigDecimal getCrEqMax() {
	    return crEqMax;
	}
	public void setCrEqMax(BigDecimal crEqMax) {
	    this.crEqMax = crEqMax;
	    setDirty();
	}

	public Integer getCuFlagInf() {
	    return cuFlagInf;
	}
	public void setCuFlagInf(Integer cuFlagInf) {
	    this.cuFlagInf = cuFlagInf;
	    setDirty();
	}

	public BigDecimal getCuMin() {
	    return cuMin;
	}
	public void setCuMin(BigDecimal cuMin) {
	    this.cuMin = cuMin;
	    setDirty();
	}

	public BigDecimal getCuMax() {
	    return cuMax;
	}
	public void setCuMax(BigDecimal cuMax) {
	    this.cuMax = cuMax;
	    setDirty();
	}

	public Integer getFeFlagInf() {
	    return feFlagInf;
	}
	public void setFeFlagInf(Integer feFlagInf) {
	    this.feFlagInf = feFlagInf;
	    setDirty();
	}

	public BigDecimal getFeMin() {
	    return feMin;
	}
	public void setFeMin(BigDecimal feMin) {
	    this.feMin = feMin;
	    setDirty();
	}

	public BigDecimal getFeMax() {
	    return feMax;
	}
	public void setFeMax(BigDecimal feMax) {
	    this.feMax = feMax;
	    setDirty();
	}

	// h* properties are already handled above (HFlagInf, HMin, HMax, HPpmOPerc).

	public Integer getJfFlagInf() {
	    return jfFlagInf;
	}
	public void setJfFlagInf(Integer jfFlagInf) {
	    this.jfFlagInf = jfFlagInf;
	    setDirty();
	}

	public BigDecimal getJfMin() {
	    return jfMin;
	}
	public void setJfMin(BigDecimal jfMin) {
	    this.jfMin = jfMin;
	    setDirty();
	}

	public BigDecimal getJfMax() {
	    return jfMax;
	}
	public void setJfMax(BigDecimal jfMax) {
	    this.jfMax = jfMax;
	    setDirty();
	}

	public Integer getMnFlagInf() {
	    return mnFlagInf;
	}
	public void setMnFlagInf(Integer mnFlagInf) {
	    this.mnFlagInf = mnFlagInf;
	    setDirty();
	}

	public BigDecimal getMnMin() {
	    return mnMin;
	}
	public void setMnMin(BigDecimal mnMin) {
	    this.mnMin = mnMin;
	    setDirty();
	}

	public BigDecimal getMnMax() {
	    return mnMax;
	}
	public void setMnMax(BigDecimal mnMax) {
	    this.mnMax = mnMax;
	    setDirty();
	}

	public Integer getMoFlagInf() {
	    return moFlagInf;
	}
	public void setMoFlagInf(Integer moFlagInf) {
	    this.moFlagInf = moFlagInf;
	    setDirty();
	}

	public BigDecimal getMoMin() {
	    return moMin;
	}
	public void setMoMin(BigDecimal moMin) {
	    this.moMin = moMin;
	    setDirty();
	}

	public BigDecimal getMoMax() {
	    return moMax;
	}
	public void setMoMax(BigDecimal moMax) {
	    this.moMax = moMax;
	    setDirty();
	}

	public Integer getNFlagInf() {
	    return nFlagInf;
	}
	public void setNFlagInf(Integer nFlagInf) {
	    this.nFlagInf = nFlagInf;
	    setDirty();
	}

	public BigDecimal getNMin() {
	    return nMin;
	}
	public void setNMin(BigDecimal nMin) {
	    this.nMin = nMin;
	    setDirty();
	}

	public BigDecimal getNMax() {
	    return nMax;
	}
	public void setNMax(BigDecimal nMax) {
	    this.nMax = nMax;
	    setDirty();
	}

	public Integer getNPpmOPerc() {
	    return nPpmOPerc;
	}
	public void setNPpmOPerc(Integer nPpmOPerc) {
	    this.nPpmOPerc = nPpmOPerc;
	    setDirty();
	}

	public Integer getNbFlagInf() {
	    return nbFlagInf;
	}
	public void setNbFlagInf(Integer nbFlagInf) {
	    this.nbFlagInf = nbFlagInf;
	    setDirty();
	}

	public BigDecimal getNbMin() {
	    return nbMin;
	}
	public void setNbMin(BigDecimal nbMin) {
	    this.nbMin = nbMin;
	    setDirty();
	}

	public BigDecimal getNbMax() {
	    return nbMax;
	}
	public void setNbMax(BigDecimal nbMax) {
	    this.nbMax = nbMax;
	    setDirty();
	}

	public Integer getNbTaFlagInf() {
	    return nbTaFlagInf;
	}
	public void setNbTaFlagInf(Integer nbTaFlagInf) {
	    this.nbTaFlagInf = nbTaFlagInf;
	    setDirty();
	}

	public BigDecimal getNbTaMin() {
	    return nbTaMin;
	}
	public void setNbTaMin(BigDecimal nbTaMin) {
	    this.nbTaMin = nbTaMin;
	    setDirty();
	}

	public BigDecimal getNbTaMax() {
	    return nbTaMax;
	}
	public void setNbTaMax(BigDecimal nbTaMax) {
	    this.nbTaMax = nbTaMax;
	    setDirty();
	}

	public Integer getNiFlagInf() {
	    return niFlagInf;
	}
	public void setNiFlagInf(Integer niFlagInf) {
	    this.niFlagInf = niFlagInf;
	    setDirty();
	}

	public BigDecimal getNiMin() {
	    return niMin;
	}
	public void setNiMin(BigDecimal niMin) {
	    this.niMin = niMin;
	    setDirty();
	}

	public BigDecimal getNiMax() {
	    return niMax;
	}
	public void setNiMax(BigDecimal niMax) {
	    this.niMax = niMax;
	    setDirty();
	}

	public Integer getOFlagInf() {
	    return oFlagInf;
	}
	public void setOFlagInf(Integer oFlagInf) {
	    this.oFlagInf = oFlagInf;
	    setDirty();
	}

	public BigDecimal getOMin() {
	    return oMin;
	}
	public void setOMin(BigDecimal oMin) {
	    this.oMin = oMin;
	    setDirty();
	}

	public BigDecimal getOMax() {
	    return oMax;
	}
	public void setOMax(BigDecimal oMax) {
	    this.oMax = oMax;
	    setDirty();
	}

	public Integer getOPpmOPerc() {
	    return oPpmOPerc;
	}
	public void setOPpmOPerc(Integer oPpmOPerc) {
	    this.oPpmOPerc = oPpmOPerc;
	    setDirty();
	}

	public Integer getPFlagInf() {
	    return pFlagInf;
	}
	public void setPFlagInf(Integer pFlagInf) {
	    this.pFlagInf = pFlagInf;
	    setDirty();
	}

	public BigDecimal getPMin() {
	    return pMin;
	}
	public void setPMin(BigDecimal pMin) {
	    this.pMin = pMin;
	    setDirty();
	}

	public BigDecimal getPMax() {
	    return pMax;
	}
	public void setPMax(BigDecimal pMax) {
	    this.pMax = pMax;
	    setDirty();
	}

	public Integer getPbFlagInf() {
	    return pbFlagInf;
	}
	public void setPbFlagInf(Integer pbFlagInf) {
	    this.pbFlagInf = pbFlagInf;
	    setDirty();
	}

	public BigDecimal getPbMin() {
	    return pbMin;
	}
	public void setPbMin(BigDecimal pbMin) {
	    this.pbMin = pbMin;
	    setDirty();
	}

	public BigDecimal getPbMax() {
	    return pbMax;
	}
	public void setPbMax(BigDecimal pbMax) {
	    this.pbMax = pbMax;
	    setDirty();
	}

	public Integer getPcmFlagInf() {
	    return pcmFlagInf;
	}
	public void setPcmFlagInf(Integer pcmFlagInf) {
	    this.pcmFlagInf = pcmFlagInf;
	    setDirty();
	}

	public BigDecimal getPcmMin() {
	    return pcmMin;
	}
	public void setPcmMin(BigDecimal pcmMin) {
	    this.pcmMin = pcmMin;
	    setDirty();
	}

	public BigDecimal getPcmMax() {
	    return pcmMax;
	}
	public void setPcmMax(BigDecimal pcmMax) {
	    this.pcmMax = pcmMax;
	    setDirty();
	}

	public Integer getPreFlagInf() {
	    return preFlagInf;
	}
	public void setPreFlagInf(Integer preFlagInf) {
	    this.preFlagInf = preFlagInf;
	    setDirty();
	}

	public BigDecimal getPreMin() {
	    return preMin;
	}
	public void setPreMin(BigDecimal preMin) {
	    this.preMin = preMin;
	    setDirty();
	}

	public BigDecimal getPreMax() {
	    return preMax;
	}
	public void setPreMax(BigDecimal preMax) {
	    this.preMax = preMax;
	    setDirty();
	}

	public Integer getSFlagInf() {
	    return sFlagInf;
	}
	public void setSFlagInf(Integer sFlagInf) {
	    this.sFlagInf = sFlagInf;
	    setDirty();
	}

	public BigDecimal getSMin() {
	    return sMin;
	}
	public void setSMin(BigDecimal sMin) {
	    this.sMin = sMin;
	    setDirty();
	}

	public BigDecimal getSMax() {
	    return sMax;
	}
	public void setSMax(BigDecimal sMax) {
	    this.sMax = sMax;
	    setDirty();
	}

	public Integer getSbFlagInf() {
	    return sbFlagInf;
	}
	public void setSbFlagInf(Integer sbFlagInf) {
	    this.sbFlagInf = sbFlagInf;
	    setDirty();
	}

	public BigDecimal getSbMin() {
	    return sbMin;
	}
	public void setSbMin(BigDecimal sbMin) {
	    this.sbMin = sbMin;
	    setDirty();
	}

	public BigDecimal getSbMax() {
	    return sbMax;
	}
	public void setSbMax(BigDecimal sbMax) {
	    this.sbMax = sbMax;
	    setDirty();
	}

	public Integer getSiFlagInf() {
	    return siFlagInf;
	}
	public void setSiFlagInf(Integer siFlagInf) {
	    this.siFlagInf = siFlagInf;
	    setDirty();
	}

	public BigDecimal getSiMin() {
	    return siMin;
	}
	public void setSiMin(BigDecimal siMin) {
	    this.siMin = siMin;
	    setDirty();
	}

	public BigDecimal getSiMax() {
	    return siMax;
	}
	public void setSiMax(BigDecimal siMax) {
	    this.siMax = siMax;
	    setDirty();
	}

	public Integer getSnFlagInf() {
	    return snFlagInf;
	}
	public void setSnFlagInf(Integer snFlagInf) {
	    this.snFlagInf = snFlagInf;
	    setDirty();
	}

	public BigDecimal getSnMin() {
	    return snMin;
	}
	public void setSnMin(BigDecimal snMin) {
	    this.snMin = snMin;
	    setDirty();
	}

	public BigDecimal getSnMax() {
	    return snMax;
	}
	public void setSnMax(BigDecimal snMax) {
	    this.snMax = snMax;
	    setDirty();
	}

	public Integer getTaFlagInf() {
	    return taFlagInf;
	}
	public void setTaFlagInf(Integer taFlagInf) {
	    this.taFlagInf = taFlagInf;
	    setDirty();
	}

	public BigDecimal getTaMin() {
	    return taMin;
	}
	public void setTaMin(BigDecimal taMin) {
	    this.taMin = taMin;
	    setDirty();
	}

	public BigDecimal getTaMax() {
	    return taMax;
	}
	public void setTaMax(BigDecimal taMax) {
	    this.taMax = taMax;
	    setDirty();
	}

	public Integer getTiFlagInf() {
	    return tiFlagInf;
	}
	public void setTiFlagInf(Integer tiFlagInf) {
	    this.tiFlagInf = tiFlagInf;
	    setDirty();
	}

	public BigDecimal getTiMin() {
	    return tiMin;
	}
	public void setTiMin(BigDecimal tiMin) {
	    this.tiMin = tiMin;
	    setDirty();
	}

	public BigDecimal getTiMax() {
	    return tiMax;
	}
	public void setTiMax(BigDecimal tiMax) {
	    this.tiMax = tiMax;
	    setDirty();
	}

	public Integer getVFlagInf() {
	    return vFlagInf;
	}
	public void setVFlagInf(Integer vFlagInf) {
	    this.vFlagInf = vFlagInf;
	    setDirty();
	}

	public BigDecimal getVMin() {
	    return vMin;
	}
	public void setVMin(BigDecimal vMin) {
	    this.vMin = vMin;
	    setDirty();
	}

	public BigDecimal getVMax() {
	    return vMax;
	}
	public void setVMax(BigDecimal vMax) {
	    this.vMax = vMax;
	    setDirty();
	}

	public Integer getWFlagInf() {
	    return wFlagInf;
	}
	public void setWFlagInf(Integer wFlagInf) {
	    this.wFlagInf = wFlagInf;
	    setDirty();
	}

	public BigDecimal getWMin() {
	    return wMin;
	}
	public void setWMin(BigDecimal wMin) {
	    this.wMin = wMin;
	    setDirty();
	}

	public BigDecimal getWMax() {
	    return wMax;
	}
	public void setWMax(BigDecimal wMax) {
	    this.wMax = wMax;
	    setDirty();
	}

	public Integer getXfFlagInf() {
	    return xfFlagInf;
	}
	public void setXfFlagInf(Integer xfFlagInf) {
	    this.xfFlagInf = xfFlagInf;
	    setDirty();
	}

	public BigDecimal getXfMin() {
	    return xfMin;
	}
	public void setXfMin(BigDecimal xfMin) {
	    this.xfMin = xfMin;
	    setDirty();
	}

	public BigDecimal getXfMax() {
	    return xfMax;
	}
	public void setXfMax(BigDecimal xfMax) {
	    this.xfMax = xfMax;
	    setDirty();
	}

	public Integer getYFlagInf() {
	    return yFlagInf;
	}
	public void setYFlagInf(Integer yFlagInf) {
	    this.yFlagInf = yFlagInf;
	    setDirty();
	}

	public BigDecimal getYMin() {
	    return yMin;
	}
	public void setYMin(BigDecimal yMin) {
	    this.yMin = yMin;
	    setDirty();
	}

	public BigDecimal getYMax() {
	    return yMax;
	}
	public void setYMax(BigDecimal yMax) {
	    this.yMax = yMax;
	    setDirty();
	}

	public Integer getZrFlagInf() {
	    return zrFlagInf;
	}
	public void setZrFlagInf(Integer zrFlagInf) {
	    this.zrFlagInf = zrFlagInf;
	    setDirty();
	}

	public BigDecimal getZrMin() {
	    return zrMin;
	}
	public void setZrMin(BigDecimal zrMin) {
	    this.zrMin = zrMin;
	    setDirty();
	}

	public BigDecimal getZrMax() {
	    return zrMax;
	}
	public void setZrMax(BigDecimal zrMax) {
	    this.zrMax = zrMax;
	    setDirty();
	}

	public String getAcFormula1() {
	    return acFormula1;
	}
	public void setAcFormula1(String acFormula1) {
	    this.acFormula1 = acFormula1;
	    setDirty();
	}

	public BigDecimal getAcFormula1Max() {
	    return acFormula1Max;
	}
	public void setAcFormula1Max(BigDecimal acFormula1Max) {
	    this.acFormula1Max = acFormula1Max;
	    setDirty();
	}

	public BigDecimal getAcFormula1Min() {
	    return acFormula1Min;
	}
	public void setAcFormula1Min(BigDecimal acFormula1Min) {
	    this.acFormula1Min = acFormula1Min;
	    setDirty();
	}

	public String getAcFormula2() {
	    return acFormula2;
	}
	public void setAcFormula2(String acFormula2) {
	    this.acFormula2 = acFormula2;
	    setDirty();
	}

	public BigDecimal getAcFormula2Max() {
	    return acFormula2Max;
	}
	public void setAcFormula2Max(BigDecimal acFormula2Max) {
	    this.acFormula2Max = acFormula2Max;
	    setDirty();
	}

	public BigDecimal getAcFormula2Min() {
	    return acFormula2Min;
	}
	public void setAcFormula2Min(BigDecimal acFormula2Min) {
	    this.acFormula2Min = acFormula2Min;
	    setDirty();
	}

	public String getAcFormula3() {
	    return acFormula3;
	}
	public void setAcFormula3(String acFormula3) {
	    this.acFormula3 = acFormula3;
	    setDirty();
	}

	public BigDecimal getAcFormula3Max() {
	    return acFormula3Max;
	}
	public void setAcFormula3Max(BigDecimal acFormula3Max) {
	    this.acFormula3Max = acFormula3Max;
	    setDirty();
	}

	public BigDecimal getAcFormula3Min() {
	    return acFormula3Min;
	}
	public void setAcFormula3Min(BigDecimal acFormula3Min) {
	    this.acFormula3Min = acFormula3Min;
	    setDirty();
	}

	public String getAcFormula4() {
	    return acFormula4;
	}
	public void setAcFormula4(String acFormula4) {
	    this.acFormula4 = acFormula4;
	    setDirty();
	}

	public BigDecimal getAcFormula4Max() {
	    return acFormula4Max;
	}
	public void setAcFormula4Max(BigDecimal acFormula4Max) {
	    this.acFormula4Max = acFormula4Max;
	    setDirty();
	}

	public BigDecimal getAcFormula4Min() {
	    return acFormula4Min;
	}
	public void setAcFormula4Min(BigDecimal acFormula4Min) {
	    this.acFormula4Min = acFormula4Min;
	    setDirty();
	}

	public String getAcFormula5() {
	    return acFormula5;
	}
	public void setAcFormula5(String acFormula5) {
	    this.acFormula5 = acFormula5;
	    setDirty();
	}

	public BigDecimal getAcFormula5Max() {
	    return acFormula5Max;
	}
	public void setAcFormula5Max(BigDecimal acFormula5Max) {
	    this.acFormula5Max = acFormula5Max;
	    setDirty();
	}

	public BigDecimal getAcFormula5Min() {
	    return acFormula5Min;
	}
	public void setAcFormula5Min(BigDecimal acFormula5Min) {
	    this.acFormula5Min = acFormula5Min;
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
		return YPTQcAnalisiRmTM.getInstance();
	}
}
