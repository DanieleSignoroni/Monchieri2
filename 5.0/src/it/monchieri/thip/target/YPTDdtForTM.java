package it.monchieri.thip.target;

import java.sql.SQLException;
import com.thera.thermfw.base.SystemParam;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.TableManager;

/**
 * <h1>Softre Solutions</h1>
 * Table manager for YPT_DDT_FOR
 * 
 * YYYYMMDD Your Name - Creation
 */

public class YPTDdtForTM extends TableManager {

	public static final String NUM_DDT_INT = "NUM_DDT_INT";
	public static final String ANNO_DOC = "ANNO_DOC";
	public static final String COD_CF = "COD_CF";
	public static final String DATA_DOC = "DATA_DOC";
	public static final String CODICE_CAUSALE = "CODICE_CAUSALE";
	public static final String COD_DEP = "COD_DEP";
	public static final String COD_DEP_2 = "COD_DEP_2";
	public static final String COD_ART = "COD_ART";
	public static final String DES_RIGA = "DES_RIGA";
	public static final String NUM_RIGA = "NUM_RIGA";
	public static final String UM_ACQ = "UM_ACQ";
	public static final String QTA_CONSEGNATA = "QTA_CONSEGNATA";
	public static final String PREZZO = "PREZZO";
	public static final String LOTTO = "LOTTO";
	public static final String NUM_DDT_FORNITORE = "NUM_DDT_FORNITORE";
	public static final String DATA_DOC_FORNITORE = "DATA_DOC_FORNITORE";
	public static final String LINGOTTO = "LINGOTTO";
	public static final String RIFERIMENTO_ANNO_ORD_FOR = "RIFERIMENTO_ANNO_ORD_FOR";
	public static final String RIFERIMENTO_NUM_ORD_FOR = "RIFERIMENTO_NUM_ORD_FOR";
	public static final String RIFERIMENTO_RIGA_ORD_FOR = "RIFERIMENTO_RIGA_ORD_FOR";
	public static final String TIMESTAMP_AGG = "TIMESTAMP_AGG";
	public static final String ELABORATO = "ELABORATO";

	public static final String TABLE_NAME = SystemParam.getSchema("dbo") + "YPT_DDT_FOR";
	private static final String CLASS_NAME = YPTDdtFor.class.getName();

	private static TableManager cInstance;

	public synchronized static TableManager getInstance() throws SQLException {
		if (cInstance == null) {
			cInstance = (TableManager) Factory.createObject(YPTDdtForTM.class);
		}
		return cInstance;
	}

	public YPTDdtForTM() throws SQLException {
		super();
	}

	protected void initialize() throws SQLException {
		setTableName(TABLE_NAME);
		setObjClassName(CLASS_NAME);
		init();
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();

		addAttribute("NumDdtInt", NUM_DDT_INT);
		addAttribute("AnnoDoc", ANNO_DOC);
		addAttribute("CodCf", COD_CF);
		addAttribute("DataDoc", DATA_DOC);
		addAttribute("CodiceCausale", CODICE_CAUSALE);
		addAttribute("CodDep", COD_DEP);
		addAttribute("CodDep2", COD_DEP_2);
		addAttribute("CodArt", COD_ART);
		addAttribute("DesRiga", DES_RIGA);
		addAttribute("NumRiga", NUM_RIGA);
		addAttribute("UmAcq", UM_ACQ);
		addAttribute("QtaConsegnata", QTA_CONSEGNATA);
		addAttribute("Prezzo", PREZZO);
		addAttribute("Lotto", LOTTO);
		addAttribute("NumDdtFornitore", NUM_DDT_FORNITORE);
		addAttribute("DataDocFornitore", DATA_DOC_FORNITORE);
		addAttribute("Lingotto", LINGOTTO);
		addAttribute("RiferimentoAnnoOrdFor", RIFERIMENTO_ANNO_ORD_FOR);
		addAttribute("RiferimentoNumOrdFor", RIFERIMENTO_NUM_ORD_FOR);
		addAttribute("RiferimentoRigaOrdFor", RIFERIMENTO_RIGA_ORD_FOR);
		addAttribute("TimestampAgg", TIMESTAMP_AGG);
		addAttribute("Elaborato", ELABORATO);

		setKeys(NUM_DDT_INT + ", " + ANNO_DOC + ", " + NUM_RIGA + ", " + TIMESTAMP_AGG);
	}

	private void init() throws SQLException {
		configure();
	}
}
