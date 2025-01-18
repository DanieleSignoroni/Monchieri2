package it.monchieri.thip.target;

import java.sql.SQLException;

import com.thera.thermfw.base.SystemParam;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.TableManager;

public class NoteAttivitaSogCollaudoTM extends TableManager {

	public static final String LANGUAGE        = "LANGUAGE";
	public static final String DESCRIPTION       = "DESCRIPTION";
	public static final String NLS_COMMENT_TEXT      = "NLS_COMMENT_TEXT";
	public static final String RESERVED      = "RESERVED";
	public static final String TEXT_TYPE    = "TEXT_TYPE";
	public static final String R_COMMENT_USE          = "R_COMMENT_USE";
	public static final String COMM_TYPE1_ID          = "COMM_TYPE1_ID";
	public static final String COMM_TYPE2_ID           = "COMM_TYPE2_ID";
	public static final String ID_AZIENDA = "ID_AZIENDA";
	public static final String  NUMERO_ORD_FMT = "NUMERO_ORD_FMT";
	public static final String ORD_CLI_RIGA = "ORD_CLI_RIGA";
	public static final String R_ATTIVITA = "R_ATTIVITA";
	public static final String NOME_TABELLA = "NOME_TABELLA";
	public static final String ID_SEQUENZA_FASE = "ID_SEQUENZA_FASE";
	public static final String SEQ_VISUALIZZ = "SEQ_VISUALIZZ";

	public static final String TABLE_NAME = SystemParam.getSchema("THIPPERS") + "ORD_ESEC_ATV_SOG_COLLAUDO_ORP_20";
	private static final String CLASS_NAME = NoteAttivitaSogCollaudo.class.getName();

	private static TableManager cInstance;

	public synchronized static TableManager getInstance() throws SQLException {
		if (cInstance == null) {
			cInstance = (TableManager) Factory.createObject(NoteAttivitaSogCollaudoTM.class);
		}
		return cInstance;
	}

	public NoteAttivitaSogCollaudoTM() throws SQLException {
		super();
	}

	@Override
	protected void initialize() throws SQLException {
		setTableName(TABLE_NAME);
		setObjClassName(CLASS_NAME);
		init();
	}

	@Override
	protected void initializeRelation() throws SQLException {
		super.initializeRelation();

		addAttribute("IdAzienda",         ID_AZIENDA);
		addAttribute("OrdCliRiga",        ORD_CLI_RIGA);
		addAttribute("NomeTabella",       NOME_TABELLA);
		addAttribute("IdSequenzaFase",    ID_SEQUENZA_FASE);
		addAttribute("SeqVisualizz",      SEQ_VISUALIZZ);
		//addAttribute("Language",LANGUAGE);
		//addAttribute("Description",DESCRIPTION);
		//addAttribute("NlsCommentText",NLS_COMMENT_TEXT);
		//addAttribute("Reserved",RESERVED);
		//addAttribute("TextType",TEXT_TYPE);
		//addAttribute("IdCommentUse",R_COMMENT_USE);
		//addAttribute("CommentType1",COMM_TYPE1_ID);
		//addAttribute("CommentType2",COMM_TYPE2_ID);
		addAttribute("NumeroOrdFmt",NUMERO_ORD_FMT);
		addAttribute("IdAttivita",R_ATTIVITA);

	}

	private void init() throws SQLException {
		configure();
	}

}
