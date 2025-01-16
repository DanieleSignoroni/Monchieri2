package it.monchieri.thip.target;

import java.sql.SQLException;
import com.thera.thermfw.base.SystemParam;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.TableManager;

/**
 * <h1>Softre Solutions</h1>
 * Table manager for YPT_EXP_ORD_ESEC
 *
 * YYYYMMDD Your Name - Creation
 */
public class YPTExpOrdEsecTM extends TableManager {

	public static final String DOC_ID = "DOC_ID";
	public static final String ANNO_DOC = "ANNO_DOC";
	public static final String NUM_DOC = "NUM_DOC";
	public static final String SERIE_DOC = "SERIE_DOC";
	public static final String DATA_DOC = "DATA_DOC";
	public static final String COD_CF = "COD_CF";
	public static final String NOTE_INT = "NOTE_INT";
	public static final String NOTE_STAMPA = "NOTE_STAMPA";
	public static final String COD_CAUS_DOC = "COD_CAUS_DOC";
	public static final String NOTE_CONSEGNA = "NOTE_CONSEGNA";
	public static final String COD_DEP = "COD_DEP";
	public static final String COD_DEP_2 = "COD_DEP_2";
	public static final String COD_ART = "COD_ART";
	public static final String COD_VAR_MATERIALE = "COD_VAR_MATERIALE";
	public static final String COD_LOT = "COD_LOT";
	public static final String DES_PROD = "DES_PROD";
	public static final String QUANT_DA_PROD = "QUANT_DA_PROD";
	public static final String RIF_RIGA_ORD_CLI_4B = "RIF_RIGA_ORD_CLI_4B";
	public static final String UTENTE_INS = "UTENTE_INS";
	public static final String DATA_INS = "DATA_INS";
	public static final String UTENTE_MOD = "UTENTE_MOD";
	public static final String DATA_MOD = "DATA_MOD";
	public static final String PESGREZ = "PESGREZ";
	public static final String PESMULT = "PESMULT";
	public static final String CHIODAIA = "CHIODAIA";
	public static final String MANDRINO = "MANDRINO";
	public static final String ATTREZZ1 = "ATTREZZ1";
	public static final String ATTREZZ2 = "ATTREZZ2";
	public static final String PROVNUM = "PROVNUM";
	public static final String DATA_SPED = "DATA_SPED";
	public static final String DATA_TT = "DATA_TT";
	public static final String IDENTCLI = "IDENTCLI";
	public static final String NUCLEARE = "NUCLEARE";
	public static final String CODCLI = "CODCLI";
	public static final String TIPO_OPERAZIONE = "TIPO_OPERAZIONE";
	public static final String ESEGUITO = "ESEGUITO";
	public static final String NUM_RIGA = "NUM_RIGA";
	public static final String DOC_RIGA_ID = "DOC_RIGA_ID";
	public static final String COD_ART_COMP = "COD_ART_COMP";
	public static final String QUANT_SCARICATA = "QUANT_SCARICATA";
	public static final String QUANT_MATEROZZA = "QUANT_MATEROZZA";
	public static final String ENTRATA = "ENTRATA";
	public static final String DATA_MOVIMENTO = "DATA_MOVIMENTO";

	public static final String TABLE_NAME = SystemParam.getSchema("MAME") + "YPT_EXP_ORD_ESEC";
	private static final String CLASS_NAME = YPTExpOrdEsec.class.getName();

	private static TableManager cInstance;

	public synchronized static TableManager getInstance() throws SQLException {
		if (cInstance == null) {
			cInstance = (TableManager) Factory.createObject(YPTExpOrdEsecTM.class);
		}
		return cInstance;
	}

	public YPTExpOrdEsecTM() throws SQLException {
		super();
	}

	protected void initialize() throws SQLException {
		setTableName(TABLE_NAME);
		setObjClassName(CLASS_NAME);
		init();
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();

		addAttribute("DocId", DOC_ID);
		addAttribute("AnnoDoc", ANNO_DOC);
		addAttribute("NumDoc", NUM_DOC);
		addAttribute("SerieDoc", SERIE_DOC);
		addAttribute("DataDoc", DATA_DOC);
		addAttribute("CodCf", COD_CF);
		addAttribute("NoteInt", NOTE_INT);
		addAttribute("NoteStampa", NOTE_STAMPA);
		addAttribute("CodCausDoc", COD_CAUS_DOC);
		addAttribute("NoteConsegna", NOTE_CONSEGNA);
		addAttribute("CodDep", COD_DEP);
		//addAttribute("CodDep2", COD_DEP_2);
		addAttribute("CodArt", COD_ART);
		addAttribute("CodVarMateriale", COD_VAR_MATERIALE);
		addAttribute("CodLot", COD_LOT);
		addAttribute("DesProd", DES_PROD);
		addAttribute("QuantDaProd", QUANT_DA_PROD);
		addAttribute("RifRigaOrdCli4b", RIF_RIGA_ORD_CLI_4B);
		addAttribute("UtenteIns", UTENTE_INS);
		addAttribute("DataIns", DATA_INS);
		addAttribute("UtenteMod", UTENTE_MOD);
		addAttribute("DataMod", DATA_MOD);
		addAttribute("Pesgrez", PESGREZ);
		addAttribute("Pesmult", PESMULT);
		addAttribute("Chiodaia", CHIODAIA);
		addAttribute("Mandrino", MANDRINO);
		addAttribute("Attrezz1", ATTREZZ1);
		addAttribute("Attrezz2", ATTREZZ2);
		addAttribute("Provnum", PROVNUM);
		addAttribute("DataSped", DATA_SPED);
		addAttribute("DataTt", DATA_TT);
		addAttribute("Identcli", IDENTCLI);
		addAttribute("Nucleare", NUCLEARE);
		addAttribute("Codcli", CODCLI);
		addAttribute("TipoOperazione", TIPO_OPERAZIONE);
		addAttribute("Eseguito", ESEGUITO);
		addAttribute("NumRiga", NUM_RIGA);
		addAttribute("DocRigaId", DOC_RIGA_ID);
		addAttribute("CodArtComp", COD_ART_COMP);
		addAttribute("QuantScaricata", QUANT_SCARICATA);
		addAttribute("QuantMaterozza", QUANT_MATEROZZA);
		addAttribute("Entrata", ENTRATA);
		addAttribute("DataMovimento", DATA_MOVIMENTO);

		setKeys(DOC_ID + ", " + DATA_MOD);
		
	}

	private void init() throws SQLException {
		configure();
	}
}
