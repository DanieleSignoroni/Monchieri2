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

public abstract class CodificaCaratteristicaCollaudoPO extends EntitaDescrAzienda implements BusinessObject, Authorizable, Deletable, ConflictableWithKey {

	private static CodificaCaratteristicaCollaudo cInstance;

	protected Short iCodice;

	protected String iFormula;

	public CodificaCaratteristicaCollaudoPO (){
		super(3);
	}

	@SuppressWarnings("rawtypes")
	public static Vector retrieveList (String where, String orderBy, boolean optimistic) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		if (cInstance == null) {
			cInstance = (CodificaCaratteristicaCollaudo) Factory.createObject(CodificaCaratteristicaCollaudo.class);
		}
		return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
	}

	public static CodificaCaratteristicaCollaudo elementWithKey (String key, int lockType) throws SQLException{
		return (CodificaCaratteristicaCollaudo) PersistentObject.elementWithKey(CodificaCaratteristicaCollaudo.class, key, lockType);
	}

	public void setCodice (Short codice){
		this.iCodice = codice;
		iDescrizione.getHandler().setFatherKeyChanged();
		setDirty();
		setOnDB(false);

	}

	public Short getCodice (){
		return iCodice;
	}

	public void setFormula (String f){
		iFormula = f;
		setDirty();
	}

	public String getFormula (){
		return iFormula;
	}

	@SuppressWarnings("rawtypes")
	public Vector checkAll (BaseComponentsCollection components){
		Vector errors = new Vector();
		components.runAllChecks(errors);
		return errors;
	}

	public void setKey (String key){
		setIdAzienda(KeyHelper.getTokenObjectKey(key, 1));
		String obj = KeyHelper.getTokenObjectKey(key, 2);
		setCodice(KeyHelper.stringToShortObj(obj));
	}

	public String getKey (){
		String IdAzienda = getIdAzienda();
		Short Codice = getCodice();
		Object[] keyParts = {IdAzienda, Codice};
		return KeyHelper.buildObjectKey(keyParts);
	}

	public boolean isDeletable (){
		return checkDelete() == null;
	}

	protected TableManager getTableManager () throws SQLException{
		return CodificaCaratteristicaCollaudoTM.getInstance();
	}

	public boolean initializeOwnedObjects (boolean ret){
		return iDescrizione.getHandler().initialize(ret);
	}

	public int saveOwnedObjects (int rc) throws SQLException{
		return super.saveOwnedObjects(rc);
	}

	public int deleteOwnedObjects () throws SQLException{
		return super.deleteOwnedObjects();
	}
}
