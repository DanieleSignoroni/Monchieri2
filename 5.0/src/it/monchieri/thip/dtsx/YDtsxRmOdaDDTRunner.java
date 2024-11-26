package it.monchieri.thip.dtsx;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.batch.BatchRunnable;
import com.thera.thermfw.persist.ConnectionDescriptor;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.security.Authorizable;

import it.mame.thip.qualita.controllo.YCicloCollaudoCaratt;
import it.mame.thip.qualita.controllo.YCicloCollaudoFase;
import it.mame.thip.qualita.controllo.YNormeQualita;
import it.mame.thip.qualita.controllo.YNormeQualitaTM;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.dipendente.Dipendente;
import it.thera.thip.qualita.controllo.CicloCollaudoTestata;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 20/11/2024
 * <br><br>
 * <b>71XXX	DSSOF3	20/11/2024</b>
 * <p>Prima stesura.<br>
 *  
 * </p>
 */

public class YDtsxRmOdaDDTRunner extends BatchRunnable implements Authorizable {

	@Override
	protected boolean run() {
		List stmtInsertNorme = estrazioneAnalisiChimichePerTarget();
		ConnectionDescriptor cnd = YDtsxPtExpOrdEsecRunner.externalConnectionDescriptor(YDtsxPtExpOrdEsecRunner.NOME_DB_EXT, YDtsxPtExpOrdEsecRunner.UTENTE_DB_EXT, YDtsxPtExpOrdEsecRunner.PWD_DB_EXT, YDtsxPtExpOrdEsecRunner.SRV_DB_EXT, YDtsxPtExpOrdEsecRunner.PORTA_DB_EXT);
		try {
			if(cnd != null) {
				ConnectionManager.pushConnection(cnd);

			}else {
				output.print("** Impossibile creare il descrittore di connesione per Target **");
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		} finally {
			if (cnd != null) {
				ConnectionManager.popConnection(cnd);
			}
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List estrazioneAnalisiChimichePerTarget() {
		List list = new ArrayList();

		Vector<YNormeQualita> norme = norme();
		if(norme != null) {

			for (Iterator<YNormeQualita> iterator = norme.iterator(); iterator.hasNext();) {
				YNormeQualita norma = (YNormeQualita) iterator.next();

				try {
					String ALIAS_ACCIAIO = norma.getAliasAcciaio() != null ? norma.getAliasAcciaio() : "";
					String ACCIAO = norma.getDscAcciaio();
					Float TP_FUS_ACC = 0.f;
					String DOC_REV_APPR	= "";
					String DOC_REV_PREP = "";
					if(norma.getIdDipendAprv() != null) {
						Dipendente dipendenteApprovazione = getDipendente(norma.getIdDipendAprv());
						DOC_REV_APPR = dipendenteApprovazione.getCognome() + " " + dipendenteApprovazione.getNome();
					}
					if(norma.getIdDipendCrz() != null) {
						Dipendente dipendentePreparazione = getDipendente(norma.getIdDipendCrz());
						DOC_REV_PREP = dipendentePreparazione.getCognome() + " " + dipendentePreparazione.getNome();
					}
					String ID = norma.getIdNorma(); //first key part
					String NOTE = norma.getNote1();
					String NOTE_2 = norma.getNote2();
					String NOTE_3 = norma.getNote3();
					Integer FLAG_PR_SPE = norma.getProcessoSpec() ? 1 : 0;
					String DOC_REV = norma.getRevisione();
					String RM_NUM = norma.getRMOrNum();
					String STD_ACC = norma.getSpecRifCod1();
					String STD_ACC_2 = norma.getSpecRifCod2();
					String STD_ACC_3 = norma.getSpecRifCod3();
					String STD_ACC_4 = norma.getSpecRifCod4();
					String STD_ACC_5 = norma.getSpecRifCod5();
					String STD_ACC_6 = norma.getSpecRifCod6();
					String STD_ACC_DES = norma.getSpecRifDsc1();
					String STD_ACC_DES_2 = norma.getSpecRifDsc2();
					String STD_ACC_DES_3 = norma.getSpecRifDsc3();
					String STD_ACC_DES_4 = norma.getSpecRifDsc4();
					String STD_ACC_DES_5 = norma.getSpecRifDsc5();
					String STD_ACC_DES_6 = norma.getSpecRifDsc6();
					String TAS_INCL_A = norma.getTasInclA();
					String TAS_INCL_B = norma.getTasInclB();
					String TAS_INCL_C = norma.getTasInclC();
					String TAS_INCL_D = norma.getTasInclD();
					String TAS_INCL_A_G = norma.getTasInclGa();
					String TAS_INCL_B_G = norma.getTasInclGb();
					String TAS_INCL_C_G = norma.getTasInclGc();
					String TAS_INCL_D_G = norma.getTasInclGd();
					String TAS_INCL_NOTE_1 = norma.getTasInclNote1();
					String TAS_INCL_NOTE_2 = norma.getTasInclNote2();
					String TAS_INCL_NOTE_3 = norma.getTasInclNote3();
					Float DOC_RICH = 0.f;
					Integer TP_AFF_ACC = norma.getTypAffAcc01() ? 1 : 0;
					Integer TP_AFF_ACC_2 = norma.getTypAffAcc02() ? 1 : 0;
					Integer TP_AFF_ACC_3 = norma.getTypAffAcc03() ? 1 : 0;
					Integer TP_AFF_ACC_4 = norma.getTypAffAcc04() ? 1 : 0;
					Integer TP_AFF_ACC_5 = norma.getTypAffAcc05() ? 1 : 0;
					Integer TP_AFF_ACC_6 = norma.getTypAffAcc06() ? 1 : 0;
					Integer TP_AFF_GR_1 = norma.getTypAffGra01() ? 1 : 0;
					String RM_NUM_REV = norma.getDescrizione();
					String RM_OR_N = norma.getRMOrNum();
					String DES_VALORE_PRODOTTO = norma.getDescrValProdotto();
					String DOC_DESC_REV = norma.getDescrizione2();
					Timestamp DOC_DATA_REV = norma.getDatiComuni().getTimestampCrz();
					String TAS_INCL_ACT = norma.getTasInclDsc();
					String TAS_INCL_ACT_G = norma.getTasInclgDsc();
					String TP_AFF_GR = "";
					Integer FLAG_RMC = norma.getTipologia() == '2' ? 1 : 0;
					Timestamp TIMESTAMP_AGG = norma.getDatiComuni().getTimestampAgg(); //second key part
					Integer AL_FLAG_INF = 0;
					Integer ALSOL_FLAG_INF = 0;
					Integer ALSOL_INF = 0;
					Integer AS_FLAG_INF = 0;
					Integer B_FLAG_INF = 0;
					Integer BI_FLAG_INF = 0;
					Integer C_FLAG_INF = 0;
					Integer C_N_FLAG_INF = 0;
					Integer CA_FLAG_INF = 0;
					Integer CB_FLAG_INF = 0;
					Integer CE_FLAG_INF = 0;
					Integer CO_FLAG_INF = 0;
					Integer CR_FLAG_INF = 0;
					Integer CR_EQ_FLAG_INF = 0;
					Integer CU_FLAG_INF = 0;
					Integer FE_FLAG_INF = 0;
					Integer H_FLAG_INF = 0;
					Integer JF_FLAG_INF = 0;
					Integer MN_FLAG_INF = 0;
					Integer MO_FLAG_INF = 0;
					Integer N_FLAG_INF = 0;
					Integer NB_FLAG_INF = 0;
					Integer NB_TA_FLAG_INF = 0;
					Integer NI_FLAG_INF = 0;
					Integer O_FLAG_INF = 0;
					Integer P_FLAG_INF = 0;
					Integer PB_FLAG_INF = 0;
					Integer PCM_FLAG_INF = 0;
					Integer PRE_FLAG_INF = 0;
					Integer S_FLAG_INF = 0;
					Integer SB_FLAG_INF = 0;
					Integer SI_FLAG_INF = 0;
					Integer SN_FLAG_INF = 0;
					Integer TA_FLAG_INF = 0;
					Integer TI_FLAG_INF = 0;
					Integer V_FLAG_INF = 0;
					Integer W_FLAG_INF = 0;
					Integer XF_FLAG_INF = 0;
					Integer Y_FLAG_INF = 0;
					Integer ZR_FLAG_INF = 0;

					BigDecimal AL_MIN = BigDecimal.ZERO;
					BigDecimal AL_MAX = BigDecimal.ZERO;
					BigDecimal ALSOL_MIN = BigDecimal.ZERO;
					BigDecimal ALSOL_MAX = BigDecimal.ZERO;
					BigDecimal AS_MIN = BigDecimal.ZERO;
					BigDecimal AS_MAX = BigDecimal.ZERO;
					BigDecimal B_MIN = BigDecimal.ZERO;
					BigDecimal B_MAX = BigDecimal.ZERO;
					BigDecimal BI_MIN = BigDecimal.ZERO;
					BigDecimal BI_MAX = BigDecimal.ZERO;
					BigDecimal C_MIN = BigDecimal.ZERO;
					BigDecimal C_MAX = BigDecimal.ZERO;
					BigDecimal C_N_MIN = BigDecimal.ZERO;
					BigDecimal C_N_MAX = BigDecimal.ZERO;
					BigDecimal CA_MIN = BigDecimal.ZERO;
					BigDecimal CA_MAX = BigDecimal.ZERO;
					BigDecimal CB_MIN = BigDecimal.ZERO;
					BigDecimal CB_MAX = BigDecimal.ZERO;
					BigDecimal CE_MIN = BigDecimal.ZERO;
					BigDecimal CE_MAX = BigDecimal.ZERO;
					BigDecimal CO_MIN = BigDecimal.ZERO;
					BigDecimal CO_MAX = BigDecimal.ZERO;
					BigDecimal CR_MIN = BigDecimal.ZERO;
					BigDecimal CR_MAX = BigDecimal.ZERO;
					BigDecimal CR_EQ_MIN = BigDecimal.ZERO;
					BigDecimal CR_EQ_MAX = BigDecimal.ZERO;
					BigDecimal CU_MIN = BigDecimal.ZERO;
					BigDecimal CU_MAX = BigDecimal.ZERO;
					BigDecimal FE_MIN = BigDecimal.ZERO;
					BigDecimal FE_MAX = BigDecimal.ZERO;
					BigDecimal H_MIN = BigDecimal.ZERO;
					BigDecimal H_MAX = BigDecimal.ZERO;
					BigDecimal H_PPM_O_PERC = BigDecimal.ZERO;
					BigDecimal JF_MIN = BigDecimal.ZERO;
					BigDecimal JF_MAX = BigDecimal.ZERO;
					BigDecimal MN_MIN = BigDecimal.ZERO;
					BigDecimal MN_MAX = BigDecimal.ZERO;
					BigDecimal MO_MIN = BigDecimal.ZERO;
					BigDecimal MO_MAX = BigDecimal.ZERO;
					BigDecimal N_MIN = BigDecimal.ZERO;
					BigDecimal N_MAX = BigDecimal.ZERO;
					BigDecimal N_PPM_O_PERC = BigDecimal.ZERO;
					BigDecimal NB_MIN = BigDecimal.ZERO;
					BigDecimal NB_MAX = BigDecimal.ZERO;
					BigDecimal NB_TA_MIN = BigDecimal.ZERO;
					BigDecimal NB_TA_MAX = BigDecimal.ZERO;
					BigDecimal NI_MIN = BigDecimal.ZERO;
					BigDecimal NI_MAX = BigDecimal.ZERO;
					BigDecimal O_MIN = BigDecimal.ZERO;
					BigDecimal O_MAX = BigDecimal.ZERO;
					BigDecimal O_PPM_O_PERC = BigDecimal.ZERO;
					BigDecimal P_MIN = BigDecimal.ZERO;
					BigDecimal P_MAX = BigDecimal.ZERO;
					BigDecimal PB_MIN = BigDecimal.ZERO;
					BigDecimal PB_MAX = BigDecimal.ZERO;
					BigDecimal PCM_MIN = BigDecimal.ZERO;
					BigDecimal PCM_MAX = BigDecimal.ZERO;
					BigDecimal PRE_MIN = BigDecimal.ZERO;
					BigDecimal PRE_MAX = BigDecimal.ZERO;
					BigDecimal S_MIN = BigDecimal.ZERO;
					BigDecimal S_MAX = BigDecimal.ZERO;
					BigDecimal SB_MIN = BigDecimal.ZERO;
					BigDecimal SB_MAX = BigDecimal.ZERO;
					BigDecimal SI_MIN = BigDecimal.ZERO;
					BigDecimal SI_MAX = BigDecimal.ZERO;
					BigDecimal SN_MIN = BigDecimal.ZERO;
					BigDecimal SN_MAX = BigDecimal.ZERO;
					BigDecimal TA_MIN = BigDecimal.ZERO;
					BigDecimal TA_MAX = BigDecimal.ZERO;
					BigDecimal TI_MIN = BigDecimal.ZERO;
					BigDecimal TI_MAX = BigDecimal.ZERO;
					BigDecimal V_MIN = BigDecimal.ZERO;
					BigDecimal V_MAX = BigDecimal.ZERO;
					BigDecimal W_MIN = BigDecimal.ZERO;
					BigDecimal W_MAX = BigDecimal.ZERO;
					BigDecimal XF_MIN = BigDecimal.ZERO;
					BigDecimal XF_MAX = BigDecimal.ZERO;
					BigDecimal Y_MIN = BigDecimal.ZERO;
					BigDecimal Y_MAX = BigDecimal.ZERO;
					BigDecimal ZR_MIN = BigDecimal.ZERO;
					BigDecimal ZR_MAX = BigDecimal.ZERO;
					String AC_FORMULA_1 = null;
					String AC_FORMULA_2 = null;
					String AC_FORMULA_3 = null;
					String AC_FORMULA_4 = null;
					String AC_FORMULA_5 = null;
					BigDecimal AC_FORMULA_1_MAX = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_1_MIN = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_2_MAX = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_2_MIN = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_3_MAX = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_3_MIN = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_4_MAX = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_4_MIN = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_5_MAX = BigDecimal.ZERO;
					BigDecimal AC_FORMULA_5_MIN = BigDecimal.ZERO;

					CicloCollaudoTestata ciclo = norma.getAnalisiChimica();
					if(ciclo != null) {
						if(ciclo.getFasi().size() > 0) {
							YCicloCollaudoFase faseAnalisiChimica = (YCicloCollaudoFase) ciclo.getFasi().get(0);
							List caratteristiche = faseAnalisiChimica.getCaratteristiche();
							Iterator iterCarat = caratteristiche.iterator();

							while(iterCarat.hasNext()) {
								YCicloCollaudoCaratt caratteristica = (YCicloCollaudoCaratt) iterCarat.next();
								String descrizioneRidotta = caratteristica.getDescrizioneCicloNLS().getDescrizioneRidotta();
								Integer INF = caratteristica.getYInformativo() ? 1 : 0;
								BigDecimal LIM_SUP_TOL = BigDecimal.ZERO;
								BigDecimal LIM_INF_TOL = BigDecimal.ZERO;

								if (caratteristica.getLimSupTolleranza() != null
										&& (caratteristica.getLimSupTolleranza().compareTo(BigDecimal.valueOf(-9999)) == 0
										|| caratteristica.getLimSupTolleranza().compareTo(BigDecimal.valueOf(9999)) == 0)) {
									LIM_SUP_TOL = null;
								} else {
									LIM_SUP_TOL = caratteristica.getLimSupTolleranza();
								}

								if (caratteristica.getLimInfTolleranza() != null
										&& (caratteristica.getLimInfTolleranza().compareTo(BigDecimal.valueOf(-9999)) == 0
										|| caratteristica.getLimInfTolleranza().compareTo(BigDecimal.valueOf(9999)) == 0)) {
									LIM_INF_TOL = null;
								} else {
									LIM_INF_TOL = caratteristica.getLimInfTolleranza();
								}

								switch (descrizioneRidotta) {
								case "Al":
									AL_FLAG_INF = INF;
									AL_MIN = LIM_INF_TOL;
									AL_MAX = LIM_SUP_TOL;
									break;
								case "Al Sol":
									ALSOL_FLAG_INF = INF;
									ALSOL_MIN = LIM_INF_TOL;
									ALSOL_MAX = LIM_SUP_TOL;
									ALSOL_INF = INF;
									break;
								case "As":
									AS_FLAG_INF = INF;
									AS_MIN = LIM_INF_TOL;
									AS_MAX = LIM_SUP_TOL;
									break;
								case "B":
									B_FLAG_INF = INF;
									B_MIN = LIM_INF_TOL;
									B_MAX = LIM_SUP_TOL;
									break;
								case "Bi":
									BI_FLAG_INF = INF;
									BI_MIN = LIM_INF_TOL;
									BI_MAX = LIM_SUP_TOL;
									break;
								case "C":
									C_FLAG_INF = INF;
									C_MIN = LIM_INF_TOL;
									C_MAX = LIM_SUP_TOL;
									break;
								case "Ca":
									CA_FLAG_INF = INF;
									CA_MIN = LIM_INF_TOL;
									CA_MAX = LIM_SUP_TOL;
									break;
								case "Cb":
									CB_FLAG_INF = INF;
									CB_MIN = LIM_INF_TOL;
									CB_MAX = LIM_SUP_TOL;
									break;
								case "Ceq":
									CE_FLAG_INF = INF;
									CE_MIN = LIM_INF_TOL;
									CE_MAX = LIM_SUP_TOL;
									break;
								case "Co":
									CO_FLAG_INF = INF;
									CO_MIN = LIM_INF_TOL;
									CO_MAX = LIM_SUP_TOL;
									break;
								case "Cr":
									CR_FLAG_INF = INF;
									CR_MIN = LIM_INF_TOL;
									CR_MAX = LIM_SUP_TOL;
									break;
								case "Creq":
									CR_EQ_FLAG_INF = INF;
									CR_EQ_MIN = LIM_INF_TOL;
									CR_EQ_MAX = LIM_SUP_TOL;
									break;
								case "Cu":
									CU_FLAG_INF = INF;
									CU_MIN = LIM_INF_TOL;
									CU_MAX = LIM_SUP_TOL;
									break;
								case "H":
									H_FLAG_INF = INF;
									H_MIN = LIM_INF_TOL;
									H_MAX = LIM_SUP_TOL;
									if(caratteristica.getIdUnitaMisura() != null && 
											(caratteristica.getIdUnitaMisura().equals("%") || caratteristica.getIdUnitaMisura().equals("P%"))) {
										H_PPM_O_PERC = BigDecimal.ONE;
									}
									break;
								case "J Fact":
									JF_FLAG_INF = INF;
									JF_MIN = LIM_INF_TOL;
									JF_MAX = LIM_SUP_TOL;
									break;
								case "Mn":
									MN_FLAG_INF = INF;
									MN_MIN = LIM_INF_TOL;
									MN_MAX = LIM_SUP_TOL;
									break;
								case "Mo":
									MO_FLAG_INF = INF;
									MO_MIN = LIM_INF_TOL;
									MO_MAX = LIM_SUP_TOL;
									break;
								case "N":
									N_FLAG_INF = INF;
									N_MIN = LIM_INF_TOL;
									N_MAX = LIM_SUP_TOL;
									if(caratteristica.getIdUnitaMisura() != null && 
											(caratteristica.getIdUnitaMisura().equals("%") || caratteristica.getIdUnitaMisura().equals("P%"))) {
										N_PPM_O_PERC = BigDecimal.ONE;
									}
									break;
								case "Nb":
									NB_FLAG_INF = INF;
									NB_MIN = LIM_INF_TOL;
									NB_MAX = LIM_SUP_TOL;
									break;
								case "Ni":
									NI_FLAG_INF = INF;
									NI_MIN = LIM_INF_TOL;
									NI_MAX = LIM_SUP_TOL;
									break;
								case "O":
									O_FLAG_INF = INF;
									O_MIN = LIM_INF_TOL;
									O_MAX = LIM_SUP_TOL;
									if(caratteristica.getIdUnitaMisura() != null && 
											(caratteristica.getIdUnitaMisura().equals("%") || caratteristica.getIdUnitaMisura().equals("P%"))) {
										O_PPM_O_PERC = BigDecimal.ONE;
									}
									break;
								case "P":
									P_FLAG_INF = INF;
									P_MIN = LIM_INF_TOL;
									P_MAX = LIM_SUP_TOL;
									break;
								case "Pb":
									PB_FLAG_INF = INF;
									PB_MIN = LIM_INF_TOL;
									PB_MAX = LIM_SUP_TOL;
									break;
								case "PCM":
									PCM_FLAG_INF = INF;
									PCM_MIN = LIM_INF_TOL;
									PCM_MAX = LIM_SUP_TOL;
									break;
								case "Pre":
									PRE_FLAG_INF = INF;
									PRE_MIN = LIM_INF_TOL;
									PRE_MAX = LIM_SUP_TOL;
									break;
								case "S":
									S_FLAG_INF = INF;
									S_MIN = LIM_INF_TOL;
									S_MAX = LIM_SUP_TOL;
									break;
								case "Sb":
									SB_FLAG_INF = INF;
									SB_MIN = LIM_INF_TOL;
									SB_MAX = LIM_SUP_TOL;
									break;
								case "Si":
									SI_FLAG_INF = INF;
									SI_MIN = LIM_INF_TOL;
									SI_MAX = LIM_SUP_TOL;
									break;
								case "Sn":
									SN_FLAG_INF = INF;
									SN_MIN = LIM_INF_TOL;
									SN_MAX = LIM_SUP_TOL;
									break;
								case "Ta":
									TA_FLAG_INF = INF;
									TA_MIN = LIM_INF_TOL;
									TA_MAX = LIM_SUP_TOL;
									break;
								case "Ti":
									TI_FLAG_INF = INF;
									TI_MIN = LIM_INF_TOL;
									TI_MAX = LIM_SUP_TOL;
									break;
								case "V":
									V_FLAG_INF = INF;
									V_MIN = LIM_INF_TOL;
									V_MAX = LIM_SUP_TOL;
									break;
								case "W":
									W_FLAG_INF = INF;
									W_MIN = LIM_INF_TOL;
									W_MAX = LIM_SUP_TOL;
									break;
								case "X Fact":
									XF_FLAG_INF = INF;
									XF_MIN = LIM_INF_TOL;
									XF_MAX = LIM_SUP_TOL;
									break;
								case "Zr":
									ZR_FLAG_INF = INF;
									ZR_MIN = LIM_INF_TOL;
									ZR_MAX = LIM_SUP_TOL;
									break;
								case "C + N":
									C_N_FLAG_INF = INF;
									C_N_MIN = LIM_INF_TOL;
									C_N_MAX = LIM_SUP_TOL;
									break;
								case "Fe":
									FE_FLAG_INF = INF;
									FE_MIN = LIM_INF_TOL;
									FE_MAX = LIM_SUP_TOL;
									break;
								case "Nb + Ta":
									NB_TA_FLAG_INF = INF;
									NB_TA_MIN = LIM_INF_TOL;
									NB_TA_MAX = LIM_SUP_TOL;
									break;
								case "Y":
									Y_FLAG_INF = INF;
									Y_MIN = LIM_INF_TOL;
									Y_MAX = LIM_SUP_TOL;
									break;
								default:
									if(caratteristica.getYFormula() != null &&  !caratteristica.getYFormula().isEmpty()) {
										if(AC_FORMULA_1 == null) {
											AC_FORMULA_1 = caratteristica.getYFormula();
											AC_FORMULA_1_MIN = LIM_INF_TOL;
											AC_FORMULA_1_MAX = LIM_SUP_TOL;
										}
										if(AC_FORMULA_2 == null) {
											AC_FORMULA_2 = caratteristica.getYFormula();
											AC_FORMULA_2_MIN = LIM_INF_TOL;
											AC_FORMULA_2_MAX = LIM_SUP_TOL;
										}
										if(AC_FORMULA_3 == null) {
											AC_FORMULA_3 = caratteristica.getYFormula();
											AC_FORMULA_3_MIN = LIM_INF_TOL;
											AC_FORMULA_3_MAX = LIM_SUP_TOL;
										}
										if(AC_FORMULA_4 == null) {
											AC_FORMULA_4 = caratteristica.getYFormula();
											AC_FORMULA_4_MIN = LIM_INF_TOL;
											AC_FORMULA_4_MAX = LIM_SUP_TOL;
										}
										if(AC_FORMULA_5 == null) {
											AC_FORMULA_5 = caratteristica.getYFormula();
											AC_FORMULA_5_MIN = LIM_INF_TOL;
											AC_FORMULA_5_MAX = LIM_SUP_TOL;
										}

									}
									break;
								}
							}
						}
					}

					//creare lo statement e metterlo nella list
					String stmt = "INSERT"
							+ "	INTO"
							+ "	dbo.YPT_QC_ANALISI_RM (ALLIAS_ACCIAIO,"
							+ "	ACCIAIO,"
							+ "	TP_FUS_ACC,"
							+ "	DOC_REV_APPR,"
							+ "	DOC_REV_PREP,"
							+ "	ID,"
							+ "	NOTE,"
							+ "	NOTE_2,"
							+ "	NOTE_3,"
							+ "	FLAG_PR_SPE,"
							+ "	DOC_REV,"
							+ "	RM_NUM,"
							+ "	STD_ACC,"
							+ "	STD_ACC_2,"
							+ "	STD_ACC_3,"
							+ "	STD_ACC_4,"
							+ "	STD_ACC_5,"
							+ "	STD_ACC_6,"
							+ "	STD_ACC_DES,"
							+ "	STD_ACC_DES_2,"
							+ "	STD_ACC_DES_3,"
							+ "	STD_ACC_DES_4,"
							+ "	STD_ACC_DES_5,"
							+ "	STD_ACC_DES_6,"
							+ "	TAS_INCL_A,"
							+ "	TAS_INCL_B,"
							+ "	TAS_INCL_C,"
							+ "	TAS_INCL_D,"
							+ "	TAS_INCL_A_G,"
							+ "	TAS_INCL_B_G,"
							+ "	TAS_INCL_C_G,"
							+ "	TAS_INCL_D_G,"
							+ "	TAS_INCL_NOTE_1,"
							+ "	TAS_INCL_NOTE_2,"
							+ "	TAS_INCL_NOTE_3,"
							+ "	DOC_RICH,"
							+ "	TP_AFF_ACC,"
							+ "	TP_AFF_ACC_2,"
							+ "	TP_AFF_ACC_3,"
							+ "	TP_AFF_ACC_4,"
							+ "	TP_AFF_ACC_5,"
							+ "	TP_AFF_ACC_6,"
							+ "	TP_AFF_GR_1,"
							+ "	RM_NUM_REV,"
							+ "	RM_OR_N,"
							+ "	DES_VALORE_PRODOTTO,"
							+ "	DOC_DESC_REV,"
							+ "	DOC_DATA_REV,"
							+ "	TAS_INCL_ACT,"
							+ "	TAS_INCL_ACT_G,"
							+ "	TP_AFF_GR,"
							+ "	FLAG_RMC,"
							+ "	TIMESTAMP_AGG,"
							+ "	AL_FLAG_INF,"
							+ "	ALSOL_FLAG_INF,"
							+ "	AL_MIN,"
							+ "	AL_MAX,"
							+ "	ALSOL_INF,"
							+ "	ALSOL_MIN,"
							+ "	ALSOL_MAX,"
							+ "	AS_FLAG_INF,"
							+ "	AS_MIN,"
							+ "	AS_MAX,"
							+ "	B_FLAG_INF,"
							+ "	B_MIN,"
							+ "	B_MAX,"
							+ "	BI_FLAG_INF,"
							+ "	BI_MIN,"
							+ "	BI_MAX,"
							+ "	C_FLAG_INF,"
							+ "	C_MIN,"
							+ "	C_MAX,"
							+ "	C_N_FLAG_INF,"
							+ "	C_N_MIN,"
							+ "	C_N_MAX,"
							+ "	CA_FLAG_INF,"
							+ "	CA_MIN,"
							+ "	CA_MAX,"
							+ "	CB_FLAG_INF,"
							+ "	CB_MIN,"
							+ "	CB_MAX,"
							+ "	CE_FLAG_INF,"
							+ "	CE_MIN,"
							+ "	CE_MAX,"
							+ "	CO_FLAG_INF,"
							+ "	CO_MIN,"
							+ "	CO_MAX,"
							+ "	CR_FLAG_INF,"
							+ "	CR_MIN,"
							+ "	CR_MAX,"
							+ "	CR_EQ_FLAG_INF,"
							+ "	CR_EQ_MIN,"
							+ "	CR_EQ_MAX,"
							+ "	CU_FLAG_INF,"
							+ "	CU_MIN,"
							+ "	CU_MAX,"
							+ "	FE_FLAG_INF,"
							+ "	FE_MIN,"
							+ "	FE_MAX,"
							+ "	H_FLAG_INF,"
							+ "	H_MIN,"
							+ "	H_MAX,"
							+ "	H_PPM_O_PERC,"
							+ "	JF_FLAG_INF,"
							+ "	JF_MIN,"
							+ "	JF_MAX,"
							+ "	MN_FLAG_INF,"
							+ "	MN_MIN,"
							+ "	MN_MAX,"
							+ "	MO_FLAG_INF,"
							+ "	MO_MIN,"
							+ "	MO_MAX,"
							+ "	N_FLAG_INF,"
							+ "	N_MIN,"
							+ "	N_MAX,"
							+ "	N_PPM_O_PERC,"
							+ "	NB_FLAG_INF,"
							+ "	NB_MIN,"
							+ "	NB_MAX,"
							+ "	NB_TA_FLAG_INF,"
							+ "	NB_TA_MIN,"
							+ "	NB_TA_MAX,"
							+ "	NI_FLAG_INF,"
							+ "	NI_MIN,"
							+ "	NI_MAX,"
							+ "	O_FLAG_INF,"
							+ "	O_MIN,"
							+ "	O_MAX,"
							+ "	O_PPM_O_PERC,"
							+ "	P_FLAG_INF,"
							+ "	P_MIN,"
							+ "	P_MAX,"
							+ "	PB_FLAG_INF,"
							+ "	PB_MIN,"
							+ "	PB_MAX,"
							+ "	PCM_FLAG_INF,"
							+ "	PCM_MIN,"
							+ "	PCM_MAX,"
							+ "	PRE_FLAG_INF,"
							+ "	PRE_MIN,"
							+ "	PRE_MAX,"
							+ "	S_FLAG_INF,"
							+ "	S_MIN,"
							+ "	S_MAX,"
							+ "	SB_FLAG_INF,"
							+ "	SB_MIN,"
							+ "	SB_MAX,"
							+ "	SI_FLAG_INF,"
							+ "	SI_MIN,"
							+ "	SI_MAX,"
							+ "	SN_FLAG_INF,"
							+ "	SN_MIN,"
							+ "	SN_MAX,"
							+ "	TA_FLAG_INF,"
							+ "	TA_MIN,"
							+ "	TA_MAX,"
							+ "	TI_FLAG_INF,"
							+ "	TI_MIN,"
							+ "	TI_MAX,"
							+ "	V_FLAG_INF,"
							+ "	V_MIN,"
							+ "	V_MAX,"
							+ "	W_FLAG_INF,"
							+ "	W_MIN,"
							+ "	W_MAX,"
							+ "	XF_FLAG_INF,"
							+ "	XF_MIN,"
							+ "	XF_MAX,"
							+ "	Y_FLAG_INF,"
							+ "	Y_MIN,"
							+ "	Y_MAX,"
							+ "	ZR_FLAG_INF,"
							+ "	ZR_MIN,"
							+ "	ZR_MAX,"
							+ "	AC_FORMULA_1,"
							+ "	AC_FORMULA_1_MAX,"
							+ "	AC_FORMULA_1_MIN,"
							+ "	AC_FORMULA_2,"
							+ "	AC_FORMULA_2_MAX,"
							+ "	AC_FORMULA_2_MIN,"
							+ "	AC_FORMULA_3,"
							+ "	AC_FORMULA_3_MAX,"
							+ "	AC_FORMULA_3_MIN,"
							+ "	AC_FORMULA_4,"
							+ "	AC_FORMULA_4_MAX,"
							+ "	AC_FORMULA_4_MIN,"
							+ "	AC_FORMULA_5,"
							+ "	AC_FORMULA_5_MAX,"
							+ "	AC_FORMULA_5_MIN,"
							+ "	ELABORATO) ";

					stmt = stmt +
							"VALUES (" +
							"'" + ALIAS_ACCIAIO + "', " +
							"'" + ACCIAO + "', " +
							TP_FUS_ACC + ", " +
							"'" + DOC_REV_APPR + "', " +
							"'" + DOC_REV_PREP + "', " +
							"'" + ID + "', " +
							"'" + NOTE + "', " +
							"'" + NOTE_2 + "', " +
							"'" + NOTE_3 + "', " +
							FLAG_PR_SPE + ", " +
							"'" + DOC_REV + "', " +
							"'" + RM_NUM + "', " +
							"'" + STD_ACC + "', " +
							"'" + STD_ACC_2 + "', " +
							"'" + STD_ACC_3 + "', " +
							"'" + STD_ACC_4 + "', " +
							"'" + STD_ACC_5 + "', " +
							"'" + STD_ACC_6 + "', " +
							"'" + STD_ACC_DES + "', " +
							"'" + STD_ACC_DES_2 + "', " +
							"'" + STD_ACC_DES_3 + "', " +
							"'" + STD_ACC_DES_4 + "', " +
							"'" + STD_ACC_DES_5 + "', " +
							"'" + STD_ACC_DES_6 + "', " +
							"'" + TAS_INCL_A + "', " +
							"'" + TAS_INCL_B + "', " +
							"'" + TAS_INCL_C + "', " +
							"'" + TAS_INCL_D + "', " +
							"'" + TAS_INCL_A_G + "', " +
							"'" + TAS_INCL_B_G + "', " +
							"'" + TAS_INCL_C_G + "', " +
							"'" + TAS_INCL_D_G + "', " +
							"'" + TAS_INCL_NOTE_1 + "', " +
							"'" + TAS_INCL_NOTE_2 + "', " +
							"'" + TAS_INCL_NOTE_3 + "', " +
							DOC_RICH + ", " +
							TP_AFF_ACC + ", " +
							TP_AFF_ACC_2 + ", " +
							TP_AFF_ACC_3 + ", " +
							TP_AFF_ACC_4 + ", " +
							TP_AFF_ACC_5 + ", " +
							TP_AFF_ACC_6 + ", " +
							TP_AFF_GR_1 + ", " +
							"'" + RM_NUM_REV + "', " +
							"'" + RM_OR_N + "', " +
							"'" + DES_VALORE_PRODOTTO + "', " +
							"'" + DOC_DESC_REV + "', " +
							"'" + DOC_DATA_REV + "', " +
							"'" + TAS_INCL_ACT + "', " +
							"'" + TAS_INCL_ACT_G + "', " +
							"'" + TP_AFF_GR + "', " +
							FLAG_RMC + ", " +
							"'" + TIMESTAMP_AGG + "', " +
							AL_FLAG_INF + ", " +
							ALSOL_FLAG_INF + ", " +
							AL_MIN + ", " +
							AL_MAX + ", " +
							ALSOL_INF + ", " +
							ALSOL_MIN + ", " +
							ALSOL_MAX + ", " +
							AS_FLAG_INF + ", " +
							AS_MIN + ", " +
							AS_MAX + ", " +
							B_FLAG_INF + ", " +
							B_MIN + ", " +
							B_MAX + ", " +
							BI_FLAG_INF + ", " +
							BI_MIN + ", " +
							BI_MAX + ", " +
							C_FLAG_INF + ", " +
							C_MIN + ", " +
							C_MAX + ", " +
							C_N_FLAG_INF + ", " +
							C_N_MIN + ", " +
							C_N_MAX + ", " +
							CA_FLAG_INF + ", " +
							CA_MIN + ", " +
							CA_MAX + ", " +
							CB_FLAG_INF + ", " +
							CB_MIN + ", " +
							CB_MAX + ", " +
							CE_FLAG_INF + ", " +
							CE_MIN + ", " +
							CE_MAX + ", " +
							CO_FLAG_INF + ", " +
							CO_MIN + ", " +
							CO_MAX + ", " +
							CR_FLAG_INF + ", " +
							CR_MIN + ", " +
							CR_MAX + ", " +
							CR_EQ_FLAG_INF + ", " +
							CR_EQ_MIN + ", " +
							CR_EQ_MAX + ", " +
							CU_FLAG_INF + ", " +
							CU_MIN + ", " +
							CU_MAX + ", " +
							FE_FLAG_INF + ", " +
							FE_MIN + ", " +
							FE_MAX + ", " +
							H_FLAG_INF + ", " +
							H_MIN + ", " +
							H_MAX + ", " +
							H_PPM_O_PERC + ", " +
							JF_FLAG_INF + ", " +
							JF_MIN + ", " +
							JF_MAX + ", " +
							MN_FLAG_INF + ", " +
							MN_MIN + ", " +
							MN_MAX + ", " +
							MO_FLAG_INF + ", " +
							MO_MIN + ", " +
							MO_MAX + ", " +
							N_FLAG_INF + ", " +
							N_MIN + ", " +
							N_MAX + ", " +
							N_PPM_O_PERC + ", " +
							NB_FLAG_INF + ", " +
							NB_MIN + ", " +
							NB_MAX + ", " +
							NB_TA_FLAG_INF + ", " +
							NB_TA_MIN + ", " +
							NB_TA_MAX + ", " +
							NI_FLAG_INF + ", " +
							NI_MIN + ", " +
							NI_MAX + ", " +
							O_FLAG_INF + ", " +
							O_MIN + ", " +
							O_MAX + ", " +
							O_PPM_O_PERC + ", " +
							P_FLAG_INF + ", " +
							P_MIN + ", " +
							P_MAX + ", " +
							PB_FLAG_INF + ", " +
							PB_MIN + ", " +
							PB_MAX + ", " +
							PCM_FLAG_INF + ", " +
							PCM_MIN + ", " +
							PCM_MAX + ", " +
							PRE_FLAG_INF + ", " +
							PRE_MIN + ", " +
							PRE_MAX + ", " +
							S_FLAG_INF + ", " +
							S_MIN + ", " +
							S_MAX + ", " +
							SB_FLAG_INF + ", " +
							SB_MIN + ", " +
							SB_MAX + ", " +
							SI_FLAG_INF + ", " +
							SI_MIN + ", " +
							SI_MAX + ", " +
							SN_FLAG_INF + ", " +
							SN_MIN + ", " +
							SN_MAX + ", " +
							TA_FLAG_INF + ", " +
							TA_MIN + ", " +
							TA_MAX + ", " +
							TI_FLAG_INF + ", " +
							TI_MIN + ", " +
							TI_MAX + ", " +
							V_FLAG_INF + ", " +
							V_MIN + ", " +
							V_MAX + ", " +
							W_FLAG_INF + ", " +
							W_MIN + ", " +
							W_MAX + ", " +
							XF_FLAG_INF + ", " +
							XF_MIN + ", " +
							XF_MAX + ", " +
							Y_FLAG_INF + ", " +
							Y_MIN + ", " +
							Y_MAX + ", " +
							ZR_FLAG_INF + ", " +
							ZR_MIN + ", " +
							ZR_MAX + ", " +
							"'" + AC_FORMULA_1 + "', " +
							AC_FORMULA_1_MAX + ", " +
							AC_FORMULA_1_MIN + ", " +
							"'" + AC_FORMULA_2 + "', " +
							AC_FORMULA_2_MAX + ", " +
							AC_FORMULA_2_MIN + ", " +
							"'" + AC_FORMULA_3 + "', " +
							AC_FORMULA_3_MAX + ", " +
							AC_FORMULA_3_MIN + ", " +
							"'" + AC_FORMULA_4 + "', " +
							AC_FORMULA_4_MAX + ", " +
							AC_FORMULA_4_MIN + ", " +
							"'" + AC_FORMULA_5 + "', " +
							AC_FORMULA_5_MAX + ", " +
							AC_FORMULA_5_MIN + ", " +
							"'0'" + 
							");";

					list.add(stmt);

				}catch (Exception e) {
					output.println(" Norma : {"+norma.getKey()+"} exc : "+e.getMessage());
					e.printStackTrace(Trace.excStream);
				}
			}
		}

		return list;
	}

	public Dipendente getDipendente(String idDipdenente) {
		try {
			return (Dipendente) Dipendente.elementWithKey(Dipendente.class, KeyHelper.buildObjectKey(new String[] {
					Azienda.getAziendaCorrente(),idDipdenente
			}) ,PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected Vector<YNormeQualita> norme() {
		try {
			return (YNormeQualita.retrieveList(YNormeQualita.class, " "+YNormeQualitaTM.ID_AZIENDA+" = '"+Azienda.getAziendaCorrente()+"' ", "", false));
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return null;
	}


	protected void passo2(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ " PantheraTarget.dbo.YPT_QC_ANALISI_RM "
					+ "SET "
					+ " ELABORATO = '0' "
					+ "WHERE "
					+ " ELABORATO is null";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_QC_ANALISI_RM Set Elaborato to 0 if Null "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo3(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE [PantheraTarget].[dbo].[YPT_QC_ANALISI_RM] "
					+ "SET ELABORATO = '0'   "
					+ "WHERE NOT EXISTS (SELECT * FROM Target.dbo.QC_ANALISI_RM WHERE QC_ANALISI_RM.ID = YPT_QC_ANALISI_RM.ID) "
					+ "AND ELABORATO <> '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Riattiva QC_ANALISI_RM "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo4(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	[PantheraTarget].[dbo].[YPT_QC_ANALISI_RM] "
					+ "SET "
					+ "	YPT_QC_ANALISI_RM.ELABORATO = '0' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT "
					+ "		* "
					+ "	from "
					+ "		[PantheraTarget].[dbo].[YPT_ORD_FOR] YPT_ORD_FOR "
					+ "	inner JOIN [PantheraTarget].[dbo].[YPT_DDT_FOR] YPT_DDT_FOR ON "
					+ "		YPT_DDT_FOR.RIFERIMENTO_ANNO_ORD_FOR = YPT_ORD_FOR.ANNO_DOC "
					+ "		and YPT_DDT_FOR.RIFERIMENTO_NUM_ORD_FOR = YPT_ORD_FOR.NUM_DOC "
					+ "		and YPT_DDT_FOR.RIFERIMENTO_RIGA_ORD_FOR = YPT_ORD_FOR.NUM_RIGA "
					+ "	WHERE "
					+ "		NOT EXISTS ( "
					+ "		SELECT "
					+ "			* "
					+ "		FROM "
					+ "			Target.DBO.QC_ANALISI_ACCIAIERIA "
					+ "		WHERE "
					+ "			QC_ANALISI_ACCIAIERIA.ENTRATA = YPT_DDT_FOR.LOTTO) "
					+ "		AND YPT_QC_ANALISI_RM.ID = YPT_ORD_FOR.ID)";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Riattiva QC_ANALISI_RM 2 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo5(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	PantheraTarget.dbo.YPT_QC_ANALISI_RM "
					+ "SET "
					+ "	ELABORATO = 'X' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "		SELECT QRY_YPT_QC_ANALISI_RM.ID, "
					+ "		QRY_YPT_QC_ANALISI_RM.TIMESTAMP_AGG "
					+ "	FROM "
					+ "		PantheraTarget.dbo.YPT_QC_ANALISI_RM QRY_YPT_QC_ANALISI_RM "
					+ "	INNER JOIN ( "
					+ "			SELECT ID , "
					+ "			MAX(TIMESTAMP_AGG) MAX_TIMESTAMP_AGG "
					+ "		FROM "
					+ "			PantheraTarget.dbo.YPT_QC_ANALISI_RM YPT_QC_ANALISI_RM "
					+ "		GROUP BY "
					+ "			ID) MAX_YPT_QC_ANALISI_RM ON "
					+ "		MAX_YPT_QC_ANALISI_RM.ID = QRY_YPT_QC_ANALISI_RM.ID "
					+ "		AND MAX_YPT_QC_ANALISI_RM.MAX_TIMESTAMP_AGG > QRY_YPT_QC_ANALISI_RM.TIMESTAMP_AGG "
					+ "	WHERE "
					+ "		YPT_QC_ANALISI_RM.ID = QRY_YPT_QC_ANALISI_RM.ID "
					+ "		AND YPT_QC_ANALISI_RM.TIMESTAMP_AGG = QRY_YPT_QC_ANALISI_RM.TIMESTAMP_AGG) "
					+ "	AND YPT_QC_ANALISI_RM.ELABORATO = '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_QC_ANALISI_RM Set Elaborato to X "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo6(ConnectionDescriptor cnd) {
		try {
			String stmt = "INSERT INTO [PantheraTarget].[dbo].[YPT_ORD_FOR] ( "
					+ "    NUM_DOC, "
					+ "    ANNO_DOC, "
					+ "    COD_CF, "
					+ "    DATA_DOC, "
					+ "    COD_ART, "
					+ "    DES_RIGA, "
					+ "    NUM_RIGA, "
					+ "    UM_BASE, "
					+ "    QUANT_UM_BASE, "
					+ "    PREZZO, "
					+ "    NOTE_INT, "
					+ "    DATA_CONS_RIGA, "
					+ "    ID, "
					+ "    TIMESTAMP_AGG "
					+ ") "
					+ "SELECT  "
					+ "    NUM_DOC, "
					+ "    ANNO_DOC, "
					+ "    COD_CF, "
					+ "    DATA_DOC, "
					+ "    COD_ART, "
					+ "    DES_RIGA, "
					+ "    NUM_RIGA, "
					+ "    UM_BASE, "
					+ "    QUANT_UM_BASE, "
					+ "    PREZZO, "
					+ "    NOTE_INT, "
					+ "    DATA_CONS_RIGA, "
					+ "    ID, "
					+ "    TIMESTAMP_AGG "
					+ "FROM MAME.VW_ORD_FOR; "
					+ "";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : INSERT INTO [PantheraTarget].[dbo].[YPT_ORD_FOR] "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo8(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ " PantheraTarget.dbo.YPT_ORD_FOR "
					+ "SET "
					+ " ELABORATO = '0' "
					+ "WHERE "
					+ " ELABORATO is null";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_ORD_FOR Set Elaborato to 0 if Null "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo9(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE [PantheraTarget].[dbo].[YPT_ORD_FOR] "
					+ "SET ELABORATO = '0' "
					+ "WHERE NOT EXISTS (SELECT * FROM Target.dbo.ORD_FOR WHERE ORD_FOR.ANNO_DOC = YPT_ORD_FOR.ANNO_DOC AND ORD_FOR.NUM_DOC = YPT_ORD_FOR.NUM_DOC) "
					+ "AND ELABORATO <> '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Riattiva ORD_FOR "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo10(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	[PantheraTarget].[dbo].[YPT_ORD_FOR] "
					+ "SET "
					+ "	ELABORATO = '0' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT "
					+ "		* "
					+ "	from "
					+ "		[PantheraTarget].[dbo].[YPT_DDT_FOR] YPT_DDT_FOR "
					+ "	WHERE "
					+ "		YPT_DDT_FOR.RIFERIMENTO_ANNO_ORD_FOR = YPT_ORD_FOR.ANNO_DOC "
					+ "		and YPT_DDT_FOR.RIFERIMENTO_NUM_ORD_FOR = YPT_ORD_FOR.NUM_DOC "
					+ "		and YPT_DDT_FOR.RIFERIMENTO_RIGA_ORD_FOR = YPT_ORD_FOR.NUM_RIGA "
					+ "		and NOT EXISTS ( "
					+ "		SELECT "
					+ "			* "
					+ "		FROM "
					+ "			Target.DBO.QC_ANALISI_ACCIAIERIA "
					+ "		WHERE "
					+ "			QC_ANALISI_ACCIAIERIA.ENTRATA = YPT_DDT_FOR.LOTTO))";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Riattiva ORD_FOR 1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo11(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	PantheraTarget.dbo.YPT_ORD_FOR "
					+ "SET "
					+ "	ELABORATO = 'X' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT "
					+ "		QRY_YPT_ORD_FOR.NUM_DOC, "
					+ "		QRY_YPT_ORD_FOR.ANNO_DOC, "
					+ "		QRY_YPT_ORD_FOR.NUM_RIGA, "
					+ "		QRY_YPT_ORD_FOR.TIMESTAMP_AGG "
					+ "	FROM "
					+ "		PantheraTarget.dbo.YPT_ORD_FOR QRY_YPT_ORD_FOR "
					+ "	INNER JOIN ( "
					+ "		SELECT "
					+ "			NUM_DOC , "
					+ "			ANNO_DOC, "
					+ "			NUM_RIGA, "
					+ "			MAX(TIMESTAMP_AGG) MAX_TIMESTAMP_AGG "
					+ "		FROM "
					+ "			PantheraTarget.dbo.YPT_ORD_FOR YPT_ORD_FOR "
					+ "		GROUP BY "
					+ "			NUM_DOC , "
					+ "			ANNO_DOC, "
					+ "			NUM_RIGA) MAX_YPT_ORD_FOR ON "
					+ "		MAX_YPT_ORD_FOR.NUM_DOC = QRY_YPT_ORD_FOR.NUM_DOC "
					+ "		AND MAX_YPT_ORD_FOR.ANNO_DOC = QRY_YPT_ORD_FOR.ANNO_DOC "
					+ "		AND MAX_YPT_ORD_FOR.NUM_RIGA = QRY_YPT_ORD_FOR.NUM_RIGA "
					+ "		AND MAX_YPT_ORD_FOR.MAX_TIMESTAMP_AGG > QRY_YPT_ORD_FOR.TIMESTAMP_AGG "
					+ "	WHERE "
					+ "		YPT_ORD_FOR.NUM_DOC = QRY_YPT_ORD_FOR.NUM_DOC "
					+ "		AND YPT_ORD_FOR.ANNO_DOC = QRY_YPT_ORD_FOR.ANNO_DOC "
					+ "		AND YPT_ORD_FOR.NUM_RIGA = QRY_YPT_ORD_FOR.NUM_RIGA "
					+ "		AND YPT_ORD_FOR.TIMESTAMP_AGG = QRY_YPT_ORD_FOR.TIMESTAMP_AGG) "
					+ "	AND YPT_ORD_FOR.ELABORATO = '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_QC_ANALISI_RM "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo12(ConnectionDescriptor cnd) {
		try {
			String stmt = "INSERT INTO [PantheraTarget].[dbo].[YPT_DDT_FOR] ( "
					+ "    NUM_DDT_INT, "
					+ "    ANNO_DOC, "
					+ "    COD_CF, "
					+ "    DATA_DOC, "
					+ "    CODICE_CAUSALE, "
					+ "    COD_DEP, "
					+ "    COD_DEP_2, "
					+ "    COD_ART, "
					+ "    DES_RIGA, "
					+ "    NUM_RIGA, "
					+ "    UM_ACQ, "
					+ "    QTA_CONSEGNATA, "
					+ "    PREZZO, "
					+ "    LOTTO, "
					+ "    NUM_DDT_FORNITORE, "
					+ "    DATA_DOC_FORNITORE, "
					+ "    LINGOTTO, "
					+ "    RIFERIMENTO_ANNO_ORD_FOR, "
					+ "    RIFERIMENTO_NUM_ORD_FOR, "
					+ "    RIFERIMENTO_RIGA_ORD_FOR, "
					+ "    TIMESTAMP_AGG "
					+ ") "
					+ "SELECT  "
					+ "    NUM_DDT_INT, "
					+ "    ANNO_DOC, "
					+ "    COD_CF, "
					+ "    DATA_DOC, "
					+ "    CODICE_CAUSALE, "
					+ "    COD_DEP, "
					+ "    COD_DEP_2, "
					+ "    COD_ART, "
					+ "    DES_RIGA, "
					+ "    NUM_RIGA, "
					+ "    UM_ACQ, "
					+ "    QTA_CONSEGNATA, "
					+ "    PREZZO, "
					+ "    LOTTO, "
					+ "    NUM_DDT_FORNITORE, "
					+ "    DATA_DOC_FORNITORE, "
					+ "    LINGOTTO, "
					+ "    RIFERIMENTO_ANNO_ORD_FOR, "
					+ "    RIFERIMENTO_NUM_ORD_FOR, "
					+ "    RIFERIMENTO_RIGA_ORD_FOR, "
					+ "    TIMESTAMP_AGG "
					+ "FROM MAME.VW_DDT_FOR; "
					+ "";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_QC_ANALISI_RM "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo13(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ " PantheraTarget.dbo.YPT_DDT_FOR "
					+ "SET "
					+ " ELABORATO = '0' "
					+ "WHERE "
					+ " ELABORATO is null";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_DDT_FOR Set Elaborato to 0 if Null "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo1(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE [PantheraTarget].[dbo].[YPT_DDT_FOR] "
					+ "SET ELABORATO = '0' "
					+ "WHERE NOT EXISTS (SELECT * FROM Target.dbo.DDT_FOR WHERE DDT_FOR.ANNO_DOC = YPT_DDT_FOR.ANNO_DOC AND DDT_FOR.NUM_DOC = YPT_DDT_FOR.NUM_DDT_INT) "
					+ "AND ELABORATO <> '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_QC_ANALISI_RM "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo14(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	[PantheraTarget].[dbo].[YPT_DDT_FOR] "
					+ "SET "
					+ "	ELABORATO = '0' "
					+ "WHERE "
					+ "	NOT EXISTS ( "
					+ "	SELECT "
					+ "		* "
					+ "	FROM "
					+ "		Target.DBO.QC_ANALISI_ACCIAIERIA "
					+ "	WHERE "
					+ "		QC_ANALISI_ACCIAIERIA.ENTRATA = YPT_DDT_FOR.LOTTO) "
					+ "";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Riattiva DDT_FOR 1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo15(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	PantheraTarget.dbo.YPT_DDT_FOR "
					+ "SET "
					+ "	ELABORATO = 'X' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT "
					+ "		QRY_YPT_DDT_FOR.NUM_DDT_INT, "
					+ "		QRY_YPT_DDT_FOR.ANNO_DOC, "
					+ "		QRY_YPT_DDT_FOR.NUM_RIGA, "
					+ "		QRY_YPT_DDT_FOR.TIMESTAMP_AGG "
					+ "	FROM "
					+ "		PantheraTarget.dbo.YPT_DDT_FOR QRY_YPT_DDT_FOR "
					+ "	INNER JOIN ( "
					+ "		SELECT "
					+ "			NUM_DDT_INT , "
					+ "			ANNO_DOC, "
					+ "			NUM_RIGA, "
					+ "			MAX(TIMESTAMP_AGG) MAX_TIMESTAMP_AGG "
					+ "		FROM "
					+ "			PantheraTarget.dbo.YPT_DDT_FOR YPT_DDT_FOR "
					+ "		GROUP BY "
					+ "			NUM_DDT_INT , "
					+ "			ANNO_DOC, "
					+ "			NUM_RIGA) MAX_YPT_DDT_FOR ON "
					+ "		MAX_YPT_DDT_FOR.NUM_DDT_INT = QRY_YPT_DDT_FOR.NUM_DDT_INT "
					+ "		AND MAX_YPT_DDT_FOR.ANNO_DOC = QRY_YPT_DDT_FOR.ANNO_DOC "
					+ "		AND MAX_YPT_DDT_FOR.NUM_RIGA = QRY_YPT_DDT_FOR.NUM_RIGA "
					+ "		AND MAX_YPT_DDT_FOR.MAX_TIMESTAMP_AGG > QRY_YPT_DDT_FOR.TIMESTAMP_AGG "
					+ "	WHERE "
					+ "		YPT_DDT_FOR.NUM_DDT_INT = QRY_YPT_DDT_FOR.NUM_DDT_INT "
					+ "		AND YPT_DDT_FOR.ANNO_DOC = QRY_YPT_DDT_FOR.ANNO_DOC "
					+ "		AND YPT_DDT_FOR.NUM_RIGA = QRY_YPT_DDT_FOR.NUM_RIGA "
					+ "		AND YPT_DDT_FOR.TIMESTAMP_AGG = QRY_YPT_DDT_FOR.TIMESTAMP_AGG) "
					+ "	AND YPT_DDT_FOR.ELABORATO = '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_DDT_FOR Set Elaborato to X "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo16(ConnectionDescriptor cnd) {
		try {
			String stmt = "INSERT INTO [PantheraTarget].[dbo].[YPT_QC_ANALISI_ACCIAIERIA] ( "
					+ "    ENTRATA, "
					+ "    \"DATA\", "
					+ "    COLATA, "
					+ "    ACCIAIERIA, "
					+ "    ID, "
					+ "    ACCIAIO, "
					+ "    ALLIAS_ACCIAIO, "
					+ "    SPECIFICA_1, "
					+ "    ELAB_ACCIAIO, "
					+ "    TIPO_TRAT, "
					+ "    NOTE, "
					+ "    TIMESTAMP_AGG, "
					+ "    AL, "
					+ "    ALSOL, "
					+ "    AS_, "
					+ "    B, "
					+ "    BI, "
					+ "    C, "
					+ "    CA, "
					+ "    CB, "
					+ "    CE, "
					+ "    CO, "
					+ "    CR, "
					+ "    CR_EQ, "
					+ "    CU, "
					+ "    H, "
					+ "    H_PPM_O_PERC, "
					+ "    JF, "
					+ "    MN, "
					+ "    MO, "
					+ "    N, "
					+ "    N_PPM_O_PERC, "
					+ "    NB, "
					+ "    NI, "
					+ "    O, "
					+ "    O_PPM_O_PERC, "
					+ "    P, "
					+ "    PB, "
					+ "    PCM, "
					+ "    PRE, "
					+ "    S, "
					+ "    SB, "
					+ "    SI, "
					+ "    SN, "
					+ "    TA, "
					+ "    TI, "
					+ "    V, "
					+ "    W, "
					+ "    XF, "
					+ "    ZR, "
					+ "    C_N, "
					+ "    FE, "
					+ "    NB_TA, "
					+ "    Y, "
					+ "    AC_FORMULA_1, "
					+ "    AC_FORMULA_1_VALORE, "
					+ "    AC_FORMULA_2, "
					+ "    AC_FORMULA_2_VALORE, "
					+ "    AC_FORMULA_3, "
					+ "    AC_FORMULA_3_VALORE, "
					+ "    AC_FORMULA_4, "
					+ "    AC_FORMULA_4_VALORE, "
					+ "    AC_FORMULA_5, "
					+ "    AC_FORMULA_5_VALORE "
					+ ") "
					+ "SELECT  "
					+ "    ENTRATA, "
					+ "    \"DATA\", "
					+ "    COLATA, "
					+ "    ACCIAIERIA, "
					+ "    ID, "
					+ "    ACCIAIO, "
					+ "    ALLIAS_ACCIAIO, "
					+ "    SPECIFICA_1, "
					+ "    ELAB_ACCIAIO, "
					+ "    TIPO_TRAT, "
					+ "    NOTE, "
					+ "    TIMESTAMP_AGG, "
					+ "    AL, "
					+ "    ALSOL, "
					+ "    AS_, "
					+ "    B, "
					+ "    BI, "
					+ "    C, "
					+ "    CA, "
					+ "    CB, "
					+ "    CE, "
					+ "    CO, "
					+ "    CR, "
					+ "    CR_EQ, "
					+ "    CU, "
					+ "    H, "
					+ "    H_PPM_O_PERC, "
					+ "    JF, "
					+ "    MN, "
					+ "    MO, "
					+ "    N, "
					+ "    N_PPM_O_PERC, "
					+ "    NB, "
					+ "    NI, "
					+ "    O, "
					+ "    O_PPM_O_PERC, "
					+ "    P, "
					+ "    PB, "
					+ "    PCM, "
					+ "    PRE, "
					+ "    S, "
					+ "    SB, "
					+ "    SI, "
					+ "    SN, "
					+ "    TA, "
					+ "    TI, "
					+ "    V, "
					+ "    W, "
					+ "    XF, "
					+ "    ZR, "
					+ "    C_N, "
					+ "    FE, "
					+ "    NB_TA, "
					+ "    Y, "
					+ "    AC_FORMULA_1, "
					+ "    AC_FORMULA_1_VALORE, "
					+ "    AC_FORMULA_2, "
					+ "    AC_FORMULA_2_VALORE, "
					+ "    AC_FORMULA_3, "
					+ "    AC_FORMULA_3_VALORE, "
					+ "    AC_FORMULA_4, "
					+ "    AC_FORMULA_4_VALORE, "
					+ "    AC_FORMULA_5, "
					+ "    AC_FORMULA_5_VALORE "
					+ "FROM MAME.WV_QC_ANALISI_ACCIAIERIA; "
					+ "";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : INSERT INTO [PantheraTarget].[dbo].[YPT_QC_ANALISI_ACCIAIERIA] "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo18(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ " PantheraTarget.dbo.YPT_QC_ANALISI_ACCIAIERIA "
					+ "SET "
					+ " ELABORATO = '0' "
					+ "WHERE "
					+ " ELABORATO is null";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_QC_ANALISI_ACCIAIERIA  Set Elaborato to 0 if Null "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo19(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE [PantheraTarget].[dbo].[YPT_QC_ANALISI_ACCIAIERIA] "
					+ "SET ELABORATO = '0' "
					+ "WHERE NOT EXISTS (SELECT * FROM Target.DBO.QC_ANALISI_ACCIAIERIA WHERE QC_ANALISI_ACCIAIERIA.ENTRATA = YPT_QC_ANALISI_ACCIAIERIA.ENTRATA) "
					+ "AND ELABORATO <> '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Riattiva QC_ANALISI_ACCIAIERIA "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo20(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	[PantheraTarget].[dbo].[YPT_QC_ANALISI_ACCIAIERIA] "
					+ "SET "
					+ "	ELABORATO = '0' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT "
					+ "		* "
					+ "	FROM "
					+ "		[PantheraTarget].[dbo].[YPT_DDT_FOR] YPT_DDT_FOR "
					+ "	WHERE "
					+ "		NOT EXISTS ( "
					+ "		SELECT "
					+ "			* "
					+ "		FROM "
					+ "			Target.DBO.QC_ANALISI_ACCIAIERIA "
					+ "		WHERE "
					+ "			QC_ANALISI_ACCIAIERIA.ENTRATA = YPT_DDT_FOR.LOTTO) "
					+ "		and YPT_DDT_FOR.LOTTO = YPT_QC_ANALISI_ACCIAIERIA.ENTRATA)";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Riattiva QC_ANALISI_ACCIAIERIA 1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo21(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	PantheraTarget.dbo.YPT_QC_ANALISI_ACCIAIERIA "
					+ "SET "
					+ "	ELABORATO = 'X' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT "
					+ "		QRY_YPT_QC_ANALISI_ACCIAIERIA.ENTRATA, "
					+ "		QRY_YPT_QC_ANALISI_ACCIAIERIA.TIMESTAMP_AGG "
					+ "	FROM "
					+ "		PantheraTarget.dbo.YPT_QC_ANALISI_ACCIAIERIA QRY_YPT_QC_ANALISI_ACCIAIERIA "
					+ "	INNER JOIN ( "
					+ "		SELECT "
					+ "			ENTRATA , "
					+ "			MAX(TIMESTAMP_AGG) MAX_TIMESTAMP_AGG "
					+ "		FROM "
					+ "			PantheraTarget.dbo.YPT_QC_ANALISI_ACCIAIERIA YPT_QC_ANALISI_ACCIAIERIA "
					+ "		GROUP BY "
					+ "			ENTRATA) MAX_YPT_QC_ANALISI_ACCIAIERIA ON "
					+ "		MAX_YPT_QC_ANALISI_ACCIAIERIA.ENTRATA = QRY_YPT_QC_ANALISI_ACCIAIERIA.ENTRATA "
					+ "		AND MAX_YPT_QC_ANALISI_ACCIAIERIA.MAX_TIMESTAMP_AGG > QRY_YPT_QC_ANALISI_ACCIAIERIA.TIMESTAMP_AGG "
					+ "	WHERE "
					+ "		YPT_QC_ANALISI_ACCIAIERIA.ENTRATA = QRY_YPT_QC_ANALISI_ACCIAIERIA.ENTRATA "
					+ "		AND YPT_QC_ANALISI_ACCIAIERIA.TIMESTAMP_AGG = QRY_YPT_QC_ANALISI_ACCIAIERIA.TIMESTAMP_AGG) "
					+ "	AND YPT_QC_ANALISI_ACCIAIERIA.ELABORATO = '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_QC_ANALISI_ACCIAIERIA Set Elaborato to X "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo22(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ "	[PantheraTarget].[dbo].[YPT_DDT_FOR] "
					+ "SET "
					+ "	ELABORATO = '0' "
					+ "WHERE "
					+ "	EXISTS ( "
					+ "	SELECT * "
					+ "  FROM [PantheraTarget].[dbo].[YPT_QC_ANALISI_ACCIAIERIA] "
					+ "	WHERE "
					+ "		[YPT_QC_ANALISI_ACCIAIERIA].ENTRATA = YPT_DDT_FOR.LOTTO "
					+ "		AND ELABORATO = '0')";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : Riattiva DDT_FOR 1 1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void passo23(ConnectionDescriptor cnd) {
		try {
			String stmt = "UPDATE "
					+ " PantheraTarget.dbo.YPT_DDT_FOR "
					+ "SET "
					+ " ELABORATO = 'X' "
					+ "WHERE "
					+ " EXISTS ( "
					+ " SELECT "
					+ "  QRY_YPT_DDT_FOR.NUM_DDT_INT, "
					+ "  QRY_YPT_DDT_FOR.ANNO_DOC, "
					+ "  QRY_YPT_DDT_FOR.NUM_RIGA, "
					+ "  QRY_YPT_DDT_FOR.TIMESTAMP_AGG "
					+ " FROM "
					+ "  PantheraTarget.dbo.YPT_DDT_FOR QRY_YPT_DDT_FOR "
					+ " INNER JOIN ( "
					+ "  SELECT "
					+ "   NUM_DDT_INT , "
					+ "   ANNO_DOC, "
					+ "   NUM_RIGA, "
					+ "   MAX(TIMESTAMP_AGG) MAX_TIMESTAMP_AGG "
					+ "  FROM "
					+ "   PantheraTarget.dbo.YPT_DDT_FOR YPT_DDT_FOR "
					+ "  GROUP BY "
					+ "   NUM_DDT_INT , "
					+ "   ANNO_DOC, "
					+ "   NUM_RIGA) MAX_YPT_DDT_FOR ON "
					+ "  MAX_YPT_DDT_FOR.NUM_DDT_INT = QRY_YPT_DDT_FOR.NUM_DDT_INT "
					+ "  AND MAX_YPT_DDT_FOR.ANNO_DOC = QRY_YPT_DDT_FOR.ANNO_DOC "
					+ "  AND MAX_YPT_DDT_FOR.NUM_RIGA = QRY_YPT_DDT_FOR.NUM_RIGA "
					+ "  AND MAX_YPT_DDT_FOR.MAX_TIMESTAMP_AGG > QRY_YPT_DDT_FOR.TIMESTAMP_AGG "
					+ " WHERE "
					+ "  YPT_DDT_FOR.NUM_DDT_INT = QRY_YPT_DDT_FOR.NUM_DDT_INT "
					+ "  AND YPT_DDT_FOR.ANNO_DOC = QRY_YPT_DDT_FOR.ANNO_DOC "
					+ "  AND YPT_DDT_FOR.NUM_RIGA = QRY_YPT_DDT_FOR.NUM_RIGA "
					+ "  AND YPT_DDT_FOR.TIMESTAMP_AGG = QRY_YPT_DDT_FOR.TIMESTAMP_AGG) "
					+ " AND YPT_DDT_FOR.ELABORATO = '0'";
			int rc = YDtsxPtExpOrdEsecRunner.eseguiUpdateSuDbExt(stmt,cnd);
			if(rc > 0)
				cnd.commit();
			else
				cnd.rollback();
			output.println(" Dtsx-block : YPT_DDT_FOR Set Elaborato to X 1 "+(rc > 0 ? "esito 1" : "esito 0"));
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	@Override
	protected String getClassAdCollectionName() {
		return "YDtsxRmOdaDDTRu";
	}

}
