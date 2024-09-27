package it.monchieri.thip.qualita.controllo;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.thera.thermfw.base.Utils;
import com.thera.thermfw.common.ErrorMessage;

import it.mame.thip.qualita.controllo.YMisuraCaracteriche;
import it.mame.thip.utils.MathEval;
import it.thera.thip.qualita.controllo.DocumentiCollaudoRilevazioneMisure;
import it.thera.thip.qualita.controllo.MisuraFase;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 26/09/2024
 * <br><br>
 * <b>71XXX	DSSOF3	26/09/2024</b>
 * <p>Prima stesura.<br>
 *  
 * </p>
 */

public class YDocumentiCollaudoRilevazioneMisure extends DocumentiCollaudoRilevazioneMisure {

	@Override
	public int save() throws SQLException {
		beforeSave();
		return super.save();
	}

	public void beforeSave() {
		calcoloFormulaAnChim();
	}

	protected ErrorMessage calcoloFormulaAnChim (){
		ErrorMessage err = null;

		YMisuraCaracteriche carat = (YMisuraCaracteriche) getMisureCaratteristica();
		if (carat.getYFormula() != null) {
			MathEval meVal01 = new MathEval();
			MathEval meVal02 = new MathEval();
			//MisuraFase fase = (MisuraFase) carat.getFase();
			//List listCaratteristiche = fase.getMisuraCaracteriche();
			//Iterator iCarat = listCaratteristiche.iterator();
			//			while (iCarat.hasNext()) {
			//				YMisuraCaracteriche c = (YMisuraCaracteriche) iCarat.next();
			//				if (!c.getKey().equals(carat.getKey())) {
			//					Iterator iMis = c.getMisura().iterator();
			//					while (iMis.hasNext()) {
			//						DocumentiCollaudoRilevazioneMisure misura = (DocumentiCollaudoRilevazioneMisure) iMis.next();
			//                        if (misura.getProgressivoPezzo().intValue() == getProgressivoPezzo().intValue()) {
			//                            if (misura.getValoreRilevato() != null) {
			//                                meVal01.setVariable(c.getDescrRidottaMame(), misura.getValoreRilevato().doubleValue());
			//                            }
			//                            if (misura.getValoreRilevatoMame() != null) {
			//                                meVal02.setVariable(c.getDescrRidottaMame(), misura.getValoreRilevatoMame().doubleValue());
			//                            }
			//                        }
			//					}
			//				}
			//			}
			BigDecimal valore01 = null;
			try {
				String formula = carat.getYFormula();
				formula = formula.replaceAll(",", ".");
				double v = meVal01.evaluate(formula);
				valore01 = new BigDecimal(v);
				valore01 = valore01.setScale(5, BigDecimal.ROUND_HALF_DOWN);
			}
			catch (Exception ex) {
				//Non fare nulla
			}
			if (valore01 != null && (valore01.compareTo(new BigDecimal("9999.99")) > 0 || valore01.compareTo(new BigDecimal("-9999.99")) < 0)) {
				valore01 = null;
			}
			setValoreRilevato(valore01);
			char indicazEsito = ricalcolaIndicaEsito(getValoreRilevato());
			setIndicazioneEsito(indicazEsito);

			BigDecimal valore02 = null;
			try {
				String formula = carat.getYFormula();
				formula = formula.replaceAll(",", ".");
				double v = meVal02.evaluate(formula);
				valore02 = new BigDecimal(v);
				valore02 = valore02.setScale(5, BigDecimal.ROUND_HALF_DOWN);
			}
			catch (Exception ex) {
				//Non fare nulla
			}
			if (valore02 != null && (valore02.compareTo(new BigDecimal("9999.99")) > 0 || valore02.compareTo(new BigDecimal("-9999.99")) < 0)) {
				valore02 = null;
			}
			//			setValoreRilevatoMame(valore02);
			//			char indicazEsitoMame = ricalcolaIndicaEsito(getValoreRilevatoMame());
			//			setIndicazioneEsitoMame(indicazEsitoMame);
		}
		return err;
	}

	public char ricalcolaIndicaEsito (BigDecimal valoreRil) {
		if (getMisureCaratteristica() == null || valoreRil == null) {
			return MisuraFase.NON_CONTROLLATO;
		}
		//BigDecimal limInfAcc = this.getMisureCaratteristica().getLimInfAccettabilita();
		BigDecimal limInfDeroga = this.getMisureCaratteristica().getLimInfDeroga();
		BigDecimal limInfToll = this.getMisureCaratteristica().getLimInfTolleranza();
		//BigDecimal limSupAcc = this.getMisureCaratteristica().getLimSupAccettabilita();
		BigDecimal limSupDeroga = this.getMisureCaratteristica().getLimSupDeroga();
		BigDecimal limSupToll = this.getMisureCaratteristica().getLimSupTolleranza();
		BigDecimal valoreNominale = this.getValoreNominale();

		if (limInfToll == null) {
			if (valoreNominale == null) {
				limInfToll = limSupToll;
			} else {
				limInfToll = valoreNominale;
			}
		}
		if (limSupToll == null) {
			if (valoreNominale == null) {
				limSupToll = limInfToll;
			} else {
				limSupToll = valoreNominale;
			}
		}
		if (limSupDeroga == null) {
			if (limSupToll == null) {
				limSupDeroga = limInfDeroga;
			} else {
				limSupDeroga = limSupToll;
			}
		}
		if (limInfDeroga == null) {
			if (limInfToll == null) {
				limInfDeroga = limSupDeroga;
			} else {
				limInfDeroga = limInfToll;
			}
		}

		if (limInfToll != null && Utils.compare(valoreRil, limInfToll) >= 0 && Utils.compare(valoreRil, limSupToll) <= 0) {
			return MisuraFase.POSITIVO;
		}

		else if (limInfDeroga != null && Utils.compare(valoreRil, limInfDeroga) >= 0 && Utils.compare(valoreRil, limSupDeroga) <= 0) {
			return MisuraFase.NON_CONTROLLATO; //DEROGA
		} else {
			if (limInfDeroga != null) {
				return MisuraFase.NEGATIVO;
			} else {
				return MisuraFase.POSITIVO;
			}

		}
	}
}