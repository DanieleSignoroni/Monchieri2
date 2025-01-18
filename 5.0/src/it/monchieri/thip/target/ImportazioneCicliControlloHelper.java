package it.monchieri.thip.target;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Database;
import com.thera.thermfw.persist.Factory;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 17/01/2025
 * <br><br>
 * <b>71XXX    DSSOF3    17/01/2025</b>
 * <p></p>
 */

public class ImportazioneCicliControlloHelper {

	static ImportazioneCicliControlloHelper instance = null;

	public ImportazioneCicliControlloHelper() { }

	public static ImportazioneCicliControlloHelper getInstance() {
		if (instance == null)
			instance = Factory.newObject(ImportazioneCicliControlloHelper.class);

		return instance;
	}

	public List<OrdEsecAtvSogCollaudo> leggiCicliControllo(String stmt){
		List<OrdEsecAtvSogCollaudo> collaudi = new ArrayList<OrdEsecAtvSogCollaudo>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = ConnectionManager.getCurrentConnection().prepareStatement(stmt);
			rs = ps.executeQuery();
			while (rs.next()){
				OrdEsecAtvSogCollaudo collaudo = (OrdEsecAtvSogCollaudo)OrdEsecAtvSogCollaudoTM.getInstance().buildPersistentObject(rs,false);
				collaudi.add(collaudo);
			}
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if (ps != null) {
					ps.close();
				}
			}catch (Exception e) {
				e.printStackTrace(Trace.excStream);	
			}
		}
		return collaudi;
	}

	public void leggiNoteCicloControllo(String stmt, OrdEsecAtvSogCollaudo collaudo) {
		List<NoteAttivitaSogCollaudo> note = null;
		if(collaudo.getNoteTargetAttivitaCollaudo() == null) {
			note = new ArrayList<NoteAttivitaSogCollaudo>();
		}else {
			note = collaudo.getNoteTargetAttivitaCollaudo();
		}
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			stmt += " AND oea.R_ATTIVITA = ? AND oea.ORD_CLI_RIGA = ? ";
			ps = ConnectionManager.getCurrentConnection().prepareStatement(stmt);
			Database db = ConnectionManager.getCurrentDatabase();
			db.setString(ps, 1, collaudo.getAttivita());
			db.setString(ps, 2, collaudo.getOrdCliRiga());
			rs = ps.executeQuery();
			while (rs.next()){
				NoteAttivitaSogCollaudo nota = (NoteAttivitaSogCollaudo)NoteAttivitaSogCollaudoTM.getInstance().buildPersistentObject(rs,false);
				nota.setDescription(rs.getString(NoteAttivitaSogCollaudoTM.DESCRIPTION));
				nota.setNlsCommentText(rs.getString(NoteAttivitaSogCollaudoTM.NLS_COMMENT_TEXT));
				nota.setReserved(rs.getBoolean(NoteAttivitaSogCollaudoTM.RESERVED));
				nota.setTextType(rs.getString(NoteAttivitaSogCollaudoTM.TEXT_TYPE).charAt(0));
				nota.setIdCommentUse(rs.getString(NoteAttivitaSogCollaudoTM.R_COMMENT_USE));
				nota.setCommentType1(rs.getInt(NoteAttivitaSogCollaudoTM.COMM_TYPE1_ID));
				nota.setCommentType2(rs.getInt(NoteAttivitaSogCollaudoTM.COMM_TYPE2_ID));
				note.add(nota);
			}
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if (ps != null) {
					ps.close();
				}
			}catch (Exception e) {
				e.printStackTrace(Trace.excStream);	
			}
		}
		collaudo.setNoteTargetAttivitaCollaudo(note);
	}
}
