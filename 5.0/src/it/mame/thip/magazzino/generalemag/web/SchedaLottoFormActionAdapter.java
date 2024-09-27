package it.mame.thip.magazzino.generalemag.web;

import java.io.*;
import java.util.*;
import javax.servlet.*;

import com.thera.thermfw.ad.*;
import com.thera.thermfw.web.*;
import com.thera.thermfw.web.servlet.*;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 27/09/2024
 * <br><br>
 * <b>71XXX	DSSOF3	27/09/2024</b>
 * <p>Prima stesura.<br>
 *  
 * </p>
 */

public class SchedaLottoFormActionAdapter extends FormActionAdapter {

	private static final long serialVersionUID = 1L;

	protected void otherActions (ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException
    {
        super.otherActions(cadc, se);
    }

    public void modifyToolBar (WebToolBar toolBar)
    {
        toolBar.removeButton("SaveAndNew");
        //--- MAURO 90013 (24/04/2009) Begin
        //toolBar.removeButton("Save");
        //--- MAURO 90013 (24/04/2009) End
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void modifyMenuBar (WebMenuBar menuBar)
    {
        WebMenuAbstract menu = menuBar.getMenu("ObjectMenu");
        ArrayList menuItemToBeRemoved = new ArrayList();

        menuItemToBeRemoved.add("SaveAndNew");
        menu.removeMenu(menuItemToBeRemoved);
        menuItemToBeRemoved.clear();

        //--- MAURO 90013 (24/04/2009) Begin
        //menuItemToBeRemoved.add("Save");
        //menu.removeMenu(menuItemToBeRemoved);
        //menuItemToBeRemoved.clear();
        //--- MAURO 90013 (24/04/2009) End
    }

}
