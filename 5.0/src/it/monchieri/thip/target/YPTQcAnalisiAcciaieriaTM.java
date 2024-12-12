package it.monchieri.thip.target;

import java.sql.SQLException;

import com.thera.thermfw.base.SystemParam;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.TableManager;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * Table manager for YPT_QC_ANALISI_ACCIAIERIA
 * <br><br>
 * <b>YYYYMMDD    Your Name    Creation</b>
 * <p></p>
 */

public class YPTQcAnalisiAcciaieriaTM extends TableManager {

	public static final String ENTRATA = "ENTRATA";
	public static final String DATA = "DATA";
	public static final String COLATA = "COLATA";
	public static final String ACCIAIERIA = "ACCIAIERIA";
	public static final String ID = "ID";
	public static final String ACCIAIO = "ACCIAIO";
	public static final String ALLIAS_ACCIAIO = "ALLIAS_ACCIAIO";
	public static final String SPECIFICA_1 = "SPECIFICA_1";
	public static final String ELAB_ACCIAIO = "ELAB_ACCIAIO";
	public static final String TIPO_TRAT = "TIPO_TRAT";
	public static final String NOTE = "NOTE";
	public static final String TIMESTAMP_AGG = "TIMESTAMP_AGG";
	public static final String AL = "AL";
	public static final String ALSOL = "ALSOL";
	public static final String AS_ = "AS_";
	public static final String B = "B";
	public static final String BI = "BI";
	public static final String C = "C";
	public static final String CA = "CA";
	public static final String CB = "CB";
	public static final String CE = "CE";
	public static final String CO = "CO";
	public static final String CR = "CR";
	public static final String CR_EQ = "CR_EQ";
	public static final String CU = "CU";
	public static final String H = "H";
	public static final String H_PPM_O_PERC = "H_PPM_O_PERC";
	public static final String JF = "JF";
	public static final String MN = "MN";
	public static final String MO = "MO";
	public static final String N = "N";
	public static final String N_PPM_O_PERC = "N_PPM_O_PERC";
	public static final String NB = "NB";
	public static final String NI = "NI";
	public static final String O = "O";
	public static final String O_PPM_O_PERC = "O_PPM_O_PERC";
	public static final String P = "P";
	public static final String PB = "PB";
	public static final String PCM = "PCM";
	public static final String PRE = "PRE";
	public static final String S = "S";
	public static final String SB = "SB";
	public static final String SI = "SI";
	public static final String SN = "SN";
	public static final String TA = "TA";
	public static final String TI = "TI";
	public static final String V = "V";
	public static final String W = "W";
	public static final String XF = "XF";
	public static final String ZR = "ZR";
	public static final String C_N = "C_N";
	public static final String FE = "FE";
	public static final String NB_TA = "NB_TA";
	public static final String Y = "Y";
	public static final String AC_FORMULA_1 = "AC_FORMULA_1";
	public static final String AC_FORMULA_1_VALORE = "AC_FORMULA_1_VALORE";
	public static final String AC_FORMULA_2 = "AC_FORMULA_2";
	public static final String AC_FORMULA_2_VALORE = "AC_FORMULA_2_VALORE";
	public static final String AC_FORMULA_3 = "AC_FORMULA_3";
	public static final String AC_FORMULA_3_VALORE = "AC_FORMULA_3_VALORE";
	public static final String AC_FORMULA_4 = "AC_FORMULA_4";
	public static final String AC_FORMULA_4_VALORE = "AC_FORMULA_4_VALORE";
	public static final String AC_FORMULA_5 = "AC_FORMULA_5";
	public static final String AC_FORMULA_5_VALORE = "AC_FORMULA_5_VALORE";
	public static final String ELABORATO = "ELABORATO";

	public static final String TABLE_NAME = SystemParam.getSchema("dbo") + "YPT_QC_ANALISI_ACCIAIERIA";
	private static final String CLASS_NAME = YPTQcAnalisiAcciaieria.class.getName();

