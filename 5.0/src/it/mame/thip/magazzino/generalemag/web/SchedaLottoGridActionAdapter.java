package it.mame.thip.magazzino.generalemag.web;

import java.util.*;

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

public class SchedaLottoGridActionAdapter extends GridActionAdapter {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void modifyMenuBar(WebMenuBar menuBar) {
        WebMenuAbstract listMenu = menuBar.getMenu("ListMenu");
        WebMenuAbstract selectedMenu = menuBar.getMenu("SelectedMenu");

        ArrayList removeList = new ArrayList();
        removeList.add("New");
        listMenu.removeMenu(removeList);

        removeList.clear();
        removeList.add("NewTemplate");
        listMenu.removeMenu(removeList);

        removeList.clear();
        removeList.add("Copy");
        selectedMenu.removeMenu(removeList);

        removeList.clear();
        removeList.add("Delete");
        selectedMenu.removeMenu(removeList);
    }

    public void modifyToolBar(WebToolBar toolBar) {
        toolBar.removeButton("New");
        toolBar.removeButton("Copy");
        toolBar.removeButton("Delete");
    }
}
