package it.monchieri.thip.dtsx;

import com.thera.thermfw.batch.BatchRunnable;
import com.thera.thermfw.security.Authorizable;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 20/12/2024
 * <br><br>
 * <b>71XXX	DSSOF3	20/12/2024</b>
 * <p></p>
 */

public class YDtsxImportCicliCollaudo extends BatchRunnable implements Authorizable {

	@Override
	protected boolean run() {
		return false;
	}

	@Override
	protected String getClassAdCollectionName() {
		return "YDtsxImportCicliCollaudo";
	}

}
