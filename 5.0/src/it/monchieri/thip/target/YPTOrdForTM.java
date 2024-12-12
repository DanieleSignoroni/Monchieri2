package it.monchieri.thip.target;

import java.sql.SQLException;
import com.thera.thermfw.base.SystemParam;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.TableManager;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * Table manager for YPT_ORD_FOR
 * <br><br>
 * <b>YYYYMMDD    Your Name    Creation</b>
 */

public class YPTOrdForTM extends TableManager {
	
	public static final String NUM_DOC = "NUM_DOC";
	public static final String ANNO_DOC = "ANNO_DOC";
	public static final String COD_CF = "COD_CF";
	public static final String DATA_DOC = "DATA_DOC";
	public static final String COD_ART = "COD_ART";
	public static final String DES_RIGA = "DES_RIGA";
	public static final String NUM_RIGA = "NUM_RIGA";
	public static final String UM_BASE = "UM_BASE";
	public static final String QUANT_UM_BASE = "QUANT_UM_BASE";
	public static final String PREZZO = "PREZZO";
	public static final String NOTE_INT = "NOTE_INT";
	public static final String DATA_CONS_RIGA = "DATA_CONS_RIGA";
	public static final String ID = "ID";
	public static final String TIMESTAMP_AGG = "TIMESTAMP_AGG";
	public static final String ELABORATO = "ELABORATO";
	
	public static final String TABLE_NAME = SystemParam.getSchema("dbo") + "YPT_ORD_FOR";
	private static final String CLASS_NAME = YPTOrdFor.class.getName();
	private static TableManager cInstance;
	
	public synchronized static TableManager getInstance() throws SQLException {
		if (cInstance == null) {
			cInstance = (TableManager) Factory.createObject(YPTOrdForTM.class);
		}
		return cInstance;
	}
	
	public YPTOrdForTM() throws SQLException {
		super();
	}
	
	protected void initialize() throws SQLException {
		setTableName(TABLE_NAME);
		setObjClassName(CLASS_NAME);
		init();
	}
	
	protected void initializeRelation() throws SQLException {
		super.initializeRelation();
		
		addAttribute("NumDoc", NUM_DOC);
		addAttribute("AnnoDoc", ANNO_DOC);
		addAttribute("CodCf", COD_CF);
		addAttribute("DataDoc", DATA_DOC);
		addAttribute("CodArt", COD_ART);
		addAttribute("DesRiga", DES_RIGA);
		addAttribute("NumRiga", NUM_RIGA);
		addAttribute("UmBase", UM_BASE);
		addAttribute("QuantUmBase", QUANT_UM_BASE);
		addAttribute("Prezzo", PREZZO);
		addAttribute("NoteInt", NOTE_INT);
		addAttribute("DataConsRiga", DATA_CONS_RIGA);
		addAttribute("Id", ID);
		addAttribute("TimestampAgg", TIMESTAMP_AGG);
		addAttribute("Elaborato", ELABORATO);
		
		setKeys(NUM_DOC + ", " + ANNO_DOC + ", " + NUM_RIGA + ", " + TIMESTAMP_AGG);
	}
	
	private void init() throws SQLException {
		configure();
	}
}