	private static TableManager cInstance;

	public synchronized static TableManager getInstance() throws SQLException {
		if (cInstance == null) {
			cInstance = (TableManager) Factory.createObject(YPTQcAnalisiAcciaieriaTM.class);
		}
		return cInstance;
	}

	public YPTQcAnalisiAcciaieriaTM() throws SQLException {
		super();
	}

	protected void initialize() throws SQLException {
		setTableName(TABLE_NAME);
		setObjClassName(CLASS_NAME);
		init();
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();

		addAttribute("Entrata", ENTRATA);
		addAttribute("Data", DATA);
		addAttribute("Colata", COLATA);
		addAttribute("Acciaieria", ACCIAIERIA);
		addAttribute("Id", ID);
		addAttribute("Acciaio", ACCIAIO);
		addAttribute("AlliasAcciaio", ALLIAS_ACCIAIO);
		addAttribute("Specifica1", SPECIFICA_1);
		addAttribute("ElabAcciaio", ELAB_ACCIAIO);
		addAttribute("TipoTrat", TIPO_TRAT);
		addAttribute("Note", NOTE);
		addAttribute("TimestampAgg", TIMESTAMP_AGG);
		addAttribute("Al", AL);
		addAttribute("Alsol", ALSOL);
		addAttribute("As_", AS_);
		addAttribute("B_", B);
		addAttribute("Bi", BI);
		addAttribute("C_", C);
		addAttribute("Ca", CA);
		addAttribute("Cb", CB);
		addAttribute("Ce", CE);
		addAttribute("Co", CO);
		addAttribute("Cr", CR);
		addAttribute("CrEq", CR_EQ);
		addAttribute("Cu", CU);
		addAttribute("H_", H);
		addAttribute("HPpmOPerc", H_PPM_O_PERC);
		addAttribute("Jf", JF);
		addAttribute("Mn", MN);
		addAttribute("Mo", MO);
		addAttribute("N_", N);
		addAttribute("NPpmOPerc", N_PPM_O_PERC);
		addAttribute("Nb", NB);
		addAttribute("Ni", NI);
		addAttribute("O_", O);
		addAttribute("OPpmOPerc", O_PPM_O_PERC);
		addAttribute("P_", P);
		addAttribute("Pb", PB);
		addAttribute("Pcm", PCM);
		addAttribute("Pre", PRE);
		addAttribute("S_", S);
		addAttribute("Sb", SB);
		addAttribute("Si", SI);
		addAttribute("Sn", SN);
		addAttribute("Ta", TA);
		addAttribute("Ti", TI);
		addAttribute("V_", V);
		addAttribute("W_", W);
		addAttribute("Xf", XF);
		addAttribute("Zr", ZR);
		addAttribute("C_N", C_N);
		addAttribute("Fe", FE);
		addAttribute("NbTa", NB_TA);
		addAttribute("Y_", Y);
		addAttribute("AcFormula1", AC_FORMULA_1);
		addAttribute("AcFormula1Valore", AC_FORMULA_1_VALORE);
		addAttribute("AcFormula2", AC_FORMULA_2);
		addAttribute("AcFormula2Valore", AC_FORMULA_2_VALORE);
		addAttribute("AcFormula3", AC_FORMULA_3);
		addAttribute("AcFormula3Valore", AC_FORMULA_3_VALORE);
		addAttribute("AcFormula4", AC_FORMULA_4);
		addAttribute("AcFormula4Valore", AC_FORMULA_4_VALORE);
		addAttribute("AcFormula5", AC_FORMULA_5);
		addAttribute("AcFormula5Valore", AC_FORMULA_5_VALORE);
		addAttribute("Elaborato", ELABORATO);

		setKeys(ENTRATA + ", " + TIMESTAMP_AGG);
	}

	private void init() throws SQLException {
		configure();
	}
}
