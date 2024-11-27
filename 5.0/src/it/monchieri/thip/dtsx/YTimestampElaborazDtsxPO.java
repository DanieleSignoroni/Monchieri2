
package it.monchieri.thip.dtsx;

import java.sql.SQLException;
import java.util.Vector;

import com.thera.thermfw.common.BaseComponentsCollection;
import com.thera.thermfw.common.BusinessObject;
import com.thera.thermfw.common.Deletable;
import com.thera.thermfw.persist.CopyException;
import com.thera.thermfw.persist.Copyable;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.persist.TableManager;
import com.thera.thermfw.security.Authorizable;
import com.thera.thermfw.security.Conflictable;

import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.cs.EntitaAzienda;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 27/11/2024
 * <br><br>
 * <b>71XXX	DSSOF3	27/11/2024</b>
 * <p>Prima stesura.<br>
 *  
 * </p>
 */

public abstract class YTimestampElaborazDtsxPO extends EntitaAzienda
		implements BusinessObject, Authorizable, Deletable, Conflictable {

	private static YTimestampElaborazDtsx cInstance;

	protected String iIdFunzione;

	@SuppressWarnings("rawtypes")
	public static Vector retrieveList(String where, String orderBy, boolean optimistic)
			throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (cInstance == null)
			cInstance = (YTimestampElaborazDtsx) Factory.createObject(YTimestampElaborazDtsx.class);
		return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
	}

	public static YTimestampElaborazDtsx elementWithKey(String key, int lockType) throws SQLException {
		return (YTimestampElaborazDtsx) PersistentObject.elementWithKey(YTimestampElaborazDtsx.class, key, lockType);
	}

	public YTimestampElaborazDtsxPO() {
		setIdAzienda(Azienda.getAziendaCorrente());
	}

	public void setIdFunzione(String idFunzione) {
		this.iIdFunzione = idFunzione;
		setDirty();
		setOnDB(false);
	}

	public String getIdFunzione() {
		return iIdFunzione;
	}

	public void setIdAzienda(String idAzienda) {
		iAzienda.setKey(idAzienda);
		setDirty();
		setOnDB(false);
	}

	public String getIdAzienda() {
		String key = iAzienda.getKey();
		return key;
	}

	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
	}

	@SuppressWarnings("rawtypes")
	public Vector checkAll(BaseComponentsCollection components) {
		Vector errors = new Vector();
		components.runAllChecks(errors);
		return errors;
	}

	public void setKey(String key) {
		setIdAzienda(KeyHelper.getTokenObjectKey(key, 1));
		setIdFunzione(KeyHelper.getTokenObjectKey(key, 2));
	}

	public String getKey() {
		String idAzienda = getIdAzienda();
		String idFunzione = getIdFunzione();
		Object[] keyParts = { idAzienda, idFunzione };
		return KeyHelper.buildObjectKey(keyParts);
	}

	public boolean isDeletable() {
		return checkDelete() == null;
	}

	public String toString() {
		return getClass().getName() + " [" + KeyHelper.formatKeyString(getKey()) + "]";
	}

	protected TableManager getTableManager() throws SQLException {
		return YTimestampElaborazDtsxTM.getInstance();
	}

}
