package it.mame.thip.qualita.controllo;

import com.thera.thermfw.persist.*;
import java.sql.*;
import java.util.*;
import com.thera.thermfw.common.*;
import com.thera.thermfw.security.*;
import it.thera.thip.cs.EntitaDescrAzienda;

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

public abstract class CodificaFaseCollaudoPO extends EntitaDescrAzienda implements BusinessObject, Authorizable, Deletable, ConflictableWithKey {

	private static CodificaFaseCollaudo cInstance;

	protected Short iCodice;

	@SuppressWarnings("rawtypes")
	public static Vector retrieveList(String where, String orderBy, boolean optimistic) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		if (cInstance == null)
			cInstance = (CodificaFaseCollaudo)Factory.createObject(CodificaFaseCollaudo.class);
		return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
	}


	public static CodificaFaseCollaudo elementWithKey(String key, int lockType) throws SQLException {
		return (CodificaFaseCollaudo) PersistentObject.elementWithKey(CodificaFaseCollaudo.class, key, lockType);
	}

	public CodificaFaseCollaudoPO(){
		super(3);
	}

	public void setCodice(Short codice){
		this.iCodice = codice;
		iDescrizione.getHandler().setFatherKeyChanged();
		setDirty();
		setOnDB(false);
	}

	public Short getCodice(){
		return iCodice;
	}

	@SuppressWarnings("rawtypes")
	public Vector checkAll(BaseComponentsCollection components){
		Vector errors = new Vector();
		components.runAllChecks(errors);
		return errors;
	}

	public void setKey(String key){
		setIdAzienda(KeyHelper.getTokenObjectKey(key, 1));
		String obj = KeyHelper.getTokenObjectKey(key, 2);
		setCodice(KeyHelper.stringToShortObj(obj));
	}

	public String getKey(){
		String IdAzienda = getIdAzienda();
		Short Codice = getCodice();
		Object[] keyParts = {IdAzienda, Codice};
		return KeyHelper.buildObjectKey(keyParts);
	}

	public boolean isDeletable() {
		return checkDelete() == null;
	}

	protected TableManager getTableManager() throws SQLException{
		return CodificaFaseCollaudoTM.getInstance();
	}


	public boolean initializeOwnedObjects(boolean ret) {
		return super.initializeOwnedObjects(ret);
	}

	public int saveOwnedObjects(int rc) throws SQLException {
		return super.saveOwnedObjects(rc);
	}

	public int deleteOwnedObjects() throws SQLException {
		return super.deleteOwnedObjects();
	}
}
